package com.example.lab7_20211602_iot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String format(long ms) {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(ms));
    }
}
