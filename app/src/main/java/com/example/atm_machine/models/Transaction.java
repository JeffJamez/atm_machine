package com.example.atm_machine.models;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private TransactionType type;
    private double amount;
    private double previousBalance;
    private double newBalance;
    private Date timestamp;
    private String description;
    private String recipientAccount; // For transfers
    private boolean isSuccessful;

    public Transaction(String transactionId, TransactionType type, double amount,
                       double previousBalance, double newBalance, String description) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
        this.timestamp = new Date();
        this.description = description;
        this.isSuccessful = true;
    }

    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public double getPreviousBalance() { return previousBalance; }
    public double getNewBalance() { return newBalance; }
    public Date getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public String getRecipientAccount() { return recipientAccount; }
    public boolean isSuccessful() { return isSuccessful; }

    public void setRecipientAccount(String recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public void setSuccessful(boolean successful) {
        this.isSuccessful = successful;
    }
}