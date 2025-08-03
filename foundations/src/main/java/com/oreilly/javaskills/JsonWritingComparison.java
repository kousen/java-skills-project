package com.oreilly.javaskills;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

/**
 * Demonstration of different JSON writing approaches
 * Shows the code clarity and conciseness of each method
 */
public class JsonWritingComparison {
    
    public static void main(String[] args) {
        System.out.println("=== JSON Writing Approaches Comparison ===\n");
        
        // Create sample data
        List<EmployeeFileWriter.Employee> employees = List.of(
            new EmployeeFileWriter.Employee("Alice Johnson", 1001, 95000, LocalDate.of(2020, 3, 15)),
            new EmployeeFileWriter.Employee("Bob Smith", 1002, 87000, LocalDate.of(2019, 7, 22))
        );
        
        String outputDir = "comparison-output";
        
        try {
            // Create output directory
            Files.createDirectories(Paths.get(outputDir));
            
            // Run all methods
            System.out.println("1. ORIGINAL MANUAL APPROACH:");
            System.out.println("   - Manually write '[', each object, commas, and ']'");
            System.out.println("   - Track whether to add comma with loop index");
            System.out.println("   - Multiple write operations\n");
            
            System.out.println("2. STRING.JOIN() APPROACH:");
            System.out.println("   - Collect all JSON strings into a List");
            System.out.println("   - Use String.join() with comma delimiter");
            System.out.println("   - Manually add brackets");
            EmployeeFileWriter.writeEmployeesToJsonWithJoin(employees, outputDir);
            
            System.out.println("\n3. COLLECTORS.JOINING() APPROACH:");
            System.out.println("   - Stream directly to joining collector");
            System.out.println("   - Prefix and suffix handle brackets automatically");
            System.out.println("   - Most concise and elegant");
            EmployeeFileWriter.writeEmployeesToJsonWithCollectors(employees, outputDir);
            
            System.out.println("\n4. STRINGJOINER APPROACH:");
            System.out.println("   - Create StringJoiner with delimiter, prefix, suffix");
            System.out.println("   - Add each element in a loop");
            System.out.println("   - Most flexible for complex scenarios");
            EmployeeFileWriter.writeEmployeesToJsonWithStringJoiner(employees, outputDir);
            
            // Show file sizes (they should all be identical)
            System.out.println("\n=== File Sizes (all should be identical) ===");
            showFileInfo(outputDir + "/employees_join.json");
            showFileInfo(outputDir + "/employees_collectors.json");
            showFileInfo(outputDir + "/employees_stringjoiner.json");
            
            // Show one file's content
            System.out.println("\n=== Sample Output ===");
            String content = Files.readString(Paths.get(outputDir + "/employees_collectors.json"));
            System.out.println(content);
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void showFileInfo(String filename) throws IOException {
        long size = Files.size(Paths.get(filename));
        System.out.printf("%-30s: %d bytes%n", filename, size);
    }
}