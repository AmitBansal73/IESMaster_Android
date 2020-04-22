package com.example.iesmaster.Test;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.Common.DataAccess;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.Common.Utility;
import com.example.iesmaster.Payment.PaymentActivity;
import com.example.iesmaster.R;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;
import com.example.iesmaster.model.Subject;
import com.example.iesmaster.Questions.QuestionsActivity;
import com.example.iesmaster.model.Test;
import com.example.iesmaster.model.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TestPaperActivity extends AppCompatActivity {
    List<Test> arrayListTest=new ArrayList<>();
    ListView testListView;
    MyAdapterTest adapterTest;
    com.example.iesmaster.model.Subject subject;
    AcademicProfile academicProfile ;
    ProgressBar progressBar;
    Profile myProfile;

    int UniversityID,StreamID,Year,unit, SubjectID;
    String subjectName;
    String Semester;
    TextView txtMessage;
    int PaperID;
    NumberFormat currFormat;
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
        currFormat = NumberFormat.getCurrencyInstance();
        currFormat.setCurrency(Currency.getInstance("INR"));

        subject = new Subject();
        myProfile = Session.GetProfile(getApplicationContext());
        academicProfile = Session.GetAcademicProfile(getApplicationContext());
       // arrayListTest = mock_data.GetTopics();
        txtMessage = findViewById(R.id.txtMessage);
        progressBar = findViewById(R.id.progressBar);
        testListView = findViewById(R.id.testListView);
        adapterTest =new MyAdapterTest(TestPaperActivity.this,0,arrayListTest);
        testListView.setAdapter(adapterTest);

        Intent intent = getIntent();
        UniversityID = intent.getIntExtra("UniversityID",0);
        StreamID = intent.getIntExtra("StreamID", 0);
        Year = intent.getIntExtra("year", 0);
        SubjectID = intent.getIntExtra("SubjectID", 0);
        subjectName = intent.getStringExtra("SubjectName");
        unit = intent.getIntExtra("unit", 0);

        GetTestPapers();
    }

    public void GetTestPapers(){
        progressBar.setVisibility(View.VISIBLE);
       // String url = Constants.Application_URL+ "/api/Paper/1000/1001/1000/1000";
        String url = Constants.Application_URL+ "/api/Paper/"+ UniversityID+"/"+StreamID+"/"+SubjectID+"/"+Year+"/"+unit+"/" + myProfile.UserID;//+myProfile.UserID;
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jArray) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        int x = jArray.length();
                        if(x>0) {
                            txtMessage.setVisibility(View.GONE);
                            for (int i = 0; i < x; i++) {

                                Test tempTest = new Test();
                                JSONObject jObj = jArray.getJSONObject(i);
                                JSONObject paperObject = jObj.getJSONObject("test");
                                tempTest.SubjectName = paperObject.getString("SubjectName");
                                tempTest.Stream = paperObject.getString("StreamName");
                                tempTest.Cost = paperObject.getInt("Cost");
                                //Semester = jObj.getString("SemesterName");
                                tempTest.Univesity = paperObject.getString("UniversityName");
                                tempTest.year = paperObject.getInt("Year");
                                tempTest.paperID = paperObject.getInt("PaperID");
                                JSONObject orderObj = jObj.getJSONObject("order");

                                String purchaseDate = orderObj.getString("PurchaseDate");
                                Date purchaseOn = Utility.StringToDate(purchaseDate);
                                Date today = new Date();
                                int dateDiff = today.compareTo(purchaseOn);
                                if ( dateDiff < 0) {
                                    tempTest.Status = 1;
                                } else {
                                    tempTest.Status = 2;
                                }
                                arrayListTest.add(tempTest);
                            }

                            adapterTest.notifyDataSetChanged();
                        }
                        else
                        {
                            txtMessage.setVisibility(View.VISIBLE);
                            txtMessage.setText("No Test Available!");
                        }

                    } catch (JSONException e) {
                        int a=1;
                        txtMessage.setVisibility(View.VISIBLE);
                        txtMessage.setText("Error Reading Data!");
                    }
                    catch (Exception ex)
                    {
                        int a=1;
                        txtMessage.setVisibility(View.VISIBLE);
                        txtMessage.setText("Error Reading Data!");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    txtMessage.setVisibility(View.VISIBLE);
                    txtMessage.setText("Error retreiving Data!");
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            progressBar.setVisibility(View.GONE);
            txtMessage.setVisibility(View.VISIBLE);
            txtMessage.setText("Error retreiving Data!");
        }
    }


    class MyAdapterTest extends ArrayAdapter<Test>{
        LayoutInflater inflat;
        ViewHolder holder;
        public MyAdapterTest(Context context, int resource, List<Test> objects) {

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
                    holder.txtSubName = convertView.findViewById(R.id.txtSubName);
                    holder.txtYear = convertView.findViewById(R.id.txtYear);

                    holder.label1 = convertView.findViewById(R.id.label1);
                    holder.label1.setText("Subject: ");
                    holder.label2 = convertView.findViewById(R.id.label2);
                    holder.label2.setText("Topic Name:");
                    holder.label3 = convertView.findViewById(R.id.label3);
                    holder.label3.setText("University:");
                    holder.label4 = convertView.findViewById(R.id.label4);
                    holder.label4.setText("Cost: ");
                    holder.btnBuy = convertView.findViewById(R.id.btnBuy);
                    holder.btnStart = convertView.findViewById(R.id.btnStart);
                   //  holder.viewScore = convertView.findViewById(R.id.viewScore);

                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();

               final Test testRow = (Test)getItem(position);
                // Log.d("Dish Name", row.complaint_type);
                holder.txtSubName.setText(testRow.SubjectName);
                holder.text1.setText(testRow.SubjectName);
                holder.text2.setText(Semester);
                holder.text3.setText(testRow.Univesity);
                holder.text4.setText( currFormat.format(testRow.Cost));
                holder.txtYear.setText(Integer.toString(Year));
               // holder.text4.setText( Integer.toString(testRow.TopicId));
                if(testRow.Status==1)
                {
                    // Buy
                    holder.btnBuy.setVisibility(View.VISIBLE);
                    holder.btnBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TestPaperActivity.this, PaymentActivity.class);
                            intent.putExtra("Subject", testRow.SubjectName);
                            //intent.putExtra("TopicName", testRow.SubjectName);
                            intent.putExtra("Semester", Semester);
                            intent.putExtra("University", testRow.Univesity);
                            intent.putExtra("Cost", testRow.Cost);
                            intent.putExtra("PaperID", testRow.paperID);
                            intent.putExtra("year",testRow.year);
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
                            try {
                                DataAccess da = new DataAccess(getApplicationContext());
                                da.open();
                                if (!da.IfTestExist(testRow)) {
                                    da.InsertFavourite(testRow);
                                }
                                Intent intent = new Intent(TestPaperActivity.this, QuestionsActivity.class);

                                //intent.putExtra("TopicName", testRow.SubjectName);
                               // intent.putExtra("Semester", Semester);
                                intent.putExtra("University", testRow.Univesity);
                                intent.putExtra("Cost", testRow.Cost);
                                intent.putExtra("year", testRow.year);
                                intent.putExtra("PaperID", testRow.paperID);
                                startActivity(intent);
                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(getApplicationContext(), "Error on Start Click",Toast.LENGTH_LONG).show();
                            }
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
        TextView text1,text2,text3,text4,txtSubName,txtYear;
        TextView label1,label2,label3,label4;
        Button btnBuy, btnStart;
        View viewScore;
    }
}
