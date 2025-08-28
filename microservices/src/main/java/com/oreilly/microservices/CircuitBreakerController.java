package com.oreilly.microservices;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Interactive Circuit Breaker Demonstration Controller
 * Allows real-time simulation of circuit breaker behavior
 */
@RestController
@RequestMapping("/api/demo/circuit-breaker")
public class CircuitBreakerController {
    
    private final AtomicBoolean simulateFailure = new AtomicBoolean(false);
    private final AtomicInteger attemptCounter = new AtomicInteger(0);
    private final AtomicInteger failureCounter = new AtomicInteger(0);
    private String circuitState = "CLOSED";
    
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("simulateFailure", simulateFailure.get());
        status.put("circuitState", circuitState);
        status.put("totalAttempts", attemptCounter.get());
        status.put("failureCount", failureCounter.get());
        status.put("instructions", Map.of(
            "toggleFailure", "POST /api/demo/circuit-breaker/toggle-failure",
            "callService", "GET /api/demo/circuit-breaker/call-service",
            "reset", "POST /api/demo/circuit-breaker/reset"
        ));
        return status;
    }
    
    @PostMapping("/toggle-failure")
    public Map<String, Object> toggleFailureMode() {
        boolean newState = !simulateFailure.get();
        simulateFailure.set(newState);
        Map<String, Object> response = new HashMap<>();
        response.put("simulateFailure", newState);
        response.put("message", newState ? "Failure simulation ENABLED - services will fail" 
                                          : "Failure simulation DISABLED - services will succeed");
        return response;
    }
    
    @GetMapping("/call-service")
    public Map<String, Object> callService() {
        attemptCounter.incrementAndGet();
        Map<String, Object> response = new HashMap<>();
        
        // Check if circuit should open
        if (failureCounter.get() >= 3 && !"OPEN".equals(circuitState)) {
            circuitState = "OPEN";
        }
        
        // Handle based on circuit state
        switch (circuitState) {
            case "OPEN":
                // Check if enough time/attempts have passed to try half-open
                if (attemptCounter.get() % 5 == 0 && attemptCounter.get() > 0) {
                    circuitState = "HALF-OPEN";
                    response.put("note", "Circuit breaker entering HALF-OPEN state for testing");
                    // Fall through to test the service
                } else {
                    // Circuit is open, return fallback
                    response.put("status", "FALLBACK");
                    response.put("message", "Circuit breaker OPEN - returning fallback response");
                    response.put("data", "Department Unavailable - Service is currently down");
                    break;
                }
            
            case "HALF-OPEN":
            case "CLOSED":
                if (simulateFailure.get()) {
                    // Service call fails
                    failureCounter.incrementAndGet();
                    response.put("status", "FAILED");
                    response.put("message", "Service call failed (simulated)");
                    response.put("error", "Connection timeout to department-service");
                    
                    if ("HALF-OPEN".equals(circuitState)) {
                        // Failed while half-open, reopen circuit
                        circuitState = "OPEN";
                        response.put("circuitAction", "Circuit breaker reopened due to failure in HALF-OPEN state");
                    } else if (failureCounter.get() >= 3) {
                        response.put("circuitAction", "Circuit breaker will OPEN on next call");
                    }
                } else {
                    // Service call succeeds
                    failureCounter.set(0);
                    circuitState = "CLOSED";
                    response.put("status", "SUCCESS");
                    response.put("message", "Service call successful");
                    response.put("data", Map.of(
                        "id", 1,
                        "name", "Engineering",
                        "description", "Software Development"
                    ));
                }
                break;
        }
        
        response.put("circuitState", circuitState);
        response.put("attemptNumber", attemptCounter.get());
        response.put("failureCount", failureCounter.get());
        
        return response;
    }
    
    @PostMapping("/reset")
    public Map<String, String> reset() {
        attemptCounter.set(0);
        failureCounter.set(0);
        circuitState = "CLOSED";
        simulateFailure.set(false);
        
        return Map.of(
            "message", "Circuit breaker demonstration reset",
            "state", "All counters cleared, circuit CLOSED, failure simulation DISABLED"
        );
    }
}