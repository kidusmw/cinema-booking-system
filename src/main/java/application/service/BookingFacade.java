package application.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookingFacade {

    private static final Logger log = LoggerFactory.getLogger(BookingFacade.class);
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

            JdbcSeatRepository txSeatRepo = new JdbcSeatRepository(txProvider);
            JdbcBookingRepository txBookingRepo = new JdbcBookingRepository(txProvider);
            JdbcPaymentRepository txPaymentRepo = new JdbcPaymentRepository(txProvider);

            BookingService txBookingService = new BookingService(txBookingRepo, txSeatRepo);
            PaymentService txPaymentService = new PaymentService(txPaymentRepo);

            domain.model.Booking booking =
                    txBookingService.createBooking(userId, showId, seatIds, amount);
            txPaymentService.processPayment(booking.getBookingId(), amount, paymentMethod);

            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.rollback();
                    }
                } catch (SQLException rollbackEx) {
                    log.warn("Rollback failed after transaction error", rollbackEx);
                }
            }
            throw new RuntimeException("Booking+Payment transaction failed", e);
        } finally {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException closeEx) {
                    log.warn("Failed to close connection after transaction", closeEx);
                }
            }
        }
    }
}
