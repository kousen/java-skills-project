package com.oreilly.microservices;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;

import static com.oreilly.microservices.MicroserviceRecords.*;

@Component
public class MicroservicesDemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
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
        
        // Demonstrate configuration management
        demonstrateConfigurationManagement();
        
        // Demonstrate event-driven communication
        demonstrateEventDrivenCommunication();
        
        // Demonstrate health monitoring
        demonstrateHealthMonitoring();

        System.out.println("\n=== Microservices demonstration complete ===");
    }

    private void demonstrateServiceCommunication() {
        System.out.println("""
            
            --- Inter-Service Communication ---
            1. Employee Service calls Department Service
            2. Payroll Service calls both Employee and Department Services
            3. Circuit breaker handles service failures
            4. Load balancer distributes requests
            """);
        
        // Demonstrate fetching all departments
        System.out.println("""
            
            --- Department Service Operations ---
            Simulating getAllDepartments() call via HTTP Exchange Interface:
              GET /api/departments -> Returns list of all departments
              Response: [Engineering, Marketing, HR, Finance]
              This demonstrates bulk data retrieval across services
            """);
        
        // Demonstrate Circuit Breaker Pattern
        System.out.println("\n--- Circuit Breaker Demonstration ---");
        demonstrateCircuitBreaker();
        
        // Demonstrate API Gateway functionality
        System.out.println("\n--- API Gateway Demonstration ---");
        ApiGateway gateway = new ApiGateway();
        gateway.handleAuthentication();
        gateway.routeToEmployeeService("/api/employees/1");
        gateway.routeToDepartmentService("/api/departments/1");
        gateway.routeToDepartmentService("/api/departments");  // Show getAllDepartments routing
        gateway.handleRateLimit();
    }
    
    private void demonstrateCircuitBreaker() {
        System.out.println("""
            Simulating circuit breaker pattern:
              Example: Service calls with circuit breaker protection
              - Normal: Circuit stays CLOSED when services are healthy
              - Failure: Circuit OPENS after threshold, returns fallback
              - Recovery: Circuit goes HALF-OPEN to test, then CLOSED
            
            🎮 INTERACTIVE DEMO AVAILABLE!
            Try the circuit breaker yourself with these endpoints:
            
              1. Check status:        GET  http://localhost:8081/api/demo/circuit-breaker/status
              2. Toggle failures:     POST http://localhost:8081/api/demo/circuit-breaker/toggle-failure
              3. Call service:        GET  http://localhost:8081/api/demo/circuit-breaker/call-service
              4. Reset demo:          POST http://localhost:8081/api/demo/circuit-breaker/reset
            
              Try this sequence:
              - Toggle failure mode ON
              - Call service 3 times to trigger circuit breaker
              - See fallback response when circuit opens
              - Toggle failure mode OFF and call again to see recovery
            """);
    }
    
    private void demonstrateConfigurationManagement() {
        System.out.println("\n--- Configuration Management ---");
        ConfigurableService configService = new ConfigurableService();
        configService.showConfiguration();
    }
    
    private void demonstrateEventDrivenCommunication() {
        System.out.println("\n--- Event-Driven Communication ---");
        
        // Create event publisher and listener
        EmployeeEventPublisher eventPublisher = new EmployeeEventPublisher();
        EmployeeEventListener eventListener = new EmployeeEventListener();
        
        // Create a sample employee and publish event
        MicroserviceEmployee sampleEmployee = new MicroserviceEmployee(
            1L, "Alice Johnson", "alice@company.com", 1L, 85000.0);
        
        // Publish employee created event
        eventPublisher.publishEmployeeCreated(sampleEmployee);
        
        // Simulate event listener receiving the event
        EmployeeCreatedEvent event = new EmployeeCreatedEvent(
            1L, "Alice Johnson", 1L, LocalDateTime.now());
        eventListener.handleEmployeeCreated(event);
    }
    
    private void demonstrateHealthMonitoring() {
        System.out.println("\n--- Health Monitoring ---");
        ServiceHealthIndicator healthIndicator = new ServiceHealthIndicator();
        
        boolean isHealthy = healthIndicator.isHealthy();
        System.out.println("Service Health Status: " + (isHealthy ? "✅ HEALTHY" : "❌ UNHEALTHY"));
        
        Map<String, Object> healthDetails = healthIndicator.getHealthDetails();
        System.out.println("Health Details:");
        healthDetails.forEach((key, value) -> 
            System.out.println("  " + key + ": " + value));
    }
}
