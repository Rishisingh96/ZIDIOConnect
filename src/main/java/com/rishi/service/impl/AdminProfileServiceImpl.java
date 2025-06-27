package com.rishi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rishi.dto.JobApplicationDTO;
import com.rishi.dto.JobListingDTO;
import com.rishi.dto.RecruiterProfileDTO;
import com.rishi.dto.UserDTO;
import com.rishi.entity.JobApplication;
import com.rishi.entity.JobListing;
import com.rishi.entity.RecruiterProfile;
import com.rishi.entity.User;
import com.rishi.repository.JobApplicationRepository;
import com.rishi.repository.JobListingRepository;
import com.rishi.repository.RecruiterProfileRepository;
import com.rishi.repository.UserRepository;
import com.rishi.service.AdminProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl implements AdminProfileService {

    private final UserRepository userRepo;
    private final JobListingRepository jobRepo;
    private final JobApplicationRepository applicationRepo;
    private final RecruiterProfileRepository recruiterRepo;
    private final ModelMapper modelMapper;


    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecruiterProfileDTO> getAllRecruiters() {
        List<RecruiterProfile> recruiters = recruiterRepo.findAll();
        return recruiters.stream()
                .map(recruiter -> modelMapper.map(recruiter, RecruiterProfileDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobListingDTO> getAllJobs() {
        List<JobListing> jobs = jobRepo.findAll();
        return jobs.stream()
                .map(j -> modelMapper.map(j, JobListingDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplicationDTO> getAllApplications() {
        List<JobApplication> apps = applicationRepo.findAll();
        return apps.stream()
                .map(a -> modelMapper.map(a, JobApplicationDTO.class))
                .collect(Collectors.toList());

    }
}