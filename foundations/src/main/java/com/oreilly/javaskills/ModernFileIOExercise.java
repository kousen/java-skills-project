package com.oreilly.javaskills;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Try It Out Exercise: Modern File I/O with NIO.2
 * <p>
 * This exercise demonstrates modern file operations using java.nio.file:
 * - Path and Files classes for cleaner code
 * - One-line file reading and writing
 * - Stream-based file processing
 * - File operations (copy, move, delete)
 * - Directory operations
 * - File attributes and metadata
 */
public class ModernFileIOExercise {
    
    private static final Path OUTPUT_DIR = Paths.get("modern-output");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void main(String[] args) {
        var exercise = new ModernFileIOExercise();
        
        System.out.println("=== Modern File I/O Exercise: NIO.2 API ===\n");
        
        try {
            // Exercise 1: Basic file operations
            exercise.demonstrateBasicOperations();
            
            // Exercise 2: One-line file operations
            exercise.demonstrateOneLineOperations();
            
            // Exercise 3: Stream-based processing
            exercise.demonstrateStreamProcessing();
            
            // Exercise 4: File operations
            exercise.demonstrateFileOperations();
            
            // Exercise 5: Directory operations
            exercise.demonstrateDirectoryOperations();
            
        } catch (IOException e) {
            System.err.println("Error during file operations: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Exercise 1: Basic file and directory operations
     */
    public void demonstrateBasicOperations() throws IOException {
        System.out.println("1. BASIC NIO.2 OPERATIONS:");
        System.out.println("--------------------------");
        
        // Create directory (including parents if needed)
        Files.createDirectories(OUTPUT_DIR);
        System.out.println("Created directory: " + OUTPUT_DIR);
        
        // Create a simple file
        Path simplePath = OUTPUT_DIR.resolve("simple.txt");
        if (!Files.exists(simplePath)) {
            Files.createFile(simplePath);
            System.out.println("Created file: " + simplePath);
        }
        
        // Check file properties
        System.out.println("File exists: " + Files.exists(simplePath));
        System.out.println("Is readable: " + Files.isReadable(simplePath));
        System.out.println("Is writable: " + Files.isWritable(simplePath));
        System.out.println();
    }
    
    /**
     * Exercise 2: One-line file reading and writing
     */
    public void demonstrateOneLineOperations() throws IOException {
        System.out.println("2. ONE-LINE FILE OPERATIONS:");
        System.out.println("----------------------------");
        
        Path employeePath = OUTPUT_DIR.resolve("employees.txt");
        
        // Write string to file - ONE LINE!
        String content = """
            Employee Directory
            ==================
            Alice Johnson - Engineering - $95,000
            Bob Smith - Marketing - $87,000
            Carol Davis - Sales - $92,000
            """;
        Files.writeString(employeePath, content);
        System.out.println("Wrote file with Files.writeString() - just one line!");
        
        // Read entire file as string - ONE LINE!
        String readContent = Files.readString(employeePath);
        System.out.println("\nRead file content:");
        System.out.println(readContent.substring(0, 50) + "...");
        
        // Write list of strings - ONE LINE!
        Path listPath = OUTPUT_DIR.resolve("employee_list.txt");
        List<String> employees = List.of(
            "David Wilson - HR - $65,000",
            "Eva Brown - Finance - $78,000",
            "Frank Miller - IT - $83,000"
        );
        Files.write(listPath, employees);
        System.out.println("\nWrote list of employees with Files.write()");
        
        // Read all lines - ONE LINE!
        List<String> readLines = Files.readAllLines(listPath);
        System.out.println("Read " + readLines.size() + " lines with Files.readAllLines()");
        System.out.println();
    }
    
    /**
     * Exercise 3: Stream-based file processing
     */
    public void demonstrateStreamProcessing() throws IOException {
        System.out.println("3. STREAM-BASED FILE PROCESSING:");
        System.out.println("--------------------------------");
        
        // Create a larger employee file
        Path largePath = OUTPUT_DIR.resolve("all_employees.csv");
        List<String> allEmployees = List.of(
            "ID,Name,Department,Salary",
            "1001,Alice Johnson,Engineering,95000",
            "1002,Bob Smith,Marketing,87000",
            "1003,Carol Davis,Engineering,92000",
            "1004,David Wilson,HR,65000",
            "1005,Eva Brown,Engineering,88000",
            "1006,Frank Miller,IT,83000",
            "1007,Grace Lee,Marketing,71000",
            "1008,Henry Taylor,Sales,69000"
        );
        Files.write(largePath, allEmployees);
        
        // Process file with streams - filter Engineering employees
        System.out.println("Engineering employees (salary > 90k):");
        try (Stream<String> lines = Files.lines(largePath)) {
            lines.skip(1)  // Skip header
                 .filter(line -> line.contains("Engineering"))
                 .map(line -> line.split(","))
                 .filter(parts -> Integer.parseInt(parts[3]) > 90000)
                 .forEach(parts -> System.out.println("  " + parts[1] + " - $" + parts[3]));
        }
        
        // Calculate average salary using streams
        try (Stream<String> lines = Files.lines(largePath)) {
            double avgSalary = lines.skip(1)
                .map(line -> line.split(",")[3])
                .mapToInt(Integer::parseInt)
                .average()
                .orElse(0.0);
            System.out.printf("\nAverage salary: $%,.2f%n", avgSalary);
        }
        System.out.println();
    }
    
    /**
     * Exercise 4: File operations (copy, move, delete)
     */
    public void demonstrateFileOperations() throws IOException {
        System.out.println("4. FILE OPERATIONS:");
        System.out.println("-------------------");
        
        Path originalPath = OUTPUT_DIR.resolve("original.txt");
        Files.writeString(originalPath, "This is the original file content.");
        
        // Copy file
        Path copyPath = OUTPUT_DIR.resolve("copy.txt");
        Files.copy(originalPath, copyPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Copied file to: " + copyPath);
        
        // Move file
        Path movedPath = OUTPUT_DIR.resolve("moved.txt");
        Files.move(copyPath, movedPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Moved file to: " + movedPath);
        
        // Get file attributes
        BasicFileAttributes attrs = Files.readAttributes(originalPath, BasicFileAttributes.class);
        System.out.println("\nFile attributes for " + originalPath.getFileName() + ":");
        System.out.println("  Size: " + attrs.size() + " bytes");
        System.out.println("  Created: " + attrs.creationTime());
        System.out.println("  Modified: " + attrs.lastModifiedTime());
        
        // Delete files
        Files.deleteIfExists(movedPath);
        System.out.println("\nDeleted: " + movedPath);
        System.out.println();
    }
    
    /**
     * Exercise 5: Directory operations
     */
    public void demonstrateDirectoryOperations() throws IOException {
        System.out.println("5. DIRECTORY OPERATIONS:");
        System.out.println("------------------------");
        
        // Create nested directories
        Path reportsDir = OUTPUT_DIR.resolve("reports/2024/january");
        Files.createDirectories(reportsDir);
        System.out.println("Created nested directories: " + reportsDir);
        
        // Create some files in different directories
        Files.writeString(OUTPUT_DIR.resolve("root.txt"), "Root file");
        Files.writeString(OUTPUT_DIR.resolve("reports/summary.txt"), "Summary");
        Files.writeString(reportsDir.resolve("report1.txt"), "January Report 1");
        Files.writeString(reportsDir.resolve("report2.txt"), "January Report 2");
        
        // List files in directory
        System.out.println("\nFiles in output directory:");
        try (Stream<Path> files = Files.list(OUTPUT_DIR)) {
            files.filter(Files::isRegularFile)
                 .forEach(file -> System.out.println("  " + file.getFileName()));
        }
        
        // Walk entire directory tree
        System.out.println("\nAll files in directory tree:");
        try (Stream<Path> walk = Files.walk(OUTPUT_DIR)) {
            walk.filter(Files::isRegularFile)
                .map(OUTPUT_DIR::relativize)
                .forEach(file -> System.out.println("  " + file));
        }
        
        // Find specific files
        System.out.println("\nAll .txt files containing 'report':");
        try (Stream<Path> found = Files.find(OUTPUT_DIR, 10,
                (path, attrs) -> path.toString().toLowerCase().contains("report") 
                              && path.toString().endsWith(".txt"))) {
            found.forEach(file -> System.out.println("  " + OUTPUT_DIR.relativize(file)));
        }
        System.out.println();
    }
    
    /**
     * Bonus: Compare with traditional approach
     */
    public void compareApproaches() throws IOException {
        System.out.println("COMPARISON: Traditional vs Modern");
        System.out.println("---------------------------------");
        
        Path comparisonFile = OUTPUT_DIR.resolve("comparison.txt");
        
        System.out.println("Traditional approach:");
        System.out.println("  FileWriter fw = new FileWriter(\"file.txt\");");
        System.out.println("  BufferedWriter bw = new BufferedWriter(fw);");
        System.out.println("  try { bw.write(data); }");
        System.out.println("  finally { bw.close(); }");
        
        System.out.println("\nModern approach:");
        System.out.println("  Files.writeString(path, data);  // That's it!");
        
        // Actually demonstrate it
        String data = "Modern file I/O is so much simpler!";
        Files.writeString(comparisonFile, data);
        
        System.out.println("\nResult: " + Files.readString(comparisonFile));
    }
    
    /**
     * Challenge: Create your own NIO.2 scenario!
     * ´
     * Ideas:
     * - Watch a directory for changes
     * - Create a file backup system
     * - Process all files matching a pattern
     * - Build a directory comparison tool
     * - Create a file synchronization utility
     */
    public void customModernIO() throws IOException {
        // TODO: Implement your own modern file I/O scenario
        System.out.println("\nChallenge: Create your own NIO.2 file operations!");
    }
}