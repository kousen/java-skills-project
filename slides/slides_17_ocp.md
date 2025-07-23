---
layout: cover
--- 

# The Open/Closed Principle (OCP)

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 17: Implement the Open/Closed Principle by designing classes open for extension but closed for modification.
  </span>
</div>

---
layout: section
---

# The Open/Closed Principle

<v-clicks>

- The **O** in the **SOLID** acronym.
- First described by Bertrand Meyer.
- It states that software entities (classes, modules, functions, etc.) should be **open for extension, but closed for modification**.
- This means you should be able to add new functionality to a class without changing its existing source code.

</v-clicks>

<div class="mt-8">
<v-click>

**Key Idea:** Use abstraction (interfaces and abstract classes) to allow new functionality to be plugged in.

</v-click>
</div>

---

# Why is OCP Important?

When you modify existing, working code, you risk:

<v-clicks>

- **Introducing bugs:** You might break something that was previously working correctly.
- **Increased testing effort:** The entire class, including all existing functionality, needs to be re-tested.
- **Cascading changes:** Changes in one class might force changes in other classes that depend on it.

</v-clicks>

By extending a class with new behavior instead of modifying it, you minimize these risks.

---

# Example: A Class That Violates OCP

Imagine a `ShapeAreaCalculator` that calculates the area of different shapes.

```java
// VIOLATION of OCP
public class ShapeAreaCalculator {
    public double calculateArea(Object shape) {
        double area = 0;
        if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            area = r.getWidth() * r.getHeight();
        } else if (shape instanceof Circle) {
            Circle c = (Circle) shape;
            area = Math.PI * c.getRadius() * c.getRadius();
        }
        // What happens when we need to add a Triangle?
        // We have to MODIFY this method!
        return area;
    }
}
```
This class is **not closed for modification**. Every time you add a new shape, you have to come back and add another `if-else` block.

---

# Refactoring to Follow OCP

We can refactor this using an interface. This moves the responsibility for calculating the area to the shapes themselves.

**1. Create a `Shape` Interface:**

```java
public interface Shape {
    double getArea();
}
```

**2. Implement the Interface in Concrete Classes:**

```java
public class Rectangle implements Shape {
    // ... fields and constructor ...
    @Override public double getArea() { return width * height; }
}

public class Circle implements Shape {
    // ... fields and constructor ...
    @Override public double getArea() { return Math.PI * radius * radius; }
}
```

---

# The OCP-Compliant Calculator

Now the calculator is simple, elegant, and closed for modification.

```java
// OCP-Compliant
public class AreaCalculator {
    public double calculateArea(Shape shape) {
        return shape.getArea();
    }
}
```

To add a new `Triangle` shape, we simply create a `Triangle` class that implements the `Shape` interface. We **do not need to change the `AreaCalculator` at all**. It is **open for extension** (with new shapes) but **closed for modification**.

---

# Code Demo: `OpenClosedPrinciple.java`

Our project demonstrates this with a notification system.

**(Show `solid-principles/src/main/java/OpenClosedPrinciple.java`)**

- **The Violation:** The `BadNotificationService` has a method with `if-else` checks for `"email"`, `"sms"`, etc. To add a new notification type like `"push"`, you have to modify this class.

- **The Solution:**
    - A `NotificationProvider` interface is created with a `send()` method.
    - `EmailProvider`, `SmsProvider`, and `PushProvider` classes implement this interface.
    - The new `GoodNotificationService` simply takes a `NotificationProvider` and calls `send()` on it, without knowing the concrete type.

---
layout: section
---

# Key Takeaways

<v-clicks>

- The Open/Closed Principle is a crucial guideline for creating robust and maintainable systems.
- **Open for extension:** You can add new behaviors.
- **Closed for modification:** You don't change existing, tested code.
- Achieve this through abstractions like interfaces and abstract classes.
- This pattern is the heart of creating pluggable, modular software.

</v-clicks>
