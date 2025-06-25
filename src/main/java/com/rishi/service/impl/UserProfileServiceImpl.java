package com.rishi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rishi.dto.UserProfileDTO;
import com.rishi.entity.User;
import com.rishi.entity.UserProfile;
import com.rishi.repository.UserProfileRepository;
import com.rishi.repository.UserRepository;
import com.rishi.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserProfileDTO getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new RuntimeException("Profile not found for user: " + email);
        }
        return modelMapper.map(profile, UserProfileDTO.class);
    }

    @Override
    public UserProfileDTO updateProfile(String email, UserProfileDTO profileDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }
        profile.setFullName(profileDTO.getFullName());
        profile.setEducation(profileDTO.getEducation());
        profile.setSkills(profileDTO.getSkills());
        profile.setExperienceSummary(profileDTO.getExperienceSummary());
        profile.setResumeLink(profileDTO.getResumeLink());
        profile.setPhone(profileDTO.getPhone());
        profile.setLocation(profileDTO.getLocation());
        profile.setGithubLink(profileDTO.getGithubLink());
        profile.setLinkedinLink(profileDTO.getLinkedinLink());
        userProfileRepository.save(profile);
        return modelMapper.map(profile, UserProfileDTO.class);
    }
} 