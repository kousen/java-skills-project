package com.oreilly.javaskills;

import java.util.List;

/**
 * Demonstrates various uses of nested loops that don't involve multidimensional arrays.
 * These examples show practical applications in an Employee Management System.
 */
public class NestedLoopsDemo {

    // Sample data classes for demonstrations
    record Employee(String name, String department, int rating, int yearsOfExperience) {
    }

    public static void main(String[] args) {
        var demo = new NestedLoopsDemo();

        System.out.println("=== Nested Loops Demo ===\n");

        // Example 1: Employee Pairing for Mentorship
        System.out.println("1. MENTORSHIP PAIRING:");
        demo.demonstrateMentorshipPairing();

        // Example 2: Work Schedule Generation
        System.out.println("\n2. WORK SCHEDULE GENERATION:");
        demo.generateWorkSchedule();

        // Example 3: Finding Duplicate Employees
        System.out.println("\n3. FINDING DUPLICATES:");
        demo.findDuplicateEmployees();

        // Example 4: Department Collaboration Matrix
        System.out.println("\n4. DEPARTMENT COLLABORATION MATRIX:");
        demo.generateCollaborationMatrix();

        // Example 5: Performance Rating Distribution
        System.out.println("\n5. PERFORMANCE RATING DISTRIBUTION:");
        demo.showRatingDistribution();

        // Example 6: Simple Pattern (Preview of exercise)
        System.out.println("\n6. SIMPLE PATTERN EXAMPLE:");
        demo.printSimplePattern();
    }

    /**
     * Example 1: Find all possible mentor-mentee pairs
     * Demonstrates nested loops with two different lists
     */
    public void demonstrateMentorshipPairing() {
        List<Employee> mentors = List.of(
                new Employee("Alice Johnson", "Engineering", 5, 8),
                new Employee("Bob Smith", "Marketing", 4, 6),
                new Employee("Carol Davis", "Sales", 5, 7)
        );

        List<Employee> newHires = List.of(
                new Employee("David Lee", "Engineering", 0, 0),
                new Employee("Emma Wilson", "Marketing", 0, 0),
                new Employee("Frank Chen", "Engineering", 0, 0)
        );

        System.out.println("Possible mentor-mentee pairs:");
        for (Employee mentor : mentors) {
            for (Employee mentee : newHires) {
                // Only pair within same department
                if (mentor.department().equals(mentee.department())) {
                    System.out.println("  " + mentor.name() + " → " + mentee.name());
                }
            }
        }
    }

    /**
     * Example 2: Generate a weekly work schedule
     * Shows nested loops with arrays of strings
     */
    public void generateWorkSchedule() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] shifts = {"Morning (8-12)", "Afternoon (12-4)", "Evening (4-8)"};

        System.out.println("Weekly Schedule Slots:");
        for (String day : days) {
            System.out.println(day + ":");
            for (String shift : shifts) {
                System.out.println("  " + shift + " - [Available]");
            }
        }
    }

    /**
     * Example 3: Find duplicate employees in a list
     * Demonstrates comparing each element with every other element
     */
    public void findDuplicateEmployees() {
        List<String> employeeIds = List.of(
                "EMP001", "EMP002", "EMP003", "EMP002", "EMP004", "EMP001", "EMP005"
        );

        System.out.println("Checking for duplicate employee IDs:");
        boolean foundDuplicates = false;

        for (int i = 0; i < employeeIds.size(); i++) {
            for (int j = i + 1; j < employeeIds.size(); j++) {
                if (employeeIds.get(i).equals(employeeIds.get(j))) {
                    System.out.println("  Duplicate found: " + employeeIds.get(i) +
                                       " at positions " + i + " and " + j);
                    foundDuplicates = true;
                }
            }
        }

        if (!foundDuplicates) {
            System.out.println("  No duplicates found.");
        }
    }

    /**
     * Example 4: Generate department collaboration matrix
     * Shows nested loops creating combinations (excluding self-pairs)
     */
    public void generateCollaborationMatrix() {
        String[] departments = {"Engineering", "Sales", "Marketing", "HR"};

        System.out.println("Department Collaboration Opportunities:");
        for (String dept1 : departments) {
            for (String dept2 : departments) {
                if (!dept1.equals(dept2)) {
                    System.out.println("  " + dept1 + " ←→ " + dept2);
                }
            }
        }
    }

    /**
     * Example 5: Show performance rating distribution
     * Nested loops with data aggregation
     */
    public void showRatingDistribution() {
        List<Employee> employees = List.of(
                new Employee("Alice", "Engineering", 5, 8),
                new Employee("Bob", "Engineering", 4, 6),
                new Employee("Carol", "Engineering", 5, 7),
                new Employee("David", "Sales", 3, 2),
                new Employee("Emma", "Sales", 4, 3),
                new Employee("Frank", "Sales", 5, 5)
        );

        String[] departments = {"Engineering", "Sales"};

        System.out.println("Performance Rating Distribution by Department:");
        for (String dept : departments) {
            System.out.println("\n" + dept + ":");
            for (int rating = 5; rating >= 1; rating--) {
                System.out.print("  " + rating + " stars: ");
                int count = 0;
                for (Employee emp : employees) {
                    if (emp.department().equals(dept) && emp.rating() == rating) {
                        System.out.print("★ ");
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.print("(none)");
                }
                System.out.println();
            }
        }
    }

    /**
     * Example 6: Simple pattern printing
     * Preview of the exercise - shows visual feedback from nested loops
     */
    public void printSimplePattern() {
        System.out.println("Simple 5x5 Square Pattern:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }
}