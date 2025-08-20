package com.oreilly.webservices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SOLUTION: Employee Search REST Controller
 * 
 * This is the complete solution for the EmployeeSearchController exercise.
 * Students should implement their own version in EmployeeSearchController.java
 * 
 * This solution shows:
 * - Constructor injection
 * - GET endpoints with path variables
 * - POST endpoints with request body
 * - Stream processing for filtering
 * - ResponseEntity usage
 */
@RestController
@RequestMapping("/api/search/solution")
public class EmployeeSearchControllerSolution {
    
    private final EmployeeService employeeService;
    
    /**
     * Constructor injection - Spring automatically provides EmployeeService
     */
    public EmployeeSearchControllerSolution(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    /**
     * Search employees by department using path variable
     * GET /api/search/solution/department/{department}
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Employee>> searchByDepartment(@PathVariable String department) {
        List<Employee> employees = employeeService.findByDepartment(department);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * Advanced search using request body with multiple criteria
     * POST /api/search/solution/advanced
     */
    @PostMapping("/advanced")
    public ResponseEntity<List<Employee>> advancedSearch(@RequestBody SearchCriteria criteria) {
        List<Employee> employees = employeeService.findAll();
        
        // Filter by criteria using streams
        List<Employee> filteredEmployees = employees.stream()
            .filter(emp -> criteria.department() == null || 
                          emp.department().equalsIgnoreCase(criteria.department()))
            .filter(emp -> criteria.minSalary() == null || 
                          emp.salary() >= criteria.minSalary())
            .filter(emp -> criteria.maxSalary() == null || 
                          emp.salary() <= criteria.maxSalary())
            .filter(emp -> criteria.nameContains() == null || 
                          emp.name().toLowerCase().contains(criteria.nameContains().toLowerCase()))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(filteredEmployees);
    }
    
    /**
     * Get all unique department names
     * GET /api/search/solution/departments
     */
    @GetMapping("/departments")
    public ResponseEntity<Set<String>> getUniqueDepartments() {
        List<Employee> employees = employeeService.findAll();
        Set<String> departments = employees.stream()
            .map(Employee::department)
            .collect(Collectors.toSet());
        
        return ResponseEntity.ok(departments);
    }
}