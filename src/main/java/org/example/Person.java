package org.example;


public class Person {

    private String name;
    private String address;
    private String password;
    private int balance;
    private int accountNumber;

    // Default constructor
    public Person() {
    }

    // Parameterized constructor
    public Person(String name, String address, String password, int balance, int accountNumber) {
        this.name = name;
        this.address = address;
        this.password = password;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public  void setBalance(int balance) {
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
