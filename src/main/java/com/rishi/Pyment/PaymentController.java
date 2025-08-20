package com.rishi.Pyment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.dto.PaymentDTO;
import com.rishi.request.PaymentRequest;
import com.rishi.response.PageableResponse;
import com.rishi.response.PaymentResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // ==================== TEST ENDPOINTS ====================
    
    // Test endpoint to verify payment service is working
    @GetMapping("/test")
    public ResponseEntity<String> testPaymentService() {
        return new ResponseEntity<>("Payment Service is working! Razorpay configured successfully.", HttpStatus.OK);
    }

    // Test endpoint to create sample payment data for testing
    @PostMapping("/test/create-sample")
    public ResponseEntity<PaymentResponse> createSamplePayment() {
        try {
            PaymentRequest sampleRequest = new PaymentRequest();
            sampleRequest.setUserId(1L);
            sampleRequest.setAmount(500.0);
            sampleRequest.setCurrency("INR");
            sampleRequest.setPurpose(Payment.PaymentPurpose.JOB_POSTING);
            sampleRequest.setPaymentMethod("RAZORPAY");
            sampleRequest.setRelatedEntityId(123L);
            sampleRequest.setRelatedEntityType("JOB");
            sampleRequest.setDescription("Test job posting payment for Postman testing");
            
            PaymentResponse response = paymentService.processPayment(sampleRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error creating sample payment: {}", e.getMessage());
            return new ResponseEntity<>(
                new PaymentResponse(null, PaymentStatus.FAILED, "Sample creation failed", e.getMessage(), false),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Test endpoint to get sample payment request JSON
    @GetMapping("/test/sample-request")
    public ResponseEntity<PaymentRequest> getSamplePaymentRequest() {
        PaymentRequest sample = new PaymentRequest();
        sample.setUserId(1L);
        sample.setAmount(1000.0);
        sample.setCurrency("INR");
        sample.setPurpose(Payment.PaymentPurpose.PREMIUM_JOB_POSTING);
        sample.setPaymentMethod("RAZORPAY");
        sample.setRelatedEntityId(456L);
        sample.setRelatedEntityType("JOB");
        sample.setDescription("Sample premium job posting payment");
        
        return new ResponseEntity<>(sample, HttpStatus.OK);
    }

    // Test endpoint to validate payment request without authentication
    @PostMapping("/test/validate-request")
    public ResponseEntity<String> testValidatePaymentRequest(@RequestBody PaymentRequest paymentRequest) {
        boolean isValid = paymentService.isValidPaymentRequest(paymentRequest);
        String message = isValid ? 
            "Payment request is VALID" : 
            "Payment request is INVALID - Check required fields";
        
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // ==================== CORE PAYMENT OPERATIONS ====================

    @PostMapping("/process")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("Processing payment request: {}", paymentRequest);
        PaymentResponse response = paymentService.processPayment(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/process/razorpay")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> processRazorpayPayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("Processing Razorpay payment request: {}", paymentRequest);
        PaymentResponse response = paymentService.processPaymentWithRazorpay(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==================== ESSENTIAL PAYMENT RETRIEVAL ====================

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

    // ==================== PAYMENT STATUS MANAGEMENT ====================

    @PutMapping("/verify/{transactionId}")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> verifyPaymentStatus(@PathVariable String transactionId) {
        PaymentResponse response = paymentService.verifyPaymentStatus(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Razorpay capture/verify webhook endpoint
    @PostMapping("/razorpay/verify")
    public ResponseEntity<PaymentResponse> verifyRazorpayPayment(
            @RequestParam String transactionId,
            @RequestParam String razorpayPaymentId,
            @RequestParam(required = false) String razorpayOrderId,
            @RequestParam(required = false) String razorpaySignature) {
        PaymentResponse response = paymentService.updatePaymentStatus(transactionId, PaymentStatus.CAPTURED);
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

    // ==================== REFUND OPERATIONS ====================

    @PostMapping("/refund/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable String transactionId,
            @RequestParam Double amount,
            @RequestParam String reason) {
        PaymentResponse response = paymentService.refundPayment(transactionId, amount, reason);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ==================== JOB PORTAL SPECIFIC PAYMENTS ====================

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

    // ==================== PAYMENT VALIDATION ====================

    @GetMapping("/validate/{transactionId}/successful")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> isPaymentSuccessful(@PathVariable String transactionId) {
        boolean isSuccessful = paymentService.isPaymentSuccessful(transactionId);
        return new ResponseEntity<>(isSuccessful, HttpStatus.OK);
    }

    // ==================== PAYMENT WEBHOOK HANDLING ====================

    @PostMapping("/webhook")
    public ResponseEntity<PaymentResponse> handlePaymentWebhook(
            @RequestParam String gatewayTransactionId,
            @RequestParam String status,
            @RequestParam String response) {
        PaymentResponse webhookResponse = paymentService.handlePaymentWebhook(gatewayTransactionId, status, response);
        return new ResponseEntity<>(webhookResponse, HttpStatus.OK);
    }
}
