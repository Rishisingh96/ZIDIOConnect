package com.rishi.controller;

import com.rishi.entity.UserProfile;
import com.rishi.repository.UserProfileRepository;

import com.rishi.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rishi.dto.UserProfileDTO;
import com.rishi.service.UserProfileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class UserProfileController {


    private final UserProfileService userProfileService;
    private final UserProfileRepository userProfileRepository;
    private final FileUploadService fileUploadService;


    @GetMapping
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileDTO profileDTO = userProfileService.getProfileByEmail(email);
        return ResponseEntity.ok(profileDTO);
    }

    @PostMapping("/update")
    public ResponseEntity<UserProfileDTO> update(@RequestBody UserProfileDTO dto, Principal principal) {
        UserProfileDTO saved = userProfileService.createOrUpdateProfile(dto, principal.getName());
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/upload-resume")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId
            ){
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String existingResumePath = fileUploadService.replaceFile(file, profile.getResumeLink(), "resume");
        profile.setResumeLink(existingResumePath);
        userProfileRepository.save(profile);
        return ResponseEntity.ok("Resume uploaded successfully: " + existingResumePath);
    }

    @PostMapping("/upload-profile-picture")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId
    ) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String existingPicturePath = fileUploadService.replaceFile(file, profile.getProfilePicture(), "profile-pictures");
        profile.setProfilePicture(existingPicturePath);
        userProfileRepository.save(profile);
        return ResponseEntity.ok("Profile picture uploaded successfully: " + existingPicturePath);
    }

    @GetMapping("/download-resume/{userId}")
    public ResponseEntity<Resource> downloadResume(
            @PathVariable Long userId
    )  {
        UserProfile profile = userProfileRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User profile not found"));

    String resumePath = profile.getResumeLink(); // e.g. "resume/filename.pdf"
        if (resumePath == null || resumePath.isEmpty()) {
        return ResponseEntity.badRequest().body(null);
    }

        try {
        String[] parts = resumePath.split("/");
        String fileName = parts[1]; // get only the filename
        Resource resource = fileUploadService.loadFile(fileName, "resume");

        String contentType = Files.probeContentType(Paths.get(resource.getFile().getAbsolutePath()));
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    } catch (IOException e) {
        return ResponseEntity.internalServerError().build();
    }
    }

    @DeleteMapping("/delete-resume/{userId}")
    @PreAuthorize("hasRole('USER')" + " or hasRole('ADMIN')")
    public ResponseEntity<String> deleteResume(@PathVariable Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        String resumePath = profile.getResumeLink();
        if (resumePath == null || resumePath.isEmpty()) {
            return ResponseEntity.badRequest().body("No resume found to delete.");
        }

        // 🧠 Use deleteFile() from service
        String[] parts = resumePath.split("/");
        if (parts.length == 2) {
            String fileName = parts[1];
            fileUploadService.deleteFile(fileName, "resume");
        }

        profile.setResumeLink(null);
        userProfileRepository.save(profile);

        return ResponseEntity.ok("✅ Resume deleted successfully.");
    }
} 