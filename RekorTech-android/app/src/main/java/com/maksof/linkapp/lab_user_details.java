package com.maksof.linkapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class lab_user_details extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    //reviews
    public static ListView reviewList;
    public static AlertDialog postDialog;
    static ImageView profImage1, profImage2, profImage3, profImage4, profImage5;
    static ExpandedHeightGridView recImgCon;
    static String contactId, contactName, phoneNum, occupation, email, addedBy, description, address, webs, latittude, longitude, isShared;
    private static final String[] CALL_PERMISSION =
            {Manifest.permission.CALL_PHONE};
    public static boolean newImg = false;
    public static ArrayList<JSONObject> listOfAddedBy, recommendedBy;
    static RatingBar avgRatingBar;
    static TextView sendToReview;
    ArrayList<ArrayList<String>> as = new ArrayList<>();
    static ArrayList<String> labimg;
    public static ScrollView lab_user_mainCon;
    Button post;
    RatingBar ratingBar;
    EditText postFeedBack;
    private Boolean profSharedByCrntUser = true;
    public static Button ToggleReshareBtn;
    public ImageButton deleteLAB;
    static float rating = 0;
    static Float avgRating = 8.0f;
    boolean flag = false;
    public static AlertDialog dialog;
    public String screen = "";
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues() {
        EditText feedback = postFeedBack;
        String s1 = feedback.getText().toString();
        Button submit = post;
        if (s1.length() == 0) {
            submit.setEnabled(false);
        } else {
            submit.setEnabled(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_user_details);
        TextView last_person_recommended = (TextView) findViewById(R.id.last_person_recommended);
        ArrayList<JSONObject> profile = PreferenceUtils.getGroupContactsJSON("labProfile", lab_user_details.this);

        contactName = "Dr. Sadaf Amin";
        phoneNum = "0900 78601";
        email = "ayesha.nadeem@healthcare.com";
        contactId = "12345";
        occupation = "Physiotherapist";

        RelativeLayout recommendedBy = (RelativeLayout) findViewById(R.id.recommendedBy);
        recommendedBy.setVisibility(View.GONE);
        description = "Dr. Sadaf Amin - Dermatologist, Lahore - Appointment Details:\n" +
                "\n" +
                "Dr. Sadaf Amin is a qualified Dermatologist in Lahore with over 15 years of experience in the field. With numerous qualifications, the doctor provides the best treatment for all Dermatologist related diseases. Dr. Sadaf Amin has treated over 2190 patients through Marham and has 1491 reviews. You can book an appointment with Dr. Sadaf Amin through Marham.\n" +
                "\n";

        webs = "https://www.marham.pk/doctors/lahore/dermatologist/dr-sadaf-amin";


        reviewList = (ListView) findViewById(R.id.reviewsList);
        ToggleReshareBtn = (Button) findViewById(R.id.lab_profReshareBtn);
        deleteLAB = (ImageButton) findViewById(R.id.deleteLAB);
        lab_user_mainCon = (ScrollView) findViewById(R.id.lab_user_mainCon);

        TextView labProfFLetter = (TextView) findViewById(R.id.lab_profFLetter);
        TextView labProfName = (TextView) findViewById(R.id.lab_profName);
        TextView numberTxt = (TextView) findViewById(R.id.numberTxt);
        TextView emailTxt = (TextView) findViewById(R.id.emailTxt);
        TextView labProfOccup = (TextView) findViewById(R.id.lab_profOccup);
        TextView labProfWeb = (TextView) findViewById(R.id.websiteTxt);
        TextView location = (TextView) findViewById(R.id.Location);
        avgRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        final ImageButton labProfBackBtn = (ImageButton) findViewById(R.id.lab_profBackBtn);
        ImageView webIcon = (ImageView) findViewById(R.id.webIcon);
        ImageView imageView11 = (ImageView) findViewById(R.id.imageView11);
        sendToReview = (TextView) findViewById(R.id.sendToReview);
        LinearLayout reviews = (LinearLayout) findViewById(R.id.reviews);
        ImageButton reviewCallBtn = (ImageButton) findViewById(R.id.reviewCallBtn);
        ImageButton reviewSmsBtn = (ImageButton) findViewById(R.id.reviewSmsBtn);
        ImageButton reviewWhatsappBtn = (ImageButton) findViewById(R.id.reviewWhatsappBtn);
        ImageButton download = (ImageButton) findViewById(R.id.labDownload);
        ImageButton delete = (ImageButton) findViewById(R.id.deleteContact);
        avgRatingBar.setRating(8.0f);
        sendToReview.setText("0.0");
        labProfName.setText(contactName);
        if (contactName != null) {
            String reg = "[a-zA-Z0-9]";
            if ((contactName.charAt(0) + "").matches(reg))
                labProfFLetter.setText(Character.toUpperCase(contactName.charAt(0)) + "");
            else labProfFLetter.setText(Constants.firstChar);
        }

        numberTxt.setText(phoneNum);
        String text = "<a href=" + email + "> " + email + " </a>";
        emailTxt.setText(Html.fromHtml(text));
        emailTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.email(email, lab_user_details.this);
            }
        });

        location.setText(address);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.location(address, lab_user_details.this);
            }
        });


        String text1 = "<a href=" + webs + "> " + webs + " </a>";
        labProfWeb.setText(Html.fromHtml(text1));
        labProfWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.website(webs, lab_user_details.this);
            }
        });

        labProfOccup.setText(occupation);
        labProfWeb.setText(webs);
        if (addedBy != null)
            if (addedBy.equals(PreferenceUtils.getKeyId(lab_user_details.this))) {
                ToggleReshareBtn.setEnabled(false);
                deleteLAB.setVisibility(View.VISIBLE);
            } else deleteLAB.setVisibility(View.GONE);

        reviewCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(lab_user_details.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    commonClass.call(phoneNum, lab_user_details.this);
                } else {

                    callTask(lab_user_details.this);
                }
            }
        });

        reviewSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.sms(lab_user_details.this, phoneNum);
            }
        });

        reviewWhatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.openWhatsApp(lab_user_details.this, phoneNum);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.downloadContact(lab_user_details.this, contactName, phoneNum, email, occupation);
            }
        });

        Intent intent = this.getIntent();
        screen = intent.getStringExtra("screen");
        if (screen != null) {
            if (screen.equals("myProfile")) {
                LinearLayout contactInfo = (LinearLayout) findViewById(R.id.contactInfo);
                contactInfo.setVisibility(View.GONE);
                sendToReview.setVisibility(View.GONE);
                labProfWeb.setVisibility(View.GONE);
                avgRatingBar.setVisibility(View.GONE);
                location.setVisibility(View.GONE);
                imageView11.setVisibility(View.GONE);
                webIcon.setVisibility(View.GONE);
                reviews.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

            }
            if (screen.equals("GroupProfile")) {
                ToggleReshareBtn.setVisibility(View.GONE);
                download.setVisibility(View.VISIBLE);
                sendToReview.setVisibility(View.VISIBLE);
                labProfWeb.setVisibility(View.GONE);
                avgRatingBar.setVisibility(View.VISIBLE);
                location.setVisibility(View.GONE);
                imageView11.setVisibility(View.GONE);
                webIcon.setVisibility(View.GONE);
                reviews.setVisibility(View.VISIBLE);
                deleteLAB.setVisibility(View.GONE);
                if (PreferenceUtils.get("createdBy", lab_user_details.this) != null) {
                    if (PreferenceUtils.get("createdBy", lab_user_details.this).equals(PreferenceUtils.getKeyId(lab_user_details.this))) {
                        delete.setVisibility(View.VISIBLE);
                    } else delete.setVisibility(View.GONE);
                }
            }
        }
        getReviews(lab_user_details.this);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder sBuilder = new AlertDialog.Builder(lab_user_details.this);
                View sView = getLayoutInflater().inflate(R.layout.close_app_permission, null);
                TextView confirmationText = (TextView) sView.findViewById(R.id.confirmationText);
                confirmationText.setText("Are you sure? You want to delete this contact from group?");
                final TextView ok = (TextView) sView.findViewById(R.id.closeOK);
                final TextView cancel = (TextView) sView.findViewById(R.id.closeCancel);
                sBuilder.setView(sView);
                dialog = sBuilder.create();
                if (!isFinishing()) {
                    dialog.show();
                    dialog.getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
                        ArrayList<String> as = new ArrayList<>();

                        JSONObject deleteContact = new JSONObject();
                        try {
                            as.add(profile.get(0).getString("id"));
                            deleteContact.put("groupId", intent.getStringExtra("groupId"));
                            deleteContact.put("contactIds", new JSONArray(as));
//                            commonClass.apiIntegration(lab_user_details.this, "groupcontacts/deletegroupcontacts", deleteContact.toString(), PreferenceUtils.getToken(lab_user_details.this), "deleteGroupContact");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ;

                    }
                });
            }
        });
//        Go Back Function
        labProfBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        Scroll to Function
        sendToReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lab_user_mainCon.smoothScrollTo(0, reviewList.getTop() + 500);
            }
        });

//        Toggle Share Function
        toggleBtnAppearance(profSharedByCrntUser, lab_user_details.this);
        ToggleReshareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unshareOrDelete("unshare");
            }
        });
        deleteLAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unshareOrDelete("delete");
            }
        });

//        Open Modal Element
        TextView openSharingUserModal = (TextView) findViewById(R.id.openSharingUserModal);
        View seperator = (View) findViewById(R.id.seperator);
       // if (recommendedBy != null)
          //  if (recommendedBy.size() > 1) {
                openSharingUserModal.setVisibility(View.VISIBLE);
                seperator.setVisibility(View.VISIBLE);
                String underlineText = "<u>+" + ("7") + "" + " Others</u>";
                openSharingUserModal.setText(HtmlCompat.fromHtml(underlineText, HtmlCompat.FROM_HTML_MODE_LEGACY));


        ImageButton openPostModal = (ImageButton) findViewById(R.id.openPostModal);

//        Modal With List Function
        openSharingUserModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder sBuilder = new AlertDialog.Builder(lab_user_details.this);
                View labShareUserView = getLayoutInflater().inflate(R.layout.nof_users_recommed_modal, null);
                ListView uR_List = (ListView) labShareUserView.findViewById(R.id.user_recommended);
                UR_Adapter ur_adapter = new UR_Adapter(new ArrayList<>());
                uR_List.setAdapter(ur_adapter);
                ImageView close = (ImageView) labShareUserView.findViewById(R.id.closeIcon);

                sBuilder.setView(labShareUserView);
                dialog = sBuilder.create();

                if (!((Activity) lab_user_details.this).isFinishing()) {
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
            }
        });

//        Modal With Form Function
        openPostModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newImg = false;
                Constants.imgsURL.removeAll(Constants.imgsURL);
                AlertDialog.Builder pBuilder = new AlertDialog.Builder(lab_user_details.this);
                View labPostReview = getLayoutInflater().inflate(R.layout.review_modal_dialgue, null);
                Button closePostDialogue = (Button) labPostReview.findViewById(R.id.closePostDialogue);
                post = (Button) labPostReview.findViewById(R.id.postReview);
                Button postuploadImage = (Button) labPostReview.findViewById(R.id.postuploadImage);
                recImgCon = (ExpandedHeightGridView) labPostReview.findViewById(R.id.recImgCon);
                profImage1 = (ImageView) labPostReview.findViewById(R.id.profImage1);
                profImage2 = (ImageView) labPostReview.findViewById(R.id.profImage2);
                profImage3 = (ImageView) labPostReview.findViewById(R.id.profImage3);
                profImage4 = (ImageView) labPostReview.findViewById(R.id.profImage4);
                profImage5 = (ImageView) labPostReview.findViewById(R.id.profImage5);
                ratingBar = (RatingBar) labPostReview.findViewById(R.id.postRatingBar);
                postFeedBack = (EditText) labPostReview.findViewById(R.id.postFeedBack);
                postFeedBack.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                //postFeedBack.addTextChangedListener(mTextWatcher);
                postuploadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(lab_user_details.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(lab_user_details.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(lab_user_details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            commonClass.selectImage(lab_user_details.this, "images");

                            // Do the file write
                        } else {
                            // Request permission from the user
                            ActivityCompat.requestPermissions(lab_user_details.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
                        }

                    }
                });

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    }
                });

                pBuilder.setView(labPostReview);
                postDialog = pBuilder.create();

                if (!((Activity) lab_user_details.this).isFinishing()) {
                    postDialog.show();
                    postDialog.getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    //show dialog
                }
                closePostDialogue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postDialog.dismiss();
                    }
                });
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // commonClass.imageAPI(requestCode , resultCode, data, lab_user_details.this,"lab_user_detail");
    }

    @Override
    public void onBackPressed() {
        TouchImageView expanded_image = (TouchImageView) findViewById(R.id.expanded_image);
        if (expanded_image.getVisibility() == View.VISIBLE) {
            expanded_image.setVisibility(View.GONE);
        } else if (screen == null || screen.equals("")) {
            Intent i = new Intent(lab_user_details.this, mainPage.class);
            startActivity(i);
        } else super.onBackPressed();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {


        new AppSettingsDialog.Builder(lab_user_details.this).build().show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 124) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                commonClass.call(phoneNum, lab_user_details.this);
            } else {
            }
        }
        if (requestCode == 1234) {
            commonClass.permissionPermanentlyDenied(lab_user_details.this, grantResults, "images");
        }

    }

    static boolean hasCallPermissions(Activity activity) {
        return EasyPermissions.hasPermissions(activity, CALL_PERMISSION);
    }

    @AfterPermissionGranted(124)
    public static void callTask(Activity activity) {
        if (hasCallPermissions(activity)) {
            commonClass.call(phoneNum, activity);
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


    class UR_Adapter extends BaseAdapter {

        ArrayList<JSONObject> recommendedBy;

        UR_Adapter(ArrayList<JSONObject> recommendedBy) {
            this.recommendedBy = recommendedBy;
        }

        @Override
        public int getCount() {
            return this.recommendedBy.size();
        }

        @Override
        public Object getItem(int position) {
            return this.recommendedBy.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.common_list_item, null);
            TextView uRName = (TextView) convertView.findViewById(R.id.comName);
            TextView comContactNumber = (TextView) convertView.findViewById(R.id.comContactNumber);
            TextView uRFLetter = (TextView) convertView.findViewById(R.id.comFLetter);
            Object getrow3 = getItem(position);
            LinkedTreeMap<Object, Object> t3 = (LinkedTreeMap) getrow3;
            Object getrow4 = t3.get("nameValuePairs");
            LinkedTreeMap<Object, Object> t4 = (LinkedTreeMap) getrow4;
            uRName.setText(t4.get("name").toString());
            uRFLetter.setText(t4.get("name").toString().charAt(0) + "");
            GradientDrawable gradientDrawable = (GradientDrawable) uRFLetter.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor(commonClass.randomColors()));
            comContactNumber.setText(t4.get("phoneNumber").toString());
            return convertView;
        }
    }
//    User Reviews Adapter

    public static void updateReviews(JSONArray reviews, Activity activity) throws JSONException {
        rating = 0;
        for (int i = 0; i < reviews.length(); i++) {
            rating += Float.parseFloat(reviews.getJSONObject(i).getString("rating"));
        }
        if (reviews.length() > 0)
            avgRating = rating / reviews.length();
        else avgRating = 0.0f;
        if (postDialog != null)
            postDialog.dismiss();
        ReviewAdapter1 reviewAdapter1 = new ReviewAdapter1(reviews, activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lab_user_mainCon != null)
                    lab_user_mainCon.smoothScrollTo(0, 0);
                DecimalFormat df = new DecimalFormat("#.#");
                sendToReview.setText(avgRating.toString());

                if (avgRatingBar != null && sendToReview != null) {
                    avgRatingBar.setRating(Float.parseFloat(df.format(avgRating)));
                    sendToReview.setText(avgRating.toString());
                }

                //if (reviewList != null)
                    reviewList.setAdapter(reviewAdapter1);
            }
        });

    }

    public static class ReviewAdapter1 extends BaseAdapter {

        Activity activity;
        JSONArray review;

        ReviewAdapter1(JSONArray review, Activity activity) {
            this.review = review;
            this.activity = activity;
        }

        @Override
        public int getCount() {
           return 6;
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
            convertView = activity.getLayoutInflater().inflate(R.layout.reviews_list_item, null);
            TextView mainLetter = (TextView) convertView.findViewById(R.id.revwFLetter);
            TextView nameTxt = (TextView) convertView.findViewById(R.id.revwName);
            TextView reviewDesc = (TextView) convertView.findViewById(R.id.revwDescription);
            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            JSONObject review = (JSONObject) getItem(position);
            ImageView deleteFeedback = (ImageView) convertView.findViewById(R.id.deleteFeedback);
            mainLetter.setText(Character.toUpperCase("P".charAt(0)) + "");
            reviewDesc.setText(description);
            ratingBar.setRating(9.9f);
            deleteFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder sBuilder = new AlertDialog.Builder(activity);
                    View sView = activity.getLayoutInflater().inflate(R.layout.close_app_permission, null);
                    TextView confirmationText = (TextView) sView.findViewById(R.id.confirmationText);
                    confirmationText.setText("Are you sure? You want to delete this feedback?");
                    final TextView ok = (TextView) sView.findViewById(R.id.closeOK);
                    final TextView cancel = (TextView) sView.findViewById(R.id.closeCancel);
                    sBuilder.setView(sView);
                    final AlertDialog dialog;
                    dialog = sBuilder.create();
                    dialog.dismiss();
                    if (!((Activity) activity).isFinishing()) {
                        dialog.show();
                        dialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
                                jsonObject.put("reviewId", review.getString("id"));
//                                commonClass.apiIntegration(activity, "review/deletereview", jsonObject.toString(), PreferenceUtils.getToken(activity),"deleteFeedback");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });



            try {
                if (PreferenceUtils.getKeyId(activity).equals(review.getString("recommendedBy")))
                    deleteFeedback.setVisibility(View.VISIBLE);
                else
                    deleteFeedback.setVisibility(View.GONE);
                nameTxt.setText("patient ");
                String reg = "[a-zA-Z0-9]";
                mainLetter.setText(Character.toUpperCase("P".charAt(0)) + "");


                reviewDesc.setText(description);
                ratingBar.setRating(9.9f);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray getrow1 = null;
            try {
                getrow1 = review.getJSONArray("Image");
                if (getrow1.length() > 0) {
                    String[] img = getrow1.getJSONObject(0).getString("imageUrl").split("\\[|\\]| |,|\\\"");
                    ArrayList<String> imgs = new ArrayList<>();
                    for (int k = 0; k < img.length; k++) {

                        if (img[k] != null && (!"".equals(img[k]))) {
                            imgs.add(img[k]);
                        }
                    }
                    ExpandedHeightGridView dirImageGridView = (ExpandedHeightGridView) convertView.findViewById(R.id.labProfImgCon);
                    ImageAdapter imageAdapter = new ImageAdapter(imgs, activity);
                    dirImageGridView.setAdapter(imageAdapter);
                    dirImageGridView.setExpanded(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    //    Share Button Appearance Toggle
    public static void toggleBtnAppearance(final Boolean bool, Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ToggleReshareBtn != null)
                    if (bool) {
                        ToggleReshareBtn.setText(R.string.shared);
                        ToggleReshareBtn.setBackgroundResource(R.drawable.shared_btn_background);
                    } else {
                        ToggleReshareBtn.setText(R.string.share_que_mark);
                        ToggleReshareBtn.setBackgroundResource(R.drawable.black_btn_background);
                    }
            }
        });

    }

    static int inc;

    public static void images(final Activity activity) {

        final ArrayList<JSONObject> imgs = PreferenceUtils.getGroupNamesJSON("imgURLSlist", activity);
        labimg = new ArrayList<>();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newImg = true;
                if (recImgCon != null) {
                    for (int i = 0; i < imgs.size(); i++) {
                        try {
                            labimg.add(imgs.get(i).getString("" + i + ""));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayList<String> imgurl = new ArrayList<>();
                    for (inc = 0; inc < imgs.size(); inc++) {
                        imgurl.add(com.maksof.linkapp.commonClass.urlBitmap(activity, inc));
                    }
                    recImgCon.setExpanded(true);
                    recImgCon.setVisibility(View.VISIBLE);
                    ImageAdapter2 imageAdapter = new ImageAdapter2(imgurl, activity);
                    recImgCon.setAdapter(imageAdapter);
                }
            }

        });

    }

    public static class ImageAdapter extends BaseAdapter {
        ArrayList<String> images;
        Activity activity;

        ImageAdapter(ArrayList<String> review, Activity activity) {

            this.images = review;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return this.images.size();
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
            convertView = activity.getLayoutInflater().inflate(R.layout.directory_profile_images, null);
            final ImageView imgViewDirec = (ImageView) convertView.findViewById(R.id.imgViewDirec);
            final String url = (String) this.getItem(position);
            String imgUrl = commonClass.imgUrl;
            activity.runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setTint(Color.parseColor("#ffffff"));
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();

                    GlideApp.with(activity)
                            .load(imgUrl + url)
                            .placeholder(circularProgressDrawable)
                            .into(imgViewDirec);
                    imgViewDirec.setVisibility(View.VISIBLE);
                }
            });
            imgViewDirec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomImageFromThumb(imgViewDirec, url, activity);
                }
            });
            return convertView;
        }

        public static void zoomImageFromThumb(final View thumbView, String imageResId, Activity activity) {
            final TouchImageView expandedImageView = (TouchImageView) activity.findViewById(R.id.expanded_image);
            commonClass.zoomImageFromThumb2(thumbView, imageResId, expandedImageView, activity);
        }
    }

    public static void getReviews(Activity activity) {
        JSONObject getRev = new JSONObject();
        try {
            getRev.put("contactId", contactId);
//            commonClass.apiIntegration(activity, "review/getreviewbycontactid", getRev.toString(), PreferenceUtils.getToken(activity), "getReviews");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void unshareOrDelete(String what) {
        AlertDialog.Builder shareBuilder = new AlertDialog.Builder(lab_user_details.this);
        View labShareProfConfirmation = getLayoutInflater().inflate(R.layout.confirmation_modal, null);

        TextView confirmationText = (TextView) labShareProfConfirmation.findViewById(R.id.confirmationText);
        Button confirmShareOrCancel = (Button) labShareProfConfirmation.findViewById(R.id.confirm);
        Button closeCnfrmModal = (Button) labShareProfConfirmation.findViewById(R.id.closeConfirmationModal);
        if (what.equals("unshare")) {
            if (profSharedByCrntUser) {
                confirmationText.setText("Are you sure you want to share your profile with Dr.");
            } else {
                confirmationText.setText(R.string.are_you_sure_txt);
            }
        } else {
            confirmationText.setText("Are you sure you want to remove this contact? They will no longer be shared.");
        }

        shareBuilder.setView(labShareProfConfirmation);
        final AlertDialog shareDialog = shareBuilder.create();
        shareDialog.show();
        shareDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        confirmShareOrCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("contactId", contactId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (what.equals("unshare")) {
                   /* if (!profSharedByCrntUser) {
                        commonClass.apiIntegration(lab_user_details.this, "labcontact/createlabcontact", jsonObject.toString(), PreferenceUtils.getToken(lab_user_details.this), "createLabContact");
                    } else {
                        commonClass.apiIntegration(lab_user_details.this, "profile/unsahrecontact", jsonObject.toString(), PreferenceUtils.getToken(lab_user_details.this), "unshareProfile");
                    }*/
                    profSharedByCrntUser = !profSharedByCrntUser;
                    toggleBtnAppearance(profSharedByCrntUser, lab_user_details.this);
                } else {
//                    commonClass.apiIntegration(lab_user_details.this, "labcontact/deletelabcontact", jsonObject.toString(), PreferenceUtils.getToken(lab_user_details.this), "deletelabcontact");
                }
                shareDialog.dismiss();
            }
        });
        closeCnfrmModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });
    }

    static class ImageAdapter2 extends BaseAdapter {
        ArrayList<String> images;
        Activity activity;

        ImageAdapter2(ArrayList<String> review, Activity activity) {

            this.images = review;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return this.images.size();
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

            final String url = (String) this.getItem(position);
            activity.runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setTint(Color.parseColor("#ffffff"));
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();
                    GlideApp.with(activity)
                            .load(com.maksof.linkapp.commonClass.imgUrl + url)
                            .placeholder(circularProgressDrawable)
                            .into(imgViewDirec);
                    imgViewDirec.setVisibility(View.VISIBLE);
                }
            });
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (labimg != null)
                        labimg.remove(url);
                    commonClass.updateConstantsImageUrl(labimg);
                    PreferenceUtils.saveGroupString(labimg, "imgString", activity);
                    imgViewDirec.setVisibility(View.GONE);
                    closeBtn.setVisibility(View.GONE);
                }
            });
            return convertView;
        }
    }
}
