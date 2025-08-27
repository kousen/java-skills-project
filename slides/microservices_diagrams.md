## Microservices Diagrams

---

### 1. Monolith vs. Microservices

```mermaid
graph TD
    subgraph Monolith
        A[UI] --> B(Business Logic)
        B --> C[Database]
    end

    subgraph Microservices
        D[UI] --> E(Employee Service)
        D --> F(Department Service)
        D --> G(Payroll Service)
        E --> H[Employee DB]
        F --> I[Department DB]
        G --> J[Payroll DB]
    end

    style Monolith fill:#f9f,stroke:#333,stroke-width:2px,color:#000
    style Microservices fill:#ccf,stroke:#333,stroke-width:2px,color:#000
    
    %% Make arrows more visible
    linkStyle default stroke:#333,stroke-width:2px
```

---

### 2. Service Discovery Flow

```mermaid
sequenceDiagram
    participant ServiceA
    participant EurekaServer
    participant ServiceB

    ServiceA->>EurekaServer: Register (ServiceA_IP:Port)
    ServiceB->>EurekaServer: Query for ServiceA
    EurekaServer-->>ServiceB: ServiceA_IP:Port
    ServiceB->>ServiceA: Call ServiceA API
```

---

### 3. Inter-Service Communication

```mermaid
sequenceDiagram
    participant Client
    participant ApiGateway
    participant EmployeeService
    participant LoadBalancer
    participant DepartmentService

    Client->>ApiGateway: Request Employee Details
    ApiGateway->>EmployeeService: Get Employee(ID)
    EmployeeService->>LoadBalancer: Request Department(ID)
    LoadBalancer->>DepartmentService: Get Department(ID)
    DepartmentService-->>LoadBalancer: Department Details
    LoadBalancer-->>EmployeeService: Department Details
    EmployeeService-->>ApiGateway: Employee + Department Details
    ApiGateway-->>Client: Employee + Department Details
```

---

### 4. Circuit Breaker States

```mermaid
graph TD
    Start([Start]) --> Closed[Closed State]
    Closed --> Open[Open State]
    Open --> HalfOpen[Half-Open State]
    HalfOpen --> Closed
    HalfOpen --> Open
    
    style Closed fill:#90EE90,stroke:#333,stroke-width:2px,color:#000
    style Open fill:#FFB6C1,stroke:#333,stroke-width:2px,color:#000
    style HalfOpen fill:#FFE4B5,stroke:#333,stroke-width:2px,color:#000
    style Start fill:#87CEEB,stroke:#333,stroke-width:2px,color:#000
```

---

### 5. API Gateway Routing

```mermaid
graph TD
    A[Client] --> B(API Gateway)
    B --> C(Employee Service)
    B --> D(Department Service)
    B --> E(Payroll Service)
    C --> F[Employee DB]
    D --> G[Department DB]
    E --> H[Payroll DB]
```
