package com.bad.batch.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String error;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(String error, String message, int value) {
        this.error = error;
        this.message = message;
    }
}
