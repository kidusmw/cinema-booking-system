package application.service;

import domain.port.BookingRepository;
import domain.port.PaymentRepository;
import domain.port.SeatRepository;
import domain.service.BookingService;
import domain.service.PaymentService;
import infrastructure.persistence.ConnectionProvider;
import infrastructure.persistence.JdbcBookingRepository;
import infrastructure.persistence.JdbcPaymentRepository;
import infrastructure.persistence.JdbcSeatRepository;
import infrastructure.persistence.TransactionalConnectionProvider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookingFacade {
    private final ConnectionProvider connectionProvider;

    public BookingFacade(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void bookAndPay(
            Long userId, Long showId, List<Long> seatIds, double amount, String paymentMethod) {
        TransactionalConnectionProvider txProvider =
                new TransactionalConnectionProvider(connectionProvider);
        Connection conn = null;
        try {
            conn = txProvider.getConnection();
            conn.setAutoCommit(false);

            SeatRepository txSeatRepo = new JdbcSeatRepository(txProvider);
            BookingRepository txBookingRepo = new JdbcBookingRepository(txProvider);
            PaymentRepository txPaymentRepo = new JdbcPaymentRepository(txProvider);

            BookingService txBookingService = new BookingService(txBookingRepo, txSeatRepo);
            PaymentService txPaymentService = new PaymentService(txPaymentRepo);

            domain.model.Booking booking =
                    txBookingService.createBooking(userId, showId, seatIds, amount);
            txPaymentService.processPayment(booking.getBookingId(), amount, paymentMethod);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw new RuntimeException("Booking+Payment transaction failed", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
