# Git Conflict Demo Branches

This document explains the branch setup used for demonstrating realistic merge conflicts in the Git collaboration video.

## Branch Structure

### `main` branch
- Contains a `findEmployees` method in `EmployeeController.java`
- Uses `/find` endpoint with simple search functionality
- This is the "upstream" change that conflicts with the feature branch

### `feature/employee-search-demo` branch
- Contains a `searchEmployees` method in `EmployeeController.java` 
- Uses `/search` endpoint with pagination support
- This is the "feature work" that will conflict when rebased against main

## Demonstrating the Conflict

When recording the video:

1. **Start on the feature branch**: `git checkout feature/employee-search-demo`
2. **Fetch upstream changes**: `git fetch upstream`
3. **Attempt rebase**: `git rebase upstream/main`
4. **Conflict occurs**: Both methods try to exist in the same file location
5. **Resolve conflict**: Choose the best implementation (demonstrated in script)
6. **Continue rebase**: `git rebase --continue`

## Conflict Resolution Strategy

The script demonstrates:
- Keeping the more comprehensive pagination implementation from the feature branch
- Adopting the simpler `/find` endpoint name from main
- Combining the best of both approaches in the resolution

This provides a realistic example of how developers balance feature requirements with upstream changes during collaboration.

## Branch Maintenance

These demo branches should be maintained for consistent video recording:
- Do not delete `feature/employee-search-demo` 
- Keep the conflict scenario intact for future recordings
- Update both branches if the base `EmployeeController` structure changes significantly