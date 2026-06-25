package domain.service;

import domain.model.Booking;
import domain.model.BookingSeat;
import domain.model.BookingStatus;
import domain.model.SeatStatus;
import domain.model.SeatUnavailableException;
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
        Booking booking =
                new Booking(
                        null, userId, showId, LocalDateTime.now(), amount, BookingStatus.PENDING);
        Booking saved = bookingRepo.save(booking);

        for (Long seatId : seatIds) {
            int rows = seatRepo.claimSeat(seatId);
            if (rows == 0) {
                throw new SeatUnavailableException("Seat " + seatId + " is not available");
            }
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
            seatRepo.updateStatus(bs.getSeatId(), SeatStatus.AVAILABLE);
        }
    }

    public List<Booking> getHistory(Long userId) {
        return bookingRepo.findByUserId(userId);
    }
}
