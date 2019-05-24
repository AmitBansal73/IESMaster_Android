package com.example.iesmaster.model;

public class Subject {

   public String SubjectName;
   public int SubjectImage, SubjectID;

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
