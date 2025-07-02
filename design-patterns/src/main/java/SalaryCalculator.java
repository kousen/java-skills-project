import java.time.LocalDate;
import java.time.Period;

// Strategy Pattern - Different salary calculation strategies
public interface SalaryCalculator {
    double calculatePay(StrategyEmployee employee, int hoursWorked);
    String getCalculationType();
}

// Concrete strategy for hourly employees
class HourlyCalculator implements SalaryCalculator {
    private final double hourlyRate;
    private final double overtimeRate;
    
    public HourlyCalculator(double hourlyRate) {
        this(hourlyRate, 1.5);
    }
    
    public HourlyCalculator(double hourlyRate, double overtimeRate) {
        if (hourlyRate <= 0) {
            throw new IllegalArgumentException("Hourly rate must be positive");
        }
        if (overtimeRate < 1.0) {
            throw new IllegalArgumentException("Overtime rate must be at least 1.0");
        }
        this.hourlyRate = hourlyRate;
        this.overtimeRate = overtimeRate;
    }
    
    @Override
    public double calculatePay(StrategyEmployee employee, int hoursWorked) {
        if (hoursWorked <= 0) return 0;
        
        if (hoursWorked <= 40) {
            return hoursWorked * hourlyRate;
        } else {
            double regularPay = 40 * hourlyRate;
            double overtimePay = (hoursWorked - 40) * hourlyRate * overtimeRate;
            return regularPay + overtimePay;
        }
    }
    
    @Override
    public String getCalculationType() {
        return String.format("Hourly (Rate: $%.2f, Overtime: %.1fx)", hourlyRate, overtimeRate);
    }
    
    public double getHourlyRate() {
        return hourlyRate;
    }
    
    public double getOvertimeRate() {
        return overtimeRate;
    }
}

// Concrete strategy for salaried employees
class SalariedCalculator implements SalaryCalculator {
    private final double annualSalary;
    private final int payPeriodsPerYear;
    
    public SalariedCalculator(double annualSalary) {
        this(annualSalary, 26); // Bi-weekly by default
    }
    
    public SalariedCalculator(double annualSalary, int payPeriodsPerYear) {
        if (annualSalary <= 0) {
            throw new IllegalArgumentException("Annual salary must be positive");
        }
        if (payPeriodsPerYear <= 0) {
            throw new IllegalArgumentException("Pay periods per year must be positive");
        }
        this.annualSalary = annualSalary;
        this.payPeriodsPerYear = payPeriodsPerYear;
    }
    
    @Override
    public double calculatePay(StrategyEmployee employee, int hoursWorked) {
        // Salaried employees get the same pay regardless of hours worked
        return annualSalary / payPeriodsPerYear;
    }
    
    @Override
    public String getCalculationType() {
        return String.format("Salaried (Annual: $%.2f, Pay Periods: %d)", 
                           annualSalary, payPeriodsPerYear);
    }
    
    public double getAnnualSalary() {
        return annualSalary;
    }
    
    public int getPayPeriodsPerYear() {
        return payPeriodsPerYear;
    }
}

// Concrete strategy for commission-based employees
class CommissionCalculator implements SalaryCalculator {
    private final double baseSalary;
    private final double commissionRate;
    private final double salesTarget;
    
    public CommissionCalculator(double baseSalary, double commissionRate) {
        this(baseSalary, commissionRate, 0);
    }
    
    public CommissionCalculator(double baseSalary, double commissionRate, double salesTarget) {
        if (baseSalary < 0) {
            throw new IllegalArgumentException("Base salary cannot be negative");
        }
        if (commissionRate < 0 || commissionRate > 1) {
            throw new IllegalArgumentException("Commission rate must be between 0 and 1");
        }
        if (salesTarget < 0) {
            throw new IllegalArgumentException("Sales target cannot be negative");
        }
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
        this.salesTarget = salesTarget;
    }
    
    @Override
    public double calculatePay(StrategyEmployee employee, int hoursWorked) {
        // For demo purposes, we'll simulate sales based on hours worked
        // In real implementation, sales would be tracked separately
        double simulatedSales = hoursWorked * 150; // $150 sales per hour worked
        double commission = simulatedSales * commissionRate;
        
        // Bonus commission if sales exceed target
        if (salesTarget > 0 && simulatedSales > salesTarget) {
            double bonusCommission = (simulatedSales - salesTarget) * commissionRate * 0.5;
            commission += bonusCommission;
        }
        
        return baseSalary + commission;
    }
    
    @Override
    public String getCalculationType() {
        return String.format("Commission (Base: $%.2f, Rate: %.1f%%, Target: $%.2f)", 
                           baseSalary, commissionRate * 100, salesTarget);
    }
    
    public double getBaseSalary() {
        return baseSalary;
    }
    
    public double getCommissionRate() {
        return commissionRate;
    }
    
    public double getSalesTarget() {
        return salesTarget;
    }
}

// Context class using the strategy
class PayrollProcessor {
    private SalaryCalculator calculator;
    
    public PayrollProcessor(SalaryCalculator calculator) {
        this.calculator = calculator;
    }
    
    public void setCalculator(SalaryCalculator calculator) {
        this.calculator = calculator;
    }
    
    public double processPayroll(StrategyEmployee employee, int hoursWorked) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (calculator == null) {
            throw new IllegalStateException("Salary calculator not set");
        }
        
        return calculator.calculatePay(employee, hoursWorked);
    }
    
    public String getPayrollSummary(StrategyEmployee employee, int hoursWorked) {
        double pay = processPayroll(employee, hoursWorked);
        return String.format(
            "Employee: %s%nCalculation Type: %s%nHours Worked: %d%nPay Amount: $%.2f",
            employee.getName(), calculator.getCalculationType(), hoursWorked, pay
        );
    }
    
    public SalaryCalculator getCalculator() {
        return calculator;
    }
}

// Simple Employee class for strategy pattern demo
class StrategyEmployee {
    private final String name;
    private final int id;
    private final LocalDate hireDate;
    
    public StrategyEmployee(String name, int id, LocalDate hireDate) {
        this.name = name;
        this.id = id;
        this.hireDate = hireDate;
    }
    
    public String getName() { return name; }
    public int getId() { return id; }
    public LocalDate getHireDate() { return hireDate; }
    
    public int getYearsOfService() {
        return Period.between(hireDate, LocalDate.now()).getYears();
    }
}

// Demo class
class StrategyPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== Strategy Pattern Demo ===");
        
        // Create employees
        StrategyEmployee hourlyEmployee = new StrategyEmployee("Alice Johnson", 1001, LocalDate.of(2020, 3, 15));
        StrategyEmployee salariedEmployee = new StrategyEmployee("Bob Smith", 1002, LocalDate.of(2019, 7, 22));
        StrategyEmployee commissionEmployee = new StrategyEmployee("Carol Davis", 1003, LocalDate.of(2021, 12, 1));
        
        // Create different salary calculators
        SalaryCalculator hourlyCalc = new HourlyCalculator(25.0, 1.5);
        SalaryCalculator salariedCalc = new SalariedCalculator(80000, 26);
        SalaryCalculator commissionCalc = new CommissionCalculator(30000, 0.05, 10000);
        
        // Create payroll processor
        PayrollProcessor processor = new PayrollProcessor(hourlyCalc);
        
        System.out.println("\n--- Hourly Employee ---");
        System.out.println(processor.getPayrollSummary(hourlyEmployee, 45)); // 5 hours overtime
        
        System.out.println("\n--- Salaried Employee ---");
        processor.setCalculator(salariedCalc);
        System.out.println(processor.getPayrollSummary(salariedEmployee, 40));
        
        System.out.println("\n--- Commission Employee ---");
        processor.setCalculator(commissionCalc);
        System.out.println(processor.getPayrollSummary(commissionEmployee, 35));
        
        // Demonstrate strategy switching at runtime
        System.out.println("\n--- Strategy Switching Demo ---");
        StrategyEmployee flexEmployee = new StrategyEmployee("Dave Wilson", 1004, LocalDate.of(2022, 1, 15));
        
        System.out.println("Same employee with different calculation strategies:");
        
        processor.setCalculator(hourlyCalc);
        System.out.println("As Hourly: $" + String.format("%.2f", processor.processPayroll(flexEmployee, 40)));
        
        processor.setCalculator(salariedCalc);
        System.out.println("As Salaried: $" + String.format("%.2f", processor.processPayroll(flexEmployee, 40)));
        
        processor.setCalculator(commissionCalc);
        System.out.println("As Commission: $" + String.format("%.2f", processor.processPayroll(flexEmployee, 40)));
    }
}