package online_examination_system.gui;

import online_examination_system.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ExamResultDashboard extends JFrame implements ActionListener {

    JButton backButton;
    JButton logoutButton;

    ExamResultDashboard(Result result) {
        setTitle("Exam Result");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        getContentPane().setBackground(new Color(210, 208, 200));

        double percent = (double) result.getMarksScored() / result.getTotalMarks() * 100;
        String grade  = result.calculateGrade();
        String remark = calculateRemark(percent);

        // ── Title label ──────────────────────────────────────────────────────
        JLabel titleLabel = new JLabel("====== Exam Result ======");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(titleLabel);

        // ── White result panel ───────────────────────────────────────────────
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 0, 6));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        panel.setPreferredSize(new Dimension(340, 210));

        Font labelFont = new Font("SansSerif", Font.PLAIN, 13);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 13);
        Color labelColor = new Color(100, 100, 100);

        // Row helper
        String[] labels = {"Student", "Exam", "Score", "Percent", "Grade", "Remark"};
        String[] values = {
            result.getStudentName(),
            result.getExamTitle(),
            result.getMarksScored() + " / " + result.getTotalMarks(),
            String.format("%.1f%%", percent),
            grade,
            remark
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(labelFont);
            lbl.setForeground(labelColor);

            JLabel val = new JLabel(values[i]);
            val.setFont(valueFont);

            if (i == 4) {
                val.setForeground(new Color(20, 110, 20));
                val.setFont(new Font("SansSerif", Font.BOLD, 13));
            } else {
                val.setForeground(Color.BLACK);
            }

            panel.add(lbl);
            panel.add(val);
        }

        add(panel);

        // Buttons
        backButton   = new JButton("Back to Dashboard");
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
            new StudentDashboard("student@example.com");
        } else if (e.getSource() == logoutButton) {
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            dispose();
            new LoginScreen();
        }
    }
}