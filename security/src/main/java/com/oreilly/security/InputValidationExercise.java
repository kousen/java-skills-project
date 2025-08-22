package com.oreilly.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Input Validation Exercise - Practice Security Validation Techniques
 * 
 * This exercise guides you through implementing secure validation methods
 * that defend against injection attacks. Complete each TODO to build
 * a comprehensive validation service.
 */
public class InputValidationExercise {

    // Pre-compiled patterns for performance
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{9,14}$");
    
    // Business constants
    public static final int MIN_COMMENT_LENGTH = 1;
    public static final int MAX_COMMENT_LENGTH = 500;
    
    // Allowed departments for business policy
    private static final String[] ALLOWED_DEPARTMENTS = {
        "Engineering", "Marketing", "Sales", "HR", "Finance", "Operations"
    };

    /**
     * TODO #1: Implement email validation with XSS protection
     * 
     * Requirements:
     * 1. Check if email matches the EMAIL_PATTERN
     * 2. Scan for potential XSS patterns using containsPotentialXss()
     * 3. Return true only if email is valid AND safe
     * 
     * Test cases to consider:
     * - "user@company.com" -> true
     * - "user<script>alert('xss')</script>@company.com" -> false
     * - "invalid-email" -> false
     * - null or empty -> false
     */
    public boolean isValidAndSafeEmail(String email) {
        // TODO: Implement email validation with XSS protection
        return false; // Replace this line
    }

    /**
     * TODO #2: Implement phone number validation with business rules
     * 
     * Requirements:
     * 1. Check if phone matches the PHONE_PATTERN
     * 2. Ensure phone doesn't contain SQL injection patterns
     * 3. Business rule: Phone numbers starting with +1-555 are test numbers (reject them)
     * 4. Return true only if phone is valid, safe, and not a test number
     * 
     * Test cases to consider:
     * - "+1-123-456-7890" -> true
     * - "+1-555-123-4567" -> false (test number)
     * - "123'; DROP TABLE phones; --" -> false (SQL injection)
     * - "invalid-phone" -> false
     */
    public boolean isValidBusinessPhone(String phone) {
        // TODO: Implement phone validation with business rules
        return false; // Replace this line
    }

    /**
     * TODO #3: Implement department name validation with whitelist
     * 
     * Requirements:
     * 1. Check if department is in the ALLOWED_DEPARTMENTS array
     * 2. Perform case-insensitive matching
     * 3. Trim whitespace before validation
     * 4. Reject null, empty, or departments not in the whitelist
     * 
     * This prevents both injection attacks and enforces business policies.
     * 
     * Test cases to consider:
     * - "Engineering" -> true
     * - "engineering" -> true (case insensitive)
     * - " Marketing " -> true (trimmed)
     * - "Hacking" -> false (not in whitelist)
     * - null -> false
     */
    public boolean isValidDepartment(String department) {
        // TODO: Implement department validation with whitelist
        return false; // Replace this line
    }

    /**
     * TODO #4: Implement user comment validation with layered security
     * 
     * Requirements:
     * 1. Check length constraints (MIN_COMMENT_LENGTH to MAX_COMMENT_LENGTH)
     * 2. Scan for XSS patterns using containsPotentialXss()
     * 3. Scan for SQL injection patterns using isSqlSafeInput()
     * 4. Return a list of validation errors (empty list if valid)
     * 
     * This demonstrates layered security validation.
     * 
     * Error messages to use:
     * - "Comment must be between 1 and 500 characters"
     * - "Comment contains potentially dangerous script content"
     * - "Comment contains potentially dangerous SQL patterns"
     */
    public List<String> validateUserComment(String comment) {
        List<String> errors = new ArrayList<>();
        
        // TODO: Implement layered comment validation
        // Add appropriate error messages to the errors list
        
        return errors;
    }

    // Helper methods - you can use these in your implementations
    
    /**
     * Detects potential XSS patterns in input.
     * You can call this method from your TODO implementations.
     */
    private boolean containsPotentialXss(String input) {
        if (input == null) return false;

        String lower = input.toLowerCase();
        String[] xssPatterns = {
            "<script", "</script", "javascript:", "onload=", "onerror=",
            "<iframe", "<object", "<embed", "vbscript:", "data:text/html"
        };

        for (String pattern : xssPatterns) {
            if (lower.contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if input is safe from SQL injection patterns.
     * You can call this method from your TODO implementations.
     */
    private boolean isSqlSafeInput(String input) {
        if (input == null) return true;

        String lower = input.toLowerCase();
        String[] sqlKeywords = {
            "drop", "delete", "insert", "update", "union", "select",
            "exec", "execute", "sp_", "xp_", "--", "/*", "*/", ";",
            "' or", "' and", "1=1", "1=2"
        };

        for (String keyword : sqlKeywords) {
            if (lower.contains(keyword)) {
                return false;
            }
        }

        // Check for suspicious quote patterns
        return !input.contains("'") || 
               (!input.contains("=") && !input.contains("or") && !input.contains("and"));
    }
}