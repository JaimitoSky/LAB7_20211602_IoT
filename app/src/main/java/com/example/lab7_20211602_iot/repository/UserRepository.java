package com.example.lab7_20211602_iot.repository;

import com.example.lab7_20211602_iot.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private final FirebaseFirestore db;

    public interface UserSaveCallback {
        void onSuccess();
        void onError(String message);
    }

    public interface UserLoadCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface UserUpdateCallback {
        void onSuccess();
        void onError(String message);
    }

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void saveUser(User user, UserSaveCallback cb) {
        db.collection("users")
                .document(user.uid)
                .set(user)
                .addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void getUser(String uid, UserLoadCallback cb) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        User u = snapshot.toObject(User.class);
                        cb.onSuccess(u);
                    } else {
                        cb.onError("Usuario no encontrado");
                    }
                })
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void updatePhotoUrl(String uid, String url, UserUpdateCallback cb) {
        db.collection("users")
                .document(uid)
                .update("photoUrl", url)
                .addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }
}
