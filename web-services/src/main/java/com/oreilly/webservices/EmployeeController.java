package com.oreilly.webservices;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Employee operations.
 * <p>
 * This demonstrates:
 * - @RestController for REST endpoint creation
 * - All CRUD operations (Create, Read, Update, Delete)
 * - Path variables and request parameters
 * - Request body binding with validation
 * - Proper HTTP status codes and response handling
 * - Service layer integration with dependency injection
 * - Query parameter handling for filtering
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*", maxAge = 3600) // Allow CORS for frontend integration
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    
    private final EmployeeService employeeService;
    
    // Constructor injection (preferred over field injection)
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
        logger.info("EmployeeController initialized");
    }
    
    /**
     * Simple hello endpoint for testing.
     * GET /api/employees/hello
     */
    @GetMapping("/hello")
    public String hello() {
        logger.debug("Hello endpoint called");
        return "Welcome to Employee Service!";
    }
    
    /**
     * Get all employees with optional filtering.
     * GET /api/employees
     * GET /api/employees?department=Engineering
     * GET /api/employees?minSalary=70000
     * GET /api/employees?department=Engineering&minSalary=70000
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") Double minSalary) {
        
        logger.debug("Getting employees with department: '{}', minSalary: {}", 
            department, minSalary);
        
        List<Employee> employees;
        if (department != null || minSalary > 0) {
            employees = employeeService.searchEmployees(department, minSalary);
            logger.info("Found {} employees matching search criteria", employees.size());
        } else {
            employees = employeeService.findAll();
            logger.info("Retrieved all {} employees", employees.size());
        }
        
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(employees.size()))
            .body(employees);
    }
    
    /**
     * Get employee by ID.
     * GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        logger.debug("Getting employee with ID: {}", id);
        
        Employee employee = employeeService.findById(id)
            .orElseThrow(() -> {
                logger.warn("Employee not found with ID: {}", id);
                return new EmployeeNotFoundException(id);
            });
        
        logger.info("Retrieved employee: {}", employee.name());
        return ResponseEntity.ok()
            .header("X-Employee-Version", "1.0")
            .body(employee);
    }
    
    /**
     * Create new employee.
     * POST /api/employees
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        logger.info("Creating new employee: {}", employee.name());
        
        // Ensure ID is null for new employees (create new instance if needed)
        Employee newEmployee = employee.id() == null ? employee : 
            new Employee(employee.name(), employee.department(), employee.salary());
        
        Employee savedEmployee = employeeService.save(newEmployee);
        logger.info("Processed new hire with ID: {}", savedEmployee.id());
        
        // Build absolute URI from current request + new resource ID
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedEmployee.id())
            .toUri();
            
        return ResponseEntity.created(location).body(savedEmployee);
    }
    
    /**
     * Update existing employee.
     * PUT /api/employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee employee) {
        
        logger.info("Updating employee with ID: {}", id);
        
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        logger.info("Updated employee: {}", updatedEmployee.name());
        
        return ResponseEntity.ok(updatedEmployee);
    }
    
    /**
     * Delete employee.
     * DELETE /api/employees/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        
        employeeService.terminateEmployee(id);
        logger.info("Successfully terminated employee with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get employees by department.
     * GET /api/employees/department/{department}
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable String department) {
        logger.debug("Getting employees in department: {}", department);
        
        List<Employee> employees = employeeService.findByDepartment(department);
        logger.info("Found {} employees in {} department", employees.size(), department);
        
        return ResponseEntity.ok()
            .header("X-Department", department)
            .header("X-Count", String.valueOf(employees.size()))
            .body(employees);
    }
    
    /**
     * Get employee count.
     * GET /api/employees/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getEmployeeCount() {
        long count = employeeService.count();
        logger.debug("Total employee count: {}", count);
        
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    /**
     * Health check endpoint.
     * GET /api/employees/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "EmployeeService",
            "employeeCount", String.valueOf(employeeService.count())
        ));
    }
    
    // ========== BUSINESS LOGIC ENDPOINTS ==========
    
    /**
     * Give employee a raise with specified amount.
     * PUT /api/employees/{id}/raise?amount={amount}
     */
    @PutMapping("/{id}/raise")
    public ResponseEntity<Employee> giveRaise(
            @PathVariable Long id,
            @RequestParam Double amount) {
        logger.info("Processing raise of ${} for employee ID: {}", amount, id);
        
        Employee updatedEmployee = employeeService.giveRaise(id, amount);
        
        return ResponseEntity.ok()
            .header("X-Raise-Amount", String.valueOf(amount))
            .body(updatedEmployee);
    }
    
    /**
     * Give employee a standard 5% raise.
     * PUT /api/employees/{id}/standard-raise
     */
    @PutMapping("/{id}/standard-raise")
    public ResponseEntity<Employee> giveStandardRaise(@PathVariable Long id) {
        logger.info("Processing standard raise for employee ID: {}", id);
        
        Employee updatedEmployee = employeeService.giveStandardRaise(id);
        
        return ResponseEntity.ok()
            .header("X-Standard-Raise", "5%")
            .body(updatedEmployee);
    }
    
    /**
     * Transfer employee to new department.
     * PUT /api/employees/{id}/transfer?department={department}
     */
    @PutMapping("/{id}/transfer")
    public ResponseEntity<Employee> transferEmployee(
            @PathVariable Long id,
            @RequestParam String department) {
        logger.info("Transferring employee ID: {} to {} department", id, department);
        
        Employee transferredEmployee = employeeService.transferEmployee(id, department);
        
        return ResponseEntity.ok()
            .header("X-New-Department", department)
            .body(transferredEmployee);
    }
    
    /**
     * Get high-performing employees.
     * GET /api/employees/high-performers
     */
    @GetMapping("/high-performers")
    public ResponseEntity<List<Employee>> getHighPerformers() {
        logger.info("Getting high-performing employees");
        
        List<Employee> highPerformers = employeeService.findHighPerformers();
        
        return ResponseEntity.ok()
            .header("X-High-Performers-Count", String.valueOf(highPerformers.size()))
            .body(highPerformers);
    }
    
    /**
     * Get total salary expense for a department.
     * GET /api/employees/department/{department}/expense
     */
    @GetMapping("/department/{department}/expense")
    public ResponseEntity<Map<String, Object>> getDepartmentExpense(@PathVariable String department) {
        logger.info("Calculating salary expense for {} department", department);
        
        double totalExpense = employeeService.calculateDepartmentSalaryExpense(department);
        
        return ResponseEntity.ok()
            .header("X-Department", department)
            .body(Map.of(
                "department", department,
                "totalSalaryExpense", totalExpense
            ));
    }
    
    /**
     * Find employees by query string (alternative implementation).
     * GET /api/employees/find?query={query}
     */
    @GetMapping("/find")
    public ResponseEntity<List<Employee>> findEmployees(@RequestParam String query) {
        logger.info("Finding employees with query: {}", query);
        
        // Simple search implementation
        List<Employee> results = employeeService.findAll().stream()
            .filter(emp -> emp.name().toLowerCase().contains(query.toLowerCase()))
            .toList();
        
        return ResponseEntity.ok()
            .header("X-Search-Query", query)
            .header("X-Result-Count", String.valueOf(results.size()))
            .body(results);
    }

    /**
     * Search employees by name with pagination support.
     * GET /api/employees/search?name={name}&page={page}&size={size}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Searching employees with name containing: '{}', page: {}, size: {}", name, page, size);

        // Use findAll() and filter - matches what's actually available
        List<Employee> employees = employeeService.findAll().stream()
                .filter(emp -> emp.name().toLowerCase().contains(name.toLowerCase()))
                .toList();

        // Apply pagination manually for demo purposes
        int start = page * size;
        int end = Math.min(start + size, employees.size());
        List<Employee> paginatedEmployees = start < employees.size() ?
                employees.subList(start, end) : List.of();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(employees.size()))
                .header("X-Page", String.valueOf(page))
                .header("X-Size", String.valueOf(size))
                .body(paginatedEmployees);
    }
}