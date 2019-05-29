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
import com.example.iesmaster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UnitActivity extends AppCompatActivity {
    GridView gridViewUnit;
    List<Integer> unitList=new ArrayList<>();
    TestPaper testUnitAdapter;
    TextView txtError;
    ProgressBar progressBar;

    int UniversityID,StreamID , Year, SubjectID;
    String subjectName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Unit of Electrical");
        actionBar.show();

        gridViewUnit = findViewById(R.id.gridViewPaper);
        testUnitAdapter = new TestPaper(this, R.layout.grid_papers,unitList );
        gridViewUnit.setAdapter(testUnitAdapter);
        txtError = findViewById(R.id.txtError);
        Intent intent = getIntent();
        UniversityID = intent.getIntExtra("UniversityID",0);
        StreamID = intent.getIntExtra("StreamID", 0);
        Year = intent.getIntExtra("year", 0);
        subjectName = intent.getStringExtra("SubjectName");


     /*   paperList.add(new Subject("Unit 1", R.drawable.a));
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

        testPaper=new TestPaper(this, R.layout.grid_papers, paperList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        gridViewPaper.setAdapter(testPaper);*/



        gridViewUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int Unit = (int)unitList.get(position);

                Intent intent = new Intent(UnitActivity.this, TestPaperActivity.class);
                intent.putExtra("UniversityID", UniversityID);
                intent.putExtra("StreamID", StreamID);
                // intent.putExtra("SemesterID", SemesterID);
                intent.putExtra("SubjectName", subjectName);
                intent.putExtra("year", Year);
                intent.putExtra("unit", Unit);
                startActivity(intent);
                //UnitActivity.this.finish();
            }
        });
        GetPaperUnit();
    }


    public void GetPaperUnit(){

       // progressBar.setVisibility(View.VISIBLE);
        //String url = Constants.Application_URL+ "/api/Paper/1006/1002/Compilers/2017";
         String url = Constants.Application_URL+ "/api/Paper/" +UniversityID+ "/"+StreamID+"/"+subjectName+"/"+Year;
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {
                        int x = response.length();
                        if (x>0) {
                            for (int i = 0; i < x; i++) {

                                JSONObject jObj = response.getJSONObject(i);
                                 int unit = jObj.getInt("Unit");
                                //int UnitID = jObj.getInt("yearId");
                                unitList.add(unit);
                            }
                            //progressBar.setVisibility(View.GONE);
                            testUnitAdapter.notifyDataSetChanged();
                        }else {
                            gridViewUnit.setVisibility(View.GONE);
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

    public class TestPaper extends ArrayAdapter {

        ArrayList<Integer> unitList = new ArrayList<>();
        public TestPaper(Context context, int textViewResourceId, List<Integer> objects) {
            super(context, textViewResourceId, objects);
            unitList = (ArrayList<Integer>) objects;
            //unitList = objects;
        }
        @Override
        public int getCount() {
            return unitList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_papers, null);
            TextView textView = convertView.findViewById(R.id.testPapers);
            int tempUnit = (int) unitList.get(position);
            textView.setText(Integer.toString(tempUnit));
            return convertView;
        }
    }


}
