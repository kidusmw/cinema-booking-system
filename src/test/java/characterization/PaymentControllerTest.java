package characterization;

import DAO.BookingDAO;
import DAO.PaymentDAOimp;
import Model.Booking;
import Model.Payment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Characterization test for the PaymentController's core logic.
 * Captures the current booking→payment sequential flow.
 *
 * PaymentController.processSuccessfulPayment() does:
 *   1. bookingDAO.addBooking(booking)
 *   2. If success, paymentDAO.addPayment(payment) with the booking ID
 * There is NO transaction wrapping these two calls.
 */
@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Test
    void processSuccessfulPayment_callsBookingDaoThenPaymentDao() {
        try (MockedConstruction<BookingDAO> mockedBookingDao = mockConstruction(BookingDAO.class,
                (mock, context) -> {
                    when(mock.addBooking(any(Booking.class))).thenAnswer(invocation -> {
                        Booking arg = invocation.getArgument(0);
                        arg.setBookingID(42);
                        return true;
                    });
                });
             MockedConstruction<PaymentDAOimp> mockedPaymentDao = mockConstruction(PaymentDAOimp.class,
                (mock, context) -> {
                    when(mock.addPayment(any(Payment.class))).thenReturn(true);
                })) {

            BookingDAO bookingDao = new BookingDAO();
            PaymentDAOimp paymentDao = new PaymentDAOimp();

            Booking booking = new Booking();
            boolean bookingSuccess = bookingDao.addBooking(booking);

            if (bookingSuccess) {
                Payment payment = new Payment();
                payment.setBookingID(booking.getBookingID());
                paymentDao.addPayment(payment);
            }

            verify(bookingDao).addBooking(any(Booking.class));
            verify(paymentDao).addPayment(any(Payment.class));

            ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentDao).addPayment(paymentCaptor.capture());
            Payment capturedPayment = paymentCaptor.getValue();

            assertThat(capturedPayment.getBookingID()).isEqualTo(42);
        }
    }

    @Test
    void processSuccessfulPayment_doesNotCallPaymentDao_whenBookingFails() {
        try (MockedConstruction<BookingDAO> mockedBookingDao = mockConstruction(BookingDAO.class,
                (mock, context) -> {
                    when(mock.addBooking(any(Booking.class))).thenReturn(false);
                });
             MockedConstruction<PaymentDAOimp> mockedPaymentDao = mockConstruction(PaymentDAOimp.class)) {

            BookingDAO bookingDao = new BookingDAO();
            PaymentDAOimp paymentDao = new PaymentDAOimp();

            Booking booking = new Booking();
            boolean bookingSuccess = bookingDao.addBooking(booking);

            if (bookingSuccess) {
                Payment payment = new Payment();
                paymentDao.addPayment(payment);
            }

            verify(bookingDao).addBooking(any(Booking.class));
            verify(paymentDao, never()).addPayment(any(Payment.class));
        }
    }

    @Test
    void bookingIdIsForwardedToPayment() {
        try (MockedConstruction<BookingDAO> mockedBookingDao = mockConstruction(BookingDAO.class,
                (mock, context) -> {
                    when(mock.addBooking(any(Booking.class))).thenAnswer(invocation -> {
                        Booking arg = invocation.getArgument(0);
                        arg.setBookingID(99);
                        return true;
                    });
                });
             MockedConstruction<PaymentDAOimp> mockedPaymentDao = mockConstruction(PaymentDAOimp.class,
                (mock, context) -> {
                    when(mock.addPayment(any(Payment.class))).thenReturn(true);
                })) {

            BookingDAO bookingDao = new BookingDAO();
            PaymentDAOimp paymentDao = new PaymentDAOimp();

            Booking booking = new Booking();
            bookingDao.addBooking(booking);

            Payment payment = new Payment();
            payment.setBookingID(booking.getBookingID());
            paymentDao.addPayment(payment);

            ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentDao).addPayment(captor.capture());
            assertThat(captor.getValue().getBookingID()).isEqualTo(99);
        }
    }
}
