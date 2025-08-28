package com.oreilly.reactive;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.oreilly.reactive.ReactiveRecords.*;

/**
 * Interactive demonstration controller for reactive programming concepts
 * Provides REST endpoints to explore Mono, Flux, and reactive patterns
 */
@RestController
@RequestMapping("/api/demo/reactive")
public class ReactiveDemoController {
    
    private final AtomicInteger processedItems = new AtomicInteger(0);
    private final AtomicBoolean simulateError = new AtomicBoolean(false);
    private final AtomicBoolean simulateSlowness = new AtomicBoolean(false);
    private final Sinks.Many<String> eventSink = Sinks.many().multicast().onBackpressureBuffer();
    
    /**
     * Get current demo status
     */
    @GetMapping("/status")
    public Mono<Map<String, Object>> getStatus() {
        return Mono.just(Map.of(
            "itemsProcessed", processedItems.get(),
            "errorSimulation", simulateError.get(),
            "slownessSimulation", simulateSlowness.get(),
            "instructions", Map.of(
                "toggleError", "POST /api/demo/reactive/toggle-error",
                "toggleSlowness", "POST /api/demo/reactive/toggle-slowness",
                "reset", "POST /api/demo/reactive/reset"
            )
        ));
    }
    
    /**
     * Demonstrate Mono operations
     */
    @GetMapping("/mono")
    public Mono<Map<String, Object>> demonstrateMono() {
        processedItems.incrementAndGet();
        
        return Mono.just(new ReactiveEmployee(1L, "Alice Johnson", "Engineering", 85000.0))
            .map(employee -> {
                eventSink.tryEmitNext("Processing employee: " + employee.name());
                return employee;
            })
            .delayElement(Duration.ofMillis(simulateSlowness.get() ? 2000 : 100))
            .map(employee -> new ReactiveEmployee(
                employee.id(),
                employee.name().toUpperCase(),
                employee.department(),
                employee.salary() * 1.1
            ))
            .map(employee -> Map.of(
                "operation", "Mono Demonstration",
                "original", "Alice Johnson",
                "transformed", employee.name(),
                "salaryIncrease", "10%",
                "newSalary", employee.salary(),
                "processingTime", simulateSlowness.get() ? "2 seconds (slow)" : "100ms (fast)",
                "message", "Mono represents a single value or empty result"
            ));
    }
    
    /**
     * Demonstrate Flux operations
     */
    @GetMapping("/flux")
    public Flux<ReactiveEmployee> demonstrateFlux() {
        var employees = List.of(
            new ReactiveEmployee(1L, "Alice Johnson", "Engineering", 85000.0),
            new ReactiveEmployee(2L, "Bob Smith", "Marketing", 70000.0),
            new ReactiveEmployee(3L, "Carol Davis", "Engineering", 90000.0),
            new ReactiveEmployee(4L, "David Wilson", "Sales", 65000.0),
            new ReactiveEmployee(5L, "Eve Brown", "HR", 75000.0)
        );
        
        return Flux.fromIterable(employees)
            .delayElements(Duration.ofMillis(200))
            .doOnNext(emp -> {
                processedItems.incrementAndGet();
                eventSink.tryEmitNext("Emitting: " + emp.name());
            })
            .filter(emp -> emp.salary() > 70000)
            .map(emp -> new ReactiveEmployee(
                emp.id(),
                emp.name(),
                emp.department(),
                emp.salary() * 1.05 // 5% raise for high earners
            ));
    }
    
    /**
     * Demonstrate transformations
     */
    @GetMapping("/transform")
    public Mono<Map<String, Object>> demonstrateTransformations() {
        return Flux.just("Engineering", "Marketing", "Sales", "HR")
            .flatMap(this::getDepartmentEmployees)
            .collectList()
            .map(employees -> {
                var stats = employees.stream()
                    .mapToDouble(ReactiveEmployee::salary)
                    .summaryStatistics();
                
                return Map.of(
                    "operation", "Transformation Demo",
                    "totalEmployees", employees.size(),
                    "averageSalary", stats.getAverage(),
                    "minSalary", stats.getMin(),
                    "maxSalary", stats.getMax(),
                    "departments", employees.stream()
                        .map(ReactiveEmployee::department)
                        .distinct()
                        .toList(),
                    "message", "FlatMap transforms and flattens nested publishers"
                );
            });
    }
    
    /**
     * Demonstrate combining streams
     */
    @GetMapping("/combine")
    public Mono<EmployeeProfile> demonstrateCombining() {
        var employeeMono = Mono.just(new ReactiveEmployee(1L, "Alice Johnson", "Engineering", 85000.0))
            .delayElement(Duration.ofMillis(100));
        
        var departmentMono = Mono.just(new ReactiveDepartment(1L, "Engineering", "Software Development"))
            .delayElement(Duration.ofMillis(150));
        
        var salaryMono = Mono.just(new ReactiveSalary(1L, 85000.0, "USD"))
            .delayElement(Duration.ofMillis(200));
        
        return Mono.zip(employeeMono, departmentMono, salaryMono)
            .map(tuple -> new EmployeeProfile(tuple.getT1(), tuple.getT2(), tuple.getT3()))
            .doOnNext(profile -> eventSink.tryEmitNext("Combined profile: " + profile.employee().name()));
    }
    
    /**
     * Demonstrate error handling
     */
    @GetMapping("/error")
    public Mono<Map<String, Object>> demonstrateErrorHandling() {
        return Mono.defer(() -> {
            if (simulateError.get()) {
                return Mono.<String>error(new RuntimeException("Database connection failed"))
                    .onErrorReturn("Fallback: Service temporarily unavailable");
            } else {
                return Mono.just("Success: Data retrieved successfully");
            }
        })
        .retryWhen(Retry.backoff(3, Duration.ofMillis(100))
            .doBeforeRetry(signal -> eventSink.tryEmitNext("Retry attempt: " + signal.totalRetries())))
        .map(result -> Map.of(
            "operation", "Error Handling Demo",
            "errorSimulated", simulateError.get(),
            "result", result,
            "strategies", List.of(
                "onErrorReturn - Provide fallback value",
                "onErrorResume - Switch to alternative stream",
                "retry - Attempt operation again",
                "timeout - Limit wait time"
            ),
            "message", simulateError.get() ? 
                "Error occurred but was handled gracefully" : 
                "No errors - operation successful"
        ));
    }
    
    /**
     * Demonstrate backpressure handling
     */
    @GetMapping("/backpressure")
    public Mono<Map<String, Object>> demonstrateBackpressure() {
        var processed = new AtomicInteger(0);
        var dropped = new AtomicInteger(0);
        
        return Flux.range(1, 1000)
            .onBackpressureBuffer(10,
                i -> dropped.incrementAndGet(),
                BufferOverflowStrategy.DROP_OLDEST)
            .delayElements(Duration.ofMillis(1))
            .take(50)
            .doOnNext(i -> processed.incrementAndGet())
            .then(Mono.just(Map.of(
                "operation", "Backpressure Demo",
                "itemsGenerated", 1000,
                "itemsProcessed", processed.get(),
                "itemsDropped", dropped.get(),
                "bufferSize", 10,
                "strategy", "DROP_OLDEST",
                "message", "Backpressure prevents overwhelming slow consumers"
            )));
    }
    
    /**
     * Stream events using Server-Sent Events
     */
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamEvents() {
        return eventSink.asFlux()
            .doOnSubscribe(sub -> eventSink.tryEmitNext("Client connected to event stream"));
    }
    
    /**
     * Toggle error simulation
     */
    @PostMapping("/toggle-error")
    public Mono<Map<String, Object>> toggleError() {
        boolean newState = !simulateError.get();
        simulateError.set(newState);
        return Mono.just(Map.of(
            "errorSimulation", newState,
            "message", newState ? "Error simulation ENABLED" : "Error simulation DISABLED"
        ));
    }
    
    /**
     * Toggle slowness simulation
     */
    @PostMapping("/toggle-slowness")
    public Mono<Map<String, Object>> toggleSlowness() {
        boolean newState = !simulateSlowness.get();
        simulateSlowness.set(newState);
        return Mono.just(Map.of(
            "slownessSimulation", newState,
            "message", newState ? "Slowness simulation ENABLED (2s delay)" : "Slowness simulation DISABLED"
        ));
    }
    
    /**
     * Reset demo state
     */
    @PostMapping("/reset")
    public Mono<Map<String, String>> reset() {
        processedItems.set(0);
        simulateError.set(false);
        simulateSlowness.set(false);
        eventSink.tryEmitNext("Demo state reset");
        
        return Mono.just(Map.of(
            "message", "Demo state reset",
            "status", "All counters cleared, simulations disabled"
        ));
    }
    
    /**
     * Helper method to generate department employees
     */
    private Flux<ReactiveEmployee> getDepartmentEmployees(String department) {
        var employees = switch (department) {
            case "Engineering" -> List.of(
                new ReactiveEmployee(1L, "Alice Johnson", department, 85000.0),
                new ReactiveEmployee(3L, "Carol Davis", department, 90000.0)
            );
            case "Marketing" -> List.of(
                new ReactiveEmployee(2L, "Bob Smith", department, 70000.0)
            );
            case "Sales" -> List.of(
                new ReactiveEmployee(4L, "David Wilson", department, 65000.0)
            );
            case "HR" -> List.of(
                new ReactiveEmployee(5L, "Eve Brown", department, 75000.0)
            );
            default -> List.<ReactiveEmployee>of();
        };
        
        return Flux.fromIterable(employees);
    }
    
}