package com.rekortech.android;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class start_screen extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks{
    public static boolean flag=false;
    private static final String[] LOCATION_AND_CONTACTS =
            { Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    public static start_screen instance;
    public start_screen() {
        instance = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_users_input_layout_i);
        setupUI(findViewById(R.id.main_parent));
        EditText welcomeName = findViewById(R.id.welcomeName);
        welcomeName.clearFocus();
        welcomeName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        locationAndContactsTask();
        Button welcomeIIContBtn = (Button)findViewById(R.id.welcomeIIContBtn);
        welcomeIIContBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                flag = true;
                EditText welcomeNumber = findViewById(R.id.welcomeNumber);
                EditText welcomeEmail = findViewById(R.id.welcomePassword);
                String numReg="[0-9]+";
                if (welcomeNumber.getText().toString().length() < 11||(!welcomeNumber.getText().toString().matches(numReg)) ) {
                    welcomeNumber.setError("Invalid number");
                    flag = false;
                }
                if (welcomeNumber.getText().toString().length() > 12) {
                    welcomeNumber.setError("Invalid number");
                    flag = false;
                }

                if (welcomeEmail.getText().length() < 8) {
                    welcomeEmail.setError("Invalid Email");
                    flag = false;
                }
                String emailReg="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
                if(!welcomeEmail.getText().toString().matches(emailReg)) {
                    flag=false;
                    welcomeEmail.setError("Invalid email address");
                }

                String data = null;
                if (flag == true) {
                    {
                        Intent resetIntent = new Intent(start_screen.this, mainPage.class);
                        startActivity(resetIntent);
                    }
                }
            }
        });
       // if(PreferenceUtils.getToken(this)!=null&&PreferenceUtils.getToken(this)!=""){

        //}
        }

    public void goNextWelcomePage(View view) {
        /*Variables Declaration*/
//        boolean flag = true;
        EditText welcomeName = findViewById(R.id.welcomeName);
        String nameReg="[a-zA-Z][a-z A-Z]{1,20}$";
//        if(!welcomeName.getText().toString().matches(nameReg)) {
//            flag = false;
//            welcomeName.setError("Invalid Name");
//        }
        if(welcomeName.getText().toString().matches(nameReg)){
            RelativeLayout firstPartCon = findViewById(R.id.firstPartCon);
            RelativeLayout SecondPartCon = findViewById(R.id.SecondPartCon);
            SecondPartCon.setTranslationX(800);

            Animation toLeft = new TranslateAnimation(0,-800,0,0);
            toLeft.setDuration(500);

            ObjectAnimator backToLeft = ObjectAnimator.ofFloat(SecondPartCon, "translationX", 0);
            backToLeft.setDuration(500);
            backToLeft.start();
            Log.d("Degub","Value of Translate : " + Float.toString(SecondPartCon.getTranslationX()));
            firstPartCon.startAnimation(toLeft);
//            welcomeName.setAnimation(toLeft);
            final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    firstPartCon.setVisibility(View.GONE);
                }
            }, 495);
        }else{
            welcomeName.setError("Invalid Name");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d("Tag", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d("Denied", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        Intent mainIntent = new Intent(start_screen.this,start_screen.class);
        start_screen.this.startActivity(mainIntent);

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,"Contact And Location permissions:"+(hasLocationAndContactsPermissions() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();
            this.finish();
        }
    }
    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d("rational accepted", "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d("rational denied", "onRationaleDenied:" + requestCode);
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationAndContactsTask() {
        if (hasLocationAndContactsPermissions()) {
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs access to your location and contacts to know where and who you are.", RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText) && !(view instanceof ImageButton)&& !(view instanceof Button)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(start_screen.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
  /*  @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }*/
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }
}
