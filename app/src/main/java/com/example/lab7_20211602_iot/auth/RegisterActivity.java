package com.example.lab7_20211602_iot.auth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lab7_20211602_iot.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(v -> {
            // Lógica real en Commit 3
            finish();
        });
    }
}
