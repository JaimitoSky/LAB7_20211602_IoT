package com.example.lab7_20211602_iot.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;

import com.example.lab7_20211602_iot.databinding.ActivityRegisterBinding;
import com.example.lab7_20211602_iot.util.Toaster;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = AuthService.getInstance();

        binding.btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String nombre = binding.etNombre.getText().toString().trim();
        String dni    = binding.etDni.getText().toString().trim();
        String email  = binding.etEmail.getText().toString().trim();
        String pass   = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) ||
                TextUtils.isEmpty(dni) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(pass)) {
            Toaster.show(this, "Complete todos los campos");
            return;
        }

        binding.btnRegister.setEnabled(false);

        authService.registerUser(nombre, dni, email, pass, new AuthService.AuthCallback() {
            @Override
            public void onSuccess() {
                binding.btnRegister.setEnabled(true);
                Toaster.show(RegisterActivity.this, "Usuario registrado correctamente");
                finish();
            }

            @Override
            public void onError(String message) {
                binding.btnRegister.setEnabled(true);
                Toaster.show(RegisterActivity.this, "Error: " + message);
            }
        });
    }
}
