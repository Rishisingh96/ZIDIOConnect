package com.rishi.service;

import com.rishi.dto.RecruiterProfileDTO;
import com.rishi.dto.UserProfileDTO;

import java.util.Set;

public interface UserProfileService {
    UserProfileDTO getProfileByEmail(String email);
    UserProfileDTO createOrUpdateProfile(UserProfileDTO dto, String email);
    Set<UserProfileDTO> getAllUserProfile();
    UserProfileDTO getUserProfileById(Long id);
    UserProfileDTO createUserProfiles(UserProfileDTO profileDTO);
    void deleteRecruiterProfile(Long id);
} 