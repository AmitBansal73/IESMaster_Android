package com.example.iesmaster.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AcademicProfile implements Parcelable {

    public int ProfileId;
    public  int  UniversityID,CollegeID,StreamID,SemesterID;
    public  String UserID,UniversityName,Stream,Semester, CollegeName,Name;

    public AcademicProfile() {

    }


    public AcademicProfile(Parcel in) {
        UniversityID = in.readInt();
        UniversityName = in.readString();
        StreamID = in.readInt();
        Stream = in.readString();
        Semester = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(UniversityID);
        dest.writeString(UniversityName);
        dest.writeInt(StreamID);
        dest.writeString(Stream);
        dest.writeString(Semester);

    }

    public static final Creator<AcademicProfile> CREATOR = new Creator<AcademicProfile>() {
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
