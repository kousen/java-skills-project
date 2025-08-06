package com.oreilly.javaskills.oop.exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import static com.oreilly.javaskills.oop.exercise.AccessModifiersExercise.BankAccount;
import static com.oreilly.javaskills.oop.exercise.AccessModifiersExercise.BankingService;

/**
 * Test class for com.oreilly.javaskills.oop.exercise.AccessModifiersExercise
 * These tests verify that the exercise demonstrates proper encapsulation
 */
class AccessModifiersExerciseTest {

    @Test
    @DisplayName("BankAccount should encapsulate data properly")
    void testBankAccountEncapsulation() {
        var account = new BankAccount("John Doe", 1000.0);
        
        // Test that public methods work
        assertThat(account.getAccountHolderName()).isEqualTo("John Doe");
        assertThat(account.getBalance()).isEqualTo(1000.0);
        assertThat(account.getAccountNumber()).startsWith("ACC");
        
        // Test deposit functionality
        account.deposit(250.0);
        assertThat(account.getBalance()).isEqualTo(1250.0);
        
        // Test withdrawal functionality
        boolean success = account.withdraw(200.0);
        assertThat(success).isTrue();
        assertThat(account.getBalance()).isEqualTo(1050.0);
        
        // Test insufficient funds
        boolean failed = account.withdraw(2000.0);
        assertThat(failed).isFalse();
        assertThat(account.getBalance()).isEqualTo(1050.0); // Balance unchanged
    }
    
    @Test
    @DisplayName("BankAccount should validate constructor parameters")
    void testBankAccountValidation() {
        // Test invalid name
        assertThatThrownBy(() -> new BankAccount("", 1000.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name cannot be empty");
        
        assertThatThrownBy(() -> new BankAccount(null, 1000.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("name cannot be empty");
        
        // Test negative initial balance
        assertThatThrownBy(() -> new BankAccount("John", -100.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be negative");
    }
    
    @Test
    @DisplayName("BankingService should have proper access levels")
    void testBankingServiceAccess() {
        var service = new BankingService();
        
        // Test that public constant is accessible
        assertThat(service.SERVICE_TYPE).isEqualTo("RETAIL");
        
        // Test that public method works
        assertThatCode(service::demonstrateAccessLevels).doesNotThrowAnyException();
        
        // Test that package-private method is accessible (same package)
        assertThatCode(service::generateReport).doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("Exercise main method should run without errors")
    void testMainMethod() {
        // Test that the main method completes without throwing exceptions
        assertThatCode(() -> AccessModifiersExercise.main(new String[]{}))
            .doesNotThrowAnyException();
    }
    
    @Test
    @DisplayName("BankAccount should generate unique account numbers")
    void testUniqueAccountNumbers() {
        var account1 = new BankAccount("Alice", 500.0);
        var account2 = new BankAccount("Bob", 1000.0);
        
        assertThat(account1.getAccountNumber()).isNotEqualTo(account2.getAccountNumber());
        assertThat(account1.getAccountNumber()).startsWith("ACC");
        assertThat(account2.getAccountNumber()).startsWith("ACC");
    }
    
    @Test
    @DisplayName("BankAccount should handle edge cases in transactions")
    void testTransactionEdgeCases() {
        var account = new BankAccount("Test User", 100.0);
        
        // Test zero and negative amounts
        account.deposit(0.0); // Should be rejected
        assertThat(account.getBalance()).isEqualTo(100.0);
        
        account.deposit(-50.0); // Should be rejected
        assertThat(account.getBalance()).isEqualTo(100.0);
        
        boolean withdrawal = account.withdraw(0.0); // Should be rejected
        assertThat(withdrawal).isFalse();
        assertThat(account.getBalance()).isEqualTo(100.0);
        
        withdrawal = account.withdraw(-25.0); // Should be rejected
        assertThat(withdrawal).isFalse();
        assertThat(account.getBalance()).isEqualTo(100.0);
    }
}