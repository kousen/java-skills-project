package com.oreilly.webservices;

import jakarta.validation.constraints.*;

/**
 * Employee domain model for REST services demonstration using modern Java records.
 * <p>
 * This record demonstrates:
 * - Bean Validation annotations for input validation
 * - JSON serialization/deserialization with Jackson
 * - Modern Java records for immutable data transfer objects
 * - Constructor overloading for different use cases
 * - Automatic generation of toString, equals, hashCode
 */
public record Employee(
    Long id,
    
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,
    
    @NotNull(message = "Department cannot be null")
    @Size(min = 2, max = 50, message = "Department must be between 2 and 50 characters")
    String department,
    
    @Min(value = 0, message = "Salary must be non-negative")
    @Max(value = 1000000, message = "Salary cannot exceed 1,000,000")
    Double salary
) {
    
    /**
     * Constructor for creating new employees (without ID).
     * The server will assign the ID upon creation.
     * 
     * @param name Employee name
     * @param department Employee department
     * @param salary Employee salary
     */
    public Employee(String name, String department, Double salary) {
        this(null, name, department, salary);
    }
    
    /**
     * Create a new Employee with updated ID.
     * Useful for converting new employees to saved employees with generated IDs.
     * 
     * @param id The ID to assign
     * @return New Employee instance with the specified ID
     */
    public Employee withId(Long id) {
        return new Employee(id, name, department, salary);
    }
    
    /**
     * Create a new Employee with updated salary.
     * Useful for salary updates while maintaining immutability.
     * 
     * @param newSalary The new salary
     * @return New Employee instance with updated salary
     */
    public Employee withSalary(Double newSalary) {
        return new Employee(id, name, department, newSalary);
    }
}