public class OperatorPrecedence {
    
    public static void main(String[] args) {
        System.out.println("=== Operator Precedence Examples ===");
        
        // Salary calculation examples
        double baseSalary = 50000;
        double bonusPercentage = 0.10;
        int overtime = 20;
        double overtimeRate = 1.5;
        double hourlyRate = 25.0;
        
        // Precedence issue - multiplication before addition
        double wrongCalculation = baseSalary + overtime * overtimeRate * hourlyRate;
        System.out.println("Wrong (no parentheses): $" + wrongCalculation);
        
        // Correct calculation with parentheses
        double correctCalculation = baseSalary + (overtime * overtimeRate * hourlyRate);
        System.out.println("Correct (with parentheses): $" + correctCalculation);
        
        // Bonus calculation precedence
        double bonusWrong = baseSalary * bonusPercentage + 1000;  // Multiplies first
        double bonusCorrect = baseSalary * (bonusPercentage + 0.02); // Add to percentage first
        System.out.println("Bonus wrong precedence: $" + bonusWrong);
        System.out.println("Bonus correct precedence: $" + bonusCorrect);
        
        // Boolean operators
        boolean isFullTime = true;
        boolean hasBonus = true;
        int yearsOfService = 3;
        
        // Precedence: && has higher precedence than ||
        boolean eligible = isFullTime || hasBonus && yearsOfService > 2;
        System.out.println("Eligible (precedence): " + eligible);
        
        // Clear intent with parentheses
        boolean eligibleClear = isFullTime || (hasBonus && yearsOfService > 2);
        System.out.println("Eligible (clear intent): " + eligibleClear);
        
        // Different grouping
        boolean eligibleDifferent = (isFullTime || hasBonus) && yearsOfService > 2;
        System.out.println("Eligible (different logic): " + eligibleDifferent);
        
        // Assignment operators
        int a = 5;
        int b = 10;
        int c = 15;
        
        // Multiple assignments - right to left
        a = b = c = 20;
        System.out.println("Multiple assignment: a=" + a + ", b=" + b + ", c=" + c);
        
        // Compound assignment
        a += b *= 2; // b = b * 2, then a = a + b
        System.out.println("Compound assignment: a=" + a + ", b=" + b);
    }
}