package domain.model;

import java.time.LocalDateTime;

public class Payment {
    private Long paymentId;
    private Long bookingId;
    private double totalAmount;
    private String status;
    private String otp;
    private String paymentMethod;
    private LocalDateTime paymentDate;

    public Payment() {}

    public Payment(
            Long paymentId,
            Long bookingId,
            double totalAmount,
            String status,
            String otp,
            String paymentMethod) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.otp = otp;
        this.paymentMethod = paymentMethod;
    }

    public void markPaid() {
        if (!"pending".equals(status)) {
            throw new IllegalStateException(
                    "Cannot mark payment " + paymentId + " as paid — current status: " + status);
        }
        this.status = "paid";
    }

    public void markFailed() {
        this.status = "failed";
    }

    public boolean verifyOtp(String inputOtp) {
        return otp != null && otp.equals(inputOtp);
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}
