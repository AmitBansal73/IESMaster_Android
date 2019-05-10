package com.example.iesmaster.model;

public class Subject {

    String subListName;
    int subListImage;

    public Subject(String subName,int subImage)
    {
        this.subListImage=subImage;
        this.subListName=subName;
    }
    public String getsubName()
    {
        return subListName;
    }
    public int getSubImage()
    {
        return subListImage;
    }
}
