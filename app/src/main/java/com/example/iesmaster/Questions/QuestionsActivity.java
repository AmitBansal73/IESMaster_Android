package com.example.iesmaster.Questions;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.model.Questions;
import com.example.iesmaster.R;
import com.example.iesmaster.model.Years;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QuestionsActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener {
    ViewPager viewPager;
    FragmentStatePagerAdapter fragmentAdapter;
    List<Questions> listQuestion;
    private Object view;
    ProgressBar progressBar;
    TextView txtMessage;
    int PaperID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" Questions ");
        actionBar.show();
        viewPager = findViewById(R.id.viewPager);
        progressBar = findViewById(R.id.progressBar);
        txtMessage = findViewById(R.id.txtMessage);
        Intent intent = getIntent();
        PaperID = intent.getIntExtra("PaperID",0);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        listQuestion = new ArrayList<>();
       // GetQuestionData();
       // if(listQuestion.size()>0) {
            fragmentAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(fragmentAdapter);
        //}
        GetQuestions();
    }



    public  class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment=null;
            try {

                if(listQuestion.size()>0) {
                    fragment = QuestionFragment.newInstance(position, listQuestion.get(position), listQuestion.size());
                    return fragment;
                }

            }
            catch (Exception ex)
            {
                Toast.makeText(getApplicationContext(),"Unable to create Fragment", Toast.LENGTH_LONG).show();

            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            int c=10;
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return listQuestion.size();
        }


    }


    private void GetQuestions(){

        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/Question/"+PaperID;
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
                            txtMessage.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            for (int i = 0; i < x; i++) {

                                JSONObject jObj = response.getJSONObject(i);
                                Questions Question = new Questions();
                                Question.QuestionId = jObj.getInt("QuesID");
                                Question.Questions = jObj.getString("Question");
                                Question.Answers = jObj.getString("Solution");
                                Question.DifficultyLevel  = jObj.getInt("DifficultyLevel");
                                listQuestion.add(Question);
                            }
                            // progressBar.setVisibility(View.GONE);
                            fragmentAdapter.notifyDataSetChanged();
                        }else {
                            viewPager.setVisibility(View.GONE);
                            txtMessage.setVisibility(View.VISIBLE);
                            txtMessage.setText("No Question Found");
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
                    viewPager.setVisibility(View.GONE);
                    txtMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("Error");

                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, 2, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
            progressBar.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText("Error");
        }
    }


    private void GetQuestionData(){

        listQuestion = new ArrayList<>();

        Questions Question = new Questions();
        Question.Questions = " The increase in hardness is shown by the value of the figure put in front of the letter H, 2H, 3H, and 4H etc.";
        Question.Answers = " Letters HB denote the medium grade where the increase in hardness is shown by the value of the figure put in front of the letter H," +
                " viz. 2H, 3H, and 4H etc. Similarly, the grade becomes softer according to letter B, 2B, 3B and 4B etc.";
        Question.Marks = 10;
        listQuestion.add(Question);

        Question = new Questions();
        Question.Questions = "What is the next size of 210 mm x 297 mm in drawing papers?";
        Question.Answers = "210 mm x 297 mm is A4 size, next one is A3 (297 mm x 420 mm), which came doubling along the width. And the next size is obtained by doubling the width i.e. A2 (420 mm x 594mm) and so on.";
        Question.Marks = 5;
        listQuestion.add(Question);

        Question = new Questions();
        Question.Questions = "Which command of MS-DOS is used to copy only files that have been modified on or after the date you specify?";
        Question.Answers = "XCOPY/D : date";
        Question.Marks = 5;
        listQuestion.add(Question);

        Question = new Questions();
        Question.Questions = "You suspect a virus has entered your computer. What will not be affected by the virus?";
        Question.Answers = "CMOS";
        Question.Marks = 5;
        listQuestion.add(Question);
        Question = new Questions();
        Question.Questions = "An OR gate has 4 inputs. One input is high and the other three are low. The output";
        Question.Answers = "is high";
        Question.Marks = 4;
        listQuestion.add(Question);

        Question = new Questions();
        Question.Questions = "Which type of entity represents an actual occurrence of an associated generalized entity?";
        Question.Answers = "Instance entity";
        Question.Marks = 2;
        listQuestion.add(Question);


        return ;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        String str = uri.getFragment();
        int i=1;

    }


    @Override
    public void onBackPressed() {


            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Alert");
            builder.setMessage("Exit from this Page");

            // add the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Intent intent = new Intent(QuestionsActivity.this, UnitActivity.class);
                    //startActivity(intent);
                    QuestionsActivity.this.finish();
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
}
