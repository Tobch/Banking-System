package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddClientAccount extends JFrame {
    private JTextField nameField;
    private JTextField addressField;
    private JPasswordField passwordField;
    private JTextField balanceField;
    private JTextField accountNumberField;
    private JTextField phoneNumberField;
    private JTextField jobField;
    private JButton addButton;
    private JButton cancelButton;

    public AddClientAccount() {
        setTitle("Add Client Account");
        setSize(400, 400);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name label and text field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Address label and text field
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(addressLabel, gbc);

        addressField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Balance label and text field
        JLabel balanceLabel = new JLabel("Balance:");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(balanceLabel, gbc);

        balanceField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(balanceField, gbc);

        // Account Number label and text field
        JLabel accountNumberLabel = new JLabel("Account Number:");
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(accountNumberLabel, gbc);

        accountNumberField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(accountNumberField, gbc);

        // Phone Number label and text field
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(phoneNumberLabel, gbc);

        phoneNumberField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(phoneNumberField, gbc);

        // Job label and text field
        JLabel jobLabel = new JLabel("Job:");
        jobLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(jobLabel, gbc);

        jobField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(jobField, gbc);

        // Add button
        addButton = new JButton("Add");
        addButton.setFont(new Font("Arial", Font.BOLD, 15));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    String address = addressField.getText();
                    String password = new String(passwordField.getPassword());
                    int balance = Integer.parseInt(balanceField.getText());
                    int accountNumber = Integer.parseInt(accountNumberField.getText());
                    String phoneNumber = phoneNumberField.getText();
                    String job = jobField.getText();

                    // Validation checks
                    if (password.length() < 8) {
                        JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (accountNumber < 10000 || accountNumber > 99999) {
                        JOptionPane.showMessageDialog(null, "Account Number must be a 5-digit number", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (balance <= 0) {
                        JOptionPane.showMessageDialog(null, "Balance must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (Admin.isAccountNumberTaken(accountNumber) || Client.isAccountNumberTaken(accountNumber)) {
                        JOptionPane.showMessageDialog(null, "Account Number is already taken", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Client newClient = new Client(name, address, password, balance, accountNumber, phoneNumber, job);
                    Client.addClient(newClient);
                    JOptionPane.showMessageDialog(null, "Client account added successfully!");
                    dispose(); // Close the AddClientAccount window
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numeric values for balance and account number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 15));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Add panels to the frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
