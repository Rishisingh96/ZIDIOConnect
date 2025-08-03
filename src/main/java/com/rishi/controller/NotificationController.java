package com.rishi.controller;

import com.rishi.dto.NotificationDTO;
import com.rishi.entity.Notification;
import com.rishi.request.NotificationRequest;
import com.rishi.response.ApiResponseMessage;
import com.rishi.response.PageableResponse;
import com.rishi.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get all notifications for a user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<PageableResponse<Notification>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        PageableResponse<Notification> response = notificationService.getNotificationsByUserId(userId, pageable);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get unread notifications for a user
    @GetMapping("/user/{userId}/unread")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotificationsByUserId(userId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    // Get unread notification count
    @GetMapping("/user/{userId}/unread-count")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Long> getUnreadNotificationCount(@PathVariable Long userId) {
        Long count = notificationService.getUnreadNotificationCount(userId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Mark notification as read
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return new ResponseEntity<>(
            new ApiResponseMessage("Notification marked as read", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Mark all notifications as read for a user
    @PutMapping("/user/{userId}/read-all")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> markAllNotificationsAsRead(@PathVariable Long userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return new ResponseEntity<>(
            new ApiResponseMessage("All notifications marked as read", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Delete a notification
    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return new ResponseEntity<>(
            new ApiResponseMessage("Notification deleted successfully", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Admin endpoints for system notifications

    // Send notification to specific users
    @PostMapping("/send-bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> sendBulkNotification(
            @RequestParam List<Long> userIds,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam Notification.NotificationType type) {
        
        notificationService.sendBulkNotification(userIds, title, message, type);
        return new ResponseEntity<>(
            new ApiResponseMessage("Bulk notification sent successfully", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Send notification to all users
    @PostMapping("/send-to-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> sendNotificationToAllUsers(
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam Notification.NotificationType type) {
        
        notificationService.sendNotificationToAllUsers(title, message, type);
        return new ResponseEntity<>(
            new ApiResponseMessage("Notification sent to all users successfully", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Clean up old notifications
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> cleanupOldNotifications(
            @RequestParam(defaultValue = "30") int daysOld) {
        
        notificationService.deleteOldNotifications(daysOld);
        return new ResponseEntity<>(
            new ApiResponseMessage("Old notifications cleaned up successfully", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Job portal specific notification endpoints

    // Send job application notification
    @PostMapping("/job-application")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponseMessage> sendJobApplicationNotification(
            @RequestParam Long userId,
            @RequestParam Long jobId,
            @RequestParam String jobTitle) {
        
        notificationService.sendJobApplicationNotification(userId, jobId, jobTitle);
        return new ResponseEntity<>(
            new ApiResponseMessage("Job application notification sent", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Send application status update notification
    @PostMapping("/application-status")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> sendApplicationStatusUpdateNotification(
            @RequestParam Long userId,
            @RequestParam Long applicationId,
            @RequestParam String status,
            @RequestParam String jobTitle) {
        
        notificationService.sendApplicationStatusUpdateNotification(userId, applicationId, status, jobTitle);
        return new ResponseEntity<>(
            new ApiResponseMessage("Application status notification sent", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Send interview scheduled notification
    @PostMapping("/interview-scheduled")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> sendInterviewScheduledNotification(
            @RequestParam Long userId,
            @RequestParam Long applicationId,
            @RequestParam String jobTitle,
            @RequestParam String interviewDate) {
        
        notificationService.sendInterviewScheduledNotification(userId, applicationId, jobTitle, interviewDate);
        return new ResponseEntity<>(
            new ApiResponseMessage("Interview scheduled notification sent", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Send interview reminder notification
    @PostMapping("/interview-reminder")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> sendInterviewReminderNotification(
            @RequestParam Long userId,
            @RequestParam Long applicationId,
            @RequestParam String jobTitle,
            @RequestParam String interviewDate) {
        
        notificationService.sendInterviewReminderNotification(userId, applicationId, jobTitle, interviewDate);
        return new ResponseEntity<>(
            new ApiResponseMessage("Interview reminder notification sent", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Send new job posting notification
    @PostMapping("/new-job-posting")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> sendNewJobPostingNotification(
            @RequestParam Long userId,
            @RequestParam Long jobId,
            @RequestParam String jobTitle,
            @RequestParam String companyName) {
        
        notificationService.sendNewJobPostingNotification(userId, jobId, jobTitle, companyName);
        return new ResponseEntity<>(
            new ApiResponseMessage("New job posting notification sent", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Send recruiter message notification
    @PostMapping("/recruiter-message")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponseMessage> sendRecruiterMessageNotification(
            @RequestParam Long userId,
            @RequestParam Long recruiterId,
            @RequestParam String message) {
        
        notificationService.sendRecruiterMessageNotification(userId, recruiterId, message);
        return new ResponseEntity<>(
            new ApiResponseMessage("Recruiter message notification sent", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Send profile update notification
    @PostMapping("/profile-update")
    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER')")
    public ResponseEntity<ApiResponseMessage> sendProfileUpdateNotification(
            @RequestParam Long userId,
            @RequestParam String updateType) {
        
        notificationService.sendProfileUpdateNotification(userId, updateType);
        return new ResponseEntity<>(
            new ApiResponseMessage("Profile update notification sent", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }

    // Send system message notification
    @PostMapping("/system-message")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseMessage> sendSystemMessageNotification(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String message) {
        
        notificationService.sendSystemMessageNotification(userId, title, message);
        return new ResponseEntity<>(
            new ApiResponseMessage("System message notification sent", null, true, HttpStatus.OK), 
            HttpStatus.OK
        );
    }
}
