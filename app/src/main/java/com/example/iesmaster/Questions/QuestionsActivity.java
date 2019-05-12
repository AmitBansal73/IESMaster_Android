package com.example.iesmaster.Questions;

import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import com.example.iesmaster.model.Questions;
import com.example.iesmaster.R;


public class QuestionsActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener {
    ViewPager viewPager;
    FragmentStatePagerAdapter fragmentAdapter;
    List<Questions> listQuestion;
    private Object view;

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

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        GetQuestionData();
        if(listQuestion.size()>0) {
            fragmentAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(fragmentAdapter);
        }
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
