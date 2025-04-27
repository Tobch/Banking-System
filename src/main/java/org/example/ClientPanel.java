package org.example;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientPanel extends JFrame {
    private JLabel nameLabel;
    private JLabel phoneNumberLabel;
    private JLabel accountNumberLabel;
    private JLabel jobLabel;
    private JButton viewBalanceButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton transferButton;
    private JButton transactionHistoryButton;

    private JButton loanButton;

    private JButton logoutButton;

    private Client currentClient;

    public ClientPanel(Client client) {
        this.currentClient = client;

        setTitle("Client Panel");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create panel for the components
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameLabel = new JLabel("Name: " + client.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(nameLabel, gbc);

        phoneNumberLabel = new JLabel("Phone Number: " + client.getPhoneNumber());
        phoneNumberLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(phoneNumberLabel, gbc);

        accountNumberLabel = new JLabel("Account Number: " + client.getAccountNumber());
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(accountNumberLabel, gbc);

        jobLabel = new JLabel("Job: " + client.getJob());
        jobLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(jobLabel, gbc);

        // Create panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10)); // Increased rows to 6
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        viewBalanceButton = new JButton("View Balance");
        viewBalanceButton.setFont(new Font("Arial", Font.BOLD, 25));
        viewBalanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showBalanceView();
            }
        });
        buttonPanel.add(viewBalanceButton);

        depositButton = new JButton("Deposit");
        depositButton.setFont(new Font("Arial", Font.BOLD, 25));
        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDepositView();
            }
        });
        buttonPanel.add(depositButton);

        withdrawButton = new JButton("Withdraw");
        withdrawButton.setFont(new Font("Arial", Font.BOLD, 25));
        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showWithdrawView();
            }
        });
        buttonPanel.add(withdrawButton);

        transferButton = new JButton("Transfer");
        transferButton.setFont(new Font("Arial", Font.BOLD, 25));
        transferButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTransferView();
            }
        });
        buttonPanel.add(transferButton);

        transactionHistoryButton = new JButton("Transaction History");
        transactionHistoryButton.setFont(new Font("Arial", Font.BOLD, 25));
        transactionHistoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTransactionHistoryView();
            }
        });
        buttonPanel.add(transactionHistoryButton);


        loanButton =  new JButton("Loan");
        loanButton.setFont(new Font("Arial", Font.BOLD, 25));
        loanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoanView();
            }
        });
        buttonPanel.add(loanButton);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 25));
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(logoutButton);

        // Add panels to the frame
        add(infoPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);
    }

    private JButton createEnterButton(final JFrame frame, final Runnable action) {
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Arial", Font.BOLD, 25));
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action.run();
                frame.dispose();
            }
        });
        return enterButton;
    }

    private void showBalanceView() {
        JFrame balanceFrame = new JFrame("Balance View");
        balanceFrame.setSize(500, 300);
        balanceFrame.setLocationRelativeTo(null); // Center the frame on the screen

        JPanel balancePanel = new JPanel(new GridBagLayout());
        balancePanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel balanceLabel = new JLabel("Balance:");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        balancePanel.add(balanceLabel, gbc);

        JLabel balanceLabel1 = new JLabel("Current Balance:");
        balanceLabel1.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset gridwidth to default
        gbc.anchor = GridBagConstraints.EAST;
        balancePanel.add(balanceLabel1, gbc);

        JTextField balanceField = new JTextField(20);
        balanceField.setText(String.valueOf(currentClient.getBalance()));
        balanceField.setFont(new Font("Arial", Font.PLAIN, 25));
        balanceField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        balancePanel.add(balanceField, gbc);

        balanceFrame.add(balancePanel);
        balanceFrame.setVisible(true);
    }

    private void showDepositView() {
        JFrame depositFrame = new JFrame("Deposit");
        depositFrame.setSize(500, 300);
        depositFrame.setLocationRelativeTo(null); // Center the frame on the screen

        JPanel depositPanel = new JPanel(new GridBagLayout());
        depositPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel depositLabel = new JLabel("Deposit");
        depositLabel.setFont(new Font("Arial", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        depositPanel.add(depositLabel, gbc);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset gridwidth to default
        gbc.anchor = GridBagConstraints.EAST;
        depositPanel.add(amountLabel, gbc);

        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Arial", Font.PLAIN, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        depositPanel.add(amountField, gbc);

        JButton enterButton = createEnterButton(depositFrame, () -> {
            try {
                int amount = Integer.parseInt(amountField.getText());
                if (amount > 0) {
                    currentClient.setBalance(currentClient.getBalance() + amount);
                    currentClient.addTransaction(new Transaction("Deposit", amount, new java.util.Date()));
                    JOptionPane.showMessageDialog(null, "Deposit successful! New balance: " + currentClient.getBalance());
                } else {
                    JOptionPane.showMessageDialog(null, "Amount must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        depositPanel.add(enterButton, gbc);

        depositFrame.add(depositPanel);
        depositFrame.setVisible(true);
    }


    private void showWithdrawView() {
        JFrame withdrawFrame = new JFrame("Withdraw");
        withdrawFrame.setSize(500, 300);
        withdrawFrame.setLocationRelativeTo(null);

        JPanel withdrawPanel = new JPanel(new GridBagLayout());
        withdrawPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel withdrawLabel = new JLabel("Withdraw");
        withdrawLabel.setFont(new Font("Arial", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        withdrawPanel.add(withdrawLabel, gbc);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset gridwidth to default
        gbc.anchor = GridBagConstraints.EAST;
        withdrawPanel.add(amountLabel, gbc);

        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Arial", Font.PLAIN, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        withdrawPanel.add(amountField, gbc);

        JButton enterButton = createEnterButton(withdrawFrame, () -> {
            try {
                int amount = Integer.parseInt(amountField.getText());
                if (amount > 0) {
                    if (amount <= currentClient.getBalance()) {
                        currentClient.setBalance(currentClient.getBalance() - amount);
                        currentClient.addTransaction(new Transaction("Withdraw", amount, new java.util.Date()));
                        JOptionPane.showMessageDialog(null, "Withdrawal successful! New balance: " + currentClient.getBalance());
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Amount must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        withdrawPanel.add(enterButton, gbc);

        withdrawFrame.add(withdrawPanel);
        withdrawFrame.setVisible(true);
    }


    private void showTransferView() {
        JFrame transferFrame = new JFrame("Transfer");
        transferFrame.setSize(500, 300);
        transferFrame.setLocationRelativeTo(null); // Center the frame on the screen

        JPanel transferPanel = new JPanel(new GridBagLayout());
        transferPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel transferLabel = new JLabel("Transfer");
        transferLabel.setFont(new Font("Arial", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        transferPanel.add(transferLabel, gbc);

        JLabel accountLabel = new JLabel("Account Number:");
        accountLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset gridwidth to default
        gbc.anchor = GridBagConstraints.EAST;
        transferPanel.add(accountLabel, gbc);

        JTextField accountField = new JTextField(20);
        accountField.setFont(new Font("Arial", Font.PLAIN, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        transferPanel.add(accountField, gbc);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Reset gridwidth to default
        gbc.anchor = GridBagConstraints.EAST;
        transferPanel.add(amountLabel, gbc);

        JTextField amountField = new JTextField(20);
        amountField.setFont(new Font("Arial", Font.PLAIN, 25));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        transferPanel.add(amountField, gbc);

        JButton enterButton = createEnterButton(transferFrame, () -> {
            try {
                String targetAccount = accountField.getText();
                int amount = Integer.parseInt(amountField.getText());

                if (amount > 0) {
                    if (amount <= currentClient.getBalance()) {
                        boolean transferSuccess = transferFunds(targetAccount, amount);

                        if (transferSuccess) {
                            currentClient.setBalance(currentClient.getBalance() - amount);
                            JOptionPane.showMessageDialog(null, "Transfer successful! New balance: " + currentClient.getBalance());
                        } else {
                            JOptionPane.showMessageDialog(null, "Transfer failed. Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Amount must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        transferPanel.add(enterButton, gbc);

        transferFrame.add(transferPanel);
        transferFrame.setVisible(true);
    }


    private boolean transferFunds(String targetAccountStr, int amount) {
        int targetAccount;
        try {
            // Convert targetAccount from String to int
            targetAccount = Integer.parseInt(targetAccountStr);
        } catch (NumberFormatException e) {
            // Handle the case where the account number is not a valid integer
            JOptionPane.showMessageDialog(null, "Invalid account number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Iterate over the list of clients
        for (Client client : Client.getClients()) {
            // Check if the account number matches the target account
            if (client.getAccountNumber() == targetAccount) {
                // Check if the current client has sufficient balance
                if (amount > currentClient.getBalance()) {
                    return false; // Insufficient funds
                }

                client.setBalance(client.getBalance() + amount);

                currentClient.addTransaction(new Transaction("Transfer", amount, new java.util.Date(), client.getAccountNumber()));
                client.addTransaction(new Transaction("Received", amount, new java.util.Date(), currentClient.getAccountNumber()));

                return true; // Transfer successful
            }
        }

        return false; // Target account not found
    }

    private void showTransactionHistoryView() {
        JFrame historyFrame = new JFrame("Transaction History");
        historyFrame.setSize(600, 400);
        historyFrame.setLocationRelativeTo(null); // Center the frame on the screen

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.WHITE);

        JLabel historyLabel = new JLabel("Transaction History");
        historyLabel.setFont(new Font("Arial", Font.BOLD, 40));
        historyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        historyPanel.add(historyLabel, BorderLayout.NORTH);

        JTextArea historyArea = new JTextArea();
        historyArea.setFont(new Font("Arial", Font.PLAIN, 20));
        historyArea.setEditable(false);

        // Display the transaction history
        StringBuilder historyText = new StringBuilder();
        for (Transaction transaction : currentClient.getTransactions()) {
            historyText.append(transaction.toString()).append("\n");
        }
        historyArea.setText(historyText.toString());

        JScrollPane scrollPane = new JScrollPane(historyArea);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the button panel to the frame
        JPanel buttonPanel = createButtonPanel(historyFrame);
        historyPanel.add(buttonPanel, BorderLayout.SOUTH);

        historyFrame.add(historyPanel);
        historyFrame.setVisible(true);
    }

    private void showLoanView() {
        new ClientLoan(currentClient);
    }

    private JPanel createButtonPanel(JFrame historyFrame) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 25));
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                historyFrame.dispose(); // Close the history frame
            }
        });
        buttonPanel.add(closeButton);

        return buttonPanel;
    }

}
