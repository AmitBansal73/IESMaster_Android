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

import com.example.iesmaster.Object.Subject;
import com.example.iesmaster.Questions.QuestionsActivity;

import java.util.ArrayList;

public class PapersActivity extends AppCompatActivity {
    GridView gridViewPaper;
    ArrayList paperList=new ArrayList<>();
    TestPaper testPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" Papers ");
        actionBar.show();

        gridViewPaper = findViewById(R.id.gridViewPaper);
        paperList.add(new Subject("Unit 1", R.drawable.a));
        paperList.add(new Subject("Unit 2", R.drawable.b));
        paperList.add(new Subject("Unit 3", R.drawable.a));
        paperList.add(new Subject("Unit 4", R.drawable.b));
        paperList.add(new Subject("Unit 5", R.drawable.a));
        paperList.add(new Subject("Unit 6", R.drawable.b));
        paperList.add(new Subject("Unit 7", R.drawable.a));
        paperList.add(new Subject("Unit 8", R.drawable.a));
        paperList.add(new Subject("Unit 9", R.drawable.b));
        paperList.add(new Subject("Unit 10", R.drawable.a));
        paperList.add(new Subject("Unit 11", R.drawable.b));
        paperList.add(new Subject("Unit 12", R.drawable.a));
        testPaper=new TestPaper(this, R.layout.gridview_years, paperList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        gridViewPaper.setAdapter(testPaper);
        gridViewPaper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PapersActivity.this, QuestionsActivity.class);
                startActivity(intent);
            }
        });
    }


    public class TestPaper extends ArrayAdapter {

        ArrayList paperList = new ArrayList<>();

        public TestPaper(Context context, int textViewResourceId, ArrayList objects) {
            super(context, textViewResourceId, objects);
            paperList = objects;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_years, null);
            TextView textView = convertView.findViewById(R.id.testYear);
            Subject tempSubject = (Subject) paperList.get(position);
            textView.setText(tempSubject.getsubName());
            return convertView;
        }
    }
}
