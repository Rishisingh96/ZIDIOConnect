package com.rishi.service;

import com.rishi.entity.RecruiterProfile;

public interface RecruiterProfileService {
    RecruiterProfile getProfileByEmail(String email);
    RecruiterProfile updateProfile(String email, RecruiterProfile profile);
} 