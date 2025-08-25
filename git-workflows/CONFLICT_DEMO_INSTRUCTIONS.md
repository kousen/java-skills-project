# Git Conflict Demo Instructions for Video Recording

This document provides step-by-step instructions for demonstrating realistic merge conflict resolution during the Topic 24 Git Collaboration video.

## Pre-Recording Setup

### 1. Start with Clean Fork Setup
```bash
# Clone your fork (not the original repo)
git clone https://github.com/YOUR-USERNAME/java-skills-project.git
cd java-skills-project

# Add upstream remote
git remote add upstream https://github.com/kousen/java-skills-project.git
git remote -v  # Verify both remotes show origin (your fork) and upstream (original)
```

### 2. Get to Demo Starting Point
```bash
# Fetch all branches and updates
git fetch upstream
git fetch origin

# Switch to the prepared demo feature branch
git checkout feature/employee-search-demo

# Verify you're on the right branch with the right content
git log --oneline -3  # Should show your searchEmployees commit
```

## During Video Recording

### The Conflict Scenario

**Your feature branch (`feature/employee-search-demo`) contains:**
- `searchEmployees` method 
- Uses `/search` endpoint
- Includes pagination support (`page` and `size` parameters)

**Main branch (`upstream/main`) contains:**
- `findEmployees` method
- Uses `/find` endpoint  
- Simple search without pagination

### Trigger the Conflict

```bash
# This command will create the conflict during recording
git rebase upstream/main
```

**Expected Git Response:**
```
Auto-merging web-services/src/main/java/com/oreilly/webservices/EmployeeController.java
CONFLICT (content): Merge conflict in web-services/src/main/java/com/oreilly/webservices/EmployeeController.java
error: could not apply eedde25... Add employee search functionality with pagination
hint: Resolve all conflicts manually, mark them as resolved with
hint: "git add/rm <conflicted_files>", then run "git rebase --continue".
```

### Show the Conflict in Editor

Open `web-services/src/main/java/com/oreilly/webservices/EmployeeController.java`

**You'll see conflict markers like this:**
```java
<<<<<<< HEAD
    /**
     * Find employees by query string (alternative implementation).
     * GET /api/employees/find?query={query}
     */
    @GetMapping("/find")
    public ResponseEntity<List<Employee>> findEmployees(@RequestParam String query) {
        logger.info("Finding employees with query: {}", query);
        
        // Simple search implementation from main branch
        List<Employee> results = employeeService.findAll().stream()
            .filter(emp -> emp.name().toLowerCase().contains(query.toLowerCase()))
            .toList();
        
        return ResponseEntity.ok()
            .header("X-Search-Query", query)
            .header("X-Result-Count", String.valueOf(results.size()))
            .body(results);
    }
=======
    /**
     * Search employees by name with pagination support.
     * GET /api/employees/search?name={name}&page={page}&size={size}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        logger.info("Searching employees with name containing: '{}', page: {}, size: {}", name, page, size);
        
        // Your pagination implementation from feature branch
        List<Employee> employees = employeeService.findByNameContaining(name);
        
        // Apply pagination manually for demo purposes
        int start = page * size;
        int end = Math.min(start + size, employees.size());
        List<Employee> paginatedEmployees = start < employees.size() ? 
            employees.subList(start, end) : List.of();
        
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(employees.size()))
            .header("X-Page", String.valueOf(page))
            .header("X-Size", String.valueOf(size))
            .body(paginatedEmployees);
    }
>>>>>>> eedde25 (Add employee search functionality with pagination)
```

### Explain the Conflict During Demo

**Key points to mention:**
- "The top section (HEAD) is from the main branch - their simpler implementation"
- "The bottom section is from my feature branch - includes pagination"  
- "Both methods are trying to exist in the same location in the file"
- "I need to decide: keep one, combine them, or create something new"

### Demonstrate Conflict Resolution

**Replace the entire conflicted section with this resolved version:**

```java
    /**
     * Search employees by name with pagination support.
     * GET /api/employees/find?name={name}&page={page}&size={size}
     */
    @GetMapping("/find")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        logger.info("Searching employees with name containing: '{}', page: {}, size: {}", name, page, size);
        
        // Combined approach: use their simpler endpoint name, keep our pagination
        List<Employee> employees = employeeService.findAll().stream()
            .filter(emp -> emp.name().toLowerCase().contains(name.toLowerCase()))
            .toList();
        
        // Apply pagination from our feature implementation
        int start = page * size;
        int end = Math.min(start + size, employees.size());
        List<Employee> paginatedEmployees = start < employees.size() ? 
            employees.subList(start, end) : List.of();
        
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(employees.size()))
            .header("X-Page", String.valueOf(page))
            .header("X-Size", String.valueOf(size))
            .body(paginatedEmployees);
    }
```

**Resolution strategy to explain:**
- "I'm keeping the `/find` endpoint name from main (simpler for users)"
- "But adding my pagination parameters because they add value"
- "Using the simpler search logic from main but with pagination applied"
- "This gives us the best of both implementations"

### Complete the Rebase

```bash
# Mark the conflict as resolved
git add web-services/src/main/java/com/oreilly/webservices/EmployeeController.java

# Continue the rebase process
git rebase --continue
```

**Expected result:**
```
[detached HEAD abc1234] Add employee search functionality with pagination
 1 file changed, 28 insertions(+)
Successfully rebased and updated refs/heads/feature/employee-search-demo.
```

### Verify the Resolution

```bash
# Show that rebase completed successfully
git status

# Show the final resolved method (optional)
git show HEAD
```

## Reset for Multiple Takes

If you need to record this section again or made a mistake:

### Option 1: If you're in the middle of a rebase
```bash
git rebase --abort
```

### Option 2: Reset to original demo state
```bash
# Reset feature branch to its pre-rebase state
git reset --hard origin/feature/employee-search-demo

# Verify you're back to the starting point
git log --oneline -2
```

### Option 3: Full reset (if things get messy)
```bash
# Go back to main and re-fetch everything
git checkout main
git fetch upstream
git fetch origin

# Delete and recreate the demo branch
git branch -D feature/employee-search-demo
git checkout origin/feature/employee-search-demo -b feature/employee-search-demo
```

## Troubleshooting

### If the conflict doesn't appear:
- Verify you're on `feature/employee-search-demo` branch
- Check that upstream remote points to the original repository
- Ensure main branch has the `findEmployees` method: `git show upstream/main:web-services/src/main/java/com/oreilly/webservices/EmployeeController.java | grep -A 5 findEmployees`

### If resolution doesn't work:
- Make sure you removed ALL conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`)
- Verify the method syntax is correct (matching braces, semicolons)
- Check that you're adding the right file: `git add web-services/src/main/java/com/oreilly/webservices/EmployeeController.java`

## Key Demo Points to Emphasize

1. **Conflict markers explanation**: Show and explain `<<<<<<<`, `=======`, `>>>>>>>` 
2. **Decision making**: Explain why you chose this resolution strategy
3. **Best practices**: 
   - Always understand both sides of the conflict
   - Choose the approach that benefits users most
   - Test the resolution if possible
   - Consider the team's preferences and project standards
4. **Professional collaboration**: This is normal, not a failure - it's how teams work together

## Post-Demo Cleanup (Optional)

After recording, you may want to:
```bash
# Push the resolved version for future reference
git push --force-with-lease origin feature/employee-search-demo

# Or reset back to original state to keep demo consistent
git reset --hard origin/feature/employee-search-demo
```

---

**Note**: These demo branches should remain consistent for reliable video recording. The conflict scenario represents realistic collaborative development situations.