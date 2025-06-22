package com.rishi.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}


/*{
  "email": "rishi@gmail.com",
  "password": "rishi123"
}*/