package com.example.atm_machine.views;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.atm_machine.R;
import com.example.atm_machine.controllers.ATMController;
import com.example.atm_machine.interfaces.TransactionCallback;
import com.example.atm_machine.models.Transaction;
import com.example.atm_machine.utils.ValidationUtils;



public class WithdrawActivity extends AppCompatActivity implements TransactionCallback {

    private TextView tvCurrentBalance;
    private EditText etAmount;
    private Button btnWithdraw;
    private Button btnQuickAmount20;
    private Button btnQuickAmount50;
    private Button btnQuickAmount100;
    private Button btnQuickAmount200;
    private ProgressBar progressBar;

    private ATMController atmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        initializeViews();
        setupClickListeners();

        atmController = ATMController.getInstance();
        updateCurrentBalance();
    }

    private void initializeViews() {
        tvCurrentBalance = findViewById(R.id.tv_current_balance);
        etAmount = findViewById(R.id.et_amount);
        btnWithdraw = findViewById(R.id.btn_withdraw);
        btnQuickAmount20 = findViewById(R.id.btn_quick_20);
        btnQuickAmount50 = findViewById(R.id.btn_quick_50);
        btnQuickAmount100 = findViewById(R.id.btn_quick_100);
        btnQuickAmount200 = findViewById(R.id.btn_quick_200);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupClickListeners() {
        btnWithdraw.setOnClickListener(v -> processWithdrawal());

        btnQuickAmount20.setOnClickListener(v -> setQuickAmount(20));
        btnQuickAmount50.setOnClickListener(v -> setQuickAmount(50));
        btnQuickAmount100.setOnClickListener(v -> setQuickAmount(100));
        btnQuickAmount200.setOnClickListener(v -> setQuickAmount(200));
    }

    private void setQuickAmount(double amount) {
        etAmount.setText(String.valueOf(amount));
    }

    private void updateCurrentBalance() {
        double balance = atmController.getCurrentBalance();
        tvCurrentBalance.setText("Available Balance: " + ValidationUtils.formatCurrency(balance));
    }

    private void processWithdrawal() {
        String amountStr = etAmount.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter withdrawal amount", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            showConfirmationDialog(amount);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog(double amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Withdrawal");
        builder.setMessage("Are you sure you want to withdraw " +
                ValidationUtils.formatCurrency(amount) + "?");
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            showLoading(true);
            atmController.processWithdrawal(amount, this);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnWithdraw.setEnabled(!show);
    }

    @Override
    public void onSuccess(Transaction transaction) {
        showLoading(false);



        Toast.makeText(this, "Withdrawal successful!", Toast.LENGTH_SHORT).show();



        updateCurrentBalance();



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
        builder.setTitle("Transaction Receipt");

        String receipt = "Transaction ID: " + transaction.getTransactionId() + "\n" +
                "Type: " + transaction.getType().getDisplayName() + "\n" +
                "Amount: " + ValidationUtils.formatCurrency(transaction.getAmount()) + "\n" +
                "Previous Balance: " + ValidationUtils.formatCurrency(transaction.getPreviousBalance()) + "\n" +
                "New Balance: " + ValidationUtils.formatCurrency(transaction.getNewBalance()) + "\n" +
                "Date: " + transaction.getTimestamp().toString();

        builder.setMessage(receipt);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}