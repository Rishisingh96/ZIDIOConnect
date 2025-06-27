package com.rishi.repository;

import com.rishi.entity.JobApplication;
import com.rishi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByApplicant(User user);
}
