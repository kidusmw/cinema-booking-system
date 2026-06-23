package ui.model;

import java.util.Date;

public class Show {
    private String showID;
    private Date showDate;
    private String showTime;
    private String movieHallID;
    private String movieID;
    private String movieName;
    private String hallName;

    public Show() {}

    public Show(
            String showID,
            Date showDate,
            String showTime,
            String movieHallID,
            String movieID,
            String movieName,
            String hallName) {
        this.showID = showID;
        this.showDate = showDate;
        this.showTime = showTime;
        this.movieHallID = movieHallID;
        this.movieID = movieID;
        this.movieName = movieName;
        this.hallName = hallName;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getShowID() {
        return showID;
    }

    public void setShowID(String showID) {
        this.showID = showID;
    }

    public Date getShowDate() {
        return showDate;
    }

    public void setShowDate(Date showDate) {
        this.showDate = showDate;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getMovieHallID() {
        return movieHallID;
    }

    public void setMovieHallID(String movieHallID) {
        this.movieHallID = movieHallID;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    @Override
    public String toString() {
        return "Show{showID='"
                + showID
                + "', showDate="
                + showDate
                + ", showTime='"
                + showTime
                + "', movieHallID='"
                + movieHallID
                + "', movieID='"
                + movieID
                + "', movieName='"
                + movieName
                + "', hallName='"
                + hallName
                + "'}";
    }
}
