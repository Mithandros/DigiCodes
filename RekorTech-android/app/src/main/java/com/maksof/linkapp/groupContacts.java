package com.maksof.linkapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AppSettingsDialog;

import static com.android.volley.VolleyLog.TAG;

public class groupContacts extends AppCompatActivity  {
    public static ArrayList<String> colorList = new ArrayList<>();
    public static String groupID;
    public Class fragmentClass;
    EditText bsnsName, bsnsOccup, bsnsFeed,bsnsNum, bsnsWeb, bsnsEmail;
    LinearLayout addContacts,mainView,selectOptions;
    static ExpandedHeightGridView bsnsImageGridView ;
    AutocompleteSupportFragment autocompleteFragment;
    Button uploadImage,phoneBook,contactForm;
    TextView ratingText;
    RatingBar bsnsRatingBar;
    String address = "";
    static AlertDialog dialog;
    boolean selectOp = false;
    View bView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Constants.groupDelete = 0;
        setContentView(R.layout.activity_group_contacts);
        groupID = getIntent().getStringExtra("groupId");
        ImageButton closeSearchLayout = findViewById(R.id.closeSearchLayout);
        phoneBook = findViewById(R.id.phoneBook);
        contactForm = findViewById(R.id.contactForm);
        addContacts  = findViewById(R.id.addContacts);
        mainView= findViewById(R.id.mainView);
        selectOptions = findViewById(R.id.selectOptions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton triggerSearchBar = findViewById(R.id.triggerSearchBar);
        addContacts.setVisibility(View.GONE);
        mainView.setVisibility(View.VISIBLE);
        triggerSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout mainNavHeader = findViewById(R.id.mainNavHeader);
                RelativeLayout searchCon = (RelativeLayout)findViewById(R.id.searchCon);
                EditText mainSearchBox = (EditText)findViewById(R.id.mainSearchBox);
                mainSearchBox.setText("");
                searchCon.setVisibility(View.VISIBLE);
                mainNavHeader.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
            }
        });
        closeSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_contacts_fragment.closeSearch(groupContacts.this);
            }
        });
        TextView groupName = (TextView)findViewById(R.id.groupName);
        groupName.setText(PreferenceUtils.getGroupName(groupContacts.this));
        Button addContactToGroup = (Button)findViewById(R.id.addContactToGroup);
        addContactToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOp=!selectOp;
                if(selectOp)
                selectOptions.setVisibility(View.VISIBLE);
                else selectOptions.setVisibility(View.GONE);
                addContactOption(groupContacts.this);
            }
        });
        fragmentClass = group_contacts_fragment.class;
        Fragment fragment = null;
        try {
            fragment = (Fragment)fragmentClass.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frageContent, fragment).commit();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
    public static void closeModal(Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // commonClass.imageAPI(requestCode , resultCode, data, groupContacts.this,"groupContacts");

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        //request code 0 for camera and gallery permission

        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                commonClass.selectImage(groupContacts.this,"images");
            }
        }

        if (requestCode == 124) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GroupExpandableListAdapter.dialCallGroup(groupContacts.this);
            }
            else{

                GroupExpandableListAdapter.callTask(groupContacts.this);
                new AppSettingsDialog.Builder(groupContacts.this).build().show();
            }
        }}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        List<JSONObject> listGroupContacts = PreferenceUtils.getGroupContactsJSON("GroupMembers",groupContacts.this);
        getMenuInflater().inflate(R.menu.group_menu, menu);

        if (PreferenceUtils.get("createdBy",groupContacts.this)!=null) {
            if (PreferenceUtils.get("createdBy", groupContacts.this).equals(PreferenceUtils.getKeyId(groupContacts.this))) {
                menu.findItem(R.id.delete).setVisible(true);
                menu.findItem(R.id.edit).setVisible(true);
                menu.findItem(R.id.members).setVisible(true);

            } else {

                menu.findItem(R.id.delete).setVisible(false);
                menu.findItem(R.id.edit).setVisible(false);
                menu.findItem(R.id.members).setVisible(false);
                for (int j = 0; j < listGroupContacts.size(); j++) {
                    try {
                        Pattern pattern = Pattern.compile("^(?:0)\\d+$");
                        Matcher matcher = pattern.matcher(listGroupContacts.get(j).getString("phoneNumber"));
                        String num = listGroupContacts.get(j).getString("phoneNumber");
                        String num2 = listGroupContacts.get(j).getString("phoneNumber");
                        String myNum = PreferenceUtils.getNumber(groupContacts.this);

                        if(matcher.matches()) {
                            TelephonyManager telephonyMngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                            String contryId = telephonyMngr.getSimCountryIso().toUpperCase();
                            String[] arrContryCode= getResources().getStringArray(R.array.DialingCountryCode);
                            for(int i=0; i<arrContryCode.length; i++){
                                String[] arrDial = arrContryCode[i].split(",");
                                if(arrDial[1].trim().equals(contryId.trim())){
                                    num = listGroupContacts.get(j).getString("phoneNumber").replaceFirst("0", arrDial[0]);
                                    num2 = listGroupContacts.get(j).getString("phoneNumber");
                                    break;
                                }
                            }

                        }

                        if (num.equals(myNum)||num2.equals(myNum)) {
                            menu.findItem(R.id.delete).setVisible(false);
                            menu.findItem(R.id.edit).setVisible(false);
                            menu.findItem(R.id.members).setVisible(true);
                            break;
                        } else {
                            menu.findItem(R.id.delete).setVisible(false);
                            menu.findItem(R.id.edit).setVisible(false);
                            menu.findItem(R.id.members).setVisible(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                final commonClass commonClass = new commonClass();
                    String obj = PreferenceUtils.getGroupName(groupContacts.this);
                    String name = Character.toUpperCase(obj.charAt(0)) + obj.substring(1, obj.length());
                    AlertDialog.Builder sBuilder = new AlertDialog.Builder(groupContacts.this);
                    View createGroupView = getLayoutInflater().inflate(R.layout.create_group_modal, null);
                    TextView heading = (TextView) createGroupView.findViewById(R.id.textView22);
                    heading.setText("Update Group Name");
                    final EditText group_name = (EditText) createGroupView.findViewById(R.id.group_name);
                    Button close = (Button) createGroupView.findViewById(R.id.closeGroupModal);
                    Button update = (Button) createGroupView.findViewById(R.id.createGroup);
                    group_name.setText(name);
                    update.setText("Update");
                    sBuilder.setView(createGroupView);
                    final AlertDialog dialog = sBuilder.create();
                if(!((Activity) groupContacts.this).isFinishing())
                {
                    dialog.show();
                    dialog.getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    //show dialog
                }
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String groupName = group_name.getText().toString(), data = "";
                            boolean flag = true;
                            if (groupName == "") {
                                group_name.setError("Group Name cannot be Empty");
                                flag = false;
                            }
                            if (flag == true) {
                                try {
                                    data = URLEncoder.encode("groupId", "UTF-8")
                                            + "=" + URLEncoder.encode(groupID + "", "UTF-8");
                                    data += "&" + URLEncoder.encode("newName", "UTF-8") + "="
                                            + URLEncoder.encode(groupName, "UTF-8");

//                                    commonClass.apiIntegration(groupContacts.this, "group/updategroup", data, PreferenceUtils.getToken(groupContacts.this), "updateGroup");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        }
                    });

                return true;
            case R.id.delete:
                deleteGroup(groupID);
                return true;
            case R.id.members:
                fragmentClass = group_member_fragment.class;
                Fragment fragment = null;
                try {
                    fragment = (Fragment)fragmentClass.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frageContent, fragment).commit();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);

    }}
    public void deleteGroup(String gId){
                AlertDialog.Builder sBuilder = new AlertDialog.Builder(groupContacts.this);
                View sView = getLayoutInflater().inflate(R.layout.close_app_permission, null);
                TextView confirmationText = (TextView) sView.findViewById(R.id.confirmationText);
                confirmationText.setText("Are you sure? You want to delete this group?");
                final TextView ok = (TextView) sView.findViewById(R.id.closeOK);
                final TextView cancel = (TextView) sView.findViewById(R.id.closeCancel);
                sBuilder.setView(sView);
                final AlertDialog dialog;dialog = sBuilder.create();
                dialog.dismiss();
                if(!((Activity) groupContacts.this).isFinishing())
                {
                    dialog.show();
                    dialog.getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    //show dialog
                }
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
//                        com.maksof.linkapp.commonClass.apiIntegrationGet(groupContacts.this, "group/deletegroup/" + gId, PreferenceUtils.getToken(groupContacts.this), "deleteGroup");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        RelativeLayout searchCon = (RelativeLayout)findViewById(R.id.searchCon);
        if(searchCon.getVisibility()==View.VISIBLE)
            group_contacts_fragment.closeSearch(groupContacts.this);
        else {
        if(Constants.groupDelete==1)
            super.onBackPressed();
        else {
        if(fragmentClass == group_member_fragment.class){
            fragmentClass = group_contacts_fragment.class;
            Fragment fragment = null;
            try {
                fragment = (Fragment)fragmentClass.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frageContent, fragment).commit();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        else
        super.onBackPressed();}
    }}
    static int inc;
    public static void images(final Activity activity){
        final ArrayList<JSONObject> imgs = PreferenceUtils.getGroupNamesJSON("imgURLSlist",activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap resized;
                if(bsnsImageGridView!=null)
                {
                    //remove images if previously uploaded
                    for( inc = 0 ;inc<imgs.size();inc++){
                        ArrayList<String> imgurl = new ArrayList<>();
                        for( inc = 0 ;inc<imgs.size();inc++) {
                            imgurl.add(com.maksof.linkapp.commonClass.urlBitmap(activity, inc));
                        }

                        bsnsImageGridView.setExpanded(true);
                        bsnsImageGridView.setVisibility(View.VISIBLE);
                        lab_home_fragment.ImageAdapter imageAdapter = new lab_home_fragment.ImageAdapter(imgurl,activity);
                        bsnsImageGridView.setAdapter(imageAdapter);

                    }
                }
            }
        });

    }
    public void addContactOption(Activity activity) {
        phoneBook = findViewById(R.id.phoneBook);
        contactForm = findViewById(R.id.contactForm);
        phoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.setVisibility(View.GONE);
                addContacts.setVisibility(View.VISIBLE);
                addContactView();
            }
        });
        contactForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactForm("","");
            }
        });
    }
    public void contactForm(String contactName,String contactNumber){
        formInit();
        Constants.imgsURL.removeAll(Constants.imgsURL);
        PreferenceUtils.saveGroupString(null,"imgString",groupContacts.this);
        AlertDialog.Builder businessBuilder;
        businessBuilder = new AlertDialog.Builder(groupContacts.this);

        if(bView!=null){
            bsnsName = (EditText) bView.findViewById(R.id.bsnsName);
            bsnsEmail = (EditText) bView.findViewById(R.id.bsnsEmail);
            bsnsWeb = (EditText) bView.findViewById(R.id.bsnsWebsite);
            bsnsNum = (EditText) bView.findViewById(R.id.bsnsPhone);
            bsnsOccup = (EditText) bView.findViewById(R.id.bsnsOccupation);
            bsnsFeed = (EditText) bView.findViewById(R.id.bsnsDesc);
            uploadImage = (Button) bView.findViewById(R.id.uploadImage);
            ratingText = (TextView) bView.findViewById(R.id.ratingText);
            bsnsRatingBar = (RatingBar) bView.findViewById(R.id.bsnsRatingBar);
            bsnsImageGridView = (ExpandedHeightGridView)bView.findViewById(R.id.bsnsRecImgCon);
            bsnsImageGridView.setVisibility(View.GONE);
            Constants.imgsURL.removeAll(Constants.imgsURL);
            bsnsName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            bsnsOccup.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            bsnsFeed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            bsnsRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                }
            });
            LinearLayout hide = (LinearLayout)bView.findViewById(R.id.hideKeyboard);
            hide.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(bView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    return true;
                }
            });
            bsnsName.setText(contactName);
            bsnsEmail.setText("");
            bsnsWeb.setText("");
            bsnsNum.setText(contactNumber);
            bsnsOccup.setText("");
            bsnsFeed.setText("");
            ratingText.setText("");
            bsnsName.setError(null);
            bsnsEmail.setError(null);
            bsnsWeb.setError(null);
            bsnsNum.setError(null);
            bsnsOccup.setError(null);
            bsnsFeed.setError(null);
            LayerDrawable stars = (LayerDrawable) bsnsRatingBar.getProgressDrawable();
            stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.lightGrey), PorterDuff.Mode.SRC_ATOP);

            uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(groupContacts.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(groupContacts.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(groupContacts.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        commonClass.selectImage(groupContacts.this,"images");

                        // Do the file write
                    } else {
                        // Request permission from the user
                        ActivityCompat.requestPermissions(groupContacts.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }

                }
            });

            Button share = (Button) bView.findViewById(R.id.shareBsnsProfile);
            Button cancel = (Button) bView.findViewById(R.id.closeBsnsDialogue);

            businessBuilder.setView(bView);
            dialog = businessBuilder.create();
            if(!isFinishing())
            {
                    dialog.show();
                    dialog.getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
            try {
                dialog.setCanceledOnTouchOutside(true);
            }
            catch (Exception e){
            }
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name, num, email, website, occupation,  bsnsF, rating;
                    name = bsnsName.getText().toString();
                    num = bsnsNum.getText().toString();
                    email = bsnsEmail.getText().toString();
                    website = bsnsWeb.getText().toString();
                    occupation = bsnsOccup.getText().toString();
                    bsnsF = bsnsFeed.getText().toString();
                    rating = bsnsRatingBar.getRating() + "";
                    PreferenceUtils.save(rating, "labContactRating", groupContacts.this);
                    PreferenceUtils.save(bsnsF, "labContactFeedBack", groupContacts.this);
                    boolean flag = true;
                    String nameReg="[a-zA-Z][a-z A-Z.\\d]{1,20}$";
                    String emailReg="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
                    String numReg ="[+|0-9][0-9]{1,20}$";
                    if(!num.matches(numReg)) {
                        flag=false;
                        bsnsNum.setError("Invalid Number");

                    }
                    if(!name.matches(nameReg)) {
                        flag=false;
                        bsnsName.setError("Invalid First Name");

                    }
                    if(email!=null&&(!email.equals(""))){
                        if(!email.matches(emailReg)) {
                            flag=false;
                            bsnsEmail.setError("Invalid email address");
                        }
                    }
                    if ("".equals(bsnsF) || bsnsF == null) {
                        flag = false;
                        bsnsFeed.setError("Required feild");
                    }

                    if(bsnsRatingBar.getRating()==0.0){
                        flag=false;
                        LayerDrawable stars = (LayerDrawable) bsnsRatingBar.getProgressDrawable();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                            stars.getDrawable(0).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                        else stars.getDrawable(0).setColorFilter(Color.parseColor("#ff9999"), PorterDuff.Mode.SRC_ATOP);

                    }
                    if(bsnsRatingBar.getRating()>0.0){
                        LayerDrawable stars = (LayerDrawable) bsnsRatingBar.getProgressDrawable();
                        stars.getDrawable(0).setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                    }

                    if (flag == true) {
                        num = num.replaceAll("[^0-9]", "");

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("contactName", name);
                            jsonObject.put("phoneNumber", num);
                            jsonObject.put("email", email);
                            jsonObject.put("latitude", Constants.latitude + "");
                            jsonObject.put("longitude", Constants.longitude + "");
                            jsonObject.put("occupation", occupation);
                            jsonObject.put("description", bsnsF);
                            jsonObject.put("address", address);
                            jsonObject.put("website", website);
                            jsonObject.put("directoryProfile", "GROUP_CONTACT");
//                            commonClass.apiIntegration(groupContacts.this, "contact/createcontact", jsonObject.toString(), PreferenceUtils.getToken(groupContacts.this), "recommendUserGroup", groupID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }
    }

    public void addContactView(){
        ListView contacts;
        LinearLayout nextButton;
        TextView title;
        ArrayList<JSONObject> contact = new ArrayList<>();
        ArrayList<contactModel> contactModal = new ArrayList<>();
        boolean match = false;
        ArrayList<JSONObject> addedMembers = new ArrayList<>();
        String groupId;
        ArrayList<String> name = new ArrayList();
        ArrayList<String> num = new ArrayList<>();
        ArrayList<String> color =  new ArrayList<>();
         final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);
            groupId = getIntent().getStringExtra("groupId");
            contacts = (ListView)findViewById(R.id.contactList);
            title = (TextView)findViewById(R.id.title);
        title.setText("Share contact");
            nextButton = (LinearLayout)findViewById(R.id.nextButton);
            nextButton.setVisibility(View.GONE);
        if(PreferenceUtils.get("adapterInitialized",groupContacts.this)!=null)
        {
            if(PreferenceUtils.get("adapterInitialized",groupContacts.this).equals("true"))
            {
                if(PreferenceUtils.getGroupString("nameList",groupContacts.this)!=null)
                {
                    name = PreferenceUtils.getGroupString("nameList",groupContacts.this);
                    num = PreferenceUtils.getGroupString("numList",groupContacts.this);
                    color = PreferenceUtils.getGroupString("colorList",groupContacts.this);

                }}}
            for(int i=0;i<name.size();i++){
                try {
                    JSONObject jobject = new JSONObject();
                    jobject.put("name",name.get(i));
                    jobject.put("number",num.get(i));
                    jobject.put("color",color.get(i));
                    contact.add(jobject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for(int i=0;i<contact.size();i++){
                contactModal.add(new contactModel(contact.get(i),false));
            }
            createGroupContactsAdapter createGroupContactsAdapter = new createGroupContactsAdapter(groupContacts.this,contactModal);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contacts.setAdapter(createGroupContactsAdapter);
                }
            });

            ImageButton participantsBack = (ImageButton) findViewById(R.id.participantsBack);
            participantsBack.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mainView.setVisibility(View.VISIBLE);
                    addContacts.setVisibility(View.GONE);
                    return true;
                }
            });
    }
    class createGroupContactsAdapter extends BaseAdapter {

        Activity act;
        ArrayList<contactModel> gContacts;
        ;

        public createGroupContactsAdapter(Activity activity, ArrayList<contactModel> as) {
            this.act = activity;
            this.gContacts = as;
        }

        @Override
        public int getCount() {
            return gContacts.size();
        }

        @Override
        public JSONObject getItem(int position) {
            return this.gContacts.get(position).getContact();

        }
        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_contact_selection, null);

            TextView gFLetter = (TextView) convertView.findViewById(R.id.gFLetter);
            TextView gContactName = (TextView) convertView.findViewById(R.id.gContactName);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkb);
            TextView gContactNum = (TextView) convertView.findViewById(R.id.gContactNum);
            checkBox.setVisibility(View.GONE);
            JSONObject job = getItem(position);
            try {
                gContactName.setText(job.getString("name"));
                gFLetter.setText(job.getString("name").charAt(0) + "");
                gContactNum.setText(job.getString("number"));
                final GradientDrawable gradientDrawable = (GradientDrawable) gFLetter.getBackground().mutate();
                gradientDrawable.setColor(Color.parseColor(job.getString("color")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (this.gContacts.get(position).isSel()) {
                checkBox.setChecked(true);
            } else checkBox.setChecked(false);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactForm(gContactName.getText().toString(),gContactNum.getText().toString().replaceAll("[^0-9]",""));
                }
            });
            return convertView;
        }
    }
    void formInit(){
        try {
            bView = getLayoutInflater().inflate(R.layout.business_recommended, null);
        } catch (InflateException e) {

        }

        if (bView != null) {
            ViewGroup parent = (ViewGroup) bView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }

        if(bView != null)
            bView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        Places.initialize(groupContacts.this, "AIzaSyCYlFhfg9LErOaVm_JivuovEcyh2CpuIN0");
        PlacesClient placesClient = Places.createClient(groupContacts.this);
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager ().findFragmentById(R.id.location_autocomplete_fragment6);
        if (autocompleteFragment != null) {
            EditText etPlace = (EditText) autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
            if (etPlace != null)
            {
                etPlace.setText("");
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
            autocompleteFragment.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, Place.Field.NAME));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

                @Override
                public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                    autocompleteFragment.setText(place.getName());
                    address = place.getName();
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                    if(autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button)!=null)
                        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button).setBackgroundColor(Color.parseColor("#ededed"));

                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
            autocompleteFragment.setHint("Address");
        }
    }
}