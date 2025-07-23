
---
layout: cover
--- 

# Java Scanner & User Input

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 4: Capture user input from the console using the Scanner class.
  </span>
</div>

---
layout: section
---

# Reading Console Input

<v-clicks>

- The `java.util.Scanner` class is the standard way to make your programs interactive.
- It can parse primitive types and strings from any input stream.
- We'll use it to read keyboard input from `System.in`.

</v-clicks>

<div class="mt-8">
<v-click>

**Golden Rule:** Never trust user input. Always validate it!

</v-click>
</div>

---

# The `nextInt()` vs `nextLine()` Pitfall

This is the most common problem new Java developers face with `Scanner`.

<div class="grid grid-cols-2 gap-8">

<div>

## ❌ **The Problem**
```java
// nextInt() reads the number but
// leaves the newline character behind.
int age = scanner.nextInt();

// nextLine() immediately reads the
// leftover newline and returns an
// empty string, skipping the prompt.
String name = scanner.nextLine(); 
```

</div>

<div>

## ✅ **The Solution**
```java
// Always read the full line.
String ageInput = scanner.nextLine();
int age = Integer.parseInt(ageInput);

// No more leftover newline!
String name = scanner.nextLine();
```

</div>

</div>

---

# The Robust Validation Loop

To handle bad input gracefully, use a `while(true)` loop with a `try-catch` block.

```java
private static int getIntInput(Scanner scanner) {
    while (true) { // Loop forever until we get valid input
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input); // Attempt to parse
        } catch (NumberFormatException e) {
            // If parsing fails, catch the error and prompt again
            System.out.print("Invalid number. Please try again: ");
        }
    }
}
```

---

# Resource Management

Always close your `Scanner` to prevent resource leaks. The `try-with-resources` statement is the best way to do this automatically.

```java
// The scanner is automatically closed at the end of the block.
try (Scanner scanner = new Scanner(System.in)) {
    
    // ... use the scanner to get input ...

} catch (Exception e) {
    System.err.println("An error occurred.");
}
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- Use `Scanner` for interactive console input.
- **Always use `nextLine()`** and parse the result to avoid buffer issues.
- Use a **validation loop** (`while` + `try-catch`) to handle bad input gracefully.
- Use **`try-with-resources`** to manage the `Scanner` lifecycle automatically.

</v-clicks>
