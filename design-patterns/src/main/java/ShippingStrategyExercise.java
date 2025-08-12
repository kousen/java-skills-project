import java.util.function.Function;

/**
 * Try It Out Exercise: Strategy Pattern for Shipping Cost Calculation
 * <p>
 * This exercise demonstrates the Strategy pattern using a simple, practical example:
 * calculating shipping costs for different service levels.
 * <p>
 * The Strategy pattern is perfect for business rules that vary by context.
 * Instead of using if/else chains, we encapsulate each calculation method
 * as a separate strategy.
 * <p>
 * TODO: Complete the missing strategy implementations below.
 */

// Data container for shipping information
record ShippingData(String destination, double weight, double distance, String serviceType) {
    
    // Constructor for domestic shipping
    public ShippingData(double weight, double distance, String serviceType) {
        this("Domestic", weight, distance, serviceType);
    }
}

// Modern Strategy Pattern using Function interface and lambdas
class ShippingCalculations {
    
    // TODO: Implement STANDARD shipping strategy
    // Rules: $5.00 base rate + $0.50 per pound + $0.10 per mile
    public static final Function<ShippingData, Double> STANDARD = data -> {
        // TODO: Replace this with actual calculation
        return 0.0;
    };
    
    // TODO: Implement EXPRESS shipping strategy  
    // Rules: $12.00 base rate + $0.75 per pound + $0.15 per mile
    public static final Function<ShippingData, Double> EXPRESS = data -> {
        // TODO: Replace this with actual calculation
        return 0.0;
    };
    
    // TODO: Implement OVERNIGHT shipping strategy
    // Rules: $25.00 base rate + $1.00 per pound + $0.25 per mile
    public static final Function<ShippingData, Double> OVERNIGHT = data -> {
        // TODO: Replace this with actual calculation  
        return 0.0;
    };
    
    // COMPLETED: International shipping with customs fee
    public static final Function<ShippingData, Double> INTERNATIONAL = data -> {
        if (!"International".equals(data.destination())) {
            throw new IllegalArgumentException("International strategy requires international destination");
        }
        
        double baseRate = 30.0;
        double weightRate = 2.0;
        double distanceRate = 0.50;
        double customsFee = 15.0;
        
        return baseRate + (data.weight() * weightRate) + (data.distance() * distanceRate) + customsFee;
    };
}

// Context class that uses shipping strategies
class ShippingCalculator {
    private Function<ShippingData, Double> shippingStrategy;
    private String strategyName;
    
    public ShippingCalculator(Function<ShippingData, Double> strategy, String name) {
        this.shippingStrategy = strategy;
        this.strategyName = name;
    }
    
    public void setStrategy(Function<ShippingData, Double> strategy, String name) {
        this.shippingStrategy = strategy;
        this.strategyName = name;
    }
    
    public double calculateShipping(ShippingData data) {
        if (shippingStrategy == null) {
            throw new IllegalStateException("Shipping strategy not set");
        }
        return shippingStrategy.apply(data);
    }
    
    public String getQuote(ShippingData data) {
        double cost = calculateShipping(data);
        return String.format("%s Shipping: $%.2f (%.1f lbs, %.1f miles)", 
                           strategyName, cost, data.weight(), data.distance());
    }
    
    public String getStrategyName() {
        return strategyName;
    }
}

// Demo class showing the Strategy pattern in action
public class ShippingStrategyExercise {
    
    public static void main(String[] args) {
        demonstrateShippingStrategies();
    }
    
    public static void demonstrateShippingStrategies() {
        System.out.println("=== Shipping Strategy Pattern Demo ===");
        
        // Create test shipping data: 5 lbs package going 100 miles
        var packageData = new ShippingData(5.0, 100.0, "Standard");
        
        // Test all shipping strategies
        var calculator = new ShippingCalculator(ShippingCalculations.STANDARD, "Standard");
        System.out.println(calculator.getQuote(packageData));
        
        calculator.setStrategy(ShippingCalculations.EXPRESS, "Express");
        System.out.println(calculator.getQuote(packageData));
        
        calculator.setStrategy(ShippingCalculations.OVERNIGHT, "Overnight");
        System.out.println(calculator.getQuote(packageData));
        
        // Test international shipping
        var intlData = new ShippingData("International", 5.0, 2000.0, "International");
        calculator.setStrategy(ShippingCalculations.INTERNATIONAL, "International");
        System.out.println(calculator.getQuote(intlData));
        
        // Demonstrate custom strategy with lambda
        System.out.println("\n=== Custom Strategy Demo ===");
        Function<ShippingData, Double> bulkDiscount = data -> {
            double standardCost = ShippingCalculations.STANDARD.apply(data);
            // 20% discount for packages over 10 lbs
            return data.weight() > 10.0 ? standardCost * 0.8 : standardCost;
        };
        
        calculator.setStrategy(bulkDiscount, "Bulk Discount");
        var heavyPackage = new ShippingData(15.0, 100.0, "Bulk");
        System.out.println(calculator.getQuote(heavyPackage));
        
        System.out.println("\n=== Strategy Pattern Benefits ===");
        System.out.println("✓ Each shipping method is isolated and testable");
        System.out.println("✓ Easy to add new shipping strategies");
        System.out.println("✓ Business rules are separate from application logic");
        System.out.println("✓ Can switch strategies at runtime");
    }
}