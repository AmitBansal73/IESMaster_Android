package com.example.iesmaster.Payment;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    Button btnPayNow;
    Transaction transaction;
    TextView text1,text2,text3,text4,txtSubName,txtYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" Payment");
        actionBar.show();

        btnPayNow = findViewById(R.id.btnPayNow);
        txtSubName = findViewById(R.id.txtSubName);
        txtYear  = findViewById(R.id.txtYear);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String SubjectName= bundle.getString("Subject");
        String TopicName= bundle.getString("TopicName");
        String TopicName1= bundle.getString("Semester");
        String TopicName2= bundle.getString("University");
        int TopicName3= bundle.getInt("Cost");
        int year = bundle.getInt("year");
        txtSubName.setText(SubjectName);
        text1.setText(TopicName);
        text2.setText(TopicName1);
        text3.setText(TopicName2);
        text4.setText(Integer.toString(TopicName3));
        txtYear.setText(Integer.toString(year));
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = new Transaction();
                transaction.UserID = 1001;
                transaction.PaperID = 1001;
                transaction.Paid = 10;
                GetCheckSum();
            }
        });
    }

    private void GetCheckSum()
    {
        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            String  url =  Constants.Application_URL+"/api/Paytm/GetChecksum" ;

            String reqBody = "{\"PaperID\":\""+ transaction.PaperID +"\",\"UserID\":\""+ transaction.UserID +"\",\"Paid\":\""+ transaction.Paid +
                    "\",\"PurchaseDate\":\"" +transaction.PurchaseDate + "\",\"ClosureDate\":\""+ transaction.ClosureDate +"\"}";

            JSONObject jsRequest = new JSONObject(reqBody);
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jObj) {

                    try {
                        transaction= new Transaction();
                        transaction.UserID = 1001;
                        transaction.PaperID = 1001;
                        transaction.Paid = 10;
                        transaction.checksum = jObj.getString("CheckSum");
                        transaction.OrderID = jObj.getInt("OrderID");

                        MakePayment(transaction);
                    }

                    catch (JSONException jEx) {
                        int a=1;
                        a++;
                    }
                    catch (Exception ex)
                    {
                        int a=1;
                        a++;
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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

    private void  MakePayment( final Transaction transaction){
        try {
            PaytmPGService Service = PaytmPGService.getStagingService();
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("MID", "JoyYHn78452054983473");
            // Key in your staging and production MID available in your dashboard
            paramMap.put("ORDER_ID", Integer.toString(transaction.OrderID));
            paramMap.put("CUST_ID", Integer.toString(transaction.UserID));
            paramMap.put("MOBILE_NO", "9591033223");
            paramMap.put("EMAIL", "amit_bansal73@yahoo.com");
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", Integer.toString(transaction.Paid));
            paramMap.put("WEBSITE", "WEBSTAGING");
            // This is the staging value. Production value is available in your dashboard
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            // This is the staging value. Production value is available in your dashboard
            //paramMap.put("CALLBACK_URL", "http://www.kevintech.in/paytmCallback?ORDER_ID=order1");
            paramMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + Integer.toString(transaction.OrderID));
            paramMap.put("CHECKSUMHASH", transaction.checksum);
            PaytmOrder Order = new PaytmOrder(paramMap);

            Service.initialize(Order, null);

            Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
                /*Call Backs*/
                public void someUIErrorOccurred(String inErrorMessage) {
                    int a=1;
                }

                public void onTransactionResponse(Bundle inResponse) {
                    Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                    Intent confirm = new Intent(PaymentActivity.this, ConfirmActivity.class);
                    confirm.putExtra("PaperID", transaction.PaperID);
                    confirm.putExtra("UserID",transaction.UserID);
                    confirm.putExtra("OrderID",transaction.OrderID);
                    confirm.putExtra("Paid",transaction.Paid);
                    confirm.putExtra("CheckSum",transaction.checksum);
                    startActivity(confirm);
                }

                public void networkNotAvailable() {
                    Toast.makeText(getApplicationContext(), "Network not available" , Toast.LENGTH_LONG).show();
                }

                public void clientAuthenticationFailed(String inErrorMessage) {
                    Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
                }

                public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                    Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
                }

                public void onBackPressedCancelTransaction() {
                    Toast.makeText(getApplicationContext(), "Back Pressed ", Toast.LENGTH_LONG).show();
                }

                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                    Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception ex)
        {
            int a=1;
            a++;
        }
    }
}
