package com.oreilly.javaskills;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Demonstrates modern file I/O using Java NIO.2 (java.nio.file package).
 * Shows the advantages of the modern approach over traditional File I/O.
 * This complements the traditional com.oreilly.javaskills.EmployeeFileWriter class.
 */
@SuppressWarnings("CallToPrintStackTrace")
public class ModernEmployeeFileManager {
    
    // Use Path instead of File for modern approach
    private final Path dataDirectory = Paths.get("employee-data");
    private final Path employeeFile = dataDirectory.resolve("employees.csv");
    private final Path backupDirectory = dataDirectory.resolve("backups");
    
    public static void main(String[] args) {
        ModernEmployeeFileManager manager = new ModernEmployeeFileManager();
        
        System.out.println("=== Modern File I/O with NIO.2 Demonstration ===");
        
        try {
            // Basic file operations
            manager.demonstrateBasicOperations();
            
            // Employee data management
            manager.demonstrateEmployeeOperations();
            
            // Advanced features
            manager.demonstrateAdvancedFeatures();
            
            // Comparison with traditional approach
            manager.compareApproaches();
            
        } catch (IOException e) {
            System.err.println("File operation failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Modern file I/O demonstration complete ===");
    }
    
    /**
     * Demonstrates basic NIO.2 file operations.
     */
    public void demonstrateBasicOperations() throws IOException {
        System.out.println("\n--- Basic NIO.2 Operations ---");
        
        // Create directories (creates parent directories automatically)
        Files.createDirectories(dataDirectory);
        Files.createDirectories(backupDirectory);
        System.out.println("Created directories: " + dataDirectory);
        
        // Simple file writing (Java 11+ feature)
        Path simpleFile = dataDirectory.resolve("simple.txt");
        Files.writeString(simpleFile, "Hello, Modern File I/O!");
        System.out.println("Wrote simple text file");
        
        // Simple file reading (Java 11+ feature)
        String content = Files.readString(simpleFile);
        System.out.println("Read content: " + content);
        
        // Check file properties
        if (Files.exists(simpleFile)) {
            System.out.println("File size: " + Files.size(simpleFile) + " bytes");
            System.out.println("Last modified: " + Files.getLastModifiedTime(simpleFile));
            System.out.println("Readable: " + Files.isReadable(simpleFile));
            System.out.println("Writable: " + Files.isWritable(simpleFile));
        }
        
        // Clean up
        Files.deleteIfExists(simpleFile);
        System.out.println("Cleaned up simple file");
    }
    
    /**
     * Demonstrates employee data operations using modern file I/O.
     */
    public void demonstrateEmployeeOperations() throws IOException {
        System.out.println("\n--- Employee Data Operations ---");
        
        // Create sample employee data
        List<Employee> employees = Arrays.asList(
            new Employee("Alice Johnson", "Engineering", 85000.0),
            new Employee("Bob Smith", "Marketing", 65000.0),
            new Employee("Carol Williams", "Engineering", 90000.0),
            new Employee("David Brown", "Sales", 70000.0),
            new Employee("Eve Davis", "Marketing", 68000.0)
        );
        
        // Save employees using modern approach
        saveEmployees(employees);
        System.out.println("Saved " + employees.size() + " employees to " + employeeFile);
        
        // Load employees back
        List<Employee> loadedEmployees = loadEmployees();
        System.out.println("Loaded " + loadedEmployees.size() + " employees from file");
        
        // Display loaded data
        System.out.println("Employee data:");
        loadedEmployees.forEach(emp -> 
            System.out.println("  " + emp.name() + " - " + emp.department() + " - $" + emp.salary()));
        
        // Filter and save specific department
        saveEmployeesByDepartment("Engineering");
        
        // Create backup
        createBackup();
    }
    
    /**
     * Saves employees to CSV file using modern NIO.2 approach.
     */
    public void saveEmployees(List<Employee> employees) throws IOException {
        // Ensure directory exists
        Files.createDirectories(dataDirectory);
        
        // Convert employees to CSV lines using streams
        List<String> lines = employees.stream()
            .map(emp -> String.format("%s,%s,%.2f", 
                emp.name(), emp.department(), emp.salary()))
            .collect(Collectors.toList());
        
        // Add header
        lines.add(0, "Name,Department,Salary");
        
        // Write all lines at once - so much simpler than traditional I/O!
        Files.write(employeeFile, lines, StandardCharsets.UTF_8);
    }
    
    /**
     * Loads employees from CSV file using modern NIO.2 approach.
     */
    public List<Employee> loadEmployees() throws IOException {
        if (!Files.exists(employeeFile)) {
            System.out.println("Employee file not found, returning empty list");
            return new ArrayList<>();
        }
        
        // Read and process lines using streams
        try (Stream<String> lines = Files.lines(employeeFile)) {
            return lines
                .skip(1) // Skip header line
                .map(line -> line.split(","))
                .filter(parts -> parts.length == 3)
                .map(parts -> new Employee(
                    parts[0].trim(),                    // name
                    parts[1].trim(),                    // department
                    Double.parseDouble(parts[2].trim()) // salary
                ))
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Filters and saves employees by department.
     */
    public void saveEmployeesByDepartment(String department) throws IOException {
        System.out.println("Filtering employees by department: " + department);
        
        Path departmentFile = dataDirectory.resolve(department.toLowerCase() + "-employees.csv");
        
        // Read, filter, and write in a streaming fashion
        try (Stream<String> lines = Files.lines(employeeFile)) {
            List<String> filteredLines = lines
                .filter(line -> line.contains(department))
                .collect(Collectors.toList());
            
            // Add header
            filteredLines.add(0, "Name,Department,Salary");
            
            Files.write(departmentFile, filteredLines);
            System.out.println("Saved " + (filteredLines.size() - 1) + " " + department + " employees to " + departmentFile.getFileName());
        }
    }
    
    /**
     * Creates a timestamped backup of the employee file.
     */
    public void createBackup() throws IOException {
        if (!Files.exists(employeeFile)) {
            return;
        }
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path backupFile = backupDirectory.resolve("employees_backup_" + timestamp + ".csv");
        
        // Copy file with modern NIO.2 - much simpler than traditional approach
        Files.copy(employeeFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Created backup: " + backupFile.getFileName());
    }
    
    /**
     * Demonstrates advanced NIO.2 features.
     */
    public void demonstrateAdvancedFeatures() throws IOException {
        System.out.println("\n--- Advanced NIO.2 Features ---");
        
        // List all files in data directory
        System.out.println("Files in data directory:");
        try (Stream<Path> files = Files.list(dataDirectory)) {
            files.filter(Files::isRegularFile)
                 .forEach(file -> System.out.println("  " + file.getFileName()));
        }
        
        // Get detailed file attributes
        if (Files.exists(employeeFile)) {
            BasicFileAttributes attrs = Files.readAttributes(employeeFile, BasicFileAttributes.class);
            System.out.println("\nEmployee file details:");
            System.out.println("  Size: " + attrs.size() + " bytes");
            System.out.println("  Created: " + attrs.creationTime());
            System.out.println("  Modified: " + attrs.lastModifiedTime());
            System.out.println("  Regular file: " + attrs.isRegularFile());
        }
        
        // Walk directory tree (useful for large hierarchies)
        System.out.println("\nAll files in directory tree:");
        try (Stream<Path> walk = Files.walk(dataDirectory)) {
            walk.filter(Files::isRegularFile)
                .forEach(file -> System.out.println("  " + dataDirectory.relativize(file)));
        }
        
        // Find files by pattern
        System.out.println("\nCSV files:");
        try (Stream<Path> files = Files.find(dataDirectory, 2, 
                (path, attrs) -> path.toString().endsWith(".csv"))) {
            files.forEach(file -> System.out.println("  " + file.getFileName()));
        }
    }
    
    /**
     * Compares traditional vs. modern file I/O approaches.
     */
    public void compareApproaches() throws IOException {
        System.out.println("\n--- Traditional vs Modern Approach Comparison ---");
        
        Path comparisonFile = dataDirectory.resolve("comparison.txt");
        String sampleData = "Employee: John Doe, Department: Engineering, Salary: $75000";
        
        // Show traditional approach (commented for reference)
        System.out.println("Traditional approach would require:");
        System.out.println("  - FileWriter or BufferedWriter");
        System.out.println("  - Manual try-catch-finally blocks");
        System.out.println("  - Manual resource management");
        System.out.println("  - Multiple lines of boilerplate code");
        
        // Demonstrate modern approach
        System.out.println("\nModern approach:");
        System.out.println("  Files.writeString(path, data); // Just one line!");
        
        // Actually do it
        Files.writeString(comparisonFile, sampleData);
        String readData = Files.readString(comparisonFile);
        
        System.out.println("  Wrote and read back: " + readData);
        
        // Clean up
        Files.deleteIfExists(comparisonFile);
    }
    
    /**
     * Demonstrates error handling with NIO.2.
     */
    public void demonstrateErrorHandling() {
        System.out.println("\n--- Error Handling Examples ---");
        
        try {
            // Try to read non-existent file
            Files.readString(Paths.get("non-existent-file.txt"));
        } catch (NoSuchFileException e) {
            System.out.println("Caught NoSuchFileException: " + e.getFile());
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }
        
        try {
            // Try to create file in non-existent directory without creating parents
            Path badPath = Paths.get("non-existent-dir", "file.txt");
            Files.writeString(badPath, "test");
        } catch (NoSuchFileException e) {
            System.out.println("Caught NoSuchFileException for directory: " + e.getFile());
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }
    }

    /**
         * Simple Employee class for demonstration.
         */
        public record Employee(String name, String department, double salary) {
        }
}