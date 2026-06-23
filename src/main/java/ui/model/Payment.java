package ui.model;

import java.util.Date;

public class Payment {
    private String paymentID;
    private String status;
    private double totalAmount;
    private String otp;
    private int bookingID;
    private String paymentMethod;
    private Date paymentDate;

    public Payment() {}

    public Payment(String paymentID, String status, double totalAmount, String otp, int bookingID) {
        this.paymentID = paymentID;
        this.status = status;
        this.totalAmount = totalAmount;
        this.otp = otp;
        this.bookingID = bookingID;
        this.paymentDate = new Date();
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{paymentID='"
                + paymentID
                + "', status='"
                + status
                + "', totalAmount="
                + totalAmount
                + ", bookingID='"
                + bookingID
                + "'}";
    }
}
