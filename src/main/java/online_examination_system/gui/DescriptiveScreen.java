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

        GuiStyle.prepareFrame(this, "Descriptive Exam : " + exam.getTitle(), 660, 620, JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = GuiStyle.surfacePanel();
        GridBagConstraints gbc = GuiStyle.constraints();
        int row = 0;

        examTitleLabel = new JLabel("Exam : " + exam.getTitle(), SwingConstants.CENTER);
        examTitleLabel.setFont(GuiStyle.TITLE_FONT);
        examTitleLabel.setForeground(GuiStyle.TEXT);
        GuiStyle.addFullWidth(contentPanel, examTitleLabel, gbc, row++);

        JPanel infoRow = new JPanel(new GridLayout(1, 3, 12, 0));
        infoRow.setBackground(GuiStyle.SURFACE);

        questionCounterLabel = new JLabel();
        marksLabel = new JLabel();
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(GuiStyle.WARNING);

        questionCounterLabel.setFont(GuiStyle.BODY_FONT);
        marksLabel.setFont(GuiStyle.BODY_FONT);
        statusLabel.setFont(GuiStyle.BODY_FONT);
        marksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        infoRow.add(questionCounterLabel);
        infoRow.add(marksLabel);
        infoRow.add(statusLabel);
        GuiStyle.addFullWidth(contentPanel, infoRow, gbc, row++);

        questionTextLabel = new JLabel(" ");
        questionTextLabel.setFont(GuiStyle.BODY_FONT);
        questionTextLabel.setForeground(GuiStyle.TEXT);
        questionTextLabel.setOpaque(false);
        questionTextLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GuiStyle.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        questionTextLabel.setPreferredSize(new Dimension(560, 76));
        questionTextLabel.setVerticalAlignment(SwingConstants.TOP);
        GuiStyle.addFullWidth(contentPanel, questionTextLabel, gbc, row++);

        JPanel answerPanel = new JPanel(new BorderLayout(0, 6));
        answerPanel.setBackground(GuiStyle.SURFACE);
        answerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GuiStyle.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        answerPanel.setPreferredSize(new Dimension(560, 170));

        JLabel answerLabel = new JLabel("Your Answer :");
        answerLabel.setFont(GuiStyle.LABEL_FONT);
        answerLabel.setForeground(GuiStyle.MUTED);
        answerPanel.add(answerLabel, BorderLayout.NORTH);

        answerArea = new JTextArea();
        answerArea.setFont(GuiStyle.BODY_FONT);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        answerScroll = new JScrollPane(answerArea);
        answerScroll.setBorder(BorderFactory.createLineBorder(GuiStyle.BORDER, 1));
        answerPanel.add(answerScroll, BorderLayout.CENTER);

        GuiStyle.addFullWidth(contentPanel, answerPanel, gbc, row++);

        prevButton = new JButton("Previous");
        submitButton = new JButton("Submit Answer");
        nextButton = new JButton("Next");
        finishButton = new JButton("Finish Exam");

        GuiStyle.addFullWidth(contentPanel,
            GuiStyle.buttonStack(prevButton, submitButton, nextButton, finishButton), gbc, row++);

        prevButton.addActionListener(this);
        submitButton.addActionListener(this);
        nextButton.addActionListener(this);
        finishButton.addActionListener(this);

        add(contentPanel, BorderLayout.CENTER);

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
        Result result = evaluator.evaluateExam(null, student, exam, answers);

        dispose();

        if (result != null) {
            new ExamResultDashboard(result, student);
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
