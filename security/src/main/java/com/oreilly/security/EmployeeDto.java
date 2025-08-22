package com.oreilly.security;

/**
 * Employee Data Transfer Object for validation demonstrations.
 * Modern Java record providing immutable data with clean syntax.
 */
public record EmployeeDto(String name, String email, Double salary, String phone, String address) {
    
    /**
     * Constructor for creating employee without full address details
     */
    public EmployeeDto(String name, String email, Double salary, String phone) {
        this(name, email, salary, phone, null);
    }
    
    /**
     * Constructor for creating employee with minimal required fields
     */
    public EmployeeDto(String name, String email, Double salary) {
        this(name, email, salary, null, null);
    }
}