package com.oreilly.webservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integration tests for EmployeeService - business logic layer.
 * <p>
 * This demonstrates:
 * - Spring Boot integration testing with clean web-services application
 * - @MockitoBean for mocking repository dependencies
 * - Testing business logic with proper Spring context
 * - Validation logic and business rule testing
 * - Clean separation from microservices module
 */
@SpringBootTest(classes = {WebServicesApplication.class})
class EmployeeServiceTest {
    
    @MockitoBean
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeService employeeService;
    
    private Employee testEmployee;
    
    @BeforeEach
    void setUp() {
        testEmployee = new Employee(1L, "John Doe", "Engineering", 75000.0);
    }
    
    // ========== BASIC CRUD DELEGATION TESTS ==========
    
    @Test
    void testFindById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        
        Optional<Employee> result = employeeService.findById(1L);
        
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("John Doe");
        verify(employeeRepository).findById(1L);
    }
    
    @Test
    void testFindAll() {
        List<Employee> employees = List.of(testEmployee);
        when(employeeRepository.findAll()).thenReturn(employees);
        
        List<Employee> result = employeeService.findAll();
        
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("John Doe");
        verify(employeeRepository).findAll();
    }
    
    @Test
    void testExistsById() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        
        boolean result = employeeService.existsById(1L);
        
        assertThat(result).isTrue();
        verify(employeeRepository).existsById(1L);
    }
    
    @Test
    void testCount() {
        when(employeeRepository.count()).thenReturn(5L);
        
        long result = employeeService.count();
        
        assertThat(result).isEqualTo(5L);
        verify(employeeRepository).count();
    }
    
    // ========== BUSINESS LOGIC TESTS ==========
    
    @Test
    void testProcessNewHireSuccess() {
        Employee newEmployee = new Employee("Alice Johnson", "Engineering", 70000.0);
        Employee savedEmployee = new Employee(2L, "Alice Johnson", "Engineering", 70000.0);
        
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        
        Employee result = employeeService.processNewHire(newEmployee);
        
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo("Alice Johnson");
        verify(employeeRepository).save(newEmployee);
    }
    
    @Test
    void testProcessNewHireSalaryTooLow() {
        Employee newEmployee = new Employee("Alice Johnson", "Engineering", 25000.0);
        
        assertThatThrownBy(() -> employeeService.processNewHire(newEmployee))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("must be between");
        
        verify(employeeRepository, never()).save(any());
    }
    
    @Test
    void testProcessNewHireSalaryTooHigh() {
        Employee newEmployee = new Employee("Alice Johnson", "Engineering", 600000.0);
        
        assertThatThrownBy(() -> employeeService.processNewHire(newEmployee))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("must be between");
        
        verify(employeeRepository, never()).save(any());
    }
    
    @Test
    void testUpdateEmployeeSuccess() {
        Employee updatedEmployee = new Employee("John Updated", "Marketing", 80000.0);
        Employee savedEmployee = new Employee(1L, "John Updated", "Marketing", 80000.0);
        
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        
        Employee result = employeeService.updateEmployee(1L, updatedEmployee);
        
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("John Updated");
        assertThat(result.department()).isEqualTo("Marketing");
        verify(employeeRepository).existsById(1L);
        verify(employeeRepository).save(any(Employee.class));
    }
    
    @Test
    void testUpdateEmployeeNotFound() {
        Employee updatedEmployee = new Employee("John Updated", "Marketing", 80000.0);
        
        when(employeeRepository.existsById(999L)).thenReturn(false);
        
        assertThatThrownBy(() -> employeeService.updateEmployee(999L, updatedEmployee))
            .isInstanceOf(EmployeeNotFoundException.class);
        
        verify(employeeRepository).existsById(999L);
        verify(employeeRepository, never()).save(any());
    }
    
    @Test
    void testGiveRaiseSuccess() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        Employee employeeWithRaise = testEmployee.withSalary(80000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeWithRaise);
        
        Employee result = employeeService.giveRaise(1L, 5000.0);
        
        assertThat(result.salary()).isEqualTo(80000.0);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));
    }
    
    @Test
    void testGiveStandardRaise() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        Employee employeeWithRaise = testEmployee.withSalary(78750.0); // 5% raise
        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeWithRaise);
        
        Employee result = employeeService.giveStandardRaise(1L);
        
        assertThat(result.salary()).isEqualTo(78750.0);
        verify(employeeRepository, times(2)).findById(1L); // Called in both giveStandardRaise and giveRaise
        verify(employeeRepository).save(any(Employee.class));
    }
    
    @Test
    void testGiveRaiseValidationErrors() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        
        // Test negative raise
        assertThatThrownBy(() -> employeeService.giveRaise(1L, -1000.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("must be positive");
        
        // Test exceeds maximum 
        assertThatThrownBy(() -> employeeService.giveRaise(1L, 500000.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("exceeds maximum");
        
        verify(employeeRepository, never()).save(any());
    }
    
    @Test
    void testTransferEmployeeSuccess() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        Employee transferredEmployee = new Employee(1L, "John Doe", "Marketing", 75000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(transferredEmployee);
        
        Employee result = employeeService.transferEmployee(1L, "Marketing");
        
        assertThat(result.department()).isEqualTo("Marketing");
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));
    }
    
    @Test
    void testTransferEmployeeValidationErrors() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        
        // Test null department
        assertThatThrownBy(() -> employeeService.transferEmployee(1L, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null");
        
        // Test same department
        assertThatThrownBy(() -> employeeService.transferEmployee(1L, "Engineering"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("already in Engineering");
        
        verify(employeeRepository, never()).save(any());
    }
    
    @Test
    void testTerminateEmployeeSuccess() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        
        employeeService.terminateEmployee(1L);
        
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).deleteById(1L);
    }
    
    @Test
    void testTerminateEmployeeNotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> employeeService.terminateEmployee(999L))
            .isInstanceOf(EmployeeNotFoundException.class);
        
        verify(employeeRepository).findById(999L);
        verify(employeeRepository, never()).deleteById(any());
    }
    
    @Test
    void testFindHighPerformers() {
        List<Employee> highPerformers = List.of(
            new Employee(2L, "Alice", "Engineering", 85000.0),
            new Employee(3L, "Bob", "Marketing", 90000.0)
        );
        when(employeeRepository.findBySalaryGreaterThanEqual(80000.0)).thenReturn(highPerformers);
        
        List<Employee> result = employeeService.findHighPerformers();
        
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Employee::name)
            .containsExactlyInAnyOrder("Alice", "Bob");
        verify(employeeRepository).findBySalaryGreaterThanEqual(80000.0);
    }
    
    @Test
    void testCalculateDepartmentSalaryExpense() {
        List<Employee> engineeringEmployees = List.of(
            new Employee(1L, "John", "Engineering", 75000.0),
            new Employee(2L, "Alice", "Engineering", 85000.0)
        );
        when(employeeRepository.findByDepartment("Engineering")).thenReturn(engineeringEmployees);
        
        double result = employeeService.calculateDepartmentSalaryExpense("Engineering");
        
        assertThat(result).isEqualTo(160000.0);
        verify(employeeRepository).findByDepartment("Engineering");
    }
    
    @Test
    void testDeleteById() {
        Long employeeId = 1L;
        
        employeeService.deleteById(employeeId);
        
        verify(employeeRepository).deleteById(employeeId);
    }
    
    @Test
    void testDeleteAll() {
        employeeService.deleteAll();
        
        verify(employeeRepository).deleteAll();
    }
}