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
import com.example.iesmaster.model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UnitActivity extends AppCompatActivity {
    GridView gridViewPaper;
    ArrayList paperList=new ArrayList<>();
    TestPaper testPaper;
    ArrayAdapter<String> adapterUnit;
    ProgressBar progressBar;
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

        gridViewPaper = findViewById(R.id.gridViewPaper);
       // testPaper = new TestPaper(this, R.layout.gridview_subjects,paperList );
      //  gridViewPaper.setAdapter(testPaper);


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
        testPaper=new TestPaper(this, R.layout.grid_papers, paperList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        gridViewPaper.setAdapter(testPaper);



        gridViewPaper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UnitActivity.this, TestPaperActivity.class);
                startActivity(intent);
                //UnitActivity.this.finish();
            }
        });
    }


    public void GetPaperUnit(){

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

                            String Unit = jObj.getString("Year");
                            int UnitID = jObj.getInt("yearId");
                            paperList.add(Unit);
                        }
                        progressBar.setVisibility(View.GONE);
                        adapterUnit.notifyDataSetChanged();

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
            convertView = inflater.inflate(R.layout.grid_papers, null);
            TextView textView = convertView.findViewById(R.id.testPapers);
            Subject tempSubject = (Subject) paperList.get(position);
            textView.setText(tempSubject.getsubName());
            return convertView;
        }
    }
}
