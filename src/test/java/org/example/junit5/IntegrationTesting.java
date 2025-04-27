package org.example.junit5;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;
import org.example.Admin;
import org.example.Client;
import org.example.Loan;
import org.example.WaitingLoan;
import org.example.Transaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IntegrationTesting {

    private Admin admin;
    private Client client;
    private Client recipient;

    @BeforeEach
    public void setUp() {
        // Clear the data before each test
        Admin.clearAdmin();
        Client.clearClients();

        // Initialize Admin and Clients
        admin = new Admin("Omar", "Alex", "admin456", 200, 67890, "Senior Admin");
        client = new Client("Osama", "Alex", "client456", 600, 67880, "4668798", "Doctor");
        recipient = new Client("Ali", "Cairo", "client123", 500, 12349, "99988776", "Engineer");

        // Adding Admin and Clients
        Admin.addAdmin1(admin);
        Client.addClient(client);
        Client.addClient(recipient);
    }

    // Test for Admin Adding and Managing Client Loans
    @Test
    public void testAdminAddClientAndManageLoan() {
        // Client requests a loan
        WaitingLoan waitingLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(waitingLoan);

        // Verify that the loan is in the waiting list
        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertEquals(1, waitingLoans.size());

        // Admin approves the loan
        admin.acceptLoan(client, waitingLoan);

        // Verify that the loan has been approved and moved from waitingLoans to loans
        List<Loan> loans = client.getLoans();
        assertEquals(1, loans.size());
        Loan approvedLoan = loans.get(0);
        assertEquals(1000, approvedLoan.getLoanAmount());

        // Verify the waitingLoans list is empty
        assertTrue(client.getWaitingLoans().isEmpty());
    }

    // Test for Client Transfer Loan to Another Client
    @Test
    public void testClientLoanTransfer() {
        // Client adds a loan
        Loan loan = new Loan(5000, 10, new java.util.Date(), client.getAccountNumber());
        client.addLoan(loan);

        // Verify that the loan was added to the client's list
        assertTrue(client.getLoans().contains(loan));

        // Client transfers the loan to another client
        client.transferLoan(loan, recipient);

        // Verify the loan is no longer with the sender client and is now with the recipient client
        assertFalse(client.getLoans().contains(loan));
        assertTrue(recipient.getLoans().contains(loan));
    }

    // Test for Client Making Multiple Transactions (Deposit, Withdraw, Transfer)
    @Test
    public void testClientMultipleTransactions() {
        // Step 1: Set up clients
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Client recipient = new Client("Sara", "Alexandria", "password123", 2000, 67890, "5556789", "Doctor");

        // Add clients to the system
        Client.addClient(client);
        Client.addClient(recipient);

        // Step 2: Perform multiple transactions
        client.deposit(500);  // Deposit 500
        client.withdraw(200); // Withdraw 200
        client.transfer(recipient, 100);  // Transfer 100 to recipient

        // Step 3: Verify transaction history for client
        List<Transaction> clientTransactions = client.getTransactionHistory();
        assertEquals(3, clientTransactions.size(), "There should be 3 transactions recorded for the client.");

        // Verify each transaction's details for client
        assertEquals("Deposit", clientTransactions.get(0).getType(), "Transaction type should be 'Deposit'.");
        assertEquals(500, clientTransactions.get(0).getAmount(), "Transaction amount should be 500 for Deposit.");
        assertEquals("Withdraw", clientTransactions.get(1).getType(), "Transaction type should be 'Withdraw'.");
        assertEquals(200, clientTransactions.get(1).getAmount(), "Transaction amount should be 200 for Withdraw.");
        assertEquals("Transfer", clientTransactions.get(2).getType(), "Transaction type should be 'Transfer'.");
        assertEquals(100, clientTransactions.get(2).getAmount(), "Transaction amount should be 100 for Transfer.");
        assertEquals(Integer.valueOf(recipient.getAccountNumber()), clientTransactions.get(2).getTargetAccount(), "Target account for Transfer should be the recipient's account.");

        // Step 4: Verify transaction history for recipient
        List<Transaction> recipientTransactions = recipient.getTransactionHistory();
        assertEquals(1, recipientTransactions.size(), "There should be 1 transaction recorded for the recipient.");

        // Verify recipient's transfer transaction
        assertEquals("Received", recipientTransactions.get(0).getType(), "Transaction type should be 'Received' for recipient.");
        assertEquals(100, recipientTransactions.get(0).getAmount(), "Transaction amount should be 100 for Received.");
        assertEquals(Integer.valueOf(client.getAccountNumber()), recipientTransactions.get(0).getTargetAccount(), "Sender account should be the client's account.");
    }



    // Test for Client Requesting and Admin Rejecting Loan
    @Test
    public void testAdminRejectLoan() {
        // Client requests a loan
        WaitingLoan requestedLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(requestedLoan);

        // Verify that the loan is in waiting loans
        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertEquals(1, waitingLoans.size());

        // Admin rejects the loan
        admin.rejectLoan(client, requestedLoan);

        // Verify that the loan has been removed from the waiting list
        assertTrue(client.getWaitingLoans().isEmpty());

        // Verify that the client has no approved loans
        assertTrue(client.getLoans().isEmpty());
    }

    // Test for Loan Approval Based on Amount
    @Test
    public void testLoanApprovalBasedOnAmount() {
        // Client requests a loan of a high amount
        WaitingLoan highLoan = new WaitingLoan(50000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(highLoan);

        // Admin rejects the high loan
        admin.rejectLoan(client, highLoan);

        // Verify the loan is rejected
        assertTrue(client.getWaitingLoans().isEmpty());

        // Client requests a smaller loan
        WaitingLoan lowLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(lowLoan);

        // Admin approves the low loan
        admin.acceptLoan(client, lowLoan);

        // Verify the loan was approved
        assertEquals(1, client.getLoans().size());
        assertEquals(1000, client.getLoans().get(0).getLoanAmount());
    }

    // Test for Transfer Loan to Non-Existent Client
    @Test
    public void testLoanTransferToNonExistentClient() {
        // Create a loan and add to client
        Loan loan = new Loan(5000, 10, new java.util.Date(), client.getAccountNumber());
        client.addLoan(loan);

        // Create a non-existent client
        Client nonExistentClient = new Client("NonExistent", "City", "pass", 0, 99999, "00000000", "None");

        // Attempt to transfer loan to non-existent client
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.transferLoan(loan, nonExistentClient);
        });
        assertEquals("Recipient does not exist", exception.getMessage());
    }

    @Test
    public void testAdminAssigningRoleToClient() {
        // Assume Client has a method to update job/role (e.g., setJob)
        // Create clients and admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Alex", "admin456", 200, 67890, "Senior Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // If you have a setter like `client.setJob()` (you'd need to ensure this exists in the Client class)
        client.setJob("Manager");

        // Verify the role was updated
        assertEquals("Manager", client.getJob(), "The client's role should be updated to 'Manager'");
    }
    @Test
    public void testAdminAndClientLoanApprovalFlow() {
        // Create client and admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Alex", "admin456", 200, 67890, "Senior Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan
        WaitingLoan waitingLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(waitingLoan);

        // Verify the loan is in the waiting list
        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertEquals(1, waitingLoans.size());
        
        // Admin approves the loan
        admin.acceptLoan(client, waitingLoan);  // Assuming this method moves from waitingLoans to loans

        // Verify that the loan has been approved
        List<Loan> loans = client.getLoans();
        assertEquals(1, loans.size());
        assertEquals(1000, loans.get(0).getLoanAmount());

        // Verify that the waitingLoans list is now empty
        assertTrue(client.getWaitingLoans().isEmpty());

        // Now, reject a loan with a larger amount
        WaitingLoan highLoan = new WaitingLoan(50000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(highLoan);
        admin.rejectLoan(client, highLoan);  // Assuming this method removes it from waitingLoans

        // Verify that the loan has been removed from the waiting list
        assertTrue(client.getWaitingLoans().isEmpty());

        // Verify that the client has no approved loans for the rejected loan
        List<Loan> finalLoans = client.getLoans();
        assertEquals(1, finalLoans.size());
        assertEquals(1000, finalLoans.get(0).getLoanAmount());
    }
    @Test
    public void testAdminRejectLoanWithDifferentAmounts() {
        // Create client and admin
        Client client = new Client("Sara", "Cairo", "password456", 10000, 23456, "5559876", "Doctor");
        Admin admin = new Admin("Ahmed", "Alex", "admin789", 300, 12345, "Senior Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan of a large amount
        WaitingLoan largeLoan = new WaitingLoan(100000, 10, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(largeLoan);

        // Admin rejects the large loan
        admin.rejectLoan(client, largeLoan);

        // Verify that the loan was rejected
        assertTrue(client.getWaitingLoans().isEmpty());

        // Now, client requests a smaller loan
        WaitingLoan smallLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(smallLoan);

        // Admin approves the small loan
        admin.acceptLoan(client, smallLoan);

        // Verify that the loan was approved
        assertEquals(1, client.getLoans().size());
        assertEquals(1000, client.getLoans().get(0).getLoanAmount());
    }
    @Test
    public void testCompleteLoanApprovalFlow() {
        // Create Client and Admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Ahmed", "Alex", "admin123", 200, 67890, "Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan (waiting for approval)
        WaitingLoan loanRequest = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Verify that the loan request is in the system's waiting loans
        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertEquals(1, waitingLoans.size(), "Client's loan request should be in waiting list.");

        // Admin approves the loan
        admin.acceptLoan(client, loanRequest);

        // After approval, verify that:
        // 1. The loan is moved to the approved list
        List<Loan> approvedLoans = client.getLoans();
        assertEquals(1, approvedLoans.size(), "Client should have one approved loan.");
        assertEquals(1000, approvedLoans.get(0).getLoanAmount(), "The approved loan amount should be correct.");

        // 2. The waiting loans list should be empty after approval
        assertTrue(client.getWaitingLoans().isEmpty(), "The waiting loans list should be empty after approval.");

        // 3. Verify the admin's actions are reflected correctly in system state.
        assertTrue(Admin.getAdmins().contains(admin), "Admin should exist in the system.");

        // Simulate a real-world interaction: client decides to request a new loan
        WaitingLoan secondLoan = new WaitingLoan(500, 3, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(secondLoan);
        assertEquals(1, client.getWaitingLoans().size(), "Client should have one loan in waiting list.");

        // Admin approves the new loan
        admin.acceptLoan(client, secondLoan);

        // Verify the new loan is approved
        List<Loan> allLoans = client.getLoans();
        assertEquals(2, allLoans.size(), "Client should have two approved loans.");
        assertEquals(500, allLoans.get(1).getLoanAmount(), "The second loan amount should be correct.");
    }
    @Test
    public void testLoanRejectionFlow() {
        // Create Client and Admin
        Client client = new Client("Ahmed", "Cairo", "password123", 3000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Alex", "admin123", 200, 67890, "Senior Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan
        WaitingLoan loanRequest = new WaitingLoan(2000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Admin rejects the loan request
        admin.rejectLoan(client, loanRequest);

        // Verify that the loan has been removed from the waiting list
        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertTrue(waitingLoans.isEmpty(), "The waiting loans list should be empty after loan rejection.");
        
        // Verify no approved loans exist
        List<Loan> loans = client.getLoans();
        assertTrue(loans.isEmpty(), "There should be no approved loans for the client.");
    }
    @Test
    public void testAdminApprovesLoanAndSystemReflectsChanges() {
        // Create a Client and Admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Alex", "admin456", 200, 67890, "Senior Admin");

        // Add Client and Admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan
        WaitingLoan loanRequest = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Admin approves the loan
        admin.acceptLoan(client, loanRequest);

        // Verify that the loan has been moved from waitingLoans to loans
        List<Loan> loans = client.getLoans();
        assertEquals(1, loans.size(), "Client should have one approved loan.");
        assertEquals(1000, loans.get(0).getLoanAmount(), "Loan amount for Client should be correct.");

        // Verify that the waitingLoans list is now empty (the loan should be removed from waitingLoans)
        assertTrue(client.getWaitingLoans().isEmpty(), "Waiting loans list should be empty after approval.");

        // Integration Check: Verify that the system reflects the loan status in the loan list of the client
        verifyLoanInClientLoans(client, 1000); // Verify the loan exists in the client's loan list
    }

    // Verification method for the loan in the Client's loans list
    private void verifyLoanInClientLoans(Client client, int loanAmount) {
        boolean loanExists = client.getLoans().stream()
                .anyMatch(loan -> loan.getLoanAmount() == loanAmount);
        assertTrue(loanExists, "Loan with amount " + loanAmount + " should exist in the client's loan list.");
    }
    @Test
    public void testLoanTransferBetweenClients() {
        // Create Clients and Admin
        Client client1 = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Client client2 = new Client("Osama", "Alex", "password456", 3000, 67890, "5556789", "Doctor");
        Admin admin = new Admin("Ahmed", "Cairo", "admin123", 200, 67890, "Admin");

        // Add Clients and Admin to the system
        Client.addClient(client1);
        Client.addClient(client2);
        Admin.addAdmin1(admin);

        // Client 1 requests a loan
        WaitingLoan waitingLoan = new WaitingLoan(3000, 5, new java.util.Date(), client1.getAccountNumber());
        client1.addWaitingLoan(waitingLoan);

        // Admin approves the loan
        admin.acceptLoan(client1, waitingLoan);

        // Now that the loan is approved, we can verify that the loan is added to the client's list
        List<Loan> loansClient1 = client1.getLoans();
        assertEquals(1, loansClient1.size(), "Client 1 should have one approved loan.");
        Loan approvedLoan = loansClient1.get(0);
        assertEquals(3000, approvedLoan.getLoanAmount(), "Loan amount for Client 1 should be correct.");

        // Client 1 transfers the loan to Client 2
        client1.transferLoan(approvedLoan, client2);

        // Verify that the loan has been transferred
        List<Loan> loansClient2 = client2.getLoans();
        assertEquals(1, loansClient2.size(), "Client 2 should now have the transferred loan.");
        assertTrue(loansClient2.contains(approvedLoan), "Loan should be transferred to Client 2.");
        
        // Verify that Client 1 no longer has the loan
        assertFalse(client1.getLoans().contains(approvedLoan), "Client 1 should no longer have the loan.");
    }
    @Test
    public void testMultipleLoanRequestsAndAdminReactions() {
        // Create Clients and Admin
        Client client1 = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Client client2 = new Client("Sara", "Alex", "password456", 3000, 12346, "5556789", "Doctor");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add clients and admin to the system
        Client.addClient(client1);
        Client.addClient(client2);
        Admin.addAdmin1(admin);

        // Clients request loans
        WaitingLoan loanRequest1 = new WaitingLoan(5000, 5, new java.util.Date(), client1.getAccountNumber());
        WaitingLoan loanRequest2 = new WaitingLoan(10000, 5, new java.util.Date(), client2.getAccountNumber());

        client1.addWaitingLoan(loanRequest1);
        client2.addWaitingLoan(loanRequest2);

        // Admin approves client 1’s loan and rejects client 2’s loan
        admin.acceptLoan(client1, loanRequest1);
        admin.rejectLoan(client2, loanRequest2);

        // Verify that client 1’s loan was approved
        assertEquals(1, client1.getLoans().size(), "Client 1 should have an approved loan.");
        assertTrue(client1.getLoans().get(0).getLoanAmount() == 5000, "Loan amount for Client 1 should be correct.");

        // Verify that client 2’s loan was rejected
        assertTrue(client2.getWaitingLoans().isEmpty(), "Client 2's loan should be removed from waiting list.");
    }
    @Test
    public void testClientMakesDepositAfterLoanApprovalAndTransactionRecorded() {
        // Create Clients and Admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan
        WaitingLoan loanRequest = new WaitingLoan(2000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Admin approves the loan
        admin.acceptLoan(client, loanRequest);

        // Verify that the loan was approved and balance is updated
        List<Loan> clientLoans = client.getLoans();
        assertEquals(1, clientLoans.size(), "Client should have one approved loan.");
        assertEquals(2000, clientLoans.get(0).getLoanAmount(), "Loan amount should be correct.");
        assertEquals(7000, client.getBalance(), "Client's balance should reflect the loan amount after approval.");

        // Client deposits the loan amount
        boolean depositSuccess = client.deposit(2000);

        // Verify that the client’s balance is updated after deposit
        assertTrue(depositSuccess, "Deposit should be successful.");
        assertEquals(9000, client.getBalance(), "Client's balance should be updated after deposit.");

        // Verify that a transaction was recorded
        List<Transaction> transactions = client.getTransactionHistory();
        System.out.println("Transaction History: " + transactions.size());  // Check history size
        assertEquals(1, transactions.size(), "Only one transaction should be recorded (Deposit).");
        assertEquals("Deposit", transactions.get(0).getType(), "Transaction type should be 'Deposit'.");
        assertEquals(2000, transactions.get(0).getAmount(), "Transaction amount should match the deposit.");
    }


    @Test
    public void testLoanApprovalAndBalanceUpdate() {
        // Step 1: Create Clients and Admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Step 2: Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Step 3: Client requests a loan of 1000
        WaitingLoan loanRequest = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Step 4: Admin approves the loan
        admin.acceptLoan(client, loanRequest);

        // Step 5: Verify that the loan is approved and added to the client's loan list
        List<Loan> clientLoans = client.getLoans();
        assertEquals(1, clientLoans.size(), "Client should have one approved loan.");
        assertEquals(1000, clientLoans.get(0).getLoanAmount(), "Loan amount should be 1000.");

        // Step 6: Verify that the client's balance is updated with the loan amount
        assertEquals(6000, client.getBalance(), "Client's balance should be updated to 6000 after loan approval.");
    }



    @Test
    public void testClientTransfersLoanToAnotherClientAfterApproval() {
        // Create Clients and Admin
        Client client1 = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Client client2 = new Client("Sara", "Alex", "password456", 3000, 12346, "5556789", "Doctor");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add clients and admin to the system
        Client.addClient(client1);
        Client.addClient(client2);
        Admin.addAdmin1(admin);

        // Client 1 requests a loan
        WaitingLoan loanRequest = new WaitingLoan(3000, 5, new java.util.Date(), client1.getAccountNumber());
        client1.addWaitingLoan(loanRequest);

        // Admin approves the loan
        admin.acceptLoan(client1, loanRequest);

        // Verify that the loan is approved
        List<Loan> client1Loans = client1.getLoans();
        assertEquals(1, client1Loans.size(), "Client 1 should have one approved loan.");

        // Assuming the loan is now approved and converted from WaitingLoan to Loan
        Loan approvedLoan = client1Loans.get(0); 

        // Client 1 transfers the loan to Client 2
        client1.transferLoan(approvedLoan, client2);  // Only Loan, not WaitingLoan

        // Verify that the loan was transferred to Client 2
        assertTrue(client2.getLoans().contains(approvedLoan), "Client 2 should have the loan.");
        assertFalse(client1.getLoans().contains(approvedLoan), "Client 1 should no longer have the loan.");
    }
    @Test
    public void testAdminRejectsHighLoanAndApprovesSmallerLoan() {
        // Create Clients and Admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a high loan
        WaitingLoan highLoan = new WaitingLoan(50000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(highLoan);

        // Admin rejects the high loan
        admin.rejectLoan(client, highLoan);

        // Verify that the high loan was rejected
        assertTrue(client.getWaitingLoans().isEmpty(), "Client's waiting loans should be empty after rejection.");

        // Client requests a smaller loan
        WaitingLoan lowLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(lowLoan);

        // Admin approves the low loan
        admin.acceptLoan(client, lowLoan);

        // Verify that the smaller loan was approved
        assertEquals(1, client.getLoans().size(), "Client should have one approved loan.");
        assertEquals(1000, client.getLoans().get(0).getLoanAmount(), "Loan amount should be 1000.");
    }
    @Test
    public void testLoanRequestAdminApprovalAndWithdraw() {
        // Create Clients and Admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan of 2000
        WaitingLoan loanRequest = new WaitingLoan(2000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Admin approves the loan
        admin.acceptLoan(client, loanRequest);

        // Verify that the loan is approved and the balance is updated
        List<Loan> clientLoans = client.getLoans();
        assertEquals(1, clientLoans.size(), "Client should have one approved loan.");
        assertEquals(2000, clientLoans.get(0).getLoanAmount(), "Loan amount should be correct.");
        assertEquals(7000, client.getBalance(), "Client's balance should reflect the loan amount after approval.");

        // Client withdraws from the loan
        boolean withdrawalSuccess = client.withdraw(1500);
        assertTrue(withdrawalSuccess, "Withdrawal should be successful.");
        assertEquals(5500, client.getBalance(), "Client's balance should reflect the withdrawal.");
    }

    @Test
    public void testClientLoginAndDeposit() {
        // Step 1: Create a Client
        Client client = new Client("Ali", "Cairo", "password123", 1000, 12345, "5551234", "Engineer");

        // Add client to the system
        Client.addClient(client);

        // Step 2: Client logs in with correct credentials
        boolean loginSuccess = client.login("Ali", "password123");
        assertTrue(loginSuccess, "Client should be able to log in with correct credentials.");

        // Step 3: Verify the client's initial balance
        assertEquals(1000, client.getBalance(), "Client's initial balance should be 1000.");

        // Step 4: Client deposits 500 into their account
        boolean depositSuccess = client.deposit(500);
        assertTrue(depositSuccess, "Deposit should be successful.");

        // Step 5: Verify the client's balance after deposit
        assertEquals(1500, client.getBalance(), "Client's balance should be updated to 1500 after deposit.");
    }



}
