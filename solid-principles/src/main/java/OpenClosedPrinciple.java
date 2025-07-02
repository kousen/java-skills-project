import java.util.List;

// Open/Closed Principle - Open for extension, closed for modification
// We can add new employee types without modifying existing code

// Abstract base class that defines the contract
abstract class Employee {
    protected final int id;
    protected final String name;
    protected final double baseSalary;
    
    protected Employee(int id, String name, double baseSalary) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
    }
    
    // Template method that uses abstract methods
    public final double calculatePay() {
        double pay = getBasePay();
        pay += calculateBonus();
        pay += calculateBenefits();
        return pay;
    }
    
    // Abstract methods - subclasses must implement these
    protected abstract double getBasePay();
    protected abstract double calculateBonus();
    protected abstract double calculateBenefits();
    public abstract String getEmployeeType();
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getBaseSalary() { return baseSalary; }
    
    @Override
    public String toString() {
        return String.format("%s: %s (ID: %d) - Total Pay: $%.2f", 
                           getEmployeeType(), name, id, calculatePay());
    }
}

// Concrete employee types - extending without modifying base class

class FullTimeEmployee extends Employee {
    private final int yearsOfService;
    
    public FullTimeEmployee(int id, String name, double baseSalary, int yearsOfService) {
        super(id, name, baseSalary);
        this.yearsOfService = yearsOfService;
    }
    
    @Override
    protected double getBasePay() {
        return baseSalary;
    }
    
    @Override
    protected double calculateBonus() {
        // Bonus based on years of service
        return baseSalary * (yearsOfService * 0.01); // 1% per year
    }
    
    @Override
    protected double calculateBenefits() {
        // Full benefits package
        return baseSalary * 0.15; // 15% of salary
    }
    
    @Override
    public String getEmployeeType() {
        return "Full-Time Employee";
    }
    
    public int getYearsOfService() {
        return yearsOfService;
    }
}

class PartTimeEmployee extends Employee {
    private final int hoursPerWeek;
    
    public PartTimeEmployee(int id, String name, double hourlyRate, int hoursPerWeek) {
        super(id, name, hourlyRate);
        this.hoursPerWeek = hoursPerWeek;
    }
    
    @Override
    protected double getBasePay() {
        // Calculate based on hourly rate and hours per week
        return baseSalary * hoursPerWeek * 52; // Annual pay
    }
    
    @Override
    protected double calculateBonus() {
        // Smaller bonus for part-time employees
        if (hoursPerWeek >= 20) {
            return getBasePay() * 0.02; // 2% bonus for 20+ hours
        }
        return 0;
    }
    
    @Override
    protected double calculateBenefits() {
        // Prorated benefits based on hours
        if (hoursPerWeek >= 20) {
            return getBasePay() * 0.05; // 5% benefits for 20+ hours
        }
        return 0;
    }
    
    @Override
    public String getEmployeeType() {
        return "Part-Time Employee";
    }
    
    public int getHoursPerWeek() {
        return hoursPerWeek;
    }
}

class ContractEmployee extends Employee {
    private final int contractMonths;
    private final boolean hasHealthBenefits;
    
    public ContractEmployee(int id, String name, double monthlyRate, 
                          int contractMonths, boolean hasHealthBenefits) {
        super(id, name, monthlyRate);
        this.contractMonths = contractMonths;
        this.hasHealthBenefits = hasHealthBenefits;
    }
    
    @Override
    protected double getBasePay() {
        // Monthly rate * 12 for annual comparison
        return baseSalary * 12;
    }
    
    @Override
    protected double calculateBonus() {
        // Contract completion bonus
        if (contractMonths >= 12) {
            return baseSalary; // One month salary as bonus
        }
        return 0;
    }
    
    @Override
    protected double calculateBenefits() {
        return hasHealthBenefits ? baseSalary * 2 : 0; // 2 months worth if has benefits
    }
    
    @Override
    public String getEmployeeType() {
        return "Contract Employee";
    }
    
    public int getContractMonths() {
        return contractMonths;
    }
    
    public boolean hasHealthBenefits() {
        return hasHealthBenefits;
    }
}

// NEW employee type - can be added without modifying existing code
class InternEmployee extends Employee {
    private final String university;
    private final boolean isPaid;
    
    public InternEmployee(int id, String name, double stipend, String university, boolean isPaid) {
        super(id, name, stipend);
        this.university = university;
        this.isPaid = isPaid;
    }
    
    @Override
    protected double getBasePay() {
        return isPaid ? baseSalary : 0;
    }
    
    @Override
    protected double calculateBonus() {
        // Performance bonus for paid interns
        return isPaid ? baseSalary * 0.1 : 0;
    }
    
    @Override
    protected double calculateBenefits() {
        // Learning opportunities have value
        return 1000; // Fixed benefit value for experience
    }
    
    @Override
    public String getEmployeeType() {
        return "Intern";
    }
    
    public String getUniversity() {
        return university;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
}

// Payroll calculator that works with any employee type
class PayrollCalculator {
    
    public double calculateTotalPayroll(List<Employee> employees) {
        return employees.stream()
                .mapToDouble(Employee::calculatePay)
                .sum();
    }
    
    public void generatePayrollReport(List<Employee> employees) {
        System.out.println("=== Payroll Report ===");
        System.out.println("Employee Details:");
        
        double totalPayroll = 0;
        for (Employee emp : employees) {
            System.out.println("  " + emp);
            totalPayroll += emp.calculatePay();
        }
        
        System.out.printf("\nTotal Payroll: $%.2f%n", totalPayroll);
        System.out.printf("Average Pay: $%.2f%n", totalPayroll / employees.size());
    }
    
    public List<Employee> getHighEarners(List<Employee> employees, double threshold) {
        return employees.stream()
                .filter(emp -> emp.calculatePay() > threshold)
                .toList();
    }
}

// Demo class showing Open/Closed Principle
class OpenClosedPrincipleDemo {
    public static void main(String[] args) {
        System.out.println("=== Open/Closed Principle Demo ===");
        
        // Create different types of employees
        List<Employee> employees = List.of(
            new FullTimeEmployee(1001, "Alice Johnson", 80000, 5),
            new PartTimeEmployee(1002, "Bob Smith", 25, 25), // $25/hour, 25 hours/week
            new ContractEmployee(1003, "Carol Davis", 7000, 18, true), // $7000/month, 18 months
            new InternEmployee(1004, "Dave Wilson", 2000, "State University", true)
        );
        
        // PayrollCalculator works with all employee types without modification
        PayrollCalculator calculator = new PayrollCalculator();
        calculator.generatePayrollReport(employees);
        
        // Demonstrate that we can add new functionality without modifying existing code
        System.out.println("\n=== High Earners (>$40,000) ===");
        List<Employee> highEarners = calculator.getHighEarners(employees, 40000);
        highEarners.forEach(System.out::println);
        
        // Show that adding new employee types doesn't break existing functionality
        System.out.println("\n=== Adding New Employee Type ===");
        
        // This could be a new FreelancerEmployee, RemoteEmployee, etc.
        // The existing PayrollCalculator and other code continues to work
        System.out.println("New employee types can be added without modifying:");
        System.out.println("- PayrollCalculator class");
        System.out.println("- Existing Employee subclasses");
        System.out.println("- Client code that uses employees");
        
        System.out.println("\nThis demonstrates the Open/Closed Principle:");
        System.out.println("- Open for extension (new employee types)");
        System.out.printf("- Closed for modification (existing code unchanged)");
    }
}