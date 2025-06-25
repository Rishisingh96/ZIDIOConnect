package com.rishi.entity;

import com.rishi.domain.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/* 5. JobApplication Entity (User Applies to Jobs)*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resumeLink;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // APPLIED, REVIEWED, ACCEPTED, REJECTED

    @CreationTimestamp
    private LocalDateTime appliedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobListing job;
}