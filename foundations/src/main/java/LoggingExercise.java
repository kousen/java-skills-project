import org.slf4j.Logger;

/**
 * Try It Out: Logging Exercise
 * <p>
 * Complete the TODO sections to practice logging with SLF4J and Logback.
 * Run this class to see your logging configuration in action.
 */
@SuppressWarnings("SameParameterValue")
public class LoggingExercise {
    // TODO 1: Create a static logger for this class
    // HINT: Use LoggerFactory.getLogger() and pass this class
    private static final Logger logger = null; // Replace this line
    
    public static void main(String[] args) {
        LoggingExercise exercise = new LoggingExercise();
        
        System.out.println("=== Logging Exercise ===");
        
        // Test different exercises
        exercise.basicLoggingExercise();
        exercise.parameterizedMessageExercise();
        exercise.conditionalLoggingExercise();
        exercise.mdcExercise();
        exercise.exceptionLoggingExercise();
        
        System.out.println("=== Exercise Complete ===");
    }
    
    /**
     * TODO 2: Basic Logging Exercise
     * Complete the logging statements using appropriate levels
     */
    public void basicLoggingExercise() {
        String employeeName = "Jane Doe";
        int employeeId = 1001;
        
        // TODO: Log an INFO message that the exercise is starting
        // Use a parameterized message with the employee name
        
        // TODO: Log a DEBUG message showing the employee ID
        
        // TODO: Log a WARN message about something suspicious
        // (e.g., "Employee has no email address")
        
        // TODO: Log a TRACE message for very detailed debugging
        
        System.out.println("✓ Basic logging exercise completed");
    }
    
    /**
     * TODO 3: Parameterized Messages Exercise
     * Practice efficient parameterized logging
     */
    public void parameterizedMessageExercise() {
        String department = "Engineering";
        double budget = 250000.50;
        int employeeCount = 15;
        
        // TODO: Log an INFO message with multiple parameters
        // Message should include department, budget, and employee count
        // Format: "Department {department} has budget ${budget} with {count} employees"
        
        // TODO: Compare efficient vs inefficient logging
        // Inefficient (commented out): logger.info("Department " + department + " has budget $" + budget);
        // Efficient: Use parameterized message
        
        System.out.println("✓ Parameterized message exercise completed");
    }
    
    /**
     * TODO 4: Conditional Logging Exercise
     * Practice guarding expensive operations
     */
    public void conditionalLoggingExercise() {
        // TODO: Check if DEBUG level is enabled before expensive operation
        // if (logger.isDebugEnabled()) {
        //     String expensiveDebugInfo = buildExpensiveString();
        //     logger.debug("Expensive debug info: {}", expensiveDebugInfo);
        // }
        
        System.out.println("✓ Conditional logging exercise completed");
    }
    
    /**
     * TODO 5: MDC (Mapped Diagnostic Context) Exercise
     * Add context that appears in all log messages
     */
    public void mdcExercise() {
        String userId = "user123";
        String requestId = "req-456";
        
        // TODO: Use try-finally pattern with MDC
        try {
            // TODO: Put userId and requestId into MDC
            // Use MDC.put("key", "value")
            
            // TODO: Log some messages - they should automatically include MDC context
            // logger.info("Processing user request");
            // logger.debug("Loading user data");
            // logger.info("Request completed successfully");
            
        } finally {
            // TODO: Clear MDC to prevent memory leaks
            // Use MDC.clear()
        }
        
        System.out.println("✓ MDC exercise completed");
    }
    
    /**
     * TODO 6: Exception Logging Exercise
     * Practice logging exceptions with stack traces
     */
    public void exceptionLoggingExercise() {
        try {
            // Simulate an operation that might fail
            processEmployeeData("invalid-data");
        } catch (Exception e) {
            // TODO: Log the exception with message and stack trace
            // HINT: Pass the exception as the last parameter
            // logger.error("Failed to process employee data: {}", e.getMessage(), e);
        }
        
        System.out.println("✓ Exception logging exercise completed");
    }
    
    // Helper methods for exercises
    
    private String buildExpensiveString() {
        // Simulate expensive operation (database query, JSON serialization, etc.)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("expensive-operation-").append(i).append(" ");
        }
        return sb.toString();
    }
    
    private void processEmployeeData(String data) {
        if ("invalid-data".equals(data)) {
            throw new IllegalArgumentException("Invalid employee data format");
        }
    }
}

/*
 * SOLUTION HINTS:
 * 
 * TODO 1: private static final Logger logger = LoggerFactory.getLogger(LoggingExercise.class);
 * 
 * TODO 2: 
 * logger.info("Starting basic logging exercise for employee: {}", employeeName);
 * logger.debug("Processing employee with ID: {}", employeeId);
 * logger.warn("Employee has no email address configured");
 * logger.trace("Entering method basicLoggingExercise()");
 * 
 * TODO 3:
 * logger.info("Department {} has budget ${} with {} employees", department, budget, employeeCount);
 * 
 * TODO 4:
 * if (logger.isDebugEnabled()) {
 *     String expensiveDebugInfo = buildExpensiveString();
 *     logger.debug("Expensive debug info: {}", expensiveDebugInfo);
 * }
 * 
 * TODO 5:
 * try {
 *     MDC.put("userId", userId);
 *     MDC.put("requestId", requestId);
 *     logger.info("Processing user request");
 *     logger.debug("Loading user data");
 *     logger.info("Request completed successfully");
 * } finally {
 *     MDC.clear();
 * }
 * 
 * TODO 6:
 * logger.error("Failed to process employee data: {}", e.getMessage(), e);
 */