package com.rishi.service;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String body);
    void sendJobApplicationConfirmation(String userEmail, String jobTitle);
    void sendJobPostedNotification(String recruiterEmail, String jobTitle);
    void sendApplicationStatusUpdate(String userEmail, String status);
} 