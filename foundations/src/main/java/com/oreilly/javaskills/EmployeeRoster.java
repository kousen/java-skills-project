package com.oreilly.javaskills;

import java.util.Arrays;

public class EmployeeRoster {
    
    private static final int MAX_DEPARTMENTS = 5;
    private static final int MAX_EMPLOYEES_PER_DEPT = 10;
    
    public static void main(String[] args) {
        System.out.println("=== Employee Roster with 2D Arrays ===");
        
        // 2D array: departments x employees
        String[][] employeeRoster = new String[MAX_DEPARTMENTS][MAX_EMPLOYEES_PER_DEPT];
        double[][] employeeSalaries = new double[MAX_DEPARTMENTS][MAX_EMPLOYEES_PER_DEPT];
        
        // Department names
        String[] departmentNames = {"Engineering", "Sales", "Marketing", "HR", "Finance"};
        
        // Populate sample data
        populateRoster(employeeRoster, employeeSalaries);
        
        // Display roster
        displayRoster(employeeRoster, employeeSalaries, departmentNames);
        
        // Calculate department statistics
        calculateStatistics(employeeSalaries, departmentNames);
        
        // Demonstrate nested loops
        demonstrateNestedLoops(employeeRoster, departmentNames);
    }
    
    private static void populateRoster(String[][] roster, double[][] salaries) {
        // Engineering department
        roster[0][0] = "Alice Johnson"; salaries[0][0] = 95000;
        roster[0][1] = "Bob Smith"; salaries[0][1] = 87000;
        roster[0][2] = "Carol Davis"; salaries[0][2] = 92000;
        
        // Sales department  
        roster[1][0] = "David Wilson"; salaries[1][0] = 65000;
        roster[1][1] = "Eva Brown"; salaries[1][1] = 68000;
        
        // Marketing department
        roster[2][0] = "Frank Miller"; salaries[2][0] = 58000;
        roster[2][1] = "Grace Lee"; salaries[2][1] = 62000;
        roster[2][2] = "Henry Taylor"; salaries[2][2] = 60000;
        
        // HR department
        roster[3][0] = "Ivy Anderson"; salaries[3][0] = 72000;
        
        // Finance department
        roster[4][0] = "Jack Thompson"; salaries[4][0] = 78000;
        roster[4][1] = "Kelly White"; salaries[4][1] = 75000;
    }
    
    private static void displayRoster(String[][] roster, double[][] salaries, String[] departments) {
        System.out.println("\nEmployee Roster by Department:");
        System.out.println("===============================");
        
        for (int dept = 0; dept < roster.length; dept++) {
            System.out.println("\n" + departments[dept] + " Department:");
            System.out.println("-".repeat(departments[dept].length() + 12));
            
            boolean hasEmployees = false;
            for (int emp = 0; emp < roster[dept].length; emp++) {
                if (roster[dept][emp] != null) {
                    System.out.printf("  %-20s $%,.2f%n", 
                                    roster[dept][emp], salaries[dept][emp]);
                    hasEmployees = true;
                }
            }
            
            if (!hasEmployees) {
                System.out.println("  No employees");
            }
        }
    }
    
    private static void calculateStatistics(double[][] salaries, String[] departments) {
        System.out.println("\n\nDepartment Statistics:");
        System.out.println("=====================");
        
        for (int dept = 0; dept < salaries.length; dept++) {
            double totalSalaries = 0;
            int countEmployees = 0;
            double maxSalary = 0;
            double minSalary = Double.MAX_VALUE;
            
            for (int emp = 0; emp < salaries[dept].length; emp++) {
                if (salaries[dept][emp] > 0) {
                    totalSalaries += salaries[dept][emp];
                    countEmployees++;
                    maxSalary = Math.max(maxSalary, salaries[dept][emp]);
                    minSalary = Math.min(minSalary, salaries[dept][emp]);
                }
            }
            
            if (countEmployees > 0) {
                double average = totalSalaries / countEmployees;
                System.out.printf("\n%s Department:%n", departments[dept]);
                System.out.printf("  Employees: %d%n", countEmployees);
                System.out.printf("  Total Payroll: $%,.2f%n", totalSalaries);
                System.out.printf("  Average Salary: $%,.2f%n", average);
                System.out.printf("  Highest Salary: $%,.2f%n", maxSalary);
                System.out.printf("  Lowest Salary: $%,.2f%n", minSalary);
            }
        }
    }
    
    private static void demonstrateNestedLoops(String[][] roster, String[] departments) {
        System.out.println("\n\nNested Loop Examples:");
        System.out.println("====================");
        
        // Count total employees
        int totalEmployees = 0;
        for (String[] strings : roster) {
            for (String string : strings) {
                if (string != null) {
                    totalEmployees++;
                }
            }
        }
        System.out.println("Total employees: " + totalEmployees);
        
        // Find employees with specific names (contains search)
        String searchTerm = "John";
        System.out.println("\nEmployees with '" + searchTerm + "' in their name:");
        for (int dept = 0; dept < roster.length; dept++) {
            for (int emp = 0; emp < roster[dept].length; emp++) {
                if (roster[dept][emp] != null && 
                    roster[dept][emp].toLowerCase().contains(searchTerm.toLowerCase())) {
                    System.out.println("  " + roster[dept][emp] + " (" + departments[dept] + ")");
                }
            }
        }
        
        // Create flattened array of all employees
        String[] allEmployees = new String[totalEmployees];
        int index = 0;
        for (String[] strings : roster) {
            for (String string : strings) {
                if (string != null) {
                    allEmployees[index++] = string;
                }
            }
        }
        
        Arrays.sort(allEmployees);
        System.out.println("\nAll employees (alphabetically):");
        for (String employee : allEmployees) {
            System.out.println("  " + employee);
        }
    }
}