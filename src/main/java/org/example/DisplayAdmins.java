package org.example;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;

public class DisplayAdmins extends JFrame {
    private JTable adminTable;
    private JButton closeButton;

    public DisplayAdmins(ArrayList<Admin> admins) {
        setTitle("Display Admins");
        setSize(800, 400);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create table to display admins
        String[] columnNames = {"Name", "Address", "Account Number", "Admin Role","Balance"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        adminTable = new JTable(tableModel);

        for (Admin admin : admins) {
            Object[] rowData = {
                    admin.getName(),
                    admin.getAddress(),
                    admin.getAccountNumber(),
                    admin.getAdminRole(),
                    admin.getBalance()
            };
            tableModel.addRow(rowData);
        }

        JScrollPane scrollPane = new JScrollPane(adminTable);
        add(scrollPane, BorderLayout.CENTER);

        // Close button
        closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);

        // Make the frame visible
        setVisible(true);
    }
}

