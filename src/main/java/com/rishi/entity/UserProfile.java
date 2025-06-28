package com.rishi.entity;

import com.rishi.domain.ROLE;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

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
    private String email;
    private String education;
    private String experienceSummary;
    private String resumeLink;
    private String profilePicture; // URL or path to the profile picture
    private String phone;
    private String location;
    private String githubLink;
    private String linkedinLink;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_profile_roles", joinColumns = @JoinColumn(name = "user_profile_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<ROLE> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_skills", // ✅ different table from job_required_skills
            joinColumns = @JoinColumn(name = "user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();
}
