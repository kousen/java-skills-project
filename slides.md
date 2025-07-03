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
layout: section
---

# Naming Conventions

Writing readable and maintainable code

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

---
layout: section
---

# String Formatting

Working with text in Java

---

# String Formatting in Java

## From Basic to Modern

<v-clicks>

- **Basic concatenation** - Simple but inefficient
- **String.format()** - C-style formatting with type safety
- **printf()** - Direct output formatting
- **Text blocks** - Modern Java 15+ multiline strings
- **StringBuilder** - Efficient for complex building
- **SLF4J logging** - Industry-standard parameterized messaging

</v-clicks>

<div class="mt-8">
<v-click>

**Goal**: Choose the right tool for each situation

</v-click>
</div>

---

# Basic String Concatenation

<div class="grid grid-cols-2 gap-8">

<div>

## ❌ **Avoid in Production**

```java
String basic = "Employee: " + employeeName + 
               " (ID: " + employeeId + ")";
```

**Problems:**
- Creates multiple intermediate String objects
- Inefficient for loops
- Hard to read with complex data

</div>

<div>

## ✅ **When It's OK**

```java
String welcome = "Hello, " + name + "!";
```

**Acceptable for:**
- Simple, one-time concatenations
- Small strings
- Quick prototyping
- Learning examples

</div>

</div>

---

# String.format() - C-Style Formatting

<v-clicks>

## **Format Specifiers**

```java
String.format("Employee: %s (ID: %05d) - Salary: $%.2f", 
             employeeName, employeeId, salary);
```

## **Common Patterns**
- `%s` - String, `%d` - Integer, `%.2f` - Float (2 decimals)
- `%05d` - Zero-padded integers, `%,.2f` - Thousands separator

## **Benefits**
- Type safety and readable format strings
- Reusable patterns across your application

</v-clicks>

---

# printf() vs String.format()

<div class="grid grid-cols-2 gap-8">

<div>

## **String.format()**
```java
String formatted = String.format(
    "Employee: %s (ID: %05d)", 
    name, id);
System.out.println(formatted);
```

**Use when:**
- Need to store the formatted string
- Building strings for further processing
- Creating reusable formatted values

</div>

<div>

## **System.out.printf()**
```java
System.out.printf(
    "Employee: %s (ID: %05d)%n", 
    name, id);
```

**Use when:**
- Direct output to console
- One-time formatting
- Slightly more efficient (no intermediate String)

**Note:** Use `%n` for platform-independent newlines

</div>

</div>

---

# Text Blocks - Modern Java (15+)

<div class="grid grid-cols-2 gap-6">

<div>

## **Before**
```java
String html = "<html>\n" +
  "  <body>\n" +
  "    <h1>" + name + "</h1>\n" +
  "  </body>\n" +
  "</html>";
```

</div>

<div>

## **With Text Blocks**
```java
String html = """
    <html>
      <body>
        <h1>%s</h1>
      </body>
    </html>
    """.formatted(name);
```

</div>

</div>

<v-clicks>

## **Benefits**
- Natural multiline strings
- Preserves formatting and indentation
- Works with `.formatted()` method
- Much more readable

</v-clicks>

---

# StringBuilder - When Efficiency Matters

<v-clicks>

## **Perfect for Complex Building**
```java
StringBuilder sb = new StringBuilder();
sb.append("Employee Summary: ")
  .append(employeeName)
  .append(" (").append(employeeId).append(")")
  .append(" earning ").append(salary);
```

## **Use StringBuilder When:**
- Building strings in loops
- Conditional string construction
- Complex multi-step assembly
- Performance is critical

</v-clicks>

---

# String.join() - Simple and Convenient

<v-clicks>

## **Static Method for Quick Joining**
```java
String departments = String.join(", ", "Engineering", "Marketing", "Sales");
// Result: Engineering, Marketing, Sales

// With Collections too
String result = String.join(", ", namesList);
```

## **When to Use**
- Simple delimiter-separated strings
- No prefix/suffix needed
- Most common joining scenario

</v-clicks>

---

# StringJoiner - For Delimited Strings

<v-clicks>

## **Clean Delimiter Handling**
```java
StringJoiner joiner = new StringJoiner(", ", "[", "]");
joiner.add("Alice").add("Bob").add("Charlie");
// Result: [Alice, Bob, Charlie]
```

## **Real-World Example**
```java
StringJoiner csvLine = new StringJoiner(",");
csvLine.add(employee.getName())
       .add(String.valueOf(employee.getId()))
       .add(employee.getDepartment());
// Result: Alice Johnson,67890,Engineering
```

## **When to Use StringJoiner**
- Creating CSV or delimited data
- Building lists with separators
- Need prefix/suffix around entire string

</v-clicks>

---

# SLF4J Logging - Industry Standard

<v-clicks>

## **Parameterized Logging**
```java
Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.info("Employee {} (ID: {}) hired on {} with salary ${}", 
           employeeName, employeeId, hireDate, salary);
```

## **Why This Matters**
- **Performance**: Only formats if logging level is enabled
- **Ubiquitous**: Every Java project uses this pattern
- **Safe**: No string concatenation, handles nulls gracefully

## **Use `{}` placeholders - order matters left to right**

</v-clicks>

---

# Specialized Formatting

<div class="grid grid-cols-2 gap-8">

<div>

## **Currency Formatting**
```java
NumberFormat currency = 
    NumberFormat.getCurrencyInstance(Locale.US);
String formatted = currency.format(82500.75);
// Result: $82,500.75
```

## **Date Formatting**
```java
DateTimeFormatter formatter = 
    DateTimeFormatter.ofPattern("MMMM dd, yyyy");
String date = hireDate.format(formatter);
// Result: March 15, 2020
```

</div>

<div>

## **Why Use Specialized Formatters?**

<v-clicks>

- **Locale-aware** - Automatic localization
- **Type-safe** - Designed for specific data types
- **Feature-rich** - Handle edge cases
- **Standards-compliant** - Follow international conventions

</v-clicks>

</div>

</div>

---

# Escape Characters in Strings

<v-clicks>

## **Essential Escape Sequences**
```java
String newline = "First line\nSecond line";
String tab = "Name:\tAlice Johnson";
String quotes = "She said, \"Hello there!\"";
String backslash = "File path: C:\\Users\\Alice\\Documents";
String singleQuote = "It\'s a beautiful day";
```

## **Remember: Backslash Escapes Special Characters**

</v-clicks>

---

# Text Blocks vs Escape Characters

<v-clicks>

## **Before Text Blocks (Escape Heavy)**
```java
String oldWay = "Employee data:\n\tName: \"Alice\"\n\tSalary: $75000";
String jsonLike = "{\n\t\"name\": \"Alice\",\n\t\"active\": true\n}";
```

## **With Text Blocks (Clean)**
```java
String newWay = """
    Employee data:
        Name: "Alice"
        Salary: $75000
    """;
```

## **Best Practice: Choose Based on Complexity**
- **Simple formatting** → Escape characters
- **Multi-line content** → Text blocks

</v-clicks>

---

# Best Practices Summary

<v-clicks>

## **Choose the Right Tool**
- **`String.format()`** - Complex formatting, reusable patterns
- **Text blocks** - Multiline strings (Java 15+)
- **`StringBuilder`** - Loops and conditional building
- **`String.join()`** - Simple delimiter joining
- **SLF4J logging** - All logging statements

## **Key Guidelines**
- `StringBuilder` for loops, avoid concatenation in hot paths
- Format strings are self-documenting
- Use parameterized logging for performance

</v-clicks>

---
layout: section
---

# Operator Precedence

Understanding the order of operations

---

# Operator Precedence in Java

## Why It Matters

<v-clicks>

- **Order of operations** determines calculation results
- **Unexpected behaviors** from incorrect assumptions
- **Debugging nightmares** when precedence is unclear
- **Financial calculations** must be precise and predictable

</v-clicks>

<div class="mt-8">
<v-click>

> "Explicit is better than implicit" - Always use parentheses when in doubt!

</v-click>
</div>

---

# Java Operator Precedence (High to Low)

<div class="grid grid-cols-2 gap-8">

<div>

## **Highest Precedence**
- **Postfix**: `++`, `--`, `[]`, `.`, `()`
- **Unary**: `++`, `--`, `+`, `-`, `!`, `~`
- **Multiplicative**: `*`, `/`, `%`
- **Additive**: `+`, `-`

</div>

<div>

## **Lower Precedence**  
- **Relational**: `<`, `>`, `<=`, `>=`
- **Equality**: `==`, `!=`
- **Logical AND**: `&&`
- **Logical OR**: `||`
- **Assignment**: `=`, `+=`, `-=`, etc.

</div>

</div>

<v-clicks>

**Key Rule:** When in doubt, use parentheses!

</v-clicks>

---

# Arithmetic Precedence Gotchas

<div class="grid grid-cols-2 gap-8">

<div>

## ❌ **Common Mistake**
```java
double baseSalary = 50000;
int overtime = 20;
double overtimeRate = 1.5;
double hourlyRate = 25.0;

// Multiplication happens first!
double wrong = baseSalary + overtime * 
               overtimeRate * hourlyRate;
// Result: 50000 + 750 = 50750
```

</div>

<div>

## ✅ **Correct Approach**
```java
// Use parentheses for clarity
double correct = baseSalary + 
    (overtime * overtimeRate * hourlyRate);
// Same result, but intent is clear

// Or separate the calculation
double overtimePay = overtime * 
                    overtimeRate * hourlyRate;
double total = baseSalary + overtimePay;
```

</div>

</div>

---

# Boolean Operator Precedence

<v-clicks>

## **`&&` has higher precedence than `||`**

```java
boolean isFullTime = true;
boolean hasBonus = true;
int yearsOfService = 3;

// This expression: isFullTime || hasBonus && yearsOfService > 2
// Is evaluated as: isFullTime || (hasBonus && yearsOfService > 2)
boolean eligible = isFullTime || hasBonus && yearsOfService > 2;
```

## **Always Be Explicit**
```java
// Clear intent - what you probably meant
boolean eligibleClear = isFullTime || (hasBonus && yearsOfService > 2);

// Different logic entirely  
boolean eligibleDifferent = (isFullTime || hasBonus) && yearsOfService > 2;
```

</v-clicks>

---

# Assignment Operator Behavior

<v-clicks>

## **Right-to-Left Associativity**
```java
int a = 5, b = 10, c = 15;

// Multiple assignments work right to left
a = b = c = 20;
// Equivalent to: a = (b = (c = 20));
// Result: a=20, b=20, c=20
```

## **Compound Assignment Precedence**
```java
a = 5; b = 10;
a += b *= 2;
// Step 1: b *= 2  → b becomes 20
// Step 2: a += b  → a becomes 5 + 20 = 25
```

## **Why This Matters**
- Can create subtle bugs
- Makes code harder to read
- Better to split into separate statements

</v-clicks>

---

# Ternary Operator (`? :`) Precedence

<v-clicks>

## **Lower Precedence Than Most Operators**
```java
int bonus = salary > 50000 ? 1000 : 0;
// Equivalent to: int bonus = (salary > 50000) ? 1000 : 0;

// But watch out for this:
int result = a + b > 5 ? x * 2 : y + 1;
// Parsed as: int result = ((a + b) > 5) ? (x * 2) : (y + 1);
```

## **Complex Expressions Need Parentheses**
```java
// Confusing precedence
boolean eligible = isManager ? salary > 80000 && hasBonus : years > 5;

// Clear intent
boolean eligible = isManager ? (salary > 80000 && hasBonus) : (years > 5);
```

## **Best Practice: Always Parenthesize Complex Ternary Conditions**

</v-clicks>

---

# Increment/Decrement Precedence

<v-clicks>

## **Pre vs. Post Increment in Expressions**
```java
int i = 5;
int a = ++i * 2;  // Pre-increment: i becomes 6, then 6 * 2 = 12
int b = i++ * 2;  // Post-increment: 5 * 2 = 10, then i becomes 6

// Reset i
i = 5;
int x = 2 + ++i;  // i becomes 6, then 2 + 6 = 8  
int y = 2 + i++;  // 2 + 5 = 7, then i becomes 6
```

## **In Real Code: Avoid Mixing**
```java
// Confusing - don't do this
result = array[i++] + ++count * value--;

// Clear and maintainable
result = array[i] + (count + 1) * value;
i++;
count++;
value--;
```

</v-clicks>

---

# Modulus (`%`) and Special Cases

<v-clicks>

## **Modulus Has Same Precedence as `*` and `/`**
```java
int result = 10 + 15 % 4;
// Parsed as: 10 + (15 % 4) = 10 + 3 = 13

// Salary calculations with modulus
int totalHours = 45;
int regularHours = totalHours % 40;  // Wrong! % has higher precedence
int regularHoursCorrect = Math.min(totalHours, 40);
```

## **Common Uses**
```java
// Even/odd checking
boolean isEven = (number % 2) == 0;

// Cycling through array indices
int index = (currentIndex + 1) % arrayLength;

// Time calculations
int minutes = totalSeconds % 60;
int hours = (totalSeconds / 60) % 24;
```

</v-clicks>

---

# Java Has No Exponentiation Operator

<v-clicks>

## **Use `Math.pow()` Instead**
```java
// Other languages: base ** exponent
// Java uses: Math.pow(base, exponent)
double result = Math.pow(2, 3);  // Returns 8.0 (double)

// Salary with compound interest
double futureValue = presentValue * Math.pow(1 + rate, years);
```

## **Simple Alternatives**
```java
// For small powers, multiplication is clearer
int squared = x * x;           // Instead of Math.pow(x, 2)
int cubed = x * x * x;         // Instead of Math.pow(x, 3)

// Cast when you need int result
int result = (int) Math.pow(base, exponent);
```

</v-clicks>

---

# Operator Precedence Best Practices

<v-clicks>

## **The Golden Rules**

1. **When in doubt, use parentheses** - Clarity beats cleverness
2. **Break complex expressions** - Use intermediate variables  
3. **Avoid mixing operators** - Especially `++`/`--` in expressions
4. **Be explicit with ternary** - Parenthesize complex conditions

## **Write for the Next Developer**
```java
// Clever but unclear
if (isManager && salary > 80000 || hasBonus && years > 5) { }

// Clear and maintainable
boolean eligible = (isManager && salary > 80000) || 
                  (hasBonus && years > 5);
if (eligible) { }
```

</v-clicks>

---
layout: section
---

# Scanner and User Input

Reading data from the console safely

---

# Working with Scanner

## Essential for Interactive Programs

<v-clicks>

- **User interaction** - Get data from keyboard input
- **File processing** - Read from files and other input sources  
- **Data parsing** - Convert strings to appropriate types
- **Validation** - Handle invalid input gracefully

</v-clicks>

<div class="mt-8">
<v-click>

> Scanner is your gateway to making programs interactive and user-friendly

</v-click>
</div>

---

# Creating and Using Scanner

<v-clicks>

## **Basic Setup**
```java
import java.util.Scanner;

Scanner scanner = new Scanner(System.in);
```

## **Reading Different Types**
```java
String name = scanner.nextLine();        // Read entire line
int age = scanner.nextInt();             // Read integer  
double salary = scanner.nextDouble();    // Read double
boolean active = scanner.nextBoolean();  // Read boolean
```

## **Always Close Resources**
```java
scanner.close();  // Important: prevents resource leaks
```

</v-clicks>

---

# The nextLine() vs next() Pitfall

<div class="grid grid-cols-2 gap-8">

<div>

## ❌ **Common Problem**
```java
Scanner scanner = new Scanner(System.in);

System.out.print("Enter age: ");
int age = scanner.nextInt();

System.out.print("Enter name: ");
String name = scanner.nextLine();
// Problem: nextLine() reads empty string!
```

**Why it happens:**
- `nextInt()` leaves newline in buffer
- `nextLine()` reads that leftover newline

</div>

<div>

## ✅ **Better Approach**
```java
Scanner scanner = new Scanner(System.in);

System.out.print("Enter age: ");
String ageInput = scanner.nextLine();
int age = Integer.parseInt(ageInput);

System.out.print("Enter name: ");
String name = scanner.nextLine();
// Works perfectly!
```

**Why it works:**
- Always use `nextLine()` for input
- Parse strings to needed types
- No buffer issues

</div>

</div>

---

# Input Validation Patterns

<v-clicks>

## **The Validation Loop Pattern**
```java
private static int getIntInput(Scanner scanner) {
    while (true) {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.print("Invalid number. Please try again: ");
        }
    }
}
```

## **Why This Works**
- **Infinite loop** until valid input received
- **Exception handling** catches parsing errors
- **User-friendly** prompts for retry
- **Input trimming** handles whitespace

</v-clicks>

---

# Handling Different Data Types

<div class="grid grid-cols-2 gap-6">

<div>

## **Numeric Input**
```java
private static double getDoubleInput(Scanner scanner) {
    while (true) {
        try {
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.print("Invalid amount: $");
        }
    }
}
```

</div>

<div>

## **Date Input**
```java
private static LocalDate getDateInput(Scanner scanner) {
    DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    while (true) {
        try {
            String input = scanner.nextLine().trim();
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            System.out.print("Use yyyy-MM-dd format: ");
        }
    }
}
```

</div>

</div>

---

# Boolean Input - User Friendly

<v-clicks>

## **Accept Multiple Formats**
```java
private static boolean getBooleanInput(Scanner scanner) {
    while (true) {
        String input = scanner.nextLine().trim().toLowerCase();
        
        if ("true".equals(input) || "yes".equals(input) || "y".equals(input)) {
            return true;
        } else if ("false".equals(input) || "no".equals(input) || "n".equals(input)) {
            return false;
        } else {
            System.out.print("Please enter true/false (or yes/no): ");
        }
    }
}
```

## **User Experience Benefits**
- Accepts `true`/`false`, `yes`/`no`, `y`/`n`
- Case-insensitive input handling
- Clear error messages with examples

</v-clicks>

---

# Scanner Best Practices

<v-clicks>

## **Resource Management**
```java
try (Scanner scanner = new Scanner(System.in)) {
    // Use scanner here
} // Automatically closed
```

## **Consistent Input Strategy**
- **Always use `nextLine()`** - avoids buffer issues
- **Parse strings** to needed types with error handling
- **Trim input** to handle user whitespace

## **Validation Guidelines**
- **Never trust user input** - always validate
- **Provide clear prompts** - tell users what you expect
- **Handle errors gracefully** - give users another chance
- **Use specific error messages** - help users understand what went wrong

</v-clicks>

---

# Real-World Scanner Example

<v-clicks>

## **Employee Data Entry System**
```java
public static void main(String[] args) {
    try (Scanner scanner = new Scanner(System.in)) {
        
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter employee ID: ");
        int id = getIntInput(scanner);
        
        System.out.print("Enter salary: $");
        double salary = getDoubleInput(scanner);
        
        Employee employee = new Employee(name, id, salary);
        System.out.println("Created: " + employee);
        
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }
}
```

**Demonstrates:** Validation, error handling, resource management

</v-clicks>

---
layout: section
---

# Arrays and Nested Loops

Working with multidimensional data structures

---

# Why Multidimensional Arrays?

## Real-World Data is Often Tabular

<v-clicks>

- **Spreadsheet-like data** - Rows and columns of related information
- **Matrix operations** - Mathematical computations, game boards
- **Organizational structures** - Departments with employees, classes with students
- **Time series data** - Multiple measurements over time periods

</v-clicks>

<div class="mt-8">
<v-click>

> Think of 2D arrays as tables: rows represent one category, columns represent another

</v-click>
</div>

---

# 2D Array Fundamentals

<v-clicks>

## **Declaration and Creation**
```java
// Declare a 2D array
String[][] employeeRoster;

// Create with fixed dimensions
employeeRoster = new String[5][10];  // 5 departments, 10 employees each

// Or combine declaration and creation
double[][] salaries = new double[5][10];
```

## **Understanding the Structure**
```java
// Array of arrays - think of it as:
// roster[department][employee]
// salaries[department][employee]

roster[0][0] = "Alice Johnson";    // First employee in first department
salaries[0][0] = 95000.0;          // Alice's salary
```

</v-clicks>

---

# Accessing 2D Array Elements

<div class="grid grid-cols-2 gap-8">

<div>

## **Index-Based Access**
```java
String[][] roster = new String[3][5];

// Set values
roster[0][0] = "Alice";
roster[0][1] = "Bob";
roster[1][0] = "Carol";

// Get values
String employee = roster[0][0];
System.out.println(employee); // "Alice"

// Check boundaries
if (roster[dept][emp] != null) {
    System.out.println(roster[dept][emp]);
}
```

</div>

<div>

## **Working with Dimensions**
```java
// Get array dimensions
int departments = roster.length;        // 3
int maxEmployees = roster[0].length;    // 5

// Each row can have different lengths
String[][] irregular = {
    {"Alice", "Bob"},
    {"Carol", "David", "Eve"},
    {"Frank"}
};

System.out.println(irregular[1].length); // 3
```

</div>

</div>

---

# Nested Loops - The Natural Partner

<v-clicks>

## **Why Nested Loops?**
- **One loop per dimension** - outer loop for rows, inner loop for columns
- **Complete traversal** - visit every element in the 2D structure
- **Pattern processing** - apply operations to all elements

## **Conceptual Flow**
```
Outer loop: Department 0 → Department 1 → Department 2 ...
  Inner loop: Employee 0 → Employee 1 → Employee 2 ...
  Inner loop: Employee 0 → Employee 1 → Employee 2 ...
  Inner loop: Employee 0 → Employee 1 → Employee 2 ...
```

</v-clicks>

---

# Basic Nested Loop Pattern

<v-clicks>

## **Standard Template**
```java
for (int row = 0; row < array.length; row++) {
    for (int col = 0; col < array[row].length; col++) {
        // Process array[row][col]
    }
}
```

## **Employee Roster Example**
```java
for (int dept = 0; dept < roster.length; dept++) {
    for (int emp = 0; emp < roster[dept].length; emp++) {
        if (roster[dept][emp] != null) {
            System.out.println(roster[dept][emp]);
        }
    }
}
```

</v-clicks>

---

# Indexed vs Enhanced For Loops

<div class="grid grid-cols-2 gap-8">

<div>

## **Use Indices When You Need Position**
```java
// Displaying with row/column info
for (int dept = 0; dept < roster.length; dept++) {
    System.out.println(departments[dept] + ":");
    
    for (int emp = 0; emp < roster[dept].length; emp++) {
        if (roster[dept][emp] != null) {
            System.out.printf("  %d. %s%n", 
                emp + 1, roster[dept][emp]);
        }
    }
}
```

</div>

<div>

## **Use Enhanced Loops for Simple Processing**
```java
// Just counting all employees
int totalEmployees = 0;
for (String[] department : roster) {
    for (String employee : department) {
        if (employee != null) {
            totalEmployees++;
        }
    }
}

// Clean, readable, less error-prone
```

</div>

</div>

## **Choose Based on What You Need**
- **Need indices** → Use traditional for loops
- **Just processing elements** → Use enhanced for loops

---

# Setting Up 2D Data Structures

<v-clicks>

## **Employee Roster Management**
```java
String[][] roster = new String[5][10];  // 5 departments, up to 10 employees each
double[][] salaries = new double[5][10]; // Parallel array for salaries

// Populate departments
roster[0][0] = "Alice Johnson"; salaries[0][0] = 95000;
roster[0][1] = "Bob Smith";     salaries[0][1] = 87000;
roster[1][0] = "Carol Davis";   salaries[1][0] = 68000;
```

## **Parallel Arrays Strategy**
- **Related data** stays synchronized by using same indices
- **roster[dept][emp]** corresponds to **salaries[dept][emp]**
- **Simple and effective** for straightforward relationships

</v-clicks>

---

# Processing 2D Data with Nested Loops

<v-clicks>

## **Statistics and Analysis**
```java
// Calculate department totals
for (int dept = 0; dept < salaries.length; dept++) {
    double total = 0;
    int count = 0;
    
    for (int emp = 0; emp < salaries[dept].length; emp++) {
        if (salaries[dept][emp] > 0) {
            total += salaries[dept][emp];
            count++;
        }
    }
    
    double average = total / count;
    System.out.printf("Dept %d average: $%.2f%n", dept, average);
}
```

</v-clicks>

---

# Common Patterns

<v-clicks>

## **Null Checking in Sparse Arrays**
```java
// Always check for null in partially filled arrays
for (String[] department : roster) {
    for (String employee : department) {
        if (employee != null) {  // Important!
            System.out.println(employee);
        }
    }
}
```

## **Why Null Checking Matters**
- **Sparse arrays** don't fill every position
- **NullPointerException** if you forget to check
- **Enhanced for loops** still visit null elements

</v-clicks>

---

# Common Pitfalls

<v-clicks>

## **Array Bounds Awareness**
```java
// Be careful with nested array lengths
for (int i = 0; i < array.length; i++) {
    for (int j = 0; j < array[i].length; j++) {  // Use array[i].length!
        // Process array[i][j]
    }
}
```

## **Parallel Array Synchronization**
```java
// Keep related arrays in sync
roster[dept][emp] = "Alice Johnson";
salaries[dept][emp] = 95000;  // Same indices!

// Consider using objects for complex data
record Employee(String name, double salary) {}
```

</v-clicks>

---

# Design Best Practices

<v-clicks>

## **When to Use 2D Arrays**
- **Fixed dimensions** - departments × employees matrix
- **Simple data types** - String, int, double
- **Performance-critical** scenarios

## **When to Consider Alternatives**
- **Dynamic data** → Use Collections (ArrayList, etc.)
- **Complex objects** → Use List of custom objects/records
- **Many null values** → Consider sparse data structures

## **Parallel Arrays Work Well For Simple Relationships**

</v-clicks>

---

# Implementation Best Practices

<v-clicks>

## **Loop Strategy**
- **Enhanced for loops** - simple element processing
- **Indexed loops** - when you need position information
- **Extract complex logic** into separate methods

## **Performance Tips**
- **Row-major order** - process by rows for better cache performance
- **Early termination** - exit loops when you find what you need
- **Minimize nested complexity** - O(n²) can get expensive

</v-clicks>