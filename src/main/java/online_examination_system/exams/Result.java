package online_examination_system.exams;

// this is the result class - stores marks and grade for a student
public class Result {

    private String resultId;    // unique id for this result
    private String studentId;   // which student got this result
    private int score;          // marks obtained
    private int totalMarks;     // maximum marks
    private String grade;       // letter grade like A+, B, etc

    // constructor - takes all the info and calculates grade
    public Result(String resultId, String studentId, int score, int totalMarks) {
        this.resultId = resultId;
        this.studentId = studentId;
        this.score = score;
        this.totalMarks = totalMarks;
        this.grade = calculateGrade(); // call method to set grade
    }

    // figures out grade based on percentage
    public String calculateGrade() {
        double percent = (double) score / totalMarks * 100;
        if (percent >= 90) return "O  (Outstanding)";
        if (percent >= 75) return "A+ (Excellent)";
        if (percent >= 60) return "A  (Good)";
        if (percent >= 50) return "B  (Average)";
        if (percent >= 40) return "C  (Pass)";
        return "F  (Fail)";
    }

    // prints result in a nice box format
    public void displayResult() {
        double percent = (double) score / totalMarks * 100;
        System.out.println("  +-----------------------------------------+");
        System.out.printf("  | Result ID   : %-26s|%n", resultId);
        System.out.printf("  | Student ID  : %-26s|%n", studentId);
        System.out.printf("  | Score       : %d / %-22d|%n", score, totalMarks);
        System.out.printf("  | Percentage  : %-26s|%n", String.format("%.1f%%", percent));
        System.out.printf("  | Grade       : %-26s|%n", grade);
        System.out.println("  +-----------------------------------------+");
    }

    // getters - so other classes can read these values
    public String getResultId() { return resultId; }
    public String getStudentId(){ return studentId; }
    public int getScore()       { return score; }
    public int getTotalMarks()  { return totalMarks; }
    public String getGrade()    { return grade; }
}
