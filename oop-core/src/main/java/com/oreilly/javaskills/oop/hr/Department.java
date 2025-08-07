package com.oreilly.javaskills.oop.hr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Department {
    private final String name;
    private final String code;
    private Employee manager;
    private final List<Employee> employees;
    private double budget;
    
    public Department(String name, String code) {
        this.name = validateName(name);
        this.code = validateCode(code);
        this.employees = new ArrayList<>();
        this.budget = 0.0;
    }
    
    public Department(String name, String code, double budget) {
        this(name, code);
        this.budget = validateBudget(budget);
    }
    
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }
        return name.trim();
    }
    
    private String validateCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Department code cannot be null or empty");
        }
        String trimmed = code.trim().toUpperCase();
        if (trimmed.length() < 2 || trimmed.length() > 5) {
            throw new IllegalArgumentException("Department code must be 2-5 characters");
        }
        return trimmed;
    }
    
    private double validateBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        return budget;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getCode() {
        return code;
    }
    
    public Employee getManager() {
        return manager;
    }
    
    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees);
    }
    
    public double getBudget() {
        return budget;
    }
    
    // Setters
    public void setManager(Employee manager) {
        this.manager = manager;
        if (manager != null && !employees.contains(manager)) {
            addEmployee(manager);
        }
    }
    
    public void setBudget(double budget) {
        this.budget = validateBudget(budget);
    }
    
    // Employee management methods
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        if (!employees.contains(employee)) {
            employees.add(employee);
            employee.setDepartment(this);
        }
    }
    
    public boolean removeEmployee(Employee employee) {
        if (employee == null) return false;
        
        boolean removed = employees.remove(employee);
        if (removed) {
            employee.setDepartment(null);
            // If removing the manager, clear manager reference
            if (employee.equals(manager)) {
                manager = null;
            }
        }
        return removed;
    }
    
    public int getEmployeeCount() {
        return employees.size();
    }
    
    public boolean hasEmployee(Employee employee) {
        return employee != null && employees.contains(employee);
    }
    
    // Business logic methods
    public double getTotalPayroll() {
        return employees.stream()
                .mapToDouble(Employee::getSalary)
                .sum();
    }
    
    public double getAveragesalary() {
        if (employees.isEmpty()) return 0.0;
        return getTotalPayroll() / employees.size();
    }
    
    public Employee getHighestPaidEmployee() {
        return employees.stream()
                .max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()))
                .orElse(null);
    }
    
    public List<Employee> getEmployeesBySalaryRange(double minSalary, double maxSalary) {
        return employees.stream()
                .filter(emp -> emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary)
                .toList();
    }
    
    public double getBudgetUtilization() {
        if (budget == 0) return 0.0;
        return (getTotalPayroll() / budget) * 100;
    }
    
    public boolean isOverBudget() {
        return budget > 0 && getTotalPayroll() > budget;
    }
    
    public String getDepartmentSummary() {
        return String.format(
            "Department[Name=%s, Code=%s, Employees=%d, Payroll=$%.2f, Budget=$%.2f, Utilization=%.1f%%]",
            name, code, getEmployeeCount(), getTotalPayroll(), budget, getBudgetUtilization()
        );
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Department that = (Department) obj;
        return Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", name, code);
    }
}