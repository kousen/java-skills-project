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
 * Cryptography Exercise - Practice Java's Cryptographic APIs
 * 
 * This exercise guides you through implementing secure cryptographic operations
 * using Java's built-in JCA (Java Cryptography Architecture) classes.
 * Complete each TODO to build a comprehensive cryptographic service.
 */
public class CryptographyExercise {

    // Constants for cryptographic operations
    private static final String AES_GCM_ALGORITHM = "AES/GCM/NoPadding";
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String RSA_SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int PBKDF2_ITERATIONS = 100000;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int AES_KEY_LENGTH = 256;

    // Helper record for storing encrypted data
    public record EncryptedData(byte[] ciphertext, byte[] iv, String algorithm) {}

    /**
     * TODO #1: Implement secure password hashing using PBKDF2
     * <p>
     * Requirements:
     * 1. Generate a random 16-byte salt using SecureRandom
     * 2. Use SecretKeyFactory with PBKDF2_ALGORITHM 
     * 3. Use PBKDF2_ITERATIONS for the iteration count
     * 4. Return both the salt and hash for storage
     * <p>
     * Remember: PBKDF2 transforms weak passwords into strong keys while making
     * brute force attacks expensive through computational cost.
     * <p>
     * Test cases to consider:
     * - "password123" -> should produce different hashes each time (due to random salt)
     * - Same password with same salt -> should produce identical hash
     * - Empty/null passwords -> should handle gracefully
     */
    public record HashedPassword(byte[] salt, byte[] hash) {}

    public HashedPassword hashPassword(String password) {
        // TODO: Implement PBKDF2 password hashing
        return null; // Replace this line
    }

    /**
     * TODO #2: Implement password verification
     * <p>
     * Requirements:
     * 1. Use the stored salt from the HashedPassword
     * 2. Hash the provided password with the same salt and iterations
     * 3. Compare the computed hash with the stored hash using Arrays.equals()
     * 4. Return true only if hashes match exactly
     * <p>
     * Security note: Use Arrays.equals() for constant-time comparison
     * to prevent timing attacks.
     */
    public boolean verifyPassword(String password, HashedPassword storedPassword) {
        // TODO: Implement password verification
        return false; // Replace this line
    }

    /**
     * TODO #3: Implement AES-GCM encryption
     * <p>
     * Requirements:
     * 1. Generate a random 12-byte IV using SecureRandom
     * 2. Initialize Cipher with AES_GCM_ALGORITHM in ENCRYPT_MODE
     * 3. Use GCMParameterSpec with the IV and GCM_TAG_LENGTH
     * 4. Return EncryptedData containing ciphertext, IV, and algorithm name
     * <p>
     * Remember: AES-GCM provides both confidentiality (encryption) and 
     * authenticity (tamper detection) in a single operation.
     * <p>
     * Test cases to consider:
     * - Same plaintext should produce different ciphertext each time (due to random IV)
     * - Empty data should be handled properly
     * - Large data blocks should work correctly
     */
    public EncryptedData encryptData(String plaintext, SecretKey key) {
        // TODO: Implement AES-GCM encryption
        return null; // Replace this line
    }

    /**
     * TODO #4: Implement AES-GCM decryption
     * <p>
     * Requirements:
     * 1. Initialize Cipher with AES_GCM_ALGORITHM in DECRYPT_MODE
     * 2. Use the IV from the EncryptedData with GCMParameterSpec
     * 3. Decrypt the ciphertext using cipher.doFinal()
     * 4. Return the decrypted plaintext as a String
     * 5. Handle authentication failures (throw exception for tampered data)
     * <p>
     * Security note: If the authentication tag doesn't match, doFinal() will
     * throw an exception - this means the data was tampered with.
     */
    public String decryptData(EncryptedData encryptedData, SecretKey key) throws Exception {
        // TODO: Implement AES-GCM decryption
        return null; // Replace this line
    }

    /**
     * TODO #5: Implement RSA digital signature creation
     * <p>
     * Requirements:
     * 1. Initialize Signature with RSA_SIGNATURE_ALGORITHM
     * 2. Use initSign() with the private key
     * 3. Update the signature with the data bytes
     * 4. Generate and return the signature bytes
     * <p>
     * Remember: Digital signatures prove authenticity - that the data came
     * from someone holding the private key and hasn't been modified.
     * <p>
     * Test cases to consider:
     * - Same data should produce different signatures each time (RSA with randomness)
     * - Different data should produce different signatures
     * - Large documents should be handled correctly
     */
    public byte[] signData(String data, PrivateKey privateKey) throws Exception {
        // TODO: Implement RSA digital signature creation
        return null; // Replace this line
    }

    /**
     * TODO #6: Implement RSA digital signature verification
     * <p>
     * Requirements:
     * 1. Initialize Signature with RSA_SIGNATURE_ALGORITHM
     * 2. Use initVerify() with the public key
     * 3. Update the signature with the original data bytes
     * 4. Verify the signature and return boolean result
     * <p>
     * Security note: This verifies both authenticity (who signed it) and 
     * integrity (data hasn't been modified).
     */
    public boolean verifySignature(String data, byte[] signature, PublicKey publicKey) throws Exception {
        // TODO: Implement RSA digital signature verification
        return false; // Replace this line
    }

    // Helper methods - you can use these in your implementations

    /**
     * Generates a new AES key for encryption.
     * You can use this in your tests.
     */
    public SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_KEY_LENGTH);
        return keyGen.generateKey();
    }

    /**
     * Generates a new RSA key pair for digital signatures.
     * You can use this in your tests.
     */
    public KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /**
     * Converts a SecretKey to a Base64 string for storage.
     * Useful for saving keys to configuration or database.
     */
    public String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Converts a Base64 string back to a SecretKey.
     * Useful for loading keys from configuration or database.
     */
    public SecretKey stringToKey(String keyString) {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(keyBytes, "AES");
    }
}