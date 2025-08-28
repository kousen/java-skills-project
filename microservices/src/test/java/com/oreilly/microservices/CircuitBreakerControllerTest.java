package com.oreilly.microservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Test class for CircuitBreakerController
 * Verifies the interactive circuit breaker demonstration works correctly
 */
@WebMvcTest(CircuitBreakerController.class)
class CircuitBreakerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CircuitBreakerController controller;
    
    @BeforeEach
    void setUp() {
        // Reset the controller state before each test
        controller.reset();
    }
    
    @Test
    @DisplayName("Should return initial status with failure simulation disabled")
    void testInitialStatus() throws Exception {
        mockMvc.perform(get("/api/demo/circuit-breaker/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.simulateFailure").value(false))
            .andExpect(jsonPath("$.circuitState").value("CLOSED"))
            .andExpect(jsonPath("$.totalAttempts").value(0))
            .andExpect(jsonPath("$.failureCount").value(0))
            .andExpect(jsonPath("$.instructions").exists());
    }
    
    @Test
    @DisplayName("Should toggle failure mode on and off")
    void testToggleFailureMode() throws Exception {
        // Toggle ON
        mockMvc.perform(post("/api/demo/circuit-breaker/toggle-failure"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.simulateFailure").value(true))
            .andExpect(jsonPath("$.message").value(containsString("ENABLED")));
        
        // Verify status shows failure mode is on
        mockMvc.perform(get("/api/demo/circuit-breaker/status"))
            .andExpect(jsonPath("$.simulateFailure").value(true));
        
        // Toggle OFF
        mockMvc.perform(post("/api/demo/circuit-breaker/toggle-failure"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.simulateFailure").value(false))
            .andExpect(jsonPath("$.message").value(containsString("DISABLED")));
        
        // Verify status shows failure mode is off
        mockMvc.perform(get("/api/demo/circuit-breaker/status"))
            .andExpect(jsonPath("$.simulateFailure").value(false));
    }
    
    @Test
    @DisplayName("Should return success when calling service with failure mode disabled")
    void testCallServiceSuccess() throws Exception {
        // Ensure failure mode is OFF
        Map<String, Object> status = controller.getStatus();
        if ((Boolean) status.get("simulateFailure")) {
            controller.toggleFailureMode();
        }
        
        mockMvc.perform(get("/api/demo/circuit-breaker/call-service"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.message").value(containsString("successful")))
            .andExpect(jsonPath("$.data.name").value("Engineering"))
            .andExpect(jsonPath("$.circuitState").value("CLOSED"))
            .andExpect(jsonPath("$.attemptNumber").value(1));
    }
    
    @Test
    @DisplayName("Should return failure when calling service with failure mode enabled")
    void testCallServiceFailure() throws Exception {
        // Enable failure mode
        mockMvc.perform(post("/api/demo/circuit-breaker/toggle-failure"));
        
        mockMvc.perform(get("/api/demo/circuit-breaker/call-service"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("FAILED"))
            .andExpect(jsonPath("$.message").value(containsString("failed")))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.failureCount").value(1));
    }
    
    @Test
    @DisplayName("Should open circuit breaker after 3 failures")
    void testCircuitBreakerOpensAfterThreshold() throws Exception {
        // Enable failure mode
        controller.toggleFailureMode();
        
        // First failure
        Map<String, Object> response = controller.callService();
        assertThat(response.get("status")).isEqualTo("FAILED");
        assertThat(response.get("circuitState")).isEqualTo("CLOSED");
        
        // Second failure
        response = controller.callService();
        assertThat(response.get("status")).isEqualTo("FAILED");
        assertThat(response.get("circuitState")).isEqualTo("CLOSED");
        
        // Third failure - should indicate circuit will open
        response = controller.callService();
        assertThat(response.get("status")).isEqualTo("FAILED");
        assertThat(response.get("circuitAction")).isEqualTo("Circuit breaker will OPEN on next call");
        
        // Fourth call - circuit should be open
        response = controller.callService();
        assertThat(response.get("status")).isEqualTo("FALLBACK");
        assertThat(response.get("circuitState")).isEqualTo("OPEN");
        assertThat(response.get("message").toString()).contains("Circuit breaker OPEN");
    }
    
    @Test
    @DisplayName("Should transition to half-open state after some attempts")
    void testCircuitBreakerHalfOpenTransition() throws Exception {
        // Enable failure mode and open the circuit (takes 3 failures)
        controller.toggleFailureMode();
        for (int i = 0; i < 3; i++) {
            controller.callService();
        }
        
        // 4th call - circuit should now be open
        Map<String, Object> response = controller.callService();
        assertThat(response.get("circuitState")).isEqualTo("OPEN");
        assertThat(response.get("status")).isEqualTo("FALLBACK");
        
        // 5th call - should trigger half-open transition (5 % 5 == 0)
        // But since failure mode is still on, it will test the service, fail, and reopen
        response = controller.callService();
        assertThat(response).isNotNull();
        assertThat(response.get("note")).isEqualTo("Circuit breaker entering HALF-OPEN state for testing");
        // Circuit goes to HALF-OPEN, tests the service, fails, and reopens
        assertThat(response.get("circuitAction")).isEqualTo("Circuit breaker reopened due to failure in HALF-OPEN state");
        assertThat(response.get("circuitState")).isEqualTo("OPEN");
        
        // Now disable failures and wait for another half-open opportunity
        controller.toggleFailureMode();
        
        // Need to make more calls to get to attempt 10 (10 % 5 == 0)
        for (int i = 6; i <= 9; i++) {
            controller.callService();
        }
        
        // 10th call - should go half-open and succeed this time
        response = controller.callService();
        assertThat(response.get("status")).isEqualTo("SUCCESS");
        assertThat(response.get("circuitState")).isEqualTo("CLOSED");
    }
    
    @Test
    @DisplayName("Should close circuit when service recovers")
    void testCircuitBreakerClosesOnRecovery() throws Exception {
        // Open the circuit
        controller.toggleFailureMode();
        for (int i = 0; i < 4; i++) {
            controller.callService();
        }
        
        // Circuit is open, now disable failures
        controller.toggleFailureMode();
        
        // Next successful call should close the circuit
        Map<String, Object> response = controller.callService();
        assertThat(response.get("status")).isEqualTo("SUCCESS");
        assertThat(response.get("circuitState")).isEqualTo("CLOSED");
        assertThat(response.get("failureCount")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Should reset all counters and state")
    void testReset() throws Exception {
        // Create some state
        controller.toggleFailureMode();
        controller.callService();
        controller.callService();
        
        // Reset
        mockMvc.perform(post("/api/demo/circuit-breaker/reset"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(containsString("reset")))
            .andExpect(jsonPath("$.state").value(containsString("cleared")));
        
        // Verify everything is reset
        Map<String, Object> status = controller.getStatus();
        assertThat(status.get("simulateFailure")).isEqualTo(false);
        assertThat(status.get("circuitState")).isEqualTo("CLOSED");
        assertThat(status.get("totalAttempts")).isEqualTo(0);
        assertThat(status.get("failureCount")).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Should handle rapid toggles correctly")
    void testRapidToggles() {
        // Test that rapid toggling doesn't cause issues
        for (int i = 0; i < 10; i++) {
            Map<String, Object> response = controller.toggleFailureMode();
            boolean expectedState = (i % 2 == 0);
            assertThat(response.get("simulateFailure")).isEqualTo(expectedState);
        }
    }
}