package com.oreilly.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Input Validation Service - Testable Design
 * Demonstrates security validation techniques with proper separation of concerns.
 * All validation methods are designed for comprehensive unit testing.
 */
public class InputValidator {

    // Validation patterns as constants for reuse and testing
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s\\-']+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{9,14}$|^\\+?1-\\d{3}-\\d{3}-\\d{4}$");

    // Business constants
    public static final double MIN_SALARY = 30000.0;
    public static final double MAX_SALARY = 1000000.0;
    public static final double EXECUTIVE_SALARY_THRESHOLD = 500000.0;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_ADDRESS_LENGTH = 200;

    // Allowed domains for company policy
    private static final String[] ALLOWED_DOMAINS = {"company.com", "contractor.company.com", "temp.company.com"};
    
    // Restricted names (security policy)
    private static final String[] NAME_BLOCKLIST = {"test user", "admin", "administrator", "root", "guest"};

    /**
     * Validates an employee record and returns list of validation errors
     */
    public List<String> validateEmployee(EmployeeDto employee) {
        List<String> errors = new ArrayList<>();

        if (!validateEmployeeName(employee.name())) {
            errors.add("Invalid name: must be 2-50 characters, letters only");
        }
        if (!isValidEmail(employee.email())) {
            errors.add("Invalid email format");
        }
        if (!isValidSalary(employee.salary())) {
            errors.add("Invalid salary: must be positive and under $1,000,000");
        }
        if (!isValidPhone(employee.phone())) {
            errors.add("Invalid phone format");
        }
        if (!isValidAddress(employee.address())) {
            errors.add("Invalid address: cannot be empty and max 200 characters");
        }

        return errors;
    }

    /**
     * Validates employee against business rules
     */
    public List<String> validateEmployeeBusinessRules(EmployeeDto employee) {
        List<String> errors = new ArrayList<>();

        // Check if email domain is allowed (company policy)
        if (employee.email() != null && !isAllowedEmailDomain(employee.email())) {
            errors.add("Email domain not allowed for employees");
        }

        // Check salary range based on position level
        if (employee.salary() != null) {
            if (employee.salary() < MIN_SALARY) {
                errors.add("Salary below minimum threshold");
            }
            if (employee.salary() > EXECUTIVE_SALARY_THRESHOLD && !isExecutiveLevel(employee)) {
                errors.add("High salary requires executive approval");
            }
        }

        // Check name against blocklist
        if (employee.name() != null && isNameBlacklisted(employee.name())) {
            errors.add("Name appears on restricted list");
        }

        return errors;
    }

    // Field validation methods - package-private for testing
    boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        String trimmed = name.trim();

        // Length check
        if (trimmed.length() < MIN_NAME_LENGTH || trimmed.length() > MAX_NAME_LENGTH) {
            return false;
        }

        // Pattern check - only letters, spaces, hyphens, and apostrophes
        return NAME_PATTERN.matcher(trimmed).matches();
    }

    boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    boolean isValidSalary(Double salary) {
        return salary != null && salary > 0.0 && salary <= MAX_SALARY;
    }

    boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.length() <= MAX_ADDRESS_LENGTH;
    }

    // Security validation methods - package-private for testing
    boolean isSqlSafeInput(String input) {
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
        return !input.contains("'") || (!input.contains("=") && !input.contains("or") && !input.contains("and"));
    }

    boolean containsPotentialXss(String input) {
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

    String escapeHtml(String input) {
        if (input == null) return null;

        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    boolean validateEmployeeName(String name) {
        if (!isValidName(name)) {
            return false;
        }

        // Check for potential XSS patterns
        return !containsPotentialXss(name);
    }

    // Business rule helper methods - package-private for testing
    boolean isAllowedEmailDomain(String email) {
        String domain = email.substring(email.lastIndexOf("@") + 1).toLowerCase();

        for (String allowedDomain : ALLOWED_DOMAINS) {
            if (domain.equals(allowedDomain)) {
                return true;
            }
        }
        return false;
    }

    boolean isExecutiveLevel(EmployeeDto employee) {
        String name = employee.name().toUpperCase();
        return name.contains("CEO") || name.contains("CTO") || name.contains("CFO") || name.contains("PRESIDENT");
    }

    boolean isNameBlacklisted(String name) {
        String lowerName = name.toLowerCase().trim();

        for (String blocked : NAME_BLOCKLIST) {
            if (lowerName.equals(blocked)) {
                return true;
            }
        }
        return false;
    }
}