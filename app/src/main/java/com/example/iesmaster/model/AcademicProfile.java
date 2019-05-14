package com.example.iesmaster.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AcademicProfile implements Parcelable {

    public int ProfileId;
    public  int  UniversityID,CollegeID,StreamID,SemesterID;
    public  String UserID,UniversityName,Stream,Semester, CollegeName;

    public AcademicProfile() {

    }


    public AcademicProfile(Parcel in) {
        UniversityName = in.readString();
        Stream = in.readString();
        Semester = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UniversityName);
        dest.writeString(Stream);
        dest.writeString(Semester);

    }

    public static final Parcelable.Creator<AcademicProfile> CREATOR = new Creator<AcademicProfile>() {
        @Override
        public AcademicProfile createFromParcel(Parcel parcel) {
            return new AcademicProfile(parcel);
        }

        @Override
        public AcademicProfile[] newArray(int size) {
            return new AcademicProfile[size];
        }
    };
}
