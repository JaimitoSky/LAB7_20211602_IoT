package com.example.lab7_20211602_iot.auth;

import com.example.lab7_20211602_iot.model.User;
import com.example.lab7_20211602_iot.remote.RegistroApiClient;
import com.example.lab7_20211602_iot.remote.RegistroService;
import com.example.lab7_20211602_iot.remote.dto.RegistroRequest;
import com.example.lab7_20211602_iot.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {

    private static AuthService instance;

    private final FirebaseAuth auth;
    private final UserRepository userRepository;
    private final RegistroService registroApi;

    public interface AuthCallback {
        void onSuccess();
        void onError(String message);
    }

    private AuthService() {
        auth = FirebaseAuth.getInstance();
        userRepository = new UserRepository();
        registroApi = RegistroApiClient.getInstance();
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

    // Registro con microservicio de registro
    public void registerUser(String nombre,
                             String dni,
                             String email,
                             String password,
                             AuthCallback cb) {

        if (nombre == null || nombre.trim().isEmpty()) {
            cb.onError("Ingrese un nombre");
            return;
        }
        if (dni == null || dni.trim().length() != 8) {
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

        // 1. Llamar microservicio /registro
        RegistroRequest req = new RegistroRequest(dni, email);

        registroApi.registrar(req).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> res) {

                if (!res.isSuccessful()) {
                    try {
                        String msg = res.errorBody() != null
                                ? res.errorBody().string()
                                : "Error del microservicio";
                        cb.onError(msg);
                    } catch (Exception e) {
                        cb.onError("Error del microservicio");
                    }
                    return;
                }

                // 2. Validaciones OK → crear usuario en Firebase
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
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
                        })
                        .addOnFailureListener(e -> cb.onError(e.getMessage()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cb.onError("No se pudo conectar al microservicio de registro");
            }
        });
    }
}
