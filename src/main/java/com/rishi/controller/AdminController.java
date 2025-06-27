package com.rishi.controller;

import com.rishi.dto.JobApplicationDTO;
import com.rishi.dto.JobListingDTO;
import com.rishi.dto.RecruiterProfileDTO;
import com.rishi.dto.UserDTO;
import com.rishi.service.AdminProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminProfileService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/recruiters")
    public ResponseEntity<List<RecruiterProfileDTO>> getAllRecruiters() {
        return ResponseEntity.ok(adminService.getAllRecruiters());
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobListingDTO>> getAllJobs() {
        return ResponseEntity.ok(adminService.getAllJobs());
    }

    @GetMapping("/applications")
    public ResponseEntity<List<JobApplicationDTO>> getAllApplications() {
        return ResponseEntity.ok(adminService.getAllApplications());
    }
}
