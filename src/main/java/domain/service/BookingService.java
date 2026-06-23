package domain.service;

import domain.model.Booking;
import domain.model.BookingSeat;
import domain.model.Seat;
import domain.port.BookingRepository;
import domain.port.SeatRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookingService {
    private final BookingRepository bookingRepo;
    private final SeatRepository seatRepo;

    public BookingService(BookingRepository bookingRepo, SeatRepository seatRepo) {
        this.bookingRepo = bookingRepo;
        this.seatRepo = seatRepo;
    }

    public Booking createBooking(Long userId, Long showId, List<Long> seatIds, double amount) {
        for (Long seatId : seatIds) {
            Optional<Seat> seatOpt = seatRepo.findById(seatId);
            if (seatOpt.isEmpty()) {
                throw new IllegalArgumentException("Seat not found: " + seatId);
            }
            Seat seat = seatOpt.get();
            if (!seat.isAvailable()) {
                throw new IllegalStateException("Seat " + seatId + " is not available");
            }
        }

        Booking booking = new Booking(null, userId, showId, LocalDateTime.now(), amount, "pending");
        Booking saved = bookingRepo.save(booking);

        for (Long seatId : seatIds) {
            seatRepo.updateStatus(seatId, "booked");
            saved.addSeat(new BookingSeat(saved.getBookingId(), seatId, amount / seatIds.size()));
        }

        return saved;
    }

    public void cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepo.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }
        Booking booking = bookingOpt.get();
        booking.cancel();
        bookingRepo.cancel(bookingId);

        for (BookingSeat bs : booking.getSeats()) {
            seatRepo.updateStatus(bs.getSeatId(), "available");
        }
    }

    public List<Booking> getHistory(Long userId) {
        return bookingRepo.findByUserId(userId);
    }
}
