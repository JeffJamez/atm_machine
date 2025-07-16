package com.example.atm_machine.views;

import com.example.atm_machine.R;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.atm_machine.controllers.ATMController;
import com.example.atm_machine.interfaces.TransactionCallback;
import com.example.atm_machine.models.Transaction;
import com.example.atm_machine.utils.ValidationUtils;

public class TransferActivity extends AppCompatActivity implements TransactionCallback {

    private TextView tvCurrentBalance;
    private EditText etRecipientAccount;
    private EditText etAmount;
    private Button btnTransfer;
    private ProgressBar progressBar;

    private ATMController atmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        initializeViews();
        setupClickListeners();

        atmController = ATMController.getInstance();
        updateCurrentBalance();
    }

    private void initializeViews() {
        tvCurrentBalance = findViewById(R.id.tv_current_balance);
        etRecipientAccount = findViewById(R.id.et_recipient_account);
        etAmount = findViewById(R.id.et_amount);
        btnTransfer = findViewById(R.id.btn_transfer);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupClickListeners() {
        btnTransfer.setOnClickListener(v -> processTransfer());
    }

    private void updateCurrentBalance() {
        double balance = atmController.getCurrentBalance();
        tvCurrentBalance.setText("Available Balance: " + ValidationUtils.formatCurrency(balance));
    }

    private void processTransfer() {
        String recipientAccount = etRecipientAccount.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();

        if (recipientAccount.isEmpty()) {
            Toast.makeText(this, "Please enter recipient account number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter transfer amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            showConfirmationDialog(recipientAccount, amount);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog(String recipientAccount, double amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Transfer");
        builder.setMessage("Transfer " + ValidationUtils.formatCurrency(amount) +
                " to account " + recipientAccount + "?\n\n" +
                "Transfer fee: $2.00\n" +
                "Total deduction: " + ValidationUtils.formatCurrency(amount + 2.0));
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            showLoading(true);
            atmController.processTransfer(recipientAccount, amount, this);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnTransfer.setEnabled(!show);
    }

    @Override
    public void onSuccess(Transaction transaction) {
        showLoading(false);

        Toast.makeText(this, "Transfer successful!", Toast.LENGTH_SHORT).show();

        updateCurrentBalance();

        etRecipientAccount.setText("");
        etAmount.setText("");

        showTransactionReceipt(transaction);
    }

    @Override
    public void onError(String errorMessage) {
        showLoading(false);
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void showTransactionReceipt(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Transfer Receipt");

        String receipt = "Transaction ID: " + transaction.getTransactionId() + "\n" +
                "Type: " + transaction.getType().getDisplayName() + "\n" +
                "Amount: " + ValidationUtils.formatCurrency(transaction.getAmount()) + "\n" +
                "Recipient: " + transaction.getRecipientAccount() + "\n" +
                "Transfer Fee: Ksh2.00\n" +
                "Total Deducted: " + ValidationUtils.formatCurrency(transaction.getAmount() + 2.0) + "\n" +
                "New Balance: " + ValidationUtils.formatCurrency(transaction.getNewBalance()) + "\n" +
                "Date: " + transaction.getTimestamp().toString();

        builder.setMessage(receipt);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
