package com.rishi.controller;

import com.rishi.dto.JobListingDTO;
import com.rishi.dto.SkillDTO;
import com.rishi.service.JobListingService;
import com.rishi.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobListingController {

    private final JobListingService jobService;
    private final SkillService skillService;

    @PostMapping("/post")
    public ResponseEntity<JobListingDTO> post(@RequestBody JobListingDTO dto, Principal principal) {
        JobListingDTO saved = jobService.postJob(dto, principal.getName());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/recruiter")
    public ResponseEntity<List<JobListingDTO>> recruiterJobs(Principal principal) {
        return ResponseEntity.ok(jobService.getJobsByRecruiter(principal.getName()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobListingDTO>> allOpenJobs() {
        return ResponseEntity.ok(jobService.getAllOpenJobs());
    }


}

