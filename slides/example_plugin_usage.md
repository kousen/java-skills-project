---
# Enhanced Java Skills Presentation Example
# Demonstrates all installed Slidev plugins

theme: apple-basic
highlighter: prism
addons:
  - slidev-addon-java-runner
  - slidev-addon-components  
  - slidev-addon-fancy-arrow
  - slidev-addon-tldraw
  - slidev-addon-d2-diagram
  - '@katzumi/slidev-addon-qrcode'

# Color scheme for Java Skills project
themeConfig:
  primary: '#00D4FF'
  secondary: '#333333'
---

# Java Skills Video Series
## Modern Java Features with Enhanced Slidev

<div class="pt-12">
  <span class="px-2 py-1 rounded" style="background-color: #00D4FF; color: white;">
    Goal: Learn modern Java with interactive presentations
  </span>
</div>

---

# Plugin Demonstration Overview

<v-clicks>

- 🎯 **Java Code Runner** - Execute Java directly in slides
- 🎨 **Enhanced Components** - Interactive elements for learning
- 📐 **Fancy Arrows** - Professional annotations and emphasis  
- ✏️ **Drawing Tools** - Hand-drawn diagrams and sketches
- 📊 **D2 Diagrams** - Text-to-diagram conversion
- 🔗 **QR Codes** - Easy access to resources

</v-clicks>

---

# Java Code Runner Demo

Execute live Java code in your presentation:

<JavaRunner>

```java
public class ModernJavaDemo {
    public static void main(String[] args) {
        // Modern Java features in action
        var employees = List.of(
            new Employee("Alice", 1001, 75000),
            new Employee("Bob", 1002, 80000)
        );
        
        System.out.println("Employee count: " + employees.size());
        
        // Stream processing with var
        var highEarners = employees.stream()
            .filter(emp -> emp.salary() > 70000)
            .map(emp -> emp.name().toUpperCase())
            .toList();
            
        System.out.println("High earners: " + highEarners);
    }
}

record Employee(String name, int id, double salary) {}
```
</JavaRunner>

---

# D2 Diagram Example
## Object Composition Relationship

```d2
Employee: {
  shape: class
  -name: String
  -id: int
  -salary: double
  +getName(): String
  +setAddress(Address): void
}

Address: {
  shape: class
  -street: String
  -city: String  
  +getMailingLabel(): String
}

Department: {
  shape: class
  -name: String
  -employees: List<Employee>
  +addEmployee(Employee): void
}

Employee -> Address: has-a
Department -> Employee: contains
```

<v-click>

**Key Insight:** Composition creates flexible "has-a" relationships

</v-click>

---

# Interactive Drawing Demo

<Tldraw id="java-architecture-sketch" />

<v-clicks>

- Draw class diagrams during live presentation
- Illustrate concepts with hand-drawn elements
- Engage students with interactive content
- Perfect for explaining complex relationships

</v-clicks>

---
layout: two-cols
---

# Before: Traditional POJO

```java
public final class EmployeePojo {
    private final String name;
    private final int id;
    private final double salary;
    
    public EmployeePojo(String name, 
                       int id, 
                       double salary) {
        this.name = name;
        this.id = id; 
        this.salary = salary;
    }
    
    public String getName() { 
        return name; 
    }
    
    public int getId() { 
        return id; 
    }
    
    public double getSalary() { 
        return salary; 
    }
    
    // Plus equals(), hashCode(), toString()
    // ... many more lines of boilerplate
}
```

::right::

# After: Java Record

<FancyArrow from="100,50" to="300,150" color="#00D4FF" />

```java
public record Employee(
    String name, 
    int id, 
    double salary
) { }
```

<v-clicks>

- **One line** replaces 50+ lines of code!
- Automatic `equals()`, `hashCode()`, `toString()`
- Immutable by default
- Perfect for data carriers

</v-clicks>

---

# Enhanced Component Features

<TwoSlashCode>
```java
var employee = new Employee("Alice", 1001, 75000);
//    ^? Employee - type inferred by compiler

var employees = List.of(employee);  
//   ^? List<Employee> - generic type preserved

// Modern for-each with var
for (var emp : employees) {
//     ^? Employee - type inferred in loop
    System.out.println(emp.name());
}
```
</TwoSlashCode>

<v-click>

**Type Inference Visualization:** Shows exactly what the compiler sees!

</v-click>

---

# Resource Access with QR Codes

<div class="grid grid-cols-2 gap-8">

<div>

## Try It Out Exercise

<QRCode 
  value="https://github.com/user/java-skills-project/tree/main/oop-core/src/main/java/com/oreilly/javaskills/oop/exercise" 
  width="200"
  style="margin: 2rem auto;"
/>

**Scan to access:** RecordsExercise.java

</div>

<div>

## Complete Project

<QRCode 
  value="https://github.com/user/java-skills-project" 
  width="200"  
  style="margin: 2rem auto;"
/>

**Scan for:** Full source code repository

</div>

</div>

---

# Advanced Diagram: Microservices Architecture

```d2
direction: right

client: Client Application {
  shape: rectangle
  style.fill: "#E3F2FD"
}

gateway: API Gateway {
  shape: rectangle
  style.fill: "#FFF3E0"
}

services: {
  employee: Employee Service {
    shape: rectangle
    style.fill: "#E8F5E8"
  }
  
  payroll: Payroll Service {
    shape: rectangle
    style.fill: "#FFF0F5"
  }
  
  department: Department Service {
    shape: rectangle
    style.fill: "#F0F8FF"
  }
}

databases: {
  empdb: Employee DB {
    shape: cylinder
    style.fill: "#FFFACD"
  }
  
  paydb: Payroll DB {
    shape: cylinder
    style.fill: "#FFFACD"
  }
  
  deptdb: Department DB {
    shape: cylinder
    style.fill: "#FFFACD"
  }
}

# Connections
client -> gateway: HTTPS
gateway -> services.employee: REST
gateway -> services.payroll: REST  
gateway -> services.department: REST

services.employee -> databases.empdb: JDBC
services.payroll -> databases.paydb: JDBC
services.department -> databases.deptdb: JDBC
```

---

# Professional Annotations

<FancyArrow 
  from="100,100" 
  to="400,200" 
  color="#00D4FF"
  style="stroke-width: 3;"
/>

<div style="position: absolute; top: 120px; left: 120px;">

**Key Point:** Records eliminate boilerplate

</div>

<FancyArrow 
  from="500,300" 
  to="200,400" 
  color="#FF6B6B"
  style="stroke-dasharray: 5,5;"
/>

<div style="position: absolute; top: 320px; left: 520px;">

**Warning:** Don't overuse var!

</div>

---

# Contact Information

Ken Kousen<br>
Kousen IT, Inc.

- ken.kousen@kousenit.com
- http://www.kousenit.com  
- http://kousenit.org (blog)
- Social Media:
  - [@kenkousen](https://twitter.com/kenkousen) (Twitter)
  - [@kousenit.com](https://bsky.app/profile/kousenit.com) (Bluesky)
  - [LinkedIn](https://www.linkedin.com/in/kenkousen/)
- *Tales from the jar side* (newsletter)
  - https://kenkousen.substack.com
  - https://youtube.com/@talesfromthejarside

<QRCode 
  value="https://kenkousen.substack.com" 
  width="150"
  style="position: absolute; top: 200px; right: 100px;"
/>