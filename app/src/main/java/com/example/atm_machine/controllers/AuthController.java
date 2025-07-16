package com.example.atm_machine.controllers;

import com.example.atm_machine.models.Account;
import com.example.atm_machine.interfaces.AuthCallback;
import com.example.atm_machine.utils.SecurityUtils;
import com.example.atm_machine.utils.ValidationUtils;

public class AuthController {
    private static AuthController instance;
    private Account defaultAccount;

    private AuthController() {
        defaultAccount = new Account(
                "1234567890",
                "Waithera Kimani",
                10000.0,
                SecurityUtils.hashPin("123")
        );
    }

    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }

    public void authenticate(String accountNumber, String pin, AuthCallback callback) {
        try {
            if (!ValidationUtils.isValidAccountNumber(accountNumber)) {
                callback.onAuthFailure("Invalid account number format");
                return;
            }

            if (!ValidationUtils.isValidPin(pin)) {
                callback.onAuthFailure("Invalid PIN format");
                return;
            }

            // Check if account is locked
//            if (defaultAccount.isLocked()) {
//                callback.onAuthFailure("Account is locked. Please contact customer service.");
//                return;
//            }

            if (!accountNumber.equals(defaultAccount.getAccountNumber())) {
                defaultAccount.incrementFailedAttempts();
                callback.onAuthFailure("Account not found");
                return;
            }

            String hashedPin = SecurityUtils.hashPin(pin);
            if (!hashedPin.equals(defaultAccount.getPin())) {
                defaultAccount.incrementFailedAttempts();

                if (defaultAccount.isLocked()) {
                    callback.onAuthFailure("Account locked after too many failed attempts");
                } else {
                    int remainingAttempts = 3 - defaultAccount.getFailedAttempts();
                    callback.onAuthFailure("Invalid PIN. " + remainingAttempts + " attempts remaining");
                }
                return;
            }

            defaultAccount.resetFailedAttempts();
            callback.onAuthSuccess(defaultAccount);

        } catch (Exception e) {
            callback.onAuthFailure("Authentication failed: " + e.getMessage());
        }
    }

    public void logout() {
        ATMController.getInstance().setCurrentAccount(null);
    }

    public Account getDefaultAccount() {
        return defaultAccount;
    }
}