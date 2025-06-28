package com.rishi.service;

import com.rishi.domain.ApplicationStatus;
import com.rishi.dto.JobApplicationDTO;

import java.util.List;

public interface JobApplicationService {
    JobApplicationDTO applyToJob(Long jobId, String resumeLink, String userEmail);
    List<JobApplicationDTO> getMyApplications(String userEmail);
    public void updateApplicationStatus(Long applicationId, ApplicationStatus newStatus);
}
