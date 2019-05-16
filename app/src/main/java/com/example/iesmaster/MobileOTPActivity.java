package com.example.iesmaster;

import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.iesmaster.Common.MySMSReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Random;

public class MobileOTPActivity extends AppCompatActivity {

    EditText txtValidation1,txtValidation2,txtValidation3,txtValidation4;
    Button btnValidate;
    private MySMSReceiver smsBroadcastReceiver;
    TextView txtResendOtp;
    private String RANDOM_OTP = "";
    private String MobileNumber="";
    int range = 9;
    int length = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        MobileNumber= bundle.getString("number");

        txtValidation1 = findViewById(R.id.txtValidation1);
        txtValidation2 = findViewById(R.id.txtValidation2);
        txtValidation3 = findViewById(R.id.txtValidation3);
        txtValidation4 = findViewById(R.id.txtValidation4);
        btnValidate = findViewById(R.id.btnValidate);
        txtResendOtp = findViewById(R.id.txtResendOtp);
        txtResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerateOTP();
            }
        });
        txtValidation1.addTextChangedListener(new GenericTextWatcher(txtValidation1));
        txtValidation2.addTextChangedListener(new GenericTextWatcher(txtValidation2));
        txtValidation3.addTextChangedListener(new GenericTextWatcher(txtValidation3));
        txtValidation4.addTextChangedListener(new GenericTextWatcher(txtValidation4));


        smsBroadcastReceiver = new MySMSReceiver(Constants.SMS_SERVICE_PROVIDER_NUMBER, Constants.SMS_MESSAGE_CONDITION);
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        smsBroadcastReceiver.setListener(new MySMSReceiver.Listener() {
            @Override
            public void onTextReceived(String text) {

                String strtext = text;

               if(text.contains(Constants.SMS_MESSAGE_CONDITION)){

                 String code =  text.substring(text.length()-4, text.length());
                // int intCode = Integer.parseInt(code);
                 /*  txtValidation1.setText(String.valueOf(code.charAt(0)));
                   txtValidation2.setText(String.valueOf(code.charAt(1)));
                   txtValidation3.setText(String.valueOf(code.charAt(2)));
                   txtValidation4.setText(String.valueOf(code.charAt(3)));  */

                if(code.equalsIgnoreCase(RANDOM_OTP))
                 {
                     SendToServer();
                 }
               }
            }
        });

        GenerateOTP();
    }


    private void GenerateOTP(){
        /// Create Random OTP
        String OTP="";
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) {
                i = -1;
                continue;
            }
            OTP = OTP + number;
        }

        String url = Constants.Application_URL + "api/User/sendOTP";
        String reqBody = "{\"String_OTP\":\""+OTP  + "\",\"MobileNumber\":\""+ MobileNumber + "\"}";
        try {
            JSONObject jsRequest = new JSONObject(reqBody);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if(response.getString("Response").matches("OK")) {
                            Toast.makeText(getApplicationContext(), "OTP sent ", Toast.LENGTH_SHORT).show();

                            String RANDOM_OTP = response.getString("String_OTP");

                            txtValidation1.setText(String.valueOf(RANDOM_OTP.charAt(0)));
                            txtValidation2.setText(String.valueOf(RANDOM_OTP.charAt(1)));
                            txtValidation3.setText(String.valueOf(RANDOM_OTP.charAt(2)));
                            txtValidation4.setText(String.valueOf(RANDOM_OTP.charAt(3)));
                        }

                    }catch (Exception ex){
                       int a= 1;
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                     int a=2;

                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0,-1,0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);

        }catch (Exception e){
          int a=3;
        }

    }

    public void Submit(View v)
    {
        String manualCode="";

        if(manualCode.matches(RANDOM_OTP)) {
            Intent intent = new Intent();
            intent.putExtra("isVerified", true);
            setResult(Constants.REQUEST_OTP_VERIFY, intent);
            this.finish();
        }
    }


   private void SendToServer()
    {
        Intent intent = new Intent();
        intent.putExtra("isVerified", true);
        setResult(Constants.REQUEST_OTP_VERIFY, intent);
        this.finish();

    }






    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.txtValidation1:
                    if(text.length()==1)
                        txtValidation2.requestFocus();
                    break;
                case R.id.txtValidation2:
                    if(text.length()==1)
                        txtValidation3.requestFocus();
                    else if(text.length()==0)
                        txtValidation1.requestFocus();
                    break;
                case R.id.txtValidation3:
                    if(text.length()==1)
                        txtValidation4.requestFocus();
                    else if(text.length()==0)
                        txtValidation2.requestFocus();
                    break;
                case R.id.txtValidation4:
                    if(text.length()==0)
                        txtValidation3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    public void setOtp(String otp) {
        if (otp.length() != 4) return;
        txtValidation1.setText(String.valueOf(otp.charAt(0)));
        txtValidation2.setText(String.valueOf(otp.charAt(1)));
        txtValidation3.setText(String.valueOf(otp.charAt(2)));
        txtValidation4.setText(String.valueOf(otp.charAt(3)));
    }
}
