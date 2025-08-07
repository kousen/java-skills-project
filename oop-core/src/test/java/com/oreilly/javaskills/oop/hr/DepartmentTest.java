package com.oreilly.javaskills.oop.hr;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@DisplayName("Department Tests")
class DepartmentTest {
    
    private Department department;
    private Employee alice;
    private Employee bob;
    private Employee charlie;
    
    @BeforeEach
    void setUp() {
        department = new Department("Engineering", "ENG");
        
        // Create test employees
        alice = new Employee(1001, "Alice Johnson", 120000, LocalDate.now().minusYears(3));
        bob = new Employee(1002, "Bob Smith", 95000, LocalDate.now().minusYears(2));
        charlie = new Employee(1003, "Charlie Brown", 85000, LocalDate.now().minusYears(1));
    }
    
    @Test
    @DisplayName("Should create department with valid data")
    void shouldCreateDepartmentWithValidData() {
        Department dept = new Department("Marketing", "MKT");
        
        assertThat(dept.getName()).isEqualTo("Marketing");
        assertThat(dept.getCode()).isEqualTo("MKT");
        assertThat(dept.getEmployeeCount()).isEqualTo(0);
        assertThat(dept.getManager()).isNull();
        assertThat(dept.getBudget()).isEqualTo(0.0);
    }
    
    @Test
    @DisplayName("Should create department with budget")
    void shouldCreateDepartmentWithBudget() {
        Department dept = new Department("Sales", "SLS", 500000);
        
        assertThat(dept.getName()).isEqualTo("Sales");
        assertThat(dept.getCode()).isEqualTo("SLS");
        assertThat(dept.getBudget()).isEqualTo(500000);
    }
    
    @Test
    @DisplayName("Should validate department creation parameters")
    void shouldValidateDepartmentCreationParameters() {
        // Invalid name
        assertThatThrownBy(() -> new Department(null, "ENG"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Department name cannot be null or empty");
            
        assertThatThrownBy(() -> new Department("", "ENG"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Department name cannot be null or empty");
            
        assertThatThrownBy(() -> new Department("   ", "ENG"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Department name cannot be null or empty");
            
        // Invalid code
        assertThatThrownBy(() -> new Department("Engineering", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Department code cannot be null or empty");
            
        assertThatThrownBy(() -> new Department("Engineering", ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Department code cannot be null or empty");
            
        // Code too short or too long
        assertThatThrownBy(() -> new Department("Engineering", "E"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Department code must be 2-5 characters");
            
        assertThatThrownBy(() -> new Department("Engineering", "ENGINEERING"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Department code must be 2-5 characters");
            
        // Negative budget
        assertThatThrownBy(() -> new Department("Engineering", "ENG", -1000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Budget cannot be negative");
    }
    
    @Test
    @DisplayName("Should trim and uppercase department code")
    void shouldTrimAndUppercaseDepartmentCode() {
        Department dept = new Department("  Engineering  ", "  eng  ");
        
        assertThat(dept.getName()).isEqualTo("Engineering");
        assertThat(dept.getCode()).isEqualTo("ENG");
    }
    
    @Test
    @DisplayName("Should add employees to department")
    void shouldAddEmployeesToDepartment() {
        assertThat(department.getEmployeeCount()).isEqualTo(0);
        
        department.addEmployee(alice);
        assertThat(department.getEmployeeCount()).isEqualTo(1);
        assertThat(department.hasEmployee(alice)).isTrue();
        
        department.addEmployee(bob);
        assertThat(department.getEmployeeCount()).isEqualTo(2);
        assertThat(department.hasEmployee(bob)).isTrue();
        
        // Should not add duplicate employees
        department.addEmployee(alice);
        assertThat(department.getEmployeeCount()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Should reject null employee")
    void shouldRejectNullEmployee() {
        assertThatThrownBy(() -> department.addEmployee(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Employee cannot be null");
    }
    
    @Test
    @DisplayName("Should remove employees from department")
    void shouldRemoveEmployeesFromDepartment() {
        department.addEmployee(alice);
        department.addEmployee(bob);
        assertThat(department.getEmployeeCount()).isEqualTo(2);
        
        boolean removed = department.removeEmployee(alice);
        assertThat(removed).isTrue();
        assertThat(department.getEmployeeCount()).isEqualTo(1);
        assertThat(department.hasEmployee(alice)).isFalse();
        assertThat(department.hasEmployee(bob)).isTrue();
        
        // Removing non-existent employee should return false
        boolean removedAgain = department.removeEmployee(alice);
        assertThat(removedAgain).isFalse();
        assertThat(department.getEmployeeCount()).isEqualTo(1);
        
        // Removing null should return false
        boolean removedNull = department.removeEmployee(null);
        assertThat(removedNull).isFalse();
    }
    
    @Test
    @DisplayName("Should manage department manager")
    void shouldManageDepartmentManager() {
        assertThat(department.getManager()).isNull();
        
        // Set manager
        department.setManager(alice);
        assertThat(department.getManager()).isEqualTo(alice);
        assertThat(department.hasEmployee(alice)).isTrue();
        assertThat(department.getEmployeeCount()).isEqualTo(1);
        
        // Change manager
        department.setManager(bob);
        assertThat(department.getManager()).isEqualTo(bob);
        assertThat(department.hasEmployee(bob)).isTrue();
        assertThat(department.hasEmployee(alice)).isTrue(); // Alice should still be an employee
        assertThat(department.getEmployeeCount()).isEqualTo(2);
        
        // Remove manager
        department.removeEmployee(bob);
        assertThat(department.getManager()).isNull();
        assertThat(department.hasEmployee(bob)).isFalse();
    }
    
    @Test
    @DisplayName("Should manage department budget")
    void shouldManageDepartmentBudget() {
        assertThat(department.getBudget()).isEqualTo(0.0);
        
        department.setBudget(250000);
        assertThat(department.getBudget()).isEqualTo(250000);
        
        // Should reject negative budget
        assertThatThrownBy(() -> department.setBudget(-1000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Budget cannot be negative");
    }
    
    @Test
    @DisplayName("Should calculate total payroll")
    void shouldCalculateTotalPayroll() {
        assertThat(department.getTotalPayroll()).isEqualTo(0.0);
        
        department.addEmployee(alice); // 120000
        assertThat(department.getTotalPayroll()).isEqualTo(120000);
        
        department.addEmployee(bob); // 95000
        assertThat(department.getTotalPayroll()).isEqualTo(215000);
        
        department.addEmployee(charlie); // 85000
        assertThat(department.getTotalPayroll()).isEqualTo(300000);
    }
    
    @Test
    @DisplayName("Should calculate average salary")
    void shouldCalculateAverageSalary() {
        assertThat(department.getAveragesalary()).isEqualTo(0.0);
        
        department.addEmployee(alice); // 120000
        assertThat(department.getAveragesalary()).isEqualTo(120000);
        
        department.addEmployee(bob); // 95000
        assertThat(department.getAveragesalary()).isEqualTo(107500); // (120000 + 95000) / 2
        
        department.addEmployee(charlie); // 85000
        assertThat(department.getAveragesalary()).isEqualTo(100000); // (120000 + 95000 + 85000) / 3
    }
    
    @Test
    @DisplayName("Should find highest paid employee")
    void shouldFindHighestPaidEmployee() {
        assertThat(department.getHighestPaidEmployee()).isNull();
        
        department.addEmployee(bob); // 95000
        assertThat(department.getHighestPaidEmployee()).isEqualTo(bob);
        
        department.addEmployee(alice); // 120000
        assertThat(department.getHighestPaidEmployee()).isEqualTo(alice);
        
        department.addEmployee(charlie); // 85000
        assertThat(department.getHighestPaidEmployee()).isEqualTo(alice);
    }
    
    @Test
    @DisplayName("Should filter employees by salary range")
    void shouldFilterEmployeesBySalaryRange() {
        department.addEmployee(alice);   // 120000
        department.addEmployee(bob);     // 95000
        department.addEmployee(charlie); // 85000
        
        List<Employee> highEarners = department.getEmployeesBySalaryRange(100000, 150000);
        assertThat(highEarners).hasSize(1);
        assertThat(highEarners).contains(alice);
        
        List<Employee> midRange = department.getEmployeesBySalaryRange(90000, 100000);
        assertThat(midRange).hasSize(1);
        assertThat(midRange).contains(bob);
        
        List<Employee> allEmployees = department.getEmployeesBySalaryRange(80000, 130000);
        assertThat(allEmployees).hasSize(3);
        assertThat(allEmployees).containsExactlyInAnyOrder(alice, bob, charlie);
        
        List<Employee> noEmployees = department.getEmployeesBySalaryRange(200000, 300000);
        assertThat(noEmployees).isEmpty();
    }
    
    @Test
    @DisplayName("Should calculate budget utilization")
    void shouldCalculateBudgetUtilization() {
        department.setBudget(300000);
        assertThat(department.getBudgetUtilization()).isEqualTo(0.0);
        
        department.addEmployee(alice);   // 120000
        assertThat(department.getBudgetUtilization()).isEqualTo(40.0); // 120000/300000 * 100
        
        department.addEmployee(bob);     // 95000
        assertThat(department.getBudgetUtilization()).isCloseTo(71.67, within(0.01)); // 215000/300000 * 100
        
        department.addEmployee(charlie); // 85000
        assertThat(department.getBudgetUtilization()).isEqualTo(100.0); // 300000/300000 * 100
        
        // Zero budget should return 0
        department.setBudget(0);
        assertThat(department.getBudgetUtilization()).isEqualTo(0.0);
    }
    
    @Test
    @DisplayName("Should check if department is over budget")
    void shouldCheckIfDepartmentIsOverBudget() {
        department.setBudget(200000);
        assertThat(department.isOverBudget()).isFalse();
        
        department.addEmployee(alice);   // 120000
        assertThat(department.isOverBudget()).isFalse();
        
        department.addEmployee(bob);     // 95000 (total: 215000)
        assertThat(department.isOverBudget()).isTrue();
        
        // Zero budget with employees should not be over budget
        department.setBudget(0);
        assertThat(department.isOverBudget()).isFalse();
    }
    
    @Test
    @DisplayName("Should return unmodifiable employee list")
    void shouldReturnUnmodifiableEmployeeList() {
        department.addEmployee(alice);
        List<Employee> employees = department.getEmployees();
        
        assertThat(employees).hasSize(1);
        assertThat(employees).contains(alice);
        
        // Should not be able to modify the returned list
        assertThatThrownBy(() -> employees.add(bob))
            .isInstanceOf(UnsupportedOperationException.class);
    }
    
    @Test
    @DisplayName("Should generate department summary")
    void shouldGenerateDepartmentSummary() {
        department.setBudget(300000);
        department.addEmployee(alice);   // 120000
        department.addEmployee(bob);     // 95000
        
        String summary = department.getDepartmentSummary();
        
        assertThat(summary).contains("Engineering");
        assertThat(summary).contains("ENG");
        assertThat(summary).contains("Employees=2");
        assertThat(summary).contains("Payroll=$215000.00");
        assertThat(summary).contains("Budget=$300000.00");
        assertThat(summary).contains("Utilization=71.7%");
    }
    
    @Test
    @DisplayName("Should implement equals and hashCode based on code")
    void shouldImplementEqualsAndHashCodeBasedOnCode() {
        Department dept1 = new Department("Engineering", "ENG");
        Department dept2 = new Department("Engineering", "ENG");
        Department dept3 = new Department("Different Name", "ENG"); // Same code, different name
        Department dept4 = new Department("Engineering", "ENGR");  // Different code
        
        // Same department code should be equal
        assertThat(dept1).isEqualTo(dept2);
        assertThat(dept1).isEqualTo(dept3); // Only code matters for equality
        assertThat(dept1).isNotEqualTo(dept4);
        
        // Hash codes should match for equal objects
        assertThat(dept1.hashCode()).isEqualTo(dept2.hashCode());
        assertThat(dept1.hashCode()).isEqualTo(dept3.hashCode());
    }
    
    @Test
    @DisplayName("Should have meaningful toString")
    void shouldHaveMeaningfulToString() {
        String str = department.toString();
        
        assertThat(str).contains("Engineering");
        assertThat(str).contains("ENG");
        assertThat(str).isEqualTo("Engineering (ENG)");
    }
}