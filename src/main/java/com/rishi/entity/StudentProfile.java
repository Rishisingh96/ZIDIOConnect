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
public class StudentProfile {

    @Id
    private Long userId;

    private String resumeUrl;
    private String education;
    private String skills;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
