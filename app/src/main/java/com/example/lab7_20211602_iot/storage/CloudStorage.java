package com.example.lab7_20211602_iot.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CloudStorage {

    private final FirebaseStorage storage;

    public interface UploadCallback {
        void onSuccess(String downloadUrl);
        void onError(String message);
    }

    public interface UrlCallback {
        void onSuccess(String downloadUrl);
        void onError(String message);
    }

    public CloudStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadProfileImage(String uid, Uri uri, UploadCallback cb) {
        StorageReference ref = storage.getReference()
                .child("profileImages/" + uid + ".jpg");

        ref.putFile(uri)
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(url ->
                                cb.onSuccess(url.toString())
                        )
                )
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void getDownloadUrl(String path, UrlCallback cb) {
        storage.getReference().child(path)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> cb.onSuccess(uri.toString()))
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }
}
