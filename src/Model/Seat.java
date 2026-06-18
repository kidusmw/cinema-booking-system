package Model;

public class Seat {
    private String seatID;
    private String seatNumber;
    private String seatType;
    private String status;
    private String showID;
    private int movieHallID;
    public Seat() {}
    public Seat(String seatID, String seatNumber, String seatType, String status, String showID) {
        this.seatID = seatID;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.status = status;
        this.showID = showID;
    }

    public String getSeatID() { return seatID; }
    public void setSeatID(String seatID) { this.seatID = seatID; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShowID() { return showID; }   // ✅ method keeps camelCase
    public void setShowID(String showID) {
        this.showID = showID;   // ✅ FIXED: assigns parameter to field
    }
    public int getMovieHallID() { return movieHallID; }
    public void setMovieHallID(int movieHallID) { this.movieHallID = movieHallID; }
    @Override
    public String toString() {
        return "Seat{seatID=" + seatID + ", seatNumber='" + seatNumber +
                "', type='" + seatType + "', status='" + status + "', hallID=" + movieHallID + "}";
    }
}
