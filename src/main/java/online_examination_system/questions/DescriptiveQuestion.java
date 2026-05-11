package online_examination_system.questions;

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

        return expectedAnswer.equalsIgnoreCase(submittedAnswer) ? marks : 0;
    }
}
