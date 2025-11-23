package com.example.lab7_20211602_iot.ui.tareas;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab7_20211602_iot.adapter.TareasAdapter;
import com.example.lab7_20211602_iot.databinding.FragmentTareasBinding;
import com.example.lab7_20211602_iot.model.Tarea;
import com.example.lab7_20211602_iot.repository.TareaRepository;
import com.example.lab7_20211602_iot.util.DateUtils;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Calendar;
import java.util.List;

public class TareasFragment extends Fragment {

    private FragmentTareasBinding binding;
    private TareasAdapter adapter;
    private TareaRepository repo;
    private ListenerRegistration tareasListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTareasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repo = new TareaRepository();

        adapter = new TareasAdapter(new TareasAdapter.Listener() {
            @Override
            public void onEdit(Tarea t) {
                mostrarDialogoTarea(t);
            }

            @Override
            public void onDelete(Tarea t) {
                confirmarEliminar(t);
            }

            @Override
            public void onToggleEstado(Tarea t, boolean nuevoEstado) {
                t.completada = nuevoEstado;
                repo.updateTarea(t, new TareaRepository.SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        // nada
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.rvTareas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTareas.setAdapter(adapter);

        binding.fabAdd.setOnClickListener(v -> mostrarDialogoTarea(null));
    }

    @Override
    public void onStart() {
        super.onStart();
        tareasListener = repo.listenTareas(new TareaRepository.TareasListener() {
            @Override
            public void onTareasChanged(List<Tarea> tareas) {
                adapter.setData(tareas);
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
        if (tareasListener != null) {
            tareasListener.remove();
            tareasListener = null;
        }
    }

    private void confirmarEliminar(Tarea t) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar tarea")
                .setMessage("¿Seguro que deseas eliminar esta tarea?")
                .setPositiveButton("Eliminar", (dialog, which) ->
                        repo.deleteTarea(t, new TareaRepository.SimpleCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        })
                )
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoTarea(@Nullable Tarea tareaEditar) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(com.example.lab7_20211602_iot.R.layout.dialog_tarea, null, false);

        EditText etTitulo = v.findViewById(com.example.lab7_20211602_iot.R.id.etTitulo);
        EditText etDescripcion = v.findViewById(com.example.lab7_20211602_iot.R.id.etDescripcion);
        EditText etFecha = v.findViewById(com.example.lab7_20211602_iot.R.id.etFecha);
        CheckBox chkCompletada = v.findViewById(com.example.lab7_20211602_iot.R.id.chkCompletada);

        final Calendar cal = Calendar.getInstance();

        etFecha.setOnClickListener(view -> {
            new DatePickerDialog(requireContext(),
                    (picker, y, m, d) -> {
                        cal.set(Calendar.YEAR, y);
                        cal.set(Calendar.MONTH, m);
                        cal.set(Calendar.DAY_OF_MONTH, d);
                        etFecha.setText(String.format("%02d/%02d/%04d", d, m + 1, y));
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        boolean esEdicion = tareaEditar != null;
        if (esEdicion) {
            etTitulo.setText(tareaEditar.titulo);
            etDescripcion.setText(tareaEditar.descripcion);
            etFecha.setText(DateUtils.formatDate(tareaEditar.fechaLimite));
            cal.setTimeInMillis(tareaEditar.fechaLimite);
            chkCompletada.setChecked(tareaEditar.completada);
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(esEdicion ? "Editar tarea" : "Nueva tarea")
                .setView(v)
                .setPositiveButton(esEdicion ? "Guardar" : "Crear", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(dlg -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setOnClickListener(btn -> {
                        String titulo = etTitulo.getText().toString().trim();
                        String desc = etDescripcion.getText().toString().trim();
                        String fechaTxt = etFecha.getText().toString().trim();

                        if (titulo.isEmpty()) {
                            etTitulo.setError("Ingrese un título");
                            return;
                        }
                        if (fechaTxt.isEmpty()) {
                            etFecha.setError("Seleccione una fecha");
                            return;
                        }

                        long fechaMillis = DateUtils.parseDate(fechaTxt);
                        boolean completada = chkCompletada.isChecked();

                        if (esEdicion) {
                            tareaEditar.titulo = titulo;
                            tareaEditar.descripcion = desc;
                            tareaEditar.fechaLimite = fechaMillis;
                            tareaEditar.completada = completada;

                            repo.updateTarea(tareaEditar, new TareaRepository.SimpleCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(String message) {
                                    Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Tarea nueva = new Tarea(null, titulo, desc, fechaMillis, completada);
                            repo.addTarea(nueva, new TareaRepository.SimpleCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getContext(), "Tarea creada", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(String message) {
                                    Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        });

        dialog.show();
    }
}
