---
layout: cover
--- 

# Nested Loops in Java

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 5: Implement nested loops to process complex data and create patterns.
  </span>
</div>

---

# Contact Info

**Ken Kousen**<br>
**Kousen IT, Inc.**

- **ken.kousen@kousenit.com**
- **http://www.kousenit.com**
- **http://kousenit.org** (blog)
- **Social Media:**
  - **[@kenkousen](https://twitter.com/kenkousen)** (Twitter)
  - **[@kousenit.com](https://bsky.app/profile/kousenit.com)** (Bluesky)
  - **[https://www.linkedin.com/in/kenkousen/](https://www.linkedin.com/in/kenkousen/)** (LinkedIn)
- ***Tales from the jar side*** (free newsletter)
  - **https://kenkousen.substack.com**
  - **https://youtube.com/@talesfromthejarside**

---

# Why Nested Loops Matter

<v-clicks>

- **Nested loops** = a loop inside another loop
- Essential for processing complex data relationships
- Not just for 2D arrays—many practical applications!

</v-clicks>

<div class="mt-8">
<v-click>

**Examples:** Employee pairing, work schedules, finding duplicates, creating visual patterns

</v-click>
</div>

---

# The Basic Nested Loop Pattern

Here's the fundamental structure you'll use repeatedly:

```java
// Outer loop controls the first dimension
for (int i = 0; i < outerLimit; i++) {
    
    // Inner loop controls the second dimension
    for (int j = 0; j < innerLimit; j++) {
        
        // Process the combination of i and j
        
    }
}
```

<v-click>

**Key concept:** Outer loop runs completely through inner loop each iteration

</v-click>

---

# Example 1: Employee Mentorship Pairing

From `com.oreilly.javaskills.NestedLoopsDemo`—pairing mentors with new hires:

```java
public void demonstrateMentorshipPairing() {
    List<Employee> mentors = Arrays.asList(
        new Employee("Alice Johnson", "Engineering", 5, 8),
        new Employee("Bob Smith", "Marketing", 4, 6)
    );
    
    List<Employee> newHires = Arrays.asList(
        new Employee("David Lee", "Engineering", 0, 0),
        new Employee("Emma Wilson", "Marketing", 0, 0)
    );
    
    // Nested loops to find all valid pairings
    for (Employee mentor : mentors) {
        for (Employee mentee : newHires) {
            if (mentor.department().equals(mentee.department())) {
                System.out.println(mentor.name() + " → " + mentee.name());
            }
        }
    }
}
```

---

# Example 2: Finding Duplicates

Classic nested loop pattern—compare each element with every element after it:

```java
public void findDuplicateEmployees() {
    List<String> employeeIds = Arrays.asList(
        "EMP001", "EMP002", "EMP003", "EMP002", "EMP004", "EMP001"
    );
    
    for (int i = 0; i < employeeIds.size(); i++) {
        for (int j = i + 1; j < employeeIds.size(); j++) {
            if (employeeIds.get(i).equals(employeeIds.get(j))) {
                System.out.println("Duplicate found: " + employeeIds.get(i) + 
                                 " at positions " + i + " and " + j);
            }
        }
    }
}
```

<v-click>

**Key insight:** Inner loop starts at `i + 1` to avoid duplicate comparisons

</v-click>

---

# Try It Out: Pattern Printing Exercise

Time to create visual patterns with `com.oreilly.javaskills.PatternPrinting`!

<div class="grid grid-cols-2 gap-8">

<div>

## **Right Triangle**
```java
public void printRightTriangle(int height) {
    for (int row = 1; row <= height; row++) {
        for (int col = 1; col <= row; col++) {
            System.out.print("* ");
        }
        System.out.println();
    }
}
```

**Output:**
```
* 
* * 
* * * 
* * * * 
```

</div>

<div>

## **Diamond Pattern**
```java
// Upper half (including middle)
for (int row = 0; row <= middle; row++) {
    // Print spaces, then stars
}

// Lower half  
for (int row = middle - 1; row >= 0; row--) {
    // Print spaces, then stars
}
```

**Output:**
```
  *
 ***
*****
 ***
  *
```

</div>

</div>

---

# Performance Considerations

<v-clicks>

- **Time Complexity:** O(n²) - can be expensive!
- If outer loop runs 100 times, inner loop runs 100 times = **10,000 total iterations**
- Use descriptive variable names: `mentor`, `mentee` instead of `i`, `j`
- Look for opportunities to **break early** when condition is met

</v-clicks>

---

# Key Takeaways

<v-clicks>

- **Nested loops** aren't just for 2D arrays—many practical uses!
- **Outer loop** controls first dimension, **inner loop** controls second
- Perfect for: employee pairing, schedules, duplicates, **pattern creation**
- Watch out for **O(n²) performance**, but don't let that stop you

</v-clicks>

<div class="mt-8">
<v-click>

**Your Challenge:** Complete the pattern printing exercises and create your own custom pattern!

</v-click>
</div>
