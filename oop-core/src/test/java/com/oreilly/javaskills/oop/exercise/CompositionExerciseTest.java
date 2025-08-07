package com.oreilly.javaskills.oop.exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import static com.oreilly.javaskills.oop.exercise.CompositionExercise.*;
import com.oreilly.javaskills.oop.hr.Employee;
import com.oreilly.javaskills.oop.hr.Address;
import com.oreilly.javaskills.oop.hr.Department;

/**
 * Test class for CompositionExercise
 * These tests verify that the composition structure works correctly
 */
class CompositionExerciseTest {

    @Test
    @DisplayName("Department should contain employees through composition using real HR classes")
    void testDepartmentWithRealHRClasses() {
        // Create departments using real Department class
        Department engineering = new Department("Engineering", "ENG");
        Department sales = new Department("Sales", "SLS");
        
        // Test that departments start empty
        assertThat(engineering.getEmployeeCount()).isEqualTo(0);
        assertThat(sales.getEmployeeCount()).isEqualTo(0);
        
        // Create employees with addresses using real Employee and Address classes
        Address aliceAddress = new Address("123 Main St", "City", "ST", "12345");
        Employee alice = new Employee(1001, "Alice Johnson", 100000, LocalDate.now().minusYears(2));
        alice.setAddress(aliceAddress);
        
        Address bobAddress = new Address("456 Oak St", "Town", "ST", "54321");
        Employee bob = new Employee(1002, "Bob Smith", 90000, LocalDate.now().minusYears(1));
        bob.setAddress(bobAddress);
        
        // Add employees to departments
        engineering.addEmployee(alice);
        sales.addEmployee(bob);
        
        // Test composition relationships
        assertThat(engineering.getEmployeeCount()).isEqualTo(1);
        assertThat(sales.getEmployeeCount()).isEqualTo(1);
        assertThat(alice.getAddress().city()).isEqualTo("City");
        assertThat(bob.getAddress().city()).isEqualTo("Town");
    }
    
    @Test
    @DisplayName("Employee should have an address through composition")
    void testEmployeeHasAddress() {
        Address homeAddress = new Address("123 Oak St", "Town", "ST", "54321");
        Employee employee = new Employee(1001, "John Doe", 75000, LocalDate.now().minusYears(1));
        employee.setAddress(homeAddress);
        
        assertThat(employee.getName()).isEqualTo("John Doe");
        assertThat(employee.getSalary()).isEqualTo(75000);
        assertThat(employee.getAddress().street()).isEqualTo("123 Oak St");
        assertThat(employee.getAddress().city()).isEqualTo("Town");
    }
    
    @Test
    @DisplayName("Department should contain employees through composition")
    void testDepartmentHasEmployees() {
        Department dept = new Department("IT", "IT");
        
        Address addr1 = new Address("1 First St", "City1", "ST", "11111");
        Address addr2 = new Address("2 Second St", "City2", "ST", "22222");
        
        Employee emp1 = new Employee(1001, "Employee1", 60000, LocalDate.now().minusYears(3));
        emp1.setAddress(addr1);
        Employee emp2 = new Employee(1002, "Employee2", 65000, LocalDate.now().minusYears(2));
        emp2.setAddress(addr2);
        
        dept.addEmployee(emp1);
        dept.addEmployee(emp2);
        
        assertThat(dept.getEmployeeCount()).isEqualTo(2);
        assertThat(dept.getName()).isEqualTo("IT");
    }
    
    @Test
    @DisplayName("ProjectTeam should contain team members through composition")
    void testProjectTeamHasMembers() {
        ProjectTeam project = new ProjectTeam(
            "New Website",
            LocalDate.now(),
            LocalDate.now().plusMonths(6)
        );
        
        TeamMember lead = new TeamMember("Sarah Lead", "Project Manager");
        TeamMember dev = new TeamMember("Dave Developer", "Senior Developer");
        
        project.addMember(lead);
        project.addMember(dev);
        
        // Test that project has members
        String summary = project.getProjectSummary();
        assertThat(summary).contains("New Website");
        assertThat(summary).contains("2 members");
        assertThat(summary).contains("Sarah Lead");
        assertThat(summary).contains("Dave Developer");
    }
    
    @Test
    @DisplayName("TeamMember should have tasks through composition")
    void testTeamMemberHasTasks() {
        TeamMember member = new TeamMember("Alice", "Developer");
        
        member.assignTask("Write unit tests");
        member.assignTask("Code review");
        member.assignTask("Fix bugs");
        
        assertThat(member.getName()).isEqualTo("Alice");
        assertThat(member.getRole()).isEqualTo("Developer");
        assertThat(member.getTaskCount()).isEqualTo(3);
    }
    
    @Test
    @DisplayName("Address should format properly")
    void testAddressFormatting() {
        Address address = new Address(
            "789 Business Blvd",
            "Enterprise City",
            "CA",
            "90210"
        );
        
        String formatted = address.getFormattedAddress();
        assertThat(formatted).contains("789 Business Blvd");
        assertThat(formatted).contains("Enterprise City");
        assertThat(formatted).contains("CA");
        assertThat(formatted).contains("90210");
    }
    
    @Test
    @DisplayName("Exercise should demonstrate composition properly")
    void testMainMethod() {
        // Test that the main method runs without errors
        assertThatCode(() -> CompositionExercise.main(new String[]{}))
            .doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("Composition should handle null safety")
    void testNullSafety() {
        Department dept = new Department("Test", "TST");
        
        // Real Department class throws exception for null employee (better error handling)
        assertThatThrownBy(() -> dept.addEmployee(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Employee cannot be null");
        assertThat(dept.getEmployeeCount()).isEqualTo(0);
        
        ProjectTeam team = new ProjectTeam("Test", LocalDate.now(), LocalDate.now());
        
        // ProjectTeam should handle null member gracefully
        team.addMember(null);
        assertThat(team.getProjectSummary()).contains("0 members");
        
        TeamMember member = new TeamMember("Test", "Tester");
        
        // TeamMember should handle null/empty tasks gracefully
        member.assignTask(null);
        member.assignTask("");
        member.assignTask("   ");
        assertThat(member.getTaskCount()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Multiple departments should manage employees independently")
    void testMultipleDepartments() {
        // Test multiple departments with real HR classes
        Department eng = new Department("Engineering", "ENG");
        Department hr = new Department("Human Resources", "HR");
        
        // Add employees to different departments
        Address empAddr = new Address("123 Home St", "Suburb", "CA", "94001");
        
        Employee engineer1 = new Employee(1001, "Engineer1", 120000, LocalDate.now().minusYears(3));
        engineer1.setAddress(empAddr);
        Employee engineer2 = new Employee(1002, "Engineer2", 110000, LocalDate.now().minusYears(2));
        engineer2.setAddress(empAddr);
        Employee hrManager = new Employee(1003, "HR Manager", 85000, LocalDate.now().minusYears(4));
        hrManager.setAddress(empAddr);
        
        eng.addEmployee(engineer1);
        eng.addEmployee(engineer2);
        hr.addEmployee(hrManager);
        
        // Test department composition
        assertThat(eng.getEmployeeCount()).isEqualTo(2);
        assertThat(hr.getEmployeeCount()).isEqualTo(1);
        assertThat(eng.getName()).isEqualTo("Engineering");
        assertThat(hr.getName()).isEqualTo("Human Resources");
    }
}