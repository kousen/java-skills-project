
# Video Script: Writing to Files in Java

**Goal:** 7. Write content to a file using FileWriter or BufferedWriter.
**Target Duration:** 4–5 minutes

---

### SCENE 1: Introduction (0:00–0:30)

**(Show Slide 1: Title Slide—"Writing to Files in Java")**

**YOU:**
"Hi everyone. In this video, we'll learn how to make our programs persist data by writing content to files. This is a fundamental skill for everything from saving user data to creating logs and reports."

**(Transition to Slide 2: Why Write to Files?)**

**YOU:**
"Writing to files allows our applications to save their state, create permanent records, and exchange data with other systems. It's a core part of almost any real-world application."

---

### SCENE 2: Two Approaches to File I/O (0:30–1:15)

**(Show Slide 3: Two Approaches)**

**YOU:**
"In modern Java, there are two main APIs for file operations. The traditional `java.io` package has been around since the beginning. It's simple, but has some drawbacks."

**YOU:**
"The modern approach is the `java.nio` package, which stands for New I/O. It was introduced to address the limitations of the original API. For any new code you write, you should **always prefer the `java.nio` package**. It's more powerful, has better error handling, and is more efficient."

---

### SCENE 3: The `try-with-resources` Statement (1:15–2:15)

**(Show Slide 4: The try-with-resources Statement)**

**YOU:**
"Before we write any code, we have to talk about the single most important concept for file handling in Java: the `try-with-resources` statement. Whenever you open a file or any other resource, you *must* close it when you're done to avoid resource leaks."

**YOU:**
"In the old days, this was done in a messy `finally` block. Today, we use `try-with-resources`. By declaring the resource inside the parentheses of the `try` statement, you tell Java to automatically close it for you, no matter what happens. It's cleaner, safer, and non-negotiable for modern Java I/O."

---

### SCENE 4: Code Demo - Traditional and Modern Mix (2:15–3:00)

**(Transition to IDE showing `com.oreilly.javaskills.EmployeeFileWriter.java`)**

**YOU:**
"Let's look at our `com.oreilly.javaskills.EmployeeFileWriter` example to see the key patterns for file writing."

**(Highlight the writeEmployeesToCsv method)**

**YOU:**
"Here's the modern way: `Files.newBufferedWriter(Paths.get(filename))`. This is cleaner than the traditional nested writer approach. We still get BufferedWriter's efficiency, but with less verbosity."

**(Show the writeEmployeesToJson method with text blocks)**

**YOU:**
"Notice how we can use text blocks with file writing. This makes multi-line content much cleaner—no more escape characters or manual line breaks."

**(Show the demonstrateFileOperations method)**

**YOU:**
"For file operations beyond writing, NIO.2 provides powerful one-liners: `Files.copy()`, `Files.readString()`, and file attribute checking. These used to require dozens of lines of code."

---

### SCENE 5: Try It Out Exercise (3:00–3:45)

**(Transition to IDE showing `com.oreilly.javaskills.FileWriterExercise.java`)**

**YOU:**
"Now for your **Try It Out** exercise. Open `FileWriterExercise.java`. You'll find several methods to implement, each building on the patterns we just saw."

**(Show the method signatures without implementations)**

**YOU:**
"Your tasks: First, implement `demonstrateBasicFileWriter` using plain FileWriter. Then progress to `demonstrateBufferedWriter` using the modern NIO.2 approach we just learned."

**(Highlight the createEmployeeReport TODO)**

**YOU:**
"For the employee report, you'll combine what you've learned: use PrintWriter for formatting, calculate totals and averages, and create a professional-looking report."

**YOU:**
"The full solutions are provided, but try implementing each method yourself first. Run the tests to verify your implementation. If you get stuck, peek at the solution for hints, then try to complete it on your own. Remember: always use try-with-resources!"

---

### SCENE 6: Conclusion (3:45–4:15)

**(Show Slide 6: Key Takeaways)**

**YOU:**
"To wrap up: traditional file I/O with FileWriter and BufferedWriter is still widely used, especially in legacy code. The key patterns are: always use try-with-resources, wrap FileWriter in BufferedWriter for efficiency, and remember append mode for log files."

**YOU:**
"While this approach works well, there's a more modern way. In our bonus video 7B, I'll show you the NIO.2 API that makes file operations incredibly simple—often just one line of code!"

**YOU:**
"Thanks for watching! Practice with the FileWriterExercise, and I'll see you in the next video where we explore the modern approach to file I/O."
