import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

// Modern Strategy Pattern using Function interface.
// We use lambdas instead of concrete strategy classes

// Data container for payroll information
record PayrollData(
        PayrollEmployee employee,
        Integer hoursWorked,
        Double salesAmount,
        Double hourlyRate,
        Double annualSalary,
        Double baseSalary,
        Double commissionRate
) {

    // Constructor for hourly workers
    public PayrollData(PayrollEmployee employee, int hoursWorked, double hourlyRate) {
        this(employee, hoursWorked, null, hourlyRate,
                null, null, null);
    }

    // Constructor for salaried workers
    public PayrollData(PayrollEmployee employee, double annualSalary) {
        this(employee, null, null,
                null, annualSalary, null, null);
    }

    // Constructor for commission workers
    public PayrollData(PayrollEmployee employee, double salesAmount,
                       double baseSalary, double commissionRate) {
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

}

// Modern context class using Function-based strategies
class PayrollProcessor {
    private Function<PayrollData, Double> payCalculator = PayrollCalculations.SALARIED; // Default strategy

    public void setCalculator(Function<PayrollData, Double> payCalculator) {
        this.payCalculator = payCalculator;
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
        var summary = new StringBuilder();
        summary.append("Employee: %s%n".formatted(payrollData.employee().name()));

        appendIfPresent(summary, "Hours Worked: %d%n", payrollData.hoursWorked());
        appendIfPresent(summary, "Hourly Rate: $%.2f%n", payrollData.hourlyRate());
        appendIfPresent(summary, "Annual Salary: $%.2f%n", payrollData.annualSalary());
        appendIfPresent(summary, "Sales Amount: $%.2f%n", payrollData.salesAmount());
        appendIfPresent(summary, "Base Salary: $%.2f%n", payrollData.baseSalary());
        if (payrollData.commissionRate() != null) {
            summary.append("Commission Rate: %.1f%%%n".formatted(payrollData.commissionRate() * 100));
        }

        summary.append("Pay Amount: $%.2f%n".formatted(pay));
        
        // Add strategy type information based on current calculator
        if (payCalculator == PayrollCalculations.HOURLY) {
            summary.append("Strategy: Hourly with Overtime");
        } else if (payCalculator == PayrollCalculations.SALARIED) {
            summary.append("Strategy: Salaried");
        } else if (payCalculator == PayrollCalculations.COMMISSION) {
            summary.append("Strategy: Commission Only");
        } else if (payCalculator == PayrollCalculations.BASE_PLUS_COMMISSION) {
            summary.append("Strategy: Base Plus Commission");
        } else {
            summary.append("Strategy: Custom");
        }
        
        return summary.toString();
    }

    // Helper method to reduce repetitive null checks
    private <T> void appendIfPresent(StringBuilder sb, String format, T value) {
        if (value != null) {
            sb.append(format.formatted(value));
        }
    }
}

// Simple Employee class for strategy pattern demo
record PayrollEmployee(String name, int id, LocalDate hireDate) {
}

// Demo class showing modern lambda-based Strategy pattern
class StrategyPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== Modern Strategy Pattern Demo (2025) ===");

        // Create test employees
        var hourlyEmployee = new PayrollEmployee("Alice Johnson", 1001, LocalDate.of(2020, 3, 15));
        var salariedEmployee = new PayrollEmployee("Bob Smith", 1002, LocalDate.of(2019, 7, 22));
        var salesEmployee = new PayrollEmployee("Carol Sales", 1003, LocalDate.of(2021, 12, 1));
        var consultantEmployee = new PayrollEmployee("Dave Consultant", 1004, LocalDate.of(2022, 8, 10));

        var processor = new PayrollProcessor();

        // 1. Predefined lambda strategies
        System.out.println("\n--- 1. Hourly with Overtime ---");
        processor.setCalculator(PayrollCalculations.HOURLY);
        var hourlyData = new PayrollData(hourlyEmployee, 45, 25.0); // 45 hours at $25/hour
        System.out.println(processor.getPayrollSummary(hourlyData));

        System.out.println("\n--- 2. Salaried ---");
        processor.setCalculator(PayrollCalculations.SALARIED);
        var salariedData = new PayrollData(salariedEmployee, 80000.0); // $80k annual
        System.out.println(processor.getPayrollSummary(salariedData));

        // 2. Custom lambda strategy
        System.out.println("\n--- 3. Tiered Commission ---");
        Function<PayrollData, Double> tieredCommission = data -> {
            double sales = data.salesAmount();
            double base = data.baseSalary();
            
            // 5% up to $20k, 8% above $20k
            double commission = sales <= 20000 ? 
                sales * 0.05 : 
                (20000 * 0.05) + ((sales - 20000) * 0.08);
            
            return base + commission;
        };

        processor.setCalculator(tieredCommission);
        var salesData = new PayrollData(salesEmployee, 25000.0, 40000.0, 0.05);
        System.out.println(processor.getPayrollSummary(salesData));

        // 3. Method reference strategy
        System.out.println("\n--- 4. Method Reference: Consultant Rate ---");
        processor.setCalculator(StrategyPatternDemo::calculateConsultantPay);
        var consultantData = new PayrollData(consultantEmployee, 35, null, 150.0, null, null, null);
        System.out.println(processor.getPayrollSummary(consultantData));

        // 4. Batch processing with strategy map and streams
        System.out.println("\n--- 5. Batch Processing with Strategy Map ---");
        
        // Strategy registry - map employees to their payment strategies
        Map<String, Function<PayrollData, Double>> employeeStrategies = Map.of(
            "Alice Johnson", PayrollCalculations.HOURLY,
            "Bob Smith", PayrollCalculations.SALARIED,
            "Carol Sales", tieredCommission,
            "Dave Consultant", StrategyPatternDemo::calculateConsultantPay
        );
        
        // Create batch of payroll data
        List<PayrollData> payrollBatch = List.of(
            new PayrollData(hourlyEmployee, 40, 25.0),           // Alice: 40 hours
            new PayrollData(salariedEmployee, 80000.0),          // Bob: $80k salary  
            new PayrollData(salesEmployee, 30000.0, 45000.0, 0.05), // Carol: $30k sales, $45k base
            new PayrollData(consultantEmployee, 32, null, 175.0, null, null, null) // Dave: 32 hours at $175/hour
        );
        
        // Process all payments in parallel using streams
        List<Double> payments = payrollBatch.parallelStream()
            .map(data -> employeeStrategies.get(data.employee().name()).apply(data))
            .toList();
        
        // Display results
        System.out.println("Batch processing results:");
        for (int i = 0; i < payrollBatch.size(); i++) {
            var data = payrollBatch.get(i);
            var payment = payments.get(i);
            System.out.printf("  %s: $%.2f%n", data.employee().name(), payment);
        }
    }

    // Example method that can be used as a strategy via method reference
    public static double calculateConsultantPay(PayrollData data) {
        // Consultants: No overtime, flat hourly rate
        return data.hoursWorked() * data.hourlyRate();
    }
}