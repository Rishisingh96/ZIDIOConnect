package com.rishi.service.impl;

import com.rishi.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body) {
        // TODO: Implement email sending logic
    }
} 