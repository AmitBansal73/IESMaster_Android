package com.example.iesmaster.Register;

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
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.HomeActivity;
import com.example.iesmaster.R;
import com.example.iesmaster.model.Profile;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Random;

import okhttp3.internal.http2.ErrorCode;

public class MobileOTPActivity extends AppCompatActivity {

    EditText txtValidation1,txtValidation2,txtValidation3,txtValidation4;
    Button btnValidate;
    private MySMSReceiver smsBroadcastReceiver;
    TextView txtResendOtp;
    private String RANDOM_OTP = "";
    private String MobileNumber="";
    int range = 9;
    int length = 4;

    Button btnSend;
    EditText txtMobile;
    Profile myProfile;
    TextView txtMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);

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

        btnSend = findViewById(R.id.btnSend);
        txtMobile = findViewById(R.id.txtMobile);
        txtMessage = findViewById(R.id.txtMessage);
        ReadyToRequestOTP();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSend.setEnabled(false);
                txtMobile.setEnabled(false);
                MobileNumber = txtMobile.getText().toString();
                GenerateOTP();
            }
        });

        smsBroadcastReceiver = new MySMSReceiver(Constants.SMS_SERVICE_PROVIDER_NUMBER, Constants.SMS_MESSAGE_CONDITION);
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        smsBroadcastReceiver.setListener(new MySMSReceiver.Listener() {
            @Override
            public void onTextReceived(String text) {


                try {
                    String strtext = text;

                    if (text.contains(Constants.SMS_MESSAGE_CONDITION)) {

                        String code = text.substring(text.length() - 4, text.length());
                        // int intCode = Integer.parseInt(code);
                        txtValidation1.setText(String.valueOf(code.charAt(0)));
                        txtValidation2.setText(String.valueOf(code.charAt(1)));
                        txtValidation3.setText(String.valueOf(code.charAt(2)));
                        txtValidation4.setText(String.valueOf(code.charAt(3)));

                        Validate();

                    }
                }
                catch (Exception ex)
                {
                    txtMessage.setText("Error Reading Message");
                   // Toast.makeText(getApplicationContext(),"OnSMSReceive", Toast.LENGTH_LONG).show();
                    int a =1;
                }
            }
        });

       // GenerateOTP();
    }


    private void GenerateOTP(){
        /// Create Random OTP
        RANDOM_OTP =CreateOTP();

        String url = Constants.Application_URL + "/api/User/sendOTP";
        String reqBody = "{\"String_OTP\":\""+RANDOM_OTP  + "\",\"MobileNumber\":\""+ MobileNumber + "\"}";
        try {
            JSONObject jsRequest = new JSONObject(reqBody);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if(response!= null)
                        {

                            String result = response.getString("status");
                             if(result.equalsIgnoreCase("Failed")){

                                 Toast.makeText(getApplicationContext(), "SMS service Failed", Toast.LENGTH_LONG);
                                 ReadyToRequestOTP();
                             }
                             else
                             {
                                 ReadyToVerify();
                                // Validate();
                             }
                                              }
                         }catch (Exception ex){
                        txtMessage.setText("Error Reading Response");
                        ReadyToRequestOTP();

                       int a= 1;
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   int errorCode= error.networkResponse.statusCode;
                   if(errorCode == 400)
                   {
                       Toast.makeText(getApplicationContext(), "Wrong OTP try again", Toast.LENGTH_LONG);
                       txtMessage.setText("Wrong OTP try again");

                   }
                    txtMessage.setText("VolleyError");
                    ReadyToRequestOTP();

                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0,-1,0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);

        }catch (Exception e){
            txtMessage.setText("Error Sending Request");
            ReadyToRequestOTP();
        }

    }

    public void Submit(View v)
    {
        if(
        !txtValidation1.getText().toString().matches("") &&
        !txtValidation2.getText().toString().matches("")&&
        !txtValidation3.getText().toString().matches("")&&
        !txtValidation4.getText().toString().matches("")
        )
        {Validate();}
        else
        {return;
        }

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
                        //Validate();
                        //txtValidation3.requestFocus();
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

    private void Validate()
    {
        String CodeReceived = txtValidation1.getText().toString() +  txtValidation2.getText().toString()
                             + txtValidation3.getText().toString() +  txtValidation4.getText().toString();

        if(CodeReceived.matches(RANDOM_OTP))
        {
            GetUserDetailByMobile();
              /*  Intent intent = new Intent(MobileOTPActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
*/
                //GetLoginDetailByMobile();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Wrong OTP try again", Toast.LENGTH_LONG);
            ReadyToRequestOTP();

        }
    }

    private void ReadyToRequestOTP()
    {
        btnSend.setEnabled(true);
        txtMobile.setEnabled(true);
        txtValidation1.setEnabled(false);
        txtValidation2.setEnabled(false);
        txtValidation3.setEnabled(false);
        txtValidation4.setEnabled(false);
        txtValidation1.setText("");
        txtValidation2.setText("");
        txtValidation3.setText("");
        txtValidation4.setText("");
    }

    private void ReadyToVerify()
    {
        btnSend.setEnabled(false);
        txtMobile.setEnabled(false);
        txtValidation1.setEnabled(true);
        txtValidation2.setEnabled(true);
        txtValidation3.setEnabled(true);
        txtValidation4.setEnabled(true);
    }


    private void GetUserDetailByMobile(){

        String url = Constants.Application_URL + "/api/User/Mobile/" + MobileNumber;

        try {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if(response!= null)
                        {
                            myProfile = new Profile();
                            myProfile.UserName = response.getString("Name");
                            myProfile.UserPassword = response.getString("Password");
                            myProfile.UserLogin = response.getString("Email");
                            myProfile.MobileNumber = response.getString("MobileNumber");
                            myProfile.ProfileImage = "";
                            myProfile.UserID = response.getString("UserID");
                            myProfile.Address = response.getString("Address");

                            Session.AddProfile(getApplicationContext(), myProfile);
                            Intent intent = new Intent(MobileOTPActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }


                    }catch (Exception ex){
                        int a= 1;
                        ReadyToRequestOTP();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    int errorCode= error.networkResponse.statusCode;
                    if(errorCode == 400)
                    {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                        ReadyToRequestOTP();
                    }


                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0,-1,0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);

        }catch (Exception e){
            ReadyToRequestOTP();
        }

    }

    private String CreateOTP(){
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
      return OTP;
    }
}
