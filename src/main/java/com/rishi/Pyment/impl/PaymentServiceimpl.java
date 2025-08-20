package com.rishi.Pyment.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rishi.Pyment.Payment;
import com.rishi.Pyment.PaymentGatewayUtil;
import com.rishi.Pyment.PaymentRepository;
import com.rishi.Pyment.PaymentService;
import com.rishi.Pyment.PaymentStatus;
import com.rishi.dto.PaymentDTO;
import com.rishi.request.PaymentRequest;
import com.rishi.response.PageableResponse;
import com.rishi.response.PaymentResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class PaymentServiceimpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentGatewayUtil paymentGatewayUtil;

    // Core Razorpay payment operations

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

            // Process payment with Razorpay (default)
            PaymentResponse response = processPaymentWithRazorpay(paymentRequest);

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
    public PaymentResponse processPaymentWithRazorpay(PaymentRequest paymentRequest) {
        try {
            String transactionId = paymentGatewayUtil.generateTransactionId();

            paymentRequest.setPaymentMethod("RAZORPAY");
            Payment payment = createPaymentRecord(paymentRequest, transactionId, PaymentStatus.PENDING);

            PaymentResponse response = paymentGatewayUtil.processRazorpayPayment(
                    transactionId,
                    paymentRequest.getAmount(),
                    paymentRequest.getCurrency()
            );

            updatePaymentWithResponse(payment, response);
            return response;
        } catch (Exception e) {
            log.error("Error processing Razorpay payment: {}", e.getMessage());
            return new PaymentResponse(null, PaymentStatus.FAILED, 
                "Razorpay payment error", e.getMessage(), false);
        }
    }

    // Essential payment retrieval methods

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
        request.setPaymentMethod("RAZORPAY");
        
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
        request.setPaymentMethod("RAZORPAY");
        
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
        request.setPaymentMethod("RAZORPAY");
        
        return processPayment(request);
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
}
