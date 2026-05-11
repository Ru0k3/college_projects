package online_examination_system.gui;

import online_examination_system.users.Student;
import online_examination_system.users.Faculty;
import online_examination_system.users.Admin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements ActionListener {

    JTextField emailField;
    JTextField passwordField;
    JTextField roleField;

    JButton loginButton;
    JButton clearButton;

    public LoginFrame() {
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
        passwordField = new JTextField(28);
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
            String password = passwordField.getText().trim();
            String role = roleField.getText().trim().toLowerCase();

            if (email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!");
                return;
            }

            int parsedPassword;
            try {
                parsedPassword = Integer.parseInt(password);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Password must be a number!");
                return;
            }

            if (role.equals("student")) {
                Student student = new Student("S001", "Student User", email, parsedPassword, "STU001");
                if (student.login(email, parsedPassword)) {
                    dispose();
                    new StudentDashboard(student);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password!");
                }

            } else if (role.equals("faculty")) {
                Faculty faculty = new Faculty("F001", "Faculty User", email, parsedPassword, "FAC001", "General");
                if (faculty.login(email, parsedPassword)) {
                    dispose();
                    new FacultyDashboard(faculty);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password!");
                }

            } else if (role.equals("admin")) {
                Admin admin = new Admin("A001", "Admin User", email, parsedPassword, "ADM001");
                if (admin.login(email, parsedPassword)) {
                    dispose();
                    new AdminDashboard(admin);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password!");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Invalid role! Enter: student, faculty, or admin");
            }
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
