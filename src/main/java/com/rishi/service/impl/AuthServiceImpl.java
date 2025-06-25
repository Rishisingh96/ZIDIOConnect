package com.rishi.service.impl;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rishi.config.JwtUtil;
import com.rishi.domain.ROLE;
import com.rishi.entity.User;
import com.rishi.entity.UserProfile;
import com.rishi.entity.RecruiterProfile;
import com.rishi.repository.UserProfileRepository;
import com.rishi.repository.UserRepository;
import com.rishi.repository.RecruiterProfileRepository;
import com.rishi.request.LoginRequest;
import com.rishi.request.SignupRequest;
import com.rishi.response.AuthResponse;
import com.rishi.service.AuthService;
import com.rishi.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserProfileService userProfileService;
    private final UserProfileRepository userProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;


    // 🔐 Signup Logic
    @Override
    public void registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists with email: " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());

        // Set role from request safely
        ROLE role;
        try {
            role = ROLE.valueOf(request.getRole().toUpperCase());
            user.setRoles(Collections.singleton(role));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided: " + request.getRole());
        }

        userRepository.save(user);
        logger.info("User registered with email: {}", request.getEmail());

        if (role == ROLE.JOB_SEEKER) {
            // Create UserProfile with basic info
            UserProfile profile = new UserProfile();
            profile.setFullName(request.getFullName());
            profile.setPhone(request.getPhone());
            profile.setUser(user);
            userProfileRepository.save(profile);
        } else if (role == ROLE.RECRUITER) {
            // Create RecruiterProfile with basic info
            RecruiterProfile recruiterProfile = new RecruiterProfile();
            recruiterProfile.setPhone(request.getPhone());
            recruiterProfile.setUser(user);
            recruiterProfileRepository.save(recruiterProfile);
        }
        // For ADMIN, no profile is created by default
    }

    // 🔐 Login Logic
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT using email (or username)
        String jwt = jwtUtil.generateToken(request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after login"));

        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRoles().stream().findFirst().get().name());

        return response;
    }

    @Override
    public String logout() {
        return "Logged out. Client should delete JWT from local storage.";
    }

    // Google login, OTP login can be added next

}
