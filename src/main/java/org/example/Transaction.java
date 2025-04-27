package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private String type;
    private int amount;
    private Date date;
    private Integer targetAccount; // Use Integer to handle null values

    // Constructor for deposit and withdraw
    public Transaction(String type, int amount, Date date) {
        this(type, amount, date, null);
    }

    // Constructor for transfer transactions
    public Transaction(String type, int amount, Date date, Integer targetAccount) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.targetAccount = targetAccount;

    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public Integer getTargetAccount() {
        return targetAccount;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Format without time zone

        if (type.equals("Transfer") && targetAccount != null) {
            return String.format("%s - %d to account %d - %s", type, amount, targetAccount, dateFormat.format(date));
        } else if (type.equals("Received") && targetAccount != null) {
            return String.format("%s - %d from account %d - %s", type, amount, targetAccount, dateFormat.format(date));
        } else {
            return String.format("%s - %d - %s", type, amount, dateFormat.format(date));
        }
    }
}
