package com.oreilly.webservices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Try It Out Exercise: Employee Search REST Controller
 * <p>
 * This exercise helps you practice REST controller fundamentals:
 * - Constructor injection
 * - GET endpoints with path variables
 * - POST endpoints with request body
 * - ResponseEntity for proper HTTP responses
 * - Reusing existing service layer methods
 * <p>
 * Complete the TODOs below to implement the search functionality.
 * You'll reuse the existing EmployeeService - no new business logic needed!
 */
@RestController
@RequestMapping("/api/search")
public class EmployeeSearchController {
    
    private final EmployeeService employeeService;
    
    /**
     * TODO #1: Add constructor injection
     * <p>
     * Create a constructor that takes EmployeeService as a parameter
     * and assigns it to the employeeService field.
     * <p>
     * Remember: Spring will automatically inject the EmployeeService bean.
     */
    // TODO: Replace this placeholder constructor with proper injection
    public EmployeeSearchController() {
        this.employeeService = null; // TODO: Fix this with constructor injection
    }
    
    /**
     * TODO #2: Search employees by department
     * <p>
     * Create a GET endpoint that accepts a department name as a path variable
     * and returns all employees in that department.
     * <p>
     * URL pattern: GET /api/search/department/{department}
     * <p>
     * Steps:
     * 1. Add @GetMapping annotation with the path pattern
     * 2. Add method parameter with @PathVariable annotation
     * 3. Call employeeService.findByDepartment() 
     * 4. Return ResponseEntity.ok() with the results
     * <p>
     * Example: GET /api/search/department/Engineering
     */
    // TODO: Add @GetMapping annotation
    public ResponseEntity<List<Employee>> searchByDepartment(/* TODO: Add @PathVariable parameter */) {
        // TODO: Call employeeService.findByDepartment() and return results
        return null;
    }
    
    /**
     * TODO #3: Advanced search with multiple criteria
     * <p>
     * Create a POST endpoint that accepts SearchCriteria in the request body
     * and returns employees matching the criteria.
     * <p>
     * URL pattern: POST /api/search/advanced
     * <p>
     * Steps:
     * 1. Add @PostMapping annotation
     * 2. Add method parameter with @RequestBody annotation
     * 3. Use the SearchCriteria fields to filter employees
     * 4. Return ResponseEntity.ok() with filtered results
     * <p>
     * Hints:
     * - Start with employeeService.findAll()
     * - Use Java streams to filter by each non-null criteria field
     * - For nameContains: use String.toLowerCase() and contains()
     * - For salary range: check salary >= minSalary && salary <= maxSalary
     * <p>
     * Example JSON body:
     * {
     *   "department": "Engineering",
     *   "minSalary": 70000,
     *   "maxSalary": 90000,
     *   "nameContains": "alice"
     * }
     */
    // TODO: Add @PostMapping annotation
    public ResponseEntity<List<Employee>> advancedSearch(/* TODO: Add @RequestBody parameter */) {
        // TODO: Implement filtering logic using SearchCriteria
        // Hint: Start with List<Employee> employees = employeeService.findAll();
        // Then use streams to filter by non-null criteria fields
        return null;
    }
    
    /**
     * TODO #4: Get unique departments
     * <p>
     * Create a GET endpoint that returns a set of all unique department names.
     * <p>
     * URL pattern: GET /api/search/departments
     * <p>
     * Steps:
     * 1. Add @GetMapping annotation 
     * 2. Call employeeService.findAll()
     * 3. Extract unique department names using streams
     * 4. Return ResponseEntity.ok() with the Set<String>
     * <p>
     * Hint: Use .stream().map(Employee::department).collect(Collectors.toSet())
     */
    // TODO: Add @GetMapping annotation
    public ResponseEntity<Set<String>> getUniqueDepartments() {
        // TODO: Get all employees and extract unique departments
        return null;
    }
}