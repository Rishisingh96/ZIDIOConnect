# Zidio Connect Notification Service API Documentation

## Overview

The Notification Service provides comprehensive notification management for the Zidio Connect job portal application. It supports various types of notifications including job applications, interview scheduling, status updates, and system messages.

## Base URL

```
http://localhost:8080/api/v1/notifications
```

## Authentication

All endpoints require authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Notification Types

```json
{
  "JOB_APPLICATION": "Job application submitted",
  "APPLICATION_STATUS_UPDATE": "Application status changed",
  "NEW_JOB_POSTING": "New job posting available",
  "INTERVIEW_SCHEDULED": "Interview scheduled",
  "INTERVIEW_REMINDER": "Interview reminder",
  "PROFILE_UPDATE": "Profile updated",
  "SYSTEM_MESSAGE": "System message",
  "RECRUITER_MESSAGE": "Message from recruiter"
}
```

## API Endpoints

### 1. Get User Notifications

**GET** `/user/{userId}`

**Parameters:**

- `page` (default: 0) - Page number
- `size` (default: 10) - Page size
- `sortBy` (default: "createdAt") - Sort field
- `sortDir` (default: "desc") - Sort direction

**Response:**

```json
{
  "content": [
    {
      "id": 1,
      "userId": 123,
      "title": "Job Application Submitted",
      "message": "Your application for 'Senior Java Developer' has been submitted successfully.",
      "type": "JOB_APPLICATION",
      "isRead": false,
      "createdAt": "2024-01-15T10:30:00",
      "readAt": null,
      "relatedEntityId": 456,
      "relatedEntityType": "JOB"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 25,
  "totalPages": 3,
  "lastPage": false
}
```

### 2. Get Unread Notifications

**GET** `/user/{userId}/unread`

**Response:**

```json
[
  {
    "id": 1,
    "userId": 123,
    "title": "Interview Scheduled",
    "message": "Great news! An interview has been scheduled for your application to 'Senior Java Developer' on 2024-01-20 at 2:00 PM.",
    "type": "INTERVIEW_SCHEDULED",
    "isRead": false,
    "createdAt": "2024-01-15T14:30:00",
    "readAt": null,
    "relatedEntityId": 789,
    "relatedEntityType": "JOB_APPLICATION"
  }
]
```

### 3. Get Unread Notification Count

**GET** `/user/{userId}/unread-count`

**Response:**

```json
5
```

### 4. Mark Notification as Read

**PUT** `/notifications/{notificationId}/read`

**Response:**

```json
{
  "message": "Notification marked as read",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 5. Mark All Notifications as Read

**PUT** `/user/{userId}/read-all`

**Response:**

```json
{
  "message": "All notifications marked as read",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 6. Delete Notification

**DELETE** `/notifications/{notificationId}`

**Response:**

```json
{
  "message": "Notification deleted successfully",
  "role": null,
  "success": true,
  "status": "OK"
}
```

## Job Portal Specific Endpoints

### 7. Send Job Application Notification

**POST** `/job-application`

**Parameters:**

- `userId` - User ID
- `jobId` - Job ID
- `jobTitle` - Job title

**Response:**

```json
{
  "message": "Job application notification sent",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 8. Send Application Status Update

**POST** `/application-status`

**Parameters:**

- `userId` - User ID
- `applicationId` - Application ID
- `status` - New status
- `jobTitle` - Job title

**Response:**

```json
{
  "message": "Application status notification sent",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 9. Send Interview Scheduled Notification

**POST** `/interview-scheduled`

**Parameters:**

- `userId` - User ID
- `applicationId` - Application ID
- `jobTitle` - Job title
- `interviewDate` - Interview date and time

**Response:**

```json
{
  "message": "Interview scheduled notification sent",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 10. Send Interview Reminder

**POST** `/interview-reminder`

**Parameters:**

- `userId` - User ID
- `applicationId` - Application ID
- `jobTitle` - Job title
- `interviewDate` - Interview date and time

**Response:**

```json
{
  "message": "Interview reminder notification sent",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 11. Send New Job Posting Notification

**POST** `/new-job-posting`

**Parameters:**

- `userId` - User ID
- `jobId` - Job ID
- `jobTitle` - Job title
- `companyName` - Company name

**Response:**

```json
{
  "message": "New job posting notification sent",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 12. Send Recruiter Message

**POST** `/recruiter-message`

**Parameters:**

- `userId` - User ID
- `recruiterId` - Recruiter ID
- `message` - Message content

**Response:**

```json
{
  "message": "Recruiter message notification sent",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 13. Send Profile Update Notification

**POST** `/profile-update`

**Parameters:**

- `userId` - User ID
- `updateType` - Type of update (e.g., "resume", "skills", "experience")

**Response:**

```json
{
  "message": "Profile update notification sent",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 14. Send System Message

**POST** `/system-message`

**Parameters:**

- `userId` - User ID
- `title` - Message title
- `message` - Message content

**Response:**

```json
{
  "message": "System message notification sent",
  "role": null,
  "success": true,
  "status": "OK"
}
```

## Admin Endpoints

### 15. Send Bulk Notification

**POST** `/send-bulk`

**Parameters:**

- `userIds` - List of user IDs
- `title` - Notification title
- `message` - Notification message
- `type` - Notification type

**Response:**

```json
{
  "message": "Bulk notification sent successfully",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 16. Send Notification to All Users

**POST** `/send-to-all`

**Parameters:**

- `title` - Notification title
- `message` - Notification message
- `type` - Notification type

**Response:**

```json
{
  "message": "Notification sent to all users successfully",
  "role": null,
  "success": true,
  "status": "OK"
}
```

### 17. Cleanup Old Notifications

**DELETE** `/cleanup`

**Parameters:**

- `daysOld` (default: 30) - Delete notifications older than this many days

**Response:**

```json
{
  "message": "Old notifications cleaned up successfully",
  "role": null,
  "success": true,
  "status": "OK"
}
```

## Usage Examples

### Example 1: Job Application Flow

```bash
# 1. User applies for a job
POST /api/v1/notifications/job-application?userId=123&jobId=456&jobTitle=Senior Java Developer

# 2. Recruiter updates application status
POST /api/v1/notifications/application-status?userId=123&applicationId=789&status=UNDER_REVIEW&jobTitle=Senior Java Developer

# 3. Interview is scheduled
POST /api/v1/notifications/interview-scheduled?userId=123&applicationId=789&jobTitle=Senior Java Developer&interviewDate=2024-01-20 14:00

# 4. Send interview reminder
POST /api/v1/notifications/interview-reminder?userId=123&applicationId=789&jobTitle=Senior Java Developer&interviewDate=2024-01-20 14:00
```

### Example 2: New Job Posting Notification

```bash
# Send notification to matching candidates
POST /api/v1/notifications/new-job-posting?userId=123&jobId=456&jobTitle=Full Stack Developer&companyName=Tech Corp
```

### Example 3: System Maintenance Notification

```bash
# Send maintenance notification to all users
POST /api/v1/notifications/send-to-all?title=System Maintenance&message=The system will be down for maintenance on Sunday from 2-4 AM&type=SYSTEM_MESSAGE
```

## Error Responses

### 404 - Notification Not Found

```json
{
  "message": "Notification not found with id: 999",
  "role": null,
  "success": false,
  "status": "NOT_FOUND"
}
```

### 403 - Access Denied

```json
{
  "message": "Access Denied",
  "role": null,
  "success": false,
  "status": "FORBIDDEN"
}
```

### 500 - Internal Server Error

```json
{
  "message": "Failed to create notification",
  "role": null,
  "success": false,
  "status": "INTERNAL_SERVER_ERROR"
}
```

## Integration with Other Services

The notification service can be integrated with other services in your application:

1. **Job Application Service**: Automatically send notifications when applications are submitted
2. **Interview Service**: Send notifications for interview scheduling and reminders
3. **Email Service**: Extend to send email notifications
4. **WebSocket Service**: Send real-time notifications to frontend
5. **SMS Service**: Send SMS notifications for critical updates

## Database Schema

The notification system uses the following database table:

```sql
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    related_entity_id BIGINT NULL,
    related_entity_type VARCHAR(50) NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_is_read (is_read)
);
```

## Best Practices

1. **Use appropriate notification types** for better categorization
2. **Include related entity information** for context
3. **Implement notification preferences** for user control
4. **Clean up old notifications** regularly
5. **Use pagination** for large notification lists
6. **Implement real-time notifications** using WebSockets
7. **Add notification templates** for consistent messaging
8. **Monitor notification delivery** and user engagement
