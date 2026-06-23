package domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private Long bookingId;
    private Long userId;
    private Long showId;
    private LocalDateTime bookingDate;
    private String movieName;
    private double amount;
    private String bookingStatus;
    private List<BookingSeat> seats = new ArrayList<>();

    public Booking() {}

    public Booking(Long bookingId, Long userId, Long showId,
                   LocalDateTime bookingDate, double amount, String bookingStatus) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.showId = showId;
        this.bookingDate = bookingDate;
        this.amount = amount;
        this.bookingStatus = bookingStatus;
    }

    public void confirm() {
        if (!"pending".equals(bookingStatus)) {
            throw new IllegalStateException("Cannot confirm booking " + bookingId
                    + " with status: " + bookingStatus);
        }
        this.bookingStatus = "confirmed";
    }

    public void cancel() {
        if ("cancelled".equals(bookingStatus)) {
            throw new IllegalStateException("Booking " + bookingId + " is already cancelled");
        }
        this.bookingStatus = "cancelled";
    }

    public double total() {
        return seats.stream()
                .mapToDouble(BookingSeat::getPrice)
                .sum();
    }

    public void addSeat(BookingSeat seat) {
        seats.add(seat);
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public List<BookingSeat> getSeats() { return seats; }
    public void setSeats(List<BookingSeat> seats) { this.seats = seats; }
}
