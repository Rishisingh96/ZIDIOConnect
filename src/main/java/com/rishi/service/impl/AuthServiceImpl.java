package com.rishi.service.impl;

import java.util.Collections;
import java.util.HashSet;

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
import com.rishi.entity.RecruiterProfile;
import com.rishi.entity.User;
import com.rishi.entity.UserProfile;
import com.rishi.repository.RecruiterProfileRepository;
import com.rishi.repository.UserProfileRepository;
import com.rishi.repository.UserRepository;
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
    private final UserProfileRepository userProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    private final UserProfileService userProfileService;

    private final EmailServiceImpl emailService;

    // 🔐 Register New User
    @Override
    public void registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists with email: " + request.getEmail());
        }

        // Create base user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());

        // Set user role
        ROLE role;
        try {
            role = ROLE.valueOf(request.getRole().toUpperCase());
            user.setRoles(Collections.singleton(role));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role provided: " + request.getRole());
        }

        userRepository.save(user);
        logger.info("✅ User registered with email: {}", request.getEmail());

        // Send welcome email
        emailService.sendSimpleMail(
            request.getEmail(),
            "Welcome to ZidioConnect",
            "Hello " + request.getFullName() + ",\n\n" +
            "Thank you for registering on our Job Portal. We are excited to have you on board!\n\n" +
            "Best regards,\nJob Portal Team"
        );

        // Auto-create corresponding profile based on role
        if (role == ROLE.JOB_SEEKER) {
            UserProfile profile = new UserProfile();
            profile.setFullName(request.getFullName());
            profile.setPhone(request.getPhone());
            profile.setEmail(request.getEmail());
            profile.setRoles(new HashSet<>(user.getRoles()));
            profile.setUser(user);
            userProfileRepository.save(profile);
            logger.info("👤 UserProfile created for: {}", request.getEmail());

        } else if (role == ROLE.RECRUITER) {
            RecruiterProfile profile = new RecruiterProfile();
            profile.setFullName(request.getFullName());
            profile.setPhone(request.getPhone());
            profile.setEmail(request.getEmail());
            profile.setRoles(new HashSet<>(user.getRoles()));
            profile.setUser(user);
            recruiterProfileRepository.save(profile);
            logger.info("🏢 RecruiterProfile created for: {}", request.getEmail());
        }
    }

    // 🔐 Login User & Return JWT
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT
        String jwt = jwtUtil.generateToken(request.getEmail());

        // Get user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after login"));

        // Build response
        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRoles().stream().findFirst().get().name());

        logger.info("🔓 User logged in: {}", user.getEmail());

        return response;
    }

    // 🔓 Logout
    @Override
    public String logout() {
        // JWT is stateless; client should delete token
        return "Logged out. Client should delete JWT from local storage.";
    }
}
