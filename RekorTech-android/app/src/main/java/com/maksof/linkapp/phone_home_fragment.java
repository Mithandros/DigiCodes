package com.maksof.linkapp;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.libraries.places.api.Places;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.android.volley.VolleyLog.TAG;

public class phone_home_fragment extends Fragment  {
    static EditText recName, recOccup, recFeed,recNum, recWeb, recEmail,recAddress;
    static Boolean canShare = true;
    private static View sView;
    AutocompleteSupportFragment autocompleteFragments;
    static String groupname, address="";
    static AlertDialog dialog;
    static ImageView openShareModal,profImage1,profImage2,profImage3,profImage4,profImage5,labProfile;
    static ExpandedHeightGridView recImageGridView ;
    static String contactNumber,groupID;
    static String contactNumber1;
    commonClass commonClass =new commonClass();
    static boolean clicked;
    private static final int REQUEST_READ_CONTACTS = 0;
    public static AnimatedExpandableListView l1;
    public static HashMap<String,List<String>>  listHashMap = new HashMap<>();
    static ArrayList<String> nameList =new ArrayList<>();
    static ArrayList<String> numList =new ArrayList<>();
    static ArrayList<String> colorList = new ArrayList<>();
    static ContactAdapter contactAdapter;
    private static final String[] CALL_PERMISSION =
            {Manifest.permission.CALL_PHONE};
    private final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

    private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

    static ArrayList<JSONObject> GroupNameJSON = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateAdapter(){
        try {
            JSONObject job = commonClass.myContacts(getContext());
            if(PreferenceUtils.getGroupString("nameList",getActivity())!=null)
            {
                nameList = (ArrayList<String>) PreferenceUtils.getGroupString("nameList",getActivity());
                numList = (ArrayList<String>) PreferenceUtils.getGroupString("numList",getActivity());
                colorList = (ArrayList<String>) PreferenceUtils.getGroupString("colorList",getActivity());

                for(int i=0;i<nameList.size();i++)
                    listHashMap.put(nameList.get(i), null);
            }
            contactAdapter = new ContactAdapter(getActivity(),nameList,numList,colorList,listHashMap,"");
            l1.setAdapter(contactAdapter);
            contactAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View phoneHomeView = inflater.inflate(R.layout.phone_home_fragment, container,false);
        final SwipeRefreshLayout pullToRefresh = phoneHomeView.findViewById(R.id.phoneHome);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateAdapter();
                pullToRefresh.setRefreshing(false);
            }
        });
        l1 = (AnimatedExpandableListView) phoneHomeView.findViewById(R.id.contactMirrorList);
        l1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (l1.getChildAt(0) != null) {
                    pullToRefresh.setEnabled(l1.getFirstVisiblePosition() == 0 && l1.getChildAt(0).getTop() == 0);
                    pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                           updateAdapter();
                            pullToRefresh.setRefreshing(false);
                        }
                    });

                }
            }
        });
            if(PreferenceUtils.get("adapterInitialized",getContext())!=null)
                if(PreferenceUtils.get("adapterInitialized",getContext()).equals("true"))
                    if(PreferenceUtils.getGroupString("nameList",getActivity())!=null)
                    {
                        nameList = (ArrayList<String>) PreferenceUtils.getGroupString("nameList",getActivity());
                        numList = (ArrayList<String>) PreferenceUtils.getGroupString("numList",getActivity());
                        colorList = (ArrayList<String>) PreferenceUtils.getGroupString("colorList",getActivity());
                        for(int i=0;i<nameList.size();i++)
                            listHashMap.put(nameList.get(i), null);
                    }
            if(contactAdapter==null)
                contactAdapter = new ContactAdapter(getActivity(),nameList,numList,colorList,listHashMap,"");
            else contactAdapter.swapItems(getActivity(),nameList,numList,colorList,listHashMap,"");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(l1!=null)
                {
                    l1.setAdapter(contactAdapter);
                    contactAdapter.notifyDataSetChanged();
                }
            }
        });

                if(PreferenceUtils.get("adapterInitialized",getContext())==null){
                    try {
                        JSONObject job = com.maksof.linkapp.commonClass.myContacts(getContext());
                        nameList = (ArrayList<String>) job.get("name");
                        numList = (ArrayList<String>) job.get("num");
                        colorList = (ArrayList<String>) job.get("color");
                        for(int i=0;i<nameList.size();i++)
                            listHashMap.put(nameList.get(i), null);
                        contactAdapter = new ContactAdapter(getActivity(),nameList,numList,colorList,listHashMap,"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    if(PreferenceUtils.getGroupString("nameList",getActivity())!=null)
                    {
                        nameList = (ArrayList<String>) PreferenceUtils.getGroupString("nameList",getActivity());
                        numList = (ArrayList<String>) PreferenceUtils.getGroupString("numList",getActivity());
                        colorList = (ArrayList<String>) PreferenceUtils.getGroupString("colorList",getActivity());
                        for(int i=0;i<nameList.size();i++)
                            listHashMap.put(nameList.get(i), null);
                    }
                    if(contactAdapter==null)
                        contactAdapter = new ContactAdapter(getActivity(),nameList,numList,colorList,listHashMap,"");
                    else contactAdapter.swapItems(getActivity(),nameList,numList,colorList,listHashMap,"");
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(l1!=null)
                        {    l1.setAdapter(contactAdapter);
                            contactAdapter.notifyDataSetChanged();
                        }
                    }
                });

        if(l1!=null)
        l1.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int previousGroup=-1;
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (l1.isGroupExpanded(groupPosition)) {
                    l1.collapseGroupWithAnimation(groupPosition);
                    previousGroup=-1;
                } else {
                    l1.expandGroupWithAnimation(groupPosition);
                    if(previousGroup!=-1){
                        l1.collapseGroupWithAnimation(previousGroup);
                    }
                    previousGroup=groupPosition;
                }

                return true;
            }
        });

        phoneHomeView.setFocusableInTouchMode(true);
        phoneHomeView.requestFocus();
        return phoneHomeView;
    }
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    class ContactAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter implements EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks  {

        Spinner spinner;
        Button uploadImage, sGroup_btn, sLab_btn;
        Spinner groups;
        TextView ratingText;
        RatingBar recRatingBar;
        private  String highlight;
        private List<String> listDataHeader;
        private  List<String> number;
        private  List<String> color;
        private HashMap<String,List<String>> listHashMap1;
        Activity activity;
        ArrayList<JSONObject> userOnLinkApp = new ArrayList<>();
        public ContactAdapter(Activity act, List<String> listDataHeader,List<String> n, List<String> colorList,HashMap<String,List<String>> listHashMap,String highlight){
            this.listDataHeader = listDataHeader;
            this.number = n;
            this.listHashMap1 = listHashMap;
            this.activity = act;
            this.color = colorList;
            this.highlight = highlight;
            PreferenceUtils.save("true","adapterInitialized",getContext());
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            if(this.listDataHeader!=null)
            if(this.listDataHeader.size()>0&&groupPosition<this.listDataHeader.size())
            return this.listDataHeader.get(groupPosition);
            else return null;
            else return null;
        }

        public Object getnumber(int groupPosition) {
            if(this.number!=null){
            if(this.number.size()>0&&groupPosition<this.number.size())
            return this.number.get(groupPosition);
            else return null;}
            return null;
        }
        public Object getcolor(int groupPosition) {
            if(this.color!=null){
            if(this.color.size()>0&&groupPosition<this.color.size())
                return   this.color.get(groupPosition);
            else return "#000000";}
            return "#000000";
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.listHashMap1.get(this.listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.contacts_mirror_item, parent, false);
            }
            this.userOnLinkApp = PreferenceUtils.getGroupContactsJSON("userRegistration",activity);
            String contactName = (String)getGroup(groupPosition);
            contactNumber=(String)getnumber(groupPosition);
            char firstchar = '#';
            if(contactName!=null && !("".equals(contactName)))
            firstchar =(char)contactName.charAt(0);
            TextView contactMainLetter= (TextView)convertView.findViewById(R.id.contactFLetter);
            TextView nameTxt= (TextView)convertView.findViewById(R.id.contactName);

            openShareModal = (ImageView) convertView.findViewById(R.id.shareProfBtn);
            labProfile = (ImageView) convertView.findViewById(R.id.labProfile);


                    openShareModal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.maksof.linkapp.commonClass.hideSoftKeyboard(activity);
                    canShare=true;
                    userOnLinkApp = PreferenceUtils.getGroupContactsJSON("userRegistration",activity);
                    if(userOnLinkApp!=null)
                    for(int k=0;k<userOnLinkApp.size();k++){
                        try{
                            if("true".equals(userOnLinkApp.get(k).getString("isAllowToLab")) && ((String)getnumber(groupPosition)).equals(userOnLinkApp.get(k).getString("phoneNo"))){
                                canShare=false;
                                showToast("The profile you are trying to share has a restricted access");
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(canShare==true) {
                        final AlertDialog.Builder sBuilder = new AlertDialog.Builder(activity);
                        sBuilder.setCancelable(true);
                        try {
                            sView = activity.getLayoutInflater().inflate(R.layout.recommend_dialogue, null);


                        } catch (InflateException e) {

                        }
                        if (sView != null) {
                            ViewGroup parent = (ViewGroup) sView.getParent();
                            if (parent != null) {
                                parent.removeAllViews();
                            }
                        }

                        if (sView != null)
                        {
                            Places.initialize(activity, "AIzaSyCYlFhfg9LErOaVm_JivuovEcyh2CpuIN0");
                            PlacesClient placesClient = Places.createClient(activity);
                        if (getActivity() == null)
                            onAttach(activity);
                        if (getActivity() != null)
                            try {
                                recAddress = (EditText) sView.findViewById(R.id.recAddress);
                                recAddress.setVisibility(View.GONE);
                                autocompleteFragments = (AutocompleteSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.location_autocomplete_fragment2);
                                if (autocompleteFragments != null) {
                                    EditText etPlace = (EditText) autocompleteFragments.getView().findViewById(R.id.places_autocomplete_search_input);
                                    if (etPlace != null) {
                                        etPlace.setText("");
                                        etPlace.setBackgroundColor(Color.parseColor("#ededed"));
                                        etPlace.setHintTextColor(Color.parseColor("#959595"));
                                        etPlace.setPadding(5, 0, 0, 0);
                                        etPlace.setTextSize(18);

                                    }
                                    autocompleteFragments.setHint("Address");
                                    if ((View) autocompleteFragments.getView().findViewById(R.id.places_autocomplete_search_button) != null)
                                        autocompleteFragments.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);
                                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                                            .build();
                                    autocompleteFragments.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
                                    autocompleteFragments.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                                        @Override
                                        public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                                            autocompleteFragments.setText(place.getName());
                                            address = place.getName();
                                            Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                                            if (autocompleteFragments.getView().findViewById(R.id.places_autocomplete_clear_button) != null)
                                                autocompleteFragments.getView().findViewById(R.id.places_autocomplete_clear_button).setBackgroundColor(Color.parseColor("#ededed"));

                                        }

                                        @Override
                                        public void onError(Status status) {
                                            // TODO: Handle the error.
                                            Log.i(TAG, "An error occurred: " + status);
                                        }
                                    });
                                }
                            } catch (Exception ne) {
                                ne.printStackTrace();
                            }
                    }

                    if(sView!=null){
                    recName = (EditText) sView.findViewById(R.id.recName);
                    recAddress = (EditText) sView.findViewById(R.id.recAddress);
                    recEmail = (EditText) sView.findViewById(R.id.recEmail);
                    recWeb = (EditText) sView.findViewById(R.id.recWebsite);
                    recNum = (EditText) sView.findViewById(R.id.recPhone);
                    recOccup = (EditText) sView.findViewById(R.id.recOccupation);
                    recFeed = (EditText) sView.findViewById(R.id.recDesc);
                    uploadImage = (Button) sView.findViewById(R.id.uploadImage);
                    sGroup_btn = (Button) sView.findViewById(R.id.sGroup_btn);
                    sLab_btn = (Button) sView.findViewById(R.id.sLab_btn);
                    groups = (Spinner) sView.findViewById(R.id.groupsSpinner);
                    ratingText = (TextView) sView.findViewById(R.id.ratingText);
                    recRatingBar = (RatingBar) sView.findViewById(R.id.recRatingBar);
                    spinner = (Spinner) sView.findViewById(R.id.groupsSpinner);
                    profImage1 = (ImageView) sView.findViewById(R.id.profImage1);
                    profImage2 = (ImageView) sView.findViewById(R.id.profImage2);
                    profImage3 = (ImageView) sView.findViewById(R.id.profImage3);
                    profImage4 = (ImageView) sView.findViewById(R.id.profImage4);
                    profImage5 = (ImageView) sView.findViewById(R.id.profImage5);
                        recImageGridView = (ExpandedHeightGridView)sView.findViewById(R.id.recImgCon);
                        Constants.imgsURL.removeAll(Constants.imgsURL);
                        LinearLayout hide = (LinearLayout)sView.findViewById(R.id.hideKeyboard);
                        hide.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(sView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                                return true;
                            }
                        });

                   // profImage1.setImageBitmap(null);
                        recImageGridView.setVisibility(View.GONE);
                    recName.setText("");
                    recEmail.setText("");
                    recWeb.setText("");
                    recNum.setText("");
                    recOccup.setText("");
                    recFeed.setText("");
                    ratingText.setText("");
                    recName.setError(null);
                    recEmail.setError(null);
                    recWeb.setError(null);
                    recNum.setError(null);
                    recOccup.setError(null);
                    recFeed.setError(null);
                    recRatingBar.setRating(0.0f);
                        LayerDrawable stars = (LayerDrawable) recRatingBar.getProgressDrawable();
                        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.lightGrey), PorterDuff.Mode.SRC_ATOP);

                        recName.requestFocus();
                        recName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                        recOccup.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                        recFeed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                    uploadImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                com.maksof.linkapp.commonClass.selectImage(activity,"images");

                                // Do the file write
                            } else {
                                // Request permission from the user
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
                            }

                        }
                    });

                    GroupNameJSON = PreferenceUtils.getGroupNamesJSON("groupNamesJson", activity);
                    ArrayList<String> arrayList = new ArrayList<>();
                    if (GroupNameJSON != null)
                        for (int i = 0; i < GroupNameJSON.size(); i++) {
                            try {
                                arrayList.add(GroupNameJSON.get(i).getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(activity,
                                android.R.layout.simple_dropdown_item_1line, arrayList);

                        areaAdapter.setDropDownViewResource(R.layout.spinner_view);
                        spinner.setAdapter(areaAdapter);
                    recName.setText((String) getGroup(groupPosition));
                    recNum.setText((String) getnumber(groupPosition).toString().replaceAll("[^0-9]",""));

                    sGroup_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clicked = true;
                            changeModalElements("Group");
                        }
                    });

                    sLab_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clicked = false;
                            changeModalElements("LAB");
                        }
                    });
                    recRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        }
                    });
                    Button share = (Button) sView.findViewById(R.id.shareRecProfile);
                    Button cancel = (Button) sView.findViewById(R.id.closeRecDialogue);
                    sBuilder.setView(sView);
                        dialog= sBuilder.create();
                        dialog.setCanceledOnTouchOutside(true);
                        cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name, num, email, website, occupation, recF, rating;
                            name = recName.getText().toString();
                            num = recNum.getText().toString();
                            email = recEmail.getText().toString();
                            website = recWeb.getText().toString();
                            occupation = recOccup.getText().toString();
                            recF = recFeed.getText().toString();
                            rating = recRatingBar.getRating() + "";
                            PreferenceUtils.save(rating, "labContactRating", activity);
                            PreferenceUtils.save(recF, "labContactFeedBack", activity);
                            if (spinner.getSelectedItem() != null)
                                groupname = spinner.getSelectedItem().toString();

                            if (GroupNameJSON != null)
                                for (int i = 0; i < GroupNameJSON.size(); i++) {
                                    try {
                                        if (groupname.contains(GroupNameJSON.get(i).getString("name").toString()))
                                        {
                                            groupID = GroupNameJSON.get(i).getString("id");
                                            PreferenceUtils.SaveGroupName(GroupNameJSON.get(i).getString("name"),getActivity());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            boolean flag = true;

                            String nameReg="[a-zA-Z][a-z A-Z.\\d]{1,20}$";
                            String emailReg="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
                            if(!name.matches(nameReg)) {
                                flag=false;
                                recName.setError("Invalid Name");
                            }
                            if(email!=null&&(!email.equals(""))){
                                if(!email.matches(emailReg)) {
                                    flag=false;
                                    recEmail.setError("Invalid email address");
                                }
                            }
                            if(num.equals("")) {
                                flag=false;
                                recNum.setError("Invalid Number");

                            }
                            if ("".equals(recF) || recF == null) {
                                flag = false;
                                recFeed.setError("Required field");
                            }
                            if ("".equals(occupation) || occupation == null) {
                                flag = false;
                                recOccup.setError("Required field");
                            }
                                if (recRatingBar.getRating() == 0.0) {
                                    flag = false;
                                   // recRatingBar.setProgressDrawable(getResources().getDrawable(R.drawable.emptystar));
                                    LayerDrawable stars = (LayerDrawable) recRatingBar.getProgressDrawable();
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                                        stars.getDrawable(0).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                                    else stars.getDrawable(0).setColorFilter(Color.parseColor("#ff9999"), PorterDuff.Mode.SRC_ATOP);
                                }

                            if (clicked == true)
                            if(spinner.getSelectedItem()==null){
                                flag = false;
                                activity.runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void run() {
                                        showToast("Create group first");
                                    }
                                });
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
                                    jsonObject.put("description", recF);
                                    jsonObject.put("address", address);
                                    jsonObject.put("website", website);
                                    if (clicked == true) {
                                        jsonObject.put("directoryProfile", "GROUP_CONTACT");
//                                        com.maksof.linkapp.commonClass.apiIntegration(activity, "contact/createcontact", jsonObject.toString(), PreferenceUtils.getToken(activity), "recommendUserGroup", groupID);
                                    } else {
                                        jsonObject.put("directoryProfile", "LAB");
//                                        com.maksof.linkapp.commonClass.apiIntegration(activity, "contact/createcontact", jsonObject.toString(), PreferenceUtils.getToken(activity), "recommendUserLab");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                        if(!(activity.isFinishing()))
                        {
                            dialog.show();
                            dialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        }
                    } }}
            });

                    if(userOnLinkApp!=null){
                                labProfile.setVisibility(View.GONE);
                                openShareModal.setVisibility(View.VISIBLE);
                                for(int k=0;k<userOnLinkApp.size();k++){
                                    try{
                                        if(userOnLinkApp.get(k).getString("contactId")!=null && userOnLinkApp.get(k).getString("phoneNo")!=null && contactNumber!=null)
                                            if(!"".equals(userOnLinkApp.get(k).getString("contactId")) && contactNumber.equals(userOnLinkApp.get(k).getString("phoneNo"))){
                                                labProfile.setVisibility(View.VISIBLE);
                                                openShareModal.setVisibility(View.GONE);
                                                break;
                                            }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                    }

            String text = contactName;
            String textToBeColored = highlight;
            String htmlText ="";
            if(text!=null)
             htmlText = text.replaceAll("(?i)"+textToBeColored,"<font color='#3f51b5' ; background-color:'#000000'>"+textToBeColored +"</font>");

            // Only letter a would be displayed in a different color.
            if(highlight.equals(""))
                nameTxt.setText(contactName);
            else
                nameTxt.setText(Html.fromHtml(htmlText));
            String reg="[a-zA-Z0-9]";
            if(((firstchar+"").matches(reg)))
                contactMainLetter.setText(Character.toUpperCase(firstchar)+"");
            else contactMainLetter.setText(Constants.firstChar);
            if(contactMainLetter!=null) {
                final GradientDrawable gradientDrawable = (GradientDrawable) contactMainLetter.getBackground().mutate();
                try{
                    gradientDrawable.setColor(Color.parseColor((String) getcolor(groupPosition)));
                }catch (IllegalArgumentException t){}
            }
            return convertView;

        }
        public void showToast(String message) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public View getRealChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.lab_list_item_child, parent,false);
                final LinearLayout animate_lab_child = (LinearLayout) convertView.findViewById(R.id.animate_lab_child);
            }
            final LinearLayout animate_lab_child = (LinearLayout) convertView.findViewById(R.id.animate_lab_child);
            ImageButton call = (ImageButton) convertView.findViewById(R.id.labCallBtn);
            ImageButton sms = (ImageButton) convertView.findViewById(R.id.labSmsBtn);
            ImageButton whatsapp = (ImageButton) convertView.findViewById(R.id.labWhatsappBtn);
            ImageButton info = (ImageButton) convertView.findViewById(R.id.labInfoBtn);
            contactNumber1=(String)getnumber(groupPosition);
            labProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    labProOnClick((String)getnumber(groupPosition));
                }
            });
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.maksof.linkapp.commonClass.openWhatsApp(activity,contactNumber1);
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        com.maksof.linkapp.commonClass.call(contactNumber1,activity);
                        // Do the file write
                    } else {
                        // Request permission from the user
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }


                }
            });

            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.maksof.linkapp.commonClass.sms(activity,contactNumber1);
                }
            });

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder iBuilder = new AlertDialog.Builder(activity);
                    View sView =activity.getLayoutInflater().inflate(R.layout.phon_numbe_info, null);

                    iBuilder.setView(sView);
                    final AlertDialog dialog = iBuilder.create();
                    if(!(activity.isFinishing()))
                    {
                        dialog.show();
                        dialog.getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        //show dialog
                    }
                    ImageView close = (ImageView)sView.findViewById(R.id.closeIcon);
                    TextView phoneNumber = (TextView)sView.findViewById(R.id.phoneNumber);
                    phoneNumber.setText(contactNumber1);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            return convertView;
        }
        public void labProOnClick(String contactNum){
            for(int k=0;k<userOnLinkApp.size();k++){
                        try{
                            ArrayList<JSONObject> al= PreferenceUtils.getGroupContactsJSON("getLabContacts", activity);
                            if(userOnLinkApp.get(k).getString("contactId")!=null && userOnLinkApp.get(k).getString("phoneNo")!=null && contactNum!=null)
                                if(userOnLinkApp.get(k).getString("phoneNo").equals(contactNum)&&(!"".equals(userOnLinkApp.get(k).getString("contactId")))){
                                    JSONObject jsonObject = new JSONObject();
                                    JSONArray jsonArray = new JSONArray();
                                    try {
                                        if(al!=null){
                                        for(int j = 0;j< al.size(); j++){
                                            if(userOnLinkApp.get(k).getString("contactId").equals(al.get(j).getString("contactId")))
                                            {
                                                Object getro = al.get(j).get("addedBy");
                                                LinkedTreeMap<Object,Object> t = (LinkedTreeMap) getro;
                                                Object getrow1 = t.get("values");
                                                ArrayList<JSONObject> listOfAddedBy2 = (ArrayList<JSONObject>) getrow1;
                                                for(int i=0;i<listOfAddedBy2.size();i++)
                                                {
                                                    try {
                                                        Object  getrow5 =(Object) listOfAddedBy2.get(i);
                                                        LinkedTreeMap<Object,Object> t3 = (LinkedTreeMap) getrow5;
                                                        Object getrow = t3.get("nameValuePairs");
                                                        LinkedTreeMap<Object,Object> getrow4 = (LinkedTreeMap) getrow;
                                                        JSONObject jb = new JSONObject();
                                                        jb.put("id",getrow4.get("id"));
                                                        jb.put("name",getrow4.get("name"));
                                                        jb.put("phoneNumber",getrow4.get("phoneNumber"));
                                                        jb.put("date",getrow4.get("date"));
                                                        jsonArray.put(jb);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                        jsonObject.put("contactId", userOnLinkApp.get(k).getString("contactId"));
                                        jsonObject.put("recommendedBy",jsonArray);
//                                        com.maksof.linkapp.commonClass.apiIntegration(activity, "profile/getlabprofile", jsonObject.toString(), PreferenceUtils.getToken(activity), "getLabProfile");
                                    }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
        private void changeModalElements(String bool){
            if(bool == "LAB"){
                recFeed.setHint("Review");
                groups.setVisibility(View.GONE);
                sGroup_btn.setBackgroundResource(R.drawable.right_toggle_btn_background);
                sLab_btn.setBackgroundResource(R.drawable.left_active_toggle_btn_background);
                sGroup_btn.setTypeface(null,Typeface.NORMAL);
                sLab_btn.setTypeface(sLab_btn.getTypeface(), Typeface.BOLD);
                sGroup_btn.setTextColor(ContextCompat.getColor(activity,R.color.fontColor));
                sLab_btn.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                uploadImage.setVisibility(View.VISIBLE);
                ratingText.setVisibility(View.VISIBLE);
                recRatingBar.setVisibility(View.VISIBLE);
            }else if(bool == "Group"){
                float size=sGroup_btn.getTextSize();
                sGroup_btn.setTypeface(sGroup_btn.getTypeface(), Typeface.BOLD);
                sLab_btn.setTypeface(null,Typeface.NORMAL);
                sGroup_btn.setBackgroundResource(R.drawable.right_active_toggle_btn_background);
                sLab_btn.setBackgroundResource(R.drawable.left_toggle_btn_background);
                sGroup_btn.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));
                sLab_btn.setTextColor(ContextCompat.getColor(activity,R.color.fontColor));
                groups.setVisibility(View.VISIBLE);
                uploadImage.setVisibility(View.VISIBLE);
                ratingText.setVisibility(View.VISIBLE);
                recRatingBar.setVisibility(View.VISIBLE);
                recFeed.setHint("Description");
            }
        }


        @Override
        public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        }

        @Override
        public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

            new AppSettingsDialog.Builder(activity).build().show();


        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            callTask(activity);
                   }

         boolean hasCallPermissions(Activity activity) {
            return EasyPermissions.hasPermissions(activity, CALL_PERMISSION);
        }

        @AfterPermissionGranted(126)
        public  void callTask(Activity activity) {
            if (hasCallPermissions(activity)) {

                com.maksof.linkapp.commonClass.call(contactNumber1,activity);
            } else {
                EasyPermissions.requestPermissions(
                        activity,
                        "This app needs access to your location and contacts to know where and who you are.", 126, CALL_PERMISSION);
            }
        }
        @Override
        public void onRationaleAccepted(int requestCode) {

        }

        @Override
        public void onRationaleDenied(int requestCode) {


        }

        public void swapItems(Activity activity,ArrayList<String> name,ArrayList<String> n,ArrayList<String> color,HashMap<String,List<String>> listHashMap,String highlight1) {
            this.listDataHeader = name;
            this.listDataHeader = listDataHeader;
            this.number = n;
            this.listHashMap1 = listHashMap;
            this.color = color;
            this.highlight = highlight1;
            this.activity = activity;
        }
    }
    class UR_Adapter extends BaseAdapter{

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
            convertView = getLayoutInflater().inflate(R.layout.common_list_item,null);

            TextView uRName = (TextView)convertView.findViewById(R.id.comName);
            TextView uRFLetter = (TextView)convertView.findViewById(R.id.comFLetter);


            return convertView;
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
   public static void dialCall(Activity activ){

           com.maksof.linkapp.commonClass.call(contactNumber1,activ);
   }
    static int inc;
   public static void images(final Activity activity){

        final ArrayList<JSONObject> imgs = PreferenceUtils.getGroupNamesJSON("imgURLSlist",activity);
        activity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
              if(recImageGridView!=null) {

                   ArrayList<String> imgurl = new ArrayList<>();
                   for( inc = 0 ;inc<imgs.size();inc++) {
                       imgurl.add(com.maksof.linkapp.commonClass.urlBitmap(activity, inc));
                   }
                  recImageGridView.setExpanded(true);
                  recImageGridView.setVisibility(View.VISIBLE);
                  ImageAdapter imageAdapter = new ImageAdapter(imgurl,activity);
                  recImageGridView.setAdapter(imageAdapter);
               }
           }
       });

    }

public static void toast(String message,Activity activity){
       Toast.makeText(activity,message,Toast.LENGTH_LONG);
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
                    ArrayList<String> imgs = PreferenceUtils.getGroupString("imgString",activity);
            if(imgs!=null)
               imgs.remove(url);
                    com.maksof.linkapp.commonClass.updateConstantsImageUrl(imgs);
                    PreferenceUtils.saveGroupString(imgs,"imgString",activity);
                    imgViewDirec.setVisibility(View.GONE);
                    closeBtn.setVisibility(View.GONE);
                }
            });
            return convertView;
        }
    }

    @Override
    public void onResume() {
       updateAdapter();
        PreferenceUtils.save("resume","phone_home",getContext());
        super.onResume();
        ImageButton triggerSearchBar = (ImageButton)getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceUtils.save("pause","phone_home",getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        contactAdapter = new ContactAdapter(getActivity(),nameList, numList,colorList,listHashMap,"");
        PreferenceUtils.save("resume","phone_home",getContext());

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void search(Activity activity, final  String newText) {
        if (newText.length() > 0) {

            ArrayList<String> nameListSearch = new ArrayList<>();
            ArrayList<String> numListSearch = new ArrayList<>();
            ArrayList<String> colorListSearch = new ArrayList<>();
            int size = nameList.size();
            for (int i = 0; i < size; i++) {
                Pattern pattern = Pattern.compile(newText.toLowerCase() + ".*");
                Matcher matcher = pattern.matcher(nameList.get(i).toLowerCase());
                if (matcher.matches()) {
                    nameListSearch.add(nameList.get(i));
                    numListSearch.add(numList.get(i));
                    colorListSearch.add(colorList.get(i));
                }
            }

            HashMap listHashMap2 = new HashMap<>();
            for (int i = 0; i < nameList.size(); i++) {
                listHashMap2.put(nameList.get(i), null);
            }

            if (contactAdapter != null) {
                contactAdapter.swapItems(activity, nameListSearch, numListSearch, colorListSearch, listHashMap, newText);
                        l1.setAdapter(contactAdapter);
                        contactAdapter.notifyDataSetChanged();

            }

            } else if (newText.length() == 0) {
                if (contactAdapter != null) {
                    contactAdapter.swapItems(activity, nameList, numList, colorList, listHashMap, "");
                    l1.setAdapter(contactAdapter);
                    contactAdapter.notifyDataSetChanged();
                    if (l1 != null)
                        l1.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            int previousGroup = -1;

                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                                if (l1.isGroupExpanded(groupPosition)) {
                                    l1.collapseGroupWithAnimation(groupPosition);
                                    previousGroup = -1;
                                } else {
                                    l1.expandGroupWithAnimation(groupPosition);
                                    if (previousGroup != -1) {
                                        l1.collapseGroupWithAnimation(previousGroup);
                                    }
                                    previousGroup = groupPosition;
                                }

                                return true;
                            }
                        });

                    l1.setFocusableInTouchMode(true);
                    l1.requestFocus();

                }
            }
        }
    }
