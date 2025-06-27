package com.rishi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListingDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String jobType;
    private Integer minExperience;
    private Integer maxExperience;
    private String experienceLevel;
    private String salaryRange;
    private String jobCategory;
    private Boolean isRemote;
    private String preferredSkills;
    private String responsibilities;
    private String benefits;
    private LocalDateTime applicationDeadline;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String companyName;

    private RecruiterProfileDTO recruiter; // ✅ Reference to recruiter info

    private List<JobApplicationDTO> applications; // ✅ Applied users

    private Set<SkillDTO> requiredSkills; // ✅ Normalized required skills
}
