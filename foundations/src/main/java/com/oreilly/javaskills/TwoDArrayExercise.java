package com.oreilly.javaskills;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Try It Out Exercise: 2D Arrays for Employee Management
 * <p>
 * This exercise demonstrates practical 2D array operations:
 * - Creating and initializing 2D arrays
 * - Processing data with nested loops
 * - Performing calculations across rows and columns
 * - Transforming and analyzing multidimensional data
 */
public class TwoDArrayExercise {
    
    // Constants for readability
    private static final int QUARTERS = 4;
    private static final NumberFormat CURRENCY = NumberFormat.getCurrencyInstance();
    private static final NumberFormat PERCENT = NumberFormat.getPercentInstance();
    
    public static void main(String[] args) {
        var exercise = new TwoDArrayExercise();
        
        System.out.println("=== 2D Arrays Exercise: Quarterly Sales Analysis ===\n");
        
        // Exercise 1: Basic 2D array creation
        exercise.demonstrateBasic2DArray();
        
        // Exercise 2: Sales performance analysis
        exercise.analyzeSalesPerformance();
        
        // Exercise 3: Commission calculation
        exercise.calculateCommissions();
        
        // Exercise 4: Find top performers
        exercise.findTopPerformers();
        
        // Exercise 5: Create performance matrix
        exercise.createPerformanceMatrix();
    }
    
    /**
     * Exercise 1: Basic 2D array creation and initialization
     */
    public void demonstrateBasic2DArray() {
        System.out.println("1. BASIC 2D ARRAY OPERATIONS:");
        System.out.println("-----------------------------");
        
        // Create a 2D array for quarterly sales (employees x quarters)
        var salesData = new double[][] {
            {45000, 52000, 48000, 61000},  // Alice
            {38000, 41000, 44000, 47000},  // Bob
            {55000, 58000, 62000, 68000},  // Carol
            {42000, 40000, 43000, 45000}   // David
        };
        
        var employees = List.of("Alice", "Bob", "Carol", "David");
        
        System.out.println("Quarterly Sales Data:");
        System.out.println("Employee        Q1          Q2          Q3          Q4");
        System.out.println("----------------------------------------------------------");
        
        for (int emp = 0; emp < salesData.length; emp++) {
            System.out.printf("%-10s", employees.get(emp));
            for (int quarter = 0; quarter < salesData[emp].length; quarter++) {
                System.out.printf("%12s", CURRENCY.format(salesData[emp][quarter]));
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Exercise 2: Analyze sales performance with row and column totals
     */
    public void analyzeSalesPerformance() {
        System.out.println("2. SALES PERFORMANCE ANALYSIS:");
        System.out.println("------------------------------");
        
        var salesData = new double[][] {
            {45000, 52000, 48000, 61000},
            {38000, 41000, 44000, 47000},
            {55000, 58000, 62000, 68000},
            {42000, 40000, 43000, 45000}
        };
        
        var employees = List.of("Alice", "Bob", "Carol", "David");
        
        // Calculate row totals (annual sales per employee)
        System.out.println("Annual Sales by Employee:");
        for (int emp = 0; emp < salesData.length; emp++) {
            double annualSales = 0;
            for (int quarter = 0; quarter < salesData[emp].length; quarter++) {
                annualSales += salesData[emp][quarter];
            }
            System.out.printf("%-8s: %s%n", employees.get(emp), 
                            CURRENCY.format(annualSales));
        }
        
        // Calculate column totals (total sales per quarter)
        System.out.println("\nTotal Sales by Quarter:");
        for (int quarter = 0; quarter < QUARTERS; quarter++) {
            double quarterTotal = 0;
            for (double[] salesDatum : salesData) {
                quarterTotal += salesDatum[quarter];
            }
            System.out.printf("Q%d: %s%n", quarter + 1, 
                            CURRENCY.format(quarterTotal));
        }
        System.out.println();
    }
    
    /**
     * Exercise 3: Calculate commissions based on sales performance
     */
    public void calculateCommissions() {
        System.out.println("3. COMMISSION CALCULATIONS:");
        System.out.println("---------------------------");
        
        var salesData = new double[][] {
            {45000, 52000, 48000, 61000},
            {38000, 41000, 44000, 47000},
            {55000, 58000, 62000, 68000},
            {42000, 40000, 43000, 45000}
        };
        
        var employees = List.of("Alice", "Bob", "Carol", "David");
        var commissionRate = 0.08; // 8% commission
        
        // 2D array to store commissions
        var commissions = new double[salesData.length][salesData[0].length];
        
        // Calculate commissions
        for (int emp = 0; emp < salesData.length; emp++) {
            for (int quarter = 0; quarter < salesData[emp].length; quarter++) {
                commissions[emp][quarter] = salesData[emp][quarter] * commissionRate;
            }
        }
        
        // Display commission report
        System.out.println("Quarterly Commission Report (8% rate):");
        System.out.println("Employee    Q1        Q2        Q3        Q4        Total");
        System.out.println("------------------------------------------------------------");
        
        for (int emp = 0; emp < commissions.length; emp++) {
            System.out.printf("%-8s", employees.get(emp));
            double totalCommission = 0;
            
            for (int quarter = 0; quarter < commissions[emp].length; quarter++) {
                System.out.printf("%10s", CURRENCY.format(commissions[emp][quarter]));
                totalCommission += commissions[emp][quarter];
            }
            
            System.out.printf("%12s%n", CURRENCY.format(totalCommission));
        }
        System.out.println();
    }
    
    /**
     * Exercise 4: Find top performers using 2D array analysis
     */
    public void findTopPerformers() {
        System.out.println("4. TOP PERFORMERS ANALYSIS:");
        System.out.println("---------------------------");
        
        var salesData = new double[][] {
            {45000, 52000, 48000, 61000},
            {38000, 41000, 44000, 47000},
            {55000, 58000, 62000, 68000},
            {42000, 40000, 43000, 45000}
        };
        
        var employees = List.of("Alice", "Bob", "Carol", "David");
        
        // Find top performer for each quarter
        System.out.println("Top Performer by Quarter:");
        for (int quarter = 0; quarter < QUARTERS; quarter++) {
            double maxSales = 0;
            int topEmployee = 0;
            
            for (int emp = 0; emp < salesData.length; emp++) {
                if (salesData[emp][quarter] > maxSales) {
                    maxSales = salesData[emp][quarter];
                    topEmployee = emp;
                }
            }
            
            System.out.printf("Q%d: %-8s (%s)%n", quarter + 1,
                            employees.get(topEmployee),
                            CURRENCY.format(maxSales));
        }
        
        // Find overall top performer
        System.out.println("\nOverall Top Performer:");
        double maxAnnualSales = 0;
        int topPerformer = 0;
        
        for (int emp = 0; emp < salesData.length; emp++) {
            double annualSales = Arrays.stream(salesData[emp]).sum();
            if (annualSales > maxAnnualSales) {
                maxAnnualSales = annualSales;
                topPerformer = emp;
            }
        }
        
        System.out.printf("%s with annual sales of %s%n",
                        employees.get(topPerformer),
                        CURRENCY.format(maxAnnualSales));
        System.out.println();
    }
    
    /**
     * Exercise 5: Create a performance matrix showing growth rates
     */
    public void createPerformanceMatrix() {
        System.out.println("5. PERFORMANCE MATRIX - QUARTER-OVER-QUARTER GROWTH:");
        System.out.println("----------------------------------------------------");
        
        var salesData = new double[][] {
            {45000, 52000, 48000, 61000},
            {38000, 41000, 44000, 47000},
            {55000, 58000, 62000, 68000},
            {42000, 40000, 43000, 45000}
        };
        
        var employees = List.of("Alice", "Bob", "Carol", "David");
        
        // Create growth rate matrix (3 columns for Q2-Q1, Q3-Q2, Q4-Q3)
        var growthRates = new double[salesData.length][QUARTERS - 1];
        
        // Calculate quarter-over-quarter growth
        for (int emp = 0; emp < salesData.length; emp++) {
            for (int quarter = 1; quarter < QUARTERS; quarter++) {
                double previousQuarter = salesData[emp][quarter - 1];
                double currentQuarter = salesData[emp][quarter];
                growthRates[emp][quarter - 1] = 
                    (currentQuarter - previousQuarter) / previousQuarter;
            }
        }
        
        // Display growth matrix
        System.out.println("Quarter-over-Quarter Growth Rates:");
        System.out.println("Employee   Q2 vs Q1   Q3 vs Q2   Q4 vs Q3   Average");
        System.out.println("-----------------------------------------------------");
        
        for (int emp = 0; emp < growthRates.length; emp++) {
            System.out.printf("%-8s", employees.get(emp));
            double totalGrowth = 0;
            
            for (int period = 0; period < growthRates[emp].length; period++) {
                System.out.printf("%11s", PERCENT.format(growthRates[emp][period]));
                totalGrowth += growthRates[emp][period];
            }
            
            double avgGrowth = totalGrowth / (QUARTERS - 1);
            System.out.printf("%11s%n", PERCENT.format(avgGrowth));
        }
        
        // Identify consistent performers
        System.out.println("\nConsistent Performers (positive growth all quarters):");
        for (int emp = 0; emp < growthRates.length; emp++) {
            boolean consistent = true;
            for (double growth : growthRates[emp]) {
                if (growth <= 0) {
                    consistent = false;
                    break;
                }
            }
            if (consistent) {
                System.out.println("  • " + employees.get(emp));
            }
        }
    }
    
    /**
     * Challenge method: Create your own 2D array analysis!
     * <p>
     * Ideas:
     * - Monthly sales data (12 months x employees)
     * - Product sales matrix (products x regions)
     * - Training scores matrix (employees x skills)
     * - Project hours matrix (employees x projects)
     */
    @SuppressWarnings("unused")
    public void customAnalysis() {
        // TODO: Implement your own 2D array analysis
        System.out.println("\nChallenge: Create your own 2D array analysis!");
    }
}