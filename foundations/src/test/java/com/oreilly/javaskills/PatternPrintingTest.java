package com.oreilly.javaskills;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for PatternPrinting exercise.
 * These tests verify that the pattern printing methods produce the expected output.
 */
class PatternPrintingTest {

    private PatternPrinting patternPrinter;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        patternPrinter = new PatternPrinting();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outputStream.reset();
    }

    @Test
    @DisplayName("Square pattern should create correct size square")
    void testPrintSquare() {
        patternPrinter.printSquare(3);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        // Should have 3 lines
        assertThat(lines).hasSize(3);
        
        // Each line should have "* * * "
        for (String line : lines) {
            assertThat(line).isEqualTo("* * * ");
        }
    }

    @Test
    @DisplayName("Right triangle should increase stars per row")
    void testPrintRightTriangle() {
        patternPrinter.printRightTriangle(4);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        // Should have 4 lines
        assertThat(lines).hasSize(4);
        
        // Check each line has correct number of stars
        assertThat(lines[0]).isEqualTo("* ");
        assertThat(lines[1]).isEqualTo("* * ");
        assertThat(lines[2]).isEqualTo("* * * ");
        assertThat(lines[3]).isEqualTo("* * * * ");
    }

    @Test
    @DisplayName("Left triangle should be right-aligned")
    void testPrintLeftTriangle() {
        patternPrinter.printLeftTriangle(3);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        // Should have 3 lines
        assertThat(lines).hasSize(3);
        
        // Check alignment and star count
        assertThat(lines[0]).isEqualTo("    * ");
        assertThat(lines[1]).isEqualTo("  * * ");
        assertThat(lines[2]).isEqualTo("* * * ");
    }

    @Test
    @DisplayName("Diamond pattern should be symmetric")
    void testPrintDiamond() {
        patternPrinter.printDiamond(5);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        // Should have 5 lines for size 5 diamond
        assertThat(lines).hasSize(5);
        
        // Check diamond shape
        assertThat(lines[0]).isEqualTo("  *");      // Top
        assertThat(lines[1]).isEqualTo(" ***");     // Upper middle
        assertThat(lines[2]).isEqualTo("*****");    // Center (widest)
        assertThat(lines[3]).isEqualTo(" ***");     // Lower middle
        assertThat(lines[4]).isEqualTo("  *");      // Bottom
    }

    @Test
    @DisplayName("Number pyramid should show row numbers")
    void testPrintNumberPyramid() {
        patternPrinter.printNumberPyramid(3);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        // Should have 3 lines
        assertThat(lines).hasSize(3);
        
        // Check number patterns
        assertThat(lines[0]).isEqualTo("  1 ");
        assertThat(lines[1]).isEqualTo(" 2 2 ");
        assertThat(lines[2]).isEqualTo("3 3 3 ");
    }

    @Test
    @DisplayName("Checkerboard should alternate X and O")
    void testPrintCheckerboard() {
        patternPrinter.printCheckerboard(4);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        // Should have 4 lines
        assertThat(lines).hasSize(4);
        
        // Check alternating pattern
        assertThat(lines[0]).isEqualTo("X O X O ");
        assertThat(lines[1]).isEqualTo("O X O X ");
        assertThat(lines[2]).isEqualTo("X O X O ");
        assertThat(lines[3]).isEqualTo("O X O X ");
    }

    @Test
    @DisplayName("Square with size 1 should print single star")
    void testPrintSquareSize1() {
        patternPrinter.printSquare(1);
        
        String output = outputStream.toString().trim();
        assertThat(output).isEqualTo("*");
    }

    @Test
    @DisplayName("Right triangle with height 1 should print single star")
    void testPrintRightTriangleHeight1() {
        patternPrinter.printRightTriangle(1);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        assertThat(lines).hasSize(1);
        assertThat(lines[0]).isEqualTo("* ");
    }

    @Test
    @DisplayName("Diamond with size 1 should print single star")
    void testPrintDiamondSize1() {
        patternPrinter.printDiamond(1);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        assertThat(lines).hasSize(1);
        assertThat(lines[0]).isEqualTo("*");
    }

    @Test
    @DisplayName("Number pyramid with height 1 should print single 1")
    void testPrintNumberPyramidHeight1() {
        patternPrinter.printNumberPyramid(1);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        assertThat(lines).hasSize(1);
        assertThat(lines[0]).isEqualTo("1 ");
    }

    @Test
    @DisplayName("Checkerboard size 2 should create 2x2 pattern")
    void testPrintCheckerboardSize2() {
        patternPrinter.printCheckerboard(2);
        
        String output = outputStream.toString();
        String[] lines = output.split("\\r?\\n");
        
        assertThat(lines).hasSize(2);
        assertThat(lines[0]).isEqualTo("X O ");
        assertThat(lines[1]).isEqualTo("O X ");
    }
}