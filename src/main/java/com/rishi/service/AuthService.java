package com.rishi.service;

import com.rishi.dto.UserDTO;
import com.rishi.request.LoginRequest;
import com.rishi.request.SignupRequest;
import com.rishi.response.AuthResponse;

public interface AuthService {

    //Using Google Facebook Github login
//    public void loginUser(UserDTO userDTO);
//
//    public UserDTO processGoogleLogin(UserDTO userDTO);

    //Normal Register and Login
    void registerUser(SignupRequest request);

    AuthResponse login(LoginRequest request);

    String logout(); //


    //Using Email OTP

}
