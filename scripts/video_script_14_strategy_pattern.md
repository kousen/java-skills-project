
# Video Script: The Strategy Design Pattern

## 1. Introduction

**Host:** "Hello, and welcome to our lesson on another fundamental design pattern: the Strategy pattern."

**Host:** "The Strategy pattern is a behavioral pattern. It’s all about defining a family of algorithms, encapsulating each one, and making them interchangeable. This means you can change the algorithm an object uses at runtime, without changing the object itself. It’s a classic pattern, described by the 'Gang of Four' in their original design patterns book, and it’s used everywhere in software design."

**Host:** "The core idea is to take a set of related algorithms and put them behind a common interface, so the client code can work with any of them."

---

## 2. The Components of the Strategy Pattern

**Host:** "The Strategy pattern has three main parts."

**Host:** "First, you have the **Strategy interface**. This interface defines a single method that all your different algorithms will implement. It's the contract that all strategies must follow."

**Host:** "Second, you have the **Concrete Strategy classes**. These are the individual classes that implement the Strategy interface. Each one provides a different version of the algorithm. For example, you might have one strategy for sorting quickly, and another for sorting with low memory usage."

**Host:** "Third, you have the **Context**. This is the class that needs to use one of the algorithms. The Context holds a reference to a Strategy object, but it doesn't know which specific strategy it is. It just calls the method on the interface, and the strategy object does the real work."

---

## 3. Code Demo: A Payroll System

**Host:** "Let's make this concrete with a payroll system example from our project. We need to calculate the pay for different types of employees: some are paid hourly, some are salaried, and some work on commission. This is a perfect use case for the Strategy pattern."

**(Show `design-patterns/src/main/java/SalaryCalculator.java` on screen)**

**Host:** "First, we define our `SalaryCalculationStrategy` interface. It has one method, `calculatePay`."

```java
public interface SalaryCalculationStrategy {
    double calculatePay(StrategyEmployee employee, int hoursWorked);
}
```

**Host:** "Next, we create our concrete strategies. We have `HourlyRateStrategy`, `SalariedRateStrategy`, and `CommissionRateStrategy`. Each one implements the `calculatePay` method with its own specific logic."

---

## 4. The Context Class

**Host:** "Now for our Context. We have a class called `PayrollProcessor`. It has a field to hold a `SalaryCalculationStrategy` object."

```java
public class PayrollProcessor {
    private SalaryCalculationStrategy calculationStrategy;

    public void setCalculationStrategy(SalaryCalculationStrategy strategy) {
        this.calculationStrategy = strategy;
    }

    public double processPayroll(StrategyEmployee employee, int hoursWorked) {
        // ...
        return calculationStrategy.calculatePay(employee, hoursWorked);
    }
}
```

**Host:** "The `PayrollProcessor` doesn't know or care about the details of the calculation. It just delegates the job to whatever strategy object it has been given. This is incredibly flexible."

---

## 5. Switching Strategies at Runtime

**Host:** "The real magic happens when we put it all together. We can create a `PayrollProcessor` and then change its behavior on the fly just by giving it a different strategy."

```java
PayrollProcessor processor = new PayrollProcessor();

// Calculate pay using the hourly strategy
processor.setCalculationStrategy(new HourlyRateStrategy(25.0));
processor.processPayroll(employee, 40);

// Now, switch to a salaried strategy
processor.setCalculationStrategy(new SalariedRateStrategy(80000));
processor.processPayroll(employee, 40);
```

**Host:** "We can use the same processor object to calculate the pay for different employee types just by swapping the strategy. This makes our `PayrollProcessor` much more flexible and reusable."

---

## 6. Summary

**Host:** "So, to recap, the Strategy pattern is a powerful tool for managing different algorithms. It encapsulates each algorithm in its own class and lets you swap them at runtime. This helps you follow the Open/Closed Principle—your context class is closed for modification but open for extension with new strategies."

**Host:** "It leads to cleaner, more maintainable, and highly flexible code. It's a fundamental pattern that every Java developer should know."

**Host:** "Thanks for watching, and I'll see you in the next video."
