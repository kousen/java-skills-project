package com.oreilly.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Security Examples - Safe vs Unsafe SQL Practices
 * 
 * This class demonstrates the difference between vulnerable SQL string concatenation
 * and safe parameterized queries using prepared statements.
 */
public class DatabaseExamples {
    
    private final Connection connection;
    
    public DatabaseExamples(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * VULNERABLE APPROACH - DON'T DO THIS!
     * String concatenation allows SQL injection attacks.
     * 
     * Example attack: If userInput is "Robert'; DROP TABLE employees; --"
     * The resulting query becomes:
     * SELECT * FROM employees WHERE name = 'Robert'; DROP TABLE employees; --'
     */
    public List<String> vulnerableQuery(String userInput) throws SQLException {
        // This is what NOT to do - direct string concatenation
        String sql = "SELECT name FROM employees WHERE name = '" + userInput + "'";
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        List<String> results = new ArrayList<>();
        while (rs.next()) {
            results.add(rs.getString("name"));
        }
        
        return results;
    }
    
    /**
     * SAFE APPROACH - Use prepared statements with parameterized queries.
     * The database treats user input as data only, never as executable SQL.
     * 
     * Even if userInput contains "Robert'; DROP TABLE employees; --",
     * it will be treated as a literal string to search for, not as SQL commands.
     */
    public List<String> safeQuery(String userInput) throws SQLException {
        // Safe parameterized query with placeholder
        String sql = "SELECT name FROM employees WHERE name = ?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, userInput); // This binds the parameter safely
        
        ResultSet rs = stmt.executeQuery();
        
        List<String> results = new ArrayList<>();
        while (rs.next()) {
            results.add(rs.getString("name"));
        }
        
        return results;
    }
    
    /**
     * SPRING DATA JPA EQUIVALENT - Framework protection
     * <p>
     * Modern frameworks like Spring Data JPA automatically use parameterized queries:
     * 
     * @Query("SELECT e FROM Employee e WHERE e.name = :name")
     * List<Employee> findByName(@Param("name") String name);
     * <p>
     * Or using method naming conventions:
     * List<Employee> findByName(String name);
     * 
     * Both generate safe parameterized queries under the hood.
     */
    
    /**
     * BATCH OPERATIONS - Also use prepared statements for multiple operations
     */
    public void safeBatchInsert(List<String> names) throws SQLException {
        String sql = "INSERT INTO employees (name) VALUES (?)";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        for (String name : names) {
            stmt.setString(1, name);
            stmt.addBatch();
        }
        
        stmt.executeBatch(); // Execute all safely
    }
    
    /**
     * MULTIPLE PARAMETERS - Prepared statements work with any number of parameters
     */
    public List<String> safeMultiParameterQuery(String name, String department, double minSalary) 
            throws SQLException {
        String sql = """
            SELECT name FROM employees 
            WHERE name LIKE ? 
            AND department = ? 
            AND salary >= ?
            """;
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, "%" + name + "%"); // LIKE pattern - still safe
        stmt.setString(2, department);
        stmt.setDouble(3, minSalary);
        
        ResultSet rs = stmt.executeQuery();
        
        List<String> results = new ArrayList<>();
        while (rs.next()) {
            results.add(rs.getString("name"));
        }
        
        return results;
    }
}