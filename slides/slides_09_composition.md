---
layout: cover
--- 

# Object Composition in Java

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 9: Use object composition to model "has-a" relationships between classes.
  </span>
</div>

---
layout: section
---

# Composition: The "Has-A" Relationship

<v-clicks>

- A fundamental design principle in object-oriented programming.
- It means that a class can **contain** an instance of another class.
- This models a "has-a" relationship. For example, a `Car` **has an** `Engine`.
- It is an alternative to inheritance (the "is-a" relationship).

</v-clicks>

<div class="mt-8">
<v-click>

**Key Idea:** Favor composition over inheritance. It leads to more flexible and maintainable designs.

</v-click>
</div>

---

# Inheritance vs. Composition

<div class="grid grid-cols-2 gap-8">

<div>

## **Inheritance (`is-a`)**
- A `Manager` **is an** `Employee`.
- Creates a tight coupling between classes.
- Can lead to rigid and complex class hierarchies.

```java
// Inheritance
public class Manager extends Employee {
    // ...
}
```

</div>

<div>

## **Composition (`has-a`)**
- An `Employee` **has an** `Address`.
- Promotes loose coupling and flexibility.
- Classes are smaller and more focused.

```java
// Composition
public class Employee {
    private Address address; // has-a
    // ...
}
```

</div>

</div>

---

# Code Demo: `Employee` and `Address`

Let's see how composition works in practice.

**(Show `oop-core/src/main/java/Address.java`)**

```java
public class Address {
    private String street;
    private String city;
    private String state;
    private String zip;

    // constructor, getters, setters...
}
```

**(Show `oop-core/src/main/java/Employee.java`)**

```java
public class Employee {
    private String name;
    private double salary;
    private Address address; // Composition

    public Employee(String name, double salary, Address address) {
        this.name = name;
        this.salary = salary;
        this.address = address;
    }

    // getters...
}
```

---

# Code Demo: `Department` and `Employee`

Composition can also model one-to-many relationships.

**(Show `oop-core/src/main/java/Department.java`)**

```java
import java.util.List;
import java.util.ArrayList;

public class Department {
    private String name;
    private List<Employee> employees = new ArrayList<>(); // Composition

    public Department(String name) {
        this.name = name;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- Composition is a powerful way to build complex objects by combining simpler ones.
- It models the "has-a" relationship (e.g., a `Department` has `Employee`s).
- It leads to more flexible, reusable, and maintainable code compared to inheritance.
- **Favor composition over inheritance** is a widely accepted design principle.

</v-clicks>