package com.rishi.service.impl;

import java.util.HashSet;

import com.rishi.dto.JobListingDTO;
import com.rishi.dto.SkillDTO;
import com.rishi.entity.JobListing;
import com.rishi.entity.RecruiterProfile;
import com.rishi.entity.Skill;
import com.rishi.repository.JobListingRepository;
import com.rishi.repository.RecruiterProfileRepository;
import com.rishi.repository.SkillRepository;
import com.rishi.service.JobListingService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobListingServiceImpl implements JobListingService {

    private final JobListingRepository jobRepo;
    private final RecruiterProfileRepository recruiterRepo;
    private final SkillRepository skillRepo;
    private final ModelMapper mapper;
    private final EmailServiceImpl emailService;

    private Set<SkillDTO> mapSkills(Set<Skill> skills) {
        return skills.stream()
            .map(skill -> new SkillDTO(skill.getId(), skill.getName()))
            .collect(Collectors.toSet());
    }

    @Override
    public JobListingDTO postJob(JobListingDTO dto, String recruiterEmail) {
        RecruiterProfile recruiter = recruiterRepo.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        JobListing job = mapper.map(dto, JobListing.class);
        job.setRecruiter(recruiter);
        job.setCompanyName(recruiter.getCompanyName());
        job.setStatus("OPEN");

        Set<Long> skillIds = dto.getRequiredSkills().stream()
                .map(SkillDTO::getId)
                .collect(Collectors.toSet());
        Set<Skill> skills = new HashSet<>(skillRepo.findAllById(skillIds));
        job.setRequiredSkills(skills);

        JobListing saved = jobRepo.save(job);

        // Send email notification to recruiter
        emailService.sendJobPostedNotification(recruiter.getEmail(), saved.getTitle());

        JobListingDTO responseDto = mapper.map(saved, JobListingDTO.class);
        responseDto.setRequiredSkills(
            saved.getRequiredSkills().stream()
                .map(skill -> new SkillDTO(skill.getId(), skill.getName()))
                .collect(Collectors.toSet())
        );
        return responseDto;
    }

    @Override
    public List<JobListingDTO> getJobsByRecruiter(String email) {
        RecruiterProfile recruiter = recruiterRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        return recruiter.getJobListings().stream()
                .map(job -> {
                    JobListingDTO dto = mapper.map(job, JobListingDTO.class);
                    dto.setRequiredSkills(mapSkills(job.getRequiredSkills()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<JobListingDTO> getAllOpenJobs() {
        return jobRepo.findByStatus("OPEN").stream()
                .map(job -> {
                    JobListingDTO dto = mapper.map(job, JobListingDTO.class);
                    dto.setRequiredSkills(mapSkills(job.getRequiredSkills()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

