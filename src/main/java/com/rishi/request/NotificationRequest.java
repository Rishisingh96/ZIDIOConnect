package com.rishi.request;

import com.rishi.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    
    private Long userId;
    private String title;
    private String message;
    private Notification.NotificationType type;
    private Long relatedEntityId;
    private String relatedEntityType;
} 