package com.rishi.controller;

import com.rishi.dto.RecruiterProfileDTO;
import com.rishi.entity.RecruiterProfile;
import com.rishi.service.RecruiterProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/profile")
public class RecruiterProfileController {

    @Autowired
    private RecruiterProfileService recruiterProfileService;

    @GetMapping
    public ResponseEntity<RecruiterProfileDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        RecruiterProfile profile = recruiterProfileService.getProfileByEmail(email);
        RecruiterProfileDTO dto = new RecruiterProfileDTO();
        dto.setFullName(profile.getFullName());
        dto.setCompanyName(profile.getCompanyName());
        dto.setDesignation(profile.getDesignation());
        dto.setCompanyWebsite(profile.getCompanyWebsite());
        dto.setPhone(profile.getPhone());
        dto.setLocation(profile.getLocation());
        dto.setEmail(profile.getEmail());
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<RecruiterProfile> updateProfile(Authentication authentication, @RequestBody RecruiterProfile profileData) {
        String email = authentication.getName();
        RecruiterProfile updated = recruiterProfileService.updateProfile(email, profileData);
        return ResponseEntity.ok(updated);
    }
} 