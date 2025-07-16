
# Video Script: Java Naming Conventions

**Goal:** 1. Apply standard naming conventions and formatting style to write readable Java code.
**Target Duration:** 3-4 minutes

---

### SCENE 1: Introduction (0:00 - 0:30)

**(Show Slide 1: Title Slide - "Java Naming Conventions")**

**YOU:**
"Hi everyone, and welcome to this series on essential Java skills. Today, we're starting with a fundamental topic that's crucial for writing professional, readable code: **Java Naming Conventions**."

**(Transition to Slide 2: Why Naming Matters)**

**YOU:**
"Why does this matter so much? Because code is read far more often than it's written. Good naming conventions make your code self-documenting and easier for you and your team to understand and maintain."

---

### SCENE 2: The Core Conventions (0:30 - 1:30)

**(Show Slide 3: The Core Conventions)**

**YOU:**
"Java has a few core conventions that you'll see everywhere. Let's break them down."

**YOU:**
"First, **Classes and Interfaces** use `PascalCase`, also known as upper camel case. Think of nouns, like `Employee` or `SalaryCalculator`."

**YOU:**
"Next, **Variables and Methods** use `camelCase`, or lower camel case. Variables are typically nouns, like `employeeName`, and methods are verbs that describe an action, like `calculateSalary`."

**YOU:**
"Third, **Constants**, which are variables that don't change, use `UPPER_SNAKE_CASE`. These are `static final` fields, like `MAX_EMPLOYEES`."

**YOU:**
"Finally, **Packages** use all `lowercase.with.dots`, typically in a reverse domain name format, like `com.oreilly.javaskills`."

---

### SCENE 3: Code Demo - Good Examples (1:30 - 2:30)

**(Transition to IDE showing `NamingConventions.java`)**

**YOU:**
"Okay, let's see this in action. Here in our `NamingConventions.java` file, we can see these rules applied."

**(Highlight the relevant lines in the code as you speak)**

**YOU:**
"At the top, we have our class, `NamingConventions`, in `PascalCase`."

**YOU:**
"Here are two constants, `COMPANY_NAME` and `MAX_EMPLOYEES`, both in `UPPER_SNAKE_CASE`. Their values can't be changed."

**YOU:**
"Inside our `main` method, we have several variables like `employeeName` and `salaryAmount`, all following `camelCase`."

**YOU:**
"And we have our method, `printEmployeeDetails`, also in `camelCase`. It's a verb that clearly describes what it does."

---

### SCENE 4: Code Demo - Common Mistakes (2:30 - 3:15)

**(Show Slide 5: Common Mistakes to Avoid)**

**YOU:**
"It's also helpful to see what *not* to do. Let's look at some common mistakes."

**(Scroll down in `NamingConventions.java` to the "Bad Naming Conventions" section)**

**YOU:**
"Here, we have a variable `n`. This is a bad name because it's not descriptive. What does 'n' stand for? We should use something like `fullName` instead."

**YOU:**
"This variable, `ANOTHER_NAME`, is in all caps, which makes it look like a constant, but it isn't. This is very confusing and should be in `camelCase`."

**YOU:**
"The variable `employee_id` uses snake_case. While common in other languages like Python, the Java convention is to use `camelCase`."

**YOU:**
"And finally, the variable `Name` starts with a capital letter, which makes it look like a Class name. This can be very misleading."

---

### SCENE 5: Conclusion (3:15 - 3:45)

**(Show Slide 6: Key Takeaways)**

**YOU:**
"So, let's recap the key takeaways. Use `PascalCase` for classes, `camelCase` for variables and methods, and `UPPER_SNAKE_CASE` for constants."

**YOU:**
"Adopting these conventions is one of the easiest ways to make your code more professional and readable. That's all for this lesson. Thanks for watching, and I'll see you in the next video!"
