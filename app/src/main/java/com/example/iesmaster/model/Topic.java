package com.example.iesmaster.model;

public class Topic {

    public int TopicId;
    public String TopicName;

    public String SubjectName;


    public Topic(int Id, String topicName,String subName)
    {
        this.TopicId = Id;
        this.TopicName=topicName;
        this.SubjectName=subName;
    }
}
