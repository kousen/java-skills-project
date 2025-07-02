import java.time.LocalDate;
import java.util.List;

// SOLID Principles Demonstration
// Single Responsibility Principle - Each class has one reason to change

// Employee entity - only responsible for employee data
class Employee {
    private final int id;
    private String name;
    private String email;
    private double salary;
    private final LocalDate hireDate;
    private String department;
    
    public Employee(int id, String name, String email, double salary, LocalDate hireDate) {
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
        return String.format("Employee{id=%d, name='%s', email='%s', salary=%.2f}", 
                           id, name, email, salary);
    }
}

// SRP: Separate interface for employee data persistence
interface EmployeeRepository {
    void save(Employee employee);
    Employee findById(int id);
    List<Employee> findAll();
    void delete(int id);
    List<Employee> findByDepartment(String department);
}

// SRP: Separate interface for salary calculations
interface SalaryCalculator {
    double calculateAnnualSalary(Employee employee);
    double calculateMonthlyPay(Employee employee);
    double calculateBonus(Employee employee);
}

// SRP: Separate interface for notifications
interface NotificationService {
    void sendWelcomeEmail(Employee employee);
    void sendSalaryChangeNotification(Employee employee, double oldSalary, double newSalary);
    void sendTerminationNotification(Employee employee);
}

// SRP: Employee business logic service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final SalaryCalculator salaryCalculator;
    private final NotificationService notificationService;
    
    // Dependency Injection Constructor
    public EmployeeService(EmployeeRepository repository, 
                          SalaryCalculator salaryCalculator, 
                          NotificationService notificationService) {
        this.repository = repository;
        this.salaryCalculator = salaryCalculator;
        this.notificationService = notificationService;
    }
    
    public void hireEmployee(Employee employee) {
        repository.save(employee);
        notificationService.sendWelcomeEmail(employee);
    }
    
    public void updateSalary(int employeeId, double newSalary) {
        Employee employee = repository.findById(employeeId);
        if (employee != null) {
            double oldSalary = employee.getSalary();
            employee.setSalary(newSalary);
            repository.save(employee);
            notificationService.sendSalaryChangeNotification(employee, oldSalary, newSalary);
        }
    }
    
    public void terminateEmployee(int employeeId) {
        Employee employee = repository.findById(employeeId);
        if (employee != null) {
            repository.delete(employeeId);
            notificationService.sendTerminationNotification(employee);
        }
    }
    
    public double getEmployeeAnnualCost(int employeeId) {
        Employee employee = repository.findById(employeeId);
        if (employee != null) {
            return salaryCalculator.calculateAnnualSalary(employee);
        }
        return 0;
    }
    
    public List<Employee> getEmployeesByDepartment(String department) {
        return repository.findByDepartment(department);
    }
}