package com.example.iesmaster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iesmaster.Object.Subject;

import java.util.ArrayList;

public class SubjectAndTestActivity extends AppCompatActivity {

    GridView gridView,gridViewUniversity;
    ArrayList subList=new ArrayList<>();
    ArrayList univList =new ArrayList<>();
    TestSubject myAdapter;
    UniversityAdpter universityAdpter;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_and_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" IES Master ");
        actionBar.show();
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubjectAndTestActivity.this,YearsActivity.class);
                startActivity(intent);
            }
        });
        gridViewUniversity = findViewById(R.id.gridViewUniversity);

        univList.add(new Subject("UPTU", R.drawable.a));
        univList.add(new Subject("Punjab University", R.drawable.b));
        univList.add(new Subject("Amity University", R.drawable.a));
        univList.add(new Subject("CCS University", R.drawable.b));

        universityAdpter=new UniversityAdpter(this, R.layout.grid_university, univList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        gridViewUniversity.setAdapter(universityAdpter);

        gridView = findViewById(R.id.gridView);
        subList.add(new Subject("Math", R.drawable.a));
        subList.add(new Subject("Physics", R.drawable.b));
        subList.add(new Subject("Chemistry", R.drawable.a));
        subList.add(new Subject("ThermoDynamics", R.drawable.b));
        subList.add(new Subject("Signal Processing", R.drawable.a));
        subList.add(new Subject("Engg Drawing", R.drawable.b));
        subList.add(new Subject("Computer Architecture", R.drawable.a));
        myAdapter=new TestSubject(this, R.layout.gridview_subjects, subList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        gridView.setAdapter(myAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SubjectAndTestActivity.this,YearsActivity.class);
                startActivity(intent);
            }
        });
    }

    public class TestSubject extends ArrayAdapter {

        ArrayList subList = new ArrayList<>();

        public TestSubject(Context context, int textViewResourceId, ArrayList objects) {
            super(context, textViewResourceId, objects);
            subList = objects;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_subjects, null);
            TextView textView = convertView.findViewById(R.id.testName);

            Subject tempSubject = (Subject) subList.get(position);
            textView.setText(tempSubject.getsubName());
            return convertView;
        }
    }

    public class UniversityAdpter extends ArrayAdapter {

        ArrayList univList = new ArrayList<>();

        public UniversityAdpter(Context context, int textViewResourceId, ArrayList objects) {
            super(context, textViewResourceId, objects);
            univList = objects;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_university, null);
            TextView textView = convertView.findViewById(R.id.univName);

            Subject tempSubject = (Subject) univList.get(position);
            textView.setText(tempSubject.getsubName());
            return convertView;
        }
    }
}