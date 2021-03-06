package com.example.iesmaster.Test;

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
import com.example.iesmaster.R;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Years;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YearsActivity extends AppCompatActivity {
    GridView gridViewYr;
    List<Years> yearList=new ArrayList<>();
   // ArrayList yearList=new ArrayList<>();
    TestYearAdapter testYear;
    TextView txtCollage,txtStream,txtError;
    AcademicProfile myAcademic;
    ProgressBar progressBar;

    int UniverID,StreamID , SemesterID, SubjectID;
    String subjectName, UniversityName, StreamName;
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
        txtError = findViewById(R.id.txtError);
        myAcademic = Session.GetAcademicProfile(getApplicationContext());
        gridViewYr = findViewById(R.id.gridViewYr);
      //  testYear = new TestYear(this, R.layout.gridview_subjects,yearList );
      //  gridViewYr.setAdapter(testYear);

        Intent intent = getIntent();
        UniverID = intent.getIntExtra("UniversityID",0);
        StreamID = intent.getIntExtra("StreamID", 0);
        SubjectID = intent.getIntExtra("SubjectID", 0);
        subjectName = intent.getStringExtra("SubjectName");
        UniversityName = intent.getStringExtra("UniversityName");
        StreamName = intent.getStringExtra("StreamName");

        txtCollage.setText(UniversityName +", " + StreamName);
        txtStream.setText(subjectName);

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
                Years Yr = (Years) yearList.get(position);
                int year = Yr.year;
                Intent intent = new Intent(YearsActivity.this, UnitActivity.class);
                intent.putExtra("UniversityID", UniverID);
                intent.putExtra("StreamID", StreamID);
                intent.putExtra("SubjectID", SubjectID);
                intent.putExtra("SubjectName", subjectName);
                intent.putExtra("year", year);
                intent.putExtra("UniversityName", UniversityName);
                 intent.putExtra("StreamName", StreamName);
                startActivity(intent);
                //YearsActivity.this.finish();
            }
        });

        GetPapersYears();
    }

    public void GetPapersYears(){
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/Paper/GetYear/"+UniverID+"/"+StreamID+"/"+ SubjectID;
         //String url = Constants.Application_URL+ "/api/Paper/1006/1002/Compilers";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);

                    try {
                        int x = response.length();
                        if (x>0) {
                            for (int i = 0; i < x; i++) {

                                JSONObject jObj = response.getJSONObject(i);
                                Years years = new Years();

                                years.year = jObj.getInt("year");
                                // int YearID = jObj.getInt("yearId");
                                yearList.add(years);
                            }
                            // progressBar.setVisibility(View.GONE);
                            testYear.notifyDataSetChanged();
                        }else {
                            gridViewYr.setVisibility(View.GONE);
                            txtError.setVisibility(View.VISIBLE);
                            txtError.setText("No data Found");
                        }
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
                   progressBar.setVisibility(View.GONE);

                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, 2, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
            progressBar.setVisibility(View.GONE);
        }
    }

    public class TestYearAdapter extends ArrayAdapter {

        List<Years> yearList = new ArrayList<>();

        public TestYearAdapter(Context context, int textViewResourceId, List<Years> objects) {
            super(context, textViewResourceId, objects);
            yearList = objects;
        }
        @Override
        public int getCount() {
            return yearList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_years, null);
            TextView textView = convertView.findViewById(R.id.testYear);
            Years tempyear = (Years) yearList.get(position);
            textView.setText(Integer.toString(tempyear.getYear()));
            return convertView;
        }
    }
}
