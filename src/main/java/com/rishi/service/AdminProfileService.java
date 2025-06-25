package com.rishi.service;

public interface AdminProfileService {
    String getProfileByEmail(String email);
    String updateProfile(String email, String profileData);
} 