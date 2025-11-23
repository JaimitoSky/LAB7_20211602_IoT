package com.example.lab7_20211602_iot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String PATTERN = "dd/MM/yyyy";

    public static String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN, Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public static long parseDate(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN, Locale.getDefault());
        try {
            Date d = sdf.parse(text);
            return d != null ? d.getTime() : System.currentTimeMillis();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
    }
}
