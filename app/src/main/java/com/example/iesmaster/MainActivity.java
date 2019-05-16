package com.example.iesmaster;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
        userName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        Login_sec = findViewById(R.id.Login_sec);
        Login_sec.setVisibility(View.VISIBLE);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();


        btnRegister= findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testActivity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(testActivity);
            }
        });


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
                    Profile myProfile = new Profile();
                    myProfile.UserName = txtUserName.getText().toString();
                    myProfile.UserPassword = txtPassword.getText().toString();
                    myProfile.UserLogin = txtUserName.getText().toString();
                    myProfile.MobileNumber = "";
                    myProfile.ProfileImage = "";
                    myProfile.UserID ="0";
;                    boolean loginResult = Session.AddProfile(getApplicationContext(), myProfile);

                    if(loginResult)
                    {
                        Intent profileActivity = new Intent(MainActivity.this, AcademicProfileActivity.class);
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


            Profile myProfile = new Profile();
            myProfile.UserName = account.getDisplayName();
            myProfile.UserLogin = account.getEmail();
            myProfile.ProfileImage = account.getPhotoUrl().toString();
            myProfile.MobileNumber = "";
            myProfile.UserID = "";
            myProfile.UserPassword = "Google";

            Session.AddProfile(getApplicationContext(),myProfile);
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
           // MainActivity.this.finish();
          //  UpdateUI(true);
        }
        else {
            UpdateUI(false);
        }
    }

    public void UpdateUI(boolean isLogin){

        if (isLogin){

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
          //  profile_sec.setVisibility(View.VISIBLE);
           // Login_sec.setVisibility(View.GONE);

        }else {
           // profile_sec.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Alert");
        builder.setMessage("Are you sure to Exit ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
