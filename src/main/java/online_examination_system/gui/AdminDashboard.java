package online_examination_system.gui;

import online_examination_system.users.Admin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame implements ActionListener {

    JTextField nameField;
    JTextField emailField;
    JTextField roleField;
    JTextField examTitleField;
    JTextField examDurationField;

    JButton registerButton;
    JButton scheduleButton;
    JButton logoutButton;

    Admin admin;

    AdminDashboard(Admin admin) {
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
            String role  = roleField.getText().trim().toLowerCase();

            if (name.isEmpty() || email.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields!");
                return;
            }

            if (role.equals("student")) {
                JOptionPane.showMessageDialog(null,
                    "[ADMIN] User Registered!\nName : " + name + "\nEmail : " + email + "\nRole : Student");
                // TODO: admin.manageUsers(user) once constructors are finalised
            } else if (role.equals("faculty")) {
                JOptionPane.showMessageDialog(null,
                    "[ADMIN] User Registered!\nName : " + name + "\nEmail : " + email + "\nRole : Faculty");
                // TODO: admin.manageUsers(user) once constructors are finalised
            } else {
                JOptionPane.showMessageDialog(null, "Invalid role! Enter: student or faculty");
                return;
            }

            nameField.setText("");
            emailField.setText("");
            roleField.setText("");

        } else if (e.getSource() == scheduleButton) {
            String examTitle    = examTitleField.getText().trim();
            String examDuration = examDurationField.getText().trim();

            if (examTitle.isEmpty() || examDuration.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all exam fields!");
                return;
            }

            JOptionPane.showMessageDialog(null,
                "[ADMIN] Exam Scheduled!\nTitle : " + examTitle + "\nDuration : " + examDuration + " minutes");
            // TODO: admin.scheduleExam(new Exam(examTitle, duration)) once Exam.java is implemented

            examTitleField.setText("");
            examDurationField.setText("");

        } else if (e.getSource() == logoutButton) {
            admin.logout();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            dispose();
            // TODO: new LoginFrame();
        }
    }
}