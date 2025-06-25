package com.rishi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishi.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile , Long> {
    // Additional query methods can be defined here if needed
    // For example, you can add methods to find UserProfile by user or user email
}
