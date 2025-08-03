package com.rishi.Pyment;

import com.rishi.dto.PaymentDTO;
import com.rishi.request.PaymentRequest;
import com.rishi.response.ApiResponseMessage;
import com.rishi.response.PageableResponse;
import com.rishi.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Basic payment operations

    @PostMapping("/process")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentService.processPayment(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/process/card")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processCardPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentService.processPaymentWithCard(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/process/paypal")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processPayPalPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentService.processPaymentWithPayPal(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/process/razorpay")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processRazorpayPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentService.processPaymentWithRazorpay(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/process/bank-transfer")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processBankTransfer(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentService.processBankTransfer(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Payment retrieval

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
        Payment payment = paymentService.getPaymentByTransactionId(transactionId);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PageableResponse<PaymentDTO>> getUserPayments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        PageableResponse<PaymentDTO> response = paymentService.getPaymentsByUserId(userId, pageable);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/purpose/{purpose}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> getPaymentsByPurpose(@PathVariable Payment.PaymentPurpose purpose) {
        List<Payment> payments = paymentService.getPaymentsByPurpose(purpose);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // Payment status management

    @PutMapping("/verify/{transactionId}")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> verifyPaymentStatus(@PathVariable String transactionId) {
        PaymentResponse response = paymentService.verifyPaymentStatus(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/status/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable String transactionId,
            @RequestParam PaymentStatus status) {
        PaymentResponse response = paymentService.updatePaymentStatus(transactionId, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/cancel/{transactionId}")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable String transactionId) {
        PaymentResponse response = paymentService.cancelPayment(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Refund operations

    @PostMapping("/refund/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable String transactionId,
            @RequestParam Double amount,
            @RequestParam String reason) {
        PaymentResponse response = paymentService.refundPayment(transactionId, amount, reason);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refund/partial/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> partialRefund(
            @PathVariable String transactionId,
            @RequestParam Double amount,
            @RequestParam String reason) {
        PaymentResponse response = paymentService.partialRefund(transactionId, amount, reason);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Job portal specific payment endpoints

    @PostMapping("/job-posting")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processJobPostingPayment(
            @RequestParam Long userId,
            @RequestParam Long jobId,
            @RequestParam String jobTitle,
            @RequestParam Double amount) {
        PaymentResponse response = paymentService.processJobPostingPayment(userId, jobId, jobTitle, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/premium-job-posting")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processPremiumJobPostingPayment(
            @RequestParam Long userId,
            @RequestParam Long jobId,
            @RequestParam String jobTitle,
            @RequestParam Double amount) {
        PaymentResponse response = paymentService.processPremiumJobPostingPayment(userId, jobId, jobTitle, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/featured-profile")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER')")
    public ResponseEntity<PaymentResponse> processFeaturedProfilePayment(
            @RequestParam Long userId,
            @RequestParam Long profileId,
            @RequestParam Double amount) {
        PaymentResponse response = paymentService.processFeaturedProfilePayment(userId, profileId, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/resume-access")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processResumeAccessPayment(
            @RequestParam Long userId,
            @RequestParam Long resumeId,
            @RequestParam Double amount) {
        PaymentResponse response = paymentService.processResumeAccessPayment(userId, resumeId, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/interview-scheduling")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processInterviewSchedulingPayment(
            @RequestParam Long userId,
            @RequestParam Long applicationId,
            @RequestParam Double amount) {
        PaymentResponse response = paymentService.processInterviewSchedulingPayment(userId, applicationId, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/recruiter-verification")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<PaymentResponse> processRecruiterVerificationPayment(
            @RequestParam Long userId,
            @RequestParam Double amount) {
        PaymentResponse response = paymentService.processRecruiterVerificationPayment(userId, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/premium-subscription")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER')")
    public ResponseEntity<PaymentResponse> processPremiumSubscriptionPayment(
            @RequestParam Long userId,
            @RequestParam String subscriptionType,
            @RequestParam Double amount) {
        PaymentResponse response = paymentService.processPremiumSubscriptionPayment(userId, subscriptionType, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/advertising")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processAdvertisingPayment(
            @RequestParam Long userId,
            @RequestParam String adType,
            @RequestParam Double amount) {
        PaymentResponse response = paymentService.processAdvertisingPayment(userId, adType, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Payment analytics and reporting

    @GetMapping("/revenue/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getTotalRevenueByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        Double revenue = paymentService.getTotalRevenueByDateRange(startDate, endDate);
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }

    @GetMapping("/revenue/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getTotalRevenueByUser(@PathVariable Long userId) {
        Double revenue = paymentService.getTotalRevenueByUser(userId);
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }

    @GetMapping("/successful-count/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getSuccessfulPaymentCountByUser(@PathVariable Long userId) {
        Long count = paymentService.getSuccessfulPaymentCountByUser(userId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/recent/{days}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> getRecentPayments(@PathVariable int days) {
        List<Payment> payments = paymentService.getRecentPayments(days);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/amount-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Payment>> getPaymentsByAmountRange(
            @RequestParam Double minAmount,
            @RequestParam Double maxAmount) {
        List<Payment> payments = paymentService.getPaymentsByAmountRange(minAmount, maxAmount);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // Payment validation

    @GetMapping("/validate/{transactionId}/successful")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> isPaymentSuccessful(@PathVariable String transactionId) {
        boolean isSuccessful = paymentService.isPaymentSuccessful(transactionId);
        return new ResponseEntity<>(isSuccessful, HttpStatus.OK);
    }

    @GetMapping("/validate/{transactionId}/pending")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> isPaymentPending(@PathVariable String transactionId) {
        boolean isPending = paymentService.isPaymentPending(transactionId);
        return new ResponseEntity<>(isPending, HttpStatus.OK);
    }

    @GetMapping("/validate/{transactionId}/failed")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> isPaymentFailed(@PathVariable String transactionId) {
        boolean isFailed = paymentService.isPaymentFailed(transactionId);
        return new ResponseEntity<>(isFailed, HttpStatus.OK);
    }

    // Payment cleanup and maintenance

    @PostMapping("/cleanup/expired")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> cleanupExpiredPayments() {
        paymentService.cleanupExpiredPayments();
        return new ResponseEntity<>(
            new ApiResponseMessage("Expired payments cleaned up successfully", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    @PostMapping("/cleanup/failed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> cleanupFailedPayments(
            @RequestParam(defaultValue = "30") int daysOld) {
        paymentService.cleanupFailedPayments(daysOld);
        return new ResponseEntity<>(
            new ApiResponseMessage("Failed payments cleaned up successfully", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    @PostMapping("/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> archiveOldPayments(
            @RequestParam(defaultValue = "365") int daysOld) {
        paymentService.archiveOldPayments(daysOld);
        return new ResponseEntity<>(
            new ApiResponseMessage("Old payments archived successfully", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Payment webhook handling

    @PostMapping("/webhook")
    public ResponseEntity<PaymentResponse> handlePaymentWebhook(
            @RequestParam String gatewayTransactionId,
            @RequestParam String status,
            @RequestParam String response) {
        PaymentResponse webhookResponse = paymentService.handlePaymentWebhook(gatewayTransactionId, status, response);
        return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
    }

    // Payment preferences and settings

    @GetMapping("/methods/available")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<List<String>> getAvailablePaymentMethods() {
        List<String> methods = paymentService.getAvailablePaymentMethods();
        return new ResponseEntity<>(methods, HttpStatus.OK);
    }

    @GetMapping("/methods/{method}/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> isPaymentMethodEnabled(@PathVariable String method) {
        boolean isEnabled = paymentService.isPaymentMethodEnabled(method);
        return new ResponseEntity<>(isEnabled, HttpStatus.OK);
    }

    @PutMapping("/methods/{method}/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> updatePaymentMethodSettings(
            @PathVariable String method,
            @RequestParam boolean enabled) {
        paymentService.updatePaymentMethodSettings(method, enabled);
        return new ResponseEntity<>(
            new ApiResponseMessage("Payment method settings updated successfully", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }
}
