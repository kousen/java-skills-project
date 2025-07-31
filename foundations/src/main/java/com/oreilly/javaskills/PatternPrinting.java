package com.oreilly.javaskills;

/**
 * Try It Out Exercise: Pattern Printing with Nested Loops
 * <p>
 * This exercise demonstrates how nested loops can create visual patterns.
 * Each method shows a different pattern that can be created using nested loops.
 * <p>
 * Instructions:
 * 1. Run each method to see the pattern it creates
 * 2. Try modifying the size parameters to see how patterns change
 * 3. Challenge: Create your own pattern variations
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
public class PatternPrinting {

    public static void main(String[] args) {
        var printer = new PatternPrinting();
        
        System.out.println("=== Pattern Printing Exercise ===\n");
        
        // Pattern 1: Simple Square
        System.out.println("1. SIMPLE SQUARE (5x5):");
        printer.printSquare(5);
        
        // Pattern 2: Right Triangle
        System.out.println("\n2. RIGHT TRIANGLE:");
        printer.printRightTriangle(6);
        
        // Pattern 3: Left Triangle
        System.out.println("\n3. LEFT TRIANGLE:");
        printer.printLeftTriangle(6);
        
        // Pattern 4: Diamond
        System.out.println("\n4. DIAMOND:");
        printer.printDiamond(7);
        
        // Pattern 5: Number Pyramid
        System.out.println("\n5. NUMBER PYRAMID:");
        printer.printNumberPyramid(5);
        
        // Pattern 6: Checkerboard
        System.out.println("\n6. CHECKERBOARD (8x8):");
        printer.printCheckerboard(8);
    }
    
    /**
     * Prints a simple square pattern using asterisks
     * @param size the width and height of the square
     */
    public void printSquare(int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }
    
    /**
     * Prints a right triangle pattern
     * @param height the height of the triangle
     */
    public void printRightTriangle(int height) {
        for (int row = 1; row <= height; row++) {
            for (int col = 1; col <= row; col++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }
    
    /**
     * Prints a left triangle pattern (right-aligned)
     * @param height the height of the triangle
     */
    public void printLeftTriangle(int height) {
        for (int row = 1; row <= height; row++) {
            // Print spaces for alignment
            for (int space = 1; space <= height - row; space++) {
                System.out.print("  ");
            }
            // Print asterisks
            for (int col = 1; col <= row; col++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }
    
    /**
     * Prints a diamond pattern
     * @param size the width of the diamond (should be odd number)
     */
    public void printDiamond(int size) {
        int middle = size / 2;
        
        // Upper half (including middle)
        for (int row = 0; row <= middle; row++) {
            // Print leading spaces
            for (int space = 0; space < middle - row; space++) {
                System.out.print(" ");
            }
            // Print asterisks
            for (int col = 0; col < 2 * row + 1; col++) {
                System.out.print("*");
            }
            System.out.println();
        }
        
        // Lower half
        for (int row = middle - 1; row >= 0; row--) {
            // Print leading spaces
            for (int space = 0; space < middle - row; space++) {
                System.out.print(" ");
            }
            // Print asterisks
            for (int col = 0; col < 2 * row + 1; col++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
    
    /**
     * Prints a number pyramid where each row shows the row number
     * @param height the height of the pyramid
     */
    public void printNumberPyramid(int height) {
        for (int row = 1; row <= height; row++) {
            // Print leading spaces for centering
            for (int space = 1; space <= height - row; space++) {
                System.out.print(" ");
            }
            // Print numbers
            for (int col = 1; col <= row; col++) {
                System.out.print(row + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * Prints a checkerboard pattern using X and O
     * @param size the size of the checkerboard (should be even)
     */
    public void printCheckerboard(int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Use modulo to alternate between X and O
                if ((row + col) % 2 == 0) {
                    System.out.print("X ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Challenge method: Create your own pattern!
     * This method is left empty for students to implement their own creative pattern.
     * <p>
     * Ideas:
     * - Hollow square (only borders)
     * - Pascal's triangle
     * - Heart shape
     * - Christmas tree
     * - Spiral pattern
     */
    public void printCustomPattern() {
        // TODO: Implement your own creative pattern here!
        System.out.println("Challenge: Create your own pattern!");
    }
}