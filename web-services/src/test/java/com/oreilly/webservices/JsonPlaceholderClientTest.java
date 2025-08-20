package com.oreilly.webservices;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for JsonPlaceholderClient demonstrating real HTTP client functionality.
 * These tests make actual HTTP requests to JsonPlaceholder API.
 * <p>
 * Note: Tests use Assumptions to only run when JsonPlaceholder API is available.
 */
class JsonPlaceholderClientTest {
    
    private JsonPlaceholderClient client;
    
    @BeforeAll
    static void checkNetworkAvailability() {
        assumeTrue(isNetworkAvailable(), "Network not available - skipping all API tests");
    }
    
    @BeforeEach
    void setUp() {
        client = new JsonPlaceholderClient();
    }
    
    /**
     * HTTP service availability check for conditional test execution.
     * Uses HEAD request to verify the actual HTTP service is responding.
     */
    static boolean isNetworkAvailable() {
        try (HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build()) {
                
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .HEAD()
                .timeout(Duration.ofSeconds(10))
                .build();
                
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Test
    void testGetSinglePost() throws Exception {
        // Act
        Optional<JsonPlaceholderClient.Post> post = client.getPost(1);
        
        // Assert
        assertThat(post).isPresent();
        assertThat(post.get().id()).isEqualTo(1);
        assertThat(post.get().userId()).isEqualTo(1);
        assertThat(post.get().title()).isNotBlank();
        assertThat(post.get().body()).isNotBlank();
    }
    
    @Test
    void testGetNonExistentPost() throws Exception {
        // Act
        Optional<JsonPlaceholderClient.Post> post = client.getPost(999);
        
        // Assert - JsonPlaceholder returns 404 for truly non-existent posts
        // This tests our error handling for 404 status codes
        assertThat(post).isEmpty();
    }
    
    @Test
    void testGetAllPosts() throws Exception {
        // Act
        List<JsonPlaceholderClient.Post> posts = client.getAllPosts();
        
        // Assert
        assertThat(posts).isNotEmpty();
        assertThat(posts).hasSize(100); // JsonPlaceholder has exactly 100 posts
        
        // Verify structure of first post
        JsonPlaceholderClient.Post firstPost = posts.getFirst();
        assertThat(firstPost.id()).isEqualTo(1);
        assertThat(firstPost.userId()).isEqualTo(1);
        assertThat(firstPost.title()).isNotBlank();
        assertThat(firstPost.body()).isNotBlank();
    }
    
    @Test
    void testGetPostsByUser() throws Exception {
        // Act
        List<JsonPlaceholderClient.Post> userPosts = client.getPostsByUser(1);
        
        // Assert
        assertThat(userPosts).isNotEmpty();
        assertThat(userPosts).hasSize(10); // Each user has exactly 10 posts
        
        // Verify all posts belong to the specified user
        assertThat(userPosts).allMatch(post -> post.userId().equals(1));
    }
    
    @Test
    void testCreatePost() throws Exception {
        // Arrange
        JsonPlaceholderClient.Post newPost = new JsonPlaceholderClient.Post(
            1, "Test Post Title", "Test post body for HTTP client demonstration"
        );
        
        // Act
        JsonPlaceholderClient.Post createdPost = client.createPost(newPost);
        
        // Assert
        assertThat(createdPost).isNotNull();
        assertThat(createdPost.id()).isEqualTo(101); // JsonPlaceholder assigns ID 101 for new posts
        assertThat(createdPost.userId()).isEqualTo(1);
        assertThat(createdPost.title()).isEqualTo("Test Post Title");
        assertThat(createdPost.body()).isEqualTo("Test post body for HTTP client demonstration");
    }
    
    @Test
    void testUpdatePost() throws Exception {
        // Arrange
        JsonPlaceholderClient.Post updateData = new JsonPlaceholderClient.Post(
            1, "Updated Title", "Updated body content"
        );
        
        // Act
        JsonPlaceholderClient.Post updatedPost = client.updatePost(1, updateData);
        
        // Assert
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.id()).isEqualTo(1);
        assertThat(updatedPost.userId()).isEqualTo(1);
        assertThat(updatedPost.title()).isEqualTo("Updated Title");
        assertThat(updatedPost.body()).isEqualTo("Updated body content");
    }
    
    @Test
    void testDeletePost() throws Exception {
        // Act
        boolean deleted = client.deletePost(1);
        
        // Assert
        assertThat(deleted).isTrue(); // JsonPlaceholder simulates successful deletion
    }
    
    @Test
    void testGetUser() throws Exception {
        // Act
        Optional<JsonPlaceholderClient.User> user = client.getUser(1);
        
        // Assert
        assertThat(user).isPresent();
        assertThat(user.get().id()).isEqualTo(1);
        assertThat(user.get().name()).isEqualTo("Leanne Graham");
        assertThat(user.get().username()).isEqualTo("Bret");
        assertThat(user.get().email()).isEqualTo("Sincere@april.biz");
    }
    
    @Test
    void testGetPostAsync() throws Exception {
        // Act
        CompletableFuture<JsonPlaceholderClient.Post> futurePost = client.getPostAsync(1);
        JsonPlaceholderClient.Post post = futurePost.get(); // Wait for completion
        
        // Assert
        assertThat(post).isNotNull();
        assertThat(post.id()).isEqualTo(1);
        assertThat(post.userId()).isEqualTo(1);
        assertThat(post.title()).isNotBlank();
        assertThat(post.body()).isNotBlank();
    }
    
    @Test
    void testPostModelCreation() {
        // Test the Post model independently
        JsonPlaceholderClient.Post post = new JsonPlaceholderClient.Post(1, "Test Title", "Test Body");
        
        assertThat(post.userId()).isEqualTo(1);
        assertThat(post.title()).isEqualTo("Test Title");
        assertThat(post.body()).isEqualTo("Test Body");
        assertThat(post.id()).isNull(); // Server sets ID
        
        // Test creating post with ID
        JsonPlaceholderClient.Post postWithId = new JsonPlaceholderClient.Post(100, 1, "Test Title", "Test Body");
        assertThat(postWithId.id()).isEqualTo(100);
        
        // Test toString (automatically generated by record)
        assertThat(post.toString()).contains("Test Title");
        
        // Test record equality and hashCode
        JsonPlaceholderClient.Post identical = new JsonPlaceholderClient.Post(1, "Test Title", "Test Body");
        assertThat(post).isEqualTo(identical);
        assertThat(post.hashCode()).isEqualTo(identical.hashCode());
    }
    
    @Test
    void testUserModelCreation() {
        // Test the User model independently
        JsonPlaceholderClient.User user = new JsonPlaceholderClient.User(1, "Test User", "testuser", "test@example.com");
        
        assertThat(user.id()).isEqualTo(1);
        assertThat(user.name()).isEqualTo("Test User");
        assertThat(user.username()).isEqualTo("testuser");
        assertThat(user.email()).isEqualTo("test@example.com");
        
        // Test toString (automatically generated by record)
        assertThat(user.toString()).contains("Test User", "testuser");
        
        // Test record equality and hashCode
        JsonPlaceholderClient.User identical = new JsonPlaceholderClient.User(1, "Test User", "testuser", "test@example.com");
        assertThat(user).isEqualTo(identical);
        assertThat(user.hashCode()).isEqualTo(identical.hashCode());
    }
    
    @Test
    void testApiException() {
        // Test custom exception
        JsonPlaceholderClient.ApiException exception = 
            new JsonPlaceholderClient.ApiException("Test error", 404);
        
        assertThat(exception.getStatusCode()).isEqualTo(404);
        assertThat(exception.getMessage()).contains("Test error", "404");
    }
    
    @Test
    void testClientInstantiation() {
        // Test that client can be created without errors
        assertThat(client).isNotNull();
        assertThatNoException().isThrownBy(JsonPlaceholderClient::new);
    }
}