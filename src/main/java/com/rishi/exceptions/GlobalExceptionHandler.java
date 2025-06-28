package com.rishi.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

public class GlobalExceptionHandler {
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity
                .badRequest()
                .body("📛 File size too large! Max allowed size is 4MB.");
    }
}
