package com.example.lab7_20211602_iot.auth;

import androidx.annotation.NonNull;

import com.example.lab7_20211602_iot.model.User;
import com.example.lab7_20211602_iot.repository.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AuthService {

    private static AuthService instance;

    private final FirebaseAuth auth;
    private final UserRepository userRepository;

    public interface AuthCallback {
        void onSuccess();
        void onError(String message);
    }

    private AuthService() {
        auth = FirebaseAuth.getInstance();
        userRepository = new UserRepository();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void login(String email, String password, AuthCallback cb) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void sendPasswordReset(String email, AuthCallback cb) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void logout() {
        auth.signOut();
    }

    public void registerUser(String nombre,
                             String dni,
                             String email,
                             String password,
                             AuthCallback cb) {

        if (nombre == null || nombre.trim().isEmpty()) {
            cb.onError("Ingrese un nombre");
            return;
        }
        if (dni == null || dni.length() != 8) {
            cb.onError("El DNI debe tener 8 dígitos");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            cb.onError("Ingrese un correo");
            return;
        }
        if (password == null || password.length() < 6) {
            cb.onError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser fbUser = authResult.getUser();
                        if (fbUser == null) {
                            cb.onError("No se pudo obtener el usuario creado");
                            return;
                        }

                        User u = new User();
                        u.uid = fbUser.getUid();
                        u.nombre = nombre;
                        u.dni = dni;
                        u.email = email;
                        u.photoUrl = null;

                        userRepository.saveUser(u, new UserRepository.UserSaveCallback() {
                            @Override
                            public void onSuccess() {
                                cb.onSuccess();
                            }

                            @Override
                            public void onError(String message) {
                                cb.onError(message);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }
}
