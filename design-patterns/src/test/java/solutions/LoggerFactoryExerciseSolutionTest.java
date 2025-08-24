package solutions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.assertj.core.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@DisplayName("Logger Factory Exercise Tests")
class LoggerFactoryExerciseSolutionTest {
    
    private LoggerConfig testConfig;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalErr;
    
    @BeforeEach
    void setUp() {
        testConfig = new LoggerConfig("TestLogger", LogLevel.DEBUG);
        
        // Capture System.out and System.err for testing
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        errorStream = new ByteArrayOutputStream();
        originalErr = System.err;
        System.setErr(new PrintStream(errorStream));
    }
    
    @AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setErr(originalErr);
        AppLoggerFactory.clearCache();
    }
    
    @Test
    @DisplayName("LogEntry should create with all parameters")
    void logEntryShouldCreateWithAllParameters() {
        var timestamp = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        var entry = new LogEntry(timestamp, LogLevel.INFO, "TestLogger", "Test message", "main");
        
        assertThat(entry.timestamp()).isEqualTo(timestamp);
        assertThat(entry.level()).isEqualTo(LogLevel.INFO);
        assertThat(entry.loggerName()).isEqualTo("TestLogger");
        assertThat(entry.message()).isEqualTo("Test message");
        assertThat(entry.threadName()).isEqualTo("main");
    }
    
    @Test
    @DisplayName("LogEntry should create with current timestamp and thread")
    void logEntryShouldCreateWithCurrentTimestampAndThread() {
        var entry = new LogEntry(LogLevel.WARN, "TestLogger", "Warning message");
        
        assertThat(entry.level()).isEqualTo(LogLevel.WARN);
        assertThat(entry.loggerName()).isEqualTo("TestLogger");
        assertThat(entry.message()).isEqualTo("Warning message");
        assertThat(entry.timestamp()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(entry.threadName()).isNotBlank();
    }
    
    @Test
    @DisplayName("LogEntry should format correctly")
    void logEntryShouldFormatCorrectly() {
        var timestamp = LocalDateTime.of(2025, 1, 1, 12, 30, 45);
        var entry = new LogEntry(timestamp, LogLevel.ERROR, "TestLogger", "Error message", "worker-1");
        
        String formatted = entry.formatted();
        
        assertThat(formatted)
            .contains("2025-01-01 12:30:45")
            .contains("[ERROR]")
            .contains("TestLogger")
            .contains("Error message");
    }
    
    @Test
    @DisplayName("LoggerConfig should create with defaults")
    void loggerConfigShouldCreateWithDefaults() {
        var config = new LoggerConfig("TestLogger", LogLevel.INFO);
        
        assertThat(config.name()).isEqualTo("TestLogger");
        assertThat(config.minimumLevel()).isEqualTo(LogLevel.INFO);
        assertThat(config.pattern()).contains("%timestamp");
        assertThat(config.properties()).isEmpty();
    }
    
    @Test
    @DisplayName("NoOpLogger should implement all methods correctly")
    void noOpLoggerShouldImplementAllMethodsCorrectly() {
        var noopLogger = new NoOpLogger(testConfig);
        
        assertThat(noopLogger.getName()).isEqualTo("No-Op Logger");
        assertThat(noopLogger.getConfig()).isEqualTo(testConfig);
        
        // These should not produce any output
        noopLogger.debug("Debug message");
        noopLogger.info("Info message");
        noopLogger.warn("Warn message");
        noopLogger.error("Error message");
        noopLogger.log(LogLevel.INFO, "Log message");
        
        assertThat(outputStream.toString()).isEmpty();
        assertThat(errorStream.toString()).isEmpty();
    }
    
    @Test
    @DisplayName("NoOpLogger should close cleanly")
    void noOpLoggerShouldCloseCleanly() {
        var noopLogger = new NoOpLogger(testConfig);
        
        noopLogger.close();
        
        assertThat(outputStream.toString()).contains("No-op logger closed");
    }
    
    @Test
    @DisplayName("AppLoggerFactory should create NoOpLogger")
    void appLoggerFactoryShouldCreateNoOpLogger() {
        AppLogger logger = AppLoggerFactory.createNoOpLogger("TestLogger");
        
        assertThat(logger).isInstanceOf(NoOpLogger.class);
        assertThat(logger.getName()).isEqualTo("No-Op Logger");
        assertThat(logger.getConfig().name()).isEqualTo("TestLogger");
    }
    
    @Test
    @DisplayName("AppLoggerFactory should cache loggers")
    void appLoggerFactoryShouldCacheLoggers() {
        AppLogger logger1 = AppLoggerFactory.getLogger("CachedLogger");
        AppLogger logger2 = AppLoggerFactory.getLogger("CachedLogger");
        
        // Should return the same instance (cached)
        assertThat(logger1).isSameAs(logger2);
    }
    
    @Test
    @DisplayName("AppLoggerFactory should create logger from class")
    void appLoggerFactoryShouldCreateLoggerFromClass() {
        AppLogger logger = AppLoggerFactory.getLogger(LoggerFactoryExerciseSolutionTest.class);
        
        assertThat(logger).isNotNull();
        assertThat(logger.getConfig().name()).isEqualTo("LoggerFactoryExerciseSolutionTest");
    }
    
    @Test
    @DisplayName("AppLoggerFactory should clear cache correctly")
    void appLoggerFactoryShouldClearCacheCorrectly() {
        AppLogger logger1 = AppLoggerFactory.getLogger("TempLogger");
        AppLoggerFactory.clearCache();
        AppLogger logger2 = AppLoggerFactory.getLogger("TempLogger");
        
        // Should be different instances after cache clear
        assertThat(logger1).isNotSameAs(logger2);
    }
    
    @Test
    @DisplayName("Logger should check level enablement correctly")
    void loggerShouldCheckLevelEnablementCorrectly() {
        var config = new LoggerConfig("TestLogger", LogLevel.WARN);
        var logger = new NoOpLogger(config);
        
        assertThat(logger.isEnabled(LogLevel.DEBUG)).isFalse();
        assertThat(logger.isEnabled(LogLevel.INFO)).isFalse();
        assertThat(logger.isEnabled(LogLevel.WARN)).isTrue();
        assertThat(logger.isEnabled(LogLevel.ERROR)).isTrue();
    }
    
    @Test
    @DisplayName("Logger should generate proper info")
    void loggerShouldGenerateProperInfo() {
        var logger = new NoOpLogger(testConfig);
        
        String info = logger.getLoggerInfo();
        
        assertThat(info)
            .contains("TestLogger")
            .contains("NoOpLogger")
            .contains("DEBUG")
            .contains("%timestamp");
    }
    
    // Tests for TODO implementations - these will pass once students complete the exercises
    
    @Test
    @DisplayName("ConsoleLogger should implement all methods correctly (TODO)")
    void consoleLoggerShouldImplementAllMethodsCorrectly() {
        var consoleLogger = new ConsoleLogger(testConfig);
        
        // These assertions will fail until students implement the TODO methods
        assertThat(consoleLogger.getName()).isEqualTo("Console Logger");
        
        consoleLogger.info("Test info message");
        consoleLogger.error("Test error message");
        
        // Should have output to System.out and System.err
        assertThat(outputStream.toString()).isNotEmpty();
        assertThat(errorStream.toString()).isNotEmpty();
    }
    
    @Test
    @DisplayName("FileLogger should implement all methods correctly (TODO)")
    void fileLoggerShouldImplementAllMethodsCorrectly() {
        var config = new LoggerConfig("FileLogger", LogLevel.INFO, 
            "%timestamp [%level] %logger - %message", Map.of("filename", "test.log"));
        var fileLogger = new FileLogger(config);
        
        // These assertions will fail until students implement the TODO methods
        assertThat(fileLogger.getName()).isEqualTo("File Logger");
        
        fileLogger.info("Test file message");
        
        // Should have FILE: prefix in output
        assertThat(outputStream.toString()).contains("FILE:");
        assertThat(outputStream.toString()).contains("test.log");
    }
    
    @Test
    @DisplayName("JsonLogger should implement all methods correctly (TODO)")
    void jsonLoggerShouldImplementAllMethodsCorrectly() {
        var jsonLogger = new JsonLogger(testConfig);
        
        // These assertions will fail until students implement the TODO methods
        assertThat(jsonLogger.getName()).isEqualTo("JSON Logger");
        
        jsonLogger.info("Test JSON message");
        
        // Should have JSON format in output
        String output = outputStream.toString();
        assertThat(output).contains("{");
        assertThat(output).contains("\"level\":");
        assertThat(output).contains("\"message\":");
    }
    
    @Test
    @DisplayName("AppLoggerFactory should create console logger (TODO)")
    void appLoggerFactoryShouldCreateConsoleLogger() {
        AppLogger logger = AppLoggerFactory.createConsoleLogger("TestConsole");
        
        // This will fail until students implement the TODO method
        assertThat(logger).isInstanceOf(ConsoleLogger.class);
        assertThat(logger).isNotNull();
        assertThat(logger.getConfig().name()).isEqualTo("TestConsole");
    }
    
    @Test
    @DisplayName("AppLoggerFactory should create file logger (TODO)")
    void appLoggerFactoryShouldCreateFileLogger() {
        AppLogger logger = AppLoggerFactory.createFileLogger("TestFile", LogLevel.WARN, "test.log");
        
        // This will fail until students implement the TODO method
        assertThat(logger).isInstanceOf(FileLogger.class);
        assertThat(logger).isNotNull();
        assertThat(logger.getConfig().name()).isEqualTo("TestFile");
        assertThat(logger.getConfig().minimumLevel()).isEqualTo(LogLevel.WARN);
    }
    
    @Test
    @DisplayName("AppLoggerFactory should create JSON logger (TODO)")
    void appLoggerFactoryShouldCreateJsonLogger() {
        AppLogger logger = AppLoggerFactory.createJsonLogger("TestJson");
        
        // This will fail until students implement the TODO method
        assertThat(logger).isInstanceOf(JsonLogger.class);
        assertThat(logger).isNotNull();
        assertThat(logger.getConfig().name()).isEqualTo("TestJson");
    }
    
    @Test
    @DisplayName("Factory should create different logger types")
    void factoryShouldCreateDifferentLoggerTypes() {
        AppLogger consoleLogger = AppLoggerFactory.createLogger(
            AppLoggerFactory.LoggerType.CONSOLE, "Console", LogLevel.INFO, Map.of());
        AppLogger fileLogger = AppLoggerFactory.createLogger(
            AppLoggerFactory.LoggerType.FILE, "File", LogLevel.DEBUG, Map.of("filename", "app.log"));
        AppLogger jsonLogger = AppLoggerFactory.createLogger(
            AppLoggerFactory.LoggerType.JSON, "Json", LogLevel.WARN, Map.of());
        AppLogger noopLogger = AppLoggerFactory.createLogger(
            AppLoggerFactory.LoggerType.NOOP, "Noop", LogLevel.ERROR, Map.of());
        
        assertThat(consoleLogger).isInstanceOf(ConsoleLogger.class);
        assertThat(fileLogger).isInstanceOf(FileLogger.class);
        assertThat(jsonLogger).isInstanceOf(JsonLogger.class);
        assertThat(noopLogger).isInstanceOf(NoOpLogger.class);
    }
    
    @Test
    @DisplayName("Different loggers should maintain independent state")
    void differentLoggersShouldMaintainIndependentState() {
        AppLogger logger1 = AppLoggerFactory.createNoOpLogger("Logger1");
        AppLogger logger2 = AppLoggerFactory.createNoOpLogger("Logger2");
        
        assertThat(logger1.getConfig().name()).isEqualTo("Logger1");
        assertThat(logger2.getConfig().name()).isEqualTo("Logger2");
        assertThat(logger1).isNotSameAs(logger2);
    }
    
    @Test
    @DisplayName("Loggers should support polymorphic behavior")
    void loggersShouldSupportPolymorphicBehavior() {
        AppLogger logger = AppLoggerFactory.createNoOpLogger("PolymorphicTest");
        
        // Should be able to call methods polymorphically through AppLogger interface
        assertThat(logger.getConfig().name()).isEqualTo("PolymorphicTest");
        assertThat(logger.isEnabled(LogLevel.ERROR)).isTrue();
        
        // Calling log methods should work regardless of concrete type
        logger.info("Test message");
        logger.close();
    }
    
    @Test
    @DisplayName("Log levels should have correct ordering")
    void logLevelsShouldHaveCorrectOrdering() {
        assertThat(LogLevel.DEBUG.ordinal()).isLessThan(LogLevel.INFO.ordinal());
        assertThat(LogLevel.INFO.ordinal()).isLessThan(LogLevel.WARN.ordinal());
        assertThat(LogLevel.WARN.ordinal()).isLessThan(LogLevel.ERROR.ordinal());
    }
}