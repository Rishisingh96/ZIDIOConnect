package com.rishi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.dto.UserProfileDTO;
import com.rishi.service.UserProfileService;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileDTO profileDTO = userProfileService.getProfileByEmail(email);
        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateProfile(Authentication authentication, @RequestBody UserProfileDTO profileDTO) {
        String email = authentication.getName();
        UserProfileDTO updated = userProfileService.updateProfile(email, profileDTO);
        return ResponseEntity.ok(updated);
    }
} 