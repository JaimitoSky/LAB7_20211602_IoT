package com.example.lab7_20211602_iot.repository;
import com.example.lab7_20211602_iot.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private final FirebaseFirestore db;

    public interface UserSaveCallback {
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
}