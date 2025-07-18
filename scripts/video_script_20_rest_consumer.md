# Video Script: Consuming REST APIs with Java

## Introduction (0:00-0:15)

Welcome back! In our last video, we learned about logging. Today, we're going to connect our Employee Management System to the outside world by consuming REST APIs. If you've ever needed to fetch data from a web service or integrate with third-party APIs, this is the video for you.

## What is a REST API? (0:15-0:45)

REST stands for Representational State Transfer. Don't let the fancy name scare you - it's just a way for applications to talk to each other over HTTP. 

Think of it like ordering from a restaurant menu. You make a request - "I'd like the employee data, please" - and the server brings you back exactly what you ordered, usually in JSON format. The main operations are GET to retrieve, POST to create, PUT to update, and DELETE to remove.

## Java's HTTP Client (0:45-1:15)

Before Java 11, making HTTP requests was painful. You had to use HttpURLConnection, which felt like programming in the stone age. But Java 11 introduced a modern HTTP client that's a joy to use.

[Show client creation code]

Look how clean this is! We create a client using the builder pattern, set the HTTP version, add a timeout, and we're ready to go. This client can be reused for multiple requests, which is much more efficient than creating a new one each time.

## Making Your First GET Request (1:15-2:00)

Let's fetch employee data from an API. 

[Show GET request code]

We build the request with a URI, add headers like "Accept" to tell the server we want JSON, and specify it's a GET request. Then we send it and get back a response.

The response contains everything - the status code, headers, and most importantly, the body with our data. If the status is 200, we know it worked and we can process the JSON.

## POST Requests - Creating Data (2:00-2:45)

Creating a new employee is just as straightforward. 

[Show POST request code]

We use a text block to create our JSON - one of my favorite Java features. Then we build a POST request, set the Content-Type header so the server knows we're sending JSON, and attach our data as the body.

Notice how we're using the fluent builder pattern throughout? Each method returns the builder, so we can chain calls together. It reads almost like English.

## Error Handling (2:45-3:15)

In the real world, things go wrong. Networks fail, servers go down, APIs change. 

[Show error handling code]

We need to handle different status codes appropriately. 200 means success, 404 means not found, 401 means we're not authorized. And we wrap everything in a try-catch to handle network errors.

Pro tip: Always log errors with enough context to debug later. Your future self will thank you.

## Asynchronous Requests (3:15-3:45)

Sometimes you don't want to wait for a response. Maybe you're making multiple API calls and want them to run in parallel.

[Show async code]

The sendAsync method returns a CompletableFuture. We can process the response when it arrives using thenAccept, and handle errors with exceptionally. This keeps your application responsive while waiting for slow APIs.

## Working with JSON (3:45-4:15)

Most REST APIs use JSON, so you'll want a good JSON library. Jackson is the most popular choice.

[Show Jackson example]

With Jackson's ObjectMapper, we can convert JSON strings to Java objects and vice versa. Define your Employee class with the right fields, and Jackson handles all the mapping automatically. No more manual JSON parsing!

## Complete API Client (4:15-4:45)

Let's put it all together in a reusable API client class.

[Show EmployeeApiClient class]

This client encapsulates all our HTTP logic. It has methods for each operation - get, create, update, delete. Each method handles the HTTP details and returns proper Java objects. Your application code stays clean and focused on business logic, not HTTP plumbing.

## Best Practices (4:45-5:00)

A few quick tips:
- Reuse your HttpClient instance - it's thread-safe and manages connections efficiently
- Always set timeouts to avoid hanging forever
- Consider retry logic for transient failures
- Log requests and responses for debugging, but be careful with sensitive data

## Wrapping Up (5:00-5:15)

You now know how to consume any REST API using Java's built-in HTTP client. No external libraries needed for basic operations! Start simple with GET requests, then add POST, error handling, and async as needed.

Next time, we'll flip the script and create our own REST APIs using Spring Boot. Until then, go forth and integrate with the world's APIs!

## Code Examples Referenced:

1. HttpClient creation with builder
2. GET request for employee data
3. POST request with JSON body
4. Error handling with status codes
5. Async request example
6. Jackson JSON processing
7. Complete EmployeeApiClient class