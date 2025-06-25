package com.rishi.service;

import com.rishi.dto.UserProfileDTO;

public interface UserProfileService {
    UserProfileDTO getProfileByEmail(String email);
    UserProfileDTO updateProfile(String email, UserProfileDTO profileDTO);
} 