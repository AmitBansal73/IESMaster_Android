package com.example.iesmaster.Test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.iesmaster.AcademicProfileActivity;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.R;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Subject;
import com.example.iesmaster.model.mock_data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    Button btnNext;
    Spinner spinnerSubject;
    ArrayAdapter<Subject> adapterSubjectType;
    List<Subject> listSubjectType= new ArrayList<>();
    HashMap<String,Integer> hashSubjectType = new HashMap<>();
    int selectedSubjectType;

    TextView txtUniversity,txtCollege,txtStream,txtyear,txtMessage;
    AcademicProfile myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar =  findViewById(R.id.toolbar);
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
                Intent intent = new Intent(SubjectActivity.this, TestPaperActivity.class);
                startActivity(intent);
            }
        });

        listSubjectType = mock_data.GetSubjects();

        txtUniversity = findViewById(R.id.txtUniversity);
        txtCollege = findViewById(R.id.txtCollege);
        txtStream = findViewById(R.id.txtStream);
        txtyear = findViewById(R.id.txtyear);
        txtMessage = findViewById(R.id.txtMessage);
        myProfile = Session.GetAcademicProfile(getApplicationContext());
         if(myProfile.UniversityID != 0)
         {
             txtMessage.setText("Please select the subject from below list to see the available papers");
             txtUniversity.setText(myProfile.UniversityName);
             txtCollege.setText(myProfile.CollegeName);
             txtStream.setText(myProfile.Stream);
             txtyear.setText(myProfile.Semester);

         }



        spinnerSubject = findViewById(R.id.Subject);
        adapterSubjectType = new ArrayAdapter<Subject>(this, android.R.layout.simple_spinner_item, listSubjectType);
        adapterSubjectType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapterSubjectType);

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject strSubject = (Subject) spinnerSubject.getSelectedItem();
                selectedSubjectType= hashSubjectType.get(strSubject.getsubName());
               // GetAccomodationType(selectedInventoryType);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {

            Intent profileIntent = new Intent(SubjectActivity.this, AcademicProfileActivity.class);
            startActivity(profileIntent);
        }
        else if (id == R.id.action_LogOff)
        {

            AlertDialog.Builder builder= new AlertDialog.Builder(
                    SubjectActivity.this);
            builder.setTitle("Log Off");
            builder.setMessage("Are you sure");
            builder.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.cancel();
                            Log.e("info", "NO");
                        }

                    });

            builder.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            Session.LogOff(getApplicationContext());
                            SubjectActivity.this.finish();
                        }
                    });


            AlertDialog Alert = builder.create();

            Alert.show();
            return true;
        }

        return  true;
    }

    private void SetSubjectData(){


    }
}
