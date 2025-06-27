package com.rishi.service;

import com.rishi.dto.JobListingDTO;

import java.util.List;

public interface JobListingService {
    JobListingDTO postJob(JobListingDTO dto, String recruiterEmail);
    List<JobListingDTO> getJobsByRecruiter(String recruiterEmail);
    List<JobListingDTO> getAllOpenJobs();
}
