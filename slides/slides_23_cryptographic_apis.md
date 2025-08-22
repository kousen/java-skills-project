---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Cryptographic APIs with Modern Java 21
  Modern encryption, hashing, and authentication patterns
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Java Cryptographic APIs with Modern Patterns
---

# Java Cryptographic APIs

Modern Encryption with Java 21 Features

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

# Modern Cryptography Goals

## Security Fundamentals

<v-clicks>

- **Confidentiality** - Keep data secret
- **Integrity** - Detect tampering
- **Authentication** - Verify identity

</v-clicks>

## Java 21 Enhancements

<v-clicks>

- **Type Safety** - Sealed interfaces for error handling
- **Concurrency** - Virtual threads for performance
- **Modern Patterns** - Pattern matching and records

</v-clicks>

---
transition: slide-left
---

# Type-Safe Error Handling

## Sealed Interface Pattern

```java
@SuppressWarnings("unused")
public sealed interface CryptoResult<T> {
    record Success<T>(T value) implements CryptoResult<T> {}
    record Error<T>(String message) implements CryptoResult<T> {}
}
```

<v-clicks>

- **Compile-time safety** - All cases must be handled
- **No exceptions** - Functional error handling
- **Generic** - Works with any data type

</v-clicks>

---
transition: slide-left
---

# Pattern Matching with Cryptography

## Modern Java 21 Syntax

```java
private void handleCryptoResult(CryptoResult<String> result, String operation) {
    switch (result) {
        case CryptoResult.Success<String>(var value) -> 
            System.out.println("✓ " + operation + " succeeded: " + 
                value.substring(0, 20) + "...");
        case CryptoResult.Error<String>(var message) -> 
            System.out.println("✗ " + operation + " failed: " + message);
    }
}
```

<v-clicks>

- **Destructuring** - Extract values directly in patterns
- **Type safety** - Compiler ensures all cases handled
- **Clean syntax** - No if-else chains needed

</v-clicks>

---
transition: slide-left
---

# Modern Encryption Method

## Functional Approach

```java
private CryptoResult<String> encryptWithResult(String plainText, SecretKey key) {
    try {
        if (key == null) {
            return new CryptoResult.Error<>("Encryption key cannot be null");
        }
        byte[] encrypted = encrypt(plainText, key);
        String encoded = Base64.getEncoder().encodeToString(encrypted);
        return new CryptoResult.Success<>(encoded);
    } catch (Exception e) {
        return new CryptoResult.Error<>(e.getMessage());
    }
}
```

<v-clicks>

- **No exceptions thrown** - Results wrapped in sealed types
- **Explicit error handling** - Null checks return errors
- **Composable** - Results can be chained functionally

</v-clicks>

---
transition: slide-left
---

# Virtual Threads for Crypto

## High-Performance Concurrency

```java
private CompletableFuture<List<String>> hashMultiplePasswordsConcurrently(
        List<String> passwords) {
    return CompletableFuture.supplyAsync(() ->
        passwords.parallelStream()
            .map(password -> {
                try {
                    return hashPassword(password);
                } catch (Exception e) {
                    return "ERROR: " + e.getMessage();
                }
            })
            .toList(),
        Executors.newVirtualThreadPerTaskExecutor()  // Virtual threads!
    );
}
```

---
transition: slide-left
---

# Why Virtual Threads?

## Performance Benefits

<v-clicks>

- **Lightweight** - Thousands of threads with minimal overhead
- **No blocking** - Perfect for I/O intensive crypto operations
- **Scalable** - Handle massive concurrent workloads

</v-clicks>

## Real-World Use Cases

<v-clicks>

- **Batch password hashing** - Process thousands concurrently
- **Data encryption** - Encrypt multiple records in parallel
- **Certificate validation** - Verify many certificates simultaneously

</v-clicks>

---
transition: slide-left
---

# Concurrent Crypto Demo

## Multiple Operations in Parallel

```java
public void demonstrateConcurrentCryptography() {
    List<String> passwords = List.of(
        "userPassword123", "adminSecure456", "guestAccess789"
    );
    
    // Hash passwords concurrently
    CompletableFuture<List<String>> hashingTask = 
        hashMultiplePasswordsConcurrently(passwords);
    List<String> hashedPasswords = hashingTask.join();
    
    // All passwords hashed simultaneously!
}
```

<v-clicks>

- **Parallel processing** - All operations run simultaneously
- **Non-blocking** - Main thread remains responsive
- **Efficient** - Virtual threads minimize resource usage

</v-clicks>

---
transition: slide-left
---

# Password Hashing with PBKDF2

## Industry Standard Implementation

```java
public String hashPassword(String password) throws Exception {
    byte[] salt = generateSalt();
    byte[] hash = pbkdf2(password.toCharArray(), salt, 
        PBKDF2_ITERATIONS, HASH_LENGTH);
    
    // Combine salt and hash
    byte[] combined = new byte[salt.length + hash.length];
    System.arraycopy(salt, 0, combined, 0, salt.length);
    System.arraycopy(hash, 0, combined, salt.length, hash.length);
    
    return Base64.getEncoder().encodeToString(combined);
}
```

<v-clicks>

- **PBKDF2** - Password-Based Key Derivation Function 2
- **Salt included** - Prevents rainbow table attacks
- **100,000 iterations** - Slow by design, prevents brute force

</v-clicks>

---
transition: slide-left
---

# AES-GCM Encryption

## Authenticated Encryption

```java
public byte[] encrypt(String plainText, SecretKey key) throws Exception {
    byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
    
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    
    byte[] iv = cipher.getIV();
    byte[] cipherText = cipher.doFinal(plainTextBytes);
    
    // Combine IV and ciphertext
    ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
    byteBuffer.put(iv);
    byteBuffer.put(cipherText);
    
    return byteBuffer.array();
}
```

---
transition: slide-left
---

# Why AES-GCM?

## Advanced Security Features

<v-clicks>

- **Confidentiality** - Data is encrypted and unreadable
- **Authentication** - Detects any tampering attempts
- **Performance** - Hardware-accelerated on modern CPUs

</v-clicks>

## Implementation Details

<v-clicks>

- **256-bit keys** - Military-grade encryption strength
- **Unique IV** - Each encryption produces different output
- **Built-in MAC** - Message Authentication Code included

</v-clicks>

---
transition: slide-left
---

# Digital Signatures

## RSA with SHA-256

```java
public byte[] signData(String data, PrivateKey privateKey) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initSign(privateKey);
    signature.update(data.getBytes(StandardCharsets.UTF_8));
    return signature.sign();
}

public boolean verifySignature(String data, byte[] signatureBytes, 
                              PublicKey publicKey) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initVerify(publicKey);
    signature.update(data.getBytes(StandardCharsets.UTF_8));
    return signature.verify(signatureBytes);
}
```

---
transition: slide-left
---

# Modern Employee Encryption

## Record-Based Data Structures

```java
public record EmployeeDataEncryption(SecretKey masterKey) {
    
    public EncryptedEmployee encryptEmployeeData(Employee employee) 
            throws Exception {
        CryptographicAPIs crypto = new CryptographicAPIs();
        
        // Encrypt sensitive fields
        byte[] encryptedSSN = crypto.encrypt(employee.ssn(), masterKey);
        byte[] encryptedSalary = crypto.encrypt(
            employee.salary().toString(), masterKey);
        
        return new EncryptedEmployee(
            employee.id(), employee.name(), employee.email(),
            employee.department(), encryptedSSN, encryptedSalary
        );
    }
}
```

---
transition: slide-left
---

# Employee Records

## Immutable Data Structures

```java
/**
 * Employee class for demonstration.
 */
public record Employee(
        Long id,
        String name,
        String email,
        String department,
        String ssn,
        Double salary
) {}

/**
 * Encrypted employee class.
 */
public record EncryptedEmployee(
        Long id, String name, String email, String department,
        byte[] encryptedSSN, byte[] encryptedSalary
) {}
```

---
transition: slide-left
---

# Secure Token Generation

## Cryptographically Strong Randomness

```java
public String generateSecureToken(int length) {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[length];
    random.nextBytes(bytes);
    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(bytes);
}
```

<v-clicks>

- **SecureRandom** - Cryptographically strong random number generator
- **URL-safe encoding** - Safe for use in URLs and tokens
- **Configurable length** - Adjust security level as needed

</v-clicks>

---
transition: slide-left
---

# Key Derivation

## Password-Based Key Generation

```java
public SecretKey deriveKeyFromPassword(String password, byte[] salt) 
        throws Exception {
    byte[] keyBytes = pbkdf2(password.toCharArray(), salt, 
        PBKDF2_ITERATIONS, 32);
    return new SecretKeySpec(keyBytes, "AES");
}

public byte[] pbkdf2(char[] password, byte[] salt, int iterations, 
                    int keyLength) throws Exception {
    PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    return skf.generateSecret(spec).getEncoded();
}
```

---
transition: slide-left
---

# Message Integrity

## SHA-256 Hashing

```java
public String sha256Hash(String input) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
    return bytesToHex(hash);
}

private String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
        result.append(String.format("%02x", b));
    }
    return result.toString();
}
```

<v-clicks>

- **One-way function** - Cannot be reversed
- **Deterministic** - Same input always produces same hash
- **Avalanche effect** - Small change = completely different hash

</v-clicks>

---
transition: slide-left
---

# Complete Modern Demo

## Full Cryptographic Demonstration

```java
public static void main(String[] args) {
    CryptographicAPIs service = new CryptographicAPIs();
    
    // Traditional demonstrations
    service.demonstratePasswordHashing();
    service.demonstrateAESEncryption();
    service.demonstrateDigitalSignatures();
    
    // Modern Java 21 features
    service.demonstrateModernErrorHandling();
    service.demonstrateConcurrentCryptography();
    
    // Real-world example
    EmployeeDataEncryption.demonstrateEmployeeEncryption();
}
```

---
transition: slide-left
---

# Modern Error Handling Demo

## Pattern Matching in Action

```java
public void demonstrateModernErrorHandling() {
    try {
        SecretKey key = generateAESKey();
        
        // Successful encryption
        var successResult = encryptWithResult("Sensitive data", key);
        handleCryptoResult(successResult, "encryption");
        
        // Error handling
        var errorResult = encryptWithResult("This will fail", null);
        handleCryptoResult(errorResult, "encryption");
        
    } catch (Exception e) {
        System.err.println("Demo error: " + e.getMessage());
    }
}
```

---
transition: slide-left
---

# Performance Comparison

## Traditional vs Virtual Threads

<v-clicks>

**Traditional Threading:**
- **Limited** - Platform threads are expensive
- **Blocking** - Thread pool exhaustion under load
- **Complex** - Manual thread management required

**Virtual Threads:**
- **Scalable** - Millions of threads possible
- **Efficient** - Minimal memory overhead
- **Simple** - Built into CompletableFuture

</v-clicks>

---
transition: slide-left
---

# Best Practices

## Modern Java Security

<v-clicks>

- **Use sealed interfaces** for error handling
- **Leverage virtual threads** for concurrent operations
- **Apply pattern matching** for cleaner code
- **Embrace records** for immutable data

</v-clicks>

## Traditional Security Rules

<v-clicks>

- **Never hardcode keys** - Use environment variables
- **Use established algorithms** - AES, RSA, SHA-256
- **Implement proper key rotation** - Regular key updates

</v-clicks>

---
transition: slide-left
---

# Common Pitfalls

## What to Avoid

<v-clicks>

- **Broken algorithms** - MD5, SHA-1, DES
- **Poor randomness** - Using Math.random() for crypto
- **Key reuse** - Same key for different purposes

</v-clicks>

## Modern Mistakes

<v-clicks>

- **Ignoring sealed types** - Missing error cases
- **Platform thread usage** - Not leveraging virtual threads
- **Exception-based flow** - Instead of result types

</v-clicks>

---
transition: slide-left
---

# Integration with Spring Boot

## Security Configuration

```java
@Configuration
@EnableWebSecurity
public class CryptoSecurityConfig {
    
    @Bean
    public CryptographicAPIs cryptoService() {
        return new CryptographicAPIs();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Strong work factor
    }
}
```

<v-clicks>

- **Bean configuration** - Dependency injection ready
- **Spring Security integration** - Works with existing auth
- **Production ready** - Proper configuration management

</v-clicks>

---
transition: slide-left
---

# Real-World Example

## Complete Employee Service

```java
@Service
public class SecureEmployeeService {
    private final CryptographicAPIs cryptoAPIs;
    private final SecretKey masterKey;
    
    public CompletableFuture<List<EncryptedEmployee>> 
            encryptEmployeesBatch(List<Employee> employees) {
        
        return CompletableFuture.supplyAsync(() ->
            employees.parallelStream()
                .map(this::encryptEmployee)
                .collect(toList()),
            Executors.newVirtualThreadPerTaskExecutor()
        );
    }
}
```

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- **Modern patterns** - Sealed interfaces and pattern matching
- **High performance** - Virtual threads for concurrency
- **Type safety** - Compile-time error handling
- **Industry standard** - AES, RSA, PBKDF2 algorithms
- **Production ready** - Spring Boot integration

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Git Collaboration

Version control workflows and team development