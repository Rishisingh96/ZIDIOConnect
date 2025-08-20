package com.rishi.Pyment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.rishi.request.PaymentRequest;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void testPaymentServiceIsWorking() {
        assertNotNull(paymentService, "Payment service should be autowired");
    }

    @Test
    public void testPaymentValidation() {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(1L);
        request.setAmount(100.0);
        request.setPurpose(Payment.PaymentPurpose.JOB_POSTING);
        request.setPaymentMethod("RAZORPAY");
        request.setDescription("Test job posting payment");
        
        boolean isValid = paymentService.isValidPaymentRequest(request);
        assertTrue(isValid, "Payment request should be valid");
    }

    @Test
    public void testInvalidPaymentRequest() {
        PaymentRequest request = new PaymentRequest();
        // Missing required fields
        
        boolean isValid = paymentService.isValidPaymentRequest(request);
        assertFalse(isValid, "Payment request should be invalid");
    }
}

