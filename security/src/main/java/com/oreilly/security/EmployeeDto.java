package com.oreilly.security;

/**
 * Employee Data Transfer Object for validation demonstrations.
 * Modern Java record providing immutable data with clean syntax.
 */
public record EmployeeDto(
        String name,
        String email,
        Double salary,
        String phone,
        String address
) {
}