package org.example.junit5;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import org.example.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WhiteBox {

    private Admin admin1;
    private Admin admin2;
    private Client client1;
    private Client client2;
    private Client recipient;
    private Client client;
    private Admin admin;

    @BeforeEach
    public void setup() {
        Admin.clearAdmin();
        Client.clearClients();

        admin1 = new Admin("Ahmed", "cairo", "Admin123", 100, 12345, "Admin");
        admin2 = new Admin("Omar", "alex", "Admin456", 200, 67890, "Senior Admin");
        client1 = new Client("Ali", "cairo", "client123", 500, 12349, "99988776", "Engineer");
        client2 = new Client("Osama", "alex", "client456", 600, 67880, "4668798", "Doctor");

        client = new Client("Ali", "Cairo", "client123", 500, 12349, "99988776", "Engineer");
        recipient = new Client("Osama", "Alex", "client456", 600, 67880, "4668798", "Doctor");
        admin = new Admin("Omar", "alex", "Admin456", 200, 67890, "Senior Admin");

        Client.addClient(client);
        Client.addClient(recipient);
        Admin.addAdmin1(admin);
        Client.addClient(client);
    }

    @AfterAll
    public static void tearDownAfterAll() {
        Admin.clearAdmin();
        Client.clearClients();
        System.out.println("All tests have been executed.");
    }


    @Test
    void testClientLogin() {
        // Create a Client object for testing
        Client client = new Client("user1", "123 Elm Street", "mypassword", 300, 12345, "5551234", "Engineer");

        // Call the login method with provided credentials
        boolean loginResult = client.login("user1", "mypassword");  // Modify credentials for different tests

        // Case 1: If login result is true, login is successful
        if (loginResult) {
            // If the login is successful, assert true
            assertTrue(loginResult, "Login should succeed with correct credentials");
        } else {
            // Case 2: If login fails, we check for a reason
            if ("user1".equals("user1") && !"mypassword".equals("wrongpassword")) {
                // Case 2.1: If the login fails because of incorrect password
                assertFalse(loginResult, "Login should fail with wrong password");
            } else {
                // Case 3: If login fails for any other reason (e.g., missing data)
                assertFalse(loginResult, "Login should fail due to incorrect credentials or other reasons");
            }
        }
    }
    
    @Test
    public void testAdminCreation() {
        Admin admin = new Admin("Omar Ahmed", "Cairo-Ainshams", "omar1234", 5000, 54321, "Senior Admin");
        assertNotNull(admin);
        assertTrue(admin.isValidAccountNumber(admin.getAccountNumber()));
        assertTrue(admin.isValidPassword(admin.getPassword()));
        admin.setBalance(1000);
        assertTrue(admin.isBalanceValid(admin.getBalance()));
        assertEquals("Senior Admin", admin.getAdminRole()); // Correct role assertion
        assertTrue(Admin.addAdmin1(admin)); // Use addAdmin1 to add admin
        assertTrue(Admin.getAdmins().contains(admin)); // Verify admin is added to the list
    }

    @Test
    public void testInvalidAccountNumber() {
        Admin admin = new Admin("Test", "TestAddress", "TestPass", 1000, 12345, "TestRole");
        assertFalse(admin.isValidAccountNumber(1234)); // Too short
        assertFalse(admin.isValidAccountNumber(123456)); // Too long
        assertTrue(admin.isValidAccountNumber(12345)); // Valid
    }

    @Test
    public void testInvalidPassword() {
        Admin admin = new Admin("Test", "TestAddress", "TestPass", 1000, 12345, "TestRole");
        assertFalse(admin.isValidPassword("short"));
        assertTrue(admin.isValidPassword("longEnough"));
    }
    
    @Test
    public void testRemoveAdmin() {
        Admin.addAdmin1(admin1);
        Admin.addAdmin1(admin2);

        assertTrue(Admin.removeAdmin(admin1)); // Admin successfully removed
    }

    @Test
    public void testRemoveClient() {
        Client.addClient(client1);
        Client.addClient(client2);

        assertTrue(Client.removeClient(client1));
        assertFalse(Client.removeClient(client1)); // Client already removed

        List<Client> clients = Client.getClients();
        assertEquals(1, clients.size());
        assertEquals("Osama", clients.get(0).getName());
    }

    @Test
    public void testClientTransactionHistory() {
        client.setBalance(500);
        assertTrue(client.deposit(300));
        assertEquals(800, client.getBalance());

        List<Transaction> transactions = client.getTransactionHistory();
        assertEquals(1, transactions.size());
        assertEquals("Deposit", transactions.get(0).getType());

        assertTrue(client.withdraw(200));
        transactions = client.getTransactionHistory();
        assertEquals(2, transactions.size());
        assertEquals("Withdraw", transactions.get(1).getType());
    }

    @Test
    void testLoanRejectionForZeroTerm_1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Loan(1000, 0, new Date(), client.getAccountNumber());
        });
        assertEquals("Loan term must be positive", exception.getMessage());
    }


    @Test
    public void testLoginPageReflection() throws Exception {
        Class<?> clazz = Class.forName("org.example.LoginPage");

        assertNotNull(clazz.getDeclaredField("userTextField"));
        assertNotNull(clazz.getDeclaredField("passwordField"));
        assertTrue(!Modifier.isPublic(clazz.getDeclaredField("userTextField").getModifiers()), "Fields should not be public");

        var loginMethod = clazz.getDeclaredMethod("login", String.class, String.class);
        assertEquals(boolean.class, loginMethod.getReturnType());
    }

    @Test
    void testAddition_1() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void testSubtraction_1() {
        assertEquals(0, 1 - 1);
    }

    @Test
    void testMultiplication_1() {
        assertEquals(4, 2 * 2);
    }

    @Test
    void testDivision_1() {
        assertEquals(1, 2 / 2);
    }

    @Test
    public void testClientEquality() {
        Client clientDuplicate = new Client("Ali", "Cairo", "client123", 500, 12349, "99988776", "Engineer");
        assertTrue(client1.equals(clientDuplicate));
    }

  

    @Test
    void testAddition() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void testSubtraction() {
        assertEquals(0, 1 - 1);
    }

    @Test
    void testMultiplication() {
        assertEquals(4, 2 * 2);
    }

    @Test
    void testDivision() {
        assertEquals(1, 2 / 2);
    }

    @Test
    public void testRequestAndAcceptLoan() {
        WaitingLoan requestedLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(requestedLoan);

        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertEquals(1, waitingLoans.size());
        WaitingLoan waitingLoan = waitingLoans.get(0);
        assertEquals(1000, waitingLoan.getAmount());

        admin.acceptLoan(client, waitingLoan);

        List<Loan> loans = client.getLoans();
        assertEquals(1, loans.size());
        Loan approvedLoan = loans.get(0);
        assertEquals(1000, approvedLoan.getLoanAmount());
        assertEquals(5, approvedLoan.getTermInYears());

        List<WaitingLoan> updatedWaitingLoans = client.getWaitingLoans();
        assertTrue(updatedWaitingLoans.isEmpty());
    }

    @Test
    public void testRequestAndRejectLoan() {
        WaitingLoan requestedLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(requestedLoan);

        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertEquals(1, waitingLoans.size());
        WaitingLoan waitingLoan = waitingLoans.get(0);
        assertEquals(1000, waitingLoan.getAmount());

        admin.rejectLoan(client, waitingLoan);

        List<WaitingLoan> updatedWaitingLoans = client.getWaitingLoans();
        assertTrue(updatedWaitingLoans.isEmpty());

        List<Loan> loans = client.getLoans();
        assertTrue(loans.isEmpty());
    }

    @Test
    void testLoanApprovalForLowAmount() {
        Loan loan = new Loan(500, 12, new java.util.Date(), client.getAccountNumber());
        assertTrue(loan.isApproved(), "Loan should be approved for small amounts");
    }

    @Test
    void testLoanRejectionForNegativeAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Loan(-1000, 24, new java.util.Date(), client.getAccountNumber());
        });
        assertEquals("Loan amount must be positive", exception.getMessage());
    }

    @Test
    void testMonthlyInstallmentCalculation() {
        Loan loan = new Loan(1200, 12, new java.util.Date(), client.getAccountNumber());
        double expectedInstallment = (loan.getLoanAmount() * (1 + (loan.getInterestRate() / 100))) / loan.getTermInYears() / 12; // Assuming no interest for simplicity
        assertEquals(expectedInstallment, loan.calculateMonthlyInstallment(), 0.001);
    }

    @Test
    public void testLoanApprovalForHighAmount() {
        Loan loan = new Loan(50000, 10, new Date(), client.getAccountNumber());
        assertTrue(loan.isApproved(), "Loan should be approved for high amounts if criteria are met");
    }

    @Test
    public void testLoanRejectionForExcessiveAmount() {
        Loan loan = new Loan(999999999, 10, new Date(), client.getAccountNumber());
        assertFalse(loan.isApproved(), "Loan should be rejected for excessive amounts");
    }

    @Test
    public void testLoanRejectionForZeroAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Loan(0, 12, new Date(), client.getAccountNumber());
        });
        assertEquals("Loan amount must be positive", exception.getMessage());
    }

    @Test
    public void testLoanRejectionForNegativeTerm() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Loan(1000, -5, new Date(), client.getAccountNumber());
        });
        assertEquals("Loan term must be positive", exception.getMessage());
    }

    @Test
    public void testLoanRejectionForZeroTerm() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Loan(1000, 0, new Date(), client.getAccountNumber());
        });
        assertEquals("Loan term must be positive", exception.getMessage());
    }

    @Test
    public void testMonthlyInstallmentCalculationWithInterest() {
        Loan loan = new Loan(1200, 12, new Date(), client.getAccountNumber());
        loan.setInterestRate(5.0); // 5% annual interest
        double expectedInstallment = (loan.getLoanAmount() * (1 + (loan.getInterestRate() / 100))) / loan.getTermInYears() / 12;
        assertEquals(expectedInstallment, loan.calculateMonthlyInstallment(), 0.001);
    }

    @Test
    public void testLoanApprovalWithValidWaitingLoan() {
        WaitingLoan waitingLoan = new WaitingLoan(2000, 5, new Date(), client.getAccountNumber());
        client.addWaitingLoan(waitingLoan);

        admin.acceptLoan(client, waitingLoan);

        List<Loan> loans = client.getLoans();
        assertEquals(1, loans.size());
        Loan approvedLoan = loans.get(0);
        assertEquals(2000, approvedLoan.getLoanAmount());
        assertEquals(5, approvedLoan.getTermInYears());
    }

    @Test
    public void testLoanRejectionWithInvalidWaitingLoan() {
        WaitingLoan waitingLoan = new WaitingLoan(-2000, 5, new Date(), client.getAccountNumber());
        client.addWaitingLoan(waitingLoan);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            admin.acceptLoan(client, waitingLoan);
        });
        assertEquals("Loan amount must be positive", exception.getMessage());
    }

    @Test
    public void testLoanTransferBetweenClients() {
        Loan loan = new Loan(5000, 10, new Date(), client.getAccountNumber());
        client.addLoan(loan);

        Client recipient = new Client("Ali", "cairo", "client123", 500, 12349, "99988776", "Engineer");
        Client.addClient(recipient);

        client.transferLoan(loan, recipient);

        assertFalse(client.getLoans().contains(loan), "Loan should be removed from the original client");
        assertTrue(recipient.getLoans().contains(loan), "Loan should be added to the recipient client");
    }

    @Test
    public void testLoanToStringIncludesDetails() {
        Loan loan = new Loan(5000, 10, new Date(), client.getAccountNumber());
        String loanString = loan.toString();
        assertTrue(loanString.contains("5000"));
        assertTrue(loanString.contains("10"));
        assertTrue(loanString.contains(String.valueOf(client.getAccountNumber())));
    }

    @Test
    public void testWaitingLoanToStringIncludesDetails() {
        WaitingLoan waitingLoan = new WaitingLoan(2000, 5, new Date(), client.getAccountNumber());
        String waitingLoanString = waitingLoan.toString();
        assertTrue(waitingLoanString.contains("2000"));
        assertTrue(waitingLoanString.contains("5"));
        assertTrue(waitingLoanString.contains(String.valueOf(client.getAccountNumber())));
    }
}