package org.example;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginPage extends JFrame  {
    
    private String usernameField;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    public void setUsernameField(String usernameField) {
        this.usernameField = usernameField;
    }
    public String getUsernameField() {
        return usernameField;
    }
    public JTextField getTextField() {
        return userTextField;
    }
    public void setTextField(JTextField textField) {
        this.userTextField = textField;
    }
    public String getPasswordField() {
        return new String(passwordField.getPassword());
    }
    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }
    public JButton getLoginButton() {
        return loginButton;
    }
    public void setLoginButton(JButton loginButton) {
        this.loginButton = loginButton;
    }
    public JButton getCancelButton() {
        return cancelButton;
    }
    public void setCancelButton(JButton cancelButton) {
        this.cancelButton = cancelButton;
    }
    public LoginPage() {
        setTitle("Login Page");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout for the frame
        setLayout(new BorderLayout());


        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel userLabel = new JLabel("Account Number:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        userTextField = new JTextField(20);
        userTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        formPanel.add(userTextField, gbc);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);


        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 25));
        loginButton.setBackground(Color.GREEN);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new LoginButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);


        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 25));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(new CancelButtonListener());
        gbc.gridy = 3;
        formPanel.add(cancelButton, gbc);


        URL imageUrl = getClass().getResource("/Bank1.jpg"); // Adjust path if needed
        if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(600, 400, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            // Create a label with the resized image
            JLabel imageLabel = new JLabel(resizedIcon);
            imageLabel.setHorizontalAlignment(JLabel.CENTER);

            // Add components to the frame
            add(imageLabel, BorderLayout.NORTH);
        } else {
            System.err.println("Image not found.");
        }

        add(formPanel, BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);
    }

    // Action listener for the login button
    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            login("admin", "admin123");
        }
    }


    private class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }

    private boolean login(String username, String password) {
        try {
            // Get account number and the password entered by the user
            int accountNumber = Integer.parseInt(userTextField.getText());
            String enteredPassword = new String(passwordField.getPassword());
    
            // Check for an authenticated administrator
            Admin authenticatedAdmin = null;
            for (Admin admin : Admin.getAdmins()) {
                if (admin.getAccountNumber() == accountNumber && admin.getPassword().equals(enteredPassword)) {
                    authenticatedAdmin = admin;
                    break;
                }
            }
    
            if (authenticatedAdmin != null) {
                // Login as admin successful
                JOptionPane.showMessageDialog(null, "Login successful!");
                AdminPanel adminPanel = new AdminPanel(authenticatedAdmin);
                adminPanel.setVisible(true);
                dispose(); // Close the login window
                return true; // Login success
            } else {
                // Check for an authenticated client
                Client authenticatedClient = null;
                for (Client client : Client.getClients()) {
                    if (client.getAccountNumber() == accountNumber && client.getPassword().equals(enteredPassword)) {
                        authenticatedClient = client;
                        break;
                    }
                }
    
                if (authenticatedClient != null) {
                    // Login as client successful
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    ClientPanel clientPanel = new ClientPanel(authenticatedClient);
                    clientPanel.setVisible(true);
                    dispose(); // Close the login window
                    return true; // Login success
                } else {
                    // Invalid login credentials
                    JOptionPane.showMessageDialog(null, "Invalid account number or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            // Account number was not numeric
            JOptionPane.showMessageDialog(null, "Please enter a valid account number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        return false; // Login failed
    }
}
