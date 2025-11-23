package com.example.lab7_20211602_iot.ui.perfil;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lab7_20211602_iot.databinding.FragmentPerfilBinding;
import com.example.lab7_20211602_iot.model.User;
import com.example.lab7_20211602_iot.repository.UserRepository;
import com.example.lab7_20211602_iot.storage.CloudStorage;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private UserRepository userRepo;
    private CloudStorage cloudStorage;
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        userRepo = new UserRepository();
        cloudStorage = new CloudStorage();

        configurarImagePicker();
        cargarDatosUsuario();

        binding.btnCambiarFoto.setOnClickListener(v ->
                pickImageLauncher.launch("image/*")
        );

        return binding.getRoot();
    }

    private void configurarImagePicker() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        subirNuevaFoto(uri);
                    }
                }
        );
    }

    private void cargarDatosUsuario() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        userRepo.getUser(uid, new UserRepository.UserLoadCallback() {
            @Override
            public void onSuccess(User user) {
                binding.tvNombre.setText(user.nombre);
                binding.tvDni.setText("DNI: " + user.dni);
                binding.tvEmail.setText(user.email);

                if (user.photoUrl != null && !user.photoUrl.isEmpty()) {
                    Glide.with(requireContext())
                            .load(user.photoUrl)
                            .into(binding.imgProfile);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(),
                        "Error cargando usuario: " + message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void subirNuevaFoto(Uri uri) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        cloudStorage.uploadProfileImage(uid, uri, new CloudStorage.UploadCallback() {
            @Override
            public void onSuccess(String downloadUrl) {
                userRepo.updatePhotoUrl(uid, downloadUrl, new UserRepository.UserUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        Glide.with(requireContext())
                                .load(downloadUrl)
                                .into(binding.imgProfile);

                        Toast.makeText(getContext(),
                                "Imagen subida. URL: " + downloadUrl,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(),
                                "Error guardando URL: " + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(),
                        "Error subiendo imagen: " + message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
