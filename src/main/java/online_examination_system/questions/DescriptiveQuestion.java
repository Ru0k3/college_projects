package online_examination_system.questions;

import java.util.LinkedHashSet;
import java.util.Set;

public class DescriptiveQuestion extends Question {

    private String expectedAnswer;

    public DescriptiveQuestion(String questionId, String questionText, int marks, String expectedAnswer) {
        super(questionId, questionText, marks);
        this.expectedAnswer = expectedAnswer == null ? "" : expectedAnswer.trim();
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    @Override
    public int checkAnswer(String response) {
        if (response == null) {
            return 0;
        }

        String submittedAnswer = response.trim();
        if (submittedAnswer.isEmpty()) {
            return 0;
        }

        if (expectedAnswer.isEmpty()) {
            return marks;
        }

        Set<String> expectedKeywords = extractKeywords(expectedAnswer);
        if (expectedKeywords.isEmpty()) {
            return expectedAnswer.equalsIgnoreCase(submittedAnswer) ? marks : 0;
        }

        Set<String> submittedKeywords = extractKeywords(submittedAnswer);
        int matched = 0;
        for (String keyword : expectedKeywords) {
            if (submittedKeywords.contains(keyword)) {
                matched++;
            }
        }

        if (matched == 0) {
            return 0;
        }
        return Math.max(1, (int) Math.round((matched * marks) / (double) expectedKeywords.size()));
    }

    private Set<String> extractKeywords(String text) {
        Set<String> keywords = new LinkedHashSet<>();
        String[] tokens = text.toLowerCase().replaceAll("[^a-z0-9 ]", " ").split("\\s+");
        for (String token : tokens) {
            if (token.length() > 3 && !isCommonWord(token)) {
                keywords.add(token);
            }
        }
        return keywords;
    }

    private boolean isCommonWord(String token) {
        return token.equals("this") || token.equals("that") || token.equals("with")
            || token.equals("from") || token.equals("based") || token.equals("concept")
            || token.equals("explain") || token.equals("programming");
    }
}
