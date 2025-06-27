package com.rishi.controller;

import com.rishi.request.LoginRequest;
import com.rishi.request.SignupRequest;
import com.rishi.response.AuthResponse;
import com.rishi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rishi.dto.UserProfileDTO;
import com.rishi.service.UserProfileService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class UserProfileController {


    private final UserProfileService userProfileService;


    @GetMapping
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileDTO profileDTO = userProfileService.getProfileByEmail(email);
        return ResponseEntity.ok(profileDTO);
    }

    @PostMapping("/update")
    public ResponseEntity<UserProfileDTO> update(@RequestBody UserProfileDTO dto, Principal principal) {
        UserProfileDTO saved = userProfileService.createOrUpdateProfile(dto, principal.getName());
        return ResponseEntity.ok(saved);
    }


} 