import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeLoggerTest {
    
    @Test
    void testEmployeeLoggerRunsWithoutError() {
        // This test verifies that the EmployeeLogger runs without throwing exceptions
        assertDoesNotThrow(() -> EmployeeLogger.main(new String[]{}));
    }
    
    @Test
    void testLoggerInstantiation() {
        // Test that we can create an instance
        assertDoesNotThrow(() -> {
            EmployeeLogger logger = new EmployeeLogger();
            assertNotNull(logger);
        });
    }
    
    @Test
    void testDemonstrationMethods() {
        EmployeeLogger logger = new EmployeeLogger();
        
        // Test that all demonstration methods run without error
        assertDoesNotThrow(() -> {
            logger.demonstrateLoggingLevels();
            logger.demonstrateParameterizedMessages();
            logger.demonstrateExceptionLogging();
            logger.demonstrateMDC();
        });
    }
}