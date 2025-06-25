package com.rishi.service.impl;

import com.rishi.entity.RecruiterProfile;
import com.rishi.entity.User;
import com.rishi.repository.RecruiterProfileRepository;
import com.rishi.repository.UserRepository;
import com.rishi.service.RecruiterProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecruiterProfileServiceImpl implements RecruiterProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    @Override
    public RecruiterProfile getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        RecruiterProfile profile = user.getRecruiterProfile();
        if (profile == null) {
            throw new RuntimeException("Recruiter profile not found for user: " + email);
        }
        return profile;
    }

    @Override
    public RecruiterProfile updateProfile(String email, RecruiterProfile profileData) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        RecruiterProfile profile = user.getRecruiterProfile();
        if (profile == null) {
            profile = new RecruiterProfile();
            profile.setUser(user);
        }
        profile.setCompanyName(profileData.getCompanyName());
        profile.setDesignation(profileData.getDesignation());
        profile.setCompanyWebsite(profileData.getCompanyWebsite());
        profile.setPhone(profileData.getPhone());
        profile.setLocation(profileData.getLocation());
        recruiterProfileRepository.save(profile);
        return profile;
    }
} 