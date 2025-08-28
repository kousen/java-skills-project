package com.oreilly.reactive;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Reactive Programming with Project Reactor - Topic 26
 * <p>
 * This Spring Boot application demonstrates:
 * - Reactive programming patterns with Project Reactor
 * - WebFlux reactive web framework  
 * - Mono and Flux operations with practical examples
 * - Employee Management System using reactive streams
 * - Testing reactive streams with StepVerifier
 */
@SpringBootApplication
public class ReactiveApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }
    
    @Bean
    CommandLineRunner startupMessage() {
        return args -> System.out.println("""
            
            ========================================
            🚀 REACTIVE PROGRAMMING DEMO STARTED!
            ========================================
            
            Interactive endpoints available:
            
            📊 Demo Endpoints:
            GET  /api/demo/reactive/mono         - Demonstrate Mono operations
            GET  /api/demo/reactive/flux         - Demonstrate Flux operations
            GET  /api/demo/reactive/transform    - Show transformations
            GET  /api/demo/reactive/combine      - Combine multiple streams
            GET  /api/demo/reactive/error        - Error handling strategies
            GET  /api/demo/reactive/backpressure - Backpressure simulation
            GET  /api/demo/reactive/status       - Current demo status
            POST /api/demo/reactive/toggle-error - Toggle error simulation
            POST /api/demo/reactive/toggle-slowness - Toggle slow processing
            POST /api/demo/reactive/reset        - Reset demo state
            GET  /api/demo/reactive/events       - Server-Sent Events stream
            
            👤 Employee API:
            GET  /api/employees/{id}             - Get single employee (Mono)
            GET  /api/employees                  - Get all employees (Flux)
            POST /api/employees                  - Create employee
            PUT  /api/employees/{id}             - Update employee
            DELETE /api/employees/{id}           - Delete employee
            GET  /api/employees/stream           - Server-Sent Events stream
            GET  /api/employees/events           - Employee event stream
            GET  /api/employees/search           - Search with filters
            GET  /api/employees/{id}/profile     - Complete profile
            GET  /api/employees/delayed          - Delayed response demo
            GET  /api/employees/infinite         - Infinite stream demo
            
            📈 Department Operations:
            GET  /api/employees/departments/{dept}/statistics - Department stats
            POST /api/employees/departments/{dept}/raise      - Give department raise
            
            Server running on port 8080
            ========================================
            """);
    }
}