package com.oreilly.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Demonstrates Java cryptographic APIs for encryption, hashing, and digital signatures.
 * Shows best practices for password hashing, data encryption, and key management.
 */
public class CryptographicAPIs {
    
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
        CryptographicAPIs service = new CryptographicAPIs();
        
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
            e.printStackTrace();
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
            System.out.println("Generated AES key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
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
    public byte[] generateSalt() throws Exception {
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
        byte[] plainTextBytes = plainText.getBytes("UTF-8");
        
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
        return new String(plainTextBytes, "UTF-8");
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
        signature.update(data.getBytes("UTF-8"));
        return signature.sign();
    }
    
    /**
     * Verify signature with RSA public key.
     */
    public boolean verifySignature(String data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes("UTF-8"));
        return signature.verify(signatureBytes);
    }
    
    /**
     * Generate SHA-256 hash of a message.
     */
    public String sha256Hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        return bytesToHex(hash);
    }
    
    /**
     * Generate cryptographically secure random token.
     */
    public String generateSecureToken(int length) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
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
    public static class EmployeeDataEncryption {
        private final SecretKey masterKey;
        
        public EmployeeDataEncryption(SecretKey masterKey) {
            this.masterKey = masterKey;
        }
        
        public EncryptedEmployee encryptEmployeeData(Employee employee) throws Exception {
            CryptographicAPIs crypto = new CryptographicAPIs();
            
            // Encrypt sensitive fields
            byte[] encryptedSSN = crypto.encrypt(employee.getSsn(), masterKey);
            byte[] encryptedSalary = crypto.encrypt(employee.getSalary().toString(), masterKey);
            
            return new EncryptedEmployee(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                encryptedSSN,
                encryptedSalary
            );
        }
        
        public Employee decryptEmployeeData(EncryptedEmployee encryptedEmployee) throws Exception {
            CryptographicAPIs crypto = new CryptographicAPIs();
            
            // Decrypt sensitive fields
            String ssn = crypto.decrypt(encryptedEmployee.getEncryptedSSN(), masterKey);
            String salaryStr = crypto.decrypt(encryptedEmployee.getEncryptedSalary(), masterKey);
            Double salary = Double.parseDouble(salaryStr);
            
            return new Employee(
                encryptedEmployee.getId(),
                encryptedEmployee.getName(),
                encryptedEmployee.getEmail(),
                encryptedEmployee.getDepartment(),
                ssn,
                salary
            );
        }
        
        public static void demonstrateEmployeeEncryption() {
            System.out.println("\n--- Employee Data Encryption Example ---");
            
            try {
                // Generate master key
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
                System.out.println("Encrypted SSN: " + Base64.getEncoder().encodeToString(encrypted.getEncryptedSSN()));
                System.out.println("Encrypted Salary: " + Base64.getEncoder().encodeToString(encrypted.getEncryptedSalary()));
                
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
    public static class Employee {
        private Long id;
        private String name;
        private String email;
        private String department;
        private String ssn;
        private Double salary;
        
        public Employee(Long id, String name, String email, String department, String ssn, Double salary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.department = department;
            this.ssn = ssn;
            this.salary = salary;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getDepartment() { return department; }
        public String getSsn() { return ssn; }
        public Double getSalary() { return salary; }
        
        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', department='" + department + 
                   "', ssn='" + ssn + "', salary=" + salary + "}";
        }
    }
    
    /**
     * Encrypted employee class.
     */
    public static class EncryptedEmployee {
        private final Long id;
        private final String name;
        private final String email;
        private final String department;
        private final byte[] encryptedSSN;
        private final byte[] encryptedSalary;
        
        public EncryptedEmployee(Long id, String name, String email, String department, 
                               byte[] encryptedSSN, byte[] encryptedSalary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.department = department;
            this.encryptedSSN = encryptedSSN;
            this.encryptedSalary = encryptedSalary;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getDepartment() { return department; }
        public byte[] getEncryptedSSN() { return encryptedSSN; }
        public byte[] getEncryptedSalary() { return encryptedSalary; }
    }
}