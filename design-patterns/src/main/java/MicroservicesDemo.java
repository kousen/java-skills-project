import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Value;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.*;
import java.time.LocalDateTime;

/**
 * Microservices Architecture Demonstration
 * Shows Spring Boot microservice patterns including:
 * - Service discovery with Eureka
 * - Load balancing
 * - Circuit breaker pattern
 * - Inter-service communication
 * - Configuration management
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MicroservicesDemo {
    
    public static void main(String[] args) {
        // This would normally start a Spring Boot application
        // For demonstration, we'll show the microservice concepts
        
        System.out.println("=== Microservices Architecture Demonstration ===");
        
        // Simulate microservice startup
        EmployeeServiceDemo employeeService = new EmployeeServiceDemo();
        DepartmentServiceDemo departmentService = new DepartmentServiceDemo();
        PayrollServiceDemo payrollService = new PayrollServiceDemo();
        
        employeeService.demonstrateService();
        departmentService.demonstrateService();
        payrollService.demonstrateService();
        
        // Demonstrate inter-service communication
        demonstrateServiceCommunication();
        
        System.out.println("\n=== Microservices demonstration complete ===");
    }
    
    private static void demonstrateServiceCommunication() {
        System.out.println("\n--- Inter-Service Communication ---");
        System.out.println("1. Employee Service calls Department Service");
        System.out.println("2. Payroll Service calls both Employee and Department Services");
        System.out.println("3. Circuit breaker handles service failures");
        System.out.println("4. Load balancer distributes requests");
    }
}

/**
 * Employee Service - Manages employee data
 * Demonstrates service registration and basic CRUD operations
 */
@RestController
@RequestMapping("/api/employees")
@RefreshScope
class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private DepartmentServiceClient departmentClient;
    
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeWithDepartment> getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Call Department Service to get department details
        Department department = departmentClient.getDepartment(employee.getDepartmentId());
        
        EmployeeWithDepartment result = new EmployeeWithDepartment(employee, department);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee saved = employeeService.save(employee);
        
        // Publish event for other services
        employeeService.publishEmployeeCreatedEvent(saved);
        
        return ResponseEntity.ok(saved);
    }
}

/**
 * Department Service - Manages department data
 * Demonstrates service boundaries and data ownership
 */
@RestController
@RequestMapping("/api/departments")
class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable Long id) {
        Department department = departmentService.findById(id);
        return department != null ? ResponseEntity.ok(department) : ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.findAll();
    }
}

/**
 * Service classes demonstrating microservice patterns
 */
class EmployeeServiceDemo {
    public void demonstrateService() {
        System.out.println("\n--- Employee Service ---");
        System.out.println("Service Name: employee-service");
        System.out.println("Port: 8081");
        System.out.println("Eureka Registration: ✓");
        System.out.println("Endpoints:");
        System.out.println("  GET /api/employees/{id}");
        System.out.println("  POST /api/employees");
        System.out.println("  GET /api/employees");
        System.out.println("Database: Employee data only");
        System.out.println("External calls: Department Service (for employee details)");
    }
}

class DepartmentServiceDemo {
    public void demonstrateService() {
        System.out.println("\n--- Department Service ---");
        System.out.println("Service Name: department-service");
        System.out.println("Port: 8082");
        System.out.println("Eureka Registration: ✓");
        System.out.println("Endpoints:");
        System.out.println("  GET /api/departments/{id}");
        System.out.println("  GET /api/departments");
        System.out.println("Database: Department data only");
        System.out.println("External calls: None (leaf service)");
    }
}

class PayrollServiceDemo {
    public void demonstrateService() {
        System.out.println("\n--- Payroll Service ---");
        System.out.println("Service Name: payroll-service");
        System.out.println("Port: 8083");
        System.out.println("Eureka Registration: ✓");
        System.out.println("Endpoints:");
        System.out.println("  POST /api/payroll/calculate");
        System.out.println("  GET /api/payroll/employee/{id}");
        System.out.println("Database: Payroll calculations only");
        System.out.println("External calls: Employee Service + Department Service");
        System.out.println("Circuit Breaker: ✓ (handles service failures)");
    }
}

/**
 * Service layer with circuit breaker and retry patterns
 */
class EmployeeService {
    
    private Map<Long, Employee> employees = new HashMap<>();
    
    public EmployeeService() {
        // Initialize with sample data
        employees.put(1L, new Employee(1L, "Alice Johnson", "alice@company.com", 1L, 85000.0));
        employees.put(2L, new Employee(2L, "Bob Smith", "bob@company.com", 2L, 75000.0));
    }
    
    public Employee findById(Long id) {
        return employees.get(id);
    }
    
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId((long) (employees.size() + 1));
        }
        employees.put(employee.getId(), employee);
        return employee;
    }
    
    public void publishEmployeeCreatedEvent(Employee employee) {
        System.out.println("📢 Publishing EmployeeCreatedEvent: " + employee.getName());
        // In real implementation, this would use message queue or event bus
    }
}

class DepartmentService {
    
    private Map<Long, Department> departments = new HashMap<>();
    
    public DepartmentService() {
        departments.put(1L, new Department(1L, "Engineering", "Software Development"));
        departments.put(2L, new Department(2L, "Marketing", "Product Marketing"));
    }
    
    public Department findById(Long id) {
        return departments.get(id);
    }
    
    public List<Department> findAll() {
        return new ArrayList<>(departments.values());
    }
}

/**
 * Inter-service communication with circuit breaker
 */
class DepartmentServiceClient {
    
    @LoadBalanced
    private RestTemplate restTemplate;
    
    @CircuitBreaker(name = "department-service", fallbackMethod = "getDepartmentFallback")
    @Retry(name = "department-service")
    public Department getDepartment(Long id) {
        String url = "http://department-service/api/departments/" + id;
        return restTemplate.getForObject(url, Department.class);
    }
    
    public Department getDepartmentFallback(Long id, Exception ex) {
        System.out.println("🔴 Circuit breaker activated for department " + id);
        return new Department(id, "Unknown Department", "Service Unavailable");
    }
}

/**
 * API Gateway simulation
 */
class ApiGateway {
    
    @LoadBalanced
    private WebClient.Builder webClientBuilder;
    
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
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
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
    
    public void publishEmployeeCreated(Employee employee) {
        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
            employee.getId(),
            employee.getName(),
            employee.getDepartmentId(),
            LocalDateTime.now()
        );
        
        System.out.println("📤 Publishing event: " + event);
        // In real implementation: messageQueue.send(event)
    }
}

class EmployeeEventListener {
    
    public void handleEmployeeCreated(EmployeeCreatedEvent event) {
        System.out.println("📥 Received EmployeeCreatedEvent: " + event.getEmployeeName());
        
        // Update local cache, send notifications, etc.
        updatePayrollService(event.getEmployeeId());
        sendWelcomeEmail(event.getEmployeeName());
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
class Employee {
    private Long id;
    private String name;
    private String email;
    private Long departmentId;
    private Double salary;
    
    public Employee() {}
    
    public Employee(Long id, String name, String email, Long departmentId, Double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.departmentId = departmentId;
        this.salary = salary;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
}

class Department {
    private Long id;
    private String name;
    private String description;
    
    public Department() {}
    
    public Department(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

class EmployeeWithDepartment {
    private Employee employee;
    private Department department;
    
    public EmployeeWithDepartment(Employee employee, Department department) {
        this.employee = employee;
        this.department = department;
    }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}

class EmployeeCreatedEvent {
    private Long employeeId;
    private String employeeName;
    private Long departmentId;
    private LocalDateTime timestamp;
    
    public EmployeeCreatedEvent(Long employeeId, String employeeName, Long departmentId, LocalDateTime timestamp) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.departmentId = departmentId;
        this.timestamp = timestamp;
    }
    
    public Long getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public Long getDepartmentId() { return departmentId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return "EmployeeCreatedEvent{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", departmentId=" + departmentId +
                ", timestamp=" + timestamp +
                '}';
    }
}