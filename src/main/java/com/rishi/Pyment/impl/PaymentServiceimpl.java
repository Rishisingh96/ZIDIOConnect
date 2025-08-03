package com.rishi.Pyment.impl;

import com.rishi.Pyment.Payment;
import com.rishi.Pyment.PaymentGatewayUtil;
import com.rishi.Pyment.PaymentRepository;
import com.rishi.Pyment.PaymentService;
import com.rishi.Pyment.PaymentStatus;
import com.rishi.dto.PaymentDTO;
import com.rishi.request.PaymentRequest;
import com.rishi.response.ApiResponseMessage;
import com.rishi.response.PageableResponse;
import com.rishi.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class PaymentServiceimpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentGatewayUtil paymentGatewayUtil;

    // Basic payment operations

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        try {
            // Validate payment request
            if (!isValidPaymentRequest(paymentRequest)) {
                return new PaymentResponse(null, PaymentStatus.FAILED, 
                    "Invalid payment request", "Validation failed", false);
            }

            // Generate transaction ID
            String transactionId = paymentGatewayUtil.generateTransactionId();

            // Create payment record
            Payment payment = createPaymentRecord(paymentRequest, transactionId, PaymentStatus.PENDING);

            // Process payment based on method
            PaymentResponse response = switch (paymentRequest.getPaymentMethod().toUpperCase()) {
                case "CREDIT_CARD", "DEBIT_CARD" -> processPaymentWithCard(paymentRequest);
                case "PAYPAL" -> processPaymentWithPayPal(paymentRequest);
                case "RAZORPAY" -> processPaymentWithRazorpay(paymentRequest);
                case "BANK_TRANSFER" -> processBankTransfer(paymentRequest);
                default -> new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                    "Unsupported payment method", "Invalid payment method", false);
            };

            // Update payment record with response
            updatePaymentWithResponse(payment, response);

            return response;
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());
            return new PaymentResponse(null, PaymentStatus.FAILED, 
                "Payment processing error", e.getMessage(), false);
        }
    }

    @Override
    public PaymentResponse processPaymentWithCard(PaymentRequest paymentRequest) {
        try {
            String transactionId = paymentGatewayUtil.generateTransactionId();
            
            // Validate card details
            if (!isValidCardDetails(paymentRequest)) {
                return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                    "Invalid card details", "Card validation failed", false);
            }

            // Process with Stripe
            return paymentGatewayUtil.processStripePayment(
                transactionId, 
                paymentRequest.getAmount(), 
                paymentRequest.getCurrency(), 
                "card_token"
            );
        } catch (Exception e) {
            log.error("Error processing card payment: {}", e.getMessage());
            return new PaymentResponse(null, PaymentStatus.FAILED, 
                "Card payment error", e.getMessage(), false);
        }
    }

    @Override
    public PaymentResponse processPaymentWithPayPal(PaymentRequest paymentRequest) {
        try {
            String transactionId = paymentGatewayUtil.generateTransactionId();
            
            return paymentGatewayUtil.processPayPalPayment(
                transactionId, 
                paymentRequest.getAmount(), 
                paymentRequest.getCurrency()
            );
        } catch (Exception e) {
            log.error("Error processing PayPal payment: {}", e.getMessage());
            return new PaymentResponse(null, PaymentStatus.FAILED, 
                "PayPal payment error", e.getMessage(), false);
        }
    }

    @Override
    public PaymentResponse processPaymentWithRazorpay(PaymentRequest paymentRequest) {
        try {
            String transactionId = paymentGatewayUtil.generateTransactionId();
            
            return paymentGatewayUtil.processRazorpayPayment(
                transactionId, 
                paymentRequest.getAmount(), 
                paymentRequest.getCurrency()
            );
        } catch (Exception e) {
            log.error("Error processing Razorpay payment: {}", e.getMessage());
            return new PaymentResponse(null, PaymentStatus.FAILED, 
                "Razorpay payment error", e.getMessage(), false);
        }
    }

    @Override
    public PaymentResponse processBankTransfer(PaymentRequest paymentRequest) {
        try {
            String transactionId = paymentGatewayUtil.generateTransactionId();
            
            return paymentGatewayUtil.processBankTransfer(
                transactionId, 
                paymentRequest.getAmount(), 
                paymentRequest.getCurrency()
            );
        } catch (Exception e) {
            log.error("Error processing bank transfer: {}", e.getMessage());
            return new PaymentResponse(null, PaymentStatus.FAILED, 
                "Bank transfer error", e.getMessage(), false);
        }
    }

    // Payment retrieval methods

    @Override
    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
    }

    @Override
    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction id: " + transactionId));
    }

    @Override
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public PageableResponse<PaymentDTO> getPaymentsByUserId(Long userId, Pageable pageable) {
        Page<Payment> page = paymentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        PageableResponse<PaymentDTO> response = new PageableResponse<>();
        response.setContent(page.getContent().stream().map(PaymentDTO::new).toList());
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        
        return response;
    }

    @Override
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Override
    public List<Payment> getPaymentsByPurpose(Payment.PaymentPurpose purpose) {
        return paymentRepository.findByPurposeOrderByCreatedAtDesc(purpose);
    }

    // Payment status management

    @Override
    public PaymentResponse verifyPaymentStatus(String transactionId) {
        try {
            Payment payment = getPaymentByTransactionId(transactionId);
            PaymentStatus newStatus = paymentGatewayUtil.verifyPaymentStatus(
                payment.getGatewayTransactionId(), 
                payment.getPaymentMethod()
            );
            
            payment.setStatus(newStatus);
            paymentRepository.save(payment);
            
            return new PaymentResponse(transactionId, newStatus, 
                "Payment status verified", payment.getGatewayTransactionId());
        } catch (Exception e) {
            log.error("Error verifying payment status: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Status verification failed", e.getMessage(), false);
        }
    }

    @Override
    public PaymentResponse updatePaymentStatus(String transactionId, PaymentStatus status) {
        try {
            Payment payment = getPaymentByTransactionId(transactionId);
            payment.setStatus(status);
            paymentRepository.save(payment);
            
            return new PaymentResponse(transactionId, status, 
                "Payment status updated", payment.getGatewayTransactionId());
        } catch (Exception e) {
            log.error("Error updating payment status: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Status update failed", e.getMessage(), false);
        }
    }

    @Override
    public PaymentResponse cancelPayment(String transactionId) {
        return updatePaymentStatus(transactionId, PaymentStatus.CANCELLED);
    }

    // Refund operations

    @Override
    public PaymentResponse refundPayment(String transactionId, Double amount, String reason) {
        try {
            Payment payment = getPaymentByTransactionId(transactionId);
            
            if (!payment.getStatus().isSuccessful()) {
                return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                    "Cannot refund unsuccessful payment", "Invalid payment status", false);
            }
            
            PaymentResponse refundResponse = paymentGatewayUtil.refundPayment(transactionId, amount, reason);
            
            if (refundResponse.isSuccess()) {
                payment.setStatus(PaymentStatus.REFUNDED);
                paymentRepository.save(payment);
            }
            
            return refundResponse;
        } catch (Exception e) {
            log.error("Error processing refund: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Refund processing error", e.getMessage(), false);
        }
    }

    @Override
    public PaymentResponse partialRefund(String transactionId, Double amount, String reason) {
        try {
            Payment payment = getPaymentByTransactionId(transactionId);
            
            if (amount >= payment.getAmount()) {
                return refundPayment(transactionId, amount, reason);
            }
            
            PaymentResponse refundResponse = paymentGatewayUtil.refundPayment(transactionId, amount, reason);
            
            if (refundResponse.isSuccess()) {
                payment.setStatus(PaymentStatus.PARTIALLY_REFUNDED);
                paymentRepository.save(payment);
            }
            
            return refundResponse;
        } catch (Exception e) {
            log.error("Error processing partial refund: {}", e.getMessage());
            return new PaymentResponse(transactionId, PaymentStatus.FAILED, 
                "Partial refund error", e.getMessage(), false);
        }
    }

    // Job portal specific payment methods

    @Override
    public PaymentResponse processJobPostingPayment(Long userId, Long jobId, String jobTitle, Double amount) {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setPurpose(Payment.PaymentPurpose.JOB_POSTING);
        request.setRelatedEntityId(jobId);
        request.setRelatedEntityType("JOB");
        request.setDescription("Payment for job posting: " + jobTitle);
        request.setPaymentMethod("STRIPE");
        
        return processPayment(request);
    }

    @Override
    public PaymentResponse processPremiumJobPostingPayment(Long userId, Long jobId, String jobTitle, Double amount) {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setPurpose(Payment.PaymentPurpose.PREMIUM_JOB_POSTING);
        request.setRelatedEntityId(jobId);
        request.setRelatedEntityType("JOB");
        request.setDescription("Payment for premium job posting: " + jobTitle);
        request.setPaymentMethod("STRIPE");
        
        return processPayment(request);
    }

    @Override
    public PaymentResponse processFeaturedProfilePayment(Long userId, Long profileId, Double amount) {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setPurpose(Payment.PaymentPurpose.FEATURED_PROFILE);
        request.setRelatedEntityId(profileId);
        request.setRelatedEntityType("PROFILE");
        request.setDescription("Payment for featured profile");
        request.setPaymentMethod("STRIPE");
        
        return processPayment(request);
    }

    @Override
    public PaymentResponse processResumeAccessPayment(Long userId, Long resumeId, Double amount) {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setPurpose(Payment.PaymentPurpose.RESUME_ACCESS);
        request.setRelatedEntityId(resumeId);
        request.setRelatedEntityType("RESUME");
        request.setDescription("Payment for resume access");
        request.setPaymentMethod("STRIPE");
        
        return processPayment(request);
    }

    @Override
    public PaymentResponse processInterviewSchedulingPayment(Long userId, Long applicationId, Double amount) {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setPurpose(Payment.PaymentPurpose.INTERVIEW_SCHEDULING);
        request.setRelatedEntityId(applicationId);
        request.setRelatedEntityType("JOB_APPLICATION");
        request.setDescription("Payment for interview scheduling");
        request.setPaymentMethod("STRIPE");
        
        return processPayment(request);
    }

    @Override
    public PaymentResponse processRecruiterVerificationPayment(Long userId, Double amount) {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setPurpose(Payment.PaymentPurpose.RECRUITER_VERIFICATION);
        request.setDescription("Payment for recruiter verification");
        request.setPaymentMethod("STRIPE");
        
        return processPayment(request);
    }

    @Override
    public PaymentResponse processPremiumSubscriptionPayment(Long userId, String subscriptionType, Double amount) {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setPurpose(Payment.PaymentPurpose.PREMIUM_SUBSCRIPTION);
        request.setRelatedEntityType("SUBSCRIPTION");
        request.setDescription("Payment for premium subscription: " + subscriptionType);
        request.setPaymentMethod("STRIPE");
        
        return processPayment(request);
    }

    @Override
    public PaymentResponse processAdvertisingPayment(Long userId, String adType, Double amount) {
        PaymentRequest request = new PaymentRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setPurpose(Payment.PaymentPurpose.ADVERTISING);
        request.setRelatedEntityType("ADVERTISEMENT");
        request.setDescription("Payment for advertising: " + adType);
        request.setPaymentMethod("STRIPE");
        
        return processPayment(request);
    }

    // Payment analytics and reporting

    @Override
    public Double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findPaymentsByDateRange(startDate, endDate);
        return payments.stream()
                .filter(p -> p.getStatus().isSuccessful())
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    @Override
    public Double getTotalRevenueByUser(Long userId) {
        Double total = paymentRepository.sumSuccessfulPaymentsByUserId(userId);
        return total != null ? total : 0.0;
    }

    @Override
    public Long getSuccessfulPaymentCountByUser(Long userId) {
        return paymentRepository.countSuccessfulPaymentsByUserId(userId);
    }

    @Override
    public List<Payment> getRecentPayments(int days) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(days);
        return paymentRepository.findRecentPayments(thirtyDaysAgo);
    }

    @Override
    public List<Payment> getPaymentsByAmountRange(Double minAmount, Double maxAmount) {
        return paymentRepository.findPaymentsByAmountRange(minAmount, maxAmount);
    }

    // Payment validation

    @Override
    public boolean isValidPaymentRequest(PaymentRequest paymentRequest) {
        return paymentRequest != null &&
               paymentRequest.getUserId() != null &&
               paymentGatewayUtil.isValidAmount(paymentRequest.getAmount()) &&
               paymentRequest.getPurpose() != null &&
               paymentGatewayUtil.isValidPaymentMethod(paymentRequest.getPaymentMethod());
    }

    @Override
    public boolean isPaymentSuccessful(String transactionId) {
        try {
            Payment payment = getPaymentByTransactionId(transactionId);
            return payment.getStatus().isSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPaymentPending(String transactionId) {
        try {
            Payment payment = getPaymentByTransactionId(transactionId);
            return payment.getStatus().isPending();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPaymentFailed(String transactionId) {
        try {
            Payment payment = getPaymentByTransactionId(transactionId);
            return payment.getStatus().isFailed();
        } catch (Exception e) {
            return false;
        }
    }

    // Payment cleanup and maintenance

    @Override
    public void cleanupExpiredPayments() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Payment> expiredPayments = paymentRepository.findPaymentsByDateRange(
            LocalDateTime.now().minusDays(90), thirtyDaysAgo
        );
        
        for (Payment payment : expiredPayments) {
            if (payment.getStatus() == PaymentStatus.PENDING) {
                payment.setStatus(PaymentStatus.EXPIRED);
                paymentRepository.save(payment);
            }
        }
        
        log.info("Cleaned up {} expired payments", expiredPayments.size());
    }

    @Override
    public void cleanupFailedPayments(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        List<Payment> failedPayments = paymentRepository.findFailedPayments();
        
        failedPayments.removeIf(payment -> payment.getCreatedAt().isAfter(cutoffDate));
        
        for (Payment payment : failedPayments) {
            paymentRepository.delete(payment);
        }
        
        log.info("Cleaned up {} failed payments older than {} days", failedPayments.size(), daysOld);
    }

    @Override
    public void archiveOldPayments(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        List<Payment> oldPayments = paymentRepository.findPaymentsByDateRange(
            LocalDateTime.now().minusDays(365), cutoffDate
        );
        
        log.info("Archived {} old payments", oldPayments.size());
        // In a real implementation, you would move these to an archive table
    }

    // Payment webhook handling

    @Override
    public PaymentResponse handlePaymentWebhook(String gatewayTransactionId, String status, String response) {
        try {
            Optional<Payment> paymentOpt = paymentRepository.findByGatewayTransactionId(gatewayTransactionId);
            if (paymentOpt.isEmpty()) {
                return new PaymentResponse(null, PaymentStatus.FAILED, 
                    "Payment not found", "Invalid gateway transaction ID", false);
            }
            
            Payment payment = paymentOpt.get();
            PaymentStatus newStatus = PaymentStatus.valueOf(status.toUpperCase());
            
            payment.setStatus(newStatus);
            payment.setGatewayResponse(response);
            paymentRepository.save(payment);
            
            return new PaymentResponse(payment.getTransactionId(), newStatus, 
                "Webhook processed successfully", gatewayTransactionId);
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage());
            return new PaymentResponse(null, PaymentStatus.FAILED, 
                "Webhook processing error", e.getMessage(), false);
        }
    }

    // Payment preferences and settings

    @Override
    public List<String> getAvailablePaymentMethods() {
        return Arrays.asList("CREDIT_CARD", "DEBIT_CARD", "PAYPAL", "RAZORPAY", "BANK_TRANSFER");
    }

    @Override
    public boolean isPaymentMethodEnabled(String paymentMethod) {
        // In a real implementation, this would check configuration
        return getAvailablePaymentMethods().contains(paymentMethod.toUpperCase());
    }

    @Override
    public void updatePaymentMethodSettings(String paymentMethod, boolean enabled) {
        log.info("Updated payment method {} to enabled: {}", paymentMethod, enabled);
        // In a real implementation, this would update configuration
    }

    // Helper methods

    private Payment createPaymentRecord(PaymentRequest request, String transactionId, PaymentStatus status) {
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setTransactionId(transactionId);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus(status);
        payment.setPurpose(request.getPurpose());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setRelatedEntityId(request.getRelatedEntityId());
        payment.setRelatedEntityType(request.getRelatedEntityType());
        payment.setDescription(request.getDescription());
        
        return paymentRepository.save(payment);
    }

    private void updatePaymentWithResponse(Payment payment, PaymentResponse response) {
        payment.setStatus(response.getStatus());
        payment.setGatewayTransactionId(response.getGatewayTransactionId());
        payment.setGatewayResponse(response.getMessage());
        
        if (response.getStatus() == PaymentStatus.FAILED) {
            payment.setFailureReason(response.getFailureReason());
        }
        
        paymentRepository.save(payment);
    }

    private boolean isValidCardDetails(PaymentRequest request) {
        return request.getCardNumber() != null && 
               request.getCardNumber().length() >= 13 &&
               request.getExpiryMonth() != null &&
               request.getExpiryYear() != null &&
               request.getCvv() != null &&
               request.getCvv().length() >= 3;
    }
}
