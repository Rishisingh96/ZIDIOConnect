package com.rishi.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*🔹 3. RecruiterProfile Entity (Details of Recruiter/Company)*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfileDTO {

    private String fullName;
    private String companyName;
    private String email;
    private String designation;
    private String companyWebsite;
    private String phone;
    private String location;
}


