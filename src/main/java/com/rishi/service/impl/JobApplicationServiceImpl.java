package com.rishi.service.impl;

import com.rishi.domain.ApplicationStatus;
import com.rishi.dto.JobApplicationDTO;
import com.rishi.entity.JobApplication;
import com.rishi.entity.JobListing;
import com.rishi.entity.User;
import com.rishi.entity.UserProfile;
import com.rishi.repository.JobApplicationRepository;
import com.rishi.repository.JobRepository;
import com.rishi.repository.UserRepository;
import com.rishi.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobRepository jobRepo;
    private final UserRepository userRepo;
    private final JobApplicationRepository applicationRepo;
    private final ModelMapper mapper;

    @Override
    public JobApplicationDTO applyToJob(Long jobId, String resumeLink, String userEmail) {
        User user = userRepo.findByEmail(userEmail).orElseThrow();
        JobListing job = jobRepo.findById(jobId).orElseThrow();



        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setApplicant(user);
        application.setResumeLink(resumeLink);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedDate(LocalDateTime.now());

        UserProfile userProfile = user.getUserProfile();
        System.out.println("Logged in user email: " + userEmail);
        System.out.println("User entity email: " + user.getEmail());
        System.out.println("User entity id: " + user.getId());
        System.out.println("UserProfile: " + user.getUserProfile());

        if (userProfile != null) {
            application.setGithubLink(userProfile.getGithubLink());
            application.setLinkedinLink(userProfile.getLinkedinLink());
        } else {
            application.setGithubLink(null);
            application.setLinkedinLink(null);
        }
        JobApplication saved = applicationRepo.save(application);
        return mapper.map(saved, JobApplicationDTO.class);
    }

    @Override
    public List<JobApplicationDTO> getMyApplications(String userEmail) {
        User user = userRepo.findByEmail(userEmail).orElseThrow();

        return applicationRepo.findByApplicant(user).stream()
                .map(app -> mapper.map(app, JobApplicationDTO.class))
                .collect(Collectors.toList());
    }
}
