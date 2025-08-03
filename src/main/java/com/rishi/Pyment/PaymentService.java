package com.rishi.Pyment;

import com.rishi.dto.PaymentDTO;
import com.rishi.request.PaymentRequest;
import com.rishi.response.PageableResponse;
import com.rishi.response.PaymentResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    
    // Basic payment operations
    PaymentResponse processPayment(PaymentRequest paymentRequest);
    PaymentResponse processPaymentWithCard(PaymentRequest paymentRequest);
    PaymentResponse processPaymentWithPayPal(PaymentRequest paymentRequest);
    PaymentResponse processPaymentWithRazorpay(PaymentRequest paymentRequest);
    PaymentResponse processBankTransfer(PaymentRequest paymentRequest);
    
    // Payment retrieval
    Payment getPaymentById(Long paymentId);
    Payment getPaymentByTransactionId(String transactionId);
    List<Payment> getPaymentsByUserId(Long userId);
    PageableResponse<PaymentDTO> getPaymentsByUserId(Long userId, Pageable pageable);
    List<Payment> getPaymentsByStatus(PaymentStatus status);
    List<Payment> getPaymentsByPurpose(Payment.PaymentPurpose purpose);
    
    // Payment status management
    PaymentResponse verifyPaymentStatus(String transactionId);
    PaymentResponse updatePaymentStatus(String transactionId, PaymentStatus status);
    PaymentResponse cancelPayment(String transactionId);
    
    // Refund operations
    PaymentResponse refundPayment(String transactionId, Double amount, String reason);
    PaymentResponse partialRefund(String transactionId, Double amount, String reason);
    
    // Job portal specific payment methods
    PaymentResponse processJobPostingPayment(Long userId, Long jobId, String jobTitle, Double amount);
    PaymentResponse processPremiumJobPostingPayment(Long userId, Long jobId, String jobTitle, Double amount);
    PaymentResponse processFeaturedProfilePayment(Long userId, Long profileId, Double amount);
    PaymentResponse processResumeAccessPayment(Long userId, Long resumeId, Double amount);
    PaymentResponse processInterviewSchedulingPayment(Long userId, Long applicationId, Double amount);
    PaymentResponse processRecruiterVerificationPayment(Long userId, Double amount);
    PaymentResponse processPremiumSubscriptionPayment(Long userId, String subscriptionType, Double amount);
    PaymentResponse processAdvertisingPayment(Long userId, String adType, Double amount);
    
    // Payment analytics and reporting
    Double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Double getTotalRevenueByUser(Long userId);
    Long getSuccessfulPaymentCountByUser(Long userId);
    List<Payment> getRecentPayments(int days);
    List<Payment> getPaymentsByAmountRange(Double minAmount, Double maxAmount);
    
    // Payment validation
    boolean isValidPaymentRequest(PaymentRequest paymentRequest);
    boolean isPaymentSuccessful(String transactionId);
    boolean isPaymentPending(String transactionId);
    boolean isPaymentFailed(String transactionId);
    
    // Payment cleanup and maintenance
    void cleanupExpiredPayments();
    void cleanupFailedPayments(int daysOld);
    void archiveOldPayments(int daysOld);
    
    // Payment webhook handling
    PaymentResponse handlePaymentWebhook(String gatewayTransactionId, String status, String response);
    
    // Payment preferences and settings
    List<String> getAvailablePaymentMethods();
    boolean isPaymentMethodEnabled(String paymentMethod);
    void updatePaymentMethodSettings(String paymentMethod, boolean enabled);
}
