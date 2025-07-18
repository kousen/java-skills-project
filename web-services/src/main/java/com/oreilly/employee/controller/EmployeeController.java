package com.oreilly.employee.controller;

import com.oreilly.employee.model.Employee;
import com.oreilly.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Employee operations.
 * Demonstrates Spring Boot REST API creation with proper HTTP status codes,
 * validation, and error handling.
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*") // Allow CORS for development
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    
    private final EmployeeService employeeService;
    
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    /**
     * GET /api/employees/{id} - Get employee by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        logger.info("GET request for employee ID: {}", id);
        
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        } else {
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
    }
    
    /**
     * GET /api/employees - Get all employees or filter by department
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) String department) {
        
        logger.info("GET request for employees, department filter: {}", department);
        
        List<Employee> employees;
        if (department != null && !department.trim().isEmpty()) {
            employees = employeeService.findByDepartment(department);
        } else {
            employees = employeeService.findAll();
        }
        
        return ResponseEntity.ok(employees);
    }
    
    /**
     * GET /api/employees/search - Search employees with multiple criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minSalary) {
        
        logger.info("Search request - department: {}, minSalary: {}", department, minSalary);
        
        List<Employee> employees = employeeService.searchEmployees(department, minSalary);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * POST /api/employees - Create new employee
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        logger.info("POST request to create employee: {}", employee.getName());
        
        // Ensure ID is null for new employees
        employee.setId(null);
        
        Employee savedEmployee = employeeService.save(employee);
        logger.info("Created employee with ID: {}", savedEmployee.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }
    
    /**
     * PUT /api/employees/{id} - Update existing employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id, 
            @Valid @RequestBody Employee employee) {
        
        logger.info("PUT request to update employee ID: {}", id);
        
        if (!employeeService.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
        
        employee.setId(id);
        Employee updatedEmployee = employeeService.save(employee);
        
        return ResponseEntity.ok(updatedEmployee);
    }
    
    /**
     * DELETE /api/employees/{id} - Delete employee
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("DELETE request for employee ID: {}", id);
        
        if (!employeeService.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
        
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * GET /api/employees/count - Get total employee count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getEmployeeCount() {
        logger.info("GET request for employee count");
        
        long count = employeeService.count();
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    /**
     * GET /api/employees/departments/stats - Get department statistics
     */
    @GetMapping("/departments/stats")
    public ResponseEntity<Map<String, Long>> getDepartmentStatistics() {
        logger.info("GET request for department statistics");
        
        Map<String, Long> stats = employeeService.getDepartmentStatistics();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * GET /api/employees/departments/{department}/salary-stats - Get salary statistics for department
     */
    @GetMapping("/departments/{department}/salary-stats")
    public ResponseEntity<EmployeeService.SalaryStatistics> getSalaryStatistics(
            @PathVariable String department) {
        
        logger.info("GET request for salary statistics in department: {}", department);
        
        EmployeeService.SalaryStatistics stats = employeeService.getSalaryStatistics(department);
        if (stats.getCount() == 0) {
            throw new EmployeeNotFoundException("No employees found in department: " + department);
        }
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * POST /api/employees/batch - Create multiple employees
     */
    @PostMapping("/batch")
    public ResponseEntity<List<Employee>> createEmployees(@Valid @RequestBody List<Employee> employees) {
        logger.info("POST request to create {} employees", employees.size());
        
        List<Employee> savedEmployees = employees.stream()
            .peek(emp -> emp.setId(null)) // Ensure new employees
            .map(employeeService::save)
            .toList();
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployees);
    }
    
    /**
     * Custom exception for employee not found scenarios.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(String message) {
            super(message);
        }
    }
}