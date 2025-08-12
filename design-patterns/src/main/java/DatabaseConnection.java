import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    
    private static volatile DatabaseConnection instance;
    
    private Connection connection;
    private final String url;
    private final Properties properties;
    
    // Private constructor prevents direct instantiation
    private DatabaseConnection() {
        this.url = "jdbc:h2:mem:employee_db;DB_CLOSE_DELAY=-1";
        this.properties = new Properties();
        this.properties.setProperty("user", "sa");
        this.properties.setProperty("password", "");
        
        initializeConnection();
        createTables();
    }
    
    // Thread-safe singleton implementation (double-checked locking)
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton cannot be cloned");
    }

    private void initializeConnection() {
        try {
            this.connection = DriverManager.getConnection(url, properties);
            System.out.println("Database connection established: " + url);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }
    
    private void createTables() {
        String createEmployeeTable = """
            CREATE TABLE IF NOT EXISTS employees (
                id INT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                salary DECIMAL(10,2) NOT NULL,
                hire_date DATE NOT NULL,
                department_id INT,
                active BOOLEAN DEFAULT TRUE
            )
            """;
        
        String createDepartmentTable = """
            CREATE TABLE IF NOT EXISTS departments (
                id INT PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                code VARCHAR(10) NOT NULL UNIQUE,
                budget DECIMAL(12,2)
            )
            """;
        
        try (var stmt = connection.createStatement()) {
            stmt.execute(createDepartmentTable);
            stmt.execute(createEmployeeTable);
            System.out.println("Database tables created successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database tables", e);
        }
    }
    
    public Connection getConnection() {
        try {
            // Check if connection is still valid
            if (connection == null || connection.isClosed()) {
                initializeConnection();
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Error accessing database connection", e);
        }
    }
    
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null; // Set to null after closing
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    // Example usage method
    public static void demonstrateSingleton() {
        System.out.println("=== Singleton Pattern Demo ===");
        
        // Get multiple references - should be same instance
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        DatabaseConnection db3 = DatabaseConnection.getInstance();
        
        System.out.println("db1 == db2: " + (db1 == db2));
        System.out.println("db2 == db3: " + (db2 == db3));
        System.out.println("db1 == db3: " + (db1 == db3));
        
        System.out.println("All references point to same instance: " + 
                          (db1 == db2 && db2 == db3));
        
        System.out.println("Instance hash codes:");
        System.out.println("db1: " + System.identityHashCode(db1));
        System.out.println("db2: " + System.identityHashCode(db2));
        System.out.println("db3: " + System.identityHashCode(db3));
        
        System.out.println("Connection status: " + db1.isConnected());
    }
    
    public static void main(String[] args) {
        demonstrateSingleton();
        
        // Clean up
        DatabaseConnection.getInstance().close();
    }
}