package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WaitingLoan {
    private int amount;
    private int termInYears;
    private Date startDate;
    private int accountNumber;
    private int Term; // Assuming `term` is a field in the `WaitingLoan` class
    // Constructor
    public WaitingLoan(int amount, int termInYears, Date startDate, int accountNumber) {
        this.amount = amount;
        this.termInYears = termInYears;
        this.startDate = startDate;
        this.accountNumber = accountNumber;
    }

    // Getter for amount
    public int getAmount() {
        return amount;
    }

    // Getter for termInYears
    public int getTermInYears() {
        return termInYears;
    }

    // Getter for startDate
    public Date getDate() {
        return startDate;
    }

    // Getter for accountNumber
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("Loan for account %d: Amount: %d, Term: %d years, Start Date: %s",
                accountNumber, amount, termInYears, sdf.format(startDate));
    }
    public void getLoan() {
        // Assuming loans below a certain threshold (e.g., $1000) are automatically approved
        if (amount <= 1000) {
            System.out.println("Loan approved for account " + accountNumber);
        } else {
            System.out.println("Loan pending for account " + accountNumber);
        }
    }
    public int getTerm() {
        return Term; // Assuming `term` is a field in the `WaitingLoan` class
    }
    public Date getStartDate() {
        return startDate;
    }
}
