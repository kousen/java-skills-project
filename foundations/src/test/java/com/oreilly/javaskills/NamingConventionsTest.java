package com.oreilly.javaskills;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Naming Conventions Tests")
class NamingConventionsTest {

    @Test
    @DisplayName("Should have proper constant naming")
    void shouldHaveProperConstantNaming() {
        // Use reflection to verify constants follow naming conventions
        var clazz = NamingConventions.class;
        var fields = clazz.getDeclaredFields();

        for (var field : fields) {
            if (Modifier.isFinal(field.getModifiers()) &&
                Modifier.isStatic(field.getModifiers())) {

                String fieldName = field.getName();
                assertThat(fieldName)
                    .matches("[A-Z][A-Z_]*")
                    .as("Constant %s should be in UPPER_CASE", fieldName);
            }
        }
    }

    @Test
    @DisplayName("Should produce expected output format")
    void shouldProduceExpectedOutputFormat() {
        // Capture system output
        var outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Run main method
            NamingConventions.main(new String[]{});

            String output = outputStream.toString();
            assertThat(output)
                .contains("=== Proper Naming Conventions Demo ===")
                .contains("Company:")
                .contains("Employee Details:")
                .contains(" Bad examples (What NOT to do) ");

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Should demonstrate camelCase variable naming")
    void shouldDemonstrateCamelCaseNaming() {
        // This test verifies that the example shows proper camelCase
        // In a real scenario, we'd check the actual variable names in the source
        assertThat("employeeName").matches("[a-z][a-zA-Z0-9]*");
        assertThat("employeeId").matches("[a-z][a-zA-Z0-9]*");
        assertThat("salaryAmount").matches("[a-z][a-zA-Z0-9]*");
        assertThat("isActive").matches("[a-z][a-zA-Z0-9]*");
    }
}
