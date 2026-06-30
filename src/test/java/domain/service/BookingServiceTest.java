package domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import domain.model.Booking;
import domain.model.BookingSeat;
import domain.model.SeatStatus;

import domain.port.BookingRepository;
import domain.port.SeatRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock BookingRepository bookingRepo;

    @Mock SeatRepository seatRepo;

    @InjectMocks BookingService bookingService;

    @Test
    void createBookingSucceedsWhenSeatsAvailable() {
        Booking savedBooking = new Booking(1L, 1L, 1L, null, 50.0, "pending");
        when(bookingRepo.save(any())).thenReturn(savedBooking);
        when(seatRepo.claimSeat(1L)).thenReturn(1);

        Booking result = bookingService.createBooking(1L, 1L, List.of(1L), 50.0);

        assertNotNull(result);
        assertEquals(1L, result.getBookingId());
        verify(seatRepo).claimSeat(1L);
    }

    @Test
    void createBookingThrowsWhenSeatNotFound() {
        Booking savedBooking = new Booking(1L, 1L, 1L, null, 50.0, "pending");
        when(bookingRepo.save(any())).thenReturn(savedBooking);
        when(seatRepo.claimSeat(999L)).thenReturn(0);

        assertThrows(
                IllegalStateException.class,
                () -> bookingService.createBooking(1L, 1L, List.of(999L), 50.0));
    }

    @Test
    void createBookingThrowsWhenSeatNotAvailable() {
        Booking savedBooking = new Booking(1L, 1L, 1L, null, 50.0, "pending");
        when(bookingRepo.save(any())).thenReturn(savedBooking);
        when(seatRepo.claimSeat(1L)).thenReturn(0);

        assertThrows(
                IllegalStateException.class,
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
        verify(seatRepo).updateStatus(1L, SeatStatus.AVAILABLE);
        verify(seatRepo).updateStatus(2L, SeatStatus.AVAILABLE);
    }

    @Test
    void cancelBookingThrowsWhenNotFound() {
        when(bookingRepo.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bookingService.cancelBooking(999L));
    }

    @Test
    void getHistoryReturnsUserBookings() {
        Booking b1 = new Booking(1L, 1L, 1L, null, 50.0, "confirmed");
        Booking b2 = new Booking(2L, 1L, 2L, null, 30.0, "cancelled");
        when(bookingRepo.findByUserId(1L)).thenReturn(List.of(b1, b2));

        List<Booking> result = bookingService.getHistory(1L);

        assertEquals(2, result.size());
        verify(bookingRepo).findByUserId(1L);
    }

    @Test
    void createBookingThrowsWhenPriceMismatch() {
        when(bookingRepo.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(
                RuntimeException.class,
                () -> bookingService.createBooking(1L, 1L, List.of(1L), 50.0));
    }

    @Test
    void concurrentBookingOnlyOneSucceeds() throws Exception {
        Booking savedBooking = new Booking(1L, 1L, 1L, null, 50.0, "pending");
        when(bookingRepo.save(any())).thenReturn(savedBooking);

        AtomicInteger callOrder = new AtomicInteger(0);
        when(seatRepo.claimSeat(1L)).thenAnswer(inv -> callOrder.incrementAndGet() == 1 ? 1 : 0);

        CyclicBarrier barrier = new CyclicBarrier(2);
        ExecutorService es = Executors.newFixedThreadPool(2);

        try {
            List<Future<Booking>> futures =
                    es.invokeAll(
                            List.of(
                                    () -> {
                                        barrier.await();
                                        return bookingService.createBooking(
                                                1L, 1L, List.of(1L), 50.0);
                                    },
                                    () -> {
                                        barrier.await();
                                        return bookingService.createBooking(
                                                1L, 1L, List.of(1L), 50.0);
                                    }));

            long success =
                    futures.stream()
                            .filter(
                                    f -> {
                                        try {
                                            f.get();
                                            return true;
                                        } catch (Exception e) {
                                            return false;
                                        }
                                    })
                            .count();

            assertEquals(1, success);
        } finally {
            es.shutdown();
        }
    }
}
