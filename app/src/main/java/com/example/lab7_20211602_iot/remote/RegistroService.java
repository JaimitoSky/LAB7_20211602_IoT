package com.example.lab7_20211602_iot.remote;

import com.example.lab7_20211602_iot.remote.dto.RegistroRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistroService {

    @POST("registro")
    Call<String> registrar(@Body RegistroRequest req);
}
