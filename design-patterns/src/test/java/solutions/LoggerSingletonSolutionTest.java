package solutions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LoggerSingletonSolution Tests")
class LoggerSingletonSolutionTest {
    
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp() {
        // Capture system output for logging tests
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    void tearDown() {
        // Restore original system output
        System.setOut(originalOut);
    }
    
    @Test
    @DisplayName("Should return same instance for multiple calls")
    void shouldReturnSameInstanceForMultipleCalls() {
        LoggerSingletonSolution instance1 = LoggerSingletonSolution.getInstance();
        LoggerSingletonSolution instance2 = LoggerSingletonSolution.getInstance();
        LoggerSingletonSolution instance3 = LoggerSingletonSolution.getInstance();
        
        assertThat(instance1).isSameAs(instance2);
        assertThat(instance2).isSameAs(instance3);
        assertThat(instance1).isSameAs(instance3);
    }
    
    @Test
    @DisplayName("Should be thread-safe")
    void shouldBeThreadSafe() throws InterruptedException {
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        Set<LoggerSingletonSolution> instances = ConcurrentHashMap.newKeySet();
        
        try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
            // Create multiple threads that get the singleton instance
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        LoggerSingletonSolution instance = LoggerSingletonSolution.getInstance();
                        instances.add(instance);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            
            // Wait for all threads to complete
            boolean completed = latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();
            
            // Ensure all threads completed within timeout
            assertThat(completed).isTrue();
        }
        
        // All threads should have gotten the same instance
        assertThat(instances).hasSize(1);
    }
    
    @Test
    @DisplayName("Should prevent cloning")
    @SuppressWarnings("JavaReflectionMemberAccess")
    void shouldPreventCloning() {
        LoggerSingletonSolution instance = LoggerSingletonSolution.getInstance();
        
        assertThatThrownBy(() -> {
            // Use reflection to access the clone method (students must override it)
            Method cloneMethod = LoggerSingletonSolution.class.getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);
            cloneMethod.invoke(instance);
        }).hasCauseInstanceOf(CloneNotSupportedException.class);
    }
    
    @Test
    @DisplayName("Should have proper logger configuration")
    void shouldHaveProperLoggerConfiguration() {
        LoggerSingletonSolution logger = LoggerSingletonSolution.getInstance();
        
        assertThat(logger.getLoggerName()).isEqualTo("ApplicationLogger");
        assertThat(logger.getLevel()).isEqualTo(Level.INFO);
    }
    
    @Test
    @DisplayName("Should log info messages")
    void shouldLogInfoMessages() {
        LoggerSingletonSolution logger = LoggerSingletonSolution.getInstance();
        
        logger.info("Test info message");
        
        String output = outputStream.toString();
        assertThat(output).contains("INFO");
        assertThat(output).contains("Test info message");
    }
    
    @Test
    @DisplayName("Should log warning messages")
    void shouldLogWarningMessages() {
        LoggerSingletonSolution logger = LoggerSingletonSolution.getInstance();
        
        logger.warning("Test warning message");
        
        String output = outputStream.toString();
        assertThat(output).contains("WARNING");
        assertThat(output).contains("Test warning message");
    }
    
    @Test
    @DisplayName("Should log severe messages")
    void shouldLogSevereMessages() {
        LoggerSingletonSolution logger = LoggerSingletonSolution.getInstance();
        
        logger.severe("Test severe message");
        
        String output = outputStream.toString();
        assertThat(output).contains("SEVERE");
        assertThat(output).contains("Test severe message");
    }
    
    @Test
    @DisplayName("Should respect log levels")
    void shouldRespectLogLevels() {
        LoggerSingletonSolution logger = LoggerSingletonSolution.getInstance();
        
        // Set to WARNING level - should not log INFO
        logger.setLevel(Level.WARNING);
        
        logger.info("This info should not appear");
        logger.warning("This warning should appear");
        
        String output = outputStream.toString();
        assertThat(output).doesNotContain("This info should not appear");
        assertThat(output).contains("This warning should appear");
        
        // Reset to INFO for other tests
        logger.setLevel(Level.INFO);
    }
    
    @Test
    @DisplayName("Should allow custom log levels")
    void shouldAllowCustomLogLevels() {
        LoggerSingletonSolution logger = LoggerSingletonSolution.getInstance();
        
        logger.log(Level.CONFIG, "Custom config message");
        logger.log(Level.FINE, "Fine level message");
        
        // Since default level is INFO, FINE won't show but CONFIG might
        String output = outputStream.toString();
        // We mainly test that the method doesn't throw exceptions
        assertThat(output).isNotNull();
    }
    
    @Test
    @DisplayName("Should maintain same identity hash codes")
    void shouldMaintainSameIdentityHashCodes() {
        LoggerSingletonSolution logger1 = LoggerSingletonSolution.getInstance();
        LoggerSingletonSolution logger2 = LoggerSingletonSolution.getInstance();
        
        int hash1 = System.identityHashCode(logger1);
        int hash2 = System.identityHashCode(logger2);
        
        assertThat(hash1).isEqualTo(hash2);
    }
    
    @Test
    @DisplayName("Should run demonstration without errors")
    void shouldRunDemonstrationWithoutErrors() {
        // This test ensures the main demonstration method works
        assertThatNoException().isThrownBy(LoggerSingletonSolution::demonstrateSingleton);
        
        String output = outputStream.toString();
        assertThat(output)
            .contains("=== LoggerSingletonSolution Demo ===")
            .contains("logger1 == logger2: true")
            .contains("All references are same instance: true")
            .contains("Instance hash codes:")
            .contains("This is an info message from logger1")
            .contains("This is a warning from logger2")
            .contains("This is a severe message from logger3")
            .contains("Logger name: ApplicationLogger")
            .contains("Current log level: INFO");
    }
}