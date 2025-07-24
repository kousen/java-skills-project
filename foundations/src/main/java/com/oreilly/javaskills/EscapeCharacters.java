package com.oreilly.javaskills;

/**
 * This class demonstrates the use of common escape characters in Java strings.
 * Escape characters are special sequences that represent non-printable or reserved characters.
 */
public class EscapeCharacters {

    public static void main(String[] args) {
        System.out.println("--- Demonstrating Escape Characters ---");

        // 1. Newline (`\n`)
        // The `\n` character inserts a line break.
        String withNewline = "First line\nSecond line";
        System.out.println("Newline Example:");
        System.out.println(withNewline);

        // 2. Tab (`\t`)
        // The `\t` character inserts a horizontal tab, useful for alignment.
        String withTab = "Column 1\tColumn 2\tColumn 3";
        System.out.println("\nTab Example:");
        System.out.println(withTab);
        System.out.println("Value A\tValue B\tValue C");

        // 3. Double Quote (`\"`)
        // To include a double quote inside a string literal, you must escape it.
        String withQuotes = "She said, \"Hello, Java!\"";
        System.out.println("\nDouble Quote Example:");
        System.out.println(withQuotes);

        // 4. Backslash (`\\`)
        // To include a literal backslash, you must escape it with another backslash.
        // This is very common in file paths on Windows.
        String filePath = "C:\\Users\\Guest\\Documents\\file.txt";
        System.out.println("\nBackslash Example:");
        System.out.println(filePath);

        // 5. Single Quote (`\'`)
        // While not always required in a String, it's good practice to escape single quotes.
        // It is required inside a character literal (char).
        String withSingleQuote = "It's a beautiful day."; // Works without escaping
        String withEscapedSingleQuote = "It\'s a beautiful day."; // Also works, but not necessary
        char quoteChar = '\''; // Required here
        System.out.println("\nSingle Quote Examples:");
        System.out.println(withSingleQuote + " (no escape needed)");
        System.out.println(withEscapedSingleQuote + " (char: " + quoteChar + ")");
        
        // 6. Modern Java: Text Blocks (Java 15+)
        // Text blocks eliminate the need for most escape characters!
        System.out.println("\n--- Modern Java: Text Blocks (Java 15+) ---");
        
        // Old way with escape characters
        String jsonOldWay = "{\n  \"name\": \"John Doe\",\n  \"age\": 30,\n  \"address\": \"123 Main St.\"\n}";
        System.out.println("Old way with escape characters:");
        System.out.println(jsonOldWay);
        
        // Modern way with text blocks - no escape characters needed!
        String jsonTextBlock = """
            {
              "name": "John Doe",
              "age": 30,
              "address": "123 Main St."
            }
            """;
        System.out.println("\nModern way with text blocks:");
        System.out.println(jsonTextBlock);
        
        // Text blocks also work great for SQL, HTML, etc.
        String sqlQuery = """
            SELECT employee_id, first_name, last_name
            FROM employees
            WHERE department = "Engineering"
            AND salary > 50000
            ORDER BY last_name
            """;
        System.out.println("\nSQL Example with text blocks:");
        System.out.println(sqlQuery);
        
        // Note: You still need to escape backslashes in text blocks
        String windowsPath = """
            File path on Windows still needs escaped backslashes:
            C:\\Users\\Guest\\Documents\\file.txt
            """;
        System.out.println("\nBackslashes still need escaping:");
        System.out.println(windowsPath);
    }
}
