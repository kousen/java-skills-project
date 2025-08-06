
# Video Script: Access Modifiers in Java

## 1. Introduction

**Host:** "Hi, and welcome back to our Java skills series. In this video, we're going to explore a fundamental concept in object-oriented programming: access modifiers."

**Host:** "Access modifiers are keywords in Java that set the visibility of your classes, methods, and variables. They are the heart of encapsulation, a core principle of OOP that bundles the data (fields) and methods that operate on the data into a single unit, or class. Access modifiers help you control who can access this data, and that's a critical part of building robust and maintainable applications."

**Host:** "The main idea is to give your classes and their members the most restrictive access level possible. Let's see how that works."

---

## 2. The Four Access Modifiers

**Host:** "Java has four access modifiers, each with a different level of visibility."

**Host:** "First, we have **`public`**. When a class, method, or variable is marked as `public`, it's accessible from anywhere in your application. This is the most permissive access level, so you should use it for your main APIs—the parts of your code that you intend for other classes to use directly."

**Host:** "Next is **`protected`**. A `protected` member is accessible within its own package and by subclasses, even if those subclasses are in a different package. This is useful when you want to expose certain functionality to child classes for extension."

**Host:** "Then there's the default, or **package-private**, access level. If you don't specify any modifier, the member is only accessible within its own package. This is great for helper classes or methods that are part of an implementation but shouldn't be exposed to the outside world."

**Host:** "Finally, we have **`private`**. This is the most restrictive access level. A `private` member is only accessible from within the same class. This is the cornerstone of encapsulation. You should always make your fields private and provide public methods (getters and setters) to access and modify them. This allows you to control and validate the data."

---

## 3. Code Examples

**Host:** "Let's look at some code to make this clearer. We'll start with a simple `Employee` class."

**(Show `oop-core/src/main/java/Employee.java` on screen)**

**Host:** "In this `Employee` class, the `name` and `salary` fields are `private`. This means they can't be accessed directly from outside the `Employee` class. To read the name, we provide a `public` getter method, `getName`. To change the salary, we have a `public` setter, `setSalary`, which includes validation to ensure the new salary is positive. This is a perfect example of encapsulation."

**Host:** "Now let's see how these modifiers work between different classes and packages. We have a few classes set up in the `com.oreilly.javaskills.oop.access` and `com.oreilly.javaskills.oop.anotherpackage` packages."

**(Show the directory structure)**

**Host:** "Inside the `access` package, we have `BaseClass`. Let's look at its members."

**(Show `oop-core/src/main/java/com/oreilly/javaskills/oop/access/BaseClass.java`)**

```java
package com.oreilly.javaskills.oop.access;

public class BaseClass {
    public String publicVar = "public";
    protected String protectedVar = "protected";
    String packageVar = "package-private";
    private String privateVar = "private";
}
```

**Host:** "Now, let's see what `SamePackageClass` can access."

**(Show `oop-core/src/main/java/com/oreilly/javaskills/oop/access/SamePackageClass.java`)**

```java
package com.oreilly.javaskills.oop.access;

public class SamePackageClass {
    public void accessMembers() {
        BaseClass base = new BaseClass();
        System.out.println(base.publicVar);
        System.out.println(base.protectedVar);
        System.out.println(base.packageVar);
        // System.out.println(base.privateVar); // COMPILE ERROR
    }
}
```

**Host:** "As you can see, it can access `public`, `protected`, and package-private members, but not `private` ones. Now let's go to a different package."

**(Show `oop-core/src/main/java/com/oreilly/javaskills/oop/anotherpackage/DifferentPackageClass.java`)**

```java
package com.oreilly.javaskills.oop.anotherpackage;

import com.oreilly.javaskills.oop.access.BaseClass;

public class DifferentPackageClass {
    public void accessMembers() {
        BaseClass base = new BaseClass();
        System.out.println(base.publicVar);
        // System.out.println(base.protectedVar); // COMPILE ERROR
        // System.out.println(base.packageVar);  // COMPILE ERROR
        // System.out.println(base.privateVar);   // COMPILE ERROR
    }
}
```

**Host:** "From a different package, we can only access the `public` members. But what if we subclass it?"

**(Show `oop-core/src/main/java/com/oreilly/javaskills/oop/anotherpackage/DifferentPackageSubClass.java`)**

```java
package com.oreilly.javaskills.oop.anotherpackage;

import com.oreilly.javaskills.oop.access.BaseClass;

public class DifferentPackageSubClass extends BaseClass {
    public void accessMembers() {
        System.out.println(publicVar);
        System.out.println(protectedVar);
        // System.out.println(packageVar); // COMPILE ERROR
        // System.out.println(privateVar); // COMPILE ERROR
    }
}
```

**Host:** "Because `DifferentPackageSubClass` extends `BaseClass`, it inherits the `public` and `protected` members, so it can access them directly."

---

## 4. Try It Out Exercise

**Host:** "Now let's put this knowledge to work with a practical exercise. Open the `com.oreilly.javaskills.oop.exercise.AccessModifiersExercise.java` file in the oop-core module."

**(Show `oop-core/src/main/java/com.oreilly.javaskills.oop.exercise.AccessModifiersExercise.java` on screen)**

**Host:** "You'll see a `BankAccount` class that needs proper encapsulation. Your task is to determine the correct access modifiers for each field and method."

**Host:** "Think about this: the account holder name, balance, and account number should be private—they're internal data that shouldn't be modified directly from outside. But we need public methods like `deposit` and `withdraw` for the class to be useful."

**Host:** "The validation and logging methods are implementation details—they should be private. The `BankingService` class shows different scenarios: public constants, protected methods for subclasses, and package-private utilities."

**Host:** "Try running the exercise and see how proper encapsulation protects your data while still providing a clean, usable interface. This is the foundation of good object-oriented design."

---

## 5. Summary

**Host:** "So, to recap:"
**Host:** "**`public`** is for everyone."
**Host:** "**`protected`** is for the package and subclasses."
**Host:** "**package-private** (no keyword) is for the package only."
**Host:** "and **`private`** is for the class itself."

**Host:** "As a best practice, always start with the most restrictive access level, `private`, and only increase the visibility if you need to. This will make your code more secure, easier to maintain, and less prone to bugs."

**Host:** "That's it for access modifiers. Thanks for watching, and I'll see you in the next video."
