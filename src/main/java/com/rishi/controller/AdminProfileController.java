package com.rishi.controller;

import com.rishi.service.AdminProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/profile")
public class AdminProfileController {

    @Autowired
    private AdminProfileService adminProfileService;

    @GetMapping
    public ResponseEntity<String> getProfile(Authentication authentication) {
        String email = authentication.getName();
        String profile = adminProfileService.getProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<String> updateProfile(Authentication authentication, @RequestBody String profileData) {
        String email = authentication.getName();
        String updated = adminProfileService.updateProfile(email, profileData);
        return ResponseEntity.ok(updated);
    }
} 