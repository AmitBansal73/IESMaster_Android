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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.example.iesmaster.Common.Constants;
import com.example.iesmaster.Common.Session;
import com.example.iesmaster.model.AcademicProfile;
import com.example.iesmaster.model.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AcademicProfileActivity extends AppCompatActivity   {
    ProgressBar progressBar;
    Button btnSave;
    EditText txtUniversity,txtCollage,txtStream;
    ListView listViewUniversity,listViewCollage,listViewStream;
    Spinner spinnerSemester;
    TextView attachedID;
    Bitmap newBitmap;
    ImageView collageID;
    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_IMAGE_CROP = 2;
    int selectedUniversity,selectedCollage,selectedStream,selectedSemester;
    List<String> universityList= new ArrayList<>();
    HashMap<String,Integer> universityHashMap = new HashMap<>();

    List<String> collegeList= new ArrayList<>();
    HashMap<String,Integer> collegeHashMap = new HashMap<>();

    List<String> streamList= new ArrayList<>();
    HashMap<String,Integer> streamHashMap = new HashMap<>();

    List<String> semesterList= new ArrayList<>();
    HashMap<String,Integer> semesterHashMap = new HashMap<>();

    ArrayList<college> arraylistcollage = new ArrayList<>();

    ArrayAdapter<String> adapterUniversity;
    ArrayAdapter<String> adapterCollege;
    ArrayAdapter<String> adapterStream;
    ArrayAdapter<String> adapterSemester;
    boolean IsResult = false;
    AcademicProfile profile;
    Profile userProfile;

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
        userProfile = Session.GetProfile(getApplicationContext());
        Intent intent = getIntent();
        IsResult = intent.getBooleanExtra("IsResult", false);
       // setUniversityData();
      //  setCollegeData();
      //  setStreamData();
      //  setSemesterData();
        progressBar = findViewById(R.id.progressBar);
        attachedID = findViewById(R.id.attachedID);
        txtUniversity = findViewById(R.id.txtUniversity);
        txtStream = findViewById(R.id.txtStream);
        txtCollage = findViewById(R.id.txtCollage);
        listViewUniversity = findViewById(R.id.listViewUniversity);
        listViewCollage = findViewById(R.id.listViewCollage);
        listViewStream = findViewById(R.id.listViewStream);
        spinnerSemester = findViewById(R.id.spinnerSemester);
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
                    profile = new AcademicProfile();
                    profile.UniversityName = txtUniversity.getText().toString();
                    profile.UniversityID = selectedUniversity;
                    profile.CollegeName = txtCollage.getText().toString();
                    profile.CollegeID = selectedCollage;
                    profile.Stream = txtStream.getText().toString();
                    profile.StreamID =  selectedStream;
                    profile.Semester = spinnerSemester.getSelectedItem().toString();
                    profile.SemesterID =selectedSemester;

                    UpdateAcademicProfile();

                   /* Session.AddAcademicProfile(getApplicationContext(), profile);
                    Intent i = new Intent(AcademicProfileActivity.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    AcademicProfileActivity.this.finish(); */
                }
                else {

                    AcademicProfile profile = new AcademicProfile();
                    profile.UniversityName = txtUniversity.getText().toString();
                    profile.Stream = txtStream.getText().toString();
                    profile.Semester = spinnerSemester.getSelectedItem().toString();
                    Intent intent = new Intent();
                    intent.putExtra("IsProfile", true);
                    intent.putExtra("Profile", profile);
                    setResult(100, intent);
                    finish();//finishing activity
                }
            }
        });

        setUniversitySpinner();

    }

    public void setUniversitySpinner()
    {
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/University/All";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        int x = response.length();
                        for (int i = 0; i <x; i++) {

                            JSONObject jObj = response.getJSONObject(i);

                            String UniversityName = jObj.getString("UniversityName");
                            int UniversityID = jObj.getInt("UniversityID");
                            universityHashMap.put(UniversityName, UniversityID);
                            universityList.add(UniversityName);
                        }

                        adapterUniversity.notifyDataSetChanged();

                    } catch (JSONException e) {
                        int a=1;
                    }
                    catch (Exception ex)
                    {
                        int a=1;
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);

                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
            progressBar.setVisibility(View.GONE);
        }



        adapterUniversity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, universityList);
        adapterUniversity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewUniversity.setAdapter(adapterUniversity);
        txtUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewUniversity.setVisibility(View.VISIBLE);

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
                selectedUniversity = universityHashMap.get(University);
                txtUniversity.setText(University);
                setCollegeSpinner(selectedUniversity);
                listViewUniversity.setVisibility(View.GONE);
            }
        });
    }

    private void setCollegeSpinner(int id )
    {
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/College/University/" + id;
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        int x = response.length();
                        collegeHashMap.clear();
                        adapterCollege.clear();
                        for (int i = 0; i <x; i++) {

                            JSONObject jObj = response.getJSONObject(i);
                            String ClgName = jObj.getString("CollegeName");
                            int ClgID = jObj.getInt("CollegeID");
                            collegeHashMap.put(ClgName, ClgID);
                            collegeList.add(ClgName);
                        }

                        adapterCollege.notifyDataSetChanged();

                    } catch (JSONException e) {
                        int a=1;
                    }
                    catch (Exception ex)
                    {
                        int a=1;
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
            progressBar.setVisibility(View.GONE);
        }

        adapterCollege = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, collegeList);
        adapterCollege.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewCollage.setAdapter(adapterCollege);

                txtCollage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            listViewCollage.setVisibility(View.VISIBLE);
                        }else {
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
                String College = listViewCollage.getItemAtPosition(position).toString();
                txtCollage.setText(College);
                selectedCollage = collegeHashMap.get(College);
                setStreamSpinner(selectedCollage);
                listViewCollage.setVisibility(View.GONE);
            }
        });
    }

    private void setStreamSpinner(int clgId)
    {
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/Stream/College/"+ clgId;
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        int x = response.length();
                        collegeHashMap.clear();
                        adapterCollege.clear();
                        for (int i = 0; i <x; i++) {
                            JSONObject jObj = response.getJSONObject(i);
                            String Stream = jObj.getString("StreamName");
                            int StreamID = jObj.getInt("StreamID");
                            streamHashMap.put(Stream, StreamID);
                            streamList.add(Stream);
                        }
                        adapterStream.notifyDataSetChanged();
                    } catch (JSONException e) {
                        int a=1;
                    }
                    catch (Exception ex)
                    {
                        int a=1;
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);

                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
            progressBar.setVisibility(View.GONE);
        }

        adapterStream = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, streamList);
        adapterStream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listViewStream.setAdapter(adapterStream);
        txtStream.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    listViewStream.setVisibility(View.VISIBLE);
                }else {
                    listViewStream.setVisibility(View.GONE);
                }
            }
        });

        txtStream.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                    AcademicProfileActivity.this.adapterStream.getFilter().filter(cs);
                    listViewStream.setVisibility(View.VISIBLE);
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
                selectedStream = streamHashMap.get(stream);
                setSemesterSpinner();
                listViewStream.setVisibility(View.GONE);
            }
        });

    }

    private void setSemesterSpinner()
    {
        progressBar.setVisibility(View.VISIBLE);
        String url = Constants.Application_URL+ "/api/Semester/All";
        try{
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);
                    try {
                        int x = response.length();
                        for (int i = 0; i <x; i++) {
                            JSONObject jObj = response.getJSONObject(i);
                            String SemesterName = jObj.getString("SemesterName");
                            int SemesterID = jObj.getInt("SemID");
                            semesterHashMap.put(SemesterName, SemesterID);
                            semesterList.add(SemesterName);
                        }
                        adapterSemester.notifyDataSetChanged();
                    } catch (JSONException e) {
                        int a=1;
                    }
                    catch (Exception ex)
                    {
                        int a=1;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);
            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);
        }catch (Exception ex){
            int a=1;
            progressBar.setVisibility(View.GONE);
        }

        adapterSemester = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, semesterList);
        adapterSemester.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(adapterSemester);

        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Semester = spinnerSemester.getSelectedItem().toString();
                selectedSemester= semesterHashMap.get(Semester);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  spinnerSemester.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Semester = spinnerSemester.getSelectedItem().toString();
                selectedSemester= semesterHashMap.get(Semester);
            }
        }); */

    }


    private void UpdateAcademicProfile() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            String url = Constants.Application_URL + "/api/User/AddAcademic";

            final String reqBody = "{\"UniversityID\":\"" + selectedUniversity + "\", \"CollegeID\":\"" +selectedCollage  + "\", \"StreamID\":\""
                    + selectedStream + "\",\"SemesterID\":\"" +selectedSemester + "\",\"UserID\":\"" + userProfile.UserID + "\"}";
            JSONObject jsRequest = new JSONObject(reqBody);
            //-------------------------------------------------------------------------------------------------
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsRequest, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.setVisibility(View.GONE);
                    try {

                        if(response.getString("Response").matches("Ok")) {
                            Toast.makeText(getApplicationContext(), "Profile Updated Successfully.", Toast.LENGTH_SHORT).show();

                            Session.AddAcademicProfile(getApplicationContext(), profile);
                            Intent intent = new Intent(AcademicProfileActivity.this, HomeActivity.class);
                            startActivity(intent);
                            AcademicProfileActivity.this.finish();

                        }else if(response.getString("Response").matches("Fail")) {

                            Toast.makeText(getApplicationContext(), "Failed... ", Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        int a = 1;
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = error.toString();

                    progressBar.setVisibility(View.GONE);
                }
            });
            RetryPolicy rPolicy = new DefaultRetryPolicy(0, -1, 0);

            jsArrayRequest.setRetryPolicy(rPolicy);
            queue.add(jsArrayRequest);

            //*******************************************************************************************************
        } catch (JSONException js) {
            Toast.makeText(getApplicationContext(), "Could not Update Academic profile,Contact Admin", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    public void valid(){
        String University = txtUniversity.getText().toString();
        String Collage = txtCollage.getText().toString();
        String Stream = txtStream.getText().toString();
        if (University.equals("")) {
            txtUniversity.setError("select University");
        }else if (Collage.equals("")) {
            txtCollage.setError("Select Collage");
        }else if (Stream.equals("")) {
            txtStream.setError(" Select Stream");
        }
    }



 /*   private void setUniversityData()
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
    } */


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

            AcademicProfileActivity.this.finish();
        }
        else {

            AcademicProfile profile = new AcademicProfile();
            Intent intent = new Intent();
            intent.putExtra("IsProfile", false);
            intent.putExtra("Profile", profile);
            setResult(100, intent);
            finish();//finishing activity




        }
    }
}
