package com.rishi.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.dto.RecruiterProfileDTO;
import com.rishi.service.RecruiterProfileService;


import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruiter/profile")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    

    @PostMapping("/update")
    public ResponseEntity<RecruiterProfileDTO> update(@RequestBody RecruiterProfileDTO dto, Principal principal) {
        RecruiterProfileDTO saved = recruiterProfileService.createOrUpdate(dto, principal.getName());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<RecruiterProfileDTO> get(Principal principal) {
        RecruiterProfileDTO profile = recruiterProfileService.getRecruiterByEmail(principal.getName());
        return ResponseEntity.ok(profile);
    }
} 
