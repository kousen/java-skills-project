/**
 * This class demonstrates standard Java naming conventions.
 * The goal is to write code that is readable, self-documenting, and easy to maintain.
 */
public class NamingConventions {

    // CONSTANTS: Use UPPER_SNAKE_CASE for constants (static final fields).
    // They are unchanging values shared across the application.
    public static final String COMPANY_NAME = "O'Reilly Media, Inc.";
    public static final int MAX_EMPLOYEES = 1000;

    public static void main(String[] args) {
        System.out.println("=== Proper Naming Conventions Demo ===");
        
        // --- GOOD NAMING CONVENTIONS ---

        // VARIABLES: Use camelCase for local variables and fields.
        // Start with a lowercase letter. Be descriptive.
        String employeeName = "John Doe";
        int employeeId = 12345;
        double salaryAmount = 75000.50;
        boolean isActive = true;

        // METHODS: Use camelCase for methods. They should be verbs that describe an action.
        printEmployeeDetails(employeeName, employeeId, salaryAmount, isActive);

        // --- BAD NAMING CONVENTIONS (AVOID THESE) ---

        // Single letters (unless it's a simple loop counter like 'i')
        String n = "Jane Doe"; // Problem: Not descriptive. What does 'n' mean?

        // All caps for a regular variable (looks like a constant)
        String ANOTHER_NAME = "This is confusing."; // Problem: Should be camelCase.

        // Underscores in variable names (not the Java convention)
        int employee_id = 67890; // Problem: Use camelCase, like 'employeeId'.

        // Starting with an uppercase letter (looks like a class name)
        String Name = "Peter Jones"; // Problem: Use camelCase, like 'name'.

        System.out.println("Bad examples:");
        System.out.println("Vague name 'n': " + n);
        System.out.println("Confusing constant-like name 'ANOTHER_NAME': " + ANOTHER_NAME);
        System.out.println("Non-standard 'employee_id': " + employee_id);
        System.out.println("Class-like name 'Name': " + Name);
    }

    /**
     * Prints the details of an employee.
     * Method names should be clear, descriptive verbs in camelCase.
     *
     * @param name   The employee's full name.
     * @param id     The employee's ID.
     * @param salary The employee's salary.
     * @param active The employee's active status.
     */
    public static void printEmployeeDetails(String name, int id, double salary, boolean active) {
        System.out.println("--- Demonstrating Good Naming ---");
        System.out.println("Company: " + COMPANY_NAME);
        System.out.println("Max Employees: " + MAX_EMPLOYEES);
        System.out.println(" Employee Details:");
        System.out.println("  Name: " + name);
        System.out.println("  ID: " + id);
        System.out.println("  Salary: $" + salary);
        System.out.println("  Active: " + active);
    }
}
