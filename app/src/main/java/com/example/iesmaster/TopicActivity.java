package com.example.iesmaster;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
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

import com.example.iesmaster.Common.DataAccess;
import com.example.iesmaster.model.Subject;
import com.example.iesmaster.Questions.QuestionsActivity;
import com.example.iesmaster.model.Topic;
import com.example.iesmaster.model.mock_data;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends AppCompatActivity {

    List<Topic> topicList=new ArrayList<>();
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
        actionBar.setTitle("Topics of Building Material");
        actionBar.show();

        gridViewTopic = findViewById(R.id.gridViewTopic);
        topicList = mock_data.GetTopics();


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

                Topic selectedTopic = topicList.get(position);
                DataAccess dataAccess = new DataAccess(getApplicationContext());
                dataAccess.open();
                dataAccess.InsertFavourite(selectedTopic);

                Intent intent = new Intent(TopicActivity.this, TestPaperActivity.class);
                startActivity(intent);
                TopicActivity.this.finish();
            }
        });

    }


    public class TopicAdapter extends ArrayAdapter {

        ArrayList yearList = new ArrayList<>();

        public TopicAdapter(Context context, int textViewResourceId, List<Topic> objects) {
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
