package com.rishi.service.impl;

import com.rishi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // ✅ Removed hardcoded 'from' email to avoid domain rejection
    // private final String FROM = "no-reply@zidioconnect.com"; ❌ Not allowed

    // 🔁 Central method to send email
    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // ❌ DO NOT SET 'FROM' manually unless it's verified
            // message.setFrom(FROM);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    // ✅ Generic mail send
    @Override
    public void sendSimpleMail(String to, String subject, String body) {
        sendEmail(to, subject, body);
    }

    // ✅ Job Application Confirmation to User
    @Override
    public void sendJobApplicationConfirmation(String userEmail, String jobTitle) {
        String subject = "📩 Application Received for " + jobTitle;
        String body = "Dear Candidate,\n\nThank you for applying to '" + jobTitle + "'.\nWe will get back to you shortly.\n\nRegards,\nZidioConnect Team";
        sendEmail(userEmail, subject, body);
    }

    // ✅ Job Posted Notification to Recruiter
    @Override
    public void sendJobPostedNotification(String recruiterEmail, String jobTitle) {
        String subject = "📢 Job Successfully Posted: " + jobTitle;
        String body = "Hi Recruiter,\n\nYour job '" + jobTitle + "' has been posted successfully.\n\nZidioConnect Team";
        sendEmail(recruiterEmail, subject, body);
    }

    // ✅ Application Status Update Notification
    @Override
    public void sendApplicationStatusUpdate(String userEmail, String status) {
        String subject = "📬 Application Status Updated";
        String body = "Hi,\n\nYour application status has been updated to: " + status + ".\n\nZidioConnect Team";
        sendEmail(userEmail, subject, body);
    }
}
