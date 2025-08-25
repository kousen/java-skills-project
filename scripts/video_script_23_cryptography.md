# Video Script: Topic 23—Cryptographic APIs

**Duration**: ~11 minutes  
**Learning Objective**: Understand cryptographic concepts and master the core Java APIs for encryption, digital signatures, secure hashing, and certificates.

---

## Scene 1: Introduction (1 minute)

**Host:** Welcome to Java Skills! In this video, we're going to talk about the cryptographic APIs available in Java that are essential for building secure applications. By the end of this video, you'll understand the core security concepts and know which Java classes to use for encryption, digital signatures, and password security.

**[Show Slide: Three Essential Security Skills]**

Today we're focusing on three fundamental skills every Java developer needs: encrypting sensitive data, verifying document integrity, and securely storing passwords. These are the building blocks that underpin libraries like BCrypt, JWT tokens, and OAuth implementations.

---

## Scene 2: Cryptographic Concepts (3 minutes)

**Host:** Let's start with the concepts. Cryptography gives us three essential security properties.

**[Show Slide: Public/Private Key Pairs]**

First is **confidentiality** through encryption. Public-key cryptography uses key pairs—one key encrypts, the other decrypts. If I encrypt with either key, only the other key can decrypt it. For example, if I encrypt a message with your public key, only you can decrypt it, because only you have your private key. You would then encrypt your reply with my public key, and only I could decrypt it. This keeps data secret during transmission.

**[Show Slide: Digital Signatures Concept]**

The second concept is **data integrity and non-repudiation**. This is achieved through digital signatures. Here's the key insight: a digital signature is actually a hash of the document encrypted with the sender's private key. 

Let me break that down using a concrete example. Say you are going to do a job for a client, and they want you to sign a non-disclosure agreement. The agreement itself isn't secret—it's a standard boilerplate document, but they need to know that you signed it, and that it wasn't changed during the process. A hash, also called a digest, is like a digital fingerprint of a document. It's a fixed length string of characters representing the overall document, made with a standard algorithm. We hash the NDA, and then encrypt that hash using our private key. That encrypted hash is the digital signature. We then send the signature to the recipient. When they get it, they decrypt it with our public key to get back our generated hash. They also generate what the hash should be from the original document, using the same algorithm we did. Then they compare the two hashes. If they match, we know two things: the document hasn't been tampered with, because the hashes matched, and that the digital signature definitely came from us, because only we could have encrypted it because only we have our private key. That's both integrity and non-repudiation.

**[Show Slide: Certificate Trust Chain]**

Third, we need to share public keys securely through certificates. A certificate contains a public key plus identity information, all signed by a trusted Certificate Authority. This creates a chain of trust—you trust the CA, the CA vouches for the certificate, so you can trust the public key.

These concepts work together: certificates distribute public keys, encryption provides confidentiality, and digital signatures provide integrity and authenticity.

---

## Scene 3: Java Encryption APIs (2 minutes)

**Host:** Now let's see how Java implements these concepts. For encryption and decryption, we use the `Cipher` class.

**[Show Code: CryptographicAPIs.java - demonstrateEncryption method]**

The pattern is always the same: get a `Cipher` instance, initialize it with a mode and key, then call `doFinal()` to process the data. Here we're encrypting sensitive data like a credit card number with the public key, then decrypting it with the private key.

Notice the three key methods: `getInstance()` to get the cipher, `init()` to set the mode and key, and `doFinal()` to actually encrypt or decrypt. This `Cipher` class is your go-to for any encryption needs in Java.

---

## Scene 4: Digital Signature APIs (2 minutes) 

**Host:** For digital signatures, we use the `Signature` class. Let me show you the two-step process.

**[Show Code: CryptographicAPIs.java - demonstrateDigitalSignatures method]**

To create a signature: get a `Signature` instance, initialize it for signing with the private key, update it with the document data, then call `sign()`. To verify: initialize for verification with the public key, update with the same document data, then call `verify()` with the signature.

Watch what happens when someone tries to tamper with the document - the verification fails immediately. That's how we detect any changes and prove the document's integrity.

The key methods here are `initSign()` and `initVerify()`, `update()` to feed in the data, and `sign()` or `verify()` to complete the operation.

---

## Scene 5: Secure Hashing APIs (1.5 minutes)

**Host:** For password security, we use the `MessageDigest` class with a crucial addition: salt.

**[Show Code: CryptographicAPIs.java - demonstrateSecureHashing method]**

Never store passwords in plain text! Instead, generate a random salt, which is just a random string that gets combined with the password, and hash the result. The salt is stored along with the hashed combination. The salt prevents rainbow table attacks and makes each password hash unique.

The process: get a `MessageDigest` instance, update it with the salt, then digest the password. To verify later, repeat the same process and compare the hashes using `MessageDigest.isEqual()`.

This is the foundation that libraries like BCrypt build upon—they use these same principles with additional security features.

---

## Scene 6: Certificate APIs (1 minute)

**Host:** The final piece is certificates for secure public key distribution. We've seen encryption and signatures, but how do you get someone's public key safely?

**[Show Code: CryptographicAPIs.java - demonstrateCertificates method]**

Certificates solve the key distribution problem. A certificate is essentially a digital signature by a trusted Certificate Authority on someone's public key and identity information. 

Here we see the CA creates a certificate for Alice by signing her public key with the CA's private key. When Bob wants to encrypt data for Alice, he can verify the certificate using the CA's public key. If the verification succeeds, Bob knows he can trust Alice's public key because the CA vouched for it.

This creates a chain of trust—you trust the CA, the CA vouches for Alice, so you can trust Alice's public key. The same `Signature` class we just learned handles both document signing and certificate verification.

---

## Scene 7: Practical Application (0.5 minutes)

**Host:** So when do you use each approach?

**[Show Slide: When to Use Each API]**

- Use `Cipher` when you need to encrypt sensitive data for storage or transmission
- Use `Signature` when you need to verify document integrity or authenticate the sender
- Use `MessageDigest` with salt when you need to securely store passwords
- Use certificates when you need to distribute public keys securely

Remember, third-party libraries like BCrypt for password hashing, JJWT for JSON Web Tokens, and OAuth implementations all build on these core Java APIs. Master these foundations, and you'll understand how all the security libraries work under the hood.

**[Show Slide: Key Takeaways]**

Your key takeaways: `Cipher` for encryption, `Signature` for digital signatures, `MessageDigest` for secure hashing, and certificates for secure key distribution. These four concepts are your toolkit for building secure Java applications.

Thanks for watching, and I'll see you in the next video!

---

## Technical Notes for Filming

**Code References:**
- Main demo: `security/src/main/java/com/oreilly/security/CryptographicAPIs.java`
- Focus on the four main methods: `demonstrateEncryption()`, `demonstrateDigitalSignatures()`, `demonstrateSecureHashing()`, `demonstrateCertificates()`
- Show actual code execution output to demonstrate concepts

**Key Teaching Points:**
1. **Concepts first**: Explain WHY before showing HOW
2. **Four clear use cases**: Encryption, signatures, hashing, certificates
3. **Standard Java APIs only**: No third-party dependencies in demos
4. **Practical context**: Mention where BCrypt/JWT fit in the bigger picture
5. **Essential methods**: Focus on the key methods developers need to remember
6. **Certificate trust chains**: Show how certificates enable secure key distribution

**Slide Timing:**
- Intro concepts: 3-4 slides max
- Code demos: Show actual IDE with working examples
- Summary: Single slide with the four key concepts