package online_examination_system.gui;

import online_examination_system.Data.DataStore;
import online_examination_system.exams.Exam;
import online_examination_system.users.Student;
import online_examination_system.exams.Result;
import online_examination_system.main.AppNavigator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentDashboard extends JFrame implements ActionListener {

    JButton enrollButton;
    JButton startExamButton;
    JButton viewResultsButton;
    JButton logoutButton;

    Student student;

    public StudentDashboard(Student student) {
        this.student = student;

        GuiStyle.prepareFrame(this, "Student Dashboard", 560, 580, JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = GuiStyle.surfacePanel();
        GridBagConstraints gbc = GuiStyle.constraints();
        int row = 0;

        GuiStyle.addTitle(contentPanel, "Student Dashboard", gbc, row++);
        GuiStyle.addInfoRow(contentPanel, row++, student.getName(), "Name :", gbc);
        GuiStyle.addInfoRow(contentPanel, row++, student.getStudentId(), "Student ID :", gbc);
        GuiStyle.addInfoRow(contentPanel, row++, student.getEmail(), "Email :", gbc);

        GuiStyle.addSection(contentPanel, "Your Enrolled Exams", gbc, row++);
        JPanel examsPanel = createListPanel();
        List<String> exams = student.getEnrolledExams();
        if (exams.isEmpty()) {
            addListItem(examsPanel, "No exams enrolled yet.");
        } else {
            for (String examId : exams) {
                addListItem(examsPanel, examId);
            }
        }
        GuiStyle.addFullWidth(contentPanel, examsPanel, gbc, row++);

        GuiStyle.addSection(contentPanel, "Your Results", gbc, row++);
        JPanel resultsPanel = createListPanel();
        List<Result> results = student.getResults();
        if (results.isEmpty()) {
            addListItem(resultsPanel, "No results yet.");
        } else {
            for (Result result : results) {
                addListItem(resultsPanel, "Result: " + result.getGrade() + " | Score: " + result.getScore());
            }
        }
        GuiStyle.addFullWidth(contentPanel, resultsPanel, gbc, row++);

        enrollButton      = new JButton("Enroll in Exam");
        startExamButton   = new JButton("Start Exam");
        viewResultsButton = new JButton("View Results");
        logoutButton      = new JButton("Logout");

        GuiStyle.addFullWidth(contentPanel,
            GuiStyle.buttonStack(enrollButton, startExamButton, viewResultsButton, logoutButton), gbc, row++);

        enrollButton.addActionListener(this);
        startExamButton.addActionListener(this);
        viewResultsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        scrollPane.getViewport().setBackground(GuiStyle.BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(GuiStyle.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GuiStyle.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return panel;
    }

    private void addListItem(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(GuiStyle.BODY_FONT);
        label.setForeground(GuiStyle.TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enrollButton) {
            String examId = JOptionPane.showInputDialog(null, "Enter Exam ID to enroll:");
            if (examId == null || examId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Exam ID!");
            } else if (DataStore.findExam(examId.trim()) == null) {
                JOptionPane.showMessageDialog(null, "Exam ID not found.");
            } else {
                student.enrollExam(examId.trim());
                DataStore.enrollStudent(student.getStudentId(), examId.trim());
                JOptionPane.showMessageDialog(null, "Enrolled in exam: " + examId);
            }

        } else if (e.getSource() == startExamButton) {
            String examId = JOptionPane.showInputDialog(null, "Enter enrolled Exam ID to start:");
            if (examId == null || examId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Exam ID!");
                return;
            }
            Exam exam = DataStore.findExam(examId.trim());
            if (exam == null) {
                JOptionPane.showMessageDialog(null, "Exam ID not found.");
                return;
            }
            dispose();
            AppNavigator.startExam(student, exam);

        } else if (e.getSource() == viewResultsButton) {
            if (student.getResults().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No results available yet!");
            } else {
                dispose();
                new ExamResultDashboard(student.getResults().get(student.getResults().size() - 1), student);
            }

        } else if (e.getSource() == logoutButton) {
            student.logout();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            dispose();
            AppNavigator.logout();
        }
    }
}
