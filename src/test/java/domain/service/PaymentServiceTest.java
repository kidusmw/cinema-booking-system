package domain.service;

import domain.model.Payment;
import domain.port.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepo;

    @InjectMocks
    PaymentService paymentService;

    @Test
    void processPaymentCreatesPendingPaymentWithOtp() {
        when(paymentRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Payment result = paymentService.processPayment(1L, 100.0, "card");
        assertNotNull(result);
        assertEquals(1L, result.getBookingId());
        assertEquals("pending", result.getStatus());
        assertEquals("card", result.getPaymentMethod());
        assertEquals(6, result.getOtp().length());
    }

    @Test
    void verifyOtpMarksPaymentPaid() {
        Payment payment = new Payment(1L, 1L, 100.0, "pending", "ABC123", "card");
        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        boolean result = paymentService.verifyOtp(1L, "ABC123");

        assertTrue(result);
        assertEquals("paid", payment.getStatus());
        verify(paymentRepo).save(payment);
    }

    @Test
    void verifyOtpFailsWithWrongOtp() {
        Payment payment = new Payment(1L, 1L, 100.0, "pending", "ABC123", "card");
        when(paymentRepo.findById(1L)).thenReturn(Optional.of(payment));

        boolean result = paymentService.verifyOtp(1L, "WRONG");

        assertFalse(result);
        assertEquals("pending", payment.getStatus());
    }

    @Test
    void verifyOtpFailsWhenPaymentNotFound() {
        when(paymentRepo.findById(999L)).thenReturn(Optional.empty());
        assertFalse(paymentService.verifyOtp(999L, "ABC123"));
    }
}
