
# Video Script: Java Scanner & User Input

**Goal:** 4. Capture user input from the console using the Scanner class.
**Target Duration:** 4-5 minutes

---

### SCENE 1: Introduction (0:00 - 0:30)

**(Show Slide 1: Title Slide - "Java Scanner & User Input")**

**YOU:**
"Welcome back. So far, our programs have been self-contained. In this video, we'll learn how to make them interactive by reading input from the user at the console, using the `java.util.Scanner` class."

**(Transition to Slide 2: Reading Console Input)**

**YOU:**
"The `Scanner` is the standard tool for this job. It's powerful, but it has a few quirks. The most important rule when dealing with user input is to never trust it. Always assume the user will enter something unexpected, and write your code to handle that gracefully."

---

### SCENE 2: The `nextInt()` vs `nextLine()` Pitfall (0:30 - 1:30)

**(Show Slide 3: The nextInt() vs nextLine() Pitfall)**

**YOU:**
"Let's address the single biggest point of confusion with `Scanner` right away. You might be tempted to use methods like `nextInt()` or `nextDouble()` to read numbers, but this can cause problems. When you type a number and hit Enter, `nextInt()` reads the number, but it leaves the newline character—the 'Enter' key press—in the input buffer."

**YOU:**
"If your next call is to `nextLine()`, it will immediately read that leftover newline and return an empty string, effectively skipping the prompt for the user. It's a frustrating bug."

**YOU:**
"The solution is simple: **always read input one full line at a time using `nextLine()`**. Then, if you need a number, you can parse it from the string. This completely avoids the problem."

---

### SCENE 3: Code Demo - The Validation Loop (1:30 - 3:00)

**(Show Slide 4: The Robust Validation Loop)**

**YOU:**
"Now let's see how to build a robust validation loop that handles any input gracefully."

**(Transition to IDE showing `EmployeeInput.java`)**

**YOU:**
"Let's look at a robust example in the `EmployeeInput.java` file. We're going to ask the user for several pieces of information to create an employee record."

**(Scroll to the `getIntInput` method)**

**YOU:**
"How do we handle cases where the user types 'abc' when we're expecting a number? We use a validation loop. Look at this `getIntInput` method. It starts an infinite `while(true)` loop. Inside, a `try-catch` block attempts to read a line and parse it as an integer. If it succeeds, it returns the number and the loop ends."

**YOU:**
"But if `Integer.parseInt` fails because the input isn't a valid number, it throws a `NumberFormatException`. We `catch` that exception, print a friendly error message, and the loop continues, prompting the user to try again. This pattern is extremely useful for creating robust interactive programs."

**(Briefly show the `getDoubleInput` and `getDateInput` methods)**

**YOU:**
"We use the exact same pattern for reading a double for the salary and even for parsing a date in a specific format."

---

### SCENE 4: Code Demo - Try-With-Resources (3:00 - 3:45)

**(Show Slide 5: Resource Management)**

**YOU:**
"One last important point. `Scanner` is a resource that needs to be closed when you're done with it to prevent resource leaks. The best way to do this is with a `try-with-resources` statement."

**(Return to IDE, highlight the `main` method's `try` block)**

**YOU:**
"Here in our `main` method, we declare the scanner inside the parentheses of the `try` block. This tells Java to automatically call the `close()` method on the scanner for us when the block is finished, whether it completes normally or an error occurs. It's the modern, safe way to manage resources."

---

### SCENE 5: Conclusion (3:45 - 4:15)

**(Show Slide 6: Key Takeaways)**

**YOU:**
"To recap: Use the `Scanner` class for console input. To avoid common bugs, always read a full line with `nextLine()` and then parse the string. Use a `while` loop with a `try-catch` block to create robust validation loops that can handle bad input. And finally, always manage your `Scanner` with a `try-with-resources` statement."

**YOU:**
"Thanks for watching. I'll see you in the next video."
