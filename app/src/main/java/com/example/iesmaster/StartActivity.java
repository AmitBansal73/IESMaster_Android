package com.example.iesmaster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.iesmaster.Common.Session;
import com.example.iesmaster.Register.LoginActivity;
import com.example.iesmaster.Register.WelcomeActivity;
import com.example.iesmaster.model.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import static com.example.iesmaster.Common.Constants.SMS_PERMISSION_CODE;

public class StartActivity extends AppCompatActivity {
    ImageView Image;
    Profile myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Image = findViewById(R.id.Image);
        //Glide.with(this).load(R.drawable.start).asGif().into(Image);



        if (!isSmsPermissionGranted())
        {
            requestReadAndSendSmsPermission();
        }
        else
        {
            CheckSignIn();
        }

    }

    private void updateUI(GoogleSignInAccount account)
    {
        if(account == null)
        {
            myProfile = Session.GetProfile(getApplicationContext());
            if(myProfile.UserLogin.matches("")) {
                Intent intent = new Intent(StartActivity.this, WelcomeActivity.class);
                startActivity(intent);
                StartActivity.this.finish();
            }
            else
            {
              Intent intent = new Intent(StartActivity.this, HomeActivity.class);
               startActivity(intent);
                StartActivity.this.finish();
            }
        }

       /* else if(account.isExpired())
        {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        } */
        else
        {
            Intent intent = new Intent(StartActivity.this, HomeActivity.class);
            startActivity(intent);
            StartActivity.this.finish();
        }
    }

    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request runtime SMS permission
     */
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html

            CheckSignIn();
        }
        else
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    CheckSignIn();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    CheckSignIn();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void CheckSignIn()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

    }
}
