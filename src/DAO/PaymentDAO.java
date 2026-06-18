package DAO;

import Model.Payment;
import java.util.List;

public interface PaymentDAO {

    boolean addPayment(Payment payment);
    boolean updatePayment(Payment payment);
    boolean deletePayment(String paymentID);
    Payment searchPaymentById(String paymentID);
    List<Payment> getAllPayments();
    Payment getPaymentByBooking(String bookingID);
    boolean verifyPayment(String paymentID, String otp);
    boolean updatePaymentStatus(String paymentID, String status);
}

