# Try It Out: Circuit Breaker Pattern

## Overview
This exercise demonstrates the Circuit Breaker pattern in action using an interactive REST API. You'll see how a circuit breaker protects microservices from cascading failures by monitoring service health and providing fallback responses.

## Prerequisites
- Java 21 installed
- A REST client (Postman, IntelliJ HTTP Client, curl, or browser extensions like REST Client)
- The microservices module running

## Starting the Application

1. Open a terminal in the project root directory
2. Run the microservices application:
   ```bash
   gradle :microservices:bootRun
   ```
3. Look for this message in the console output:
   ```
   🎮 INTERACTIVE DEMO AVAILABLE!
   Try the circuit breaker yourself with these endpoints:
   ```
4. The application runs on port 8081 by default

## Available Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/demo/circuit-breaker/status` | GET | Check current circuit breaker state |
| `/api/demo/circuit-breaker/toggle-failure` | POST | Toggle failure simulation on/off |
| `/api/demo/circuit-breaker/call-service` | GET | Simulate a service call |
| `/api/demo/circuit-breaker/reset` | POST | Reset all counters and state |

## Exercise Steps

### Part 1: Understanding Normal Operation

1. **Check Initial Status**
   ```
   GET http://localhost:8081/api/demo/circuit-breaker/status
   ```
   
   Expected response:
   ```json
   {
     "simulateFailure": false,
     "circuitState": "CLOSED",
     "totalAttempts": 0,
     "failureCount": 0
   }
   ```
   
   **What this means:** The circuit is CLOSED (normal operation), no failures are being simulated.

2. **Make a Successful Service Call**
   ```
   GET http://localhost:8081/api/demo/circuit-breaker/call-service
   ```
   
   Expected response:
   ```json
   {
     "status": "SUCCESS",
     "message": "Service call successful",
     "data": {
       "id": 1,
       "name": "Engineering",
       "description": "Software Development"
     },
     "circuitState": "CLOSED"
   }
   ```
   
   **What this means:** When the service is healthy, calls succeed and return data normally.

### Part 2: Triggering the Circuit Breaker

1. **Enable Failure Simulation**
   ```
   POST http://localhost:8081/api/demo/circuit-breaker/toggle-failure
   ```
   
   Expected response:
   ```json
   {
     "simulateFailure": true,
     "message": "Failure simulation ENABLED - services will fail"
   }
   ```

2. **Make Failed Service Calls**
   
   Call the service endpoint 3 times:
   ```
   GET http://localhost:8081/api/demo/circuit-breaker/call-service
   ```
   
   - **1st call:** Returns `"status": "FAILED"`, `"failureCount": 1`
   - **2nd call:** Returns `"status": "FAILED"`, `"failureCount": 2`
   - **3rd call:** Returns `"status": "FAILED"`, `"failureCount": 3`, and includes:
     ```json
     "circuitAction": "Circuit breaker will OPEN on next call"
     ```

3. **Observe Circuit Opening**
   
   Make a 4th call:
   ```
   GET http://localhost:8081/api/demo/circuit-breaker/call-service
   ```
   
   Expected response:
   ```json
   {
     "status": "FALLBACK",
     "message": "Circuit breaker OPEN - returning fallback response",
     "data": "Department Unavailable - Service is currently down",
     "circuitState": "OPEN"
   }
   ```
   
   **What this means:** The circuit is now OPEN. Instead of trying to call the failing service (and waiting for timeouts), it immediately returns a fallback response.

### Part 3: Half-Open State and Recovery

1. **Observe Half-Open Transition**
   
   Continue calling the service. On the 5th total attempt:
   ```
   GET http://localhost:8081/api/demo/circuit-breaker/call-service
   ```
   
   You'll see:
   ```json
   {
     "note": "Circuit breaker entering HALF-OPEN state for testing",
     "circuitAction": "Circuit breaker reopened due to failure in HALF-OPEN state"
   }
   ```
   
   **What this means:** The circuit briefly went to HALF-OPEN to test if the service recovered, but since failures are still enabled, it failed and reopened.

2. **Simulate Service Recovery**
   
   Disable failure simulation:
   ```
   POST http://localhost:8081/api/demo/circuit-breaker/toggle-failure
   ```
   
   Response: `"Failure simulation DISABLED - services will succeed"`

3. **Watch Circuit Close**
   
   Call the service again (circuit immediately tests recovery):
   ```
   GET http://localhost:8081/api/demo/circuit-breaker/call-service
   ```
   
   Expected response:
   ```json
   {
     "status": "SUCCESS",
     "circuitState": "CLOSED",
     "failureCount": 0,
     "note": "Service appears healthy, testing recovery"
   }
   ```
   
   **What this means:** The circuit detected the service is healthy and immediately tested recovery. Since the test succeeded, the circuit closed and normal operation resumed.

### Part 4: Experiment on Your Own

Try these scenarios:

1. **Intermittent Failures**
   - Toggle failures on and off between calls
   - See how the circuit breaker responds to sporadic issues

2. **Quick Recovery**
   - Open the circuit with 3 failures
   - Immediately disable failures
   - See how quickly the circuit recovers

3. **Reset and Retry**
   ```
   POST http://localhost:8081/api/demo/circuit-breaker/reset
   ```
   - This clears all state
   - Try different failure patterns

## Understanding Circuit Breaker States

### CLOSED (Normal Operation)
- All requests pass through to the service
- Failures are counted
- Too many failures trigger opening

### OPEN (Failing Fast)
- Requests immediately return fallback response
- No calls to the failing service (prevents cascading failures)
- After a timeout/threshold, transitions to HALF-OPEN

### HALF-OPEN (Testing Recovery)
- Limited requests allowed through to test service health
- Success → Circuit closes
- Failure → Circuit reopens

## Key Concepts Demonstrated

1. **Fail Fast:** When a service is down, don't waste time waiting for timeouts
2. **Fallback Responses:** Provide degraded but functional service
3. **Automatic Recovery:** Circuit automatically tests and recovers when service is healthy
4. **Prevents Cascading Failures:** One failing service doesn't bring down the entire system

## Common Issues and Solutions

**Issue:** Port 8081 already in use
**Solution:** Stop other applications or change port in `application.properties`

**Issue:** Cannot connect to localhost:8081
**Solution:** Ensure the application started successfully (check console for errors)

**Issue:** Circuit doesn't open after failures
**Solution:** Make sure you've enabled failure simulation first

## Challenge Exercises

1. **Find the Pattern:** How many calls in the OPEN state before it tries HALF-OPEN? (Hint: It's based on attempt number modulo 5)

2. **Custom Thresholds:** Modify `CircuitBreakerController.java` to change the failure threshold from 3 to 5

3. **Add Metrics:** Extend the status endpoint to show average response time and success rate

## Next Steps

After completing this exercise, you should understand:
- How circuit breakers protect distributed systems
- The three states and their transitions
- Why fallback responses are important
- How this pattern prevents cascading failures

In production, you'd use libraries like Resilience4j or Hystrix with real service calls, but this simulation demonstrates the core concepts clearly.