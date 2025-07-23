
---
layout: cover
--- 

# The Single Responsibility Principle (SRP)

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 16: Apply the Single Responsibility Principle by ensuring each class has one reason to change.
  </span>
</div>

---

# Contact Info

Ken Kousen<br>
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
layout: section
---

# The Single Responsibility Principle

<v-clicks>

- The **S** in the **SOLID** acronym.
- Coined by Robert C. Martin (Uncle Bob).
- It states that a class should have **one, and only one, reason to change**.
- This doesn't mean a class should only have one method. It means all the methods and properties in a class should be closely related to a single, primary responsibility.

</v-clicks>

<div class="mt-8">
<v-click>

**Key Idea:** A class should do one thing, and do it well. This minimizes the impact of changes.

</v-click>
</div>

---

# Why is SRP Important?

When a class has multiple responsibilities:

<v-clicks>

- **High Coupling:** The class becomes coupled to multiple parts of the application that may not be related.
- **Difficult to Understand:** The class becomes a "god object" that does too much, making it hard to read and maintain.
- **Fragile:** A change in one responsibility can break another, unrelated responsibility.
- **Hard to Test:** It's difficult to write focused unit tests for a class that has many different behaviors.

</v-clicks>

---

# Example: A Class That Violates SRP

Imagine a single `Employee` class that handles everything.

```java
// VIOLATION of SRP
public class Employee {
    // Responsibility 1: Employee Data
    private String name;
    private double salary;

    // Responsibility 2: Database Logic
    public void saveToDatabase() {
        // ... code to connect to DB and save employee ...
    }

    // Responsibility 3: Payroll Calculation
    public double calculatePay() {
        // ... code to calculate payroll ...
    }

    // Responsibility 4: Report Generation
    public String generateReport() {
        // ... code to format a report ...
    }
}
```
This class has **four** reasons to change. A change in the database schema, payroll rules, or report format would all require changing this one class.

---

# Refactoring to Follow SRP

We can refactor this by separating each responsibility into its own class.

<div class="grid grid-cols-2 gap-4">

<div>

**`Employee`**
(Data only)

```java
public class Employee {
    private String name;
    private double salary;
    // getters/setters
}
```

**`EmployeeRepository`**
(Handles persistence)

```java
public class EmployeeRepository {
    public void save(Employee e) {
        // ... DB logic ...
    }
}
```

</div>

<div>

**`PayrollService`**
(Handles calculations)

```java
public class PayrollService {
    public double calculatePay(Employee e) {
        // ... payroll logic ...
    }
}
```

**`ReportGenerator`**
(Handles reporting)

```java
public class ReportGenerator {
    public String generate(Employee e) {
        // ... reporting logic ...
    }
}
```

</div>
</div>

Now, each class has only **one** reason to change.

---

# Code Demo: `EmployeeService.java`

Let's look at the `solid-principles` module in our project.

**(Show `solid-principles/src/main/java/EmployeeService.java`)**

This file demonstrates the separation of concerns.

- **`Employee`:** A simple record that just holds employee data. Its only responsibility is to represent an employee.
- **`EmployeeRepository`:** Its only responsibility is to save and find `Employee` objects in a data store.
- **`PayrollService`:** Its only responsibility is to calculate pay.
- **`EmployeeService`:** This class acts as a coordinator or facade, using the other classes to perform a higher-level operation. It doesn't have the low-level logic itself.

```java
// From EmployeeService.java
public class EmployeeService {
    private final EmployeeRepository repository = new EmployeeRepository();
    private final PayrollService payrollService = new PayrollService();

    public void hireEmployee(String name, double salary) {
        Employee employee = new Employee(name, salary);
        repository.save(employee);
    }
}
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- The Single Responsibility Principle is a fundamental concept for writing clean, maintainable code.
- A class should have only one reason to change.
- Following SRP leads to smaller, more focused classes that are easier to understand, test, and maintain.
- It reduces coupling and makes your system more resilient to change.

</v-clicks>
