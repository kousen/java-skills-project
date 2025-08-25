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

Core Security Concepts and Essential Java Classes

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

# Three Essential Security Skills

## Every Java Developer Needs

<v-clicks>

- **Encrypt sensitive data** - Keep information confidential
- **Verify document integrity** - Detect tampering and authenticate senders
- **Securely store passwords** - Hash with salt for security

</v-clicks>

## These Are the Building Blocks

<v-clicks>

- **BCrypt** - Password hashing library
- **JWT tokens** - Authentication tokens
- **OAuth implementations** - Authorization frameworks

</v-clicks>

---
transition: slide-left
---

# Public/Private Key Pairs

## How Encryption Works

<v-clicks>

- **Key pairs** - One key encrypts, the other decrypts
- **Public key** - Can be shared with anyone
- **Private key** - Must be kept secret

</v-clicks>

## Confidentiality Example

<v-clicks>

- **Encrypt with your public key** - Only you can decrypt it
- **You encrypt replies with my public key** - Only I can decrypt them
- **Data stays secret during transmission**

</v-clicks>

---
transition: slide-left
---

# Digital Signatures Concept

## Real-World Example: NDA Signing

<v-clicks>

- **The Problem** - Client needs to know you signed an NDA and it wasn't changed
- **Solution** - Digital signatures provide integrity and non-repudiation

</v-clicks>

## What is a Hash?

<v-clicks>

- **Digital fingerprint** - Fixed length string representing the document
- **Standard algorithm** - Same input always produces same hash
- **Tamper detection** - Any change produces completely different hash

</v-clicks>

---
transition: slide-left
---

# How Digital Signatures Work

## Step-by-Step Process

<v-clicks>

1. **Hash the NDA document** - Creates digital fingerprint
2. **Encrypt hash with your private key** - This is the digital signature
3. **Send signature to recipient**

</v-clicks>

## Verification Process

<v-clicks>

4. **Recipient decrypts signature with your public key** - Gets your hash
5. **Recipient generates hash from document** - Using same algorithm
6. **Compare the two hashes** - If they match: integrity + non-repudiation proven

</v-clicks>

---
transition: slide-left
---

# Certificate Trust Chain

## Secure Public Key Distribution

<v-clicks>

- **The Problem** - How do you get someone's public key safely?
- **The Solution** - Certificates create a chain of trust

</v-clicks>

## How Certificates Work

<v-clicks>

- **Certificate Authority (CA)** - Trusted third party
- **CA vouches for identity** - Signs public key + identity info
- **Chain of trust** - You trust CA → CA vouches for certificate → You trust public key

</v-clicks>

---
transition: slide-left
---

# Java Encryption APIs

## The Cipher Class

```java
// Three key methods - always the same pattern:
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] encrypted = cipher.doFinal(sensitiveData.getBytes());
```

<v-clicks>

- **getInstance()** - Get the cipher
- **init()** - Set the mode and key
- **doFinal()** - Actually encrypt or decrypt

</v-clicks>

---
transition: slide-left
---

# Java Digital Signature APIs

## The Signature Class

```java
// Creating a signature:
Signature signature = Signature.getInstance("SHA256withRSA");
signature.initSign(privateKey);
signature.update(document.getBytes());
byte[] digitalSignature = signature.sign();

// Verifying a signature:
signature.initVerify(publicKey);
signature.update(document.getBytes());
boolean valid = signature.verify(digitalSignature);
```

---
transition: slide-left
---

# Java Password Hashing APIs

## The MessageDigest Class with Salt

```java
// Generate random salt - crucial for security!
SecureRandom random = SecureRandom.getInstanceStrong();
byte[] salt = new byte[16];
random.nextBytes(salt);

// Hash password with salt
MessageDigest digest = MessageDigest.getInstance("SHA-256");
digest.update(salt);
byte[] hashedPassword = digest.digest(password.getBytes());
```

<v-clicks>

- **Never store passwords in plain text!**
- **Salt prevents rainbow table attacks**
- **Same password + same salt = same hash**

</v-clicks>

---
transition: slide-left
---

# Certificate APIs

## Using the Same Signature Class

```java
// CA signs Alice's certificate (like a digital signature on her public key)
String certificateInfo = "Subject: Alice, Public Key: " + alice_pubkey;
Signature caSignature = Signature.getInstance("SHA256withRSA");
caSignature.initSign(caPrivateKey);
caSignature.update(certificateInfo.getBytes());
byte[] certSignature = caSignature.sign();

// Bob verifies the certificate using CA's public key  
Signature verifyCA = Signature.getInstance("SHA256withRSA");
verifyCA.initVerify(caPublicKey);
verifyCA.update(certificateInfo.getBytes());
boolean certValid = verifyCA.verify(certSignature);
```

<v-clicks>

- **Same Signature class** - Just applied to certificates
- **CA vouches for Alice's public key**
- **Bob can now trust Alice's public key for encryption**

</v-clicks>

---
transition: slide-left
---

# When to Use Each API

## Choose the Right Tool

<v-clicks>

- **Cipher class** - Encrypt sensitive data for storage or transmission
- **Signature class** - Verify document integrity or authenticate the sender
- **MessageDigest with salt** - Securely store passwords
- **Certificates** - Distribute public keys securely

</v-clicks>

## Foundation for Libraries

<v-clicks>

- **BCrypt** - Password hashing (builds on MessageDigest)
- **JWT tokens** - Authentication tokens (builds on Signature)
- **OAuth implementations** - Authorization frameworks (builds on all three)

</v-clicks>

---
transition: slide-left
---

# Key Takeaways

## Your Cryptographic Toolkit

<v-clicks>

- **Cipher class** - For encryption and decryption
- **Signature class** - For digital signatures and certificates
- **MessageDigest class** - For secure password hashing with salt
- **Certificates** - For secure public key distribution

</v-clicks>

## Master These Foundations

<v-clicks>

- **Understand the concepts** - Why each tool exists
- **Know which classes to use** - The right tool for each job
- **Foundation for all security libraries** - BCrypt, JWT, OAuth build on these

</v-clicks>

---
transition: slide-left
layout: center
---

# Summary

Your toolkit for building secure Java applications: 

**Cipher**, **Signature**, **MessageDigest**, and **Certificates**

---
transition: slide-left
layout: center
---

# Next: Git Collaboration

Version control workflows and team development