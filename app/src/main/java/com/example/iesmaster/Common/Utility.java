package com.example.iesmaster.Common;

import android.text.format.Time;

import java.text.DateFormat;
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

    public static String CurrentDate()
    {
        try {
            DateFormat formatter = new SimpleDateFormat(INPUT_DATE_FORMAT);
            //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = formatter.format(new Date());
            return currentDate;
        }
        catch (Exception ex)
        {
            return "";
        }

    }
}
