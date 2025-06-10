package com.example.exceptions;

public class NoContentException extends RuntimeException {
    public NoContentException(String message) {
        super(message);
    }
}