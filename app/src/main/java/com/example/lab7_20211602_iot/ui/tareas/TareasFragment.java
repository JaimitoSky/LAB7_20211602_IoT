package com.example.lab7_20211602_iot.ui.tareas;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab7_20211602_iot.databinding.FragmentTareasBinding;

public class TareasFragment extends Fragment {

    FragmentTareasBinding binding;

    public TareasFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTareasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
