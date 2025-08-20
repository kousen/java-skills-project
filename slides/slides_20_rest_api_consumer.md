---
theme: seriph
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Consuming REST APIs
  Learn to use Java's built-in HTTP client to consume REST APIs
drawings:
  persist: false
defaults:
  foo: true
transition: slide-left
title: Consuming REST APIs with Java
---

# Consuming REST APIs

Using Java's Built-in HTTP Client

<div class="pt-12">
  <span @click="$slidev.nav.next" class="px-2 py-1 rounded cursor-pointer" hover="bg-white bg-opacity-10">
    Press Space for next page <carbon:arrow-right class="inline"/>
  </span>
</div>

---

# Contact Info

Ken Kousen<br>
Kousen IT, Inc.

- ken.kousen@kousenit.com
- http://www.kousenit.com
- http://kousenit.org (blog)
- Social Media:
  - [@kenkousen](https://twitter.com/kenkousen) (Twitter)
  - [@kousenit.com](https://bsky.app/profile/kousenit.com) (Bluesky)
  - [https://www.linkedin.com/in/kenkousen/](https://www.linkedin.com/in/kenkousen/) (LinkedIn)
- *Tales from the jar side* (free newsletter)
  - https://kenkousen.substack.com
  - https://youtube.com/@talesfromthejarside

---
transition: slide-left
---

# Demo API: JsonPlaceholder

## Free Fake REST API for Learning

<v-clicks>

- **URL:** https://jsonplaceholder.typicode.com/
- **No authentication required**
- **Perfect for learning and testing**

</v-clicks>

## Available Resources

<v-clicks>

- `/posts` - 100 blog posts
- `/users` - 10 users
- `/comments` - 500 comments
- **Supports all HTTP methods (GET, POST, PUT, DELETE)**

</v-clicks>

---
transition: slide-left
---

# What is a REST API?

## RESTful Web Services

<v-clicks>

- **RE**presentational **S**tate **T**ransfer
- HTTP-based communication
- JSON or XML data format

</v-clicks>

## Common Operations

<v-clicks>

- GET - Retrieve data
- POST - Create new resources
- PUT/PATCH - Update resources
- DELETE - Remove resources

</v-clicks>

---
transition: slide-left
---

# Java HTTP Client

## Introduction in Java 11

<v-clicks>

- Modern, built-in HTTP client
- Replaces legacy HttpURLConnection
- Supports HTTP/2 and WebSocket

</v-clicks>

## Key Features

<v-clicks>

- Synchronous and asynchronous APIs
- Fluent builder pattern
- Built-in JSON support with libraries

</v-clicks>

---
transition: slide-left
---

# Creating an HTTP Client

## Basic Client Setup

```java
import java.net.http.HttpClient;
import java.time.Duration;

HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .connectTimeout(Duration.ofSeconds(10))
    .build();
```

---
transition: slide-left
---

# Making a GET Request

## Retrieving Post Data from JsonPlaceholder

```java
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
    .header("Accept", "application/json")
    .timeout(Duration.ofSeconds(30))
    .GET()
    .build();

HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString());
```

---
transition: slide-left
---

# Handling the Response

## Processing JSON Data

```java
if (response.statusCode() == 200) {
    String json = response.body();
    System.out.println("Post data: " + json);
    // Output: {"userId": 1, "id": 1, 
    //          "title": "sunt aut facere...", 
    //          "body": "quia et suscipit..."}
} else {
    System.err.println("Error: " + response.statusCode());
}
```

---
transition: slide-left
---

# POST Request

## Creating a New Post

```java
String postJson = """
    {
        "userId": 1,
        "title": "Learning HTTP Client",
        "body": "This post demonstrates POST requests with JsonPlaceholder"
    }
    """;

HttpRequest postRequest = HttpRequest.newBuilder()
    .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
    .header("Content-Type", "application/json")
    .header("Accept", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(postJson))
    .build();
```

---
transition: slide-left
---

# Request Headers

## Common Headers

<v-clicks>

- **Content-Type** - Format of request body
- **Accept** - Desired response format
- **Authorization** - Authentication credentials

</v-clicks>

```java
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create(apiUrl))
    .header("Authorization", "Bearer " + token)
    .header("Accept", "application/json")
    .build();
```

---
transition: slide-left
---

# Error Handling

## Robust API Communication

```java
try {
    HttpResponse<String> response = client.send(request, 
        HttpResponse.BodyHandlers.ofString());
    
    switch (response.statusCode()) {
        case 200 -> processSuccess(response.body());
        case 404 -> handleNotFound();
        case 401 -> handleUnauthorized();
        default -> handleError(response.statusCode());
    }
} catch (IOException | InterruptedException e) {
    logger.error("API call failed", e);
}
```

---
transition: slide-left
---

# Asynchronous Requests

## Non-blocking API Calls

```java
CompletableFuture<HttpResponse<String>> future = 
    client.sendAsync(request, 
        HttpResponse.BodyHandlers.ofString());

future.thenAccept(response -> {
    System.out.println("Status: " + response.statusCode());
    System.out.println("Body: " + response.body());
}).exceptionally(e -> {
    System.err.println("Request failed: " + e.getMessage());
    return null;
});
```

---
transition: slide-left
---

# JSON Processing

## Using Jackson

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

ObjectMapper mapper = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

// Deserialize JSON to Post
Post post = mapper.readValue(response.body(), Post.class);

// Serialize Post to JSON
String json = mapper.writeValueAsString(post);
```

---
transition: slide-left
---

# Complete Example

## JsonPlaceholder API Client

```java
public class JsonPlaceholderClient {
    private final HttpClient client;
    private final ObjectMapper mapper;
    private static final String BASE_URL = 
        "https://jsonplaceholder.typicode.com";
    
    public JsonPlaceholderClient() {
        this.client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.mapper = new ObjectMapper();
    }
}
```

---
transition: slide-left
---

# GET Method Implementation

## Fetching Single Post

```java
public Optional<Post> getPost(int id) 
        throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL + "/posts/" + id))
        .header("Accept", "application/json")
        .timeout(Duration.ofSeconds(30))
        .GET()
        .build();
    
    HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() == 200) {
        Post post = mapper.readValue(response.body(), Post.class);
        return Optional.of(post);
    }
    return Optional.empty();
}
```

---
transition: slide-left
---

# GET All Posts

## Fetching Collections

```java
public List<Post> getAllPosts() 
        throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_URL + "/posts"))
        .header("Accept", "application/json")
        .GET()
        .build();
    
    HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() == 200) {
        return mapper.readValue(response.body(),
            mapper.getTypeFactory().constructCollectionType(
                List.class, Post.class));
    }
    throw new ApiException("Failed to fetch posts", response.statusCode());
}
```

---
transition: slide-left
---

# POST Method Implementation

## Creating New Employee

```java
public Employee createEmployee(Employee employee) 
        throws IOException, InterruptedException {
    String json = mapper.writeValueAsString(employee);
    
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/employees"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
    
    HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    
    if (response.statusCode() == 201) {
        return mapper.readValue(response.body(), 
            Employee.class);
    }
    throw new RuntimeException("Failed to create employee");
}
```

---
transition: slide-left
---

# PUT Method Implementation

## Updating Employee

```java
public Employee updateEmployee(Long id, Employee employee) 
        throws IOException, InterruptedException {
    String json = mapper.writeValueAsString(employee);
    
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/employees/" + id))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .build();
    
    HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    
    return mapper.readValue(response.body(), 
        Employee.class);
}
```

---
transition: slide-left
---

# DELETE Method

## Removing Employee

```java
public boolean deleteEmployee(Long id) 
        throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/employees/" + id))
        .DELETE()
        .build();
    
    HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    
    return response.statusCode() == 204;
}
```

---
transition: slide-left
---

# Query Parameters

## Building URLs with Parameters

```java
public List<Employee> searchEmployees(String department, 
                                    Double minSalary) {
    String queryParams = String.format(
        "?department=%s&minSalary=%.2f",
        URLEncoder.encode(department, StandardCharsets.UTF_8),
        minSalary
    );
    
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + "/employees/search" + queryParams))
        .GET()
        .build();
    
    // Send request and process response...
}
```

---
transition: slide-left
---

# Request Timeout

## Setting Timeouts

```java
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create(apiUrl))
    .timeout(Duration.ofSeconds(30))
    .GET()
    .build();

// Client-level timeout
HttpClient client = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .build();
```

---
transition: slide-left
---

# Authentication

## Bearer Token Example

```java
public class AuthenticatedApiClient {
    private final String token;
    
    private HttpRequest.Builder addAuth(
            HttpRequest.Builder builder) {
        return builder.header("Authorization", 
            "Bearer " + token);
    }
    
    public Employee getEmployee(Long id) {
        HttpRequest request = addAuth(
            HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
        ).build();
        // Send request...
    }
}
```

---
transition: slide-left
---

# Retry Logic

## Handling Transient Failures

```java
public <T> T executeWithRetry(
        Supplier<T> operation, int maxRetries) {
    int attempts = 0;
    while (attempts < maxRetries) {
        try {
            return operation.get();
        } catch (Exception e) {
            attempts++;
            if (attempts >= maxRetries) {
                throw new RuntimeException("Max retries exceeded", e);
            }
            try {
                Thread.sleep(1000 * attempts); // Exponential backoff
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
    throw new IllegalStateException("Shouldn't reach here");
}
```

---
transition: slide-left
---

# Best Practices

## API Client Design

<v-clicks>

- Use connection pooling (HttpClient reuse)
- Implement proper error handling
- Add logging for debugging

</v-clicks>

---
transition: slide-left
---

# Best Practices (continued)

## Performance & Reliability

<v-clicks>

- Set appropriate timeouts
- Consider retry strategies
- Use async for concurrent requests

</v-clicks>

---
transition: slide-left
---

# Testing API Clients

## Mock Server Approach

```java
@Test
void testGetEmployee() throws Exception {
    // Use MockWebServer or WireMock
    mockServer.enqueue(new MockResponse()
        .setBody("""
            {"id": 1, "name": "John Doe", "salary": 50000}
            """)
        .setResponseCode(200));
    
    Employee result = client.getEmployee(1L).orElseThrow();
    
    assertEquals("John Doe", result.getName());
    assertEquals(50000, result.getSalary());
}
```

---
transition: slide-left
---

# Try It Out Exercise

## Hands-on HTTP Client Practice

File: `web-services/src/main/java/com/oreilly/webservices/HttpClientExercise.java`

<v-clicks>

- **Complete the TODO sections** to implement HTTP client methods
- **Practice with JsonPlaceholder's /todos endpoint** - different from our demo!
- **Learn by doing:** GET, POST, async requests, error handling

</v-clicks>

## What You'll Implement

<v-clicks>

- HTTP client configuration with timeout and HTTP/2
- GET requests for single items and collections
- POST requests with JSON payloads
- Asynchronous requests with CompletableFuture
- Proper error handling for different status codes

</v-clicks>

---
transition: slide-left
layout: center
---

# Summary

<v-clicks>

- Java 11+ provides a modern HTTP client
- Use builder pattern for configuration
- Handle responses based on status codes
- Integrate with JSON libraries for data processing
- Implement proper error handling and retries

</v-clicks>

---
transition: slide-left
layout: center
---

# Next: Creating REST Services

Build your own REST APIs with Spring Boot