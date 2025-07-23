---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Cryptographic APIs
  Encrypt sensitive data and authenticate users
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Java Cryptographic APIs
---

# Java Cryptographic APIs

Encrypt Sensitive Data and Authenticate Users

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

# Why Cryptography?

## Protecting Sensitive Data

<v-clicks>

- Passwords and credentials
- Personal information (PII)
- Financial data

</v-clicks>

## Security Goals

<v-clicks>

- **Confidentiality** - Keep secrets secret
- **Integrity** - Detect tampering
- **Authentication** - Verify identity

</v-clicks>

---
transition: slide-left
---

# Java Cryptography Architecture

## Built-in Support

<v-clicks>

- JCA (Java Cryptography Architecture)
- JCE (Java Cryptography Extension)
- No external libraries needed

</v-clicks>

## Key Classes

<v-clicks>

- MessageDigest - Hashing
- Cipher - Encryption/Decryption
- KeyGenerator - Key creation

</v-clicks>

---
transition: slide-left
---

# Password Hashing

## Never Store Plain Text!

```java
// NEVER do this
String password = "secret123";
database.save(password); // DANGER!

// Always hash passwords
String hashedPassword = hashPassword(password);
database.save(hashedPassword); // Safe
```

---
transition: slide-left
---

# BCrypt for Passwords

## Industry Standard

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordService {
    private final BCryptPasswordEncoder encoder = 
        new BCryptPasswordEncoder();
    
    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }
    
    public boolean verifyPassword(String plainPassword, 
                                 String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
```

---
transition: slide-left
---

# Why BCrypt?

## Built-in Protection

<v-clicks>

- Automatic salt generation
- Configurable work factor
- Slow by design (prevents brute force)

</v-clicks>

```java
// Work factor 10 (default) = 2^10 iterations
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

// Higher = more secure but slower
BCryptPasswordEncoder strongEncoder = new BCryptPasswordEncoder(12);
```

---
transition: slide-left
---

# SHA-256 Hashing

## For Data Integrity

```java
import java.security.MessageDigest;

public class HashingService {
    public String sha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        return bytesToHex(hash);
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
```

---
transition: slide-left
---

# AES Encryption

## Symmetric Encryption

```java
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESEncryption {
    public SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // 256-bit key
        return keyGen.generateKey();
    }
}
```

---
transition: slide-left
---

# Encrypting Data

## Using AES

```java
public byte[] encrypt(String plainText, SecretKey key) 
        throws Exception {
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    
    byte[] iv = cipher.getIV(); // Initialization vector
    byte[] cipherText = cipher.doFinal(
        plainText.getBytes("UTF-8"));
    
    // Combine IV and ciphertext for storage
    return concatenate(iv, cipherText);
}
```

---
transition: slide-left
---

# Decrypting Data

## Reversing the Process

```java
public String decrypt(byte[] encryptedData, SecretKey key) 
        throws Exception {
    // Extract IV and ciphertext
    byte[] iv = extractIV(encryptedData);
    byte[] cipherText = extractCipherText(encryptedData);
    
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(128, iv);
    cipher.init(Cipher.DECRYPT_MODE, key, spec);
    
    byte[] plainText = cipher.doFinal(cipherText);
    return new String(plainText, "UTF-8");
}
```

---
transition: slide-left
---

# Key Storage

## Protecting Encryption Keys

```java
import java.security.KeyStore;

public class KeyStoreManager {
    public void saveKey(SecretKey key, String alias, 
                       char[] password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, password);
        
        KeyStore.SecretKeyEntry keyEntry = 
            new KeyStore.SecretKeyEntry(key);
        KeyStore.PasswordProtection protection = 
            new KeyStore.PasswordProtection(password);
        
        keyStore.setEntry(alias, keyEntry, protection);
        
        try (FileOutputStream fos = 
                new FileOutputStream("keystore.p12")) {
            keyStore.store(fos, password);
        }
    }
}
```

---
transition: slide-left
---

# Digital Signatures

## Proving Authenticity

```java
import java.security.*;

public class DigitalSignature {
    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = 
            KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
    
    public byte[] sign(String data, PrivateKey privateKey) 
            throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes("UTF-8"));
        return signature.sign();
    }
}
```

---
transition: slide-left
---

# Verifying Signatures

## Checking Authenticity

```java
public boolean verify(String data, byte[] signatureBytes, 
                     PublicKey publicKey) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initVerify(publicKey);
    signature.update(data.getBytes("UTF-8"));
    return signature.verify(signatureBytes);
}
```

---
transition: slide-left
---

# JWT Tokens

## Modern Authentication

```java
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTService {
    private final String SECRET = "your-secret-key";
    
    public String createToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() 
                + 3600000)) // 1 hour
            .signWith(SignatureAlgorithm.HS256, SECRET)
            .compact();
    }
}
```

---
transition: slide-left
---

# Validating JWT

## Token Verification

```java
public String validateToken(String token) {
    try {
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .getBody();
        
        return claims.getSubject(); // username
    } catch (Exception e) {
        throw new InvalidTokenException("Invalid JWT");
    }
}
```

---
transition: slide-left
---

# Secure Random

## Cryptographic Randomness

```java
import java.security.SecureRandom;

public class SecurityUtils {
    private final SecureRandom random = new SecureRandom();
    
    public String generateToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public String generateSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }
}
```

---
transition: slide-left
---

# Environment Variables

## Secure Configuration

```java
public class CryptoConfig {
    // Never hardcode secrets!
    private final String masterKey = 
        System.getenv("MASTER_ENCRYPTION_KEY");
    
    private final String jwtSecret = 
        System.getenv("JWT_SECRET");
    
    public SecretKey getMasterKey() {
        byte[] keyBytes = Base64.getDecoder()
            .decode(masterKey);
        return new SecretKeySpec(keyBytes, "AES");
    }
}
```

---
transition: slide-left
---

# Complete Example

## Employee Data Encryption

```java
@Service
public class EmployeeSecurityService {
    private final SecretKey key;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public void saveEmployee(Employee employee) {
        // Hash password
        employee.setPassword(
            passwordEncoder.encode(employee.getPassword()));
        
        // Encrypt sensitive data
        employee.setSsn(
            encrypt(employee.getSsn(), key));
        
        repository.save(employee);
    }
}
```

---
transition: slide-left
---

# Common Mistakes

## What to Avoid

<v-clicks>

- Using MD5 or SHA-1 (broken)
- Hardcoding encryption keys
- Rolling your own crypto

</v-clicks>

---
transition: slide-left
---

# Common Mistakes (continued)

## More Pitfalls

<v-clicks>

- Reusing IVs/nonces
- Weak random number generation
- Not rotating keys

</v-clicks>

---
transition: slide-left
---

# Best Practices

## Security Guidelines

<v-clicks>

- Use established algorithms (AES, RSA)
- Keep keys separate from data
- Use key management services

</v-clicks>

---
transition: slide-left
---

# Best Practices (continued)

## Implementation Tips

<v-clicks>

- Always use SecureRandom
- Implement key rotation
- Log security events (not secrets!)

</v-clicks>

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- Hash passwords with BCrypt
- Encrypt sensitive data with AES
- Sign data with RSA for authenticity
- Use JWTs for modern authentication
- Never roll your own crypto

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Git Collaboration

Working with branches and merge requests