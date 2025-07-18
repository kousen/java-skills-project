package com.oreilly.employee.controller;

import com.oreilly.employee.model.Employee;
import com.oreilly.employee.service.ReactiveEmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Reactive REST Controller demonstrating Project Reactor with Spring WebFlux.
 * Shows non-blocking, async operations with Mono and Flux.
 */
@RestController
@RequestMapping("/api/reactive/employees")
public class ReactiveEmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveEmployeeController.class);
    
    private final ReactiveEmployeeService employeeService;
    
    @Autowired
    public ReactiveEmployeeController(ReactiveEmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    /**
     * GET /api/reactive/employees/{id} - Reactive employee lookup
     */
    @GetMapping("/{id}")
    public Mono<Employee> getEmployee(@PathVariable Long id) {
        logger.info("Reactive GET request for employee ID: {}", id);
        
        return employeeService.findById(id)
            .doOnNext(emp -> logger.info("Found employee: {}", emp.getName()))
            .doOnError(error -> logger.error("Error finding employee {}: {}", id, error.getMessage()))
            .switchIfEmpty(Mono.error(new EmployeeController.EmployeeNotFoundException("Employee not found: " + id)));
    }
    
    /**
     * GET /api/reactive/employees - Get all employees as a stream
     */
    @GetMapping
    public Flux<Employee> getAllEmployees() {
        logger.info("Reactive GET request for all employees");
        
        return employeeService.findAll()
            .doOnNext(emp -> logger.debug("Streaming employee: {}", emp.getName()))
            .doOnComplete(() -> logger.info("Completed streaming all employees"));
    }
    
    /**
     * GET /api/reactive/employees/department/{dept} - Get employees by department
     */
    @GetMapping("/department/{department}")
    public Flux<Employee> getEmployeesByDepartment(@PathVariable String department) {
        logger.info("Reactive GET request for department: {}", department);
        
        return employeeService.findByDepartment(department)
            .doOnNext(emp -> logger.debug("Streaming employee from {}: {}", department, emp.getName()));
    }
    
    /**
     * POST /api/reactive/employees - Create employee reactively
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Employee> createEmployee(@Valid @RequestBody Mono<Employee> employeeMono) {
        logger.info("Reactive POST request to create employee");
        
        return employeeMono
            .doOnNext(emp -> logger.info("Creating employee: {}", emp.getName()))
            .flatMap(emp -> {
                emp.setId(null); // Ensure new employee
                return employeeService.save(emp);
            })
            .doOnSuccess(emp -> logger.info("Created employee with ID: {}", emp.getId()))
            .doOnError(error -> logger.error("Error creating employee: {}", error.getMessage()));
    }
    
    /**
     * PUT /api/reactive/employees/{id} - Update employee reactively
     */
    @PutMapping("/{id}")
    public Mono<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Mono<Employee> employeeMono) {
        logger.info("Reactive PUT request for employee ID: {}", id);
        
        return employeeService.findById(id)
            .switchIfEmpty(Mono.error(new EmployeeController.EmployeeNotFoundException("Employee not found: " + id)))
            .then(employeeMono)
            .doOnNext(emp -> emp.setId(id))
            .flatMap(employeeService::save)
            .doOnSuccess(emp -> logger.info("Updated employee: {}", emp.getName()));
    }
    
    /**
     * DELETE /api/reactive/employees/{id} - Delete employee reactively
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("Reactive DELETE request for employee ID: {}", id);
        
        return employeeService.findById(id)
            .switchIfEmpty(Mono.error(new EmployeeController.EmployeeNotFoundException("Employee not found: " + id)))
            .then(employeeService.deleteById(id))
            .doOnSuccess(unused -> logger.info("Deleted employee with ID: {}", id));
    }
    
    /**
     * GET /api/reactive/employees/search - Reactive search with backpressure
     */
    @GetMapping("/search")
    public Flux<Employee> searchEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minSalary) {
        
        logger.info("Reactive search - department: {}, minSalary: {}", department, minSalary);
        
        return employeeService.findAll()
            .filter(emp -> department == null || emp.getDepartment().equalsIgnoreCase(department))
            .filter(emp -> minSalary == null || emp.getSalary() >= minSalary)
            .filter(Employee::isActive)
            .onBackpressureBuffer(100) // Handle backpressure
            .doOnNext(emp -> logger.debug("Search result: {}", emp.getName()));
    }
    
    /**
     * GET /api/reactive/employees/stream - Server-Sent Events stream
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Employee>> streamEmployees() {
        logger.info("Starting employee stream");
        
        return employeeService.findAll()
            .delayElements(Duration.ofSeconds(1)) // Simulate real-time updates
            .map(employee -> ServerSentEvent.builder(employee)
                .id(String.valueOf(employee.getId()))
                .event("employee-data")
                .comment("Employee streaming at " + LocalDateTime.now())
                .build())
            .doOnNext(sse -> logger.debug("Streaming SSE for employee: {}", sse.data().getName()))
            .doOnComplete(() -> logger.info("Completed employee stream"));
    }
    
    /**
     * GET /api/reactive/employees/notifications - Real-time notifications
     */
    @GetMapping(value = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getNotifications() {
        logger.info("Starting notification stream");
        
        return Flux.interval(Duration.ofSeconds(5))
            .map(sequence -> {
                String message = "Employee system heartbeat #" + sequence + " at " + LocalDateTime.now();
                return ServerSentEvent.builder(message)
                    .id(String.valueOf(sequence))
                    .event("heartbeat")
                    .build();
            })
            .doOnNext(sse -> logger.debug("Sending notification: {}", sse.data()));
    }
    
    /**
     * POST /api/reactive/employees/batch - Process batch of employees
     */
    @PostMapping("/batch")
    public Flux<Employee> createEmployeesBatch(@RequestBody Flux<Employee> employeeFlux) {
        logger.info("Reactive batch employee creation");
        
        return employeeFlux
            .doOnNext(emp -> emp.setId(null)) // Ensure new employees
            .flatMap(employeeService::save, 4) // Concurrency of 4
            .onBackpressureBuffer(50)
            .doOnNext(emp -> logger.debug("Created employee in batch: {}", emp.getName()))
            .doOnComplete(() -> logger.info("Completed batch employee creation"));
    }
    
    /**
     * GET /api/reactive/employees/stats - Reactive statistics calculation
     */
    @GetMapping("/stats")
    public Mono<EmployeeStats> getEmployeeStatistics() {
        logger.info("Calculating reactive employee statistics");
        
        Mono<Long> totalCount = employeeService.count();
        Mono<Double> avgSalary = employeeService.findAll()
            .map(Employee::getSalary)
            .reduce(Double::sum)
            .zipWith(totalCount)
            .map(tuple -> tuple.getT1() / tuple.getT2());
        
        return Mono.zip(totalCount, avgSalary)
            .map(tuple -> new EmployeeStats(tuple.getT1(), tuple.getT2(), LocalDateTime.now()))
            .doOnSuccess(stats -> logger.info("Calculated stats: {}", stats));
    }
    
    /**
     * GET /api/reactive/employees/department-summary - Complex reactive pipeline
     */
    @GetMapping("/department-summary")
    public Flux<DepartmentSummary> getDepartmentSummary() {
        logger.info("Generating department summary");
        
        return employeeService.findAll()
            .groupBy(Employee::getDepartment)
            .flatMap(groupedFlux -> 
                groupedFlux.collectList()
                    .map(employees -> {
                        String department = groupedFlux.key();
                        long count = employees.size();
                        double avgSalary = employees.stream()
                            .mapToDouble(Employee::getSalary)
                            .average()
                            .orElse(0.0);
                        return new DepartmentSummary(department, count, avgSalary);
                    })
            )
            .doOnNext(summary -> logger.debug("Department summary: {}", summary));
    }
    
    /**
     * Statistics data class
     */
    public static class EmployeeStats {
        private final long totalEmployees;
        private final double averageSalary;
        private final LocalDateTime calculatedAt;
        
        public EmployeeStats(long totalEmployees, double averageSalary, LocalDateTime calculatedAt) {
            this.totalEmployees = totalEmployees;
            this.averageSalary = averageSalary;
            this.calculatedAt = calculatedAt;
        }
        
        public long getTotalEmployees() { return totalEmployees; }
        public double getAverageSalary() { return averageSalary; }
        public LocalDateTime getCalculatedAt() { return calculatedAt; }
        
        @Override
        public String toString() {
            return String.format("EmployeeStats{total=%d, avgSalary=%.2f}", totalEmployees, averageSalary);
        }
    }
    
    /**
     * Department summary data class
     */
    public static class DepartmentSummary {
        private final String department;
        private final long employeeCount;
        private final double averageSalary;
        
        public DepartmentSummary(String department, long employeeCount, double averageSalary) {
            this.department = department;
            this.employeeCount = employeeCount;
            this.averageSalary = averageSalary;
        }
        
        public String getDepartment() { return department; }
        public long getEmployeeCount() { return employeeCount; }
        public double getAverageSalary() { return averageSalary; }
        
        @Override
        public String toString() {
            return String.format("DepartmentSummary{dept='%s', count=%d, avgSalary=%.2f}", 
                department, employeeCount, averageSalary);
        }
    }
}