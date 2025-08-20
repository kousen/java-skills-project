package com.oreilly.webservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for EmployeeRepository - data access layer.
 * <p>
 * This demonstrates:
 * - Testing repository layer in isolation
 * - CRUD operations testing
 * - Query method testing
 * - Data consistency validation
 */
class EmployeeRepositoryTest {
    
    private EmployeeRepository repository;
    private Employee testEmployee;
    
    @BeforeEach
    void setUp() {
        repository = new EmployeeRepository();
        testEmployee = new Employee("Test User", "Engineering", 75000.0);
        
        // Clear any existing data
        repository.deleteAll();
    }
    
    @Test
    void testSaveNewEmployee() {
        Employee savedEmployee = repository.save(testEmployee);
        
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.id()).isNotNull();
        assertThat(savedEmployee.name()).isEqualTo("Test User");
        assertThat(savedEmployee.department()).isEqualTo("Engineering");
        assertThat(savedEmployee.salary()).isEqualTo(75000.0);
    }
    
    @Test
    void testSaveExistingEmployee() {
        Employee savedEmployee = repository.save(testEmployee);
        Employee updatedEmployee = savedEmployee.withSalary(80000.0);
        
        Employee result = repository.save(updatedEmployee);
        
        assertThat(result.id()).isEqualTo(savedEmployee.id());
        assertThat(result.salary()).isEqualTo(80000.0);
        assertThat(repository.count()).isEqualTo(1); // Should not create duplicate
    }
    
    @Test
    void testFindById() {
        Employee savedEmployee = repository.save(testEmployee);
        
        Optional<Employee> found = repository.findById(savedEmployee.id());
        
        assertThat(found).isPresent();
        assertThat(found.get().name()).isEqualTo("Test User");
    }
    
    @Test
    void testFindByIdNotFound() {
        Optional<Employee> found = repository.findById(999L);
        
        assertThat(found).isEmpty();
    }
    
    @Test
    void testFindAll() {
        repository.save(new Employee("Alice", "Engineering", 70000.0));
        repository.save(new Employee("Bob", "Marketing", 65000.0));
        repository.save(new Employee("Charlie", "Sales", 72000.0));
        
        List<Employee> employees = repository.findAll();
        
        assertThat(employees).hasSize(3);
        assertThat(employees).extracting(Employee::name)
            .containsExactlyInAnyOrder("Alice", "Bob", "Charlie");
    }
    
    @Test
    void testFindByDepartment() {
        repository.save(new Employee("Alice", "Engineering", 70000.0));
        repository.save(new Employee("Bob", "Engineering", 75000.0));
        repository.save(new Employee("Charlie", "Marketing", 65000.0));
        
        List<Employee> engineeringEmployees = repository.findByDepartment("Engineering");
        
        assertThat(engineeringEmployees).hasSize(2);
        assertThat(engineeringEmployees).extracting(Employee::name)
            .containsExactlyInAnyOrder("Alice", "Bob");
    }
    
    @Test
    void testFindByDepartmentCaseInsensitive() {
        repository.save(new Employee("Alice", "Engineering", 70000.0));
        
        List<Employee> employees = repository.findByDepartment("engineering");
        
        assertThat(employees).hasSize(1);
        assertThat(employees.getFirst().name()).isEqualTo("Alice");
    }
    
    @Test
    void testFindBySalaryGreaterThanEqual() {
        repository.save(new Employee("Alice", "Engineering", 60000.0));
        repository.save(new Employee("Bob", "Engineering", 75000.0));
        repository.save(new Employee("Charlie", "Marketing", 90000.0));
        
        List<Employee> highEarners = repository.findBySalaryGreaterThanEqual(70000.0);
        
        assertThat(highEarners).hasSize(2);
        assertThat(highEarners).extracting(Employee::name)
            .containsExactlyInAnyOrder("Bob", "Charlie");
    }
    
    @Test
    void testFindByDepartmentAndSalaryGreaterThanEqual() {
        repository.save(new Employee("Alice", "Engineering", 60000.0));
        repository.save(new Employee("Bob", "Engineering", 80000.0));
        repository.save(new Employee("Charlie", "Marketing", 85000.0));
        repository.save(new Employee("David", "Engineering", 90000.0));
        
        List<Employee> results = repository.findByDepartmentAndSalaryGreaterThanEqual("Engineering", 75000.0);
        
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Employee::name)
            .containsExactlyInAnyOrder("Bob", "David");
    }
    
    @Test
    void testFindByDepartmentAndSalaryGreaterThanEqualWithNullDepartment() {
        repository.save(new Employee("Alice", "Engineering", 60000.0));
        repository.save(new Employee("Bob", "Marketing", 80000.0));
        repository.save(new Employee("Charlie", "Sales", 85000.0));
        
        List<Employee> results = repository.findByDepartmentAndSalaryGreaterThanEqual(null, 75000.0);
        
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Employee::name)
            .containsExactlyInAnyOrder("Bob", "Charlie");
    }
    
    @Test
    void testDeleteById() {
        Employee savedEmployee = repository.save(testEmployee);
        Long employeeId = savedEmployee.id();
        
        repository.deleteById(employeeId);
        
        assertThat(repository.findById(employeeId)).isEmpty();
        assertThat(repository.count()).isEqualTo(0);
    }
    
    @Test
    void testDeleteByIdNonExistent() {
        // Should not throw exception when deleting non-existent employee
        repository.deleteById(999L);
        
        assertThat(repository.count()).isEqualTo(0);
    }
    
    @Test
    void testExistsById() {
        Employee savedEmployee = repository.save(testEmployee);
        
        assertThat(repository.existsById(savedEmployee.id())).isTrue();
        assertThat(repository.existsById(999L)).isFalse();
    }
    
    @Test
    void testCount() {
        assertThat(repository.count()).isEqualTo(0);
        
        repository.save(new Employee("Alice", "Engineering", 70000.0));
        assertThat(repository.count()).isEqualTo(1);
        
        repository.save(new Employee("Bob", "Marketing", 65000.0));
        assertThat(repository.count()).isEqualTo(2);
    }
    
    @Test
    void testDeleteAll() {
        repository.save(new Employee("Alice", "Engineering", 70000.0));
        repository.save(new Employee("Bob", "Marketing", 65000.0));
        
        assertThat(repository.count()).isEqualTo(2);
        
        repository.deleteAll();
        
        assertThat(repository.count()).isEqualTo(0);
        assertThat(repository.findAll()).isEmpty();
    }
    
    @Test
    void testIdGenerationIsConsistent() {
        Employee emp1 = repository.save(new Employee("Alice", "Engineering", 70000.0));
        Employee emp2 = repository.save(new Employee("Bob", "Marketing", 65000.0));
        
        assertThat(emp1.id()).isNotNull();
        assertThat(emp2.id()).isNotNull();
        assertThat(emp1.id()).isNotEqualTo(emp2.id());
        
        // IDs should be sequential
        assertThat(emp2.id()).isGreaterThan(emp1.id());
    }
    
    @Test
    void testThreadSafety() throws InterruptedException {
        // Test concurrent operations don't cause data corruption
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                repository.save(new Employee("Employee" + i, "Engineering", 70000.0 + i));
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 10; i < 20; i++) {
                repository.save(new Employee("Employee" + i, "Marketing", 60000.0 + i));
            }
        });
        
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        
        assertThat(repository.count()).isEqualTo(20);
        
        // Verify no data corruption occurred
        List<Employee> allEmployees = repository.findAll();
        assertThat(allEmployees).hasSize(20);
        assertThat(allEmployees).extracting(Employee::name)
            .allMatch(name -> name.startsWith("Employee"));
    }
}