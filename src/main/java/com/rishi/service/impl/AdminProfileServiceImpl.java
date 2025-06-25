package com.rishi.service.impl;

import com.rishi.service.AdminProfileService;
import org.springframework.stereotype.Service;

@Service
public class AdminProfileServiceImpl implements AdminProfileService {
    @Override
    public String getProfileByEmail(String email) {
        // Dummy implementation
        return "Admin profile for: " + email;
    }

    @Override
    public String updateProfile(String email, String profileData) {
        // Dummy implementation
        return "Updated admin profile for: " + email;
    }
} 