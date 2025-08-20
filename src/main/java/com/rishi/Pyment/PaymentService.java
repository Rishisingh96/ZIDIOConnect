package com.rishi.Pyment;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.rishi.dto.PaymentDTO;
import com.rishi.request.PaymentRequest;
import com.rishi.response.PageableResponse;
import com.rishi.response.PaymentResponse;

public interface PaymentService {
    
    // Core Razorpay payment operations
    PaymentResponse processPayment(PaymentRequest paymentRequest);
    PaymentResponse processPaymentWithRazorpay(PaymentRequest paymentRequest);
    
    // Essential payment retrieval
    Payment getPaymentById(Long paymentId);
    Payment getPaymentByTransactionId(String transactionId);
    List<Payment> getPaymentsByUserId(Long userId);
    PageableResponse<PaymentDTO> getPaymentsByUserId(Long userId, Pageable pageable);
    
    // Payment status management
    PaymentResponse verifyPaymentStatus(String transactionId);
    PaymentResponse updatePaymentStatus(String transactionId, PaymentStatus status);
    
    // Refund operations
    PaymentResponse refundPayment(String transactionId, Double amount, String reason);
    
    // Job portal specific payment methods
    PaymentResponse processJobPostingPayment(Long userId, Long jobId, String jobTitle, Double amount);
    PaymentResponse processPremiumJobPostingPayment(Long userId, Long jobId, String jobTitle, Double amount);
    PaymentResponse processFeaturedProfilePayment(Long userId, Long profileId, Double amount);
    
    // Payment validation
    boolean isValidPaymentRequest(PaymentRequest paymentRequest);
    boolean isPaymentSuccessful(String transactionId);
    
    // Payment webhook handling
    PaymentResponse handlePaymentWebhook(String gatewayTransactionId, String status, String response);
}
