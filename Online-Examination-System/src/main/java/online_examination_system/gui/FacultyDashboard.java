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

        GuiStyle.prepareFrame(this, "Faculty Dashboard", 660, 760, JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = GuiStyle.surfacePanel();
        GridBagConstraints gbc = GuiStyle.constraints();
        int row = 0;

        GuiStyle.addTitle(contentPanel, "Faculty Dashboard", gbc, row++);
        GuiStyle.addInfoRow(contentPanel, row++, faculty.getName(), "Name :", gbc);
        GuiStyle.addInfoRow(contentPanel, row++, faculty.getFacultyID(), "Faculty ID :", gbc);
        GuiStyle.addInfoRow(contentPanel, row++, faculty.getSubject(), "Subject :", gbc);
        GuiStyle.addInfoRow(contentPanel, row++, faculty.getEmail(), "Email :", gbc);

        GuiStyle.addSection(contentPanel, "Add a Question to Question Bank", gbc, row++);

        questionIdField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, questionIdField, "Question ID :", gbc);

        questionTypeField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, questionTypeField, "Type (mcq / descriptive) :", gbc);

        questionTextField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, questionTextField, "Question Text :", gbc);

        marksField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, marksField, "Marks :", gbc);

        optionsField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, optionsField, "MCQ Options (A|B|C|D) :", gbc);

        answerField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, answerField, "Correct option or descriptive keywords :", gbc);

        addQuestionButton = new JButton("Add Question");
        viewQuestionsButton = new JButton("View My Questions");
        GuiStyle.addFullWidth(contentPanel, GuiStyle.buttonStack(addQuestionButton, viewQuestionsButton), gbc, row++);

        GuiStyle.addSection(contentPanel, "Create an Exam", gbc, row++);

        examIdField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, examIdField, "Exam ID :", gbc);

        examTitleField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, examTitleField, "Exam Title :", gbc);

        examDurationField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, examDurationField, "Duration (mins) :", gbc);

        examTimeField = new JTextField(30);
        GuiStyle.addFormRow(contentPanel, row++, examTimeField, "Scheduled Time (e.g. 10:00 AM) :", gbc);

        createExamButton = new JButton("Create Exam");
        GuiStyle.addFullWidth(contentPanel, GuiStyle.buttonStack(createExamButton), gbc, row++);

        logoutButton = new JButton("Logout");
        GuiStyle.addFullWidth(contentPanel, GuiStyle.buttonStack(logoutButton), gbc, row++);

        addQuestionButton.addActionListener(this);
        viewQuestionsButton.addActionListener(this);
        createExamButton.addActionListener(this);
        logoutButton.addActionListener(this);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        scrollPane.getViewport().setBackground(GuiStyle.BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

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
