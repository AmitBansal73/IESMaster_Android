package com.example.iesmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button LoginMannual,btnNext,btnSkip;
    TextView txtUserName,txtPassword,btnRegister;

    LinearLayout profile_sec,Login_sec;
    Button signOut;
    TextView userName,txtEmail;
    ImageView profileImg;
    SignInButton signIn;
    GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(" IES Master ");
        actionBar.show();

        progressBar = findViewById(R.id.progressBar);
        userName = findViewById(R.id.userName);
        profileImg = findViewById(R.id.profileImg);
        txtEmail = findViewById(R.id.txtEmail);
        profile_sec = findViewById(R.id.profile_sec);
        signIn = findViewById(R.id.signIn);
        signOut = findViewById(R.id.signOut);
        profile_sec.setVisibility(View.GONE);
        signIn.setOnClickListener(this);
        signOut.setOnClickListener(this);
        Login_sec = findViewById(R.id.Login_sec);
        Login_sec.setVisibility(View.VISIBLE);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        btnNext= findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testActivity = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(testActivity);
            }
        });
        btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testActivity = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(testActivity);
            }
        });
        btnRegister= findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testActivity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(testActivity);
            }
        });

       // LoginWithGoogle = findViewById(R.id.LoginWithGoogle);
       // LoginWithGoogle.setOnClickListener(new View.OnClickListener() {
        //    @Override
       //     public void onClick(View v) {

           /*    Profile academicProfile = Session.GetAcademicProfile(getApplicationContext());
                if(academicProfile== null || academicProfile.UniversityID ==0) {
                    Intent profileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileActivity);
                    MainActivity.this.finish();
                }
                else
                {
                    Intent testActivity = new Intent(MainActivity.this, SubjectActivity.class);
                    startActivity(testActivity);
                    MainActivity.this.finish();
                }

                Intent testActivity = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(testActivity);

            }
        });*/

        Profile myProfile = Session.GetLogin(getApplicationContext());

        if(!myProfile.UserLogin.matches(""))
        {
            Profile academicProfile = Session.GetAcademicProfile(getApplicationContext());

            if(academicProfile== null || academicProfile.UniversityID ==0) {
                Intent profileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileActivity);
                MainActivity.this.finish();
            }
            else
            {
                Intent testActivity = new Intent(MainActivity.this, SubjectActivity.class);
                startActivity(testActivity);
                MainActivity.this.finish();
            }
        }

        LoginMannual = findViewById(R.id.LoginMannual);
        LoginMannual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtUserName.getText().toString().matches("") || txtPassword.getText().toString().matches(""))
                {
                    Toast.makeText(getApplicationContext(),"Login or Password is empty", Toast.LENGTH_LONG);
                }
                else
                {
                    String login = txtUserName.getText().toString();
                    String Password = txtPassword.getText().toString();
                    boolean loginResult = Session.AddLogin(getApplicationContext(), login, Password);

                    if(loginResult)
                    {
                        Intent profileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(profileActivity);
                        MainActivity.this.finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error Saving Login info", Toast.LENGTH_LONG);
                    }

                }


            }
        });


        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.signIn:
                SignIn();
                break;
            case R.id.signOut:
                SignOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void SignIn(){
    progressBar.setVisibility(View.VISIBLE);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    public void SignOut(){

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                UpdateUI(false);
                Toast.makeText(MainActivity.this, "You are Successfully SignOut", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            progressBar.setVisibility(View.GONE);
            GoogleSignInAccount account = result.getSignInAccount();
            String Name = account.getDisplayName();
            String Email = account.getEmail();
            String img_url = account.getPhotoUrl().toString();
           // userName.setText(Name);
           // txtEmail.setText(Email);
           // Glide.with(this).load(img_url).into(profileImg);
           // UpdateUI(true);

            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            intent.putExtra("name", Name);
            intent.putExtra("email", Email);
            intent.putExtra("img", img_url);
            startActivity(intent);
        }else {
            UpdateUI(false);
        }
    }

    public void UpdateUI(boolean isLogin){

        if (isLogin){
            profile_sec.setVisibility(View.VISIBLE);
            Login_sec.setVisibility(View.GONE);

        }else {
            profile_sec.setVisibility(View.GONE);
            Login_sec.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
