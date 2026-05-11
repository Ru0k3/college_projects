package online_examination_system.gui;

import online_examination_system.Data.DataStore;
import online_examination_system.exams.Exam;
import online_examination_system.main.AppNavigator;
import online_examination_system.users.Admin;
import online_examination_system.users.Users;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame implements ActionListener {

    JTextField nameField;
    JTextField emailField;
    JTextField passwordField;
    JTextField roleField;
    JTextField examTitleField;
    JTextField examDurationField;
    JTextField examTimeField;

    JButton registerButton;
    JButton scheduleButton;
    JButton logoutButton;

    Admin admin;

    public AdminDashboard(Admin admin) {
        this.admin = admin;

        setTitle("Admin Dashboard");
        setSize(430, 480);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("=== Admin Dashboard ==="));
        add(new JLabel("Logged in as : " + admin.getName()));
        add(new JLabel("Admin ID     : " + admin.getAdminId()));

        add(new JLabel("--- Register a New User ---"));
        add(new JLabel("Name :"));
        nameField = new JTextField(25);
        add(nameField);

        add(new JLabel("Email :"));
        emailField = new JTextField(25);
        add(emailField);

        add(new JLabel("Password :"));
        passwordField = new JTextField(25);
        add(passwordField);

        add(new JLabel("Role (student / faculty) :"));
        roleField = new JTextField(25);
        add(roleField);

        registerButton = new JButton("Register User");
        add(registerButton);

        add(new JLabel("--- Schedule an Exam ---"));
        add(new JLabel("Exam Title :"));
        examTitleField = new JTextField(25);
        add(examTitleField);

        add(new JLabel("Duration (mins) :"));
        examDurationField = new JTextField(25);
        add(examDurationField);

        add(new JLabel("Scheduled Time :"));
        examTimeField = new JTextField(25);
        add(examTimeField);

        scheduleButton = new JButton("Schedule Exam");
        add(scheduleButton);

        logoutButton = new JButton("Logout");
        add(logoutButton);

        registerButton.addActionListener(this);
        scheduleButton.addActionListener(this);
        logoutButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String name  = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String role  = roleField.getText().trim().toLowerCase();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!");
                return;
            }

            if (!role.equals("student") && !role.equals("faculty")) {
                JOptionPane.showMessageDialog(null, "Invalid role! Enter: student or faculty");
                return;
            }

            Users user = DataStore.addUser(name, email, password, role);
            admin.manageUsers(user);
            JOptionPane.showMessageDialog(null,
                "[ADMIN] User Registered!\nName : " + name + "\nEmail : " + email + "\nRole : " + role);

            nameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            roleField.setText("");

        } else if (e.getSource() == scheduleButton) {   
            String examTitle    = examTitleField.getText().trim();
            String examDuration = examDurationField.getText().trim();
            String examTime     = examTimeField.getText().trim();

            if (examTitle.isEmpty() || examDuration.isEmpty() || examTime.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all exam fields!");
                return;
            }
            int duration;
            try {
                duration = Integer.parseInt(examDuration);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Duration must be a whole number!");
                return;
            }

            Exam exam = DataStore.addExam(examTitle, duration, examTime, admin.getAdminId());
            admin.scheduleExam(exam);
            JOptionPane.showMessageDialog(null,
                "[ADMIN] Exam Scheduled!\nID : " + exam.getExamId() + "\nTitle : " + examTitle + "\nDuration : " + examDuration + " minutes");

            examTitleField.setText("");
            examDurationField.setText("");
            examTimeField.setText("");

        } else if (e.getSource() == logoutButton) {
            admin.logout();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            dispose();
            AppNavigator.logout();
        }
    }
}
