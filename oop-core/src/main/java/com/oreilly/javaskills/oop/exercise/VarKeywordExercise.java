package com.oreilly.javaskills.oop.exercise;

import com.oreilly.javaskills.oop.hr.Department;
import com.oreilly.javaskills.oop.hr.Employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Try It Out Exercise: The var Keyword in Java
 * <p>
 * This exercise demonstrates using var for local variable type inference.
 * The var keyword was introduced in Java 10 and helps reduce boilerplate
 * while maintaining type safety and readability.
 * <p>
 * Study Approach:
 * 1. Examine the BEFORE sections showing traditional explicit typing
 * 2. Compare with the AFTER sections using var keyword
 * 3. Notice how var makes code more readable without losing type safety
 * 4. Run the exercise to see var in action with complex types
 * 5. Try the exercise tasks to practice using var appropriately
 * <p>
 * Key Concepts to Observe:
 * - var works with local variables only (not fields, parameters, or return types)
 * - The compiler infers the type from the right-hand side expression
 * - var maintains full type safety - it's just syntactic sugar
 * - var is especially useful with complex generic types
 * - var improves readability when the type is obvious from context
 * - var should be used when it clarifies code, not just to save typing
 * <p>
 * When to Use var:
 * - Complex generic types: Map&lt;String, List&lt;Employee&gt;&gt;
 * - Stream operations with complex return types
 * - When the type is obvious from the right-hand side
 * - Constructor calls where type is repeated
 * <p>
 * When NOT to Use var:
 * - When it makes code less readable
 * - With primitive literals where type isn't clear
 * - When the inferred type might not be what you expect
 */
public class VarKeywordExercise {

    public static void main(String[] args) {
        System.out.println("=== var Keyword Exercise ===\n");
        
        // Demonstrate var with different scenarios
        demonstrateBasicVarUsage();
        demonstrateVarWithCollections();
        demonstrateVarWithComplexTypes();
        
        System.out.println("\n=== EXERCISE TASKS ===");
        exerciseTasks();
    }
    
    /**
     * SECTION 1: Basic var Usage
     * Shows simple cases where var improves readability
     */
    private static void demonstrateBasicVarUsage() {
        System.out.println("1. BASIC VAR USAGE:");
        System.out.println("-------------------");
        
        // BEFORE: Explicit types (traditional approach)
        String name = "Alice Johnson";
        LocalDate hireDate = LocalDate.of(2020, 1, 15);
        StringBuilder builder = new StringBuilder();
        
        // AFTER: Using var
        // ... your solution here ...

        System.out.printf("Name: %s%n", name);
        System.out.printf("Hire date: %s%n", hireDate);
        System.out.printf("Builder: %s%n", builder);
        System.out.println();
    }
    
    /**
     * SECTION 2: var with Collections
     * Shows how var simplifies working with generic collections
     */
    private static void demonstrateVarWithCollections() {
        System.out.println("2. VAR WITH COLLECTIONS:");
        System.out.println("------------------------");
        
        // BEFORE: Verbose generic declarations
        List<Employee> employees = new ArrayList<>();
        Map<String, Department> departmentMap = new HashMap<>();
        
        System.out.println("\nAFTER (var eliminates repetition):");
        // ... your solution here ...

        // Populate with sample data
        var alice = new Employee(1001, "Alice Johnson",
                120000, LocalDate.now().minusYears(3));
        var bob = new Employee(1002, "Bob Smith",
                95000, LocalDate.now().minusYears(2));
        
        employees.add(alice);
        employees.add(bob);
        
        var engineering = new Department("Engineering", "ENG");
        departmentMap.put("ENG", engineering);
        
        System.out.printf("employees list has %d items%n", employees.size());
        System.out.printf("department map has %d entries%n", departmentMap.size());
        System.out.println();
    }
    
    /**
     * SECTION 3: var with Complex Types
     * Shows var's real power with complex nested generic types
     */
    private static void demonstrateVarWithComplexTypes() {
        System.out.println("3. VAR WITH COMPLEX TYPES:");
        System.out.println("--------------------------");
        
        // BEFORE: Very verbose nested generics
        Map<Department, List<Employee>> departmentEmployees = new HashMap<>();
        Map<String, Map<String, List<Employee>>> organizationChart = new HashMap<>();
        
        System.out.println("\nAFTER (much cleaner with var):");
        // ... your solution here ...

        // Set up sample complex data structure
        var engineering = new Department("Engineering", "ENG");
        var marketing = new Department("Marketing", "MKT");
        
        var alice = new Employee(1001, "Alice Johnson",
                120000, LocalDate.now().minusYears(3));
        var bob = new Employee(1002, "Bob Smith",
                95000, LocalDate.now().minusYears(2));
        var charlie = new Employee(1003, "Charlie Wilson",
                105000, LocalDate.now().minusYears(1));
        
        // Build complex nested structure
        var engEmployees = new ArrayList<Employee>();
        engEmployees.add(alice);
        engEmployees.add(charlie);
        
        var mktEmployees = new ArrayList<Employee>();
        mktEmployees.add(bob);
        
        departmentEmployees.put(engineering, engEmployees);
        departmentEmployees.put(marketing, mktEmployees);

        // Populate organization chart
        var sales = new Department("Sales", "SAL");
        var finance = new Department("Finance", "FIN");
        var salesDept = new HashMap<String, List<Employee>>();
        salesDept.put("Sales", engEmployees);
        salesDept.put("Finance", mktEmployees);

        var financeDept = new HashMap<String, List<Employee>>();
        financeDept.put("Sales", mktEmployees);
        financeDept.put("Finance", engEmployees);
        organizationChart.put("Sales", salesDept);
        organizationChart.put("Finance", financeDept);
        organizationChart.put(sales.getName(), salesDept);
        organizationChart.put(finance.getName(), financeDept);

        System.out.printf("Organization chart has %d departments%n", organizationChart.size());
        System.out.printf("Department employees has %d entries%n", departmentEmployees.size());
        System.out.println();
        
        // Show the power of var in iteration
        System.out.println("\nIterating with var:");
        for (var entry : departmentEmployees.entrySet()) {  // var infers Map.Entry<Department, List<Employee>>
            var dept = entry.getKey();  // var infers Department
            var empList = entry.getValue();  // var infers List<Employee>
            System.out.printf("Department %s has %d employees%n", dept.getName(), empList.size());
        }
        System.out.println();
    }
    
    /**
     * EXERCISE TASKS: Your turn to practice with var
     * <p>
     * TASKS TO COMPLETE:
     * 1. Convert the explicit types below to use var where appropriate
     * 2. Create a complex data structure using var
     * 3. Use var in a loop to process the data structure
     * 4. Identify one case where var should NOT be used and explain why
     * <p>
     * SOLUTION BELOW (try first, then check):
     */
    private static void exerciseTasks() {
        System.out.println("4. YOUR EXERCISE TASKS:");
        System.out.println("----------------------");
        
        System.out.println("TASK 1: Convert these to use var where appropriate:");
        
        // TODO: Convert these explicit types to var where it improves readability
         String companyName = "Tech Solutions Inc";
         LocalDate foundingDate = LocalDate.of(2010, 3, 15);
         List<String> technologies = new ArrayList<>();
         Map<String, Integer> salaryRanges = new HashMap<>();
        
        // YOUR SOLUTIONS:
        System.out.println("✓ Converted explicit types to var");
        System.out.printf("Company name: %s%n", companyName);
        System.out.printf("Founding date: %s%n", foundingDate);
        System.out.printf("Technologies: %s%n", technologies);
        System.out.printf("Salary ranges: %s%n", salaryRanges);
        System.out.println();
        
        System.out.println("\nTASK 2: Create a complex data structure with var:");
        System.out.println();
        
        // TODO: Employee skills tracking system (replace explicit types with var)
        HashMap<Employee, Map<String, Integer>> skillsDatabase = new HashMap<>();  // Employee -> Skills -> Proficiency Level

        Employee developer = new Employee(2001, "Sarah Developer",
                110000, LocalDate.now().minusYears(4));
        Employee architect = new Employee(2002, "Mike Architect",
                140000, LocalDate.now().minusYears(6));
        
        // Create skill maps
        HashMap<String, Integer> sarahSkills = new HashMap<>();
        sarahSkills.put("Java", 8);
        sarahSkills.put("Spring Boot", 7);
        sarahSkills.put("React", 6);

        HashMap<String, Integer> mikeSkills = new HashMap<>();
        mikeSkills.put("Java", 9);
        mikeSkills.put("System Architecture", 9);
        mikeSkills.put("Microservices", 8);
        
        skillsDatabase.put(developer, sarahSkills);
        skillsDatabase.put(architect, mikeSkills);
        
        System.out.println("✓ Created complex skills database with var");
        
        System.out.println("\nTASK 3: Process the data structure with var:");
        
        // TODO: Use var in foreach loop variable
        for (Map.Entry<Employee, Map<String, Integer>> employeeEntry : skillsDatabase.entrySet()) {
            var employee = employeeEntry.getKey();
            var skills = employeeEntry.getValue();
            
            System.out.printf("%s has %d skills:%n", employee.getName(), skills.size());
            
            var topSkill = "";
            var maxLevel = 0;
            for (var skillEntry : skills.entrySet()) {
                var skill = skillEntry.getKey();
                var level = skillEntry.getValue();
                if (level > maxLevel) {
                    maxLevel = level;
                    topSkill = skill;
                }
            }
            System.out.printf("  Top skill: %s (level %d)%n", topSkill, maxLevel);
        }
        
        System.out.println("\nTASK 4: Case where var should NOT be used:");
        
        // DON'T use var here - compiles, but type isn't obvious
        int employeeCount = getEmployeeCount();  // Better than: var employeeCount = getEmployeeCount();
        double averageRating = 4.5;  // Better than: var averageRating = 4.5; (could be float)
        
        System.out.println("✓ Used explicit types where var would reduce clarity");
        System.out.printf("Company has %d employees with average rating %.1f%n", 
            employeeCount, averageRating);
        
        System.out.println("\n=== Exercise Complete! ===");
        System.out.println("Key Takeaway: Use var when it makes code MORE readable, not just shorter.");
    }
    
    /**
     * Helper method demonstrating when explicit return types are needed
     * (var cannot be used for return types, parameters, or fields)
     */
    private static int getEmployeeCount() {
        return 150;  // Simulated company size
    }
}