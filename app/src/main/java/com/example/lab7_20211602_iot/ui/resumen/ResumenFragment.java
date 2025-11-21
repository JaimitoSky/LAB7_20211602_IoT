package com.example.lab7_20211602_iot.ui.resumen;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab7_20211602_iot.databinding.FragmentResumenBinding;

public class ResumenFragment extends Fragment {

    FragmentResumenBinding binding;

    public ResumenFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResumenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
