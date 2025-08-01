---
layout: cover
---

# Modern File I/O with NIO.2

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 7B: Master modern file I/O using the NIO.2 API for cleaner, more efficient code.
  </span>
</div>

---

# Contact Info

**Ken Kousen**<br>
**Kousen IT, Inc.**

- **ken.kousen@kousenit.com**
- **http://www.kousenit.com**
- **http://kousenit.org** (blog)
- **Social Media:**
  - **[@kenkousen](https://twitter.com/kenkousen)** (Twitter)
  - **[@kousenit.com](https://bsky.app/profile/kousenit.com)** (Bluesky)
  - **[https://www.linkedin.com/in/kenkousen/](https://www.linkedin.com/in/kenkousen/)** (LinkedIn)
- ***Tales from the jar side*** (free newsletter)
  - **https://kenkousen.substack.com**
  - **https://youtube.com/@talesfromthejarside**

# Traditional vs Modern Comparison

<div class="grid grid-cols-2 gap-8">

<div>

## **Traditional java.io**
```java
FileWriter writer = new FileWriter("employees.txt");
BufferedWriter buffered = new BufferedWriter(writer);
try {
    buffered.write("John Doe,Engineering,75000");
    buffered.newLine();
} finally {
    buffered.close(); // Don't forget!
}
```

<v-click>

**8 lines**, multiple wrapper classes, manual resource management

</v-click>

</div>

<div>

## **Modern java.nio.file**
```java
Path path = Paths.get("employees.txt");
Files.writeString(path, "John Doe,Engineering,75000");
```

<v-click>

**2 lines**, automatic resource management, cleaner code

</v-click>

</div>

</div>

# Core NIO.2 Classes

<v-clicks>

## **Path** - Modern replacement for File
```java
Path path = Paths.get("data", "employees.txt");  // Cross-platform paths
```

## **Paths** - Factory for creating Path objects  
```java
Path absolute = Paths.get("/Users/ken/data/file.txt");
Path relative = Paths.get("reports", "2024", "summary.txt");
```

## **Files** - Static utility methods for everything
```java
Files.writeString(path, content);     // Write
Files.readString(path);               // Read
Files.copy(source, target);           // Copy
Files.deleteIfExists(path);           // Delete
```

</v-clicks>

# One-Line File Operations

From `ModernFileIOExercise.java` - incredibly simple operations:

<div class="grid grid-cols-2 gap-8">

<div>

## **Writing Files**
```java
// Write a string - ONE LINE!
Files.writeString(path, content);

// Write multiple lines - ONE LINE!
List<String> employees = List.of(
    "Alice,Engineering,95000",
    "Bob,Marketing,87000"
);
Files.write(path, employees);
```

</div>

<div>

## **Reading Files**
```java
// Read entire file - ONE LINE!
String content = Files.readString(path);

// Read all lines - ONE LINE!
List<String> lines = Files.readAllLines(path);

// Stream large files
try (Stream<String> lines = Files.lines(path)) {
    lines.filter(line -> line.contains("Engineering"))
         .forEach(System.out::println);
}
```

</div>

</div>

# Common File Operations

NIO.2 makes everything simple and readable:

<div class="grid grid-cols-2 gap-8">

<div>

## **File Checks & Properties**
```java
// Check existence
if (Files.exists(path)) { ... }

// Get file attributes
BasicFileAttributes attrs = 
    Files.readAttributes(path, BasicFileAttributes.class);
System.out.println("Size: " + attrs.size());
System.out.println("Created: " + attrs.creationTime());
```

</div>

<div>

## **File Operations**
```java
// Copy files
Files.copy(source, target, 
    StandardCopyOption.REPLACE_EXISTING);

// Create directories (including parents)
Files.createDirectories(Paths.get("data/backups"));

// Delete safely
Files.deleteIfExists(path);
```

</div>

</div>

# Try It Out: Modern File I/O Exercise

`com.oreilly.javaskills.ModernFileIOExercise` shows the power of NIO.2:

<div class="grid grid-cols-2 gap-8">

<div>

## **Stream Processing**
<v-clicks>

- **Filter employees** by department using streams
- **Calculate averages** from file data  
- **Process large files** without loading all into memory
- **Directory walking** with Files.walk()
- **Pattern matching** with Files.find()

</v-clicks>

</div>

<div>

## **Advanced Operations**
<v-clicks>

- **One-line reading/writing** with text blocks
- **List processing** from/to files
- **Directory tree operations**
- **File attribute inspection**
- **Batch file operations**

</v-clicks>

</div>

</div>

# When to Use Each API

<div class="grid grid-cols-2 gap-8">

<div>

## **Use NIO.2 (java.nio.file) When:**
<v-clicks>

- **Writing new code** (always prefer this!)
- **Simple file operations** (reading/writing)
- **Directory operations** are needed
- **Cross-platform paths** are important
- **Modern, clean code** is the goal

</v-clicks>

</div>

<div>

## **Use Traditional I/O When:**
<v-clicks>

- **Maintaining legacy code**
- **Very specific streaming** requirements  
- **Working with old Java versions** (pre-7)
- **Interfacing with legacy libraries**

</v-clicks>

</div>

</div>

# Key Takeaways

<v-clicks>

- **NIO.2 is the modern way** to handle files in Java
- **One-line operations** for most common tasks
- **Automatic resource management** - no manual cleanup
- **Stream-based processing** for large files  
- **Cross-platform paths** and better error messages
- **Always prefer NIO.2 for new development**

</v-clicks>

<div class="mt-8">
<v-click>

**Your Challenge:** Complete the ModernFileIOExercise and experience the difference!

</v-click>
</div>