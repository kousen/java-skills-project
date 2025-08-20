package com.oreilly.webservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Employee operations with business logic.
 * <p>
 * This demonstrates the refactored architecture:
 * - Service delegates to Repository for data access
 * - Service contains business logic and validation
 * - Clear separation of concerns between service and repository layers
 * - Business logic methods with proper validation and error handling
 * - Transactional boundaries for business operations
 */
@Service
@Transactional
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    private final EmployeeRepository employeeRepository;
    
    // Business constants
    private static final double MIN_SALARY = 30000.0;
    private static final double MAX_SALARY = 500000.0;
    private static final double STANDARD_RAISE_PERCENTAGE = 0.05; // 5%
    private static final double HIGH_PERFORMER_THRESHOLD = 80000.0;
    
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        logger.info("EmployeeService initialized with repository delegation");
    }
    
    // ========== BASIC CRUD OPERATIONS (delegating to repository) ==========
    
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }
    
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }
    
    @Transactional(readOnly = true)
    public long count() {
        return employeeRepository.count();
    }
    
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Employee> findByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }
    
    @Transactional(readOnly = true)
    public List<Employee> searchEmployees(String department, Double minSalary) {
        // Apply default minimum salary if not specified (business rule)
        Double effectiveMinSalary = (minSalary != null && minSalary > 0) ? minSalary : MIN_SALARY;
        
        return employeeRepository.findByDepartmentAndSalaryGreaterThanEqual(department, effectiveMinSalary);
    }
    
    // ========== BUSINESS LOGIC METHODS ==========
    
    /**
     * Process a new employee hire with business validation.
     */
    public Employee save(Employee employee) {
        if (employee.id() == null) {
            // New employee - apply business logic
            return processNewHire(employee);
        } else {
            // Existing employee - apply business validation
            return updateEmployee(employee.id(), employee);
        }
    }
    
    /**
     * Process a new employee hire with business validation.
     */
    public Employee processNewHire(Employee employee) {
        logger.info("Processing new hire: {}", employee.name());
        
        // Business validation
        validateEmployee(employee);
        
        // Ensure salary is within company range
        if (employee.salary() < MIN_SALARY) {
            throw new IllegalArgumentException(
                String.format("Salary %.2f is below minimum wage of %.2f", 
                    employee.salary(), MIN_SALARY));
        }
        
        // Save the new employee
        Employee savedEmployee = employeeRepository.save(employee);
        
        logger.info("Successfully hired employee {} with ID: {} in {} department", 
            savedEmployee.name(), savedEmployee.id(), savedEmployee.department());
        
        return savedEmployee;
    }
    
    /**
     * Update employee information with business validation.
     */
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee) {
        logger.info("Updating employee ID: {}", employeeId);
        
        // Verify employee exists
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException(employeeId);
        }
        
        // Create employee with correct ID
        Employee employeeWithId = updatedEmployee.withId(employeeId);
        
        // Business validation
        validateEmployee(employeeWithId);
        
        return employeeRepository.save(employeeWithId);
    }
    
    /**
     * Give employee a raise with business logic validation.
     */
    public Employee giveRaise(Long employeeId, double raiseAmount) {
        logger.info("Processing raise of ${} for employee ID: {}", raiseAmount, employeeId);
        
        Employee employee = findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        
        double newSalary = employee.salary() + raiseAmount;
        
        // Business validation
        if (raiseAmount <= 0) {
            throw new IllegalArgumentException("Raise amount must be positive");
        }
        
        if (newSalary > MAX_SALARY) {
            throw new IllegalArgumentException(
                String.format("New salary %.2f exceeds maximum of %.2f", 
                    newSalary, MAX_SALARY));
        }
        
        Employee updatedEmployee = employee.withSalary(newSalary);
        Employee savedEmployee = employeeRepository.save(updatedEmployee);
        
        logger.info("Employee {} received raise: ${} -> ${}", 
            employee.name(), employee.salary(), newSalary);
        
        return savedEmployee;
    }
    
    /**
     * Give standard percentage raise to employee.
     */
    public Employee giveStandardRaise(Long employeeId) {
        Employee employee = findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        
        double raiseAmount = employee.salary() * STANDARD_RAISE_PERCENTAGE;
        return giveRaise(employeeId, raiseAmount);
    }
    
    /**
     * Transfer employee to new department.
     */
    public Employee transferEmployee(Long employeeId, String newDepartment) {
        logger.info("Transferring employee ID: {} to {} department", employeeId, newDepartment);
        
        Employee employee = findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        
        String oldDepartment = employee.department();
        
        // Business validation
        if (newDepartment == null || newDepartment.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be null or empty");
        }
        
        if (oldDepartment.equalsIgnoreCase(newDepartment)) {
            throw new IllegalArgumentException("Employee is already in " + newDepartment + " department");
        }
        
        Employee transferredEmployee = new Employee(
            employee.id(), employee.name(), newDepartment, employee.salary());
        
        Employee savedEmployee = employeeRepository.save(transferredEmployee);
        
        logger.info("Successfully transferred {} from {} to {} department", 
            employee.name(), oldDepartment, newDepartment);
        
        return savedEmployee;
    }
    
    /**
     * Find high-performing employees based on salary threshold.
     */
    @Transactional(readOnly = true)
    public List<Employee> findHighPerformers() {
        logger.info("Finding high-performing employees (salary >= ${})", HIGH_PERFORMER_THRESHOLD);
        
        List<Employee> highPerformers = employeeRepository.findBySalaryGreaterThanEqual(HIGH_PERFORMER_THRESHOLD);
        
        logger.info("Found {} high-performing employees", highPerformers.size());
        return highPerformers;
    }
    
    /**
     * Calculate total salary expense for a department.
     */
    @Transactional(readOnly = true)
    public double calculateDepartmentSalaryExpense(String department) {
        logger.info("Calculating salary expense for {} department", department);
        
        List<Employee> departmentEmployees = employeeRepository.findByDepartment(department);
        
        double totalExpense = departmentEmployees.stream()
            .mapToDouble(Employee::salary)
            .sum();
        
        logger.info("{} department has {} employees with total salary expense: ${}", 
            department, departmentEmployees.size(), totalExpense);
        
        return totalExpense;
    }
    
    /**
     * Delete employee with business logic (termination process).
     */
    public void terminateEmployee(Long employeeId) {
        logger.info("Terminating employee ID: {}", employeeId);
        
        Employee employee = findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        
        // Business logic: Log termination
        logger.warn("Terminating employee: {} from {} department", 
            employee.name(), employee.department());
        
        employeeRepository.deleteById(employeeId);
        
        logger.info("Employee termination completed for ID: {}", employeeId);
    }
    
    // ========== PRIVATE BUSINESS VALIDATION METHODS ==========
    
    /**
     * Validate employee data according to business rules.
     */
    private void validateEmployee(Employee employee) {
        if (employee.salary() < MIN_SALARY || employee.salary() > MAX_SALARY) {
            throw new IllegalArgumentException(
                String.format("Salary %.2f must be between %.2f and %.2f", 
                    employee.salary(), MIN_SALARY, MAX_SALARY));
        }
    }
    
    // ========== TEST SUPPORT METHODS ==========
    
    /**
     * Clear all employees (for testing only).
     */
    public void deleteAll() {
        logger.warn("Deleting all employees - this should only be used in tests!");
        employeeRepository.deleteAll();
    }
}