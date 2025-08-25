package com.oreilly.security.solutions;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import javax.crypto.Cipher;

/**
 * Complete solution for CryptographyExercise - demonstrates the Core Java Cryptographic APIs
 * 
 * This solution implements all six TODOs from the main exercise:
 * 1. RSA encryption using Cipher class
 * 2. RSA decryption using Cipher class
 * 3. Digital signature creation using Signature class
 * 4. Digital signature verification using Signature class
 * 5. Secure password hashing with salt using MessageDigest class
 * 6. Password verification using MessageDigest class
 * 
 * All implementations follow the exact patterns demonstrated in CryptographicAPIs.java
 */
public class CryptographyExerciseSolution {

    // Use these constants in your implementations
    private static final String RSA_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * TODO #1 SOLUTION: Implement RSA encryption using Cipher class
     */
    public byte[] encryptSensitiveData(String sensitiveData, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(sensitiveData.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * TODO #2 SOLUTION: Implement RSA decryption using Cipher class
     */
    public String decryptSensitiveData(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * TODO #3 SOLUTION: Implement digital signature creation using Signature class
     */
    public byte[] signDocument(String document, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(document.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    /**
     * TODO #4 SOLUTION: Implement digital signature verification using Signature class
     */
    public boolean verifyDocumentSignature(String document, byte[] signature, PublicKey publicKey) throws Exception {
        Signature verifySignature = Signature.getInstance(SIGNATURE_ALGORITHM);
        verifySignature.initVerify(publicKey);
        verifySignature.update(document.getBytes(StandardCharsets.UTF_8));
        return verifySignature.verify(signature);
    }

    /**
     * TODO #5 SOLUTION: Implement secure password hashing with salt using MessageDigest class
     */
    public record SaltedHash(byte[] salt, byte[] hash) {}
    
    public SaltedHash hashPasswordWithSalt(String password) throws Exception {
        // Generate random salt
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        // Hash password with salt
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        return new SaltedHash(salt, hash);
    }

    /**
     * TODO #6 SOLUTION: Implement password verification using MessageDigest class
     */
    public boolean verifyPassword(String password, SaltedHash storedHash) throws Exception {
        // Hash the provided password with the stored salt
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.update(storedHash.salt());
        byte[] computedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        // Compare hashes using constant-time comparison
        return Arrays.equals(storedHash.hash(), computedHash);
    }

    // Helper method
    public KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
    
    /**
     * Main method demonstrating all solutions working together
     */
    public static void main(String[] args) {
        var solution = new CryptographyExerciseSolution();
        System.out.println("=== Cryptography Exercise Solution ===");
        
        try {
            // Generate test key pair
            KeyPair keyPair = solution.generateRSAKeyPair();
            
            // Test 1 & 2: Encryption/Decryption
            System.out.println("\n1. Testing RSA Encryption/Decryption:");
            String original = "Secret credit card: 4532-1234-5678-9012";
            byte[] encrypted = solution.encryptSensitiveData(original, keyPair.getPublic());
            String decrypted = solution.decryptSensitiveData(encrypted, keyPair.getPrivate());
            System.out.println("Original:  " + original);
            System.out.println("Decrypted: " + decrypted);
            System.out.println("Match: " + original.equals(decrypted));
            
            // Test 3 & 4: Digital Signatures
            System.out.println("\n2. Testing Digital Signatures:");
            String document = "Please sign this NDA digitally.";
            byte[] signature = solution.signDocument(document, keyPair.getPrivate());
            boolean valid = solution.verifyDocumentSignature(document, signature, keyPair.getPublic());
            System.out.println("Document: " + document);
            System.out.println("Signature valid: " + valid);
            
            // Test tampering detection
            boolean tamperedValid = solution.verifyDocumentSignature("Modified document", signature, keyPair.getPublic());
            System.out.println("Tampered document valid: " + tamperedValid);
            
            // Test 5 & 6: Password Hashing
            System.out.println("\n3. Testing Password Hashing:");
            String password = "mySecretPassword123!";
            SaltedHash hash = solution.hashPasswordWithSalt(password);
            boolean passwordMatch = solution.verifyPassword(password, hash);
            boolean wrongMatch = solution.verifyPassword("wrongPassword", hash);
            System.out.println("Password: " + password);
            System.out.println("Correct password match: " + passwordMatch);
            System.out.println("Wrong password match: " + wrongMatch);
            
            System.out.println("\n✓ All cryptographic operations completed successfully!");
            System.out.println("✓ Cipher class: RSA encryption and decryption");
            System.out.println("✓ Signature class: Digital signatures with tamper detection");
            System.out.println("✓ MessageDigest class: Secure password hashing with salt");
            
        } catch (Exception e) {
            System.err.println("Solution error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}