package com.oreilly.reactive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

import static com.oreilly.reactive.ReactiveRecords.*;

/**
 * Reactive WebFlux REST controller for employee operations
 * Demonstrates non-blocking web endpoints with Mono and Flux
 */
@RestController
@RequestMapping("/api/employees")
public class ReactiveEmployeeController {
    
    private final ReactiveEmployeeService employeeService;
    
    @Autowired
    public ReactiveEmployeeController(ReactiveEmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    /**
     * Get single employee - returns Mono
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReactiveEmployee>> getEmployee(@PathVariable Long id) {
        return employeeService.findById(id)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all employees - returns Flux
     */
    @GetMapping
    public Flux<ReactiveEmployee> getAllEmployees() {
        return employeeService.findAll();
    }
    
    /**
     * Create new employee
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ReactiveEmployee> createEmployee(@RequestBody CreateEmployeeRequest request) {
        return employeeService.save(request.toEmployee());
    }
    
    /**
     * Update employee
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ReactiveEmployee>> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeUpdateRequest request) {
        return employeeService.update(id, request)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.notFound().build());
    }
    
    /**
     * Delete employee
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable Long id) {
        return employeeService.delete(id);
    }
    
    /**
     * Stream employees using Server-Sent Events
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ReactiveEmployee> streamEmployees() {
        return employeeService.findAll()
            .delayElements(Duration.ofSeconds(1)); // Emit one per second
    }
    
    /**
     * Stream employee events
     */
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EmployeeEvent> streamEvents() {
        return employeeService.getEventStream();
    }
    
    /**
     * Search employees with reactive filtering
     */
    @GetMapping("/search")
    public Flux<ReactiveEmployee> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double minSalary) {
        return employeeService.searchEmployees(name, department, minSalary);
    }
    
    /**
     * Get department statistics
     */
    @GetMapping("/departments/{department}/statistics")
    public Mono<Map<String, Object>> getDepartmentStatistics(@PathVariable String department) {
        return employeeService.getDepartmentStatistics(department);
    }
    
    /**
     * Give raise to department
     */
    @PostMapping("/departments/{department}/raise")
    public Flux<ReactiveEmployee> giveRaiseToDepartment(
            @PathVariable String department,
            @RequestParam double percentage) {
        return employeeService.giveRaiseToDepartment(department, percentage);
    }
    
    /**
     * Get complete employee profile
     */
    @GetMapping("/{id}/profile")
    public Mono<ResponseEntity<EmployeeProfile>> getEmployeeProfile(@PathVariable Long id) {
        return employeeService.getCompleteProfile(id)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.notFound().build());
    }
    
    /**
     * Demonstrate delayed response
     */
    @GetMapping("/delayed")
    public Mono<String> getDelayedResponse(@RequestParam(defaultValue = "2") int seconds) {
        return Mono.just("Response after " + seconds + " seconds")
            .delayElement(Duration.ofSeconds(seconds));
    }
    
    /**
     * Demonstrate infinite stream with take
     */
    @GetMapping(value = "/infinite", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> infiniteStream(@RequestParam(defaultValue = "10") int limit) {
        return Flux.interval(Duration.ofSeconds(1)).take(limit);
    }
}