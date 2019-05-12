package com.example.iesmaster.model;

public class Subject {

    String SubjectName;
    int SubjectImage;

    public Subject(){}

    public Subject(String subName,int subImage)
    {
        this.SubjectName=subName;
        this.SubjectImage=subImage;
    }
    public String getsubName()
    {
        return SubjectName;
    }
    public int getSubImage()
    {
        return SubjectImage;
    }
}
