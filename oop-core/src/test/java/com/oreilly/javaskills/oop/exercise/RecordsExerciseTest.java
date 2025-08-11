package com.oreilly.javaskills.oop.exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RecordsExercise Tests")
class RecordsExerciseTest {

    @Test
    @DisplayName("Exercise runs without errors and produces expected output")
    void exerciseRunsSuccessfully() {
        // Capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // This should run without throwing any exceptions
            RecordsExercise.main(new String[]{});
            
            String output = outputStream.toString();
            String normalized = output.replace("\r\n", "\n");

            // Verify key sections are present
            assertThat(normalized)
                .contains("=== Java Records Exercise ===")
                .contains("1. BASIC RECORDS VS TRADITIONAL POJOS:")
                .contains("2. RECORD FEATURES (ALL AUTOMATIC):")
                .contains("3. CUSTOMIZED RECORDS:")
                .contains("4. YOUR EXERCISE TASKS:")
                .contains("=== Exercise Complete! ===")
                // Verify it demonstrates key record concepts
                .contains("BEFORE - Traditional POJO")
                .contains("AFTER - Using Records")
                .contains("Accessor methods (no 'get' prefix):")
                .contains("Automatic toString():")
                .contains("Automatic equals()")
                .contains("Automatic hashCode()")
                // Verify record instances and features
                .contains("Alice: EmployeeRecord[name=Alice Johnson")
                .contains("Bob: EmployeeRecord[name=Bob Smith")
                .contains("employee1.equals(employee2): true")
                .contains("Hash codes equal: true")
                // Verify validation and customization examples
                .contains("✓ Valid employee created:")
                .contains("✓ Validation prevents invalid data")
                .contains("Formatted info:")
                .contains("Qualifies for bonus:")
                .contains("Records support immutable updates")
                // Verify exercise tasks are present
                .contains("✓ Book record task completed")
                .contains("✓ Validated Person record task completed")
                .contains("✓ Nested records task completed")
                .contains("✓ Custom methods task completed");
            
            // Additional chained assertions for specific concepts
            assertThat(normalized)
                .contains("BEFORE - Traditional POJO")
                .contains("AFTER - Using Records")
                .contains("Accessor methods (no 'get' prefix):")
                .contains("Automatic toString():")
                .contains("Automatic equals()")
                .contains("Automatic hashCode()")
                .contains("Alice: EmployeeRecord[name=Alice Johnson")
                .contains("Bob: EmployeeRecord[name=Bob Smith")
                .contains("employee1.equals(employee2): true")
                .contains("Hash codes equal: true")
                .contains("✓ Valid employee created:")
                .contains("✓ Validation prevents invalid data")
                .contains("Formatted info:")
                .contains("Qualifies for bonus:")
                .contains("Records support immutable updates")
                .contains("✓ Book record task completed")
                .contains("✓ Validated Person record task completed")
                .contains("✓ Nested records task completed")
                .contains("✓ Custom methods task completed");
            
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Record instances demonstrate expected behavior")
    void recordInstancesBehaveCorrectly() {
        // Access the nested record classes for testing
        var employee1 = new RecordsExercise.EmployeeRecord("John Doe", 100, 50000);
        var employee2 = new RecordsExercise.EmployeeRecord("John Doe", 100, 50000);
        var employee3 = new RecordsExercise.EmployeeRecord("Jane Smith", 101, 60000);

        // Test accessor methods (no "get" prefix)
        assertThat(employee1.name()).isEqualTo("John Doe");
        assertThat(employee1.id()).isEqualTo(100);
        assertThat(employee1.salary()).isEqualTo(50000);

        // Test automatic equals (value-based equality)
        assertThat(employee1)
            .isEqualTo(employee2)
            .isNotEqualTo(employee3);

        // Test automatic hashCode (consistent with equals)
        assertThat(employee1.hashCode())
            .isEqualTo(employee2.hashCode())
            .isNotEqualTo(employee3.hashCode());

        // Test automatic toString
        String toString = employee1.toString();
        assertThat(toString)
            .contains("EmployeeRecord")
            .contains("name=John Doe")
            .contains("id=100")
            .contains("salary=50000.0");
    }

    @Test
    @DisplayName("ValidatedEmployee record enforces validation")
    void validatedEmployeeEnforcesValidation() {
        // Test valid employee creation
        var validEmployee = new RecordsExercise.ValidatedEmployee("Alice Johnson", 1001, 75000);
        assertThat(validEmployee.name()).isEqualTo("Alice Johnson");
        assertThat(validEmployee.id()).isEqualTo(1001);
        assertThat(validEmployee.salary()).isEqualTo(75000);

        // Test validation failures
        assertThatThrownBy(() -> new RecordsExercise.ValidatedEmployee(null, 1002, 50000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Name cannot be null or empty");

        assertThatThrownBy(() -> new RecordsExercise.ValidatedEmployee("", 1003, 50000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Name cannot be null or empty");

        assertThatThrownBy(() -> new RecordsExercise.ValidatedEmployee("Bob Smith", 0, 50000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ID must be positive");

        assertThatThrownBy(() -> new RecordsExercise.ValidatedEmployee("Charlie Wilson", 1004, -1000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Salary cannot be negative");
    }

    @Test
    @DisplayName("ValidatedEmployee custom methods work correctly")
    void validatedEmployeeCustomMethodsWork() {
        var employee = new RecordsExercise.ValidatedEmployee("Diana Prince", 1005, 80000);

        // Test getFormattedInfo
        String formatted = employee.getFormattedInfo();
        assertThat(formatted)
            .contains("Diana Prince")
            .contains("ID: 1005")
            .contains("$80,000.00");

        // Test qualifiesForBonus
        assertThat(employee.qualifiesForBonus()).isTrue(); // 80000 >= 70000

        var lowPaidEmployee = new RecordsExercise.ValidatedEmployee("Frank Miller", 1006, 60000);
        assertThat(lowPaidEmployee.qualifiesForBonus()).isFalse(); // 60000 < 70000

        // Test immutable update with withSalary
        var updatedEmployee = employee.withSalary(90000);
        
        // Original should be unchanged
        assertThat(employee.salary()).isEqualTo(80000);
        
        // New instance should have updated salary
        assertThat(updatedEmployee.salary()).isEqualTo(90000);
        assertThat(updatedEmployee.name()).isEqualTo("Diana Prince"); // Other fields unchanged
        assertThat(updatedEmployee.id()).isEqualTo(1005); // Other fields unchanged
        
        // Should be different instances
        assertThat(updatedEmployee).isNotSameAs(employee);
    }

    @Test
    @DisplayName("Records demonstrate immutability")
    void recordsDemonstrateImmutability() {
        var employee = new RecordsExercise.EmployeeRecord("Test Employee", 999, 55000);
        
        // Records are immutable - fields should be final
        // We can't directly test final fields, but we can verify accessors work
        assertThat(employee.name()).isEqualTo("Test Employee");
        assertThat(employee.id()).isEqualTo(999);
        assertThat(employee.salary()).isEqualTo(55000);
        
        // Creating new instance with same data should be equal but different instances
        var sameEmployee = new RecordsExercise.EmployeeRecord("Test Employee", 999, 55000);
        assertThat(employee)
            .isEqualTo(sameEmployee)
            .isNotSameAs(sameEmployee);
    }
}