package online_examination_system.gui;

import online_examination_system.users.Student;
import online_examination_system.exams.Result;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentDashboard extends JFrame implements ActionListener {

    JButton enrollButton;
    JButton viewResultsButton;
    JButton logoutButton;

    Student student;

    StudentDashboard(Student student) {
        this.student = student;

        setTitle("Student Dashboard");
        setSize(420, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("=== Student Dashboard ==="));
        add(new JLabel("Name       : " + student.getName()));
        add(new JLabel("Student ID : " + student.getStudentId()));
        add(new JLabel("Email      : " + student.getEmail()));

        add(new JLabel("--- Your Enrolled Exams ---"));
        List<String> exams = student.getEnrolledExams();
        if (exams.isEmpty()) {
            add(new JLabel("No exams enrolled yet."));
        } else {
            for (String examId : exams) {
                add(new JLabel(examId));
            }
        }

        add(new JLabel("--- Your Results ---"));
        List<Result> results = student.getResults();
        if (results.isEmpty()) {
            add(new JLabel("No results yet."));
        } else {
            for (Result result : results) {
                add(new JLabel("Result: " + result.getGrade() + " | Score: " + result.getScore()));
            }
        }

        enrollButton      = new JButton("Enroll in Exam");
        viewResultsButton = new JButton("View Results");
        logoutButton      = new JButton("Logout");

        add(enrollButton);
        add(viewResultsButton);
        add(logoutButton);

        enrollButton.addActionListener(this);
        viewResultsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enrollButton) {
            String examId = JOptionPane.showInputDialog(null, "Enter Exam ID to enroll:");
            if (examId == null || examId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Exam ID!");
            } else {
                student.enrollInExam(examId);
                JOptionPane.showMessageDialog(null, "Enrolled in exam: " + examId);
            }

        } else if (e.getSource() == viewResultsButton) {
            if (student.getResults().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No results available yet!");
            } else {
                dispose();
                // TODO: new ExamResultDashboard(student);
            }

        } else if (e.getSource() == logoutButton) {
            student.logout();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            dispose();
            // TODO: new LoginFrame();
        }
    }
}