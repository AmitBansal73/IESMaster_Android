package com.example.iesmaster.Register;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.example.iesmaster.AcademicProfileActivity;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.Common.DataAccess;
import com.example.iesmaster.Common.ImageServer;
import com.example.iesmaster.Common.OvalImageView;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.Common.Utility;
import com.example.iesmaster.HomeActivity;
import com.example.iesmaster.R;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.iesmaster.Common.Constants.RESOLVE_HINT;

public class RegisterActivity extends AppCompatActivity {
    OvalImageView profileImage;
    ImageView addImage;
    EditText txtMobile,txtAddress,txtPassword,txtName,txtEmail;
    Button btnSubmit;
    Profile myProfile;
    boolean IsLogin = false;
    Bitmap newBitmap;
    String strImage;


    GoogleApiClient apiClient = new GoogleApiClient() {
        @Override
        public boolean hasConnectedApi(@NonNull Api<?> api) {
            return false;
        }

        @NonNull
        @Override
        public ConnectionResult getConnectionResult(@NonNull Api<?> api) {
            return null;
        }

        @Override
        public void connect() {

        }

        @Override
        public ConnectionResult blockingConnect() {
            return null;
        }

        @Override
        public ConnectionResult blockingConnect(long l, @NonNull TimeUnit timeUnit) {
            return null;
        }

        @Override
        public void disconnect() {

        }

        @Override
        public void reconnect() {

        }

        @Override
        public PendingResult<Status> clearDefaultAccountAndReconnect() {
            return null;
        }

        @Override
        public void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {

        }

        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public boolean isConnecting() {
            return false;
        }

        @Override
        public void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

        }

        @Override
        public boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks) {
            return false;
        }

        @Override
        public void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

        }

        @Override
        public void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

        }

        @Override
        public boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
            return false;
        }

        @Override
        public void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

        }

        @Override
        public void dump(String s, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strings) {

        }
    };

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
        btnSubmit = findViewById(R.id.btnSubmit);
        profileImage = findViewById(R.id.profileImage);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        txtName = findViewById(R.id.txtName);
        txtPassword = findViewById(R.id.txtPassword);

      //  myProfile = Session.GetProfile(getApplicationContext());
        myProfile = new Profile();
        Intent intent = getIntent();
         String loginType =  intent.getStringExtra("loginType");
        myProfile.UserLogin = intent.getStringExtra("login");
        myProfile.UserName = intent.getStringExtra("name");

        if(loginType.matches("google"))
        {
            IsLogin = true;
            txtName.setText(myProfile.UserName);
            txtEmail.setText(myProfile.UserLogin);
            txtPassword.setText("");
            txtName.setEnabled(false);
            txtEmail.setEnabled(false);
           // Glide.with(this).load(myProfile.ProfileImage).into(profileImage);
        }else {
            IsLogin = false;
        }


        txtMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestHint();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegistration();
            }
        });

    }

   public void UserRegistration(){
       String Name= txtName.getText().toString();
       String Email= txtEmail.getText().toString();
       String Mobile = txtMobile.getText().toString();
       myProfile.UserPassword= txtPassword.getText().toString();
       String Address= txtAddress.getText().toString();

       if(Name.matches("") ||  Mobile.matches("")|| Email.matches("")|| Address.matches("") )
       {
           Toast.makeText(getApplicationContext(),"Password Can not be Empty",Toast.LENGTH_LONG).show();
           return;
       }
       else
       {
           myProfile.UserName = Name;
           myProfile.UserLogin = Email;
           myProfile.MobileNumber = Mobile;
           myProfile.Address = Address;
       }

       if(!IsLogin) {
           if(txtPassword.getText().toString().matches(""))
           {
               Toast.makeText(getApplicationContext(),"Password Can not be Empty",Toast.LENGTH_LONG).show();
               return;
           }

       }
       ServerRegister();

    }

    private void SaveProfile()
    {
        Intent intent = new Intent(getApplicationContext(), AcademicProfileActivity.class);
        intent.putExtra("IsResult", false);
        startActivity(intent);
        RegisterActivity.this.finish();
    }

    public void EditImage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Constants.REQUEST_IMAGE_GET);
        }
    }

    private void requestHint() {
        try {
            HintRequest hintRequest = new HintRequest.Builder()
                    .setPhoneNumberIdentifierSupported(true)
                    .build();

            PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                    apiClient, hintRequest);
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT, null, 0, 0, 0);
        }
        catch (IntentSender.SendIntentException sex)
        {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == Constants.REQUEST_IMAGE_GET) {

                if (data != null) {
                    try {
                        Uri uri = data.getData();
                        InputStream image_stream = getContentResolver().openInputStream(uri);
                        byte[] imgByte = ImageServer.getBytes(image_stream);
                        ImageServer.SaveFileToExternal(imgByte, "crop.jpg", getApplicationContext());
                        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        File myDir = new File(root + "/IESMaster/crop.jpg");
                        myDir.mkdirs();
                        Uri contentUri = Uri.fromFile(myDir);
                        ImageCropFunction(contentUri);
                    }
                    catch (Exception ex)
                    {
                     int a=1;
                    }
                }
            } else if (requestCode == Constants.REQUEST_IMAGE_CROP) {

                if (data != null) {

                    Bundle bundle = data.getExtras();
                    if(bundle!= null) {
                        newBitmap = bundle.getParcelable("data");
                        profileImage.setImageBitmap(newBitmap);

                    }
                    else
                    {
                        Uri cropUri =  data.getData();
                        InputStream image_stream = getContentResolver().openInputStream(cropUri);
                        newBitmap= BitmapFactory.decodeStream(image_stream);
                        profileImage.setImageBitmap(newBitmap);

                    }
                    strImage = ImageServer.getStringFromBitmap(newBitmap);
                    profileImage.invalidate();
                }
            }
            else if (requestCode == RESOLVE_HINT) {
                if (resultCode == RESULT_OK) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId(); <-- E.164 format phone number on 10.2.+ devices
                }
            }

            else if(requestCode == Constants.REQUEST_OTP_VERIFY)
            {
                boolean result = data.getBooleanExtra("isVerified", false);
                if(result) {
                    Profile profile = new Profile();
                    profile.UserName = txtName.getText().toString();
                    profile.UserLogin = txtEmail.getText().toString();
                    profile.MobileNumber = txtMobile.getText().toString();
                    profile.UserPassword = txtPassword.getText().toString();
                    profile.UserID ="999";
                    Session.AddProfile(getApplicationContext(), profile);
                    Intent homeIntent = new Intent(RegisterActivity.this, AcademicProfileActivity.class);
                    startActivity(homeIntent);
                }
            }
        }
        catch (Exception ex)
        {
            int a=1;
        }
    }

    public void ImageCropFunction(Uri uri) {

        // Image Crop Code
        try {
            Intent CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 100);
            CropIntent.putExtra("outputY", 100);
            CropIntent.putExtra("aspectX", 1);
            CropIntent.putExtra("aspectY", 1);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            // CropIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            // CropIntent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(CropIntent, Constants.REQUEST_IMAGE_CROP);
        }
        catch (Exception e)
        {
            int a =1;
        }
    }


    public void ServerRegister(){

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String ActivationDate = Utility.CurrentDate();
        try {
            String url = Constants.Application_URL + "/api/User/Register";
            final String reqBody = "{\"Token\":\" \", \"Email\":\"" +myProfile.UserLogin  + "\", \"Password\":\"" + myProfile.UserPassword +
                    "\", \"ActivationDate\":\"" + ActivationDate+
                    "\",\"Name\":\"" + myProfile.UserName + "\",\"MobileNumber\":\"" +myProfile.MobileNumber + "\",\"Address\":\"" + myProfile.Address + "\"}";

            JSONObject jsRequest = new JSONObject(reqBody);
            //-------------------------------------------------------------------------------------------------
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                            if(response!=null) {
                                Profile userProfile = new Profile();
                                userProfile.UserID = response.getString("UserID");
                                userProfile.UserName = response.getString("Name");
                                userProfile.UserLogin = response.getString("Email");
                                userProfile.MobileNumber = response.getString("MobileNumber");
                                userProfile.Address = response.getString("Address");
                                Session.AddProfile(getApplicationContext(),userProfile);

                                Intent intent = new Intent(RegisterActivity.this, AcademicProfileActivity.class);
                                startActivity(intent);
                                RegisterActivity.this.finish();

                            }

                    } catch (Exception e) {
                        int a = 1;
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = error.toString();

                    // prgBar.setVisibility(View.GONE);
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);

            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);

            //*******************************************************************************************************
        } catch (JSONException js) {
            Toast.makeText(getApplicationContext(), "Could not Update Academic profile,Contact Admin", Toast.LENGTH_LONG).show();
        }

      /*
        Intent intent = new Intent(getApplicationContext(), MobileOTPActivity.class);
        intent.putExtra("number", Mobile);
        //intent.putExtra("IsResult", false);
        startActivityForResult(intent, Constants.REQUEST_OTP_VERIFY);  */




    }
}
