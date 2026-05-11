package online_examination_system.gui;

import online_examination_system.Data.DataStore;
import online_examination_system.users.Faculty;
import online_examination_system.exams.Exam;
import online_examination_system.main.AppNavigator;
import online_examination_system.questions.DescriptiveQuestion;
import online_examination_system.questions.MCQQuestion;
import online_examination_system.questions.Question;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyDashboard extends JFrame implements ActionListener {

    JTextField questionIdField;
    JTextField questionTypeField;
    JTextField questionTextField;
    JTextField marksField;
    JTextField optionsField;
    JTextField answerField;

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

    public FacultyDashboard(Faculty faculty) {
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

        add(new JLabel("Type (mcq / descriptive) :"));
        questionTypeField = new JTextField(30);
        add(questionTypeField);

        add(new JLabel("Question Text :"));
        questionTextField = new JTextField(30);
        add(questionTextField);

        add(new JLabel("Marks :"));
        marksField = new JTextField(30);
        add(marksField);

        add(new JLabel("MCQ Options (A|B|C|D) :"));
        optionsField = new JTextField(30);
        add(optionsField);

        add(new JLabel("Correct option or descriptive keywords :"));
        answerField = new JTextField(30);
        add(answerField);

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
            String qType = questionTypeField.getText().trim().toLowerCase();
            String qText = questionTextField.getText().trim();
            String qMark = marksField.getText().trim();
            String answer = answerField.getText().trim();

            if (qId.isEmpty() || qType.isEmpty() || qText.isEmpty() || qMark.isEmpty() || answer.isEmpty()) {
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

            Question q;
            if (qType.equals("mcq")) {
                String[] options = optionsField.getText().split("\\|");
                if (options.length != 4) {
                    JOptionPane.showMessageDialog(null, "Enter exactly four MCQ options separated by |");
                    return;
                }
                q = new MCQQuestion(qId, qText, marks, options[0].trim(), options[1].trim(),
                    options[2].trim(), options[3].trim(), answer);
            } else if (qType.equals("descriptive")) {
                q = new DescriptiveQuestion(qId, qText, marks, answer);
            } else {
                JOptionPane.showMessageDialog(null, "Question type must be mcq or descriptive.");
                return;
            }

            faculty.createQuestion(q);
            JOptionPane.showMessageDialog(null,
                "[FACULTY] Question Added!\nID    : " + qId +
                "\nText  : " + qText +
                "\nMarks : " + marks);

            questionIdField.setText("");
            questionTypeField.setText("");
            questionTextField.setText("");
            marksField.setText("");
            optionsField.setText("");
            answerField.setText("");

        } else if (e.getSource() == viewQuestionsButton) {
            JOptionPane.showMessageDialog(null, "Question bank size: " + faculty.getQuestionBank().size());

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
            for (Question question : faculty.getQuestionBank()) {
                exam.addQuestion(question);
            }
            createdExams.add(exam);
            DataStore.saveExam(exam, faculty.getFacultyID());

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
            AppNavigator.logout();
        }
    }
}
