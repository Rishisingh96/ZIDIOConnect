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
    private Long id;
    private String education;
    private String skills; // Comma-separated or List<String>
    private String experienceSummary;
    private String resumeLink;
    private Long userId;
}
