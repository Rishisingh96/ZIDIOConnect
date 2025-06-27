package com.rishi.repository;

import com.rishi.entity.JobListing;
import com.rishi.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobListing, Long> {

    List<JobListing> findByStatus(String status);

    List<JobListing> findByRecruiter(RecruiterProfile recruiter);
}

