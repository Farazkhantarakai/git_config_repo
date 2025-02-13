package com.tech.gateway.exceptions;

import jakarta.validation.ConstraintViolationException;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalException {

    Logger log = Logger.getLogger(GlobalException.class.getName());

    // Handle validation errors (from @Valid annotations, e.g., MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(@NotNull MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        // Iterate through field errors and add them to the errors map
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
            log.info("Validation error: " + error.getField() + " -> " + error.getDefaultMessage());
        }

        // Return a BAD_REQUEST response with the error details
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(@NotNull Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        log.warning("General exception: " + ex.getMessage());  // Log general exceptions with warning

        // Return a BAD_REQUEST response with the error details
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle constraint violations (like @NotNull or @Size violations)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        
        // Iterate over all constraint violations and add them to the error map
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
            log.info("Constraint violation: " + fieldName + " -> " + errorMessage);  // Log the violation
        });

        // Return a BAD_REQUEST response with the error details
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
