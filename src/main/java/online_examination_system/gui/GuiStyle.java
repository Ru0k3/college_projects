package online_examination_system.gui;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

final class GuiStyle {

    static final Color BACKGROUND = UIManager.getColor("Panel.background");
    static final Color SURFACE = UIManager.getColor("Panel.background");
    static final Color BORDER = Color.GRAY;
    static final Color TEXT = Color.BLACK;
    static final Color MUTED = Color.BLACK;
    static final Color WARNING = Color.DARK_GRAY;

    static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 16);
    static final Font SECTION_FONT = new Font("SansSerif", Font.BOLD, 13);
    static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 12);
    static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 12);

    private GuiStyle() {
    }

    static void prepareFrame(JFrame frame, String title, int width, int height, int closeOperation) {
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setLayout(new BorderLayout(16, 16));
        frame.setDefaultCloseOperation(closeOperation);
        frame.getContentPane().setBackground(BACKGROUND);
    }

    static JPanel surfacePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        return panel;
    }

    static GridBagConstraints constraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        return gbc;
    }

    static void addFullWidth(JPanel panel, Component component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    static void addTitle(JPanel panel, String text, GridBagConstraints gbc, int row) {
        JLabel titleLabel = new JLabel(text, SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT);
        addFullWidth(panel, titleLabel, gbc, row);
    }

    static void addSubtitle(JPanel panel, String text, GridBagConstraints gbc, int row) {
        JLabel subtitleLabel = new JLabel(text, SwingConstants.CENTER);
        subtitleLabel.setFont(BODY_FONT);
        subtitleLabel.setForeground(MUTED);
        addFullWidth(panel, subtitleLabel, gbc, row);
    }

    static void addSection(JPanel panel, String text, GridBagConstraints gbc, int row) {
        JLabel sectionLabel = new JLabel("--- " + text + " ---");
        sectionLabel.setFont(SECTION_FONT);
        sectionLabel.setForeground(TEXT);
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 2, 0));
        addFullWidth(panel, sectionLabel, gbc, row);
    }

    static void addFormRow(JPanel panel, int row, JComponent input, String labelText, GridBagConstraints gbc) {
        styleInput(input);

        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(MUTED);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(input, gbc);
    }

    static void addInfoRow(JPanel panel, int row, String valueText, String labelText, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(MUTED);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        JLabel value = new JLabel(valueText);
        value.setFont(BODY_FONT);
        value.setForeground(TEXT);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(value, gbc);
    }

    static JPanel buttonStack(JButton... buttons) {
        JPanel panel = new JPanel(new GridLayout(buttons.length, 1, 0, 8));
        panel.setBackground(SURFACE);
        for (JButton button : buttons) {
            styleButton(button);
            panel.add(button);
        }
        return panel;
    }

    static void styleButton(JButton button) {
        button.setFont(BODY_FONT);
        button.setFocusPainted(false);
    }

    static void styleInput(JComponent input) {
        input.setFont(BODY_FONT);
        input.setPreferredSize(new Dimension(280, 30));
        if (input instanceof JTextComponent) {
            ((JTextComponent) input).setForeground(TEXT);
        }
    }
}
