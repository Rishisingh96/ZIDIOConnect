package com.rishi.response;

import com.rishi.Pyment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    
    private String transactionId;
    private PaymentStatus status;
    private String message;
    private String gatewayTransactionId;
    private String paymentUrl; // For redirect-based payments
    private LocalDateTime expiresAt; // For payment links
    private String failureReason;
    private boolean success;
    
    // Constructor for successful payment
    public PaymentResponse(String transactionId, PaymentStatus status, String message, String gatewayTransactionId) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.gatewayTransactionId = gatewayTransactionId;
        this.success = status.isSuccessful();
    }
    
    // Constructor for failed payment
    public PaymentResponse(String transactionId, PaymentStatus status, String message, String failureReason, boolean isFailed) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.failureReason = failureReason;
        this.success = false;
    }
    
    // Constructor for pending payment with URL
    public PaymentResponse(String transactionId, PaymentStatus status, String message, String paymentUrl, LocalDateTime expiresAt) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.paymentUrl = paymentUrl;
        this.expiresAt = expiresAt;
        this.success = false;
    }
} 