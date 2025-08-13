import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.util.function.Function;

@DisplayName("Modern Lambda-based Strategy Pattern Tests")
class SalaryCalculatorTest {
    
    private PayrollEmployee employee;
    private PayrollProcessor processor;
    
    @BeforeEach
    void setUp() {
        employee = new PayrollEmployee("Alice Johnson", 1001,
                LocalDate.of(2020, 3, 15));
        processor = new PayrollProcessor();
    }
    
    @Test
    @DisplayName("Hourly strategy should calculate regular pay correctly")
    void hourlyStrategyShouldCalculateRegularPayCorrectly() {
        var payrollData = new PayrollData(employee, 40, 25.0);
        
        double pay = PayrollCalculations.HOURLY.apply(payrollData);
        
        assertThat(pay).isEqualTo(1000.0); // 40 hours * $25/hour
    }
    
    @Test
    @DisplayName("Hourly strategy should calculate overtime pay correctly")
    void hourlyStrategyShouldCalculateOvertimePayCorrectly() {
        var payrollData = new PayrollData(employee, 45, 25.0);
        
        double pay = PayrollCalculations.HOURLY.apply(payrollData);
        
        double expectedPay = (40 * 25.0) + (5 * 25.0 * 1.5); // Regular + overtime
        assertThat(pay).isEqualTo(expectedPay);
    }
    
    @Test
    @DisplayName("Hourly strategy should reject missing required data")
    void hourlyStrategyShouldRejectMissingRequiredData() {
        var noHours = new PayrollData(employee, null, null, 25.0, null, null, null);
        var noRate = new PayrollData(employee, 40, null, null, null, null, null);
        
        assertThatThrownBy(() -> PayrollCalculations.HOURLY.apply(noHours))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Hours worked and hourly rate required");
            
        assertThatThrownBy(() -> PayrollCalculations.HOURLY.apply(noRate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Hours worked and hourly rate required");
    }
    
    @Test
    @DisplayName("Salaried strategy should calculate pay correctly")
    void salariedStrategyShouldCalculatePayCorrectly() {
        var payrollData = new PayrollData(employee, 78000.0);
        
        double pay = PayrollCalculations.SALARIED.apply(payrollData);
        
        assertThat(pay).isEqualTo(3000.0); // $78,000 / 26 pay periods
    }
    
    @Test
    @DisplayName("Salaried strategy should ignore hours worked")
    void salariedStrategyShouldIgnoreHoursWorked() {
        var payrollData40 = new PayrollData(employee, 40, null, null, 78000.0, null, null);
        var payrollData60 = new PayrollData(employee, 60, null, null, 78000.0, null, null);
        
        double pay40Hours = PayrollCalculations.SALARIED.apply(payrollData40);
        double pay60Hours = PayrollCalculations.SALARIED.apply(payrollData60);
        
        assertThat(pay40Hours).isEqualTo(pay60Hours);
    }
    
    @Test
    @DisplayName("Base plus commission strategy should calculate correctly")
    void basePlusCommissionStrategyShouldCalculateCorrectly() {
        var payrollData = new PayrollData(employee, 6000.0, 30000.0, 0.05); // $6k sales, $30k base, 5%
        
        double pay = PayrollCalculations.BASE_PLUS_COMMISSION.apply(payrollData);
        
        double expectedCommission = 6000 * 0.05; // $300 commission
        double expectedPay = 30000 + expectedCommission; // Base + commission
        
        assertThat(pay).isEqualTo(expectedPay);
    }
    
    @Test
    @DisplayName("Commission-only strategy should calculate correctly")
    void commissionOnlyStrategyShouldCalculateCorrectly() {
        var payrollData = new PayrollData(employee, null, 10000.0, null, null, null, 0.08);
        
        double pay = PayrollCalculations.COMMISSION.apply(payrollData);
        
        double expectedPay = 10000 * 0.08; // $800 commission
        assertThat(pay).isEqualTo(expectedPay);
    }
    
    @Test
    @DisplayName("Payroll processor should use lambda strategy correctly")
    void payrollProcessorShouldUseLambdaStrategyCorrectly() {
        processor.setCalculator(PayrollCalculations.HOURLY);
        var payrollData = new PayrollData(employee, 40, 25.0);
        
        double pay = processor.processPayroll(payrollData);
        
        assertThat(pay).isEqualTo(1000.0);
    }
    
    @Test
    @DisplayName("Payroll processor should allow strategy switching")
    void payrollProcessorShouldAllowStrategySwitching() {
        processor.setCalculator(PayrollCalculations.HOURLY);
        var hourlyData = new PayrollData(employee, 40, 25.0);
        
        double hourlyPay = processor.processPayroll(hourlyData);
        
        processor.setCalculator(PayrollCalculations.SALARIED);
        var salariedData = new PayrollData(employee, 78000.0);
        double salariedPay = processor.processPayroll(salariedData);
        
        assertThat(hourlyPay).isNotEqualTo(salariedPay);
        assertThat(hourlyPay).isEqualTo(1000.0);
        assertThat(salariedPay).isEqualTo(3000.0);
    }
    
    @Test
    @DisplayName("Payroll processor should reject null payroll data")
    void payrollProcessorShouldRejectNullPayrollData() {
        var testProcessor = new PayrollProcessor(PayrollCalculations.HOURLY, "Test Hourly");
        
        assertThatThrownBy(() -> testProcessor.processPayroll(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Payroll data and employee cannot be null");
    }
    
    @Test
    @DisplayName("Payroll processor should reject null calculator")
    void payrollProcessorShouldRejectNullCalculator() {
        var nullProcessor = new PayrollProcessor(null, "Null Calculator");
        var payrollData = new PayrollData(employee, 40, 25.0);
        
        assertThatThrownBy(() -> nullProcessor.processPayroll(payrollData))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Pay calculator not set");
    }
    
    @Test
    @DisplayName("Should generate payroll summary")
    void shouldGeneratePayrollSummary() {
        processor.setCalculator(PayrollCalculations.HOURLY);
        var payrollData = new PayrollData(employee, 40, 25.0);
        
        String summary = processor.getPayrollSummary(payrollData);
        
        assertThat(summary)
            .contains("Employee: Alice Johnson")
            .contains("Hours Worked: 40")
            .contains("Hourly Rate: $25.00")
            .contains("Pay Amount: $1000.00")
            .contains("Hourly with Overtime");
    }
    
    @Test
    @DisplayName("Custom lambda strategy should work correctly")
    void customLambdaStrategyShouldWorkCorrectly() {
        // Custom strategy: Consultant rate with no overtime
        Function<PayrollData, Double> consultantStrategy = data -> {
            if (data.hoursWorked() == null || data.hourlyRate() == null) {
                throw new IllegalArgumentException("Hours and rate required for consultant");
            }
            return data.hoursWorked() * data.hourlyRate();
        };
        
        processor.setCalculator(consultantStrategy, "Consultant Rate");
        var payrollData = new PayrollData(employee, 45, 150.0); // 45 hours at $150/hour
        
        double pay = processor.processPayroll(payrollData);
        
        assertThat(pay).isEqualTo(6750.0); // No overtime for consultants
    }
    
    @Test
    @DisplayName("Should demonstrate tiered commission strategy")
    void shouldDemonstrateTieredCommissionStrategy() {
        // Custom tiered commission strategy
        Function<PayrollData, Double> tieredCommission = data -> {
            if (data.baseSalary() == null || data.salesAmount() == null) {
                throw new IllegalArgumentException("Base salary and sales amount required");
            }
            
            double base = data.baseSalary();
            double sales = data.salesAmount();
            
            // Tiered: 5% up to $20k, 8% above $20k, 12% above $50k
            double commission;
            if (sales <= 20000) {
                commission = sales * 0.05;
            } else if (sales <= 50000) {
                commission = (20000 * 0.05) + ((sales - 20000) * 0.08);
            } else {
                commission = (20000 * 0.05) + (30000 * 0.08) + ((sales - 50000) * 0.12);
            }
            
            return base + commission;
        };
        
        processor.setCalculator(tieredCommission, "Tiered Commission");
        var payrollData = new PayrollData(employee, null, 65000.0, null, null, 50000.0, null);
        
        double pay = processor.processPayroll(payrollData);
        
        // Expected: $50k base + $1k (20k*5%) + $2.4k (30k*8%) + $1.8k (15k*12%) = $55.2k
        assertThat(pay).isEqualTo(55200.0);
    }
    
    @Test
    @DisplayName("Custom strategy factory should work for contractor scenario")
    void customStrategyFactoryShouldWorkForContractorScenario() {
        // Test the customStrategy factory method with a contractor strategy
        Function<PayrollData, Double> contractorStrategy = PayrollCalculations.customStrategy(
            "Contractor with daily rate and weekend bonus",
            data -> {
                if (data.hoursWorked() == null || data.hourlyRate() == null) {
                    throw new IllegalArgumentException("Hours and daily rate required for contractor");
                }
                
                int hours = data.hoursWorked();
                double dailyRate = data.hourlyRate(); // Using as daily rate
                
                // Calculate full days (minimum 8 hours = 1 day)
                int fullDays = Math.max(1, (hours + 7) / 8); // Round up to nearest day
                double basePay = fullDays * dailyRate;
                
                // Weekend bonus: 20% if working more than 5 days
                if (fullDays > 5) {
                    basePay *= 1.20; // 20% weekend bonus
                }
                
                return basePay;
            }
        );
        
        processor.setCalculator(contractorStrategy, "Contractor (Custom Strategy Factory)");
        
        // Test regular week (42 hours = 6 days, gets weekend bonus)
        var contractorData = new PayrollData(employee, 42, null, 600.0, null, null, null);
        double pay = processor.processPayroll(contractorData);
        
        // Expected: 6 days * $600 * 1.20 (weekend bonus) = $4,320
        assertThat(pay).isEqualTo(4320.0);
        
        // Test short week (32 hours = 4 days, no weekend bonus)
        var shortWeekData = new PayrollData(employee, 32, null, 600.0, null, null, null);
        double shortPay = processor.processPayroll(shortWeekData);
        
        // Expected: 4 days * $600 = $2,400 (no weekend bonus)
        assertThat(shortPay).isEqualTo(2400.0);
    }
}