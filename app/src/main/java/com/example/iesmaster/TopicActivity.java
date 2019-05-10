package com.example.iesmaster;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.iesmaster.model.Subject;
import com.example.iesmaster.Questions.QuestionsActivity;
import com.example.iesmaster.model.Topic;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {

    ArrayList topicList=new ArrayList<>();
    GridView gridViewTopic;
    TopicAdapter myTopicAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Topics");
        actionBar.show();

        gridViewTopic = findViewById(R.id.gridViewTopic);
        topicList.add(new Topic(1,"Introduction", "Soil"));
        topicList.add(new Topic(2,"Formation of Soils", "Soil"));
        topicList.add(new Topic(3,"Soil Types", "Soil"));
        topicList.add(new Topic(4,"Soil Classification", "Soil"));
        topicList.add(new Topic(5,"Seepage in Soil", "Soil"));


        myTopicAdaptor=new TopicAdapter(this, R.layout.grid_item_topic, topicList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        gridViewTopic.setAdapter(myTopicAdaptor);
        gridViewTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(TopicActivity.this, QuestionsActivity.class);
                startActivity(intent);
                TopicActivity.this.finish();
            }
        });



    }


    public class TopicAdapter extends ArrayAdapter {

        ArrayList yearList = new ArrayList<>();

        public TopicAdapter(Context context, int textViewResourceId, ArrayList objects) {
            super(context, textViewResourceId, objects);
            topicList = objects;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item_topic, null);
            TextView textView = convertView.findViewById(R.id.txtTopic);

            Topic tempTopic = (Topic) topicList.get(position);
            textView.setText( tempTopic.TopicName);
            return convertView;
        }
    }
}
