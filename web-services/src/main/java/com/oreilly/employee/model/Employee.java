package com.oreilly.employee.model;

import jakarta.validation.constraints.*;

/**
 * Employee domain model with validation annotations.
 * Demonstrates Bean Validation for REST APIs.
 */
public class Employee {
    
    private Long id;
    
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name can only contain letters and spaces")
    private String name;
    
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotNull(message = "Department cannot be null")
    @Size(min = 2, max = 50, message = "Department must be between 2 and 50 characters")
    private String department;
    
    @NotNull(message = "Salary cannot be null")
    @Min(value = 0, message = "Salary cannot be negative")
    @Max(value = 1000000, message = "Salary cannot exceed 1,000,000")
    private Double salary;
    
    private boolean active = true;
    
    // Default constructor
    public Employee() {}
    
    // Constructor with all fields
    public Employee(Long id, String name, String email, String department, Double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Double getSalary() {
        return salary;
    }
    
    public void setSalary(Double salary) {
        this.salary = salary;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", active=" + active +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Employee employee = (Employee) o;
        return id != null ? id.equals(employee.id) : employee.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}