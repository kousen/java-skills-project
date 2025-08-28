package com.oreilly.reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.oreilly.reactive.ReactiveRecords.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for ReactiveEmployeeService using StepVerifier
 * Demonstrates testing reactive streams with Project Reactor
 */
class ReactiveEmployeeServiceTest {
    
    private ReactiveEmployeeService service;
    
    @BeforeEach
    void setUp() {
        service = new ReactiveEmployeeService();
    }
    
    @Test
    @DisplayName("Should find employee by ID")
    void testFindById() {
        // Test finding existing employee
        Mono<ReactiveEmployee> employeeMono = service.findById(1L);
        
        StepVerifier.create(employeeMono)
            .assertNext(employee -> {
                assertThat(employee.id()).isEqualTo(1L);
                assertThat(employee.name()).isEqualTo("Alice Johnson");
                assertThat(employee.department()).isEqualTo("Engineering");
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should return error when employee not found")
    void testFindByIdNotFound() {
        Mono<ReactiveEmployee> employeeMono = service.findById(999L);
        
        StepVerifier.create(employeeMono)
            .expectError(RuntimeException.class)
            .verify();
    }
    
    @Test
    @DisplayName("Should return all employees as Flux")
    void testFindAll() {
        Flux<ReactiveEmployee> employeeFlux = service.findAll();
        
        StepVerifier.create(employeeFlux)
            .expectNextCount(5) // We initialized with 5 employees
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should filter employees by department")
    void testFindByDepartment() {
        Flux<ReactiveEmployee> engineeringFlux = service.findByDepartment("Engineering");
        
        StepVerifier.create(engineeringFlux)
            .assertNext(emp -> assertThat(emp.name()).isEqualTo("Alice Johnson"))
            .assertNext(emp -> assertThat(emp.name()).isEqualTo("Carol Davis"))
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should save new employee and emit event")
    void testSaveEmployee() {
        var newEmployee = new ReactiveEmployee(null, "John Doe", "IT", 75000.0);
        
        // Subscribe to events before saving
        StepVerifier.create(service.getEventStream())
            .then(() -> service.save(newEmployee).subscribe())
            .assertNext(event -> {
                assertThat(event.eventType()).isEqualTo("CREATED");
                assertThat(event.employeeName()).isEqualTo("John Doe");
            })
            .thenCancel()
            .verify();
    }
    
    @Test
    @DisplayName("Should update employee")
    void testUpdateEmployee() {
        var updateRequest = new EmployeeUpdateRequest("Alice Updated", null, 95000.0);
        
        Mono<ReactiveEmployee> updatedMono = service.update(1L, updateRequest);
        
        StepVerifier.create(updatedMono)
            .assertNext(employee -> {
                assertThat(employee.name()).isEqualTo("Alice Updated");
                assertThat(employee.salary()).isEqualTo(95000.0);
                assertThat(employee.department()).isEqualTo("Engineering"); // Unchanged
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should delete employee and emit event")
    void testDeleteEmployee() {
        StepVerifier.create(service.getEventStream())
            .then(() -> service.delete(1L).subscribe())
            .assertNext(event -> {
                assertThat(event.eventType()).isEqualTo("DELETED");
                assertThat(event.employeeId()).isEqualTo(1L);
            })
            .thenCancel()
            .verify();
    }
    
    @Test
    @DisplayName("Should search employees with filters")
    void testSearchEmployees() {
        // Search for "john" in name, Engineering department, min salary 80000
        Flux<ReactiveEmployee> searchResults = service.searchEmployees(
            "john",      // name contains
            "Engineering", // department  
            80000.0       // min salary
        );
        
        StepVerifier.create(searchResults)
            .assertNext(emp -> {
                assertThat(emp.name()).isEqualTo("Alice Johnson");
                assertThat(emp.department()).isEqualTo("Engineering");
                assertThat(emp.salary()).isGreaterThanOrEqualTo(80000.0);
            })
            // Carol Davis has no "john" in her name, so she won't match
            .verifyComplete();
        
        // Test without name filter
        Flux<ReactiveEmployee> engineeringHighEarners = service.searchEmployees(
            null,         // no name filter
            "Engineering", // department
            80000.0       // min salary
        );
        
        StepVerifier.create(engineeringHighEarners)
            .expectNextCount(2) // Alice and Carol
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should get complete employee profile")
    void testGetCompleteProfile() {
        Mono<EmployeeProfile> profileMono = service.getCompleteProfile(1L);
        
        StepVerifier.create(profileMono)
            .assertNext(profile -> {
                assertThat(profile.employee().name()).isEqualTo("Alice Johnson");
                assertThat(profile.department().name()).isEqualTo("Engineering");
                assertThat(profile.salary().amount()).isEqualTo(85000.0);
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should calculate department statistics")
    void testDepartmentStatistics() {
        Mono<java.util.Map<String, Object>> statsMono = 
            service.getDepartmentStatistics("Engineering");
        
        StepVerifier.create(statsMono)
            .assertNext(stats -> {
                assertThat(stats.get("department")).isEqualTo("Engineering");
                assertThat(stats.get("employeeCount")).isEqualTo(2);
                assertThat((Double) stats.get("averageSalary")).isEqualTo(87500.0);
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should give raises to entire department")
    void testGiveRaiseToDepartment() {
        Flux<ReactiveEmployee> updatedEmployees = 
            service.giveRaiseToDepartment("Marketing", 10.0);
        
        StepVerifier.create(updatedEmployees)
            .assertNext(emp -> {
                assertThat(emp.name()).isEqualTo("Bob Smith");
                assertThat(emp.salary()).isEqualTo(77000.0); // 70000 * 1.1
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should handle delays with virtual time")
    void testWithVirtualTime() {
        // Test that findById has a 50 ms delay
        Mono<ReactiveEmployee> delayedMono = service.findById(1L);
        
        StepVerifier.withVirtualTime(() -> delayedMono)
            .expectSubscription()
            .expectNoEvent(Duration.ofMillis(49))
            .thenAwait(Duration.ofMillis(1))
            .assertNext(emp -> assertThat(emp.name()).isEqualTo("Alice Johnson"))
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should stream employees with delay")
    void testStreamingWithDelay() {
        // findAll has 10ms delay between elements
        Flux<ReactiveEmployee> streamFlux = service.findAll().take(3);
        
        StepVerifier.withVirtualTime(() -> streamFlux)
            .expectSubscription()
            .thenAwait(Duration.ofMillis(10))
            .expectNextCount(1)
            .thenAwait(Duration.ofMillis(10))
            .expectNextCount(1)
            .thenAwait(Duration.ofMillis(10))
            .expectNextCount(1)
            .verifyComplete();
    }
}