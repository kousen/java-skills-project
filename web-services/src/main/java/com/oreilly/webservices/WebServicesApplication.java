package com.oreilly.webservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * Main Spring Boot application for web services demonstrations.
 * This module focuses on:
 * - Topic 20: REST API Consumer (EmployeeApiClient)  
 * - Topic 21: REST Service Creator (Employee REST controllers, service, repository)
 * - Topic 26: Reactive Programming (ReactiveDemo)
 * <p>
 * Note: Topic 25 (Microservices Architecture) has been moved to its own module.
 */
@SpringBootApplication
@EnableTransactionManagement
public class WebServicesApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WebServicesApplication.class, args);
    }
}