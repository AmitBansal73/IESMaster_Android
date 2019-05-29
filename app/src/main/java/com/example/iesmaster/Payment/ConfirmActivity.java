package com.example.iesmaster.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.R;
import com.example.iesmaster.model.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmActivity extends AppCompatActivity {

    Transaction transaction;
    TextView txtMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        transaction = new Transaction();
        Intent intent = getIntent();
        transaction.PaperID = intent.getIntExtra("PaperID", 0);
        transaction.UserID =  intent.getIntExtra("UserID",0);
        transaction.OrderID = intent.getIntExtra("OrderID",0);
        transaction.Paid =    intent.getIntExtra("Paid",0);
        transaction.checksum =intent.getStringExtra("CheckSum");
        txtMessage = findViewById(R.id.txtMessage);
        Verify();
    }
    private void Verify()
    {
        try {
            txtMessage.setText("Verifying!");
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            String  url =  Constants.Application_URL+"/api/Paytm/VerifyChecksum" ;

            String reqBody = "{\"PaperID\":\""+ transaction.PaperID +"\",\"UserID\":\""+ transaction.UserID +"\",\"Paid\":\""+ transaction.Paid
                    +"\",\"OrderID\":\""+ transaction.OrderID +"\",\"CheckSum\":\""+ transaction.checksum + "\"}";

            JSONObject jsRequest = new JSONObject(reqBody);
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jObj) {

                    try {
                        String result = jObj.getString("Response");
                        if(result.equalsIgnoreCase("Ok"))
                        {
                            txtMessage.setText("Successfull!");
                        }
                        else
                        {
                            txtMessage.setText("Failed!");
                        }

                    }

                    catch (JSONException jEx) {
                        txtMessage.setText("JSON Exceptio Occured!");
                        int a=1;
                        a++;
                    }
                    catch (Exception ex)
                    {
                        txtMessage.setText("Exception reading response!");
                        int a=1;
                        a++;
                    }


                }



            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    txtMessage.setText("Server Error!");
                    int a=1;
                    a++;
                }


            });


            RetryPolicy rPolicy = new DefaultRetryPolicy(0,-1,0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
            //*******************************************************************************************************
        } catch (Exception js) {
            int a=1;
            a++;
        } finally {

        }

    }
}
