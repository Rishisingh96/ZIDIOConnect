package com.rishi.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rishi.dto.RecruiterProfileDTO;
import com.rishi.entity.RecruiterProfile;
import com.rishi.entity.User;
import com.rishi.repository.RecruiterProfileRepository;
import com.rishi.repository.UserRepository;
import com.rishi.service.RecruiterProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruiterProfileServiceImpl implements RecruiterProfileService {

    private final UserRepository userRepo;
    private final RecruiterProfileRepository recruiterRepo;
    private final ModelMapper mapper;


    // ✅ Get RecruiterProfile by email
    @Override
    public RecruiterProfileDTO getRecruiterByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        RecruiterProfile profile = recruiterRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found for user: " + email));

        RecruiterProfileDTO dto = mapper.map(profile, RecruiterProfileDTO.class);
        dto.setRoles(user.getRoles()); // ✅ Add roles manually
        return dto;
    }

    // ✅ Create or update recruiter profile
    @Override
    public RecruiterProfileDTO createOrUpdate(RecruiterProfileDTO dto, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        RecruiterProfile profile = recruiterRepo.findById(dto.getId())
                .orElseThrow(()->new RuntimeException("Recruiter profile not found with ID: " + dto.getId()));

        if(dto.getFullName() != null) profile.setFullName(dto.getFullName());
        if(dto.getCompanyName() != null) profile.setCompanyName(dto.getCompanyName());
        if(dto.getEmail() != null) profile.setEmail(dto.getEmail());
        if(dto.getDesignation() != null) profile.setDesignation(dto.getDesignation());
        if(dto.getCompanyWebsite() != null) profile.setCompanyWebsite(dto.getCompanyWebsite());
        if(dto.getPhone() != null) profile.setPhone(dto.getPhone());
        if(dto.getLocation() != null) profile.setLocation(dto.getLocation());
        // ✅ Update roles if provided
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            profile.setRoles(new HashSet<>(dto.getRoles()));
        } else {
            profile.setRoles(new HashSet<>(user.getRoles())); // Use user's roles if none provided
        }

        RecruiterProfile saved = recruiterRepo.save(profile);

        RecruiterProfileDTO response = mapper.map(saved, RecruiterProfileDTO.class);
        response.setRoles(user.getRoles()); // ✅ Set roles again in response
        return response;
    }

    // ✅ Get all recruiter profiles
    @Override
    public Set<RecruiterProfileDTO> getAllRecruiterProfiles() {
        return recruiterRepo.findAll().stream().map(profile -> {
            RecruiterProfileDTO dto = mapper.map(profile, RecruiterProfileDTO.class);
            dto.setRoles(profile.getUser().getRoles());
            return dto;
        }).collect(Collectors.toSet());
    }

    // ✅ Get profile by ID
    @Override
    public RecruiterProfileDTO getRecruiterProfileById(Long id) {
        RecruiterProfile profile = recruiterRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found with ID: " + id));
        RecruiterProfileDTO dto = mapper.map(profile, RecruiterProfileDTO.class);
        dto.setRoles(profile.getUser().getRoles());
        return dto;
    }

    // ✅ Create new profile (optional method)
    @Override
    public RecruiterProfileDTO createRecruiterProfile(RecruiterProfileDTO dto) {
        if (recruiterRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Recruiter already exists with email: " + dto.getEmail());
        }

        RecruiterProfile profile = mapper.map(dto, RecruiterProfile.class);
        RecruiterProfile saved = recruiterRepo.save(profile);
        return mapper.map(saved, RecruiterProfileDTO.class);
    }

    // ✅ Delete recruiter profile by ID
    @Override
    public void deleteRecruiterProfile(Long id) {
        recruiterRepo.deleteById(id);
    }
}


/*@Override
    public RecruiterProfileDTO createOrUpdate(RecruiterProfileDTO dto, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        RecruiterProfile profile = mapper.map(dto, RecruiterProfile.class);
        profile.setUser(user);
        profile.setRoles(new HashSet<>(user.getRoles()));

        RecruiterProfile saved = recruiterRepo.save(profile);

        RecruiterProfileDTO response = mapper.map(saved, RecruiterProfileDTO.class);
        response.setRoles(user.getRoles()); // ✅ Set roles again in response
        return response;
    }*/