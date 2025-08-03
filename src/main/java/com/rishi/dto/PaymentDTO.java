package com.rishi.dto;

import com.rishi.Pyment.Payment;
import com.rishi.Pyment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    
    private Long id;
    private Long userId;
    private String transactionId;
    private Double amount;
    private String currency;
    private PaymentStatus status;
    private Payment.PaymentPurpose purpose;
    private String paymentMethod;
    private String gatewayResponse;
    private String gatewayTransactionId;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long relatedEntityId;
    private String relatedEntityType;
    private String description;
    private String failureReason;
    
    // Constructor to convert from Payment entity
    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.userId = payment.getUserId();
        this.transactionId = payment.getTransactionId();
        this.amount = payment.getAmount();
        this.currency = payment.getCurrency();
        this.status = payment.getStatus();
        this.purpose = payment.getPurpose();
        this.paymentMethod = payment.getPaymentMethod();
        this.gatewayResponse = payment.getGatewayResponse();
        this.gatewayTransactionId = payment.getGatewayTransactionId();
        this.paymentDate = payment.getPaymentDate();
        this.createdAt = payment.getCreatedAt();
        this.updatedAt = payment.getUpdatedAt();
        this.relatedEntityId = payment.getRelatedEntityId();
        this.relatedEntityType = payment.getRelatedEntityType();
        this.description = payment.getDescription();
        this.failureReason = payment.getFailureReason();
    }
} 