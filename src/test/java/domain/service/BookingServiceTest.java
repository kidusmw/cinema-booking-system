package domain.service;

import domain.model.Booking;
import domain.model.BookingSeat;
import domain.model.Seat;
import domain.port.BookingRepository;
import domain.port.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepo;

    @Mock
    SeatRepository seatRepo;

    @InjectMocks
    BookingService bookingService;

    @Test
    void createBookingSucceedsWhenSeatsAvailable() {
        Seat seat = new Seat(1L, 1L, "A1", "regular", "available");
        when(seatRepo.findById(1L)).thenReturn(Optional.of(seat));
        Booking savedBooking = new Booking(1L, 1L, 1L, null, 50.0, "pending");
        when(bookingRepo.save(any())).thenReturn(savedBooking);

        Booking result = bookingService.createBooking(1L, 1L, List.of(1L), 50.0);

        assertNotNull(result);
        assertEquals(1L, result.getBookingId());
        verify(seatRepo).updateStatus(1L, "booked");
    }

    @Test
    void createBookingThrowsWhenSeatNotFound() {
        when(seatRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(1L, 1L, List.of(999L), 50.0));
    }

    @Test
    void createBookingThrowsWhenSeatNotAvailable() {
        Seat seat = new Seat(1L, 1L, "A1", "regular", "booked");
        when(seatRepo.findById(1L)).thenReturn(Optional.of(seat));
        assertThrows(IllegalStateException.class,
                () -> bookingService.createBooking(1L, 1L, List.of(1L), 50.0));
    }

    @Test
    void cancelBookingReleasesSeats() {
        Booking booking = new Booking(1L, 1L, 1L, null, 50.0, "confirmed");
        booking.addSeat(new BookingSeat(1L, 1L, 25.0));
        booking.addSeat(new BookingSeat(1L, 2L, 25.0));
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L);

        verify(bookingRepo).cancel(1L);
        verify(seatRepo).updateStatus(1L, "available");
        verify(seatRepo).updateStatus(2L, "available");
    }

    @Test
    void cancelBookingThrowsWhenNotFound() {
        when(bookingRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.cancelBooking(999L));
    }
}
