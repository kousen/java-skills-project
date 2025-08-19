import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoggingExerciseTest {
    
    @Test
    void testLoggingExerciseRunsWithoutError() {
        // This test verifies that the LoggingExercise runs without throwing exceptions
        // Note: This will show incomplete TODOs, which is expected for the exercise
        assertDoesNotThrow(() -> LoggingExercise.main(new String[]{}));
    }
    
    @Test
    void testExerciseInstantiation() {
        assertDoesNotThrow(() -> {
            LoggingExercise exercise = new LoggingExercise();
            assertNotNull(exercise);
        });
    }
}