package org.example;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class DisplayLoans extends JFrame {
    private JTable loansTable;
    private DefaultTableModel tableModel;
    private JButton closeButton;

    public DisplayLoans() {
        setTitle("Display Loans");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create panel for loan display
        JPanel loansPanel = createLoansPanel();
        add(loansPanel, BorderLayout.CENTER);

        // Create panel for the close button
        JPanel buttonPanel = new JPanel();
        closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setBackground(new Color(255, 69, 0)); // Red-Orange
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(150, 50));
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Show the frame
        setVisible(true);
    }

    private JPanel createLoansPanel() {
        JPanel loansPanel = new JPanel(new BorderLayout());
        loansPanel.setBackground(Color.WHITE);

        JLabel loansLabel = new JLabel("Loans");
        loansLabel.setFont(new Font("Arial", Font.BOLD, 28));
        loansLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loansPanel.add(loansLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Account Number","Client Name", "Amount", "Term", "Start Date", }, 0);
        loansTable = new JTable(tableModel);
        loansTable.setFont(new Font("Arial", Font.PLAIN, 18));
        loansTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(loansTable);
        loansPanel.add(scrollPane, BorderLayout.CENTER);

        populateLoanTable();

        return loansPanel;
    }

    private void populateLoanTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (Client client : Client.getClients()) {
            for (Loan Loans : client.getLoans()) {
                tableModel.addRow(new Object[]{
                        client.getAccountNumber(),
                        client.getName(),
                        Loans.getLoanAmount(),
                        Loans.getTermInYears(),
                        Loans.getStartDate()
                });
            }
        }
    }



}
