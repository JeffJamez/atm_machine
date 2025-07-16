package com.example.atm_machine.interfaces;

import com.example.atm_machine.models.Account;


public interface AuthCallback {
    void onAuthSuccess(Account account);
    void onAuthFailure(String errorMessage);
}