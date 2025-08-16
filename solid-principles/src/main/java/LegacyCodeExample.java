import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Refactoring Demo: Legacy Code to Clean Code
 * <p>
 * This file demonstrates the refactoring process by showing:
 * 1. BEFORE: LegacyOrderProcessor - A God class with multiple code smells
 * 2. AFTER: Refactored classes following SOLID principles
 * <p>
 * Key concepts demonstrated:
 * - Technical debt and code smells
 * - Single Responsibility Principle
 * - Don't Repeat Yourself (DRY)
 * - Performance improvements through better algorithms
 */

// Simple data class to represent an order
record RefactorOrder(int id, String customerId, double amount, String status, LocalDateTime orderDate) {
}

// ============================================================================
// BEFORE: The Legacy Code (with code smells)
// ============================================================================

/**
 * LEGACY CODE - DO NOT COPY THIS PATTERN!
 * <p>
 * This class demonstrates multiple code smells:
 * 1. God Class - handles too many responsibilities
 * 2. Duplicated code - order finding logic repeated
 * 3. Performance issues - multiple loops over same data
 * 4. Tight coupling - everything in one place
 * 5. Violates SRP - order processing, notifications, and inventory in one class
 */
class LegacyOrderProcessor {
    private final List<RefactorOrder> orders = new ArrayList<>();
    
    public void addOrder(RefactorOrder order) {
        orders.add(order);
    }
    
    /**
     * CODE SMELL: This method does WAY too much!
     * - Processes orders
     * - Sends notifications  
     * - Updates inventory
     * - Loops through the same data THREE times (inefficient)
     */
    public void processOrders() {
        // First loop: Process orders
        for (RefactorOrder order : orders) {
            if ("NEW".equals(order.status())) {
                System.out.println("Processing order " + order.id());
                // Simulate order processing
                var processedOrder = new RefactorOrder(
                    order.id(), order.customerId(), order.amount(), 
                    "PROCESSED", order.orderDate()
                );
                
                // Update the order in place (problematic)
                orders.set(orders.indexOf(order), processedOrder);
            }
        }
        
        // Second loop: Send notifications (CODE SMELL: Separate loop!)
        for (RefactorOrder order : orders) {
            if ("PROCESSED".equals(order.status())) {
                System.out.println("Sending confirmation email for order " + order.id() + 
                                 " to customer " + order.customerId());
                // Simulate email sending
            }
        }
        
        // Third loop: Update inventory (CODE SMELL: Another separate loop!)
        for (RefactorOrder order : orders) {
            if ("PROCESSED".equals(order.status())) {
                System.out.println("Updating inventory for order " + order.id() + 
                                 " amount: $" + order.amount());
                // Simulate inventory update
            }
        }
    }
    
    /**
     * CODE SMELL: Duplicated logic!
     * This order-finding logic is repeated in multiple methods
     */
    public RefactorOrder findOrder(int orderId) {
        RefactorOrder foundOrder = null;
        for (RefactorOrder order : orders) {
            if (order.id() == orderId) {
                foundOrder = order;
                break;
            }
        }
        return foundOrder;
    }
    
    /**
     * CODE SMELL: More duplicated logic!
     * Same order-finding pattern as above
     */
    public void cancelOrder(int orderId) {
        RefactorOrder orderToCancel = null;
        for (RefactorOrder order : orders) {  // DUPLICATION!
            if (order.id() == orderId) {
                orderToCancel = order;
                break;
            }
        }
        
        if (orderToCancel != null) {
            var cancelledOrder = new RefactorOrder(
                orderToCancel.id(), orderToCancel.customerId(), 
                orderToCancel.amount(), "CANCELLED", orderToCancel.orderDate()
            );
            orders.set(orders.indexOf(orderToCancel), cancelledOrder);
            System.out.println("Order " + orderId + " has been cancelled");
        }
    }
    
    public List<RefactorOrder> getAllOrders() {
        return new ArrayList<>(orders);
    }
}

// ============================================================================
// AFTER: The Refactored Code (following SOLID principles)
// ============================================================================

/**
 * Refactored: Separate class for order persistence (Single Responsibility)
 */
class RefactorOrderRepository {
    private final List<RefactorOrder> orders = new ArrayList<>();
    
    public void save(RefactorOrder order) {
        // Remove old version if exists, add new version
        orders.removeIf(existing -> existing.id() == order.id());
        orders.add(order);
        System.out.println("Saved order " + order.id() + " to database");
    }
    
    public RefactorOrder findById(int orderId) {
        return orders.stream()
                .filter(order -> order.id() == orderId)
                .findFirst()
                .orElse(null);
    }
    
    public List<RefactorOrder> findAll() {
        return new ArrayList<>(orders);
    }
    
    public List<RefactorOrder> findByStatus(String status) {
        return orders.stream()
                .filter(order -> status.equals(order.status()))
                .toList();
    }
}

/**
 * Refactored: Separate class for notifications (Single Responsibility)
 */
class RefactorNotificationService {
    
    public void sendOrderConfirmation(RefactorOrder order) {
        System.out.printf("""
                📧 Sending confirmation email:
                   Customer: %s
                   Order: %d
                   Amount: $%.2f
                   Status: %s%n""", 
                order.customerId(), order.id(), order.amount(), order.status());
    }
    
    public void sendCancellationNotice(RefactorOrder order) {
        System.out.printf("""
                📧 Sending cancellation notice:
                   Customer: %s
                   Order: %d
                   Refund: $%.2f%n""",
                order.customerId(), order.id(), order.amount());
    }
}

/**
 * Refactored: Separate class for inventory management (Single Responsibility)
 */
class RefactorInventoryService {
    
    public void updateInventory(RefactorOrder order) {
        System.out.printf("""
                📦 Updating inventory:
                   Order: %d
                   Amount: $%.2f
                   Inventory adjusted%n""",
                order.id(), order.amount());
    }
    
    public void restoreInventory(RefactorOrder order) {
        System.out.printf("""
                📦 Restoring inventory:
                   Order: %d
                   Amount: $%.2f
                   Items returned to stock%n""",
                order.id(), order.amount());
    }
}

/**
 * Refactored: Clean, focused order processor (Single Responsibility)
 * <p>
 * This class is now:
 * - Easy to test (dependencies can be mocked)
 * - Easy to understand (single responsibility)
 * - Efficient (single pass processing)
 * - Extensible (new services can be added)
 */
@SuppressWarnings({"WeakerAccess", "ClassEscapesDefinedScope"})
public class LegacyCodeExample {
    private final RefactorOrderRepository repository;
    private final RefactorNotificationService notificationService;
    private final RefactorInventoryService inventoryService;
    
    // Dependency injection constructor
    public LegacyCodeExample(RefactorOrderRepository repository,
                           RefactorNotificationService notificationService,
                           RefactorInventoryService inventoryService) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.inventoryService = inventoryService;
    }
    
    /**
     * IMPROVED: Process one order completely in a single pass
     * Much more efficient than the legacy version's three loops
     */
    public void processOrder(RefactorOrder order) {
        if (!"NEW".equals(order.status())) {
            return; // Only process new orders
        }
        
        // Create processed order
        var processedOrder = new RefactorOrder(
            order.id(), order.customerId(), order.amount(), 
            "PROCESSED", order.orderDate()
        );
        
        // Single logical flow - all operations for this order
        repository.save(processedOrder);
        notificationService.sendOrderConfirmation(processedOrder);
        inventoryService.updateInventory(processedOrder);
    }
    
    /**
     * IMPROVED: Process all new orders efficiently
     */
    public void processAllNewOrders() {
        List<RefactorOrder> newOrders = repository.findByStatus("NEW");
        System.out.printf("Processing %d new orders...%n", newOrders.size());
        
        for (RefactorOrder order : newOrders) {
            processOrder(order);
        }
        
        System.out.println("All new orders processed successfully!");
    }
    
    /**
     * IMPROVED: Clean cancellation logic using injected services
     */
    public void cancelOrder(int orderId) {
        RefactorOrder order = repository.findById(orderId);
        
        if (order == null) {
            System.out.println("Order " + orderId + " not found");
            return;
        }
        
        if ("CANCELLED".equals(order.status())) {
            System.out.println("Order " + orderId + " is already cancelled");
            return;
        }
        
        // Create canceled order
        var cancelledOrder = new RefactorOrder(
            order.id(), order.customerId(), order.amount(), 
            "CANCELLED", order.orderDate()
        );
        
        // Single logical flow
        repository.save(cancelledOrder);
        notificationService.sendCancellationNotice(cancelledOrder);
        inventoryService.restoreInventory(cancelledOrder);
        
        System.out.println("Order " + orderId + " cancelled successfully");
    }
    
    /**
     * Demonstration method showing the difference between legacy and refactored code
     */
    public static void main(String[] args) {
        System.out.println("=== Refactoring Demo: Legacy vs Clean Code ===");
        
        // Sample orders
        var orders = List.of(
            new RefactorOrder(1001, "CUST-001", 299.99, "NEW", LocalDateTime.now()),
            new RefactorOrder(1002, "CUST-002", 149.50, "NEW", LocalDateTime.now()),
            new RefactorOrder(1003, "CUST-003", 89.99, "NEW", LocalDateTime.now())
        );
        
        System.out.println("\n--- BEFORE: Legacy Code (God Class, Multiple Loops) ---");
        var legacyProcessor = new LegacyOrderProcessor();
        orders.forEach(legacyProcessor::addOrder);
        legacyProcessor.processOrders(); // Inefficient: 3 separate loops!
        
        System.out.println("\n--- AFTER: Refactored Code (Clean, Efficient, Testable) ---");
        
        // Create services (in real app, these would be injected)
        var repository = new RefactorOrderRepository();
        var notificationService = new RefactorNotificationService();
        var inventoryService = new RefactorInventoryService();
        
        // Add orders to repository
        orders.forEach(repository::save);
        
        // Create clean processor
        var cleanProcessor = new LegacyCodeExample(repository, notificationService, inventoryService);
        cleanProcessor.processAllNewOrders(); // Efficient: Single pass!
        
        System.out.println("""
                
                === Key Improvements Demonstrated ===
                ✅ Single Responsibility: Each class has one job
                ✅ DRY Principle: No duplicated order-finding logic
                ✅ Performance: Single pass vs three loops
                ✅ Testability: Dependencies can be mocked
                ✅ Maintainability: Easy to understand and modify
                ✅ Extensibility: Easy to add new services
                
                💡 Remember: Never refactor without a good test suite!
                Tests give you the confidence and freedom to improve code safely.""");
    }
}