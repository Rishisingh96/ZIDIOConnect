package com.rishi.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rishi.entity.Notification;
import com.rishi.entity.User;
import com.rishi.repository.NotificationRepository;
import com.rishi.repository.UserRepository;
import com.rishi.response.PageableResponse;
import com.rishi.service.EmailService;
import com.rishi.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public Notification createNotification(Long userId, String title, String message, Notification.NotificationType type) {
        return createNotification(userId, title, message, type, null, null);
    }

    @Override
    public Notification createNotification(Long userId, String title, String message, Notification.NotificationType type, 
                                       Long relatedEntityId, String relatedEntityType) {
        try {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setType(type);
            notification.setIsRead(false);
            notification.setRelatedEntityId(relatedEntityId);
            notification.setRelatedEntityType(relatedEntityType);
            
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Notification created successfully for user {}: {}", userId, title);
            // Send email notification
            userRepository.findById(userId).ifPresent(user -> {
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    try {
                        emailService.sendSimpleMail(user.getEmail(), title, message);
                    } catch (Exception e) {
                        log.error("Failed to send email notification to {}: {}", user.getEmail(), e.getMessage());
                    }
                }
            });
            return savedNotification;
        } catch (Exception e) {
            log.error("Error creating notification for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to create notification", e);
        }
    }

    @Override
    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public PageableResponse<Notification> getNotificationsByUserId(Long userId, Pageable pageable) {
        Page<Notification> page = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        PageableResponse<Notification> response = new PageableResponse<>();
        response.setContent(page.getContent());
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        
        return response;
    }

    @Override
    public List<Notification> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    public Long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        try {
            notificationRepository.markAsRead(notificationId, LocalDateTime.now());
            log.info("Notification marked as read: {}", notificationId);
        } catch (Exception e) {
            log.error("Error marking notification as read: {}", e.getMessage());
            throw new RuntimeException("Failed to mark notification as read", e);
        }
    }

    @Override
    public void markAllNotificationsAsRead(Long userId) {
        try {
            notificationRepository.markAllAsReadByUserId(userId, LocalDateTime.now());
            log.info("All notifications marked as read for user: {}", userId);
        } catch (Exception e) {
            log.error("Error marking all notifications as read for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to mark all notifications as read", e);
        }
    }

    @Override
    public void deleteNotification(Long notificationId) {
        try {
            notificationRepository.deleteById(notificationId);
            log.info("Notification deleted: {}", notificationId);
        } catch (Exception e) {
            log.error("Error deleting notification: {}", e.getMessage());
            throw new RuntimeException("Failed to delete notification", e);
        }
    }

    @Override
    public void deleteOldNotifications(int daysOld) {
        try {
            LocalDateTime beforeDate = LocalDateTime.now().minusDays(daysOld);
            notificationRepository.deleteOldNotifications(beforeDate);
            log.info("Old notifications deleted (older than {} days)", daysOld);
        } catch (Exception e) {
            log.error("Error deleting old notifications: {}", e.getMessage());
            throw new RuntimeException("Failed to delete old notifications", e);
        }
    }

    // Job Portal Specific Notifications

    @Override
    public void sendJobApplicationNotification(Long userId, Long jobId, String jobTitle) {
        String title = "Job Application Submitted";
        String message = String.format("Your application for '%s' has been submitted successfully. We'll review your application and get back to you soon.", jobTitle);
        createNotification(userId, title, message, Notification.NotificationType.JOB_APPLICATION, jobId, "JOB");
    }

    @Override
    public void sendApplicationStatusUpdateNotification(Long userId, Long applicationId, String status, String jobTitle) {
        String title = "Application Status Updated";
        String message = String.format("Your application for '%s' status has been updated to: %s", jobTitle, status);
        createNotification(userId, title, message, Notification.NotificationType.APPLICATION_STATUS_UPDATE, applicationId, "JOB_APPLICATION");
    }

    @Override
    public void sendInterviewScheduledNotification(Long userId, Long applicationId, String jobTitle, String interviewDate) {
        String title = "Interview Scheduled";
        String message = String.format("Great news! An interview has been scheduled for your application to '%s' on %s. Please check your email for details.", jobTitle, interviewDate);
        createNotification(userId, title, message, Notification.NotificationType.INTERVIEW_SCHEDULED, applicationId, "JOB_APPLICATION");
    }

    @Override
    public void sendInterviewReminderNotification(Long userId, Long applicationId, String jobTitle, String interviewDate) {
        String title = "Interview Reminder";
        String message = String.format("Reminder: You have an interview for '%s' scheduled for %s. Good luck!", jobTitle, interviewDate);
        createNotification(userId, title, message, Notification.NotificationType.INTERVIEW_REMINDER, applicationId, "JOB_APPLICATION");
    }

    @Override
    public void sendNewJobPostingNotification(Long userId, Long jobId, String jobTitle, String companyName) {
        String title = "New Job Opportunity";
        String message = String.format("A new job posting '%s' at %s matches your profile. Apply now!", jobTitle, companyName);
        createNotification(userId, title, message, Notification.NotificationType.NEW_JOB_POSTING, jobId, "JOB");
    }

    @Override
    public void sendRecruiterMessageNotification(Long userId, Long recruiterId, String message) {
        String title = "New Message from Recruiter";
        createNotification(userId, title, message, Notification.NotificationType.RECRUITER_MESSAGE, recruiterId, "USER");
    }

    @Override
    public void sendProfileUpdateNotification(Long userId, String updateType) {
        String title = "Profile Updated";
        String message = String.format("Your %s has been updated successfully.", updateType);
        createNotification(userId, title, message, Notification.NotificationType.PROFILE_UPDATE);
    }

    @Override
    public void sendSystemMessageNotification(Long userId, String title, String message) {
        createNotification(userId, title, message, Notification.NotificationType.SYSTEM_MESSAGE);
    }

    // Bulk Notifications

    @Override
    public void sendBulkNotification(List<Long> userIds, String title, String message, Notification.NotificationType type) {
        try {
            for (Long userId : userIds) {
                createNotification(userId, title, message, type);
            }
            log.info("Bulk notification sent to {} users", userIds.size());
        } catch (Exception e) {
            log.error("Error sending bulk notification: {}", e.getMessage());
            throw new RuntimeException("Failed to send bulk notification", e);
        }
    }

    @Override
    public void sendNotificationToAllUsers(String title, String message, Notification.NotificationType type) {
        try {
            List<User> allUsers = userRepository.findAll();
            for (User user : allUsers) {
                createNotification(user.getId(), title, message, type);
            }
            log.info("System notification sent to all {} users", allUsers.size());
        } catch (Exception e) {
            log.error("Error sending notification to all users: {}", e.getMessage());
            throw new RuntimeException("Failed to send notification to all users", e);
        }
    }

    // Notification Preferences (Future Enhancement)

    @Override
    public boolean isNotificationEnabled(Long userId, Notification.NotificationType type) {
        // TODO: Implement notification preferences
        // This would check user preferences for specific notification types
        return true; // Default to enabled
    }

    @Override
    public void updateNotificationPreferences(Long userId, Notification.NotificationType type, boolean enabled) {
        // TODO: Implement notification preferences update
        // This would update user preferences for specific notification types
        log.info("Notification preference updated for user {}: {} = {}", userId, type, enabled);
    }
} 