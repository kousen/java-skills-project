import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ModernJavaFeatures {
    
    public static void main(String[] args) {
        System.out.println("=== Modern Java Features Demo ===");
        
        demonstrateVar();
        demonstrateRecords();
        demonstratePatternMatching();
        demonstrateTextBlocks();
    }
    
    private static void demonstrateVar() {
        System.out.println("\n--- var keyword (Java 10+) ---");
        
        // Type inference with var
        var employeeName = "Alice Johnson";  // String
        var employeeId = 12345;             // int
        var salary = 75000.50;              // double
        var isActive = true;                // boolean
        var hireDate = LocalDate.now();     // LocalDate
        
        System.out.println("var examples:");
        System.out.println("Name: " + employeeName + " (type: " + employeeName.getClass().getSimpleName() + ")");
        System.out.println("ID: " + employeeId + " (type: " + ((Object) employeeId).getClass().getSimpleName() + ")");
        System.out.println("Salary: " + salary + " (type: " + ((Object) salary).getClass().getSimpleName() + ")");
        System.out.println("Active: " + isActive + " (type: " + ((Object) isActive).getClass().getSimpleName() + ")");
        System.out.println("Hire Date: " + hireDate + " (type: " + hireDate.getClass().getSimpleName() + ")");
        
        // var with collections
        var employees = List.of(
            new EmployeeRecord("Alice", 1001, 75000),
            new EmployeeRecord("Bob", 1002, 80000),
            new EmployeeRecord("Carol", 1003, 85000)
        );
        
        System.out.println("\nEmployee list size: " + employees.size());
        
        // var in enhanced for loop
        for (var employee : employees) {
            System.out.println("  " + employee);
        }
    }
    
    private static void demonstrateRecords() {
        System.out.println("\n--- Records (Java 14+) ---");
        
        // Creating record instances
        var alice = new EmployeeRecord("Alice Johnson", 1001, 75000);
        var bob = new EmployeeRecord("Bob Smith", 1002, 80000);
        var aliceClone = new EmployeeRecord("Alice Johnson", 1001, 75000);
        
        System.out.println("Record examples:");
        System.out.println("Alice: " + alice);
        System.out.println("Bob: " + bob);
        
        // Records provide equals(), hashCode(), toString() automatically
        System.out.println("Alice equals Alice clone: " + alice.equals(aliceClone));
        System.out.println("Alice equals Bob: " + alice.equals(bob));
        System.out.println("Alice hashCode: " + alice.hashCode());
        System.out.println("Alice clone hashCode: " + aliceClone.hashCode());
        
        // Records provide accessor methods
        System.out.println("Alice's name: " + alice.name());
        System.out.println("Alice's ID: " + alice.id());
        System.out.println("Alice's salary: $" + alice.salary());
        
        // Records can have methods
        System.out.println("Alice's formatted info: " + alice.getFormattedInfo());
        System.out.println("Alice qualifies for bonus: " + alice.qualifiesForBonus());
        
        // Complex record with nested data
        var address = new AddressRecord("123 Main St", "Anytown", "ST", "12345");
        var employee = new DetailedEmployeeRecord("Carol Davis", 1003, 85000, address, LocalDate.of(2020, 1, 15));
        
        System.out.println("Detailed employee: " + employee);
        System.out.println("Employee city: " + employee.address().city());
    }
    
    private static void demonstratePatternMatching() {
        System.out.println("\n--- Pattern Matching (Java 17+) ---");
        
        Object[] data = {
            "Hello World",
            42,
            new EmployeeRecord("Dave Wilson", 1004, 90000),
            null,
            75.5
        };
        
        for (var item : data) {
            var result = switch (item) {
                case null -> "null value";
                case String s -> "String with length " + s.length() + ": " + s;
                case Integer i when i > 0 -> "Positive integer: " + i;
                case Integer i -> "Non-positive integer: " + i;
                case EmployeeRecord(var name, var id, var salary) -> 
                    "Employee " + name + " (ID: " + id + ") earning $" + salary;
                case Double d -> "Double value: " + d;
                default -> "Unknown type: " + item.getClass().getSimpleName();
            };
            
            System.out.println("Pattern match result: " + result);
        }
    }
    
    private static void demonstrateTextBlocks() {
        System.out.println("\n--- Text Blocks (Java 15+) ---");
        
        var employee = new EmployeeRecord("Eve Brown", 1005, 95000);
        
        // Traditional string concatenation
        String traditional = "Employee Report:\n" +
                           "================\n" +
                           "Name: " + employee.name() + "\n" +
                           "ID: " + employee.id() + "\n" +
                           "Salary: $" + employee.salary() + "\n" +
                           "Status: Active";
        
        // Text block with embedded expressions
        String textBlock = """
                Employee Report:
                ================
                Name: %s
                ID: %d
                Salary: $%,.2f
                Status: Active
                
                Performance Notes:
                - Excellent communication skills
                - Strong technical abilities
                - Team player
                """.formatted(employee.name(), employee.id(), employee.salary());
        
        System.out.println("Traditional string:");
        System.out.println(traditional);
        
        System.out.println("\nText block:");
        System.out.println(textBlock);
        
        // JSON-like text block
        String jsonLike = """
                {
                  "employee": {
                    "name": "%s",
                    "id": %d,
                    "salary": %.2f,
                    "department": "Engineering",
                    "skills": ["Java", "Spring", "Microservices"]
                  }
                }
                """.formatted(employee.name(), employee.id(), employee.salary());
        
        System.out.println("JSON-like structure:");
        System.out.println(jsonLike);
    }
    
    // Record definitions
    record EmployeeRecord(String name, int id, double salary) {
        // Compact constructor for validation
        public EmployeeRecord {
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
        
        // Custom methods in records
        public String getFormattedInfo() {
            return String.format("%s (ID: %d) - $%,.2f", name, id, salary);
        }
        
        public boolean qualifiesForBonus() {
            return salary >= 70000;
        }
        
        public EmployeeRecord withSalary(double newSalary) {
            return new EmployeeRecord(name, id, newSalary);
        }
    }
    
    record AddressRecord(String street, String city, String state, String zipCode) {
        public AddressRecord {
            if (street == null || street.trim().isEmpty()) {
                throw new IllegalArgumentException("Street cannot be null or empty");
            }
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("City cannot be null or empty");
            }
        }
        
        public String getFullAddress() {
            return String.format("%s, %s, %s %s", street, city, state, zipCode);
        }
    }
    
    record DetailedEmployeeRecord(
        String name, 
        int id, 
        double salary, 
        AddressRecord address, 
        LocalDate hireDate
    ) {
        public DetailedEmployeeRecord {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (address == null) {
                throw new IllegalArgumentException("Address cannot be null");
            }
            if (hireDate == null) {
                throw new IllegalArgumentException("Hire date cannot be null");
            }
        }
        
        public int getYearsOfService() {
            return java.time.Period.between(hireDate, LocalDate.now()).getYears();
        }
    }
}