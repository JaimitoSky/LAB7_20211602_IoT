package com.example.lab7_20211602_iot.repository;

import androidx.annotation.Nullable;

import com.example.lab7_20211602_iot.model.Tarea;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TareaRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public interface TareasListener {
        void onTareasChanged(List<Tarea> tareas);
        void onError(String message);
    }

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    public TareaRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private @Nullable CollectionReference getUserTareasRef() {
        if (auth.getCurrentUser() == null) return null;
        String uid = auth.getCurrentUser().getUid();
        return db.collection("users")
                .document(uid)
                .collection("tareas");
    }

    public ListenerRegistration listenTareas(TareasListener listener) {
        CollectionReference ref = getUserTareasRef();
        if (ref == null) {
            listener.onError("No hay usuario autenticado");
            return null;
        }

        return ref.orderBy("fechaLimite")
                .addSnapshotListener((snap, e) -> {
                    if (e != null) {
                        listener.onError(e.getMessage());
                        return;
                    }
                    if (snap == null) return;

                    List<Tarea> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : snap) {
                        Tarea t = doc.toObject(Tarea.class);
                        t.id = doc.getId();
                        list.add(t);
                    }
                    listener.onTareasChanged(list);
                });
    }

    public void addTarea(Tarea tarea, SimpleCallback cb) {
        CollectionReference ref = getUserTareasRef();
        if (ref == null) {
            cb.onError("No hay usuario autenticado");
            return;
        }

        // id autogenerado
        ref.add(tarea)
                .addOnSuccessListener(doc -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void updateTarea(Tarea tarea, SimpleCallback cb) {
        CollectionReference ref = getUserTareasRef();
        if (ref == null) {
            cb.onError("No hay usuario autenticado");
            return;
        }
        if (tarea.id == null) {
            cb.onError("Tarea sin ID");
            return;
        }

        ref.document(tarea.id)
                .set(tarea)
                .addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void deleteTarea(Tarea tarea, SimpleCallback cb) {
        CollectionReference ref = getUserTareasRef();
        if (ref == null) {
            cb.onError("No hay usuario autenticado");
            return;
        }
        if (tarea.id == null) {
            cb.onError("Tarea sin ID");
            return;
        }

        ref.document(tarea.id)
                .delete()
                .addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }
}
