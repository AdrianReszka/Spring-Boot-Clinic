package com.example.exceptions;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
	    return new ResponseEntity<>(
	        new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()),
	        HttpStatus.BAD_REQUEST
	    );
	}
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation error!", errors),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage()),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid parameter format: " + ex.getName();
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.BAD_REQUEST, message),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternal(Exception ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!"),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ErrorResponse> handleNoContent(NoContentException ex) {
        return new ResponseEntity<>(
            new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }
}
