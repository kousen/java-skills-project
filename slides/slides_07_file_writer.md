

---
layout: cover
---

# Writing to Files in Java

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 7: Write content to a file using FileWriter or BufferedWriter.
  </span>
</div>

---
layout: section
---

# Why Write to Files?

<v-clicks>

- **Data Persistence**: Save program results and user data.
- **Logging**: Record application events and errors.
- **Configuration**: Create and modify settings files.
- **Data Exchange**: Generate reports and files (like CSVs) to be used by other systems.

</v-clicks>

---

# Two Approaches: `java.nio` vs `java.io`

<div class="grid grid-cols-2 gap-8">

<div>

## **Modern: `java.nio` (NIO.2)**
- **Stands for**: New I/O
- **Classes**: `Path`, `Paths`, `Files`
- **Pros**: More powerful, better exception handling, feature-rich.
- **Recommendation**: **Always prefer this for new code.**

</div>

<div>

## **Traditional: `java.io`**
- **Stands for**: Input/Output
- **Classes**: `File`, `FileWriter`, `PrintWriter`
- **Pros**: Simple for basic cases.
- **Cons**: Less efficient, older API, less informative exceptions.

</div>

</div>

---

# The `try-with-resources` Statement

This is the **most important concept** for file I/O.

```java
// This syntax automatically closes the resource (the writer)
// when the block is finished, even if an error occurs.

try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
    
    // ... write to the file here ...

} // No finally block needed! The writer is closed automatically.
```

<v-clicks>

- **Prevents resource leaks.**
- **Makes code cleaner and safer.**
- **Always use it for I/O operations.**

</v-clicks>

---

# Code Demo: `SimpleFileWriter.java`

Let's compare the two approaches for writing a few lines to a file.

<div class="grid grid-cols-2 gap-8">

<div>

## **Modern `nio` Approach**
```java
Path filePath = Paths.get("output.txt");

try (BufferedWriter writer = 
    Files.newBufferedWriter(filePath)) {
    
    writer.write("A line of text.");
    writer.newLine();
}
```

</div>

<div>

## **Traditional `io` Approach**
```java
// More layers to achieve the same result
try (PrintWriter writer = new PrintWriter(
    new BufferedWriter(
        new FileWriter("output.txt")))) {
            
    writer.println("A line of text.");
}
```

</div>

</div>

---
layout: section
---

# Key Takeaways

<v-clicks>

- For new code, **prefer the `java.nio` API** (`Path`, `Files`).
- **Always use `try-with-resources`** to ensure files are closed automatically.
- `BufferedWriter` is efficient for writing text.
- `writer.newLine()` is better than `\n` because it's platform-independent.

</v-clicks>

