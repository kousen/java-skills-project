import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates REST service concepts and patterns without Spring Boot framework.
 * Shows HTTP methods, status codes, JSON processing, and resource management.
 * 
 * This is a conceptual demonstration - in practice you'd use Spring Boot,
 * but this shows the underlying principles that frameworks implement.
 */
public class RestServiceConcepts {
    private static final Logger logger = LoggerFactory.getLogger(RestServiceConcepts.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // In-memory storage simulating a database
    private static final Map<Long, EmployeeResource> employees = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);
    
    static {
        // Initialize with sample data
        employees.put(1L, new EmployeeResource(1L, "Alice Johnson", "alice@example.com", "Engineering", 85000.0));
        employees.put(2L, new EmployeeResource(2L, "Bob Smith", "bob@example.com", "Marketing", 65000.0));
        employees.put(3L, new EmployeeResource(3L, "Carol Davis", "carol@example.com", "Sales", 70000.0));
        idGenerator.set(4L);
    }
    
    /**
     * Demonstrates HTTP GET - Retrieve a single resource.
     * Maps to: GET /api/employees/{id}
     */
    public static HttpResponse<EmployeeResource> getEmployee(Long id) {
        logger.info("GET /api/employees/{}", id);
        
        EmployeeResource employee = employees.get(id);
        if (employee != null) {
            logger.info("Found employee: {}", employee.getName());
            return new HttpResponse<>(200, "OK", employee);
        } else {
            logger.warn("Employee not found with ID: {}", id);
            return new HttpResponse<>(404, "Employee not found", null);
        }
    }
    
    /**
     * Demonstrates HTTP GET - Retrieve multiple resources.
     * Maps to: GET /api/employees
     */
    public static HttpResponse<List<EmployeeResource>> getAllEmployees() {
        logger.info("GET /api/employees");
        
        List<EmployeeResource> allEmployees = new ArrayList<>(employees.values());
        logger.info("Retrieved {} employees", allEmployees.size());
        
        return new HttpResponse<>(200, "OK", allEmployees);
    }
    
    /**
     * Demonstrates HTTP POST - Create a new resource.
     * Maps to: POST /api/employees
     */
    public static HttpResponse<EmployeeResource> createEmployee(EmployeeResource newEmployee) {
        logger.info("POST /api/employees - Creating: {}", newEmployee.getName());
        
        // Validation
        if (newEmployee.getName() == null || newEmployee.getName().trim().isEmpty()) {
            return new HttpResponse<>(400, "Name is required", null);
        }
        if (newEmployee.getEmail() == null || !newEmployee.getEmail().contains("@")) {
            return new HttpResponse<>(400, "Valid email is required", null);
        }
        
        // Generate ID and save
        Long newId = idGenerator.getAndIncrement();
        newEmployee.setId(newId);
        employees.put(newId, newEmployee);
        
        logger.info("Created employee with ID: {}", newId);
        return new HttpResponse<>(201, "Created", newEmployee);
    }
    
    /**
     * Demonstrates HTTP PUT - Update an existing resource.
     * Maps to: PUT /api/employees/{id}
     */
    public static HttpResponse<EmployeeResource> updateEmployee(Long id, EmployeeResource updatedEmployee) {
        logger.info("PUT /api/employees/{} - Updating: {}", id, updatedEmployee.getName());
        
        if (!employees.containsKey(id)) {
            logger.warn("Employee not found for update: {}", id);
            return new HttpResponse<>(404, "Employee not found", null);
        }
        
        // Validation
        if (updatedEmployee.getName() == null || updatedEmployee.getName().trim().isEmpty()) {
            return new HttpResponse<>(400, "Name is required", null);
        }
        
        updatedEmployee.setId(id);
        employees.put(id, updatedEmployee);
        
        logger.info("Updated employee: {}", updatedEmployee.getName());
        return new HttpResponse<>(200, "OK", updatedEmployee);
    }
    
    /**
     * Demonstrates HTTP DELETE - Remove a resource.
     * Maps to: DELETE /api/employees/{id}
     */
    public static HttpResponse<Void> deleteEmployee(Long id) {
        logger.info("DELETE /api/employees/{}", id);
        
        if (employees.containsKey(id)) {
            EmployeeResource removed = employees.remove(id);
            logger.info("Deleted employee: {}", removed.getName());
            return new HttpResponse<>(204, "No Content", null);
        } else {
            logger.warn("Employee not found for deletion: {}", id);
            return new HttpResponse<>(404, "Employee not found", null);
        }
    }
    
    /**
     * Demonstrates query parameters and filtering.
     * Maps to: GET /api/employees/search?department=Engineering&minSalary=50000
     */
    public static HttpResponse<List<EmployeeResource>> searchEmployees(String department, Double minSalary) {
        logger.info("GET /api/employees/search?department={}&minSalary={}", department, minSalary);
        
        List<EmployeeResource> results = employees.values().stream()
            .filter(emp -> department == null || department.equals(emp.getDepartment()))
            .filter(emp -> minSalary == null || emp.getSalary() >= minSalary)
            .toList();
        
        logger.info("Search returned {} employees", results.size());
        return new HttpResponse<>(200, "OK", results);
    }
    
    /**
     * Demonstrates JSON serialization/deserialization.
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.error("Failed to serialize to JSON", e);
            return "{}";
        }
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("Failed to deserialize from JSON", e);
            return null;
        }
    }
    
    /**
     * Employee resource representation for REST API.
     */
    public static class EmployeeResource {
        private Long id;
        private String name;
        private String email;
        private String department;
        private Double salary;
        
        // Default constructor for Jackson
        public EmployeeResource() {}
        
        public EmployeeResource(Long id, String name, String email, String department, Double salary) {
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
     * HTTP response wrapper demonstrating status codes and data.
     */
    public static class HttpResponse<T> {
        private final int statusCode;
        private final String statusMessage;
        private final T body;
        
        public HttpResponse(int statusCode, String statusMessage, T body) {
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
            this.body = body;
        }
        
        public int getStatusCode() { return statusCode; }
        public String getStatusMessage() { return statusMessage; }
        public T getBody() { return body; }
        
        public boolean isSuccessful() {
            return statusCode >= 200 && statusCode < 300;
        }
        
        @Override
        public String toString() {
            return String.format("HTTP %d %s", statusCode, statusMessage);
        }
    }
    
    /**
     * Main method demonstrating REST service concepts.
     */
    public static void main(String[] args) {
        System.out.println("=== REST Service Concepts Demonstration ===");
        
        // 1. GET - Retrieve existing employee
        System.out.println("\n1. GET /api/employees/1");
        HttpResponse<EmployeeResource> getResponse = getEmployee(1L);
        System.out.println("   Response: " + getResponse);
        if (getResponse.isSuccessful()) {
            System.out.println("   JSON: " + toJson(getResponse.getBody()));
        }
        
        // 2. GET - Retrieve all employees
        System.out.println("\n2. GET /api/employees");
        HttpResponse<List<EmployeeResource>> getAllResponse = getAllEmployees();
        System.out.println("   Response: " + getAllResponse);
        System.out.println("   Count: " + getAllResponse.getBody().size());
        
        // 3. POST - Create new employee
        System.out.println("\n3. POST /api/employees");
        EmployeeResource newEmployee = new EmployeeResource(null, "Dave Wilson", "dave@example.com", "IT", 75000.0);
        System.out.println("   Request JSON: " + toJson(newEmployee));
        HttpResponse<EmployeeResource> createResponse = createEmployee(newEmployee);
        System.out.println("   Response: " + createResponse);
        if (createResponse.isSuccessful()) {
            System.out.println("   Created: " + createResponse.getBody());
        }
        
        // 4. PUT - Update employee
        System.out.println("\n4. PUT /api/employees/2");
        EmployeeResource updateData = new EmployeeResource(null, "Bob Smith Jr.", "bob.jr@example.com", "Marketing", 68000.0);
        HttpResponse<EmployeeResource> updateResponse = updateEmployee(2L, updateData);
        System.out.println("   Response: " + updateResponse);
        if (updateResponse.isSuccessful()) {
            System.out.println("   Updated: " + updateResponse.getBody());
        }
        
        // 5. GET with query parameters - Search
        System.out.println("\n5. GET /api/employees/search?department=Engineering&minSalary=50000");
        HttpResponse<List<EmployeeResource>> searchResponse = searchEmployees("Engineering", 50000.0);
        System.out.println("   Response: " + searchResponse);
        System.out.println("   Results: " + searchResponse.getBody().size());
        
        // 6. DELETE - Remove employee
        System.out.println("\n6. DELETE /api/employees/3");
        HttpResponse<Void> deleteResponse = deleteEmployee(3L);
        System.out.println("   Response: " + deleteResponse);
        
        // 7. GET - Try to retrieve deleted employee (should be 404)
        System.out.println("\n7. GET /api/employees/3 (after deletion)");
        HttpResponse<EmployeeResource> getDeletedResponse = getEmployee(3L);
        System.out.println("   Response: " + getDeletedResponse);
        
        // 8. POST - Try to create invalid employee (should be 400)
        System.out.println("\n8. POST /api/employees (invalid data)");
        EmployeeResource invalidEmployee = new EmployeeResource(null, "", "invalid-email", "IT", 75000.0);
        HttpResponse<EmployeeResource> invalidResponse = createEmployee(invalidEmployee);
        System.out.println("   Response: " + invalidResponse);
        
        System.out.println("\n=== Key REST Concepts Demonstrated ===");
        System.out.println("- HTTP Methods: GET, POST, PUT, DELETE");
        System.out.println("- Status Codes: 200 (OK), 201 (Created), 204 (No Content), 400 (Bad Request), 404 (Not Found)");
        System.out.println("- Resource-based URLs: /api/employees/{id}");
        System.out.println("- JSON serialization/deserialization");
        System.out.println("- Query parameters for filtering");
        System.out.println("- Input validation and error handling");
        System.out.println("- Stateless operations (each request is independent)");
        
        System.out.println("\n=== Spring Boot Implementation Notes ===");
        System.out.println("In Spring Boot, you would use:");
        System.out.println("- @RestController for the class");
        System.out.println("- @GetMapping, @PostMapping, @PutMapping, @DeleteMapping");
        System.out.println("- @PathVariable for URL parameters");
        System.out.println("- @RequestParam for query parameters");
        System.out.println("- @RequestBody for request payload");
        System.out.println("- ResponseEntity<T> for responses");
        System.out.println("- @Valid for validation");
        System.out.println("- Spring Data JPA for database operations");
    }
}