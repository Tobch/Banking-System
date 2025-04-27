package org.example;


import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JFrame {
    private JLabel nameLabel;
    private JLabel roleLabel;
    private JLabel accountNumberLabel;
    private JButton waitingLoanButton;
    private JButton addAccountButton;
    private JButton addLoanButton;
    private JButton displayAdminsButton;
    private JButton displayClientsButton;
    private JButton logoutButton;
    private JButton addClientAccountButton;
    private JButton adminBonusButton;

    private Admin currentAdmin;

    public AdminPanel(Admin admin) {
        this.currentAdmin = admin;

        setTitle("Admin Panel");
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

        nameLabel = new JLabel("Name: " + admin.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(nameLabel, gbc);

        roleLabel = new JLabel("Role: " + admin.getAdminRole());
        roleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(roleLabel, gbc);

        accountNumberLabel = new JLabel("Account Number: " + admin.getAccountNumber());
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        infoPanel.add(accountNumberLabel, gbc);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 1, 10, 10)); // Adjusted grid layout for 8 buttons
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Dimension buttonSize = new Dimension(150, 40);

        waitingLoanButton = new JButton("Waiting Loan");
        waitingLoanButton.setFont(new Font("Arial", Font.BOLD, 25));
        waitingLoanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AdminWaitingLoan().setVisible(true);
            }
        });
        buttonPanel.add(waitingLoanButton);

        addAccountButton = new JButton("Add Admin Account");
        addAccountButton.setFont(new Font("Arial", Font.BOLD, 25));
        addAccountButton.addActionListener(e -> {
            AddAccountAdmin er = new AddAccountAdmin(currentAdmin);
            er.setVisible(true);
        });
        buttonPanel.add(addAccountButton);

        addClientAccountButton = new JButton("Add Client Account");
        addClientAccountButton.setFont(new Font("Arial", Font.BOLD, 25));
        addClientAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddClientAccount er = new AddClientAccount();
                er.setVisible(true);
            }
        });
        buttonPanel.add(addClientAccountButton);

        addLoanButton = new JButton("Loan");
        addLoanButton.setFont(new Font("Arial", Font.BOLD, 25));
        addLoanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DisplayLoans().setVisible(true);
            }
        });
        buttonPanel.add(addLoanButton);

        displayAdminsButton = new JButton("Display Admins");
        displayAdminsButton.setFont(new Font("Arial", Font.BOLD, 25));
        displayAdminsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DisplayAdmins e3 = new DisplayAdmins(new ArrayList<>(Admin.getAdmins()));
                e3.setVisible(true);
            }
        });
        buttonPanel.add(displayAdminsButton);

        displayClientsButton = new JButton("Display Clients");
        displayClientsButton.setFont(new Font("Arial", Font.BOLD, 25));
        displayClientsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ClientDisplay().setVisible(true);
            }
        });
        buttonPanel.add(displayClientsButton);

        adminBonusButton = new JButton("Admin Bonus");
        adminBonusButton.setFont(new Font("Arial", Font.BOLD, 25));
        adminBonusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AdminBonus().setVisible(true);
            }
        });
        buttonPanel.add(adminBonusButton);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 25));
        logoutButton.addActionListener(new LogoutButtonListener());
        buttonPanel.add(logoutButton);


        add(infoPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);


        if (!admin.getAdminRole().equals("Senior Admin")) {
            adminBonusButton.setVisible(false);
            displayAdminsButton.setVisible(false);
            addAccountButton.setVisible(false);
        }


        setVisible(true);
    }
public 

    class LogoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LoginPage a = new LoginPage();
            a.setVisible(true);
            dispose();
        }
    }
}
