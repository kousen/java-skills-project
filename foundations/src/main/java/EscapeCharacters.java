
package com.oreilly.javaskills.foundations;

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
        String withEscapedSingleQuote = "It\'s a beautiful day."; // Also works
        char quoteChar = '\''; // Required here
        System.out.println("\nSingle Quote Example:");
        System.out.println(withEscapedSingleQuote + " (char: " + quoteChar + ")");
    }
}
