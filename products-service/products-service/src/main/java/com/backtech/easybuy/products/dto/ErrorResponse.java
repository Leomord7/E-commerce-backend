package com.backtech.easybuy.products.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter

public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String message;
    private String path;
    private String error;
    private List<String> fieldError;

    public ErrorResponse() {
    }

    public ErrorResponse(Instant timestamp, int status, String message, String path, String error, List<String> fieldError) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
        this.error = error;
        this.fieldError = fieldError;
    }
}
