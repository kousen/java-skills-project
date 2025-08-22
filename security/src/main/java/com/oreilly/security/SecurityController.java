package com.oreilly.security;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Security Controller - Demonstrates security layered onto web services
 * Shows how input validation and cryptographic APIs integrate with REST endpoints
 */
@RestController
@RequestMapping("/api/security")
@Validated
public class SecurityController {
    
    private final InputValidator inputValidator = new InputValidator();
    private final CryptographicAPIs cryptoAPI = new CryptographicAPIs();
    
    /**
     * Endpoint demonstrating input validation (Topic 22)
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateInput(
            @RequestBody EmployeeDto employee) {
        
        if (employee == null) {
            return ResponseEntity.badRequest().build();
        }
        
        System.out.println("=== Security Layer: Input Validation ===");
        
        // Perform actual validation using our service
        var validationErrors = inputValidator.validateEmployee(employee);
        var businessErrors = inputValidator.validateEmployeeBusinessRules(employee);
        
        Map<String, Object> result = Map.of(
            "message", "Employee validation completed",
            "employee", employee.name(),
            "validationErrors", validationErrors,
            "businessErrors", businessErrors,
            "isValid", validationErrors.isEmpty() && businessErrors.isEmpty()
        );
        
        System.out.println("✓ Input validation complete: " + 
            (validationErrors.isEmpty() && businessErrors.isEmpty() ? "PASSED" : "FAILED"));
        return ResponseEntity.ok(result);
    }
    
    /**
     * Endpoint demonstrating password hashing (Topic 23)
     */
    @PostMapping("/hash-password")
    public ResponseEntity<Map<String, String>> hashPassword(
            @RequestBody Map<String, String> request) {
        
        String password = request.get("password");
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        System.out.println("=== Security Layer: Password Hashing ===");
        
        try {
            String hashedPassword = cryptoAPI.hashPassword(password);
            
            Map<String, String> result = Map.of(
                "message", "Password hashing demonstration",
                "hashedPassword", hashedPassword.substring(0, 20) + "...",
                "algorithm", "PBKDF2WithHmacSHA256"
            );
            
            System.out.println("✓ Password hashing complete");
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            System.err.println("✗ Password hashing failed: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint demonstrating cryptographic APIs (Topic 23)
     */
    @PostMapping("/crypto-demo")
    public ResponseEntity<Map<String, String>> cryptoDemo() {
        
        System.out.println("=== Security Layer: Cryptographic APIs ===");
        
        // Trigger all cryptographic demonstrations
        cryptoAPI.main(new String[]{});
        
        Map<String, String> result = Map.of(
            "message", "Cryptographic APIs demonstration completed",
            "demonstrations", "Password Hashing, AES Encryption, Digital Signatures, SHA Hashing, Key Derivation, Token Generation",
            "status", "Check console output for detailed examples"
        );
        
        System.out.println("✓ Cryptographic demonstrations triggered");
        return ResponseEntity.ok(result);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "module", "security",
            "topics", "22-23 (Input Validation, Cryptographic APIs)"
        ));
    }
}