package com.rishi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;



    // 🔁 Mapped with users (optional bidirectional)
    @ManyToMany(mappedBy = "skills")
    private Set<UserProfile> userProfiles = new HashSet<>();

    // 🔁 Mapped with jobs
    @ManyToMany(mappedBy = "requiredSkills")
    private Set<JobListing> jobListings = new HashSet<>();

    public Skill() {

    }

    public Skill(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Skill(Long id, String name, Set<UserProfile> userProfiles, Set<JobListing> jobListings) {
        this.id = id;
        this.name = name;
        this.userProfiles = userProfiles;
        this.jobListings = jobListings;
    }

    public Skill(String name) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public Set<JobListing> getJobListings() {
        return jobListings;
    }

    public void setJobListings(Set<JobListing> jobListings) {
        this.jobListings = jobListings;
    }
}

