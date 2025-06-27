package com.rishi.repository;

import com.rishi.dto.UserProfileDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rishi.entity.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile , Long> {
    Boolean existsByEmail(String email);


}
