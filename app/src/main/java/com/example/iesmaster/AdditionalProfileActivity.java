package com.example.iesmaster;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.Common.DataAccess;
import com.example.iesmaster.model.AcademicProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdditionalProfileActivity extends AppCompatActivity {

    List<String> universityList= new ArrayList<>();
    HashMap<String,Integer> universityHashMap = new HashMap<>();

    List<String> streamList= new ArrayList<>();
    HashMap<String,Integer> streamHashMap = new HashMap<>();

    List<String> semesterList= new ArrayList<>();
    HashMap<String,Integer> semesterHashMap = new HashMap<>();

    ArrayAdapter<String> adapterUniversity;
    ArrayAdapter<String> adapterStream;
    ArrayAdapter<String> adapterSemester;
    boolean IsResult = false;

    EditText txtUniversity,txtStream;
    ListView listViewUniversity,listViewStream;
    Spinner spinnerSemester;
    Button btnSave;
  int selectedUniversity,selectedStream,selectedSemester;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_profile);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Academic Profile");
        actionBar.show();
        progressBar = findViewById(R.id.progressBar);
        closeKeyboard();
        Intent intent = getIntent();
        IsResult = intent.getBooleanExtra("IsResult", false);



        txtUniversity = findViewById(R.id.txtUniversity);
        txtStream = findViewById(R.id.txtStream);

        listViewUniversity = findViewById(R.id.listViewUniversity);
        listViewStream = findViewById(R.id.listViewStream);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        btnSave = findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcademicProfile profile = new AcademicProfile();
                profile.UniversityName = txtUniversity.getText().toString();
                profile.UniversityID = selectedUniversity;
                profile.Stream = txtStream.getText().toString();
                profile.StreamID = selectedStream;
                profile.Semester = spinnerSemester.getSelectedItem().toString();


                DataAccess dataAccess = new DataAccess(getApplicationContext());
                dataAccess.open();
                if(dataAccess.IfProfileExist(profile))
                {
                    Toast.makeText(getApplicationContext(), "Profile already added", Toast.LENGTH_LONG).show();
                }
                else {
                    dataAccess.InsertProfile(profile);

                    Intent intent = new Intent();
                    intent.putExtra("IsProfile", true);
                    intent.putExtra("Profile", profile);
                    setResult(100, intent);
                    AdditionalProfileActivity.this.finish();//finishing activity
                }
            }
        });

        setUniversitySpinner();
        setSemesterSpinner();
    }



    public void setUniversitySpinner()
    {

        adapterUniversity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, universityList);
        adapterUniversity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewUniversity.setAdapter(adapterUniversity);
        txtUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewUniversity.setVisibility(View.VISIBLE);

            }
        });

        txtUniversity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                adapterUniversity.getFilter().filter(cs);
                listViewUniversity.setVisibility(View.VISIBLE);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listViewUniversity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String University = (String) listViewUniversity.getItemAtPosition(position);
                selectedUniversity = universityHashMap.get(University);
                txtUniversity.setText(University);
                listViewUniversity.setVisibility(View.GONE);
                setStreamSpinner(selectedUniversity);

            }
        });

        GetUniversityData();
    }


    public void GetUniversityData() {
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL + "/api/Paper/College";
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        int x = response.length();
                        for (int i = 0; i < x; i++) {

                            JSONObject jObj = response.getJSONObject(i);

                            String UniversityName = jObj.getString("UniversityName");
                            int UniversityID = jObj.getInt("UniversityID");
                            if (!universityHashMap.containsKey(UniversityName)) {
                                universityHashMap.put(UniversityName, UniversityID);
                                universityList.add(UniversityName);
                            }
                        }

                        adapterUniversity.notifyDataSetChanged();

                    } catch (JSONException e) {
                        int a = 1;
                    } catch (Exception ex) {
                        int a = 1;
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
        } catch (Exception ex) {
            int a = 1;
            progressBar.setVisibility(View.GONE);
        }

    }





    private void setStreamSpinner( int UnivID)
    {
        adapterStream = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, streamList);
        adapterStream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewStream.setAdapter(adapterStream);
        txtStream.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    listViewStream.setVisibility(View.VISIBLE);
                }else {
                    listViewStream.setVisibility(View.GONE);
                }
            }
        });

        txtStream.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                AdditionalProfileActivity.this.adapterStream.getFilter().filter(cs);
                listViewStream.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listViewStream.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stream = listViewStream.getItemAtPosition(position).toString();
                txtStream.setText(stream);
                selectedStream = streamHashMap.get(stream);
                setSemesterSpinner();
                listViewStream.setVisibility(View.GONE);
            }
        });

        GetStreamData(UnivID);

    }


    private void GetStreamData(int UnivID)
    {
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/Paper/University/"+ UnivID;
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        int x = response.length();

                        for (int i = 0; i <x; i++) {
                            JSONObject jObj = response.getJSONObject(i);
                            String Stream = jObj.getString("StreamName");
                            int StreamID = jObj.getInt("StreamID");
                            streamHashMap.put(Stream, StreamID);
                            streamList.add(Stream);
                        }
                        adapterStream.notifyDataSetChanged();
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
            progressBar.setVisibility(View.GONE);
        }
    }



    private void setSemesterSpinner()
    {
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/Semester/All";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        int x = response.length();
                        for (int i = 0; i <x; i++) {

                            JSONObject jObj = response.getJSONObject(i);

                            String SemesterName = jObj.getString("SemesterName");
                            int SemesterID = jObj.getInt("SemID");
                            semesterHashMap.put(SemesterName, SemesterID);
                            semesterList.add(SemesterName);
                        }
                       // progressBar.setVisibility(View.GONE);
                        adapterSemester.notifyDataSetChanged();

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
            progressBar.setVisibility(View.GONE);
        }

        adapterSemester = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, semesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(adapterSemester);

        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Semester = spinnerSemester.getSelectedItem().toString();
                selectedSemester= semesterHashMap.get(Semester);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  spinnerSemester.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Semester = spinnerSemester.getSelectedItem().toString();
                selectedSemester= semesterHashMap.get(Semester);
            }
        }); */

    }



    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public void onBackPressed() {
        if(!IsResult) {

            AdditionalProfileActivity.this.finish();
        }
        else {

            AcademicProfile profile = new AcademicProfile();
            Intent intent = new Intent();
            intent.putExtra("IsProfile", false);
            intent.putExtra("Profile", profile);
            setResult(100, intent);
            AdditionalProfileActivity.this.finish();//finishing activity




        }
    }
}
