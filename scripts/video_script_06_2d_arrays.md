# Video Script: Two-Dimensional Arrays

## Introduction (0:00-0:15)

Welcome back! In our last video, we explored nested loops. Today, we're looking at the perfect data structure to pair with them - two-dimensional arrays. If you've ever worked with spreadsheets, game boards, or any grid-like data, 2D arrays are exactly what you need.

## What is a 2D Array? (0:15-0:45)

A two-dimensional array is essentially an array of arrays. Think of it as a grid or table with rows and columns, just like a spreadsheet. 

In our Employee Management System, we might use a 2D array to store employee data where each row represents a department and each column represents a specific employee within that department. It's perfect for organizing data that naturally fits into a table structure.

## Declaration and Creation (0:45-1:15)

Declaring a 2D array uses two sets of square brackets:

[Show declaration example]

```java
String[][] employeeNames;  // Declaration
employeeNames = new String[5][10];  // 5 departments, 10 employees each
```

You can also initialize with data directly:

```java
String[][] departments = {
    {"Alice", "Bob", "Charlie"},      // Engineering
    {"Diana", "Eve"},                 // Marketing  
    {"Frank", "Grace", "Henry", "Ivy"} // Sales
};
```

## Accessing Elements (1:15-1:45)

To access an element, you provide both row and column indices:

[Show access example]

```java
String employee = employeeNames[0][2];  // Third employee in first department
employeeNames[1][0] = "New Employee";   // Set first employee in second department
```

Remember, Java is zero-indexed, so the first row is index 0, first column is index 0.

## Working with Variable Lengths (1:45-2:15)

Here's something cool - each row in a 2D array can have different lengths! This is called a "jagged array."

[Show EmployeeRoster.java example]

In our EmployeeRoster class, departments might have different numbers of employees. Engineering might have 8 people, while Marketing has only 3. Java handles this perfectly.

You can check the length of any row using `array[row].length`, and the number of rows using `array.length`.

## Processing with Enhanced For Loops (2:15-2:45)

You can use enhanced for loops with 2D arrays too:

[Show enhanced for loop example]

```java
for (String[] department : employeeNames) {
    for (String employee : department) {
        if (employee != null) {
            System.out.println("Employee: " + employee);
        }
    }
}
```

This is cleaner when you don't need the indices, just the values.

## Real-World Example (2:45-3:30)

Let's look at our EmployeeRoster class again. We have two parallel 2D arrays - one for names and one for salaries:

[Show EmployeeRoster.java]

Notice how we use nested loops to process these arrays. The outer loop goes through each department, and the inner loop processes each employee in that department. The arrays work together - `employeeNames[dept][emp]` corresponds to `employeeSalaries[dept][emp]`.

This pattern is incredibly common in real applications - organizing data by categories and subcategories.

## Common Use Cases (3:30-3:45)

2D arrays are perfect for:
- Game boards (chess, tic-tac-toe, Sudoku)
- Image data (pixels in rows and columns)  
- Mathematical matrices
- Seating charts or room layouts
- Any tabular data where both dimensions matter

## Memory Considerations (3:45-4:00)

Remember that 2D arrays can use significant memory. A 1000x1000 array of integers uses about 4MB. For very large datasets, consider using ArrayList of ArrayLists or other data structures that grow dynamically.

## Wrapping Up (4:00-4:15)

Two-dimensional arrays are your go-to tool for grid-based data. Combined with nested loops from our previous video, you can process complex tabular information efficiently and elegantly.

Next time, we'll explore file I/O operations to save and load our employee data. Until then, think in grids and happy coding!

## Code Examples Referenced:

1. 2D array declaration and initialization
2. Element access and modification
3. EmployeeRoster with parallel 2D arrays
4. Enhanced for loop processing
5. Jagged array examples