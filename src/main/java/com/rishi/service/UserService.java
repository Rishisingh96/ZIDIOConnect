package com.rishi.service;

import com.rishi.dto.UserDTO;
import com.rishi.request.LoginRequest;
import com.rishi.request.SignupRequest;
import com.rishi.response.AuthResponse;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

}
