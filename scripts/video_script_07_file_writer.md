
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

### SCENE 4: Code Demo - Traditional Approach (2:15–3:00)

**(Transition to IDE showing `com.oreilly.javaskills.EmployeeFileWriter.java`)**

**YOU:**
"Let's look at our `com.oreilly.javaskills.EmployeeFileWriter` example demonstrating the traditional approach. This shows both the power and the verbosity of traditional file I/O."

**(Highlight the try-with-resources with BufferedWriter)**

**YOU:**
"Here's the key pattern: we wrap a `FileWriter` in a `BufferedWriter` for efficiency. The try-with-resources ensures proper cleanup. Notice how we manually call `newLine()` for platform-independent line breaks."

**(Show the PrintWriter alternative)**

**YOU:**
"For formatted output, we often add a `PrintWriter` wrapper. This gives us convenient methods like `printf` and `println`. Yes, that's three layers of writers! Each serves a purpose, but it's definitely verbose."

**(Show the append mode example)**

**YOU:**
"One useful feature: passing `true` as the second parameter to FileWriter enables append mode. This is perfect for log files where you want to add entries without overwriting existing content."

---

### SCENE 5: Try It Out Exercise (3:00–3:45)

**(Transition to IDE showing `com.oreilly.javaskills.FileWriterExercise.java`)**

**YOU:**
"Now for your **Try It Out** exercise. We'll create employee reports and logs using traditional file I/O."

**(Show the createEmployeeReport method)**

**YOU:**
"This method generates a formatted salary report. Notice how PrintWriter makes formatting easy with `printf`. We calculate totals and averages, creating a professional-looking text report."

**(Show the CSV writing example)**

**YOU:**
"Here we're writing CSV data—perfect for Excel import. The traditional API handles this well, though we need to manually format each line and remember those commas and quotes."

**YOU:**
"Try the exercise: create different types of files - reports, logs, configuration files. Practice with append mode for ongoing logs. Remember: always use try-with-resources!"

---

### SCENE 6: Conclusion (3:45–4:15)

**(Show Slide 6: Key Takeaways)**

**YOU:**
"To wrap up: traditional file I/O with FileWriter and BufferedWriter is still widely used, especially in legacy code. The key patterns are: always use try-with-resources, wrap FileWriter in BufferedWriter for efficiency, and remember append mode for log files."

**YOU:**
"While this approach works well, there's a more modern way. In our bonus video 7B, I'll show you the NIO.2 API that makes file operations incredibly simple—often just one line of code!"

**YOU:**
"Thanks for watching! Practice with the FileWriterExercise, and I'll see you in the next video where we explore the modern approach to file I/O."
