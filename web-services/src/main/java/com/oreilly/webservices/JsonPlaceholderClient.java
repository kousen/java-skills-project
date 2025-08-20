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
 * HTTP Client demonstrating REST API consumption using JsonPlaceholder.
 * JsonPlaceholder (<a href="https://jsonplaceholder.typicode.com/">...</a>) is a free fake REST API
 * for testing and prototyping, perfect for learning HTTP client usage.
 * <p>
 * Demonstrates:
 * - All HTTP methods (GET, POST, PUT, DELETE)
 * - Error handling and status codes
 * - JSON serialization/deserialization  
 * - Asynchronous requests
 * - Best practices for API client design
 */
@SuppressWarnings("JavadocLinkAsPlainText")
public class JsonPlaceholderClient {
    private static final Logger logger = LoggerFactory.getLogger(JsonPlaceholderClient.class);
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    
    public JsonPlaceholderClient() {
        this.client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        logger.info("JsonPlaceholderClient initialized for base URL: {}", BASE_URL);
    }
    
    // ========== GET REQUESTS ==========
    
    /**
     * Demonstrates GET request to fetch a single post.
     * Endpoint: GET https://jsonplaceholder.typicode.com/posts/1
     */
    public Optional<Post> getPost(int id) throws IOException, InterruptedException {
        logger.debug("Fetching post with ID: {}", id);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/posts/" + id))
            .header("Accept", "application/json")
            .header("User-Agent", "JsonPlaceholderClient/1.0")
            .timeout(Duration.ofSeconds(30))
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        
        logger.debug("Response status: {} for post ID: {}", response.statusCode(), id);
        
        switch (response.statusCode()) {
            case 200:
                Post post = objectMapper.readValue(response.body(), Post.class);
                logger.info("Successfully retrieved post: {}", post.title());
                return Optional.of(post);
            case 404:
                logger.warn("Post not found with ID: {}", id);
                return Optional.empty();
            default:
                logger.error("Unexpected response status: {} for post ID: {}", response.statusCode(), id);
                throw new ApiException("Failed to fetch post", response.statusCode());
        }
    }
    
    /**
     * Demonstrates GET request for multiple posts.
     * Endpoint: GET https://jsonplaceholder.typicode.com/posts
     */
    public List<Post> getAllPosts() throws IOException, InterruptedException {
        logger.debug("Fetching all posts");
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/posts"))
            .header("Accept", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            List<Post> posts = objectMapper.readValue(response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Post.class));
            logger.info("Retrieved {} posts", posts.size());
            return posts;
        } else {
            logger.error("Failed to retrieve posts, status: {}", response.statusCode());
            throw new ApiException("Failed to fetch posts", response.statusCode());
        }
    }
    
    /**
     * Demonstrates GET request with query parameters.
     * Endpoint: GET https://jsonplaceholder.typicode.com/posts?userId=1
     */
    public List<Post> getPostsByUser(int userId) throws IOException, InterruptedException {
        logger.debug("Fetching posts for user ID: {}", userId);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/posts?userId=" + userId))
            .header("Accept", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            List<Post> posts = objectMapper.readValue(response.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Post.class));
            logger.info("Retrieved {} posts for user {}", posts.size(), userId);
            return posts;
        } else {
            logger.error("Failed to retrieve posts for user {}, status: {}", userId, response.statusCode());
            throw new ApiException("Failed to fetch user posts", response.statusCode());
        }
    }
    
    // ========== POST REQUESTS ==========
    
    /**
     * Demonstrates POST request to create a new post.
     * Endpoint: POST https://jsonplaceholder.typicode.com/posts
     * Note: JsonPlaceholder simulates creation but doesn't persist data
     */
    public Post createPost(Post post) throws IOException, InterruptedException {
        logger.debug("Creating new post: {}", post.title());
        
        String jsonPayload = objectMapper.writeValueAsString(post);
        logger.debug("JSON payload: {}", jsonPayload);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/posts"))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            Post createdPost = objectMapper.readValue(response.body(), Post.class);
            logger.info("Successfully created post with ID: {}", createdPost.id());
            return createdPost;
        } else {
            logger.error("Failed to create post, status: {}", response.statusCode());
            throw new ApiException("Failed to create post", response.statusCode());
        }
    }
    
    // ========== PUT REQUESTS ==========
    
    /**
     * Demonstrates PUT request to update an existing post.
     * Endpoint: PUT https://jsonplaceholder.typicode.com/posts/1
     */
    public Post updatePost(int id, Post post) throws IOException, InterruptedException {
        logger.debug("Updating post with ID: {}", id);
        
        String jsonPayload = objectMapper.writeValueAsString(post);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/posts/" + id))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            Post updatedPost = objectMapper.readValue(response.body(), Post.class);
            logger.info("Successfully updated post with ID: {}", updatedPost.id());
            return updatedPost;
        } else {
            logger.error("Failed to update post {}, status: {}", id, response.statusCode());
            throw new ApiException("Failed to update post", response.statusCode());
        }
    }
    
    // ========== DELETE REQUESTS ==========
    
    /**
     * Demonstrates DELETE request to remove a post.
     * Endpoint: DELETE https://jsonplaceholder.typicode.com/posts/1
     */
    public boolean deletePost(int id) throws IOException, InterruptedException {
        logger.debug("Deleting post with ID: {}", id);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/posts/" + id))
            .DELETE()
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            logger.info("Successfully deleted post with ID: {}", id);
            return true;
        } else {
            logger.error("Failed to delete post {}, status: {}", id, response.statusCode());
            return false;
        }
    }
    
    // ========== ASYNCHRONOUS REQUESTS ==========
    
    /**
     * Demonstrates asynchronous GET request using CompletableFuture.
     * Shows non-blocking HTTP operations.
     */
    public CompletableFuture<Post> getPostAsync(int id) {
        logger.debug("Fetching post asynchronously with ID: {}", id);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/posts/" + id))
            .header("Accept", "application/json")
            .GET()
            .build();
        
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(response -> {
                logger.debug("Async response received, status: {}", response.statusCode());
                try {
                    if (response.statusCode() == 200) {
                        Post post = objectMapper.readValue(response.body(), Post.class);
                        logger.info("Async fetch successful for post: {}", post.title());
                        return post;
                    } else {
                        throw new RuntimeException("API call failed with status: " + response.statusCode());
                    }
                } catch (IOException e) {
                    logger.error("Failed to parse async response", e);
                    throw new RuntimeException("Failed to parse response", e);
                }
            })
            .exceptionally(throwable -> {
                logger.error("Async request failed for post ID: {}", id, throwable);
                return null;
            });
    }
    
    // ========== USER OPERATIONS ==========
    
    /**
     * Demonstrates fetching user data to show different endpoint usage.
     * Endpoint: GET https://jsonplaceholder.typicode.com/users/1
     */
    public Optional<User> getUser(int id) throws IOException, InterruptedException {
        logger.debug("Fetching user with ID: {}", id);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/users/" + id))
            .header("Accept", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            User user = objectMapper.readValue(response.body(), User.class);
            logger.info("Successfully retrieved user: {}", user.name());
            return Optional.of(user);
        } else if (response.statusCode() == 404) {
            logger.warn("User not found with ID: {}", id);
            return Optional.empty();
        } else {
            logger.error("Failed to fetch user {}, status: {}", id, response.statusCode());
            throw new ApiException("Failed to fetch user", response.statusCode());
        }
    }
    
    // ========== DATA MODELS ==========
    
    /**
     * Post data model matching JsonPlaceholder's post structure.
     * Using record for immutable data transfer object.
     */
    public record Post(Integer id, Integer userId, String title, String body) {
        
        // Constructor for creating new posts (without ID)
        public Post(Integer userId, String title, String body) {
            this(null, userId, title, body);
        }
        
        // Getters are automatically generated by record
        // getId(), getUserId(), getTitle(), getBody()
        // toString(), equals(), hashCode() are also automatically generated
    }
    
    /**
     * User data model for JsonPlaceholder user structure.
     * Using record for immutable data transfer object.
     */
    public record User(Integer id, String name, String username, String email) {
        
        // Getters are automatically generated by record
        // id(), name(), username(), email()
        // toString(), equals(), hashCode() are also automatically generated
    }
    
    /**
     * Custom exception for API errors.
     */
    public static class ApiException extends RuntimeException {
        private final int statusCode;
        
        public ApiException(String message, int statusCode) {
            super(message + " (HTTP " + statusCode + ")");
            this.statusCode = statusCode;
        }
        
        public int getStatusCode() {
            return statusCode;
        }
    }
    
    /**
     * Main method demonstrating all HTTP client capabilities.
     */
    public static void main(String[] args) {
        JsonPlaceholderClient client = new JsonPlaceholderClient();
        
        try {
            System.out.println("=== JsonPlaceholder HTTP Client Demonstration ===");
            System.out.println("Using real API at: " + BASE_URL);
            System.out.println();
            
            // 1. GET single post
            System.out.println("1. GET Single Post:");
            Optional<Post> post = client.getPost(1);
            post.ifPresent(value -> System.out.printf("   Retrieved: %s%n", value.title()));
            System.out.println();
            
            // 2. GET all posts (limited output)
            System.out.println("2. GET All Posts:");
            List<Post> allPosts = client.getAllPosts();
            System.out.printf("   Retrieved %d posts%n", allPosts.size());
            System.out.printf("   First post: %s%n", allPosts.getFirst().title());
            System.out.println();
            
            // 3. GET posts by user
            System.out.println("3. GET Posts by User:");
            List<Post> userPosts = client.getPostsByUser(1);
            System.out.printf("   User 1 has %d posts%n", userPosts.size());
            System.out.println();
            
            // 4. POST create new post
            System.out.println("4. POST Create New Post:");
            Post newPost = new Post(1, "Learning HTTP Client", 
                "This post demonstrates creating new content via POST request");
            Post createdPost = client.createPost(newPost);
            System.out.printf("   Created post with ID: %d%n", createdPost.id());
            System.out.println();
            
            // 5. PUT update post
            System.out.println("5. PUT Update Post:");
            Post updateData = new Post(1, "Updated Post Title", "Updated content via PUT request");
            Post updatedPost = client.updatePost(1, updateData);
            System.out.printf("   Updated post: %s%n", updatedPost.title());
            System.out.println();
            
            // 6. GET user information
            System.out.println("6. GET User Information:");
            Optional<User> user = client.getUser(1);
            user.ifPresent(value -> System.out.printf("   User: %s (%s)%n", value.name(), value.email()));
            System.out.println();
            
            // 7. Asynchronous request
            System.out.println("7. Asynchronous GET Request:");
            CompletableFuture<Post> futurePost = client.getPostAsync(2);
            System.out.println("   Async request initiated...");
            Post asyncPost = futurePost.get(); // Wait for completion
            if (asyncPost != null) {
                System.out.printf("   Async result: %s%n", asyncPost.title());
            }
            System.out.println();
            
            // 8. DELETE post
            System.out.println("8. DELETE Post:");
            boolean deleted = client.deletePost(1);
            System.out.printf("   Post deletion %s%n", deleted ? "successful" : "failed");
            System.out.println();
            
            System.out.println("""
                === All HTTP Methods Demonstrated Successfully! ===
                
                Key Features Shown:
                ✓ GET requests (single item, collections, with parameters)
                ✓ POST requests (creating new resources)
                ✓ PUT requests (updating existing resources)
                ✓ DELETE requests (removing resources)
                ✓ JSON serialization and deserialization
                ✓ Error handling and status codes
                ✓ Asynchronous requests with CompletableFuture
                ✓ Real API interaction (not simulated)
                """);
            
        } catch (Exception e) {
            logger.error("Demonstration failed", e);
            System.err.println("Error: " + e.getMessage());
        }
    }
}