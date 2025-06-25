package com.rishi.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*🔹 2. UserProfile Entity (Details of Job Seeker)*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String fullName;
    private String education;
    private String skills;
    private String experienceSummary;
    private String resumeLink;
    private String phone;
    private String location;
    private String githubLink;
    private String linkedinLink;
}

