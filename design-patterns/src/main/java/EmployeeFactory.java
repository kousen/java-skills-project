import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Factory Pattern - Create different types of employees
public abstract class EmployeeFactory {
    
    // Factory method
    public abstract Employee createEmployee(String name, int id, double compensation, LocalDate hireDate);
    
    // Template method that uses the factory method
    public Employee createAndConfigureEmployee(String name, int id, double compensation, 
                                             LocalDate hireDate, String department) {
        Employee employee = createEmployee(name, id, compensation, hireDate);
        employee.setDepartment(department);
        employee.initialize();
        return employee;
    }
    
    // Static factory method to get appropriate factory
    public static EmployeeFactory getFactory(EmployeeType type) {
        return switch (type) {
            case DEVELOPER -> new DeveloperFactory();
            case MANAGER -> new ManagerFactory();
            case INTERN -> new InternFactory();
            case SALES_REP -> new SalesRepFactory();
        };
    }
}

// Concrete factories
class DeveloperFactory extends EmployeeFactory {
    @Override
    public Employee createEmployee(String name, int id, double compensation, LocalDate hireDate) {
        return new Developer(name, id, compensation, hireDate);
    }
}

class ManagerFactory extends EmployeeFactory {
    @Override
    public Employee createEmployee(String name, int id, double compensation, LocalDate hireDate) {
        return new Manager(name, id, compensation, hireDate);
    }
}

class InternFactory extends EmployeeFactory {
    @Override
    public Employee createEmployee(String name, int id, double compensation, LocalDate hireDate) {
        return new Intern(name, id, compensation, hireDate);
    }
}

class SalesRepFactory extends EmployeeFactory {
    @Override
    public Employee createEmployee(String name, int id, double compensation, LocalDate hireDate) {
        return new SalesRep(name, id, compensation, hireDate);
    }
}

// Employee types enum
enum EmployeeType {
    DEVELOPER, MANAGER, INTERN, SALES_REP
}

// Base Employee class
abstract class Employee {
    protected final String name;
    protected final int id;
    protected final double compensation;
    protected final LocalDate hireDate;
    protected String department;
    protected boolean initialized = false;
    
    protected Employee(String name, int id, double compensation, LocalDate hireDate) {
        this.name = name;
        this.id = id;
        this.compensation = compensation;
        this.hireDate = hireDate;
    }
    
    // Abstract methods to be implemented by concrete classes
    public abstract String getRole();
    public abstract double calculatePay(int hoursWorked);
    public abstract String getSkills();
    public abstract String getResponsibilities();
    
    // Common methods
    public void initialize() {
        this.initialized = true;
        System.out.println("Initialized " + getRole() + ": " + name);
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    // Getters
    public String getName() { return name; }
    public int getId() { return id; }
    public double getCompensation() { return compensation; }
    public LocalDate getHireDate() { return hireDate; }
    public String getDepartment() { return department; }
    public boolean isInitialized() { return initialized; }
    
    public String getEmployeeInfo() {
        return String.format("""
            %s Employee Information:
            Name: %s
            ID: %d
            Department: %s
            Compensation: $%.2f
            Hire Date: %s
            Skills: %s
            Responsibilities: %s
            """, getRole(), name, id, department, compensation, hireDate, getSkills(), getResponsibilities());
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s (ID: %d)", getRole(), name, id);
    }
}

// Concrete Employee types
class Developer extends Employee {
    private static final Map<String, Double> SKILL_MULTIPLIERS = Map.of(
        "Java", 1.2,
        "Python", 1.1,
        "JavaScript", 1.0,
        "Senior", 1.5
    );
    
    public Developer(String name, int id, double hourlySalary, LocalDate hireDate) {
        super(name, id, hourlySalary, hireDate);
    }
    
    @Override
    public String getRole() {
        return "Developer";
    }
    
    @Override
    public double calculatePay(int hoursWorked) {
        double basePay = compensation * hoursWorked;
        // Developers get overtime pay for hours > 40
        if (hoursWorked > 40) {
            double overtimePay = (hoursWorked - 40) * compensation * 0.5;
            basePay += overtimePay;
        }
        return basePay;
    }
    
    @Override
    public String getSkills() {
        return "Java, Spring Boot, Microservices, Database Design, Testing";
    }
    
    @Override
    public String getResponsibilities() {
        return "Write clean code, Design systems, Code reviews, Mentor junior developers";
    }
}

class Manager extends Employee {
    public Manager(String name, int id, double annualSalary, LocalDate hireDate) {
        super(name, id, annualSalary, hireDate);
    }
    
    @Override
    public String getRole() {
        return "Manager";
    }
    
    @Override
    public double calculatePay(int hoursWorked) {
        // Managers get annual salary divided by pay periods
        return compensation / 26; // Bi-weekly
    }
    
    @Override
    public String getSkills() {
        return "Leadership, Strategic Planning, Budget Management, Team Building";
    }
    
    @Override
    public String getResponsibilities() {
        return "Lead team, Set goals, Performance reviews, Resource allocation";
    }
}

class Intern extends Employee {
    private final LocalDate internshipEndDate;
    
    public Intern(String name, int id, double hourlyRate, LocalDate hireDate) {
        super(name, id, hourlyRate, hireDate);
        // Internships typically last 3 months
        this.internshipEndDate = hireDate.plusMonths(3);
    }
    
    @Override
    public String getRole() {
        return "Intern";
    }
    
    @Override
    public double calculatePay(int hoursWorked) {
        // Interns have a cap on hours per week
        int maxHours = Math.min(hoursWorked, 20);
        return compensation * maxHours;
    }
    
    @Override
    public String getSkills() {
        return "Basic programming, Learning mindset, Academic knowledge";
    }
    
    @Override
    public String getResponsibilities() {
        return "Learn from mentors, Complete assigned tasks, Attend training sessions";
    }
    
    public LocalDate getInternshipEndDate() {
        return internshipEndDate;
    }
    
    public boolean isInternshipActive() {
        return LocalDate.now().isBefore(internshipEndDate);
    }
}

class SalesRep extends Employee {
    private final double commissionRate;
    
    public SalesRep(String name, int id, double baseSalary, LocalDate hireDate) {
        super(name, id, baseSalary, hireDate);
        this.commissionRate = 0.05; // 5% commission
    }
    
    @Override
    public String getRole() {
        return "Sales Representative";
    }
    
    @Override
    public double calculatePay(int hoursWorked) {
        // Base salary plus commission (simulated based on hours worked)
        double basePay = compensation / 26; // Bi-weekly base salary
        double simulatedSales = hoursWorked * 200; // $200 sales per hour
        double commission = simulatedSales * commissionRate;
        return basePay + commission;
    }
    
    @Override
    public String getSkills() {
        return "Customer relations, Negotiation, Product knowledge, CRM systems";
    }
    
    @Override
    public String getResponsibilities() {
        return "Generate leads, Close deals, Maintain client relationships, Meet sales targets";
    }
    
    public double getCommissionRate() {
        return commissionRate;
    }
}

// Factory Pattern Demo
class FactoryPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== Factory Pattern Demo ===");
        
        // Create employees using different factories
        EmployeeFactory developerFactory = EmployeeFactory.getFactory(EmployeeType.DEVELOPER);
        EmployeeFactory managerFactory = EmployeeFactory.getFactory(EmployeeType.MANAGER);
        EmployeeFactory internFactory = EmployeeFactory.getFactory(EmployeeType.INTERN);
        EmployeeFactory salesFactory = EmployeeFactory.getFactory(EmployeeType.SALES_REP);
        
        // Create and configure employees
        Employee developer = developerFactory.createAndConfigureEmployee(
            "Alice Johnson", 1001, 45.0, LocalDate.of(2020, 3, 15), "Engineering"
        );
        
        Employee manager = managerFactory.createAndConfigureEmployee(
            "Bob Smith", 2001, 95000, LocalDate.of(2018, 7, 22), "Engineering"
        );
        
        Employee intern = internFactory.createAndConfigureEmployee(
            "Carol Davis", 3001, 15.0, LocalDate.now().minusMonths(1), "Engineering"
        );
        
        Employee salesRep = salesFactory.createAndConfigureEmployee(
            "Dave Wilson", 4001, 50000, LocalDate.of(2021, 12, 1), "Sales"
        );
        
        // Display employee information
        System.out.println("\n=== Employee Information ===");
        Employee[] employees = {developer, manager, intern, salesRep};
        
        for (Employee emp : employees) {
            System.out.println(emp.getEmployeeInfo());
        }
        
        // Calculate pay for 40 hours worked
        System.out.println("\n=== Pay Calculation (40 hours) ===");
        for (Employee emp : employees) {
            double pay = emp.calculatePay(40);
            System.out.printf("%s: $%.2f%n", emp, pay);
        }
        
        // Demonstrate polymorphism
        System.out.println("\n=== Polymorphism Demo ===");
        processEmployees(employees);
        
        // Demonstrate factory method benefits
        System.out.println("\n=== Factory Benefits ===");
        demonstrateFactoryBenefits();
    }
    
    private static void processEmployees(Employee[] employees) {
        for (Employee emp : employees) {
            System.out.printf("Processing %s - Skills: %s%n", 
                            emp, emp.getSkills());
        }
    }
    
    private static void demonstrateFactoryBenefits() {
        System.out.println("Creating employees without knowing concrete types:");
        
        EmployeeType[] types = {EmployeeType.DEVELOPER, EmployeeType.MANAGER, 
                               EmployeeType.INTERN, EmployeeType.SALES_REP};
        
        for (int i = 0; i < types.length; i++) {
            EmployeeType type = types[i];
            EmployeeFactory factory = EmployeeFactory.getFactory(type);
            Employee emp = factory.createEmployee(
                "Employee " + (i + 1), 1000 + i, 50000, LocalDate.now()
            );
            
            System.out.printf("Created: %s (Type determined at runtime)%n", emp);
        }
    }
}