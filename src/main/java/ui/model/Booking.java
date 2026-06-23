package ui.model;

import java.util.Date;

public class Booking {
    private int bookingID;
    private int userID;
    private Date bookingDate;
    private String movieName;
    private String showID;
    private String seatID;
    private double amount;
    private String bookingStatus;
    private int customerID;
    private String customerName;

    public Booking() {}
    public Booking(int bookingID, int userID, String customerName, String showID,
                   String seatID, double amount, String bookingStatus) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.customerName = customerName;
        this.showID = showID;
        this.seatID = seatID;
        this.amount = amount;
        this.bookingStatus = bookingStatus;
    }

    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getShowID() { return showID; }
    public void setShowID(String showID) { this.showID = showID; }

    public String getSeatID() { return seatID; }
    public void setSeatID(String seatID) { this.seatID = seatID; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public int getCustomerID() { return customerID; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingID=" + bookingID +
                ", userID=" + userID +
                ", username='" + customerName + '\'' +
                ", movieName='" + movieName + '\'' +
                ", showID='" + showID + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}