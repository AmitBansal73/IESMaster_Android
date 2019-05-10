package com.example.iesmaster.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.iesmaster.model.Profile;

public class Session {




    public static boolean AddLogin(Context context, String UserLogin, String UserPassword )
    {
        try {
            SharedPreferences prefs = context.getSharedPreferences("IESMaster", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserLogin", UserLogin);
            editor.putString("UserPassword", UserPassword);
            editor.commit();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static Profile GetLogin(Context context)
    {
        Profile mProfile = new Profile();
        try {
            SharedPreferences prefs = context.getSharedPreferences("IESMaster", Context.MODE_PRIVATE);
            mProfile.UserLogin =  prefs.getString("UserLogin","");
            mProfile.UserPassword = prefs.getString("UserPassword","");

            return mProfile;
        }
        catch (Exception ex)
        {
            return null;
        }
    }




    public static boolean AddAcademicProfile(Context context, Profile myProfile)
    {
        try {
            SharedPreferences prefs = context.getSharedPreferences("IESMaster", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserID", myProfile.UserID);
            editor.putInt("UniversityID",myProfile.UniversityID);
            editor.putString("University", myProfile.University);
            editor.putInt("CollegeID", myProfile.CollegeID);
            editor.putString("College",myProfile.College);
            editor.putInt("StreamID",myProfile.StreamID);
            editor.putString("Stream",myProfile.Stream);
            editor.putInt("SemesterID",myProfile.SemesterID);
            editor.putString("Semester",myProfile.Semester);
            editor.commit();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static Profile GetAcademicProfile(Context context)
    {
        Profile mProfile = new Profile();
        try {
            SharedPreferences prefs = context.getSharedPreferences("IESMaster", Context.MODE_PRIVATE);
            mProfile.UserID =  prefs.getString("UserID","");
            mProfile.UniversityID = prefs.getInt("UniversityID",0);
            mProfile.University =  prefs.getString("University","");
            mProfile.CollegeID =  prefs.getInt("CollegeID",0);
            mProfile.College = prefs.getString("College","");
            mProfile.StreamID = prefs.getInt("StreamID",0);
            mProfile.Stream = prefs.getString("Stream","");
            mProfile.SemesterID= prefs.getInt("SemesterID",0);
            mProfile.Semester = prefs.getString("Semester","");
            return mProfile;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static void LogOff(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("IESMaster", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

    }
}
