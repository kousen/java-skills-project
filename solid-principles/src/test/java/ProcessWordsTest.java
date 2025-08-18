import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ProcessWordsTest {
    private final ProcessWords processWords = new ProcessWords();

    @Test
    void testProcessWords() {
        List<String> result =
                processWords.getSortedEvenLengthUpperCaseWords("This is an array of words");
        assertThat(result).containsExactlyInAnyOrder("IS", "AN", "OF", "THIS");
    }

}