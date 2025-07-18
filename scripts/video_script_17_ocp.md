
# Video Script: The Open/Closed Principle (OCP)

## 1. Introduction

**Host:** "Welcome back to our deep dive into the SOLID principles. In this video, we're tackling the 'O': The Open/Closed Principle."

**Host:** "The Open/Closed Principle, or OCP, states that software entities—like classes or modules—should be open for extension, but closed for modification. This sounds a bit like a contradiction, but it's a powerful idea. It means you should be able to add new functionality to your system without changing existing, working code."

**Host:** "The key to achieving this is through abstraction. By programming to interfaces rather than concrete implementations, you can create systems that are flexible and resilient to change."

---

## 2. The Problem with Violating OCP

**Host:** "Let's consider a classic example: a class that calculates the area of different shapes. A naive implementation might have a single method with a long `if-else-if` chain, checking the type of the shape and then calculating its area."

**(Show the OCP violation slide)**

**Host:** "This code works, but it has a major flaw. What happens when your boss asks you to add a `Triangle`? You have to go back into this `calculateArea` method and add another `else if` block. You are modifying existing code. This is a violation of the Open/Closed Principle. Every time a new shape is added, this class has to change. This is risky, because you might accidentally break the logic for rectangles or circles, and it means the class needs to be fully re-tested every time."

---

## 3. Refactoring to Follow OCP

**Host:** "So how do we fix this? We use abstraction. We can create a `Shape` interface that declares a `getArea` method. Then, each of our shape classes—`Rectangle`, `Circle`, and our new `Triangle`—will implement this interface and provide its own specific logic for calculating its area."

**Host:** "Now, our `AreaCalculator` class becomes incredibly simple. Its `calculateArea` method just takes any object that implements the `Shape` interface and calls its `getArea` method."

```java
// OCP-Compliant
public class AreaCalculator {
    public double calculateArea(Shape shape) {
        return shape.getArea();
    }
}
```

**Host:** "This is beautiful. If we need to add a `Pentagon` shape tomorrow, we just create a new `Pentagon` class that implements `Shape`. The `AreaCalculator` doesn't need to be touched. It's **closed** for modification, but it's **open** to be extended with new shapes. That's the principle in action."

---

## 4. Code Demo: `OpenClosedPrinciple.java`

**Host:** "Our project has another great example of this, using a notification system."

**(Show `solid-principles/src/main/java/OpenClosedPrinciple.java` on screen)**

**Host:** "First, we see the 'bad' way. The `BadNotificationService` has a method full of `if-else` statements to handle sending an email, an SMS, or a Slack message. To add a new method, like a push notification, you'd have to change this class."

**Host:** "Then we see the 'good' way. We have a `NotificationProvider` interface with a `send` method. We have concrete classes like `EmailProvider` and `SmsProvider` that implement this interface. Our `GoodNotificationService` doesn't have any `if-else` logic. It just takes a `NotificationProvider` in its constructor and calls the `send` method. To add push notifications, we just create a new `PushProvider` class. The `GoodNotificationService` never needs to change."

---

## 5. Summary

**Host:** "To sum up, the Open/Closed Principle is a vital concept for building flexible and maintainable software. By relying on abstractions, you can create components that are open to being extended with new functionality, without having to modify their existing, stable source code."

**Host:** "This leads to systems that are easier to change, less risky to update, and more modular in their design."

**Host:** "That's it for OCP. Next time, we'll explore the 'L' in SOLID, the Liskov Substitution Principle."
