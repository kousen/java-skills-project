package com.oreilly.webservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the REST API.
 * <p>
 * This demonstrates:
 * - @RestControllerAdvice for centralized exception handling
 * - Different exception types and appropriate HTTP status codes
 * - Validation error handling with detailed field-level errors
 * - Proper logging of exceptions
 * - Modern ProblemDetail responses (RFC 7807)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handle EmployeeNotFoundException - returns 404 Not Found.
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ProblemDetail handleEmployeeNotFound(EmployeeNotFoundException ex, WebRequest request) {
        logger.warn("Employee not found: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            ex.getMessage()
        );
        
        problemDetail.setType(URI.create("https://api.example.com/problems/employee-not-found"));
        problemDetail.setTitle("Employee Not Found");
        problemDetail.setProperty("path", getPath(request));
        
        return problemDetail;
    }
    
    /**
     * Handle validation errors from @Valid - returns 400 Bad Request.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        logger.warn("Validation failed: {}", ex.getMessage());
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Validation failed for one or more fields"
        );
        
        problemDetail.setType(URI.create("https://api.example.com/problems/validation-failed"));
        problemDetail.setTitle("Validation Failed");
        problemDetail.setProperty("path", getPath(request));
        problemDetail.setProperty("validationErrors", fieldErrors);
        
        return problemDetail;
    }
    
    /**
     * Handle IllegalArgumentException - returns 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );
        
        problemDetail.setType(URI.create("https://api.example.com/problems/illegal-argument"));
        problemDetail.setTitle("Invalid Request");
        problemDetail.setProperty("path", getPath(request));
        
        return problemDetail;
    }
    
    /**
     * Handle all other exceptions - returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred", ex);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred. Please try again later."
        );
        
        problemDetail.setType(URI.create("https://api.example.com/problems/internal-server-error"));
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("path", getPath(request));
        
        return problemDetail;
    }
    
    /**
     * Extract the request path from WebRequest.
     */
    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}