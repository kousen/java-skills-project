import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Test Suite for LegacyCodeExample Refactoring Demo
 * 
 * These tests demonstrate how a comprehensive test suite enables safe refactoring.
 * They verify the BEHAVIOR of the order processing system, not implementation details.
 * 
 * Notice how these tests will work with BOTH the legacy LegacyOrderProcessor 
 * AND the refactored LegacyCodeExample coordinator - that's the power of 
 * behavior-based testing!
 */
class LegacyCodeExampleTest {
    
    private LegacyCodeExample processor;
    private RefactorOrderRepository repository;
    private RefactorNotificationService notificationService;
    private RefactorInventoryService inventoryService;
    
    @BeforeEach
    void setUp() {
        // Create dependencies for the refactored version
        repository = new RefactorOrderRepository();
        notificationService = new RefactorNotificationService();
        inventoryService = new RefactorInventoryService();
        
        // Create the processor with dependencies
        processor = new LegacyCodeExample(repository, notificationService, inventoryService);
    }
    
    @Nested
    @DisplayName("Order Processing")
    class OrderProcessing {
        
        @Test
        @DisplayName("Should process new order successfully")
        void shouldProcessNewOrderSuccessfully() {
            var order = new RefactorOrder(1001, "CUST-001", 299.99, "NEW", LocalDateTime.now());
            
            assertThatNoException().isThrownBy(() -> processor.processOrder(order));
            
            // Verify order was saved as processed
            var savedOrder = repository.findById(1001);
            assertThat(savedOrder).isNotNull();
            assertThat(savedOrder.status()).isEqualTo("PROCESSED");
            assertThat(savedOrder.customerId()).isEqualTo("CUST-001");
            assertThat(savedOrder.amount()).isEqualTo(299.99);
        }
        
        @Test
        @DisplayName("Should not process already processed order")
        void shouldNotProcessAlreadyProcessedOrder() {
            var processedOrder = new RefactorOrder(1001, "CUST-001", 299.99, "PROCESSED", LocalDateTime.now());
            
            processor.processOrder(processedOrder);
            
            // Should not save anything since order is already processed
            var orders = repository.findAll();
            assertThat(orders).isEmpty();
        }
        
        @Test
        @DisplayName("Should not process cancelled order")
        void shouldNotProcessCancelledOrder() {
            var cancelledOrder = new RefactorOrder(1001, "CUST-001", 299.99, "CANCELLED", LocalDateTime.now());
            
            processor.processOrder(cancelledOrder);
            
            // Should not save anything since order is cancelled
            var orders = repository.findAll();
            assertThat(orders).isEmpty();
        }
        
        @Test
        @DisplayName("Should process multiple new orders")
        void shouldProcessMultipleNewOrders() {
            // Setup multiple new orders
            var orders = List.of(
                new RefactorOrder(1001, "CUST-001", 299.99, "NEW", LocalDateTime.now()),
                new RefactorOrder(1002, "CUST-002", 149.50, "NEW", LocalDateTime.now()),
                new RefactorOrder(1003, "CUST-003", 89.99, "NEW", LocalDateTime.now())
            );
            
            // Add orders to repository
            orders.forEach(repository::save);
            
            assertThatNoException().isThrownBy(() -> processor.processAllNewOrders());
            
            // Verify all orders were processed
            var processedOrders = repository.findByStatus("PROCESSED");
            assertThat(processedOrders).hasSize(3);
            
            // Verify order IDs are correct
            assertThat(processedOrders).extracting(RefactorOrder::id)
                    .containsExactlyInAnyOrder(1001, 1002, 1003);
        }
        
        @Test
        @DisplayName("Should handle empty order list")
        void shouldHandleEmptyOrderList() {
            assertThatNoException().isThrownBy(() -> processor.processAllNewOrders());
            
            var allOrders = repository.findAll();
            assertThat(allOrders).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("Order Cancellation")
    class OrderCancellation {
        
        @BeforeEach
        void setUpOrders() {
            // Add some test orders
            repository.save(new RefactorOrder(1001, "CUST-001", 299.99, "NEW", LocalDateTime.now()));
            repository.save(new RefactorOrder(1002, "CUST-002", 149.50, "PROCESSED", LocalDateTime.now()));
        }
        
        @Test
        @DisplayName("Should cancel existing order successfully")
        void shouldCancelExistingOrderSuccessfully() {
            assertThatNoException().isThrownBy(() -> processor.cancelOrder(1001));
            
            var cancelledOrder = repository.findById(1001);
            assertThat(cancelledOrder).isNotNull();
            assertThat(cancelledOrder.status()).isEqualTo("CANCELLED");
            assertThat(cancelledOrder.customerId()).isEqualTo("CUST-001");
            assertThat(cancelledOrder.amount()).isEqualTo(299.99);
        }
        
        @Test
        @DisplayName("Should handle cancellation of non-existent order")
        void shouldHandleCancellationOfNonExistentOrder() {
            assertThatNoException().isThrownBy(() -> processor.cancelOrder(9999));
            
            // No order should be created
            var order = repository.findById(9999);
            assertThat(order).isNull();
        }
        
        @Test
        @DisplayName("Should handle cancellation of already cancelled order")
        void shouldHandleCancellationOfAlreadyCancelledOrder() {
            // First cancellation
            processor.cancelOrder(1001);
            
            // Second cancellation should be handled gracefully
            assertThatNoException().isThrownBy(() -> processor.cancelOrder(1001));
            
            var order = repository.findById(1001);
            assertThat(order.status()).isEqualTo("CANCELLED");
        }
        
        @Test
        @DisplayName("Should cancel processed order")
        void shouldCancelProcessedOrder() {
            assertThatNoException().isThrownBy(() -> processor.cancelOrder(1002));
            
            var cancelledOrder = repository.findById(1002);
            assertThat(cancelledOrder.status()).isEqualTo("CANCELLED");
        }
    }
    
    @Nested
    @DisplayName("Repository Integration")
    class RepositoryIntegration {
        
        @Test
        @DisplayName("Should save and retrieve orders correctly")
        void shouldSaveAndRetrieveOrdersCorrectly() {
            var order = new RefactorOrder(1001, "CUST-001", 299.99, "NEW", LocalDateTime.now());
            
            repository.save(order);
            
            var retrievedOrder = repository.findById(1001);
            assertThat(retrievedOrder).isEqualTo(order);
        }
        
        @Test
        @DisplayName("Should update existing order")
        void shouldUpdateExistingOrder() {
            var originalOrder = new RefactorOrder(1001, "CUST-001", 299.99, "NEW", LocalDateTime.now());
            var updatedOrder = new RefactorOrder(1001, "CUST-001", 299.99, "PROCESSED", LocalDateTime.now());
            
            repository.save(originalOrder);
            repository.save(updatedOrder);
            
            var retrievedOrder = repository.findById(1001);
            assertThat(retrievedOrder.status()).isEqualTo("PROCESSED");
            
            // Should only have one order (updated, not duplicated)
            var allOrders = repository.findAll();
            assertThat(allOrders).hasSize(1);
        }
        
        @Test
        @DisplayName("Should find orders by status")
        void shouldFindOrdersByStatus() {
            repository.save(new RefactorOrder(1001, "CUST-001", 299.99, "NEW", LocalDateTime.now()));
            repository.save(new RefactorOrder(1002, "CUST-002", 149.50, "NEW", LocalDateTime.now()));
            repository.save(new RefactorOrder(1003, "CUST-003", 89.99, "PROCESSED", LocalDateTime.now()));
            
            var newOrders = repository.findByStatus("NEW");
            assertThat(newOrders).hasSize(2);
            assertThat(newOrders).extracting(RefactorOrder::id)
                    .containsExactlyInAnyOrder(1001, 1002);
            
            var processedOrders = repository.findByStatus("PROCESSED");
            assertThat(processedOrders).hasSize(1);
            assertThat(processedOrders.get(0).id()).isEqualTo(1003);
        }
        
        @Test
        @DisplayName("Should return null for non-existent order")
        void shouldReturnNullForNonExistentOrder() {
            var order = repository.findById(9999);
            assertThat(order).isNull();
        }
        
        @Test
        @DisplayName("Should return empty list for status with no orders")
        void shouldReturnEmptyListForStatusWithNoOrders() {
            var orders = repository.findByStatus("NON_EXISTENT_STATUS");
            assertThat(orders).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("Service Integration")
    class ServiceIntegration {
        
        @Test
        @DisplayName("Should demonstrate services working together")
        void shouldDemonstrateServicesWorkingTogether() {
            var order = new RefactorOrder(1001, "CUST-001", 299.99, "NEW", LocalDateTime.now());
            
            // Process order - this should trigger all three services
            processor.processOrder(order);
            
            // Verify repository was called (order saved as processed)
            var savedOrder = repository.findById(1001);
            assertThat(savedOrder.status()).isEqualTo("PROCESSED");
            
            // Note: In a real application, we might mock the notification and inventory services
            // to verify they were called. For this demo, we're focusing on integration testing.
        }
        
        @Test
        @DisplayName("Should handle workflow for cancelled order")
        void shouldHandleWorkflowForCancelledOrder() {
            // Setup: Add an order first
            repository.save(new RefactorOrder(1001, "CUST-001", 299.99, "PROCESSED", LocalDateTime.now()));
            
            // Cancel the order
            processor.cancelOrder(1001);
            
            // Verify the workflow completed
            var cancelledOrder = repository.findById(1001);
            assertThat(cancelledOrder.status()).isEqualTo("CANCELLED");
        }
    }
    
    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {
        
        @Test
        @DisplayName("Should handle null order gracefully")
        void shouldHandleNullOrderGracefully() {
            // The current implementation doesn't explicitly handle null,
            // but this test documents expected behavior
            assertThatNoException().isThrownBy(() -> processor.processOrder(null));
        }
        
        @Test
        @DisplayName("Should handle orders with special characters in customer ID")
        void shouldHandleOrdersWithSpecialCharactersInCustomerId() {
            var order = new RefactorOrder(1001, "CUST-001@#$%", 299.99, "NEW", LocalDateTime.now());
            
            assertThatNoException().isThrownBy(() -> processor.processOrder(order));
            
            var savedOrder = repository.findById(1001);
            assertThat(savedOrder.customerId()).isEqualTo("CUST-001@#$%");
        }
        
        @Test
        @DisplayName("Should handle very large order amounts")
        void shouldHandleVeryLargeOrderAmounts() {
            var order = new RefactorOrder(1001, "CUST-001", 999999.99, "NEW", LocalDateTime.now());
            
            assertThatNoException().isThrownBy(() -> processor.processOrder(order));
            
            var savedOrder = repository.findById(1001);
            assertThat(savedOrder.amount()).isEqualTo(999999.99);
        }
    }
}