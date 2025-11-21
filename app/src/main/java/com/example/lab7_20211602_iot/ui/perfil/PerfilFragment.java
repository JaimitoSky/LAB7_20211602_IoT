package com.example.lab7_20211602_iot.ui.perfil;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab7_20211602_iot.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    FragmentPerfilBinding binding;

    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
