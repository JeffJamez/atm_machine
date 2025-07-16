package com.example.atm_machine.models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Account {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String pin;
    private Date lastUpdated;
    private List<Transaction> transactionHistory;
    private boolean isLocked;
    private int failedAttempts;

    public Account(String accountNumber, String accountHolderName, double initialBalance, String pin) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.pin = pin;
        this.lastUpdated = new Date();
        this.transactionHistory = new ArrayList<>();
        this.isLocked = false;
        this.failedAttempts = 0;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance; }
    public String getPin() { return pin; }
    public Date getLastUpdated() { return lastUpdated; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }
    public boolean isLocked() { return isLocked; }
    public int getFailedAttempts() { return failedAttempts; }

    public void setBalance(double balance) {
        this.balance = balance;
        this.lastUpdated = new Date();
    }

    public void setLocked(boolean locked) { this.isLocked = locked; }
    public void setFailedAttempts(int attempts) { this.failedAttempts = attempts; }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
        setBalance(transaction.getNewBalance());
    }

    public boolean isLowBalance() {
        return balance < 1000.0;
    }

    public void incrementFailedAttempts() {
        failedAttempts++;
        if (failedAttempts >= 3) {
            isLocked = true;
        }
    }

    public void resetFailedAttempts() {
        failedAttempts = 0;
    }
}