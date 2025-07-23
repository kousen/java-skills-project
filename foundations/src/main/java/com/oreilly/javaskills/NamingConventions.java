package com.oreilly.javaskills;

import java.text.NumberFormat;
import java.util.List;

/**
 * This class demonstrates standard Java naming conventions using modern Java 21 features.
 * The goal is to write code that is readable, self-documenting, and easy to maintain.
 */
public class NamingConventions {

    // CONSTANTS: Use UPPER_SNAKE_CASE for constants (static final fields).
    // They are unchanging values shared across the application.
    public static final String COMPANY_NAME = "O'Reilly Media, Inc.";
    public static final int MAX_EMPLOYEES = 1000;
    public static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    
    // ENUMS: Use PascalCase like classes (they are special classes)
    public enum Department {
        ENGINEERING("Engineering"), 
        MARKETING("Marketing"), 
        SALES("Sales"), 
        HR("Human Resources");
        
        private final String displayName;
        
        // Enum constructor (implicitly private - cannot be public/protected)
        Department(String displayName) {
            this.displayName = displayName;
        }
        
        // Method name follows camelCase convention
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Modern Java: Using List.of() for immutable collections (Java 9+)
    public static final List<String> VALID_DEPARTMENTS = List.of(
        "Engineering", "Marketing", "Sales", "HR"
    );

    /**
     * Constructor for com.oreilly.javaskills.NamingConventions.
     * Note: Constructors use PascalCase because they must match the class name.
     * This is the one exception to the camelCase rule for methods.
     */
    public NamingConventions() {
        // Constructor body (if needed)
    }

    @SuppressWarnings({"DataFlowIssue", "ExtractMethodRecommender"})
    public static void main(String[] args) {
        System.out.println("=== Proper Naming Conventions Demo ===");
        
        // --- GOOD NAMING CONVENTIONS ---

        // VARIABLES: Use camelCase for local variables and fields.
        // Start with a lowercase letter. Be descriptive.
        
        // Modern Java: Using var for local variable type inference (Java 10+)
        var employeeName = "John Doe";  // String inferred
        var employeeId = 12345;         // int inferred
        var salaryAmount = 75_000.50;   // double inferred (underscore separators - Java 7+)
        var isActive = true;            // boolean inferred
        
        // ENUMS: Example of using an enum in a natural way
        var employeeDepartment = Department.ENGINEERING;
        
        // Modern Java: Enhanced switch expression on enum (no default needed!)
        var departmentBudget = switch (employeeDepartment) {
            case ENGINEERING -> 500_000;
            case MARKETING -> 300_000;
            case SALES -> 400_000;
            case HR -> 200_000;
        };

        // METHODS: Use camelCase for methods. They should be verbs that describe an action.
        printEmployeeDetails(employeeName, employeeId, salaryAmount, isActive,
                employeeDepartment.getDisplayName(), departmentBudget);

        // --- BAD NAMING CONVENTIONS (AVOID THESE) ---

        // Single letters (unless it's a simple loop counter like 'i')
        String n = "Jane Doe"; // Problem: Not descriptive. What does 'n' mean?

        // All caps for a regular variable (looks like a constant)
        String ANOTHER_NAME = "This is confusing."; // Problem: Should be camelCase.

        // Underscores in variable names (not the Java convention)
        int employee_id = 67890; // Problem: Use camelCase, like 'employeeId'.

        // Starting with an uppercase letter (looks like a class name)
        String Name = "Peter Jones"; // Problem: Use camelCase, like 'name'.

        // Modern formatting for bad examples too
        var badExamplesOutput = """
                
                --- Bad examples (What NOT to do) ---
                Vague name 'n':                           %s
                Confusing constant-like 'ANOTHER_NAME':   %s
                Non-standard 'employee_id':               %d
                Class-like name 'Name':                   %s
                """.formatted(n, ANOTHER_NAME, employee_id, Name);
        
        System.out.print(badExamplesOutput);
    }

    /**
     * Prints the details of an employee using modern Java features.
     * Method names should be clear, descriptive verbs in camelCase.
     *
     * @param name       The employee's full name.
     * @param id         The employee's ID.
     * @param salary     The employee's salary.
     * @param active     The employee's active status.
     * @param department The employee's department.
     * @param budget     The department's budget.
     */
    public static void printEmployeeDetails(String name, int id, double salary, boolean active, String department, int budget) {
        // Modern Java: Text blocks with formatted() method (Java 15+)
        var formattedOutput = """
                --- Demonstrating Good Naming ---
                Company:       %s
                Max Employees: %d
                Departments:   %s
                
                Employee Details:
                  Name:        %s
                  ID:          %d
                  Department:  %s
                  Dept Budget: %s
                  Salary:      %s
                  Active:      %s
                """.formatted(COMPANY_NAME, MAX_EMPLOYEES,
                String.join(", ", VALID_DEPARTMENTS),  // Modern: String.join() 
                name, id, department,
                CURRENCY_FORMATTER.format(budget),
                CURRENCY_FORMATTER.format(salary), 
                active
        );
        
        System.out.print(formattedOutput);
    }
}
