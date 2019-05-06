package com.example.iesmaster;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RegisterActivity extends AppCompatActivity {

    ImageView profileImage;
    EditText txtMobile,txtParentName,txtAddress,txtPassword,txtName,txtEmail;
    TextView attachedFile;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" IES Master ");
        actionBar.show();

        btnSubmit= findViewById(R.id.btnSubmit);
        attachedFile = findViewById(R.id.btnSubmit);
        profileImage = findViewById(R.id.profileImage);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        txtName = findViewById(R.id.txtName);
        txtParentName = findViewById(R.id.txtParentName);
        txtPassword = findViewById(R.id.txtPassword);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        String Name = intent.getStringExtra("name");
        String Email= intent.getStringExtra("email");
        String img_url = intent.getStringExtra("img");
        txtName.setText(Name);
        txtEmail.setText(Email);
        Glide.with(this).load(img_url).into(profileImage);
       // profileImage.setText(Name);
    }
}
