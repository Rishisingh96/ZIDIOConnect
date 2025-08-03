package com.rishi.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.rishi.entity.Notification;
import com.rishi.response.PageableResponse;

public interface NotificationService {
    
    // Basic notification operations
    Notification createNotification(Long userId, String title, String message, Notification.NotificationType type);
    Notification createNotification(Long userId, String title, String message, Notification.NotificationType type, Long relatedEntityId, String relatedEntityType);
    Notification getNotificationById(Long notificationId);
    List<Notification> getNotificationsByUserId(Long userId);
    PageableResponse<Notification> getNotificationsByUserId(Long userId, Pageable pageable);
    List<Notification> getUnreadNotificationsByUserId(Long userId);
    Long getUnreadNotificationCount(Long userId);
    
    // Mark notifications as read
    void markNotificationAsRead(Long notificationId);
    void markAllNotificationsAsRead(Long userId);
    
    // Delete notifications
    void deleteNotification(Long notificationId);
    void deleteOldNotifications(int daysOld);
    
    // Job portal specific notifications
    void sendJobApplicationNotification(Long userId, Long jobId, String jobTitle);
    void sendApplicationStatusUpdateNotification(Long userId, Long applicationId, String status, String jobTitle);
    void sendInterviewScheduledNotification(Long userId, Long applicationId, String jobTitle, String interviewDate);
    void sendInterviewReminderNotification(Long userId, Long applicationId, String jobTitle, String interviewDate);
    void sendNewJobPostingNotification(Long userId, Long jobId, String jobTitle, String companyName);
    void sendRecruiterMessageNotification(Long userId, Long recruiterId, String message);
    void sendProfileUpdateNotification(Long userId, String updateType);
    void sendSystemMessageNotification(Long userId, String title, String message);
    
    // Bulk notifications
    void sendBulkNotification(List<Long> userIds, String title, String message, Notification.NotificationType type);
    void sendNotificationToAllUsers(String title, String message, Notification.NotificationType type);
    
    // Notification preferences (future enhancement)
    boolean isNotificationEnabled(Long userId, Notification.NotificationType type);
    void updateNotificationPreferences(Long userId, Notification.NotificationType type, boolean enabled);
} 