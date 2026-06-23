package domain.service;

import domain.model.Payment;
import domain.port.PaymentRepository;
import java.util.Optional;
import java.util.UUID;

public class PaymentService {
    private final PaymentRepository paymentRepo;

    public PaymentService(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public Payment processPayment(Long bookingId, double amount, String paymentMethod) {
        String otp = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Payment payment = new Payment(null, bookingId, amount, "pending", otp, paymentMethod);
        return paymentRepo.save(payment);
    }

    public boolean verifyOtp(Long paymentId, String otp) {
        Optional<Payment> paymentOpt = paymentRepo.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            return false;
        }
        Payment payment = paymentOpt.get();
        if (payment.verifyOtp(otp)) {
            payment.markPaid();
            paymentRepo.save(payment);
            return true;
        }
        return false;
    }

    public void refund(Long paymentId) {
        Optional<Payment> paymentOpt = paymentRepo.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            throw new IllegalArgumentException("Payment not found: " + paymentId);
        }
        Payment payment = paymentOpt.get();
        payment.markFailed();
        paymentRepo.save(payment);
    }
}
