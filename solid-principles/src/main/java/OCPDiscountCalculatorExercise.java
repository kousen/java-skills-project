import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Try It Out Exercise: Open/Closed Principle (OCP)
 * <p>
 * This exercise demonstrates OCP by refactoring a discount calculation system
 * that violates OCP (using if-else chains) into a system that follows OCP
 * (using abstractions and polymorphism).
 * <p>
 * SCENARIO: An e-commerce discount system that currently uses if-else logic
 * to handle different discount types. Your job is to refactor it to follow
 * the Open/Closed Principle using abstract classes and polymorphism.
 * <p>
 * TODO: Complete the missing implementations below to properly implement
 * the Open/Closed Principle for the discount calculation system.
 */

// Data classes for the exercise
record Customer(String customerId, String name, CustomerType type, LocalDate joinDate, int totalOrders) {
    public int getYearsAsCustomer() {
        return LocalDate.now().getYear() - joinDate.getYear();
    }
}

enum CustomerType {
    REGULAR, PREMIUM, VIP
}

record Product(String productId, String name, String category, BigDecimal price) {
}

record DiscountOrderItem(Product product, int quantity) {
    public BigDecimal getSubtotal() {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }
}

class DiscountOrder {
    private final String orderId;
    private final Customer customer;
    private final List<DiscountOrderItem> items;
    private final LocalDate orderDate;
    private final String promoCode;

    public DiscountOrder(String orderId, Customer customer, List<DiscountOrderItem> items, String promoCode) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = items;
        this.orderDate = LocalDate.now();
        this.promoCode = promoCode;
    }

    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<DiscountOrderItem> getItems() { return items; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getPromoCode() { return promoCode; }

    public BigDecimal getSubtotal() {
        return items.stream()
                .map(DiscountOrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

// === VIOLATION OF OCP ===
// This class violates OCP because every new discount type requires modifying this class
class BadDiscountCalculator {
    
    public BigDecimal calculateDiscount(DiscountOrder order, String discountType) {
        BigDecimal subtotal = order.getSubtotal();
        
        // VIOLATION: if-else chain that grows with every new discount type
        if ("STUDENT".equals(discountType)) {
            // 10% student discount
            return subtotal.multiply(BigDecimal.valueOf(0.10));
        } else if ("SENIOR".equals(discountType)) {
            // 15% senior discount
            return subtotal.multiply(BigDecimal.valueOf(0.15));
        } else if ("EMPLOYEE".equals(discountType)) {
            // 20% employee discount
            return subtotal.multiply(BigDecimal.valueOf(0.20));
        } else if ("BULK".equals(discountType)) {
            // 5% bulk discount if order > $100
            if (subtotal.compareTo(BigDecimal.valueOf(100)) > 0) {
                return subtotal.multiply(BigDecimal.valueOf(0.05));
            }
        } else if ("LOYALTY".equals(discountType)) {
            // Loyalty discount based on customer type
            Customer customer = order.getCustomer();
            return switch (customer.type()) {
                case PREMIUM -> subtotal.multiply(BigDecimal.valueOf(0.08));
                case VIP -> subtotal.multiply(BigDecimal.valueOf(0.12));
                default -> BigDecimal.ZERO;
            };
        }
        // What happens when we need HOLIDAY, CLEARANCE, FLASH_SALE discounts?
        // We have to MODIFY this method!
        return BigDecimal.ZERO;
    }
}

// === OCP-COMPLIANT SOLUTION ===

// TODO: Create abstract DiscountStrategy class
// Requirements:
// - Abstract method: calculateDiscount(Order order) returns BigDecimal
// - Method: getDiscountName() returns String (can be abstract or concrete)
// - Optional: helper methods for common discount calculations
abstract class DiscountStrategy {
    
    public abstract BigDecimal calculateDiscount(DiscountOrder order);
    
    public abstract String getDiscountName();
    
    // TODO: Implement abstract calculateDiscount method
    // Each concrete strategy will override this method
    
    // Helper method for percentage-based discounts
    protected BigDecimal applyPercentageDiscount(BigDecimal amount, double percentage) {
        return amount.multiply(BigDecimal.valueOf(percentage));
    }
    
    // Helper method for threshold-based discounts
    protected boolean meetsMinimumAmount(BigDecimal amount, double minimum) {
        return amount.compareTo(BigDecimal.valueOf(minimum)) >= 0;
    }
}

// TODO: Implement StudentDiscountStrategy
// Requirements:
// - Extends DiscountStrategy
// - Applies 10% discount
// - getDiscountName() returns "Student Discount"
class StudentDiscountStrategy extends DiscountStrategy {
    
    @Override
    public BigDecimal calculateDiscount(DiscountOrder order) {
        // TODO: Implement 10% student discount
        System.out.println("TODO: Implement student discount calculation");
        return BigDecimal.ZERO; // Minimal implementation for compilation
    }
    
    @Override
    public String getDiscountName() {
        // TODO: Return appropriate discount name
        return "TODO: Student Discount";
    }
}

// TODO: Implement SeniorDiscountStrategy  
// Requirements:
// - Extends DiscountStrategy
// - Applies 15% discount
// - getDiscountName() returns "Senior Citizen Discount"
class SeniorDiscountStrategy extends DiscountStrategy {
    
    @Override
    public BigDecimal calculateDiscount(DiscountOrder order) {
        // TODO: Implement 15% senior discount
        System.out.println("TODO: Implement senior citizen discount calculation");
        return BigDecimal.ZERO; // Minimal implementation for compilation
    }
    
    @Override
    public String getDiscountName() {
        // TODO: Return appropriate discount name
        return "TODO: Senior Citizen Discount";
    }
}

// TODO: Implement BulkOrderDiscountStrategy
// Requirements:
// - Extends DiscountStrategy
// - Applies 5% discount only if order total > $100
// - getDiscountName() returns "Bulk Order Discount"
// - Use helper method meetsMinimumAmount()
class BulkOrderDiscountStrategy extends DiscountStrategy {
    
    private static final double MINIMUM_AMOUNT = 100.0;
    private static final double DISCOUNT_PERCENTAGE = 0.05;
    
    @Override
    public BigDecimal calculateDiscount(DiscountOrder order) {
        // TODO: Implement bulk order discount
        // Apply 5% discount only if subtotal > $100
        // Use the helper method meetsMinimumAmount()
        System.out.println("TODO: Implement bulk order discount with minimum threshold");
        return BigDecimal.ZERO; // Minimal implementation for compilation
    }
    
    @Override
    public String getDiscountName() {
        return "TODO: Bulk Order Discount";
    }
}

// TODO: Implement LoyaltyDiscountStrategy
// Requirements:
// - Extends DiscountStrategy
// - PREMIUM customers: 8% discount
// - VIP customers: 12% discount
// - REGULAR customers: no discount
// - getDiscountName() returns "Loyalty Discount"
class LoyaltyDiscountStrategy extends DiscountStrategy {
    
    @Override
    public BigDecimal calculateDiscount(DiscountOrder order) {
        // TODO: Implement loyalty-based discount
        // Check customer type and apply appropriate percentage
        // PREMIUM: 8%, VIP: 12%, REGULAR: 0%
        System.out.println("TODO: Implement loyalty discount based on customer type");
        return BigDecimal.ZERO; // Minimal implementation for compilation
    }
    
    @Override
    public String getDiscountName() {
        return "TODO: Loyalty Discount";
    }
}

// TODO: Create a NEW discount strategy (your choice!)
// Requirements:
// - Extends DiscountStrategy
// - Implement any creative discount logic you want
// - Examples: Holiday discount, First-time buyer, Category-specific, etc.
// - This demonstrates how easy it is to ADD new discount types without modifying existing code
class HolidayDiscountStrategy extends DiscountStrategy {
    
    private final LocalDate holidayStart;
    private final LocalDate holidayEnd;
    private final double discountPercentage;
    
    public HolidayDiscountStrategy(LocalDate holidayStart, LocalDate holidayEnd, double discountPercentage) {
        this.holidayStart = holidayStart;
        this.holidayEnd = holidayEnd;
        this.discountPercentage = discountPercentage;
    }
    
    @Override
    public BigDecimal calculateDiscount(DiscountOrder order) {
        // TODO: Implement holiday discount
        // Apply discount only if order date falls within holiday period
        // Use the discountPercentage provided in constructor
        System.out.println("TODO: Implement holiday discount for specific date range");
        return BigDecimal.ZERO; // Minimal implementation for compilation
    }
    
    @Override
    public String getDiscountName() {
        return "TODO: Holiday Special Discount";
    }
}

// OCP-Compliant discount calculator - closed for modification, open for extension
@SuppressWarnings({"WeakerAccess", "ClassEscapesDefinedScope"})
public class OCPDiscountCalculatorExercise {
    
    // This method works with ANY discount strategy - past, present, or future!
    public BigDecimal calculateDiscount(DiscountOrder order, DiscountStrategy strategy) {
        return strategy.calculateDiscount(order);
    }
    
    // Method to apply the best discount from multiple strategies
    public DiscountResult calculateBestDiscount(DiscountOrder order, List<DiscountStrategy> strategies) {
        BigDecimal bestDiscount = BigDecimal.ZERO;
        String bestDiscountName = "No Discount";
        
        for (DiscountStrategy strategy : strategies) {
            BigDecimal discount = strategy.calculateDiscount(order);
            if (discount.compareTo(bestDiscount) > 0) {
                bestDiscount = discount;
                bestDiscountName = strategy.getDiscountName();
            }
        }
        
        return new DiscountResult(bestDiscountName, bestDiscount);
    }
    
    // Result class to hold discount information
    public record DiscountResult(String discountName, BigDecimal discountAmount) {
        public BigDecimal getFinalPrice(BigDecimal originalPrice) {
            return originalPrice.subtract(discountAmount);
        }
    }
    
    public static void main(String[] args) {
        demonstrateDiscountCalculation();
    }
    
    public static void demonstrateDiscountCalculation() {
        System.out.println("=== Open/Closed Principle - Discount Calculator Exercise ===");
        
        // Create sample data
        var customer = new Customer("CUST-001", "Alice Johnson", CustomerType.PREMIUM, 
                                  LocalDate.of(2020, 1, 15), 25);
        
        var products = List.of(
            new Product("BOOK-001", "Java Programming Guide", "Books", BigDecimal.valueOf(45.99)),
            new Product("LAPTOP-002", "Programming Laptop", "Electronics", BigDecimal.valueOf(899.99))
        );
        
        var orderItems = List.of(
            new DiscountOrderItem(products.get(0), 2),  // 2 books
            new DiscountOrderItem(products.get(1), 1)   // 1 laptop
        );
        
        var order = new DiscountOrder("ORD-001", customer, orderItems, "STUDENT2024");
        
        // Create the OCP-compliant calculator
        var calculator = new OCPDiscountCalculatorExercise();
        
        // Create different discount strategies
        var strategies = List.of(
            new StudentDiscountStrategy(),
            new SeniorDiscountStrategy(),
            new BulkOrderDiscountStrategy(),
            new LoyaltyDiscountStrategy(),
            new HolidayDiscountStrategy(LocalDate.now().minusDays(5), LocalDate.now().plusDays(5), 0.20)
        );
        
        System.out.printf("""
                
                --- Processing Order ---
                Order ID: %s
                Customer: %s (%s)
                Items: %d
                Subtotal: $%s
                %n""",
                order.getOrderId(), order.getCustomer().name(), order.getCustomer().type(),
                order.getItems().size(), order.getSubtotal());
        
        // Test individual discount strategies
        System.out.println("\n--- Testing Individual Discount Strategies ---");
        for (DiscountStrategy strategy : strategies) {
            BigDecimal discount = calculator.calculateDiscount(order, strategy);
            System.out.printf("%s: $%s%n", strategy.getDiscountName(), discount);
        }
        
        // Find the best discount
        var bestDiscount = calculator.calculateBestDiscount(order, strategies);
        BigDecimal finalPrice = bestDiscount.getFinalPrice(order.getSubtotal());
        
        System.out.printf("""
                
                --- Best Discount Applied ---
                Discount Type: %s
                Discount Amount: $%s
                Final Price: $%s
                Savings: $%s
                %n""",
                bestDiscount.discountName(), bestDiscount.discountAmount(),
                finalPrice, bestDiscount.discountAmount());
        
        System.out.println("""
                
                === OCP Benefits Demonstrated ===
                ✓ StudentDiscountStrategy: Handles student discounts only
                ✓ SeniorDiscountStrategy: Handles senior discounts only
                ✓ BulkOrderDiscountStrategy: Handles bulk order logic only
                ✓ LoyaltyDiscountStrategy: Handles customer loyalty logic only
                ✓ HolidayDiscountStrategy: Handles date-based discounts only
                ✓ OCPDiscountCalculatorExercise: Coordinates discount calculation only
                
                💡 KEY INSIGHT: We can ADD new discount types without modifying existing code!
                The calculator is CLOSED for modification but OPEN for extension.
                
                📝 TO COMPLETE THIS EXERCISE:
                1. Implement the calculateDiscount() method in each strategy class
                2. Add proper discount logic to StudentDiscountStrategy (10%)
                3. Add proper discount logic to SeniorDiscountStrategy (15%)
                4. Add conditional logic to BulkOrderDiscountStrategy (5% if > $100)
                5. Add customer-type logic to LoyaltyDiscountStrategy (8% Premium, 12% VIP)
                6. Add date-range logic to HolidayDiscountStrategy
                7. Update getDiscountName() methods to return proper names
                8. Try adding your own creative discount strategy!
                9. Notice how OCPDiscountCalculatorExercise NEVER needs to change!
                """);
    }
}