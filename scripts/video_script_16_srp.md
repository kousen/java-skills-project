
# Video Script: The Single Responsibility Principle (SRP)

## 1. Introduction

**Host:** "Hello, and welcome to our series on the SOLID principles of object-oriented design. In this video, we're starting with the 'S', which stands for the Single Responsibility Principle, or SRP."

**Host:** "The Single Responsibility Principle was defined by Robert C. Martin, also known as Uncle Bob. The principle states that a class should have one, and only one, reason to change. This is one of the most fundamental concepts in writing clean, maintainable code. It doesn't mean your class can only have one method. It means that all the methods and properties in your class should be tightly focused on a single, primary purpose."

**Host:** "The goal is to create classes that do one thing and do it well. Let's explore why this is so important."

---

## 2. The Problem with Multiple Responsibilities

**Host:** "What happens when a class does too much? Imagine an `Employee` class that not only holds employee data, but also saves itself to a database, calculates payroll, and generates HR reports."

**(Show the SRP violation slide)**

**Host:** "This class has at least four different reasons to change. A change to the database schema, a change in the tax calculation logic, or a change in the report format would all require modifying this single class. This makes the class bloated, hard to understand, and very fragile. A change in one area could easily break something completely unrelated."

**Host:** "We call these 'god objects', and they are a nightmare to maintain and test."

---

## 3. Refactoring to Follow SRP

**Host:** "The solution is to break this god object apart. We can refactor the code so that each responsibility is handled by its own, separate class."

**(Show the refactored slide)**

**Host:** "We would have an `Employee` class that only holds data. Then, an `EmployeeRepository` class would be responsible only for database operations. A `PayrollService` would handle all the salary calculations. And a `ReportGenerator` would be in charge of creating the reports."

**Host:** "Now, each class is small, focused, and has only one reason to change. If the database logic needs to be updated, you only have to touch the `EmployeeRepository`. The other classes are completely unaffected. This is the power of SRP."

---

## 4. Code Demo: `EmployeeService.java`

**Host:** "Let's look at the `solid-principles` module in our project, which is designed to follow this principle."

**(Show `solid-principles/src/main/java/EmployeeService.java` and related files)**

**Host:** "Here, we see the separation in action. We have an `Employee` record, which is just a simple data carrier. Its only job is to represent an employee. We have an `EmployeeRepository` whose sole purpose is to manage the persistence of `Employee` objects. And we have a `PayrollService` that only knows how to calculate pay."

**Host:** "Then we have a class like `EmployeeService`. It acts as a coordinator. It doesn't contain the low-level logic for saving or calculating pay. Instead, it uses the other, more specialized classes to accomplish its task. This is a great example of how to structure your code to follow SRP."

---

## 5. Summary

**Host:** "To wrap up, the Single Responsibility Principle is your first line of defense against complex, messy code. By ensuring every class has just one reason to change, you create a system that is more modular, less coupled, and far easier to understand, test, and maintain."

**Host:** "It's a simple idea, but applying it consistently will have a massive positive impact on the quality of your software."

**Host:** "That's it for SRP. Join us next time as we tackle the 'O' in SOLID: the Open/Closed Principle."
