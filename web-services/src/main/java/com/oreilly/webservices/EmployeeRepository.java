package com.oreilly.webservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repository layer for Employee data access operations.
 * <p>
 * This demonstrates:
 * - Repository pattern with @Repository annotation
 * - Pure data access operations (CRUD, queries)
 * - Thread-safe in-memory storage using ConcurrentHashMap
 * - No business logic - just data operations
 * - Separation of concerns from business logic
 */
@Repository
public class EmployeeRepository {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeRepository.class);
    
    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public EmployeeRepository() {
        // Initialize with some sample data for demonstration
        initializeSampleData();
        logger.info("EmployeeRepository initialized with {} employees", employees.size());
    }
    
    /**
     * Find employee by ID.
     * @param id Employee ID
     * @return Optional containing employee if found
     */
    public Optional<Employee> findById(Long id) {
        logger.debug("Finding employee with ID: {}", id);
        Employee employee = employees.get(id);
        if (employee != null) {
            logger.debug("Found employee: {}", employee.name());
        } else {
            logger.debug("Employee with ID {} not found", id);
        }
        return Optional.ofNullable(employee);
    }
    
    /**
     * Get all employees.
     * @return List of all employees
     */
    public List<Employee> findAll() {
        logger.debug("Retrieving all employees, count: {}", employees.size());
        return new ArrayList<>(employees.values());
    }
    
    /**
     * Find employees by department.
     * @param department Department name
     * @return List of employees in the department
     */
    public List<Employee> findByDepartment(String department) {
        logger.debug("Finding employees in department: {}", department);
        
        List<Employee> results = employees.values().stream()
            .filter(employee -> employee.department().equalsIgnoreCase(department))
            .collect(Collectors.toList());
            
        logger.debug("Found {} employees in {} department", results.size(), department);
        return results;
    }
    
    /**
     * Find employees with salary greater than or equal to minimum.
     * @param minSalary Minimum salary
     * @return List of employees meeting salary criteria
     */
    public List<Employee> findBySalaryGreaterThanEqual(Double minSalary) {
        logger.debug("Finding employees with salary >= {}", minSalary);
        
        List<Employee> results = employees.values().stream()
            .filter(employee -> employee.salary() >= minSalary)
            .collect(Collectors.toList());
            
        logger.debug("Found {} employees with salary >= {}", results.size(), minSalary);
        return results;
    }
    
    /**
     * Find employees by department and minimum salary.
     * @param department Department to filter by (optional)
     * @param minSalary Minimum salary to filter by (optional)
     * @return Filtered list of employees
     */
    public List<Employee> findByDepartmentAndSalaryGreaterThanEqual(String department, Double minSalary) {
        logger.debug("Finding employees with department: '{}', minSalary: {}", department, minSalary);
        
        List<Employee> results = employees.values().stream()
            .filter(employee -> department == null || 
                employee.department().equalsIgnoreCase(department))
            .filter(employee -> minSalary == null || 
                employee.salary() >= minSalary)
            .collect(Collectors.toList());
        
        logger.debug("Found {} matching employees", results.size());
        return results;
    }
    
    /**
     * Save employee (create or update).
     * @param employee Employee to save
     * @return Saved employee with generated ID if new
     */
    public Employee save(Employee employee) {
        Employee savedEmployee;
        if (employee.id() == null) {
            // Creating new employee
            Long newId = idGenerator.getAndIncrement();
            savedEmployee = employee.withId(newId);
            logger.debug("Creating new employee with ID: {}", newId);
        } else {
            // Updating existing employee
            savedEmployee = employee;
            logger.debug("Updating employee with ID: {}", employee.id());
        }
        
        employees.put(savedEmployee.id(), savedEmployee);
        logger.debug("Saved employee: {}", savedEmployee);
        return savedEmployee;
    }
    
    /**
     * Delete employee by ID.
     * Consistent with Spring Data JPA deleteById() method signature.
     * @param id Employee ID to delete
     */
    public void deleteById(Long id) {
        logger.debug("Attempting to delete employee with ID: {}", id);
        Employee removed = employees.remove(id);
        
        if (removed != null) {
            logger.debug("Successfully deleted employee: {}", removed.name());
        } else {
            logger.debug("Cannot delete - employee with ID {} not found", id);
        }
    }
    
    /**
     * Check if employee exists by ID.
     * @param id Employee ID
     * @return true if employee exists
     */
    public boolean existsById(Long id) {
        return employees.containsKey(id);
    }
    
    /**
     * Get total count of employees.
     * @return Number of employees
     */
    public long count() {
        return employees.size();
    }
    
    /**
     * Clear all employees (useful for testing).
     */
    public void deleteAll() {
        logger.warn("Deleting all employees - this should only be used in tests!");
        employees.clear();
        idGenerator.set(1);
    }
    
    /**
     * Initialize repository with sample data for demonstration.
     */
    private void initializeSampleData() {
        save(new Employee("John Doe", "Engineering", 75000.0));
        save(new Employee("Jane Smith", "Marketing", 65000.0));
        save(new Employee("Bob Johnson", "Engineering", 80000.0));
        save(new Employee("Alice Wilson", "Sales", 70000.0));
        save(new Employee("Charlie Brown", "HR", 60000.0));
    }
}