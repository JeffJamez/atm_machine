package com.example.atm_machine.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SecurityUtils {


    public static String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pin.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }


    public static long getSessionTimeout() {
        return 30 * 60 * 1000; // 30 minutes
    }


    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return accountNumber;
        }

        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < accountNumber.length() - 4; i++) {
            masked.append('*');
        }
        masked.append(accountNumber.substring(accountNumber.length() - 4));

        return masked.toString();
    }
}