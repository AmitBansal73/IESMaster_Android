package com.example.iesmaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.AcademicProfile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AcademicProfileActivity extends AppCompatActivity  {
    Button btnSave;
    EditText txtUniversity,txtCollage,txtStream;
    ListView listViewUniversity,listViewCollage,listViewStream;
    Spinner spinnerSemester;
    TextView attachedID;
    Bitmap newBitmap;
    ImageView collageID;
    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_IMAGE_CROP = 2;

    ArrayAdapter<String> adapterUniversity;
   // ArrayAdapter<String> adapterClg;
    List<String> universityList= new ArrayList<>();
    HashMap<String,Integer> universityHashMap = new HashMap<>();

    List<String> collegeList= new ArrayList<>();
    HashMap<String,Integer> collegeHashMap = new HashMap<>();

    List<String> streamList= new ArrayList<>();
    HashMap<String,Integer> streamHashMap = new HashMap<>();

    List<String> semesterList= new ArrayList<>();
    HashMap<String,Integer> semesterHashMap = new HashMap<>();

    ArrayList<college> arraylistcollage = new ArrayList<>();
    ArrayAdapter<String> adapterCollege,adapterStream, adapterSemester;

    boolean IsResult = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_academic);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Academic Profile");
        actionBar.show();
        closeKeyboard();

       Intent intent = getIntent();
        IsResult = intent.getBooleanExtra("IsResult", false);
        setUniversityData();
        setCollegeData();
        setStreamData();
        setSemesterData();

        attachedID = findViewById(R.id.attachedID);
        txtUniversity = findViewById(R.id.txtUniversity);
        txtStream = findViewById(R.id.txtStream);
        txtCollage = findViewById(R.id.txtCollage);
        listViewUniversity = findViewById(R.id.listViewUniversity);
        listViewCollage = findViewById(R.id.listViewCollage);
        listViewStream = findViewById(R.id.listViewStream);
        btnSave = findViewById(R.id.btnSave);

        if(IsResult)
        {
            attachedID.setVisibility(View.GONE);
        }

        attachedID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtUniversity.getText().toString()== null ||
                   txtStream.getText().toString() == null ||
                   spinnerSemester.getSelectedItem().toString()== null)
                {
                    Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_LONG);
                    return;
                }

                if(!IsResult) {
                    AcademicProfile profile = new AcademicProfile();
                    profile.UniversityName = txtUniversity.getText().toString();
                    profile.UniversityID = 99;
                    profile.CollegeName = txtCollage.getText().toString();
                    profile.CollegeID = 99;
                    profile.Stream = txtStream.getText().toString();
                    profile.StreamID = 99;
                    profile.Semester = spinnerSemester.getSelectedItem().toString();
                    profile.SemesterID =99;
                    Session.AddAcademicProfile(getApplicationContext(), profile);
                    Intent i = new Intent(AcademicProfileActivity.this, HomeActivity.class);
                    startActivity(i);
                    AcademicProfileActivity.this.finish();
                }
                else {

                    AcademicProfile profile = new AcademicProfile();
                    profile.UniversityName = txtUniversity.getText().toString();
                    profile.Stream = txtStream.getText().toString();
                    profile.Semester = spinnerSemester.getSelectedItem().toString();
                    Intent intent = new Intent();
                    intent.putExtra("Profile", profile);
                    setResult(100, intent);
                    finish();//finishing activity
                }

            }
        });

        setUniversitySpinner();
        setCollegeSpinner();
        setStreamSpinner();

        spinnerSemester = findViewById(R.id.spinnerSemester);
        adapterSemester = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, semesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(adapterSemester);


    }



    private void setUniversitySpinner()
    {
        adapterUniversity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, universityList);
        adapterUniversity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewUniversity.setAdapter(adapterUniversity);

        txtUniversity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                closeKeyboard();

                if(hasFocus)
                {
                    listViewUniversity.setVisibility(View.VISIBLE);
                }
                else
                {
                    listViewUniversity.setVisibility(View.GONE);
                }

            }
        });

        txtUniversity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                adapterUniversity.getFilter().filter(cs);
                listViewUniversity.setVisibility(View.VISIBLE);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listViewUniversity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String University = (String) listViewUniversity.getItemAtPosition(position);
                txtUniversity.setText(University);
                listViewUniversity.setVisibility(View.GONE);
            }
        });


    }

    private void setCollegeSpinner()
    {
        adapterCollege = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, collegeList);
        adapterCollege.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewCollage.setAdapter(adapterCollege);

        txtCollage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                closeKeyboard();
                if(hasFocus)
                {
                    listViewCollage.setVisibility(View.VISIBLE);
                }
                else
                {
                    listViewCollage.setVisibility(View.GONE);
                }
            }
        });

        txtCollage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                AcademicProfileActivity.this.adapterCollege.getFilter().filter(cs);
                listViewCollage.setVisibility(View.VISIBLE);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listViewCollage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String University = listViewCollage.getItemAtPosition(position).toString();
                txtCollage.setText(University);
                listViewCollage.setVisibility(View.GONE);
            }
        });
    }


    private void setStreamSpinner()
    {
        adapterStream = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, streamList);
        adapterStream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewStream.setAdapter(adapterStream);

        txtStream.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                {
                    listViewStream.setVisibility(View.VISIBLE);
                }
                else
                {
                    listViewStream.setVisibility(View.GONE);
                }
            }
        });

        txtStream.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(cs.length()<2)
                {
                    listViewStream.setVisibility(View.GONE);
                }
                else {
                    AcademicProfileActivity.this.adapterStream.getFilter().filter(cs);
                    listViewStream.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listViewStream.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stream = listViewStream.getItemAtPosition(position).toString();
                txtStream.setText(stream);
                listViewStream.setVisibility(View.GONE);
            }
        });

    }



    private void setUniversityData()
    {
        universityList.add("UPTU");
        universityHashMap.put("UPTU",1);
        universityList.add("Punjab University");
        universityHashMap.put("Punjab University",2);
        universityList.add("Amity University");
        universityHashMap.put("Amity University",3);
        universityList.add("CCS University");
        universityHashMap.put("CCS University",4);

    }


    private void setCollegeData()
    {
        collegeList.add("ABES College");
        collegeHashMap.put("ABES College",1);
        collegeList.add("KTIS College");
        collegeHashMap.put("KTIS College",2);
        collegeList.add("ITS College");
        collegeHashMap.put("ITS College",3);
        collegeList.add("AK Garg College");
        collegeHashMap.put("AK Garg College",4);
    }


    private void setStreamData()
    {
        streamList.add("Civil");
        streamHashMap.put("Civil",1);
        streamList.add("Mechanical");
        streamHashMap.put("Mechanical",2);
        streamList.add("Electrical");
        streamHashMap.put("Electrical",3);
        streamList.add("Computer Science");
        streamHashMap.put("Computer Science",4);
    }


    private void setSemesterData()
    {
        semesterList.add("1st Year (Sem I)");
        semesterHashMap.put("1st Year (Sem I)",1);
        semesterList.add("1st Year (Sem II)");
        semesterHashMap.put("1st Year (Sem II)",2);
        semesterList.add("2nd Year (Sem III)");
        semesterHashMap.put("2nd Year (Sem III)",3);
        semesterList.add("2nd Year (Sem IV)");
        semesterHashMap.put("2nd Year (Sem IV)",4);

        semesterList.add("3rd Year (Sem V)");
        semesterHashMap.put("3rd Year (Sem V)",5);
        semesterList.add("3rd Year (Sem VI)");
        semesterHashMap.put("3rd Year (Sem VI)",6);
        semesterList.add("Final Year (Sem VII)");
        semesterHashMap.put("Final Year (Sem VII)",7);
        semesterList.add("Final Year (Sem VIII)");
        semesterHashMap.put("Final Year (Sem VIII)",8);

    }


    private class ViewHolder
    {
        TextView txtCollage;
    }

    class MyAdapterClg extends ArrayAdapter<college>{
        LayoutInflater inflat;
        ViewHolder holder;
        public MyAdapterClg(Context context, int resource, int textViewResourceId, ArrayList<college> objects) {

            super(context, resource, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            inflat= LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arraylistcollage.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    convertView = inflat.inflate(R.layout.row_item_collages, null);
                    holder = new ViewHolder();
                    holder.txtCollage = convertView.findViewById(R.id.txtCollage);
                    convertView.setTag(holder);
                }
                college row = getItem(position);
                // Log.d("Dish Name", row.complaint_type);
                holder.txtCollage.setText(row.CollegeName);

                return convertView;
            }

            catch (Exception ex)

            {
                Toast.makeText(getApplicationContext(),"Could not Load Forum Data", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        public int getPosition(college item) {
            return super.getPosition(item);
        }

        @Override
        public college getItem(int position) {
            return arraylistcollage .get(position);
        }
    }

    public class college{
        String CollegeName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_IMAGE_GET) {

                if (data != null) {
                    Uri uri = data.getData();
                    InputStream image_stream = getContentResolver().openInputStream(uri);
                   // byte[] imgByte= ImageServer.getBytes(image_stream);
                  //  ImageServer.SaveFileToExternal(imgByte,"crop.jpg",getApplicationContext());
                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File myDir = new File(root + "/SCM/crop.jpg");
                    myDir.mkdirs();
                    Uri contentUri = Uri.fromFile(myDir);
                    ImageCropFunction(contentUri);
                }
            } else if (requestCode == REQUEST_IMAGE_CROP) {

                if (data != null) {

                    Bundle bundle = data.getExtras();
                    if(bundle!= null) {
                        newBitmap = bundle.getParcelable("data");
                        collageID.setImageBitmap(newBitmap);
                    }
                    else
                    {
                        Uri cropUri =  data.getData();
                        InputStream image_stream = getContentResolver().openInputStream(cropUri);
                        newBitmap= BitmapFactory.decodeStream(image_stream);
                        collageID.setImageBitmap(newBitmap);
                    }
                   // strImage = ImageServer.getStringFromBitmap(newBitmap);
                    collageID.invalidate();
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



    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }


    @Override
    public void onBackPressed() {
        if(!IsResult) {

            Intent i = new Intent(AcademicProfileActivity.this, RegisterActivity.class);
            startActivity(i);
            AcademicProfileActivity.this.finish();
        }
        else {

            AcademicProfile profile = null;
            Intent intent = new Intent();
            intent.putExtra("Profile", profile);
            setResult(100, intent);
            finish();//finishing activity
        }
    }
}
