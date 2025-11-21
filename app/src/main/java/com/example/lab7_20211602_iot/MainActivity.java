package com.example.lab7_20211602_iot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.lab7_20211602_iot.databinding.ActivityMainBinding;
import com.example.lab7_20211602_iot.ui.perfil.PerfilFragment;
import com.example.lab7_20211602_iot.ui.resumen.ResumenFragment;
import com.example.lab7_20211602_iot.ui.tareas.TareasFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new TareasFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_tareas:
                    replaceFragment(new TareasFragment());
                    break;
                case R.id.menu_resumen:
                    replaceFragment(new ResumenFragment());
                    break;
                case R.id.menu_perfil:
                    replaceFragment(new PerfilFragment());
                    break;
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
}
