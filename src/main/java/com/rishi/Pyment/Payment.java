package com.rishi.Pyment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_purpose", nullable = false)
    private PaymentPurpose purpose;

    @Column(name = "payment_method")
    private String paymentMethod; // "CREDIT_CARD", "DEBIT_CARD", "BANK_TRANSFER", "PAYPAL", "STRIPE"

    @Column(name = "gateway_response")
    private String gatewayResponse;

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "related_entity_id")
    private Long relatedEntityId; // Job ID, Profile ID, etc.

    @Column(name = "related_entity_type")
    private String relatedEntityType; // "JOB_POSTING", "PROFILE_FEATURE", "PREMIUM_SUBSCRIPTION"

    @Column(name = "description")
    private String description;

    @Column(name = "failure_reason")
    private String failureReason;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum PaymentPurpose {
        JOB_POSTING("Job Posting"),
        PREMIUM_JOB_POSTING("Premium Job Posting"),
        FEATURED_PROFILE("Featured Profile"),
        RESUME_ACCESS("Resume Access"),
        INTERVIEW_SCHEDULING("Interview Scheduling"),
        RECRUITER_VERIFICATION("Recruiter Verification"),
        PREMIUM_SUBSCRIPTION("Premium Subscription"),
        ADVERTISING("Advertising"),
        CUSTOM_SERVICE("Custom Service");

        private final String displayName;

        PaymentPurpose(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
