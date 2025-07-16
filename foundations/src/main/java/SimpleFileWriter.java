
package com.oreilly.javaskills.foundations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * This class demonstrates writing content to a file in Java, highlighting the
 * difference between the modern java.nio and traditional java.io approaches.
 */
public class SimpleFileWriter {

    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) throws IOException {
        // Ensure the output directory exists
        Files.createDirectories(Paths.get(OUTPUT_DIR));

        List<String> linesToWrite = Arrays.asList(
                "Line 1: The quick brown fox jumps over the lazy dog.",
                "Line 2: This is a simple file writing demonstration.",
                "Line 3: We will use two different methods."
        );

        // Method 1: Modern java.nio approach (Preferred)
        writeWithNio(linesToWrite);

        // Method 2: Traditional java.io approach
        writeWithIo(linesToWrite);

        System.out.println("Files written successfully to the '" + OUTPUT_DIR + "' directory.");
    }

    /**
     * Writes lines to a file using the modern java.nio API.
     * This is the recommended approach for new code.
     */
    private static void writeWithNio(List<String> lines) throws IOException {
        Path filePath = Paths.get(OUTPUT_DIR, "nio_output.txt");

        // try-with-resources ensures the writer is automatically closed.
        // Files.newBufferedWriter is efficient as it's already buffered.
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Writes a platform-independent newline character.
            }
        }
    }

    /**
     * Writes lines to a file using the traditional java.io API.
     * This shows how older codebases might handle file writing.
     */
    private static void writeWithIo(List<String> lines) throws IOException {
        // Using PrintWriter for convenient methods like println.
        // It wraps a BufferedWriter, which wraps a FileWriter for efficiency.
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_DIR + "/io_output.txt")))) {
            for (String line : lines) {
                writer.println(line); // println automatically adds a newline.
            }
        }
    }
}
