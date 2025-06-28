package com.rishi.dto;

import java.util.Set;

import com.rishi.domain.ROLE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String fullName;
    private String email;
    private String education;
    private String experienceSummary;
    private String resumeLink;
    private String profilePicture;
    private String phone;
    private String location;
    private String githubLink;
    private String linkedinLink;

    private Set<SkillDTO> skills;
    private Set<ROLE> roles;
}
