package com.example.lab7_20211602_iot.ui.resumen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lab7_20211602_iot.databinding.FragmentResumenBinding;
import com.example.lab7_20211602_iot.model.Tarea;
import com.example.lab7_20211602_iot.repository.TareaRepository;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class ResumenFragment extends Fragment {

    private FragmentResumenBinding binding;
    private TareaRepository repo;
    private ListenerRegistration listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResumenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repo = new TareaRepository();
    }

    @Override
    public void onStart() {
        super.onStart();
        listener = repo.listenTareas(new TareaRepository.TareasListener() {
            @Override
            public void onTareasChanged(List<Tarea> tareas) {
                actualizarResumen(tareas);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listener != null) {
            listener.remove();
            listener = null;
        }
    }

    private void actualizarResumen(List<Tarea> tareas) {
        int total = tareas.size();
        int completadas = 0;
        for (Tarea t : tareas) {
            if (t.completada) completadas++;
        }
        int pendientes = total - completadas;

        binding.tvTotal.setText("Total de tareas: " + total);
        binding.tvCompletadas.setText("Tareas completadas: " + completadas);
        binding.tvPendientes.setText("Tareas pendientes: " + pendientes);
    }
}
