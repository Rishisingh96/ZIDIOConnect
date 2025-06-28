package com.rishi.dto;
import com.rishi.domain.ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/*🔹 3. RecruiterProfile Entity (Details of Recruiter/Company)*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfileDTO {

    private Long id;
    private String fullName;
    private String companyName;
    private String email;
    private String designation;
    private String companyWebsite;
    private String phone;
    private String location;
    private String profilePicture;
    private String companyLogo;

    private Set<ROLE> roles;
}


