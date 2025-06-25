package com.rishi.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/*🔹 4. JobListing Entity (Recruiter Posts Jobs)*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListingDTO {
    private String title;
    private String description;
    private String location;
    private String jobType;
    private Integer minExperience;
    private Integer maxExperience;
    private String experienceLevel;
    private String requiredSkills;
    private String preferredSkills;
    private String salaryRange;
    private String jobCategory;
    private Boolean isRemote;
    private String responsibilities;
    private String benefits;
    private String companyName;
    private LocalDateTime applicationDeadline;
    private String status;
}

