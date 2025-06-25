package com.rishi.dto;
import com.rishi.domain.Providers;
import com.rishi.domain.ROLE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/* 1. User Entity (Common for both Recruiter & Job Seeker)*/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private Set<ROLE> roles;
}



