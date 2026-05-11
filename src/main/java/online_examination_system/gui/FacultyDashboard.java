package online_examination_system.gui;

import online_examination_system.users.Faculty;
import online_examination_system.exams.Exam;
import online_examination_system.questions.Question;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDashboard extends JFrame implements ActionListener {

    JTextField questionIdField;
    JTextField questionTextField;
    JTextField marksField;

    JTextField examIdField;
    JTextField examTitleField;
    JTextField examDurationField;
    JTextField examTimeField;

    JButton addQuestionButton;
    JButton createExamButton;
    JButton viewQuestionsButton;
    JButton logoutButton;

    Faculty faculty;
    List<Exam> createdExams;

    FacultyDashboard(Faculty faculty) {
        this.faculty = faculty;
        this.createdExams = new ArrayList<>();

        setTitle("Faculty Dashboard");
        setSize(450, 560);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("=== Faculty Dashboard ==="));
        add(new JLabel("Name       : " + faculty.getName()));
        add(new JLabel("Faculty ID : " + faculty.getFacultyID()));
        add(new JLabel("Subject    : " + faculty.getSubject()));
        add(new JLabel("Email      : " + faculty.getEmail()));

        add(new JLabel("--- Add a Question to Question Bank ---"));

        add(new JLabel("Question ID :"));
        questionIdField = new JTextField(30);
        add(questionIdField);

        add(new JLabel("Question Text :"));
        questionTextField = new JTextField(30);
        add(questionTextField);

        add(new JLabel("Marks :"));
        marksField = new JTextField(30);
        add(marksField);

        addQuestionButton = new JButton("Add Question");
        add(addQuestionButton);

        viewQuestionsButton = new JButton("View My Questions");
        add(viewQuestionsButton);

        add(new JLabel("--- Create an Exam ---"));

        add(new JLabel("Exam ID :"));
        examIdField = new JTextField(30);
        add(examIdField);

        add(new JLabel("Exam Title :"));
        examTitleField = new JTextField(30);
        add(examTitleField);

        add(new JLabel("Duration (mins) :"));
        examDurationField = new JTextField(30);
        add(examDurationField);

        add(new JLabel("Scheduled Time (e.g. 10:00 AM) :"));
        examTimeField = new JTextField(30);
        add(examTimeField);

        createExamButton = new JButton("Create Exam");
        add(createExamButton);

        logoutButton = new JButton("Logout");
        add(logoutButton);

        addQuestionButton.addActionListener(this);
        viewQuestionsButton.addActionListener(this);
        createExamButton.addActionListener(this);
        logoutButton.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addQuestionButton) {
            String qId   = questionIdField.getText().trim();
            String qText = questionTextField.getText().trim();
            String qMark = marksField.getText().trim();

            if (qId.isEmpty() || qText.isEmpty() || qMark.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all question fields!");
                return;
            }

            int marks;
            try {
                marks = Integer.parseInt(qMark);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Marks must be a whole number!");
                return;
            }

            Question q = new Question(qId, qText, marks) {
                public int checkAnswer(String response) { return 0; }
            };

            faculty.createQuestion(q);
            JOptionPane.showMessageDialog(null,
                "[FACULTY] Question Added!\nID    : " + qId +
                "\nText  : " + qText +
                "\nMarks : " + marks);

            questionIdField.setText("");
            questionTextField.setText("");
            marksField.setText("");

        } else if (e.getSource() == viewQuestionsButton) {
            JOptionPane.showMessageDialog(null,
                "Questions are saved to the question bank.\nCheck the console for the full list printed by createQuestion().");

        } else if (e.getSource() == createExamButton) {
            String eId       = examIdField.getText().trim();
            String eTitle    = examTitleField.getText().trim();
            String eDuration = examDurationField.getText().trim();
            String eTime     = examTimeField.getText().trim();

            if (eId.isEmpty() || eTitle.isEmpty() || eDuration.isEmpty() || eTime.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all exam fields!");
                return;
            }

            int duration;
            try {
                duration = Integer.parseInt(eDuration);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Duration must be a whole number!");
                return;
            }

            Exam exam = new Exam(eId, eTitle, duration, eTime);
            createdExams.add(exam);

            JOptionPane.showMessageDialog(null,
                "[FACULTY] Exam Created!\nID       : " + eId +
                "\nTitle    : " + eTitle +
                "\nDuration : " + duration + " mins" +
                "\nTime     : " + eTime);

            examIdField.setText("");
            examTitleField.setText("");
            examDurationField.setText("");
            examTimeField.setText("");

        } else if (e.getSource() == logoutButton) {
            faculty.logout();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            dispose();
            // TODO: new LoginFrame();
        }
    }
}
