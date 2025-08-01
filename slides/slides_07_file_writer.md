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

---

# Why Write to Files?

<v-clicks>

- **Data Persistence**: Save program results and user data
- **Logging**: Record application events and errors  
- **Configuration**: Create and modify settings files
- **Data Exchange**: Generate reports and files (like CSVs) for other systems

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

# Traditional File I/O Example

From `com.oreilly.javaskills.EmployeeFileWriter.java` - creating employee reports:

```java
// The traditional approach requires layered writers
try (PrintWriter writer = new PrintWriter(
        new BufferedWriter(new FileWriter(REPORT_FILE)))) {
    
    // Write report header
    writer.println("EMPLOYEE SALARY REPORT");
    writer.println("Generated: " + LocalDateTime.now().format(DATE_FORMAT));
    writer.println("=" .repeat(60));
    
    // Formatted employee data
    for (Employee emp : employees) {
        writer.printf("%-20s %-15s $%,14.2f%n", 
            emp.name(), emp.department(), emp.salary());
    }
}
```

<v-click>

**Key Pattern**: FileWriter → BufferedWriter → PrintWriter for efficiency and formatting

</v-click>

---

# Try It Out: FileWriter Exercise

`com.oreilly.javaskills.FileWriterExercise` demonstrates practical file operations:

<div class="grid grid-cols-2 gap-8">

<div>

## **Basic Operations**
<v-clicks>

- **Simple writing**: FileWriter with try-with-resources
- **Buffered writing**: Wrap FileWriter in BufferedWriter  
- **Formatted reports**: Use PrintWriter for printf
- **Append mode**: FileWriter(filename, true)
- **CSV generation**: Manual formatting for Excel

</v-clicks>

</div>

<div>

## **Example Output Files**
<v-clicks>

- Employee salary reports
- Activity log files  
- CSV data for spreadsheets
- Configuration files
- Formatted text documents

</v-clicks>

</div>

</div>

---

# Key Takeaways

<v-clicks>

- **Always use try-with-resources** for automatic resource management
- **Wrap FileWriter in BufferedWriter** for better performance  
- **Use PrintWriter for formatting** with printf and println methods
- **Append mode**: FileWriter(filename, true) for log files
- **Traditional I/O is still widely used** in legacy systems

</v-clicks>

<div class="mt-8">
<v-click>

**Next**: See Video 7B for the modern NIO.2 approach - often just one line of code!

</v-click>
</div>

