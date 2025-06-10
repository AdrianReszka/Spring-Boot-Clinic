package com.example.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Getter
@Setter
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private final Map<String, String> errors;

    public ErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(HttpStatus status, String message) {
        this(status, message, null);
    }

}
