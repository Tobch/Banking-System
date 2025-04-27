package org.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class AdminWaitingLoan extends JFrame {
    private DefaultTableModel tableModel;
    private JTable waitingLoansTable;

    public AdminWaitingLoan() {
        setTitle("Admin Waiting Loans");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        add(createWaitingLoansPanel(), BorderLayout.CENTER);
    }

    private JPanel createWaitingLoansPanel() {
        JPanel waitingLoansPanel = new JPanel(new BorderLayout());
        waitingLoansPanel.setBackground(Color.WHITE);

        JLabel loansLabel = new JLabel("Waiting Loans");
        loansLabel.setFont(new Font("Arial", Font.BOLD, 28));
        loansLabel.setHorizontalAlignment(SwingConstants.CENTER);
        waitingLoansPanel.add(loansLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Account Number", "Client Name", "Amount", "Term", "Request Date"}, 0);
        waitingLoansTable = new JTable(tableModel);
        waitingLoansTable.setFont(new Font("Arial", Font.PLAIN, 18));
        waitingLoansTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(waitingLoansTable);
        waitingLoansPanel.add(scrollPane, BorderLayout.CENTER);

        updateWaitingLoansDisplay();

        return waitingLoansPanel;
    }

    private void updateWaitingLoansDisplay() {
        tableModel.setRowCount(0); // Clear existing rows
        for (Client client : Client.getClients()) {
            for (WaitingLoan waitingLoan : client.getWaitingLoans()) { // Use the no-argument method
                tableModel.addRow(new Object[]{
                        client.getAccountNumber(),
                        client.getName(),
                        waitingLoan.getAmount(),
                        waitingLoan.getTerm(),
                        waitingLoan.getStartDate()
                });
            }
        }
    }

    private void acceptSelectedLoan() {
        int selectedRow = waitingLoansTable.getSelectedRow();
        if (selectedRow != -1) {
            // Logic to accept the selected loan
        } else {
            JOptionPane.showMessageDialog(this, "Please select a loan to accept.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
