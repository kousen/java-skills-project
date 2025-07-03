import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Operator Precedence Demonstrations")
public class OperatorPrecedenceTest {
    
    @Test
    @DisplayName("Arithmetic operators: multiplication before addition")
    void demonstrateArithmeticPrecedence() {
        double baseSalary = 50000;
        int overtime = 20;
        double overtimeRate = 1.5;
        double hourlyRate = 25.0;
        
        // Without parentheses: multiplication happens first
        double calculation = baseSalary + overtime * overtimeRate * hourlyRate;
        double expected = 50000 + (20 * 1.5 * 25.0); // 50000 + 750 = 50750
        
        assertThat(calculation)
            .isEqualTo(expected)
            .isEqualTo(50750.0);
            
        // Bonus calculation precedence
        double bonusPercentage = 0.10;
        double bonusCalculation = baseSalary * bonusPercentage + 1000;
        double bonusExpected = (50000 * 0.10) + 1000; // 5000 + 1000 = 6000
        
        assertThat(bonusCalculation)
            .isEqualTo(bonusExpected)
            .isEqualTo(6000.0);
    }
    
    @Test
    @DisplayName("Boolean operators: && has higher precedence than ||")
    void demonstrateBooleanPrecedence() {
        boolean isFullTime = true;
        boolean hasBonus = false;
        int yearsOfService = 3;
        
        // Without parentheses: && binds tighter than ||
        boolean eligible = isFullTime || hasBonus && yearsOfService > 2;
        // Equivalent to: isFullTime || (hasBonus && yearsOfService > 2)
        // true || (false && true) = true || false = true
        
        assertThat(eligible).isTrue();
        
        // Different grouping changes the result
        isFullTime = false;
        hasBonus = true;
        yearsOfService = 1;
        
        boolean eligibleDefault = isFullTime || hasBonus && yearsOfService > 2;
        boolean eligibleGrouped = (isFullTime || hasBonus) && yearsOfService > 2;
        
        // false || (true && false) = false || false = false
        assertThat(eligibleDefault).isFalse();
        
        // (false || true) && false = true && false = false  
        assertThat(eligibleGrouped).isFalse();
    }
    
    @Test
    @DisplayName("Assignment operators: right-to-left associativity")
    void demonstrateAssignmentPrecedence() {
        int a = 5;
        int b = 10;
        int c = 15;
        
        // Multiple assignments work right to left
        a = b = c = 20;
        // Equivalent to: a = (b = (c = 20))
        
        assertThat(a).isEqualTo(20);
        assertThat(b).isEqualTo(20);
        assertThat(c).isEqualTo(20);
        
        // Compound assignment precedence
        a = 5;
        b = 10;
        a += b *= 2; // b = b * 2 first, then a = a + b
        
        assertThat(b).isEqualTo(20); // 10 * 2
        assertThat(a).isEqualTo(25); // 5 + 20
    }
    
    @Test
    @DisplayName("Ternary operator: lower precedence than most operators")
    void demonstrateTernaryPrecedence() {
        int salary = 60000;
        boolean isManager = true;
        
        // Ternary has lower precedence than comparison
        int bonus = salary > 50000 ? 1000 : 0;
        // Equivalent to: (salary > 50000) ? 1000 : 0
        
        assertThat(bonus).isEqualTo(1000);
        
        // Complex ternary expression
        int years = 3;
        boolean hasBonus = true;
        
        // Without parentheses - precedence can be confusing
        boolean eligible = isManager ? salary > 80000 && hasBonus : years > 5;
        // Equivalent to: isManager ? ((salary > 80000) && hasBonus) : (years > 5)
        // true ? (false && true) : false = true ? false : false = false
        
        assertThat(eligible).isFalse();
    }
    
    @Test
    @DisplayName("Increment/decrement: pre vs post increment in expressions")
    void demonstrateIncrementDecrementPrecedence() {
        int i = 5;
        
        // Pre-increment: increment first, then use value
        int preResult = ++i * 2;
        assertThat(i).isEqualTo(6);
        assertThat(preResult).isEqualTo(12); // 6 * 2
        
        // Reset and test post-increment
        i = 5;
        int postResult = i++ * 2;
        assertThat(i).isEqualTo(6);
        assertThat(postResult).isEqualTo(10); // 5 * 2, then i becomes 6
        
        // Addition with increment
        i = 5;
        int preAdd = 2 + ++i;
        assertThat(i).isEqualTo(6);
        assertThat(preAdd).isEqualTo(8); // 2 + 6
        
        i = 5;
        int postAdd = 2 + i++;
        assertThat(i).isEqualTo(6);
        assertThat(postAdd).isEqualTo(7); // 2 + 5, then i becomes 6
    }
    
    @Test
    @DisplayName("Modulus operator: same precedence as multiplication and division")
    void demonstrateModulusPrecedence() {
        // Modulus has same precedence as * and /
        int result = 10 + 15 % 4;
        int expected = 10 + (15 % 4); // 10 + 3 = 13
        
        assertThat(result)
            .isEqualTo(expected)
            .isEqualTo(13);
        
        // Common modulus use cases
        int number = 17;
        boolean isEven = (number % 2) == 0;
        assertThat(isEven).isFalse();
        
        // Time calculations
        int totalSeconds = 3665; // 1 hour, 1 minute, 5 seconds
        int minutes = (totalSeconds / 60) % 60;
        int seconds = totalSeconds % 60;
        
        assertThat(minutes).isEqualTo(1);
        assertThat(seconds).isEqualTo(5);
    }
    
    @Test
    @DisplayName("Math.pow() for exponentiation (no ** operator in Java)")
    void demonstrateExponentiationWithMathPow() {
        // Java has no exponentiation operator like Python's **
        double base = 2.0;
        double exponent = 3.0;
        double result = Math.pow(base, exponent);
        
        assertThat(result).isEqualTo(8.0);
        
        // For integer results, cast is needed
        int intBase = 3;
        int intExponent = 2;
        int intResult = (int) Math.pow(intBase, intExponent);
        
        assertThat(intResult).isEqualTo(9);
        
        // For small integer powers, multiplication is clearer
        int x = 4;
        int squared = x * x;
        int cubed = x * x * x;
        
        assertThat(squared).isEqualTo(16);
        assertThat(cubed).isEqualTo(64);
        
        // Verify they match Math.pow()
        assertThat(squared).isEqualTo((int) Math.pow(x, 2));
        assertThat(cubed).isEqualTo((int) Math.pow(x, 3));
    }
}