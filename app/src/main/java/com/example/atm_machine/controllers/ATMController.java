package com.example.atm_machine.controllers;



import com.example.atm_machine.models.Account;
import com.example.atm_machine.models.Transaction;
import com.example.atm_machine.models.TransactionType;
import com.example.atm_machine.interfaces.TransactionCallback;
import com.example.atm_machine.utils.ValidationUtils;
import java.util.UUID;

public class ATMController {
    private Account currentAccount;
    private static ATMController instance;

    private static final double DAILY_WITHDRAWAL_LIMIT = 5000.0;
    private static final double DAILY_DEPOSIT_LIMIT = 50000.0;
    private static final double TRANSFER_FEE = 2.0;

    private ATMController() {}

    public static ATMController getInstance() {
        if (instance == null) {
            instance = new ATMController();
        }
        return instance;
    }

    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void processWithdrawal(double amount, TransactionCallback callback) {
        try {
            if (!ValidationUtils.isValidAmount(amount)) {
                callback.onError("Invalid withdrawal amount");
                return;
            }

            if (currentAccount == null) {
                callback.onError("No account selected");
                return;
            }

            if (amount > currentAccount.getBalance()) {
                callback.onError("Insufficient funds. Available balance: Ksh" +
                        String.format("%.2f", currentAccount.getBalance()));
                return;
            }

            if (amount > DAILY_WITHDRAWAL_LIMIT) {
                callback.onError("Daily withdrawal limit exceeded. Maximum: Ksh" +
                        String.format("%.2f", DAILY_WITHDRAWAL_LIMIT));
                return;
            }

            double previousBalance = currentAccount.getBalance();
            double newBalance = previousBalance - amount;

            String transactionId = UUID.randomUUID().toString();
            Transaction transaction = new Transaction(
                    transactionId,
                    TransactionType.WITHDRAWAL,
                    amount,
                    previousBalance,
                    newBalance,
                    "Cash withdrawal"
            );

            currentAccount.addTransaction(transaction);
            callback.onSuccess(transaction);

        } catch (Exception e) {
            callback.onError("Transaction failed: " + e.getMessage());
        }
    }

    public void processDeposit(double amount, TransactionCallback callback) {
        try {
            if (!ValidationUtils.isValidAmount(amount)) {
                callback.onError("Invalid deposit amount");
                return;
            }

            if (currentAccount == null) {
                callback.onError("No account selected");
                return;
            }

            if (amount > DAILY_DEPOSIT_LIMIT) {
                callback.onError("Daily deposit limit exceeded. Maximum: Ksh" +
                        String.format("%.2f", DAILY_DEPOSIT_LIMIT));
                return;
            }

            double previousBalance = currentAccount.getBalance();
            double newBalance = previousBalance + amount;

            String transactionId = UUID.randomUUID().toString();
            Transaction transaction = new Transaction(
                    transactionId,
                    TransactionType.DEPOSIT,
                    amount,
                    previousBalance,
                    newBalance,
                    "Cash deposit"
            );

            currentAccount.addTransaction(transaction);
            callback.onSuccess(transaction);

        } catch (Exception e) {
            callback.onError("Transaction failed: " + e.getMessage());
        }
    }

    public void processTransfer(String recipientAccount, double amount, TransactionCallback callback) {
        try {
            if (!ValidationUtils.isValidAmount(amount)) {
                callback.onError("Invalid transfer amount");
                return;
            }

            if (!ValidationUtils.isValidAccountNumber(recipientAccount)) {
                callback.onError("Invalid recipient account number");
                return;
            }

            if (currentAccount == null) {
                callback.onError("No account selected");
                return;
            }

            double totalAmount = amount + TRANSFER_FEE;
            if (totalAmount > currentAccount.getBalance()) {
                callback.onError("Insufficient funds. Required: Ksh" +
                        String.format("%.2f", totalAmount) +
                        " (including Ksh" + String.format("%.2f", TRANSFER_FEE) + " fee)");
                return;
            }

            double previousBalance = currentAccount.getBalance();
            double newBalance = previousBalance - totalAmount;

            String transactionId = UUID.randomUUID().toString();
            Transaction transaction = new Transaction(
                    transactionId,
                    TransactionType.TRANSFER,
                    amount,
                    previousBalance,
                    newBalance,
                    "Transfer to " + recipientAccount
            );

            transaction.setRecipientAccount(recipientAccount);
            currentAccount.addTransaction(transaction);
            callback.onSuccess(transaction);

        } catch (Exception e) {
            callback.onError("Transaction failed: " + e.getMessage());
        }
    }

    public double getCurrentBalance() {
        return currentAccount != null ? currentAccount.getBalance() : 0.0;
    }

    public boolean isLowBalance() {
        return currentAccount != null && currentAccount.isLowBalance();
    }
}
