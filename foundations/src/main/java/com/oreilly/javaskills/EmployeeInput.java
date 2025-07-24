package com.oreilly.javaskills;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeInput {
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        System.out.println("=== Employee Data Entry System ===");
        
        try (Scanner scanner = new Scanner(System.in)) {
            Employee employee = collectEmployeeData(scanner);
            displayEmployee(employee);
        } catch (Exception e) {
            System.err.println("Error collecting employee data: " + e.getMessage());
        }
    }
    
    private static Employee collectEmployeeData(Scanner scanner) {
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter employee ID: ");
        int id = getIntInput(scanner);
        
        System.out.print("Enter salary: $");
        double salary = getDoubleInput(scanner);
        
        System.out.print("Enter hire date (yyyy-MM-dd): ");
        LocalDate hireDate = getDateInput(scanner);
        
        System.out.print("Is employee active? (true/false): ");
        boolean isActive = getBooleanInput(scanner);
        
        return new Employee(name, id, salary, hireDate, isActive);
    }
    
    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter a valid integer: ");
            }
        }
    }
    
    private static double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter a valid amount: $");
            }
        }
    }
    
    private static LocalDate getDateInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format. Please use yyyy-MM-dd: ");
            }
        }
    }
    
    private static boolean getBooleanInput(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if ("true".equals(input) || "yes".equals(input) || "y".equals(input)) {
                return true;
            } else if ("false".equals(input) || "no".equals(input) || "n".equals(input)) {
                return false;
            } else {
                System.out.print("Please enter true/false (or yes/no): ");
            }
        }
    }
    
    private static void displayEmployee(Employee employee) {
        System.out.println("\n=== Employee Created ===");
        System.out.println("Name: " + employee.name());
        System.out.println("ID: " + employee.id());
        System.out.println("Salary: $" + String.format("%,.2f", employee.salary()));
        System.out.println("Hire Date: " + employee.hireDate().format(DATE_FORMAT));
        System.out.println("Active: " + employee.isActive());
    }
    
    record Employee(String name, int id, double salary, LocalDate hireDate, boolean isActive) {
        public Employee {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive");
            }
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
            if (hireDate == null) {
                throw new IllegalArgumentException("Hire date cannot be null");
            }
        }
    }
}