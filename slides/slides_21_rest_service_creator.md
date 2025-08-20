---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Creating REST Services
  Build REST APIs with Spring Boot
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Creating REST Services with Spring Boot
---

# Creating REST Services

Build REST APIs with Spring Boot

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

# Why Spring Boot?

## The Industry Standard

<v-clicks>

- Most popular Java framework
- Convention over configuration
- Production-ready features built-in

</v-clicks>

## Developer Experience

<v-clicks>

- Minimal boilerplate code
- Embedded server included
- Auto-configuration magic

</v-clicks>

---
transition: slide-left
---

# Spring Boot Setup

## Dependencies We Need

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---
transition: slide-left
---

# Main Application Class

## Starting Point

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebServicesApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WebServicesApplication.class, args);
    }
}
```

---
transition: slide-left
---

# Your First REST Controller

## Basic Controller Structure

```java
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Welcome to Employee Service!";
    }
}
```

---
transition: slide-left
---

# REST Annotations

## Core Mappings

<v-clicks>

- **@RestController** - Marks class as REST controller
- **@RequestMapping** - Base path for all endpoints
- **@GetMapping** - Handles GET requests

</v-clicks>

---
transition: slide-left
---

# REST Annotations (continued)

## CRUD Operations

<v-clicks>

- **@PostMapping** - Handles POST requests
- **@PutMapping** - Handles PUT requests
- **@DeleteMapping** - Handles DELETE requests

</v-clicks>

---
transition: slide-left
---

# Employee Model

## Modern Java Record

```java
public record Employee(Long id, String name, 
                      String department, Double salary) {
    
    // Constructor for new employees (no ID yet)
    public Employee(String name, String department, Double salary) {
        this(null, name, department, salary);
    }
    
    // Helper methods for immutable updates
    public Employee withId(Long id) {
        return new Employee(id, name, department, salary);
    }
    
    public Employee withSalary(Double salary) {
        return new Employee(id, name, department, salary);
    }
}
```

---
transition: slide-left
---

# GET Endpoint

## Retrieving Single Employee

```java
@GetMapping("/{id}")
public Employee getEmployee(@PathVariable Long id) {
    // In real app, fetch from database
    return new Employee(id, "John Doe", 
        "Engineering", 75000.0);
}
```

---
transition: slide-left
---

# GET All Employees

## Retrieving Collections

```java
@GetMapping
public List<Employee> getAllEmployees() {
    return List.of(
        new Employee(1L, "John Doe", "Engineering", 75000.0),
        new Employee(2L, "Jane Smith", "Marketing", 65000.0)
    );
}
```

---
transition: slide-left
---

# POST Endpoint

## Creating New Employee

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Employee createEmployee(@RequestBody Employee employee) {
    // In real app, save to database
    Employee savedEmployee = employee.withId(generateId());
    return savedEmployee;
}
```

---
transition: slide-left
---

# Request Body Binding

## @RequestBody Annotation

```java
// JSON Request:
{
    "name": "Alice Johnson",
    "department": "Sales",
    "salary": 70000
}

// Automatically converts to Employee record
@PostMapping
public Employee create(@RequestBody Employee employee) {
    // employee record is populated from JSON
}
```

---
transition: slide-left
---

# PUT Endpoint

## Updating Employee

```java
@PutMapping("/{id}")
public Employee updateEmployee(
        @PathVariable Long id,
        @RequestBody Employee employee) {
    Employee updatedEmployee = employee.withId(id);
    // In real app, update in database
    return updatedEmployee;
}
```

---
transition: slide-left
---

# DELETE Endpoint

## Removing Employee

```java
@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteEmployee(@PathVariable Long id) {
    // In real app, delete from database
}
```

---
transition: slide-left
---

# Layered Architecture

## Three-Tier Design

<v-clicks>

- **Controller Layer** - REST endpoints, request/response handling
- **Service Layer** - Business logic, validation, orchestration
- **Repository Layer** - Data access, persistence operations

</v-clicks>

---
transition: slide-left
---

# Repository Layer

## Data Access

```java
@Repository
public class EmployeeRepository {
    private final Map<Long, Employee> employees = 
        new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public Employee save(Employee employee) {
        if (employee.id() == null) {
            Long id = idGenerator.getAndIncrement();
            employee = employee.withId(id);
        }
        employees.put(employee.id(), employee);
        return employee;
    }
    
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }
}
```

---
transition: slide-left
---

# Service Layer

## Business Logic & Validation

```java
@Service
public class EmployeeService {
    private static final double MIN_SALARY = 30000.0;
    private static final double MAX_SALARY = 500000.0;
    private final EmployeeRepository employeeRepository;
    
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public Employee processNewHire(Employee employee) {
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }
    
    public Employee giveRaise(Long id, Double amount) {
        Employee employee = findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
        return employeeRepository.save(
            employee.withSalary(employee.salary() + amount));
    }
}
```

---
transition: slide-left
---

# Transaction Management

## Professional Service Layer Patterns

```java
@Service
@Transactional  // Default for all methods
public class EmployeeService {
    
    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }
    
    @Transactional(readOnly = true) 
    public List<Employee> findHighPerformers() {
        return employeeRepository.findBySalaryGreaterThanEqual(80000.0);
    }
    
    // Write operations use default @Transactional
    public Employee giveRaise(Long id, double amount) {
        // Business logic with transaction boundaries
    }
}
```

---
transition: slide-left
---

# Transaction Configuration

## Enabling Transaction Management

```java
@SpringBootApplication
@EnableTransactionManagement
public class WebServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebServicesApplication.class, args);
    }
}
```

<v-clicks>

- **@EnableTransactionManagement** - Enables Spring's transaction infrastructure
- **Read-only optimization** - `@Transactional(readOnly = true)` for queries
- **Business boundaries** - Transactions at service layer, not repository

</v-clicks>

---
transition: slide-left
---

# Controller Layer

## REST Endpoints

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return employeeService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody Employee employee) {
        Employee saved = employeeService.processNewHire(employee);
        return ResponseEntity.created(/* location */).body(saved);
    }
}
```

---
transition: slide-left
---

# Exception Handling

## Custom Exception

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException 
        extends RuntimeException {
    
    public EmployeeNotFoundException(Long id) {
        super("Employee not found: " + id);
    }
}
```

---
transition: slide-left
---

# Global Exception Handler

## Modern Error Handling with ProblemDetail

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ProblemDetail handleEmployeeNotFound(
            EmployeeNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
            .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create(
            "https://api.example.com/problems/employee-not-found"));
        return problemDetail;
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleValidation(
            IllegalArgumentException ex) {
        return ProblemDetail
            .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
```

---
transition: slide-left
---

# Employee Record

## Modern Immutable Data

```java
public record Employee(Long id, String name, 
                      String department, Double salary) {
    
    // Constructor for new employees (no ID yet)
    public Employee(String name, String department, Double salary) {
        this(null, name, department, salary);
    }
    
    // Helper method for immutable updates
    public Employee withId(Long id) {
        return new Employee(id, name, department, salary);
    }
    
    public Employee withSalary(Double salary) {
        return new Employee(id, name, department, salary);
    }
}
```

---
transition: slide-left
---

# Request Validation

## Business Logic Validation

```java
@Service
public class EmployeeService {
    private static final double MIN_SALARY = 30000.0;
    private static final double MAX_SALARY = 500000.0;
    
    private void validateEmployee(Employee employee) {
        if (employee.salary() < MIN_SALARY || 
            employee.salary() > MAX_SALARY) {
            throw new IllegalArgumentException(
                String.format("Salary %.2f must be between %.2f and %.2f",
                    employee.salary(), MIN_SALARY, MAX_SALARY));
        }
    }
}
```

---
transition: slide-left
---

# Business Logic Endpoints

## Beyond Simple CRUD

```java
@RestController 
@RequestMapping("/api/employees")
public class EmployeeController {
    
    @PutMapping("/{id}/raise")
    public ResponseEntity<Employee> giveRaise(
            @PathVariable Long id,
            @RequestParam Double amount) {
        Employee updated = employeeService.giveRaise(id, amount);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/{id}/standard-raise")  
    public ResponseEntity<Employee> giveStandardRaise(
            @PathVariable Long id) {
        Employee updated = employeeService.giveStandardRaise(id);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/high-performers")
    public List<Employee> getHighPerformers() {
        return employeeService.findHighPerformers();
    }
}

---
transition: slide-left
---

# Query Parameters

## Filtering Results

```java
@GetMapping
public List<Employee> searchEmployees(
        @RequestParam(required = false) String department,
        @RequestParam(defaultValue = "0") Double minSalary) {
    
    return service.findAll().stream()
        .filter(e -> department == null || 
            e.getDepartment().equals(department))
        .filter(e -> e.getSalary() >= minSalary)
        .collect(Collectors.toList());
}
```

---
transition: slide-left
---

# Response Headers

## Custom Headers

```java
@GetMapping("/{id}")
public ResponseEntity<Employee> getEmployee(
        @PathVariable Long id) {
    
    Employee employee = service.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));
    
    return ResponseEntity.ok()
        .header("X-Employee-Version", "1.0")
        .body(employee);
}
```

---
transition: slide-left
---

# CORS Configuration

## Cross-Origin Requests

```java
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    // Controller methods
}

// Or globally:
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

---
transition: slide-left
---

# Testing REST APIs

## Three-Layer Testing Strategy

```java
// Repository Layer - Pure unit tests
class EmployeeRepositoryTest {
    private EmployeeRepository repository = new EmployeeRepository();
    
    @Test
    void testSaveAndFindById() {
        Employee employee = new Employee("John", "Engineering", 75000.0);
        Employee saved = repository.save(employee);
        assertThat(saved.id()).isNotNull();
    }
}

// Service Layer - Spring Boot integration with mocks
@SpringBootTest(classes = {WebServicesApplication.class})
class EmployeeServiceTest {
    @MockitoBean
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeService employeeService;
}
```

---
transition: slide-left
---

# Controller Layer Testing

## MockMvc Web Layer Tests

```java
@WebMvcTest(controllers = EmployeeController.class)
@Import({GlobalExceptionHandler.class})
class EmployeeControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private EmployeeService employeeService;
    
    @Test
    void testCreateEmployee() throws Exception {
        Employee newEmployee = new Employee("Alice", "Sales", 70000.0);
        Employee savedEmployee = new Employee(4L, "Alice", "Sales", 70000.0);
        
        when(employeeService.processNewHire(any(Employee.class)))
            .thenReturn(savedEmployee);
        
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Alice"));
    }
}
```

---
transition: slide-left
---

# Application Properties

## Configuration

```properties
# application.properties
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.com.oreilly=DEBUG
logging.level.org.springframework.web=INFO

# Jackson JSON
spring.jackson.property-naming-strategy=SNAKE_CASE
spring.jackson.default-property-inclusion=non_null
```

---
transition: slide-left
---

# Running the Application

## In Our Project Structure

<v-clicks>

- **Multi-module Gradle project**
- **web-services module** - REST APIs (Goals 20 & 21)
- **microservices module** - Spring Cloud (Goal 25)
- **reactive module** - WebFlux (Goal 26)

</v-clicks>

```bash
# Run the web-services module
gradle :web-services:bootRun

# Application starts on http://localhost:8080
# Try: http://localhost:8080/api/employees/hello
```

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- **Layered Architecture** - Controller, Service, Repository
- **Modern Java** - Records, immutable data, pattern matching
- **Spring Boot** - Auto-configuration, embedded server
- **Business Logic** - Validation, custom operations
- **Comprehensive Testing** - All three layers tested
- **Clean Separation** - Each layer has single responsibility

</v-clicks>

---
transition: slide-left
---

# Try It Out Exercise

## Employee Search Controller

<div class="grid grid-cols-2 gap-4">

<div>

### 🎯 **Your Mission**
Complete 4 TODOs in `EmployeeSearchController.java`:

<v-clicks>

1. **Constructor injection** with EmployeeService
2. **GET** `/department/{name}` with @PathVariable 
3. **POST** `/advanced` with @RequestBody filtering
4. **GET** `/departments` returning unique names

</v-clicks>

</div>

<div>

### 🛠️ **What You'll Practice**
<v-clicks>

- `@RestController` and mapping annotations
- Constructor dependency injection  
- Path variables and request bodies
- Java streams for filtering
- ResponseEntity patterns
- Reusing existing service layers

</v-clicks>

</div>

</div>

<v-clicks>

### 🚀 **Real-World Skills**
Build search endpoints that demonstrate production REST API patterns!

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Input Validation

Preventing SQL injection and XSS attacks