import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StringFormatting {
    
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
        
        // StringBuilder for complex building
        StringBuilder sb = new StringBuilder();
        sb.append("Employee Summary: ")
          .append(employeeName)
          .append(" (")
          .append(employeeId)
          .append(") earning ")
          .append(currencyFormat.format(salary))
          .append(" since ")
          .append(hireDate.format(dateFormatter));
        System.out.println("StringBuilder: " + sb.toString());
    }
}