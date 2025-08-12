import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

/**
 * Try It Out Exercise: Implement a thread-safe Singleton Logger
 * <p>
 * Your task is to complete this LoggerSingleton class following the Singleton pattern.
 * The class should manage a single java.util.logging.Logger instance across the application.
 * <p>
 * Requirements:
 * 1. Implement thread-safe lazy initialization using double-checked locking
 * 2. Prevent cloning by overriding the clone() method
 * 3. Provide logging methods that delegate to the internal Logger
 * 4. Configure the logger with console output and INFO level by default
 * <p>
 * TODO: Complete the implementation below
 */
public class LoggerSingleton {
    
    // TODO: Add the static volatile instance variable
    
    private final Logger logger;
    private final String loggerName;
    
    // TODO: Implement private constructor
    private LoggerSingleton() {
        this.loggerName = "ApplicationLogger";
        
        // Initialize the java.util.logging.Logger
        this.logger = Logger.getLogger(loggerName);
        
        // Configure console handler with simple formatting
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        consoleHandler.setLevel(Level.ALL);
        
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.INFO);
        logger.setUseParentHandlers(false); // Don't use parent handlers
    }
    
    // TODO: Implement thread-safe getInstance() method using double-checked locking
    public static LoggerSingleton getInstance() {
        // Your implementation here
        throw new UnsupportedOperationException("TODO: Implement getInstance() method");
    }
    
    // TODO: Override clone() method to prevent cloning
    
    // Logging methods - delegate to internal logger
    public void info(String message) {
        logger.info(message);
    }
    
    public void warning(String message) {
        logger.warning(message);
    }
    
    public void severe(String message) {
        logger.severe(message);
    }
    
    public void fine(String message) {
        logger.fine(message);
    }
    
    public void log(Level level, String message) {
        logger.log(level, message);
    }
    
    public void setLevel(Level level) {
        logger.setLevel(level);
    }
    
    public Level getLevel() {
        return logger.getLevel();
    }
    
    public String getLoggerName() {
        return loggerName;
    }
    
    // Demonstration method to show the singleton in action
    public static void demonstrateSingleton() {
        System.out.println("=== LoggerSingleton Demo ===");
        
        LoggerSingleton logger1 = LoggerSingleton.getInstance();
        LoggerSingleton logger2 = LoggerSingleton.getInstance();
        LoggerSingleton logger3 = LoggerSingleton.getInstance();
        
        System.out.println("logger1 == logger2: " + (logger1 == logger2));
        System.out.println("logger2 == logger3: " + (logger2 == logger3));
        System.out.println("All references are same instance: " + 
                          (logger1 == logger2 && logger2 == logger3));
        
        System.out.println("Instance hash codes:");
        System.out.println("logger1: " + System.identityHashCode(logger1));
        System.out.println("logger2: " + System.identityHashCode(logger2));
        System.out.println("logger3: " + System.identityHashCode(logger3));
        
        // Test logging functionality
        logger1.fine("This is a fine message from logger1");
        logger1.info("This is an info message from logger1");
        logger2.warning("This is a warning from logger2");
        logger3.severe("This is a severe message from logger3");
        
        System.out.println("Logger name: " + logger1.getLoggerName());
        System.out.println("Current log level: " + logger1.getLevel());
    }
    
    public static void main(String[] args) {
        demonstrateSingleton();
    }
}