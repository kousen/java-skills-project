package com.oreilly.javaskills.oop.exercise;

/**
 * Try It Out Exercise: Access Modifiers in Java
 * <p>
 * This exercise demonstrates understanding of Java's four access modifiers:
 * - public: accessible from anywhere
 * - protected: accessible within package and by subclasses
 * - package-private (default): accessible within package only  
 * - private: accessible within the same class only
 * <p>
 * Instructions:
 * 1. Replace each "/* TODO: access modifier */" comment with the appropriate access modifier
 * 2. Consider what should be accessible from outside vs internal implementation
 * 3. Think about which methods are part of the public API vs helper methods
 * 4. Run the program to test your choices (it should compile and run successfully)
 * 5. Ask yourself: Could external classes break the encapsulation with your choices?
 * <p>
 * Hints:
 * - BankAccount fields: Should account data be directly accessible outside the class?
 * - Getter methods: These provide controlled access to internal data
 * - Transaction methods: These are the main operations clients need
 * - Validation methods: These are internal implementation details
 * - BankingService: Shows different scenarios for different access needs
 */
public class AccessModifiersExercise {

    public static void main(String[] args) {
        System.out.println("=== Access Modifiers Exercise ===\n");
        
        // Test the BankAccount class
        testBankAccount();
        
        // Test access from different contexts
        testAccessLevels();
    }
    
    /**
     * Test basic encapsulation with BankAccount
     */
    private static void testBankAccount() {
        System.out.println("1. TESTING BANK ACCOUNT ENCAPSULATION:");
        System.out.println("-------------------------------------");
        
        BankAccount account = new BankAccount("John Doe", 1000.0);
        
        // Test public methods (proper API)
        System.out.println("Account holder: " + account.getAccountHolderName());
        System.out.println("Initial balance: $" + account.getBalance());
        
        // Test controlled access through public methods
        account.deposit(250.0);
        System.out.println("After deposit: $" + account.getBalance());
        
        boolean success = account.withdraw(150.0);
        System.out.println("Withdrawal successful: " + success);
        System.out.println("Final balance: $" + account.getBalance());
        
        // Try invalid operations (should fail gracefully)
        account.withdraw(2000.0); // Insufficient funds
        account.deposit(-50.0);   // Negative deposit
        
        System.out.println();
    }
    
    /**
     * Test access levels between different contexts
     */
    private static void testAccessLevels() {
        System.out.println("2. TESTING ACCESS LEVELS:");
        System.out.println("-------------------------");
        
        BankingService service = new BankingService();
        service.demonstrateAccessLevels();
        
        System.out.println();
    }
    
    /**
     * Exercise: Complete this BankAccount class with proper encapsulation
     */
    static class BankAccount {
        // TODO: Add appropriate access modifiers to these fields
        // HINT: What should be accessible from outside vs inside only?
        /* TODO: access modifier */ final String accountHolderName;
        /* TODO: access modifier */ double balance;
        /* TODO: access modifier */ final String accountNumber;
        /* TODO: access modifier */ static int nextAccountNumber = 1000;
        
        // TODO: Constructor - what access level should this have?
        /* TODO: access modifier */ BankAccount(String accountHolderName, double initialBalance) {
            this.accountHolderName = validateName(accountHolderName);
            this.balance = validateInitialBalance(initialBalance);
            this.accountNumber = generateAccountNumber();
        }
        
        // TODO: Add appropriate access modifiers to these methods
        // HINT: Which methods are part of the public API vs internal implementation?
        
        /* TODO: access modifier */ String getAccountHolderName() {
            return accountHolderName;
        }
        
        /* TODO: access modifier */ double getBalance() {
            return balance;
        }
        
        /* TODO: access modifier */ String getAccountNumber() {
            return accountNumber;
        }
        
        /* TODO: access modifier */ void deposit(double amount) {
            if (amount <= 0) {
                System.out.println("Deposit amount must be positive");
                return;
            }
            balance += amount;
            logTransaction("DEPOSIT", amount);
        }
        
        /* TODO: access modifier */ boolean withdraw(double amount) {
            if (amount <= 0) {
                System.out.println("Withdrawal amount must be positive");
                return false;
            }
            if (amount > balance) {
                System.out.println("Insufficient funds");
                return false;
            }
            balance -= amount;
            logTransaction("WITHDRAWAL", amount);
            return true;
        }
        
        // Helper methods - should these be public?
        /* TODO: access modifier */ String validateName(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Account holder name cannot be empty");
            }
            return name.trim();
        }
        
        /* TODO: access modifier */ double validateInitialBalance(double balance) {
            if (balance < 0) {
                throw new IllegalArgumentException("Initial balance cannot be negative");
            }
            return balance;
        }
        
        /* TODO: access modifier */ String generateAccountNumber() {
            return "ACC" + (nextAccountNumber++);
        }
        
        /* TODO: access modifier */ void logTransaction(String type, double amount) {
            System.out.printf("Transaction: %s of $%.2f%n", type, amount);
        }
    }
    
    /**
     * Exercise: Complete this service class showing proper access control
     */
    @SuppressWarnings("FieldCanBeLocal")
    static class BankingService {
        // TODO: What access level should these fields have?
        /* TODO: access modifier */ final String serviceName = "Premium Banking";
        /* TODO: access modifier */ String serviceVersion = "2.1.0";  // Might be extended
        /* TODO: access modifier */ final String SERVICE_TYPE = "RETAIL"; // Public constant
        
        // TODO: Determine appropriate access levels for these methods
        
        /* TODO: access modifier */ void demonstrateAccessLevels() {
            System.out.println("Service: " + serviceName);
            System.out.println("Version: " + serviceVersion);
            System.out.println("Type: " + SERVICE_TYPE);
            
            // Call internal helper methods
            validateService();
            processInternalOperation();
        }
        
        // Internal validation - should this be public?
        /* TODO: access modifier */ void validateService() {
            System.out.println("Service validation completed");
        }
        
        // Protected method - might be overridden by subclasses
        /* TODO: access modifier */ void processInternalOperation() {
            System.out.println("Processing internal banking operation");
        }
        
        // Package-private utility - used by other classes in same package
        /* TODO: access modifier */ void generateReport() {
            System.out.println("Generating banking report");
        }
    }
}