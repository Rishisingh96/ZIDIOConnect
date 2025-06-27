package com.rishi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(length = 100)
    private String salaryRange; // ₹3LPA – ₹6LPA

    @Column(length = 50)
    private String jobCategory; // IT, Marketing, Finance, etc.

    private Boolean isRemote;

    @Column(columnDefinition = "TEXT")
    private String preferredSkills; // Optional: still string for now

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String benefits;

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

    // ✅ Properly normalized skill mapping
    @ManyToMany
    @JoinTable(
            name = "job_required_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> requiredSkills = new HashSet<>();
}
