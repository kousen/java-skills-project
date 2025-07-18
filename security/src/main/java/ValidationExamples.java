import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.*;
import org.springframework.web.util.HtmlUtils;

import java.lang.annotation.*;
import java.util.regex.Pattern;

/**
 * Demonstrates input validation techniques for preventing security vulnerabilities
 * like SQL injection and XSS attacks. Shows both built-in and custom validators.
 */
public class ValidationExamples {
    
    public static void main(String[] args) {
        ValidationExamples examples = new ValidationExamples();
        
        System.out.println("=== Input Validation for Security ===");
        
        // SQL injection prevention
        examples.demonstrateSQLInjectionPrevention();
        
        // XSS prevention
        examples.demonstrateXSSPrevention();
        
        // Input sanitization
        examples.demonstrateInputSanitization();
        
        // File upload validation
        examples.demonstrateFileUploadValidation();
        
        System.out.println("\n=== Validation demonstration complete ===");
    }
    
    /**
     * Demonstrates SQL injection prevention techniques.
     */
    public void demonstrateSQLInjectionPrevention() {
        System.out.println("\n--- SQL Injection Prevention ---");
        
        // Examples of dangerous input
        String[] dangerousInputs = {
            "'; DROP TABLE employees; --",
            "1' OR '1'='1",
            "admin'/*",
            "1; DELETE FROM users WHERE 't'='t",
            "'; INSERT INTO admin VALUES ('hacker','password'); --"
        };
        
        SqlInjectionValidator validator = new SqlInjectionValidator();
        
        for (String input : dangerousInputs) {
            boolean isSafe = validator.isValid(input, null);
            System.out.println("Input: \"" + input + "\" - Safe: " + isSafe);
        }
        
        // Safe inputs
        String[] safeInputs = {
            "John Doe",
            "alice@company.com",
            "Engineering Department",
            "123-456-7890"
        };
        
        System.out.println("\nSafe inputs:");
        for (String input : safeInputs) {
            boolean isSafe = validator.isValid(input, null);
            System.out.println("Input: \"" + input + "\" - Safe: " + isSafe);
        }
    }
    
    /**
     * Demonstrates XSS prevention through output encoding.
     */
    public void demonstrateXSSPrevention() {
        System.out.println("\n--- XSS Prevention Through Encoding ---");
        
        String[] xssAttempts = {
            "<script>alert('XSS')</script>",
            "<img src=x onerror=alert('XSS')>",
            "javascript:alert('XSS')",
            "<iframe src='javascript:alert(\"XSS\")'></iframe>",
            "<svg onload=alert('XSS')>",
            "<body onload=alert('XSS')>"
        };
        
        System.out.println("Original XSS attempts and their encoded versions:");
        for (String xss : xssAttempts) {
            String encoded = HtmlUtils.htmlEscape(xss);
            System.out.println("Original: " + xss);
            System.out.println("Encoded:  " + encoded);
            System.out.println("---");
        }
        
        // Demonstrate different encoding contexts
        String userInput = "<script>alert('XSS')</script>";
        System.out.println("\nContext-specific encoding:");
        System.out.println("HTML context: " + HtmlUtils.htmlEscape(userInput));
        System.out.println("JavaScript context: " + HtmlUtils.htmlEscapeDecimal(userInput));
    }
    
    /**
     * Demonstrates input sanitization techniques.
     */
    public void demonstrateInputSanitization() {
        System.out.println("\n--- Input Sanitization ---");
        
        InputSanitizer sanitizer = new InputSanitizer();
        
        String[] testInputs = {
            "Normal text input",
            "Text with <script>alert('XSS')</script> embedded",
            "'; DROP TABLE users; --",
            "Valid email@domain.com with script<script>",
            "Path traversal: ../../../etc/passwd",
            "Special chars: !@#$%^&*()_+"
        };
        
        for (String input : testInputs) {
            String sanitized = sanitizer.sanitize(input);
            System.out.println("Original:  " + input);
            System.out.println("Sanitized: " + sanitized);
            System.out.println("---");
        }
    }
    
    /**
     * Demonstrates file upload validation.
     */
    public void demonstrateFileUploadValidation() {
        System.out.println("\n--- File Upload Validation ---");
        
        FileUploadValidator validator = new FileUploadValidator();
        
        // Test various file scenarios
        FileUpload[] testFiles = {
            new FileUpload("document.pdf", "application/pdf", 1024 * 1024), // 1MB PDF
            new FileUpload("image.jpg", "image/jpeg", 2 * 1024 * 1024), // 2MB image
            new FileUpload("script.exe", "application/x-executable", 500 * 1024), // Executable
            new FileUpload("large_file.zip", "application/zip", 50 * 1024 * 1024), // 50MB zip
            new FileUpload("../../../etc/passwd", "text/plain", 1024), // Path traversal
            new FileUpload("normal.txt", "text/plain", 1024), // Normal text file
            new FileUpload("image.php.jpg", "image/jpeg", 1024 * 1024) // Double extension
        };
        
        for (FileUpload file : testFiles) {
            ValidationResult result = validator.validateFile(file);
            System.out.println("File: " + file.getFilename());
            System.out.println("Valid: " + result.isValid());
            if (!result.isValid()) {
                System.out.println("Errors: " + String.join(", ", result.getErrors()));
            }
            System.out.println("---");
        }
    }
    
    /**
     * Custom annotation for SQL injection detection.
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = SqlInjectionValidator.class)
    public @interface NoSqlInjection {
        String message() default "Input contains potentially dangerous SQL characters";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
    
    /**
     * Validator for detecting SQL injection attempts.
     */
    public static class SqlInjectionValidator implements ConstraintValidator<NoSqlInjection, String> {
        
        // Pattern to detect common SQL injection patterns
        private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            ".*(;|--|'|\"|\\*|xp_|sp_|<|>|script|alert|union|select|insert|update|delete|drop|create|alter|exec|execute).*",
            Pattern.CASE_INSENSITIVE
        );
        
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // Let @NotNull handle null validation
            }
            
            return !SQL_INJECTION_PATTERN.matcher(value).matches();
        }
    }
    
    /**
     * Input sanitizer for removing dangerous content.
     */
    public static class InputSanitizer {
        
        // Patterns for various types of dangerous content
        private static final Pattern SCRIPT_PATTERN = Pattern.compile(
            "<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        private static final Pattern HTML_PATTERN = Pattern.compile(
            "<[^>]+>", Pattern.CASE_INSENSITIVE);
        private static final Pattern SQL_PATTERN = Pattern.compile(
            "(;|--|'|\")", Pattern.CASE_INSENSITIVE);
        private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile(
            "(\\.\\./|\\.\\\\)", Pattern.CASE_INSENSITIVE);
        
        public String sanitize(String input) {
            if (input == null) {
                return null;
            }
            
            String sanitized = input;
            
            // Remove script tags
            sanitized = SCRIPT_PATTERN.matcher(sanitized).replaceAll("");
            
            // Remove HTML tags
            sanitized = HTML_PATTERN.matcher(sanitized).replaceAll("");
            
            // Remove SQL injection characters
            sanitized = SQL_PATTERN.matcher(sanitized).replaceAll("");
            
            // Remove path traversal attempts
            sanitized = PATH_TRAVERSAL_PATTERN.matcher(sanitized).replaceAll("");
            
            // Trim whitespace
            sanitized = sanitized.trim();
            
            return sanitized;
        }
        
        public String sanitizeForHTML(String input) {
            return HtmlUtils.htmlEscape(input);
        }
        
        public String sanitizeFilename(String filename) {
            if (filename == null) {
                return null;
            }
            
            // Remove path components
            String sanitized = filename.replaceAll("[\\\\/]", "");
            
            // Remove dangerous characters
            sanitized = sanitized.replaceAll("[^a-zA-Z0-9._-]", "");
            
            // Ensure it doesn't start with a dot
            if (sanitized.startsWith(".")) {
                sanitized = "file" + sanitized;
            }
            
            return sanitized;
        }
    }
    
    /**
     * File upload validator.
     */
    public static class FileUploadValidator {
        
        // Allowed file types
        private static final String[] ALLOWED_TYPES = {
            "image/jpeg", "image/png", "image/gif",
            "application/pdf", "text/plain",
            "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        };
        
        // Maximum file size (5MB)
        private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
        
        // Dangerous file extensions
        private static final String[] DANGEROUS_EXTENSIONS = {
            ".exe", ".bat", ".cmd", ".com", ".pif", ".scr", ".vbs", ".js", ".jar", ".php", ".asp", ".jsp"
        };
        
        public ValidationResult validateFile(FileUpload file) {
            ValidationResult result = new ValidationResult();
            
            // Check filename
            if (file.getFilename() == null || file.getFilename().trim().isEmpty()) {
                result.addError("Filename cannot be empty");
                return result;
            }
            
            // Check for path traversal
            if (file.getFilename().contains("..") || file.getFilename().contains("/") || file.getFilename().contains("\\")) {
                result.addError("Filename contains invalid path characters");
            }
            
            // Check file size
            if (file.getSize() > MAX_FILE_SIZE) {
                result.addError("File size exceeds maximum allowed size of " + (MAX_FILE_SIZE / 1024 / 1024) + "MB");
            }
            
            // Check content type
            boolean typeAllowed = false;
            for (String allowedType : ALLOWED_TYPES) {
                if (allowedType.equals(file.getContentType())) {
                    typeAllowed = true;
                    break;
                }
            }
            if (!typeAllowed) {
                result.addError("File type '" + file.getContentType() + "' is not allowed");
            }
            
            // Check for dangerous extensions
            String filename = file.getFilename().toLowerCase();
            for (String dangerousExt : DANGEROUS_EXTENSIONS) {
                if (filename.endsWith(dangerousExt)) {
                    result.addError("File extension '" + dangerousExt + "' is not allowed");
                    break;
                }
            }
            
            // Check for double extensions (e.g., file.php.jpg)
            String[] parts = filename.split("\\.");
            if (parts.length > 2) {
                for (int i = 0; i < parts.length - 1; i++) {
                    for (String dangerousExt : DANGEROUS_EXTENSIONS) {
                        if (dangerousExt.equals("." + parts[i])) {
                            result.addError("File contains dangerous double extension");
                            break;
                        }
                    }
                }
            }
            
            return result;
        }
    }
    
    /**
     * Employee model with validation annotations.
     */
    public static class SecureEmployee {
        
        @NotNull(message = "ID cannot be null")
        @Min(value = 1, message = "ID must be positive")
        private Long id;
        
        @NotNull(message = "Name cannot be null")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name can only contain letters and spaces")
        @NoSqlInjection
        private String name;
        
        @NotNull(message = "Email cannot be null")
        @Email(message = "Email must be valid")
        @NoSqlInjection
        private String email;
        
        @NotNull(message = "Department cannot be null")
        @Size(min = 2, max = 50, message = "Department must be between 2 and 50 characters")
        @NoSqlInjection
        private String department;
        
        @NotNull(message = "Salary cannot be null")
        @DecimalMin(value = "0.0", message = "Salary cannot be negative")
        @DecimalMax(value = "10000000.0", message = "Salary cannot exceed 10 million")
        private Double salary;
        
        // Constructors, getters, and setters...
        public SecureEmployee() {}
        
        public SecureEmployee(Long id, String name, String email, String department, Double salary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.department = department;
            this.salary = salary;
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public Double getSalary() { return salary; }
        public void setSalary(Double salary) { this.salary = salary; }
    }
    
    /**
     * Simple file upload representation.
     */
    public static class FileUpload {
        private final String filename;
        private final String contentType;
        private final long size;
        
        public FileUpload(String filename, String contentType, long size) {
            this.filename = filename;
            this.contentType = contentType;
            this.size = size;
        }
        
        public String getFilename() { return filename; }
        public String getContentType() { return contentType; }
        public long getSize() { return size; }
    }
    
    /**
     * Validation result holder.
     */
    public static class ValidationResult {
        private boolean valid = true;
        private final java.util.List<String> errors = new java.util.ArrayList<>();
        
        public boolean isValid() { return valid && errors.isEmpty(); }
        
        public void addError(String error) {
            this.valid = false;
            this.errors.add(error);
        }
        
        public java.util.List<String> getErrors() { return errors; }
    }
}