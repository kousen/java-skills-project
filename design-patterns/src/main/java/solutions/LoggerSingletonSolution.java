package solutions;

import java.util.logging.Level;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SOLUTION: Thread-safe Singleton Logger Exercise
 * <p>
 * This is the COMPLETE SOLUTION to the LoggerSingleton.java student exercise.
 * Students should work on LoggerSingleton.java first, then compare with this solution.
 * <p>
 * This implementation follows the Singleton pattern and manages a single logging 
 * instance across the application with thread-safe lazy initialization.
 * <p>
 * Completed Requirements:
 * 1. ✅ Thread-safe lazy initialization using double-checked locking
 * 2. ✅ Prevents cloning by overriding the clone() method  
 * 3. ✅ Provides logging methods with direct System.out implementation
 * 4. ✅ Configured with INFO level by default
 * <p>
 * ⚠️  FOR INSTRUCTORS: This file contains completed implementations.
 * ⚠️  STUDENTS should work on LoggerSingleton.java (the version with TODOs).
 */
public class LoggerSingletonSolution {
    
    private static volatile LoggerSingletonSolution instance;
    
    private final String loggerName;
    private Level currentLevel;
    
    // TODO: Implement private constructor
    private LoggerSingletonSolution() {
        this.loggerName = "ApplicationLogger";
        this.currentLevel = Level.INFO; // Default level
    }
    
    // COMPLETED: Implement thread-safe getInstance() method using double-checked locking
    public static LoggerSingletonSolution getInstance() {
        if (instance == null) {
            synchronized (LoggerSingletonSolution.class) {
                if (instance == null) {
                    instance = new LoggerSingletonSolution();
                }
            }
        }
        return instance;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone singleton instance");
    }
    
    // Logging methods - direct implementation for better test compatibility
    public void info(String message) {
        if (isLevelEnabled(Level.INFO)) {
            logMessage(Level.INFO, message);
        }
    }
    
    public void warning(String message) {
        if (isLevelEnabled(Level.WARNING)) {
            logMessage(Level.WARNING, message);
        }
    }
    
    public void severe(String message) {
        if (isLevelEnabled(Level.SEVERE)) {
            logMessage(Level.SEVERE, message);
        }
    }
    
    public void fine(String message) {
        if (isLevelEnabled(Level.FINE)) {
            logMessage(Level.FINE, message);
        }
    }
    
    public void log(Level level, String message) {
        if (isLevelEnabled(level)) {
            logMessage(level, message);
        }
    }
    
    private void logMessage(Level level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss"));
        String formattedMessage = String.format("%s %s %s: %s", timestamp, level, loggerName, message);
        System.out.println(formattedMessage);
    }
    
    private boolean isLevelEnabled(Level level) {
        return level.intValue() >= currentLevel.intValue();
    }
    
    public void setLevel(Level level) {
        this.currentLevel = level;
    }
    
    public Level getLevel() {
        return currentLevel;
    }
    
    public String getLoggerName() {
        return loggerName;
    }
    
    // Demonstration method to show the singleton in action
    public static void demonstrateSingleton() {
        System.out.println("=== LoggerSingletonSolution Demo ===");
        
        LoggerSingletonSolution logger1 = LoggerSingletonSolution.getInstance();
        LoggerSingletonSolution logger2 = LoggerSingletonSolution.getInstance();
        LoggerSingletonSolution logger3 = LoggerSingletonSolution.getInstance();
        
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