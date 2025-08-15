import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Try It Out Exercise: Single Responsibility Principle
 * <p>
 * This exercise demonstrates SRP by refactoring a monolithic OrderProcessor
 * that violates SRP into separate, focused classes that each handle one
 * clear responsibility.
 * <p>
 * SCENARIO: An e-commerce order processing system that currently does
 * everything in one class. Your job is to separate the concerns.
 * <p>
 * TODO: Complete the missing implementations below to properly separate
 * the responsibilities according to the Single Responsibility Principle.
 */

// Data classes for the exercise
record OrderItem(String productId, String productName, int quantity, BigDecimal unitPrice) {
    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

record ShippingAddress(String street, String city, String state, String zipCode, String country) {
}

record PaymentInfo(String cardNumber, String expiryDate, String cvv, String cardholderName) {
    // Mask sensitive data for display
    public String getMaskedCardNumber() {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}

// Main order data class
class Order {
    private final String orderId;
    private final String customerId;
    private final List<OrderItem> items;
    private final ShippingAddress shippingAddress;
    private final PaymentInfo paymentInfo;
    private final LocalDateTime orderDate;
    private String status;

    public Order(String orderId, String customerId, List<OrderItem> items,
                 ShippingAddress shippingAddress, PaymentInfo paymentInfo) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.paymentInfo = paymentInfo;
        this.orderDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getSubtotal() {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

// TODO: Implement OrderValidator
// Requirements:
// - Validates that order has items (not empty)
// - Validates that all quantities are positive
// - Validates that shipping address is complete
// - Validates that payment info is present
// - Method: validateOrder(Order order) returns boolean
class OrderValidator {

    public boolean validateOrder(Order order) {
        // TODO: Implement order validation logic
        // Check: order has items, quantities > 0, address complete, payment present
        System.out.println("TODO: Implement order validation");
        return true; // Minimal implementation for compilation
    }

    public String getValidationErrors(Order order) {
        // TODO: Return specific validation error messages
        // Useful for providing feedback to users
        return "TODO: Implement validation error reporting";
    }
}

// TODO: Implement PaymentProcessor  
// Requirements:
// - Processes payment using payment info
// - Returns transaction ID on success
// - Handles payment failures gracefully
// - Method: processPayment(PaymentInfo paymentInfo, BigDecimal amount) returns String
class PaymentProcessor {

    public String processPayment(PaymentInfo paymentInfo, BigDecimal amount) {
        // TODO: Implement payment processing logic
        // Simulate credit card processing, return transaction ID
        System.out.println("TODO: Process payment of " + amount);
        return "TXN-TODO-12345"; // Minimal implementation for compilation
    }

    public boolean refundPayment(String transactionId, BigDecimal amount) {
        // TODO: Implement refund logic
        System.out.println("TODO: Refund payment " + transactionId);
        return true; // Minimal implementation for compilation
    }
}

// TODO: Implement InventoryService
// Requirements:
// - Checks if items are in stock
// - Reserves items for the order
// - Releases reserved items if order fails
// - Method: reserveItems(List<OrderItem> items) returns boolean
class InventoryService {

    public boolean checkAvailability(List<OrderItem> items) {
        // TODO: Check if all items are available in requested quantities
        System.out.println("TODO: Check inventory availability");
        return true; // Minimal implementation for compilation
    }

    public boolean reserveItems(List<OrderItem> items) {
        // TODO: Reserve items in inventory
        // Prevent other orders from claiming these items
        System.out.println("TODO: Reserve inventory items");
        return true; // Minimal implementation for compilation
    }

    public void releaseReservation(List<OrderItem> items) {
        // TODO: Release reserved items back to available inventory
        System.out.println("TODO: Release inventory reservation");
    }
}

// TODO: Implement OrderNotificationService
// Requirements:
// - Sends order confirmation emails
// - Sends shipping notifications
// - Sends order status updates
// - Method: sendOrderConfirmation(Order order) 
class OrderNotificationService {

    public void sendOrderConfirmation(Order order) {
        // TODO: Send confirmation email to customer
        // Include order details, estimated delivery, etc.
        System.out.println("TODO: Send order confirmation email");
    }

    public void sendShippingNotification(Order order, String trackingNumber) {
        // TODO: Send shipping notification with tracking info
        System.out.println("TODO: Send shipping notification");
    }

    public void sendOrderStatusUpdate(Order order, String oldStatus, String newStatus) {
        // TODO: Send status change notification
        System.out.println("TODO: Send status update notification");
    }
}

// TODO: Implement ShippingCalculator
// Requirements:
// - Calculates shipping cost based on address and items
// - Determines estimated delivery date
// - Handles different shipping methods
// - Method: calculateShippingCost(Order order) returns BigDecimal
class ShippingCalculator {

    public BigDecimal calculateShippingCost(Order order) {
        // TODO: Calculate shipping cost based on:
        // - Item weight/size, destination, shipping method
        System.out.println("TODO: Calculate shipping cost");
        return BigDecimal.valueOf(9.99); // Minimal implementation for compilation
    }

    public LocalDateTime estimateDeliveryDate(Order order) {
        // TODO: Estimate delivery date based on shipping method and location
        System.out.println("TODO: Estimate delivery date");
        return LocalDateTime.now().plusDays(3); // Minimal implementation for compilation
    }

    public List<String> getAvailableShippingMethods(ShippingAddress address) {
        // TODO: Return available shipping methods for the address
        return List.of("STANDARD", "EXPRESS", "OVERNIGHT"); // Minimal implementation
    }
}

// Coordinator class - demonstrates proper SRP with dependency injection
@SuppressWarnings({"WeakerAccess", "ClassEscapesDefinedScope"})
public class SRPOrderProcessorExercise {
    private final OrderValidator validator;
    private final PaymentProcessor paymentProcessor;
    private final InventoryService inventoryService;
    private final OrderNotificationService notificationService;
    private final ShippingCalculator shippingCalculator;

    // Dependency injection constructor
    public SRPOrderProcessorExercise(OrderValidator validator,
                                     PaymentProcessor paymentProcessor,
                                     InventoryService inventoryService,
                                     OrderNotificationService notificationService,
                                     ShippingCalculator shippingCalculator) {
        this.validator = validator;
        this.paymentProcessor = paymentProcessor;
        this.inventoryService = inventoryService;
        this.notificationService = notificationService;
        this.shippingCalculator = shippingCalculator;
    }

    public String processOrder(Order order) {
        try {
            // Step 1: Validate order
            if (!validator.validateOrder(order)) {
                return "Order validation failed: " + validator.getValidationErrors(order);
            }

            // Step 2: Check inventory
            if (!inventoryService.checkAvailability(order.getItems())) {
                return "Some items are not available";
            }

            // Step 3: Reserve inventory
            if (!inventoryService.reserveItems(order.getItems())) {
                return "Failed to reserve inventory";
            }

            // Step 4: Calculate total (subtotal + shipping)
            BigDecimal subtotal = order.getSubtotal();
            BigDecimal shipping = shippingCalculator.calculateShippingCost(order);
            BigDecimal total = subtotal.add(shipping);

            // Step 5: Process payment
            String transactionId = paymentProcessor.processPayment(order.getPaymentInfo(), total);
            if (transactionId == null) {
                inventoryService.releaseReservation(order.getItems());
                return "Payment processing failed";
            }

            // Step 6: Update order status and send confirmation
            order.setStatus("CONFIRMED");
            notificationService.sendOrderConfirmation(order);

            return "Order processed successfully. Transaction ID: " + transactionId;

        } catch (Exception e) {
            // Clean up on failure
            inventoryService.releaseReservation(order.getItems());
            return "Order processing failed: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        demonstrateOrderProcessing();
    }

    public static void demonstrateOrderProcessing() {
        System.out.println("=== Single Responsibility Principle - Order Processing Exercise ===");

        // Create dependencies (in real app, these would be injected)
        var validator = new OrderValidator();
        var paymentProcessor = new PaymentProcessor();
        var inventoryService = new InventoryService();
        var notificationService = new OrderNotificationService();
        var shippingCalculator = new ShippingCalculator();

        // Create the order processor with dependencies
        var orderProcessor = new SRPOrderProcessorExercise(
                validator, paymentProcessor, inventoryService,
                notificationService, shippingCalculator
        );

        // Create sample order
        var items = List.of(
                new OrderItem("BOOK-001", "Java Programming Guide", 2, BigDecimal.valueOf(39.99)),
                new OrderItem("MUG-002", "Coffee Mug", 1, BigDecimal.valueOf(12.99))
        );

        var address = new ShippingAddress(
                "123 Main St", "Anytown", "CA", "90210", "USA"
        );

        var payment = new PaymentInfo(
                "4532123456789012", "12/25", "123", "John Doe"
        );

        var order = new Order("ORD-001", "CUST-123", items, address, payment);

        // Process the order
        System.out.printf("""
                        
                        --- Processing Order ---
                        Order ID: %s
                        Customer: %s
                        Items: %d
                        Subtotal: $%s
                        %n""",
                order.getOrderId(), order.getCustomerId(),
                order.getItems().size(), order.getSubtotal());

        String result = orderProcessor.processOrder(order);
        System.out.println("\nResult: " + result);
        System.out.println("Final Status: " + order.getStatus());

        System.out.println("""
                
                === SRP Benefits Demonstrated ===
                ✓ OrderValidator: Handles validation logic only
                ✓ PaymentProcessor: Handles payment processing only
                ✓ InventoryService: Handles inventory management only
                ✓ OrderNotificationService: Handles order notifications only
                ✓ ShippingCalculator: Handles shipping calculations only
                ✓ SRPOrderProcessorExercise: Coordinates the workflow only
                
                💡 KEY INSIGHT: We simplified a complex problem by ADDING classes!
                6 focused classes are easier to understand than 1 bloated class.
                
                📝 TO COMPLETE THIS EXERCISE:
                1. Implement the TODO methods in each service class
                2. Add proper validation logic to OrderValidator
                3. Simulate payment processing in PaymentProcessor
                4. Implement inventory checking in InventoryService
                5. Add email sending logic to OrderNotificationService
                6. Calculate realistic shipping costs in ShippingCalculator
                7. Notice how each class has ONE clear responsibility!
                """);
    }
}