package com.oreilly.reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

import static com.oreilly.reactive.ReactiveRecords.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests for ReactiveDemoController
 * Demonstrates testing reactive operations and transformations
 */
class ReactiveDemoControllerTest {
    
    private ReactiveDemoController controller;
    
    @BeforeEach
    void setUp() {
        controller = new ReactiveDemoController();
        controller.reset().block(); // Reset state before each test
    }
    
    @Test
    @DisplayName("Should demonstrate Mono operations")
    void testMonoDemo() {
        Mono<Map<String, Object>> resultMono = controller.demonstrateMono();
        
        StepVerifier.create(resultMono)
            .assertNext(result -> {
                assertThat(result.get("operation")).isEqualTo("Mono Demonstration");
                assertThat(result.get("original")).isEqualTo("Alice Johnson");
                assertThat(result.get("transformed")).isEqualTo("ALICE JOHNSON");
                assertThat((Double) result.get("newSalary")).isCloseTo(93500.0, within(0.01));
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should demonstrate Flux operations with filtering")
    void testFluxDemo() {
        Flux<ReactiveEmployee> resultFlux = controller.demonstrateFlux();
        
        StepVerifier.create(resultFlux)
            .assertNext(emp -> {
                assertThat(emp.name()).isEqualTo("Alice Johnson");
                assertThat(emp.salary()).isEqualTo(89250.0); // 85000 * 1.05
            })
            .assertNext(emp -> {
                assertThat(emp.name()).isEqualTo("Carol Davis");
                assertThat(emp.salary()).isEqualTo(94500.0); // 90000 * 1.05
            })
            .assertNext(emp -> {
                assertThat(emp.name()).isEqualTo("Eve Brown");
                assertThat(emp.salary()).isEqualTo(78750.0); // 75000 * 1.05
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should combine multiple streams")
    void testCombineStreams() {
        Mono<EmployeeProfile> profileMono = controller.demonstrateCombining();
        
        StepVerifier.create(profileMono)
            .assertNext(profile -> {
                assertThat(profile.employee().name()).isEqualTo("Alice Johnson");
                assertThat(profile.department().name()).isEqualTo("Engineering");
                assertThat(profile.salary().amount()).isEqualTo(85000.0);
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should handle errors with fallback")
    void testErrorHandlingWithSimulation() {
        // Enable error simulation
        controller.toggleError().block();
        
        Mono<Map<String, Object>> errorResult = controller.demonstrateErrorHandling();
        
        StepVerifier.create(errorResult)
            .assertNext(result -> {
                assertThat(result.get("errorSimulated")).isEqualTo(true);
                assertThat(result.get("result")).isEqualTo("Fallback: Service temporarily unavailable");
            })
            .verifyComplete();
        
        // Disable error simulation
        controller.toggleError().block();
        
        Mono<Map<String, Object>> successResult = controller.demonstrateErrorHandling();
        
        StepVerifier.create(successResult)
            .assertNext(result -> {
                assertThat(result.get("errorSimulated")).isEqualTo(false);
                assertThat(result.get("result")).isEqualTo("Success: Data retrieved successfully");
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should demonstrate backpressure")
    void testBackpressure() {
        Mono<Map<String, Object>> backpressureResult = controller.demonstrateBackpressure();
        
        StepVerifier.create(backpressureResult)
            .assertNext(result -> {
                assertThat(result.get("itemsGenerated")).isEqualTo(1000);
                assertThat((Integer) result.get("itemsProcessed")).isLessThanOrEqualTo(50);
                assertThat(result.get("bufferSize")).isEqualTo(10);
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should toggle error simulation")
    void testToggleError() {
        // Initial state should be false
        StepVerifier.create(controller.getStatus())
            .assertNext(status -> 
                assertThat(status.get("errorSimulation")).isEqualTo(false)
            )
            .verifyComplete();
        
        // Toggle on
        StepVerifier.create(controller.toggleError())
            .assertNext(result -> {
                assertThat(result.get("errorSimulation")).isEqualTo(true);
                assertThat(result.get("message")).isEqualTo("Error simulation ENABLED");
            })
            .verifyComplete();
        
        // Toggle off
        StepVerifier.create(controller.toggleError())
            .assertNext(result -> {
                assertThat(result.get("errorSimulation")).isEqualTo(false);
                assertThat(result.get("message")).isEqualTo("Error simulation DISABLED");
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should toggle slowness simulation")
    void testToggleSlowness() {
        // Toggle on
        StepVerifier.create(controller.toggleSlowness())
            .assertNext(result -> {
                assertThat(result.get("slownessSimulation")).isEqualTo(true);
                assertThat(result.get("message")).isEqualTo("Slowness simulation ENABLED (2s delay)");
            })
            .verifyComplete();
        
        // Test that slowness actually affects timing
        controller.toggleSlowness().block(); // Turn off first
        
        // Without slowness - should be fast
        StepVerifier.withVirtualTime(() -> controller.demonstrateMono())
            .expectSubscription()
            .thenAwait(Duration.ofMillis(100))
            .expectNextCount(1)
            .verifyComplete();
        
        // With slowness - should be slow
        controller.toggleSlowness().block(); // Turn on
        
        StepVerifier.withVirtualTime(() -> controller.demonstrateMono())
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(1))
            .thenAwait(Duration.ofSeconds(1))
            .expectNextCount(1)
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should reset demo state")
    void testReset() {
        // Change some state
        controller.toggleError().block();
        controller.toggleSlowness().block();
        controller.demonstrateMono().block(); // Increment counter
        
        // Reset
        StepVerifier.create(controller.reset())
            .assertNext(result ->
                    assertThat(result.get("message")).isEqualTo("Demo state reset"))
            .verifyComplete();
        
        // Verify state is reset
        StepVerifier.create(controller.getStatus())
            .assertNext(status -> {
                assertThat(status.get("itemsProcessed")).isEqualTo(0);
                assertThat(status.get("errorSimulation")).isEqualTo(false);
                assertThat(status.get("slownessSimulation")).isEqualTo(false);
            })
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should stream events")
    void testEventStream() {
        // Just verify that events are being emitted to the stream
        Flux<String> eventStream = controller.streamEvents().take(3);
        
        StepVerifier.create(eventStream)
            .then(() -> {
                controller.reset().subscribe();
                controller.demonstrateMono().subscribe();
            })
            .expectNextMatches(event -> event != null && !event.isEmpty())
            .expectNextMatches(event -> event != null && !event.isEmpty())
            .expectNextMatches(event -> event != null && !event.isEmpty())
            .verifyComplete();
    }
    
    @Test
    @DisplayName("Should demonstrate transformations")
    void testTransformations() {
        Mono<Map<String, Object>> transformResult = controller.demonstrateTransformations();
        
        StepVerifier.create(transformResult)
            .assertNext(result -> {
                assertThat(result.get("operation")).isEqualTo("Transformation Demo");
                assertThat((Integer) result.get("totalEmployees")).isEqualTo(5);
                assertThat((Double) result.get("averageSalary")).isEqualTo(77000.0);
            })
            .verifyComplete();
    }
}