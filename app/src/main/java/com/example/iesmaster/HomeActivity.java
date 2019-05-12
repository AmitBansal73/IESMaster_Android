package com.example.iesmaster;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.iesmaster.Common.DataAccess;
import com.example.iesmaster.Common.MyGridView;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.Questions.QuestionsActivity;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;
import com.example.iesmaster.model.Subject;
import com.example.iesmaster.model.Topic;
import com.example.iesmaster.model.mock_data;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    GridView gridView,gridViewUniversity;
    List<Subject> subList=new ArrayList<>();
    List<AcademicProfile> univList =new ArrayList<>();
    List<AcademicProfile> profileList =new ArrayList<>();
    TestSubject myAdapter;
    UniversityAdpter universityAdpter;
    Button btnNext;
    TextView txtAddProfile,txtFavourites,txtNoFavourite;
    private Integer ClickCount=0;
    private long prevTime = 0;

    Profile myProfile;

    View viewFavourite;
    MyGridView gridViewFavourite;
    List<Topic> listFavourite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myProfile = Session.GetProfile(getApplicationContext());


        if(myProfile.UserLogin.matches(""))
        {
            Intent intent = new Intent(HomeActivity.this,WelcomeActivity.class);
            startActivity(intent);
            HomeActivity.this.finish();
        }
        else
        {
           AcademicProfile academicProfile = Session.GetAcademicProfile(getApplicationContext());
            if(academicProfile.UniversityID == 0)
            {
                Intent intent = new Intent(HomeActivity.this, AcademicProfileActivity.class);
                startActivity(intent);
                HomeActivity.this.finish();
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Subjects");
        actionBar.show();

        DataAccess da = new DataAccess(getApplicationContext());
        da.open();
        listFavourite = da.GetFavourite();
        univList = da.GetProfiles();

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,YearsActivity.class);
                startActivity(intent);
                HomeActivity.this.finish();
            }
        });


        txtAddProfile = findViewById(R.id.txtAddProfile);

        txtAddProfile.setText("Recommended : Add other semester to get more Test papers.");

        txtAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileActivity = new Intent(HomeActivity.this, AcademicProfileActivity.class );
                profileActivity.putExtra("IsResult", true);
                startActivityForResult(profileActivity,100);
            }
        });

        gridViewUniversity = findViewById(R.id.gridViewUniversity);
        setProfileGrid();

        gridView = findViewById(R.id.gridView);
        setSubjectGrid();

        txtFavourites = findViewById(R.id.txtFavourites);
        txtNoFavourite = findViewById(R.id.txtNoFavourite);
        txtFavourites.setText("My Favourite / Recent Tests");

        viewFavourite = findViewById(R.id.viewFavourite);



        if ( listFavourite != null && listFavourite.size()>0)
        {
            viewFavourite.setVisibility(View.VISIBLE);
            txtNoFavourite.setVisibility(View.GONE);
         gridViewFavourite = findViewById(R.id.gridViewFavourite);
          SetFavouriteGrid();
        }
        else
        {
            txtNoFavourite.setVisibility(View.VISIBLE);
            viewFavourite.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100)
        {
            AcademicProfile newProfile = data.getParcelableExtra("Profile");

            DataAccess dataAccess = new DataAccess(getApplicationContext());
            dataAccess.open();
            dataAccess.InsertProfile(newProfile);
            univList.add(newProfile);
            universityAdpter.notifyDataSetChanged();
        }
    }

    private void setProfileGrid()
    {

        AcademicProfile myAcademicProfile = new AcademicProfile();
        myAcademicProfile.UniversityName = "UPTU";
        myAcademicProfile.Stream = "Civil";
        myAcademicProfile.Semester = "3rd";
        univList.add(myAcademicProfile);
        /*univList.add(new Subject("Punjab University", R.drawable.b));
        univList.add(new Subject("Amity University", R.drawable.a));
        univList.add(new Subject("CCS University", R.drawable.b));*/

        universityAdpter=new UniversityAdpter(this, R.layout.grid_item_profile, univList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        };
        gridViewUniversity.setAdapter(universityAdpter);
    }

    private void setSubjectGrid()
    {
        subList = mock_data.GetSubjects();

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

                Intent intent = new Intent(HomeActivity.this,YearsActivity.class);
                startActivity(intent);
               // HomeActivity.this.finish();
            }
        });
    }


    private void SetFavouriteGrid(){

        FavouriteAdapter favouriteAdapter = new FavouriteAdapter(this, R.layout.grid_item_topic,listFavourite );
        gridViewFavourite.setAdapter(favouriteAdapter);

        gridViewFavourite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this,QuestionsActivity.class);
                startActivity(intent);
                HomeActivity.this.finish();
            }
        });
    }

    public class TestSubject extends ArrayAdapter {

        List<Subject> subList = new ArrayList<>();

            public TestSubject(Context context, int textViewResourceId, List<Subject> objects) {
            super(context, textViewResourceId, objects);
            subList = objects;
        }

        @Override
        public int getCount() {
            return subList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                try {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.gridview_subjects, null);
                    TextView textView = convertView.findViewById(R.id.testName);

                    Subject tempSubject = (Subject) subList.get(position);
                    textView.setText(tempSubject.getsubName());
                }
                catch (Exception ex)
                {
                   int a=1;
                }
            return convertView;
        }
    }

    public class UniversityAdpter extends ArrayAdapter {

        List<AcademicProfile>  univList = new ArrayList<>();

        public UniversityAdpter(Context context, int textViewResourceId, List<AcademicProfile> objects) {
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
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.grid_item_profile, null);

            }
            TextView textView = convertView.findViewById(R.id.univName);
            TextView txtStream = convertView.findViewById(R.id.txtStream);
            AcademicProfile tempProfile = (AcademicProfile) univList.get(position);
            textView.setText(tempProfile.UniversityName);
            String strStream = tempProfile.Stream + "," + tempProfile.Semester ;
            txtStream.setText(strStream);
            return convertView;
        }
    }

    public class FavouriteAdapter extends  ArrayAdapter
    {
        LayoutInflater inflat;
        ViewHolder holder;

        public FavouriteAdapter(Context context, int resource, List<Topic> objects) {
            super(context, resource, objects);
            inflat= LayoutInflater.from(context);
        }


        @Override
        public Object getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition( Object item) {
            return super.getPosition(item);
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            try {
                if (convertView == null) {
                    convertView = inflat.inflate(R.layout.grid_item_favourite, null);
                    holder = new ViewHolder();
                    holder.text1 = convertView.findViewById(R.id.text1);
                    holder.text2 = convertView.findViewById(R.id.text2);
                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
                Topic testRow = (Topic)getItem(position);
                // Log.d("Dish Name", row.complaint_type);
                holder.text1.setText(testRow.TopicName);
                holder.text2.setText(testRow.SubjectName);
                return convertView;
            }
            catch (Exception ex)
            {
                int a=1;
                Toast.makeText(getApplicationContext(),"Could not Load RentData", Toast.LENGTH_LONG).show();
                return null;
            }
        }
    }

    private class ViewHolder
    {
        TextView text1,text2,text3,text4;
        TextView label1,label2,label3,label4;
        Button btnBuy, btnStart;
        View viewScore;
    }
    @Override
    public void onBackPressed() {
        try {
            long time = SystemClock.currentThreadTimeMillis();

            if (prevTime == 0) {
                prevTime = SystemClock.currentThreadTimeMillis();
            }

            if (time - prevTime > 1000) {
                prevTime = time;
                ClickCount=0;
                String msg = "double click to close";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            if (time - prevTime < 1000 && time > prevTime) {
                ClickCount++;
                if (ClickCount == 2) {

                    HomeActivity.this.finish();
                } else {
                    prevTime = time;
                    String msg = "double click to close";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}