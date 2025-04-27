package org.example.junit5;

import java.util.Date;
import java.util.List;

import org.example.Admin;
import org.example.Client;
import org.example.Loan;
import org.example.WaitingLoan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoanOperationsTest {

    private Admin admin;
    private Client client;

    @BeforeEach
    public void setUp() {

        Admin.clearAdmin();
        Client.clearClients();

        admin = new Admin("Omar", "alex", "Admin456", 200, 67890, "Senior Admin");
        client = new Client("Osama", "alex", "client456", 600, 67880, "4668798", "Doctor");

        Admin.addAdmin1(admin);
        Client.addClient(client);
    }

    @Test
    public void testRequestAndAcceptLoan() {
        // Client requests a loan
        WaitingLoan requestedLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(requestedLoan);

        // Verify that the loan is in waiting loans
        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertEquals(1, waitingLoans.size());
        WaitingLoan waitingLoan = waitingLoans.get(0);
        assertEquals(1000, waitingLoan.getAmount());

        // Admin accepts the loan
        admin.acceptLoan(client, waitingLoan);

        // Verify that the loan has been moved to the approved list
        List<Loan> loans = client.getLoans();
        assertEquals(1, loans.size());
        Loan approvedLoan = loans.get(0);
        assertEquals(1000, approvedLoan.getLoanAmount());
        assertEquals(5, approvedLoan.getTermInYears());

        // Verify that the waitingLoans list is empty or properly updated
        List<WaitingLoan> updatedWaitingLoans = client.getWaitingLoans();
        assertTrue(updatedWaitingLoans.isEmpty());
    }

    @Test
    public void testRequestAndRejectLoan() {
        // Client requests a loan
        WaitingLoan requestedLoan = new WaitingLoan(1000, 5, new java.util.Date(), client.getAccountNumber());
        client.addWaitingLoan(requestedLoan);

        // Verify that the loan is in waiting loans
        List<WaitingLoan> waitingLoans = client.getWaitingLoans();
        assertEquals(1, waitingLoans.size());
        WaitingLoan waitingLoan = waitingLoans.get(0);
        assertEquals(1000, waitingLoan.getAmount());

        // Admin rejects the loan
        admin.rejectLoan(client, waitingLoan);

        // Verify that the loan has been removed from the waiting list
        List<WaitingLoan> updatedWaitingLoans = client.getWaitingLoans();
        assertTrue(updatedWaitingLoans.isEmpty());

        // Verify that the client has no approved loans
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