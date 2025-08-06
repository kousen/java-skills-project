package com.oreilly.javaskills;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Try It Out Exercise: Traditional File Writing with FileWriter and BufferedWriter
 * <p>
 * This exercise demonstrates traditional file I/O operations:
 * - Writing text files with FileWriter
 * - Using BufferedWriter for efficiency
 * - try-with-resources for proper resource management
 * - Creating reports and logs
 * - Appending to existing files
 */
@SuppressWarnings("CallToPrintStackTrace")
public class FileWriterExercise {
    
    private static final String OUTPUT_DIR = "output";
    private static final String REPORT_FILE = OUTPUT_DIR + "/employee_report.txt";
    private static final String LOG_FILE = OUTPUT_DIR + "/activity_log.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void main(String[] args) {
        var exercise = new FileWriterExercise();
        
        System.out.println("=== FileWriter Exercise: Traditional File I/O ===\n");
        
        try {
            // Create output directory
            exercise.createOutputDirectory();
            
            // Exercise 1: Basic FileWriter
            exercise.demonstrateBasicFileWriter();
            
            // Exercise 2: BufferedWriter for efficiency
            exercise.demonstrateBufferedWriter();
            
            // Exercise 3: Create employee report
            exercise.createEmployeeReport();

            // Bonus: Compare with text blocks
            exercise.compareWithTextBlocks();

            // Exercise 4: Append to log file
            exercise.demonstrateAppending();

            // Exercise 5: Write formatted data
            exercise.writeFormattedData();

        } catch (IOException e) {
            System.err.println("Error during file operations: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates the output directory if it doesn't exist
     */
    private void createOutputDirectory() throws IOException {
        // Old style
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Created output directory: " + OUTPUT_DIR);
            }
        }

        // New style
        Path dataPath = Paths.get(OUTPUT_DIR);
        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
            System.out.println("Created data directory: " + OUTPUT_DIR);
        }
    }
    
    /**
     * Exercise 1: Basic FileWriter usage
     */
    public void demonstrateBasicFileWriter() throws IOException {
        System.out.println("1. BASIC FILEWRITER:");
        System.out.println("--------------------");
        
        String filename = OUTPUT_DIR + "/basic_output.txt";
        
        // Using try-with-resources - FileWriter implements Closeable
        try (FileWriter writer = new FileWriter(filename)) {
            // Traditional approach: manual \n for line breaks
            writer.write("Employee Management System\n");
            writer.write("==========================\n");
            writer.write("This file was created using FileWriter.\n");
            writer.write("Date: " + LocalDateTime.now().format(DATE_FORMAT) + "\n");
            
            // Note: We could use a text block here, but that would require
            // writing the entire content as one string to demonstrate the
            // traditional character-by-character approach of FileWriter
        }
        
        System.out.println("Created file: " + filename);
        System.out.println("Note: FileWriter automatically closed by try-with-resources\n");
    }
    
    /**
     * Exercise 2: BufferedWriter for better performance
     */
    public void demonstrateBufferedWriter() throws IOException {
        System.out.println("2. BUFFEREDWRITER FOR EFFICIENCY:");
        System.out.println("---------------------------------");
        
        String filename = OUTPUT_DIR + "/buffered_output.txt";
        
        // Wrap FileWriter in BufferedWriter for better performance
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            writer.write("Employee List");
            writer.newLine();  // Platform-independent line separator
            writer.write("=============");
            writer.newLine();
            writer.newLine();
            
            // Write multiple lines efficiently
            var employees = List.of(
                "Alice Johnson - Engineering - $95,000",
                "Bob Smith - Marketing - $87,000",
                "Carol Davis - Sales - $92,000",
                "David Wilson - HR - $65,000"
            );
            
            for (String employee : employees) {
                writer.write(employee);
                writer.newLine();
            }
        }
        
        System.out.println("Created buffered file: " + filename);
        System.out.println("BufferedWriter improves performance for multiple writes\n");
    }
    
    /**
     * Exercise 3: Create a formatted employee report
     */
    public void createEmployeeReport() throws IOException {
        System.out.println("3. EMPLOYEE REPORT GENERATION:");
        System.out.println("------------------------------");
        
        // Create sample employees
        List<Employee> employees = createSampleEmployees();
        
        // Use PrintWriter for convenient formatting method
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(REPORT_FILE)))) {
            // Write report header
            writer.println("EMPLOYEE SALARY REPORT");
            writer.println("Generated: " + LocalDateTime.now().format(DATE_FORMAT));
            writer.println("=" .repeat(60));
            writer.println();
            
            // Column headers
            writer.printf("%-20s %-15s %15s%n", "Name", "Department", "Salary");
            writer.println("-" .repeat(60));
            
            // Employee data
            double totalSalary = 0;
            for (Employee emp : employees) {
                writer.printf("%-20s %-15s $%,14.2f%n", 
                    emp.name(), emp.department(), emp.salary());
                totalSalary += emp.salary();
            }
            
            // Summary
            writer.println("-" .repeat(60));
            writer.printf("%-20s %-15s $%,14.2f%n", "TOTAL", "", totalSalary);
            writer.printf("%-20s %-15s $%,14.2f%n", "AVERAGE", "", 
                totalSalary / employees.size());
            
            writer.println();
            writer.println("Report completed successfully.");
        }
        
        System.out.println("Generated employee report: " + REPORT_FILE);
        System.out.println("Used PrintWriter for convenient formatting\n");
    }

    /**
     * Bonus: Compare traditional approach with text blocks
     */
    public void compareWithTextBlocks() throws IOException {
        System.out.println("BONUS: TRADITIONAL vs TEXT BLOCKS:");
        System.out.println("----------------------------------");

        String traditionalFile = OUTPUT_DIR + "/traditional_approach.txt";
        String modernFile = OUTPUT_DIR + "/modern_approach.txt";

        // Traditional approach - multiple write calls
        try (FileWriter writer = new FileWriter(traditionalFile)) {
            writer.write("Employee Report\n");
            writer.write("================\n");
            writer.write("Generated: " + LocalDateTime.now().format(DATE_FORMAT) + "\n\n");
            writer.write("Name: Alice Johnson\n");
            writer.write("Department: Engineering\n");
            writer.write("Salary: $95,000\n");
        }

        // Modern approach - text block with traditional FileWriter
        try (FileWriter writer = new FileWriter(modernFile)) {
            String content = """
                Employee Report
                ================
                Generated: %s
                
                Name: Alice Johnson
                Department: Engineering
                Salary: $95,000
                """.formatted(LocalDateTime.now().format(DATE_FORMAT));

            writer.write(content);
        }

        System.out.println("Traditional approach: " + traditionalFile);
        System.out.println("With text blocks: " + modernFile);
        System.out.println("Both create identical files, but text blocks are much cleaner!\n");
    }

    /**
     * Exercise 4: Append to existing files
     */
    public void demonstrateAppending() throws IOException {
        System.out.println("4. APPENDING TO FILES:");
        System.out.println("----------------------");
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(LOG_FILE),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            
            writer.write("[" + LocalDateTime.now().format(DATE_FORMAT) + "] ");
            writer.write("Application started");
            writer.newLine();
            
            writer.write("[" + LocalDateTime.now().format(DATE_FORMAT) + "] ");
            writer.write("Employee report generated");
            writer.newLine();
            
            writer.write("[" + LocalDateTime.now().format(DATE_FORMAT) + "] ");
            writer.write("5 employees processed");
            writer.newLine();
        }
        
        System.out.println("Appended log entries to: " + LOG_FILE);
        System.out.println("FileWriter(filename, true) enables append mode\n");
    }
    
    /**
     * Exercise 5: Write formatted CSV data
     */
    public void writeFormattedData() throws IOException {
        System.out.println("5. WRITING FORMATTED DATA (CSV):");
        System.out.println("--------------------------------");
        
        String csvFile = OUTPUT_DIR + "/employees.csv";
        List<Employee> employees = createSampleEmployees();
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFile))) {
            // Write CSV header
            writer.write("ID,Name,Department,Salary,Bonus");
            writer.newLine();
            
            // Write employee data
            int id = 1001;
            for (Employee emp : employees) {
                double bonus = emp.salary() * 0.10; // 10% bonus
                
                // Format: ID,Name,Department,Salary,Bonus
                writer.write(String.format("%d,\"%s\",\"%s\",%.2f,%.2f",
                    id++,
                    emp.name(),
                    emp.department(),
                    emp.salary(),
                    bonus
                ));
                writer.newLine();
            }
        }
        
        System.out.println("Created CSV file: " + csvFile);
        System.out.println("CSV format suitable for Excel/spreadsheet import\n");
    }
    

    /**
     * Create sample employees for demonstrations
     */
    private List<Employee> createSampleEmployees() {
        return List.of(
            new Employee("Alice Johnson", "Engineering", 95000),
            new Employee("Bob Smith", "Marketing", 87000),
            new Employee("Carol Davis", "Sales", 92000),
            new Employee("David Wilson", "HR", 65000),
            new Employee("Eva Brown", "Finance", 78000)
        );
    }

    /**
     * Simple Employee record for demonstrations
     */
    record Employee(String name, String department, double salary) {
        public Employee {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (department == null || department.trim().isEmpty()) {
                throw new IllegalArgumentException("Department cannot be empty");
            }
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
        }
    }
}