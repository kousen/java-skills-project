
# Video Script: 2D Arrays & Nested Loops

**Goal:** 5 & 6. Create, manipulate, and process multidimensional data.
**Target Duration:** 4-5 minutes

---

### SCENE 1: Introduction (0:00 - 0:45)

**(Show Slide 1: Title Slide - "2D Arrays & Nested Loops")**

**YOU:**
"Welcome back. In this video, we're going to look at how to handle more complex data structures by exploring two related topics: **two-dimensional arrays** and **nested loops**."

**(Transition to Slide 2: What is a 2D Array?)**

**YOU:**
"So what is a 2D array? The easiest way to think about it is as a grid or a table with rows and columns. It's perfect for any kind of tabular data, like a spreadsheet, a tic-tac-toe board, or, in our case, a roster of employees organized by department. Technically, it's an array of arrays, but the grid analogy is often the most helpful."

---

### SCENE 2: Declaring and Using 2D Arrays (0:45 - 1:30)

**(Show Slide 3: Declaring and Initializing 2D Arrays)**

**YOU:**
"Declaring one is straightforward. You use two sets of square brackets to indicate two dimensions. For example, `String[][]` declares a 2D array of Strings."

**YOU:**
"When you create it, you specify the number of rows and columns. So `new String[5][10]` creates a grid with 5 rows and 10 columns. To access a specific element, you provide its coordinates, or index, for the row and column, remembering that Java is zero-indexed."

---

### SCENE 3: Nested Loops (1:30 - 2:30)

**(Show Slide 4: Nested Loops)**

**YOU:**
"Now, how do we visit every element in this grid? This is where nested loops come in. To process a 2D array, you need a loop inside of another loop."

**YOU:**
"The pattern is almost always the same. The **outer loop** iterates through the rows, from row 0 to the last row. And for each row, the **inner loop** iterates through all the columns in that row. This ensures you visit every single cell in the grid, one row at a time."

---

### SCENE 4: Code Demo (2:30 - 4:00)

**(Transition to IDE showing `EmployeeRoster.java`)**

**YOU:**
"Let's look at a practical example in our `EmployeeRoster.java` file. Here, we have two 2D arrays: one for employee names and a parallel one for their salaries, both organized by department."

**(Scroll to the `calculateStatistics` method)**

**YOU:**
"This `calculateStatistics` method is a perfect demonstration. We want to calculate the average salary for each department. The outer loop iterates from department 0 to 4."

**YOU:**
"Inside, for each department, we initialize some variables to track the total salary and employee count. Then, the inner loop kicks in. It iterates through all the potential employee slots in the current department's salary list."

**YOU:**
"Inside the inner loop, we check if a salary exists, and if it does, we add it to the total and increment the count. Once the inner loop finishes, we have the stats for one department, we can calculate the average, print it, and then the outer loop moves to the next department and the process repeats."

---

### SCENE 5: Conclusion (4:00 - 4:30)

**(Show Slide 6: Key Takeaways)**

**YOU:**
"To summarize: use 2D arrays to model grid-like data. And when you need to process every element in that grid, your go-to tool is a nested loop, with the outer loop for rows and the inner loop for columns. This is a fundamental pattern for processing all kinds of complex data in Java."

**YOU:**
"Thanks for watching, and I'll see you in the next video."
