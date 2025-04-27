package org.example;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class ClientLoan extends JFrame {
    private JButton requestLoanButton;
    private JButton displayLoansButton;
    private JButton closeButton;




    private Client currentClient;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextArea loansArea; // Make this field accessible

    public ClientLoan(Client client) {
        this.currentClient = client;

        setTitle("Loan Panel");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create a panel for card layout
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        add(cardPanel, BorderLayout.CENTER);

        // Add the loan request view
        JPanel requestLoanPanel = createRequestLoanPanel();
        cardPanel.add(requestLoanPanel, "RequestLoan");

        // Add the display loans view
        JPanel displayLoansPanel = createDisplayLoansPanel();
        cardPanel.add(displayLoansPanel, "DisplayLoans");

        // Create a panel for navigation buttons
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Centered buttons with padding
        add(navPanel, BorderLayout.SOUTH);

        requestLoanButton = new JButton("Request Loan");
        requestLoanButton.setFont(new Font("Arial", Font.BOLD, 18));
        requestLoanButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        requestLoanButton.setForeground(Color.WHITE);
        requestLoanButton.setFocusPainted(false);
        requestLoanButton.setPreferredSize(new Dimension(200, 50));
        requestLoanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "RequestLoan");
            }
        });
        navPanel.add(requestLoanButton);

        displayLoansButton = new JButton("Display My Loans");
        displayLoansButton.setFont(new Font("Arial", Font.BOLD, 18));
        displayLoansButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        displayLoansButton.setForeground(Color.WHITE);
        displayLoansButton.setFocusPainted(false);
        displayLoansButton.setPreferredSize(new Dimension(200, 50));
        displayLoansButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateLoansDisplay(); // Refresh the loans display before showing it
                cardLayout.show(cardPanel, "DisplayLoans");
            }
        });
        navPanel.add(displayLoansButton);

         closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setBackground(new Color(255, 69, 58)); // Red-Orange
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(100, 50));
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });
        navPanel.add(closeButton);

        // Show the frame
        setVisible(true);
    }


    private JPanel createRequestLoanPanel() {
        JPanel requestLoanPanel = new JPanel(new GridBagLayout());
        requestLoanPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loanLabel = new JLabel("Request Loan");
        loanLabel.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        requestLoanPanel.add(loanLabel, gbc);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        requestLoanPanel.add(amountLabel, gbc);

        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        requestLoanPanel.add(amountField, gbc);

        JLabel termLabel = new JLabel("Term (years):");
        termLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        requestLoanPanel.add(termLabel, gbc);

        JTextField termField = new JTextField(20);
        termField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        requestLoanPanel.add(termField, gbc);

        JButton enterButton = createEnterButton(amountField, termField);
        enterButton.setPreferredSize(new Dimension(120, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        requestLoanPanel.add(enterButton, gbc);

        return requestLoanPanel;
    }

    private JButton createEnterButton(JTextField amountField, JTextField termField) {
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Arial", Font.BOLD, 18));
        enterButton.setBackground(new Color(34, 139, 34)); // Forest Green
        enterButton.setForeground(Color.WHITE);
        enterButton.setFocusPainted(false);
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int amount = Integer.parseInt(amountField.getText());
                    int term = Integer.parseInt(termField.getText());
                    currentClient.addWaitingLoan(new WaitingLoan(amount, term, new Date(), currentClient.getAccountNumber()));
                    JOptionPane.showMessageDialog(null, "Loan request successful!");
                    // Clear the text fields
                    amountField.setText("");
                    termField.setText("");
                    // Optionally switch to display loans after adding a loan
                    updateLoansDisplay(); // Refresh the loan list
                    cardLayout.show(cardPanel, "DisplayLoans"); // Uncomment if you want to auto-switch to DisplayLoans
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return enterButton;
    }


    private JPanel createDisplayLoansPanel() {
        JPanel displayLoansPanel = new JPanel(new BorderLayout());
        displayLoansPanel.setBackground(Color.WHITE);

        JLabel loansLabel = new JLabel("My Loans");
        loansLabel.setFont(new Font("Arial", Font.BOLD, 28));
        loansLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayLoansPanel.add(loansLabel, BorderLayout.NORTH);

        loansArea = new JTextArea(); // Initialize here
        loansArea.setFont(new Font("Arial", Font.PLAIN, 18));
        loansArea.setEditable(false);
        loansArea.setLineWrap(true);
        loansArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(loansArea);
        displayLoansPanel.add(scrollPane, BorderLayout.CENTER);

        return displayLoansPanel;
    }

    private void updateLoansDisplay() {
        // Refresh the loan display
        StringBuilder loansText = new StringBuilder();
        for (Loan loan : currentClient.getLoans()) {
            loansText.append(loan.toString()).append("\n");
        }
        loansArea.setText(loansText.toString());
    }


}
