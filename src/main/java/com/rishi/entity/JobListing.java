package com.rishi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String location;

    @Column(length = 50)
    private String jobType; // Full-time, Part-time, Internship, etc.

    private Integer minExperience;
    private Integer maxExperience;

    @Column(length = 50)
    private String experienceLevel; // Entry, Mid, Senior

    @Column(columnDefinition = "TEXT")
    private String requiredSkills; // comma-separated

    @Column(columnDefinition = "TEXT")
    private String preferredSkills; // comma-separated

    @Column(length = 100)
    private String salaryRange; // ₹3LPA – ₹6LPA

    @Column(length = 50)
    private String jobCategory; // IT, Marketing, Finance, etc.

    private Boolean isRemote;

    @Column(columnDefinition = "TEXT")
    private String responsibilities; // comma-separated or paragraph

    @Column(columnDefinition = "TEXT")
    private String benefits; // Health Insurance, Paid Leave etc.

    private LocalDateTime applicationDeadline;

    @Column(length = 20)
    private String status; // OPEN, CLOSED, DRAFT

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(length = 100)
    private String companyName; // Optional duplicate from recruiter for quick access

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private RecruiterProfile recruiter;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobApplication> applications;
}
