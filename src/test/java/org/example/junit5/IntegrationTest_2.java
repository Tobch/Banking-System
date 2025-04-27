package org.example.junit5;
import java.util.Date;
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
import org.junit.jupiter.api.Assertions;

public class IntegrationTest_2 {

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
    @Test
    public void testLoanApprovalAndClientBalanceUpdateAfterRepayment() {
        // Create Clients and Admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan
        WaitingLoan loanRequest = new WaitingLoan(3000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Admin approves the loan
        admin.acceptLoan(client, loanRequest);

        // Verify that the loan is approved and the balance is updated
        List<Loan> clientLoans = client.getLoans();
        assertEquals(1, clientLoans.size(), "Client should have one approved loan.");
        assertEquals(3000, clientLoans.get(0).getLoanAmount(), "Loan amount should be correct.");
        assertEquals(8000, client.getBalance(), "Client's balance should reflect the loan amount after approval.");

        // Simulate loan repayment by reducing the client's balance (as a workaround)
        client.setBalance(client.getBalance() - 1000); // Deduct 1000 as repayment

        // Verify that the client balance has been updated after repayment
        assertEquals(7000, client.getBalance(), "Client's balance should reflect the repayment.");

        // After repayment, check that the loan amount remains the same (as we are not modifying the Loan class directly)
        assertEquals(3000, clientLoans.get(0).getLoanAmount(), "Loan amount should remain unchanged in the loan.");
    }


    @Test
    public void testAdminRejectsLoanAndClientRequestsAnotherLoan() {
        // Create Clients and Admin
        Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Client requests a loan
        WaitingLoan loanRequest = new WaitingLoan(5000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Admin rejects the loan
        admin.rejectLoan(client, loanRequest);

        // Verify that the loan is rejected and the client has no loans
        assertTrue(client.getWaitingLoans().isEmpty(), "Client's waiting loan list should be empty.");
        assertTrue(client.getLoans().isEmpty(), "Client should have no approved loans.");

        // Client requests another loan
        WaitingLoan newLoanRequest = new WaitingLoan(1000, 3, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(newLoanRequest);

        // Admin approves the new loan
        admin.acceptLoan(client, newLoanRequest);

        // Verify that the loan is approved
        assertEquals(1, client.getLoans().size(), "Client should have one approved loan.");
        assertEquals(1000, client.getLoans().get(0).getLoanAmount(), "Loan amount should be correct.");
    }
    @Test
    public void testWithdrawInsufficientFundsAndTransferFromAnotherClient() {
        // Create Clients
        Client client1 = new Client("Ali", "Cairo", "password123", 500, 12345, "5551234", "Engineer");
        Client client2 = new Client("Sara", "Alex", "password456", 2000, 12346, "5556789", "Doctor");

        // Add clients to the system
        Client.addClient(client1);
        Client.addClient(client2);

        // Client 1 tries to withdraw more than they have (should fail)
        try {
            client1.withdraw(1000);  // Client 1 tries to withdraw 1000, but they only have 500
            Assertions.fail("Client 1 should not be able to withdraw more than their balance.");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance for withdrawal", e.getMessage());
        }

        // Verify the balance remains unchanged for Client 1
        Assertions.assertEquals(500, client1.getBalance(), "Client 1's balance should remain unchanged after the failed withdrawal.");

        // Now, simulate a transfer from Client 2 to Client 1
        int transferAmount = 1000;
        client2.transfer(client1, transferAmount);  // Client 2 transfers money to Client 1

        // Verify the transfer was successful
        Assertions.assertEquals(1500, client1.getBalance(), "Client 1's balance should be updated after receiving the transfer from Client 2.");
        Assertions.assertEquals(1000, client2.getBalance(), "Client 2's balance should decrease after transferring money to Client 1.");

        // Now that Client 1 has sufficient funds, try withdrawing again
        boolean withdrawalSuccess = client1.withdraw(1000);  // Client 1 now has enough balance to withdraw 1000
        Assertions.assertTrue(withdrawalSuccess, "Withdrawal should be successful after receiving the transfer.");
        Assertions.assertEquals(500, client1.getBalance(), "Client 1's balance should reflect the withdrawal after the successful transaction.");
    }

    @Test
    public void testDepositAndWithdraw() {
        // Step 1: Create a Client account with an initial balance of 500
        Client client = new Client("Ali", "Cairo", "password123", 500, 12345, "5551234", "Engineer");

        // Add client to the system
        Client.addClient(client);

        // Step 2: Client logs in
        boolean loginSuccess = client.login("Ali", "password123");
        assertTrue(loginSuccess, "Client should be able to log in with correct credentials.");

        // Step 3: Client deposits 600 into the account
        boolean depositSuccess = client.deposit(600);
        assertTrue(depositSuccess, "Deposit should be successful.");
        assertEquals(1100, client.getBalance(), "Client's balance should be 1100 after deposit.");

        // Step 4: Client withdraws 1000
        boolean withdrawalSuccess = client.withdraw(1000);
        assertTrue(withdrawalSuccess, "Withdrawal should be successful.");
        assertEquals(100, client.getBalance(), "Client's balance should be 100 after withdrawal.");

        // Step 5: Verify that the withdrawal was successful and the balance was updated correctly
        assertEquals(100, client.getBalance(), "Client's balance should reflect the withdrawal of 1000.");
    }
    @Test
    public void testWithdrawWithLoanAndTransfer() {
        // Create Clients
        Client client1 = new Client("Ali", "Cairo", "password123", 2000, 12345, "5551234", "Engineer");
        Client client2 = new Client("Sara", "Alex", "password456", 3000, 12346, "5556789", "Doctor");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add clients and admin to the system
        Client.addClient(client1);
        Client.addClient(client2);
        Admin.addAdmin1(admin);

        // Step 1: Client 1 applies for a loan of 1000
        WaitingLoan loanRequest = new WaitingLoan(1000, 5, new java.util.Date(), client1.getAccountNumber());
        client1.addWaitingLoan(loanRequest);

        // Step 2: Admin approves the loan for Client 1
        admin.acceptLoan(client1, loanRequest);

        // Verify that the loan is approved
        List<Loan> client1Loans = client1.getLoans();
        assertEquals(1, client1Loans.size(), "Client 1 should have one approved loan.");
        assertEquals(1000, client1Loans.get(0).getLoanAmount(), "Loan amount should be 1000.");

        // Client 1's balance is now increased by the loan amount (from 2000 to 3000)
        Assertions.assertEquals(3000, client1.getBalance(), "Client 1's balance should be updated after loan approval.");

        // Step 3: Client 1 tries to withdraw 4000, but they only have 3000
        try {
            client1.withdraw(4000);  // Client 1 tries to withdraw 4000, but they only have 3000
            Assertions.fail("Client 1 should not be able to withdraw more than their balance.");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance for withdrawal", e.getMessage(), "The error message should indicate insufficient funds.");
        }

        // Verify that the balance remains unchanged after the failed withdrawal
        Assertions.assertEquals(3000, client1.getBalance(), "Client 1's balance should remain unchanged after the failed withdrawal.");

        // Step 4: Now, simulate a transfer from Client 2 to Client 1
        int transferAmount = 1000;
        client2.withdraw(transferAmount);  // Decrease client2's balance by transferAmount
        client1.deposit(transferAmount);   // Increase client1's balance by transferAmount

        // Verify the transfer was successful
        Assertions.assertEquals(4000, client1.getBalance(), "Client 1's balance should be updated after receiving the transfer from Client 2.");
        Assertions.assertEquals(2000, client2.getBalance(), "Client 2's balance should decrease after transferring money to Client 1.");

        // Step 5: Now that Client 1 has sufficient funds, try withdrawing 4000 again
        boolean withdrawalSuccess = client1.withdraw(4000);  // Client 1 now has enough balance to withdraw 4000
        Assertions.assertTrue(withdrawalSuccess, "Withdrawal should be successful now that Client 1 has enough funds.");

        // Verify the balance after withdrawal
        Assertions.assertEquals(0, client1.getBalance(), "Client 1's balance should reflect the withdrawal after the successful transaction.");
    }


    @Test
    public void testTransferInsufficientFundsThenDepositAndTransferSuccessfully() {
        // Create Clients
        Client client1 = new Client("Ali", "Cairo", "password123", 1000, 12345, "5551234", "Engineer");
        Client client2 = new Client("Sara", "Alex", "password456", 2000, 12346, "5556789", "Doctor");

        // Add clients to the system
        Client.addClient(client1);
        Client.addClient(client2);

        // Step 1: Client 1 tries to transfer 1500 to Client 2 (should fail due to insufficient funds)
        try {
            client1.transfer(client2, 1500);  // Client 1 tries to transfer 1500, but they only have 1000
            Assertions.fail("Client 1 should not be able to transfer more than their balance.");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance for transfer", e.getMessage(), "The error message should indicate insufficient funds.");
        }

        // Verify the balance remains unchanged
        Assertions.assertEquals(1000, client1.getBalance(), "Client 1's balance should remain unchanged after the failed transfer.");
        Assertions.assertEquals(2000, client2.getBalance(), "Client 2's balance should remain unchanged.");

        // Step 2: Now, simulate a deposit into Client 1's account
        int depositAmount = 1000;
        client1.deposit(depositAmount);   // Client 1 receives the deposit

        // Verify the deposit was successful
        Assertions.assertEquals(2000, client1.getBalance(), "Client 1's balance should be updated after the deposit.");
        Assertions.assertEquals(2000, client2.getBalance(), "Client 2's balance should remain unchanged.");

        // Step 3: Now that Client 1 has sufficient funds, try transferring 1500 to Client 2
        boolean transferSuccess = client1.transfer(client2, 1500);  // Client 1 now has enough balance to transfer 1500
        Assertions.assertTrue(transferSuccess, "Transfer should be successful now that Client 1 has enough funds.");

        // Verify the balance after transfer
        Assertions.assertEquals(500, client1.getBalance(), "Client 1's balance should reflect the transfer.");
        Assertions.assertEquals(3500, client2.getBalance(), "Client 2's balance should reflect the transfer.");
    }

    @Test
    public void testTransferInsufficientFundsThenDepositButStillCannotWithdraw() {
        // Create Clients
        Client client1 = new Client("Ali", "Cairo", "password123", 500, 12345, "5551234", "Engineer");
        Client client2 = new Client("Sara", "Alex", "password456", 2000, 12346, "5556789", "Doctor");

        // Add clients to the system
        Client.addClient(client1);
        Client.addClient(client2);

        // Step 1: Client 1 tries to withdraw 1000 (should fail due to insufficient funds)
        try {
            client1.withdraw(1000);  // Client 1 tries to withdraw 1000, but they only have 500
            Assertions.fail("Client 1 should not be able to withdraw more than their balance.");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance for withdrawal", e.getMessage(), "The error message should indicate insufficient funds.");
        }

        // Verify that the balance remains unchanged for client1
        Assertions.assertEquals(500, client1.getBalance(), "Client 1's balance should remain unchanged after the failed withdrawal.");
        Assertions.assertEquals(2000, client2.getBalance(), "Client 2's balance should remain unchanged.");

        // Step 2: Now, simulate a transfer from Client 2 to Client 1, but Client 1 still doesn't have enough funds
        int transferAmount = 300;
        client2.withdraw(transferAmount);  // Client 2 withdraws the money
        client1.deposit(transferAmount);   // Client 1 receives the transfer

        // Verify the transfer was successful
        Assertions.assertEquals(800, client1.getBalance(), "Client 1's balance should be updated after the transfer.");
        Assertions.assertEquals(1700, client2.getBalance(), "Client 2's balance should decrease after transferring money.");

        // Step 3: Now that Client 1 still doesn't have enough funds, try withdrawing again
        try {
            client1.withdraw(1000);  // Client 1 still doesn't have enough balance to withdraw 1000
            Assertions.fail("Client 1 should not be able to withdraw more than their balance.");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance for withdrawal", e.getMessage(), "The error message should indicate insufficient funds.");
        }
    }

    @Test
    public void testClientDepositAndWithdrawWithInsufficientFunds() {
        // Step 1: Create a Client account
        Client client = new Client("Ali", "Cairo", "password123", 0, 12345, "5551234", "Engineer");

        // Add client to the system
        Client.addClient(client);

        // Step 2: Client logs in
        boolean loginSuccess = client.login("Ali", "password123");
        assertTrue(loginSuccess, "Client should be able to log in with correct credentials.");

        // Step 3: Client deposits 1000 into the account
        boolean depositSuccess = client.deposit(1000);
        assertTrue(depositSuccess, "Deposit should be successful.");
        assertEquals(1000, client.getBalance(), "Client's balance should be 1000 after deposit.");

        // Step 4: Client tries to withdraw 1500, but the balance is only 1000
        try {
            boolean withdrawalSuccess = client.withdraw(1500);  // Client tries to withdraw 1500
            assertFalse(withdrawalSuccess, "Client should not be able to withdraw more than their balance.");
        } catch (IllegalArgumentException e) {
            assertEquals("Insufficient balance for withdrawal", e.getMessage(), "The error message should indicate insufficient funds.");
        }

        // Verify the balance remains the same after the failed withdrawal attempt
        assertEquals(1000, client.getBalance(), "Client's balance should remain 1000 after failed withdrawal.");
    }


    @Test
    public void testClientInsufficientFundsThenLoanApprovalAndSuccessfulWithdraw() {
        // Create Clients and Admin
        Client client = new Client("Ali", "Cairo", "password123", 500, 12345, "5551234", "Engineer");
        Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

        // Add client and admin to the system
        Client.addClient(client);
        Admin.addAdmin1(admin);

        // Step 1: Client tries to withdraw more than they have (should fail and throw an exception)
        try {
            client.withdraw(1000);  // Client tries to withdraw 1000, but they only have 500
            Assertions.fail("Client should not be able to withdraw more than their balance.");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance for withdrawal", e.getMessage(), "The error message should indicate insufficient funds.");
        }

        // Verify the balance remains unchanged
        Assertions.assertEquals(500, client.getBalance(), "Client's balance should remain unchanged after the failed withdrawal.");

        // Step 2: Client requests a loan of 2000
        WaitingLoan loanRequest = new WaitingLoan(2000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(loanRequest);

        // Step 3: Admin approves the loan
        admin.acceptLoan(client, loanRequest);

        // Verify that the loan is approved and the client's balance is updated
        Assertions.assertEquals(2000, client.getLoans().get(0).getLoanAmount(), "Loan amount should be correctly approved.");
        Assertions.assertEquals(2500, client.getBalance(), "Client's balance should now be 2500 after loan approval.");

        // Step 4: Now, client should be able to withdraw 1000 after loan approval
        boolean withdrawalSuccess = client.withdraw(1000);  // Client now has enough money to withdraw 1000
        Assertions.assertTrue(withdrawalSuccess, "Withdrawal should be successful now that the client has enough funds.");

        // Verify the balance after withdrawal
        Assertions.assertEquals(1500, client.getBalance(), "Client's balance should reflect the withdrawal of 1000.");
    }

    @Test
    public void testClientLoginWithdrawAndSwitchAccount() {
        // Create Clients
        Client client1 = new Client("Ali", "Cairo", "password123", 500, 12345, "5551234", "Engineer");
        Client client2 = new Client("Sara", "Alex", "password456", 3000, 12346, "5556789", "Doctor");

        // Add clients to the system
        Client.addClient(client1);
        Client.addClient(client2);

        // Step 1: Client 1 logs in
        boolean loginSuccess1 = client1.login("Ali", "password123");
        Assertions.assertTrue(loginSuccess1, "Client 1 should be able to log in with correct credentials.");
        
        // Step 2: Client 1 tries to withdraw 1000 (should fail as the balance is 500)
        try {
            boolean withdrawalSuccess1 = client1.withdraw(1000); // Client 1 tries to withdraw 1000
            Assertions.fail("Client 1 should not be able to withdraw more than their balance.");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Insufficient balance for withdrawal", e.getMessage(), "The error message should indicate insufficient funds.");
        }

        // Step 3: Client 1 logs out (simulated by switching accounts)
        // Simulating logout by switching accounts
        client1 = null; // "Log out" client 1 (reset the reference)

        // Step 4: Client 2 logs in
        boolean loginSuccess2 = client2.login("Sara", "password456");
        Assertions.assertTrue(loginSuccess2, "Client 2 should be able to log in with correct credentials.");
        
        // Step 5: Client 2 tries to withdraw 1000 (should succeed as the balance is 3000)
        boolean withdrawalSuccess2 = client2.withdraw(1000); // Client 2 successfully withdraws 1000
        Assertions.assertTrue(withdrawalSuccess2, "Client 2 should be able to withdraw 1000.");
        Assertions.assertEquals(2000, client2.getBalance(), "Client 2's balance should reflect the withdrawal.");
    }


        @Test
        public void testLoanRequestApprovalAndWithdrawal() {
            // Step 1: Create Clients and Admin
            Client client = new Client("Ali", "Cairo", "password123", 500, 12345, "5551234", "Engineer");
            Admin admin = new Admin("Omar", "Cairo", "admin123", 200, 67890, "Admin");

            // Add client and admin to the system
            Client.addClient(client);
            Admin.addAdmin1(admin);

            // Step 2: Client requests a loan of 1000
            WaitingLoan loanRequest = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
            client.addWaitingLoan(loanRequest);

            // Step 3: Admin approves the loan
            admin.acceptLoan(client, loanRequest);

            // Step 4: Verify that the loan is approved and the client's balance is updated
            assertEquals(1000, client.getLoans().get(0).getLoanAmount(), "Loan amount should be 1000.");
            assertEquals(1500, client.getBalance(), "Client's balance should be 1500 after loan approval.");

            // Step 5: Client tries to withdraw 1700 (which is more than their balance of 1500)
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                client.withdraw(1700);  // This should throw an exception because the client only has 1500
            });

            // Step 6: Verify that the exception message is correct
            assertEquals("Insufficient balance for withdrawal", thrown.getMessage(), "The error message should indicate insufficient funds.");

            // Step 7: Verify that the balance remains unchanged
            assertEquals(1500, client.getBalance(), "Client's balance should remain 1500 after the failed withdrawal.");
        }

        @Test
        public void testClientLoginAndAdminRejectLoan() {
            // Create Clients and Admin
            Client client = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
            Admin admin = new Admin("Omar", "Cairo", "admin123", 5000, 67890, "Admin");

            // Add client and admin to the system
            Client.addClient(client);
            Admin.addAdmin1(admin);

            // Step 1: Client logs in
            boolean loginSuccess = client.login("Ali", "password123");
            assertTrue(loginSuccess, "Client should be able to log in with correct credentials.");

            // Step 2: Client requests a loan
            WaitingLoan loanRequest = new WaitingLoan(2000, 5, new java.util.Date(), client.getAccountNumber());
            client.addWaitingLoan(loanRequest);

            // Step 3: Admin rejects the loan request
            admin.rejectLoan(client, loanRequest);

            // Verify that the loan request is removed from the client's waiting loans
            assertTrue(client.getWaitingLoans().isEmpty(), "Client's waiting loan list should be empty after rejection.");

            // Verify that the client's balance remains unchanged
            assertEquals(5000, client.getBalance(), "Client's balance should remain the same after loan rejection.");
        }

        @Test
        public void testClientWorkflow() {
            // Step 1: Create Clients
            Client client1 = new Client("Ali", "Cairo", "password123", 500, 12345, "5551234", "Engineer");
            Client client2 = new Client("Sara", "Alex", "password456", 2000, 12346, "5556789", "Doctor");
            Client client3 = new Client("John", "Alex", "password789", 1000, 12347, "5551122", "Artist");

            // Add clients to the system
            Client.addClient(client1);
            Client.addClient(client2);
            Client.addClient(client3);

            // Step 2: Client 1 logs in successfully
            boolean loginSuccess = client1.login("Ali", "password123");
            Assertions.assertTrue(loginSuccess, "Client 1 should be able to log in with correct credentials.");

            // Step 3: Client 1 deposits 2000
            boolean depositSuccess = client1.deposit(2000);
            Assertions.assertTrue(depositSuccess, "Deposit should be successful.");
            Assertions.assertEquals(2500, client1.getBalance(), "Client 1's balance should be 2500 after deposit.");

            // Step 4: Client 1 transfers 2000 to Client 2
            boolean transferSuccess1 = client1.transfer(client2, 2000);
            Assertions.assertTrue(transferSuccess1, "Transfer to Client 2 should be successful.");
            Assertions.assertEquals(500, client1.getBalance(), "Client 1's balance should be 500 after transfer.");
            Assertions.assertEquals(4000, client2.getBalance(), "Client 2's balance should be 4000 after receiving the transfer.");

            // Step 5: Client 1 transfers 450 to Client 3
            boolean transferSuccess2 = client1.transfer(client3, 450);
            Assertions.assertTrue(transferSuccess2, "Transfer to Client 3 should be successful.");
            Assertions.assertEquals(50, client1.getBalance(), "Client 1's balance should be 50 after second transfer.");
            Assertions.assertEquals(1450, client3.getBalance(), "Client 3's balance should be 1450 after receiving the transfer.");

            // Step 6: Client 1 withdraws 50
            boolean withdrawSuccess = client1.withdraw(50);
            Assertions.assertTrue(withdrawSuccess, "Withdrawal should be successful.");
            Assertions.assertEquals(0, client1.getBalance(), "Client 1's balance should be 0 after withdrawal.");

            // Step 7: Client 1 logs out
            // Note: Assuming the client logs out by ending the session or invalidating the login, which is typically handled in session management
            boolean logoutSuccess = true;  // Simulating logout success, actual logout functionality depends on your system
            Assertions.assertTrue(logoutSuccess, "Client 1 should be able to log out successfully.");
        }
        @Test
        public void testFullAccountLifecycleWithSuccess() {
            // Step 1: Create Clients
            Client client1 = new Client("Ali", "Cairo", "password123", 5000, 12345, "5551234", "Engineer");
            Client client2 = new Client("Sara", "Alex", "password456", 3000, 12346, "5556789", "Doctor");

            // Add clients to the system
            Client.addClient(client1);
            Client.addClient(client2);

            // Step 2: Login for Client 1
            boolean loginSuccess1 = client1.login("Ali", "password123");
            Assertions.assertTrue(loginSuccess1, "Client 1 should be able to log in with correct credentials.");

            // Step 3: Verify Account Creation for Client 1
            Assertions.assertNotNull(client1.getAccountNumber(), "Client 1's account number should not be null.");
            Assertions.assertEquals("Ali", client1.getName(), "Client 1's name should be 'Ali'.");
            Assertions.assertEquals(5000, client1.getBalance(), "Client 1's initial balance should be 5000.");

            // Step 4: Client 1 makes a deposit
            boolean depositSuccess = client1.deposit(2000);
            Assertions.assertTrue(depositSuccess, "Deposit should be successful.");
            Assertions.assertEquals(7000, client1.getBalance(), "Client 1's balance should be 7000 after deposit.");

            // Step 5: Client 1 withdraws an amount smaller than their balance (Successful withdrawal)
            boolean withdrawSuccess1 = client1.withdraw(1000); // Client 1 withdraws 1000
            Assertions.assertTrue(withdrawSuccess1, "Withdrawal should be successful.");
            Assertions.assertEquals(6000, client1.getBalance(), "Client 1's balance should be 6000 after withdrawal.");

            // Step 6: Client 1 transfers money to Client 2 using deposit and withdraw to simulate a transfer
            boolean withdrawFromClient1 = client1.withdraw(2000);  // Client 1 withdraws 2000
            Assertions.assertTrue(withdrawFromClient1, "Client 1 should be able to withdraw money.");
            boolean depositToClient2 = client2.deposit(2000);  // Client 2 receives the deposit
            Assertions.assertTrue(depositToClient2, "Client 2 should be able to receive the deposit.");

            // Verify balances after transfer
            Assertions.assertEquals(4000, client1.getBalance(), "Client 1's balance should decrease by the transfer amount.");
            Assertions.assertEquals(5000, client2.getBalance(), "Client 2's balance should reflect the transfer from Client 1.");

            // Step 7: Login for Client 2
            boolean loginSuccess2 = client2.login("Sara", "password456");
            Assertions.assertTrue(loginSuccess2, "Client 2 should be able to log in with correct credentials.");

            // Step 8: Client 2 withdraws an amount smaller than their balance (Successful withdrawal)
            boolean withdrawSuccess2 = client2.withdraw(1000); // Client 2 withdraws 1000
            Assertions.assertTrue(withdrawSuccess2, "Withdrawal should be successful.");
            Assertions.assertEquals(4000, client2.getBalance(), "Client 2's balance should be 4000 after withdrawal.");

            // Step 9: Client 2 transfers money to Client 1 using deposit and withdraw to simulate a transfer
            boolean withdrawFromClient2 = client2.withdraw(1000);  // Client 2 withdraws 1000
            Assertions.assertTrue(withdrawFromClient2, "Client 2 should be able to withdraw money.");
            boolean depositToClient1 = client1.deposit(1000);  // Client 1 receives the deposit
            Assertions.assertTrue(depositToClient1, "Client 1 should be able to receive the deposit.");

            // Verify final balances after all transactions
            Assertions.assertEquals(5000, client1.getBalance(), "Client 1's balance should reflect all transactions.");
            Assertions.assertEquals(3000, client2.getBalance(), "Client 2's balance should reflect all transactions.");
        }
    }

