package com.oreilly.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Arrays;
import java.util.Base64;

/**
 * Cryptography Exercise - Practice the Core Java Cryptographic APIs
 * 
 * This exercise focuses on the three essential cryptographic skills:
 * 1. Encrypt sensitive data (Cipher class)
 * 2. Create digital signatures (Signature class) 
 * 3. Hash passwords securely (MessageDigest class)
 * 
 * Complete each TODO using the same patterns shown in CryptographicAPIs.java
 */
public class CryptographyExercise {

    // Use these constants in your implementations
    private static final String RSA_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * TODO #1: Implement RSA encryption (like CryptographicAPIs demonstrates)
     * <p>
     * Pattern to follow:
     * 1. Get a Cipher instance using RSA_ALGORITHM
     * 2. Initialize it in ENCRYPT_MODE with the public key
     * 3. Use doFinal() to encrypt the sensitive data
     * 4. Return the encrypted bytes
     * <p>
     * Hint: Follow the same pattern as demonstrateEncryption() method
     */
    public byte[] encryptSensitiveData(String sensitiveData, PublicKey publicKey) throws Exception {
        // TODO: Implement RSA encryption using Cipher class
        return null; // Replace this line
    }

    /**
     * TODO #2: Implement RSA decryption
     * <p>
     * Pattern to follow:
     * 1. Get a Cipher instance using RSA_ALGORITHM  
     * 2. Initialize it in DECRYPT_MODE with the private key
     * 3. Use doFinal() to decrypt the encrypted data
     * 4. Convert bytes back to String and return
     * <p>
     * Hint: Follow the same pattern as demonstrateEncryption() method
     */
    public String decryptSensitiveData(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        // TODO: Implement RSA decryption using Cipher class
        return null; // Replace this line
    }

    /**
     * TODO #3: Implement digital signature creation
     * <p>
     * Pattern to follow:
     * 1. Get a Signature instance using SIGNATURE_ALGORITHM
     * 2. Initialize it for signing with initSign() and the private key
     * 3. Update it with the document data using update()
     * 4. Generate the signature using sign()
     * <p>
     * Hint: Follow the same pattern as demonstrateDigitalSignatures() method
     */
    public byte[] signDocument(String document, PrivateKey privateKey) throws Exception {
        // TODO: Implement digital signature creation using Signature class
        return null; // Replace this line
    }

    /**
     * TODO #4: Implement digital signature verification
     * <p>
     * Pattern to follow:
     * 1. Get a Signature instance using SIGNATURE_ALGORITHM
     * 2. Initialize it for verification with initVerify() and the public key
     * 3. Update it with the original document data using update()
     * 4. Verify the signature using verify() and return the boolean result
     * <p>
     * Hint: Follow the same pattern as demonstrateDigitalSignatures() method
     */
    public boolean verifyDocumentSignature(String document, byte[] signature, PublicKey publicKey) throws Exception {
        // TODO: Implement digital signature verification using Signature class
        return false; // Replace this line
    }

    /**
     * TODO #5: Implement secure password hashing with salt
     * <p>
     * Pattern to follow:
     * 1. Generate a random 16-byte salt using SecureRandom
     * 2. Get a MessageDigest instance using HASH_ALGORITHM
     * 3. Update the digest with the salt first
     * 4. Hash the password and return both salt and hash
     * <p>
     * Hint: Follow the same pattern as demonstrateSecureHashing() method
     */
    public record SaltedHash(byte[] salt, byte[] hash) {}
    
    public SaltedHash hashPasswordWithSalt(String password) throws Exception {
        // TODO: Implement secure password hashing using MessageDigest class
        return null; // Replace this line
    }

    /**
     * TODO #6: Implement password verification
     * <p>
     * Pattern to follow:
     * 1. Use the stored salt from the SaltedHash
     * 2. Get a MessageDigest instance using HASH_ALGORITHM
     * 3. Update the digest with the same salt
     * 4. Hash the provided password and compare with stored hash
     * <p>
     * Hint: Use Arrays.equals() to compare the hash bytes
     */
    public boolean verifyPassword(String password, SaltedHash storedHash) throws Exception {
        // TODO: Implement password verification using MessageDigest class
        return false; // Replace this line
    }

    // Helper methods - Use these to test your implementations

    /**
     * Generates a new RSA key pair for encryption and signatures.
     * Use this to create key pairs for testing your methods.
     */
    public KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
    
    /**
     * Main method to test your implementations.
     * Uncomment each test as you complete the TODOs.
     */
    public static void main(String[] args) {
        var exercise = new CryptographyExercise();
        System.out.println("=== Cryptography Exercise ===");
        
        try {
            // Generate test key pair
            KeyPair keyPair = exercise.generateRSAKeyPair();
            
            // Test 1 & 2: Encryption/Decryption
            // System.out.println("\n1. Testing RSA Encryption/Decryption:");
            // String original = "Secret credit card: 4532-1234-5678-9012";
            // byte[] encrypted = exercise.encryptSensitiveData(original, keyPair.getPublic());
            // String decrypted = exercise.decryptSensitiveData(encrypted, keyPair.getPrivate());
            // System.out.println("Original:  " + original);
            // System.out.println("Decrypted: " + decrypted);
            // System.out.println("Match: " + original.equals(decrypted));
            
            // Test 3 & 4: Digital Signatures
            // System.out.println("\n2. Testing Digital Signatures:");
            // String document = "Transfer $1000 from Alice to Bob";
            // byte[] signature = exercise.signDocument(document, keyPair.getPrivate());
            // boolean valid = exercise.verifyDocumentSignature(document, signature, keyPair.getPublic());
            // System.out.println("Document: " + document);
            // System.out.println("Signature valid: " + valid);
            
            // Test 5 & 6: Password Hashing
            // System.out.println("\n3. Testing Password Hashing:");
            // String password = "mySecretPassword123!";
            // SaltedHash hash = exercise.hashPasswordWithSalt(password);
            // boolean passwordMatch = exercise.verifyPassword(password, hash);
            // boolean wrongMatch = exercise.verifyPassword("wrongPassword", hash);
            // System.out.println("Password: " + password);
            // System.out.println("Correct password match: " + passwordMatch);
            // System.out.println("Wrong password match: " + wrongMatch);
            
            System.out.println("\nComplete the TODOs above and uncomment the tests!");
            
        } catch (Exception e) {
            System.err.println("Exercise error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}