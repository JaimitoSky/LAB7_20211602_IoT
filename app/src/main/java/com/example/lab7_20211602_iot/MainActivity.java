package com.example.lab7_20211602_iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lab7_20211602_iot.auth.AuthService;
import com.example.lab7_20211602_iot.auth.LoginActivity;
import com.example.lab7_20211602_iot.databinding.ActivityMainBinding;
import com.example.lab7_20211602_iot.ui.perfil.PerfilFragment;
import com.example.lab7_20211602_iot.ui.resumen.ResumenFragment;
import com.example.lab7_20211602_iot.ui.tareas.TareasFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = AuthService.getInstance();

        if (authService.getCurrentUser() == null) {
            goToLogin();
            return;
        }

        setSupportActionBar(binding.toolbar);

        replaceFragment(new TareasFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_tareas) {
                replaceFragment(new TareasFragment());
            } else if (id == R.id.menu_resumen) {
                replaceFragment(new ResumenFragment());
            } else if (id == R.id.menu_perfil) {
                replaceFragment(new PerfilFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, f)
                .commit();
    }

    // ====== Menú para cerrar sesión ======
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            authService.logout();
            goToLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
