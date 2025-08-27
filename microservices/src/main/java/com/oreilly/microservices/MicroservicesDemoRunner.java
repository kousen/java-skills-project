package com.oreilly.microservices;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MicroservicesDemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
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

    private void demonstrateServiceCommunication() {
        System.out.println("\n--- Inter-Service Communication ---");
        System.out.println("1. Employee Service calls Department Service");
        System.out.println("2. Payroll Service calls both Employee and Department Services");
        System.out.println("3. Circuit breaker handles service failures");
        System.out.println("4. Load balancer distributes requests");
    }
}
