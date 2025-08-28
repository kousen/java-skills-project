# Reactive Programming Module

Topic 26: Reactive Programming with Project Reactor

## Overview
This module demonstrates reactive programming concepts using Spring WebFlux and Project Reactor. It showcases non-blocking I/O, reactive streams, backpressure handling, and event-driven architectures.

## Quick Start

### Running the Application
```bash
# From project root
gradle :reactive:bootRun

# Application starts on port 8080
```

### Running Tests
```bash
# Run all tests
gradle :reactive:test

# Tests use StepVerifier for reactive stream testing
```

## Project Structure

```
reactive/
├── src/main/java/com/oreilly/reactive/
│   ├── ReactiveApplication.java          # Main Spring Boot application
│   ├── ReactiveRecords.java             # Java 21 record classes
│   ├── ReactiveEmployeeService.java     # Service layer with reactive operations
│   ├── ReactiveEmployeeController.java  # REST API with Mono/Flux endpoints
│   └── ReactiveDemoController.java      # Interactive demo endpoints
├── src/test/java/com/oreilly/reactive/
│   ├── ReactiveEmployeeServiceTest.java # Service layer tests
│   └── ReactiveDemoControllerTest.java  # Controller tests
├── demo-scripts.sh                      # Shell script for easy demos
├── REACTIVE_PROGRAMMING_EXERCISE.md     # Hands-on exercises
└── reactive_diagrams.md                 # Mermaid diagrams for concepts
```

## Key Components

### ReactiveEmployeeService
- CRUD operations returning Mono/Flux
- Event streaming with Sinks
- Simulated delays for realistic async behavior
- Department statistics and bulk operations

### ReactiveDemoController
- `/api/demo/reactive/*` endpoints for interactive learning
- Toggle error and slowness simulations
- Backpressure demonstration
- Server-Sent Events streaming

### ReactiveEmployeeController
- `/api/employees/*` REST endpoints
- Traditional CRUD with reactive types
- Real-time streaming endpoints
- Complete profile aggregation

## Demo Script Usage

The `demo-scripts.sh` file provides easy command-line demos:

```bash
# Make executable (first time only)
chmod +x demo-scripts.sh

# Interactive menu
./demo-scripts.sh

# Direct commands
./demo-scripts.sh mono3      # 3 concurrent requests
./demo-scripts.sh mono10     # 10 concurrent requests
./demo-scripts.sh error      # Toggle error simulation
./demo-scripts.sh slow       # Toggle slowness simulation
./demo-scripts.sh reset      # Reset demo state
./demo-scripts.sh status     # Check current status
./demo-scripts.sh backpressure  # Backpressure demo
./demo-scripts.sh perf       # Performance test (100 concurrent)
./demo-scripts.sh events     # Stream events (Ctrl+C to stop)
```

## Interactive Endpoints

### Demo Control
- `GET /api/demo/reactive/status` - Current demo status
- `POST /api/demo/reactive/toggle-error` - Toggle error simulation
- `POST /api/demo/reactive/toggle-slowness` - Toggle slow processing
- `POST /api/demo/reactive/reset` - Reset all state

### Reactive Demonstrations
- `GET /api/demo/reactive/mono` - Mono operations demo
- `GET /api/demo/reactive/flux` - Flux streaming demo
- `GET /api/demo/reactive/transform` - Transformation operations
- `GET /api/demo/reactive/combine` - Combining multiple streams
- `GET /api/demo/reactive/error` - Error handling strategies
- `GET /api/demo/reactive/backpressure` - Backpressure handling
- `GET /api/demo/reactive/events` - Server-Sent Events stream

### Employee API
- `GET /api/employees/{id}` - Get single employee (Mono)
- `GET /api/employees` - Get all employees (Flux)
- `POST /api/employees` - Create employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee
- `GET /api/employees/stream` - Stream employees (SSE)
- `GET /api/employees/events` - Stream employee events
- `GET /api/employees/search?name=&dept=&minSalary=` - Search
- `GET /api/employees/{id}/profile` - Complete profile
- `GET /api/employees/delayed?seconds=N` - Delayed response
- `GET /api/employees/infinite?limit=N` - Infinite stream

## Key Concepts Demonstrated

1. **Mono vs Flux** - Single vs multiple value publishers
2. **Non-blocking I/O** - Thread efficiency with async operations
3. **Backpressure** - Handling fast producers and slow consumers
4. **Error Handling** - Resilient stream processing
5. **Stream Composition** - Combining and transforming streams
6. **Event Streaming** - Real-time updates with SSE
7. **Testing** - StepVerifier for reactive stream testing

## Testing Approach

Tests use StepVerifier to verify reactive streams:
- `expectNext()` - Verify specific values
- `expectNextMatches()` - Verify with predicates
- `expectError()` - Verify error conditions
- `withVirtualTime()` - Test time-based operations
- `then()` - Trigger actions during verification

## Dependencies

- Spring Boot 3.5.3
- Spring WebFlux (Reactive Web)
- Project Reactor
- Reactor Test (StepVerifier)

## Tips for Video Recording

1. Start the application first
2. Use `demo-scripts.sh` for consistent demos
3. Show the Mermaid diagrams to visualize concepts
4. Use the exercise document for hands-on portions
5. Monitor `/api/demo/reactive/status` to show state changes

## Troubleshooting

**Port 8080 already in use:**
```bash
# Find process using port
lsof -i :8080
# Change port in application.properties if needed
```

**Script permission denied:**
```bash
chmod +x demo-scripts.sh
```

**JSON output not pretty:**
```bash
# Install jq for formatted JSON
brew install jq  # macOS
apt-get install jq  # Ubuntu
```