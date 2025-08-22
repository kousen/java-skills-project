---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Input Validation for Security
  Prevent SQL injection and XSS attacks
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Input Validation for Security
---

# Input Validation for Security

Preventing SQL Injection and XSS Attacks

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Press Space for next page <carbon:arrow-right class="inline"/>
  </span>
</div>

---

# Contact Info

Ken Kousen<br>
Kousen IT, Inc.

- ken.kousen@kousenit.com
- http://www.kousenit.com
- http://kousenit.org (blog)
- Social Media:
  - [@kenkousen](https://twitter.com/kenkousen) (Twitter)
  - [@kousenit.com](https://bsky.app/profile/kousenit.com) (Bluesky)
  - [https://www.linkedin.com/in/kenkousen/](https://www.linkedin.com/in/kenkousen/) (LinkedIn)
- *Tales from the jar side* (free newsletter)
  - https://kenkousen.substack.com
  - https://youtube.com/@talesfromthejarside

---
transition: slide-left
---

# Why Input Validation Matters

## The Threat Landscape

<v-clicks>

- Users can be malicious
- Automated attacks are common
- One vulnerability can compromise everything

</v-clicks>

## Common Attack Vectors

<v-clicks>

- SQL Injection
- Cross-Site Scripting (XSS)
- Command Injection

</v-clicks>

---
transition: slide-left
---

# SQL Injection Explained

## How It Works

```java
// DANGEROUS - Never do this!
String query = "SELECT * FROM employees WHERE name = '" 
    + userInput + "'";

// If userInput is: Robert'; DROP TABLE employees; --
// Query becomes: 
// SELECT * FROM employees WHERE name = 'Robert'; 
// DROP TABLE employees; --'
```

---
transition: slide-left
---

# Preventing SQL Injection

## Use Prepared Statements

```java
// SAFE - Always use parameterized queries
String sql = "SELECT * FROM employees WHERE name = ?";
PreparedStatement pstmt = connection.prepareStatement(sql);
pstmt.setString(1, userInput);
ResultSet rs = pstmt.executeQuery();
```

---
transition: slide-left
---

# JPA/Hibernate Safety

## Named Parameters

```java
@Repository
public class EmployeeRepository {
    @PersistenceContext
    private EntityManager em;
    
    public List<Employee> findByName(String name) {
        return em.createQuery(
            "SELECT e FROM Employee e WHERE e.name = :name", 
            Employee.class)
            .setParameter("name", name)
            .getResultList();
    }
}
```

---
transition: slide-left
---

# Spring Data JPA

## Built-in Protection

```java
@Repository
public interface EmployeeRepository 
        extends JpaRepository<Employee, Long> {
    
    // Automatically safe from SQL injection
    List<Employee> findByName(String name);
    
    @Query("SELECT e FROM Employee e WHERE e.department = ?1")
    List<Employee> findByDepartment(String department);
}
```

---
transition: slide-left
---

# Cross-Site Scripting (XSS)

## The Danger

```java
// DANGEROUS - User input displayed directly
String comment = request.getParameter("comment");
response.getWriter().write("<p>" + comment + "</p>");

// If comment contains:
// <script>alert('XSS');</script>
// The script executes in the user's browser!
```

---
transition: slide-left
---

# XSS Prevention

## Output Encoding

```java
import org.springframework.web.util.HtmlUtils;

// SAFE - HTML encode all output
String safeComment = HtmlUtils.htmlEscape(userInput);
response.getWriter().write("<p>" + safeComment + "</p>");

// <script> becomes &lt;script&gt;
```

---
transition: slide-left
---

# Bean Validation

## First Line of Defense

```java
import jakarta.validation.constraints.*;

public class Employee {
    @NotNull
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[a-zA-Z ]+$")
    private String name;
    
    @Email
    private String email;
    
    @Min(0)
    @Max(1000000)
    private Double salary;
}
```

---
transition: slide-left
---

# Custom Validators

## Business Rule Validation

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoSqlInjectionValidator.class)
public @interface NoSqlInjection {
    String message() default "Input contains forbidden characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

---
transition: slide-left
---

# Validator Implementation

## Custom Logic

```java
public class NoSqlInjectionValidator 
        implements ConstraintValidator<NoSqlInjection, String> {
    
    private static final Pattern SQL_PATTERN = Pattern.compile(
        ".*(;|--|'|\"|\\*|xp_|sp_|<|>|script|alert).*", 
        Pattern.CASE_INSENSITIVE
    );
    
    @Override
    public boolean isValid(String value, 
            ConstraintValidatorContext context) {
        if (value == null) return true;
        return !SQL_PATTERN.matcher(value).matches();
    }
}
```

---
transition: slide-left
---

# Validation in Controllers

## Spring Integration

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody Employee employee,
            BindingResult result) {
        
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        
        return ResponseEntity.ok(service.save(employee));
    }
}
```

---
transition: slide-left
---

# Global Validation Handler

## Consistent Error Responses

```java
@RestControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        return ResponseEntity.badRequest().body(errors);
    }
}
```

---
transition: slide-left
---

# Sanitization vs Validation

## Different Approaches

<v-clicks>

**Validation**: Reject invalid input
```java
if (!isValidEmail(input)) {
    throw new ValidationException("Invalid email");
}
```

**Sanitization**: Clean the input
```java
String cleaned = input.replaceAll("[^a-zA-Z0-9]", "");
```

</v-clicks>

---
transition: slide-left
---

# Input Length Limits

## Prevent DoS Attacks

```java
@Component
public class RequestSizeFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException {
        
        if (request.getContentLength() > 1_000_000) { // 1MB
            response.sendError(413, "Request too large");
            return;
        }
        
        chain.doFilter(request, response);
    }
}
```

---
transition: slide-left
---

# File Upload Validation

## Critical Security Checks

```java
@PostMapping("/upload")
public ResponseEntity<String> uploadFile(
        @RequestParam("file") MultipartFile file) {
    
    // Validate file type
    String contentType = file.getContentType();
    if (!Arrays.asList("image/jpeg", "image/png")
            .contains(contentType)) {
        throw new ValidationException("Invalid file type");
    }
    // Additional validations...
}
```

---
transition: slide-left
---

# File Upload Security Rules

## Size and Filename Validation

```java
// Validate file size
if (file.getSize() > 5_000_000) { // 5MB
    throw new ValidationException("File too large");
}

// Validate filename
String filename = Paths.get(file.getOriginalFilename())
    .getFileName().toString();
if (!filename.matches("[a-zA-Z0-9._-]+")) {
    throw new ValidationException("Invalid filename");
}
```

---
transition: slide-left
---

# Path Traversal Prevention

## Directory Security

```java
public File getSafeFile(String filename) {
    // Remove path traversal attempts
    filename = filename.replaceAll("\\.\\./", "");
    filename = filename.replaceAll("\\.\\.\\\\", "");
    
    File file = new File(UPLOAD_DIR, filename);
    // Additional safety checks...
}
```

---
transition: slide-left
---

# Directory Boundary Checks

## Prevent Path Traversal

```java
// Ensure file is within allowed directory
if (!file.getCanonicalPath()
        .startsWith(UPLOAD_DIR.getCanonicalPath())) {
    throw new SecurityException("Path traversal attempt");
}

return file;
```

---
transition: slide-left
---

# Rate Limiting

## Prevent Abuse

```java
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final Map<String, List<Long>> requestCounts = 
        new ConcurrentHashMap<>();
    
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        // Rate limiting logic
    }
}
```

---
transition: slide-left
---

# Rate Limiting Logic

## Request Tracking

```java
String ip = request.getRemoteAddr();
List<Long> timestamps = requestCounts.computeIfAbsent(
    ip, k -> new ArrayList<>());

long now = System.currentTimeMillis();
timestamps.removeIf(t -> now - t > 60000); // 1 minute

if (timestamps.size() >= 100) { // 100 requests per minute
    response.setStatus(429); // Too Many Requests
    return false;
}

timestamps.add(now);
return true;
```

---
transition: slide-left
---

# Content Security Policy

## Browser Protection

```java
@Component
public class SecurityHeadersFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain) {
        // Set security headers
        chain.doFilter(request, response);
    }
}
```

---
transition: slide-left
---

# Security Headers

## Essential Protection Headers

```java
response.setHeader("Content-Security-Policy", 
    "default-src 'self'; script-src 'self'");
response.setHeader("X-Content-Type-Options", "nosniff");
response.setHeader("X-Frame-Options", "DENY");
response.setHeader("X-XSS-Protection", "1; mode=block");
```

---
transition: slide-left
---

# OWASP Encoder

## Professional XSS Prevention

```java
import org.owasp.encoder.Encode;

public class SafeRenderer {
    
    public String renderHtml(String userInput) {
        return "<div>" + Encode.forHtml(userInput) + "</div>";
    }
    
    public String renderJavaScript(String userInput) {
        return "var data = '" + 
            Encode.forJavaScript(userInput) + "';";
    }
    
    public String renderUrl(String userInput) {
        return "<a href='/search?q=" + 
            Encode.forUriComponent(userInput) + "'>Link</a>";
    }
}
```

---
transition: slide-left
---

# Test-Driven Security Validation

## Modern JUnit 5 Approach

```java
@DisplayName("Input Validation Security Tests")
class InputValidationTest {
    private final InputValidator validator = new InputValidator();
    
    @Nested
    @DisplayName("SQL Injection Prevention")
    class SqlInjectionPreventionTest {
        // 61 comprehensive tests covering all scenarios
    }
}
```

---
transition: slide-left
---

# Parameterized Security Tests

## Testing Attack Patterns

```java
@ParameterizedTest
@ValueSource(strings = {
    "Bob'; DROP TABLE employees; --",
    "Carol' OR '1'='1", 
    "Dave'; INSERT INTO users VALUES('hacker', 'password'); --"
})
void sqlInjectionAttemptsShouldBeDetected(String maliciousInput) {
    assertThat(validator.isSqlSafeInput(maliciousInput))
        .as("Input '%s' should be detected as SQL injection", 
            maliciousInput)
        .isFalse();
}
```

---
transition: slide-left
---

# Modern Java Validation Service

## InputValidator Design

```java
public class InputValidator {
    // Business constants as public finals
    public static final double MIN_SALARY = 30000.0;
    public static final double MAX_SALARY = 1000000.0;
    
    // Pattern-based validation
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-Z\\s\\-']+$");
}
```

---
transition: slide-left
---

# Testable Method Design

## Package-Private for Testing

```java
// Main validation methods
public List<String> validateEmployee(EmployeeDto employee) {
    // Returns specific error messages
}

// Package-private methods for testing
boolean isValidName(String name) { /* validation logic */ }
boolean isSqlSafeInput(String input) { /* security logic */ }
boolean containsPotentialXss(String input) { /* XSS detection */ }
```

---
transition: slide-left
---

# Comprehensive Test Coverage

## 61 Tests Covering All Security Scenarios

<v-clicks>

- **Field Validation Tests:** Name patterns, email formats, phone numbers
- **Security Tests:** SQL injection detection, XSS prevention  
- **Business Rule Tests:** Domain policies, salary thresholds

</v-clicks>

---
transition: slide-left
---

# Test Categories

## Complete Security Coverage

<v-clicks>

**Name Validation:**
- Pattern matching, length limits, XSS detection

**Injection Prevention:**  
- SQL injection patterns, dangerous keywords

**Business Rules:**
- Email domain allowlisting, executive approval thresholds

</v-clicks>

---
transition: slide-left
---

# XSS Prevention in Action

## Layered Name Validation

```java
boolean validateEmployeeName(String name) {
    // First: Basic pattern validation
    if (!isValidName(name)) {
        return false;
    }
    
    // Second: XSS pattern detection  
    return !containsPotentialXss(name);
}
```

---
transition: slide-left
---

# XSS Pattern Detection

## Dangerous Content Scanning

```java
private boolean containsPotentialXss(String input) {
    String[] xssPatterns = {
        "<script", "</script", "javascript:", "onload=", 
        "<iframe", "<object", "vbscript:", "data:text/html"
    };
    // Check for dangerous patterns
}
```

---
transition: slide-left
---

# REST API Integration

## Security Controller

```java
@RestController
@RequestMapping("/api/security")
public class SecurityController {
    private final InputValidator inputValidator = new InputValidator();
    
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateInput(
            @RequestBody EmployeeDto employee) {
        // Perform validation and return results
    }
}
```

---
transition: slide-left
---

# Real Validation Results

## Structured JSON Response

```java
var validationErrors = inputValidator.validateEmployee(employee);
var businessErrors = inputValidator.validateEmployeeBusinessRules(employee);

return ResponseEntity.ok(Map.of(
    "validationErrors", validationErrors,
    "businessErrors", businessErrors,
    "isValid", validationErrors.isEmpty() && businessErrors.isEmpty()
));
```

---
transition: slide-left
---

# Security Best Practices

## Modern Approach Benefits

<v-clicks>

- **Test-Driven Security:** 61 comprehensive tests verify all logic
- **Separation of Concerns:** Field vs. business rule validation  
- **Production Ready:** Constants, error messages, REST integration

</v-clicks>

---
transition: slide-left
---

# Why This Approach Works

## Confidence Through Testing

<v-clicks>

**Systematic Coverage:**
- Parameterized tests cover edge cases
- No security validation goes untested

**Clean Architecture:**
- Package-private methods enable testing
- Clear separation of validation and HTTP concerns

</v-clicks>

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- SQL Injection: Use prepared statements
- XSS: Encode all output
- Validate input with Bean Validation
- Implement defense in depth
- Test security thoroughly

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Cryptographic APIs

Encrypting data and authenticating users