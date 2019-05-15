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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.iesmaster.Common.ImageServer;
import com.example.iesmaster.Common.OvalImageView;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;

import java.io.File;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    OvalImageView profileImage;
    ImageView addImage;
    EditText txtMobile,txtAddress,txtPassword,txtName,txtEmail;
    Button btnSubmit;
    Profile myProfile;
    boolean IsLogin = false;
    Bitmap newBitmap;
    String strImage;
    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_IMAGE_CROP = 2;
    static  final int REQUEST_OTP_VERIFY =3;

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

        if(myProfile.UserLogin!= null && !myProfile.UserLogin.matches(""))
        {
            IsLogin = true;
            txtName.setText(myProfile.UserName);
            txtEmail.setText(myProfile.UserLogin);
            txtName.setEnabled(false);
            txtEmail.setEnabled(false);
            Glide.with(this).load(myProfile.ProfileImage).into(profileImage);
        }else {
            IsLogin = false;
        }
            btnSubmit = findViewById(R.id.btnSubmit);
            profileImage = findViewById(R.id.profileImage);
            txtAddress = findViewById(R.id.txtAddress);
            txtEmail = findViewById(R.id.txtEmail);
            txtMobile = findViewById(R.id.txtMobile);
            txtName = findViewById(R.id.txtName);
            txtPassword = findViewById(R.id.txtPassword);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsLogin) {
                    SaveProfile();
                }
                else
                {
                    UserRegistration();
                }
            }
        });

    }

   public void UserRegistration(){

        String Name= txtName.getText().toString();
        String Email= txtEmail.getText().toString();
        String Mobile= txtMobile.getText().toString();
        String Password= txtPassword.getText().toString();
        String Address= txtAddress.getText().toString();

        if(Mobile!=null && Email != null && Password!= null)
        {
            ServerRegister();
        }

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
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == REQUEST_IMAGE_GET) {

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
            } else if (requestCode == REQUEST_IMAGE_CROP) {

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

            else if(requestCode == REQUEST_OTP_VERIFY)
            {
                boolean result = data.getBooleanExtra("isVerified", false);
                if(result) {
                    Profile profile = new Profile();
                    profile.UserName = txtName.getText().toString();
                    profile.UserLogin = txtEmail.getText().toString();
                    profile.MobileNumber = txtMobile.getText().toString();
                    profile.UserPassword = txtPassword.getText().toString();
                    // profile.Address= txtAddress.getText().toString();

                    Session.AddProfile(getApplicationContext(), profile);

                    Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
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
            startActivityForResult(CropIntent, REQUEST_IMAGE_CROP);
        }
        catch (Exception e)
        {
            int a =1;
        }
    }


    public void ServerRegister(){

        Intent intent = new Intent(getApplicationContext(), MobileOTPActivity.class);
       //intent.putExtra("IsResult", false);
        startActivityForResult(intent, REQUEST_OTP_VERIFY);
    }
}
