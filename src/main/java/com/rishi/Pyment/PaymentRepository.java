package com.rishi.Pyment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payments by user ID
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Find payments by user ID with pagination
    Page<Payment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // Find payments by status
    List<Payment> findByStatusOrderByCreatedAtDesc(PaymentStatus status);
    
    // Find payments by purpose
    List<Payment> findByPurposeOrderByCreatedAtDesc(Payment.PaymentPurpose purpose);
    
    // Find payments by user ID and status
    List<Payment> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, PaymentStatus status);
    
    // Find payments by user ID and purpose
    List<Payment> findByUserIdAndPurposeOrderByCreatedAtDesc(Long userId, Payment.PaymentPurpose purpose);
    
    // Find payment by transaction ID
    Optional<Payment> findByTransactionId(String transactionId);
    
    // Find payments by gateway transaction ID
    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);
    
    // Find successful payments by user ID
    @Query("SELECT p FROM Payment p WHERE p.userId = :userId AND p.status IN ('SUCCESS', 'CAPTURED') ORDER BY p.createdAt DESC")
    List<Payment> findSuccessfulPaymentsByUserId(@Param("userId") Long userId);
    
    // Find payments by date range
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<Payment> findPaymentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find payments by user ID and date range
    @Query("SELECT p FROM Payment p WHERE p.userId = :userId AND p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
    List<Payment> findPaymentsByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Count successful payments by user ID
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.userId = :userId AND p.status IN ('SUCCESS', 'CAPTURED')")
    Long countSuccessfulPaymentsByUserId(@Param("userId") Long userId);
    
    // Sum total amount of successful payments by user ID
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.userId = :userId AND p.status IN ('SUCCESS', 'CAPTURED')")
    Double sumSuccessfulPaymentsByUserId(@Param("userId") Long userId);
    
    // Find payments by related entity
    List<Payment> findByRelatedEntityIdAndRelatedEntityTypeOrderByCreatedAtDesc(Long relatedEntityId, String relatedEntityType);
    
    // Find payments by payment method
    List<Payment> findByPaymentMethodOrderByCreatedAtDesc(String paymentMethod);
    
    // Find pending payments
    @Query("SELECT p FROM Payment p WHERE p.status IN ('PENDING', 'PROCESSING', 'AUTHORIZED') ORDER BY p.createdAt ASC")
    List<Payment> findPendingPayments();
    
    // Find payments for job postings
    @Query("SELECT p FROM Payment p WHERE p.purpose IN ('JOB_POSTING', 'PREMIUM_JOB_POSTING') ORDER BY p.createdAt DESC")
    List<Payment> findJobPostingPayments();
    
    // Find payments for profile features
    @Query("SELECT p FROM Payment p WHERE p.purpose IN ('FEATURED_PROFILE', 'RESUME_ACCESS') ORDER BY p.createdAt DESC")
    List<Payment> findProfileFeaturePayments();
    
    // Find failed payments
    @Query("SELECT p FROM Payment p WHERE p.status IN ('FAILED', 'CANCELLED', 'DECLINED', 'EXPIRED') ORDER BY p.createdAt DESC")
    List<Payment> findFailedPayments();
    
    // Find payments by amount range
    @Query("SELECT p FROM Payment p WHERE p.amount BETWEEN :minAmount AND :maxAmount ORDER BY p.amount DESC")
    List<Payment> findPaymentsByAmountRange(@Param("minAmount") Double minAmount, @Param("maxAmount") Double maxAmount);
    
    // Find recent payments (last 30 days)
    @Query("SELECT p FROM Payment p WHERE p.createdAt >= :thirtyDaysAgo ORDER BY p.createdAt DESC")
    List<Payment> findRecentPayments(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
}
