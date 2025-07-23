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

# Contact Info

**Ken Kousen**<br>
Kousen IT, Inc.

- ken.kousen@kousenit.com
- http://www.kousenit.com
- http://kousenit.org (blog)
- Social Media:
  - [@kenkousen](https://twitter.com/kenkousen) (Twitter)
  - [@kousenit.com](https://bsky.app/profile/kousenit.com) (Bluesky)
  - [https://www.linkedin.com/in/kenkousen/](https://www.linkedin.com/in/kenkousen/) (LinkedIn)
- *Tales from the jar side* (free newsletter)
  - https://kenkousen.substack.com
  - https://youtube.com/@talesfromthejarside

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

<v-click at="1">

## **Types**
- <span style="color: #00D4FF;">**PascalCase**</span>
- Classes (e.g., `Employee`, `SalaryCalculator`)
- Annotations: `@Override`, `@CustomAnnotation`
- Enums: `EmployeeStatus`, `PaymentType`
- Records: `PersonData`, `EmployeeRecord`

</v-click>

<v-click at="3">

## **Constants**
- <span style="color: #00D4FF;">**UPPER_SNAKE_CASE**</span>
- Unchanging `static final` fields (e.g., `MAX_EMPLOYEES`)

</v-click>

</div>

<div>

<v-click at="2">

## **Variables & Methods**
- <span style="color: #00D4FF;">**camelCase**</span>
- Variables: `employeeName`, `salaryAmount`
- Methods: `calculateSalary()`
- Parameters: `userId`, `isActive`
- Fields: `firstName`, `departmentId`

</v-click>

<v-click at="4">

## **Packages**
- <span style="color: #00D4FF;">**lowercase.with.dots**</span>
- Reverse domain name (e.g., `com.oreilly.javaskills`)

</v-click>

</div>

</div>

---

# Constants and Enums

```java
// Good Example: Constants (UPPER_SNAKE_CASE)
public static final String COMPANY_NAME = "O'Reilly Media, Inc.";
public static final NumberFormat CURRENCY_FORMATTER = 
    NumberFormat.getCurrencyInstance();

// Good Example: Enum (PascalCase like classes)
public enum Department {
    ENGINEERING("Engineering"), HR("Human Resources");
    // ... enum methods use camelCase
}
```

---

# Classes and Methods

```java
// Good Example: Class Name (PascalCase)
public class com.oreilly.javaskills.NamingConventions {
    
    @SuppressWarnings("ExtractMethodRecommender") // Annotation
    public com.oreilly.javaskills.NamingConventions() { ... } // Constructor matches class
    
    public static void main(String[] args) {
        var employeeName = "John Doe";        // Modern Java
        var dept = Department.ENGINEERING;    // Enum usage
    }
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

- **Types**: <span style="color: #00D4FF;">**PascalCase**</span>
- **Variables/Methods**: <span style="color: #00D4FF;">**camelCase**</span>
- **Constants**: <span style="color: #00D4FF;">**UPPER_SNAKE_CASE**</span>
- **Packages**: <span style="color: #00D4FF;">**lowercase.with.dots**</span>

</v-clicks>

<v-clicks>

- Adopting these conventions makes your code professional, readable, and maintainable.

</v-clicks>