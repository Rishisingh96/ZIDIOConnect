package com.rishi.response;

import com.rishi.domain.ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseMessage {
    private String message;
    private ROLE role;
    private boolean success;
    private HttpStatus status;
}
