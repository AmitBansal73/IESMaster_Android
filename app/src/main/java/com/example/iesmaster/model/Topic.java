package com.example.iesmaster.model;

public class Topic {

    public int TopicId;
    public String TopicName;
    public String SubjectName;
    public int Status;

    public Topic()
    {}

    public Topic(int Id, String topicName,String subName, int Status)
    {
        this.TopicId = Id;
        this.TopicName=topicName;
        this.SubjectName=subName;
        this.Status = Status;
    }
}
