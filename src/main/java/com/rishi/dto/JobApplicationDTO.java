package com.rishi.dto;

import com.rishi.domain.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {
    private Long id;

    private String resumeLink;

    private ApplicationStatus status; // APPLIED, REVIEWED, etc.

    private LocalDateTime appliedDate;

    private Long userId;
    private Long jobId;
}
