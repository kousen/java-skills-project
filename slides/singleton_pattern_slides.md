
---
layout: cover
--- 

# The Singleton Pattern

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 13: Implement the Singleton pattern to ensure a class has only one instance.
  </span>
</div>

---
layout: section
---

# What is the Singleton Pattern?

<v-clicks>

- A **creational** design pattern.
- Its primary goal is to ensure that a class has **only one instance** and to provide a **global point of access** to it.
- It's useful when exactly one object is needed to coordinate actions across the system.
- Examples: A database connection pool, a logger, or a configuration manager.

</v-clicks>

<div class="mt-8">
<v-click>

**Key Idea:** The class itself is responsible for creating its single instance and ensuring no other instance can be created.

</v-click>
</div>

---

# Core Components of a Singleton

<v-clicks>

1.  **A `private` static instance variable:** This holds the single instance of the class.

2.  **A `private` constructor:** This prevents other classes from instantiating the Singleton using the `new` operator.

3.  **A `public` static `getInstance()` method:** This is the global access point. It creates the instance if it doesn't exist and returns it.

</v-clicks>

---

# Implementation: Eager vs. Lazy

<div class="grid grid-cols-2 gap-8">

<div>

## **Eager Initialization**
- The instance is created when the class is loaded.
- Simple and inherently thread-safe.

```java
public class EagerSingleton {
    private static final EagerSingleton INSTANCE = 
        new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
}
```

</div>

<div>

## **Lazy Initialization**
- The instance is created only when `getInstance()` is first called.
- Saves resources if the instance is not always needed.
- **Must be made thread-safe.**

```java
public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
```

</div>

</div>

---

# Thread-Safe Lazy Initialization

When multiple threads could access `getInstance()` at the same time, you need to handle concurrency.

## The Double-Checked Locking Pattern

```java
public class DatabaseConnection {
    private static volatile DatabaseConnection instance;

    private DatabaseConnection() { ... }

    public static DatabaseConnection getInstance() {
        if (instance == null) {                  // First check
            synchronized (DatabaseConnection.class) {
                if (instance == null) {          // Second check
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
}
```
- The `volatile` keyword ensures visibility of changes to `instance` across threads.
- The `synchronized` block ensures only one thread can create the instance.

---

# Code Demo: `DatabaseConnection.java`

Let's examine a complete, thread-safe Singleton implementation.

**(Show `design-patterns/src/main/java/DatabaseConnection.java`)**

```java
public class DatabaseConnection implements Cloneable {
    // 1. private static volatile instance
    private static volatile DatabaseConnection instance;
    private Connection connection;

    // 2. private constructor
    private DatabaseConnection() {
        // ... connects to a database ...
    }

    // 3. public static getInstance method (with double-checked locking)
    public static DatabaseConnection getInstance() {
        // ... as seen on previous slide ...
    }

    // Also: prevent cloning and deserialization from creating new instances
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton cannot be cloned");
    }
}
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- The Singleton pattern guarantees one and only one instance of a class.
- It involves a `private` constructor and a `static getInstance()` method.
- Be aware of the difference between eager and lazy initialization.
- If you use lazy initialization, you **must** make it thread-safe, often with double-checked locking.
- Consider the downsides: it can be hard to test and can introduce global state.

</v-clicks>
