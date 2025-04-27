package org.example;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminBonus extends JFrame {
    private JTable bonusTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton closeButton;

    public AdminBonus() {
        setTitle("Admin Bonus Management");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create panel for bonus management
        JPanel bonusPanel = createBonusPanel();
        add(bonusPanel, BorderLayout.CENTER);

        // Create panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton = new JButton("Add Bonus");
        addButton.setFont(new Font("Arial", Font.BOLD, 18));
        addButton.setPreferredSize(new Dimension(200, 50));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBonus();
            }
        });
        buttonPanel.add(addButton);

        closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setPreferredSize(new Dimension(200, 50));
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Show the frame
        setVisible(true);
    }

    private JPanel createBonusPanel() {
        JPanel bonusPanel = new JPanel(new BorderLayout());
        bonusPanel.setBackground(Color.WHITE);

        JLabel bonusLabel = new JLabel("Admin Bonus Management");
        bonusLabel.setFont(new Font("Arial", Font.BOLD, 28));
        bonusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bonusPanel.add(bonusLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Account Number", "Admin Name", "Current Balance"}, 0);
        bonusTable = new JTable(tableModel);
        bonusTable.setFont(new Font("Arial", Font.PLAIN, 18));
        bonusTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(bonusTable);
        bonusPanel.add(scrollPane, BorderLayout.CENTER);

        populateBonusTable();

        return bonusPanel;
    }

    private void populateBonusTable() {
        tableModel.setRowCount(0);
        for (Admin admin : Admin.getAdmins()) {
            tableModel.addRow(new Object[]{
                    admin.getAccountNumber(),
                    admin.getName(),
                    admin.getBalance()
            });
        }
    }

    private void addBonus() {
        String accountNumberStr = JOptionPane.showInputDialog(this, "Enter Admin Account Number:");
        String bonusAmountStr = JOptionPane.showInputDialog(this, "Enter Bonus Amount:");
        try {
            int accountNumber = Integer.parseInt(accountNumberStr);
            int bonusAmount = Integer.parseInt(bonusAmountStr);
            Admin admin = findAdminByAccountNumber(accountNumber);
            if (admin != null) {
                admin.setBalance(admin.getBalance() + bonusAmount);
                populateBonusTable();
                JOptionPane.showMessageDialog(this, "Bonus added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Admin not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid account number or bonus amount.");
        }
    }

    private Admin findAdminByAccountNumber(int accountNumber) {
        for (Admin admin : Admin.getAdmins()) {
            if (admin.getAccountNumber() == accountNumber) {
                return admin;
            }
        }
        return null;
    }


}
