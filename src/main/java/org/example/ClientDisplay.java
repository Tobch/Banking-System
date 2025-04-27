package org.example;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class ClientDisplay extends JFrame {
    private JTable clientsTable;
    private DefaultTableModel tableModel;
    private JButton closeButton;

    public ClientDisplay() {
        setTitle("Client Display");
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Create panel for client display
        JPanel clientsPanel = createClientsPanel();
        add(clientsPanel, BorderLayout.CENTER);

        // Create panel for the close button
        JPanel buttonPanel = new JPanel();
        closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setBackground(Color.GRAY);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(150, 50));
        closeButton.addActionListener(e -> dispose()); // Close the frame
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Show the frame
        setVisible(true);
    }

    private JPanel createClientsPanel() {
        JPanel clientsPanel = new JPanel(new BorderLayout());
        clientsPanel.setBackground(Color.WHITE);

        JLabel clientsLabel = new JLabel("Clients");
        clientsLabel.setFont(new Font("Arial", Font.BOLD, 28));
        clientsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        clientsPanel.add(clientsLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Account Number", "Client Name", "Balance", "Job", "Phone Number"}, 0);
        clientsTable = new JTable(tableModel);
        clientsTable.setFont(new Font("Arial", Font.PLAIN, 18));
        clientsTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        clientsPanel.add(scrollPane, BorderLayout.CENTER);

        populateClientsTable();

        return clientsPanel;
    }

    private void populateClientsTable() {
        tableModel.setRowCount(0);
        for (Client client : Client.getClients()) {
            tableModel.addRow(new Object[]{
                    client.getAccountNumber(),
                    client.getName(),
                    client.getBalance(),
                    client.getJob(),
                    client.getPhoneNumber()
            });
        }
    }
}
