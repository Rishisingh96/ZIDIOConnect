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

    private String education;
    private String skills; // comma-separated or List<String>
    private String experienceSummary;
    private String resumeLink;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
