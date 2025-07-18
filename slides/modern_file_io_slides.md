---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Modern File I/O with NIO.2
  Learn the modern way to handle files in Java
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Modern File I/O with NIO.2
---

# Modern File I/O with NIO.2

The Modern Way to Handle Files in Java

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Press Space for next page <carbon:arrow-right class="inline"/>
  </span>
</div>

---
transition: slide-left
---

# Why NIO.2?

## Problems with Traditional I/O

<v-clicks>

- Verbose and error-prone
- Poor error messages
- Limited file system operations

</v-clicks>

## NIO.2 Benefits (Java 7+)

<v-clicks>

- Simpler, more readable code
- Better error handling
- Rich set of file operations

</v-clicks>

---
transition: slide-left
---

# Core NIO.2 Classes

## Essential Classes

<v-clicks>

- **Path** - Represents file/directory location
- **Paths** - Factory for creating Path objects
- **Files** - Utility class with static methods

</v-clicks>

## Modern Approach

<v-clicks>

- Path-based instead of File-based
- Functional style operations
- Built-in exception handling

</v-clicks>

---
transition: slide-left
---

# Creating Paths

## Path vs String

```java
// Traditional approach
File file = new File("employees.txt");

// Modern approach
Path path = Paths.get("employees.txt");

// Alternative syntax (Java 11+)
Path path = Path.of("employees.txt");
```

---
transition: slide-left
---

# Path Operations

## Working with Paths

```java
Path employeeFile = Paths.get("data", "employees.txt");

// Get components
System.out.println("Filename: " + employeeFile.getFileName());
System.out.println("Parent: " + employeeFile.getParent());
System.out.println("Absolute: " + employeeFile.toAbsolutePath());

// Path manipulation
Path backup = employeeFile.resolveSibling("employees.backup");
```

---
transition: slide-left
---

# Reading Files

## Simple File Reading

```java
// Read entire file as string (Java 11+)
String content = Files.readString(path);

// Read all lines
List<String> lines = Files.readAllLines(path);

// Process line by line
try (Stream<String> lines = Files.lines(path)) {
    lines.filter(line -> line.contains("Engineering"))
         .forEach(System.out::println);
}
```

---
transition: slide-left
---

# Writing Files

## Simple File Writing

```java
// Write string to file (Java 11+)
String employeeData = "John Doe,Engineering,75000";
Files.writeString(path, employeeData);

// Write lines
List<String> employees = Arrays.asList(
    "Alice,Engineering,80000",
    "Bob,Marketing,65000"
);
Files.write(path, employees);
```

---
transition: slide-left
---

# File Operations

## Common Operations

```java
// Check if file exists
if (Files.exists(path)) {
    System.out.println("File found!");
}

// Create directories
Files.createDirectories(Paths.get("data", "backups"));

// Copy file
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

// Delete file
Files.deleteIfExists(path);
```

---
transition: slide-left
---

# Employee File Manager

## Complete Example

```java
public class ModernEmployeeFileManager {
    private final Path dataDir = Paths.get("employee-data");
    private final Path employeeFile = dataDir.resolve("employees.csv");
    
    public void saveEmployees(List<Employee> employees) throws IOException {
        Files.createDirectories(dataDir);
        
        List<String> lines = employees.stream()
            .map(emp -> String.format("%s,%s,%.2f", 
                emp.getName(), emp.getDepartment(), emp.getSalary()))
            .collect(Collectors.toList());
            
        Files.write(employeeFile, lines);
    }
}
```

---
transition: slide-left
---

# Reading Employee Data

## Loading from CSV

```java
public List<Employee> loadEmployees() throws IOException {
    if (!Files.exists(employeeFile)) {
        return new ArrayList<>();
    }
    
    return Files.lines(employeeFile)
        .map(line -> line.split(","))
        .map(parts -> new Employee(
            parts[0],                    // name
            parts[1],                    // department  
            Double.parseDouble(parts[2]) // salary
        ))
        .collect(Collectors.toList());
}
```

---
transition: slide-left
---

# Directory Operations

## Working with Directories

```java
// Create backup directory
Path backupDir = Paths.get("backups");
Files.createDirectories(backupDir);

// List files in directory
try (Stream<Path> files = Files.list(dataDir)) {
    files.filter(path -> path.toString().endsWith(".csv"))
         .forEach(System.out::println);
}

// Walk directory tree
try (Stream<Path> walk = Files.walk(dataDir)) {
    walk.filter(Files::isRegularFile)
        .forEach(this::processFile);
}
```

---
transition: slide-left
---

# File Attributes

## Getting File Information

```java
// Basic attributes
System.out.println("Size: " + Files.size(path));
System.out.println("Last modified: " + Files.getLastModifiedTime(path));
System.out.println("Readable: " + Files.isReadable(path));
System.out.println("Writable: " + Files.isWritable(path));

// Detailed attributes
BasicFileAttributes attrs = Files.readAttributes(path, 
    BasicFileAttributes.class);
System.out.println("Creation time: " + attrs.creationTime());
```

---
transition: slide-left
---

# Watching for Changes

## File System Monitoring

```java
WatchService watcher = FileSystems.getDefault().newWatchService();

dataDir.register(watcher, 
    StandardWatchEventKinds.ENTRY_CREATE,
    StandardWatchEventKinds.ENTRY_MODIFY,
    StandardWatchEventKinds.ENTRY_DELETE);

// Process events
WatchKey key = watcher.take();
for (WatchEvent<?> event : key.pollEvents()) {
    System.out.println("Event: " + event.kind() + 
                      " File: " + event.context());
}
```

---
transition: slide-left
---

# Error Handling

## Better Exception Messages

```java
try {
    Files.copy(source, target);
} catch (NoSuchFileException e) {
    System.err.println("Source file not found: " + e.getFile());
} catch (FileAlreadyExistsException e) {
    System.err.println("Target already exists: " + e.getFile());
} catch (IOException e) {
    System.err.println("Copy failed: " + e.getMessage());
}
```

---
transition: slide-left
---

# Comparison: Old vs New

## Traditional I/O

```java
// Old way - verbose and error-prone
FileWriter writer = new FileWriter("employees.txt");
BufferedWriter buffered = new BufferedWriter(writer);
try {
    buffered.write("John Doe,Engineering,75000");
    buffered.newLine();
} finally {
    buffered.close();
}
```

---
transition: slide-left
---

# Comparison: Old vs New

## Modern NIO.2

```java
// New way - simple and clear
Path path = Paths.get("employees.txt");
Files.writeString(path, "John Doe,Engineering,75000");

// Or with multiple lines
List<String> lines = Arrays.asList(
    "John Doe,Engineering,75000",
    "Jane Smith,Marketing,65000"
);
Files.write(path, lines);
```

---
transition: slide-left
---

# Advanced Features

## Memory-Mapped Files

```java
// For large files - map to memory
try (FileChannel channel = FileChannel.open(path, 
        StandardOpenOption.READ)) {
    
    MappedByteBuffer buffer = channel.map(
        FileChannel.MapMode.READ_ONLY, 0, channel.size());
    
    // Process buffer efficiently
}
```

---
transition: slide-left
---

# Best Practices

## Modern File I/O Tips

<v-clicks>

- Use `Path` instead of `File`
- Prefer `Files` utility methods
- Use try-with-resources for streams

</v-clicks>

---
transition: slide-left
---

# Best Practices (continued)

## Performance & Safety

<v-clicks>

- Use `Files.exists()` before operations
- Handle specific exceptions
- Consider `Files.walk()` for large directories

</v-clicks>

---
transition: slide-left
---

# When to Use Each

## Traditional I/O (FileWriter)

<v-clicks>

- Legacy code maintenance
- Streaming large amounts of data
- Custom buffering requirements

</v-clicks>

## Modern NIO.2

<v-clicks>

- New development (recommended)
- File system operations
- Simple read/write operations

</v-clicks>

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- NIO.2 provides simpler, more readable file operations
- Path and Files classes are the modern approach
- Better error handling and file system integration
- Use NIO.2 for new development
- Traditional I/O still has its place

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Object-Oriented Programming

Building robust applications with classes and objects