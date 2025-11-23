package com.example.lab7_20211602_iot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab7_20211602_iot.R;
import com.example.lab7_20211602_iot.model.Tarea;
import com.example.lab7_20211602_iot.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.VH> {

    public interface Listener {
        void onEdit(Tarea t);
        void onDelete(Tarea t);
        void onToggleEstado(Tarea t, boolean nuevoEstado);
    }

    private List<Tarea> data = new ArrayList<>();
    private final Listener listener;

    public TareasAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setData(List<Tarea> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarea, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Tarea t = data.get(pos);
        h.tvTitulo.setText(t.titulo);
        h.tvDescripcion.setText(t.descripcion == null ? "" : t.descripcion);
        String fechaTxt = "Vence: " + DateUtils.formatDate(t.fechaLimite);
        h.tvFecha.setText(fechaTxt);

        h.chkCompletada.setOnCheckedChangeListener(null);
        h.chkCompletada.setChecked(t.completada);

        h.chkCompletada.setOnCheckedChangeListener((buttonView, isChecked) ->
                listener.onToggleEstado(t, isChecked)
        );

        h.btnEdit.setOnClickListener(v -> listener.onEdit(t));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(t));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        CheckBox chkCompletada;
        TextView tvTitulo, tvDescripcion, tvFecha;
        ImageButton btnEdit, btnDelete;

        public VH(@NonNull View itemView) {
            super(itemView);
            chkCompletada = itemView.findViewById(R.id.chkCompletada);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
