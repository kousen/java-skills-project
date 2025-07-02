import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.assertj.core.api.Assertions.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@DisplayName("Database Connection Singleton Tests")
class DatabaseConnectionTest {
    
    @AfterEach
    void tearDown() {
        // Clean up after each test
        if (DatabaseConnection.getInstance().isConnected()) {
            DatabaseConnection.getInstance().close();
        }
    }
    
    @Test
    @DisplayName("Should return same instance for multiple calls")
    void shouldReturnSameInstanceForMultipleCalls() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();
        
        assertThat(instance1).isSameAs(instance2);
        assertThat(instance2).isSameAs(instance3);
        assertThat(instance1).isSameAs(instance3);
    }
    
    @Test
    @DisplayName("Should be thread-safe")
    void shouldBeThreadSafe() throws InterruptedException {
        int threadCount = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        Set<DatabaseConnection> instances = ConcurrentHashMap.newKeySet();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // Create multiple threads that get the singleton instance
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    DatabaseConnection instance = DatabaseConnection.getInstance();
                    instances.add(instance);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Wait for all threads to complete
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        
        // All threads should have gotten the same instance
        assertThat(instances).hasSize(1);
    }
    
    @Test
    @DisplayName("Should establish database connection")
    void shouldEstablishDatabaseConnection() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        
        assertThat(dbConnection.isConnected()).isTrue();
        assertThat(dbConnection.getConnection()).isNotNull();
    }
    
    @Test
    @DisplayName("Should prevent cloning")
    void shouldPreventCloning() {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        
        assertThatThrownBy(() -> {
            // Use reflection to access the protected clone method
            java.lang.reflect.Method cloneMethod = Object.class.getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);
            cloneMethod.invoke(instance);
        }).hasCauseInstanceOf(CloneNotSupportedException.class);
    }
    
    @Test
    @DisplayName("Should close connection properly")
    void shouldCloseConnectionProperly() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        
        assertThat(dbConnection.isConnected()).isTrue();
        
        dbConnection.close();
        
        assertThat(dbConnection.isConnected()).isFalse();
    }
    
    @Test
    @DisplayName("Should recreate connection after close")
    void shouldRecreateConnectionAfterClose() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        
        // Close the connection
        dbConnection.close();
        assertThat(dbConnection.isConnected()).isFalse();
        
        // Getting connection again should recreate it
        assertThat(dbConnection.getConnection()).isNotNull();
        assertThat(dbConnection.isConnected()).isTrue();
    }
}