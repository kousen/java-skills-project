package com.oreilly.webservices;

/**
 * Demonstration class showing the Employee record in action.
 * This class can be run to show how the record works with immutability and the helper methods.
 */
public class EmployeeRecordDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Employee Record Demonstration ===");
        
        // Create a new employee without ID (for creation)
        Employee newEmployee = new Employee("Alice Johnson", "Engineering", 85000.0);
        System.out.println("New employee: " + newEmployee);
        System.out.println("ID is null: " + (newEmployee.id() == null));
        
        // Simulate what happens when the service assigns an ID
        Employee savedEmployee = newEmployee.withId(100L);
        System.out.println("After saving: " + savedEmployee);
        System.out.println("Original unchanged: " + newEmployee);
        
        // Demonstrate salary update with immutability
        Employee updatedEmployee = savedEmployee.withSalary(90000.0);
        System.out.println("After salary update: " + updatedEmployee);
        System.out.println("Previous version unchanged: " + savedEmployee);
        
        // Create full employee with all fields
        Employee fullEmployee = new Employee(200L, "Bob Smith", "Marketing", 75000.0);
        System.out.println("Full employee: " + fullEmployee);
        
        // Demonstrate record methods (automatically generated)
        System.out.printf("""
                
                === Record Features ===
                toString(): %s
                Name accessor: %s
                Department accessor: %s
                Salary accessor: %s
                %n""", fullEmployee, fullEmployee.name(), fullEmployee.department(), fullEmployee.salary());
        
        // Test equality (records automatically generate equals/hashCode)
        Employee copy = new Employee(200L, "Bob Smith", "Marketing", 75000.0);
        System.out.println("Equality test: " + fullEmployee.equals(copy));
        System.out.println("HashCode same: " + (fullEmployee.hashCode() == copy.hashCode()));
        
        System.out.println("""
            
            === Benefits of Using Records ===
            ✓ Automatic toString, equals, hashCode
            ✓ Immutable by default
            ✓ Compact syntax
            ✓ Works perfectly with Jackson JSON
            ✓ Works with Bean Validation
            ✓ Thread-safe due to immutability
            """);
    }
}