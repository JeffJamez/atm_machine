package com.example.atm_machine.views;

import com.example.atm_machine.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.atm_machine.controllers.ATMController;
import com.example.atm_machine.controllers.AuthController;
import com.example.atm_machine.models.Account;
import com.example.atm_machine.utils.SecurityUtils;
import com.example.atm_machine.utils.ValidationUtils;


public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private TextView tvAccountNumber;
    private TextView tvBalance;
    private TextView tvLowBalanceWarning;
    private CardView cardWithdraw;
    private CardView cardDeposit;
    private CardView cardTransfer;
    private CardView cardBalance;
    private CardView cardHistory;

    private ATMController atmController;
    private Account currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();
        setupClickListeners();

        atmController = ATMController.getInstance();
        currentAccount = atmController.getCurrentAccount();

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tv_welcome);
        tvAccountNumber = findViewById(R.id.tv_account_number);
        tvBalance = findViewById(R.id.tv_balance);
        tvLowBalanceWarning = findViewById(R.id.tv_low_balance_warning);


        cardWithdraw = findViewById(R.id.card_withdraw);
        cardDeposit = findViewById(R.id.card_deposit);
        cardTransfer = findViewById(R.id.card_transfer);
        cardBalance = findViewById(R.id.card_balance);
        cardHistory = findViewById(R.id.card_history);
    }

    private void setupClickListeners() {
        cardWithdraw.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, WithdrawActivity.class)));

        cardDeposit.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, DepositActivity.class)));

        cardTransfer.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, TransferActivity.class)));

        cardBalance.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, BalanceActivity.class)));

        cardHistory.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, TransactionHistoryActivity.class)));
    }

    private void updateUI() {
        if (currentAccount != null) {
            tvWelcome.setText("Welcome, " + currentAccount.getAccountHolderName());
            tvAccountNumber.setText("Account: " +
                    SecurityUtils.maskAccountNumber(currentAccount.getAccountNumber()));
            tvBalance.setText(ValidationUtils.formatCurrency(currentAccount.getBalance()));


            if (atmController.isLowBalance()) {
                tvLowBalanceWarning.setVisibility(View.VISIBLE);
                tvLowBalanceWarning.setText("⚠️ Low balance warning");
            } else {
                tvLowBalanceWarning.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AuthController.getInstance().logout();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}