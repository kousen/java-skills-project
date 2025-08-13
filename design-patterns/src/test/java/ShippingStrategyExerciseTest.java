import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;
import java.util.function.Function;

@DisplayName("Shipping Strategy Exercise Tests")
class ShippingStrategyExerciseTest {
    
    private ShippingData testPackage;
    private ShippingCalculator calculator;
    
    @BeforeEach
    void setUp() {
        testPackage = new ShippingData(5.0, 100.0, "Standard");
        calculator = new ShippingCalculator(); // Uses default STANDARD strategy
    }
    
    @Test
    @DisplayName("Standard shipping strategy should calculate correctly")
    void standardShippingStrategyShouldCalculateCorrectly() {
        // Standard: $5.00 base + $0.50 per pound + $0.10 per mile
        // 5 lbs, 100 miles = $5.00 + $2.50 + $10.00 = $17.50
        double cost = ShippingCalculations.STANDARD.apply(testPackage);
        
        assertThat(cost).isEqualTo(17.50);
    }
    
    @Test
    @DisplayName("Express shipping strategy should calculate correctly")
    void expressShippingStrategyShouldCalculateCorrectly() {
        // Express: $12.00 base + $0.75 per pound + $0.15 per mile
        // 5 lbs, 100 miles = $12.00 + $3.75 + $15.00 = $30.75
        double cost = ShippingCalculations.EXPRESS.apply(testPackage);
        
        assertThat(cost).isEqualTo(30.75);
    }
    
    @Test
    @DisplayName("Overnight shipping strategy should calculate correctly")
    void overnightShippingStrategyShouldCalculateCorrectly() {
        // Overnight: $25.00 base + $1.00 per pound + $0.25 per mile
        // 5 lbs, 100 miles = $25.00 + $5.00 + $25.00 = $55.00
        double cost = ShippingCalculations.OVERNIGHT.apply(testPackage);
        
        assertThat(cost).isEqualTo(55.00);
    }
    
    @Test
    @DisplayName("International shipping strategy should calculate correctly")
    void internationalShippingStrategyShouldCalculateCorrectly() {
        ShippingData intlPackage = new ShippingData("International", 5.0, 2000.0, "International");
        
        // International: $30.00 base + $2.00 per pound + $0.50 per mile + $15.00 customs
        // 5 lbs, 2000 miles = $30.00 + $10.00 + $1000.00 + $15.00 = $1055.00
        double cost = ShippingCalculations.INTERNATIONAL.apply(intlPackage);
        
        assertThat(cost).isEqualTo(1055.00);
    }
    
    @Test
    @DisplayName("International strategy should reject domestic destinations")
    void internationalStrategyShouldRejectDomesticDestinations() {
        ShippingData domesticPackage = new ShippingData("Domestic", 5.0, 100.0, "Standard");
        
        assertThatThrownBy(() -> ShippingCalculations.INTERNATIONAL.apply(domesticPackage))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("International strategy requires international destination");
    }
    
    @Test
    @DisplayName("Calculator should use strategy correctly")
    void calculatorShouldUseStrategyCorrectly() {
        calculator.setStrategy(ShippingCalculations.EXPRESS);
        
        double cost = calculator.calculateShipping(testPackage);
        
        assertThat(cost).isEqualTo(30.75); // Express rate
    }
    
    @Test
    @DisplayName("Calculator should allow strategy switching")
    void calculatorShouldAllowStrategySwitching() {
        // Start with Standard (default)
        double standardCost = calculator.calculateShipping(testPackage);
        
        // Switch to Overnight
        calculator.setStrategy(ShippingCalculations.OVERNIGHT);
        double overnightCost = calculator.calculateShipping(testPackage);
        
        assertThat(standardCost).isEqualTo(17.50);
        assertThat(overnightCost).isEqualTo(55.00);
        assertThat(standardCost).isNotEqualTo(overnightCost);
    }
    
    @Test
    @DisplayName("Calculator should reject null shipping data")
    void calculatorShouldRejectNullShippingData() {
        assertThatThrownBy(() -> calculator.calculateShipping(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Shipping data cannot be null");
    }
    
    @Test
    @DisplayName("Calculator should generate proper shipping summaries")
    void calculatorShouldGenerateProperShippingSummaries() {
        String summary = calculator.getShippingSummary(testPackage);
        
        assertThat(summary)
            .contains("Package Details")
            .contains("$17.50")
            .contains("5.0 lbs")
            .contains("100.0 miles")
            .contains("Domestic");
    }
    
    @Test
    @DisplayName("Should handle zero weight packages")
    void shouldHandleZeroWeightPackages() {
        ShippingData zeroWeight = new ShippingData(0.0, 50.0, "Standard");
        
        // Standard: $5.00 base + $0.00 weight + $5.00 distance = $10.00
        double cost = ShippingCalculations.STANDARD.apply(zeroWeight);
        
        assertThat(cost).isEqualTo(10.00);
    }
    
    @Test
    @DisplayName("Should handle zero distance packages")
    void shouldHandleZeroDistancePackages() {
        ShippingData zeroDistance = new ShippingData(10.0, 0.0, "Standard");
        
        // Standard: $5.00 base + $5.00 weight + $0.00 distance = $10.00
        double cost = ShippingCalculations.STANDARD.apply(zeroDistance);
        
        assertThat(cost).isEqualTo(10.00);
    }
    
    @Test
    @DisplayName("Custom strategy should work with calculator")
    void customStrategyShouldWorkWithCalculator() {
        // Custom strategy: flat rate of $20.00
        Function<ShippingData, Double> flatRate = data -> 20.0;
        
        calculator.setStrategy(flatRate);
        double cost = calculator.calculateShipping(testPackage);
        
        assertThat(cost).isEqualTo(20.00);
    }
    
    @Test
    @DisplayName("All strategies should handle large packages")
    void allStrategiesShouldHandleLargePackages() {
        ShippingData largePackage = new ShippingData(100.0, 1000.0, "Heavy");
        
        double standardCost = ShippingCalculations.STANDARD.apply(largePackage);
        double expressCost = ShippingCalculations.EXPRESS.apply(largePackage);
        double overnightCost = ShippingCalculations.OVERNIGHT.apply(largePackage);
        
        // Verify costs increase with service level
        assertThat(expressCost).isGreaterThan(standardCost);
        assertThat(overnightCost).isGreaterThan(expressCost);
        
        // Verify specific calculations
        // Standard: $5 + $50 + $100 = $155
        assertThat(standardCost).isEqualTo(155.00);
        // Express: $12 + $75 + $150 = $237
        assertThat(expressCost).isEqualTo(237.00);
        // Overnight: $25 + $100 + $250 = $375
        assertThat(overnightCost).isEqualTo(375.00);
    }
    
    @Test
    @DisplayName("ShippingData constructors should work correctly")
    void shippingDataConstructorsShouldWorkCorrectly() {
        ShippingData domestic = new ShippingData(5.0, 100.0, "Standard");
        ShippingData international = new ShippingData("International", 5.0, 100.0, "Standard");
        
        assertThat(domestic.destination()).isEqualTo("Domestic");
        assertThat(domestic.weight()).isEqualTo(5.0);
        assertThat(domestic.distance()).isEqualTo(100.0);
        assertThat(domestic.serviceType()).isEqualTo("Standard");
        
        assertThat(international.destination()).isEqualTo("International");
        assertThat(international.weight()).isEqualTo(5.0);
        assertThat(international.distance()).isEqualTo(100.0);
        assertThat(international.serviceType()).isEqualTo("Standard");
    }
}