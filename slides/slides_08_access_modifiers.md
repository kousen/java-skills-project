
---
layout: cover
--- 

# Java Access Modifiers

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 8: Apply access modifiers to control visibility.
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
layout: section
---

# What Are Access Modifiers?

<v-clicks>

- Keywords that set the **visibility** (accessibility) of classes, methods, and variables.
- They are the core of **encapsulation** in Java.
- They answer the question: "Who can use this?"

</v-clicks>

<div class="mt-8">
<v-click>

**The Goal:** Give classes and members the most restrictive access level possible.

</v-click>
</div>

---

# The Four Access Modifiers

<div class="grid grid-cols-2 gap-8">

<div>

## **`public`**
- **Visibility**: Everywhere
- **Use Case**: For APIs and methods intended for general use.

## **`protected`**
- **Visibility**: Same package + subclasses anywhere
- **Use Case**: For members that subclasses may need to use or override.

</div>

<div>

## **`private`**
- **Visibility**: Only within the same class
- **Use Case**: The default choice for fields and helper methods. The cornerstone of encapsulation.

## **Package-Private (default)**
- **Visibility**: Only within the same package
- **Use Case**: For helper classes and members that should not be part of the public API.

</div>

</div>

---

# Visibility Summary (Most to Least Visible)

| Modifier | Same Class | Same Package | Subclass (Diff Pkg) | World |
| :--- | :---: | :---: | :---: | :---: |
| `public` | ✅ | ✅ | ✅ | ✅ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| package-private | ✅ | ✅ | ❌ | ❌ |
| `private` | ✅ | ❌ | ❌ | ❌ |

---

# Code Demo: `Employee.java`

Let's look at a real-world example of encapsulation.

```java
public class Employee {
    // Fields are private to protect the object's state
    private String name;
    private double salary;

    // Public methods provide controlled access
    public String getName() {
        return this.name;
    }

    public void setSalary(double salary) {
        // The method can enforce rules (validation)
        if (salary > 0) {
            this.salary = salary;
        }
    }

    // This method is an internal implementation detail
    private double calculateBonus() {
        // ... complex logic ...
    }
}
```

---

# Try It Out: Bank Account Exercise

Time to practice proper encapsulation with `com.oreilly.javaskills.oop.exercise.AccessModifiersExercise.java`!

<div class="grid grid-cols-2 gap-8">

<div>

## **BankAccount Class**
```java
class BankAccount {
    // Replace TODO comments with access modifiers
    /* TODO: access modifier */ String accountHolderName;
    /* TODO: access modifier */ double balance;
    /* TODO: access modifier */ String accountNumber;
    
    // What about the public API methods?
    /* TODO: access modifier */ void deposit(double amount) { ... }
    /* TODO: access modifier */ boolean withdraw(double amount) { ... }
    
    // Helper methods - internal implementation?
    /* TODO: access modifier */ void validateAmount(double amount) { ... }
    /* TODO: access modifier */ void logTransaction(String type) { ... }
}
```

</div>

<div>

## **Key Questions**
<v-clicks>

- **Fields**: Should account data be directly accessible?
- **Public API**: Which methods need external access?
- **Helpers**: Are validation methods implementation details?
- **Security**: How do we protect sensitive operations?

</v-clicks>

<v-click>

**Practice**: Run the exercise and see encapsulation in action!

</v-click>

</div>

</div>

---

# Key Takeaways

<v-clicks>

- Access modifiers are essential for encapsulation.
- Start with the most restrictive access level (`private`) and increase visibility only when necessary.
- `public`: Open to everyone.
- `protected`: Open to the package and subclasses.
- `package-private` (no keyword): Open to the package.
- `private`: Open only to the class itself.

</v-clicks>
