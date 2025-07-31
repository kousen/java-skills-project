package com.oreilly.javaskills;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for TwoDArrayExercise.
 * Verifies that the 2D array operations produce expected results.
 */
class TwoDArrayExerciseTest {
    
    private TwoDArrayExercise exercise;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @BeforeEach
    void setUp() {
        exercise = new TwoDArrayExercise();
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outputStream.reset();
    }
    
    @Test
    @DisplayName("Basic 2D array operations should display quarterly sales")
    void testDemonstrateBasic2DArray() {
        exercise.demonstrateBasic2DArray();
        
        String output = outputStream.toString();
        
        // Verify header
        assertThat(output).contains("1. BASIC 2D ARRAY OPERATIONS:");
        assertThat(output).contains("Employee    Q1        Q2        Q3        Q4");
        
        // Verify employee data is displayed
        assertThat(output).contains("Alice");
        assertThat(output).contains("Bob");
        assertThat(output).contains("Carol");
        assertThat(output).contains("David");
        
        // Verify sales data formatting (currency)
        assertThat(output).contains("$45,000");
        assertThat(output).contains("$52,000");
        assertThat(output).contains("$68,000");
    }
    
    @Test
    @DisplayName("Sales performance analysis should calculate totals")
    void testAnalyzeSalesPerformance() {
        exercise.analyzeSalesPerformance();
        
        String output = outputStream.toString();
        
        // Verify sections
        assertThat(output).contains("2. SALES PERFORMANCE ANALYSIS:");
        assertThat(output).contains("Annual Sales by Employee:");
        assertThat(output).contains("Total Sales by Quarter:");
        
        // Verify annual totals are calculated
        assertThat(output).contains("Alice");
        assertThat(output).contains("$206,000"); // Alice's total
        assertThat(output).contains("Carol");
        assertThat(output).contains("$243,000"); // Carol's total
        
        // Verify quarterly totals
        assertThat(output).contains("Q1:");
        assertThat(output).contains("Q4:");
    }
    
    @Test
    @DisplayName("Commission calculations should apply 8% rate")
    void testCalculateCommissions() {
        exercise.calculateCommissions();
        
        String output = outputStream.toString();
        
        // Verify section and rate
        assertThat(output).contains("3. COMMISSION CALCULATIONS:");
        assertThat(output).contains("Quarterly Commission Report (8% rate):");
        
        // Verify commission calculations
        assertThat(output).contains("$3,600"); // 45000 * 0.08
        assertThat(output).contains("$4,160"); // 52000 * 0.08
        assertThat(output).contains("$5,440"); // 68000 * 0.08
        
        // Verify total column
        assertThat(output).contains("Total");
    }
    
    @Test
    @DisplayName("Top performers analysis should identify best performers")
    void testFindTopPerformers() {
        exercise.findTopPerformers();
        
        String output = outputStream.toString();
        
        // Verify sections
        assertThat(output).contains("4. TOP PERFORMERS ANALYSIS:");
        assertThat(output).contains("Top Performer by Quarter:");
        assertThat(output).contains("Overall Top Performer:");
        
        // Verify quarterly top performers
        assertThat(output).contains("Q1: Carol"); // Carol has highest Q1 sales
        assertThat(output).contains("Q4: Carol"); // Carol has highest Q4 sales
        
        // Verify overall top performer
        assertThat(output).contains("Carol with annual sales of $243,000");
    }
    
    @Test
    @DisplayName("Performance matrix should calculate growth rates")
    void testCreatePerformanceMatrix() {
        exercise.createPerformanceMatrix();
        
        String output = outputStream.toString();
        
        // Verify sections
        assertThat(output).contains("5. PERFORMANCE MATRIX - QUARTER-OVER-QUARTER GROWTH:");
        assertThat(output).contains("Quarter-over-Quarter Growth Rates:");
        assertThat(output).contains("Employee   Q2 vs Q1   Q3 vs Q2   Q4 vs Q3   Average");
        
        // Verify growth rates are calculated
        assertThat(output).contains("Alice");
        assertThat(output).contains("16%"); // Alice Q2 vs Q1: (52000-45000)/45000
        assertThat(output).contains("-8%"); // Alice Q3 vs Q2: (48000-52000)/52000
        
        // Verify consistent performers section
        assertThat(output).contains("Consistent Performers (positive growth all quarters):");
        assertThat(output).contains("• Bob"); // Bob has positive growth all quarters
        assertThat(output).contains("• Carol"); // Carol has positive growth all quarters
    }
    
    @Test
    @DisplayName("All methods should execute without errors")
    void testAllMethodsExecute() {
        // This test ensures all methods can run without throwing exceptions
        exercise.demonstrateBasic2DArray();
        exercise.analyzeSalesPerformance();
        exercise.calculateCommissions();
        exercise.findTopPerformers();
        exercise.createPerformanceMatrix();
        
        String output = outputStream.toString();
        
        // Verify all sections are present
        assertThat(output).contains("1. BASIC 2D ARRAY OPERATIONS:");
        assertThat(output).contains("2. SALES PERFORMANCE ANALYSIS:");
        assertThat(output).contains("3. COMMISSION CALCULATIONS:");
        assertThat(output).contains("4. TOP PERFORMERS ANALYSIS:");
        assertThat(output).contains("5. PERFORMANCE MATRIX");
    }
    
    @Test
    @DisplayName("Output should use proper currency formatting")
    void testCurrencyFormatting() {
        exercise.demonstrateBasic2DArray();
        
        String output = outputStream.toString();
        
        // Verify currency formatting with dollar signs and commas
        assertThat(output).contains("$45,000");
        assertThat(output).contains("$52,000");
        assertThat(output).contains("$61,000");
        assertThat(output).doesNotContain("45000.0");
        assertThat(output).doesNotContain("52000.00");
    }
    
    @Test
    @DisplayName("Performance matrix should identify consistent performers correctly")
    void testConsistentPerformersIdentification() {
        exercise.createPerformanceMatrix();
        
        String output = outputStream.toString();
        
        // Bob and Carol should be identified as consistent performers
        // Bob: 38k->41k(+), 41k->44k(+), 44k->47k(+)
        // Carol: 55k->58k(+), 58k->62k(+), 62k->68k(+)
        assertThat(output).contains("• Bob");
        assertThat(output).contains("• Carol");
        
        // Alice and David should not be listed (they have negative growth periods)
        String consistentSection = output.substring(
            output.indexOf("Consistent Performers")
        );
        assertThat(consistentSection).doesNotContain("• Alice");
        assertThat(consistentSection).doesNotContain("• David");
    }
}