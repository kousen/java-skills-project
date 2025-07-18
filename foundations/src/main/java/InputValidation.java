import java.util.Set;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Input Validation Examples - Defensive Programming
 * Demonstrates validation techniques without external validation frameworks.
 * Shows protection against SQL injection, XSS, and other security threats.
 */
public class InputValidation {
    
    public static void main(String[] args) {
        System.out.println("=== Input Validation Demo ===");
        
        demonstrateBasicValidation();
        demonstrateEmployeeValidation();
        demonstrateSqlInjectionPrevention();
        demonstrateXssPreventionBasics();
        demonstrateBusinessRuleValidation();
    }
    
    private static void demonstrateBasicValidation() {
        System.out.println("\n--- Basic Input Validation ---");
        
        // Test valid employee
        EmployeeDto validEmployee = new EmployeeDto(
            "Alice Johnson",
            "alice.johnson@company.com",
            85000.00,
            "+1-555-123-4567",
            "123 Main St, Anytown, ST 12345"
        );
        
        validateEmployee(validEmployee);
        
        // Test invalid employee
        EmployeeDto invalidEmployee = new EmployeeDto(
            "", // Empty name
            "not-an-email", // Invalid email
            -1000.00, // Negative salary
            "123", // Invalid phone
            "" // Empty address
        );
        
        validateEmployee(invalidEmployee);
    }
    
    private static void validateEmployee(EmployeeDto employee) {
        List<String> errors = new ArrayList<>();
        
        // Validate name
        if (!isValidName(employee.getName())) {
            errors.add("Invalid name: must be 2-50 characters, letters only");
        }
        
        // Validate email
        if (!isValidEmail(employee.getEmail())) {
            errors.add("Invalid email format");
        }
        
        // Validate salary
        if (!isValidSalary(employee.getSalary())) {
            errors.add("Invalid salary: must be positive and under $1,000,000");
        }
        
        // Validate phone
        if (!isValidPhone(employee.getPhone())) {
            errors.add("Invalid phone format");
        }
        
        // Validate address
        if (!isValidAddress(employee.getAddress())) {
            errors.add("Invalid address: cannot be empty and max 200 characters");
        }
        
        if (errors.isEmpty()) {
            System.out.println("✓ Employee validation passed: " + employee.getName());
        } else {
            System.out.println("✗ Employee validation failed: " + employee.getName());
            for (String error : errors) {
                System.out.println("  - " + error);
            }
        }
    }
    
    private static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = name.trim();
        
        // Length check
        if (trimmed.length() < 2 || trimmed.length() > 50) {
            return false;
        }
        
        // Pattern check - only letters, spaces, hyphens, and apostrophes
        Pattern namePattern = Pattern.compile("^[a-zA-Z\\s\\-']+$");
        return namePattern.matcher(trimmed).matches();
    }
    
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Simple email pattern - in production use more robust validation
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return emailPattern.matcher(email.trim()).matches();
    }
    
    private static boolean isValidSalary(Double salary) {
        return salary != null && salary > 0.0 && salary <= 1000000.0;
    }
    
    private static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        
        // Allow various phone formats
        Pattern phonePattern = Pattern.compile("^\\+?[1-9]\\d{1,14}$|^\\+?1-\\d{3}-\\d{3}-\\d{4}$");
        return phonePattern.matcher(phone.trim()).matches();
    }
    
    private static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.length() <= 200;
    }
    
    private static void demonstrateEmployeeValidation() {
        System.out.println("\n--- Custom Employee Validation ---");
        
        List<String> testInputs = List.of(
            "Alice Johnson",     // Valid
            "Bob",              // Too short
            "Carol-Anne O'Brien", // Valid with special chars
            "Dave123",          // Contains numbers
            "",                 // Empty
            "VeryLongNameThatExceedsTheMaximumLengthAllowedForEmployeeNames", // Too long
            "John <script>alert('xss')</script> Doe" // Potential XSS
        );
        
        for (String name : testInputs) {
            boolean isValid = validateEmployeeName(name);
            System.out.printf("Name: %-50s -> %s%n", 
                            "'" + name + "'", 
                            isValid ? "✓ Valid" : "✗ Invalid");
        }
    }
    
    private static boolean validateEmployeeName(String name) {
        if (!isValidName(name)) {
            return false;
        }
        
        // Check for potential XSS patterns
        if (containsPotentialXss(name)) {
            return false;
        }
        
        return true;
    }
    
    private static boolean containsPotentialXss(String input) {
        if (input == null) return false;
        
        String lower = input.toLowerCase();
        String[] xssPatterns = {
            "<script", "</script", "javascript:", "onload=", "onerror=", 
            "<iframe", "<object", "<embed", "vbscript:", "data:text/html"
        };
        
        for (String pattern : xssPatterns) {
            if (lower.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
    
    private static void demonstrateSqlInjectionPrevention() {
        System.out.println("\n--- SQL Injection Prevention ---");
        
        List<String> potentiallyMaliciousInputs = List.of(
            "Alice Johnson",                           // Safe
            "Bob'; DROP TABLE employees; --",         // SQL Injection attempt
            "Carol' OR '1'='1",                       // Boolean injection
            "Dave'; INSERT INTO users VALUES('hacker', 'password'); --", // Data manipulation
            "Eve' UNION SELECT * FROM users --"       // Union-based injection
        );
        
        for (String input : potentiallyMaliciousInputs) {
            boolean isSafe = isSqlSafeInput(input);
            System.out.printf("Input: %-60s -> %s%n", 
                            "'" + input + "'", 
                            isSafe ? "✓ Safe" : "✗ Potential SQL Injection");
        }
        
        System.out.println("\nNote: Always use parameterized queries/prepared statements in real code!");
    }
    
    private static boolean isSqlSafeInput(String input) {
        if (input == null) return true;
        
        String lower = input.toLowerCase();
        String[] sqlKeywords = {
            "drop", "delete", "insert", "update", "union", "select", 
            "exec", "execute", "sp_", "xp_", "--", "/*", "*/", ";",
            "' or", "' and", "1=1", "1=2"
        };
        
        for (String keyword : sqlKeywords) {
            if (lower.contains(keyword)) {
                return false;
            }
        }
        
        // Check for suspicious quote patterns
        if (input.contains("'") && (input.contains("=") || input.contains("or") || input.contains("and"))) {
            return false;
        }
        
        return true;
    }
    
    private static void demonstrateXssPreventionBasics() {
        System.out.println("\n--- Basic XSS Prevention ---");
        
        List<String> inputs = List.of(
            "Alice Johnson",
            "<script>alert('XSS')</script>",
            "Bob & Carol",
            "<img src='x' onerror='alert(1)'>",
            "Dave said \"Hello World\"",
            "<b>Bold text</b>",
            "javascript:alert('XSS')"
        );
        
        for (String input : inputs) {
            String escaped = escapeHtml(input);
            System.out.printf("Original: %-40s%n", input);
            System.out.printf("Escaped:  %-40s%n%n", escaped);
        }
    }
    
    private static String escapeHtml(String input) {
        if (input == null) return null;
        
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
    
    private static void demonstrateBusinessRuleValidation() {
        System.out.println("\n--- Business Rule Validation ---");
        
        List<EmployeeDto> testEmployees = List.of(
            new EmployeeDto("Alice Johnson", "alice@company.com", 75000.0, "555-123-4567", "123 Main St"),
            new EmployeeDto("Bob Smith", "bob@external.com", 85000.0, "555-234-5678", "456 Oak Ave"),
            new EmployeeDto("Carol CEO", "carol@company.com", 350000.0, "555-345-6789", "789 Pine Rd"),
            new EmployeeDto("Admin User", "admin@company.com", 50000.0, "555-456-7890", "321 Elm St")
        );
        
        for (EmployeeDto employee : testEmployees) {
            List<String> businessErrors = validateEmployeeBusinessRules(employee);
            if (businessErrors.isEmpty()) {
                System.out.println("✓ Business rules passed: " + employee.getName());
            } else {
                System.out.println("✗ Business rules failed: " + employee.getName());
                for (String error : businessErrors) {
                    System.out.println("  - " + error);
                }
            }
        }
    }
    
    public static List<String> validateEmployeeBusinessRules(EmployeeDto employee) {
        List<String> errors = new ArrayList<>();
        
        // Check if email domain is allowed (company policy)
        if (employee.getEmail() != null && !isAllowedEmailDomain(employee.getEmail())) {
            errors.add("Email domain not allowed for employees");
        }
        
        // Check salary range based on position level
        if (employee.getSalary() != null) {
            if (employee.getSalary() < 30000) {
                errors.add("Salary below minimum threshold");
            }
            if (employee.getSalary() > 500000 && !isExecutiveLevel(employee)) {
                errors.add("High salary requires executive approval");
            }
        }
        
        // Check name against blacklist
        if (employee.getName() != null && isNameBlacklisted(employee.getName())) {
            errors.add("Name appears on restricted list");
        }
        
        return errors;
    }
    
    private static boolean isAllowedEmailDomain(String email) {
        String[] allowedDomains = {"company.com", "contractor.company.com", "temp.company.com"};
        String domain = email.substring(email.lastIndexOf("@") + 1).toLowerCase();
        
        for (String allowedDomain : allowedDomains) {
            if (domain.equals(allowedDomain)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isExecutiveLevel(EmployeeDto employee) {
        String name = employee.getName().toUpperCase();
        return name.contains("CEO") || name.contains("CTO") || name.contains("CFO") || name.contains("PRESIDENT");
    }
    
    private static boolean isNameBlacklisted(String name) {
        String[] blacklist = {"test user", "admin", "administrator", "root", "guest"};
        String lowerName = name.toLowerCase().trim();
        
        for (String blocked : blacklist) {
            if (lowerName.equals(blocked)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * Employee DTO for validation demonstrations.
 */
class EmployeeDto {
    private String name;
    private String email;
    private Double salary;
    private String phone;
    private String address;
    
    public EmployeeDto(String name, String email, Double salary, String phone, String address) {
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.phone = phone;
        this.address = address;
    }
    
    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Double getSalary() { return salary; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    
    @Override
    public String toString() {
        return "EmployeeDto{name='" + name + "', email='" + email + "', salary=" + salary + "}";
    }
}