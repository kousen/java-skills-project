---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Git Collaboration
  Working with branches and merge requests
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Git Collaboration for Java Projects
---

# Git Collaboration

Working with Branches and Merge Requests

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Press Space for next page <carbon:arrow-right class="inline"/>
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
transition: slide-left
---

# Why Git Matters

## Version Control Benefits

<v-clicks>

- Track every change to your code
- Collaborate without conflicts
- Rollback when things go wrong

</v-clicks>

## Team Collaboration

<v-clicks>

- Work on features independently
- Review code before merging
- Maintain code quality

</v-clicks>

---
transition: slide-left
---

# Git Workflow Overview

## Branch-Based Development

<v-clicks>

- `main` - Production-ready code
- `feature/*` - New features
- `bugfix/*` - Bug fixes

</v-clicks>

## Process

<v-clicks>

1. Create feature branch
2. Make changes
3. Open merge request
4. Code review
5. Merge to main

</v-clicks>

---
transition: slide-left
---

# Setting Up Git

## Initial Configuration

```bash
# Configure identity
git config --global user.name "Your Name"
git config --global user.email "your.email@company.com"

# Useful aliases
git config --global alias.st status
git config --global alias.co checkout
git config --global alias.br branch
```

---
transition: slide-left
---

# Cloning a Repository

## Getting Started

```bash
# Clone the Employee Management project
git clone https://github.com/company/employee-management.git
cd employee-management

# Check current status
git status
git branch -a
```

---
transition: slide-left
---

# Creating Feature Branches

## Starting New Work

```bash
# Update main branch
git checkout main
git pull origin main

# Create feature branch
git checkout -b feature/employee-validation

# Alternative single command
git checkout -b feature/employee-validation origin/main
```

---
transition: slide-left
---

# Making Changes

## Development Workflow

```bash
# Check what's changed
git status
git diff

# Add files to staging
git add src/main/java/Employee.java
git add src/test/java/EmployeeTest.java

# Or add all changes
git add .
```

---
transition: slide-left
---

# Committing Changes

## Good Commit Practices

```bash
# Commit with descriptive message
git commit -m "Add email validation to Employee class

- Added @Email annotation to email field
- Created test for invalid email formats
- Updated documentation for validation rules"
```

---
transition: slide-left
---

# Commit Message Best Practices

## Structure

<v-clicks>

- First line: Brief summary (50 chars)
- Blank line
- Detailed explanation (72 chars per line)

</v-clicks>

## Tips

<v-clicks>

- Use imperative mood ("Add" not "Added")
- Explain why, not just what
- Reference issue numbers

</v-clicks>

---
transition: slide-left
---

# Working with Remote Branches

## Pushing Your Branch

```bash
# First push (creates remote branch)
git push -u origin feature/employee-validation

# Subsequent pushes
git push

# Check remote branches
git branch -r
```

---
transition: slide-left
---

# Staying Updated

## Syncing with Main

```bash
# Fetch latest changes
git fetch origin

# Merge main into your branch
git checkout feature/employee-validation
git merge origin/main

# Or rebase (cleaner history)
git rebase origin/main
```

---
transition: slide-left
---

# Resolving Merge Conflicts

## When Conflicts Occur

```java
// File: Employee.java
public class Employee {
<<<<<<< HEAD
    private String firstName;
    private String lastName;
=======
    private String fullName;
>>>>>>> origin/main
}
```

---
transition: slide-left
---

# Fixing Conflicts

## Manual Resolution

```java
// After discussion, choose the approach
public class Employee {
    private String firstName;
    private String lastName;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
```

```bash
# Mark as resolved
git add Employee.java
git commit -m "Resolve merge conflict in Employee class"
```

---
transition: slide-left
---

# Creating Pull/Merge Requests

## Using GitHub

```bash
# Push your branch
git push origin feature/employee-validation

# Then on GitHub:
# 1. Navigate to repository
# 2. Click "New Pull Request"
# 3. Select your branch
# 4. Add description
# 5. Request reviewers
```

---
transition: slide-left
---

# Pull Request Best Practices

## Description Template

```markdown
## Summary
Add email validation to Employee class

## Changes
- Added @Email annotation to email field
- Created comprehensive test suite
- Updated documentation

## Testing
- Unit tests pass
- Integration tests pass
- Manual testing completed

## Related Issues
Closes #123
```

---
transition: slide-left
---

# Code Review Process

## As a Reviewer

<v-clicks>

- Check for bugs and edge cases
- Verify tests cover new functionality
- Ensure code follows style guidelines

</v-clicks>

## As an Author

<v-clicks>

- Respond to feedback professionally
- Make requested changes promptly
- Ask questions if unclear

</v-clicks>

---
transition: slide-left
---

# GitHub Review Features

## Review Actions

```bash
# Approve
"LGTM! Good implementation of validation."

# Request changes
"Please add test for null email case."

# Comment
"Consider using a constant for the regex pattern."
```

---
transition: slide-left
---

# Handling Review Feedback

## Making Changes

```bash
# Make requested changes
git add src/main/java/Employee.java
git commit -m "Add test for null email validation"

# Push updates
git push origin feature/employee-validation

# PR automatically updates!
```

---
transition: slide-left
---

# Merging Strategies

## Three Options

<v-clicks>

**Merge Commit**: Preserves branch history
**Squash and Merge**: Clean, single commit
**Rebase and Merge**: Linear history

</v-clicks>

## Choosing Strategy

<v-clicks>

- Feature work: Squash and merge
- Hotfixes: Rebase and merge
- Integration: Merge commit

</v-clicks>

---
transition: slide-left
---

# After Merging

## Cleanup

```bash
# Switch to main
git checkout main

# Pull latest changes
git pull origin main

# Delete feature branch
git branch -d feature/employee-validation

# Delete remote branch
git push origin --delete feature/employee-validation
```

---
transition: slide-left
---

# Git Hooks for Java

## Pre-commit Hook

```bash
#!/bin/sh
# .git/hooks/pre-commit

# Run tests before commit
gradle test
if [ $? -ne 0 ]; then
    echo "Tests failed. Commit aborted."
    exit 1
fi

# Check code style
gradle checkstyleMain
if [ $? -ne 0 ]; then
    echo "Checkstyle violations. Commit aborted."
    exit 1
fi
```

---
transition: slide-left
---

# Continuous Integration

## GitHub Actions

```yaml
# .github/workflows/java.yml
name: Java CI
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 21
      uses: actions/setup-java@v2
      with:
        java-version: '21'
    
    - name: Run tests
      run: gradle test
```

---
transition: slide-left
---

# Advanced Git Commands

## Useful for Java Development

```bash
# View changes in specific file
git log -p src/main/java/Employee.java

# Find when a bug was introduced
git bisect start
git bisect bad HEAD
git bisect good v1.0

# Cherry-pick specific commit
git cherry-pick abc123

# Stash changes temporarily
git stash push -m "WIP: refactoring"
git stash pop
```

---
transition: slide-left
---

# Gitignore for Java

## Essential Patterns

```gitignore
# Java
*.class
*.jar
*.war
*.ear

# Gradle
build/
.gradle/

# IDE
.idea/
*.iml
.vscode/

# OS
.DS_Store
Thumbs.db

# Logs
*.log
```

---
transition: slide-left
---

# Branch Protection Rules

## Repository Settings

<v-clicks>

- Require pull request reviews
- Require status checks (CI)
- Restrict who can push to main

</v-clicks>

## Benefits

<v-clicks>

- Prevents accidental direct pushes
- Ensures code review process
- Maintains code quality

</v-clicks>

---
transition: slide-left
---

# Release Management

## Semantic Versioning

```bash
# Tag releases
git tag -a v1.2.0 -m "Release version 1.2.0"
git push origin v1.2.0

# Create release branch
git checkout -b release/1.2.0

# Hotfix workflow
git checkout -b hotfix/security-patch release/1.2.0
```

---
transition: slide-left
---

# Team Workflow Example

## Feature Development

```bash
# Day 1: Start feature
git checkout -b feature/employee-search
# ... make changes ...
git push -u origin feature/employee-search

# Day 2: Continue work
git pull origin feature/employee-search
# ... more changes ...
git push

# Day 3: Ready for review
# Create pull request
# Address review feedback
# Merge when approved
```

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- Use feature branches for all development
- Write clear commit messages
- Create detailed pull requests
- Review code thoroughly
- Keep main branch stable

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Microservices

Building distributed systems with Spring Boot