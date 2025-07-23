
---
layout: cover
--- 

# 2D Arrays & Nested Loops

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goals 5 & 6: Create, manipulate, and process multidimensional data.
  </span>
</div>

---

# Contact Info

Ken Kousen<br>
Kousen IT, Inc.

- ken.kousen@kousenit.com
- http://www.kousenit.com
- http://kousenit.org (blog)
- Social Media:
  - [@kenkousen](https://twitter.com/kenkousen) (Twitter)
  - [@kousenit.com](https://bsky.app/profile/kousenit.com) (Bluesky)
  - [https://www.linkedin.com/in/kenkousen/](https://www.linkedin.com/in/kenkousen/) (LinkedIn)
- *Tales from the jar side* (free newsletter)
  - https://kenkousen.substack.com
  - https://youtube.com/@talesfromthejarside

---
layout: section
---

# What is a 2D Array?

<v-clicks>

- Think of it as a **grid** or a **table** with rows and columns.
- It's technically an "array of arrays".
- Perfect for representing tabular data, like a spreadsheet.

</v-clicks>

<div class="mt-8">
<v-click>

**Example:** A roster of employees, organized by department.

</v-click>
</div>

---

# Declaring and Initializing 2D Arrays

Here's how you create a 2D array in Java.

```java
// Declare a 2D array of Strings
String[][] employeeRoster;

// Create a grid of 5 rows (departments) and 10 columns (employees)
employeeRoster = new String[5][10];

// Access an element at a specific row and column
// (Row 0, Column 0 is the top-left element)
employeeRoster[0][0] = "Alice Johnson"; 
```

---

# Nested Loops: The Key to Processing 2D Arrays

To visit every element in the grid, you need a loop inside another loop.

<div class="grid grid-cols-2 gap-8">

<div>

## **The Pattern**
```java
// The outer loop iterates through the rows (departments)
for (int row = 0; row < array.length; row++) {

    // The inner loop iterates through the columns (employees)
    for (int col = 0; col < array[row].length; col++) {
        
        // Process the element at array[row][col]

    }
}
```

</div>

<div>

## **Conceptual Flow**

1.  Start with the first row.
2.  Visit every column in that row.
3.  Move to the next row.
4.  Visit every column in that new row.
5.  Repeat until all rows are processed.

</div>

</div>

---

# Code Demo: `EmployeeRoster.java`

Let's see a practical example of calculating statistics for each department using nested loops.

```java
// From EmployeeRoster.java

// Outer loop: Iterate through each department's salary list
for (int dept = 0; dept < salaries.length; dept++) {
    double total = 0;
    int count = 0;

    // Inner loop: Iterate through each salary in the current department
    for (int emp = 0; emp < salaries[dept].length; emp++) {
        if (salaries[dept][emp] > 0) { // Check for actual salary
            total += salaries[dept][emp];
            count++;
        }
    }
    
    double average = total / count;
    // ... print statistics ...
}
```

---
layout: section
---

# Key Takeaways

<v-clicks>

- Use **2D arrays** to model grid-like or tabular data.
- Use **nested loops** to process every element in a 2D array.
- The **outer loop** handles the rows; the **inner loop** handles the columns.
- Always be mindful of array boundaries to avoid errors.

</v-clicks>
