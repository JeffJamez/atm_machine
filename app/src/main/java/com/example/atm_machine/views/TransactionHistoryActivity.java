package com.example.atm_machine.views;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.atm_machine.R;
import com.example.atm_machine.controllers.ATMController;
import com.example.atm_machine.models.Account;
import com.example.atm_machine.models.Transaction;
import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTransactions;
    private TextView tvEmptyState;
    private TextView tvTotalTransactions;

    private ATMController atmController;
    private Account currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        initializeViews();

        atmController = ATMController.getInstance();
        currentAccount = atmController.getCurrentAccount();

        loadTransactionHistory();
    }

    private void initializeViews() {
        recyclerViewTransactions = findViewById(R.id.recycler_view_transactions);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        tvTotalTransactions = findViewById(R.id.tv_total_transactions);
    }

    private void loadTransactionHistory() {
        if (currentAccount != null) {
            List<Transaction> transactions = currentAccount.getTransactionHistory();

            if (transactions.isEmpty()) {
                tvEmptyState.setText("No transactions found");
                tvTotalTransactions.setText("Total: 0 transactions");
            } else {
                recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(this));
                tvTotalTransactions.setText("Total: " + transactions.size() + " transactions");
            }
        }
    }
}