

package com.oreilly.javaskills.refactoring;

import java.util.List;
import java.util.ArrayList;

// Topic: Refactor legacy code to improve performance or reduce duplication and technical debt.

// ======================================================================================
// "BEFORE" - Legacy Code Example
// ======================================================================================

/**
 * This class represents a legacy system for processing customer orders.
 * It has several issues:
 * 1.  **Violates SRP:** It handles order processing, customer notification, and inventory management.
 * 2.  **Duplication (Not DRY):** The logic for finding an order is duplicated.
 * 3.  **Hard to Test:** The methods are tightly coupled to the internal data structure.
 * 4.  **Inefficient:** The `processOrders` method iterates over all orders multiple times.
 */
class LegacyOrderProcessor {
    private List<Order> orders = new ArrayList<>();

    public LegacyOrderProcessor(List<Order> orders) {
        this.orders = orders;
    }

    // Processes all orders, sends notifications, and updates inventory
    public void processOrders() {
        for (Order order : orders) {
            if (order.getStatus().equals("NEW")) {
                // Process payment, etc.
                order.setStatus("PROCESSED");
                System.out.println("Order " + order.getId() + " processed.");
            }
        }

        for (Order order : orders) {
            if (order.getStatus().equals("PROCESSED")) {
                // Send email notification
                System.out.println("Email sent for order " + order.getId());
            }
        }

        for (Order order : orders) {
            if (order.getStatus().equals("PROCESSED")) {
                // Update inventory
                System.out.println("Inventory updated for order " + order.getId());
            }
        }
    }

    // Finds a specific order by ID
    public Order findOrder(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }
    // Cancels an order
    public void cancelOrder(int id) {
        Order orderToCancel = null;
        for (Order order : orders) {
            if (order.getId() == id) {
                orderToCancel = order;
                break;
            }
        }
        if (orderToCancel != null && orderToCancel.getStatus().equals("NEW")) {
            orderToCancel.setStatus("CANCELLED");
            System.out.println("Order " + id + " cancelled.");
        }
    }
}

// ======================================================================================
// "AFTER" - Refactored Code Example
// ======================================================================================

/**
 * The refactored system separates responsibilities into different classes.
 */

// 1. Data class is simple and focused (using a record for immutability)
record Order(int id, String customer, String status) { }

// 2. Each responsibility has its own class (SRP)
class OrderRepository {
    private List<Order> orders;

    public OrderRepository(List<Order> orders) {
        this.orders = orders;
    }

    public Order findById(int id) {
        return orders.stream()
                     .filter(o -> o.id() == id)
                     .findFirst()
                     .orElse(null);
    }

    public void save(Order order) {
        // In a real system, this would update the order in the list or DB
        System.out.println("Order " + order.id() + " saved with status: " + order.status());
    }
}

class NotificationService {
    public void sendConfirmation(Order order) {
        System.out.println("Email sent for order " + order.id());
    }
}

class InventoryService {
    public void updateInventory(Order order) {
        System.out.println("Inventory updated for order " + order.id());
    }
}

// 3. The main service class coordinates the work (Facade pattern)
class RefactoredOrderProcessor {
    private final OrderRepository repository;
    private final NotificationService notificationService;
    private final InventoryService inventoryService;

    public RefactoredOrderProcessor(OrderRepository repository, 
                                  NotificationService notificationService, 
                                  InventoryService inventoryService) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.inventoryService = inventoryService;
    }

    public void processOrder(Order order) {
        if (order.status().equals("NEW")) {
            Order processedOrder = new Order(order.id(), order.customer(), "PROCESSED");
            repository.save(processedOrder);
            notificationService.sendConfirmation(processedOrder);
            inventoryService.updateInventory(processedOrder);
        }
    }

    public void cancelOrder(int orderId) {
        Order order = repository.findById(orderId);
        if (order != null && order.status().equals("NEW")) {
            Order cancelledOrder = new Order(order.id(), order.customer(), "CANCELLED");
            repository.save(cancelledOrder);
        }
    }
}

