package online_examination_system.gui;

import online_examination_system.Data.DataStore;
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

    JButton registerButton;
    JButton logoutButton;

    Admin admin;

    public AdminDashboard(Admin admin) {
        this.admin = admin;

        GuiStyle.prepareFrame(this, "Admin Dashboard", 560, 430, JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = GuiStyle.surfacePanel();
        GridBagConstraints gbc = GuiStyle.constraints();
        int row = 0;

        GuiStyle.addTitle(contentPanel, "Admin Dashboard", gbc, row++);
        GuiStyle.addInfoRow(contentPanel, row++, admin.getName(), "Logged in as :", gbc);
        GuiStyle.addInfoRow(contentPanel, row++, admin.getAdminId(), "Admin ID :", gbc);

        GuiStyle.addSection(contentPanel, "Register a New User", gbc, row++);
        nameField = new JTextField(25);
        GuiStyle.addFormRow(contentPanel, row++, nameField, "Name :", gbc);

        emailField = new JTextField(25);
        GuiStyle.addFormRow(contentPanel, row++, emailField, "Email :", gbc);

        passwordField = new JTextField(25);
        GuiStyle.addFormRow(contentPanel, row++, passwordField, "Password :", gbc);

        roleField = new JTextField(25);
        GuiStyle.addFormRow(contentPanel, row++, roleField, "Role (student / faculty) :", gbc);

        registerButton = new JButton("Register User");
        GuiStyle.addFullWidth(contentPanel, GuiStyle.buttonStack(registerButton), gbc, row++);

        logoutButton = new JButton("Logout");
        GuiStyle.addFullWidth(contentPanel, GuiStyle.buttonStack(logoutButton), gbc, row++);

        registerButton.addActionListener(this);
        logoutButton.addActionListener(this);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        scrollPane.getViewport().setBackground(GuiStyle.BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
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

        } else if (e.getSource() == logoutButton) {
            admin.logout();
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
            dispose();
            AppNavigator.logout();
        }
    }
}
