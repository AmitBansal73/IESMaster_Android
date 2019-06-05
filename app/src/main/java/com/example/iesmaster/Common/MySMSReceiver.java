package com.example.iesmaster.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class MySMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";

    private final String serviceProviderNumber;
    private final String serviceProviderSmsCondition;

    private Listener listener;

    public MySMSReceiver(String serviceProviderNumber, String serviceProviderSmsCondition) {

        this.serviceProviderNumber = serviceProviderNumber;
        this.serviceProviderSmsCondition = serviceProviderSmsCondition;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                String smsSender = "";
                String smsBody = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                        smsSender = smsMessage.getDisplayOriginatingAddress();
                        smsBody += smsMessage.getMessageBody();
                    }
                } else {
                    Bundle smsBundle = intent.getExtras();
                    if (smsBundle != null) {
                        Object[] pdus = (Object[]) smsBundle.get("pdus");
                        if (pdus == null) {
                            // Display some error to the user
                            Log.e(TAG, "SmsBundle had no pdus key");

                            return;
                        }
                        SmsMessage[] messages = new SmsMessage[pdus.length];
                        for (int i = 0; i < messages.length; i++) {
                            messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            smsBody += messages[i].getMessageBody();
                        }
                        smsSender = messages[0].getOriginatingAddress();
                    }
                }
                if (listener != null) {
                    listener.onTextReceived(smsBody);
                }
                else{
                    Toast.makeText(context, "Listener is null", Toast.LENGTH_LONG).show();
                }

             /*   smsSender = smsSender.substring(smsSender.length() - 10, smsSender.length());
                if (smsSender.matches(serviceProviderNumber)) {
                    if (listener != null) {
                        listener.onTextReceived(smsBody);
                    }
                }*/
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(context,"Error service - OnReceive", Toast.LENGTH_LONG).show();
        }
    }

   public void setListener(Listener listener) {
        this.listener = listener;
    }

   public interface Listener {
        void onTextReceived(String text);
    }
}
