package com.oreilly.javaskills.oop.exercise;

/**
 * Try It Out Exercise: Java Records
 * <p>
 * This exercise demonstrates creating and using record classes for immutable data carriers.
 * Records were introduced as a standard feature in Java 16 and provide a highly concise 
 * syntax for creating classes that are transparent carriers for immutable data.
 * <p>
 * Study Approach:
 * 1. Examine the BEFORE sections showing traditional POJO classes with lots of boilerplate
 * 2. Compare with the AFTER sections using record classes
 * 3. Notice how records eliminate constructor, getter, equals, hashCode, and toString boilerplate
 * 4. Run the exercise to see records in action with real data
 * 5. Try the exercise tasks to practice creating and customizing records
 * <p>
 * Key Concepts to Observe:
 * - Records are immutable by default (all fields are implicitly final)
 * - Records automatically generate constructor, accessors, equals, hashCode, toString
 * - Record accessor methods don't use "get" prefix (name() not getName())
 * - Records can have compact constructors for validation
 * - Records can have custom methods and implement interfaces
 * - Records cannot extend other classes (they extend java.lang.Record)
 * <p>
 * When to Use Records:
 * - Data Transfer Objects (DTOs)
 * - API request/response objects
 * - Database result objects
 * - Any simple data aggregate that doesn't need mutability
 * <p>
 * When NOT to Use Records:
 * - When you need mutable objects
 * - When you need inheritance from other classes
 * - When you need JavaBean-style getters/setters
 */
public class RecordsExercise {

    public static void main(String[] args) {
        System.out.println("=== Java Records Exercise ===\n");
        
        // Demonstrate records with different scenarios
        demonstrateBasicRecords();
        demonstrateRecordFeatures();
        demonstrateCustomizedRecords();
        
        System.out.println("\n=== EXERCISE TASKS ===");
        exerciseTasks();
    }
    
    /**
     * SECTION 1: Basic Records vs. Traditional POJOs
     * Shows the dramatic difference in code volume
     */
    private static void demonstrateBasicRecords() {
        System.out.println("""
                1. BASIC RECORDS VS TRADITIONAL POJOS:
                --------------------------------------
                
                BEFORE - Traditional POJO (lots of boilerplate):
                public final class EmployeePojo {
                    private final String name;
                    private final int id;
                    private final double salary;
                
                    public EmployeePojo(String name, int id, double salary) { ... }
                    public String getName() { return name; }
                    public int getId() { return id; }
                    public double getSalary() { return salary; }
                    // Plus equals(), hashCode(), toString() - many more lines!
                }
                """);
        
        System.out.println("\nAFTER - Using Records (one line!):");
        System.out.println("public record EmployeeRecord(String name, int id, double salary) { }");
        
        // Create instances to demonstrate
        var alice = new EmployeeRecord("Alice Johnson", 1001, 75000);
        var bob = new EmployeeRecord("Bob Smith", 1002, 80000);
        
        System.out.println("\nRecord instances created:");
        System.out.println("Alice: " + alice);
        System.out.println("Bob: " + bob);
        System.out.println();
    }
    
    /**
     * SECTION 2: Record Features
     * Shows all the methods you get for free
     */
    private static void demonstrateRecordFeatures() {
        System.out.println("2. RECORD FEATURES (ALL AUTOMATIC):");
        System.out.println("-----------------------------------");
        
        var employee1 = new EmployeeRecord("Charlie Wilson", 1003, 85000);
        var employee2 = new EmployeeRecord("Charlie Wilson", 1003, 85000); // Same data
        var employee3 = new EmployeeRecord("Diana Prince", 1004, 90000);   // Different data
        
        System.out.println("Accessor methods (no 'get' prefix):");
        System.out.printf("Name: %s%n", employee1.name());     // Not getName()!
        System.out.printf("ID: %d%n", employee1.id());         // Not getId()!
        System.out.printf("Salary: $%.2f%n", employee1.salary()); // Not getSalary()!
        
        System.out.println("\nAutomatic toString():");
        System.out.println("employee1: " + employee1);
        
        System.out.println("\nAutomatic equals() (value-based equality):");
        System.out.printf("employee1.equals(employee2): %b (same data)%n", employee1.equals(employee2));
        System.out.printf("employee1.equals(employee3): %b (different data)%n", employee1.equals(employee3));
        
        System.out.println("\nAutomatic hashCode() (consistent with equals):");
        System.out.printf("employee1.hashCode(): %d%n", employee1.hashCode());
        System.out.printf("employee2.hashCode(): %d%n", employee2.hashCode());
        System.out.printf("Hash codes equal: %b%n", employee1.hashCode() == employee2.hashCode());
        System.out.println();
    }
    
    /**
     * SECTION 3: Customized Records
     * Shows compact constructors and custom methods
     */
    private static void demonstrateCustomizedRecords() {
        System.out.println("3. CUSTOMIZED RECORDS:");
        System.out.println("---------------------");
        
        System.out.println("Records can have compact constructors for validation:");
        System.out.println("public record ValidatedEmployee(String name, int id, double salary) {");
        System.out.println("    public ValidatedEmployee {  // Compact constructor");
        System.out.println("        if (salary < 0) throw new IllegalArgumentException(\"Negative salary\");");
        System.out.println("    }");
        System.out.println("}");
        
        try {
            var validEmployee = new ValidatedEmployee("Eve Adams", 1005, 70000);
            System.out.println("✓ Valid employee created: " + validEmployee);
            
            // This would throw an exception:
            // var invalidEmployee = new ValidatedEmployee("Bad Employee", 1006, -1000);
            System.out.println("✓ Validation prevents invalid data");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Validation caught: " + e.getMessage());
        }
        
        System.out.println("\nRecords can have custom methods:");
        var emp = new ValidatedEmployee("Frank Miller", 1006, 95000);
        System.out.printf("Formatted info: %s%n", emp.getFormattedInfo());
        System.out.printf("Qualifies for bonus: %b%n", emp.qualifiesForBonus());
        
        System.out.println("\nRecords support immutable updates (returns new instance):");
        var updatedEmp = emp.withSalary(100000);
        System.out.printf("Original: %s%n", emp);
        System.out.printf("Updated:  %s%n", updatedEmp);
        System.out.println();
    }
    
    /**
     * EXERCISE TASKS: Your turn to practice with records
     * <p>
     * TASKS TO COMPLETE:
     * 1. Create a simple record for a Book
     * 2. Create a record with validation
     * 3. Create nested records for complex data
     * 4. Add custom methods to records
     */
    private static void exerciseTasks() {
        System.out.println("4. YOUR EXERCISE TASKS:");
        System.out.println("----------------------");
        
        System.out.println("TASK 1: Create a simple Book record");
        System.out.println("TODO: Create a record with title, author, isbn, and price fields");
        
        // TODO: Uncomment and complete this record definition
        // record Book(String title, String author, String isbn, double price) { }
        
        // TODO: Create a Book instance and print it
        // var book = new Book("Modern Java Recipes", "Ken Kousen", "978-1491973172", 42.99);
        // System.out.println("Book: " + book);
        
        System.out.println("✓ Book record task completed");
        System.out.println();
        
        System.out.println("TASK 2: Create a validated Person record");
        System.out.println("TODO: Create a Person record with validation in compact constructor");
        
        // TODO: Create a Person record with name, age, and email
        // Add validation: name not empty, age >= 0, email contains @
        /*
        record Person(String name, int age, String email) {
            public Person {
                // Add your validation here
            }
        }
        */
        
        // TODO: Test the validation
        // var person = new Person("John Doe", 30, "john@example.com");
        // System.out.println("Valid person: " + person);
        
        System.out.println("✓ Validated Person record task completed");
        System.out.println();
        
        System.out.println("TASK 3: Create nested records");
        System.out.println("TODO: Create Address and Customer records, with Customer containing Address");
        
        // TODO: Create Address record (street, city, state, zipCode)
        // record Address(String street, String city, String state, String zipCode) { }
        
        // TODO: Create Customer record (name, customerId, address)
        // record Customer(String name, int customerId, Address address) { }
        
        // TODO: Create instances and show nested access
        // var address = new Address("123 Main St", "Anytown", "CA", "12345");
        // var customer = new Customer("Jane Smith", 2001, address);
        // System.out.println("Customer city: " + customer.address().city());
        
        System.out.println("✓ Nested records task completed");
        System.out.println();
        
        System.out.println("TASK 4: Add custom methods to records");
        System.out.println("TODO: Add business logic methods to your records");
        
        // Example of what you could add to Book record:
        // public boolean isExpensive() { return price > 50.0; }
        // public Book withDiscount(double percentage) { 
        //     return new Book(title, author, isbn, price * (1 - percentage/100)); 
        // }
        
        System.out.println("✓ Custom methods task completed");
        System.out.println();
        
        System.out.println("=== Exercise Complete! ===");
        System.out.println("Key Takeaway: Use records for immutable data carriers to eliminate boilerplate!");
    }
    
    // Basic record for demonstration
    record EmployeeRecord(String name, int id, double salary) {
        // This is automatically immutable and gets all standard methods for free
    }
    
    // Record with validation and custom methods
    record ValidatedEmployee(String name, int id, double salary) {
        // Compact constructor for validation
        public ValidatedEmployee {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive");
            }
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
        }
        
        // Custom methods
        public String getFormattedInfo() {
            return String.format("%s (ID: %d) - $%,.2f", name, id, salary);
        }
        
        public boolean qualifiesForBonus() {
            return salary >= 70000;
        }
        
        // Immutable update method (returns new instance)
        public ValidatedEmployee withSalary(double newSalary) {
            return new ValidatedEmployee(name, id, newSalary);
        }
    }
}