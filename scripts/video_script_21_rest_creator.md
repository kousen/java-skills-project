# Video Script: Creating REST Services with Spring Boot

## Introduction (0:00-0:15)

Welcome back! Last time, we learned how to consume REST APIs. Now let's flip the script and create our own REST services. We'll use Spring Boot, the most popular Java framework that makes building REST APIs almost embarrassingly easy.

## Why Spring Boot? (0:15-0:45)

If you've ever tried building a web service from scratch, you know it's painful. You need a server, routing, JSON conversion, error handling - the list goes on. Spring Boot handles all of this with minimal configuration.

It's like the difference between building a car from parts versus buying one ready to drive. Spring Boot gives you a production-ready REST API framework right out of the box.

## Getting Started (0:45-1:15)

First, we need the Spring Boot Web starter dependency. If you're using our project, it's already configured in the web-services module.

[Show main application class]

This is your entire application! The @SpringBootApplication annotation sets up component scanning, auto-configuration, and embedded Tomcat server. One annotation, and you're ready to build APIs.

## Your First REST Controller (1:15-1:45)

Let's create an Employee REST controller.

[Show basic controller]

Look at these annotations. @RestController tells Spring this class handles REST requests. @RequestMapping sets the base path. @GetMapping handles GET requests. 

Run this, and you've got a working REST endpoint at localhost:8080/api/employees/hello. That's it!

## CRUD Operations (1:45-2:30)

Now let's implement the full CRUD - Create, Read, Update, Delete.

[Show GET endpoint]

The @PathVariable annotation extracts the ID from the URL. Spring automatically converts our Employee object to JSON. No manual JSON handling needed!

[Show POST endpoint]

@RequestBody does the opposite - it takes incoming JSON and converts it to an Employee object. @ResponseStatus sets the HTTP status to 201 Created.

## Service Layer Pattern (2:30-3:15)

In real applications, controllers shouldn't contain business logic. That belongs in a service layer.

[Show service class]

The @Service annotation marks this as a Spring component. We're using a ConcurrentHashMap as a simple in-memory database. In production, you'd use a real database.

[Show controller with service]

Notice the constructor injection? Spring automatically wires the EmployeeService into our controller. This is dependency injection at work - one of Spring's core features.

## Exception Handling (3:15-3:45)

What happens when an employee isn't found? We need proper error handling.

[Show custom exception and handler]

By throwing EmployeeNotFoundException, Spring automatically returns a 404 status. The @RestControllerAdvice creates a global exception handler that catches these exceptions and returns a proper error response.

## Input Validation (3:45-4:15)

Never trust user input! Spring integrates with Bean Validation to make this easy.

[Show validated Employee class]

Add validation annotations to your model. Then use @Valid in your controller method. If validation fails, Spring automatically returns a 400 Bad Request with details about what's wrong.

This prevents bad data from ever reaching your business logic.

## Testing Your API (4:15-4:45)

Spring Boot includes excellent testing support.

[Show test example]

MockMvc lets you test your REST endpoints without starting a real server. You can verify status codes, response bodies, headers - everything. This makes test-driven development a breeze.

## Running and Testing (4:45-5:00)

Start your application with 'gradle bootRun' or just run the main method. Spring Boot starts an embedded Tomcat server on port 8080.

You can test with curl, Postman, or even your browser for GET requests. Your Employee Management System is now accessible to any client that speaks HTTP!

## Wrapping Up (5:00-5:15)

You've just built a complete REST API with Spring Boot! We covered controllers, services, exception handling, and validation - the core of any REST service.

Next time, we'll secure these endpoints by preventing SQL injection and XSS attacks. Because with great API power comes great security responsibility!

## Code Examples Referenced:

1. Main Spring Boot application class
2. Basic REST controller with @GetMapping
3. Full CRUD operations (GET, POST, PUT, DELETE)
4. Service layer with dependency injection
5. Custom exception and global error handler
6. Bean validation annotations
7. MockMvc test example