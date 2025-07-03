# Java Skills Teaching Project

A comprehensive multi-module Gradle project designed as the foundation for a Java skills video series. This project demonstrates core Java concepts through a progressive Employee Management System that evolves from basic syntax to advanced design patterns.

## Project Overview

This project is structured as a Gradle multi-module build with Java 21, designed to teach 26 essential Java topics through connected, practical examples. Each module builds upon previous concepts while maintaining a cohesive narrative around employee management.

## 📊 Slides and Presentations

The project includes Slidev-powered slides for enhanced teaching:

- **slides.md** - Interactive presentations covering naming conventions, string formatting, and operator precedence
- **package.json** - Slidev dependencies and build scripts

### Running Slides
```bash
npm install          # Install Slidev dependencies
npm run dev         # Start development server with hot reload
npm run build       # Build static presentation
npm run export      # Export to PDF
```

## Module Structure

### 📚 **foundations** - Basic Java Concepts (Videos 1-7)
**Status: ✅ Complete & Tested**

- **NamingConventions.java** - Proper Java naming conventions with good/bad examples
- **StringFormatting.java** - String concatenation, formatting, text blocks, StringBuilder, String.join(), StringJoiner, SLF4J logging
- **OperatorPrecedenceTest.java** - JUnit tests demonstrating operator precedence rules with arithmetic, boolean, assignment, ternary, increment/decrement, modulus, and Math.pow() examples
- **EmployeeInput.java** - Scanner input with validation and error handling
- **EmployeeRoster.java** - 2D arrays, nested loops, department management
- **EmployeeFileWriter.java** - File I/O operations (CSV, JSON, Apache Commons IO)

### 🏗️ **oop-core** - Object-Oriented Programming (Videos 8-12)
**Status: ✅ Complete & Tested**

- **Employee.java** - Encapsulation, validation, business logic
- **Address.java** - Composition example with proper validation
- **Department.java** - Has-a relationships, collections, business methods
- **ModernJavaFeatures.java** - var keyword, records, pattern matching, text blocks

### 🎨 **design-patterns** - Design Patterns (Videos 13-15)
**Status: ✅ Complete & Mostly Tested**

- **DatabaseConnection.java** - Thread-safe Singleton pattern
- **SalaryCalculator.java** - Strategy pattern (Hourly, Salaried, Commission)
- **EmployeeFactory.java** - Factory pattern (Developer, Manager, Intern, Sales)

### 🔧 **solid-principles** - SOLID Principles (Videos 16-17)
**Status: ⚠️ Partial Implementation**

- **EmployeeService.java** - Single Responsibility Principle examples
- **OpenClosedPrinciple.java** - Extension without modification examples

### 🔒 **security** - Security Concepts (Videos 22-23)
**Status: ⚠️ Needs Dependencies**

- **InputValidation.java** - Input validation, XSS prevention, SQL injection basics

### 🌐 **web-services** - Ready for Spring Boot Implementation
**Status: 📋 Structure Only**

### 🚀 **advanced-systems** - Ready for Microservices/Reactive
**Status: 📋 Structure Only**

### 🎯 **final-project** - Integration Project
**Status: 📋 Structure Only**

## Technology Stack (2025 Latest)

### Core Framework Versions
- **Java**: 21 (LTS)
- **Spring Boot**: 3.5.3 (Latest 2025)
- **Gradle**: 8.14.2 (June 2025)

### Testing with BOMs (Best Practice)
```gradle
// Modern dependency management using BOMs
testImplementation platform('org.junit:junit-bom:5.11.0')
testImplementation platform('org.mockito:mockito-bom:5.18.0')
testImplementation 'org.junit.jupiter:junit-jupiter'
testImplementation 'org.mockito:mockito-core'
```

### Key Libraries
- **Jackson**: 2.19.1 (with jackson-bom)
- **SLF4J**: 2.0.17
- **Logback**: 1.5.18
- **AssertJ**: 3.26.3
- **Apache Commons IO**: 2.19.0

## Getting Started

### Prerequisites
- Java 21 or higher
- Gradle 8.14+ (or use included wrapper)

### Running the Project

```bash
# Build all modules
gradle build

# Run specific examples
gradle runExample -Pmodule=foundations -PmainClass=NamingConventions
gradle runExample -Pmodule=oop-core -PmainClass=ModernJavaFeatures
gradle runExample -Pmodule=design-patterns -PmainClass=StrategyPatternDemo

# Run tests
gradle test

# Run specific module tests
gradle :foundations:test
gradle :oop-core:test
gradle :design-patterns:test
```

### Example Outputs

**Foundations Module:**
```bash
gradle runExample -Pmodule=foundations -PmainClass=StringFormatting
```

**OOP Core Module:**
```bash
gradle runExample -Pmodule=oop-core -PmainClass=ModernJavaFeatures
```

**Design Patterns Module:**
```bash
gradle runExample -Pmodule=design-patterns -PmainClass=DatabaseConnection
```

## Learning Progression

### Arc 1: Foundations to OOP (Videos 1-12)
Start with a simple Employee class that evolves:
- Videos 1-3: Basic syntax with employee examples
- Video 4: Scanner input for employee data
- Videos 5-6: Employee roster using 2D arrays
- Video 7: File I/O for employee data persistence
- Videos 8-9: Proper encapsulation and composition
- Videos 10-11: Modern Java features (var, records)
- Video 12: Reflection examples

### Arc 2: Design Patterns (Videos 13-15)
Enhance the system with patterns:
- **Singleton**: Database connection manager
- **Strategy**: Different salary calculation algorithms
- **Factory**: Create different employee types

### Arc 3: SOLID Principles (Videos 16-17)
Refactor for better design:
- **SRP**: Split monolithic classes
- **OCP**: Add new employee types without modification

### Arc 4: Production Ready (Videos 18-26)
Transform into enterprise application:
- Code quality, logging, testing
- Web services and REST APIs
- Security and validation
- Microservices architecture

## Key Features

### 🔄 **Connected Learning**
- Each concept builds on previous examples
- Consistent Employee/Department theme throughout
- Natural progression from simple to complex

### 💻 **Modern Java (2025)**
- Java 21 features (records, pattern matching, text blocks)
- Latest Spring Boot 3.5.3 patterns
- Modern dependency management with BOMs
- Best practices and defensive programming

### 🧪 **Comprehensive Testing**
- JUnit 5.11 with BOM management
- AssertJ fluent assertions
- Mockito 5.18 with BOM
- High test coverage for core modules

### 🏗️ **Enterprise Patterns**
- SOLID principles demonstrations
- Design patterns with practical implementations
- Scalable architecture examples

## Test Results

### ✅ Passing Tests (31 total)
- **foundations**: 3/3 tests passing (100%)
- **oop-core**: 13/13 tests passing (100%)
- **design-patterns**: 12/15 tests passing (80%)

### Current Issues
- **solid-principles**: Class naming conflicts (easy fix)
- **security**: Jakarta validation imports needed (easy fix)

## Dependencies & BOMs

### Core Dependencies (All Modules)
```gradle
// Using BOMs for consistent version management
testImplementation platform('org.junit:junit-bom:5.11.0')
testImplementation platform('org.mockito:mockito-bom:5.18.0')
implementation platform('com.fasterxml.jackson:jackson-bom:2.19.1')
```

### Module-Specific Dependencies
- **foundations**: Apache Commons IO 2.19.0
- **oop-core**: Jackson (via BOM)
- **design-patterns**: H2 Database 2.3.232
- **security**: Hibernate Validator 9.0.1, Spring Security Crypto
- **web-services**: Spring Boot 3.5.3, WebFlux
- **advanced-systems**: Spring Boot 3.5.3, Reactor

## Video Production Notes

### Ready for Recording (2025 Standards)
1. **foundations** - All examples working with latest dependencies
2. **oop-core** - Complete OOP progression with modern Java 21
3. **design-patterns** - Pattern implementations using current best practices

### Content Highlights
- **Latest 2025 technology stack** - Students learn current industry standards
- **Modern dependency management** - BOM usage and best practices
- **Clear before/after comparisons** - Progressive complexity demonstration
- **Real-world problem solutions** - Practical implementations
- **Modern Java feature demonstrations** - Records, pattern matching, text blocks

### Suggested Recording Order
1. Start with `NamingConventions` for immediate engagement
2. Progress through `StringFormatting` and `OperatorPrecedence`
3. Build complexity with `EmployeeInput` and file operations
4. Transition to OOP with encapsulation examples
5. Showcase modern features with records and pattern matching
6. Demonstrate design patterns with practical implementations

## Modern Development Practices

### BOM (Bill of Materials) Usage
The project demonstrates modern dependency management:
```gradle
// Benefits of BOMs:
// - Version consistency across related dependencies
// - Simplified dependency management
// - Conflict resolution
// - Easy ecosystem updates
```

### 2025 Technology Choices
- **Spring Boot 3.5.3** - Latest features and security updates
- **JUnit 5.11** - Enhanced Java 21+ support
- **Modern validation** - Jakarta EE 11 compliance
- **Latest tooling** - Gradle 8.14.2 with Java 24 support

## Contributing

This project is designed for educational content creation. Each module should:
- Build upon previous concepts
- Include comprehensive examples
- Provide clear learning objectives
- Maintain the employee management theme
- Use latest 2025 best practices

## License

Educational content for O'Reilly Java skills video series.