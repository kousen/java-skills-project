# Try It Out: Git Merge Conflict Resolution

This exercise walks you through resolving a realistic merge conflict that occurs during collaborative development.

## The Scenario

You've been working on adding employee search functionality with pagination to the Java Skills Project. Meanwhile, another developer added a simpler employee search feature to the main branch. When you try to integrate your changes, Git detects a conflict that you need to resolve.

## Setup: Fork-Based Workflow

This exercise demonstrates the **fork-based workflow** commonly used in open source projects:

1. **Fork the repository** on GitHub (create your own copy)
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR-USERNAME/java-skills-project.git
   cd java-skills-project
   ```

3. **Add the upstream remote** (original repository):
   ```bash
   git remote add upstream https://github.com/kousen/java-skills-project.git
   git remote -v  # Verify you have both 'origin' (your fork) and 'upstream' (original)
   ```

## Step 1: Work on Your Feature Branch

Switch to the prepared demo branch that contains your feature work:

```bash
# Fetch all branches
git fetch origin
git fetch upstream

# Switch to your feature branch
git checkout feature/employee-search-demo
```

**What's on this branch:**
- A `searchEmployees` method with pagination support
- Uses `/search` endpoint with `name`, `page`, and `size` parameters
- More comprehensive than the simple search that was added to main

## Step 2: Stay in Sync with Upstream

Before creating a pull request, sync your branch with the latest main branch:

```bash
# Fetch latest changes from the original repository
git fetch upstream

# Rebase your work on top of the latest main branch
git rebase upstream/main
```

**What happens:** Git will try to apply your commits on top of the latest main branch, but discovers a conflict!

## Step 3: Understanding the Conflict

Git stops the rebase and shows you:

```
CONFLICT (content): Merge conflict in web-services/src/main/java/com/oreilly/webservices/EmployeeController.java
error: could not apply eedde25... Add employee search functionality with pagination
```

Open the conflicted file in your editor. You'll see conflict markers:

```java
<<<<<<< HEAD
    /**
     * Find employees by query string (alternative implementation).
     * GET /api/employees/find?query={query}
     */
    @GetMapping("/find")
    public ResponseEntity<List<Employee>> findEmployees(@RequestParam String query) {
        // Their simpler implementation from main branch
        List<Employee> results = employeeService.findAll().stream()
            .filter(emp -> emp.name().toLowerCase().contains(query.toLowerCase()))
            .toList();
        
        return ResponseEntity.ok()
            .header("X-Search-Query", query)
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
        
        // Your pagination implementation
        List<Employee> employees = employeeService.findByNameContaining(name);
        
        int start = page * size;
        int end = Math.min(start + size, employees.size());
        List<Employee> paginatedEmployees = start < employees.size() ? 
            employees.subList(start, end) : List.of();
        
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(employees.size()))
            .header("X-Page", String.valueOf(page))
            .body(paginatedEmployees);
    }
>>>>>>> eedde25 (Add employee search functionality with pagination)
```

## Step 4: Resolve the Conflict

The conflict markers show:
- **`<<<<<<< HEAD`**: What's currently on the main branch (their changes)
- **`=======`**: Separator between the two versions  
- **`>>>>>>> commit-hash`**: Your changes from the feature branch

**Decision:** Combine the best of both implementations. Keep your pagination feature but adopt their simpler endpoint name.

Replace the entire conflicted section with this resolved version:

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
        
        // Combined approach: simpler filtering logic with pagination
        List<Employee> employees = employeeService.findAll().stream()
            .filter(emp -> emp.name().toLowerCase().contains(name.toLowerCase()))
            .toList();
        
        // Apply pagination
        int start = page * size;
        int end = Math.min(start + size, employees.size());
        List<Employee> paginatedEmployees = start < employees.size() ? 
            employees.subList(start, end) : List.of();
        
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(employees.size()))
            .header("X-Page", String.valueOf(page))
            .body(paginatedEmployees);
    }
```

**Why this resolution?**
- Uses their `/find` endpoint (simpler for API users)
- Keeps your pagination parameters (adds valuable functionality)
- Adopts their simpler search logic (easier to maintain)
- Combines the best of both implementations

## Step 5: Complete the Rebase

Mark the conflict as resolved and continue:

```bash
# Add the resolved file
git add web-services/src/main/java/com/oreilly/webservices/EmployeeController.java

# Continue the rebase
git rebase --continue
```

Git completes the rebase and your feature branch now includes both your work and the upstream changes.

## Step 6: Push and Create Pull Request

```bash
# Push your updated branch
git push --force-with-lease origin feature/employee-search-demo
```

Now create a pull request from your fork's `feature/employee-search-demo` branch to the upstream repository's `main` branch.

## Key Lessons

1. **Conflicts are normal** in collaborative development - they're not errors!

2. **Communication matters** - understanding both implementations helps you make better resolution decisions

3. **Consider the bigger picture** - sometimes the best resolution combines ideas from both sides

4. **Test your resolution** - make sure your merged code actually works

5. **Stay in sync frequently** - regular rebasing reduces conflict complexity

## Try It Yourself

Want to practice more? Try these variations:

- What if you chose to keep your `/search` endpoint instead?
- How would you handle a conflict where both changes are essential?
- Practice with different conflict scenarios in your own projects

## Professional Git Workflow Summary

This exercise demonstrates the complete professional workflow:
1. **Fork** → Create your own copy
2. **Clone** → Work locally on your machine  
3. **Branch** → Create focused feature branches
4. **Sync** → Stay updated with upstream changes
5. **Resolve** → Handle conflicts professionally
6. **Communicate** → Create clear pull requests
7. **Collaborate** → Work effectively with team members

These skills are essential for any developer working on team projects or contributing to open source!