package com.rishi.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/*🔹 3. RecruiterProfile Entity (Details of Recruiter/Company)*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfileDTO {
    private Long id;
    private String companyName;
    private String designation;
    private String companyWebsite;
    private Long userId;
}

