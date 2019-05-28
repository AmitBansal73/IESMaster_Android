package com.example.iesmaster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.Common.ImageServer;
import com.example.iesmaster.Common.OvalImageView;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;
import com.google.android.gms.auth.api.credentials.Credential;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import static com.example.iesmaster.Common.Constants.RESOLVE_HINT;

public class MyProfileActivity extends AppCompatActivity {
    OvalImageView profileImage,editProfileImage;
    TextView txtName,txtEmail,txtMobile,txtAddress,txtUnivName,txtCollage,txtStream,txtStudentID,btnEditAcademicProfile,btnEditPersonalProfile,
            editEmail,editName;
    Profile myProfile;
    AcademicProfile myAcademicProfile;
    Button btnUpdate;
    LinearLayout layoutEdit;
    EditText editMobile,editAddress;
    Bitmap newBitmap;
    String strImage;
    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_IMAGE_CROP = 2;
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
        layoutEdit = findViewById(R.id.layoutEdit);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editMobile = findViewById(R.id.editMobile);
        editAddress = findViewById(R.id.editAddress);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEdit.setVisibility(View.GONE);
            }
        });
        profileImage = findViewById(R.id.profileImage);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        txtAddress = findViewById(R.id.txtAddress);
        txtUnivName = findViewById(R.id.txtUnivName);
        txtCollage = findViewById(R.id.txtCollage);
        txtStream = findViewById(R.id.txtStream);
        txtStudentID = findViewById(R.id.txtStudentID);
        editProfileImage = findViewById(R.id.editProfileImage);
        btnEditPersonalProfile = findViewById(R.id.btnEditPersonalProfile);
        btnEditPersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEdit.setVisibility(View.VISIBLE);
                //EditProfile();
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
        txtAddress.setText(myProfile.Address);
        txtUnivName.setText(myAcademicProfile.UniversityName);
        txtCollage.setText(myAcademicProfile.CollegeName);
        txtStream.setText(myAcademicProfile.Stream+", "+myAcademicProfile.Semester);
        txtStudentID.setText(myAcademicProfile.UserID);
    }

    private void EditProfile(){
        String Mobile = txtMobile.getText().toString();
        String Address = txtAddress.getText().toString();

        try {
            String url = Constants.Application_URL + "/api/User";

            final String reqBody = "{\"UserName\":\"" + myProfile.UserName + "\", \"Email\":\"" +myProfile.UserLogin  + "\", \"MobileNumber\":\"" + Mobile + "\",\"Address\":\"" +Address + "\"}";
            JSONObject jsRequest = new JSONObject(reqBody);
            //-------------------------------------------------------------------------------------------------
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {


                        Toast.makeText(getApplicationContext(), "Profile Updated Successfully.", Toast.LENGTH_SHORT).show();

                        Profile myProfile = new Profile();
                        myProfile.UserName = response.getString("");
                        myProfile.MobileNumber = response.getString("");
                        myProfile.UserLogin = response.getString("");
                        myProfile.Address = response.getString("");
                       // myProfile.ProfileImage =  response.getString("");

                        Session.AddProfile(getApplicationContext(),myProfile);
                        layoutEdit.setVisibility(View.GONE);
                       // MyProfileActivity.this.finish();


                    } catch (JSONException e) {
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

    }

    public void EditImage(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
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
}
