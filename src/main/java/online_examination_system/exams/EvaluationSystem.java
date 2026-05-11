package online_examination_system.exams;

import java.util.List;

import online_examination_system.questions.Question;
import online_examination_system.users.Student;

// this is the evaluation system class - calculates score and saves result
public class EvaluationSystem {

    private String evaluationId;

    // constructor - stores evaluation id
    public EvaluationSystem(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    // checks each question with the matching answer and returns total score
    public int calculateScore(List<Question> questions, List<Answer> answers) {
        int totalScore = 0;
        System.out.println("\n  [EVAL]   Starting evaluation...");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String response = "";

            if (i < answers.size()) {
                response = answers.get(i).getStudentResponse();
            }

            System.out.println("  [EVAL]   Checking Q" + q.getQuestionID() + ":");
            totalScore += q.checkAnswer(response);
        }

        System.out.println("  [EVAL]   Total score: " + totalScore);
        return totalScore;
    }

    // evaluates one student's exam, creates result, and stores it with the student
    public Result evaluateExam(String resultId, Student student, Exam exam, List<Answer> answers) {
        try {
            if (answers.size() == 0) {
                throw new Exception("No answers submitted by " + student.getName());
            }

            int score = calculateScore(exam.getQuestions(), answers);
            Result result = new Result(resultId, student.getStudentId(), score, exam.getTotalMarks());

            student.addResult(result);
            exam.endExam();

            System.out.println("  [EVAL]   Result saved for: " + student.getName());
            return result;
        } catch (Exception e) {
            System.out.println("  [ERROR]  " + e.getMessage());
            return null;
        }
    }

    // getter - so other classes can read evaluation id
    public String getEvaluationId() {
        return evaluationId;
    }
}
