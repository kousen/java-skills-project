# Video Script: Git Collaboration for Java Projects

**Goal:** 24. Master Git workflows for effective team collaboration on Java projects.
**Target Duration:** 8–10 minutes
**Format:** On-camera intro → Screen-share demo → On-camera summary

---

## PART 1: ON-CAMERA INTRODUCTION (0:00–1:30)

**Host on camera:**

"Welcome back to our Java Skills series! I'm [Host Name], and today we're covering something that's essential for professional development—Git collaboration workflows.

If you've been following along with this series, you've learned a lot about Java code, but now it's time to learn how to work with that code in a team environment. Whether you're contributing to open source projects, working on a development team, or just want to follow industry best practices, mastering Git collaboration is crucial.

Today I'm going to show you the complete workflow for contributing to a Java project using fork-based collaboration. This is the standard approach used in open source projects and many professional environments.

We'll walk through the entire process: forking a repository, creating feature branches, making commits with proper messages, handling conflicts, creating pull requests, and getting your code reviewed and merged.

I'll be using our Java Skills Project as the example—it's actually live on GitHub, so you can follow along with the exact same steps I'm showing you. By the end of this video, you'll know how to contribute to any Java project professionally.

Let's dive in and see how real Git collaboration works."

---

## PART 2: SCREEN-SHARE DEMONSTRATION (1:30–7:00)

**[SWITCH TO SCREEN SHARE]**

**Host voice-over during screen demo:**

### Setting Up the Fork Workflow (1:30–2:30)

"First, let's start with the GitHub repository for our Java Skills Project. I'm going to show you the fork-based workflow, which is what you'd use when contributing to a project you don't own.

**[Screen: Show GitHub repository page]**
*Display text overlay: "github.com/kousen/java-skills-project"*

Here's our project repository. You can see it has all the Java modules we've been working with throughout this series. Notice we have several open issues here—these represent real features and bugs that need to be implemented.

Before contributing to any open source project, you should always look for contribution guidelines and the code of conduct. Most projects have a CONTRIBUTING.md file and a CODE_OF_CONDUCT.md file, or these may be included in the README or documentation. The contribution guidelines tell you about coding standards, testing requirements, and workflow preferences. The code of conduct explains the community standards for respectful collaboration and what behavior is expected from contributors.

*Display text overlay: "Always check: CONTRIBUTING.md + CODE_OF_CONDUCT.md"*

**[Screen: Click on Issues tab]**
*Display text overlay: "5 open issues available for contribution"*

Let's say I want to work on issue #1—'Add employee search functionality with pagination.' This is exactly the kind of task you'd pick up as a contributor.

**[Screen: Click Fork button]**
*Display text overlay: "Click 'Fork' to create your own copy"*

First step—I fork the repository. This creates my own copy that I can modify freely without affecting the original.

**[Screen: Show forked repository]**

Great! Now I have my own fork. Let's clone it locally and set up the proper remotes.

**[Screen: Open terminal]**

```bash
# Clone MY fork, not the original
git clone https://github.com/MY-USERNAME/java-skills-project.git
cd java-skills-project
```

*Display text overlay: "Always clone your fork, not the original"*

Now I need to add the original repository as an 'upstream' remote:

```bash
# Add the original repo as upstream
git remote add upstream https://github.com/kousen/java-skills-project.git
git remote -v
```

*Display text overlay: "upstream = original repo, origin = your fork"*

Perfect! Now I have both remotes configured properly.

### Creating and Working on a Feature Branch (2:30–3:45)

Before starting any work, I always sync with the upstream repository:

```bash
# Get the latest changes from upstream
git fetch upstream
git checkout main
git merge upstream/main
git push origin main
```

*Display text overlay: "Always sync before starting new work"*

Now let's create a feature branch for our work:

```bash
# Create feature branch for issue #1
git checkout -b feature/employee-search
git push -u origin feature/employee-search
```

*Display text overlay: "Branch name connects to issue: feature/employee-search"*

Let me make some changes to implement the employee search functionality. I'll edit a few files to add the search endpoints.

**[Screen: Show code editor, make realistic changes to a Java file]**

```bash
# Check what we've changed
git status
git diff
```

*Display text overlay: "Always review your changes before committing"*

Now let's commit with a proper commit message:

```bash
git add web-services/src/main/java/com/oreilly/webservices/EmployeeController.java
git commit -m "Add employee search functionality with pagination

- Implement search by name, department, and salary range
- Add pagination support with page/size parameters  
- Include input validation for search criteria
- Add comprehensive unit tests for search methods

Fixes #1"
```

*Display text overlay: "Good commit messages: imperative mood, explain why, reference issues"*

### Staying in Sync and Handling Conflicts (3:45–4:45)

While I was working, the upstream repository might have changed. Let me sync again:

```bash
# Fetch latest changes
git fetch upstream

# Rebase my feature branch against upstream main
git rebase upstream/main
```

*Display text overlay: "Rebase keeps a clean, linear history"*

Oh look, there's a conflict! This is totally normal in collaborative development.

**[Screen: Show conflict in code editor]**

```java
// <<<<<<< HEAD
    public List<Employee> searchEmployees(String name) {
        // My implementation
        return employeeService.findByNameContaining(name);
    }
// =======
    public List<Employee> findEmployees(String query) {
        // Their implementation
        return employeeService.search(query);
    }
// >>>>>>> upstream/main
```

*Display text overlay: "Conflicts show: your changes (top) vs their changes (bottom)"*

I need to resolve this by choosing the best approach or combining both:

```java
public List<Employee> searchEmployees(String name) {
    // Combined the best of both approaches
    return employeeService.findByNameContaining(name);
}
```

```bash
# Mark conflict as resolved
git add web-services/src/main/java/com/oreilly/webservices/EmployeeController.java
git rebase --continue
```

*Display text overlay: "Edit file, remove conflict markers, add file, continue rebase"*

### Creating a Pull Request (4:45–6:00)

Now my feature is ready. Let's push it and create a pull request:

```bash
# Push to my fork
git push --force-with-lease origin feature/employee-search
```

*Display text overlay: "--force-with-lease is safer than --force"*

**[Screen: Go to GitHub, show PR creation]**

Back on GitHub, I'll create a pull request from my fork to the upstream repository.

**[Screen: Click 'New Pull Request,' show cross-repo PR setup]**
*Display text overlay: "Creating PR: from my-fork/feature-branch to upstream/main"*

Notice GitHub automatically loads our comprehensive PR template. This is much more detailed than what you typically see in tutorials.

**[Screen: Show PR template being filled out]**
*Display text overlay: "Professional PR template includes: testing, security, documentation checklist"*

I'll fill this out properly:
- Summary of what I implemented
- Testing details 
- Security considerations
- Documentation updates

**[Screen: Submit PR]**

### Code Review and CI Integration (6:00–7:00)

Once I submit the PR, several things happen automatically:

**[Screen: Show PR page with CI checks]**
*Display text overlay: "Automated CI runs: builds project, runs tests, checks quality"*

Our GitHub Actions workflow automatically builds the project and runs tests. You can see the green checkmarks showing everything passed.

**[Screen: Show review comments section]**

Now the maintainer can review my code. They might ask for changes:

*Display text overlay: "Code review is collaborative—be open to feedback"*

If they request changes, I just make them on my local branch and push:

```bash
# Make requested changes
git add .
git commit -m "Address code review feedback - add input validation"
git push origin feature/employee-search
```

*Display text overlay: "PR automatically updates when you push to the branch"*

**[Screen: Show PR getting approved and merged]**

Once approved, the maintainer merges the PR. My contribution is now part of the main project!

### Cleanup (7:00 - 7:15)

Finally, I clean up my local environment:

```bash
# Switch back to main and sync
git checkout main
git fetch upstream
git merge upstream/main
git push origin main

# Delete feature branch
git branch -d feature/employee-search
git push origin --delete feature/employee-search
```

*Display text overlay: "Always clean up merged branches"*

**[END SCREEN SHARE]**

---

## PART 3: ON-CAMERA SUMMARY (7:15–8:30)

**Host back on camera:**

"And that's the complete Git collaboration workflow! Let me summarize the key lessons we learned today.

First, **always check for contribution guidelines and code of conduct before starting work**. Look for CONTRIBUTING.md and CODE_OF_CONDUCT.md files, or similar sections in the README or documentation. These explain the project's technical requirements and community standards for respectful collaboration.

Second, **fork-based workflows are the standard for open source and external contributions**. You fork the repository, work in your own copy, and submit pull requests to contribute back. This keeps the original repository clean and gives maintainers full control.

Third, **proper branch management is crucial**. Always create feature branches, never work directly on main. Keep your branches focused on specific issues or features, and use descriptive names that connect to your issues.

Fourth, **commit messages matter**. Write them in the imperative mood, keep the first line short, and explain why you made the change, not just what you changed. Good commits tell the story of your code.

Fifth, **stay synchronized with upstream**. Projects evolve constantly, so fetch regularly and rebase your work against the latest main branch. This prevents conflicts and keeps your contributions relevant.

Finally, **embrace code review**. It's not criticism—it's collaboration. Be open to feedback, ask questions when something's unclear, and remember that the goal is better code for everyone.

## Try It Out Exercise

For your hands-on exercise, I want you to fork our Java Skills Project repository and work through this exact workflow. Pick one of the open issues - they're all realistic features that would improve the project. Follow the complete process we just demonstrated: fork, branch, commit, push, and create a pull request.

The git-workflows directory in the repository has detailed step-by-step instructions in the git-workflow-examples.md file. It covers everything we showed here plus additional scenarios like hotfixes and release workflows.

This isn't just practice—if you create a quality pull request, it might actually get merged into the project! That would give you a real open source contribution to show on your résumé.

Git collaboration skills are essential for any professional Java developer. Whether you're joining a team, contributing to open source, or just following best practices, these workflows will serve you well throughout your career.

Next time, we'll dive into microservices architecture with Spring Boot. Until then, happy collaborating!"

---

**Key Screen Text Overlays Used:**
- Repository URL and navigation cues
- Command explanations and safety notes  
- Workflow step identifiers
- Best practice reminders
- Visual callouts for important concepts