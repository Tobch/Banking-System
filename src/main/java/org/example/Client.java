package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;
public class Client {
    private static List<Client> clients = new ArrayList<>();
    private String name;
    private String city;
    private String password;
    private int balance;
    private int id;
    private String phone;
    private String profession;
    private List<Loan> loans = new ArrayList<>();
    private List<WaitingLoan> waitingLoans = new ArrayList<>(); // List to hold waiting loans for the client
    private String job;
    private String phoneNumber;
    private List transactions = new ArrayList<>(); 
    private List<Transaction> transactionHistory = new ArrayList<>();

public List<Transaction> getTransactionHistory() {
    return Collections.unmodifiableList(transactionHistory);
}
    public Client(String name, String city, String password, int balance, int id, String phone, String profession) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (password == null) {
            throw new NullPointerException("Password cannot be null");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        if (profession == null || profession.isEmpty()) {
            throw new IllegalArgumentException("Profession cannot be empty");
        }
        if(phone.length()<7)
        {
            throw new IllegalArgumentException("Phone number must be at least 7 digits");
        }
        if(profession==null || profession.isEmpty())
        {
            throw new IllegalArgumentException("Job cannot be empty");
        }
        this.name = name;
        this.city = city;
        this.password = password;
        this.balance = balance;
        this.id = id;
        this.phone = phone;
        this.profession = profession;
    }

    public static boolean addClient(Client client) {
    if(client.balance < 100)
    {
        return false;
    }
    if (client.password.length() < 6) {
        return false;
    }
    if(isValidAccountNumber(client.id) == false)
    {
        return false;
    }
        if (clients.stream().anyMatch(c -> c.id == client.id)) {
            return false; // Duplicate ID
        }
        return clients.add(client);

    }

    public static boolean removeClient(Client client) {
        return clients.remove(client);
    }

    public static List<Client> getClients() {
        return Collections.unmodifiableList(clients);
    }

    public static void clearClients() {
        clients.clear();
    }

    public String getName() {
        return name;
    }

    public int getAccountNumber() {
        return id;
    }

    public Loan getLoan(int index) {
        return loans.get(index);
    }

    public List<WaitingLoan> getWaitingLoans() {
        return waitingLoans; // Return the waiting loans list
    }

    @Override
    public String toString() {
        return "Client{name='" + name + "', profession='" + profession + "', phone='" + phone + "', accountNumber=" + id + ", balance=" + balance + ", salary=" + balance + ", id=" + id + ", profession=" + profession + ", city=" + city + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Client client = (Client) obj;
        return id == client.id;
    }

    public static boolean isAccountNumberTaken(int accountNumber) {
        return clients.stream().anyMatch(client -> client.id == accountNumber);
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        if (profession == null || profession.trim().isEmpty()) {
            throw new IllegalArgumentException("Job cannot be empty");
        }

        this.profession = profession;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {

    return phone;
    }   
    public int getBalance() {
        return balance;
    }
    public String getJob() {
        return job;
    }
    public String getprofession() {
    	return profession;
    }
    public List addWaitingLoan(WaitingLoan waitingLoan) {
        waitingLoans.add(waitingLoan); // Add the waiting loan to the list
        return waitingLoans; // Return the updated list of waiting loans
    }
    public List<Loan> getLoans() {
        return loans; // Return the list of loans
    }
    public void setBalance(int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }
    public void addTransaction(Transaction transaction) {
        // Ensure both the transactions and transactionHistory are updated
        this.transactions.add(transaction);
        this.transactionHistory.add(transaction);  // Update the transaction history as well
    }
    public List<Transaction> getTransactions() {
        return transactions; // Return the list of transactions
    }
    public void setPhoneNumber(String phoneNumber) {
    if(phoneNumber.length()!=7)
    {
        throw new IllegalArgumentException("Phone number must be 7 digits");
    }
        this.phone = phone; // Set the phone number
    }
    public boolean deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
        Transaction transaction = new Transaction("Deposit", amount, new Date());
        transactions.add(transaction);
        transactionHistory.add(transaction);
        return true; // Indicate that the deposit was successful
    }
    public boolean withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (this.balance < amount) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }
        this.balance -= amount;  // Subtract the withdrawal amount from the balance
        Transaction transaction = new Transaction("Withdraw", amount, new Date());
        transactions.add(transaction);  // Add to transactions list
        transactionHistory.add(transaction);  // Add to transaction history
        return true;
    }


    public synchronized boolean transfer(Client recipient, int amount) {
        if (recipient == null) {
            throw new IllegalArgumentException("Recipient cannot be null");
        }
        if (this.id == recipient.id) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (clients == null || clients.isEmpty()) {
            throw new IllegalArgumentException("Clients list is empty");
        }

        if (clients.stream().noneMatch(c -> c.equals(recipient))) {
            throw new IllegalArgumentException("Recipient does not exist in the system");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (this.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }

        Date transferDate = new Date(); // Create a single date object for consistency
        try {
            // Update balances
            this.setBalance(this.getBalance() - amount);
            recipient.setBalance(recipient.getBalance() + amount);

            // Create and add transactions for both parties
            Transaction transaction = new Transaction("Transfer", amount, transferDate, recipient.getAccountNumber());
            this.addTransaction(transaction);  // Add to sender's transaction history

            Transaction receivedTransaction = new Transaction("Received", amount, transferDate, this.getAccountNumber());
            recipient.addTransaction(receivedTransaction);  // Add to recipient's transaction history

        } catch (Exception e) {
            // Rollback in case of an error
            this.setBalance(this.getBalance() + amount);
            recipient.setBalance(recipient.getBalance() - amount);
            throw new RuntimeException("Transfer failed, rolled back changes", e);
        }

        return true;
    }

    public boolean isvalidPhoneNumber(String phoneNumber) {
        if(phoneNumber.length()<7)
        {
            return false;
        }
        return true;
    }

    public String getAddress() {
        return city; // Return the address of the client
    }
    public String getPassword() {
        return password; // Return the password of the client
    }
    public static boolean isValidAccountNumber(int accountNumber) {
        // Example validation: account number must be 5 digits
        if( String.valueOf(accountNumber).length() != 5)
        {
            return false;
        }
        return true;
    }
    public boolean login(String username, String password) {
        return this.name.equals(username) && this.password.equals(password);
    }
    public Client(String name, String password, double balance) {
        this(name, "Unknown Address", password, (int) balance, 0, "Unknown Phone", "Unknown Job");
    }
    public boolean requestLoan(double amount) {
        // Logic to request a loan
        if (amount > 10000) {
            System.out.println("Loan request denied: Amount exceeds limit.");
            return false; // Loan denied
        } else {
            System.out.println("Loan request approved for amount: " + amount);
            return true; // Loan approved
        }
    }
    public void setJob(String job) {
        if (job == null || job.trim().isEmpty()) {
            throw new IllegalArgumentException("Job cannot be empty");
        }

        this.job = job; // Set the job of the client
    }
    public void addLoan(Loan loan) {
        loans.add(loan); // Add the loan to the list of loans
    }
    public void removeWaitingLoan(WaitingLoan waitingLoan) {
        waitingLoans.remove(waitingLoan); // Remove the waiting loan from the list
    }
    public void transferLoan(Loan loan, Client recipient) {
        // Check if the recipient exists in the system
        if (recipient == null || !Client.getClients().contains(recipient)) {
            throw new IllegalArgumentException("Recipient does not exist");
        }

        // Proceed with the loan transfer if recipient exists
        if (loans.contains(loan)) {
            loans.remove(loan);
            recipient.addLoan(loan);  // Transfer the loan to the recipient
        } else {
            throw new IllegalArgumentException("Loan not found in client's loan list");
        }
    }

}