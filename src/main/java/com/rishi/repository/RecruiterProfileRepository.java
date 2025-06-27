package com.rishi.repository;

import com.rishi.entity.RecruiterProfile;
import com.rishi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile , Long>{
    Optional<RecruiterProfile> findByUser(User user);
    Optional<RecruiterProfile> findByEmail(String email); // for job posting, recruiter access

    boolean existsByEmail(String email);
}
