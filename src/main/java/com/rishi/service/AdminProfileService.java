package com.rishi.service;

import com.rishi.dto.JobApplicationDTO;
import com.rishi.dto.JobListingDTO;
import com.rishi.dto.RecruiterProfileDTO;
import com.rishi.dto.UserDTO;

import java.util.List;

public interface AdminProfileService {
    List<UserDTO> getAllUsers();
    List<RecruiterProfileDTO> getAllRecruiters();
    List<JobListingDTO> getAllJobs();
    List<JobApplicationDTO> getAllApplications();
} 