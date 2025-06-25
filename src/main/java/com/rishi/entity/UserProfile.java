package com.rishi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/*🔹 2. UserProfile Entity (Details of Job Seeker)*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String education;
    private String skills; // e.g., "Java, Spring Boot, React"
    private String experienceSummary;
    private String resumeLink;
    private String phone;
    private String location;
    private String githubLink;
    private String linkedinLink;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
