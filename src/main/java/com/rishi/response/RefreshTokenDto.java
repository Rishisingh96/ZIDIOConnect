package com.rishi.response;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RefreshTokenDto {
    private int id;
    private String token;
    private Instant expiryDate;


}
