
# Video Script: The Single Responsibility Principle (SRP)

## 1. Introduction

**Host:** "Hello, and welcome to our series on the SOLID principles of object-oriented design. In this video, we're starting with the 'S', which stands for the Single Responsibility Principle, or SRP."

**Host:** "Just to give you context, SOLID is an acronym for five key design principles: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion. Today we're focusing on that first one - Single Responsibility - which is foundational to all the others."

**Host:** "The Single Responsibility Principle states that a class should have one clear responsibility. This is one of the most fundamental concepts in writing clean, maintainable code. It doesn't mean your class can only have one method. It means that all the methods and properties in your class should be tightly focused on a single, primary purpose."

**Host:** "The goal is to create classes that do one thing and do it well. Let's explore why this is so important."

---

## 2. The Problem with Multiple Responsibilities

**Host:** "What happens when a class does too much? Imagine an `Employee` class that not only holds employee data, but also saves itself to a database, calculates payroll, and generates HR reports."

**(Show the SRP violation slide)**

**Host:** "This class has at least four different responsibilities. A change to the database schema, a change in the tax calculation logic, or a change in the report format would all require modifying this single class. This makes the class bloated, hard to understand, and very fragile. A change in one area could easily break something completely unrelated."

**Host:** "We call these 'god objects', and they are a nightmare to maintain and test."

---

## 3. Refactoring to Follow SRP

**Host:** "The solution is to break this god object apart. We can refactor the code so that each responsibility is handled by its own, separate class. Here's a key insight: you often simplify difficult problems by adding classes. More classes with focused purposes are actually easier to understand than fewer classes doing too much."

**(Show the refactored slide)**

**Host:** "We would have an `Employee` class that only holds data. Then, an `EmployeeRepository` class would be responsible only for database operations. A `PayrollService` would handle all the salary calculations. And a `ReportGenerator` would be in charge of creating the reports."

**Host:** "Now, each class is small, focused, and has only one clear responsibility. If the database logic needs to be updated, you only have to touch the `EmployeeRepository`. The other classes are completely unaffected. This is the power of SRP."

---

## 4. Code Demo: `SRPEmployeeService.java`

**Host:** "Let's look at the `solid-principles` module in our project, which is designed to follow this principle."

**(Show `solid-principles/src/main/java/SRPEmployeeService.java`)**

**Host:** "Here, we see the separation in action. We have an `SRPEmployee` class, which is just a simple data carrier. Its only job is to represent an employee. We have separate interfaces for different responsibilities: `EmployeeRepository` for persistence, `SalaryCalculator` for salary computations, and `NotificationService` for sending emails."

**Host:** "Then we have the `SRPEmployeeService` class. It acts as a coordinator that uses dependency injection to collaborate with these specialized services. It doesn't contain the low-level logic for database operations, salary calculations, or sending emails. Instead, it orchestrates these separate concerns. This is a great example of how to structure your code to follow SRP with modern dependency injection patterns."

---

## 5. Try It Out Exercise

**Host:** "Now it's your turn to practice applying the Single Responsibility Principle. In the same solid-principles module, you'll find the SRPOrderProcessorExercise file. This gives you hands-on experience with a realistic scenario: refactoring an e-commerce order processing system."

**Host:** "The exercise starts with a monolithic order processor that handles everything - validation, payment processing, inventory management, notifications, and shipping calculations all in one place. Sound familiar? Your job is to break it apart using SRP, just like we did with the employee example."

**Host:** "What makes this exercise valuable is that it uses a realistic business scenario you might encounter in the real world. You'll create separate classes for OrderValidator, PaymentProcessor, InventoryService, OrderNotificationService, and ShippingCalculator. Each class gets one clear responsibility, and you'll see how much simpler the code becomes."

**Host:** "The exercise includes TODO comments that guide you through implementing each service, so you can focus on applying SRP principles rather than figuring out business logic. Give it a try - it really reinforces how adding focused classes actually simplifies complex problems."

---

## 6. Summary

**Host:** "To wrap up, the Single Responsibility Principle is your first line of defense against complex, messy code. By ensuring every class has one clear responsibility, you create a system that is more modular, less coupled, and far easier to understand, test, and maintain."

**Host:** "It's a simple idea, but applying it consistently will have a massive positive impact on the quality of your software."

**Host:** "That's it for SRP. Join us next time as we tackle the 'O' in SOLID: the Open/Closed Principle."
