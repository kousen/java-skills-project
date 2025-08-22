# Try It Out: Security Validation Exercise

## Overview
In this hands-on exercise, you'll implement secure input validation by bridging the Employee model from Topic 21 (REST services) with the enhanced security validation from Topic 22. You'll create a secure data transformation service that validates and converts between models while preventing security vulnerabilities.

## Learning Objectives
After completing this exercise, you will:
- Transform data between domain models securely
- Implement comprehensive validation with both field and business rules
- Prevent SQL injection and XSS attacks through validation
- Write test-driven security validation
- Handle validation errors in REST controllers

## Exercise: Secure Employee Data Processor

### Background
Your company needs to process employee data from external sources (using the enhanced EmployeeDto with email, phone, and address) and convert it to the internal Employee model (from Topic 21) while ensuring all security validations pass.

### Your Task
Create a `SecureEmployeeProcessor` class that:
1. Validates incoming EmployeeDto data using InputValidator
2. Transforms valid data to Employee records
3. Handles validation errors appropriately
4. Provides comprehensive test coverage

### Step 1: Create the SecureEmployeeProcessor Class

Create `/security/src/main/java/com/oreilly/security/SecureEmployeeProcessor.java`:

```java
package com.oreilly.security;

import com.oreilly.webservices.Employee;
import java.util.List;
import java.util.Optional;

/**
 * TODO: Implement this class with the following methods:
 * 
 * 1. processEmployee(EmployeeDto dto) -> ProcessingResult
 * 2. private Employee convertToEmployee(EmployeeDto dto)
 * 3. validateAndProcess(List<EmployeeDto> employees) -> List<ProcessingResult>
 */
public class SecureEmployeeProcessor {
    private final InputValidator validator = new InputValidator();
    
    // TODO: Implement your methods here
}
```

### Step 2: Create the ProcessingResult Record

Create a result record to encapsulate validation outcomes:

```java
/**
 * TODO: Create this record to represent processing results
 * 
 * Should include:
 * - boolean success
 * - Optional<Employee> employee (only present if successful)
 * - List<String> validationErrors
 * - List<String> businessRuleErrors
 */
public record ProcessingResult(
    // TODO: Add your fields here
) {
    // TODO: Add convenience methods like isSuccess(), hasErrors()
}
```

### Step 3: Implementation Requirements

#### Method 1: processEmployee
```java
public ProcessingResult processEmployee(EmployeeDto dto) {
    // TODO: Implement this method
    // 1. Validate the EmployeeDto using InputValidator
    // 2. Check business rules
    // 3. If valid, convert to Employee
    // 4. Return ProcessingResult with appropriate data
}
```

#### Method 2: convertToEmployee
```java
private Employee convertToEmployee(EmployeeDto dto) {
    // TODO: Convert EmployeeDto to Employee record
    // Map: name -> name, salary -> salary
    // Derive department from email domain or use "Unknown"
    // Remember: Employee(String name, String department, Double salary)
}
```

#### Method 3: validateAndProcess (Batch Processing)
```java
public List<ProcessingResult> validateAndProcess(List<EmployeeDto> employees) {
    // TODO: Process multiple employees
    // Use Stream API for modern Java approach
    // Return list of ProcessingResult objects
}
```

### Step 4: Write Comprehensive Tests

Create `/security/src/test/java/com/oreilly/security/SecureEmployeeProcessorTest.java`:

```java
@DisplayName("Secure Employee Processor Tests")
class SecureEmployeeProcessorTest {
    private SecureEmployeeProcessor processor;
    
    @BeforeEach
    void setUp() {
        processor = new SecureEmployeeProcessor();
    }
    
    @Test
    @DisplayName("Valid employee should process successfully")
    void validEmployeeShouldProcessSuccessfully() {
        // TODO: Test with valid EmployeeDto
        // Verify: success=true, employee present, no errors
    }
    
    @Test
    @DisplayName("Invalid email should return validation error")
    void invalidEmailShouldReturnValidationError() {
        // TODO: Test with invalid email format
        // Verify: success=false, validation errors present
    }
    
    @Test
    @DisplayName("SQL injection attempt should be blocked")
    void sqlInjectionAttemptShouldBeBlocked() {
        // TODO: Test with malicious input like "Robert'; DROP TABLE employees; --"
        // Verify: validation catches SQL injection
    }
    
    @Test
    @DisplayName("XSS attempt should be blocked")
    void xssAttemptShouldBeBlocked() {
        // TODO: Test with XSS payload like "<script>alert('XSS')</script>"
        // Verify: validation catches XSS attempt
    }
    
    @Test
    @DisplayName("High salary should trigger business rule")
    void highSalaryShouldTriggerBusinessRule() {
        // TODO: Test with salary > EXECUTIVE_SALARY_THRESHOLD
        // Verify: business rule error present
    }
    
    @Test
    @DisplayName("Batch processing should handle mixed results")
    void batchProcessingShouldHandleMixedResults() {
        // TODO: Test with list containing valid and invalid employees
        // Verify: appropriate results for each
    }
}
```

### Step 5: Integration with REST Controller

Enhance the SecurityController to use your processor:

```java
@PostMapping("/process")
public ResponseEntity<ProcessingResult> processEmployee(
        @RequestBody EmployeeDto employee) {
    // TODO: Use SecureEmployeeProcessor
    // Return appropriate HTTP status based on result
}

@PostMapping("/process-batch")
public ResponseEntity<List<ProcessingResult>> processBatch(
        @RequestBody List<EmployeeDto> employees) {
    // TODO: Process multiple employees
    // Consider: How to handle partial success scenarios
}
```

## Test Data for Validation

Use these test cases to verify your implementation:

### Valid Employee
```java
new EmployeeDto(
    "Alice Johnson", 
    "alice.johnson@company.com", 
    75000.0, 
    "+1-555-123-4567", 
    "123 Main St"
)
```

### SQL Injection Attempts
```java
new EmployeeDto(
    "Robert'; DROP TABLE employees; --", 
    "test@company.com", 
    50000.0
)
```

### XSS Attempts
```java
new EmployeeDto(
    "<script>alert('XSS')</script>", 
    "test@company.com", 
    50000.0
)
```

### Business Rule Violations
```java
new EmployeeDto(
    "High Earner", 
    "earner@external.com",  // Not allowed domain
    600000.0                // Requires executive approval
)
```

## Expected Learning Outcomes

By completing this exercise, you'll understand:

1. **Defensive Programming**: How to validate data at system boundaries
2. **Security Layers**: Multiple validation checks (format, injection, business rules)
3. **Modern Java**: Records, Optional, Stream API for clean code
4. **Test-Driven Security**: Comprehensive test coverage for security scenarios
5. **Error Handling**: Structured error reporting without exposing internals

## Bonus Challenges

1. **Logging Integration**: Add security event logging for failed validations
2. **Metrics**: Count validation failures by type for monitoring
3. **Configuration**: Make validation rules configurable via properties
4. **Performance**: Benchmark validation performance with large datasets

## Solution Guidelines

Your implementation should demonstrate:
- Clean separation between validation and conversion logic
- Comprehensive error handling without information leakage
- Modern Java features (records, streams, optionals)
- Thorough test coverage including edge cases
- Integration with existing InputValidator and Employee models

Remember: Security validation should be strict but user-friendly, providing clear feedback about what went wrong without revealing system internals.