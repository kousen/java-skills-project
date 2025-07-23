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
public class EmployeeServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(
            EmployeeServiceApplication.class, args);
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

## Domain Object

```java
public class Employee {
    private Long id;
    private String name;
    private String department;
    private Double salary;
    
    // Constructors, getters, setters
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
    employee.setId(generateId());
    return employee;
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

// Automatically converts to Employee object
@PostMapping
public Employee create(@RequestBody Employee employee) {
    // employee object is populated from JSON
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
    employee.setId(id);
    // In real app, update in database
    return employee;
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

# Service Layer

## Business Logic Separation

```java
@Service
public class EmployeeService {
    private final Map<Long, Employee> employees = 
        new ConcurrentHashMap<>();
    
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(generateId());
        }
        employees.put(employee.getId(), employee);
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

# Controller with Service

## Dependency Injection

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService service;
    
    public EmployeeController(EmployeeService service) {
        this.service = service;
    }
    
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return service.findById(id)
            .orElseThrow(() -> 
                new EmployeeNotFoundException(id));
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

## Centralized Error Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ErrorResponse handleNotFound(
            EmployeeNotFoundException ex) {
        return new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            Instant.now()
        );
    }
}
```

---
transition: slide-left
---

# Request Validation

## Bean Validation

```java
public class Employee {
    @NotNull
    @Size(min = 2, max = 100)
    private String name;
    
    @NotNull
    private String department;
    
    @Min(0)
    @Max(1000000)
    private Double salary;
}
```

---
transition: slide-left
---

# Validation in Controller

## @Valid Annotation

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Employee createEmployee(
        @Valid @RequestBody Employee employee) {
    return service.save(employee);
}

// Spring automatically returns 400 Bad Request
// if validation fails
```

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

## Spring Boot Test

```java
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetEmployee() throws Exception {
        mockMvc.perform(get("/api/employees/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }
}
```

---
transition: slide-left
---

# Testing POST Requests

## Creating Data in Tests

```java
@Test
void testCreateEmployee() throws Exception {
    String json = """
        {
            "name": "New Employee",
            "department": "IT",
            "salary": 80000
        }
        """;
    
    mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("New Employee"));
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

## Three Ways to Start

<v-clicks>

1. IDE: Run main method
2. Maven: `mvn spring-boot:run`
3. Gradle: `gradle bootRun`

</v-clicks>

```bash
# Application starts on http://localhost:8080
```

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- Spring Boot makes REST API creation simple
- Use @RestController and mapping annotations
- Separate concerns with Service layer
- Validate input with Bean Validation
- Test with MockMvc

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Input Validation

Preventing SQL injection and XSS attacks