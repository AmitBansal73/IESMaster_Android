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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YearsActivity extends AppCompatActivity {
    GridView gridViewYr;
    ArrayList yearList=new ArrayList<>();
    TestYearAdapter testYear;
    TextView txtCollage,txtStream;
    AcademicProfile myAcademic;
    ArrayAdapter<String> adapterYear;
    Subject subject;
    ProgressBar progressBar;

    int CollegeID,StreamID , SemesterID, SubjectID;
    String subjectName;
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
        txtStream = findViewById(R.id.txtStream);
        txtCollage = findViewById(R.id.txtCollage);
        progressBar = findViewById(R.id.progressBar);

        myAcademic = Session.GetAcademicProfile(getApplicationContext());
        gridViewYr = findViewById(R.id.gridViewYr);
      //  testYear = new TestYear(this, R.layout.gridview_subjects,yearList );
      //  gridViewYr.setAdapter(testYear);

        Intent intent = getIntent();
        CollegeID = intent.getIntExtra("CollegeID",0);
        StreamID = intent.getIntExtra("StreamID", 0);
       // SemesterID = intent.getIntExtra("SemesterID", 0);
        subjectName = intent.getStringExtra("SubjectName");

        /*
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
        */


        testYear=new TestYearAdapter(this, R.layout.gridview_years, yearList);
        gridViewYr.setAdapter(testYear);
        gridViewYr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int Year = (int)yearList.get(position);

                Intent intent = new Intent(YearsActivity.this, UnitActivity.class);
                intent.putExtra("CollegeID", CollegeID);
                intent.putExtra("StreamID", StreamID);
               // intent.putExtra("SemesterID", SemesterID);
                intent.putExtra("SubjectName", subjectName);
                intent.putExtra("year", Year);
                startActivity(intent);
                //YearsActivity.this.finish();
            }
        });

        GetPapersYears();

    }

    public void GetPapersYears(){
        //progressBar.setVisibility(View.VISIBLE);
       // String url = Constants.Application_URL+ "/api/Paper/"+CollegeID+"/"+StreamID+"/"+ subjectName;
        String url = Constants.Application_URL+ "/api/Paper/1006/1002/Compilers";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {
                        int x = response.length();
                        for (int i = 0; i <x; i++) {

                            JSONObject jObj = response.getJSONObject(i);

                            int Year = jObj.getInt("year");
                           // int YearID = jObj.getInt("yearId");
                            yearList.add(Year);
                        }
                       // progressBar.setVisibility(View.GONE);
                        testYear.notifyDataSetChanged();

                    } catch (JSONException e) {
                        int a=1;
                    }
                    catch (Exception ex)
                    {
                        int a=1;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // progressBar.setVisibility(View.GONE);

                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
        }
    }



    public class TestYearAdapter extends ArrayAdapter {

        ArrayList yearList = new ArrayList<>();

        public TestYearAdapter(Context context, int textViewResourceId, ArrayList objects) {
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
            textView.setText((Integer) yearList.get(position));
            return convertView;
        }
    }
}
