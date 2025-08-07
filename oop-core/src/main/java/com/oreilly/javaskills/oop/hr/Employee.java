package com.oreilly.javaskills.oop.hr;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Employee {
    private final int id;
    private String name;
    private double salary;
    private final LocalDate hireDate;
    private boolean active;
    private Address address;
    private Department department;
    
    public Employee(int id, String name, double salary, LocalDate hireDate) {
        this.id = validateId(id);
        this.name = validateName(name);
        this.salary = validateSalary(salary);
        this.hireDate = validateHireDate(hireDate);
        this.active = true;
    }
    
    // Validation methods demonstrating encapsulation
    private int validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Employee ID must be positive");
        }
        return id;
    }
    
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty");
        }
        return name.trim();
    }
    
    private double validateSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        return salary;
    }
    
    private LocalDate validateHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            throw new IllegalArgumentException("Hire date cannot be null");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Hire date cannot be in the future");
        }
        return hireDate;
    }
    
    // Getters (controlled access)
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    // Setters with validation (controlled modification)
    public void setName(String name) {
        this.name = validateName(name);
    }
    
    public void setSalary(double salary) {
        this.salary = validateSalary(salary);
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    
    // Business logic methods
    public int getYearsOfService() {
        return Period.between(hireDate, LocalDate.now()).getYears();
    }
    
    public double getAnnualBonus() {
        int years = getYearsOfService();
        if (years < 1) return 0;
        if (years < 3) return salary * 0.02;
        if (years < 5) return salary * 0.05;
        return salary * 0.08;
    }
    
    public void giveRaise(double percentage) {
        if (percentage < 0) {
            throw new IllegalArgumentException("Raise percentage cannot be negative");
        }
        this.salary = salary * (1 + percentage / 100);
    }
    
    public String getEmployeeSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("Employee[ID=%d, Name=%s, Salary=$%.2f, Years=%d", 
                      id, name, salary, getYearsOfService()));
        
        if (address != null) {
            summary.append(", Address=").append(address.city());
        }
        
        if (department != null) {
            summary.append(", Dept=").append(department.getName());
        }
        
        summary.append("]");
        return summary.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return id == employee.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return getEmployeeSummary();
    }
}