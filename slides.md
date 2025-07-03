---
theme: seriph
background: https://images.unsplash.com/photo-1555066931-4365d14bab8c?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80

class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Java Skills Training
  
  By Kenneth Kousen
  
  Learn more at [KouseniT](https://kousenit.com)
drawings:
  persist: false
transition: slide-left
title: "Java Skills Training"
mdc: true
slidev:
  slide-number: true
  controls: true
  progress: true
css: unocss
---

<style>
.slidev-page-num {
  display: block !important;
  opacity: 1 !important;
  visibility: visible !important;
  position: fixed !important;
  bottom: 1rem !important;
  right: 1rem !important;
  z-index: 100 !important;
  color: #666 !important;
  font-size: 0.875rem !important;
}
</style>

# Java Skills Training

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Press Space for next page <carbon:arrow-right class="inline"/>
  </span>
</div>

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Kenneth Kousen - O'Reilly Video Series
  </span>
</div>

---

# Java Naming Conventions

## Why Naming Matters

<v-clicks>

- Code is read more often than it's written
- Clear names reduce cognitive load
- Consistent naming improves team productivity
- Well-named code is self-documenting

</v-clicks>

<div class="mt-8">
<v-click>

> "There are only two hard things in Computer Science: cache invalidation and naming things." 
> 
> — Phil Karlton

</v-click>
</div>

---

# Core Naming Principles

<v-clicks>

## 1. **Be Descriptive**
- Use intention-revealing names
- Avoid mental mapping

## 2. **Be Consistent** 
- Follow established conventions
- Use uniform patterns across codebase

## 3. **Be Precise**
- Choose specific over generic
- Avoid misleading names

</v-clicks>

---

# Java Naming Conventions Overview

<div class="grid grid-cols-2 gap-8">

<div>

## **Variables & Methods**
- `camelCase`
- Start with lowercase letter
- Descriptive and meaningful

## **Constants**
- `UPPER_SNAKE_CASE`
- All uppercase with underscores
- Final static fields

</div>

<div>

## **Classes & Interfaces**
- `PascalCase`
- Start with uppercase letter
- Nouns for classes

## **Packages**
- `lowercase.with.dots`
- All lowercase
- Reverse domain naming

</div>

</div>

---

# Variable Naming Examples

<div class="grid grid-cols-2 gap-8">

<div>

## ✅ **Good Examples**

```java
String employeeName = "John Doe";
int employeeId = 12345;
double salaryAmount = 75000.50;
boolean isActive = true;
List<Employee> activeEmployees;
LocalDate startDate;
```

</div>

<div>

## ❌ **Bad Examples**

```java
String name = "John Doe";        // Too generic
String n = "John Doe";           // Too short
String employee_name = "John";   // Wrong case
String EmployeeName = "John";    // Wrong case
String emp = "John Doe";         // Abbreviated
```

</div>

</div>

---

# Method Naming Best Practices

<v-clicks>

## **Use Verbs**
- Methods perform actions
- `calculateSalary()`, `validateInput()`, `processOrder()`

## **Boolean Methods**
- Start with `is`, `has`, `can`, `should`
- `isActive()`, `hasPermission()`, `canEdit()`

## **Accessor Methods**
- `get` prefix for getters: `getName()`
- `set` prefix for setters: `setName(String name)`

</v-clicks>

---

# Constant Naming

<div class="grid grid-cols-2 gap-8">

<div>

## **Correct Format**

```java
public static final String COMPANY_NAME = "ACME Corp";
public static final int MAX_EMPLOYEES = 100;
public static final double DEFAULT_SALARY = 50000.0;
private static final String CONFIG_FILE_PATH = 
    "/etc/app/config.properties";
```

</div>

<div>

## **Why UPPER_SNAKE_CASE?**

<v-clicks>

- **Visibility**: Constants stand out clearly
- **Convention**: Java standard since JDK 1.0
- **Readability**: Underscores separate words
- **Immutability**: Visual cue for final values

</v-clicks>

</div>

</div>

---

# Class and Package Naming

<div class="grid grid-cols-2 gap-8">

<div>

## **Class Names**

```java
// Classes - PascalCase nouns
public class Employee { }
public class PayrollCalculator { }
public class DatabaseConnection { }

// Interfaces - often adjectives
public interface Serializable { }
public interface Comparable<T> { }
public interface PaymentProcessor { }
```

</div>

<div>

## **Package Names**

```java
// All lowercase, reverse domain
package com.oreilly.javaskills.foundations;
package com.oreilly.javaskills.patterns;
package com.oreilly.javaskills.security;

// Avoid
package com.oreilly.JavaSkills;     // Wrong case
package foundations;                // Too generic
```

</div>

</div>

---

# Common Naming Mistakes

<v-clicks>

## **1. Abbreviations & Acronyms**
```java
// Avoid
String empName, calcSal, mgr;
// Prefer  
String employeeName, calculatedSalary, manager;
```

## **2. Meaningless Names**
```java
// Avoid
String data, info, item, obj;
// Prefer
String customerData, userInfo, orderItem, employeeRecord;
```

## **3. Mental Mapping**
```java
// Avoid - requires mental translation
for (int i = 0; i < employees.size(); i++) { }
// Prefer - clear intent
for (Employee employee : employees) { }
```

</v-clicks>

---

# Hungarian Notation - Avoid!

<div class="grid grid-cols-2 gap-8">

<div>

## ❌ **Don't Do This**

```java
String strName;           // Type prefix
int intCount;            // Redundant
boolean bIsActive;       // Unnecessary
List<String> lstNames;   // Type in name
```

</div>

<div>

## ✅ **Modern Java Style**

```java
String name;             // Type inferred
int count;              // Clean
boolean isActive;       // Semantic meaning
List<String> names;     // Simple
```

**Why avoid Hungarian notation?**
- IDEs show types
- Makes refactoring harder
- Violates DRY principle

</div>

</div>

---

# Practical Exercise

## Look at `NamingConventions.java`

<v-clicks>

**Good examples in the code:**
- `COMPANY_NAME` - Proper constant naming
- `employeeName` - Descriptive camelCase
- `isActive` - Boolean naming convention

**Intentional bad examples:**
- `badname` - Violates camelCase
- `ANOTHERBADNAME` - Wrong context for constants

</v-clicks>

<div class="mt-8">
<v-click>

**Your turn**: Can you identify other naming improvements in your own code?

</v-click>
</div>

---

# Key Takeaways

<v-clicks>

## **The Big Three Conventions**
1. **Variables/Methods**: `camelCase`
2. **Classes/Interfaces**: `PascalCase`  
3. **Constants**: `UPPER_SNAKE_CASE`

## **Core Principles**
- Choose clarity over brevity
- Be consistent across your codebase
- Use intention-revealing names

</v-clicks>

---

# Tools and Support

<v-clicks>

## **IDEs Enforce Conventions**
- Real-time highlighting of violations
- Auto-completion follows standards

## **Static Analysis**
- Checkstyle, SpotBugs, SonarQube
- Catch violations in CI/CD

## **Team Practices**
- Code reviews maintain standards
- Style guides document conventions

</v-clicks>

---

# Next Steps

<v-clicks>

## **In This Module**
- Examine `NamingConventions.java`
- Run the example code
- Analyze good vs. bad naming

## **Coming Up**
- Variables and data types
- Control structures
- Methods and parameters

</v-clicks>

<div class="mt-8">
<v-click>

**Ready to dive into the code?** 🚀

</v-click>
</div>