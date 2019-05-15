package com.example.iesmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MobileOTPActivity extends AppCompatActivity {

    TextView txtValidation;
    Button btnValidate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);

        txtValidation = findViewById(R.id.txtValidation);
        btnValidate = findViewById(R.id.btnValidate);
    }

    public void Submit(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("isVerified", true);
        setResult(3, intent);
        this.finish();
    }
}
