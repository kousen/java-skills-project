public class NamingConventions {
    
    private static final String COMPANY_NAME = "ACME Corp";
    private static final int MAX_EMPLOYEES = 100;
    
    public static void main(String[] args) {
        
        String employeeName = "John Doe";
        int employeeId = 12345;
        double salaryAmount = 75000.50;
        boolean isActive = true;
        
        
        String badname = "this violates conventions";
        String ANOTHERBADNAME = "this too";
        
        System.out.println("=== Proper Naming Conventions Demo ===");
        System.out.println("Company: " + COMPANY_NAME);
        System.out.println("Max Employees: " + MAX_EMPLOYEES);
        System.out.println();
        
        System.out.println("Employee Details:");
        System.out.println("Name: " + employeeName);
        System.out.println("ID: " + employeeId);
        System.out.println("Salary: $" + salaryAmount);
        System.out.println("Active: " + isActive);
        System.out.println();
        
        System.out.println("Bad examples (avoid these):");
        System.out.println("badname: " + badname);
        System.out.println("ANOTHERBADNAME: " + ANOTHERBADNAME);
    }
}