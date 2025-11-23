package com.example.lab7_20211602_iot.util;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    public static void show(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
