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
        setTitle("Online Examination System - Login");
        setSize(400, 280);
        setLayout(new FlowLayout(FlowLayout.LEFT, 14, 8));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("=== Online Examination System ==="));
        add(new JLabel("             Please Log In             "));

        add(new JLabel("Email :"));
        emailField = new JTextField(28);
        add(emailField);

        add(new JLabel("Password :"));
        passwordField = new JPasswordField(28);
        add(passwordField);

        add(new JLabel("Role (student / faculty / admin) :"));
        roleField = new JTextField(28);
        add(roleField);

        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");
        add(loginButton);
        add(clearButton);

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
