package com.oreilly.javaskills.oop.records;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeePojoTest {
    // Generate tests for the EmployeePojo class here.
    @Test
    void testConstructorAndGetters() {
        // Given
        String name = "John Doe";
        int id = 123;
        double salary = 75000.0;

        // When
        EmployeePojo employee = new EmployeePojo(name, id, salary);

        // Then
        assertEquals(name, employee.name());
        assertEquals(id, employee.id());
        assertEquals(salary, employee.salary());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        EmployeePojo employee1 = new EmployeePojo("Alice Johnson", 1001, 75000.0);
        EmployeePojo employee2 = new EmployeePojo("Alice Johnson", 1001, 75000.0);
        EmployeePojo employee3 = new EmployeePojo("Bob Smith", 1002, 80000.0);

        // Then
        assertThat(employee1)
                .isNotNull()
                .isEqualTo(employee2)
                .isNotEqualTo(employee3);

        assertThat(employee1.hashCode())
                .isEqualTo(employee2.hashCode())
                .isNotEqualTo(employee3.hashCode());
    }

    @Test
    void testToString() {
        // Given
        var employee = new EmployeePojo("Charlie Wilson", 1003, 85000.0);

        // When
        String result = employee.toString();

        // Then
        assertThat(result)
                .isNotEmpty()
                .contains("Charlie Wilson")
                .contains("1003")
                .contains("85000.0");
    }

    @SuppressWarnings("unused")
    @Test
    void testImmutability() {
        // Given
        EmployeePojo employee = new EmployeePojo("Diana Prince", 1004, 90000.0);

        // No way to change the fields once they're set
        // See the next test for another way to test immutability
    }

    @Test
    void fieldsAreFinal() {
        Field[] fields = EmployeePojo.class.getDeclaredFields(); // Reflection API

        assertThat(Arrays.stream(fields).map(Field::getModifiers))
                .allMatch(Modifier::isFinal);
    }
}