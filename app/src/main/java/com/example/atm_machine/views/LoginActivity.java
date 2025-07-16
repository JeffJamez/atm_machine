
package com.example.atm_machine.views;

import com.example.atm_machine.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.atm_machine.controllers.AuthController;
import com.example.atm_machine.controllers.ATMController;
import com.example.atm_machine.interfaces.AuthCallback;
import com.example.atm_machine.models.Account;


public class LoginActivity extends AppCompatActivity implements AuthCallback {

    private EditText etAccountNumber;
    private EditText etPin;
    private Button btnLogin;
    private ProgressBar progressBar;

    private AuthController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupClickListeners();

        authController = AuthController.getInstance();
    }

    private void initializeViews() {
        etAccountNumber = findViewById(R.id.et_account_number);
        etPin = findViewById(R.id.et_pin);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        String accountNumber = etAccountNumber.getText().toString().trim();
        String pin = etPin.getText().toString().trim();


        showLoading(true);


        authController.authenticate(accountNumber, pin, this);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }

    @Override
    public void onAuthSuccess(Account account) {
        showLoading(false);

        ATMController.getInstance().setCurrentAccount(account);

        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAuthFailure(String errorMessage) {
        showLoading(false);

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

        etPin.setText("");
    }
}