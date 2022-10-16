package com.maksof.linkapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class directory_profile extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {
    static ArrayList<String> img;
    public static TouchImageView expandedImageView;
    public static boolean newImg = false;
    static ExpandedHeightGridView dirImageGridView;
    static String profileURL,id,profileType,services,isEditable, directoryImages,contactName, phoneNum, occupation, email, addedBy, olDescription, address, webs, latittude, longitude;
    public static TextView updateImage,dirProfMainDesc, dirProfName, labProfOccupTxt, numberTxt,emailTxt, websiteTxt, location, dir_oneLineDesc,viewMore, viewLess ;
    ImageButton dir_profAddImage,lab_profOccupTxtEdit, emailTxtEdit, websiteTxtEdit, LocationEdit,  dir_oneLineDescEdit, dirProfMainDescEdit;
    ArrayList<JSONObject> listOfAddedBy;
    public static ImageView imageView11,dirProfFLetter;
    static ImageAdapter imageAdapter;
    static ArrayList<String> images;
    ArrayList<JSONObject> t5;
    private static final String[] CALL_PERMISSION =
            {Manifest.permission.CALL_PHONE};
    static Context cxt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.cxt = directory_profile.this;
        super.onCreate(savedInstanceState);
        newImg = false;
        setContentView(R.layout.directory_profile);
        PreferenceUtils.saveGroupJSON(null, "imgURLSlist", directory_profile.this);
        Constants.imgsURL.removeAll(Constants.imgsURL);
        lab_profOccupTxtEdit = (ImageButton)findViewById(R.id.lab_profOccupTxtEdit);
        emailTxtEdit = (ImageButton)findViewById(R.id.emailTxtEdit);
        websiteTxtEdit = (ImageButton)findViewById(R.id.websiteTxtEdit);
        LocationEdit = (ImageButton)findViewById(R.id.LocationEdit);
        dir_oneLineDescEdit  = (ImageButton)findViewById(R.id.dir_oneLineDescEdit);
        dirProfMainDescEdit  = (ImageButton)findViewById(R.id.dirProfMainDescEdit);
        imageView11 = (ImageView)findViewById(R.id.imageView11);
        ArrayList<JSONObject>  profile = PreferenceUtils.getGroupContactsJSON("labProfile",directory_profile.this);
        try{
            if(profile!=null) {

                contactName = profile.get(0).getString("contactName");
                phoneNum = profile.get(0).getString("phoneNumber");
                email = profile.get(0).getString("email");
                occupation = profile.get(0).getString("occupation");
                addedBy = profile.get(0).getString("addedBy");
                olDescription = profile.get(0).getString("description");
                address = profile.get(0).getString("address");
                webs = profile.get(0).getString("website");
                latittude = profile.get(0).getString("latitude");
                longitude = profile.get(0).getString("longitude");
                directoryImages = profile.get(0).getString("directoryImages");
                isEditable = profile.get(0).getString("isEditable");
                profileType = profile.get(0).getString("directoryProfile");
                id = profile.get(0).getString("id");
                services = profile.get(0).getString("services");
                images = new ArrayList<>();
                try {
                    String[] p = profile.get(0).getString("directoryImages").split("\\[|\\]| |,|\"");
                    for(int i = 0;i<p.length;i++){
                        if(p[i]!=null&&!(p[i].equals("")))
                            images.add(p[i]);
                    }
                    img = images;
                    updateImage("init");
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            } }
        catch (JSONException e) {
            e.printStackTrace();
        }
        dirProfFLetter = (ImageView)findViewById(R.id.dir_profFLetter);
        dirProfName = (TextView)findViewById(R.id.dir_profName);
        labProfOccupTxt = (TextView)findViewById(R.id.lab_profOccupTxt);
        numberTxt = (TextView)findViewById(R.id.numberTxt);
        emailTxt = (TextView)findViewById(R.id.emailTxt);
        websiteTxt = (TextView)findViewById(R.id.websiteTxt);
        location = (TextView)findViewById(R.id.Location);
        dir_oneLineDesc = (TextView) findViewById(R.id.dir_oneLineDesc);
        viewMore = (TextView) findViewById(R.id.viewMore);
        viewLess = (TextView) findViewById(R.id.viewLess);
        dirProfMainDesc = (TextView) findViewById((R.id.dirProfMainDesc));
        dir_profAddImage = (ImageButton)findViewById(R.id.dir_profAddImage);
        updateImage = (TextView) findViewById(R.id.updateImage);

        ImageButton reviewCallBtn = (ImageButton)findViewById(R.id.reviewCallBtn);
        ImageButton reviewSmsBtn = (ImageButton)findViewById(R.id.reviewSmsBtn);
        ImageButton reviewWhatsappBtn = (ImageButton)findViewById(R.id.reviewWhatsappBtn);
        ImageButton deleteProfile = (ImageButton)findViewById(R.id.dir_profDelete);
        ImageButton download = (ImageButton)findViewById(R.id.dir_profDownloadBtn);
        try {
            uploadProfileImage(directory_profile.this,dirProfFLetter,profile.get(0).getString("profileImages"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lab_profOccupTxtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAttribute("Occupation","Enter Occupation",occupation);
            }
        });
        emailTxtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAttribute("Email","Enter Email",email);
            }
        });
        LocationEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAttribute("Address","Enter Address",address);
            }
        });
        websiteTxtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAttribute("Website","Enter Website",webs);
            }
        });
        dir_oneLineDescEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAttribute("Description","Enter One Line Description",olDescription);
            }
        });
        dirProfMainDescEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAttribute("Services","Enter Services",services);
            }
        });
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage("update");
            }
        });

        if("true".equals(isEditable)){
            lab_profOccupTxtEdit.setVisibility(View.VISIBLE);
            emailTxtEdit.setVisibility(View.VISIBLE);
            websiteTxtEdit.setVisibility(View.VISIBLE);
            LocationEdit.setVisibility(View.VISIBLE);
            dir_oneLineDescEdit.setVisibility(View.VISIBLE);
            dirProfMainDescEdit.setVisibility(View.VISIBLE);
            deleteProfile.setVisibility(View.VISIBLE);
            updateImage.setVisibility(View.VISIBLE);
            download.setVisibility(View.GONE);
        }
        else{
            dir_profAddImage.setVisibility(View.GONE);
            lab_profOccupTxtEdit.setVisibility(View.GONE);
            emailTxtEdit.setVisibility(View.GONE);
            websiteTxtEdit.setVisibility(View.GONE);
            LocationEdit.setVisibility(View.GONE);
            dir_oneLineDescEdit.setVisibility(View.GONE);
            dirProfMainDescEdit.setVisibility(View.GONE);
            deleteProfile.setVisibility(View.GONE);
            updateImage.setVisibility(View.GONE);
            download.setVisibility(View.VISIBLE);
            if(olDescription.equals("")){
                dir_oneLineDesc.setVisibility(View.GONE);
                dir_oneLineDescEdit.setVisibility(View.GONE);
            }
            if(services!=null)
            if (services.equals("")){
                dirProfMainDesc.setVisibility(View.GONE);
                dirProfMainDescEdit.setVisibility(View.GONE);
            }
            if(address.equals("")) {
                location.setVisibility(View.GONE);
                LocationEdit.setVisibility(View.GONE);
                imageView11.setVisibility(View.GONE);
            }
        }
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.downloadContact(directory_profile.this,contactName,phoneNum,email,occupation);
            }
        });
        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder sBuilder = new AlertDialog.Builder(directory_profile.this);
                View sView = getLayoutInflater().inflate(R.layout.close_app_permission, null);
                TextView confirmationText = (TextView) sView.findViewById(R.id.confirmationText);
                confirmationText.setText("Are you sure? You want to delete this profile?");
                final TextView ok = (TextView) sView.findViewById(R.id.closeOK);
                final TextView cancel = (TextView) sView.findViewById(R.id.closeCancel);
                sBuilder.setView(sView);
                final AlertDialog dialog;dialog = sBuilder.create();
                dialog.dismiss();
                if(!((Activity) directory_profile.this).isFinishing())
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
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("profileType",profileType);
                            jsonObject.put("contactId",id);
//                            commonClass.apiIntegration(directory_profile.this, "profile/deleteprofile", jsonObject.toString(), PreferenceUtils.getToken(directory_profile.this),"deleteDirectory");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                  }
        });
        reviewCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(directory_profile.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    commonClass.call(phoneNum, directory_profile.this);
                } else {

                    callTask(directory_profile.this);
                }

            }
        });

        reviewSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.sms(directory_profile.this, phoneNum);
            }
        });

        reviewWhatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.openWhatsApp(directory_profile.this, phoneNum);
            }
        });

        dirProfName.setText(contactName);
        labProfOccupTxt.setText(occupation);
        numberTxt.setText(phoneNum);

        emailTxt.setText(Html.fromHtml(email));
        String text1 = "<a href="+webs+"> "+webs+" </a>";
        websiteTxt.setText(Html.fromHtml(text1));
        location.setText(address);
        dir_oneLineDesc.setText(olDescription);
        dirProfMainDesc.setText(services);

        emailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.email(email,directory_profile.this);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.location(address,directory_profile.this);
            }
        });

        websiteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.website(webs,directory_profile.this);
            }
        });

        dirImageGridView = (ExpandedHeightGridView)findViewById(R.id.dirProfImgCon);
        dirImageGridView.setExpanded(true);
        if(images!=null) {
            imageAdapter = new ImageAdapter(images, directory_profile.this);
            dirImageGridView.setAdapter(imageAdapter);
        }
        ImageButton dir_profBackBtn = (ImageButton)findViewById(R.id.dir_profBackBtn);
        dir_profBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        dir_profAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(directory_profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(directory_profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(directory_profile.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    commonClass.selectImage(directory_profile.this,"images");

                    // Do the file write
                } else {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(directory_profile.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(expandedImageView!=null)
        {if(expandedImageView.getVisibility()==View.VISIBLE)
                expandedImageView.setVisibility(View.GONE);
        else super.onBackPressed();
        }
        else
        super.onBackPressed();
    }

    public void viewMoreMethod(View view) {
        dirProfMainDesc.setEms(0);
        dirProfMainDesc.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, (float) 0.5));
        dirProfMainDesc.setSingleLine(false);
        viewMore.setVisibility(View.GONE);
        viewLess.setVisibility(View.VISIBLE);
    }

    public void viewLessMethod(View view) {
        dirProfMainDesc.setSingleLine(true);
        dirProfMainDesc.setSingleLine(true);
        dirProfMainDesc.setEms(30);
        dirProfMainDesc.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 0.5));
        viewLess.setVisibility(View.GONE);
        viewMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        new AppSettingsDialog.Builder(directory_profile.this).build().show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 124) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        commonClass.call(phoneNum,directory_profile.this);
            }
            else{
                new AppSettingsDialog.Builder(directory_profile.this).build().show();
            }}
        if(requestCode == 1234){
            commonClass.permissionPermanentlyDenied(directory_profile.this,grantResults,"images");
        }
        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                commonClass.selectImage(directory_profile.this,"images");
            }
        }
    }
    @Override
    public void onActivityResult( int requestCode,  int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // commonClass.imageAPI(requestCode , resultCode, data, directory_profile.this,"directory_profile");

    }
    static boolean hasCallPermissions(Activity activity) {
        return EasyPermissions.hasPermissions(activity, CALL_PERMISSION);
    }

    @AfterPermissionGranted(124)
    public static void callTask(Activity activity) {
        if (hasCallPermissions(activity)) {
            commonClass.call(phoneNum,activity);
        } else {

            EasyPermissions.requestPermissions(
                    activity,
                    "This app needs access to your location and contacts to know where and who you are.", 124, CALL_PERMISSION);
        }
    }
    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {


    }

    class TagAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.directory_profile_tags, null);

            TextView dirProftag = (TextView)convertView.findViewById(R.id.dirProftag);

            return convertView;
        }
    }

   static class ImageAdapter extends BaseAdapter{
        ArrayList<String> images;
        Activity activity;
       ImageAdapter(ArrayList<String> review,Activity activity){

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
            String imgUrl =commonClass.imgUrl;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                        circularProgressDrawable.setStrokeWidth(5f);
                        circularProgressDrawable.setCenterRadius(30f);
                        circularProgressDrawable.start();
                        GlideApp.with(activity)
                                    .load(imgUrl+url)
                                    .placeholder(circularProgressDrawable)
                                    .into(imgViewDirec);
                            imgViewDirec.setVisibility(View.VISIBLE);
                    }
                });
            if("true".equals(isEditable)){
                closeBtn.setVisibility(View.VISIBLE);
            }
            else closeBtn.setVisibility(View.GONE);

            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(img!=null){
                        if(img.remove(url)){
                            imgViewDirec.setVisibility(View.GONE);
                            closeBtn.setVisibility(View.GONE);
                            newImg = true;
                            commonClass.updateConstantsImageUrl(img);
                        }
                       }
                }
            });
            imgViewDirec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomImageFromThumb1(imgViewDirec,url,activity);
                }
            });
            return convertView;
        }

}

    public static void zoomImageFromThumb1(final View thumbView, String imageResId, Activity activity) {
        expandedImageView = (TouchImageView) activity.findViewById(R.id.expanded_image2);
        commonClass.zoomImageFromThumb2(thumbView,imageResId,expandedImageView,activity);

}

    public void editAttribute(String whatEdit,String heading1,String oldValue){
        AlertDialog.Builder sBuilder = new AlertDialog.Builder(directory_profile.this);
        View updateInfo = getLayoutInflater().inflate(R.layout.edit_modal, null);
        TextView heading = (TextView) updateInfo.findViewById(R.id.textView22);
        heading.setText(heading1);
        EditText newValue = (EditText) updateInfo.findViewById(R.id.newValue);
        if((!"Website".equals(whatEdit))&&(!"Email".equals(whatEdit)))
        newValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        Button update = (Button) updateInfo.findViewById(R.id.save);
        Button close = (Button) updateInfo.findViewById(R.id.cancel);
        newValue.setHint(whatEdit);
        newValue.setText(oldValue);
        sBuilder.setView(updateInfo);
        final AlertDialog dialog = sBuilder.create();
        if(!((Activity) directory_profile.this).isFinishing())
        {
            dialog.show();
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        JSONObject drInfo = null;
        ArrayList<JSONObject>  profile = PreferenceUtils.getGroupContactsJSON("labProfile",directory_profile.this);
        try{
            if(profile!=null) {
                contactName = profile.get(0).getString("contactName");
                phoneNum = profile.get(0).getString("phoneNumber");
                email = profile.get(0).getString("email");
                occupation = profile.get(0).getString("occupation");
                addedBy = profile.get(0).getString("addedBy");
                olDescription = profile.get(0).getString("description");
                address = profile.get(0).getString("address");
                webs = profile.get(0).getString("website");
                latittude = profile.get(0).getString("latitude");
                longitude = profile.get(0).getString("longitude");
                directoryImages = profile.get(0).getString("directoryImages");
                isEditable = profile.get(0).getString("isEditable");
                profileType = profile.get(0).getString("directoryProfile");
                services = profile.get(0).getString("services");
                id = profile.get(0).getString("id");
                images = new ArrayList<>();
                try {
                    String[] p = profile.get(0).getString("directoryImages").split("\\[|\\]| |,|\"");
                    for(int i = 0;i<p.length;i++){
                        if(p[i]!=null&&!(p[i].equals("")))
                            images.add(p[i]);
                        Constants.imgsURL.add(p[i]);
                    }
                    img = images;
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            } }
        catch (JSONException e) {
            e.printStackTrace();
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

                String groupName = newValue.getText().toString(), data = "";
                boolean flag = true;
                if (groupName == "") {
                    newValue.setError("Required field");
                    flag = false;
                }
                if (flag == true) {
                    try {
                        if("Occupation".equals(whatEdit)){
                            occupation = newValue.getText().toString();
                        }
                        else  if("Email".equals(whatEdit)){
                            email =  newValue.getText().toString();
                        }
                        else  if("Address".equals(whatEdit)){
                            address =  newValue.getText().toString();
                        }
                        else  if("Website".equals(whatEdit))
                        {
                            webs =  newValue.getText().toString();
                        }
                        else  if("Services".equals(whatEdit)){
                            services =  newValue.getText().toString();
                        }
                        else  if("Description".equals(whatEdit)){
                            olDescription =  newValue.getText().toString();
                        }

                        JSONObject newVal = new JSONObject();
                        newVal.put("profileType",profileType);
                        newVal.put("contactId",id);
                        newVal.put("contactName",contactName);
                        newVal.put("occupation",occupation);
                        newVal.put("email",email);
                        newVal.put("description",olDescription);
                        newVal.put("address",address);
                        newVal.put("website",webs);
                        newVal.put("about",services);
                        newVal.put("services",services);
                        newVal.put("directoryImages",images);
//                        commonClass.apiIntegration(directory_profile.this, "profile/editprofile", newVal.toString(), PreferenceUtils.getToken(directory_profile.this), "updateDirectoryProfile");
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void updateImage(String when){
        try{

            JSONObject newVal = new JSONObject();
            newVal.put("profileType",profileType);
            newVal.put("contactId",id);
            newVal.put("contactName",contactName);
            newVal.put("occupation",occupation);
            newVal.put("email",email);
            newVal.put("description",olDescription);
            newVal.put("address",address);
            newVal.put("website",webs);
            newVal.put("about",services);
            newVal.put("services",services);
            newVal.put("directoryImages",img);
         /*   if(when.equals("init"))
                commonClass.apiIntegration(directory_profile.this, "profile/editprofile", newVal.toString(), PreferenceUtils.getToken(directory_profile.this), "initDirectoryProfile");
            else
                commonClass.apiIntegration(directory_profile.this, "profile/editprofile", newVal.toString(), PreferenceUtils.getToken(directory_profile.this), "updateDirectoryProfile");
      */  } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static int inc=0;
    public static void images(final Activity activity) throws JSONException {
        newImg = true;
        final ArrayList<JSONObject> imgs = PreferenceUtils.getGroupNamesJSON("imgURLSlist",activity);
        img = new ArrayList<>();

        int size = imgs.size();
        if (images!=null){
            for(int k=0;k<images.size();k++){
                try {

                    if(!images.get(k).equals(""))
                    {
                        JSONObject job = new JSONObject();
                        job.put(""+(size++)+"",images.get(k));
                        imgs.add(job);}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}
        for(int i = 0;i<imgs.size();i++){
            img.add(imgs.get(i).getString("" + i + ""));
        }
        PreferenceUtils.saveGroupJSON(imgs,"imgURLSlist",activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap resized;
                if(dirImageGridView!=null)
                {
                    for( inc = 0 ;inc<imgs.size();inc++){
                        ArrayList<String> imgurl = new ArrayList<>();
                        for( inc = 0 ;inc<imgs.size();inc++) {
                            imgurl.add(com.maksof.linkapp.commonClass.urlBitmap(activity, inc));
                        }
                        dirImageGridView.setExpanded(true);
                        ImageAdapter imageAdapter = new ImageAdapter(imgurl,activity);
                        dirImageGridView.setAdapter(imageAdapter);

                    }
                }
            }
        });

    }
    public static void updatedProfile(Activity activity, ArrayList<JSONObject>  profile){
        try{
            if(profile!=null) {
                contactName = profile.get(0).getString("contactName");
                phoneNum = profile.get(0).getString("phoneNumber");
                email = profile.get(0).getString("email");
                occupation = profile.get(0).getString("occupation");
                addedBy = profile.get(0).getString("addedBy");
                olDescription = profile.get(0).getString("description");
                address = profile.get(0).getString("address");
                webs = profile.get(0).getString("website");
                latittude = profile.get(0).getString("latitude");
                longitude = profile.get(0).getString("longitude");
                directoryImages = profile.get(0).getString("directoryImages");
                profileType = profile.get(0).getString("directoryProfile");
                id = profile.get(0).getString("id");
                services = profile.get(0).getString("services");
                profileURL = profile.get(0).getString("profileImages");
                images = new ArrayList<>();
                try {
                    String[] p = profile.get(0).getString("directoryImages").split("\\[|\\]| |,|\"");
                    for(int i = 0;i<p.length;i++){
                        if(p[i]!=null&&!(p[i].equals("")))
                            images.add(p[i]);
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            } }
        catch (JSONException e) {
            e.printStackTrace();
        }
        activity.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                if((dirProfFLetter!=null)&&(dirProfName!=null)&&(labProfOccupTxt!=null)&&(numberTxt!=null)&&(emailTxt!=null)&&(websiteTxt!=null)
                        &&(location!=null)
                        &&(dir_oneLineDesc!=null)&&(dirProfMainDesc!=null)&&(dirImageGridView!=null)&&(dirProfFLetter!=null))

                dirProfName.setText(contactName);
                labProfOccupTxt.setText(occupation);
                numberTxt.setText(phoneNum);
                uploadProfileImage(activity,dirProfFLetter,profileURL);

                emailTxt.setText(Html.fromHtml(email));
                String text1 = "<a href="+webs+"> "+webs+" </a>";
                websiteTxt.setText(Html.fromHtml(text1));
                location.setText(address);
                dir_oneLineDesc.setText(olDescription);
                dirProfMainDesc.setText(services);
                imageAdapter = new ImageAdapter(images,activity);
                dirImageGridView.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void uploadProfileImage(Activity activity, ImageView imageView, String url){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(url!=null&&(!url.equals(""))){
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Picasso.with(activity).load("http://lynk-app.com/api/cdn/images/" + url).placeholder(circularProgressDrawable).into(imageView);
                }
                }
                else  imageView.setImageDrawable(activity.getDrawable(R.drawable.ic_profile));
            }
        });


    }
}
