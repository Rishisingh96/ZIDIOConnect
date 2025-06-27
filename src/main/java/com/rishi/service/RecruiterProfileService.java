package com.rishi.service;

import com.rishi.dto.RecruiterProfileDTO;

import java.util.Set;

public interface RecruiterProfileService {
    RecruiterProfileDTO getRecruiterByEmail(String email);
    RecruiterProfileDTO createOrUpdate(RecruiterProfileDTO dto, String email);

    // ✅ Get all recruiter profiles
    Set<RecruiterProfileDTO> getAllRecruiterProfiles();

    // ✅ Get profile by ID
    RecruiterProfileDTO getRecruiterProfileById(Long id);

    // ✅ Create new profile (optional method)
    RecruiterProfileDTO createRecruiterProfile(RecruiterProfileDTO dto);

    // ✅ Delete recruiter profile by ID
    void deleteRecruiterProfile(Long id);
//    RecruiterProfileDTO updateProfile(String email, RecruiterProfileDTO profileDTO);
//    Set<RecruiterProfileDTO> getAllRecruiterProfiles();
//    RecruiterProfileDTO getRecruiterProfileById(Long id);
//    RecruiterProfileDTO createRecruiterProfile(RecruiterProfileDTO profileDTO);
//    void deleteRecruiterProfile(Long id);


} 