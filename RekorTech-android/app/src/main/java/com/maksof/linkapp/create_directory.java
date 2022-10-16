package com.maksof.linkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.api.Places;
import static com.google.android.libraries.places.api.model.Place.Field.LAT_LNG;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.android.volley.VolleyLog.TAG;

public class create_directory extends AppCompatActivity {

    static ArrayList<String> dirImg;
    public static boolean newImg = false;
    static ExpandedHeightGridView recImgCon;
    static ArrayList<String> imgURL = new ArrayList<>();
    EditText newDirecEmail, addressSearched,newDirecWebsite,newDirecPhone,newDirecFName ,newDirecOccupation,newDirecOne_LineDescription;
    MultiAutoCompleteTextView newDirecServices;
    String nameReg="[a-zA-Z][a-z A-Z.\\d]{1,20}$";
    String emailReg="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    String desc, website, address, phone, fName,lName, occupation, oneLineDesc, Services, email,about;
    static AutocompleteSupportFragment autocompleteFragment;
    public  static  ImageView newDirecPorfileImage;
    String address1 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        setContentView(R.layout.create_directory);
        PreferenceUtils.save("","directoryProfileImage",create_directory.this);
        Button submit = (Button) findViewById(R.id.shareNewDirecProfile);
        Button uploadImage = (Button) findViewById(R.id.newDirecuploadImage);
        newDirecPorfileImage = (ImageView) findViewById(R.id.newDirecPorfileImage);

        newDirecWebsite = (EditText)  findViewById(R.id.newDirecWebsite);
        newDirecPhone = (EditText)   findViewById(R.id.newDirecPhone);
        newDirecFName = (EditText)  findViewById(R.id.newDirecFName);
        newDirecOccupation = (EditText) findViewById(R.id.newDirecOccupation);
        newDirecOne_LineDescription = (EditText) findViewById(R.id.newDirecOne_LineDescription);
        newDirecServices = (MultiAutoCompleteTextView) findViewById(R.id.newDirecServices);
        newDirecEmail = (EditText) findViewById(R.id.newDirecEmail);
        recImgCon = (ExpandedHeightGridView)findViewById(R.id.recImgCon);
        imgURL = null;
        newDirecFName.setText(PreferenceUtils.getfName(this)+" "+PreferenceUtils.getlName(this));
        newDirecPhone.setText(PreferenceUtils.getNumber(this));
        newDirecEmail.setText(PreferenceUtils.getEmail(this));

        newDirecFName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        newDirecOccupation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        newDirecOne_LineDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        newDirecServices.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        focusChange(newDirecFName);
        focusChange(newDirecWebsite);
        focusChange(newDirecOccupation);
        focusChange(newDirecOne_LineDescription);
        focusChange(newDirecServices);
        submit.setEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Places.initialize(create_directory.this, "AIzaSyCYlFhfg9LErOaVm_JivuovEcyh2CpuIN0");
        PlacesClient placesClient = Places.createClient(create_directory.this);
        autocompleteFragment = (AutocompleteSupportFragment)getSupportFragmentManager ().findFragmentById(R.id.location_autocomplete_fragment6);

        if (autocompleteFragment != null) {
            EditText etPlace = (EditText) autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
            if (etPlace != null)
            {

                etPlace.setText("");
              //  etPlace.setBackgroundColor(getResources().getColor(R.color.defaultBorderFlagColor));
                etPlace.setBackgroundColor(Color.parseColor("#ededed"));
                etPlace.setHintTextColor(Color.parseColor("#959595"));
                etPlace.setPadding(5,0,0,0);
                etPlace.setTextSize(18);
            }
            if((View)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button)!=null)
                autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();
            autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME));
            autocompleteFragment.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {

                @Override
                public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                    autocompleteFragment.setText(place.getName());
                    address1 = place.getName();
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
               /*     if(autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button)!=null)
                        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button).setBackgroundColor(getResources().getColor(R.color.defaultBorderFlagColor));
             */
                    if(autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button)!=null)
                        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button).setBackgroundColor(Color.parseColor("#ededed"));

                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }

            });

        }
        autocompleteFragment.setHint("Address");

        newDirecPorfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(create_directory.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(create_directory.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(create_directory.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    commonClass.selectImage(create_directory.this,"profileImage");
                } else {
                    ActivityCompat.requestPermissions(create_directory.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
                }
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(create_directory.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(create_directory.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(create_directory.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    commonClass.selectImage(create_directory.this,"images");
                    // Do the file write
                } else {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(create_directory.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
                }

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                desc  = newDirecOne_LineDescription.getText().toString();
                website = newDirecWebsite.getText().toString();
                phone = newDirecPhone.getText().toString();
                fName = newDirecFName.getText().toString();
                occupation = newDirecOccupation.getText().toString();
                Services = newDirecServices.getText().toString();
                email = newDirecEmail.getText().toString();

                boolean flag = true;

                if(!fName.matches(nameReg)) {
                    flag = false;
                    newDirecFName.setError("Invalid Name");
                }

                String emailReg="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

                if(email!=null&&(!email.equals(""))){
                    if(!email.matches(emailReg)) {
                        flag=false;
                        newDirecEmail.setError("Invalid email address");
                    }
                }

                if("".equals(phone)) {
                    flag = false;
                    newDirecPhone.setError("Enter Phone");
                }

                if("".equals(oneLineDesc)){
                    flag = false;
                    newDirecOne_LineDescription.setError("Required Field");
                }
                if("".equals(Services)) {
                    flag = false;
                    newDirecServices.setError("Enter Services");
                }

                if(flag == true){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("contactName",fName);
                        jsonObject.put("phoneNumber",phone);
                        jsonObject.put("email",email);
                        jsonObject.put("latitude",Constants.latitude);
                        jsonObject.put("longitude",Constants.longitude);
                        jsonObject.put("occupation",occupation);
                        jsonObject.put("description",desc);
                        jsonObject.put("address",address1);
                        jsonObject.put("website",website);
                        jsonObject.put("about",desc);
                        jsonObject.put("services",Services);
                        jsonObject.put("directoryProfile","DIRECTORY");
                        if(PreferenceUtils.get("directoryProfileImage",create_directory.this)!=null){
                            jsonObject.put("profileImage",PreferenceUtils.get("directoryProfileImage",create_directory.this));
                        }
                        jsonObject.put("directoryImages",dirImg);
//                        commonClass.apiIntegration(create_directory.this, "contact/createcontact", jsonObject.toString(), PreferenceUtils.getToken(create_directory.this), "createDirectory");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ArrayAdapter<String> servicesAdapter = new ArrayAdapter<String>(create_directory.this,
                android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.occupation));
        MultiAutoCompleteTextView direcServices = (MultiAutoCompleteTextView) findViewById(R.id.newDirecServices);
        direcServices.setAdapter(servicesAdapter);
        direcServices.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    static int inc;
    public static void images(final Activity activity){
        final ArrayList<JSONObject> imgs = PreferenceUtils.getGroupNamesJSON("imgURLSlist",activity);
        dirImg = new ArrayList<>();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newImg = true;
                if(recImgCon!=null) {
                    for(int i = 0;i<imgs.size();i++){
                        try {
                            dirImg.add(imgs.get(i).getString("" + i + ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayList<String> imgurl = new ArrayList<>();
                    for( inc = 0 ;inc<imgs.size();inc++) {
                        imgurl.add(com.maksof.linkapp.commonClass.urlBitmap(activity, inc));
                    }
                    recImgCon.setExpanded(true);
                    recImgCon.setVisibility(View.VISIBLE);
                    ImageAdapter2 imageAdapter = new ImageAdapter2(imgurl,activity);
                    recImgCon.setAdapter(imageAdapter);
                }
            }
        });
    }
    static class ImageAdapter2 extends BaseAdapter {
        ArrayList<String> images;
        Activity activity;
        ImageAdapter2(ArrayList<String> review,Activity activity){

            this.images = review;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return  this.images.size();
        }

        @Override
        public Object getItem(int position) {
            return this.images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = activity.getLayoutInflater().inflate(R.layout.recommneded_images, null);
            final ImageView imgViewDirec = (ImageView) convertView.findViewById(R.id.imgViewDirec);
            final ImageView closeBtn = (ImageView) convertView.findViewById(R.id.btn_close1);

            final String url = (String)this.getItem(position);
            activity.runOnUiThread(new Runnable()                {
                @Override
                public void run() {
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();

                    GlideApp.with(activity)
                            .load(com.maksof.linkapp.commonClass.imgUrl+url)
                            .placeholder(circularProgressDrawable)
                            .into(imgViewDirec);
                    imgViewDirec.setVisibility(View.VISIBLE);
                }
            });
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(dirImg!=null)
                        dirImg.remove(url);
                    commonClass.updateConstantsImageUrl(dirImg);
                    PreferenceUtils.saveGroupString(dirImg,"imgString",activity);
                    imgViewDirec.setVisibility(View.GONE);
                    closeBtn.setVisibility(View.GONE);
                }
            });
            return convertView;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //commonClass.imageAPI(requestCode , resultCode, data, create_directory.this,"create_directory");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1234){
            commonClass.permissionPermanentlyDenied(create_directory.this,grantResults,"images");
        }
        if(requestCode == 1){
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gpsTracker gpsTracker  = new gpsTracker(create_directory.this);
                Constants.longitude = gpsTracker.getLongitude();
                Constants.latitude = gpsTracker.getLatitude();
                if(Constants.longitude==0)
                    Toast.makeText(create_directory.this,"Couldn't finf location",Toast.LENGTH_LONG);
            }

        }
    }

    static void setImgURL(Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imgURL = PreferenceUtils.getGroupString("imgString",activity);
            }
        });
    }
    public  void focusChange(EditText et){
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    public  static void uploadProfileImage(Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Constants.imgsURL.removeAll(Constants.imgsURL);
                if(newDirecPorfileImage!=null){
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();
                    if(PreferenceUtils.get("directoryProfileImage",activity)!=null){

                        GlideApp.with(activity)
                            .load("http://lynk-app.com/api/cdn/images/"+PreferenceUtils.get("directoryProfileImage",activity))
                            .placeholder(circularProgressDrawable)
                            .into(newDirecPorfileImage);
                    newDirecPorfileImage.setVisibility(View.VISIBLE);}
                }
            }
        });
    }
}
