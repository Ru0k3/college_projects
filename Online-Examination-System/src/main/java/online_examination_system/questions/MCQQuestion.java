package online_examination_system.questions;

public class MCQQuestion extends Question {

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;

    public MCQQuestion(String questionId, String questionText, int marks,
                       String optionA, String optionB, String optionC, String optionD,
                       String correctOption) {
        super(questionId, questionText, marks);
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption == null ? "" : correctOption.trim().toUpperCase();
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    @Override
    public int checkAnswer(String response) {
        if (response == null) {
            return 0;
        }
        return correctOption.equals(response.trim().toUpperCase()) ? marks : 0;
    }

    @Override
    public void displayQuestion() {
        super.displayQuestion();
        System.out.println("    A) " + optionA);
        System.out.println("    B) " + optionB);
        System.out.println("    C) " + optionC);
        System.out.println("    D) " + optionD);
    }
}
