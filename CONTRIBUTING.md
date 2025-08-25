# Contributing to Java Skills Project

First off, thank you for considering contributing to the Java Skills Project! This project is designed to help developers learn modern Java development practices through hands-on examples.

## Table of Contents
- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Getting Started](#getting-started)
- [Development Guidelines](#development-guidelines)
- [Pull Request Process](#pull-request-process)
- [Style Guidelines](#style-guidelines)
- [Community](#community)

## Code of Conduct

This project and everyone participating in it is governed by our [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to [ken.kousen@kousenit.com](mailto:ken.kousen@kousenit.com).

## How Can I Contribute?

### Reporting Bugs
Before creating bug reports, please check the existing issues as you might find that the problem has already been reported. When creating a bug report, please include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps to reproduce the problem**
- **Describe the behavior you observed and what you expected**
- **Include details about your environment** (Java version, OS, etc.)

### Suggesting Enhancements
Enhancement suggestions are welcome! Please:
- **Use a clear and descriptive title**
- **Provide a detailed description of the suggested enhancement**
- **Explain why this enhancement would be useful**
- **Consider if this fits with the educational goals of the project**

### Contributing Code
Look for issues labeled with:
- `good first issue` - Good for newcomers
- `help wanted` - Extra attention is needed
- `enhancement` - New feature or improvement
- `bug` - Something isn't working

## Getting Started

### Prerequisites
- Java 21 or higher
- Git for version control
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

### Fork and Clone
1. Fork the repository on GitHub
2. Clone your fork locally:
   ```bash
   git clone https://github.com/YOUR-USERNAME/java-skills-project.git
   cd java-skills-project
   ```
3. Add the original repository as upstream:
   ```bash
   git remote add upstream https://github.com/kousen/java-skills-project.git
   ```

### Build and Test
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run a specific example
./gradlew runExample -Pmodule=foundations -PmainClass=com.oreilly.javaskills.NamingConventions
```

## Development Guidelines

### Project Structure
- `foundations/` - Core Java concepts (Topics 1-7, 19-26)
- `oop-core/` - Object-oriented programming (Topics 8-12)
- `design-patterns/` - Essential patterns (Topics 13-15)
- `solid-principles/` - Design principles (Topics 16-17)
- `web-services/` - REST APIs and layered architecture (Topics 20-21)
- `security/` - Security and validation (Topics 22-23)
- `microservices/` - Spring Cloud microservices (Topic 25)
- `reactive/` - WebFlux reactive programming (Topic 26)

### Coding Standards
- **Use Java 21 features** where appropriate (records, text blocks, var, pattern matching)
- **Follow existing code style** - we use standard Java conventions
- **Write meaningful variable and method names**
- **Add JavaDoc for public methods and classes**
- **Include comprehensive tests** for new functionality
- **Follow the Employee Management System theme** for consistency

### Testing Requirements
- **Unit tests** for all business logic
- **Integration tests** for complex interactions
- **Tests must pass** before submitting PR
- **Aim for good test coverage** of new code

### Documentation
- **Update README** if you change functionality
- **Add JavaDoc** for public APIs
- **Include code comments** for complex logic
- **Update relevant documentation** in the module

## Pull Request Process

### Before Submitting
1. **Sync with upstream main**:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```
2. **Run all tests**: `./gradlew test`
3. **Build the project**: `./gradlew build`
4. **Review your changes**: Make sure they align with project goals

### Creating the Pull Request
1. **Push your branch** to your fork
2. **Create a pull request** from your fork to the upstream repository
3. **Fill out the PR template** completely - it helps reviewers understand your changes
4. **Link to related issues** using keywords like "Fixes #123"

### PR Requirements
- **Descriptive title and summary**
- **All tests pass** (GitHub Actions will verify)
- **Code follows style guidelines**
- **Documentation updated** if needed
- **No merge conflicts** with main branch

### Review Process
- **Be responsive** to reviewer feedback
- **Make requested changes** promptly
- **Ask questions** if feedback is unclear
- **Be patient** - reviews take time
- **Thank reviewers** for their time and effort

## Style Guidelines

### Java Code Style
- **Use 4 spaces** for indentation (no tabs)
- **Follow standard Java naming conventions**:
  - Classes: `PascalCase`
  - Methods/variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Packages: `lowercase.with.dots`
- **Limit lines to 120 characters**
- **Use meaningful names** over short abbreviations
- **Organize imports** and remove unused ones

### Modern Java Features
- **Use `var`** for local variables where type is obvious
- **Use text blocks** for multi-line strings
- **Use records** for simple data carriers
- **Use enhanced switch expressions** where appropriate
- **Prefer `List.of()`, `Set.of()`** over legacy collection creation

### Commit Messages
Follow the conventional commit format:
```
type: brief description

Detailed explanation of what and why, not how.

- List specific changes
- Reference issue numbers
- Explain business context

Fixes #123
```

**Types**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

### Git Workflow
- **Create feature branches** from main: `feature/employee-search`
- **Keep branches focused** on single features or fixes
- **Rebase before submitting** PR to keep history clean
- **Use `--force-with-lease`** if force push is needed

## Community

### Getting Help
- **Check existing documentation** first
- **Search closed issues** for similar problems
- **Ask questions in issues** - we're happy to help
- **Join discussions** about project direction

### Recognition
Contributors are recognized in:
- **Git commit history** - permanent record of your contributions
- **Pull request discussions** - community recognition
- **Potential employment opportunities** - real open source experience

## Educational Goals

This project aims to teach:
- **Modern Java development practices** (Java 21 features)
- **Professional software development** (testing, documentation, code review)
- **Real-world collaboration** (Git workflows, open source contribution)
- **Industry standards** (Spring Boot, Gradle, CI/CD)

When contributing, consider how your changes help achieve these educational objectives.

---

## Questions?

Don't hesitate to reach out if you have questions! You can:
- **Open an issue** for general questions
- **Start a discussion** for broader topics
- **Email the maintainer** at ken.kousen@kousenit.com

Thank you for contributing to Java education! 🎉