package com.example.atm_machine.models;


public enum TransactionType {
    WITHDRAWAL("Withdrawal"),
    DEPOSIT("Deposit"),
    TRANSFER("Transfer"),
    BALANCE_INQUIRY("Balance Inquiry");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}