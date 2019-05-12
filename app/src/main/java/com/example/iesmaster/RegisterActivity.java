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

import com.bumptech.glide.Glide;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.Profile;

public class RegisterActivity extends AppCompatActivity {

    ImageView profileImage;
    EditText txtMobile,txtAddress,txtPassword,txtName,txtEmail;
    Button btnSubmit;
    Profile myProfile;

    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_IMAGE_CROP = 2;

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
        actionBar.setTitle("User Registration");
        actionBar.show();

        myProfile = Session.GetProfile(getApplicationContext());

        if(myProfile.UserLogin!= null)
        {
            txtName.setText(myProfile.UserName);
            txtEmail.setText(myProfile.UserLogin);
            txtName.setEnabled(false);
            txtEmail.setEnabled(false);
            Glide.with(this).load(myProfile.ProfileImage).into(profileImage);
        }

        btnSubmit= findViewById(R.id.btnSubmit);
        profileImage = findViewById(R.id.profileImage);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        txtName = findViewById(R.id.txtName);

        txtPassword = findViewById(R.id.txtPassword);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveProfile();
            }
        });

    }



    private void SaveProfile()
    {
        Intent intent = new Intent(getApplicationContext(), AcademicProfileActivity.class);
        intent.putExtra("IsResult", false);
        startActivity(intent);
        RegisterActivity.this.finish();

    }
}
