package application.service;

import domain.model.Booking;
import domain.model.Payment;
import domain.service.BookingService;
import domain.service.PaymentService;
import infrastructure.persistence.ConnectionProvider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookingFacade {
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final ConnectionProvider connectionProvider;

    public BookingFacade(BookingService bookingService, PaymentService paymentService,
                         ConnectionProvider connectionProvider) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.connectionProvider = connectionProvider;
    }

    public Payment bookAndPay(Long userId, Long showId, List<Long> seatIds,
                              double amount, String paymentMethod) {
        Connection connection = null;
        try {
            connection = connectionProvider.getConnection();
            connection.setAutoCommit(false);

            Booking booking = bookingService.createBooking(userId, showId, seatIds, amount);
            Payment payment = paymentService.processPayment(booking.getBookingId(), amount, paymentMethod);

            connection.commit();
            return payment;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Booking and payment transaction failed", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}
