package com.oreilly.employee.service;

import com.oreilly.employee.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Employee service layer containing business logic.
 * Demonstrates service layer pattern with in-memory storage.
 */
@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public EmployeeService() {
        // Initialize with sample data
        initializeSampleData();
    }
    
    /**
     * Find employee by ID.
     */
    public Optional<Employee> findById(Long id) {
        logger.debug("Finding employee by ID: {}", id);
        Employee employee = employees.get(id);
        if (employee != null) {
            logger.info("Found employee: {}", employee.getName());
        } else {
            logger.warn("Employee not found with ID: {}", id);
        }
        return Optional.ofNullable(employee);
    }
    
    /**
     * Get all employees.
     */
    public List<Employee> findAll() {
        logger.debug("Finding all employees");
        List<Employee> allEmployees = new ArrayList<>(employees.values());
        logger.info("Found {} employees", allEmployees.size());
        return allEmployees;
    }
    
    /**
     * Find employees by department.
     */
    public List<Employee> findByDepartment(String department) {
        logger.debug("Finding employees by department: {}", department);
        List<Employee> departmentEmployees = employees.values().stream()
            .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
            .collect(Collectors.toList());
        logger.info("Found {} employees in department: {}", departmentEmployees.size(), department);
        return departmentEmployees;
    }
    
    /**
     * Search employees by criteria.
     */
    public List<Employee> searchEmployees(String department, Double minSalary) {
        logger.debug("Searching employees - department: {}, minSalary: {}", department, minSalary);
        
        return employees.values().stream()
            .filter(emp -> department == null || emp.getDepartment().equalsIgnoreCase(department))
            .filter(emp -> minSalary == null || emp.getSalary() >= minSalary)
            .filter(Employee::isActive)
            .collect(Collectors.toList());
    }
    
    /**
     * Save (create or update) an employee.
     */
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            // Create new employee
            employee.setId(idGenerator.getAndIncrement());
            logger.info("Creating new employee: {} with ID: {}", employee.getName(), employee.getId());
        } else {
            // Update existing employee
            logger.info("Updating employee: {} with ID: {}", employee.getName(), employee.getId());
        }
        
        employees.put(employee.getId(), employee);
        return employee;
    }
    
    /**
     * Delete employee by ID.
     */
    public boolean deleteById(Long id) {
        logger.debug("Deleting employee by ID: {}", id);
        Employee removed = employees.remove(id);
        if (removed != null) {
            logger.info("Deleted employee: {}", removed.getName());
            return true;
        } else {
            logger.warn("Cannot delete - employee not found with ID: {}", id);
            return false;
        }
    }
    
    /**
     * Check if employee exists.
     */
    public boolean existsById(Long id) {
        boolean exists = employees.containsKey(id);
        logger.debug("Employee exists check for ID {}: {}", id, exists);
        return exists;
    }
    
    /**
     * Get employee count.
     */
    public long count() {
        long count = employees.size();
        logger.debug("Total employee count: {}", count);
        return count;
    }
    
    /**
     * Get department statistics.
     */
    public Map<String, Long> getDepartmentStatistics() {
        logger.debug("Calculating department statistics");
        
        Map<String, Long> stats = employees.values().stream()
            .filter(Employee::isActive)
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.counting()
            ));
        
        logger.info("Department statistics: {}", stats);
        return stats;
    }
    
    /**
     * Get salary statistics for a department.
     */
    public SalaryStatistics getSalaryStatistics(String department) {
        logger.debug("Calculating salary statistics for department: {}", department);
        
        List<Double> salaries = employees.values().stream()
            .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
            .filter(Employee::isActive)
            .map(Employee::getSalary)
            .collect(Collectors.toList());
        
        if (salaries.isEmpty()) {
            return new SalaryStatistics(0, 0.0, 0.0, 0.0, 0.0);
        }
        
        double sum = salaries.stream().mapToDouble(Double::doubleValue).sum();
        double average = sum / salaries.size();
        double min = salaries.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double max = salaries.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        
        SalaryStatistics stats = new SalaryStatistics(salaries.size(), sum, average, min, max);
        logger.info("Salary statistics for {}: {}", department, stats);
        return stats;
    }
    
    /**
     * Initialize with sample data for demonstration.
     */
    private void initializeSampleData() {
        logger.info("Initializing sample employee data");
        
        save(new Employee(null, "John Doe", "john.doe@company.com", "Engineering", 75000.0));
        save(new Employee(null, "Jane Smith", "jane.smith@company.com", "Marketing", 65000.0));
        save(new Employee(null, "Bob Johnson", "bob.johnson@company.com", "Engineering", 80000.0));
        save(new Employee(null, "Alice Brown", "alice.brown@company.com", "Sales", 60000.0));
        save(new Employee(null, "Charlie Wilson", "charlie.wilson@company.com", "Engineering", 90000.0));
        save(new Employee(null, "Diana Lee", "diana.lee@company.com", "Marketing", 70000.0));
        
        logger.info("Sample data initialized with {} employees", employees.size());
    }
    
    /**
     * Inner class for salary statistics.
     */
    public static class SalaryStatistics {
        private final int count;
        private final double total;
        private final double average;
        private final double minimum;
        private final double maximum;
        
        public SalaryStatistics(int count, double total, double average, double minimum, double maximum) {
            this.count = count;
            this.total = total;
            this.average = average;
            this.minimum = minimum;
            this.maximum = maximum;
        }
        
        // Getters
        public int getCount() { return count; }
        public double getTotal() { return total; }
        public double getAverage() { return average; }
        public double getMinimum() { return minimum; }
        public double getMaximum() { return maximum; }
        
        @Override
        public String toString() {
            return String.format("SalaryStatistics{count=%d, avg=%.2f, min=%.2f, max=%.2f}", 
                count, average, minimum, maximum);
        }
    }
}