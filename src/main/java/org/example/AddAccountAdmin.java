package org.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AddAccountAdmin extends JFrame {
    private Admin admin; // Assuming Admin is a class that has been defined elsewhere
    private JTextField nameTextField;
    private JTextField addressTextField;
    private JPasswordField passwordField;
    private JTextField balanceTextField;
    private JTextField accountNumberTextField;
    private JTextField adminRoleTextField;
    private JButton addButton;
    private JButton cancelButton;
    public AddAccountAdmin(Admin admin) {
        this.admin = admin;
        // Additional initialization logic if needed
    }
    public AddAccountAdmin() {
        setTitle("Add Account");
        setSize(700, 400);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Removed as 'admin' is not declared or used elsewhere
        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create panel for the components
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name label and text field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        nameTextField = new JTextField(20);
        nameTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        formPanel.add(nameTextField, gbc);

        // Address label and text field
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(addressLabel, gbc);

        addressTextField = new JTextField(20);
        addressTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        formPanel.add(addressTextField, gbc);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Balance label and text field
        JLabel balanceLabel = new JLabel("Balance:");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(balanceLabel, gbc);

        balanceTextField = new JTextField(20);
        balanceTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        formPanel.add(balanceTextField, gbc);

        // Account number label and text field
        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(accountNumberLabel, gbc);

        accountNumberTextField = new JTextField(20);
        accountNumberTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        formPanel.add(accountNumberTextField, gbc);

        // Admin role label and text field
        JLabel adminRoleLabel = new JLabel("Admin Role:");
        adminRoleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(adminRoleLabel, gbc);

        adminRoleTextField = new JTextField(20);
        adminRoleTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        formPanel.add(adminRoleTextField, gbc);

        // Add button
        addButton = new JButton("Add Account");
        addButton.setFont(new Font("Arial", Font.BOLD, 25));
        addButton.setBackground(Color.GREEN);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(new AddButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(addButton, gbc);

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 25));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(new CancelButtonListener());
        gbc.gridy = 7;
        formPanel.add(cancelButton, gbc);

        // Add components to the frame
        add(formPanel, BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);
    }

    // Action listener for the add button
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameTextField.getText();
                String address = addressTextField.getText();
                String password = new String(passwordField.getPassword());
                int balance = Integer.parseInt(balanceTextField.getText());
                int accountNumber = Integer.parseInt(accountNumberTextField.getText());
                String adminRole = adminRoleTextField.getText();

                // Validation checks
                if (name.isEmpty() || address.isEmpty() || password.isEmpty() || adminRole.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.length() < 8) {
                    JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!adminRole.equals("Senior Admin") && !adminRole.equals("Admin")) {
                    JOptionPane.showMessageDialog(null, "Admin Role must be 'Senior Admin' or 'Admin'", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (accountNumber < 10000 || accountNumber > 99999) {
                    JOptionPane.showMessageDialog(null, "Account Number must be a 5-digit number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (balance < 0) {
                    JOptionPane.showMessageDialog(null, "Balance cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (Admin.isAccountNumberTaken(accountNumber) || Client.isAccountNumberTaken(accountNumber)) {
                    JOptionPane.showMessageDialog(null, "Account Number is already taken", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Admin newAdmin = new Admin(name, address, password, balance, accountNumber, adminRole);

                if (Admin.addAdmin1(newAdmin)) {
                    JOptionPane.showMessageDialog(null, "Account added successfully!");
                    dispose(); // Close the add account window
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add account. Duplicate account number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric data for balance and account number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Action listener for the cancel button
    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}
