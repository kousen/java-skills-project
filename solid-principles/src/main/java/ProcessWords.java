import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProcessWords {

    public List<String> getSortedEvenLengthUpperCaseWords(String sentence) {
        String[] words = sentence.split("\\s+");

        List<String> evenLengthWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() % 2 == 0) {
                evenLengthWords.add(word.toUpperCase());
            }
        }
        evenLengthWords.sort(new Comparator<>() {
            @Override
            public int compare(String word1, String word2) {
                return word1.length() - word2.length();
            }
        });
        return evenLengthWords;
    }
}
