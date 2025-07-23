---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Microservices Architecture
  Building distributed systems with Spring Boot, REST, and messaging
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Microservices with Spring Boot
---

# Microservices Architecture

Building Distributed Systems with Spring Boot

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

# What are Microservices?

## Architectural Approach

<v-clicks>

- Small, independent services
- Single business capability
- Communicate over network

</v-clicks>

## Characteristics

<v-clicks>

- Loosely coupled
- Independently deployable
- Technology agnostic

</v-clicks>

---
transition: slide-left
---

# Monolith vs Microservices

## Monolithic Architecture

<v-clicks>

- Single deployable unit
- Shared database
- Internal method calls

</v-clicks>

## Microservices Benefits

<v-clicks>

- Independent scaling
- Technology diversity
- Fault isolation

</v-clicks>

---
transition: slide-left
---

# Employee System Architecture

## Service Breakdown

<v-clicks>

- **Employee Service** - Manage employee data
- **Department Service** - Handle departments
- **Payroll Service** - Calculate salaries

</v-clicks>

## Communication

<v-clicks>

- REST APIs for synchronous calls
- Message queues for async events

</v-clicks>

---
transition: slide-left
---

# Spring Boot Microservice

## Employee Service

```java
@SpringBootApplication
@EnableEurekaClient  // Service discovery
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

# Service Configuration

## application.yml

```yaml
server:
  port: 8081

spring:
  application:
    name: employee-service
  datasource:
    url: jdbc:h2:mem:employees
    
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

---
transition: slide-left
---

# Employee REST Controller

## API Design

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeService.findById(id);
    }
    
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }
}
```

---
transition: slide-left
---

# Service Discovery

## Eureka Server

```java
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(
            DiscoveryServerApplication.class, args);
    }
}
```

---
transition: slide-left
---

# Inter-Service Communication

## Using RestTemplate

```java
@Service
public class PayrollService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public Employee getEmployee(Long id) {
        return restTemplate.getForObject(
            "http://employee-service/api/employees/" + id,
            Employee.class
        );
    }
}
```

---
transition: slide-left
---

# Load Balancing

## Ribbon Configuration

```java
@Configuration
public class ServiceConfig {
    
    @Bean
    @LoadBalanced  // Enables client-side load balancing
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---
transition: slide-left
---

# Circuit Breaker Pattern

## Hystrix Implementation

```java
@Service
public class PayrollService {
    
    @HystrixCommand(fallbackMethod = "getEmployeeFallback")
    public Employee getEmployee(Long id) {
        return restTemplate.getForObject(
            "http://employee-service/api/employees/" + id,
            Employee.class
        );
    }
    
    public Employee getEmployeeFallback(Long id) {
        return new Employee(id, "Unknown", "Unknown", 0.0);
    }
}
```

---
transition: slide-left
---

# Message-Driven Architecture

## Spring Cloud Stream

```java
@EnableBinding(Source.class)
@RestController
public class EmployeeEventController {
    
    @Autowired
    private Source source;
    
    @PostMapping("/api/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        Employee saved = employeeService.save(employee);
        
        // Publish event
        source.output().send(
            MessageBuilder.withPayload(
                new EmployeeCreatedEvent(saved.getId())
            ).build()
        );
        
        return saved;
    }
}
```

---
transition: slide-left
---

# Event Handling

## Message Listener

```java
@EnableBinding(Sink.class)
@Component
public class DepartmentEventHandler {
    
    @StreamListener(Sink.INPUT)
    public void handleEmployeeCreated(EmployeeCreatedEvent event) {
        logger.info("Employee created: {}", event.getEmployeeId());
        
        // Update department statistics
        departmentService.incrementEmployeeCount(
            event.getDepartmentId()
        );
    }
}
```

---
transition: slide-left
---

# API Gateway

## Zuul Configuration

```java
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ApiGatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(
            ApiGatewayApplication.class, args);
    }
}
```

---
transition: slide-left
---

# Gateway Routing

## application.yml

```yaml
zuul:
  routes:
    employee-service:
      path: /employees/**
      service-id: employee-service
    department-service:
      path: /departments/**
      service-id: department-service
    payroll-service:
      path: /payroll/**
      service-id: payroll-service
```

---
transition: slide-left
---

# Distributed Configuration

## Config Server

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(
            ConfigServerApplication.class, args);
    }
}
```

---
transition: slide-left
---

# External Configuration

## Bootstrap Properties

```yaml
# bootstrap.yml in each service
spring:
  application:
    name: employee-service
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
```

---
transition: slide-left
---

# Database Per Service

## Employee Service Database

```java
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private Long departmentId; // Reference, not entity
}
```

---
transition: slide-left
---

# Data Consistency

## Saga Pattern

```java
@Service
public class EmployeeCreationSaga {
    
    public void createEmployee(CreateEmployeeCommand cmd) {
        try {
            // Step 1: Create employee
            Employee employee = employeeService.create(cmd);
            
            // Step 2: Update department
            departmentService.addEmployee(employee.getDepartmentId());
            
            // Step 3: Create payroll record
            payrollService.createRecord(employee.getId());
            
        } catch (Exception e) {
            // Compensating transactions
            rollbackEmployeeCreation(cmd);
        }
    }
}
```

---
transition: slide-left
---

# Monitoring and Observability

## Spring Boot Actuator

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
```

---
transition: slide-left
---

# Health Checks

## Custom Health Indicator

```java
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public Health health() {
        try {
            dataSource.getConnection().close();
            return Health.up()
                .withDetail("database", "Available")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("database", "Unavailable")
                .withException(e)
                .build();
        }
    }
}
```

---
transition: slide-left
---

# Distributed Tracing

## Sleuth Integration

```java
@RestController
public class EmployeeController {
    
    @Autowired
    private DepartmentClient departmentClient;
    
    @GetMapping("/{id}")
    public EmployeeDTO getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        
        // This call is automatically traced
        Department dept = departmentClient.getDepartment(
            employee.getDepartmentId()
        );
        
        return new EmployeeDTO(employee, dept);
    }
}
```

---
transition: slide-left
---

# Security in Microservices

## JWT Token Validation

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException {
        
        String token = extractToken(request);
        if (token != null && jwtService.validateToken(token)) {
            String username = jwtService.getUsernameFromToken(token);
            // Set authentication context
        }
        
        chain.doFilter(request, response);
    }
}
```

---
transition: slide-left
---

# Testing Microservices

## Contract Testing

```java
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureStubRunner(
    ids = "com.company:department-service:+:stubs:8082",
    workOffline = true)
public class EmployeeServiceContractTest {
    
    @Test
    public void shouldGetDepartmentFromStub() {
        Department dept = departmentClient.getDepartment(1L);
        assertThat(dept.getName()).isEqualTo("Engineering");
    }
}
```

---
transition: slide-left
---

# Deployment Strategies

## Docker Containerization

```dockerfile
FROM openjdk:21-jdk-slim

COPY target/employee-service-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---
transition: slide-left
---

# Container Orchestration

## Docker Compose

```yaml
version: '3.8'
services:
  eureka-server:
    image: employee-eureka:latest
    ports:
      - "8761:8761"
  
  employee-service:
    image: employee-service:latest
    ports:
      - "8081:8081"
    depends_on:
      - eureka-server
```

---
transition: slide-left
---

# Challenges and Solutions

## Common Issues

<v-clicks>

- **Network latency** - Use caching
- **Data consistency** - Implement sagas
- **Service dependencies** - Circuit breakers

</v-clicks>

---
transition: slide-left
---

# Challenges (continued)

## Operational Complexity

<v-clicks>

- **Monitoring** - Centralized logging
- **Debugging** - Distributed tracing
- **Testing** - Contract-based testing

</v-clicks>

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- Microservices enable scalable, maintainable systems
- Spring Boot provides excellent microservice support
- Consider service discovery, configuration, and monitoring
- Handle distributed data carefully
- Test service contracts, not just individual services

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Reactive Programming

Building async, non-blocking applications