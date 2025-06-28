package com.rishi.controller;

import com.rishi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @PostMapping("/send-welcome")
    public ResponseEntity<String> sendWelcome(@RequestBody Map<String, String> request) {
        emailService.sendSimpleMail(request.get("to"), "Welcome to ZidioConnect", "Thanks for signing up, " + request.get("name"));
        return ResponseEntity.ok("✅ Welcome Email Sent");
    }

    @PostMapping("/job-posted")
    public ResponseEntity<String> jobPosted(@RequestBody Map<String, String> request) {
        emailService.sendJobPostedNotification(request.get("to"), request.get("jobTitle"));
        return ResponseEntity.ok("✅ Job Posted Email Sent");
    }

    @PostMapping("/application-confirm")
    public ResponseEntity<String> applicationConfirm(@RequestBody Map<String, String> request) {
        emailService.sendJobApplicationConfirmation(request.get("to"), request.get("jobTitle"));
        return ResponseEntity.ok("✅ Application Confirmation Email Sent");
    }

    @PostMapping("/status-update")
    public ResponseEntity<String> statusUpdate(@RequestBody Map<String, String> request) {
        emailService.sendApplicationStatusUpdate(request.get("to"), request.get("status"));
        return ResponseEntity.ok("✅ Status Update Email Sent");
    }
}
