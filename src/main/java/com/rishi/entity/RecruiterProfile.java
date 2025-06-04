package com.rishi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecruiterProfile {

    @Id
    private Long userId;

    private String companyName;
    private String designation;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

}
