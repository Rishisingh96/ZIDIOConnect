package com.rishi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rishi.dto.UserDTO;
import com.rishi.entity.User;
import com.rishi.repository.UserRepository;
import com.rishi.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // Check if the user already exists by email
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            // If user exists, return null or throw an exception
            // return null; // or throw new UserAlreadyExistsException("User already exists with email: " + userDTO.getEmail());
            throw new RuntimeException("User already exists with email: " + userDTO.getEmail());
        }
        User user = modelMapper.map(userDTO, User.class);
        User savedUser  = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);

    }

}
/*


//Without ModelMapper
@Override
public UserDTO createUser(UserDTO userDTO) {
    // Check if the user already exists by email
    if(userRepository.existsByEmail(userDTO.getEmail())) {
        // If user exists, return null or throw an exception
        // return null; // or throw new UserAlreadyExistsException("User already exists with email: " + userDTO.getEmail());
        throw new RuntimeException("User already exists with email: " + userDTO.getEmail());
    }
    User user = new User();
    user.setEmail(userDTO.getEmail());
    user.setFullName(userDTO.getFullName());
    user.setPassword(userDTO.getPassword()); // later hash it
    user.setPhoneNumber(userDTO.getPhoneNumber());
    user.setRole(userDTO.getRole());

    user = userRepository.save(user);

    // return as DTO
    return new UserDTO(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPassword(),
            user.getPhoneNumber(),
            user.getRole()
    );
}*/
