# Try It Out: Reactive Programming with Project Reactor

## Overview
This exercise demonstrates reactive programming concepts using Spring WebFlux and Project Reactor. You'll explore Mono and Flux operations, see how backpressure works, and understand the difference between blocking and non-blocking code.

## Prerequisites
- Java 21 installed
- A REST client (Postman, IntelliJ HTTP Client, curl, or browser extensions)
- The reactive module running
- (Optional) `jq` for pretty JSON output (`brew install jq` on macOS)

## Starting the Application

1. Open a terminal in the project root directory
2. Run the reactive application:
   ```bash
   gradle :reactive:bootRun
   ```
3. Look for this message in the console:
   ```
   🚀 REACTIVE PROGRAMMING DEMO STARTED!
   Interactive endpoints available:
   ```
4. The application runs on port 8080 by default

## Using the Demo Script (Recommended)

For easier interaction during exercises, use the provided demo script:

```bash
cd reactive
chmod +x demo-scripts.sh  # First time only
./demo-scripts.sh         # Shows interactive menu
```

The script provides shortcuts for common operations:
- `./demo-scripts.sh mono3` - Test 3 concurrent requests
- `./demo-scripts.sh error` - Toggle error simulation
- `./demo-scripts.sh slow` - Toggle slow processing
- `./demo-scripts.sh reset` - Reset all demo state
- `./demo-scripts.sh status` - Check current status
- `./demo-scripts.sh perf` - Run performance test

## Part 1: Understanding Mono (Single Values)

### Exercise 1.1: Basic Mono Operation

1. **Call the Mono demo endpoint**
   ```
   GET http://localhost:8080/api/demo/reactive/mono
   ```
   
   Expected response:
   ```json
   {
     "operation": "Mono Demonstration",
     "original": "Alice Johnson",
     "transformed": "ALICE JOHNSON",
     "salaryIncrease": "10%",
     "newSalary": 93500.0,
     "processingTime": "100ms (fast)",
     "message": "Mono represents a single value or empty result"
   }
   ```
   
   **What this means:** Mono wraps a single value. The operations (uppercase, salary increase) are applied lazily when subscribed.

### Exercise 1.2: Simulating Slow Processing

1. **Enable slow processing**
   ```
   POST http://localhost:8080/api/demo/reactive/toggle-slowness
   ```
   
   Response: `"Slowness simulation ENABLED (2s delay)"`

2. **Call Mono again**
   ```
   GET http://localhost:8080/api/demo/reactive/mono
   ```
   
   Notice it takes 2 seconds but your thread isn't blocked - other requests can still be processed!

3. **Disable slow processing**
   ```
   POST http://localhost:8080/api/demo/reactive/toggle-slowness
   ```

## Part 2: Understanding Flux (Multiple Values)

### Exercise 2.1: Basic Flux Stream

1. **Call the Flux demo endpoint**
   ```
   GET http://localhost:8080/api/demo/reactive/flux
   ```
   
   You'll receive a JSON array, but notice each element is emitted with a 200ms delay. The response streams back as data becomes available.
   
   Expected: Employees with salary > 70000 get a 5% raise

### Exercise 2.2: Server-Sent Events

1. **Open a browser or use curl for streaming**
   ```
   curl http://localhost:8080/api/employees/stream
   ```
   
   Or in browser: `http://localhost:8080/api/employees/stream`
   
   You'll see employees arriving one per second as Server-Sent Events. This is a true reactive stream!

### Exercise 2.3: Infinite Stream with Limit

1. **Request an infinite stream with limit**
   ```
   curl http://localhost:8080/api/employees/infinite?limit=5
   ```
   
   Counts from 0 to 4, one number per second. The stream automatically completes after 5 items.

## Part 3: Combining Reactive Streams

### Exercise 3.1: Zip Multiple Sources

1. **Call the combine endpoint**
   ```
   GET http://localhost:8080/api/demo/reactive/combine
   ```
   
   This demonstrates `Mono.zip()` - it waits for all three sources (employee, department, salary) and combines them. Each source has different delays (100ms, 150ms, 200ms) but they run in parallel!

### Exercise 3.2: Complete Profile

1. **Get a complete employee profile**
   ```
   GET http://localhost:8080/api/employees/1/profile
   ```
   
   This combines data from multiple "services" into a complete profile.

## Part 4: Error Handling

### Exercise 4.1: Error Recovery

1. **Check error handling with simulation off**
   ```
   GET http://localhost:8080/api/demo/reactive/error
   ```
   
   Response: `"result": "Success: Data retrieved successfully"`

2. **Enable error simulation**
   ```
   POST http://localhost:8080/api/demo/reactive/toggle-error
   ```

3. **Try again with errors**
   ```
   GET http://localhost:8080/api/demo/reactive/error
   ```
   
   Response: `"result": "Fallback: Service temporarily unavailable"`
   
   The error was handled gracefully with a fallback value!

4. **Disable error simulation**
   ```
   POST http://localhost:8080/api/demo/reactive/toggle-error
   ```

## Part 5: Backpressure

### Exercise 5.1: Understanding Backpressure

1. **Call the backpressure demo**
   ```
   GET http://localhost:8080/api/demo/reactive/backpressure
   ```
   
   Expected response shows:
   ```json
   {
     "itemsGenerated": 1000,
     "itemsProcessed": 50,
     "itemsDropped": [varies],
     "bufferSize": 10,
     "strategy": "DROP_OLDEST"
   }
   ```
   
   **What this means:** When the consumer can't keep up, backpressure strategies prevent memory overflow by dropping or buffering items.

## Part 6: Real-Time Events

### Exercise 6.1: Monitor Event Stream

1. **Open the event stream in a browser**
   ```
   http://localhost:8080/api/demo/reactive/events
   ```

2. **In another tab, perform operations**
   - Create employee: `POST /api/employees` with body
   - Call various demo endpoints
   
3. **Watch events appear in real-time** in the first tab

## Part 7: Practical Operations

### Exercise 7.1: Search and Filter

1. **Search employees by criteria**
   ```
   GET http://localhost:8080/api/employees/search?department=Engineering&minSalary=80000
   ```
   
   Returns a Flux of employees matching all criteria.

### Exercise 7.2: Department Statistics

1. **Get department stats**
   ```
   GET http://localhost:8080/api/employees/departments/Engineering/statistics
   ```
   
   Aggregates data reactively from the employee stream.

### Exercise 7.3: Bulk Operation

1. **Give everyone in Marketing a 10% raise**
   ```
   POST http://localhost:8080/api/employees/departments/Marketing/raise?percentage=10
   ```
   
   Returns a Flux of updated employees - each processed reactively.

## Part 8: Delayed Responses

### Exercise 8.1: Simulate Network Delay

1. **Request with custom delay**
   ```
   GET http://localhost:8080/api/employees/delayed?seconds=3
   ```
   
   The response takes 3 seconds, but the thread isn't blocked. Try making multiple requests simultaneously!

## Understanding Reactive vs Blocking

### Key Observations:

1. **Non-blocking:** While waiting for one slow request, the server can handle others
2. **Streaming:** Data flows as it becomes available, not all at once
3. **Backpressure:** Prevents overwhelming slow consumers
4. **Composition:** Complex operations built from simple reactive primitives

### Try This Comparison:

1. **Make 5 simultaneous slow requests:**
   ```bash
   for i in {1..5}; do
     curl "http://localhost:8080/api/employees/delayed?seconds=2" &
   done
   ```
   
   All complete in ~2 seconds (parallel), not 10 seconds (sequential)!

## Common Patterns to Observe

1. **Lazy Evaluation:** Operations don't execute until subscribed
2. **Error Propagation:** Errors flow through the stream and can be handled at any point
3. **Resource Efficiency:** Few threads handle many concurrent operations
4. **Time-based Operations:** Delays, timeouts, and intervals are first-class concepts

## Challenge Exercises

### Challenge 1: Chain Operations
Create an employee, update their salary, then get their complete profile - all using reactive chains.

### Challenge 2: Error Recovery
Enable error simulation and observe how different endpoints handle errors. Notice retry attempts in the event stream.

### Challenge 3: Performance Test
Compare the performance of:
- 100 sequential blocking calls (traditional)
- 100 concurrent reactive calls

Use the `/api/employees/delayed?seconds=1` endpoint.

## Troubleshooting

**Issue:** Server-Sent Events not streaming
**Solution:** Use curl or a browser, not all REST clients support SSE

**Issue:** Port 8080 already in use
**Solution:** Stop other applications or change port in `application.properties`

**Issue:** Operations seem instant despite delays
**Solution:** Check that you're looking at individual items, not the complete response

## Key Concepts Demonstrated

1. **Mono vs Flux:** Single vs multiple values
2. **Hot vs Cold:** Cold streams start fresh for each subscriber
3. **Backpressure:** Flow control between fast producers and slow consumers
4. **Error Handling:** Resilient stream processing
5. **Composition:** Building complex flows from simple operations

## Next Steps

After completing this exercise, you understand:
- How reactive streams differ from traditional blocking I/O
- When to use Mono vs Flux
- How backpressure prevents system overload
- Why reactive programming enables high-concurrency applications

Experiment with:
- Creating custom operators
- Testing with StepVerifier
- Building complete reactive applications
- Integrating with reactive databases (R2DBC)