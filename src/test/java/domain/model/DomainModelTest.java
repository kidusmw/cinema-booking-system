package domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class DomainModelTest {

    @Test
    void userIsAdminWhenRoleIsAdmin() {
        User user = new User(1L, "Test", "User", "admin1", "pass", "admin", "123", "a@b.com");
        assertTrue(user.isAdmin());
        assertFalse(user.isCustomer());
    }

    @Test
    void userIsCustomerWhenRoleIsCustomer() {
        User user = new User(2L, "Test", "User", "cust1", "pass", "customer", "123", "a@b.com");
        assertTrue(user.isCustomer());
        assertFalse(user.isAdmin());
    }

    @Test
    void seatBookChangesStatus() {
        Seat seat = new Seat(1L, 1L, "A1", "regular", "available");
        assertTrue(seat.isAvailable());
        seat.book();
        assertFalse(seat.isAvailable());
        assertEquals("booked", seat.getStatus());
    }

    @Test
    void seatBookThrowsIfNotAvailable() {
        Seat seat = new Seat(1L, 1L, "A1", "regular", "booked");
        assertThrows(IllegalStateException.class, seat::book);
    }

    @Test
    void seatReleaseMakesAvailable() {
        Seat seat = new Seat(1L, 1L, "A1", "regular", "booked");
        seat.release();
        assertTrue(seat.isAvailable());
    }

    @Test
    void showtimeIsPastWhenDateBeforeToday() {
        Showtime show =
                new Showtime(1L, 1L, 1L, LocalDate.of(2020, 1, 1), LocalTime.of(10, 0), 10.0);
        assertTrue(show.isPast());
    }

    @Test
    void showtimeIsNotPastWhenDateInFuture() {
        Showtime show =
                new Showtime(1L, 1L, 1L, LocalDate.now().plusDays(1), LocalTime.of(10, 0), 10.0);
        assertFalse(show.isPast());
    }

    @Test
    void showtimeHasStartedWhenTimePassedToday() {
        Showtime show =
                new Showtime(1L, 1L, 1L, LocalDate.now(), LocalTime.now().minusHours(1), 10.0);
        assertTrue(show.hasStarted());
    }

    @Test
    void showtimeHasNotStartedWhenTimeStillFuture() {
        Showtime show =
                new Showtime(1L, 1L, 1L, LocalDate.now(), LocalTime.now().plusHours(1), 10.0);
        assertFalse(show.hasStarted());
    }

    @Test
    void bookingConfirmChangesStatus() {
        Booking booking = new Booking(1L, 1L, 1L, LocalDateTime.now(), 50.0, "pending");
        booking.confirm();
        assertEquals("confirmed", booking.getBookingStatus());
    }

    @Test
    void bookingConfirmThrowsIfNotPending() {
        Booking booking = new Booking(1L, 1L, 1L, LocalDateTime.now(), 50.0, "confirmed");
        assertThrows(IllegalStateException.class, booking::confirm);
    }

    @Test
    void bookingCancelChangesStatus() {
        Booking booking = new Booking(1L, 1L, 1L, LocalDateTime.now(), 50.0, "confirmed");
        booking.cancel();
        assertEquals("cancelled", booking.getBookingStatus());
    }

    @Test
    void bookingCancelThrowsIfAlreadyCancelled() {
        Booking booking = new Booking(1L, 1L, 1L, LocalDateTime.now(), 50.0, "cancelled");
        assertThrows(IllegalStateException.class, booking::cancel);
    }

    @Test
    void bookingTotalSumsSeatPrices() {
        Booking booking = new Booking(1L, 1L, 1L, LocalDateTime.now(), 0, "pending");
        booking.addSeat(new BookingSeat(1L, 1L, 25.0));
        booking.addSeat(new BookingSeat(1L, 2L, 25.0));
        assertEquals(50.0, booking.total());
    }

    @Test
    void paymentMarkPaidChangesStatus() {
        Payment payment = new Payment(1L, 1L, 100.0, "pending", "ABC123", "card");
        payment.markPaid();
        assertEquals("paid", payment.getStatus());
    }

    @Test
    void paymentMarkPaidThrowsIfNotPending() {
        Payment payment = new Payment(1L, 1L, 100.0, "paid", "ABC123", "card");
        assertThrows(IllegalStateException.class, payment::markPaid);
    }

    @Test
    void paymentMarkFailedChangesStatus() {
        Payment payment = new Payment(1L, 1L, 100.0, "pending", "ABC123", "card");
        payment.markFailed();
        assertEquals("failed", payment.getStatus());
    }

    @Test
    void paymentVerifyOtpMatches() {
        Payment payment = new Payment(1L, 1L, 100.0, "pending", "XYZ789", "card");
        assertTrue(payment.verifyOtp("XYZ789"));
        assertFalse(payment.verifyOtp("wrong"));
    }

    @Test
    void hallIsVipWhenTypeIsVip() {
        Hall hall = new Hall(1L, "VIP Room", 50, "vip");
        assertTrue(hall.isVip());
    }

    @Test
    void hallIsNotVipWhenTypeIsRegular() {
        Hall hall = new Hall(1L, "Main Hall", 200, "regular");
        assertFalse(hall.isVip());
    }

    @Test
    void movieFormattedDuration() {
        Movie movie = new Movie(1L, "Test", "Action", 142, 8.5, "Desc", null, "EN", "/poster.jpg");
        assertEquals("2h 22m", movie.getFormattedDuration());
    }

    @Test
    void movieFormattedDurationZero() {
        Movie movie = new Movie(2L, "Test", "Action", 0, 0, "Desc", null, "EN", null);
        assertEquals("0h 0m", movie.getFormattedDuration());
    }

    @Test
    void bookingAddSeatAccumulatesTotal() {
        Booking booking = new Booking(1L, 1L, 1L, LocalDateTime.now(), 0, "pending");
        booking.addSeat(new BookingSeat(1L, 1L, 25.0));
        booking.addSeat(new BookingSeat(1L, 2L, 25.0));
        booking.addSeat(new BookingSeat(1L, 3L, 50.0));
        assertEquals(100.0, booking.total());
    }

    @Test
    void bookingTotalZeroWhenNoSeats() {
        Booking booking = new Booking(1L, 1L, 1L, LocalDateTime.now(), 0, "pending");
        assertEquals(0.0, booking.total());
    }

    @Test
    void showtimeIsNotPastWhenToday() {
        Showtime show =
                new Showtime(1L, 1L, 1L, LocalDate.now(), LocalTime.now().plusHours(2), 10.0);
        assertFalse(show.isPast());
    }
}
