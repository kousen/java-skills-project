# Video Script: Creating REST Services with Spring Boot

**Goal:** 21. Build RESTful web services using Spring Boot with proper layered architecture.
**Target Duration:** 5-6 minutes

---

### SCENE 1: Introduction (0:00 - 0:30)

**(Show Slide 1: Title Slide - "Creating REST Services with Spring Boot")**

**Host:**
"Welcome back! Last time, we learned how to consume REST APIs. Now let's flip the script and create our own REST services. We'll build a comprehensive, production-ready API with proper layered architecture using Spring Boot."

---

### SCENE 2: Why Spring Boot? (0:30 - 1:00)

**(Show Slide 2: Why Spring Boot? - The Industry Standard)**

**Host:**
"Spring Boot is the industry standard for a reason. It follows convention over configuration, includes production-ready features built-in, and provides an exceptional developer experience."

**(Show Slide 3: Developer Experience)**

**Host:**
"Minimal boilerplate code, embedded server included, auto-configuration magic. It's like having a senior architect design your application structure for you."

---

### SCENE 3: Project Structure (1:00 - 1:30)

**(Show Slide 4: Spring Boot Setup - Dependencies We Need)**

**Host:**
"We're working with a multi-module Gradle project. Our web-services module focuses on REST APIs, with Spring Boot Web starter and validation dependencies already configured."

**(Show Slide 5: Main Application Class)**

**Host:**
"Here's our WebServicesApplication. One @SpringBootApplication annotation gives us component scanning, auto-configuration, and an embedded Tomcat server. That's it - we're ready to build APIs."

---

### SCENE 4: Layered Architecture (1:30 - 2:15)

**(Show Slide 6: Layered Architecture - Three-Tier Design)**

**Host:**
"Instead of building a simple controller, we'll create proper layered architecture. Controller layer handles REST endpoints, Service layer contains business logic and validation, Repository layer manages data access."

**(Show Slide 7: Repository Layer)**

**Host:**
"Our EmployeeRepository uses @Repository annotation and handles data persistence. Notice we're using a modern Java record for Employee, with immutable data and helper methods like withId()."

---

### SCENE 5: Service Layer with Business Logic (2:15 - 2:45)

**(Show Slide 8: Service Layer - Business Logic & Validation)**

**Host:**
"The Service layer is where the magic happens. Our EmployeeService contains business constants, validation logic, and operations like processNewHire() and giveRaise(). Notice how it validates salary ranges and handles business rules."

**(Show Slide 9: Transaction Management - Professional Service Layer Patterns)**

**Host:**
"We've also added professional transaction boundaries with @Transactional annotations. The service class uses @Transactional for write operations and @Transactional(readOnly = true) for queries. This demonstrates proper enterprise patterns even with in-memory storage."

**(Show Slide 10: Transaction Configuration - Enabling Transaction Management)**

**Host:**
"Our main application class uses @EnableTransactionManagement to activate Spring's transaction infrastructure. This shows how to structure applications for easy database migration later - just change the storage backend and your transaction boundaries are already defined."

**(Show Slide 11: Controller Layer - REST Endpoints)**

**Host:**
"The Controller layer stays focused on HTTP concerns. Look at the constructor injection - Spring automatically wires the EmployeeService. The controller methods handle ResponseEntity for proper HTTP status codes and headers."

---

### SCENE 6: Modern Java Features (2:45 - 3:15)

**(Show Slide 12: Employee Record - Modern Immutable Data)**

**Host:**
"We're using Java records for immutable data. No getters, setters, or equals/hashCode boilerplate. The withId() and withSalary() helper methods create new instances for updates - that's immutable design done right."

**(Show Slide 13: Request Validation - Business Logic Validation)**

**Host:**
"Instead of bean validation annotations, we implement business logic validation in the service layer. This gives us complete control over validation rules and custom error messages."

---

### SCENE 7: Exception Handling (3:15 - 3:45)

**(Show Slide 14: Global Exception Handler - Modern Error Handling with ProblemDetail)**

**Host:**
"Modern exception handling uses ProblemDetail from RFC 7807. Our GlobalExceptionHandler catches EmployeeNotFoundException and returns structured error responses with proper HTTP status codes."

---

### SCENE 8: Business Logic Endpoints (3:45 - 4:15)

**(Show Slide 15: Business Logic Endpoints - Beyond Simple CRUD)**

**Host:**
"This is where our architecture shines. We're not just doing basic CRUD - we have business operations like giveRaise(), giveStandardRaise(), and getHighPerformers(). Each endpoint encapsulates real business logic."

---

### SCENE 9: Testing Strategy (4:15 - 4:45)

**(Show Slide 16: Testing REST APIs - Three-Layer Testing Strategy)**

**Host:**
"Our layered architecture enables comprehensive testing. Repository tests are pure unit tests with no Spring context. Service tests use Spring Boot integration with @MockitoBean. Controller tests use @WebMvcTest for the web layer."

**(Show Slide 17: Controller Layer Testing - MockMvc Web Layer Tests)**

**Host:**
"MockMvc simulates HTTP requests without starting a server. We mock the service layer and test HTTP status codes, JSON responses, and request mapping. This gives us fast, focused web layer testing."

---

### SCENE 10: Running and Summary (4:45 - 5:15)

**(Show Slide 18: Running the Application - In Our Project Structure)**

**Host:**
"Our multi-module project separates concerns beautifully. Run gradle :web-services:bootRun and you'll have a full REST API at localhost:8080. Try the /api/employees/hello endpoint to see it in action."

**(Show Slide 19: Summary)**

**Host:**
"We've built a production-ready REST API with proper layered architecture. Modern Java records, comprehensive business logic, and testing at all three layers. This is how you build maintainable, scalable APIs."

**Host:**
"Spring Boot makes the plumbing invisible so you can focus on business value. Clean separation of concerns means each layer has a single responsibility, making the code easier to test, maintain, and extend."

---

### SCENE 11: Conclusion (5:15 - 5:30)

**(Show Slide 20: Next: Input Validation)**

**Host:**
"Next time, we'll dive deeper into input validation and preventing security vulnerabilities like SQL injection and XSS attacks. Because building great APIs is just the first step - securing them is equally important!"

**Host:**
"Thanks for watching, and I'll see you in the next video where we'll make our APIs bulletproof!"

---

**🎬 End of Video Script**