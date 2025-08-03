package com.oreilly.javaskills;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class to verify different JSON writing approaches produce equivalent output
 */
class EmployeeFileWriterTest {
    
    private static final String TEST_DIR = "test-employee-data";
    private List<EmployeeFileWriter.Employee> testEmployees;
    
    @BeforeEach
    void setUp() throws IOException {
        // Create test directory
        Path testPath = Paths.get(TEST_DIR);
        if (!Files.exists(testPath)) {
            Files.createDirectories(testPath);
        }
        
        // Create test employees
        testEmployees = List.of(
            new EmployeeFileWriter.Employee("Alice Johnson", 1001, 95000, LocalDate.of(2020, 3, 15)),
            new EmployeeFileWriter.Employee("Bob Smith", 1002, 87000, LocalDate.of(2019, 7, 22)),
            new EmployeeFileWriter.Employee("Carol Davis", 1003, 92000, LocalDate.of(2021, 1, 10))
        );
    }
    
    @AfterEach
    void tearDown() throws IOException {
        // Clean up test files
        Path testPath = Paths.get(TEST_DIR);
        if (Files.exists(testPath)) {
            try (Stream<Path> pathStream = Files.walk(testPath)) {
                pathStream.sorted(Comparator.reverseOrder()) // Delete files before directories
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            // Ignore
                        }
                    });
            }
        }
    }
    
    @Test
    @DisplayName("All JSON writing methods should produce valid JSON with same data")
    void testAllJsonWritingMethodsProduceSameData() throws IOException {
        // Write JSON using all different methods
        writeJsonManual(testEmployees);
        
        // Use the actual public methods from EmployeeFileWriter with test directory
        EmployeeFileWriter.writeEmployeesToJsonWithJoin(testEmployees, TEST_DIR);
        EmployeeFileWriter.writeEmployeesToJsonWithCollectors(testEmployees, TEST_DIR);
        EmployeeFileWriter.writeEmployeesToJsonWithStringJoiner(testEmployees, TEST_DIR);
        
        // Read the generated files
        String jsonJoin = Files.readString(Paths.get(TEST_DIR + "/employees_join.json"));
        String jsonCollectors = Files.readString(Paths.get(TEST_DIR + "/employees_collectors.json"));
        String jsonStringJoiner = Files.readString(Paths.get(TEST_DIR + "/employees_stringjoiner.json"));
        
        // All methods should produce non-empty JSON
        assertThat(jsonJoin).isNotEmpty();
        assertThat(jsonCollectors).isNotEmpty();
        assertThat(jsonStringJoiner).isNotEmpty();
        
        // All should start with [ and end with ]
        assertThat(jsonJoin.trim()).startsWith("[").endsWith("]");
        assertThat(jsonCollectors.trim()).startsWith("[").endsWith("]");
        assertThat(jsonStringJoiner.trim()).startsWith("[").endsWith("]");
        
        // All should contain the same employee data
        for (var emp : testEmployees) {
            assertThat(jsonJoin).contains("\"id\": " + emp.id());
            assertThat(jsonJoin).contains("\"name\": \"" + emp.name() + "\"");
            
            assertThat(jsonCollectors).contains("\"id\": " + emp.id());
            assertThat(jsonCollectors).contains("\"name\": \"" + emp.name() + "\"");
            
            assertThat(jsonStringJoiner).contains("\"id\": " + emp.id());
            assertThat(jsonStringJoiner).contains("\"name\": \"" + emp.name() + "\"");
        }
    }
    
    @Test
    @DisplayName("String.join approach should handle empty list correctly")
    void testStringJoinWithEmptyList() throws IOException {
        List<EmployeeFileWriter.Employee> emptyList = List.of();
        
        // Use the actual public method with test directory
        EmployeeFileWriter.writeEmployeesToJsonWithJoin(emptyList, TEST_DIR);
        
        // Read the generated file
        String json = Files.readString(Paths.get(TEST_DIR + "/employees_join.json"));
        // The String.join with empty list creates "[\n\n]"
        assertThat(json).startsWith("[").endsWith("]");
        assertThat(json.replace("\n", "").replace(" ", "")).isEqualTo("[]");
    }
    
    @Test
    @DisplayName("Collectors.joining approach should handle single employee")
    void testCollectorsJoiningWithSingleEmployee() throws IOException {
        List<EmployeeFileWriter.Employee> singleEmployee = List.of(
            new EmployeeFileWriter.Employee("Test User", 9999, 50000, LocalDate.now())
        );
        
        // Use the actual public method with test directory
        EmployeeFileWriter.writeEmployeesToJsonWithCollectors(singleEmployee, TEST_DIR);
        
        // Read the generated file
        String json = Files.readString(Paths.get(TEST_DIR + "/employees_collectors.json"));
        
        assertThat(json).contains("\"name\": \"Test User\"");
        assertThat(json).contains("\"id\": 9999");
        assertThat(json).doesNotContain(",\n  {"); // No comma between objects
    }
    
    // Helper method for manual JSON writing (original approach)
    private void writeJsonManual(List<EmployeeFileWriter.Employee> employees) throws IOException {
        String jsonFile = TEST_DIR + "/employees_manual.json";
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(jsonFile))) {
            writer.write("[");
            writer.newLine();

            for (int i = 0; i < employees.size(); i++) {
                var emp = employees.get(i);
                String jsonEmployee = """
                      {
                        "id": %d,
                        "name": "%s",
                        "salary": %.2f,
                        "hireDate": "%s"
                      }""".formatted(emp.id(), emp.name(), emp.salary(), 
                                   emp.hireDate());
                
                writer.write(jsonEmployee);
                if (i < employees.size() - 1) {
                    writer.write(",");
                }
                writer.newLine();
            }

            writer.write("]");
        }
    }
}