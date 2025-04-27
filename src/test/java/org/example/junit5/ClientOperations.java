package org.example.junit5;
import java.util.List;

import org.example.Client;
import org.example.Transaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientOperations {

    private Client client;
    private Client recipient;

    @BeforeEach
    public void setup() {
        // Initialize clients

        client = new Client("Ali", "Cairo", "client123", 500, 12349, "99988776", "Engineer");
        recipient = new Client("Osama", "Alex", "client456", 600, 67880, "4668798", "Doctor");

        // Adding clients to a list or a database if needed
        Client.addClient(client);
        Client.addClient(recipient);
    }

    @Test
    public void testDeposit() {
        // Perform deposit
        assertTrue(client.deposit(500));
        assertEquals(1000, client.getBalance());

        // Verify transaction history
        List<Transaction> transactions = client.getTransactionHistory();
        assertEquals(1, transactions.size());
        assertEquals("Deposit", transactions.get(0).getType());
        assertEquals(500, transactions.get(0).getAmount());
    }

    @Test
    public void testWithdraw() {
        // Create a client with an initial balance
        Client client = new Client("Ali", "Cairo", "password123", 500, 12345, "5551234", "Engineer");

        // Perform a valid withdrawal
        assertTrue(client.withdraw(200), "Withdrawal should be successful.");
        assertEquals(300, client.getBalance(), "Client's balance should be updated to 300 after withdrawal.");

        // Verify that the transaction history has been updated
        List<Transaction> transactions = client.getTransactionHistory();
        assertEquals(1, transactions.size(), "There should be one transaction recorded.");
        assertEquals("Withdraw", transactions.get(0).getType(), "Transaction type should be 'Withdraw'.");
        assertEquals(200, transactions.get(0).getAmount(), "Transaction amount should be 200.");

        // Attempt to withdraw more than the balance (should fail)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            client.withdraw(400);  // Client only has 300, withdrawal should fail
        });
        assertEquals("Insufficient balance for withdrawal", exception.getMessage(), "Exception message should be 'Insufficient balance for withdrawal'.");

        // Verify that the balance remains unchanged after the failed withdrawal
        assertEquals(300, client.getBalance(), "Client's balance should remain 300 after failed withdrawal.");
    }


    @Test
    public void testTransfer() {
        // Perform transfer
        assertTrue(client.transfer(recipient, 300));
        assertEquals(200, client.getBalance());
        assertEquals(900, recipient.getBalance());

        // Verify transaction history for client (sender)
        List<Transaction> clientTransactions = client.getTransactionHistory();
        assertEquals(1, clientTransactions.size());
        assertEquals("Transfer", clientTransactions.get(0).getType());
        assertEquals(300, clientTransactions.get(0).getAmount());

        // Verify transaction history for recipient
        List<Transaction> recipientTransactions = recipient.getTransactionHistory();
        assertEquals(1, recipientTransactions.size());
        assertEquals("Received", recipientTransactions.get(0).getType());  // Fix here to "Received"
        assertEquals(300, recipientTransactions.get(0).getAmount());
    }


    @Test
    public void testTransactionHistory() {
        // Perform transactions
        client.deposit(500);
        client.withdraw(300);
        client.transfer(recipient, 200);

        // Verify transaction history
        List<Transaction> transactions = client.getTransactionHistory();
        assertEquals(3, transactions.size());

        Transaction t1 = transactions.get(0);
        assertEquals("Deposit", t1.getType());
        assertEquals(500, t1.getAmount());

        Transaction t2 = transactions.get(1);
        assertEquals("Withdraw", t2.getType());
        assertEquals(300, t2.getAmount());

        Transaction t3 = transactions.get(2);
        assertEquals("Transfer", t3.getType());
        assertEquals(200, t3.getAmount());
    }

    @Test
    void testWithdrawMoreThanBalance() {
        client.setBalance(100);
        
        // Expect an IllegalArgumentException when attempting to withdraw more than the balance
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            client.withdraw(150);  // Withdrawal exceeds the balance (100 < 150)
        });

        // Verify that the exception message matches the expected message
        assertEquals("Insufficient balance for withdrawal", exception.getMessage(), "Exception message should be 'Insufficient balance for withdrawal'");
    }

    @Test
    void testDepositNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.deposit(-20);
        });
        assertEquals("Deposit amount must be positive", exception.getMessage());
    }

    @Test
    void testValidTransferBetweenClients() {
        int transferAmount = 100;
        client.setBalance(1000);
        recipient.setBalance(500);
        // Perform the transfer
        boolean result = client.transfer(recipient, transferAmount);

        // Validate the transfer succeeded
        assertTrue(result, "Transfer should succeed");

        // Validate the sender's balance is reduced
        assertEquals(900, client.getBalance(), "Sender's balance should be reduced by the transfer amount");

        // Validate the recipient's balance is increased
        assertEquals(600, recipient.getBalance(), "Recipient's balance should be increased by the transfer amount");

        // Validate the transaction was added to the sender's history
        assertEquals(1, client.getTransactionHistory().size(), "Sender should have one transaction in history");
        assertEquals("Transfer", client.getTransactionHistory().get(0).getType(), "Transaction type should be 'Transfer'");
        assertEquals(transferAmount, client.getTransactionHistory().get(0).getAmount(), "Transaction amount should match");

        // Validate the transaction was added to the recipient's history
        assertEquals(1, recipient.getTransactionHistory().size(), "Recipient should have one transaction in history");
        assertEquals("Received", recipient.getTransactionHistory().get(0).getType(), "Transaction type should be 'Received'");
        assertEquals(transferAmount, recipient.getTransactionHistory().get(0).getAmount(), "Transaction amount should match");
    }

    @Test
    public void testWithdrawExactBalance() {
        // Withdraw the exact balance
        assertTrue(client.withdraw(500));
        assertEquals(0, client.getBalance(), "Balance should be zero after withdrawing the exact amount");
    }

    @Test
    public void testTransferFullBalance() {
        // Transfer the full balance
        assertTrue(client.transfer(recipient, 500));
        assertEquals(0, client.getBalance(), "Sender's balance should be zero after transferring full balance");
        assertEquals(1100, recipient.getBalance(), "Recipient's balance should increase by the transferred amount");
    }

    @Test
    public void testTransferNegativeAmount() {
        // Attempt to transfer a negative amount
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.transfer(recipient, -100);
        });
        assertEquals("Transfer amount must be positive", exception.getMessage());
    }
    @Test
    public void testTransferToSelf() {
        // Attempt to transfer to the same client
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.transfer(client, 100);
        });
        assertEquals("Cannot transfer to the same account", exception.getMessage());
    }

    @Test
    public void testTransactionHistoryAfterMultipleTransfers() {
        // Perform multiple transfers
        client.transfer(recipient, 100);
        client.transfer(recipient, 200);

        // Verify transaction history
        List<Transaction> transactions = client.getTransactionHistory();
        assertEquals(2, transactions.size());

        Transaction t1 = transactions.get(0);
        assertEquals("Transfer", t1.getType());
        assertEquals(100, t1.getAmount());

        Transaction t2 = transactions.get(1);
        assertEquals("Transfer", t2.getType());
        assertEquals(200, t2.getAmount());
    }
    @Test
    public void testDepositZeroAmount() {
        // Attempt to deposit zero
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.deposit(0);
        });
        assertEquals("Deposit amount must be positive", exception.getMessage());
    }

    @Test
    public void testWithdrawZeroAmount() {
        // Attempt to withdraw zero and expect an exception to be thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            client.withdraw(0);  // Withdrawal of zero, should throw exception
        });

        // Verify the exception message is as expected
        assertEquals("Withdrawal amount must be positive", exception.getMessage(), "Exception message should be 'Withdrawal amount must be positive'");
    }


    @Test
    public void testClientCreationWithEmptyName() {
        // Attempt to create a client with an empty name
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client("", "Cairo", "client123", 500, 12349, "99988776", "Engineer");
        });
        assertEquals("Name cannot be empty", exception.getMessage());
    }
    @Test
    public void testClientCreationWithNegativeBalance() {
        // Attempt to create a client with a negative balance
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Client("Ali", "Cairo", "client123", -500, 12349, "99988776", "Engineer");
        });
        assertEquals("Balance cannot be negative", exception.getMessage());
    }

    @Test
    public void testClientToStringIncludesDetails() {
        // Verify that the toString method includes all client details
        String clientString = client.toString();
        assertTrue(clientString.contains("Ali"));
        assertTrue(clientString.contains("Cairo"));
        assertTrue(clientString.contains("Engineer"));

    }

    @Test
    public void testTransactionHistoryIsImmutable() {
        // Verify that the transaction history list is immutable
        client.deposit(100);
        List<Transaction> transactions = client.getTransactionHistory();
        assertThrows(UnsupportedOperationException.class, () -> {
            transactions.add(new Transaction("Deposit", 100, null));
        });
    }
    @Test
    public void testTransferToNonExistentClient() {
        // Attempt to transfer to a non-existent client
        Client nonExistentClient = new Client("NonExistent", "City", "pass", 0, 99999, "00000000", "None");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            client.transfer(nonExistentClient, 100);
        });
        assertEquals("Recipient does not exist in the system", exception.getMessage());
    }

    @Test
    public void testClientEquality() {
        // Verify that two clients with the same details are considered equal
        Client duplicateClient = new Client("Ali", "Cairo", "client123", 500, 12349, "99988776", "Engineer");
        assertTrue(client.equals(duplicateClient));
    }

    @Test
    public void testClientInequality() {
        // Verify that two clients with different details are not considered equal
        Client differentClient = new Client("Ali", "Cairo", "client123", 600, 12350, "99988776", "Engineer");
        assertFalse(client.equals(differentClient));
    }
}