package online_examination_system.exams;

// this is the answer class - stores one response given by a student
public class Answer {

    private String answerId;
    private String questionId;
    private String studentResponse;

    // constructor - takes answer details and stores them
    public Answer(String answerId, String questionId, String studentResponse) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.studentResponse = studentResponse;
    }

    // prints the submitted answer for a question
    public void submitAnswer() {
        System.out.println("  [ANSWER] Q" + questionId + " -> " + studentResponse);
    }

    // getters - so other classes can read these values
    public String getAnswerId() {
        return answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getStudentResponse() {
        return studentResponse;
    }
}
