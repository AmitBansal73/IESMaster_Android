package com.example.iesmaster;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.Common.DataAccess;
import com.example.iesmaster.Common.MyGridView;
import com.example.iesmaster.Common.OvalImageView;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.Questions.QuestionsActivity;
import com.example.iesmaster.Register.LoginActivity;
import com.example.iesmaster.Register.WelcomeActivity;
import com.example.iesmaster.Test.YearsActivity;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;
import com.example.iesmaster.model.Subject;
import com.example.iesmaster.model.Test;
import com.example.iesmaster.model.Topic;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {


    GridView gridViewSubject,gridViewProfile;
    List<Subject> subList=new ArrayList<>();
    List<AcademicProfile> univList =new ArrayList<>();
    SubjectAdapter subjectAdapter;
    ProfileAdpter profileAdpter;
    Button btnNext;
    TextView txtAddProfile,txtFavourites,txtNoFavourite,univName,txtStream,txtName,txtClgName;
    OvalImageView profile_Image;
    Profile myProfile;
    AcademicProfile academicProfile;
    View viewFavourite;
    MyGridView gridViewFavourite;
    List<Test> listFavourite;
    ProgressBar progressBar,progressBarSubject;
    AcademicProfile selectedProfile;
    LinearLayout profileLayout;
    TextView txtErrorSubject;
    GoogleSignInClient mGoogleSignInClient;
    int selectedUniversityID, selectedStreamID, selectedSemesterID, selectedSubjectID;

    int color_arr[] = {R.drawable.grid_univ, R.drawable.gradient, R.drawable.gradient_paper,R.drawable.gradient_years,R.drawable.gradient_3,
            R.drawable.gradient_4,R.drawable.gradient_2,R.drawable.gradient_1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" Home ");
        actionBar.show();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        progressBarSubject = findViewById(R.id.progressBarSubject);

        myProfile = Session.GetProfile(getApplicationContext());
        selectedProfile = academicProfile = Session.GetAcademicProfile(getApplicationContext());
        univName = findViewById(R.id.univName);
        txtStream = findViewById(R.id.txtStream);
        profile_Image = findViewById(R.id.profile_Image);
        txtClgName = findViewById(R.id.txtClgName);
        txtName = findViewById(R.id.txtName);
        txtErrorSubject = findViewById(R.id.txtErrorSubject);
        gridViewSubject = findViewById(R.id.gridViewSubject);
        profileLayout = findViewById(R.id.profileLayout);
        gridViewFavourite = findViewById(R.id.gridViewFavourite);
        gridViewProfile = findViewById(R.id.gridViewProfile);
        viewFavourite = findViewById(R.id.viewFavourite);

        txtFavourites = findViewById(R.id.txtFavourites);
        txtNoFavourite = findViewById(R.id.txtNoFavourite);

        if(myProfile.UserLogin.matches(""))
        {
            Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
            startActivity(intent);
            HomeActivity.this.finish();
        }
        else
        {
            if(academicProfile.UniversityID == 0)
            {
                txtName.setText(myProfile.UserName);
                univName.setText("Set Your Academic Profile");
                txtStream.setText("");
                gridViewSubject.setVisibility(View.GONE);
                txtErrorSubject.setVisibility(View.VISIBLE);
                txtErrorSubject.setText("Select Profile to see subject!");
            }
            else
            {
                txtName.setText(myProfile.UserName);
                univName.setText(academicProfile.UniversityName+", "+academicProfile.CollegeName);
                txtStream.setText(academicProfile.Stream+ ",  " +academicProfile.Semester );
                Glide.with(this).load(myProfile.ProfileImage).into(profile_Image);
                selectedUniversityID = academicProfile.UniversityID;
                selectedStreamID =academicProfile.StreamID;
                profileLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GetSubjects(academicProfile.UniversityID, academicProfile.StreamID);
                    }
                });
            }
        }
        DataAccess da = new DataAccess(getApplicationContext());
        da.open();
        listFavourite = da.GetFavourite();
        univList = da.GetProfiles();

        setProfileGrid();
        SetFavouriteGrid();

        if(academicProfile.UniversityID!=0)
        {
            GetSubjects(academicProfile.UniversityID,academicProfile.StreamID);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100)
        {

            boolean IsProfile = data.getBooleanExtra("IsProfile", false);
            if(IsProfile) {
                selectedProfile = data.getParcelableExtra("Profile");
                univList.add(selectedProfile);
                profileAdpter.notifyDataSetChanged();
            }
        }
    }

    private void setProfileGrid()
    {
        txtAddProfile = findViewById(R.id.txtAddProfile);

        //txtAddProfile.setText("Add other semester to get more Test papers.");

        txtAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileActivity = new Intent(HomeActivity.this, AdditionalProfileActivity.class );
                profileActivity.putExtra("IsResult", true);
                startActivityForResult(profileActivity,100);
            }
        });


        profileAdpter=new ProfileAdpter(this, R.layout.grid_item_profile, univList);
        gridViewProfile.setAdapter(profileAdpter);
       // profileAdpter.notifyDataSetChanged();

        gridViewProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProfile =  univList.get(position);

                selectedUniversityID =selectedProfile.UniversityID;

                selectedStreamID =selectedProfile.StreamID;
                GetSubjects(selectedUniversityID, selectedStreamID);

            }
        });
    }

    public void GetSubjects(int UniversityId, int StreamId ){
        progressBarSubject.setVisibility(View.VISIBLE);
       // String url = Constants.Application_URL+ "/api/Paper/1006/1002";
          String url = Constants.Application_URL+ "/api/Paper/GetSubject/"+ UniversityId+"/"+StreamId;
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBarSubject.setVisibility(View.GONE);
                    try {
                        int x = response.length();
                        subList.clear();
                        if(x>0) {
                            for (int i = 0; i < x; i++) {
                                Subject subject = new Subject();
                                JSONObject jObj = response.getJSONObject(i);
                                subject.SubjectName = jObj.getString("SubjectName");
                                subject.SubjectID = jObj.getInt("SubjectId");
                                subList.add(subject);
                            }
                            gridViewSubject.setVisibility(View.VISIBLE);
                            txtErrorSubject.setVisibility(View.GONE);
                            //progressBar.setVisibility(View.GONE);
                            setSubjectGrid();
                        }
                        else
                        {
                            gridViewSubject.setVisibility(View.INVISIBLE);
                            txtErrorSubject.setVisibility(View.VISIBLE);
                            txtErrorSubject.setText("No Subject For selected profile!");
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
                    progressBarSubject.setVisibility(View.GONE);

                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
            progressBarSubject.setVisibility(View.GONE);
        }
    }


    private void setSubjectGrid()
    {
       // subList = mock_data.GetSubjects();

        subjectAdapter=new SubjectAdapter(this, R.layout.gridview_subjects, subList);
        gridViewSubject.setAdapter(subjectAdapter);

        gridViewSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Subject sub = (Subject) subList.get(position);
                Intent intent = new Intent(HomeActivity.this,YearsActivity.class);
                intent.putExtra("UniversityID", selectedUniversityID);
                intent.putExtra("UniversityName", selectedProfile .UniversityName);
                intent.putExtra("StreamID", selectedStreamID);
                intent.putExtra("StreamName", selectedProfile .Stream);
                intent.putExtra("SubjectID", sub.SubjectID);
                intent.putExtra("SubjectName", sub.SubjectName);
                startActivity(intent);
               // HomeActivity.this.finish();
            }
        });
    }
    
    private void SetFavouriteGrid(){


        txtFavourites.setText("My Favourite / Recent Tests");



        FavouriteAdapter favouriteAdapter = new FavouriteAdapter(this, R.layout.grid_item_topic,listFavourite );
        gridViewFavourite.setAdapter(favouriteAdapter);

        gridViewFavourite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this,QuestionsActivity.class);
                startActivity(intent);
                // HomeActivity.this.finish();
            }
        });

        if ( listFavourite != null && listFavourite.size()>0)
        {
            viewFavourite.setVisibility(View.VISIBLE);
            txtNoFavourite.setVisibility(View.GONE);

         //   SetFavouriteGrid();
        }
        else
        {
            txtNoFavourite.setVisibility(View.VISIBLE);
            viewFavourite.setVisibility(View.GONE);
        }

    }

    public class SubjectAdapter extends ArrayAdapter {

        List<Subject> subList;

            public SubjectAdapter(Context context, int textViewResourceId, List<Subject> objects) {
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
                    if(convertView==null) {
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.gridview_subjects, null);

                    }
                    TextView textView = convertView.findViewById(R.id.testName);
                    Subject tempSubject = (Subject) subList.get(position);
                    textView.setText(tempSubject.getsubName());
                    int rnd = new Random().nextInt(color_arr.length);
                    convertView.setBackgroundResource(color_arr[rnd]);
                }
                catch (Exception ex)
                {
                   int a=1;
                }
            return convertView;
        }
    }

    public class ProfileAdpter extends ArrayAdapter {

        List<AcademicProfile>  univList = new ArrayList<>();

        public ProfileAdpter(Context context, int textViewResourceId, List<AcademicProfile> objects) {
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
            TextView btnRemove = convertView.findViewById(R.id.btnRemove);

            final AcademicProfile tempProfile = (AcademicProfile) univList.get(position);
            final int Index = position;
            textView.setText(tempProfile.UniversityName);
            String strStream = tempProfile.Stream + "," + tempProfile.Semester ;
            txtStream.setText(strStream);
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    univList.remove(Index);
                    DataAccess da = new DataAccess(getApplicationContext());
                    da.open();
                    da.RemoveProfile(tempProfile);
                    profileAdpter.notifyDataSetChanged();
                }
            });

            int rnd = new Random().nextInt(color_arr.length);
            convertView.setBackgroundResource(color_arr[rnd]);

            return convertView;
        }
    }


    public class FavouriteAdapter extends  ArrayAdapter
    {
        LayoutInflater inflat;
        ViewHolder holder;

        public FavouriteAdapter(Context context, int resource, List<Test> objects) {
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
                Test testRow = (Test)getItem(position);
                // Log.d("Dish Name", row.complaint_type);
                holder.text1.setText(testRow.SubjectName);
                holder.text2.setText(Integer.toString(testRow.year));
                int rnd = new Random().nextInt(color_arr.length);
                convertView.setBackgroundResource(color_arr[rnd]);

            }
            catch (Exception ex)
            {
                int a=1;
                Toast.makeText(getApplicationContext(),"Could not Load TestData", Toast.LENGTH_LONG).show();
                //return null;
            }
            return convertView;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Alert");
        builder.setMessage("Are you sure to Exit ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HomeActivity.this.finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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

            Intent profileIntent = new Intent(HomeActivity.this, MyProfileActivity.class);
            startActivity(profileIntent);
        }
        else if (id == R.id.action_LogOff)
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                    HomeActivity.this);
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

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            DataAccess da = new DataAccess(getApplicationContext());
                                            da.open();
                                            da.ClearAll();
                                            da.close();
                                            Session.LogOff(getApplicationContext());
                                            HomeActivity.this.finish();
                                        }
                                    });


                        }
                    });

            android.app.AlertDialog Alert = builder.create();
            Alert.show();
            return true;
        }

        return  true;
    }



}