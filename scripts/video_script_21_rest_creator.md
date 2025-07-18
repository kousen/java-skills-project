# Video Script: Creating REST Services with Spring Boot

**Goal:** 21. Build RESTful web services using Spring Boot for modern API development.
**Target Duration:** 4-5 minutes

---

### SCENE 1: Introduction (0:00 - 0:30)

**(Show Slide 1: Title Slide - "Creating REST Services with Spring Boot")**

**Host:**
"Welcome back! Last time, we learned how to consume REST APIs. Now let's flip the script and create our own REST services. We'll use Spring Boot, the most popular Java framework that makes building REST APIs almost embarrassingly easy."

---

### SCENE 2: Why Spring Boot? (0:30 - 1:00)

**(Show Slide 2: Why Spring Boot?)**

**Host:**
"If you've ever tried building a web service from scratch, you know it's painful. You need a server, routing, JSON conversion, error handling - the list goes on. Spring Boot handles all of this with minimal configuration."

**(Show Slide 3: Developer Experience)**

**Host:**
"It's like the difference between building a car from parts versus buying one ready to drive. Spring Boot gives you a production-ready REST API framework right out of the box."

---

### SCENE 3: Getting Started (1:00 - 1:30)

**(Show Slide 4: Spring Boot Setup)**

**Host:**
"First, we need the Spring Boot Web starter dependency. If you're using our project, it's already configured in the web-services module."

**(Show Slide 5: Main Application Class)**

**Host:**
"This is your entire application! The @SpringBootApplication annotation sets up component scanning, auto-configuration, and embedded Tomcat server. One annotation, and you're ready to build APIs."

---

### SCENE 4: Your First REST Controller (1:30 - 2:00)

**(Show Slide 6: Your First REST Controller)**

**Host:**
"Let's create an Employee REST controller."

**(Transition to IDE showing EmployeeController.java)**

**Host:**
"Look at these annotations. @RestController tells Spring this class handles REST requests. @RequestMapping sets the base path. @GetMapping handles GET requests."

**Host:**
"Run this, and you've got a working REST endpoint at localhost:8080/api/employees/hello. That's it!"

## CRUD Operations (1:45-2:30)

Now let's implement the full CRUD - Create, Read, Update, Delete.

**(Show GET endpoint in code)**

The @PathVariable annotation extracts the ID from the URL. Spring automatically converts our Employee object to JSON. No manual JSON handling needed!

**(Show POST endpoint in code)**

@RequestBody does the opposite - it takes incoming JSON and converts it to an Employee object. @ResponseStatus sets the HTTP status to 201 Created.

## Service Layer Pattern (2:30-3:15)

In real applications, controllers shouldn't contain business logic. That belongs in a service layer.

**(Show service class in IDE)**

The @Service annotation marks this as a Spring component. We're using a ConcurrentHashMap as a simple in-memory database. In production, you'd use a real database.

**(Show controller with service injection)**

Notice the constructor injection? Spring automatically wires the EmployeeService into our controller. This is dependency injection at work - one of Spring's core features.

## Exception Handling (3:15-3:45)

What happens when an employee isn't found? We need proper error handling.

[Show custom exception and handler]

By throwing EmployeeNotFoundException, Spring automatically returns a 404 status. The @RestControllerAdvice creates a global exception handler that catches these exceptions and returns a proper error response.

---

### SCENE 8: Input Validation (4:00 - 4:30)

**(Show Slide 12: Input Validation)**

**Host:**
"Never trust user input! Spring integrates with Bean Validation to make this easy."

**(Show validated Employee class in code)**

**Host:**
"Add validation annotations to your model. Then use @Valid in your controller method. If validation fails, Spring automatically returns a 400 Bad Request with details about what's wrong. This prevents bad data from ever reaching your business logic."

---

### SCENE 9: Testing Your API (4:30 - 5:00)

**(Show Slide 13: Testing Support)**

**Host:**
"Spring Boot includes excellent testing support."

**(Show test example in code)**

**Host:**
"MockMvc lets you test your REST endpoints without starting a real server. You can verify status codes, response bodies, headers - everything. This makes test-driven development a breeze."

---

### SCENE 10: Conclusion (5:00 - 5:15)

**(Show Slide 14: Key Takeaways)**

**Host:**
"You've just built a complete REST API with Spring Boot! We covered controllers, services, exception handling, and validation - the core of any REST service."

**(Show Slide 15: Next Steps)**

**Host:**
"Next time, we'll secure these endpoints by preventing SQL injection and XSS attacks. Because with great API power comes great security responsibility!"