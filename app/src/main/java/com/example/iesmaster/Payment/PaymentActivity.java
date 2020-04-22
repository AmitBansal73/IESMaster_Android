package com.example.iesmaster.Payment;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.R;
import com.example.iesmaster.model.Profile;
import com.example.iesmaster.model.Test;
import com.example.iesmaster.model.Transaction;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    Button btnPayNow;
    Transaction currentTransaction;
    TextView txtSubject,txtUniversity,txtCost,txtYear,txtMessage;
    ProgressBar progressBar;
    NumberFormat currFormat;
    Profile myProfile;

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

        myProfile = Session.GetProfile(getApplicationContext());

        btnPayNow = findViewById(R.id.btnPayNow);
        txtSubject = findViewById(R.id.txtSubject);
        txtYear  = findViewById(R.id.txtYear);
        txtUniversity = findViewById(R.id.txtUniversity);
        txtCost = findViewById(R.id.txtCost);

        txtMessage = findViewById(R.id.txtMessage);
        progressBar = findViewById(R.id.progressBar);

        currFormat = NumberFormat.getCurrencyInstance();
        currFormat.setCurrency(Currency.getInstance("INR"));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Test selectedTest = new Test();
        selectedTest.SubjectName= bundle.getString("Subject");
        //selectedTest.TopicName= bundle.getString("TopicName");
        selectedTest.Semester= bundle.getString("Semester");
        selectedTest.Univesity= bundle.getString("University");
        selectedTest.Cost  = bundle.getInt("Cost");
        selectedTest.year = bundle.getInt("year");
        selectedTest.paperID = bundle.getInt("PaperID");


        txtSubject.setText(selectedTest.SubjectName);
        txtUniversity.setText(selectedTest.Univesity);
        txtCost.setText(currFormat.format(selectedTest.Cost));
        txtYear.setText(Integer.toString(selectedTest.year));
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTransaction = new Transaction();
                currentTransaction.UserID = Integer.parseInt(myProfile.UserID);
                currentTransaction.PaperID = selectedTest.paperID;
                currentTransaction.Paid = selectedTest.Cost;
                GetCheckSum(currentTransaction);
            }
        });
    }

    private void GetCheckSum(final Transaction transaction)
    {
        try {
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            String  url =  Constants.Application_URL+"/api/Paytm/GetChecksum" ;

            String reqBody = "{\"PaperID\":\""+ transaction.PaperID +"\",\"UserID\":\""+ transaction.UserID +"\",\"Paid\":\""+ transaction.Paid +
                    "\",\"PurchaseDate\":\"" +transaction.PurchaseDate + "\",\"ClosureDate\":\""+ transaction.ClosureDate +"\"}";

            JSONObject jsRequest = new JSONObject(reqBody);
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jObj) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        currentTransaction= new Transaction();
                        currentTransaction.UserID = transaction.UserID;
                        currentTransaction.PaperID = transaction.PaperID;
                        currentTransaction.Paid = transaction.Paid;
                        currentTransaction.checksum = jObj.getString("CheckSum");
                        currentTransaction.OrderID = jObj.getInt("OrderID");

                        MakePayment(currentTransaction);
                    }

                    catch (JSONException jEx) {
                        int a=1;
                        a++;
                        txtMessage.setText("! Exception while reading data");
                    }
                    catch (Exception ex)
                    {
                        int a=1;
                        a++;
                        txtMessage.setText("! Exception while reading data");
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    int a=1;
                    progressBar.setVisibility(View.GONE);
                    txtMessage.setText("! Volley Exception");
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0,-1,0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
            //*******************************************************************************************************
        } catch (Exception js) {
            int a=1;
            a++;
            progressBar.setVisibility(View.GONE);
            txtMessage.setText("! Network Exception");


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
                    PaymentActivity.this.finish();
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
