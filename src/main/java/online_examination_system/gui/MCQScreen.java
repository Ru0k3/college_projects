package online_examination_system.gui;

import online_examination_system.exams.Answer;
import online_examination_system.exams.Exam;
import online_examination_system.exams.EvaluationSystem;
import online_examination_system.exams.Result;
import online_examination_system.questions.DescriptiveQuestion;
import online_examination_system.questions.MCQQuestion;
import online_examination_system.questions.Question;
import online_examination_system.users.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exam screen for exams that contain MCQ and descriptive questions.
 */
public class MCQScreen extends JFrame implements ActionListener {

    JButton prevButton;
    JButton nextButton;
    JButton submitButton;
    JButton finishButton;

    JLabel examTitleLabel;
    JLabel questionCounterLabel;
    JLabel marksLabel;
    JLabel statusLabel;
    JLabel questionTextLabel;

    JRadioButton optionA;
    JRadioButton optionB;
    JRadioButton optionC;
    JRadioButton optionD;
    ButtonGroup mcqGroup;
    JPanel optionsPanel;
    JTextArea descriptiveArea;
    JScrollPane descriptiveScroll;

    Student student;
    Exam exam;
    List<Question> questions;
    String[] savedAnswers;
    boolean[] submitted;
    int currentIndex;

    public MCQScreen(Student student, Exam exam) {
        this.student = student;
        this.exam = exam;
        this.questions = exam.getQuestions();
        this.savedAnswers = new String[questions.size()];
        this.submitted = new boolean[questions.size()];
        this.currentIndex = 0;

        GuiStyle.prepareFrame(this, "Exam : " + exam.getTitle(), 660, 700, JFrame.DISPOSE_ON_CLOSE);

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

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(GuiStyle.SURFACE);
        optionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GuiStyle.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));
        optionsPanel.setPreferredSize(new Dimension(560, 140));

        optionA = new JRadioButton();
        optionB = new JRadioButton();
        optionC = new JRadioButton();
        optionD = new JRadioButton();

        optionA.setBackground(GuiStyle.SURFACE);
        optionB.setBackground(GuiStyle.SURFACE);
        optionC.setBackground(GuiStyle.SURFACE);
        optionD.setBackground(GuiStyle.SURFACE);

        optionA.setFont(GuiStyle.BODY_FONT);
        optionB.setFont(GuiStyle.BODY_FONT);
        optionC.setFont(GuiStyle.BODY_FONT);
        optionD.setFont(GuiStyle.BODY_FONT);

        optionA.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionB.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionC.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionD.setAlignmentX(Component.LEFT_ALIGNMENT);

        optionsPanel.add(optionA);
        optionsPanel.add(optionB);
        optionsPanel.add(optionC);
        optionsPanel.add(optionD);

        mcqGroup = new ButtonGroup();
        mcqGroup.add(optionA);
        mcqGroup.add(optionB);
        mcqGroup.add(optionC);
        mcqGroup.add(optionD);

        GuiStyle.addFullWidth(contentPanel, optionsPanel, gbc, row++);

        descriptiveArea = new JTextArea();
        descriptiveArea.setFont(GuiStyle.BODY_FONT);
        descriptiveArea.setLineWrap(true);
        descriptiveArea.setWrapStyleWord(true);
        descriptiveArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        descriptiveScroll = new JScrollPane(descriptiveArea);
        descriptiveScroll.setBorder(BorderFactory.createLineBorder(GuiStyle.BORDER, 1));
        descriptiveScroll.setPreferredSize(new Dimension(560, 130));
        GuiStyle.addFullWidth(contentPanel, descriptiveScroll, gbc, row++);

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

        if (q instanceof MCQQuestion) {
            MCQQuestion mcq = (MCQQuestion) q;
            optionA.setText("A)  " + mcq.getOptionA());
            optionB.setText("B)  " + mcq.getOptionB());
            optionC.setText("C)  " + mcq.getOptionC());
            optionD.setText("D)  " + mcq.getOptionD());
        } else {
            optionA.setText("A)  Option A");
            optionB.setText("B)  Option B");
            optionC.setText("C)  Option C");
            optionD.setText("D)  Option D");
        }

        mcqGroup.clearSelection();
        descriptiveArea.setText(savedAnswers[index] != null ? savedAnswers[index] : "");
        if (savedAnswers[index] != null) {
            switch (savedAnswers[index]) {
                case "A": optionA.setSelected(true); break;
                case "B": optionB.setSelected(true); break;
                case "C": optionC.setSelected(true); break;
                case "D": optionD.setSelected(true); break;
                default: break;
            }
        }

        boolean locked = submitted[index];
        optionA.setEnabled(!locked);
        optionB.setEnabled(!locked);
        optionC.setEnabled(!locked);
        optionD.setEnabled(!locked);
        boolean mcqMode = q instanceof MCQQuestion;
        optionsPanel.setVisible(mcqMode);
        optionA.setVisible(mcqMode);
        optionB.setVisible(mcqMode);
        optionC.setVisible(mcqMode);
        optionD.setVisible(mcqMode);
        descriptiveScroll.setVisible(q instanceof DescriptiveQuestion);
        descriptiveArea.setEnabled(!locked);
        descriptiveArea.setBackground(locked ? new Color(240, 240, 240) : Color.WHITE);

        submitButton.setEnabled(!locked);
        statusLabel.setText(locked ? "Submitted - you cannot change this answer." : " ");

        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < questions.size() - 1);

        revalidate();
        repaint();
    }

    void saveCurrentAnswer() {
        if (submitted[currentIndex]) {
            return;
        }
        if (questions.get(currentIndex) instanceof DescriptiveQuestion) {
            String text = descriptiveArea.getText().trim();
            savedAnswers[currentIndex] = text.isEmpty() ? null : text;
            return;
        }
        if (optionA.isSelected()) {
            savedAnswers[currentIndex] = "A";
        } else if (optionB.isSelected()) {
            savedAnswers[currentIndex] = "B";
        } else if (optionC.isSelected()) {
            savedAnswers[currentIndex] = "C";
        } else if (optionD.isSelected()) {
            savedAnswers[currentIndex] = "D";
        }
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
            Question q = questions.get(currentIndex);
            if (q instanceof MCQQuestion && !optionA.isSelected() && !optionB.isSelected()
                    && !optionC.isSelected() && !optionD.isSelected()) {
                JOptionPane.showMessageDialog(null, "Please select an option before submitting!");
                return;
            }
            if (q instanceof DescriptiveQuestion && descriptiveArea.getText().trim().isEmpty()) {
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
