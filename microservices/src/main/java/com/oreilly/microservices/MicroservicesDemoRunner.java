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
        System.out.println("\n--- Inter-Service Communication ---");
        System.out.println("1. Employee Service calls Department Service");
        System.out.println("2. Payroll Service calls both Employee and Department Services");
        System.out.println("3. Circuit breaker handles service failures");
        System.out.println("4. Load balancer distributes requests");
        
        // Demonstrate API Gateway functionality
        System.out.println("\n--- API Gateway Demonstration ---");
        ApiGateway gateway = new ApiGateway();
        gateway.handleAuthentication();
        gateway.routeToEmployeeService("/api/employees/1");
        gateway.routeToDepartmentService("/api/departments/1");
        gateway.handleRateLimit();
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
