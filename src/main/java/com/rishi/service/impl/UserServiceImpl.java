// ✅ Final: UserServiceImpl.java (Clean + Working with ModelMapper)
package com.rishi.service.impl;

import com.rishi.config.JwtUtil;
import com.rishi.domain.ROLE;
import com.rishi.dto.UserDTO;
import com.rishi.entity.User;
import com.rishi.repository.UserRepository;
import com.rishi.service.ModelMapperService;
import com.rishi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ModelMapperService modelMapperService;

    // 🔹 Create User (Signup)
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("User already exists with email: " + userDTO.getEmail());
        }

        User user = modelMapperService.dtoToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role: JOB_SEEKER
        user.setRoles(Collections.singleton(ROLE.JOB_SEEKER));

        User savedUser = userRepository.save(user);
        logger.info("User created successfully with email: {}", savedUser.getEmail());

        return modelMapperService.entityToDto(savedUser);
    }

    // 🔹 (Optional Future Methods)
    // public UserDTO getUserById(Long id) {}
    // public List<UserDTO> getAllUsers() {}
    // public void deleteUser(Long id) {}
    // public UserDTO updateUser(UserDTO dto) {}
}
