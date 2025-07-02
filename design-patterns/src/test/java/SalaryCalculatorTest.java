import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;

@DisplayName("Salary Calculator Strategy Pattern Tests")
class SalaryCalculatorTest {
    
    private StrategyEmployee employee;
    
    @BeforeEach
    void setUp() {
        employee = new StrategyEmployee("Alice Johnson", 1001, LocalDate.of(2020, 3, 15));
    }
    
    @Test
    @DisplayName("Hourly calculator should calculate regular pay correctly")
    void hourlyCalculatorShouldCalculateRegularPayCorrectly() {
        HourlyCalculator calculator = new HourlyCalculator(25.0);
        
        double pay = calculator.calculatePay(employee, 40);
        
        assertThat(pay).isEqualTo(1000.0); // 40 hours * $25/hour
    }
    
    @Test
    @DisplayName("Hourly calculator should calculate overtime pay correctly")
    void hourlyCalculatorShouldCalculateOvertimePayCorrectly() {
        HourlyCalculator calculator = new HourlyCalculator(25.0, 1.5);
        
        double pay = calculator.calculatePay(employee, 45); // 5 hours overtime
        
        double expectedPay = (40 * 25.0) + (5 * 25.0 * 1.5); // Regular + overtime
        assertThat(pay).isEqualTo(expectedPay);
    }
    
    @Test
    @DisplayName("Hourly calculator should reject invalid rates")
    void hourlyCalculatorShouldRejectInvalidRates() {
        assertThatThrownBy(() -> new HourlyCalculator(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Hourly rate must be positive");
        
        assertThatThrownBy(() -> new HourlyCalculator(25.0, 0.5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Overtime rate must be at least 1.0");
    }
    
    @Test
    @DisplayName("Salaried calculator should calculate pay correctly")
    void salariedCalculatorShouldCalculatePayCorrectly() {
        SalariedCalculator calculator = new SalariedCalculator(78000, 26); // Bi-weekly
        
        double pay = calculator.calculatePay(employee, 40);
        
        assertThat(pay).isEqualTo(3000.0); // $78,000 / 26 pay periods
    }
    
    @Test
    @DisplayName("Salaried calculator should ignore hours worked")
    void salariedCalculatorShouldIgnoreHoursWorked() {
        SalariedCalculator calculator = new SalariedCalculator(78000, 26);
        
        double pay40Hours = calculator.calculatePay(employee, 40);
        double pay60Hours = calculator.calculatePay(employee, 60);
        
        assertThat(pay40Hours).isEqualTo(pay60Hours);
    }
    
    @Test
    @DisplayName("Commission calculator should calculate base plus commission")
    void commissionCalculatorShouldCalculateBasePlusCommission() {
        CommissionCalculator calculator = new CommissionCalculator(30000, 0.05, 10000);
        
        double pay = calculator.calculatePay(employee, 40);
        
        double simulatedSales = 40 * 150; // $6,000 sales
        double expectedCommission = simulatedSales * 0.05; // $300 commission
        double expectedPay = 30000 + expectedCommission; // Base + commission
        
        assertThat(pay).isEqualTo(expectedPay);
    }
    
    @Test
    @DisplayName("Commission calculator should give bonus for exceeding target")
    void commissionCalculatorShouldGiveBonusForExceedingTarget() {
        CommissionCalculator calculator = new CommissionCalculator(30000, 0.05, 5000);
        
        double pay = calculator.calculatePay(employee, 40);
        
        double simulatedSales = 40 * 150; // $6,000 sales (exceeds $5,000 target)
        double regularCommission = simulatedSales * 0.05;
        double bonusCommission = (simulatedSales - 5000) * 0.05 * 0.5; // Bonus on excess
        double expectedPay = 30000 + regularCommission + bonusCommission;
        
        assertThat(pay).isEqualTo(expectedPay);
    }
    
    @Test
    @DisplayName("Payroll processor should use strategy correctly")
    void payrollProcessorShouldUseStrategyCorrectly() {
        HourlyCalculator hourlyCalc = new HourlyCalculator(25.0);
        PayrollProcessor processor = new PayrollProcessor(hourlyCalc);
        
        double pay = processor.processPayroll(employee, 40);
        
        assertThat(pay).isEqualTo(1000.0);
    }
    
    @Test
    @DisplayName("Payroll processor should allow strategy switching")
    void payrollProcessorShouldAllowStrategySwitching() {
        PayrollProcessor processor = new PayrollProcessor(new HourlyCalculator(25.0));
        
        double hourlyPay = processor.processPayroll(employee, 40);
        
        processor.setCalculator(new SalariedCalculator(78000, 26));
        double salariedPay = processor.processPayroll(employee, 40);
        
        assertThat(hourlyPay).isNotEqualTo(salariedPay);
        assertThat(hourlyPay).isEqualTo(1000.0);
        assertThat(salariedPay).isEqualTo(3000.0);
    }
    
    @Test
    @DisplayName("Payroll processor should reject null employee")
    void payrollProcessorShouldRejectNullEmployee() {
        PayrollProcessor processor = new PayrollProcessor(new HourlyCalculator(25.0));
        
        assertThatThrownBy(() -> processor.processPayroll(null, 40))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Employee cannot be null");
    }
    
    @Test
    @DisplayName("Payroll processor should reject null calculator")
    void payrollProcessorShouldRejectNullCalculator() {
        PayrollProcessor processor = new PayrollProcessor(null);
        
        assertThatThrownBy(() -> processor.processPayroll(employee, 40))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Salary calculator not set");
    }
    
    @Test
    @DisplayName("Should generate payroll summary")
    void shouldGeneratePayrollSummary() {
        PayrollProcessor processor = new PayrollProcessor(new HourlyCalculator(25.0));
        
        String summary = processor.getPayrollSummary(employee, 40);
        
        assertThat(summary)
            .contains("Employee: Alice Johnson")
            .contains("Hours Worked: 40")
            .contains("Pay Amount: $1000.00")
            .contains("Hourly");
    }
}