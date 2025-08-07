package com.oreilly.javaskills.oop.exercise;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Import the real HR classes from the hr package
import com.oreilly.javaskills.oop.hr.Employee;
import com.oreilly.javaskills.oop.hr.Address;
import com.oreilly.javaskills.oop.hr.Department;

/**
 * Try It Out Exercise: Object Composition in Java
 * <p>
 * This exercise demonstrates building complex objects using composition.
 * The code below shows complete solutions with exercise tasks listed above each class.
 * <p>
 * Study Approach:
 * 1. Read the EXERCISE TASKS at the top of each class section
 * 2. Try to think through how you would implement each task
 * 3. Review the SOLUTION BELOW to see the implementation
 * 4. Run the exercise to see composition in action
 * 5. Try modifying the code or creating your own composition examples
 * <p>
 * Key Concepts to Observe:
 * - Uses real Employee, Address, and Department classes from the hr package
 * - Composition creates "has-a" relationships (Department HAS employees)
 * - Objects contain references to other objects (Employee HAS an address)
 * - Lists enable one-to-many relationships (Department HAS many employees)
 * - ProjectTeam demonstrates flexible composition patterns
 * - This approach promotes loose coupling and flexibility
 * - Composition is favored over inheritance for building complex systems
 * <p>
 * Real-World Examples to Consider:
 * - Car "has-a" Engine, Wheels, Dashboard
 * - Library "has" Books, Book "has-a" Author
 * - Computer "has-a" CPU, Memory, Storage
 * - Restaurant "has" Menu, Menu "has" MenuItems
 */
public class CompositionExercise {

    public static void main(String[] args) {
        System.out.println("=== Composition Exercise ===\n");
        
        // Test the composition structure
        testCompanyStructure();
        
        // Test project team composition
        testProjectTeam();
    }
    
    /**
     * Test the company structure with composition
     */
    private static void testCompanyStructure() {
        System.out.println("1. TESTING COMPANY STRUCTURE:");
        System.out.println("-----------------------------");
        
        // Create departments using real Department class
        Department engineering = new Department("Engineering", "ENG");
        Department marketing = new Department("Marketing", "MKT");
        
        // Create employees with addresses using real Employee and Address classes
        Address aliceAddress = new Address(
            "123 Main St", "San Francisco", "CA", "94102"
        );
        // Employee constructor: (int id, String name, double salary, LocalDate hireDate)
        Employee alice = new Employee(1001, "Alice Johnson",
                120000, LocalDate.now().minusYears(3));
        alice.setAddress(aliceAddress);
        
        Address bobAddress = new Address(
            "456 Oak Ave", "Oakland", "CA", "94601"
        );
        Employee bob = new Employee(1002, "Bob Smith",
                95000, LocalDate.now().minusYears(2));
        bob.setAddress(bobAddress);
        
        // Add employees to departments (demonstrates composition)
        engineering.addEmployee(alice);
        marketing.addEmployee(bob);
        
        // Display composition results
        System.out.println("Department: " + engineering.getName());
        System.out.println("Employees: " + engineering.getEmployeeCount());
        System.out.println("Employee: " + alice.getName() + " lives in " + alice.getAddress().city());
        System.out.println("Employee: " + bob.getName() + " lives in " + bob.getAddress().city());
        System.out.println("Marketing dept has " + marketing.getEmployeeCount() + " employees");
        
        // Demonstrate Department business analytics methods
        System.out.println("\n--- Department Analytics ---");
        engineering.setBudget(250000);
        System.out.println("Engineering Budget: $" + engineering.getBudget());
        System.out.println("Total Payroll: $" + engineering.getTotalPayroll());
        System.out.println("Average Salary: $" + engineering.getAveragesalary());
        System.out.println("Budget Utilization: " + engineering.getBudgetUtilization() + "%");
        System.out.println("Over Budget: " + engineering.isOverBudget());
        System.out.println("Highest Paid: " + engineering.getHighestPaidEmployee().getName());
        
        // Add another employee to show more analytics
        Employee charlie = new Employee(1003, "Charlie Wilson",
                105000, LocalDate.now().minusYears(1));
        charlie.setAddress(new Address("789 Pine St", "San Francisco", "CA", "94103"));
        engineering.addEmployee(charlie);
        
        System.out.println("Employees earning $100K+: " + 
            engineering.getEmployeesBySalaryRange(100000, 200000).size());
        System.out.println("Final Department Summary:");
        System.out.println(engineering.getDepartmentSummary());
        System.out.println();
    }
    
    /**
     * Test project team composition
     */
    private static void testProjectTeam() {
        System.out.println("2. TESTING PROJECT TEAM COMPOSITION:");
        System.out.println("------------------------------------");
        
        // Create a project team that starts now and ends in three months
        // ProjectTeam instances have names, start and end dates, and team members
        ProjectTeam webProject = new ProjectTeam(
            "Website Redesign", 
            LocalDate.now(), 
            LocalDate.now().plusMonths(3)
        );
        
        // Create team members (composition: team "has-a" members)
        // Team members have names, roles, and assigned tasks
        TeamMember lead = new TeamMember("Carol Davis", "Project Lead");
        TeamMember developer1 = new TeamMember("David Lee", "Senior Developer");
        TeamMember developer2 = new TeamMember("Emma Wilson", "Frontend Developer");
        TeamMember designer = new TeamMember("Frank Chen", "UI/UX Designer");
        
        // Add members to team
        webProject.addMember(lead);
        webProject.addMember(developer1);
        webProject.addMember(developer2);
        webProject.addMember(designer);
        
        // Assign tasks to members
        lead.assignTask("Define project scope and timeline");
        lead.assignTask("Coordinate with stakeholders");
        developer1.assignTask("Implement backend API");
        developer2.assignTask("Build responsive UI components");
        designer.assignTask("Create mockups and style guide");
        
        // Display project team info
        System.out.println(webProject.getProjectSummary());
    }
    
    /**
     * ProjectTeam class demonstrating composition
     * <p>
     * EXERCISE TASKS:
     * 1. ProjectTeam "has" team members (List<TeamMember>) - initialize and manage list
     * 2. Implement addMember() to add team members to the project
     * 3. Implement getProjectSummary() to display project and team information
     * <p>
     * SOLUTION BELOW:
     */
    static class ProjectTeam {
        private final String projectName;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final List<TeamMember> members;  // Team "has" members (composition)
        
        public ProjectTeam(String projectName, LocalDate startDate, LocalDate endDate) {
            this.projectName = projectName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.members = new ArrayList<>();  // Initialize members list
        }
        
        // Method to add a team member
        public void addMember(TeamMember member) {
            if (member != null && !members.contains(member)) {
                members.add(member);
            }
        }
        
        public String getProjectSummary() {
            StringBuilder summary = new StringBuilder();
            summary.append(String.format("Project: %s%n", projectName));
            summary.append(String.format("Duration: %s to %s%n", startDate, endDate));
            summary.append(String.format("Team Size: %d members%n", members.size()));
            summary.append("Team Members:%n");
            
            for (TeamMember member : members) {
                summary.append(String.format("  - %s (%s) - %d tasks%n", 
                    member.getName(), member.getRole(), member.getTaskCount()));
            }
            
            return summary.toString();
        }
    }
    
    /**
     * TeamMember class with composition
     * <p>
     * EXERCISE TASKS:
     * 1. TeamMember "has" tasks (List<String>) - initialize and manage list
     * 2. Implement assignTask() to add tasks to the member's task list
     * 3. Implement getTaskCount() to return the number of assigned tasks
     * <p>
     * SOLUTION BELOW:
     */
    static class TeamMember {
        private final String name;
        private final String role;
        private final List<String> tasks;  // Member "has" tasks (composition)
        
        public TeamMember(String name, String role) {
            this.name = name;
            this.role = role;
            this.tasks = new ArrayList<>();  // Initialize tasks list
        }
        
        // Method to assign a task
        public void assignTask(String task) {
            if (task != null && !task.trim().isEmpty()) {
                tasks.add(task);
            }
        }
        
        public String getName() {
            return name;
        }
        
        public String getRole() {
            return role;
        }
        
        public int getTaskCount() {
            return tasks.size();
        }
    }
}