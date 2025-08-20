package com.oreilly.webservices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for when an employee is not found.
 * 
 * This demonstrates:
 * - Custom exception creation
 * - @ResponseStatus annotation to automatically return HTTP 404
 * - Meaningful error messages for better debugging
 * - REST API error handling best practices
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Employee not found")
public class EmployeeNotFoundException extends RuntimeException {
    
    /**
     * Constructor with employee ID.
     * @param id The ID of the employee that was not found
     */
    public EmployeeNotFoundException(Long id) {
        super(String.format("Employee not found with ID: %d", id));
    }
    
    /**
     * Constructor with custom message.
     * @param message Custom error message
     */
    public EmployeeNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause.
     * @param message Custom error message
     * @param cause The underlying cause
     */
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}