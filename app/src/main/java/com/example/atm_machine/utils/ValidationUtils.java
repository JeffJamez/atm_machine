package com.example.atm_machine.utils;



public class ValidationUtils {



    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 999999.99;
    }


    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }

        String cleanNumber = accountNumber.replaceAll("\\s+", "");
        return cleanNumber.length() >= 8 && cleanNumber.length() <= 12 &&
                cleanNumber.matches("\\d+");
    }


    public static boolean isValidPin(String pin) {
        if (pin == null || pin.trim().isEmpty()) {
            return false;
        }

        return pin.length() >= 3 && pin.length() <= 6 && pin.matches("\\d+");
    }


    public static String formatCurrency(double amount) {
        return String.format("Ksh %.2f", amount);
    }


    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
}