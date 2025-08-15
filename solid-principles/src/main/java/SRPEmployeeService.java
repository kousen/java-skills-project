import java.time.LocalDate;
import java.util.List;

// SOLID Principles Demonstration
// Single Responsibility Principle - Each class has one responsibility

// SRPEmployee entity - only cares about employee data
class SRPEmployee {
    private final int id;
    private String name;
    private String email;
    private double salary;
    private final LocalDate hireDate;
    private String department;
    
    public SRPEmployee(int id, String name, String email, double salary, LocalDate hireDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.hireDate = hireDate;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public LocalDate getHireDate() { return hireDate; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    @Override
    public String toString() {
        return String.format("SRPEmployee{id=%d, name='%s', email='%s', salary=%.2f}", 
                           id, name, email, salary);
    }
}

// SRP: Separate interface for employee data persistence
interface EmployeeRepository {
    void save(SRPEmployee employee);
    SRPEmployee findById(int id);
    List<SRPEmployee> findAll();
    void delete(int id);
    List<SRPEmployee> findByDepartment(String department);
}

// SRP: Separate interface for salary calculations
interface SalaryCalculator {
    double calculateAnnualSalary(SRPEmployee employee);
    double calculateMonthlyPay(SRPEmployee employee);
    double calculateBonus(SRPEmployee employee);
}

// SRP: Separate interface for notifications
interface NotificationService {
    void sendWelcomeEmail(SRPEmployee employee);
    void sendSalaryChangeNotification(SRPEmployee employee, double oldSalary, double newSalary);
    void sendTerminationNotification(SRPEmployee employee);
}

// SRP: Employee business logic service
@SuppressWarnings({"WeakerAccess", "ClassEscapesDefinedScope"})
public class SRPEmployeeService {
    private final EmployeeRepository repository;
    private final SalaryCalculator salaryCalculator;
    private final NotificationService notificationService;

    // Dependency Injection Constructor
    public SRPEmployeeService(EmployeeRepository repository,
                              SalaryCalculator salaryCalculator,
                              NotificationService notificationService) {
        this.repository = repository;
        this.salaryCalculator = salaryCalculator;
        this.notificationService = notificationService;
    }

    public void hireEmployee(SRPEmployee employee) {
        repository.save(employee);
        notificationService.sendWelcomeEmail(employee);
    }

    public void updateSalary(int employeeId, double newSalary) {
        SRPEmployee employee = repository.findById(employeeId);
        if (employee != null) {
            double oldSalary = employee.getSalary();
            employee.setSalary(newSalary);
            repository.save(employee);
            notificationService.sendSalaryChangeNotification(employee, oldSalary, newSalary);
        }
    }

    public void terminateEmployee(int employeeId) {
        SRPEmployee employee = repository.findById(employeeId);
        if (employee != null) {
            repository.delete(employeeId);
            notificationService.sendTerminationNotification(employee);
        }
    }

    public double getEmployeeAnnualCost(int employeeId) {
        SRPEmployee employee = repository.findById(employeeId);
        if (employee != null) {
            return salaryCalculator.calculateAnnualSalary(employee);
        }
        return 0;
    }

    public List<SRPEmployee> getEmployeesByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    public static void main(String[] args) {
        System.out.println("""
                === Single Responsibility Principle Demo ===
                Each class has one reason to change:
                - SRPEmployee: Employee data only
                - EmployeeRepository: Data persistence only
                - SalaryCalculator: Salary calculations only
                - NotificationService: Notifications only
                - SRPEmployeeService: Business logic coordination only
                
                Benefits:
                - Easy to test each component in isolation
                - Changes to notification logic don't affect salary calculations
                - Database changes don't affect business logic
                - New features can be added without modifying existing classes""");
    }
}