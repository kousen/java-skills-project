---
layout: cover
---

# Two-Dimensional Arrays

<div class="pt-12">
  <span class="px-2 py-1 rounded">
    Goal 6: Utilize two-dimensional arrays to organize and process employee roster data.
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

# What is a 2D Array?

<v-clicks>

- Think of it as a **grid** or **table** with rows and columns
- It's technically an "array of arrays"
- Perfect for representing tabular data, like a spreadsheet

</v-clicks>

<div class="mt-8">
<v-click>

**Example:** Employee roster organized by department, quarterly sales data, game boards

</v-click>
</div>

---

# Declaring and Initializing 2D Arrays

```java
// Declare a 2D array of Strings
String[][] employeeRoster;

// Create with dimensions: 5 departments, 10 employees max each
employeeRoster = new String[5][10];

// Initialize with data
double[][] salesData = {
    {45000, 52000, 48000, 61000},  // Alice's quarterly sales
    {38000, 41000, 44000, 47000},  // Bob's quarterly sales
    {55000, 58000, 62000, 68000}   // Carol's quarterly sales
};

// Access element: [row][column]
employeeRoster[0][0] = "Alice Johnson"; // First dept, first employee
double q1Sales = salesData[0][0];      // Alice's Q1 sales: 45000
```

---

# Processing 2D Arrays with Nested Loops

<div class="grid grid-cols-2 gap-8">

<div>

## **The Pattern**
```java
// Outer loop: rows (departments)
for (int dept = 0; dept < array.length; dept++) {
    
    // Inner loop: columns (employees)
    for (int emp = 0; emp < array[dept].length; emp++) {
        
        // Process array[dept][emp]
        
    }
}
```

</div>

<div>

## **Enhanced For Loops**
```java
// When you don't need indices
for (String[] department : roster) {
    for (String employee : department) {
        if (employee != null) {
            System.out.println(employee);
        }
    }
}
```

</div>

</div>

---

# Real-World Example: Employee Roster

From `com.oreilly.javaskills.EmployeeRoster`:

```java
// Parallel 2D arrays for related data
String[][] employeeRoster = new String[5][10];  // Names
double[][] employeeSalaries = new double[5][10]; // Salaries

// Calculate department statistics
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
    System.out.printf("%s avg: $%,.2f%n", departments[dept], average);
}
```

---

# Try It Out: Quarterly Sales Analysis

`com.oreilly.javaskills.TwoDArrayExercise` demonstrates practical 2D array operations:

<div class="grid grid-cols-2 gap-8">

<div>

## **Sales Data Structure**
```java
double[][] salesData = {
    //Q1     Q2     Q3     Q4
    {45000, 52000, 48000, 61000}, // Alice
    {38000, 41000, 44000, 47000}, // Bob
    {55000, 58000, 62000, 68000}, // Carol
    {42000, 40000, 43000, 45000}  // David
};
```

</div>

<div>

## **Analysis Operations**
<v-clicks>

- **Row totals**: Annual sales per employee
- **Column totals**: Total sales per quarter
- **Commission calculations**: Apply rates
- **Growth analysis**: Quarter-over-quarter
- **Top performers**: Find max values

</v-clicks>

</div>

</div>

---

# Advanced Features

<v-clicks>

## **Jagged Arrays**
```java
// Each row can have different lengths
String[][] teams = new String[3][];
teams[0] = new String[5];  // Engineering: 5 people
teams[1] = new String[3];  // Marketing: 3 people  
teams[2] = new String[8];  // Sales: 8 people
```

## **Array Bounds**
```java
// Always check bounds to avoid errors
if (dept < roster.length && emp < roster[dept].length) {
    // Safe to access roster[dept][emp]
}
```

## **Constants for Dimensions**
```java
private static final int MAX_DEPARTMENTS = 5;
private static final int MAX_EMPLOYEES_PER_DEPT = 10;
```

</v-clicks>

---

# Common Use Cases

<div class="grid grid-cols-2 gap-8">

<div>

## **Business Applications**
<v-clicks>

- Spreadsheet data
- Sales matrices
- Inventory grids
- Schedule tables
- Budget allocations

</v-clicks>

</div>

<div>

## **Technical Applications**
<v-clicks>

- Game boards (chess, tic-tac-toe)
- Image pixel data
- Mathematical matrices
- Graph adjacency matrices
- Heat maps

</v-clicks>

</div>

</div>

---

# Key Takeaways

<v-clicks>

- **2D arrays** = arrays of arrays, perfect for grid-like data
- Use **nested loops** to process every element systematically
- **Row-major order**: `array[row][column]` convention
- Can have **jagged arrays** with different row lengths
- Always **check bounds** before accessing elements

</v-clicks>

<div class="mt-8">
<v-click>

**Your Challenge:** Complete the quarterly sales analysis exercise and create a monthly (12×n) sales matrix!

</v-click>
</div>