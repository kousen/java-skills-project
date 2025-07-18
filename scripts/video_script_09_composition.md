
# Video Script: Object Composition in Java

## 1. Introduction

**Host:** "Hello, and welcome to our next lesson in the Java skills series. Today, we're talking about a design principle that is fundamental to object-oriented programming: object composition."

**Host:** "Composition is how you build classes that are made up of other classes. It models what we call a 'has-a' relationship. For instance, you could say that a `Car` 'has-a' `Engine`. The `Car` class, in this case, would contain an instance of the `Engine` class. This is a powerful alternative to inheritance, which models an 'is-a' relationship."

**Host:** "A common piece of advice you'll hear is to 'favor composition over inheritance.' In this video, we'll explore why that is and how to use composition effectively in your own code."

---

## 2. Inheritance vs. Composition

**Host:** "Let's quickly compare inheritance and composition. Inheritance is an 'is-a' relationship. For example, a `Manager` **is an** `Employee`. This creates a tight bond between the `Manager` and `Employee` classes. While useful, inheritance can sometimes lead to rigid and complicated class structures."

**Host:** "Composition, on the other hand, is a 'has-a' relationship. An `Employee` **has an** `Address`. This is a more flexible arrangement. The `Employee` class contains an `Address` object, but the two are not as tightly linked. This promotes what we call 'loose coupling,' making your classes smaller, more focused, and easier to maintain."

---

## 3. Code Example: Employee and Address

**Host:** "Let's look at a practical example. We have an `Employee` class and an `Address` class."

**(Show `oop-core/src/main/java/Address.java` on screen)**

**Host:** "The `Address` class is a simple data carrier with fields for street, city, state, and zip."

**(Show `oop-core/src/main/java/Employee.java` on screen)**

**Host:** "The `Employee` class has a name and a salary, but it also has an `Address`. See how the `address` field is an instance of the `Address` class? That's composition. When we create an `Employee`, we pass in an `Address` object."

```java
// Inside the Employee class
private Address address; // has-a relationship

public Employee(String name, double salary, Address address) {
    this.name = name;
    this.salary = salary;
    this.address = address;
}
```

**Host:** "This is a much cleaner and more flexible design than, say, adding all the address fields directly to the `Employee` class."

---

## 4. Code Example: Department and Employees

**Host:** "Composition is also great for modeling one-to-many relationships. For example, a `Department` can have multiple `Employee`s."

**(Show `oop-core/src/main/java/Department.java` on screen)**

**Host:** "In our `Department` class, we have a `List` of `Employee` objects. This is another example of composition. The `Department` 'has-a' list of employees. The `Department` class has methods to add an employee and to get the list of all employees."

```java
// Inside the Department class
private List<Employee> employees = new ArrayList<>(); // has-a relationship

public void addEmployee(Employee employee) {
    employees.add(employee);
}
```

---

## 5. Summary

**Host:** "To sum up, object composition is a powerful tool in your toolbox."

**Host:** "It lets you build complex objects by combining simpler ones, modeling the 'has-a' relationship. It generally leads to more flexible, reusable, and maintainable code than inheritance. Remember the principle: favor composition over inheritance. It will help you design better, more robust Java applications."

**Host:** "Thanks for watching. Join us next time as we continue to explore the world of Java development."
