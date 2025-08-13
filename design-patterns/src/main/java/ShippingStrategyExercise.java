import java.util.List;
import java.util.Map;
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

// Modern context class using Function-based strategies
class ShippingCalculator {
    private Function<ShippingData, Double> shippingStrategy =
            ShippingCalculations.STANDARD; // Default strategy
    
    public void setStrategy(Function<ShippingData, Double> strategy) {
        this.shippingStrategy = strategy;
    }
    
    public double calculateShipping(ShippingData data) {
        if (data == null) {
            throw new IllegalArgumentException("Shipping data cannot be null");
        }
        return shippingStrategy.apply(data);
    }
    
    public String getShippingSummary(ShippingData data) {
        double cost = calculateShipping(data);
        return """
               Package Details:
                 Weight: %.1f lbs
                 Distance: %.1f miles
                 Destination: %s
               Shipping Cost: $%.2f""".formatted(data.weight(), data.distance(),
                data.destination(), cost);
    }
}

// Demo class showing the Strategy pattern in action
public class ShippingStrategyExercise {
    
    public static void main(String[] args) {
        demonstrateShippingStrategies();
    }
    
    public static void demonstrateShippingStrategies() {
        System.out.println("=== Modern Shipping Strategy Pattern Demo (2025) ===");
        
        var calculator = new ShippingCalculator(); // Uses default STANDARD strategy
        
        // 1. Default strategy
        System.out.println("\n--- 1. Default Strategy: Standard Shipping ---");
        var package1 = new ShippingData(5.0, 100.0, "Standard");
        System.out.println(calculator.getShippingSummary(package1));
        
        // 2. Switch strategies
        System.out.println("\n--- 2. Switch Strategy: Express Shipping ---");
        calculator.setStrategy(ShippingCalculations.EXPRESS);
        System.out.println(calculator.getShippingSummary(package1));
        
        System.out.println("\n--- 3. Overnight Shipping ---");
        calculator.setStrategy(ShippingCalculations.OVERNIGHT);
        var package2 = new ShippingData(3.0, 50.0, "Overnight");
        System.out.println(calculator.getShippingSummary(package2));
        
        // 3. Custom lambda strategy
        System.out.println("\n--- 4. Custom Strategy: Bulk Discount ---");
        Function<ShippingData, Double> bulkDiscount = data -> {
            double standardCost = 5.0 + (data.weight() * 0.50) + (data.distance() * 0.10);
            // 20% discount for packages over 10 lbs
            return data.weight() > 10.0 ? standardCost * 0.8 : standardCost;
        };
        
        calculator.setStrategy(bulkDiscount);
        var heavyPackage = new ShippingData(15.0, 200.0, "Bulk");
        System.out.println(calculator.getShippingSummary(heavyPackage));
        
        // 4. Batch processing with strategy map
        System.out.println("\n--- 5. Batch Processing: Multiple Packages ---");
        
        // Strategy registry for different package types
        Map<String, Function<ShippingData, Double>> shippingStrategies = Map.of(
            "Standard Package", ShippingCalculations.STANDARD,
            "Express Package", ShippingCalculations.EXPRESS,
            "Overnight Package", ShippingCalculations.OVERNIGHT,
            "Bulk Package", bulkDiscount
        );
        
        // Create batch of shipping data
        List<ShippingData> shippingBatch = List.of(
            new ShippingData(2.0, 50.0, "Standard Package"),
            new ShippingData(5.0, 150.0, "Express Package"),
            new ShippingData(1.0, 25.0, "Overnight Package"),
            new ShippingData(20.0, 300.0, "Bulk Package")
        );
        
        // Process all shipments in parallel
        List<Double> costs = shippingBatch.parallelStream()
            .map(data -> shippingStrategies.get(data.serviceType()).apply(data))
            .toList();
        
        // Display batch results
        System.out.println("Batch shipping costs:");
        for (int i = 0; i < shippingBatch.size(); i++) {
            var data = shippingBatch.get(i);
            var cost = costs.get(i);
            System.out.printf("  %s (%.1f lbs, %.0f mi): $%.2f%n", 
                data.serviceType(), data.weight(), data.distance(), cost);
        }
        
        System.out.println("\n=== Strategy Pattern Benefits ===");
        System.out.println("✓ Runtime strategy switching for different shipping needs");
        System.out.println("✓ Easy to add new shipping calculation methods");
        System.out.println("✓ Batch processing with parallel streams for performance");
        System.out.println("✓ Clean separation of shipping rules from application logic");
    }
}