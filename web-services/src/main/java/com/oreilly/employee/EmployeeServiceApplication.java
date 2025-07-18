package com.oreilly.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Employee Service.
 * Demonstrates Spring Boot auto-configuration and embedded server.
 */
@SpringBootApplication
public class EmployeeServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
        System.out.println("Employee Service started on http://localhost:8080");
        System.out.println("Try: GET http://localhost:8080/api/employees");
    }
}