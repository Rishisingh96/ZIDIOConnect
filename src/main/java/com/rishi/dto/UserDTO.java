package com.rishi.dto;
import com.rishi.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 1. User Entity (Common for both Recruiter & Job Seeker)*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone; // For registration only
    private String password; // For registration only
    private Role role;
}

