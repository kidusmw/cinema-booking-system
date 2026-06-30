package domain.model;

import java.util.Objects;

public class Seat {
    private Long seatId;
    private Long hallId;
    private String seatNumber;
    private String seatType;
    private SeatStatus status;

    public Seat() {}

    public Seat(Long seatId, Long hallId, String seatNumber, String seatType, SeatStatus status) {
        this.seatId = seatId;
        this.hallId = hallId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.status = status;
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public void book() {
        if (status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Seat " + seatId + " is not available (status: " + status + ")");
        }
        this.status = SeatStatus.BOOKED;
    }

    public void release() {
        this.status = SeatStatus.AVAILABLE;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Seat seat)) return false;
        return Objects.equals(seatId, seat.seatId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(seatId);
    }
}
