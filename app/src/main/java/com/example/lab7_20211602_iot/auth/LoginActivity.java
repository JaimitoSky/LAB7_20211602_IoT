package com.example.lab7_20211602_iot.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.lab7_20211602_iot.MainActivity;
import com.example.lab7_20211602_iot.databinding.ActivityLoginBinding;
import com.example.lab7_20211602_iot.util.Toaster;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = AuthService.getInstance();

        if (authService.getCurrentUser() != null) {
            goToMain();
            return;
        }

        binding.btnLogin.setOnClickListener(v -> doLogin());

        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        binding.tvForgotPassword.setOnClickListener(v -> sendResetPassword());
    }

    private void doLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String pass  = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toaster.show(this, "Ingrese el correo");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toaster.show(this, "Ingrese la contraseña");
            return;
        }

        binding.btnLogin.setEnabled(false);

        authService.login(email, pass, new AuthService.AuthCallback() {
            @Override
            public void onSuccess() {
                binding.btnLogin.setEnabled(true);
                goToMain();
            }

            @Override
            public void onError(String message) {
                binding.btnLogin.setEnabled(true);
                Toaster.show(LoginActivity.this, "Error: " + message);
            }
        });
    }

    private void sendResetPassword() {
        String email = binding.etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toaster.show(this, "Ingrese el correo para recuperar la contraseña");
            return;
        }

        authService.sendPasswordReset(email, new AuthService.AuthCallback() {
            @Override
            public void onSuccess() {
                Toaster.show(LoginActivity.this,
                        "Se envió un correo para restablecer la contraseña");
            }

            @Override
            public void onError(String message) {
                Toaster.show(LoginActivity.this, "Error: " + message);
            }
        });
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
