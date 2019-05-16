package com.example.iesmaster;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.iesmaster.Common.OvalImageView;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;

public class MyProfileActivity extends AppCompatActivity {
    OvalImageView profileImage;
    TextView txtName,txtEmail,txtMobile,txtAddress,txtUnivName,txtCollage,txtStream,txtStudentID,btnEditAcademicProfile,btnEditPersonalProfile;
    Profile myProfile;
    AcademicProfile myAcademicProfile;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" My Profile ");
        actionBar.show();

        myProfile = Session.GetProfile(getApplicationContext());
        myAcademicProfile = Session.GetAcademicProfile(getApplicationContext());

        profileImage = findViewById(R.id.profileImage);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        txtAddress = findViewById(R.id.txtAddress);
        txtUnivName = findViewById(R.id.txtUnivName);
        txtCollage = findViewById(R.id.txtCollage);
        txtStream = findViewById(R.id.txtStream);
        txtStudentID = findViewById(R.id.txtStudentID);
        btnEditPersonalProfile = findViewById(R.id.btnEditPersonalProfile);
        btnEditPersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent personal = new Intent(MyProfileActivity.this,RegisterActivity.class);
                startActivity(personal);
            }
        });
        btnEditAcademicProfile = findViewById(R.id.btnEditAcademicProfile);
        btnEditAcademicProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent academic = new Intent(MyProfileActivity.this,AcademicProfileActivity.class);
                startActivity(academic);
            }
        });
        Glide.with(this).load(myProfile.ProfileImage).into(profileImage);
        txtName.setText(myProfile.UserName);
        txtEmail.setText(myProfile.UserLogin);
        txtMobile.setText(myProfile.MobileNumber);
        txtAddress.setText("");
        txtUnivName.setText(myAcademicProfile.UniversityName);
        txtCollage.setText(myAcademicProfile.CollegeName);
        txtStream.setText(myAcademicProfile.Stream+", "+myAcademicProfile.Semester);
        txtStudentID.setText(myAcademicProfile.UserID);
    }
}
