package com.example.lab7_20211602_iot.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegistroApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/"; // emulador

    private static RegistroService instance;

    public static RegistroService getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(RegistroService.class);
        }
        return instance;
    }
}
