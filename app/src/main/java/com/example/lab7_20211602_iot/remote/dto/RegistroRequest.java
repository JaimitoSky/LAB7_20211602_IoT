package com.example.lab7_20211602_iot.remote.dto;

public class RegistroRequest {
    public String dni;
    public String correo;

    public RegistroRequest(String dni, String correo) {
        this.dni = dni;
        this.correo = correo;
    }
}