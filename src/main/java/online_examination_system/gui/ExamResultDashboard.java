package online_examination_system.gui;

import online_examination_system.exams.Result;
import online_examination_system.main.AppNavigator;
import online_examination_system.users.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ExamResultDashboard extends JFrame implements ActionListener {

    JButton backButton;
    JButton logoutButton;
    Student student;

    public ExamResultDashboard(Result result) {
        this(result, null);
    }

    public ExamResultDashboard(Result result, Student student) {
        this.student = student;
        setTitle("Exam Result");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        getContentPane().setBackground(new Color(210, 208, 200));

        double percent = result.getTotalMarks() == 0 ? 0 : (double) result.getScore() / result.getTotalMarks() * 100;
        String grade = result.getGrade();
        String remark = calculateRemark(percent);

        JLabel titleLabel = new JLabel("====== Exam Result ======");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(titleLabel);

        JPanel panel = new JPanel(new GridLayout(6, 2, 0, 6));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        panel.setPreferredSize(new Dimension(340, 210));

        Font labelFont = new Font("SansSerif", Font.PLAIN, 13);
        Color labelColor = new Color(100, 100, 100);

        String[] labels = {"Student", "Exam", "Score", "Percent", "Grade", "Remark"};
        String[] values = {
            result.getStudentId(),
            result.getResultId(),
            result.getScore() + " / " + result.getTotalMarks(),
            String.format("%.1f%%", percent),
            grade,
            remark
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(labelFont);
            label.setForeground(labelColor);

            JLabel value = new JLabel(values[i]);
            value.setFont(labelFont);
            value.setForeground(Color.BLACK);

            if (labels[i].equals("Grade")) {
                value.setForeground(new Color(20, 110, 20));
                value.setFont(new Font("SansSerif", Font.BOLD, 13));
            }

            panel.add(label);
            panel.add(value);
        }

        add(panel);

        backButton = new JButton("Back to Dashboard");
        logoutButton = new JButton("Logout");

        backButton.setFont(labelFont);
        logoutButton.setFont(labelFont);

        add(backButton);
        add(logoutButton);

        backButton.addActionListener(this);
        logoutButton.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    String calculateRemark(double percent) {
        if (percent >= 90) return "Exceptional performance!";
        if (percent >= 75) return "Great work! Keep it up.";
        if (percent >= 60) return "Good job! Room to improve.";
        if (percent >= 50) return "Average. Study harder!";
        if (percent >= 40) return "Passed. Needs more effort.";
        return "Failed. Please retake the exam.";
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            dispose();
            if (student != null) {
                AppNavigator.showStudentDashboard(student);
            }
        } else if (e.getSource() == logoutButton) {
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            dispose();
            AppNavigator.logout();
        }
    }
}
