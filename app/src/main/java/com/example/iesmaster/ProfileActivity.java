package com.example.iesmaster;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iesmaster.Common.Session;
import com.example.iesmaster.Object.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity  {
    Button btnSave;
    EditText txtUniversity,txtCollage,txtStream;
    ListView listViewUniversity,listViewCollage,listViewStream;
    Spinner spinnerUniversity,spinnerCollege,spinnerStream,spinnerSemester;
    ArrayAdapter<String> adapterUniversity;
   // ArrayAdapter<String> adapterClg;
    List<String> universityList= new ArrayList<>();
    HashMap<String,Integer> universityHashMap = new HashMap<>();

    List<String> collegeList= new ArrayList<>();
    HashMap<String,Integer> collegeHashMap = new HashMap<>();

    List<String> streamList= new ArrayList<>();
    HashMap<String,Integer> streamHashMap = new HashMap<>();

    List<String> semesterList= new ArrayList<>();
    HashMap<String,Integer> semesterHashMap = new HashMap<>();

    ArrayList<college> arraylistcollage = new ArrayList<>();
    ArrayAdapter<String> adapterCollege,adapterStream, adapterSemester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Profile");
        actionBar.show();

        setUniversityData();
        setCollegeData();
        setStreamData();
        setSemesterData();

        txtUniversity = findViewById(R.id.txtUniversity);
        txtStream = findViewById(R.id.txtStream);
        txtCollage = findViewById(R.id.txtCollage);
        listViewUniversity = findViewById(R.id.listViewUniversity);
        listViewCollage = findViewById(R.id.listViewCollage);
        listViewStream = findViewById(R.id.listViewStream);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(ProfileActivity.this, SubjectAndTestActivity.class);
                startActivity(i);

            }
        });

        txtUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //spinnerUniversity = findViewById(R.id.spinnerUniversity);
        adapterUniversity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, universityList);
        adapterUniversity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewUniversity.setAdapter(adapterUniversity);

        txtUniversity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if(cs.length()<2)
                {
                    listViewUniversity.setVisibility(View.GONE);
                }
                else {
                    adapterUniversity.getFilter().filter(cs);
                    listViewUniversity.setVisibility(View.VISIBLE);
                }
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
                txtUniversity.setText(University);
                listViewUniversity.setVisibility(View.GONE);
            }
        });



        adapterCollege = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, collegeList);
        adapterCollege.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewCollage.setAdapter(adapterCollege);

        txtCollage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                if(cs.length()<2)
                {
                    listViewCollage.setVisibility(View.GONE);
                }
                else {
                    ProfileActivity.this.adapterCollege.getFilter().filter(cs);
                    listViewCollage.setVisibility(View.VISIBLE);
                }
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

        listViewCollage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String University = listViewCollage.getItemAtPosition(position).toString();
                txtCollage.setText(University);
                listViewCollage.setVisibility(View.GONE);
            }
        });


        //spinnerStream = findViewById(R.id.spinnerStream);
        adapterStream = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, streamList);
        adapterStream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewStream.setAdapter(adapterStream);
        txtStream.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(cs.length()<2)
                {
                    listViewStream.setVisibility(View.GONE);
                }
                else {
                    ProfileActivity.this.adapterStream.getFilter().filter(cs);
                    listViewStream.setVisibility(View.VISIBLE);
                }
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
                listViewStream.setVisibility(View.GONE);
            }
        });


        spinnerSemester = findViewById(R.id.spinnerSemester);
        adapterSemester = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(adapterSemester);


    }



    private void setUniversityData()
    {
        universityList.add("UPTU");
        universityHashMap.put("UPTU",1);
        universityList.add("Punjab University");
        universityHashMap.put("Punjab University",2);
        universityList.add("Amity University");
        universityHashMap.put("Amity University",3);
        universityList.add("CCS University");
        universityHashMap.put("CCS University",4);

    }


    private void setCollegeData()
    {
        collegeList.add("ABES College");
        collegeHashMap.put("ABES College",1);
        collegeList.add("KTIS College");
        collegeHashMap.put("KTIS College",2);
        collegeList.add("ITS College");
        collegeHashMap.put("ITS College",3);
        collegeList.add("AK Garg College");
        collegeHashMap.put("AK Garg College",4);
    }


    private void setStreamData()
    {
        streamList.add("Civil");
        streamHashMap.put("Civil",1);
        streamList.add("Mechanical");
        streamHashMap.put("Mechanical",2);
        streamList.add("Electrical");
        streamHashMap.put("Electrical",3);
        streamList.add("Computer Science");
        streamHashMap.put("Computer Science",4);
    }


    private void setSemesterData()
    {
        semesterList.add("1st Year (Sem I)");
        semesterHashMap.put("1st Year (Sem I)",1);
        semesterList.add("1st Year (Sem II)");
        semesterHashMap.put("1st Year (Sem II)",2);
        semesterList.add("2nd Year (Sem I)");
        semesterHashMap.put("2nd Year (Sem I)",3);
        semesterList.add("2nd Year (Sem II)");
        semesterHashMap.put("2nd Year (Sem II)",4);

        semesterList.add("3rd Year (Sem I)");
        semesterHashMap.put("3rd Year (Sem I)",5);
        semesterList.add("3rd Year (Sem II)");
        semesterHashMap.put("3rd Year (Sem II)",6);
        semesterList.add("Final Year (Sem I)");
        semesterHashMap.put("Final Year (Sem I)",7);
        semesterList.add("Final Year (Sem II)");
        semesterHashMap.put("Final Year (Sem II)",8);

    }


    private class ViewHolder
    {
        TextView txtCollage,txtCity,txtState,txtPincode,txtIntercom,txtSociety,txtFlatNumber;
    }

    class MyAdapterClg extends ArrayAdapter<college>{
        LayoutInflater inflat;
        ViewHolder holder;
        public MyAdapterClg(Context context, int resource, int textViewResourceId, ArrayList<college> objects) {

            super(context, resource, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            inflat= LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arraylistcollage.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    convertView = inflat.inflate(R.layout.row_item_collages, null);
                    holder = new ViewHolder();
                    holder.txtCollage = convertView.findViewById(R.id.txtCollage);
                    convertView.setTag(holder);
                }
                college row = getItem(position);
                // Log.d("Dish Name", row.complaint_type);
                holder.txtCollage.setText(row.CollegeName);
                //  holder.txtFloor.setText(row.Floor);
                //  holder.txtBlock.setText(row.Block);
                //  holder.txtArea.setText(row.FlatArea);
                // holder.txtIntercom.setText(row.IntercomNumber);
                return convertView;
            }

            catch (Exception ex)

            {
                Toast.makeText(getApplicationContext(),"Could not Load Forum Data", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        public int getPosition(college item) {
            return super.getPosition(item);
        }

        @Override
        public college getItem(int position) {
            return arraylistcollage .get(position);
        }
    }

    public class college{
        String CollegeName;
    }

}
