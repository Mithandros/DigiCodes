package com.maksof.linkapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class mainPage extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {
    EditText mainSearchBox;
    RelativeLayout searchCon;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public Fragment fragment;
    private static final int RC_LOCATION_CONTACTS_PERM = 1;
    private static final String[] LOCATION_AND_CONTACTS =
            {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
    public Class fragmentClass;
    boolean shouldBackPressed = false;
    commonClass commonClass=new commonClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        setContentView(R.layout.activity_phone_book);
        locationAndContactsTask();
            if(PreferenceUtils.getNotificationStatus(mainPage.this)!=null)
            {   if(PreferenceUtils.getNotificationStatus(mainPage.this).equals("true"))
                    NotificationEventReceiver.setupAlarm(mainPage.this);
            }
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog progressDialog;
            @Override
            protected void onPostExecute(Void aVoid) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                super.onPostExecute(aVoid);
            }

            @Override
            protected void onPreExecute() {
                progressDialog=new ProgressDialog(mainPage.this);
                if(PreferenceUtils.get("adapterInitialized",mainPage.this)==null){

                progressDialog.getWindow().setGravity(Gravity.CENTER);
                progressDialog.setMessage("Loading Contacts...");
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                }
                super.onPreExecute();
            }
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    com.maksof.linkapp.commonClass.myContacts(mainPage.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }}.execute();

        phone_home_fragment pf = new phone_home_fragment();
        ImageButton triggerSearchBar = findViewById(R.id.triggerSearchBar);
        ImageButton closeSearchLayout = findViewById(R.id.closeSearchLayout);
        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(PreferenceUtils.get("tab",mainPage.this)!=null) {
                    if (PreferenceUtils.get("tab", mainPage.this).equals("0"))
                    {
                        pf.search( mainPage.this,charSequence.toString());
                    }
                    else if (PreferenceUtils.get("tab", mainPage.this).equals("1"))
                    {
                        lab_home_fragment.search(mainPage.this, charSequence.toString());
                    }
                    else if (PreferenceUtils.get("tab", mainPage.this).contains("2")) {
                        {
                            groups_home_fragment.search(charSequence.toString(), mainPage.this);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // check Fields For Empty Values
            }
        };
        mainSearchBox = (EditText)findViewById(R.id.mainSearchBox);
        mainSearchBox.addTextChangedListener(mTextWatcher);
        searchCon = findViewById(R.id.searchCon);
        PreferenceUtils.save("pause","directorySearch",mainPage.this);
        if(PreferenceUtils.get("tab",mainPage.this)!=null) {
            if (PreferenceUtils.get("tab", mainPage.this).equals("3"))
                triggerSearchBar.setVisibility(View.GONE);
        }
       /* if(PreferenceUtils.getToken(mainPage.this)!=null && !"".equals(PreferenceUtils.getToken(mainPage.this))) {
            commonClass.apiIntegration(mainPage.this, "group/getgroupsofuser", "", PreferenceUtils.getToken(mainPage.this), "getGroupOfUser");
        }*/
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("occupation","");
            int distance = 10*Constants.distance;
            jsonObject.put("distance", distance);
//            commonClass.apiIntegration(mainPage.this, "profile/searchdirectoryprofile", jsonObject.toString(), PreferenceUtils.getToken(mainPage.this), "directorySearch");

        }catch (JSONException je){}
        fragmentClass = lab_home_fragment.class;
        try {

            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frageContent, fragment).commit();


            drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            navigationView = (NavigationView) findViewById(R.id.navigationView);

            ImageButton openDrawerMenu = (ImageButton) findViewById(R.id.openDrawerMenu);

            openDrawerMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    commonClass.apiIntegration(mainPage.this, "notification/getusernotification", "",PreferenceUtils.getToken(mainPage.this), "getNotification");
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(GravityCompat.START);
                    else drawerLayout.closeDrawer(GravityCompat.END);

                }
            });

            setDrawerContent(navigationView);
            View headerView = (View) navigationView.getHeaderView(0);
            LinearLayout SideheaderLinearView = (LinearLayout) headerView.findViewById(R.id.SideheaderLinearView);
            TextView userName=(TextView)SideheaderLinearView.findViewById(R.id.userProfileName);
            TextView textView12=(TextView)SideheaderLinearView.findViewById(R.id.textView12);

            if (PreferenceUtils.getName(this) != null && !"".equals(PreferenceUtils.getName(this) != null)){
                String name= PreferenceUtils.getName(this);
                String reg="[a-zA-Z0-9]";
                if(((name.charAt(0))+"").matches(reg))
                    textView12.setText(Character.toUpperCase(name.charAt(0))+"");
                else textView12.setText(Constants.firstChar);
                userName.setText(Character.toUpperCase(name.charAt(0))+name.substring(1,name.length()));
            }


        } catch (Exception e){
            Toast.makeText(mainPage.this,e.toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        triggerSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout mainNavHeader = findViewById(R.id.mainNavHeader);
                LinearLayout tabButtonCon = findViewById(R.id.tabButtonCon);
                mainNavHeader.setVisibility(View.GONE);
//                tabButtonCon.animate().translationY(-120);
                ViewPager homeFragment = findViewById(R.id.homeFragment);
                tabButtonCon.setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) homeFragment.getLayoutParams();
                lp.topMargin = 0;
                mainSearchBox.setText("");
                mainSearchBox.clearFocus();
                searchCon = findViewById(R.id.searchCon);
                searchCon.setVisibility(View.GONE);
                mainSearchBox.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mainSearchBox, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        closeSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removerSearchView();
            }
        });

    }
    public void selectedItemDrawer(MenuItem menuItem){
        fragment = null;
        switch (menuItem.getItemId()){
            case R.id.home:
                try{
                    if(fragmentClass==phone_home_fragment.class)
                        shouldBackPressed = true;
                    else shouldBackPressed = false;
                fragmentClass = phone_home_fragment.class;
                }
                catch (Exception e){
                    Toast.makeText(mainPage.this,"home.class"+ e.toString(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.setting:
                try{
                    if(fragmentClass==setting_fragment.class)
                        shouldBackPressed = true;
                    else shouldBackPressed = false;
                    fragmentClass = setting_fragment.class;}
                catch (Exception e){
                    Toast.makeText(mainPage.this,"setting_fragment.class"+ e.toString(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.notifcation:
                if(fragmentClass==notification.class)
                    shouldBackPressed = true;
                else shouldBackPressed = false;
//                commonClass.apiIntegration(mainPage.this, "notification/getusernotification", "",PreferenceUtils.getToken(mainPage.this), "getNotification");
                fragmentClass = notification.class;
                break;
            default:
                shouldBackPressed = false;
                fragmentClass = phone_home_fragment.class;
        }

        if(shouldBackPressed==false){
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frageContent,fragment).commit();
                menuItem.setChecked(true);
                setTitle(menuItem.getTitle());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        drawerLayout.closeDrawers();
    }

    private void setDrawerContent(final NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectedItemDrawer(menuItem);
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(searchCon.getVisibility()==View.VISIBLE){
            removerSearchView();}
        else
        if(fragmentClass==notification.class||fragmentClass==setting_fragment.class){
            try {
                fragmentClass = phone_home_fragment.class;
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frageContent,fragment).commit();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        else if(fragmentClass == phone_home_fragment.class){
            onBackPressed();
        }
}
public void removerSearchView(){
    if(PreferenceUtils.get("tab",mainPage.this)!=null) {
        if (PreferenceUtils.get("tab", mainPage.this).equals("0"))
        {
            phone_home_fragment pf = new phone_home_fragment();
            pf.search( mainPage.this,"");
        }
        else if (PreferenceUtils.get("tab", mainPage.this).equals("1"))
        {
            lab_home_fragment.search(mainPage.this, "");
        }
        else if (PreferenceUtils.get("tab", mainPage.this).contains("2")) {
            {
                groups_home_fragment.search("", mainPage.this);
            }
        }
    }
    searchCon = findViewById(R.id.searchCon);
    if(searchCon!=null)
        if(searchCon.getVisibility()==View.VISIBLE){
    LinearLayout mainNavHeader = findViewById(R.id.mainNavHeader);
    LinearLayout tabButtonCon = findViewById(R.id.tabButtonCon);
    mainNavHeader.setVisibility(View.VISIBLE);
//                tabButtonCon.animate().translationY(0);
    ViewPager homeFragment = findViewById(R.id.homeFragment);
    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) homeFragment.getLayoutParams();
    lp.topMargin += 128;
    tabButtonCon.setVisibility(View.VISIBLE);
    searchCon.setVisibility(View.GONE);
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(mainSearchBox.getWindowToken(), 0);
    if(mainSearchBox!=null){
     //   mainSearchBox.setQuery("", false);
    mainSearchBox.clearFocus();}}
}
    @Override
    public void onActivityResult( int requestCode,  int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //commonClass.imageAPI(requestCode , resultCode, data, mainPage.this,"main_page");

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],

                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                commonClass.selectImage(mainPage.this,"images");
            }
        }
        if(requestCode == 1){
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gpsTracker gpsTracker  = new gpsTracker(mainPage.this);
                    Constants.longitude = gpsTracker.getLongitude();
                    Constants.latitude = gpsTracker.getLatitude();
                }
        }
        if (requestCode == 125) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            GroupExpandableListAdapter.dialCallGroup(mainPage.this);
        }
        else{
            ExpandableListAdapter.callTask(mainPage.this);
            new AppSettingsDialog.Builder(mainPage.this).build().show();
        }}
        if (requestCode == 126) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                phone_home_fragment.dialCall(mainPage.this);
            }
            else{
                new AppSettingsDialog.Builder(mainPage.this).build().show();
            }}
        if(requestCode == 1234){
            commonClass.permissionPermanentlyDenied(mainPage.this,grantResults,"images");
        }
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }
    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationAndContactsTask() {
        if (hasLocationAndContactsPermissions()) {

        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs access to your location to know where you are.", RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }
    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS);
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
        else{
         /*   locationAndContactsTask();*/
            Intent mainIntent = new Intent(mainPage.this,mainPage.class);
            startActivity(mainIntent);

        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }
}
