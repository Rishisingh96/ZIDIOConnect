package com.rishi.service;

import com.rishi.dto.JobApplicationDTO;

import java.util.List;

public interface JobApplicationService {
    JobApplicationDTO applyToJob(Long jobId, String resumeLink, String userEmail);
    List<JobApplicationDTO> getMyApplications(String userEmail);
}
