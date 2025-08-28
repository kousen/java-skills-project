package com.oreilly.microservices;

import java.time.LocalDateTime;

/**
 * Data Transfer Objects and Entities for Microservices module.
 * These records represent the core domain objects used across
 * the microservices demonstration.
 */
public class MicroserviceRecords {
    
    public record MicroserviceEmployee(
        Long id,
        String name,
        String email,
        Long departmentId,
        Double salary
    ) {}
    
    public record MicroserviceDepartment(
        Long id,
        String name,
        String description
    ) {}
    
    public record EmployeeWithDepartment(
        MicroserviceEmployee employee,
        MicroserviceDepartment department
    ) {}
    
    public record EmployeeCreatedEvent(
        Long employeeId,
        String employeeName,
        Long departmentId,
        LocalDateTime timestamp
    ) {}
}