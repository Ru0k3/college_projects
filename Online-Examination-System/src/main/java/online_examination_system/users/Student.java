package online_examination_system.users;

import java.util.*;

import online_examination_system.exams.Exam;
import online_examination_system.exams.Result;

// Student extends Users - a student is a type of user
public class Student extends Users {

    private String studentId;
    private List<String> enrolledExams;
    private List<Result> results;

    public Student(String userId, String name, String email, int password, String studentId) {
        this(userId, name, email, String.valueOf(password), studentId);
    }

    public Student(String userId, String name, String email, String password, String studentId) {
        super(userId, name, email, password);
        this.studentId = studentId;
        this.enrolledExams = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public void enrollExam(String examId) {
        if (!isEnrolled(examId)) {
            enrolledExams.add(examId);
        }
        System.out.println("  [ENROLL] " + getName() + " enrolled in exam ID: " + examId);
    }

    public void enrollInExam(String examId) {
        enrollExam(examId);
    }

    public boolean isEnrolled(String examId) {
        return examId != null && enrolledExams.contains(examId);
    }

    // check enrollment before taking exam
    public void takeExam(Exam exam) {
        if (!isEnrolled(exam.getExamId())) {
            System.out.println("  [ERROR]  " + getName() + " is not enrolled in: " + exam.getTitle());
            return;
        }
        System.out.println("  [EXAM]   " + getName() + " is taking: " + exam.getTitle());
        exam.startExam();
    }

    public void viewResult() {
        if (results.isEmpty()) {
            System.out.println("  [RESULT] No results available for " + getName());
            return;
        }
        System.out.println("\n  ===== Results for " + getName() + " =====");
        for (Result r : results) r.displayResult();
    }

    public void addResult(Result result)     { results.add(result); }
    public String getStudentId()             { return studentId; }
    public List<String> getEnrolledExams()   { return enrolledExams; }
    public List<Result> getResults()         { return results; }
    public void setEnrolledExams(List<String> enrolledExams) {
        this.enrolledExams = enrolledExams == null ? new ArrayList<>() : enrolledExams;
    }
    public void setResults(List<Result> results) {
        this.results = results == null ? new ArrayList<>() : results;
    }
}
