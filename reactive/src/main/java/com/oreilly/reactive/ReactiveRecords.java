package com.oreilly.reactive;

/**
 * Java records for the Reactive Programming module
 * Using Java 21 record features for immutable data classes
 */
public class ReactiveRecords {
    
    /**
     * Employee record with all fields
     */
    public record ReactiveEmployee(
        Long id,
        String name,
        String department,
        Double salary
    ) {
        // Compact constructor for validation
        public ReactiveEmployee {
            if (salary != null && salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
        }
    }
    
    /**
     * Department information
     */
    public record ReactiveDepartment(
        Long id,
        String name,
        String description
    ) {}
    
    /**
     * Salary information with currency
     */
    public record ReactiveSalary(
        Long employeeId,
        Double amount,
        String currency
    ) {}
    
    /**
     * Complete employee profile combining multiple data sources
     */
    public record EmployeeProfile(
        ReactiveEmployee employee,
        ReactiveDepartment department,
        ReactiveSalary salary
    ) {}
    
    /**
     * Request/Response records for API
     */
    public record CreateEmployeeRequest(
        String name,
        String department,
        Double salary
    ) {
        public ReactiveEmployee toEmployee() {
            return new ReactiveEmployee(null, name, department, salary);
        }
    }
    
    public record EmployeeUpdateRequest(
        String name,
        String department,
        Double salary
    ) {}
    
    /**
     * Event record for reactive streams
     */
    public record EmployeeEvent(
        String eventType,
        Long employeeId,
        String employeeName,
        String timestamp
    ) {
        public static EmployeeEvent created(ReactiveEmployee employee) {
            return new EmployeeEvent(
                "CREATED",
                employee.id(),
                employee.name(),
                java.time.Instant.now().toString()
            );
        }
        
        public static EmployeeEvent updated(ReactiveEmployee employee) {
            return new EmployeeEvent(
                "UPDATED",
                employee.id(),
                employee.name(),
                java.time.Instant.now().toString()
            );
        }
        
        public static EmployeeEvent deleted(Long employeeId) {
            return new EmployeeEvent(
                "DELETED",
                employeeId,
                null,
                java.time.Instant.now().toString()
            );
        }
    }
    
}