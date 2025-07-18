
---
layout: cover
--- 

# Refactoring Legacy Code

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 18: Refactor legacy code to improve performance or reduce duplication and technical debt.
  </span>
</div>

---
layout: section
---

# What is Refactoring?

<v-clicks>

- A disciplined technique for restructuring existing computer code—changing the factoring—without changing its external behavior.
- The goal is to improve non-functional attributes of the software.
- **Improves:** Readability, maintainability, extensibility, performance.
- **Reduces:** Complexity, duplication, technical debt.

</v-clicks>

<div class="mt-8">
<v-click>

**Key Idea:** Make a series of small, behavior-preserving changes. Each change should make the code "a little bit better".

</v-click>
</div>

---

# When to Refactor

<v-clicks>

- **The Rule of Three:** The first time you do something, you just do it. The second time, you cringe but do the same thing. The third time you do something similar, you refactor.
- **Before adding a new feature:** Clean up the existing code to make it easier to add the new functionality.
- **During a code review:** Identify "code smells"—symptoms of deeper problems in the code.
- **When fixing a bug:** The bug may be a symptom of a design flaw that refactoring can address.

</v-clicks>

---

# Code Smells: Signs You Need to Refactor

- **God Class:** A class that does too much (violates SRP).
- **Duplicated Code:** The same code block appears in multiple places (violates DRY - Don't Repeat Yourself).
- **Long Method:** A method that is too long and tries to do too many things.
- **Complex Conditional Logic:** Deeply nested `if-else` statements.
- **Tight Coupling:** Changes in one class force changes in many other classes.
- **Primitive Obsession:** Using primitive data types to represent complex concepts (e.g., using a `String` for a phone number).

---

# Code Demo: `LegacyOrderProcessor` (The "Before")

Let's look at a legacy class with several code smells.

**(Show `refactoring/src/main/java/LegacyCode.java` - the `LegacyOrderProcessor` class)**

```java
class LegacyOrderProcessor {
    // ...
    public void processOrders() {
        // Three separate loops to process orders!
        for (Order order : orders) { /* ... */ }
        for (Order order : orders) { /* ... */ }
        for (Order order : orders) { /* ... */ }
    }

    public void cancelOrder(int id) {
        // Duplicated logic to find an order
        Order orderToCancel = null;
        for (Order order : orders) { /* ... */ }
        // ...
    }
}
```

**Problems:**
1.  **Violates SRP:** It processes, notifies, and manages inventory.
2.  **Inefficient:** `processOrders` loops through the entire list three times.
3.  **Duplicated Logic:** The code to find an order by its ID is written twice.

---

# Refactoring Strategy

1.  **Identify Responsibilities:** The clear responsibilities are Order Persistence, Notifications, and Inventory Management.
2.  **Extract Classes:** Create a new class for each responsibility (`OrderRepository`, `NotificationService`, `InventoryService`).
3.  **Simplify the Data Class:** Turn the `Order` class into a simple data carrier, a `record` is perfect for this.
4.  **Create a Coordinator:** Create a new `RefactoredOrderProcessor` class that acts as a facade, delegating work to the specialized service classes.
5.  **Improve Efficiency:** Process each order completely in a single pass instead of using multiple loops.

---

# Code Demo: The "After" State

The refactored code is much cleaner and more modular.

**(Show `refactoring/src/main/java/LegacyCode.java` - the refactored classes)**

```java
// Each class has one job
class OrderRepository { ... }
class NotificationService { ... }
class InventoryService { ... }

// The coordinator class is clean and readable
class RefactoredOrderProcessor {
    private final OrderRepository repository;
    // ...

    public void processOrder(Order order) {
        if (order.status().equals("NEW")) {
            // One logical flow per order
            Order processedOrder = new Order(order.id(), ..., "PROCESSED");
            repository.save(processedOrder);
            notificationService.sendConfirmation(processedOrder);
            inventoryService.updateInventory(processedOrder);
        }
    }
}
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- Refactoring is the process of improving your code's internal structure without changing its external behavior.
- Look for "code smells" like duplicated code and classes with too many responsibilities.
- Apply principles like SRP and DRY (Don't Repeat Yourself) to guide your refactoring.
- The goal is to make the code easier to understand, maintain, and extend in the future.

</v-clicks>
