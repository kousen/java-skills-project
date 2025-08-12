import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.function.Function;

// Modern Strategy Pattern using Function interface.
// We use lambdas instead of concrete strategy classes

// Data container for payroll information
record PayrollData(
        StrategyEmployee employee,
        Integer hoursWorked,
        Double salesAmount,
        Double hourlyRate,
        Double annualSalary,
        Double baseSalary,
        Double commissionRate
) {
    
    // Constructor for hourly workers
    public PayrollData(StrategyEmployee employee, int hoursWorked, double hourlyRate) {
        this(employee, hoursWorked, null, hourlyRate,
                null, null, null);
    }
    
    // Constructor for salaried workers
    public PayrollData(StrategyEmployee employee, double annualSalary) {
        this(employee, null, null,
                null, annualSalary, null, null);
    }
    
    // Constructor for commission workers
    public PayrollData(StrategyEmployee employee, double salesAmount, double baseSalary, double commissionRate) {
        this(employee, null, salesAmount, null,
                null, baseSalary, commissionRate);
    }
}

// Modern Strategy Pattern using static methods and lambdas
class PayrollCalculations {
    
    // Standard hourly calculation with overtime
    public static final Function<PayrollData, Double> HOURLY = data -> {
        if (data.hoursWorked() == null || data.hourlyRate() == null) {
            throw new IllegalArgumentException("Hours worked and hourly rate required");
        }
        
        int hours = data.hoursWorked();
        double rate = data.hourlyRate();
        
        if (hours <= 0) return 0.0;
        if (hours <= 40) {
            return hours * rate;
        } else {
            return (40 * rate) + ((hours - 40) * rate * 1.5); // 1.5x overtime
        }
    };
    
    // Salaried calculation (bi-weekly)
    public static final Function<PayrollData, Double> SALARIED = data -> {
        if (data.annualSalary() == null) {
            throw new IllegalArgumentException("Annual salary required");
        }
        return data.annualSalary() / 26; // Bi-weekly
    };
    
    // Commission-only calculation
    public static final Function<PayrollData, Double> COMMISSION = data -> {
        if (data.salesAmount() == null || data.commissionRate() == null) {
            throw new IllegalArgumentException("Sales amount and commission rate required");
        }
        return data.salesAmount() * data.commissionRate();
    };
    
    // Base salary plus commission (for sales people)
    public static final Function<PayrollData, Double> BASE_PLUS_COMMISSION = data -> {
        if (data.baseSalary() == null || data.salesAmount() == null || data.commissionRate() == null) {
            throw new IllegalArgumentException("Base salary, sales amount, and commission rate required");
        }
        return data.baseSalary() + (data.salesAmount() * data.commissionRate());
    };
    
    // Custom strategy factory for complex scenarios
    public static Function<PayrollData, Double> customStrategy(
            String description, Function<PayrollData, Double> calculation) {
        return calculation; // Could add logging, validation, etc.
    }
}

// Modern context class using Function-based strategies
class PayrollProcessor {
    private Function<PayrollData, Double> payCalculator;
    private String calculatorDescription;
    
    public PayrollProcessor(Function<PayrollData, Double> payCalculator, String description) {
        this.payCalculator = payCalculator;
        this.calculatorDescription = description;
    }
    
    public void setCalculator(Function<PayrollData, Double> payCalculator, String description) {
        this.payCalculator = payCalculator;
        this.calculatorDescription = description;
    }
    
    public double processPayroll(PayrollData payrollData) {
        if (payrollData == null || payrollData.employee() == null) {
            throw new IllegalArgumentException("Payroll data and employee cannot be null");
        }
        if (payCalculator == null) {
            throw new IllegalStateException("Pay calculator not set");
        }
        
        return payCalculator.apply(payrollData);
    }
    
    public String getPayrollSummary(PayrollData payrollData) {
        double pay = processPayroll(payrollData);
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("Employee: %s%n", payrollData.employee().name()));
        summary.append(String.format("Calculation Type: %s%n", calculatorDescription));
        
        if (payrollData.hoursWorked() != null) {
            summary.append(String.format("Hours Worked: %d%n", payrollData.hoursWorked()));
        }
        if (payrollData.hourlyRate() != null) {
            summary.append(String.format("Hourly Rate: $%.2f%n", payrollData.hourlyRate()));
        }
        if (payrollData.annualSalary() != null) {
            summary.append(String.format("Annual Salary: $%.2f%n", payrollData.annualSalary()));
        }
        if (payrollData.salesAmount() != null) {
            summary.append(String.format("Sales Amount: $%.2f%n", payrollData.salesAmount()));
        }
        if (payrollData.baseSalary() != null) {
            summary.append(String.format("Base Salary: $%.2f%n", payrollData.baseSalary()));
        }
        if (payrollData.commissionRate() != null) {
            summary.append(String.format("Commission Rate: %.1f%%%n", payrollData.commissionRate() * 100));
        }
        
        summary.append(String.format("Pay Amount: $%.2f", pay));
        return summary.toString();
    }
    
    public Function<PayrollData, Double> getCalculator() {
        return payCalculator;
    }
    
    public String getCalculatorDescription() {
        return calculatorDescription;
    }
}

// Simple Employee class for strategy pattern demo
record StrategyEmployee(String name, int id, LocalDate hireDate) {

    public int getYearsOfService() {
        return Period.between(hireDate, LocalDate.now()).getYears();
    }
}

// Demo class showing modern lambda-based Strategy pattern
class StrategyPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== Modern Strategy Pattern Demo (2025) ===");
        
        // Create employees
        var hourlyEmployee = new StrategyEmployee("Alice Johnson", 1001, LocalDate.of(2020, 3, 15));
        var salariedEmployee = new StrategyEmployee("Bob Smith", 1002, LocalDate.of(2019, 7, 22));
        var salesPerson = new StrategyEmployee("Carol Sales", 1003, LocalDate.of(2021, 12, 1));
        var customEmployee = new StrategyEmployee("Dave Custom", 1004, LocalDate.of(2022, 1, 15));
        
        // Create payroll processor with lambda strategies
        System.out.println("\n--- Standard Strategies (Predefined Lambdas) ---");
        
        // Hourly worker
        var processor = new PayrollProcessor(PayrollCalculations.HOURLY, "Hourly with Overtime");
        var hourlyData = new PayrollData(hourlyEmployee, 45, 25.0); // 45 hours at $25/hour
        System.out.println(processor.getPayrollSummary(hourlyData));
        
        System.out.println("\n--- Salaried Employee ---");
        processor.setCalculator(PayrollCalculations.SALARIED, "Salaried (Bi-weekly)");
        var salariedData = new PayrollData(salariedEmployee, 80000.0); // $80k annual
        System.out.println(processor.getPayrollSummary(salariedData));
        
        System.out.println("\n--- Sales Person (Base + Commission) ---");
        processor.setCalculator(PayrollCalculations.BASE_PLUS_COMMISSION, "Base Salary + Commission");
        var salesData = new PayrollData(salesPerson, 15000.0, 40000.0, 0.08); // $15k sales, $40k base, 8%
        System.out.println(processor.getPayrollSummary(salesData));
        
        System.out.println("\n--- Custom Strategy (Lambda Expression) ---");
        
        // Custom strategy: Senior sales rep with tiered commission
        Function<PayrollData, Double> seniorSalesStrategy = data -> {
            if (data.baseSalary() == null || data.salesAmount() == null) {
                throw new IllegalArgumentException("Base salary and sales amount required");
            }
            
            double base = data.baseSalary();
            double sales = data.salesAmount();
            
            // Tiered commission: 5% up to $20k, 8% above $20k, 12% above $50k
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
        
        processor.setCalculator(seniorSalesStrategy, "Senior Sales Rep (Tiered Commission)");
        var customData = new PayrollData(customEmployee, null, 65000.0, null, null, 50000.0, null);
        System.out.println(processor.getPayrollSummary(customData));
        
        System.out.println("\n--- Strategy Map Example ---");
        
        // Show how you might use a Map for strategy selection
        Map<String, Function<PayrollData, Double>> strategies = Map.of(
            "HOURLY", PayrollCalculations.HOURLY,
            "SALARIED", PayrollCalculations.SALARIED,
            "COMMISSION", PayrollCalculations.COMMISSION,
            "BASE_PLUS_COMMISSION", PayrollCalculations.BASE_PLUS_COMMISSION,
            "SENIOR_SALES", seniorSalesStrategy
        );
        
        System.out.println("Available strategies: " + strategies.keySet());
        
        // Dynamic strategy selection
        String strategyType = "SENIOR_SALES";
        Function<PayrollData, Double> selectedStrategy = strategies.get(strategyType);
        processor.setCalculator(selectedStrategy, "Dynamically Selected: " + strategyType);
        
        var dynamicData = new PayrollData(customEmployee, null, 45000.0, null, null, 55000.0, null);
        System.out.println("\nDynamic selection result:");
        System.out.println(processor.getPayrollSummary(dynamicData));
        
        System.out.println("\n--- Method Reference Example ---");
        
        // You can also use method references as strategies
        processor.setCalculator(StrategyPatternDemo::consultantRate, "Consultant (Method Reference)");
        var consultantData = new PayrollData(customEmployee, 35, null, 150.0, null, null, null);
        System.out.println(processor.getPayrollSummary(consultantData));
        
        System.out.println("\n--- Custom Strategy Factory Example ---");
        
        // Use the customStrategy factory method for a contractor with special rules
        Function<PayrollData, Double> contractorStrategy = PayrollCalculations.customStrategy(
            "Contractor with daily rate and bonus",
            data -> {
                if (data.hoursWorked() == null || data.hourlyRate() == null) {
                    throw new IllegalArgumentException("Hours and daily rate required for contractor");
                }
                
                // Contractor: Paid by full days (8-hour minimum) + bonus for weekend work
                int hours = data.hoursWorked();
                double dailyRate = data.hourlyRate(); // Using hourlyRate field as daily rate
                
                // Calculate full days (minimum 8 hours = 1 day)
                int fullDays = Math.max(1, (hours + 7) / 8); // Round up to nearest day
                double basePay = fullDays * dailyRate;
                
                // Weekend bonus: 20% if working more than 5 days (simulating weekend work)
                if (fullDays > 5) {
                    basePay *= 1.20; // 20% weekend bonus
                }
                
                return basePay;
            }
        );
        
        processor.setCalculator(contractorStrategy, "Contractor (Custom Strategy Factory)");
        var contractorData = new PayrollData(customEmployee, 42, null, 600.0, null, null, null); // 42 hours, $600/day
        System.out.println(processor.getPayrollSummary(contractorData));
    }
    
    // Example method that can be used as a strategy via method reference
    public static double consultantRate(PayrollData data) {
        if (data.hoursWorked() == null || data.hourlyRate() == null) {
            throw new IllegalArgumentException("Hours and rate required for consultant");
        }
        
        // Consultants: No overtime, but higher rate
        return data.hoursWorked() * data.hourlyRate();
    }
}