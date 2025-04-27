package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Admin {
    private static List<Admin> admins = new ArrayList<>();
    private String name;
    private String city;
    private String password;
    private int salary;
    private int id; // Account number
    private String role;
    private int balance; // Added missing field
    private String address;
    private double bonusPercentage;
    private String phoneNumber; // Added missing field
    private List<String> transactions = new ArrayList<>(); // List to hold transactions for the admin

    static {
        admins = new ArrayList<>();
        initializeDefaultAdmins();
    }

    private static void initializeDefaultAdmins() {
        Admin e1 = new Admin("Admin Ahmed", "Address Two", "Admin234", 100, 98765, "Senior Admin");
        admins.add(e1);
    }
    
    public void setBonusPercentage(double bonusPercentage) {
        this.bonusPercentage = bonusPercentage;
    }

    public double getBonusPercentage() {
        return bonusPercentage;
    }

    // Constructor
    public Admin(String name, String city, String password, int salary, int id, String role) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null) {
            throw new NullPointerException("Password cannot be null");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        this.name = name;
        this.city = city;
        this.password = password;
        this.salary = salary;
        this.id = id;
        this.role = role;
        this.balance = 0; // Default balance
    }

    // Add an admin to the list
    public static boolean addAdmin1(Admin admin) {
        if (admins.stream().anyMatch(a -> a.id == admin.id)) {
            return false; // Duplicate ID
        }
        return admins.add(admin);
    }

    // Remove an admin from the list
    public static boolean removeAdmin(Admin admin) {
        return admins.remove(admin);
    }

    // Check if an account number is already taken
    public static boolean isAccountNumberTaken(int accountNumber) {
        return admins.stream().anyMatch(admin -> admin.id == accountNumber);
    }

    // Get the list of admins (immutable)
    public static List<Admin> getAdmins() {
        return Collections.unmodifiableList(admins);
    }

    // Clear the list of admins
    public static void clearAdmin() {
        admins.clear();
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for account number
    public int getAccountNumber() {
        return id;
    }

    // Getter for balance
    public int getBalance() {
        return balance;
    }

    // Setter for balance
    public void setBalance(int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Admin{name='" + name + "', role='" + role + "', accountNumber=" + id + ", balance=" + balance + "salary=" + salary + "id=" + id + "role=" + role + "city=" + city + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Admin admin = (Admin) obj;
        return id == admin.id;
    }

    public String getAdminRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public boolean isValidPassword(String password) {

        if (password.length() < 6) {
            return false;
        }
        return true;
    }

    public boolean isValidAccountNumber(int id) {
        return id >= 10000 && id <= 99999; // Example validation for a 5-digit account number
    }

    public boolean isBalanceValid(int balance) {

        if (balance < 100) {
            return false;
        }
        return true;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double calculateBonus(int bonusPercentage) {
        if (bonusPercentage < 0) {
            throw new IllegalArgumentException("Bonus percentage must be positive");
        }
        return (balance * bonusPercentage) / 100.0;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.salary = salary;
    }

    public void setAdminRole(String role) {
        if (role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
        this.role = role;
    }

    // Logic to accept a loan
    public void acceptLoan(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null");
        }
        transactions.add("Accepted Loan: " + loan.toString()); // Log the accepted loan
        loan.setStatus("Accepted"); // Update the loan status (method must be defined in Loan class)
    }

    // Logic to reject a loan
    public void rejectLoan(Client client, WaitingLoan waitingLoan) {
        client.removeWaitingLoan(waitingLoan);
        // Additional logic for rejecting a waiting loan, if needed
    }

    public void acceptLoan(Client client, WaitingLoan waitingLoan) {
        if (waitingLoan.getAmount() <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        Loan loan = new Loan(waitingLoan.getAmount(), waitingLoan.getTermInYears(), waitingLoan.getDate(), waitingLoan.getAccountNumber());
        client.addLoan(loan);
        client.setBalance(client.getBalance() + loan.getLoanAmount());  // Update the balance with the loan amount
        client.removeWaitingLoan(waitingLoan);
    }

    
}





