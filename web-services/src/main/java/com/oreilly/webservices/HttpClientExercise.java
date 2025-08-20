package com.oreilly.webservices;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Try It Out: HTTP Client Exercise
 * <p>
 * Complete the TODO sections to practice consuming REST APIs with Java's HttpClient.
 * This exercise uses JsonPlaceholder's /todos endpoint (https://jsonplaceholder.typicode.com/),
 * a free fake REST API perfect for learning and testing.
 * <p>
 * You'll implement:
 * 1. GET requests (single todo and collections)
 * 2. POST requests (creating new todos)
 * 3. Error handling and status codes
 * 4. JSON processing with Jackson
 * 5. Asynchronous requests
 */
@SuppressWarnings("JavadocLinkAsPlainText")
public class HttpClientExercise {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientExercise.class);
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    
    // TODO 1: Create HttpClient and ObjectMapper instances
    // HINT: Use HttpClient.newBuilder() with HTTP/2 and 10-second timeout
    // HINT: Configure ObjectMapper to ignore unknown properties
    private final HttpClient client = null; // Replace this line
    private final ObjectMapper objectMapper = null; // Replace this line
    
    public HttpClientExercise() {
        logger.info("HttpClientExercise initialized for: {}", BASE_URL);
    }
    
    /**
     * TODO 2: Implement GET request to fetch a single todo
     * Endpoint: GET https://jsonplaceholder.typicode.com/todos/{id}
     * Return Optional.empty() for 404 status, throw exception for other errors
     */
    public Optional<Todo> getTodo(int id) throws IOException, InterruptedException {
        logger.debug("Fetching todo with ID: {}", id);
        
        // TODO: Create HttpRequest with:
        // - URI: BASE_URL + "/todos/" + id
        // - Accept header: "application/json"
        // - GET method
        // - 30 second timeout
        HttpRequest request = null; // Replace this implementation
        
        // TODO: Send request and get response
        HttpResponse<String> response = null; // Replace this line
        
        // TODO: Handle response status codes:
        // - 200: Parse JSON and return Optional.of(todo)
        // - 404: Return Optional.empty()
        // - Other: Throw RuntimeException with status code
        
        return Optional.empty(); // Replace this line
    }
    
    /**
     * TODO 3: Implement GET request for multiple todos
     * Endpoint: GET https://jsonplaceholder.typicode.com/todos
     */
    public List<Todo> getAllTodos() throws IOException, InterruptedException {
        logger.debug("Fetching all todos");
        
        // TODO: Implement similar to getTodo but return List<Todo>
        // HINT: Use objectMapper.getTypeFactory().constructCollectionType(List.class, Todo.class)
        
        return List.of(); // Replace this line
    }
    
    /**
     * TODO 4: Implement POST request to create a new todo
     * Endpoint: POST https://jsonplaceholder.typicode.com/todos
     * Content-Type: application/json
     */
    public Todo createTodo(Todo todo) throws IOException, InterruptedException {
        logger.debug("Creating new todo: {}", todo.title());
        
        // TODO: Convert todo to JSON string using objectMapper
        String jsonPayload = null; // Replace this line
        
        // TODO: Create POST request with:
        // - URI: BASE_URL + "/todos"
        // - Content-Type header: "application/json"
        // - Accept header: "application/json"
        // - Body: jsonPayload
        HttpRequest request = null; // Replace this implementation
        
        // TODO: Send request and handle response
        // - 201: Parse and return created todo
        // - Other: Throw exception
        
        return null; // Replace this line
    }
    
    /**
     * TODO 5: Implement asynchronous GET request
     * Show how to make non-blocking HTTP calls using CompletableFuture
     */
    public CompletableFuture<Todo> getTodoAsync(int id) {
        logger.debug("Fetching todo asynchronously with ID: {}", id);
        
        // TODO: Create HTTP request (same as getTodo)
        HttpRequest request = null; // Replace this line
        
        // TODO: Use client.sendAsync() and return CompletableFuture<Todo>
        // HINT: Use .thenApply() to parse JSON response
        // HINT: Use .exceptionally() to handle errors
        
        return CompletableFuture.completedFuture(null); // Replace this line
    }
    
    /**
     * TODO 6: Implement error handling demonstration
     * Try to fetch a todo that doesn't exist and handle it gracefully
     */
    public void demonstrateErrorHandling() {
        logger.info("=== Error Handling Demonstration ===");
        
        try {
            // TODO: Try to fetch todo with ID 999 (doesn't exist)
            // Handle the empty Optional gracefully
            Optional<Todo> todo = null; // Replace this line
            
            if (todo.isPresent()) {
                System.out.println("Found todo: " + todo.get().title());
            } else {
                System.out.println("Todo not found - this is expected behavior");
            }
            
        } catch (Exception e) {
            logger.error("Error handling demonstration failed", e);
        }
    }
    
    /**
     * Todo data model for JsonPlaceholder API.
     * Using record for immutable data transfer object.
     */
    public record Todo(Integer id, Integer userId, String title, Boolean completed) {
        
        // Constructor for creating new todos (without ID)
        public Todo(Integer userId, String title, Boolean completed) {
            this(null, userId, title, completed);
        }
        
        // Getters are automatically generated by record
        // id(), userId(), title(), completed()
        // toString(), equals(), hashCode() are also automatically generated
    }
    
    /**
     * Main method to test your implementations
     */
    public static void main(String[] args) {
        HttpClientExercise exercise = new HttpClientExercise();
        
        try {
            System.out.println("=== HTTP Client Exercise ===");
            System.out.println("Complete the TODOs and run this to test your implementation!");
            System.out.println();
            
            // Test 1: Get single todo
            System.out.println("1. Testing getTodo():");
            Optional<Todo> todo = exercise.getTodo(1);
            if (todo.isPresent()) {
                System.out.printf("   ✓ Retrieved: %s (completed: %b)%n", todo.get().title(), todo.get().completed());
            } else {
                System.out.println("   ✗ Failed to retrieve todo");
            }
            System.out.println();
            
            // Test 2: Get all todos
            System.out.println("2. Testing getAllTodos():");
            List<Todo> todos = exercise.getAllTodos();
            if (!todos.isEmpty()) {
                System.out.printf("   ✓ Retrieved %d todos%n", todos.size());
            } else {
                System.out.println("   ✗ Failed to retrieve todos");
            }
            System.out.println();
            
            // Test 3: Create new todo
            System.out.println("3. Testing createTodo():");
            Todo newTodo = new Todo(1, "Learn HTTP Client API", false);
            Todo createdTodo = exercise.createTodo(newTodo);
            if (createdTodo != null && createdTodo.id() != null) {
                System.out.printf("   ✓ Created todo with ID: %d%n", createdTodo.id());
            } else {
                System.out.println("   ✗ Failed to create todo");
            }
            System.out.println();
            
            // Test 4: Async request
            System.out.println("4. Testing getTodoAsync():");
            CompletableFuture<Todo> futureTodo = exercise.getTodoAsync(2);
            Todo asyncTodo = futureTodo.get(); // Wait for completion
            if (asyncTodo != null) {
                System.out.printf("   ✓ Async result: %s (completed: %b)%n", asyncTodo.title(), asyncTodo.completed());
            } else {
                System.out.println("   ✗ Async request failed");
            }
            System.out.println();
            
            // Test 5: Error handling
            System.out.println("5. Testing error handling:");
            exercise.demonstrateErrorHandling();
            System.out.println();
            
            System.out.println("=== Exercise Complete! ===");
            
            if (exercise.client != null && exercise.objectMapper != null) {
                System.out.println("""
                    🎉 Congratulations! You've successfully implemented:
                       ✓ HTTP client configuration
                       ✓ GET requests (single and multiple items)
                       ✓ POST requests with JSON
                       ✓ Error handling
                       ✓ Asynchronous requests
                       ✓ JSON serialization/deserialization
                    """);
            } else {
                System.out.println("❌ Complete the TODOs to see your HTTP client in action!");
            }
            
        } catch (Exception e) {
            System.err.println("Exercise failed: " + e.getMessage());
            System.err.println("Make sure to complete all TODO items!");
        }
    }
}

/*
 * SOLUTION HINTS:
 * 
 * TODO 1:
 * private final HttpClient client = HttpClient.newBuilder()
 *     .version(HttpClient.Version.HTTP_2)
 *     .connectTimeout(Duration.ofSeconds(10))
 *     .build();
 * private final ObjectMapper objectMapper = new ObjectMapper()
 *     .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
 * 
 * TODO 2:
 * HttpRequest request = HttpRequest.newBuilder()
 *     .uri(URI.create(BASE_URL + "/todos/" + id))
 *     .header("Accept", "application/json")
 *     .timeout(Duration.ofSeconds(30))
 *     .GET()
 *     .build();
 * 
 * HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
 * 
 * switch (response.statusCode()) {
 *     case 200:
 *         Todo todo = objectMapper.readValue(response.body(), Todo.class);
 *         return Optional.of(todo);
 *     case 404:
 *         return Optional.empty();
 *     default:
 *         throw new RuntimeException("Failed with status: " + response.statusCode());
 * }
 * 
 * TODO 3:
 * Similar to TODO 2 but use:
 * List<Todo> todos = objectMapper.readValue(response.body(),
 *     objectMapper.getTypeFactory().constructCollectionType(List.class, Todo.class));
 * 
 * TODO 4:
 * String jsonPayload = objectMapper.writeValueAsString(todo);
 * 
 * HttpRequest request = HttpRequest.newBuilder()
 *     .uri(URI.create(BASE_URL + "/todos"))
 *     .header("Content-Type", "application/json")
 *     .header("Accept", "application/json")
 *     .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
 *     .build();
 * 
 * TODO 5:
 * return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
 *     .thenApply(response -> {
 *         try {
 *             return objectMapper.readValue(response.body(), Todo.class);
 *         } catch (IOException e) {
 *             throw new RuntimeException(e);
 *         }
 *     })
 *     .exceptionally(throwable -> {
 *         logger.error("Async request failed", throwable);
 *         return null;
 *     });
 * 
 * TODO 6:
 * Optional<Todo> todo = getTodo(999);
 */