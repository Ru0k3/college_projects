package online_examination_system.main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import online_examination_system.auth.Authentication;
import online_examination_system.exams.Exam;
import online_examination_system.exams.Result;
import online_examination_system.gui.AdminDashboard;
import online_examination_system.gui.ExamResultDashboard;
import online_examination_system.gui.FacultyDashboard;
import online_examination_system.gui.LoginFrame;
import online_examination_system.gui.MCQScreen;
import online_examination_system.gui.StudentDashboard;
import online_examination_system.users.Admin;
import online_examination_system.users.Faculty;
import online_examination_system.users.Student;
import online_examination_system.users.Users;

public class AppNavigator {

    private static final Authentication AUTHENTICATION = new Authentication();

    public static void showLogin() {
        SwingUtilities.invokeLater(() -> new LoginFrame(AUTHENTICATION));
    }

    public static void routeToDashboard(Users user) {
        if (user instanceof Student) {
            new StudentDashboard((Student) user);
        } else if (user instanceof Faculty) {
            new FacultyDashboard((Faculty) user);
        } else if (user instanceof Admin) {
            new AdminDashboard((Admin) user);
        } else {
            JOptionPane.showMessageDialog(null, "Unknown user role.");
            showLogin();
        }
    }

    public static void showStudentDashboard(Student student) {
        new StudentDashboard(student);
    }

    public static void startExam(Student student, Exam exam) {
        if (student == null || exam == null) {
            JOptionPane.showMessageDialog(null, "Unable to start exam.");
            return;
        }
        if (!student.isEnrolled(exam.getExamId())) {
            JOptionPane.showMessageDialog(null, "You are not enrolled in this exam.");
            return;
        }
        exam.startExam();
        new MCQScreen(student, exam);
    }

    public static void showResult(Result result) {
        new ExamResultDashboard(result);
    }

    public static void logout() {
        AUTHENTICATION.logoutCurrentUser();
        showLogin();
    }
}
