# Video Script: Modern File I/O with NIO.2

## Introduction (0:00-0:15)

Welcome back! In our last video, we learned about file I/O using FileWriter and BufferedWriter. Today, I want to show you the modern way to handle files in Java - using the NIO.2 API introduced in Java 7. Trust me, once you see how much simpler this is, you'll never want to go back to the old way.

## The Problem with Traditional I/O (0:15-0:45)

Let me show you the difference. Here's how we wrote to a file the traditional way:

[Show traditional code]

```java
FileWriter writer = new FileWriter("employees.txt");
BufferedWriter buffered = new BufferedWriter(writer);
try {
    buffered.write("John Doe,Engineering,75000");
    buffered.newLine();
} finally {
    buffered.close(); // Don't forget this!
}
```

That's a lot of boilerplate code just to write one line to a file! And if you forget to close the writer, you'll have resource leaks.

Now here's the modern way:

```java
Path path = Paths.get("employees.txt");
Files.writeString(path, "John Doe,Engineering,75000");
```

Two lines instead of eight. Much cleaner, and the resources are handled automatically.

## Core NIO.2 Classes (0:45-1:15)

The NIO.2 API revolves around three main classes. First, there's `Path` - think of it as a modern replacement for the old `File` class. It represents a location in the file system.

Then there's `Paths` - a factory class for creating Path objects. You call `Paths.get()` with a file name or path.

Finally, `Files` is where the magic happens. It's packed with static methods for every file operation you can imagine - reading, writing, copying, deleting, you name it.

## Reading Files Made Simple (1:15-1:45)

Reading files is incredibly simple now. Want to read an entire file as a string? One line:

```java
String content = Files.readString(path);
```

Want all lines as a List? Also one line:

```java
List<String> lines = Files.readAllLines(path);
```

Need to process a large file line by line without loading it all into memory? Use a Stream:

```java
try (Stream<String> lines = Files.lines(path)) {
    lines.filter(line -> line.contains("Engineering"))
         .forEach(System.out::println);
}
```

The try-with-resources automatically closes the stream when done.

## Writing Files (1:45-2:15)

Writing is just as simple. To write a string to a file:

```java
Files.writeString(path, employeeData);
```

To write multiple lines:

```java
List<String> employees = Arrays.asList(
    "Alice,Engineering,80000",
    "Bob,Marketing,65000"
);
Files.write(path, employees);
```

No manual buffering, no remembering to close resources, no try-catch blocks for basic operations.

## File Operations (2:15-2:45)

NIO.2 makes common file operations trivial. Check if a file exists:

```java
if (Files.exists(path)) {
    // File exists
}
```

Copy a file:

```java
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
```

Create directories:

```java
Files.createDirectories(Paths.get("data", "backups"));
```

Delete a file:

```java
Files.deleteIfExists(path);
```

Each of these operations would require multiple lines and careful error handling with the old API.

## Real Example: Employee File Manager (2:45-3:30)

Let's see this in action with our Employee Management System. Here's a complete file manager using NIO.2:

[Show ModernEmployeeFileManager code]

Look how clean this is! We create directories automatically, convert our employee list to CSV format using streams, and write everything in just a few lines.

Loading data back is equally elegant - we read lines as a stream, split each line, and map directly to Employee objects.

## Advanced Features (3:30-3:45)

NIO.2 also gives you powerful features like directory watching - you can monitor folders for changes in real-time. You can walk entire directory trees with `Files.walk()`. You can even memory-map large files for high-performance access.

## When to Use Each (3:45-4:00)

So when should you use traditional I/O versus NIO.2? For new development, always start with NIO.2. It's simpler, safer, and more powerful. Use traditional I/O only when maintaining legacy code or when you need very specific streaming behavior.

## Wrapping Up (4:00-4:15)

The NIO.2 API represents how file I/O should be done in modern Java. It's more readable, less error-prone, and incredibly powerful. Your code will be cleaner, your bugs will be fewer, and your productivity will be higher.

Next time, we'll dive into object-oriented programming and start building more sophisticated applications. Until then, embrace the modern way and happy coding!

## Code Examples Referenced:

1. Traditional vs modern file writing comparison
2. Simple file reading methods
3. ModernEmployeeFileManager class
4. Path operations and file system utilities
5. Directory operations and file monitoring