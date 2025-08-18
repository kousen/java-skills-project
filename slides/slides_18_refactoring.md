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

# Refactoring Legacy Code

---

# What is Refactoring?

<v-click>

A disciplined technique for restructuring existing computer code without changing its external behavior.

</v-click>

<v-clicks>

- **Improves:** Readability, maintainability, extensibility, performance
- **Reduces:** Complexity, duplication, technical debt

</v-clicks>

---

# What is Technical Debt?

<div class="text-center mt-16">
<v-click>

## The accumulation of problems that make code harder to work with over time

</v-click>
</div>

<v-clicks>

- Like financial debt - **if ignored, it compounds**
- Makes every change more difficult and risky
- **Refactoring is how we pay down technical debt**

</v-clicks>

---

# The Golden Rule of Refactoring

<div class="text-center mt-16">
<v-click>

## **Never refactor without a good test suite in place**

</v-click>
</div>

---

# Why Tests Are Critical

<v-clicks>

- **Tests give you confidence** that refactoring preserves behavior
- **Tests enable fearless refactoring** - make changes without breaking functionality  
- **Tests act as a safety net** - catch regressions immediately
- **Without tests, refactoring is just changing code** - potentially dangerous

</v-clicks>

<div class="text-center mt-8">
<v-click>

### Tests give you the **freedom** to refactor safely

</v-click>
</div>

---

# When to Refactor

<v-clicks>

- **The Rule of Three** - First time you do it, second time you cringe, third time you refactor
- **Before adding a new feature** - Clean up existing code first
- **During a code review** - Identify "code smells"
- **When fixing a bug** - Bug may be symptom of design flaw

</v-clicks>

---

# Code Smells: Warning Signs

<v-clicks>

- **God Class** - A class that does too much (violates SRP)
- **Duplicated Code** - Same code block appears in multiple places (violates DRY)
- **Long Method** - Method that tries to do too many things
- **Complex Conditional Logic** - Deeply nested if-else statements

</v-clicks>

---

# More Code Smells

<v-clicks>

- **Tight Coupling** - Changes in one class force changes in many others
- **Primitive Obsession** - Using primitive types for complex concepts
- **Large Class** - Class with too many fields and methods
- **Feature Envy** - Method uses another class more than its own

</v-clicks>

---

# Code Demo: Test-Driven Refactoring

Files: `solid-principles/src/main/java/LegacyCodeExample.java`  
Tests: `solid-principles/src/test/java/LegacyCodeExampleTest.java`

<v-click>

Our demo **practices what we preach** - comprehensive tests enable safe refactoring.

</v-click>

---

# Legacy Code: The God Class

```java
class LegacyOrderProcessor {
    public void processOrders() {
        // First loop: Process orders
        for (Order order : orders) { /* ... */ }
        
        // Second loop: Send notifications  
        for (Order order : orders) { /* ... */ }
        
        // Third loop: Update inventory
        for (Order order : orders) { /* ... */ }
    }
}
```

<v-clicks>

- **Violates SRP** - Processing, notifications, AND inventory
- **Inefficient** - Three separate loops over same data
- **Hard to test** - Everything coupled together

</v-clicks>

---

# Legacy Code: Duplicated Logic

```java
public Order findOrder(int orderId) {
    Order foundOrder = null;
    for (Order order : orders) {
        if (order.id() == orderId) {
            foundOrder = order;
            break;
        }
    }
    return foundOrder;
}

public void cancelOrder(int orderId) {
    Order orderToCancel = null;
    for (Order order : orders) {  // DUPLICATION!
        if (order.id() == orderId) {
            orderToCancel = order;
            break;
        }
    }
    // ...
}
```

---

# Problems with Legacy Code

<v-clicks>

- **Multiple responsibilities** in one class
- **Inefficient algorithms** - unnecessary loops
- **Duplicated logic** - order finding repeated
- **Hard to extend** - tightly coupled components
- **Difficult to test** - can't mock dependencies

</v-clicks>

---

# Refactoring Strategy

<v-clicks>

- **Identify responsibilities** - Order persistence, notifications, inventory
- **Extract classes** - Create focused service classes
- **Simplify data** - Use records for simple data carriers
- **Create coordinator** - Facade that delegates to services
- **Improve efficiency** - Single pass processing

</v-clicks>

---

# After: Order Repository

```java
class RefactorOrderRepository {
    private List<RefactorOrder> orders = new ArrayList<>();
    
    public void save(RefactorOrder order) {
        orders.removeIf(existing -> existing.id() == order.id());
        orders.add(order);
    }
    
    public RefactorOrder findById(int orderId) {
        return orders.stream()
                .filter(order -> order.id() == orderId)
                .findFirst()
                .orElse(null);
    }
    
    public List<RefactorOrder> findByStatus(String status) {
        return orders.stream()
                .filter(order -> status.equals(order.status()))
                .toList();
    }
}
```

---

# After: Notification Service

```java
class RefactorNotificationService {
    
    public void sendOrderConfirmation(RefactorOrder order) {
        System.out.printf("""
                📧 Sending confirmation email:
                   Customer: %s
                   Order: %d
                   Amount: $%.2f
                   Status: %s%n""", 
                order.customerId(), order.id(), 
                order.amount(), order.status());
    }
    
    public void sendCancellationNotice(RefactorOrder order) {
        // Send cancellation email...
    }
}
```

---

# After: Inventory Service

```java
class RefactorInventoryService {
    
    public void updateInventory(RefactorOrder order) {
        System.out.printf("""
                📦 Updating inventory:
                   Order: %d
                   Amount: $%.2f
                   Inventory adjusted%n""",
                order.id(), order.amount());
    }
    
    public void restoreInventory(RefactorOrder order) {
        // Restore items to stock...
    }
}
```

---

# After: Clean Coordinator

```java
public class LegacyCodeExample {
    private final RefactorOrderRepository repository;
    private final RefactorNotificationService notificationService;
    private final RefactorInventoryService inventoryService;
    
    // Dependency injection constructor
    public LegacyCodeExample(RefactorOrderRepository repository,
                           RefactorNotificationService notificationService,
                           RefactorInventoryService inventoryService) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.inventoryService = inventoryService;
    }
}
```

---

# After: Efficient Processing

```java
public void processOrder(RefactorOrder order) {
    if (!"NEW".equals(order.status())) {
        return;
    }
    
    var processedOrder = new RefactorOrder(
        order.id(), order.customerId(), order.amount(), 
        "PROCESSED", order.orderDate()
    );
    
    // Single logical flow - all operations for this order
    repository.save(processedOrder);
    notificationService.sendOrderConfirmation(processedOrder);
    inventoryService.updateInventory(processedOrder);
}
```

<v-click>

**One pass instead of three loops!**

</v-click>

---

# Benefits of Refactored Code

<v-clicks>

- **Single Responsibility** - Each class has one job
- **DRY Principle** - No duplicated order-finding logic  
- **Performance** - Single pass vs three loops
- **Testability** - Dependencies can be mocked
- **Maintainability** - Easy to understand and modify
- **Extensibility** - Easy to add new services

</v-clicks>

<div class="text-center mt-8">
<v-click>

### **All verified by our comprehensive test suite!**

</v-click>
</div>

---

# Try It Out Exercise: Test-Driven Refactoring

File: `solid-principles/src/main/java/RefactoringExercise.java`  
Tests: `solid-principles/src/test/java/RefactoringExerciseTest.java`

<v-click>

Practice **safe refactoring** with a comprehensive test suite as your safety net.

</v-click>

---

# Exercise: Library Management System

<v-clicks>

- **Legacy class** - `RefactoringExercise` with multiple code smells
- **God class** handling books, members, checkouts, and fines
- **Comprehensive tests** that verify behavior (not implementation)
- **Your mission** - Refactor while keeping all tests green

</v-clicks>

---

# The Safety Net: Behavior-Based Tests

<v-clicks>

- **98 test cases** covering all functionality
- **Tests verify BEHAVIOR**, not implementation details
- **Should stay GREEN** throughout refactoring
- **If tests fail** - you've changed behavior (bad!)
- **Use tests to refactor fearlessly**

</v-clicks>

<div class="text-center mt-8">
<v-click>

### Demonstrates the **golden rule** in action

</v-click>
</div>

---

# Refactoring Steps (Guided TODOs)

<v-clicks>

- **Step 1:** Extract `BookRepository` class
- **Step 2:** Extract `MemberRepository` class  
- **Step 3:** Extract `CheckoutService` class
- **Step 4:** Extract `FineCalculator` class
- **Step 5:** Create `LibrarySystem` coordinator
- **Step 6:** Run tests after each step!

</v-clicks>

---

# Modern Refactoring: Embracing Language Evolution

File: `solid-principles/src/main/java/ProcessWords.java`  
Test: `solid-principles/src/test/java/ProcessWordsTest.java`

<v-click>

Sometimes refactoring means **adopting newer language features** for better expressiveness.

</v-click>

---

# From Imperative to Functional: Java 7 Style

```java
public List<String> getSortedEvenLengthUpperCaseWords(String sentence) {
    String[] words = sentence.split("\\s+");

    List<String> evenLengthWords = new ArrayList<>();
    for (String word : words) {
        if (word.length() % 2 == 0) {
            evenLengthWords.add(word.toUpperCase());
        }
    }
    evenLengthWords.sort(new Comparator<>() {
        @Override
        public int compare(String word1, String word2) {
            return word1.length() - word2.length();
        }
    });
    return evenLengthWords;
}
```

<v-clicks>

- **Imperative style** - tells the computer HOW to do it
- **Verbose boilerplate** - anonymous inner class for sorting
- **Multiple steps** - filter, transform, sort separately

</v-clicks>

---

# Modern Java 8+ Style (Refactored)

```java
public List<String> getSortedEvenLengthUpperCaseWords(String sentence) {
    return Arrays.stream(sentence.split("\\s+"))
        .filter(word -> word.length() % 2 == 0)
        .map(String::toUpperCase)  
        .sorted(Comparator.comparing(String::length))
        .collect(Collectors.toList());
}
```

<v-clicks>

- **Declarative style** - describes WHAT we want
- **Functional pipeline** - clear data transformation flow
- **Modern idioms** - method references, lambda expressions
- **Single expression** - all operations chained together

</v-clicks>

---

# Benefits of Stream Refactoring

<v-clicks>

- **More readable** - pipeline shows intent clearly
- **Less error-prone** - no manual loop management
- **Better performance** - potential for parallel processing
- **Maintainable** - easier to modify individual steps
- **Modern** - uses current Java idioms

</v-clicks>

<div class="text-center mt-8">
<v-click>

### **Same behavior, better expression**

</v-click>
</div>

---

# IDE-Assisted Modernization

<v-clicks>

- **IntelliJ/Eclipse** can suggest stream conversions automatically
- **"Replace with forEach"** inspections
- **"Convert to method reference"** suggestions
- **Tool-assisted evolution** of your codebase

</v-clicks>

<div class="text-center mt-8">
<v-click>

## Let your IDE help modernize your code!

</v-click>
</div>

---

# A Word of Wisdom: Best Practices Expire

<v-clicks>

- **Today's best practice** can become **tomorrow's maintenance problem**
- **Technology evolves** - what was optimal 5 years ago may not be today
- **Context matters** - best practices depend on team size, project scope
- **Question established patterns** - especially when they feel complex

</v-clicks>

---

# Examples of Evolving Practices

<v-clicks>

- **XML configuration** → **Annotations** → **Code-based config**
- **Heavyweight frameworks** → **Lightweight alternatives** → **Microframeworks**
- **Monolithic architectures** → **Microservices** → **Serverless functions**

</v-clicks>

<div class="text-center mt-12">
<v-click>

## Be ready to refactor your "best practices" when they stop serving you

</v-click>
</div>

---
layout: section
---

# Key Takeaways

---

# Remember These Points

<v-clicks>

- **Never refactor without tests** - they give you confidence and freedom
- **Refactoring reduces technical debt** - accumulation of code problems
- **Look for code smells** - God classes, duplication, complex conditionals  
- **Apply SOLID principles** - SRP, DRY guide effective refactoring
- **Embrace language evolution** - refactor to use modern features (streams, lambdas)
- **Best practices expire** - be ready to refactor them when they stop serving you

</v-clicks>

<div class="text-center mt-12">
<v-click>

## Questions about refactoring legacy code?

</v-click>
</div>