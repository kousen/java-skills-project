package com.oreilly.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Demonstrates Java cryptographic APIs for encryption, hashing, and digital signatures.
 * Shows best practices for password hashing, data encryption, and key management.
 * Enhanced with Java 21 features including virtual threads and pattern matching.
 */
public class CryptographicAPIs {

    /**
     * Modern result type for cryptographic operations using sealed interfaces.
     * Demonstrates Java 21 pattern matching and functional error handling.
     */
    @SuppressWarnings("unused")
    public sealed interface CryptoResult<T> {
        record Success<T>(T value) implements CryptoResult<T> {}
        record Error<T>(String message) implements CryptoResult<T> {}
    }

    // AES encryption constants
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;

    // PBKDF2 constants for password hashing
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int PBKDF2_ITERATIONS = 100000;
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;

    public static void main(String[] args) {
        var service = new CryptographicAPIs();

        System.out.println("=== Java Cryptography Demonstration ===");

        // Password hashing
        service.demonstratePasswordHashing();

        // AES encryption
        service.demonstrateAESEncryption();

        // Digital signatures
        service.demonstrateDigitalSignatures();

        // Message hashing
        service.demonstrateMessageHashing();

        // Key derivation
        service.demonstrateKeyDerivation();

        // Secure token generation
        service.demonstrateTokenGeneration();

        // Employee data encryption example
        EmployeeDataEncryption.demonstrateEmployeeEncryption();

        // Modern Java 21 features
        service.demonstrateModernErrorHandling();
        service.demonstrateConcurrentCryptography();

        System.out.println("\n=== Cryptography demonstration complete ===");
    }

    /**
     * Demonstrates secure password hashing with PBKDF2.
     */
    public void demonstratePasswordHashing() {
        System.out.println("\n--- Password Hashing with PBKDF2 ---");

        try {
            String plainPassword = "mySecretPassword123!";

            // Hash the password
            String hashedPassword = hashPassword(plainPassword);
            System.out.println("Original password: " + plainPassword);
            System.out.println("Hashed password: " + hashedPassword);

            // Verify the password
            boolean isValid = verifyPassword(plainPassword, hashedPassword);
            System.out.println("Password verification: " + isValid);

            // Show that same password produces different hashes (due to salt)
            String hash2 = hashPassword(plainPassword);
            System.out.println("Second hash (different salt): " + hash2);
            System.out.println("Both hashes verify: " + verifyPassword(plainPassword, hash2));

            // Demonstrate wrong password
            boolean wrongPassword = verifyPassword("wrongPassword", hashedPassword);
            System.out.println("Wrong password verification: " + wrongPassword);

        } catch (Exception e) {
            System.err.println("Password hashing error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates AES encryption for sensitive data.
     */
    public void demonstrateAESEncryption() {
        System.out.println("\n--- AES Encryption for Sensitive Data ---");

        try {
            // Generate a secret key
            SecretKey secretKey = generateAESKey();
            System.out.println("Generated AES key: " +
                               Base64.getEncoder().encodeToString(secretKey.getEncoded()));

            // Sensitive employee data
            String sensitiveData = "SSN: 123-45-6789, Credit Card: 4111-1111-1111-1111";
            System.out.println("Original data: " + sensitiveData);

            // Encrypt the data
            byte[] encryptedData = encrypt(sensitiveData, secretKey);
            String encodedEncrypted = Base64.getEncoder().encodeToString(encryptedData);
            System.out.println("Encrypted data (Base64): " + encodedEncrypted);

            // Decrypt the data
            String decryptedData = decrypt(encryptedData, secretKey);
            System.out.println("Decrypted data: " + decryptedData);

            // Show encryption produces different results each time (due to IV)
            byte[] encryptedData2 = encrypt(sensitiveData, secretKey);
            String encodedEncrypted2 = Base64.getEncoder().encodeToString(encryptedData2);
            System.out.println("Second encryption (different IV): " + encodedEncrypted2);

        } catch (Exception e) {
            System.err.println("AES encryption error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates digital signatures for data authenticity.
     */
    public void demonstrateDigitalSignatures() {
        System.out.println("\n--- Digital Signatures for Authenticity ---");

        try {
            // Generate RSA key pair
            KeyPair keyPair = generateRSAKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            System.out.println("Generated RSA key pair");
            System.out.println("Public key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));

            // Data to sign
            String employeeData = "Employee: John Doe, Salary: $75000, Department: Engineering";
            System.out.println("Data to sign: " + employeeData);

            // Sign the data
            byte[] signature = signData(employeeData, privateKey);
            String encodedSignature = Base64.getEncoder().encodeToString(signature);
            System.out.println("Digital signature: " + encodedSignature);

            // Verify the signature
            boolean isValid = verifySignature(employeeData, signature, publicKey);
            System.out.println("Signature verification: " + isValid);

            // Show that tampering is detected
            String tamperedData = "Employee: John Doe, Salary: $95000, Department: Engineering";
            boolean tamperedVerification = verifySignature(tamperedData, signature, publicKey);
            System.out.println("Tampered data verification: " + tamperedVerification);

        } catch (Exception e) {
            System.err.println("Digital signature error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates message hashing for integrity.
     */
    public void demonstrateMessageHashing() {
        System.out.println("\n--- Message Hashing for Integrity ---");

        try {
            String message = "Employee record: ID=1001, Name=Alice Johnson, Status=Active";
            System.out.println("Original message: " + message);

            // SHA-256 hash
            String hash = sha256Hash(message);
            System.out.println("SHA-256 hash: " + hash);

            // Show that small changes produce very different hashes
            String modifiedMessage = "Employee record: ID=1001, Name=Alice Johnson, Status=Inactive";
            String modifiedHash = sha256Hash(modifiedMessage);
            System.out.println("Modified message: " + modifiedMessage);
            System.out.println("Modified hash: " + modifiedHash);

            // Verify integrity
            boolean integrityCheck = hash.equals(sha256Hash(message));
            System.out.println("Integrity check (original): " + integrityCheck);

            boolean tamperedCheck = hash.equals(modifiedHash);
            System.out.println("Integrity check (modified): " + tamperedCheck);

        } catch (Exception e) {
            System.err.println("Hashing error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates key derivation from passwords.
     */
    public void demonstrateKeyDerivation() {
        System.out.println("\n--- Key Derivation from Passwords ---");

        try {
            String password = "userPassword123";
            byte[] salt = generateSalt();

            System.out.println("Password: " + password);
            System.out.println("Salt: " + Base64.getEncoder().encodeToString(salt));

            // Derive encryption key from password
            SecretKey derivedKey = deriveKeyFromPassword(password, salt);
            System.out.println("Derived key: " + Base64.getEncoder().encodeToString(derivedKey.getEncoded()));

            // Use derived key for encryption
            String data = "Confidential employee information";
            byte[] encrypted = encrypt(data, derivedKey);
            String decrypted = decrypt(encrypted, derivedKey);

            System.out.println("Original: " + data);
            System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));
            System.out.println("Decrypted: " + decrypted);

        } catch (Exception e) {
            System.err.println("Key derivation error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates secure token generation.
     */
    public void demonstrateTokenGeneration() {
        System.out.println("\n--- Secure Token Generation ---");

        try {
            // Generate session tokens
            String sessionToken = generateSecureToken(32);
            System.out.println("Session token: " + sessionToken);

            // Generate API keys
            String apiKey = generateSecureToken(64);
            System.out.println("API key: " + apiKey);

            // Generate password reset tokens
            String resetToken = generateSecureToken(24);
            System.out.println("Password reset token: " + resetToken);

            // Show tokens are unique
            String anotherToken = generateSecureToken(32);
            System.out.println("Another token: " + anotherToken);

        } catch (Exception e) {
            System.err.println("Token generation error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates modern error handling with sealed interfaces and pattern matching.
     * Shows Java 21 patterns for functional cryptographic operations.
     */
    public void demonstrateModernErrorHandling() {
        System.out.println("\n--- Modern Error Handling with Pattern Matching ---");

        try {
            SecretKey key = generateAESKey();
            
            // Demonstrate successful encryption
            var successResult = encryptWithResult("Sensitive employee data", key);
            handleCryptoResult(successResult, "encryption");

            // Demonstrate error handling (simulate error by using null key)
            var errorResult = encryptWithResult("This will fail", null);
            handleCryptoResult(errorResult, "encryption");

        } catch (Exception e) {
            System.err.println("Modern error handling demo error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates concurrent cryptography using virtual threads.
     * Shows Java 21 virtual threads for high-performance crypto operations.
     */
    public void demonstrateConcurrentCryptography() {
        System.out.println("\n--- Concurrent Cryptography with Virtual Threads ---");

        try {
            // Multiple passwords to hash concurrently
            List<String> passwords = List.of(
                "userPassword123",
                "adminSecure456", 
                "guestAccess789",
                "managerKey101",
                "developerAuth202"
            );

            System.out.println("Hashing " + passwords.size() + " passwords concurrently...");
            
            // Use virtual threads for concurrent hashing
            CompletableFuture<List<String>> hashingTask = hashMultiplePasswordsConcurrently(passwords);
            List<String> hashedPasswords = hashingTask.join();

            System.out.println("Successfully hashed all passwords:");
            for (int i = 0; i < passwords.size(); i++) {
                System.out.println("Password " + (i + 1) + " hash: " + 
                    hashedPasswords.get(i).substring(0, 20) + "...");
            }

            // Demonstrate concurrent encryption
            List<String> dataToEncrypt = List.of(
                "SSN: 123-45-6789",
                "Credit Card: 4111-1111-1111-1111", 
                "Bank Account: 987654321"
            );

            System.out.println("\nEncrypting " + dataToEncrypt.size() + " pieces of data concurrently...");
            
            SecretKey key = generateAESKey();
            CompletableFuture<List<String>> encryptionTask = encryptMultipleDataConcurrently(dataToEncrypt, key);
            List<String> encryptedData = encryptionTask.join();

            System.out.println("Successfully encrypted all data:");
            for (int i = 0; i < encryptedData.size(); i++) {
                System.out.println("Encrypted data " + (i + 1) + ": " + 
                    encryptedData.get(i).substring(0, 20) + "...");
            }

        } catch (Exception e) {
            System.err.println("Concurrent crypto demo error: " + e.getMessage());
        }
    }

    /**
     * Modern encryption with result type - demonstrates sealed interfaces.
     */
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

    /**
     * Handle crypto results using pattern matching - demonstrates modern Java 21 syntax.
     */
    @SuppressWarnings("SameParameterValue")
    private void handleCryptoResult(CryptoResult<String> result, String operation) {
        switch (result) {
            case CryptoResult.Success<String>(var value) -> 
                System.out.println("✓ " + operation + " succeeded: " + value.substring(0, 20) + "...");
            case CryptoResult.Error<String>(var message) -> 
                System.out.println("✗ " + operation + " failed: " + message);
        }
    }

    /**
     * Hash multiple passwords concurrently using virtual threads.
     */
    private CompletableFuture<List<String>> hashMultiplePasswordsConcurrently(List<String> passwords) {
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
            Executors.newVirtualThreadPerTaskExecutor()
        );
    }

    /**
     * Encrypt multiple data items concurrently using virtual threads.
     */
    private CompletableFuture<List<String>> encryptMultipleDataConcurrently(List<String> dataItems, SecretKey key) {
        return CompletableFuture.supplyAsync(() ->
            dataItems.parallelStream()
                .map(data -> {
                    try {
                        byte[] encrypted = encrypt(data, key);
                        return Base64.getEncoder().encodeToString(encrypted);
                    } catch (Exception e) {
                        return "ERROR: " + e.getMessage();
                    }
                })
                .toList(),
            Executors.newVirtualThreadPerTaskExecutor()
        );
    }

    /**
     * Hash a password using PBKDF2.
     */
    public String hashPassword(String password) throws Exception {
        byte[] salt = generateSalt();
        byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_LENGTH);

        // Combine salt and hash
        byte[] combined = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hash, 0, combined, salt.length, hash.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    /**
     * Verify a password against its hash.
     */
    public boolean verifyPassword(String password, String storedHash) throws Exception {
        byte[] combined = Base64.getDecoder().decode(storedHash);

        // Extract salt and hash
        byte[] salt = new byte[SALT_LENGTH];
        byte[] hash = new byte[HASH_LENGTH];
        System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
        System.arraycopy(combined, SALT_LENGTH, hash, 0, HASH_LENGTH);

        // Hash the provided password with the extracted salt
        byte[] testHash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_LENGTH);

        return MessageDigest.isEqual(hash, testHash);
    }

    /**
     * Generate random salt.
     */
    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * PBKDF2 key derivation.
     */
    public byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Generate AES secret key.
     */
    public SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGen.init(256); // 256-bit key
        return keyGen.generateKey();
    }

    /**
     * Derive AES key from password.
     */
    public SecretKey deriveKeyFromPassword(String password, byte[] salt) throws Exception {
        byte[] keyBytes = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, 32);
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }

    /**
     * Encrypt data using AES-GCM.
     */
    public byte[] encrypt(String plainText, SecretKey key) throws Exception {
        byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] iv = cipher.getIV();
        byte[] cipherText = cipher.doFinal(plainTextBytes);

        // Combine IV and ciphertext
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);

        return byteBuffer.array();
    }

    /**
     * Decrypt data using AES-GCM.
     */
    public String decrypt(byte[] encryptedData, SecretKey key) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);

        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plainTextBytes = cipher.doFinal(cipherText);
        return new String(plainTextBytes, StandardCharsets.UTF_8);
    }

    /**
     * Generate RSA key pair for digital signatures.
     */
    public KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /**
     * Sign data with RSA private key.
     */
    public byte[] signData(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    /**
     * Verify signature with RSA public key.
     */
    public boolean verifySignature(String data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(signatureBytes);
    }

    /**
     * Generate SHA-256 hash of a message.
     */
    public String sha256Hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    /**
     * Generate cryptographically secure random token.
     */
    public String generateSecureToken(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    /**
     * Convert byte array to hexadecimal string.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Employee data encryption utility.
     */
    public record EmployeeDataEncryption(SecretKey masterKey) {

        public EncryptedEmployee encryptEmployeeData(Employee employee) throws Exception {
            CryptographicAPIs crypto = new CryptographicAPIs();

            // Encrypt sensitive fields
            byte[] encryptedSSN = crypto.encrypt(employee.ssn(), masterKey);
            byte[] encryptedSalary = crypto.encrypt(employee.salary()
                    .toString(), masterKey);

            return new EncryptedEmployee(
                    employee.id(),
                    employee.name(),
                    employee.email(),
                    employee.department(),
                    encryptedSSN,
                    encryptedSalary
            );
        }

        public Employee decryptEmployeeData(EncryptedEmployee encryptedEmployee) throws Exception {
            CryptographicAPIs crypto = new CryptographicAPIs();

            // Decrypt sensitive fields
            String ssn = crypto.decrypt(encryptedEmployee.encryptedSSN(), masterKey);
            String salaryStr = crypto.decrypt(encryptedEmployee.encryptedSalary(), masterKey);
            Double salary = Double.parseDouble(salaryStr);

            return new Employee(
                    encryptedEmployee.id(),
                    encryptedEmployee.name(),
                    encryptedEmployee.email(),
                    encryptedEmployee.department(),
                    ssn,
                    salary
            );
        }

        public static void demonstrateEmployeeEncryption() {
            System.out.println("\n--- Employee Data Encryption Example ---");

            try {
                // Generate pass key
                CryptographicAPIs crypto = new CryptographicAPIs();
                SecretKey masterKey = crypto.generateAESKey();
                EmployeeDataEncryption encryption = new EmployeeDataEncryption(masterKey);

                // Create employee with sensitive data
                Employee employee = new Employee(
                        1001L,
                        "Alice Johnson",
                        "alice@company.com",
                        "Engineering",
                        "123-45-6789",
                        85000.0
                );

                System.out.println("Original employee: " + employee);

                // Encrypt employee data
                EncryptedEmployee encrypted = encryption.encryptEmployeeData(employee);
                System.out.println("Encrypted SSN: " + Base64.getEncoder()
                        .encodeToString(encrypted.encryptedSSN()));
                System.out.println("Encrypted Salary: " + Base64.getEncoder()
                        .encodeToString(encrypted.encryptedSalary()));

                // Decrypt employee data
                Employee decrypted = encryption.decryptEmployeeData(encrypted);
                System.out.println("Decrypted employee: " + decrypted);

            } catch (Exception e) {
                System.err.println("Employee encryption error: " + e.getMessage());
            }
        }
    }

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
    ) {
    }

    /**
     * Encrypted employee class.
     */
    public record EncryptedEmployee(
            Long id,
            String name,
            String email,
            String department,
            byte[] encryptedSSN,
            byte[] encryptedSalary
    ) {
    }
}