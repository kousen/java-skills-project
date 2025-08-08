package com.oreilly.javaskills.oop.exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("VarKeywordExercise Tests")
class VarKeywordExerciseTest {

    @Test
    @DisplayName("Exercise runs without errors and produces expected output")
    void exerciseRunsSuccessfully() {
        // Capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // This should run without throwing any exceptions
            VarKeywordExercise.main(new String[]{});

            String output = outputStream.toString();
            String normalized = output.replace("\r\n", "\n");

            // Basic sanity: non-empty output and mentions of 'var'
            assertThat(normalized).isNotBlank();
            assertThat(normalized.toLowerCase()).contains("var");

            // Looser, case-insensitive checks to accommodate revised wording
            assertThat(normalized.toLowerCase()).contains("exercise"); // banner/title presence

            // Section headers (allow numbering and revised titles)
            assertThat(containsAnyIgnoreCase(normalized,
                    "1. basic var", "1) basic var", "basics with var", "var basics")).isTrue();
            assertThat(containsAnyIgnoreCase(normalized,
                    "2. var with collections", "collections with var", "collections")).isTrue();
            assertThat(containsAnyIgnoreCase(normalized,
                    "3. var with complex types", "complex types", "nested generics", "complex generics")).isTrue();
            assertThat(containsAnyIgnoreCase(normalized,
                    "4. your exercise tasks", "exercise tasks", "challenges", "tasks")).isTrue();

            // Start/end banners (tolerate formatting changes)
            assertThat(Pattern.compile("=+\\s*var\\s*keyword\\s*exercise\\s*=+", Pattern.CASE_INSENSITIVE)
                    .matcher(normalized).find()).isTrue();
            assertThat(Pattern.compile("=+\\s*exercise\\s*complete!?\\s*=+", Pattern.CASE_INSENSITIVE)
                    .matcher(normalized).find()).isTrue();

            // Demonstrated concepts (student exercise format - simpler checks)
            assertThat(containsAnyIgnoreCase(normalized,
                    "var eliminates repetition", "reduces verbosity", "less repetition", "avoids repetition")).isTrue();
            assertThat(containsAnyIgnoreCase(normalized,
                    "much cleaner with var", "cleaner with var", "more concise with var")).isTrue();
            
            // Verify basic output that should be present
            assertThat(normalized).contains("Alice Johnson");
            assertThat(normalized).contains("employees list has 2 items");
            assertThat(normalized).contains("department map has 1 entries");

            // Exercise tasks: ensure at least three completed items (✓ or [x] style)
            long completedCount = normalized.lines()
                    .filter(line -> line.trim().matches("^(?:[✓✔]\\s|\\[x]\\s).+"))
                    .count();
            assertThat(completedCount).isGreaterThanOrEqualTo(3);

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("var keyword demonstrates type inference correctly")
    void varKeywordDemonstratesTypeInference() {
        // Test that var infers types correctly
        var name = "Alice Johnson";
        var hireDate = LocalDate.of(2020, 1, 15);
        var employees = new ArrayList<String>();
        var departmentMap = new HashMap<String, Integer>();

        // Verify inferred types
        assertThat(name).isInstanceOf(String.class);
        assertThat(hireDate).isInstanceOf(LocalDate.class);
        assertThat(employees).isInstanceOf(ArrayList.class);
        assertThat(departmentMap).isInstanceOf(HashMap.class);

        // Verify the values work as expected
        assertThat(name).isEqualTo("Alice Johnson");
        assertThat(hireDate.getYear()).isEqualTo(2020);
        assertThat(employees).isEmpty();
        assertThat(departmentMap).isEmpty();
    }

    @Test
    @DisplayName("var works with complex generic types")
    void varWorksWithComplexGenerics() {
        // Test var with nested generics
        var complexMap = new HashMap<String, ArrayList<Integer>>();

        var numbers = new ArrayList<Integer>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);

        complexMap.put("primes", numbers);

        // Verify type inference worked correctly
        assertThat(complexMap).isInstanceOf(HashMap.class);
        assertThat(complexMap.get("primes")).hasSize(3);
        assertThat(complexMap.get("primes")).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("var in loops works correctly")
    void varInLoopsWorksCorrectly() {
        var testMap = new HashMap<String, Integer>();
        testMap.put("one", 1);
        testMap.put("two", 2);
        testMap.put("three", 3);

        var sum = 0;
        for (var entry : testMap.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            assertThat(key).isInstanceOf(String.class);
            assertThat(value).isInstanceOf(Integer.class);

            sum += value;
        }

        assertThat(sum).isEqualTo(6);
    }

    // Helper for flexible, case-insensitive matching of any expected phrase
    private static boolean containsAnyIgnoreCase(String haystack, String... needles) {
        String lower = haystack.toLowerCase();
        for (String needle : needles) {
            if (lower.contains(needle.toLowerCase())) return true;
        }
        return false;
    }
}