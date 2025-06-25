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
    // 🔸 Role string bhi bhejna hai — add this:
    private String role;

}
