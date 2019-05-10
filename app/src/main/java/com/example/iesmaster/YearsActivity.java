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

import java.util.ArrayList;

public class YearsActivity extends AppCompatActivity {
    GridView gridViewYr;
    ArrayList yearList=new ArrayList<>();
    TestYear testYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_years);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Test Paper for Year ");
        actionBar.show();

        gridViewYr = findViewById(R.id.gridViewYr);
        yearList.add(new Subject("2008", R.drawable.a));
        yearList.add(new Subject("2009", R.drawable.b));
        yearList.add(new Subject("2010", R.drawable.a));
        yearList.add(new Subject("2011", R.drawable.b));
        yearList.add(new Subject("2012", R.drawable.a));
        yearList.add(new Subject("2013", R.drawable.b));
        yearList.add(new Subject("2014", R.drawable.a));
        yearList.add(new Subject("2015", R.drawable.a));
        yearList.add(new Subject("2016", R.drawable.b));
        yearList.add(new Subject("2017", R.drawable.a));
        yearList.add(new Subject("2018", R.drawable.b));
        yearList.add(new Subject("2019", R.drawable.a));

        testYear=new TestYear(this, R.layout.gridview_years, yearList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        gridViewYr.setAdapter(testYear);
        gridViewYr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(YearsActivity.this,PapersActivity.class);
                startActivity(intent);
                YearsActivity.this.finish();
            }
        });

    }



    public class TestYear extends ArrayAdapter {

        ArrayList yearList = new ArrayList<>();

        public TestYear(Context context, int textViewResourceId, ArrayList objects) {
            super(context, textViewResourceId, objects);
            yearList = objects;
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

            Subject tempSubject = (Subject) yearList.get(position);
            textView.setText( tempSubject.getsubName());
            return convertView;
        }
    }
}
