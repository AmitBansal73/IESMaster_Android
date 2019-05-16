package com.example.iesmaster;

import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.Common.MySMSReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MobileOTPActivity extends AppCompatActivity {

    TextView txtValidation;
    Button btnValidate;
    private MySMSReceiver smsBroadcastReceiver;

    private int RANDOM_OTP = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);

        txtValidation = findViewById(R.id.txtValidation);
        btnValidate = findViewById(R.id.btnValidate);

        smsBroadcastReceiver = new MySMSReceiver(Constants.SMS_SERVICE_PROVIDER_NUMBER, Constants.SMS_MESSAGE_CONDITION);
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        smsBroadcastReceiver.setListener(new MySMSReceiver.Listener() {
            @Override
            public void onTextReceived(String text) {

                String strtext = text;

               if(text.contains(Constants.SMS_MESSAGE_CONDITION)){

                 String code =  text.substring(text.length()-4, text.length());
                 int intCode = Integer.parseInt(code);

                 if(intCode ==RANDOM_OTP)
                 {
                     SendToServer();
                 }

               }
            }
        });
    }

    public void Submit(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("isVerified", true);
        setResult(Constants.REQUEST_OTP_VERIFY, intent);
        this.finish();
    }


   private void SendToServer()
    {
        Intent intent = new Intent();
        intent.putExtra("isVerified", true);
        setResult(Constants.REQUEST_OTP_VERIFY, intent);
        this.finish();

    }
}
