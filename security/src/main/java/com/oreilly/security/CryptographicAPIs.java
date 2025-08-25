package com.oreilly.security;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * Demonstrates core Java cryptographic APIs for practical security tasks.
 * Focuses on the three essential skills: encryption, digital signatures, and secure hashing.
 * <p> 
 * This class shows the foundational Java APIs that underpin security libraries like
 * BCrypt, JWT, and OAuth implementations.
 */
public class CryptographicAPIs {

    public static void main(String[] args) {
        var crypto = new CryptographicAPIs();
        
        System.out.println("=== Java Cryptographic APIs Demonstration ===");
        System.out.println("Learning the foundation classes for secure applications\n");
        
        // 1. Encryption for confidentiality
        crypto.demonstrateEncryption();
        
        // 2. Digital signatures for integrity and non-repudiation  
        crypto.demonstrateDigitalSignatures();
        
        // 3. Secure hashing for password storage
        crypto.demonstrateSecureHashing();
        
        // 4. Certificates for secure key distribution
        crypto.demonstrateCertificates();

        System.out.println("""
                
                === Key Takeaways ===
                • Cipher class: Encrypt/decrypt sensitive data
                • Signature class: Verify document integrity and authenticity
                • MessageDigest class: Securely hash passwords with salt
                • Certificates: Enable secure public key distribution
                • These Java APIs are the foundation for BCrypt, JWT, and OAuth libraries""");
    }

    /**
     * Demonstrates the Cipher class for encrypting and decrypting sensitive data.
     * This provides CONFIDENTIALITY - keeping data secret.
     */
    private void demonstrateEncryption() {
        System.out.println("1. ENCRYPTION - Keeping Data Confidential");
        System.out.println("==========================================");
        
        try {
            // Generate a key pair (public/private keys)
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            
            String sensitiveData = "User's credit card: 4532-1234-5678-9012";
            System.out.println("Original data: " + sensitiveData);
            
            // ENCRYPT with public key
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedData = encryptCipher.doFinal(sensitiveData.getBytes(StandardCharsets.UTF_8));
            
            String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedData);
            System.out.println("Encrypted: " + encryptedBase64.substring(0, 50) + "...");
            
            // DECRYPT with private key
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedData = decryptCipher.doFinal(encryptedData);
            
            String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
            System.out.println("Decrypted: " + decryptedText);
            System.out.println("✓ Data remains confidential during transmission\n");
            
        } catch (Exception e) {
            System.err.println("Encryption error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates the Signature class for document integrity and authenticity.
     * This provides INTEGRITY (data unchanged) and NON-REPUDIATION (proves sender).
     */
    private void demonstrateDigitalSignatures() {
        System.out.println("2. DIGITAL SIGNATURES - Integrity and Authenticity");
        System.out.println("==================================================");
        
        try {
            // Generate sender's key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair senderKeyPair = keyGen.generateKeyPair();
            
            String document = "Please sign this NDA digitally.";
            System.out.println("Document: " + document);
            
            // CREATE SIGNATURE with sender's private key
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(senderKeyPair.getPrivate());
            signature.update(document.getBytes(StandardCharsets.UTF_8));
            byte[] digitalSignature = signature.sign();
            
            String signatureBase64 = Base64.getEncoder().encodeToString(digitalSignature);
            System.out.println("Digital signature: " + signatureBase64.substring(0, 50) + "...");
            
            // VERIFY SIGNATURE with sender's public key
            Signature verifySignature = Signature.getInstance("SHA256withRSA");
            verifySignature.initVerify(senderKeyPair.getPublic());
            verifySignature.update(document.getBytes(StandardCharsets.UTF_8));
            boolean isValid = verifySignature.verify(digitalSignature);
            
            System.out.println("Signature valid: " + isValid);
            System.out.println("✓ Proves document integrity and sender authenticity");
            
            // Show what happens with tampered data
            String tamperedDocument = "Transfer $9999 from Alice to Bob";
            verifySignature.initVerify(senderKeyPair.getPublic());
            verifySignature.update(tamperedDocument.getBytes(StandardCharsets.UTF_8));
            boolean tamperedValid = verifySignature.verify(digitalSignature);
            System.out.println("Tampered document valid: " + tamperedValid);
            System.out.println("✓ Detects any data tampering\n");
            
        } catch (Exception e) {
            System.err.println("Digital signature error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates MessageDigest for secure password hashing with salt.
     * This is the foundation for libraries like BCrypt.
     */
    private void demonstrateSecureHashing() {
        System.out.println("3. SECURE HASHING - Password Storage");
        System.out.println("====================================");
        
        try {
            String password = "user123!";
            System.out.println("Original password: " + password);
            
            // Generate random salt
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            System.out.println("Random salt: " + Base64.getEncoder().encodeToString(salt));
            
            // Hash password with salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);
            System.out.println("Hashed password: " + hashBase64);
            
            // Verify password (same process)
            digest.reset();
            digest.update(salt);
            byte[] testHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            boolean passwordMatches = MessageDigest.isEqual(hashedPassword, testHash);
            
            System.out.println("Password verification: " + passwordMatches);
            System.out.println("✓ Passwords stored securely, never in plain text");
            System.out.println("Note: Production systems use BCrypt or Argon2 for better security\n");
            
        } catch (Exception e) {
            System.err.println("Hashing error: " + e.getMessage());
        }
    }

    /**
     * Public method for password hashing (used by SecurityController).
     * Demonstrates the MessageDigest pattern with salt.
     */
    public String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            // Hash password with salt
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Return salt + hash encoded as Base64 for storage
            byte[] saltAndHash = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
            System.arraycopy(hashedPassword, 0, saltAndHash, salt.length, hashedPassword.length);
            
            return Base64.getEncoder().encodeToString(saltAndHash);
            
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    /**
     * Demonstrates certificate creation and verification.
     * Shows how certificates enable secure public key distribution.
     */
    private void demonstrateCertificates() {
        System.out.println("4. CERTIFICATES - Secure Public Key Distribution");
        System.out.println("===============================================");
        
        try {
            // Generate CA (Certificate Authority) key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair caKeyPair = keyGen.generateKeyPair();
            
            // Generate Alice's key pair
            KeyPair aliceKeyPair = keyGen.generateKeyPair();
            
            System.out.println("Certificate Authority: TrustedCA");
            System.out.println("Certificate Subject: Alice");
            
            // Create certificate content (simplified)
            String certificateInfo = String.format("""
                Subject: Alice
                Public Key: %s...
                Issuer: TrustedCA
                Valid From: 2024-01-01
                Valid To: 2025-01-01""", 
                Base64.getEncoder().encodeToString(aliceKeyPair.getPublic().getEncoded()).substring(0, 20));
            
            System.out.println("Certificate contents:");
            System.out.println(certificateInfo);
            
            // CA SIGNS the certificate (like a digital signature on the cert)
            Signature caSignature = Signature.getInstance("SHA256withRSA");
            caSignature.initSign(caKeyPair.getPrivate());
            caSignature.update(certificateInfo.getBytes(StandardCharsets.UTF_8));
            byte[] certSignature = caSignature.sign();
            
            String certSigBase64 = Base64.getEncoder().encodeToString(certSignature);
            System.out.println("CA Signature: " + certSigBase64.substring(0, 50) + "...");
            
            // VERIFY the certificate using CA's public key
            Signature verifyCA = Signature.getInstance("SHA256withRSA");
            verifyCA.initVerify(caKeyPair.getPublic());
            verifyCA.update(certificateInfo.getBytes(StandardCharsets.UTF_8));
            boolean certValid = verifyCA.verify(certSignature);
            
            System.out.println("Certificate validation: " + certValid);
            System.out.println("✓ CA vouches for Alice's public key");
            System.out.println("✓ Now Bob can trust Alice's public key for encryption");
            System.out.println("✓ Certificate creates a chain of trust\\n");
            
        } catch (Exception e) {
            System.err.println("Certificate error: " + e.getMessage());
        }
    }
}