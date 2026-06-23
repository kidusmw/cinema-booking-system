package domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Showtime {
    private Long showId;
    private Long movieId;
    private Long hallId;
    private LocalDate showDate;
    private LocalTime showTime;
    private double ticketPrice;
    private String movieName;
    private String hallName;

    public Showtime() {}

    public Showtime(Long showId, Long movieId, Long hallId, LocalDate showDate,
                    LocalTime showTime, double ticketPrice) {
        this.showId = showId;
        this.movieId = movieId;
        this.hallId = hallId;
        this.showDate = showDate;
        this.showTime = showTime;
        this.ticketPrice = ticketPrice;
    }

    public boolean isPast() { return showDate != null && showDate.isBefore(LocalDate.now()); }

    public boolean hasStarted() {
        if (isPast()) return true;
        return showDate != null && showDate.equals(LocalDate.now())
                && showTime != null && showTime.isBefore(LocalTime.now());
    }

    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public Long getHallId() { return hallId; }
    public void setHallId(Long hallId) { this.hallId = hallId; }

    public LocalDate getShowDate() { return showDate; }
    public void setShowDate(LocalDate showDate) { this.showDate = showDate; }

    public LocalTime getShowTime() { return showTime; }
    public void setShowTime(LocalTime showTime) { this.showTime = showTime; }

    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public String getHallName() { return hallName; }
    public void setHallName(String hallName) { this.hallName = hallName; }
}
