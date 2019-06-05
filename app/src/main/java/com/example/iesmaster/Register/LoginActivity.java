package com.example.iesmaster.Register;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.iesmaster.AcademicProfileActivity;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.HomeActivity;
import com.example.iesmaster.R;
import com.example.iesmaster.model.AcademicProfile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button btnLoginMannual,btnNext,btnSkip;
    TextView txtUserName,txtPassword,btnRegister;
    Button btnMobile;
    LinearLayout profile_sec,Login_sec;
    Button signOut;
    TextView userName,txtEmail;
    ImageView profileImg;
    SignInButton btnGoogleSignin;
    GoogleApiClient googleApiClient;

    GoogleSignInClient mGoogleSignInClient;

    private static final int REQ_CODE = 9001;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        btnGoogleSignin = findViewById(R.id.btnGoogleSignin);
        btnGoogleSignin.setOnClickListener(this);
        btnMobile = findViewById(R.id.btnMobile);
        btnMobile.setOnClickListener(this);

        Login_sec = findViewById(R.id.Login_sec);
        Login_sec.setVisibility(View.VISIBLE);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
      //  googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
        //        .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();


        btnRegister= findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                registerActivity.putExtra("loginType", "new");
                registerActivity.putExtra("login", "");
                registerActivity.putExtra("name", "");
                startActivity(registerActivity);
            }
        });


        btnLoginMannual = findViewById(R.id.btnLoginMannual);
        btnLoginMannual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtUserName.getText().toString().matches("") || txtPassword.getText().toString().matches(""))
                {
                    Toast.makeText(getApplicationContext(),"Login or Password is empty", Toast.LENGTH_LONG);
                }
                else
                {
                    MannualLogin();
                }

            }
        });

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

    }




    public void MannualLogin(){


        String Email= txtUserName.getText().toString();
        String Password= txtPassword.getText().toString();
        txtUserName.setEnabled(false);
        txtPassword.setEnabled(false);
        btnLoginMannual.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);


        try {
            String url = Constants.Application_URL + "/api/User/Login";
            String reqBody = "{\"Token\":\"  \", \"Email\":\"" +Email  + "\", \"Password\":\"" + Password + "\"}";

            JSONObject jsRequest = new JSONObject(reqBody);
            //-------------------------------------------------------------------------------------------------
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.setVisibility(View.GONE);
                    try {

                        if(response!=null) {

                            Profile myProfile = new Profile();
                            myProfile.UserName = response.getString("Name");
                            myProfile.UserPassword = response.getString("Password");
                            myProfile.UserLogin = response.getString("Email");
                            myProfile.MobileNumber = response.getString("MobileNumber");
                            myProfile.ProfileImage = "";
                            myProfile.UserID = response.getString("UserID");
                            myProfile.Address = response.getString("Address");

                            Session.AddProfile(getApplicationContext(), myProfile);

                            GetAcademicProfile(myProfile.UserID);
                           /* Intent profileActivity = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(profileActivity);
                            LoginActivity.this.finish();*/

                          /*  if (loginResult) {
                                Intent profileActivity = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(profileActivity);
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error Saving Login info", Toast.LENGTH_LONG);
                            }  */
                        }

                    } catch (JSONException e) {
                        int a = 1;
                        txtUserName.setEnabled(true);
                        txtPassword.setEnabled(true);
                        btnLoginMannual.setEnabled(true);

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = error.toString();
                    txtUserName.setEnabled(true);
                    txtPassword.setEnabled(true);
                    btnLoginMannual.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);

            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);

            //*******************************************************************************************************
        } catch (JSONException js) {
            Toast.makeText(getApplicationContext(), "Login Failed ,Contact Admin", Toast.LENGTH_LONG).show();
            txtUserName.setEnabled(true);
            txtPassword.setEnabled(true);
            btnLoginMannual.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
    }


    private void GetAcademicProfile(String UserID){
        try {
            progressBar.setVisibility(View.VISIBLE);
            String url = Constants.Application_URL + "/api/User/GetAcademic/" + UserID;

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject obj) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        Toast.makeText(getApplicationContext(), "Login Successfully. Getting Data", Toast.LENGTH_SHORT).show();
                        if(obj != null) {
                            AcademicProfile myProfile = new AcademicProfile();
                            myProfile.UniversityID = obj.getInt("UniversityID");
                            myProfile.UniversityName = obj.getString("UniversityName");
                            myProfile.CollegeID = obj.getInt("CollegeID");
                            myProfile.CollegeName = obj.getString("CollegeName");
                            myProfile.StreamID =  obj.getInt("StreamID");
                            myProfile.Stream = obj.getString("StreamName");
                            myProfile.SemesterID = obj.getInt("SemesterID");
                            myProfile.Semester = obj.getString("SemesterName");
                            myProfile.UserID = obj.getString("UserID");

                            Session.AddAcademicProfile(getApplicationContext(), myProfile);

                            Intent profileActivity = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(profileActivity);
                            LoginActivity.this.finish();

                          /*  if (loginResult) {
                                Intent profileActivity = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(profileActivity);
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error Saving Login info", Toast.LENGTH_LONG);
                            }  */
                        }
                        else{
                            Intent profileActivity = new Intent(LoginActivity.this, AcademicProfileActivity.class);
                            startActivity(profileActivity);
                            LoginActivity.this.finish();
                        }

                    } catch (JSONException e) {
                        int a = 1;
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Login Failed, Error retreiving Academic Profile ", Toast.LENGTH_LONG).show();
                    /*String message = error.toString();
                    Intent profileActivity = new Intent(LoginActivity.this, AcademicProfileActivity.class);
                    startActivity(profileActivity);
                    LoginActivity.this.finish();
                    prgBar.setVisibility(View.GONE);*/
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);

            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);

            //*******************************************************************************************************
        } catch (Exception js) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Login Failed ,Contact Admin", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onStart() {
      //  GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
       // UpdateUI(account);
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnGoogleSignin:
                SignIn();
                break;
            case R.id.btnMobile:
                Intent mobileIntent = new Intent(LoginActivity.this, MobileOTPActivity.class);
                startActivity(mobileIntent);
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void SignIn(){

    progressBar.setVisibility(View.VISIBLE);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);

       // Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
       // startActivityForResult(intent,REQ_CODE);
    }

   /* public void SignOut(){

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                UpdateUI(false);
                Toast.makeText(LoginActivity.this, "You are Successfully SignOut", Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
/*
    public void UpdateUI(boolean isLogin){
        if (isLogin){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
          //  profile_sec.setVisibility(View.VISIBLE);
           // Login_sec.setVisibility(View.GONE);
        }else {
            Toast.makeText(getApplicationContext(),"Error in Google Signin", Toast.LENGTH_LONG).show();
            Login_sec.setVisibility(View.VISIBLE);
        }
    }
*/

    public void UpdateUI(GoogleSignInAccount account){

        if(account== null)
        {
            Toast.makeText(getApplicationContext(),"Error in Google Signin", Toast.LENGTH_LONG).show();
            Login_sec.setVisibility(View.VISIBLE);
        }

       else if (!account.isExpired()){

            Profile myProfile = new Profile();
            myProfile.UserName = account.getDisplayName();
            myProfile.UserLogin = account.getEmail();
            myProfile.ProfileImage = account.getPhotoUrl().toString();
            myProfile.MobileNumber = "";
            myProfile.UserID = "";
            myProfile.UserPassword = "";

            GetUserData( myProfile.UserLogin, myProfile.UserName);

            /*
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();*/
            //  profile_sec.setVisibility(View.VISIBLE);
            // Login_sec.setVisibility(View.GONE);
        }else {
            Toast.makeText(getApplicationContext(),"Error in Google Signin", Toast.LENGTH_LONG).show();
            Login_sec.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ_CODE){
           // GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           // handleResult(result);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            UpdateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            UpdateUI(null);
        }
    }

    private void handleResult(GoogleSignInResult result){
        progressBar.setVisibility(View.GONE);
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            Profile myProfile = new Profile();
            myProfile.UserName = account.getDisplayName();
            myProfile.UserLogin = account.getEmail();
            myProfile.ProfileImage = account.getPhotoUrl().toString();
            myProfile.MobileNumber = "";
            myProfile.UserID = "";
            myProfile.UserPassword = "";

            GetUserData( myProfile.UserLogin, myProfile.UserName);
         //   Session.AddProfile(getApplicationContext(),myProfile);

          //  UpdateUI(true);
        }
        else {
            UpdateUI(null);
        }
    }



    public void GetUserData(final String Email, final String Name){
        try {
            txtUserName.setEnabled(false);
            txtPassword.setEnabled(false);
            btnLoginMannual.setEnabled(false);
            String url = Constants.Application_URL + "/api/User/Email";
            String reqBody = "{\"Token\":\"  \", \"Email\":\"" +Email  + "\", \"Password\":\"\"}";

            JSONObject jsRequest = new JSONObject(reqBody);
            //-------------------------------------------------------------------------------------------------
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        if(response!=null) {
                          int id = response.getInt("UserID");
                            if(id>0) {
                                Profile myProfile = new Profile();
                                myProfile.UserName = response.getString("Name");
                                myProfile.UserPassword = response.getString("Password");
                                myProfile.UserLogin = response.getString("Email");
                                myProfile.MobileNumber = response.getString("MobileNumber");
                                myProfile.ProfileImage = "";
                                myProfile.UserID = response.getString("UserID");
                                myProfile.Address = response.getString("Address");
                                Session.AddProfile(getApplicationContext(), myProfile);
                                GetAcademicProfile(myProfile.UserID);
                            }
                            else{
                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                intent.putExtra("loginType", "google");
                                intent.putExtra("login", Email);
                                intent.putExtra("name", Name);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        }
                        else
                        {
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("loginType", "google");
                            intent.putExtra("login", Email);
                            intent.putExtra("name", Name);

                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                    } catch (JSONException e) {
                        int a = 1;
                        txtUserName.setEnabled(true);
                        txtPassword.setEnabled(true);
                        btnLoginMannual.setEnabled(true);

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = error.toString();
                    txtUserName.setEnabled(true);
                    txtPassword.setEnabled(true);
                    btnLoginMannual.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);

            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);

            //*******************************************************************************************************
        } catch (JSONException js) {
            Toast.makeText(getApplicationContext(), "Login Failed ,Contact Admin", Toast.LENGTH_LONG).show();
            txtUserName.setEnabled(true);
            txtPassword.setEnabled(true);
            btnLoginMannual.setEnabled(true);
            progressBar.setVisibility(View.GONE);
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
                LoginActivity.this.finish();
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
