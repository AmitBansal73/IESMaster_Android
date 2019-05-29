package com.example.iesmaster.Common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;
import com.example.iesmaster.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class DataAccess {
    private  static final String DATABASE_NAME = "GAP.db";
    private static final  String PROFILE_TABLE = "PROFILE_TABLE";
    private static final  String FAVOURITE_TABLE = "FAVOURITE_TABLE";
    private static final String TABLE_CREATE_PROFILE = "CREATE TABLE IF NOT EXISTS "
            + PROFILE_TABLE
            + "( profile_id INTEGER PRIMARY KEY AUTOINCREMENT, semester VARCHAR(60), stream_id INTEGER, stream VARCHAR(60), university_id INTEGER , university VARCHAR(60));";

    private static final String TABLE_CREATE_FAVOURITE = "CREATE TABLE IF NOT EXISTS "
            + FAVOURITE_TABLE
            + "( favourite_id INTEGER PRIMARY KEY AUTOINCREMENT, topic VARCHAR(20), subject VARCHAR(20));";


    private static  final  int DATABASE_VERSION =2;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase myDatabase;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        //region General Function

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_PROFILE);
            db.execSQL(TABLE_CREATE_FAVOURITE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

              db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE);
            // db.execSQL("DROP TABLE IF EXISTS " + PROJECT_TABLE);
            // db.execSQL("DROP TABLE IF EXISTS " + SERVER_ACTIVITY_TABLE);

            onCreate(db);

        }
        // endregion
    }

    public DataAccess(Context mCtx) {
        this.mCtx = mCtx;
    }

    public DataAccess open() throws SQLException
    {
        try {
            mDBHelper = new DatabaseHelper(mCtx);

            myDatabase = mDBHelper.getWritableDatabase();

            return this;
        }
        catch (Exception ex)
        {
            int b=1;
            return null;
        }

    }


    //region Profile Function


    public long InsertProfile( AcademicProfile trans)
    {
        try
        {
            myDatabase.execSQL(TABLE_CREATE_PROFILE);
            ContentValues initialValues = new ContentValues();
            //initialValues.put("profile_id",trans.ProfileId);
            initialValues.put("semester",trans.Semester);
            initialValues.put("stream_id",trans.StreamID);
            initialValues.put("stream",trans.Stream);
            initialValues.put("university_id",trans.UniversityID);
            initialValues.put("university",trans.UniversityName);

            long result  = myDatabase.insert(PROFILE_TABLE, null,initialValues);

            return  result;
        }
        catch (Exception ex)
        {
            return 0;
        }

    }


    public List<AcademicProfile> GetProfiles()
    {
        List<AcademicProfile> academicList = new ArrayList<>();
        AcademicProfile academic;

        try{
            String selectQuery = "SELECT * FROM " + PROFILE_TABLE +" order by profile_id asc";

            Cursor c = myDatabase.rawQuery(selectQuery,null);
            if(c!=null && c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        academic = new AcademicProfile();
                        academic.ProfileId = c.getInt(c.getColumnIndex("profile_id"));
                        academic.Semester = c.getString(c.getColumnIndex("semester"));
                        academic.StreamID = c.getInt(c.getColumnIndex("stream_id"));
                        academic.Stream = c.getString(c.getColumnIndex("stream"));
                        academic.UniversityID = c.getInt(c.getColumnIndex("university_id"));
                        academic.UniversityName = c.getString(c.getColumnIndex("university"));
                        academicList.add(academic);
                    }
                    while (c.moveToNext());
                }
            }
        }
        catch (Exception ex)
        {
            int a=1;
        }
        return academicList;
    }



    // endregion


    //region Favourite Function


    public long InsertFavourite( Topic favourite)
    {
        try
        {
            myDatabase.execSQL(TABLE_CREATE_PROFILE);
            ContentValues initialValues = new ContentValues();
            initialValues.put("topic",favourite.TopicName);
            initialValues.put("subject",favourite.SubjectName);
            long result  = myDatabase.insert(FAVOURITE_TABLE, null,initialValues);

            return  result;
        }
        catch (Exception ex)
        {
            return 0;
        }

    }


    public List<Topic> GetFavourite()
    {
        List<Topic> favouriteList = new ArrayList<>();
        Topic favourite;

        try{
            String selectQuery = "SELECT * FROM " + FAVOURITE_TABLE +" order by favourite_id asc";

            Cursor c = myDatabase.rawQuery(selectQuery,null);
            if(c!=null && c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        favourite = new Topic();
                        favourite.TopicId = c.getInt(c.getColumnIndex("favourite_id"));
                        favourite.TopicName = c.getString(c.getColumnIndex("topic"));
                        favourite.SubjectName = c.getString(c.getColumnIndex("subject"));
                        favouriteList.add(favourite);
                    }
                    while (c.moveToNext());
                }
            }
        }
        catch (Exception ex)
        {
            int a=1;
        }
        return favouriteList;
    }

    // endregion
}
