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

import com.example.iesmaster.Common.DataAccess;
import com.example.iesmaster.model.Test;
import com.example.iesmaster.Questions.QuestionsActivity;
import com.example.iesmaster.model.Topic;
import com.example.iesmaster.model.mock_data;


public class TestPaperActivity extends AppCompatActivity {
    Button btnNext;
    List<Topic> arrayListTest=new ArrayList<>();
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

        arrayListTest = mock_data.GetTopics();

        testListView = findViewById(R.id.testListView);
        adapterTest =new MyAdapterTest(TestPaperActivity.this,0,arrayListTest);
        testListView.setAdapter(adapterTest);



    }




    class MyAdapterTest extends ArrayAdapter<Topic>{
        LayoutInflater inflat;
        ViewHolder holder;
        public MyAdapterTest(Context context, int resource, List<Topic> objects) {

            super(context, resource);
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
                    holder.text1 = convertView.findViewById(R.id.text1);
                    holder.text2 = convertView.findViewById(R.id.text2);
                    holder.text3 = convertView.findViewById(R.id.text3);
                    holder.text4 = convertView.findViewById(R.id.text4);

                    holder.label1 = convertView.findViewById(R.id.label1);
                    holder.label1.setText("Topic Name");
                    holder.label2 = convertView.findViewById(R.id.label2);
                    holder.label2.setText("Topic Name");
                    holder.label3 = convertView.findViewById(R.id.label3);
                    holder.label3.setText("Subject Name");
                    holder.label4 = convertView.findViewById(R.id.label4);

                    holder.btnBuy = convertView.findViewById(R.id.btnBuy);
                    holder.btnStart = convertView.findViewById(R.id.btnStart);
                     holder.viewScore = convertView.findViewById(R.id.viewScore);

                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
               final Topic testRow = (Topic)getItem(position);
                // Log.d("Dish Name", row.complaint_type);
                holder.text1.setText(testRow.TopicName);
                holder.text2.setText(testRow.TopicName);
                holder.text3.setText(testRow.SubjectName);
                holder.text4.setText( Integer.toString(testRow.TopicId));

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
                  //  holder.viewScore.setVisibility(View.GONE);

                }
                else if(testRow.Status ==2)
                {
                    // Start
                    holder.btnStart.setVisibility(View.VISIBLE);
                    holder.btnStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DataAccess da = new DataAccess(getApplicationContext());
                            da.open();
                            da.InsertFavourite(testRow);
                            Intent intent = new Intent(TestPaperActivity.this, QuestionsActivity.class);
                            startActivity(intent);
                        }
                    });
                    holder.btnBuy.setVisibility(View.GONE);
                   // holder.viewScore.setVisibility(View.GONE);
                }
                else if(testRow.Status == 3)
                {
                    // Show Score
                    holder.btnStart.setVisibility(View.GONE);
                     holder.btnBuy.setVisibility(View.GONE);
                    //holder.viewScore.setVisibility(View.VISIBLE);

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
        public Topic getItem(int position) {
            // TODO Auto-generated method stub
            return arrayListTest.get(position);
        }

        @Override
        public int getPosition(Topic item) {
            return super.getPosition(item);
        }

    }

    private class ViewHolder
    {
        TextView text1,text2,text3,text4;
        TextView label1,label2,label3,label4;
        Button btnBuy, btnStart;
        View viewScore;
    }
}
