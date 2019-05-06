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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.example.iesmaster.Object.Test;
import com.example.iesmaster.Questions.QuestionsActivity;


public class TestPaperActivity extends AppCompatActivity {
    Button btnNext;
    ArrayList<Test> arrayListTest=new ArrayList<>();
    ListView testListView;
    MyAdapterTest adapterTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_paper);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" Select Test ");
        actionBar.show();

        testListView = findViewById(R.id.testListView);
        adapterTest =new MyAdapterTest(TestPaperActivity.this,0,arrayListTest);
        testListView.setAdapter(adapterTest);

        GetTestData();



    }


    private List<Test> GetTestData(){

        List<Test> tempSubject = new ArrayList<>();

        Test test1 = new Test();
        test1.DifficulyLevel = "Difficult";
        test1.StreamID = 2;
        test1.Cost = 30;
        test1.Question = 20;
        test1.Status = 1;
        tempSubject.add(test1);
        arrayListTest.add(test1);

        test1 = new Test();
        test1.DifficulyLevel = "Medium";
        test1.StreamID = 2;
        test1.Cost = 20;
        test1.Question = 25;
        test1.Status = 2;
        tempSubject.add(test1);
        arrayListTest.add(test1);

        test1 = new Test();
        test1.DifficulyLevel = "Easy";
        test1.StreamID = 2;
        test1.Cost = 10;
        test1.Question = 30;
        tempSubject.add(test1);
        test1.Status = 2;
        arrayListTest.add(test1);

        test1 = new Test();
        test1.DifficulyLevel = "Medium";
        test1.StreamID = 2;
        test1.Cost = 20;
        test1.Question = 25;
        tempSubject.add(test1);
        test1.Status = 1;
        arrayListTest.add(test1);

        test1 = new Test();
        test1.DifficulyLevel = "Medium";
        test1.StreamID = 2;
        test1.Cost = 20;
        test1.Question = 25;
        tempSubject.add(test1);
        test1.Status = 1;
        arrayListTest.add(test1);

        test1 = new Test();
        test1.DifficulyLevel = "Easy";
        test1.StreamID = 2;
        test1.Cost = 10;
        test1.Question =30;
        tempSubject.add(test1);
        test1.Status = 3;
        arrayListTest.add(test1);

        return tempSubject;
    }

    class MyAdapterTest extends ArrayAdapter<Test>{
        LayoutInflater inflat;
        ViewHolder holder;
        public MyAdapterTest(Context context, int resource, ArrayList<Test> objects) {

            super(context, resource, objects);
            // TODO Auto-generated constructor stub
            inflat= LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayListTest.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    convertView = inflat.inflate(R.layout.row_item_testlist, null);
                    holder = new ViewHolder();
                    holder.txtDifficulty = convertView.findViewById(R.id.txtDifficulty);
                    holder.txtStreamID = convertView.findViewById(R.id.txtStreamID);
                    holder.txtCost = convertView.findViewById(R.id.txtCost);
                    holder.txtQuestion = convertView.findViewById(R.id.txtQuestion);
                    holder.btnBuy = convertView.findViewById(R.id.btnBuy);
                    holder.btnStart = convertView.findViewById(R.id.btnStart);
                    holder.txtScore = convertView.findViewById(R.id.txtScore);
                    holder.viewScore = convertView.findViewById(R.id.viewScore);

                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
                Test testRow = getItem(position);
                // Log.d("Dish Name", row.complaint_type);
                holder.txtDifficulty.setText(testRow.DifficulyLevel);
                holder.txtStreamID.setText(Integer.toString(testRow.StreamID));
                holder.txtCost.setText(Integer.toString(testRow.Cost));
                holder.txtQuestion.setText(Integer.toString(testRow.Question));

                if(testRow.Status==1)
                {
                    // Buy
                    holder.btnBuy.setVisibility(View.VISIBLE);
                    holder.btnBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TestPaperActivity.this, PaymentActivity.class);
                            startActivity(intent);
                        }
                    });
                    holder.btnStart.setVisibility(View.GONE);
                    holder.viewScore.setVisibility(View.GONE);

                }
                else if(testRow.Status ==2)
                {
                    // Start
                    holder.btnStart.setVisibility(View.VISIBLE);
                    holder.btnStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TestPaperActivity.this, QuestionsActivity.class);
                            startActivity(intent);
                        }
                    });
                    holder.btnBuy.setVisibility(View.GONE);
                    holder.viewScore.setVisibility(View.GONE);
                }
                else if(testRow.Status == 3)
                {
                    // Show Score
                    holder.btnStart.setVisibility(View.GONE);
                     holder.btnBuy.setVisibility(View.GONE);
                    holder.viewScore.setVisibility(View.VISIBLE);
                    holder.txtScore.setText("You Scored 70% on 12thApril, 2019");

                }


                return convertView;
            }
            catch (Exception ex)
            {
                int a=1;
                Toast.makeText(getApplicationContext(),"Could not Load RentData", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        public Test getItem(int position) {
            // TODO Auto-generated method stub
            return arrayListTest.get(position);
        }

        @Override
        public int getPosition(Test item) {
            return super.getPosition(item);
        }

    }

    private class ViewHolder
    {
        TextView txtDifficulty,txtStreamID,txtCost,txtQuestion,txtScore;
        Button btnBuy, btnStart;
        View viewScore;
    }
}
