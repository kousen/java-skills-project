# Git Workflow Examples for Java Projects

## Workflow Models Overview

### Fork-Based Workflow (Open Source / External Contributors)
**Use when:** Contributing to projects you don't own, open source projects, or strict security environments.
- Contributors fork the repository
- No direct push access to main repository
- All changes via Pull Requests from forks
- Maintainers have full control over what gets merged

### Branch-Based Workflow (Team Development)
**Use when:** Working on a team project with trusted collaborators.
- Team members have push access to the main repository
- Create feature branches directly in the main repo
- Pull Requests used for code review, not access control
- Faster iteration, less overhead

---

## Fork-Based Workflow (Recommended for Open Source)

### Initial Setup - Fork and Clone
```bash
# 1. Fork the repository on GitHub (click "Fork" button)
# 2. Clone YOUR fork (not the original repo)
git clone https://github.com/YOUR-USERNAME/java-skills-project.git
cd java-skills-project

# 3. Add the original repo as "upstream" remote
git remote add upstream https://github.com/ORIGINAL-OWNER/java-skills-project.git

# 4. Verify your remotes
git remote -v
# origin    https://github.com/YOUR-USERNAME/java-skills-project.git (fetch)
# origin    https://github.com/YOUR-USERNAME/java-skills-project.git (push)
# upstream  https://github.com/ORIGINAL-OWNER/java-skills-project.git (fetch)
# upstream  https://github.com/ORIGINAL-OWNER/java-skills-project.git (push)
```

### Starting a new feature
```bash
# Always sync with upstream first
git checkout main
git fetch upstream
git merge upstream/main
git push origin main

# Create and switch to feature branch
git checkout -b feature/employee-search
git push -u origin feature/employee-search
```

### Working on the feature
```bash
# Make your changes
# Edit files: EmployeeService.java, EmployeeController.java

# Check what changed
git status
git diff

# Stage specific files
git add web-services/src/main/java/com/oreilly/webservices/EmployeeService.java
git add web-services/src/main/java/com/oreilly/webservices/EmployeeController.java

# Commit with descriptive message
git commit -m "Add employee search functionality with pagination

- Implement search by name, department, and salary range
- Add pagination support for large result sets
- Include unit tests for search methods
- Update API documentation

Fixes #123"

# Push changes
git push origin feature/employee-search
```

### Creating a Pull Request
```bash
# Push your feature branch to YOUR fork
git push origin feature/employee-search

# Create PR from your fork to upstream main
# Using GitHub CLI (if available)
gh pr create --repo ORIGINAL-OWNER/java-skills-project \
             --title "Add employee search functionality" \
             --body "## Summary
Implements comprehensive employee search with pagination support.

## Changes
- New search endpoint: GET /api/employees/search
- Query parameters: name, department, minSalary, maxSalary
- Pagination with page/size parameters
- Comprehensive test coverage

## Testing
- Unit tests for service layer
- Integration tests for REST endpoints
- Manual testing with various search criteria

Fixes #123"

# Or create PR through GitHub web interface:
# 1. Go to the original repository
# 2. Click "New Pull Request"
# 3. Click "compare across forks"
# 4. Select your fork and branch as the source
```

### Code Review Process
```bash
# Maintainer/reviewer can check out the PR branch to test locally
gh pr checkout 123  # Using GitHub CLI

# Or manually:
git fetch upstream pull/123/head:pr-123
git checkout pr-123

# Run tests
./gradlew test

# Check code quality
./gradlew check

# After review feedback, contributor makes changes on their fork
git add .
git commit -m "Address code review feedback

- Extract search criteria to separate class
- Add input validation for salary ranges
- Improve error messages"

# Push to your fork - PR automatically updates
git push origin feature/employee-search
```

### Merging the Pull Request
```bash
# After approval, contributor can sync with latest upstream
git checkout feature/employee-search
git fetch upstream
git rebase upstream/main

# Push the rebased branch (use --force-with-lease for safety)
git push --force-with-lease origin feature/employee-search

# Maintainer merges through GitHub UI (recommended)
# This preserves PR history and attribution

# After merge, contributor cleans up their fork
git checkout main
git fetch upstream
git merge upstream/main
git push origin main
git branch -d feature/employee-search
git push origin --delete feature/employee-search
```

---

## Branch-Based Workflow (Team Development)

### Starting a new feature (Team Member)
```bash
# Make sure you're on main and up to date
git checkout main
git pull origin main

# Create and switch to feature branch
git checkout -b feature/employee-search
git push -u origin feature/employee-search
```

### Working on the feature
```bash
# Make your changes (same as fork-based workflow)
# Edit files, commit, and push to origin

# The key difference: you're pushing to the main repository
git push origin feature/employee-search
```

### Creating a Pull Request (Same Repository)
```bash
# Create PR within the same repository
gh pr create --title "Add employee search functionality" --body "..."

# Or through GitHub web interface:
# 1. Go to the repository
# 2. Click "New Pull Request"
# 3. Select your feature branch
# 4. Target branch is usually 'main'
```

### After Merge Cleanup
```bash
# Clean up after merge (simpler since same repo)
git checkout main
git pull origin main
git branch -d feature/employee-search
git push origin --delete feature/employee-search
```

---

## Hotfix Workflow

### Emergency production fix
```bash
# Create hotfix branch from main
git checkout main
git pull origin main
git checkout -b hotfix/security-vulnerability
git push -u origin hotfix/security-vulnerability

# Or use alias
git hotfix security-vulnerability

# Make the critical fix
# Edit: CryptographicAPIs.java

git add security/src/main/java/com/oreilly/security/CryptographicAPIs.java
git commit -m "Fix security vulnerability in password validation

- Strengthen password complexity requirements
- Fix regex pattern that allowed weak passwords
- Add additional validation tests

Fixes SECURITY-456"

# Push and create urgent PR
git push origin hotfix/security-vulnerability
gh pr create --title "URGENT: Fix security vulnerability" --label "security,hotfix"
```

### Fast-track review and deployment
```bash
# After urgent review and approval
git checkout main
git merge hotfix/security-vulnerability
git tag v1.2.1
git push origin main
git push origin v1.2.1

# Deploy to production immediately
# Clean up hotfix branch
git branch -d hotfix/security-vulnerability
git push origin --delete hotfix/security-vulnerability
```

## Release Workflow

### Preparing a release
```bash
# Create release branch
git checkout develop
git pull origin develop
git checkout -b release/v2.0.0

# Update version numbers
# Edit: build.gradle, README.md, CHANGELOG.md

git add .
git commit -m "Prepare release v2.0.0

- Update version to 2.0.0
- Update CHANGELOG with new features
- Update documentation"

# Final testing and bug fixes
./gradlew build
./gradlew test

# Any last-minute fixes
git add .
git commit -m "Fix minor issues found in release testing"

# Merge to main
git checkout main
git merge release/v2.0.0
git tag -a v2.0.0 -m "Release version 2.0.0

Major features:
- Employee search functionality
- Department management
- Performance improvements
- Security enhancements"

# Merge back to develop
git checkout develop
git merge release/v2.0.0

# Push everything
git push origin main
git push origin develop
git push origin v2.0.0

# Clean up release branch
git branch -d release/v2.0.0
```

## Conflict Resolution

### Handling merge conflicts
```bash
# When rebasing or merging causes conflicts
git status
# Shows conflicted files

# Edit conflicted files
# Look for conflict markers:
# <<<<<<< HEAD
# Your changes
# =======
# Their changes
# >>>>>>> commit-hash

# For Java files, common conflicts:
# - Import statements
# - Method implementations
# - Configuration changes

# Example conflict in EmployeeService.java:
```

```java
public class EmployeeService {
// <<<<<<< HEAD
    public Employee findByEmail(String email) {
        // Your implementation
        return employeeRepository.findByEmailIgnoreCase(email);
    }
// =======
//    public Employee findByEmail(String email) {
//        // Their implementation
//        return employeeRepository.findByEmail(email.toLowerCase());
//    }
// >>>>>>> feature/email-search
}
```

```bash
# Resolve by choosing the best approach or combining both
# After editing, mark as resolved
git add web-services/src/main/java/com/oreilly/webservices/EmployeeService.java

# Continue the rebase or merge
git rebase --continue
# or
git merge --continue

# Verify the resolution
./gradlew test
```

## Advanced Git Commands

### Interactive rebase to clean up commits
```bash
# Clean up last 3 commits before pushing
git rebase -i HEAD~3

# In the editor, choose actions:
# pick abc1234 Add employee search
# squash def5678 Fix typo in search
# squash ghi9012 Add tests for search

# This combines 3 commits into 1 clean commit
```

### Cherry-picking commits
```bash
# Apply specific commit from another branch
git cherry-pick abc1234

# Cherry-pick a range of commits
git cherry-pick start-commit..end-commit

# Cherry-pick with edit (to modify the commit)
git cherry-pick -e abc1234
```

### Finding problematic commits
```bash
# Find when a bug was introduced
git bisect start
git bisect bad          # Current commit is bad
git bisect good v1.0.0  # Version 1.0.0 was good

# Git will checkout commits for you to test
# After each test:
git bisect good  # if tests pass
git bisect bad   # if tests fail

# Git will identify the problematic commit
git bisect reset  # when done
```

### Stashing work in progress
```bash
# Save current work without committing
git stash push -m "WIP: employee search implementation"

# Switch to different branch for urgent fix
git checkout hotfix/urgent-bug

# After fixing, return to original work
git checkout feature/employee-search
git stash pop  # or git stash apply
```

## Commit Message Best Practices

### Good commit message format
```
Type: Brief description (50 chars or less)

Detailed explanation of what and why (wrap at 72 chars).
Include business context and technical details.

- Bullet points for multiple changes
- Reference issue numbers
- Explain any breaking changes

Fixes #123
Closes #456
```

### Commit types
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Formatting, missing semicolons, etc.
- `refactor`: Code change that neither fixes a bug nor adds feature
- `test`: Adding missing tests
- `chore`: Updating build tasks, package manager configs, etc.

### Examples
```bash
git commit -m "feat: Add employee search with pagination

Implement comprehensive search functionality allowing users to
find employees by name, department, and salary range. Added
pagination support to handle large datasets efficiently.

- New REST endpoint: GET /api/employees/search
- Query parameters: name, department, minSalary, maxSalary, page, size
- Service layer methods with proper error handling
- Comprehensive unit and integration tests

Fixes #123"

git commit -m "fix: Resolve null pointer exception in employee service

Handle case where department is null when calculating
employee statistics. Add null checks and appropriate
default values.

Fixes #456"

git commit -m "refactor: Extract search criteria into separate class

Improve code organization by moving search parameters
into a dedicated SearchCriteria class. This makes the
service methods cleaner and more maintainable.

No functional changes."
```