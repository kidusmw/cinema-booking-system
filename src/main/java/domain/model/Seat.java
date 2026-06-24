package domain.model;

import java.util.Objects;

public class Seat {
    private Long seatId;
    private Long hallId;
    private String seatNumber;
    private String seatType;
    private String status;

    public Seat() {}

    public Seat(Long seatId, Long hallId, String seatNumber, String seatType, String status) {
        this.seatId = seatId;
        this.hallId = hallId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.status = status;
    }

    public boolean isAvailable() {
        return "available".equals(status);
    }

    public void book() {
        if (!"available".equals(status)) {
            throw new IllegalStateException(
                    "Seat " + seatId + " is not available (status: " + status + ")");
        }
        this.status = "booked";
    }

    public void release() {
        this.status = "available";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat seat)) return false;
        return Objects.equals(seatId, seat.seatId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(seatId);
    }
}
