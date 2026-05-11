package online_examination_system.exams;

import java.util.ArrayList;
import java.util.List;

import online_examination_system.questions.Question;

// this is the exam class - stores exam details and its questions
public class Exam {

    private String examId;
    private String title;
    private int duration;
    private String scheduledTime;
    private List<Question> questionsList;
    private boolean isActive;
    private int totalMarks;

    // constructor - sets exam details and creates an empty question list
    public Exam(String examId, String title, int duration, String scheduledTime) {
        this.examId = examId;
        this.title = title;
        this.duration = duration;
        this.scheduledTime = scheduledTime;
        this.questionsList = new ArrayList<Question>();
        this.isActive = false;
        this.totalMarks = 0;
    }

    // adds a question to this exam and updates total marks
    public void addQuestion(Question q) {
        questionsList.add(q);
        totalMarks += q.getMarks();
    }

    // starts the exam and displays all questions
    public void startExam() {
        isActive = true;
        System.out.println("\n  ============================================");
        System.out.println("  Exam  : " + title);
        System.out.println("  Time  : " + scheduledTime + " | Duration: " + duration + " mins");
        System.out.println("  Total : " + questionsList.size()
                + " questions | " + totalMarks + " marks");
        System.out.println("  ============================================");

        for (int i = 0; i < questionsList.size(); i++) {
            questionsList.get(i).displayQuestion();
        }
    }

    // ends the exam
    public void endExam() {
        isActive = false;
        System.out.println("  [EXAM]   \"" + title + "\" has ended.");
    }

    // getters - so other classes can read these values
    public String getExamId() {
        return examId;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public List<Question> getQuestions() {
        return questionsList;
    }

    public boolean isActive() {
        return isActive;
    }
}
