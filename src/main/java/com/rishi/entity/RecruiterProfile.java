package com.rishi.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
/*🔹 3. RecruiterProfile Entity (Details of Recruiter/Company)*/


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecruiterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String companyName;
    private String designation;
    private String companyWebsite;
    private String phone;

    @Column(unique = true)
    private String email;
    private String location;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    private List<JobListing> jobListings;
}

