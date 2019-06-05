package com.example.iesmaster.Common;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;

public class Session {

    private static final String SESSION_NAME = "IESMaster";



    public static boolean AddProfile(Context context, Profile UserProfile)
    {
        try {
            SharedPreferences prefs = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserLogin", UserProfile.UserLogin);
            editor.putString("UserName", UserProfile.UserName);
            editor.putString("MobileNumber", UserProfile.MobileNumber);
            editor.putString("ProfileImage", UserProfile.ProfileImage);
            editor.putString("UserID", UserProfile.UserID.toString());
            editor.putString("UserPassword", UserProfile.UserPassword);
            editor.commit();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static Profile GetProfile(Context context)
    {
        Profile mProfile = new Profile();
        try {
            SharedPreferences prefs = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
            mProfile.UserLogin =  prefs.getString("UserLogin","");
            mProfile.UserName = prefs.getString("UserName","");
            mProfile.MobileNumber =  prefs.getString("MobileNumber","");
            mProfile.ProfileImage = prefs.getString("ProfileImage","");
            mProfile.UserID =  prefs.getString("UserID","");
            mProfile.UserPassword = prefs.getString("UserPassword","");

            return mProfile;
        }
        catch (Exception ex)
        {
            return null;
        }
    }




    public static boolean AddAcademicProfile(Context context, AcademicProfile myProfile)
    {
        try {
            SharedPreferences prefs = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("UniversityID",myProfile.UniversityID);
            editor.putString("University", myProfile.UniversityName);
            editor.putInt("CollegeID", myProfile.CollegeID);
            editor.putString("College",myProfile.CollegeName);
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

    public static AcademicProfile GetAcademicProfile(Context context)
    {
        AcademicProfile mProfile = new AcademicProfile();
        try {
            SharedPreferences prefs = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
            mProfile.UniversityID = prefs.getInt("UniversityID",0);
            mProfile.UniversityName =  prefs.getString("University","");
            mProfile.CollegeID =  prefs.getInt("CollegeID",0);
            mProfile.CollegeName = prefs.getString("College","");
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
        SharedPreferences prefs = context.getSharedPreferences(SESSION_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();

    }
}
