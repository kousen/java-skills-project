import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StringFormatting {
    
    @SuppressWarnings("StringBufferReplaceableByString")
    public static void main(String[] args) {
        String employeeName = "Alice Johnson";
        int employeeId = 67890;
        double salary = 82500.75;
        LocalDate hireDate = LocalDate.of(2020, 3, 15);
        
        System.out.println("=== String Formatting Examples ===");
        
        // Basic concatenation (avoid in production)
        String basic = "Employee: " + employeeName + " (ID: " + employeeId + ")";
        System.out.println("Basic: " + basic);
        
        // String.format() - C-style formatting
        String formatted = String.format("Employee: %s (ID: %05d) - Salary: $%.2f", 
                                        employeeName, employeeId, salary);
        System.out.println("Formatted: " + formatted);
        
        // System.out.printf() - direct output formatting
        System.out.printf("Printf: Employee: %s (ID: %05d) - Salary: $%.2f%n", 
                         employeeName, employeeId, salary);
        
        // Text blocks (Java 15+)
        String textBlock = """
                Employee Report:
                ================
                Name: %s
                ID: %d
                Salary: $%,.2f
                Hire Date: %s
                """.formatted(employeeName, employeeId, salary, hireDate);
        System.out.println(textBlock);
        
        // NumberFormat for currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String currencyFormatted = String.format("Salary: %s", currencyFormat.format(salary));
        System.out.println("Currency: " + currencyFormatted);
        
        // Date formatting
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String dateFormatted = String.format("Hired: %s", hireDate.format(dateFormatter));
        System.out.println("Date: " + dateFormatted);
        
        // StringBuilder for complex building (better for loops or conditional construction)
        StringBuilder sb = new StringBuilder();
        sb.append("Employee Summary: ")
          .append(employeeName)
          .append(" (")
          .append(employeeId)
          .append(") earning ")
          .append(currencyFormat.format(salary))
          .append(" since ")
          .append(hireDate.format(dateFormatter));
        System.out.println("StringBuilder: " + sb);
        
        // SLF4J parameterized logging (modern best practice)
        Logger logger = LoggerFactory.getLogger(StringFormatting.class);
        logger.info("Employee {} (ID: {}) hired on {} with salary ${}", 
                   employeeName, employeeId, hireDate, salary);
        System.out.println("Logging: Check console/logs for SLF4J output above");
    }
}