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
    private Long jobId;
    private String resumeLink;
    private ApplicationStatus status;
}

