package domain.model;

import java.util.Objects;

public class BookingSeat {
    private Long bookingId;
    private Long seatId;
    private double price;

    public BookingSeat() {}

    public BookingSeat(Long bookingId, Long seatId, double price) {
        this.bookingId = bookingId;
        this.seatId = seatId;
        this.price = price;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) return true;
        if (!(o instanceof BookingSeat that)) return false;
        return Objects.equals(bookingId, that.bookingId) && Objects.equals(seatId, that.seatId);
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return Objects.hash(bookingId, seatId);
    }
}
