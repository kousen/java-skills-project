package com.oreilly.reactive;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.oreilly.reactive.ReactiveRecords.*;

/**
 * Reactive service layer for employee operations
 * Demonstrates async, non-blocking data access patterns
 */
@Service
public class ReactiveEmployeeService {
    
    private final Map<Long, ReactiveEmployee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(10);
    private final Sinks.Many<EmployeeEvent> eventSink =
            Sinks.many().multicast().onBackpressureBuffer();
    
    public ReactiveEmployeeService() {
        // Initialize with sample data using Java 21 features
        var initialEmployees = Map.of(
            1L, new ReactiveEmployee(1L, "Alice Johnson", "Engineering", 85000.0),
            2L, new ReactiveEmployee(2L, "Bob Smith", "Marketing", 70000.0),
            3L, new ReactiveEmployee(3L, "Carol Davis", "Engineering", 90000.0),
            4L, new ReactiveEmployee(4L, "David Wilson", "Sales", 65000.0),
            5L, new ReactiveEmployee(5L, "Eve Brown", "HR", 75000.0)
        );
        employees.putAll(initialEmployees);
    }
    
    /**
     * Find employee by ID - returns Mono
     */
    public Mono<ReactiveEmployee> findById(Long id) {
        return Mono.fromCallable(() -> employees.get(id))
            .delayElement(Duration.ofMillis(50)) // Simulate network delay
            .switchIfEmpty(Mono.error(new RuntimeException("Employee not found: " + id)));
    }
    
    /**
     * Find all employees - returns Flux
     */
    public Flux<ReactiveEmployee> findAll() {
        return Flux.fromIterable(employees.values())
            .delayElements(Duration.ofMillis(10)); // Simulate streaming
    }
    
    /**
     * Save new employee
     */
    public Mono<ReactiveEmployee> save(ReactiveEmployee employee) {
        return Mono.fromCallable(() -> {
            var newEmployee = employee.id() == null
                ? new ReactiveEmployee(
                    idGenerator.incrementAndGet(),
                    employee.name(),
                    employee.department(),
                    employee.salary()
                )
                : employee;
            
            employees.put(newEmployee.id(), newEmployee);
            eventSink.tryEmitNext(EmployeeEvent.created(newEmployee));
            return newEmployee;
        })
        .delayElement(Duration.ofMillis(100)); // Simulate save delay
    }
    
    /**
     * Update existing employee
     */
    public Mono<ReactiveEmployee> update(Long id, EmployeeUpdateRequest request) {
        return Mono.fromCallable(() -> {
            var existing = employees.get(id);
            if (existing == null) {
                throw new RuntimeException("Employee not found: " + id);
            }
            
            var updated = new ReactiveEmployee(
                id,
                request.name() != null ? request.name() : existing.name(),
                request.department() != null ? request.department() : existing.department(),
                request.salary() != null ? request.salary() : existing.salary()
            );
            
            employees.put(id, updated);
            eventSink.tryEmitNext(EmployeeEvent.updated(updated));
            return updated;
        })
        .delayElement(Duration.ofMillis(100));
    }
    
    /**
     * Delete employee
     */
    public Mono<Void> delete(Long id) {
        return Mono.fromRunnable(() -> {
            employees.remove(id);
            eventSink.tryEmitNext(EmployeeEvent.deleted(id));
        })
        .delayElement(Duration.ofMillis(50))
        .then();
    }
    
    /**
     * Find employees by department
     */
    public Flux<ReactiveEmployee> findByDepartment(String department) {
        return findAll()
            .filter(emp -> emp.department().equals(department));
    }
    
    /**
     * Search employees with complex criteria
     */
    public Flux<ReactiveEmployee> searchEmployees(String nameContains, String department, Double minSalary) {
        return findAll()
            .filter(emp -> nameContains == null ||
                           emp.name().toLowerCase().contains(nameContains.toLowerCase()))
            .filter(emp -> (department == null || emp.department().equals(department)) &&
                           (minSalary == null || emp.salary() >= minSalary));
    }
    
    /**
     * Get complete employee profile by combining multiple data sources
     */
    public Mono<EmployeeProfile> getCompleteProfile(Long employeeId) {
        var employeeMono = findById(employeeId);
        var departmentMono = getDepartmentInfo(employeeId);
        var salaryMono = getSalaryInfo(employeeId);
        
        return Mono.zip(employeeMono, departmentMono, salaryMono)
            .map(tuple ->
                    new EmployeeProfile(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }
    
    /**
     * Calculate department statistics
     */
    public Mono<Map<String, Object>> getDepartmentStatistics(String department) {
        return findByDepartment(department)
            .collectList()
            .map(employees -> {
                var stats = employees.stream()
                    .mapToDouble(ReactiveEmployee::salary)
                    .summaryStatistics();
                
                return Map.of(
                    "department", department,
                    "employeeCount", employees.size(),
                    "averageSalary", stats.getAverage(),
                    "minSalary", stats.getMin(),
                    "maxSalary", stats.getMax(),
                    "totalSalary", stats.getSum()
                );
            });
    }
    
    /**
     * Stream employee events
     */
    public Flux<EmployeeEvent> getEventStream() {
        return eventSink.asFlux();
    }
    
    /**
     * Give raise to all employees in a department
     */
    public Flux<ReactiveEmployee> giveRaiseToDepartment(String department, double percentage) {
        return findByDepartment(department)
            .flatMap(emp -> {
                var updated = new ReactiveEmployee(
                    emp.id(),
                    emp.name(),
                    emp.department(),
                    emp.salary() * (1 + percentage / 100)
                );
                return save(updated);
            });
    }
    
    // Helper methods for demonstration
    private Mono<ReactiveDepartment> getDepartmentInfo(Long employeeId) {
        return findById(employeeId)
            .map(emp -> switch (emp.department()) {
                case "Engineering" -> new ReactiveDepartment(1L, "Engineering", "Software Development");
                case "Marketing" -> new ReactiveDepartment(2L, "Marketing", "Brand & Communications");
                case "Sales" -> new ReactiveDepartment(3L, "Sales", "Revenue Generation");
                case "HR" -> new ReactiveDepartment(4L, "HR", "People & Culture");
                default -> new ReactiveDepartment(0L, "Unknown", "Unknown Department");
            })
            .delayElement(Duration.ofMillis(75));
    }
    
    private Mono<ReactiveSalary> getSalaryInfo(Long employeeId) {
        return findById(employeeId)
            .map(emp -> new ReactiveSalary(emp.id(), emp.salary(), "USD"))
            .delayElement(Duration.ofMillis(100));
    }
}