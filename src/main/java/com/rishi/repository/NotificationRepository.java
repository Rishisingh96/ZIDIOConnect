package com.rishi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rishi.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find notifications by user ID
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Find notifications by user ID with pagination
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    // Find unread notifications by user ID
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    // Count unread notifications by user ID
    Long countByUserIdAndIsReadFalse(Long userId);
    
    // Find notifications by user ID and type
    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, Notification.NotificationType type);
    
    // Find notifications by user ID and related entity
    List<Notification> findByUserIdAndRelatedEntityIdAndRelatedEntityTypeOrderByCreatedAtDesc(
            Long userId, Long relatedEntityId, String relatedEntityType);
    
    // Find notifications created after a specific date
    List<Notification> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(Long userId, LocalDateTime after);
    
    // Find notifications by type
    List<Notification> findByTypeOrderByCreatedAtDesc(Notification.NotificationType type);
    
    // Mark notifications as read by user ID
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.userId = :userId AND n.isRead = false")
    void markAllAsReadByUserId(@Param("userId") Long userId, @Param("readAt") LocalDateTime readAt);
    
    // Mark specific notification as read
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.id = :notificationId")
    void markAsRead(@Param("notificationId") Long notificationId, @Param("readAt") LocalDateTime readAt);
    
    // Delete old notifications (cleanup)
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :beforeDate")
    void deleteOldNotifications(@Param("beforeDate") LocalDateTime beforeDate);
    
    // Find notifications for job applications
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.type = 'JOB_APPLICATION' ORDER BY n.createdAt DESC")
    List<Notification> findJobApplicationNotifications(@Param("userId") Long userId);
    
    // Find notifications for interview updates
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.type IN ('INTERVIEW_SCHEDULED', 'INTERVIEW_REMINDER') ORDER BY n.createdAt DESC")
    List<Notification> findInterviewNotifications(@Param("userId") Long userId);
}
