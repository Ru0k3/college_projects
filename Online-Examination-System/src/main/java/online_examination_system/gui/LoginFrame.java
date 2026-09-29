package online_examination_system.gui;

import online_examination_system.auth.Authentication;
import online_examination_system.main.AppNavigator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements ActionListener {

    JTextField emailField;
    JPasswordField passwordField;
    JTextField roleField;
    Authentication authentication;

    JButton loginButton;
    JButton clearButton;

    public LoginFrame() {
        this(new Authentication());
    }

    public LoginFrame(Authentication authentication) {
        this.authentication = authentication;
        GuiStyle.prepareFrame(this, "Online Examination System - Login", 520, 360, JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = GuiStyle.surfacePanel();
        GridBagConstraints gbc = GuiStyle.constraints();
        int row = 0;

        GuiStyle.addTitle(contentPanel, "Online Examination System", gbc, row++);
        GuiStyle.addSubtitle(contentPanel, "Please log in to continue", gbc, row++);

        emailField = new JTextField(28);
        GuiStyle.addFormRow(contentPanel, row++, emailField, "Email :", gbc);

        passwordField = new JPasswordField(28);
        GuiStyle.addFormRow(contentPanel, row++, passwordField, "Password :", gbc);

        roleField = new JTextField(28);
        GuiStyle.addFormRow(contentPanel, row++, roleField, "Role (student / faculty / admin) :", gbc);

        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");

        GuiStyle.addFullWidth(contentPanel, GuiStyle.buttonStack(loginButton, clearButton), gbc, row++);
        add(contentPanel, BorderLayout.CENTER);

        loginButton.addActionListener(this);
        clearButton.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            emailField.setText("");
            passwordField.setText("");
            roleField.setText("");
            return;
        }

        if (e.getSource() == loginButton) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = roleField.getText().trim().toLowerCase();

            if (email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!");
                return;
            }

            if (!role.equals("student") && !role.equals("faculty") && !role.equals("admin")) {
                JOptionPane.showMessageDialog(null, "Invalid role! Enter: student, faculty, or admin");
                return;
            }

            if (authentication.validateUser(email, password, role)) {
                dispose();
                AppNavigator.routeToDashboard(authentication.getCurrentUser());
            } else {
                JOptionPane.showMessageDialog(null, "Invalid email, password, or role!");
            }
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
