package com.example.atm_machine.interfaces;

import com.example.atm_machine.models.Transaction;


public interface TransactionCallback {
    void onSuccess(Transaction transaction);
    void onError(String errorMessage);
}