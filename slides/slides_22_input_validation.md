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

## Critical Security

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
    
    // Ensure file is within allowed directory
    if (!file.getCanonicalPath()
            .startsWith(UPLOAD_DIR.getCanonicalPath())) {
        throw new SecurityException("Path traversal attempt");
    }
    
    return file;
}
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
    }
}
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
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException {
        
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; script-src 'self'");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        chain.doFilter(request, response);
    }
}
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

# Testing Security

## Validation Tests

```java
@Test
void testSqlInjectionPrevention() {
    String maliciousInput = "'; DROP TABLE employees; --";
    
    assertThrows(ValidationException.class, () ->
        validator.validate(maliciousInput)
    );
}

@Test
void testXssPrevention() {
    String xssAttempt = "<script>alert('XSS')</script>";
    String sanitized = sanitizer.clean(xssAttempt);
    
    assertFalse(sanitized.contains("<script>"));
}
```

---
transition: slide-left
---

# Security Best Practices

## Defense in Depth

<v-clicks>

- Never trust user input
- Validate on multiple layers
- Use allow-lists, not deny-lists

</v-clicks>

---
transition: slide-left
---

# Best Practices (continued)

## Implementation Tips

<v-clicks>

- Log security violations
- Fail securely (deny by default)
- Keep security libraries updated

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