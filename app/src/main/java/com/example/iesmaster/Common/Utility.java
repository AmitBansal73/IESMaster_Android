package com.example.iesmaster.Common;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utility {

    static final String DATEFORMAT = "dd/MM/yyyy HH:mm:ss";
    static final String INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    static final String SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";


    public static Date StringToDate(String date)
    {
        try {
            SimpleDateFormat idf = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT);
            Date dateTime = idf.parse(date);
            return dateTime;
        }
        catch (Exception ex)
        {
            return new Date();
        }

    }
}
