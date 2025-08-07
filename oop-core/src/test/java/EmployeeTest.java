import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;

import com.oreilly.javaskills.oop.hr.Employee;
import com.oreilly.javaskills.oop.hr.Address;
import com.oreilly.javaskills.oop.hr.Department;

@DisplayName("Employee Tests")
class EmployeeTest {
    
    private Employee employee;
    private final LocalDate hireDate = LocalDate.of(2020, 3, 15);
    
    @BeforeEach
    void setUp() {
        employee = new Employee(1001, "Alice Johnson", 75000.0, hireDate);
    }
    
    @Test
    @DisplayName("Should create employee with valid data")
    void shouldCreateEmployeeWithValidData() {
        assertThat(employee.getId()).isEqualTo(1001);
        assertThat(employee.getName()).isEqualTo("Alice Johnson");
        assertThat(employee.getSalary()).isEqualTo(75000.0);
        assertThat(employee.getHireDate()).isEqualTo(hireDate);
        assertThat(employee.isActive()).isTrue();
    }
    
    @Test
    @DisplayName("Should reject invalid employee ID")
    void shouldRejectInvalidEmployeeId() {
        assertThatThrownBy(() -> new Employee(0, "John Doe", 50000, hireDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Employee ID must be positive");
        
        assertThatThrownBy(() -> new Employee(-1, "John Doe", 50000, hireDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Employee ID must be positive");
    }
    
    @Test
    @DisplayName("Should reject invalid employee name")
    void shouldRejectInvalidEmployeeName() {
        assertThatThrownBy(() -> new Employee(1001, null, 50000, hireDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Employee name cannot be null or empty");
        
        assertThatThrownBy(() -> new Employee(1001, "", 50000, hireDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Employee name cannot be null or empty");
        
        assertThatThrownBy(() -> new Employee(1001, "   ", 50000, hireDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Employee name cannot be null or empty");
    }
    
    @Test
    @DisplayName("Should reject negative salary")
    void shouldRejectNegativeSalary() {
        assertThatThrownBy(() -> new Employee(1001, "John Doe", -1000, hireDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Salary cannot be negative");
    }
    
    @Test
    @DisplayName("Should reject future hire date")
    void shouldRejectFutureHireDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertThatThrownBy(() -> new Employee(1001, "John Doe", 50000, futureDate))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Hire date cannot be in the future");
    }
    
    @Test
    @DisplayName("Should calculate years of service correctly")
    void shouldCalculateYearsOfServiceCorrectly() {
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
        Employee emp = new Employee(1001, "John Doe", 50000, twoYearsAgo);
        
        assertThat(emp.getYearsOfService()).isGreaterThanOrEqualTo(2);
    }
    
    @Test
    @DisplayName("Should calculate annual bonus based on years of service")
    void shouldCalculateAnnualBonusBasedOnYearsOfService() {
        // Less than 1 year
        Employee newEmployee = new Employee(1001, "New Employee", 50000, LocalDate.now().minusMonths(6));
        assertThat(newEmployee.getAnnualBonus()).isEqualTo(0);
        
        // 2 years of service
        Employee twoYearEmployee = new Employee(1002, "Two Year Employee", 50000, LocalDate.now().minusYears(2));
        assertThat(twoYearEmployee.getAnnualBonus()).isEqualTo(50000 * 0.02);
        
        // 4 years of service
        Employee fourYearEmployee = new Employee(1003, "Four Year Employee", 50000, LocalDate.now().minusYears(4));
        assertThat(fourYearEmployee.getAnnualBonus()).isEqualTo(50000 * 0.05);
        
        // 6 years of service
        Employee sixYearEmployee = new Employee(1004, "Six Year Employee", 50000, LocalDate.now().minusYears(6));
        assertThat(sixYearEmployee.getAnnualBonus()).isEqualTo(50000 * 0.08);
    }
    
    @Test
    @DisplayName("Should give raise correctly")
    void shouldGiveRaiseCorrectly() {
        double originalSalary = employee.getSalary();
        employee.giveRaise(10.0); // 10% raise
        
        assertThat(employee.getSalary()).isEqualTo(originalSalary * 1.1);
    }
    
    @Test
    @DisplayName("Should reject negative raise percentage")
    void shouldRejectNegativeRaisePercentage() {
        assertThatThrownBy(() -> employee.giveRaise(-5.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Raise percentage cannot be negative");
    }
    
    @Test
    @DisplayName("Should set and get address")
    void shouldSetAndGetAddress() {
        Address address = new Address("123 Main St", "Anytown", "ST", "12345");
        employee.setAddress(address);
        
        assertThat(employee.getAddress()).isEqualTo(address);
    }
    
    @Test
    @DisplayName("Should set and get department")
    void shouldSetAndGetDepartment() {
        Department department = new Department("Engineering", "ENG");
        employee.setDepartment(department);
        
        assertThat(employee.getDepartment()).isEqualTo(department);
    }
    
    @Test
    @DisplayName("Should implement equals and hashCode based on ID")
    void shouldImplementEqualsAndHashCodeBasedOnId() {
        Employee employee1 = new Employee(1001, "Alice Johnson", 75000, hireDate);
        Employee employee2 = new Employee(1001, "Bob Smith", 80000, hireDate); // Same ID, different data
        Employee employee3 = new Employee(1002, "Alice Johnson", 75000, hireDate); // Different ID, same data
        
        assertThat(employee1).isEqualTo(employee2); // Same ID
        assertThat(employee1).isNotEqualTo(employee3); // Different ID
        assertThat(employee1.hashCode()).isEqualTo(employee2.hashCode());
    }
    
    @Test
    @DisplayName("Should generate employee summary")
    void shouldGenerateEmployeeSummary() {
        String summary = employee.getEmployeeSummary();
        
        assertThat(summary)
            .contains("Employee[ID=1001")
            .contains("Name=Alice Johnson")
            .contains("Salary=$75000.00")
            .contains("Years=");
    }
}