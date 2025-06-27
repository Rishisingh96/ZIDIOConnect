package com.rishi.controller;

import com.rishi.dto.JobApplicationDTO;
import com.rishi.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<JobApplicationDTO> apply(@PathVariable Long jobId,
                                                   @RequestParam String resumeLink,
                                                   Principal principal) {
        JobApplicationDTO app = service.applyToJob(jobId, resumeLink, principal.getName());
        return ResponseEntity.ok(app);
    }

    @GetMapping("/my")
    public ResponseEntity<List<JobApplicationDTO>> myApplications(Principal principal) {
        return ResponseEntity.ok(service.getMyApplications(principal.getName()));
    }
}
