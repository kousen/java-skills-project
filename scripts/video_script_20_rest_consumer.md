# Video Script: Consuming REST APIs with Java

**Goal:** 20. Integrate with external services by consuming REST APIs using modern Java HTTP client.  
**Target Duration:** 6-7 minutes

---

### SCENE 1: Introduction (0:00 - 0:30)

**(Show Slide 1: Title Slide - "Consuming REST APIs with Java")**

**Host:**
"Hi everyone, and welcome to this series on essential Java skills. Today, we're consuming **REST APIs** using Java's modern HTTP client, and we'll be working with real, live data!"

**(Show Slide 3: Demo API: JsonPlaceholder)**

**Host:**
"We'll use JsonPlaceholder, a free fake REST API that's perfect for learning. It provides real endpoints with posts, users, and comments - and no authentication required!"

---

### SCENE 2: Understanding REST APIs (0:30 - 1:00)

**(Show Slide 4: What is a REST API?)**

**Host:**
"REST stands for Representational State Transfer. Don't let the fancy name scare you - it's just a way for applications to talk to each other over HTTP."

**(Show Slide 5: Common Operations)**

**Host:**
"Think of it like ordering from a restaurant menu. You make a request - 'I'd like post number 1, please' - and the server brings you back exactly what you ordered, usually in JSON format. The main operations are GET to retrieve, POST to create, PUT to update, and DELETE to remove."

---

### SCENE 3: Java's Modern HTTP Client (1:00 - 1:30)

**(Show Slide 6: Java HTTP Client)**

**Host:**
"Before Java 11, making HTTP requests was painful. You had to use HttpURLConnection, which felt like programming in the stone age. But Java 11 introduced a modern HTTP client that's a joy to use."

**(Transition to IDE showing JsonPlaceholderClient.java)**

**Host:**
"Look how clean this is! We create a client using the builder pattern, set HTTP/2, add a timeout, and we're ready to go. This client can be reused for multiple requests, which is much more efficient than creating a new one each time."

---

### SCENE 4: Making GET Requests (1:30 - 2:15)

**(Show Slide 8: Making a GET Request)**

**Host:**
"Let's fetch a real post from JsonPlaceholder. This is a live API call - no simulation!"

**(Highlight the GET request method in the code)**

**Host:**
"We build the request with a URI pointing to JsonPlaceholder, add headers like 'Accept' to tell the server we want JSON, set a timeout, and specify it's a GET request. Then we send it and get back a response."

**Host:**
"The response contains everything - the status code, headers, and most importantly, the body with our data. If the status is 200, we know it worked and we can process the real JSON from JsonPlaceholder."

---

### SCENE 5: POST Requests and JSON Handling (2:15 - 3:00)

**(Show Slide 11: POST Request)**

**Host:**
"Creating a new post is just as straightforward, and JsonPlaceholder will actually process our request!"

**(Highlight the POST request method)**

**Host:**
"We use a text block to create our JSON - one of my favorite Java features. Then we build a POST request, set the Content-Type header so the server knows we're sending JSON, and attach our data as the body."

**Host:**
"Notice how we're using the fluent builder pattern throughout? Each method returns the builder, so we can chain calls together. It reads almost like English."

---

### SCENE 6: Error Handling and Best Practices (3:00 - 3:45)

**(Show Slide 10: Error Handling)**

**YOU:**
"In the real world, things go wrong. Networks fail, servers go down, APIs change."

**(Highlight the error handling code)**

**YOU:**
"We need to handle different status codes appropriately. 200 means success, 404 means not found, 401 means we're not authorized. And we wrap everything in a try-catch to handle network errors."

**(Show Slide 11: Robust API Communication)**

**YOU:**
"Pro tip: Always log errors with enough context to debug later. Your future self will thank you."

---

### SCENE 7: Asynchronous Requests (3:45 - 4:15)

**(Show Slide 12: Asynchronous Requests)**

**YOU:**
"Sometimes you don't want to wait for a response. Maybe you're making multiple API calls and want them to run in parallel."

**(Highlight the async method)**

**YOU:**
"The `sendAsync` method returns a CompletableFuture. We can process the response when it arrives using `thenAccept`, and handle errors with `exceptionally`. This keeps your application responsive while waiting for slow APIs."

---

### SCENE 8: Complete Implementation (4:15 - 4:45)

**(Show Slide 21: Complete Example)**

**Host:**
"Let's look at our complete `JsonPlaceholderClient` class."

**(Show the full class structure)**

**Host:**
"This client encapsulates all our HTTP logic. It has methods for each operation - get, create, update, delete. Each method handles the HTTP details and returns proper Java objects. Your application code stays clean and focused on business logic, not HTTP plumbing."

---

### SCENE 9: Best Practices (4:45 - 5:15)

**(Show Slide 21: Best Practices)**

**YOU:**
"Let me share some best practices for API client design."

**(Show Slide 22: Performance & Reliability)**

**YOU:**
"Reuse your HttpClient instance - it's thread-safe and manages connections efficiently. Always set timeouts to avoid hanging forever. Consider retry logic for transient failures, and log requests and responses for debugging, but be careful with sensitive data."

---

### SCENE 10: Try It Out Exercise (5:15 - 5:45)

**(Show Slide: Try It Out Exercise)**

**Host:**
"Now it's your turn to practice! In the web-services module, you'll find HttpClientExercise.java - a hands-on exercise with TODO sections."

**Host:**
"Complete the TODOs to implement your own HTTP client methods using JsonPlaceholder's /todos endpoint. You'll practice GET requests, POST requests, async calls, and error handling - all with real HTTP requests to a different endpoint than our demo!"

**Host:**
"The exercise comes with tests to verify your implementation, and you can run the main method to see everything in action."

---

### SCENE 11: Conclusion (5:45 - 6:00)

**(Show Slide: Summary)**

**Host:**
"You now know how to consume any REST API using Java's built-in HTTP client. No external libraries needed for basic operations! Start simple with GET requests, then add POST, error handling, and async as needed."

**(Show Slide: Next Steps)**

**Host:**
"Next time, we'll flip the script and create our own REST APIs using Spring Boot. Thanks for watching!"