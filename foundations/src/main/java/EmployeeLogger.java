import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Demonstrates logging best practices with SLF4J and Logback.
 * This class shows proper logging levels, parameterized messages,
 * and Mapped Diagnostic Context (MDC) usage.
 */
public class EmployeeLogger {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeLogger.class);
    
    public static void main(String[] args) {
        EmployeeLogger demo = new EmployeeLogger();
        
        System.out.println("=== Employee Logging Demonstration ===");
        
        // Basic logging levels
        demo.demonstrateLoggingLevels();
        
        // Parameterized messages
        demo.demonstrateParameterizedMessages();
        
        // Exception logging
        demo.demonstrateExceptionLogging();
        
        // MDC context
        demo.demonstrateMDC();
        
        System.out.println("\n=== Logging demonstration complete ===");
    }
    
    /**
     * Demonstrates different logging levels and when to use them.
     */
    public void demonstrateLoggingLevels() {
        logger.info("=== Demonstrating Logging Levels ===");
        
        // TRACE - Very detailed diagnostic information
        logger.trace("Starting employee validation process");
        
        // DEBUG - Debugging information
        logger.debug("Validating employee data structure");
        
        // INFO - General information about application flow
        logger.info("Processing employee record for John Doe");
        
        // WARN - Warning about potential issues
        logger.warn("Employee salary is unusually high: $500,000");
        
        // ERROR - Error conditions that don't stop the application
        logger.error("Failed to validate employee email format");
    }
    
    /**
     * Shows efficient parameterized message formatting.
     * This approach only formats strings if the log level is enabled.
     */
    public void demonstrateParameterizedMessages() {
        logger.info("=== Demonstrating Parameterized Messages ===");
        
        String employeeName = "Alice Johnson";
        int employeeId = 1001;
        double salary = 75000.0;
        String department = "Engineering";
        
        // Good - parameterized messages (efficient)
        logger.info("Processing employee: {}", employeeName);
        logger.info("Employee {} has salary ${}", employeeName, salary);
        logger.info("Employee {} (ID: {}) works in {}", employeeName, employeeId, department);
        
        // Show what NOT to do (commented out but shown for education)
        // BAD: String concatenation always happens
        // logger.info("Processing employee: " + employeeName);
        
        // Show conditional logging for expensive operations
        if (logger.isDebugEnabled()) {
            String expensiveDebugInfo = buildExpensiveDebugString(employeeName, employeeId, salary);
            logger.debug("Detailed employee info: {}", expensiveDebugInfo);
        }
    }
    
    /**
     * Demonstrates proper exception logging with stack traces.
     */
    public void demonstrateExceptionLogging() {
        logger.info("=== Demonstrating Exception Logging ===");
        
        try {
            // Simulate some operation that might fail
            processEmployeeData("invalid-email-format");
        } catch (ValidationException e) {
            // Log exception with stack trace - notice the exception as last parameter
            logger.error("Failed to process employee data: {}", e.getMessage(), e);
        }
        
        try {
            // Simulate database operation
            saveToDatabase("employee-data");
        } catch (DatabaseException e) {
            // Different logging based on exception type
            logger.warn("Database temporarily unavailable, will retry: {}", e.getMessage());
        }
    }
    
    /**
     * Demonstrates Mapped Diagnostic Context (MDC) for adding context to logs.
     * MDC is thread-local and automatically included in log patterns.
     */
    public void demonstrateMDC() {
        logger.info("=== Demonstrating MDC (Mapped Diagnostic Context) ===");
        
        String userId = "user123";
        String requestId = "req-456";
        String sessionId = "session-789";
        
        try {
            // Add context to MDC - this will appear in all log messages in this thread
            MDC.put("userId", userId);
            MDC.put("requestId", requestId);
            MDC.put("sessionId", sessionId);
            
            logger.info("Processing employee request");
            
            // Simulate some business logic
            processEmployeeRequest();
            
            logger.info("Employee request completed successfully");
            
        } finally {
            // Always clear MDC to prevent memory leaks
            MDC.clear();
        }
        
        // Log message after clearing MDC - no context
        logger.info("MDC demonstration complete");
    }
    
    /**
     * Simulates expensive debug information generation.
     */
    private String buildExpensiveDebugString(String name, int id, double salary) {
        // Simulate expensive operation (string building, JSON serialization, etc.)
        StringBuilder sb = new StringBuilder();
        sb.append("Employee Details: {");
        sb.append("name='").append(name).append("', ");
        sb.append("id=").append(id).append(", ");
        sb.append("salary=").append(salary).append(", ");
        sb.append("timestamp=").append(System.currentTimeMillis());
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Simulates employee data processing that might throw validation errors.
     */
    private void processEmployeeData(String email) throws ValidationException {
        if (!email.contains("@")) {
            throw new ValidationException("Invalid email format: " + email);
        }
        logger.debug("Employee email validation passed for: {}", email);
    }
    
    /**
     * Simulates database operation that might fail.
     */
    private void saveToDatabase(String data) throws DatabaseException {
        // Simulate random database failure
        if (Math.random() < 0.3) {
            throw new DatabaseException("Database connection timeout");
        }
        logger.debug("Successfully saved to database: {}", data);
    }
    
    /**
     * Simulates processing an employee request with multiple steps.
     */
    private void processEmployeeRequest() {
        logger.debug("Validating employee request");
        logger.debug("Loading employee from database");
        logger.debug("Applying business rules");
        logger.debug("Updating employee record");
    }
    
    /**
     * Custom exception for validation errors.
     */
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
    
    /**
     * Custom exception for database errors.
     */
    public static class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }
}