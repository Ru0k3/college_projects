package online_examination_system.gui;

import online_examination_system.exams.Answer;
import online_examination_system.exams.Exam;
import online_examination_system.exams.EvaluationSystem;
import online_examination_system.exams.Result;
import online_examination_system.questions.Question;
import online_examination_system.users.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exam screen for exams that contain only descriptive questions.
 */
public class DescriptiveScreen extends JFrame implements ActionListener {

    JButton prevButton;
    JButton nextButton;
    JButton submitButton;
    JButton finishButton;

    JLabel examTitleLabel;
    JLabel questionCounterLabel;
    JLabel marksLabel;
    JLabel statusLabel;
    JLabel questionTextLabel;

    JTextArea answerArea;
    JScrollPane answerScroll;

    Student student;
    Exam exam;
    List<Question> questions;
    String[] savedAnswers;
    boolean[] submitted;
    int currentIndex;

    public DescriptiveScreen(Student student, Exam exam) {
        this.student = student;
        this.exam = exam;
        this.questions = exam.getQuestions();
        this.savedAnswers = new String[questions.size()];
        this.submitted = new boolean[questions.size()];
        this.currentIndex = 0;

        setTitle("Descriptive Exam : " + exam.getTitle());
        setSize(560, 440);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 8));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(210, 208, 200));

        examTitleLabel = new JLabel("Exam : " + exam.getTitle());
        examTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(examTitleLabel);

        JPanel infoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        infoRow.setBackground(new Color(210, 208, 200));
        infoRow.setPreferredSize(new Dimension(520, 24));

        questionCounterLabel = new JLabel();
        marksLabel = new JLabel();
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(180, 60, 0));

        infoRow.add(questionCounterLabel);
        infoRow.add(marksLabel);
        infoRow.add(statusLabel);
        add(infoRow);

        questionTextLabel = new JLabel(" ");
        questionTextLabel.setPreferredSize(new Dimension(520, 56));
        questionTextLabel.setVerticalAlignment(SwingConstants.TOP);
        add(questionTextLabel);

        JPanel answerPanel = new JPanel(new BorderLayout(0, 6));
        answerPanel.setBackground(Color.WHITE);
        answerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        answerPanel.setPreferredSize(new Dimension(520, 150));

        JLabel answerLabel = new JLabel("Your Answer :");
        answerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        answerLabel.setForeground(new Color(100, 100, 100));
        answerPanel.add(answerLabel, BorderLayout.NORTH);

        answerArea = new JTextArea();
        answerArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        answerScroll = new JScrollPane(answerArea);
        answerScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        answerPanel.add(answerScroll, BorderLayout.CENTER);

        add(answerPanel);

        prevButton = new JButton("< Previous");
        submitButton = new JButton("Submit Answer");
        nextButton = new JButton("Next >");
        finishButton = new JButton("Finish Exam");

        add(prevButton);
        add(submitButton);
        add(nextButton);
        add(finishButton);

        prevButton.addActionListener(this);
        submitButton.addActionListener(this);
        nextButton.addActionListener(this);
        finishButton.addActionListener(this);

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "This exam has no questions.");
            dispose();
            return;
        }

        loadQuestion(0);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void loadQuestion(int index) {
        Question q = questions.get(index);

        questionCounterLabel.setText("Question " + (index + 1) + " of " + questions.size());
        marksLabel.setText("Marks : " + q.getMarks());
        questionTextLabel.setText("<html><b>Q" + (index + 1) + ".</b> "
            + q.getQuestion() + "</html>");

        answerArea.setText(savedAnswers[index] != null ? savedAnswers[index] : "");

        boolean locked = submitted[index];
        answerArea.setEnabled(!locked);
        answerArea.setBackground(locked ? new Color(240, 240, 240) : Color.WHITE);

        submitButton.setEnabled(!locked);
        statusLabel.setText(locked ? "Submitted - you cannot change this answer." : " ");

        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < questions.size() - 1);
    }

    void saveCurrentAnswer() {
        if (submitted[currentIndex]) {
            return;
        }

        String text = answerArea.getText().trim();
        savedAnswers[currentIndex] = text.isEmpty() ? null : text;
    }

    void handleFinalSubmit() {
        saveCurrentAnswer();

        int unanswered = 0;
        for (String answer : savedAnswers) {
            if (answer == null || answer.isEmpty()) {
                unanswered++;
            }
        }

        String warning = unanswered > 0 ? unanswered + " question(s) unanswered. " : "";
        int confirm = JOptionPane.showConfirmDialog(null,
            warning + "Are you sure you want to finish the exam?",
            "Finish Exam", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            answers.add(new Answer(
                "ANS" + (i + 1),
                questions.get(i).getQuestionID(),
                savedAnswers[i] != null ? savedAnswers[i] : ""
            ));
        }

        EvaluationSystem evaluator = new EvaluationSystem("EVAL001");
        Result result = evaluator.evaluateExam("RES001", student, exam, answers);

        dispose();

        if (result != null) {
            new ExamResultDashboard(result);
        } else {
            JOptionPane.showMessageDialog(null,
                "Evaluation failed. Please contact your faculty.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            if (answerArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter an answer before submitting!");
                return;
            }

            saveCurrentAnswer();
            submitted[currentIndex] = true;

            boolean allDone = true;
            for (boolean answerSubmitted : submitted) {
                if (!answerSubmitted) {
                    allDone = false;
                    break;
                }
            }

            if (allDone) {
                handleFinalSubmit();
            } else {
                loadQuestion(currentIndex);
                if (currentIndex < questions.size() - 1) {
                    currentIndex++;
                    loadQuestion(currentIndex);
                }
            }
        } else if (e.getSource() == nextButton) {
            saveCurrentAnswer();
            currentIndex++;
            loadQuestion(currentIndex);
        } else if (e.getSource() == prevButton) {
            saveCurrentAnswer();
            currentIndex--;
            loadQuestion(currentIndex);
        } else if (e.getSource() == finishButton) {
            handleFinalSubmit();
        }
    }
}
