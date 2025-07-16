package com.example.atm_machine.views;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.atm_machine.R;
import com.example.atm_machine.controllers.ATMController;
import com.example.atm_machine.models.Account;
import com.example.atm_machine.utils.ValidationUtils;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class BalanceActivity extends AppCompatActivity {

    private TextView tvAccountHolder;
    private TextView tvAccountNumber;
    private TextView tvCurrentBalance;
    private TextView tvLastUpdated;

    private ATMController atmController;
    private Account currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        initializeViews();

        atmController = ATMController.getInstance();
        currentAccount = atmController.getCurrentAccount();

        updateBalanceInfo();
    }

    private void initializeViews() {
        tvAccountHolder = findViewById(R.id.tv_account_holder);
        tvAccountNumber = findViewById(R.id.tv_account_number);
        tvCurrentBalance = findViewById(R.id.tv_current_balance);
        tvLastUpdated = findViewById(R.id.tv_last_updated);
    }

    private void updateBalanceInfo() {
        if (currentAccount != null) {
            tvAccountHolder.setText(currentAccount.getAccountHolderName());
            tvAccountNumber.setText("Account: " + currentAccount.getAccountNumber());
            tvCurrentBalance.setText(ValidationUtils.formatCurrency(currentAccount.getBalance()));

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
            String lastUpdated = sdf.format(currentAccount.getLastUpdated());
            tvLastUpdated.setText("Last updated: " + lastUpdated);
        }
    }
}
