package com.rishi.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rishi.dto.SkillDTO;
import com.rishi.dto.UserProfileDTO;
import com.rishi.entity.Skill;
import com.rishi.entity.User;
import com.rishi.entity.UserProfile;
import com.rishi.repository.SkillRepository;
import com.rishi.repository.UserProfileRepository;
import com.rishi.repository.UserRepository;
import com.rishi.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;

    // ✅ GET profile by email
    @Override
    public UserProfileDTO getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new RuntimeException("Profile not found for user: " + email);
        }

        UserProfileDTO dto = modelMapper.map(profile, UserProfileDTO.class);
        dto.setRoles(user.getRoles()); // ✅ Add roles
        return dto;
    }

    // ✅ CREATE or UPDATE profile
    @Override
    public UserProfileDTO createOrUpdateProfile(UserProfileDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfile profile = userProfileRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (dto.getFullName() != null) profile.setFullName(dto.getFullName());
        if (dto.getEmail() != null) profile.setEmail(dto.getEmail());
        if (dto.getEducation() != null) profile.setEducation(dto.getEducation());
        if (dto.getExperienceSummary() != null) profile.setExperienceSummary(dto.getExperienceSummary());
        if (dto.getResumeLink() != null) profile.setResumeLink(dto.getResumeLink());
        if (dto.getPhone() != null) profile.setPhone(dto.getPhone());
        if (dto.getLocation() != null) profile.setLocation(dto.getLocation());
        if (dto.getGithubLink() != null) profile.setGithubLink(dto.getGithubLink());
        if (dto.getLinkedinLink() != null) profile.setLinkedinLink(dto.getLinkedinLink());
        if (dto.getSkills() != null) {
            Set<Skill> skillEntities = dto.getSkills().stream()
                    .map(skillDTO -> skillRepository.findByName(skillDTO.getName())
                            .orElseGet(() -> skillRepository.save(new Skill(skillDTO.getName()))))
                    .collect(Collectors.toSet());
            profile.setSkills(skillEntities);
        }
        if (dto.getRoles() != null) profile.setRoles(new HashSet<>(dto.getRoles()));

        UserProfile saved = userProfileRepository.save(profile);

        // ✅ Convert back to DTO with roles
        UserProfileDTO response = modelMapper.map(saved, UserProfileDTO.class);
        response.setRoles(user.getRoles());
        return response;
    }

    // ✅ GET all profiles
    @Override
    public Set<UserProfileDTO> getAllUserProfile() {
        return userProfileRepository.findAll().stream().map(profile -> {
            UserProfileDTO dto = modelMapper.map(profile, UserProfileDTO.class);
            dto.setRoles(profile.getUser().getRoles());
            return dto;
        }).collect(Collectors.toSet());
    }

    // ✅ GET by ID
    @Override
    public UserProfileDTO getUserProfileById(Long id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with ID: " + id));
        UserProfileDTO dto = modelMapper.map(profile, UserProfileDTO.class);
        dto.setRoles(profile.getUser().getRoles());
        return dto;
    }

    // ✅ Optional creation method (if needed separately)
    @Override
    public UserProfileDTO createUserProfiles(UserProfileDTO dto) {
        if (userProfileRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("User already exists with email : " + dto.getEmail());
        }

        UserProfile profile = modelMapper.map(dto, UserProfile.class);
        UserProfile saved = userProfileRepository.save(profile);
        return modelMapper.map(saved, UserProfileDTO.class);
    }

    // ✅ Delete (optional)
    @Override
    public void deleteRecruiterProfile(Long id) {
        userProfileRepository.deleteById(id);
    }

    private Set<SkillDTO> mapSkills(Set<Skill> skills) {
        return skills.stream()
            .map(skill -> new SkillDTO(skill.getId(), skill.getName()))
            .collect(Collectors.toSet());
    }
}


/* UserProfile profile = modelMapper.map(dto, UserProfile.class);
        profile.setUser(user);
        profile.setRoles(new HashSet<>(user.getRoles())); // ✅ Deep copy, no shared reference

        // ✅ Convert SkillDTOs to Skill entities (if provided)
        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
            Set<Skill> skillEntities = dto.getSkills().stream()
                    .map(skillDTO -> skillRepository.findByName(skillDTO.getName())
                            .orElseGet(() -> skillRepository.save(new Skill(skillDTO.getName()))))
                    .collect(Collectors.toSet());
            profile.setSkills(skillEntities);
             */