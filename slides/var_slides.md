
---
layout: cover
--- 

# Local Variable Type Inference: `var`

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 10: Use the "var" reserved type name for local variable type inference.
  </span>
</div>

---
layout: section
---

# What is `var`?

<v-clicks>

- Introduced in Java 10, `var` allows you to declare local variables without explicitly specifying their type.
- The compiler **infers** the type from the right-hand side of the assignment.
- It is **not** a new keyword, but a *reserved type name*. This means you can still have a variable named `var`.
- It is **not** dynamic typing. The type is still statically checked at compile time.

</v-clicks>

<div class="mt-8">
<v-click>

**The Goal:** Reduce boilerplate code and improve readability, especially with complex generic types.

</v-click>
</div>

---

# How to Use `var`

<div class="grid grid-cols-2 gap-8">

<div>

## **Before `var`**

```java
String message = "Hello, Java!";

List<Map<String, Integer>> list = new ArrayList<>();

// Verbose and repetitive
```

</div>

<div>

## **With `var`**

```java
var message = "Hello, Java!"; // Inferred as String

var list = new ArrayList<Map<String, Integer>>(); // Inferred as ArrayList

// Concise and readable
```

</div>

</div>

---

# Where You CAN Use `var`

<v-clicks>

- **Local variables inside methods**

```java
public void myMethod() {
    var name = "Ken Kousen";
}
```

- **Enhanced `for` loops**

```java
var names = List.of("a", "b", "c");
for (var name : names) {
    // ...
}
```

- **`try-with-resources` statements**

```java
try (var reader = new BufferedReader(...)) {
    // ...
}
```

</v-clicks>

---

# Where You CANNOT Use `var`

<v-clicks>

- **Fields (instance variables)**
- **Method parameters**
- **Method return types**
- **`catch` clauses**
- **With `null` initialization** (compiler needs a type!)
- **With lambda expressions** (compiler needs a target type!)

</v-clicks>

---

# Code Demo: `ModernJavaFeatures.java`

Let's see `var` in action.

**(Show `oop-core/src/main/java/ModernJavaFeatures.java`)**

```java
// Simple types
var employeeName = "Alice Johnson";  // String
var employeeId = 12345;             // int
var salary = 75000.50;              // double

// Complex types
var employees = List.of(
    new EmployeeRecord("Alice", 1, 75000),
    new EmployeeRecord("Bob", 2, 80000)
);

// In a for loop
for (var employee : employees) {
    System.out.println(employee.name());
}
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- `var` is for local variable type inference, making code more concise.
- The type is determined at **compile time**, not runtime.
- Use it to improve readability, especially with nested or complex generic types.
- Don't overuse it. If the type isn't clear from the context, it's better to be explicit.

</v-clicks>
