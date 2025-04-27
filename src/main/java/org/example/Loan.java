package org.example;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Loan {
    private int loanAmount;
    private int termInYears;
    private Date startDate;
    private int accountNumber; // Account number of the client who took the loan
   private String status;
   private double inerestrtate; // Status of the loan (e.g., "approved", "pending", "rejected")
    public Loan(int loanAmount, int termInYears, Date startDate, int accountNumber) {
        if(loanAmount <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if(termInYears <= 0) {
            throw new IllegalArgumentException("Loan term must be positive");
        }
        this.loanAmount = loanAmount;
        this.termInYears = termInYears;
        this.startDate = startDate;
        this.accountNumber = accountNumber;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public int getTermInYears() {
        return termInYears;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("Loan for account %d: Amount: %d, Term: %d years, Start Date: %s",
                accountNumber, loanAmount, termInYears, sdf.format(startDate));
    }

    public boolean isApproved() {
        // Assuming loans below a certain threshold (e.g., $1000) are automatically approved
        return loanAmount <= 1000000;
    }

    public double calculateMonthlyInstallment() {
        // Assuming no interest for simplicity
        return  (loanAmount * (1 + (inerestrtate / 100))) / termInYears / 12;
    }
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        this.status = status;
    }
    public void getTermInYears(int termInYears) {
        if (termInYears <= 0) {
            throw new IllegalArgumentException("Loan term must be positive");
        }
        this.termInYears = termInYears;
}
public void setInterestRate(double interestRate) {
    if (interestRate < 0) {
        throw new IllegalArgumentException("Interest rate cannot be negative");
    }
    this.inerestrtate = interestRate;
}
public double getInterestRate() {
    return inerestrtate;    
}
}