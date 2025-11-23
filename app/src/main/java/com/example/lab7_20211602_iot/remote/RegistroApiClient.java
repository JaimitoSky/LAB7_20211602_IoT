package com.example.lab7_20211602_iot.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroApiClient {

    private static RegistroService instance;

    public static RegistroService getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(RegistroService.class);
        }
        return instance;
    }
}
