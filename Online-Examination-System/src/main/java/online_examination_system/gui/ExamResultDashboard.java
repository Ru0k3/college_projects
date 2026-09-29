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
        GuiStyle.prepareFrame(this, "Exam Result", 500, 430, JFrame.EXIT_ON_CLOSE);

        double percent = result.getTotalMarks() == 0 ? 0 : (double) result.getScore() / result.getTotalMarks() * 100;
        String grade = result.getGrade();
        String remark = calculateRemark(percent);

        JPanel contentPanel = GuiStyle.surfacePanel();
        GridBagConstraints gbc = GuiStyle.constraints();
        int row = 0;

        String[] labels = {"Student", "Exam", "Score", "Percent", "Grade", "Remark"};
        String[] values = {
            result.getStudentId(),
            result.getResultId(),
            result.getScore() + " / " + result.getTotalMarks(),
            String.format("%.1f%%", percent),
            grade,
            remark
        };

        GuiStyle.addTitle(contentPanel, "Exam Result", gbc, row++);

        for (int i = 0; i < labels.length; i++) {
            addResultRow(contentPanel, row++, values[i], labels[i] + " :", labels[i].equals("Grade"), gbc);
        }

        backButton = new JButton("Back to Dashboard");
        logoutButton = new JButton("Logout");

        GuiStyle.addFullWidth(contentPanel, GuiStyle.buttonStack(backButton, logoutButton), gbc, row++);

        backButton.addActionListener(this);
        logoutButton.addActionListener(this);

        add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addResultRow(JPanel panel, int row, String valueText, String labelText,
                              boolean highlight, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(GuiStyle.LABEL_FONT);
        label.setForeground(GuiStyle.MUTED);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panel.add(label, gbc);

        JLabel value = new JLabel(valueText);
        value.setFont(highlight ? GuiStyle.SECTION_FONT : GuiStyle.BODY_FONT);
        value.setForeground(GuiStyle.TEXT);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(value, gbc);
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
