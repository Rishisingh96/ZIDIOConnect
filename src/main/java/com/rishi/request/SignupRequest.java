package com.rishi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String role; // "JOB_SEEKER" or "RECRUITER"
}



/*{
  "fullName": "Rishi Singh",
  "email": "rishi@gmail.com",
  "password": "rishi123",
  "phone": "9876543210",
  "role": "JOB_SEEKER"
}*/