package com.oreilly.webservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main Spring Boot application for web services demonstrations.
 * This module contains examples for:
 * - Topic 20: REST API Consumer (EmployeeApiClient)
 * - Topic 21: REST Service Creator (REST controllers)
 * - Topic 25: Microservices Architecture (MicroservicesDemo)
 * - Topic 26: Reactive Programming (ReactiveDemo)
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WebServicesApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WebServicesApplication.class, args);
    }
}