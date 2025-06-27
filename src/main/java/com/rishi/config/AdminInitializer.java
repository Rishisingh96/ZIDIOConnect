package com.rishi.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rishi.domain.ROLE;
import com.rishi.entity.User;
import com.rishi.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class AdminInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        if (!userRepository.existsByEmail("admin@zidio.com")) {
            User admin = new User();
            admin.setFullName("Rishi Singh");
            admin.setEmail("admin@zidio.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("7800017055");
            admin.setRoles(Set.of(ROLE.ADMIN));
            userRepository.save(admin);
        }
    }
}

