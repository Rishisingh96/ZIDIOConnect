package com.rishi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String role; // STUDENT, RECRUITER, ADMIN

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private StudentProfile studentProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RecruiterProfile recruiterProfile;
}
