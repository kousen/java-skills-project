package com.oreilly.javaskills;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class EmployeeFileWriter {

    private static final String DATA_DIR = "employee-data";
    private static final String CSV_FILE = DATA_DIR + "/employees.csv";
    private static final String JSON_FILE = DATA_DIR + "/employees.json";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        System.out.println("=== Employee File I/O Operations ===");

        try {
            // Create sample employees
            List<Employee> employees = createSampleEmployees();

            // Ensure data directory exists
            createDataDirectory();

            // Write to CSV using different approaches
            writeEmployeesToCsv(employees);

            // Write to JSON-like format
            writeEmployeesToJson(employees);

            // Read employees back from file
            List<Employee> loadedEmployees = readEmployeesFromCsv();

            // Display loaded data
            displayEmployees(loadedEmployees);

            // File operations examples
            demonstrateFileOperations();

        } catch (Exception e) {
            System.err.println("Error during file operations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Employee> createSampleEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Alice Johnson", 1001, 95000, LocalDate.of(2020, 3, 15)));
        employees.add(new Employee("Bob Smith", 1002, 87000, LocalDate.of(2019, 7, 22)));
        employees.add(new Employee("Carol Davis", 1003, 92000, LocalDate.of(2021, 1, 10)));
        employees.add(new Employee("David Wilson", 1004, 65000, LocalDate.of(2022, 5, 3)));
        employees.add(new Employee("Eva Brown", 1005, 68000, LocalDate.of(2020, 11, 18)));
        return employees;
    }

    private static void createDataDirectory() throws IOException {
        Path dataPath = Paths.get(DATA_DIR);
        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
            System.out.println("Created data directory: " + DATA_DIR);
        }
    }

    private static void writeEmployeesToCsv(List<Employee> employees) throws IOException {
        System.out.println("\nWriting employees to CSV file...");

        // Using try-with-resources for automatic resource management
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE))) {
            // Write header
            writer.write("ID,Name,Salary,HireDate");
            writer.newLine();

            // Write employee data
            for (Employee emp : employees) {
                writer.write(String.format("%d,%s,%.2f,%s",
                        emp.id(), emp.name(), emp.salary(),
                        emp.hireDate().format(DATE_FORMAT)));
                writer.newLine();
            }
        }

        System.out.println("CSV file written: " + CSV_FILE);

        // Alternative approach using FileWriter
        writeEmployeesAlternative(employees);
    }

    private static void writeEmployeesAlternative(List<Employee> employees) throws IOException {
        String altFile = DATA_DIR + "/employees_alt.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(altFile))) {
            writer.println("ID,Name,Salary,HireDate");

            employees.forEach(emp ->
                    writer.printf("%d,%s,%.2f,%s%n",
                            emp.id(), emp.name(), emp.salary(),
                            emp.hireDate().format(DATE_FORMAT))
            );
        }

        System.out.println("Alternative CSV file written: " + altFile);
    }

    private static void writeEmployeesToJson(List<Employee> employees) throws IOException {
        System.out.println("\nWriting employees to JSON-like file...");

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(JSON_FILE))) {
            writer.write("[");
            writer.newLine();

            for (int i = 0; i < employees.size(); i++) {
                Employee emp = employees.get(i);
                
                // Use text block for cleaner JSON formatting
                String jsonEmployee = """
                      {
                        "id": %d,
                        "name": "%s",
                        "salary": %.2f,
                        "hireDate": "%s"
                      }""".formatted(emp.id(), emp.name(), emp.salary(), 
                                   emp.hireDate().format(DATE_FORMAT));
                
                writer.write(jsonEmployee);
                if (i < employees.size() - 1) {
                    writer.write(",");
                }
                writer.newLine();
            }

            writer.write("]");
        }

        System.out.println("JSON file written: " + JSON_FILE);
    }

    private static List<Employee> readEmployeesFromCsv() throws IOException {
        System.out.println("\nReading employees from CSV file...");

        List<Employee> employees = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            reader.readLine(); // Skip header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double salary = Double.parseDouble(parts[2]);
                    LocalDate hireDate = LocalDate.parse(parts[3], DATE_FORMAT);

                    employees.add(new Employee(name, id, salary, hireDate));
                }
            }
        }

        System.out.println("Loaded " + employees.size() + " employees from CSV");
        return employees;
    }

    private static void displayEmployees(List<Employee> employees) {
        System.out.println("\n=== Loaded Employee Data ===");
        employees.forEach(emp ->
                System.out.printf("ID: %d, Name: %-15s, Salary: $%,.2f, Hired: %s%n",
                        emp.id(), emp.name(), emp.salary(),
                        emp.hireDate().format(DATE_FORMAT))
        );
    }

    private static void demonstrateFileOperations() throws IOException {
        System.out.println("\n=== File Operations Demo ===");

        Path csvPath = Paths.get(CSV_FILE);

        // File information
        System.out.println("File exists: " + Files.exists(csvPath));
        System.out.println("File size: " + Files.size(csvPath) + " bytes");
        System.out.println("Last modified: " + Files.getLastModifiedTime(csvPath));
        System.out.println("Readable: " + Files.isReadable(csvPath));
        System.out.println("Writable: " + Files.isWritable(csvPath));

        // Using Apache Commons IO
        String csvContent = FileUtils.readFileToString(csvPath.toFile(), "UTF-8");
        System.out.println("\nFile content (first 100 chars):");
        System.out.println(csvContent.substring(0, Math.min(100, csvContent.length())) + "...");

        // Copy file
        Path backupPath = Paths.get(DATA_DIR + "/employees_backup.csv");
        Files.copy(csvPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Backup created: " + backupPath);
    }

    record Employee(String name, int id, double salary, LocalDate hireDate) {
        public Employee {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive");
            }
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
            if (hireDate == null) {
                throw new IllegalArgumentException("Hire date cannot be null");
            }
        }
    }
}