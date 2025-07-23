---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Reactive Programming
  Building async, non-blocking applications with Project Reactor
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Reactive Programming with Project Reactor
---

# Reactive Programming

Building Async, Non-blocking Applications

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Press Space for next page <carbon:arrow-right class="inline"/>
  </span>
</div>

---

# Contact Info

Ken Kousen<br>
Kousen IT, Inc.

- ken.kousen@kousenit.com
- http://www.kousenit.com
- http://kousenit.org (blog)
- Social Media:
  - [@kenkousen](https://twitter.com/kenkousen) (Twitter)
  - [@kousenit.com](https://bsky.app/profile/kousenit.com) (Bluesky)
  - [https://www.linkedin.com/in/kenkousen/](https://www.linkedin.com/in/kenkousen/) (LinkedIn)
- *Tales from the jar side* (free newsletter)
  - https://kenkousen.substack.com
  - https://youtube.com/@talesfromthejarside

---
transition: slide-left
---

# What is Reactive Programming?

## Programming Paradigm

<v-clicks>

- Asynchronous data streams
- Non-blocking operations
- Event-driven architecture

</v-clicks>

## Key Concepts

<v-clicks>

- **Publisher** - Emits data
- **Subscriber** - Consumes data
- **Backpressure** - Flow control

</v-clicks>

---
transition: slide-left
---

# Why Reactive?

## Traditional Blocking Model

<v-clicks>

- Thread per request
- Blocks on I/O operations
- Limited scalability

</v-clicks>

## Reactive Benefits

<v-clicks>

- Better resource utilization
- Higher throughput
- Responsive under load

</v-clicks>

---
transition: slide-left
---

# Project Reactor

## Spring's Reactive Library

<v-clicks>

- **Mono** - 0 or 1 element
- **Flux** - 0 to N elements
- Non-blocking I/O

</v-clicks>

## Reactive Streams

<v-clicks>

- Standard specification
- Backpressure support
- Composable operations

</v-clicks>

---
transition: slide-left
---

# Mono Basics

## Single Value Publisher

```java
import reactor.core.publisher.Mono;

public class EmployeeService {
    
    public Mono<Employee> findById(Long id) {
        return Mono.fromCallable(() -> 
            database.findEmployee(id)
        ).subscribeOn(Schedulers.boundedElastic());
    }
    
    public Mono<Employee> createEmployee(Employee emp) {
        return Mono.fromCallable(() -> 
            database.save(emp)
        );
    }
}
```

---
transition: slide-left
---

# Flux Basics

## Multiple Values Publisher

```java
import reactor.core.publisher.Flux;

public class EmployeeService {
    
    public Flux<Employee> findAll() {
        return Flux.fromIterable(
            database.getAllEmployees()
        );
    }
    
    public Flux<Employee> findByDepartment(String dept) {
        return Flux.fromIterable(database.findByDepartment(dept))
            .filter(emp -> emp.isActive());
    }
}
```

---
transition: slide-left
---

# Subscribing to Streams

## Consuming Reactive Streams

```java
public void demonstrateSubscription() {
    Mono<Employee> employee = employeeService.findById(1L);
    
    // Subscribe with consumer
    employee.subscribe(
        emp -> System.out.println("Found: " + emp.getName()),
        error -> System.err.println("Error: " + error),
        () -> System.out.println("Completed")
    );
}
```

---
transition: slide-left
---

# Transforming Data

## Map and FlatMap

```java
public Mono<EmployeeDTO> getEmployeeWithDepartment(Long id) {
    return employeeService.findById(id)
        .flatMap(employee -> 
            departmentService.findById(employee.getDepartmentId())
                .map(department -> 
                    new EmployeeDTO(employee, department)
                )
        );
}
```

---
transition: slide-left
---

# Error Handling

## Reactive Error Strategies

```java
public Mono<Employee> findEmployeeWithFallback(Long id) {
    return employeeService.findById(id)
        .onErrorReturn(new Employee(id, "Unknown", "unknown@company.com"))
        .timeout(Duration.ofSeconds(5))
        .retry(3);
}

public Mono<Employee> findEmployeeWithRecovery(Long id) {
    return employeeService.findById(id)
        .onErrorResume(error -> {
            logger.error("Failed to find employee", error);
            return cacheService.getEmployee(id);
        });
}
```

---
transition: slide-left
---

# Combining Streams

## Zip and Merge

```java
public Mono<EmployeeReport> createReport(Long empId) {
    Mono<Employee> employee = employeeService.findById(empId);
    Mono<Department> department = departmentService.findById(deptId);
    Mono<Salary> salary = payrollService.getSalary(empId);
    
    return Mono.zip(employee, department, salary)
        .map(tuple -> new EmployeeReport(
            tuple.getT1(), // employee
            tuple.getT2(), // department
            tuple.getT3()  // salary
        ));
}
```

---
transition: slide-left
---

# WebFlux Controllers

## Reactive REST APIs

```java
@RestController
@RequestMapping("/api/employees")
public class ReactiveEmployeeController {
    
    @GetMapping("/{id}")
    public Mono<Employee> getEmployee(@PathVariable Long id) {
        return employeeService.findById(id);
    }
    
    @GetMapping
    public Flux<Employee> getAllEmployees() {
        return employeeService.findAll();
    }
    
    @PostMapping
    public Mono<Employee> createEmployee(@RequestBody Employee emp) {
        return employeeService.save(emp);
    }
}
```

---
transition: slide-left
---

# WebClient

## Reactive HTTP Client

```java
@Service
public class ReactiveEmployeeClient {
    private final WebClient webClient;
    
    public ReactiveEmployeeClient() {
        this.webClient = WebClient.builder()
            .baseUrl("http://employee-service")
            .build();
    }
    
    public Mono<Employee> getEmployee(Long id) {
        return webClient.get()
            .uri("/api/employees/{id}", id)
            .retrieve()
            .bodyToMono(Employee.class);
    }
}
```

---
transition: slide-left
---

# Reactive Database Access

## R2DBC Integration

```java
@Repository
public class ReactiveEmployeeRepository {
    private final DatabaseClient client;
    
    public Mono<Employee> findById(Long id) {
        return client.sql("SELECT * FROM employees WHERE id = :id")
            .bind("id", id)
            .map(row -> new Employee(
                row.get("id", Long.class),
                row.get("name", String.class),
                row.get("email", String.class)
            ))
            .first();
    }
}
```

---
transition: slide-left
---

# Backpressure

## Handling Fast Producers

```java
public Flux<Employee> processEmployees() {
    return employeeService.findAll()
        .onBackpressureBuffer(1000) // Buffer up to 1000 items
        .flatMap(employee -> 
            processEmployee(employee)
                .subscribeOn(Schedulers.parallel()),
            4 // Concurrency of 4
        );
}

public Flux<Employee> processWithDropping() {
    return employeeService.findAll()
        .onBackpressureDrop(dropped -> 
            logger.warn("Dropped employee: {}", dropped.getId())
        );
}
```

---
transition: slide-left
---

# Schedulers

## Threading in Reactive

```java
public Mono<Employee> processEmployee(Employee emp) {
    return Mono.fromCallable(() -> {
        // CPU-intensive work
        return expensiveCalculation(emp);
    })
    .subscribeOn(Schedulers.parallel()) // CPU-bound
    .then(Mono.fromCallable(() -> {
        // I/O operation
        return database.save(emp);
    }))
    .subscribeOn(Schedulers.boundedElastic()); // I/O-bound
}
```

---
transition: slide-left
---

# Hot vs Cold Streams

## Understanding Publishers

```java
// Cold stream - starts from beginning for each subscriber
Flux<Employee> coldEmployees = Flux.fromIterable(employeeList);

// Hot stream - shares data among subscribers
ConnectableFlux<Employee> hotEmployees = 
    Flux.interval(Duration.ofSeconds(1))
        .map(i -> generateEmployee())
        .publish();

hotEmployees.connect(); // Start emitting
```

---
transition: slide-left
---

# Testing Reactive Code

## StepVerifier

```java
@Test
void testEmployeeService() {
    Employee testEmployee = new Employee(1L, "John", "john@test.com");
    
    Mono<Employee> result = employeeService.findById(1L);
    
    StepVerifier.create(result)
        .expectNext(testEmployee)
        .verifyComplete();
}

@Test
void testEmployeeServiceWithError() {
    StepVerifier.create(employeeService.findById(999L))
        .expectError(EmployeeNotFoundException.class)
        .verify();
}
```

---
transition: slide-left
---

# Testing Time-based Operations

## Virtual Time

```java
@Test
void testDelayedOperation() {
    Flux<Employee> delayed = employeeService.findAll()
        .delayElements(Duration.ofMinutes(1));
    
    StepVerifier.withVirtualTime(() -> delayed.take(2))
        .expectSubscription()
        .thenAwait(Duration.ofMinutes(1))
        .expectNextCount(1)
        .thenAwait(Duration.ofMinutes(1))
        .expectNextCount(1)
        .verifyComplete();
}
```

---
transition: slide-left
---

# Server-Sent Events

## Real-time Updates

```java
@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<Employee> streamEmployees() {
    return Flux.interval(Duration.ofSeconds(1))
        .map(sequence -> generateRandomEmployee())
        .take(10);
}

@GetMapping("/notifications")
public Flux<ServerSentEvent<String>> notifications() {
    return notificationService.getNotifications()
        .map(notification -> ServerSentEvent.builder(notification)
            .id(String.valueOf(notification.getId()))
            .event("employee-update")
            .build());
}
```

---
transition: slide-left
---

# Performance Considerations

## Optimization Tips

<v-clicks>

- Use appropriate schedulers
- Avoid blocking operations
- Consider backpressure strategies

</v-clicks>

## Common Pitfalls

<v-clicks>

- Blocking in reactive chains
- Not handling errors properly
- Creating too many threads

</v-clicks>

---
transition: slide-left
---

# When to Use Reactive

## Good Use Cases

<v-clicks>

- High-concurrency applications
- I/O-intensive operations
- Real-time data streams

</v-clicks>

## Consider Alternatives

<v-clicks>

- Simple CRUD applications
- CPU-intensive tasks
- Team unfamiliar with reactive

</v-clicks>

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- Reactive programming enables non-blocking, async operations
- Mono handles single values, Flux handles streams
- WebFlux provides reactive web framework
- Use appropriate schedulers and handle backpressure
- Test with StepVerifier and virtual time

</v-clicks>

---
transition: slide-left
layout: center
---

# Course Complete!

You've mastered Java from basics to advanced reactive programming