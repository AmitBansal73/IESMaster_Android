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
    TestYear testYear;
    TextView txtCollage,txtStream;
    AcademicProfile myAcademic;
    ArrayAdapter<String> adapterYear;
    Subject subject;
    ProgressBar progressBar;
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

        myAcademic = Session.GetAcademicProfile(getApplicationContext());
        gridViewYr = findViewById(R.id.gridViewYr);
      //  testYear = new TestYear(this, R.layout.gridview_subjects,yearList );
      //  gridViewYr.setAdapter(testYear);


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

                Intent intent = new Intent(YearsActivity.this, UnitActivity.class);
                startActivity(intent);
                //YearsActivity.this.finish();
            }
        });

    }

    public void GetPapersYears(){
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/Question/univ/1000/Stream/1001/Subject/1001";
       // String url = Constants.Application_URL+ "/api/Question/univ/" +myAcademic.UniversityID+ "/Stream/"+myAcademic.StreamID+"/Subject/"+subject.SubjectID;
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {
                        int x = response.length();
                        for (int i = 0; i <x; i++) {

                            JSONObject jObj = response.getJSONObject(i);

                            int Year = jObj.getInt("Year");
                            int YearID = jObj.getInt("yearId");
                            yearList.add(Year);
                        }
                        progressBar.setVisibility(View.GONE);
                        adapterYear.notifyDataSetChanged();

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
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
        }
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
