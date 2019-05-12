package com.example.iesmaster.model;

import com.example.iesmaster.R;

import java.util.ArrayList;
import java.util.List;

public class mock_data {

    public static List<Topic> GetTopics()
    {
        List<Topic> topicList = new ArrayList<>();

        Topic tempTopic = new Topic(1,"Introduction","Soil Mechanics",1);
        topicList.add(tempTopic);

        tempTopic = new Topic(2,"Formation of Soils","Soil Mechanics",2);
        topicList.add(tempTopic);

        tempTopic = new Topic(3,"Soil Types","Soil Mechanics",3);
        topicList.add(tempTopic);

        tempTopic = new Topic(4,"Soil Classification","Soil Mechanics",2);
        topicList.add(tempTopic);

        tempTopic = new Topic(3,"Seepage in Soil","Soil Mechanics",2);
        topicList.add(tempTopic);

        return topicList;
    }

    public static List<Test> GetTestData(){

        List<Test> listTest = new ArrayList<>();

        Test test1 = new Test();
        test1.DifficulyLevel = "Difficult";
        test1.StreamID = 2;
        test1.Cost = 30;
        test1.Question = 20;
        test1.Status = 1;
        listTest.add(test1);

        test1 = new Test();
        test1.DifficulyLevel = "Medium";
        test1.StreamID = 2;
        test1.Cost = 20;
        test1.Question = 25;
        test1.Status = 2;
        listTest.add(test1);

        test1 = new Test();
        test1.DifficulyLevel = "Easy";
        test1.StreamID = 2;
        test1.Cost = 10;
        test1.Question = 30;
        listTest.add(test1);
        test1.Status = 2;

        test1 = new Test();
        test1.DifficulyLevel = "Medium";
        test1.StreamID = 2;
        test1.Cost = 20;
        test1.Question = 25;
        listTest.add(test1);
        test1.Status = 1;

        test1 = new Test();
        test1.DifficulyLevel = "Medium";
        test1.StreamID = 2;
        test1.Cost = 20;
        test1.Question = 25;
        listTest.add(test1);
        test1.Status = 1;

        test1 = new Test();
        test1.DifficulyLevel = "Easy";
        test1.StreamID = 2;
        test1.Cost = 10;
        test1.Question =30;
        listTest.add(test1);
        test1.Status = 3;

        return listTest;
    }

    public static List<Topic> GetFavouriteTopics()
    {
        List<Topic> topicList = new ArrayList<>();

        Topic tempTopic = new Topic(1,"Doubly Reinforced Beams","Concrete Structure",1);
        topicList.add(tempTopic);

        tempTopic = new Topic(2,"Water Level Stabilization","Irrigation",2);
        topicList.add(tempTopic);

        tempTopic = new Topic(3,"irrigation","Transportation",3);
        topicList.add(tempTopic);


        return topicList;
    }

    public static List<Subject> GetSubjects()
    {
        List<Subject> listSubjectType = new ArrayList<>();

        Subject sub1 = new Subject();
        sub1.SubjectName = "Engg Drawing";
        sub1.SubjectImage = R.drawable.gradient_1;
        listSubjectType.add(sub1);


        sub1 = new Subject();
        sub1.SubjectName = "Fluid Mechanics";
        sub1.SubjectImage = R.drawable.gradient_2;
        listSubjectType.add(sub1);

        sub1 = new Subject();
        sub1.SubjectName = "ThermoDynamics";
        sub1.SubjectImage = R.drawable.b;
        listSubjectType.add(sub1);


        sub1 = new Subject();
        sub1.SubjectName = "Signal Processing";
        sub1.SubjectImage = R.drawable.gradient_2;
        listSubjectType.add(sub1);


        sub1 = new Subject();
        sub1.SubjectName = "Maths";
        sub1.SubjectImage = R.drawable.a;
        listSubjectType.add(sub1);

        sub1 = new Subject();
        sub1.SubjectName = "Physics";
        sub1.SubjectImage = R.drawable.a;
        listSubjectType.add(sub1);

        return listSubjectType;
    }

    public static List<Subject> GetFavourite()
    {
        List<Subject> listSubjectType = new ArrayList<>();

        Subject sub1 = new Subject();
        sub1.SubjectName = "Engg Drawing";
        sub1.SubjectImage = R.drawable.gradient_1;
        listSubjectType.add(sub1);


        sub1 = new Subject();
        sub1.SubjectName = "Fluid Mechanics";
        sub1.SubjectImage = R.drawable.gradient_2;
        listSubjectType.add(sub1);

        sub1 = new Subject();
        sub1.SubjectName = "ThermoDynamics";
        sub1.SubjectImage = R.drawable.b;
        listSubjectType.add(sub1);


        sub1 = new Subject();
        sub1.SubjectName = "Signal Processing";
        sub1.SubjectImage = R.drawable.gradient_2;
        listSubjectType.add(sub1);


        return listSubjectType;
    }
}
