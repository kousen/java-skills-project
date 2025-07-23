---
layout: cover
--- 

# Java Naming Conventions

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 1: Apply standard naming conventions and formatting style to write readable Java code.
  </span>
</div>

---

# Why Naming Matters

<v-clicks>

- Code is read more often than it is written.
- Clear names reduce cognitive load.
- Well-named code is self-documenting.

</v-clicks>

---

# The Core Conventions

<div class="grid grid-cols-2 gap-8">

<div>

## **Classes & Interfaces**
- `PascalCase`
- Nouns (e.g., `Employee`, `SalaryCalculator`)

## **Constants**
- `UPPER_SNAKE_CASE`
- Unchanging `static final` fields (e.g., `MAX_EMPLOYEES`)

</div>

<div>

## **Variables & Methods**
- `camelCase`
- Verbs for methods (e.g., `calculateSalary()`)

## **Packages**
- `lowercase.with.dots`
- Reverse domain name (e.g., `com.oreilly.javaskills`)

</div>

</div>

---

# Let's See It In Action

Now, let's look at the `NamingConventions.java` file to see these rules applied in practice.

```java
// Good Example: A Constant
public static final String COMPANY_NAME = "O'Reilly Media, Inc.";

// Good Example: A Class Name
public class NamingConventions { ... }

// Good Example: A Method Name
public static void printEmployeeDetails(...) {
    // Good Example: A Variable Name
    String employeeName = "John Doe";
}
```

---

# Common Mistakes to Avoid

<div class="grid grid-cols-2 gap-8">

<div>

## ❌ **Bad Examples**

```java
// Problem: Not descriptive
String n = "Jane Doe";

// Problem: Looks like a constant
String ANOTHER_NAME = "Confusing";

// Problem: Uses underscores
int employee_id = 67890;

// Problem: Looks like a class
String Name = "Peter Jones";
```

</div>

<div>

## ✅ **Good Alternatives**

```java
// Descriptive and clear
String fullName = "Jane Doe";

// Correct camelCase
String anotherName = "Not confusing";

// Correct camelCase
int employeeId = 67890;

// Correct camelCase
String name = "Peter Jones";
```

</div>

</div>

---

# Key Takeaways

<v-clicks>

- **Classes**: `PascalCase`
- **Variables/Methods**: `camelCase`
- **Constants**: `UPPER_SNAKE_CASE`

</v-clicks>

<v-clicks>

- Sticking to these conventions makes your code professional, readable, and maintainable.

</v-clicks>