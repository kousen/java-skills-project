import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Try It Out Exercise: Logger Factory Pattern
 * <p>
 * This exercise demonstrates the Factory pattern using a realistic example:
 * creating different types of loggers (Console, File, JSON) without
 * the client code knowing the specific logger implementations.
 * <p>
 * This prepares you for Section 19 (Logging) and shows how SLF4J's
 * LoggerFactory works under the hood.
 * <p>
 * TODO: Complete the missing logger implementations below.
 */

// Enum for log levels
enum LogLevel {
    DEBUG, INFO, WARN, ERROR
}

// Data container for log entries
record LogEntry(
    LocalDateTime timestamp,
    LogLevel level,
    String loggerName,
    String message,
    String threadName
) {
    // Constructor with current timestamp and thread
    public LogEntry(LogLevel level, String loggerName, String message) {
        this(LocalDateTime.now(), level, loggerName, message, Thread.currentThread().getName());
    }
    
    // Format for display
    public String formatted() {
        return "%s [%s] %s - %s".formatted(
            timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            level,
            loggerName,
            message
        );
    }
}

// Configuration for different logger types
record LoggerConfig(
    String name,
    LogLevel minimumLevel,
    String pattern,
    Map<String, String> properties
) {
    // Constructor with defaults
    public LoggerConfig(String name, LogLevel minimumLevel) {
        this(name, minimumLevel, "%timestamp [%level] %logger - %message", Map.of());
    }
}

// Abstract logger interface - the product
interface AppLogger {
    String getName();
    LoggerConfig getConfig();
    void log(LogLevel level, String message);
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    void close();
    
    default boolean isEnabled(LogLevel level) {
        return level.ordinal() >= getConfig().minimumLevel().ordinal();
    }
    
    default String getLoggerInfo() {
        var config = getConfig();
        return """
            Logger Information:
              Name: %s
              Type: %s
              Minimum Level: %s
              Pattern: %s
              Properties: %s
            """.formatted(config.name(), getClass().getSimpleName(),
                config.minimumLevel(), config.pattern(), config.properties());
    }
}

// TODO: Implement ConsoleLogger
// Requirements:
// - Logs to System.out for INFO/DEBUG, System.err for WARN/ERROR
// - Uses config pattern for formatting
// - getName() returns "Console Logger"
// - close() prints "Console logger closed"
record ConsoleLogger(LoggerConfig config) implements AppLogger {
    
    @Override
    public String getName() {
        // TODO: Return "Console Logger"
        return "Console Logger"; // Minimal implementation for compilation
    }
    
    @Override
    public LoggerConfig getConfig() {
        return config; // Records get this automatically but needed for compilation
    }
    
    @Override
    public void log(LogLevel level, String message) {
        // TODO: Implement console logging
        // Check if level is enabled, create LogEntry, format and print
        // Use System.out for DEBUG/INFO, System.err for WARN/ERROR
        System.out.println("TODO: Implement console logging"); // Minimal implementation
    }
    
    @Override
    public void debug(String message) {
        // TODO: Call log() with DEBUG level
        log(LogLevel.DEBUG, message); // Minimal implementation
    }
    
    @Override
    public void info(String message) {
        // TODO: Call log() with INFO level
        log(LogLevel.INFO, message); // Minimal implementation
    }
    
    @Override
    public void warn(String message) {
        // TODO: Call log() with WARN level
        log(LogLevel.WARN, message); // Minimal implementation
    }
    
    @Override
    public void error(String message) {
        // TODO: Call log() with ERROR level
        log(LogLevel.ERROR, message); // Minimal implementation
    }
    
    @Override
    public void close() {
        // TODO: Print "Console logger closed"
        System.out.println("TODO: Console logger closed"); // Minimal implementation
    }
}

// TODO: Implement FileLogger
// Requirements:
// - Simulates file logging (just print with "FILE:" prefix)
// - getName() returns "File Logger"
// - close() prints "File logger closed: [filename]"
// - Use filename from config properties ("filename" key)
record FileLogger(LoggerConfig config) implements AppLogger {
    
    @Override
    public String getName() {
        // TODO: Return "File Logger"
        return "File Logger"; // Minimal implementation for compilation
    }
    
    @Override
    public LoggerConfig getConfig() {
        return config; // Records get this automatically but needed for compilation
    }
    
    @Override
    public void log(LogLevel level, String message) {
        // TODO: Implement file logging simulation
        // Check if enabled, create LogEntry, print with "FILE:" prefix
        // Include filename from config.properties().get("filename")
        System.out.println("TODO: Implement file logging"); // Minimal implementation
    }
    
    @Override
    public void debug(String message) {
        // TODO: Call log() with DEBUG level
        log(LogLevel.DEBUG, message); // Minimal implementation
    }
    
    @Override
    public void info(String message) {
        // TODO: Call log() with INFO level
        log(LogLevel.INFO, message); // Minimal implementation
    }
    
    @Override
    public void warn(String message) {
        // TODO: Call log() with WARN level
        log(LogLevel.WARN, message); // Minimal implementation
    }
    
    @Override
    public void error(String message) {
        // TODO: Call log() with ERROR level
        log(LogLevel.ERROR, message); // Minimal implementation
    }
    
    @Override
    public void close() {
        // TODO: Print closing message with filename
        System.out.println("TODO: File logger closed"); // Minimal implementation
    }
}

// TODO: Implement JsonLogger
// Requirements:
// - Outputs JSON format: {"timestamp":"...", "level":"...", "logger":"...", "message":"..."}
// - getName() returns "JSON Logger"
// - close() prints "JSON logger closed"
record JsonLogger(LoggerConfig config) implements AppLogger {
    
    @Override
    public String getName() {
        // TODO: Return "JSON Logger"
        return "JSON Logger"; // Minimal implementation for compilation
    }
    
    @Override
    public LoggerConfig getConfig() {
        return config; // Records get this automatically but needed for compilation
    }
    
    @Override
    public void log(LogLevel level, String message) {
        // TODO: Implement JSON logging
        // Format: {"timestamp":"2025-01-01T12:00:00", "level":"INFO", "logger":"TestLogger", "message":"Test message"}
        System.out.println("TODO: Implement JSON logging"); // Minimal implementation
    }
    
    @Override
    public void debug(String message) {
        // TODO: Call log() with DEBUG level
        log(LogLevel.DEBUG, message); // Minimal implementation
    }
    
    @Override
    public void info(String message) {
        // TODO: Call log() with INFO level
        log(LogLevel.INFO, message); // Minimal implementation
    }
    
    @Override
    public void warn(String message) {
        // TODO: Call log() with WARN level
        log(LogLevel.WARN, message); // Minimal implementation
    }
    
    @Override
    public void error(String message) {
        // TODO: Call log() with ERROR level
        log(LogLevel.ERROR, message); // Minimal implementation
    }
    
    @Override
    public void close() {
        // TODO: Print "JSON logger closed"
        System.out.println("TODO: JSON logger closed"); // Minimal implementation
    }
}

// COMPLETED: NoOpLogger as an example
record NoOpLogger(LoggerConfig config) implements AppLogger {
    
    @Override
    public String getName() {
        return "No-Op Logger";
    }
    
    @Override
    public LoggerConfig getConfig() {
        return config;
    }
    
    @Override
    public void log(LogLevel level, String message) {
        // No-op: does nothing (for testing/production where logging is disabled)
    }
    
    @Override
    public void debug(String message) { /* no-op */ }
    
    @Override
    public void info(String message) { /* no-op */ }
    
    @Override
    public void warn(String message) { /* no-op */ }
    
    @Override
    public void error(String message) { /* no-op */ }
    
    @Override
    public void close() {
        System.out.println("No-op logger closed (no resources to release)");
    }
}

// Logger factory - creates different types of loggers
class AppLoggerFactory {
    
    // TODO: Implement createConsoleLogger static factory method
    // Should create ConsoleLogger with given name and INFO level
    public static AppLogger createConsoleLogger(String name) {
        // TODO: Return new ConsoleLogger instance
        var config = new LoggerConfig(name, LogLevel.INFO); // Minimal implementation
        return new ConsoleLogger(config); // Minimal implementation
    }
    
    // TODO: Implement createFileLogger static factory method  
    // Should create FileLogger with given name, level, and filename
    public static AppLogger createFileLogger(String name, LogLevel level, String filename) {
        // TODO: Create LoggerConfig with filename in properties, return FileLogger
        var config = new LoggerConfig(name, level, "%timestamp [%level] %logger - %message", 
            Map.of("filename", filename)); // Minimal implementation
        return new FileLogger(config); // Minimal implementation
    }
    
    // TODO: Implement createJsonLogger static factory method
    // Should create JsonLogger with given name and DEBUG level
    public static AppLogger createJsonLogger(String name) {
        // TODO: Return new JsonLogger instance
        var config = new LoggerConfig(name, LogLevel.DEBUG); // Minimal implementation
        return new JsonLogger(config); // Minimal implementation
    }
    
    // COMPLETED: NoOpLogger factory as an example
    public static AppLogger createNoOpLogger(String name) {
        var config = new LoggerConfig(name, LogLevel.ERROR); // High threshold = no logging
        return new NoOpLogger(config);
    }
    
    // Factory method for different logger types
    public enum LoggerType {
        CONSOLE, FILE, JSON, NOOP
    }
    
    public static AppLogger createLogger(LoggerType type, String name, LogLevel level, Map<String, String> properties) {
        var config = new LoggerConfig(name, level, "%timestamp [%level] %logger - %message", properties);
        
        return switch (type) {
            case CONSOLE -> new ConsoleLogger(config);
            case FILE -> new FileLogger(config);
            case JSON -> new JsonLogger(config);
            case NOOP -> new NoOpLogger(config);
        };
    }
    
    // Registry-based approach (like SLF4J)
    private static final Map<String, AppLogger> LOGGER_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
    
    public static AppLogger getLogger(String name) {
        // Default to console logger (like SLF4J defaults)
        return LOGGER_CACHE.computeIfAbsent(name, AppLoggerFactory::createConsoleLogger);
    }
    
    public static AppLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }
    
    public static void clearCache() {
        LOGGER_CACHE.values().forEach(AppLogger::close);
        LOGGER_CACHE.clear();
    }
}

// Demo class showing the Logger Factory pattern in action
public class LoggerFactoryExercise {
    
    public static void main(String[] args) {
        demonstrateLoggerFactories();
    }
    
    public static void demonstrateLoggerFactories() {
        System.out.println("=== Logger Factory Pattern Demo (Preview of Section 19) ===");
        
        // 1. Create different logger types using static factory methods
        System.out.println("\n--- 1. Creating Different Logger Types ---");
        AppLogger consoleLogger = AppLoggerFactory.createConsoleLogger("ConsoleApp");
        AppLogger fileLogger = AppLoggerFactory.createFileLogger("FileApp", LogLevel.DEBUG, "app.log");
        AppLogger jsonLogger = AppLoggerFactory.createJsonLogger("JsonApp");
        AppLogger noopLogger = AppLoggerFactory.createNoOpLogger("NoOpApp");
        
        // 2. Display logger information
        System.out.println("\n--- 2. Logger Information ---");
        AppLogger[] loggers = {consoleLogger, fileLogger, jsonLogger, noopLogger};
        
        for (AppLogger logger : loggers) {
            if (logger != null) {  // Some might be null if TODO items aren't completed
                System.out.println(logger.getLoggerInfo());
            }
        }
        
        // 3. Test logging at different levels
        System.out.println("\n--- 3. Testing Different Log Levels ---");
        for (AppLogger logger : loggers) {
            if (logger != null) {
                System.out.println("\nTesting " + logger.getName() + ":");
                logger.debug("This is a debug message");
                logger.info("Application started successfully");
                logger.warn("This is a warning message");
                logger.error("An error occurred");
                logger.close();
            }
        }
        
        // 4. Demonstrate SLF4J-style usage
        System.out.println("\n--- 4. SLF4J-Style Logger Usage ---");
        AppLogger classLogger = AppLoggerFactory.getLogger(LoggerFactoryExercise.class);
        AppLogger namedLogger = AppLoggerFactory.getLogger("com.example.service");
        
        if (classLogger != null && namedLogger != null) {
            classLogger.info("Logger created for class: " + LoggerFactoryExercise.class.getSimpleName());
            namedLogger.info("Logger created for service: com.example.service");
        }
        
        // 5. Batch processing with different configurations
        System.out.println("\n--- 5. Batch Logger Creation ---");
        
        // Different logger configurations
        List<Map<String, Object>> loggerConfigs = List.of(
            Map.of("type", AppLoggerFactory.LoggerType.CONSOLE, "name", "WebApp", "level", LogLevel.INFO),
            Map.of("type", AppLoggerFactory.LoggerType.FILE, "name", "DatabaseApp", "level", LogLevel.DEBUG),
            Map.of("type", AppLoggerFactory.LoggerType.JSON, "name", "ApiApp", "level", LogLevel.WARN),
            Map.of("type", AppLoggerFactory.LoggerType.NOOP, "name", "TestApp", "level", LogLevel.ERROR)
        );
        
        System.out.println("Creating loggers from configuration:");
        for (var config : loggerConfigs) {
            var type = (AppLoggerFactory.LoggerType) config.get("type");
            var name = (String) config.get("name");
            var level = (LogLevel) config.get("level");
            
            var logger = AppLoggerFactory.createLogger(type, name, level, Map.of());
            System.out.printf("  Created %s for %s (min level: %s)%n",
                    logger.getName(), name, level);
            logger.info("Logger initialized for " + name);
            logger.close();
        }
        
        // Clean up
        AppLoggerFactory.clearCache();

        System.out.println("""
                \n=== Logger Factory Pattern Benefits ===
                ✓ Client code doesn't know specific logger implementations
                ✓ Easy to switch between console, file, JSON, or no-op logging
                ✓ Caching prevents duplicate logger creation (like SLF4J)
                ✓ Configurable logging levels and output formats
                ✓ Preview of how SLF4J LoggerFactory works internally!
                \n📖 This prepares you for Section 19: Logging Frameworks""");
    }
}