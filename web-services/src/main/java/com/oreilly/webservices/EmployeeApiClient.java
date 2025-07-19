package com.oreilly.webservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * HTTP Client for consuming Employee REST APIs.
 * Demonstrates Java 11+ HttpClient usage with proper error handling,
 * JSON processing, and async operations.
 */
public class EmployeeApiClient {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeApiClient.class);
    
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public EmployeeApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.objectMapper = new ObjectMapper();
        
        logger.info("EmployeeApiClient initialized with base URL: {}", baseUrl);
    }
    
    /**
     * Demonstrates GET request to fetch a single employee.
     */
    public Optional<EmployeeDto> getEmployee(Long id) throws IOException, InterruptedException {
        logger.debug("Fetching employee with ID: {}", id);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/employees/" + id))
            .header("Accept", "application/json")
            .header("User-Agent", "EmployeeApiClient/1.0")
            .timeout(Duration.ofSeconds(30))
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        
        logger.debug("Response status: {}", response.statusCode());
        
        switch (response.statusCode()) {
            case 200:
                EmployeeDto employee = objectMapper.readValue(response.body(), EmployeeDto.class);
                logger.info("Successfully retrieved employee: {}", employee.getName());
                return Optional.of(employee);
            case 404:
                logger.warn("Employee not found with ID: {}", id);
                return Optional.empty();
            case 401:
                logger.error("Unauthorized access to employee API");
                throw new ApiException("Unauthorized", response.statusCode());
            default:
                logger.error("Unexpected response status: {}", response.statusCode());
                throw new ApiException("API call failed", response.statusCode());
        }
    }
    
    /**
     * Demonstrates GET request for multiple employees with error handling.
     */
    public List<EmployeeDto> getAllEmployees() throws IOException, InterruptedException {
        logger.debug("Fetching all employees");
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/employees"))
            .header("Accept", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            List<EmployeeDto> employees = objectMapper.readValue(response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, EmployeeDto.class));
            logger.info("Retrieved {} employees", employees.size());
            return employees;
        } else {
            logger.error("Failed to retrieve employees, status: {}", response.statusCode());
            return Collections.emptyList();
        }
    }
    
    /**
     * Demonstrates POST request to create a new employee.
     */
    public EmployeeDto createEmployee(EmployeeDto employee) throws IOException, InterruptedException {
        logger.debug("Creating new employee: {}", employee.getName());
        
        String jsonPayload = objectMapper.writeValueAsString(employee);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/employees"))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            EmployeeDto createdEmployee = objectMapper.readValue(response.body(), EmployeeDto.class);
            logger.info("Successfully created employee with ID: {}", createdEmployee.getId());
            return createdEmployee;
        } else {
            logger.error("Failed to create employee, status: {}", response.statusCode());
            throw new ApiException("Failed to create employee", response.statusCode());
        }
    }
    
    /**
     * Demonstrates query parameters and URL encoding.
     */
    public List<EmployeeDto> searchEmployees(String department, Double minSalary) 
            throws IOException, InterruptedException {
        logger.debug("Searching employees in department: {} with min salary: {}", department, minSalary);
        
        String encodedDepartment = URLEncoder.encode(department, StandardCharsets.UTF_8);
        String queryParams = String.format("?department=%s&minSalary=%.2f", 
            encodedDepartment, minSalary);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/employees/search" + queryParams))
            .header("Accept", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            List<EmployeeDto> employees = objectMapper.readValue(response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, EmployeeDto.class));
            logger.info("Found {} employees matching search criteria", employees.size());
            return employees;
        } else {
            logger.error("Search failed, status: {}", response.statusCode());
            return Collections.emptyList();
        }
    }
    
    /**
     * Demonstrates asynchronous HTTP requests using CompletableFuture.
     */
    public CompletableFuture<EmployeeDto> getEmployeeAsync(Long id) {
        logger.debug("Fetching employee asynchronously with ID: {}", id);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/employees/" + id))
            .header("Accept", "application/json")
            .GET()
            .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(response -> {
                logger.debug("Async response received, status: {}", response.statusCode());
                try {
                    if (response.statusCode() == 200) {
                        EmployeeDto employee = objectMapper.readValue(response.body(), EmployeeDto.class);
                        logger.info("Async fetch successful for employee: {}", employee.getName());
                        return employee;
                    } else {
                        throw new RuntimeException("API call failed with status: " + response.statusCode());
                    }
                } catch (IOException e) {
                    logger.error("Failed to parse response", e);
                    throw new RuntimeException("Failed to parse response", e);
                }
            })
            .exceptionally(throwable -> {
                logger.error("Async request failed", throwable);
                return null;
            });
    }
    
    /**
     * Demonstrates retry logic for handling transient failures.
     */
    public <T> T executeWithRetry(SupplierWithException<T> operation, int maxRetries) 
            throws IOException, InterruptedException {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return operation.get();
            } catch (IOException | InterruptedException e) {
                attempts++;
                if (attempts >= maxRetries) {
                    logger.error("Max retries ({}) exceeded", maxRetries);
                    throw e;
                }
                
                long backoffMs = 1000L * attempts; // Linear backoff
                logger.warn("Request failed (attempt {}/{}), retrying in {}ms", 
                    attempts, maxRetries, backoffMs);
                
                try {
                    Thread.sleep(backoffMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
        throw new IllegalStateException("Should not reach here");
    }
    
    /**
     * Functional interface for operations that can throw exceptions.
     */
    @FunctionalInterface
    public interface SupplierWithException<T> {
        T get() throws IOException, InterruptedException;
    }
    
    /**
     * Custom exception for API errors.
     */
    public static class ApiException extends RuntimeException {
        private final int statusCode;
        
        public ApiException(String message, int statusCode) {
            super(message + " (HTTP " + statusCode + ")");
            this.statusCode = statusCode;
        }
        
        public int getStatusCode() {
            return statusCode;
        }
    }
    
    /**
     * Simple Employee DTO for demonstration.
     */
    public static class EmployeeDto {
        private Long id;
        private String name;
        private String email;
        private String department;
        private Double salary;
        
        // Default constructor for Jackson
        public EmployeeDto() {}
        
        public EmployeeDto(Long id, String name, String email, String department, Double salary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.department = department;
            this.salary = salary;
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        
        public Double getSalary() { return salary; }
        public void setSalary(Double salary) { this.salary = salary; }
        
        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', department='" + department + "'}";
        }
    }
    
    /**
     * Main method demonstrating the API client usage.
     */
    public static void main(String[] args) {
        EmployeeApiClient client = new EmployeeApiClient("https://api.example.com");
        
        try {
            System.out.println("=== Employee API Client Demonstration ===");
            
            // Create a new employee
            EmployeeDto newEmployee = new EmployeeDto(null, "John Doe", "john@example.com", "Engineering", 75000.0);
            
            // Note: These calls would work if connecting to a real API
            // For demonstration, we'll show how the calls would be made
            
            System.out.println("1. Creating employee: " + newEmployee.getName());
            System.out.println("   POST " + client.baseUrl + "/employees");
            System.out.println("   Content-Type: application/json");
            System.out.println("   Body: " + client.objectMapper.writeValueAsString(newEmployee));
            
            System.out.println("\n2. Fetching employee by ID");
            System.out.println("   GET " + client.baseUrl + "/employees/1");
            System.out.println("   Accept: application/json");
            
            System.out.println("\n3. Searching employees by department");
            System.out.println("   GET " + client.baseUrl + "/employees/search?department=Engineering&minSalary=50000.00");
            
            System.out.println("\n4. Async employee fetch demonstrates non-blocking operations");
            CompletableFuture<EmployeeDto> futureEmployee = client.getEmployeeAsync(1L);
            System.out.println("   Async request initiated...");
            
            System.out.println("\n=== Key HTTP Client Features Demonstrated ===");
            System.out.println("- HTTP/2 support for better performance");
            System.out.println("- Proper timeout configuration");
            System.out.println("- JSON serialization/deserialization");
            System.out.println("- Error handling for different HTTP status codes");
            System.out.println("- Asynchronous requests with CompletableFuture");
            System.out.println("- URL encoding for query parameters");
            System.out.println("- Retry logic for handling transient failures");
            
        } catch (Exception e) {
            logger.error("Demonstration failed", e);
        }
    }
}