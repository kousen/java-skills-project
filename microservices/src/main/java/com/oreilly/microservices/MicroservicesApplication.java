package com.oreilly.microservices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Microservices Architecture Demonstration
 * Shows Spring Boot microservice patterns including
 * - Service discovery with Eureka
 * - Load balancing
 * - Circuit breaker pattern
 * - Inter-service communication
 * - Configuration management
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MicroservicesApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MicroservicesApplication.class, args);
    }
}

/**
 * Employee Service - Manages employee data
 * Demonstrates service registration and basic CRUD operations
 * <p>
 * NOTE: This controller is part of the simulated microservice environment.
 * In a real application, this would be a separate Spring Boot application.
 */
@RestController
@RequestMapping("/api/employees")
@RefreshScope
class EmployeeController {
    
    private final MicroserviceEmployeeService employeeService;
    
    private final DepartmentHttpExchangeClient departmentClient;

    public EmployeeController(MicroserviceEmployeeService employeeService,
                              DepartmentHttpExchangeClient departmentClient) {
        this.employeeService = employeeService;
        this.departmentClient = departmentClient;
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "departmentService", fallbackMethod = "getEmployeeFallback")
    @Retry(name = "departmentService")
    public ResponseEntity<EmployeeWithDepartment> getEmployee(@PathVariable Long id) {
        MicroserviceEmployee employee = employeeService.findById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Call Department Service to get department details
        MicroserviceDepartment department =
                departmentClient.getDepartment(employee.departmentId());
        
        EmployeeWithDepartment result = new EmployeeWithDepartment(employee, department);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/{id}/async")
    public CompletableFuture<ResponseEntity<EmployeeWithDepartment>> getEmployeeAsync(@PathVariable Long id) {
        return CompletableFuture.supplyAsync(() -> {
            MicroserviceEmployee employee = employeeService.findById(id);
            if (employee == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Simulate async call to Department Service
            MicroserviceDepartment department =
                    departmentClient.getDepartment(employee.departmentId());
            
            EmployeeWithDepartment result = new EmployeeWithDepartment(employee, department);
            return ResponseEntity.ok(result);
        });
    }
    
    @SuppressWarnings("unused")
    public ResponseEntity<EmployeeWithDepartment> getEmployeeFallback(Long id, Exception e) {
        System.out.println("🔄 Circuit breaker activated for employee " + id + ": " + e.getMessage());
        
        MicroserviceEmployee employee = employeeService.findById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Return employee with a default department when department service is down
        MicroserviceDepartment defaultDepartment = new MicroserviceDepartment(
            employee.departmentId(), 
            "Department Unavailable", 
            "Department service is currently unavailable"
        );
        
        EmployeeWithDepartment result = new EmployeeWithDepartment(employee, defaultDepartment);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping
    public ResponseEntity<MicroserviceEmployee> createEmployee(@RequestBody MicroserviceEmployee employee) {
        MicroserviceEmployee saved = employeeService.save(employee);
        
        // Publish event for other services
        employeeService.publishEmployeeCreatedEvent(saved);
        
        return ResponseEntity.ok(saved);
    }
}

/**
 * Department Service - Manages department data
 * Demonstrates service boundaries and data ownership
 * <p>
 * NOTE: This controller is part of the simulated microservice environment.
 * In a real application, this would be a separate Spring Boot application.
 */
@RestController
@RequestMapping("/api/departments")
class DepartmentController {
    
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MicroserviceDepartment> getDepartment(@PathVariable Long id) {
        MicroserviceDepartment department = departmentService.findById(id);
        return department != null ? ResponseEntity.ok(department) : ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public List<MicroserviceDepartment> getAllDepartments() {
        return departmentService.findAll();
    }
}

/**
 * Service classes demonstrating microservice patterns
 */
class EmployeeServiceDemo {
    public void demonstrateService() {
        System.out.println("""
            
            --- Employee Service ---
            Service Name: employee-service
            Port: 8081
            Eureka Registration: ✓
            Endpoints:
              GET /api/employees
              GET /api/employees/{id}
              POST /api/employees
            Database: Employee data only
            External calls: Department Service (for employee details)
            """);
    }
}

class DepartmentServiceDemo {
    public void demonstrateService() {
        System.out.println("""
            
            --- Department Service ---
            Service Name: department-service
            Port: 8082
            Eureka Registration: ✓
            Endpoints:
              GET /api/departments
              GET /api/departments/{id}
            Database: Department data only
            External calls: None (leaf service)
            """);
    }
}

class PayrollServiceDemo {
    public void demonstrateService() {
        System.out.println("""
            
            --- Payroll Service ---
            Service Name: payroll-service
            Port: 8083
            Eureka Registration: ✓
            Endpoints:
              POST /api/payroll/calculate
              GET /api/payroll/employee/{id}
            Database: Payroll calculations only
            External calls: Employee Service + Department Service
            Circuit Breaker: ✓ (handles service failures)
            """);
    }
}

/**
 * Service layer with circuit breaker and retry patterns
 */
@Service
class MicroserviceEmployeeService {
    
    private final Map<Long, MicroserviceEmployee> employees = new HashMap<>();
    
    public MicroserviceEmployeeService() {
        // Initialize with sample data
        employees.put(1L, new MicroserviceEmployee(1L, "Alice Johnson",
                "alice@company.com", 1L, 85000.0));
        employees.put(2L, new MicroserviceEmployee(2L, "Bob Smith",
                "bob@company.com", 2L, 75000.0));
    }
    
    public MicroserviceEmployee findById(Long id) {
        return switch (employees.get(id)) {
            case null -> null;
            case MicroserviceEmployee employee -> employee;
        };
    }
    
    public MicroserviceEmployee save(MicroserviceEmployee employee) {
        if (employee.id() == null) {
            employee = new MicroserviceEmployee((long) (employees.size() + 1), 
                employee.name(), employee.email(), employee.departmentId(), employee.salary());
        }
        employees.put(employee.id(), employee);
        return employee;
    }
    
    public void publishEmployeeCreatedEvent(MicroserviceEmployee employee) {
        System.out.println("📢 Publishing EmployeeCreatedEvent: " + employee.name());
        // In real implementation, this would use message queue or event bus
    }
}

@Service
class DepartmentService {
    
    private final Map<Long, MicroserviceDepartment> departments = new HashMap<>();
    
    public DepartmentService() {
        departments.put(1L, new MicroserviceDepartment(1L, "Engineering",
                "Software Development"));
        departments.put(2L, new MicroserviceDepartment(2L, "Marketing",
                "Product Marketing"));
    }
    
    public MicroserviceDepartment findById(Long id) {
        return departments.get(id);
    }
    
    public List<MicroserviceDepartment> findAll() {
        return new ArrayList<>(departments.values());
    }
}

/**
 * API Gateway simulation
 */
class ApiGateway {
    
    public void routeToEmployeeService(String path) {
        System.out.println("🌐 API Gateway routing to employee-service: " + path);
    }
    
    public void routeToDepartmentService(String path) {
        System.out.println("🌐 API Gateway routing to department-service: " + path);
    }
    
    public void handleAuthentication() {
        System.out.println("🔐 API Gateway handling authentication");
    }
    
    public void handleRateLimit() {
        System.out.println("⚡ API Gateway enforcing rate limits");
    }
}

/**
 * Configuration management
 */
@Configuration
class MicroserviceConfiguration {
    
    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
    
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public DepartmentHttpExchangeClient departmentHttpExchangeClient() {
        var webClient = WebClient.builder()
            .baseUrl("http://localhost:8082")
            .build();
        var adapter = WebClientAdapter.create(webClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(DepartmentHttpExchangeClient.class);
    }
}

/**
 * Configuration properties from Config Server
 */
@RefreshScope
class ConfigurableService {
    
    @Value("${service.timeout:5000}")
    private int timeout;
    
    @Value("${service.retries:3}")
    private int retries;
    
    @Value("${feature.enabled:true}")
    private boolean featureEnabled;
    
    public void showConfiguration() {
        System.out.println("⚙️ Service Configuration:");
        System.out.println("  Timeout: " + timeout + "ms");
        System.out.println("  Retries: " + retries);
        System.out.println("  Feature Enabled: " + featureEnabled);
    }
}

/**
 * Event-driven communication
 */
class EmployeeEventPublisher {
    
    public void publishEmployeeCreated(MicroserviceEmployee employee) {
        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
            employee.id(),
            employee.name(),
            employee.departmentId(),
            LocalDateTime.now()
        );
        
        System.out.println("📤 Publishing event: " + event);
        // In real implementation: messageQueue.send(event)
    }
}

class EmployeeEventListener {
    
    public void handleEmployeeCreated(EmployeeCreatedEvent event) {
        System.out.println("📥 Received EmployeeCreatedEvent: " + event.employeeName());
        
        // Update local cache, send notifications, etc.
        updatePayrollService(event.employeeId());
        sendWelcomeEmail(event.employeeName());
    }
    
    private void updatePayrollService(Long employeeId) {
        System.out.println("💰 Updating payroll service for employee " + employeeId);
    }
    
    private void sendWelcomeEmail(String employeeName) {
        System.out.println("📧 Sending welcome email to " + employeeName);
    }
}

/**
 * Health check and monitoring
 */
class ServiceHealthIndicator {
    
    public boolean isHealthy() {
        // Check database connection, external services, etc.
        return true;
    }
    
    public Map<String, Object> getHealthDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("status", "UP");
        details.put("database", "connected");
        details.put("external-services", "available");
        details.put("memory-usage", "75%");
        return details;
    }
}

// Data Transfer Objects and Entities
record MicroserviceEmployee(
    Long id,
    String name,
    String email,
    Long departmentId,
    Double salary
) {}

record MicroserviceDepartment(
    Long id,
    String name,
    String description
) {}

record EmployeeWithDepartment(
    MicroserviceEmployee employee,
    MicroserviceDepartment department
) {}

record EmployeeCreatedEvent(
        Long employeeId,
        String employeeName,
        Long departmentId,
        LocalDateTime timestamp
) {
}

