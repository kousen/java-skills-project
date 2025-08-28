import java.util.*;
import java.time.LocalDateTime;

/**
 * Microservices Architecture Concepts Demonstration
 * Shows microservice patterns and principles without Spring Boot dependencies.
 * Demonstrates the concepts that frameworks like Spring Boot implement.
 */
public class MicroservicesConcepts {
    
    public static void main(String[] args) {
        System.out.println("=== Microservices Architecture Concepts ===");
        
        // Simulate microservice architecture
        MicroserviceRegistry registry = new MicroserviceRegistry();
        LoadBalancer loadBalancer = new LoadBalancer();
        CircuitBreakerDemo circuitBreaker = new CircuitBreakerDemo();
        
        // Register services
        registry.registerService("employee-service", Arrays.asList("localhost:8081", "localhost:8082"));
        registry.registerService("department-service", List.of("localhost:8083"));
        registry.registerService("payroll-service", Arrays.asList("localhost:8084", "localhost:8085"));
        
        // Demonstrate microservice patterns
        demonstrateServiceDiscovery(registry);
        demonstrateLoadBalancing(loadBalancer, registry);
        demonstrateCircuitBreaker(circuitBreaker);
        demonstrateAPIGateway();
        demonstrateEventDrivenCommunication();
        demonstrateConfigurationManagement();
        
        System.out.println("\n=== Microservices concepts demonstration complete ===");
    }
    
    /**
     * Service Discovery - How services find each other
     */
    private static void demonstrateServiceDiscovery(MicroserviceRegistry registry) {
        System.out.println("\n--- Service Discovery ---");
        System.out.println("Services register themselves with a service registry (like Eureka)");
        
        // Services register on startup
        registry.registerService("employee-service", List.of("192.168.1.10:8081"));
        System.out.println("✓ employee-service registered at 192.168.1.10:8081");
        
        // Other services discover by name
        List<String> employeeServiceInstances = registry.discoverService("employee-service");
        System.out.println("✓ Discovered employee-service instances: " + employeeServiceInstances);
        
        // Health checking
        registry.checkHealth("employee-service");
        System.out.println("✓ Health check passed for employee-service");
        
        System.out.println("""
            
            Benefits:
            - No hardcoded service URLs
            - Automatic service discovery
            - Dynamic scaling support
            """);
    }
    
    /**
     * Load Balancing - Distributing requests across instances
     */
    private static void demonstrateLoadBalancing(LoadBalancer loadBalancer, MicroserviceRegistry registry) {
        System.out.println("\n--- Load Balancing ---");
        
        List<String> instances = registry.discoverService("employee-service");
        
        // Round-robin load balancing
        for (int i = 0; i < 5; i++) {
            String selectedInstance = loadBalancer.selectInstance(instances);
            System.out.println("✓ Request " + (i + 1) + " routed to: " + selectedInstance);
        }
        
        System.out.println("""
            
            Load Balancing Strategies:
            - Round Robin: Equal distribution
            - Weighted: Based on capacity
            - Least Connections: Route to least busy
            - Health-based: Avoid unhealthy instances
            """);
    }
    
    /**
     * Circuit Breaker - Preventing cascading failures
     */
    private static void demonstrateCircuitBreaker(CircuitBreakerDemo circuitBreaker) {
        System.out.println("\n--- Circuit Breaker Pattern ---");
        
        // Simulate service calls
        for (int i = 1; i <= 10; i++) {
            boolean success = circuitBreaker.callExternalService("employee-service");
            System.out.println("Call " + i + ": " + (success ? "✓ Success" : "✗ Failed/Circuit Open"));
        }
        
        System.out.println("""
            
            Circuit Breaker States:
            - CLOSED: Normal operation, requests pass through
            - OPEN: Service failing, requests rejected immediately
            - HALF_OPEN: Testing if service recovered
            """);
    }
    
    /**
     * API Gateway - Single entry point
     */
    private static void demonstrateAPIGateway() {
        System.out.println("\n--- API Gateway Pattern ---");
        
        APIGateway gateway = new APIGateway();
        
        // Route different requests
        gateway.routeRequest("GET", "/api/employees/123");
        gateway.routeRequest("POST", "/api/departments");
        gateway.routeRequest("GET", "/api/payroll/employee/123");
        
        System.out.println("""
            
            API Gateway Responsibilities:
            - Request routing to appropriate services
            - Authentication and authorization
            - Rate limiting and throttling
            - Request/response transformation
            - Monitoring and analytics
            """);
    }
    
    /**
     * Event-Driven Communication - Loose coupling through events
     */
    private static void demonstrateEventDrivenCommunication() {
        System.out.println("\n--- Event-Driven Communication ---");
        
        EventBus eventBus = new EventBus();
        
        // Services subscribe to events they care about
        eventBus.subscribe("EmployeeCreated", "payroll-service");
        eventBus.subscribe("EmployeeCreated", "notification-service");
        eventBus.subscribe("DepartmentUpdated", "employee-service");
        
        // Publish events when things happen
        eventBus.publish("EmployeeCreated", Map.of(
            "employeeId", 123,
            "name", "Alice Johnson",
            "department", "Engineering"
        ));
        
        eventBus.publish("DepartmentUpdated", Map.of(
            "departmentId", 1,
            "name", "Engineering",
            "budget", 1000000
        ));
        
        System.out.println("""
            
            Event-Driven Benefits:
            - Loose coupling between services
            - Easy to add new functionality
            - Asynchronous processing
            - Scalable architecture
            """);
    }
    
    /**
     * Configuration Management - Centralized config
     */
    private static void demonstrateConfigurationManagement() {
        System.out.println("\n--- Configuration Management ---");
        
        ConfigurationServer configServer = new ConfigurationServer();
        
        // Services fetch configuration on startup
        Map<String, Object> employeeConfig = configServer.getConfiguration("employee-service", "production");
        Map<String, Object> payrollConfig = configServer.getConfiguration("payroll-service", "production");
        
        System.out.println("✓ employee-service config: " + employeeConfig);
        System.out.println("✓ payroll-service config: " + payrollConfig);
        
        // Configuration can be updated without redeploying
        configServer.updateConfiguration("employee-service", "database.pool.size", 20);
        System.out.println("✓ Updated employee-service database pool size");
        
        System.out.println("""
            
            Configuration Benefits:
            - Environment-specific settings
            - Runtime configuration updates
            - Secret management
            - Audit trail for changes
            """);
    }
}

/**
 * Service Registry - Keeps track of service instances
 */
class MicroserviceRegistry {
    private final Map<String, List<String>> services = new HashMap<>();
    private final Map<String, Boolean> healthStatus = new HashMap<>();
    
    public void registerService(String serviceName, List<String> instances) {
        services.put(serviceName, new ArrayList<>(instances));
        healthStatus.put(serviceName, true);
        System.out.println("📋 Registered " + serviceName + " with " + instances.size() + " instances");
    }
    
    public List<String> discoverService(String serviceName) {
        return services.getOrDefault(serviceName, Collections.emptyList());
    }
    
    public void checkHealth(String serviceName) {
        boolean healthy = Math.random() > 0.1; // 90% healthy
        healthStatus.put(serviceName, healthy);
        
        if (!healthy) {
            System.out.println("⚠️ " + serviceName + " failed health check");
            // Remove unhealthy instances
        }
    }
    
    public boolean isHealthy(String serviceName) {
        return healthStatus.getOrDefault(serviceName, false);
    }
}

/**
 * Load Balancer - Distributes requests across instances
 */
class LoadBalancer {
    private int roundRobinIndex = 0;
    
    public String selectInstance(List<String> instances) {
        if (instances.isEmpty()) {
            throw new RuntimeException("No available instances");
        }
        
        // Round-robin selection
        String selected = instances.get(roundRobinIndex % instances.size());
        roundRobinIndex++;
        return selected;
    }
    
    public String selectInstanceWeighted(Map<String, Integer> instanceWeights) {
        // Weighted selection based on capacity
        int totalWeight = instanceWeights.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = new Random().nextInt(totalWeight);
        
        int currentWeight = 0;
        for (Map.Entry<String, Integer> entry : instanceWeights.entrySet()) {
            currentWeight += entry.getValue();
            if (randomValue < currentWeight) {
                return entry.getKey();
            }
        }
        
        return instanceWeights.keySet().iterator().next();
    }
}

/**
 * Circuit Breaker - Prevents cascading failures
 */
class CircuitBreakerDemo {
    private enum State { CLOSED, OPEN, HALF_OPEN }
    
    private State state = State.CLOSED;
    private int failureCount = 0;
    private int successCount = 0;
    private long lastFailureTime = 0;

    public boolean callExternalService(String serviceName) {
        if (state == State.OPEN) {
            // 5 seconds
            long timeout = 5000;
            if (System.currentTimeMillis() - lastFailureTime > timeout) {
                state = State.HALF_OPEN;
                System.out.println("🔄 Circuit breaker moving to HALF_OPEN state");
            } else {
                System.out.println("🔴 Circuit breaker OPEN - rejecting call to " + serviceName);
                return false;
            }
        }
        
        // Simulate service call (70% success rate)
        boolean success = Math.random() > 0.3;
        
        if (success) {
            onSuccess();
        } else {
            onFailure();
        }
        
        return success;
    }
    
    private void onSuccess() {
        successCount++;

        int successThreshold = 2;
        if (state == State.HALF_OPEN && successCount >= successThreshold) {
            state = State.CLOSED;
            failureCount = 0;
            successCount = 0;
            System.out.println("🟢 Circuit breaker CLOSED - service recovered");
        }
    }
    
    private void onFailure() {
        failureCount++;
        successCount = 0;
        lastFailureTime = System.currentTimeMillis();

        int failureThreshold = 3;
        if (failureCount >= failureThreshold) {
            state = State.OPEN;
            System.out.println("🔴 Circuit breaker OPEN - too many failures");
        }
    }
}

/**
 * API Gateway - Single entry point for all requests
 */
class APIGateway {
    private final MicroserviceRegistry registry = new MicroserviceRegistry();
    private final LoadBalancer loadBalancer = new LoadBalancer();
    
    public void routeRequest(String method, String path) {
        // Route based on path
        String targetService = determineTargetService(path);
        
        // Apply cross-cutting concerns
        if (!authenticate()) {
            System.out.println("🔒 Authentication failed for " + method + " " + path);
            return;
        }
        
        if (!checkRateLimit()) {
            System.out.println("⚡ Rate limit exceeded for " + method + " " + path);
            return;
        }
        
        // Route to service
        List<String> instances = registry.discoverService(targetService);
        if (!instances.isEmpty()) {
            String selectedInstance = loadBalancer.selectInstance(instances);
            System.out.println("🌐 Routing " + method + " " + path + " to " + targetService + " at " + selectedInstance);
        } else {
            System.out.println("❌ No available instances for " + targetService);
        }
    }
    
    private String determineTargetService(String path) {
        if (path.startsWith("/api/employees")) return "employee-service";
        if (path.startsWith("/api/departments")) return "department-service";
        if (path.startsWith("/api/payroll")) return "payroll-service";
        return "unknown-service";
    }
    
    private boolean authenticate() {
        // Simulate authentication check
        return Math.random() > 0.1; // 90% success
    }
    
    private boolean checkRateLimit() {
        // Simulate rate limiting
        return Math.random() > 0.05; // 95% pass rate
    }
}

/**
 * Event Bus - Enables event-driven communication
 */
class EventBus {
    private final Map<String, List<String>> subscriptions = new HashMap<>();
    
    public void subscribe(String eventType, String serviceName) {
        subscriptions.computeIfAbsent(eventType, k -> new ArrayList<>()).add(serviceName);
        System.out.println("📋 " + serviceName + " subscribed to " + eventType + " events");
    }
    
    public void publish(String eventType, Map<String, Object> eventData) {
        System.out.println("📤 Publishing " + eventType + " event: " + eventData);
        
        List<String> subscribers = subscriptions.getOrDefault(eventType, Collections.emptyList());
        for (String subscriber : subscribers) {
            deliverEvent(subscriber, eventType, eventData);
        }
    }
    
    private void deliverEvent(String serviceName, String eventType, Map<String, Object> eventData) {
        System.out.println("📥 Delivering " + eventType + " to " + serviceName);
        
        // In real implementation, this would:
        // - Send message to service via message queue
        // - Handle delivery failures
        // - Implement retry logic
        // - Track delivery status
    }
}

/**
 * Configuration Server - Centralized configuration management
 */
class ConfigurationServer {
    private final Map<String, Map<String, Object>> configurations = new HashMap<>();
    
    public ConfigurationServer() {
        // Initialize with sample configurations
        Map<String, Object> employeeServiceConfig = Map.of(
            "database.url", "jdbc:mysql://db:3306/employees",
            "database.pool.size", 10,
            "cache.ttl", 300,
            "feature.newSearch", true
        );
        
        Map<String, Object> payrollServiceConfig = Map.of(
            "database.url", "jdbc:mysql://db:3306/payroll",
            "external.tax.service.url", "https://tax-api.gov/v1",
            "batch.size", 100,
            "calculation.precision", 2
        );
        
        configurations.put("employee-service:production", employeeServiceConfig);
        configurations.put("payroll-service:production", payrollServiceConfig);
    }
    
    public Map<String, Object> getConfiguration(String serviceName, String environment) {
        String key = serviceName + ":" + environment;
        return configurations.getOrDefault(key, Collections.emptyMap());
    }
    
    public void updateConfiguration(String serviceName, String property, Object value) {
        String key = serviceName + ":production";
        Map<String, Object> config = configurations.get(key);
        if (config != null) {
            // In real implementation, configs would be mutable
            System.out.println("⚙️ Updated " + serviceName + " " + property + " = " + value);
            
            // Notify service of configuration change
            notifyConfigurationChange(serviceName, property, value);
        }
    }
    
    private void notifyConfigurationChange(String serviceName, String property, Object value) {
        System.out.println("📨 Notifying " + serviceName + " of configuration change: " + property);
        
        // In real implementation:
        // - Send webhook to service
        // - Use message queue for notification
        // - Service refreshes configuration
    }
}

/**
 * Service Health Monitoring
 */
class ServiceMonitor {
    public void checkServiceHealth(String serviceName, String instance) {
        // Simulate health check
        boolean healthy = Math.random() > 0.15; // 85% healthy
        
        Map<String, Object> healthStatus = Map.of(
            "status", healthy ? "UP" : "DOWN",
            "timestamp", LocalDateTime.now(),
            "details", Map.of(
                "database", "connected",
                "memory", "75% used",
                "disk", "60% used"
            )
        );
        
        System.out.println("🏥 Health check for " + serviceName + "@" + instance + ": " + 
                         (healthy ? "✅ HEALTHY" : "❌ UNHEALTHY"));
    }
    
    public void collectMetrics(String serviceName) {
        // Simulate metrics collection
        System.out.println("📊 Metrics for " + serviceName + ":");
        System.out.println("  - Requests/sec: " + (int)(Math.random() * 1000));
        System.out.println("  - Avg response time: " + (int)(Math.random() * 200) + "ms");
        System.out.println("  - Error rate: " + String.format("%.1f", Math.random() * 5) + "%");
        System.out.println("  - CPU usage: " + (int)(Math.random() * 80 + 20) + "%");
    }
}