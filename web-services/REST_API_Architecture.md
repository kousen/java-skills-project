# REST API Three-Layered Architecture

## Architecture Overview

This diagram shows our clean three-layered architecture for the Employee REST API in the web-services module.

```mermaid
graph TD
    subgraph "Client Layer"
        Client["HTTP Client
        Postman, Browser, etc."]
    end

    subgraph "Presentation Layer"
        Controller["EmployeeController
        @RestController
        /api/employees"]
        ExceptionHandler["GlobalExceptionHandler
        @RestControllerAdvice
        ProblemDetail responses"]
    end

    subgraph "Business Layer"
        Service["EmployeeService
        @Service
        Business logic & validation"]
        Exception["EmployeeNotFoundException
        Custom business exceptions"]
    end

    subgraph "Data Layer"
        Repository["EmployeeRepository
        @Repository
        Data access operations"]
        Storage["ConcurrentHashMap
        In-memory storage"]
    end

    subgraph "Domain Model"
        Record["Employee Record
        Immutable data
        Helper methods"]
    end

    %% Client to Controller
    Client -->|"HTTP Requests
    GET, POST, PUT, DELETE"| Controller
    Controller -->|"HTTP Responses
    JSON + Status Codes"| Client

    %% Controller to Service
    Controller -->|"Method calls
    processNewHire()
    giveRaise()"| Service
    Service -->|"Employee objects
    Business results"| Controller

    %% Service to Repository
    Service -->|"CRUD operations
    save(), findById()
    findAll()"| Repository
    Repository -->|"Employee objects
    Optional Employee"| Service

    %% Repository to Storage
    Repository -->|"Read/Write"| Storage

    %% Exception flow
    Service -->|"Throws"| Exception
    Exception -->|"Caught by"| ExceptionHandler
    ExceptionHandler -->|"ProblemDetail"| Client

    %% Domain model usage
    Controller -.->|"Uses"| Record
    Service -.->|"Uses"| Record
    Repository -.->|"Uses"| Record

    %% Styling with dark theme compatibility
    classDef controller fill:#2196F3,stroke:#0D47A1,stroke-width:2px,color:#FFFFFF
    classDef service fill:#9C27B0,stroke:#4A148C,stroke-width:2px,color:#FFFFFF
    classDef repository fill:#4CAF50,stroke:#1B5E20,stroke-width:2px,color:#FFFFFF
    classDef domain fill:#FF9800,stroke:#E65100,stroke-width:2px,color:#FFFFFF
    classDef client fill:#E91E63,stroke:#880E4F,stroke-width:2px,color:#FFFFFF

    class Controller,ExceptionHandler controller
    class Service,Exception service
    class Repository,Storage repository
    class Record domain
    class Client client
```

## Key Architectural Principles

### 1. **Separation of Concerns**
- **Controller Layer**: HTTP/REST concerns only
- **Service Layer**: Business logic and validation
- **Repository Layer**: Data access operations

### 2. **Dependency Direction**
- Controllers depend on Services
- Services depend on Repositories
- No upward dependencies (Repository doesn't know about Service)

### 3. **Modern Java Features**
- **Records**: Immutable data with helper methods (`withId()`, `withSalary()`)
- **Constructor Injection**: Clean dependency injection pattern
- **Business Constants**: Centralized in service layer

### 4. **Exception Handling**
- **Custom Exceptions**: `EmployeeNotFoundException` for business errors
- **Global Handler**: `@RestControllerAdvice` with `ProblemDetail` (RFC 7807)
- **Proper HTTP Status**: 404 for not found, 400 for validation errors

### 5. **Testing Strategy**
- **Repository**: Pure unit tests (no Spring context)
- **Service**: Spring Boot integration tests with `@MockitoBean`
- **Controller**: Web layer tests with `@WebMvcTest` and `MockMvc`

## Business Operations

Our service layer includes sophisticated business operations beyond simple CRUD:

- `processNewHire()` - Salary validation and employee creation
- `giveRaise()` - Salary increase with business rules
- `giveStandardRaise()` - Standard 5% salary increase
- `transferEmployee()` - Department transfer with validation
- `findHighPerformers()` - Query employees above performance threshold
- `calculateDepartmentSalaryExpense()` - Business reporting

## Data Flow Example

1. **Client** sends `POST /api/employees` with JSON
2. **Controller** validates request, calls `employeeService.processNewHire()`
3. **Service** validates business rules (salary range), calls `repository.save()`
4. **Repository** generates ID, stores in `ConcurrentHashMap`
5. **Response** flows back through layers with proper HTTP status

This architecture ensures maintainability, testability, and clear separation of responsibilities.