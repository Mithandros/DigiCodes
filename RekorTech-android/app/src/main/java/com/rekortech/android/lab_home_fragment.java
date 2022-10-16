package com.rekortech.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.snackbar.Snackbar;
import com.rekortech.android.data.DoctorsProfile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class lab_home_fragment extends Fragment {

    EditText bsnsName, bsnsOccup, bsnsFeed,bsnsNum, bsnsWeb, bsnsEmail;
    static ExpandedHeightGridView bsnsImageGridView ;

    AutocompleteSupportFragment autocompleteFragment;
    Button uploadImage;
    TextView ratingText;
    RatingBar bsnsRatingBar;
    String address1 = "";
    String address;
    static AlertDialog dialog;
    public static AnimatedExpandableListView labListView;
    public static ExpandableListAdapter labListAdapter;
    public static HashMap<String,List<String>> listHashMap;
    public static   ArrayList<DoctorsProfile> listLabContacts;
    public static TextView noLabs;

    View bView;
    public static ArrayList<String> colorList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View labView =  inflater.inflate(R.layout.lab_home_fragment, container,false);
        labListView = (AnimatedExpandableListView)labView.findViewById(R.id.labExpandList);
        final SwipeRefreshLayout pullToRefresh = labView.findViewById(R.id.phoneHome2);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                APIServices.updateLab(getActivity());
                pullToRefresh.setRefreshing(false);
            }
        });
        labListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (labListView.getChildAt(0) != null) {
                    pullToRefresh.setEnabled(labListView.getFirstVisiblePosition() == 0 && labListView.getChildAt(0).getTop() == 0);
                    pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            APIServices.updateLab(getActivity());
                            pullToRefresh.setRefreshing(false);
                        }
                    });

                }
            }
        });
        noLabs = (TextView) labView.findViewById(R.id.noLabs);
        Places.initialize(getContext(), "AIzaSyCYlFhfg9LErOaVm_JivuovEcyh2CpuIN0");
        PlacesClient placesClient = Places.createClient(getContext());
        initData(getActivity());

        labListView.setFocusableInTouchMode(true);
        labListView.requestFocus();
        labListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int previousGroup=-1;
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                if (labListView.isGroupExpanded(groupPosition)) {
                    labListView.collapseGroupWithAnimation(groupPosition);
                    previousGroup=-1;
                } else {
                    labListView.expandGroupWithAnimation(groupPosition);
                    if(previousGroup!=-1){
                        labListView.collapseGroupWithAnimation(previousGroup); }
                    previousGroup=groupPosition;
                }
                return true;
            }
        });



        return labView;
    }

    public static void initData(final Activity activity) {
        final ArrayList<String> clrList = new ArrayList<>();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listLabContacts = new ArrayList<>();
                listHashMap = new LinkedHashMap<>();
                listLabContacts = new ArrayList<>(); //PreferenceUtils.getGroupContactsJSON("getLabContacts", activity);
                for (int i = 0; i < 20; i++)
                    listLabContacts.add(new DoctorsProfile("Doctor name "+i , "03112533120"));

                if (listLabContacts != null) {
                     for (int i = 0; i < listLabContacts.size(); i++) {
                            listHashMap.put(listLabContacts.get(i).getName(), null);
                            clrList.add(commonClass.randomColors());
                        }
                        if(listLabContacts.size()==0){
                            labListView.setVisibility(View.GONE);
                            noLabs.setVisibility(View.VISIBLE);
                        }else {
                            labListView.setVisibility(View.VISIBLE);
                            noLabs.setVisibility(View.GONE);
                        }
                    if(labListAdapter==null)
                        labListAdapter = new ExpandableListAdapter(activity,listLabContacts,clrList,listHashMap, activity,"");
                    else
                    {
                        labListAdapter.swapItems((Context)activity,clrList,listLabContacts,listHashMap, activity,"");
                    }
                    labListView.setAdapter(labListAdapter);
                    labListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private View parentLayout;
    int REQUEST_READ_CONTACTS = 123;
    private boolean contactPermissionNotGiven,flag=false;
    public boolean askContactsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            Snackbar.make(parentLayout, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            if (getActivity() == null) {
                                return;
                            }
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            getActivity().startActivity(intent);
                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);

                        }
                    }).show();
            return true;
        } else if (contactPermissionNotGiven) {
            Toast.makeText(getContext(),"Contact Access Permission Not Given",Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            contactPermissionNotGiven = true;

        }
        return false;
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
    public void toast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG);
    }

    static int inc;
    public static void images(final Activity activity){
        final ArrayList<JSONObject> imgs = PreferenceUtils.getGroupNamesJSON("imgURLSlist",activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap resized;
                if(bsnsImageGridView!=null)
                {
                    for( inc = 0 ;inc<imgs.size();inc++){
                        ArrayList<String> imgurl = new ArrayList<>();
                            for( inc = 0 ;inc<imgs.size();inc++) {
                                imgurl.add(com.rekortech.android.commonClass.urlBitmap(activity, inc));
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
    static class ImageAdapter extends BaseAdapter {
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
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();
                    GlideApp.with(activity)
                            .load(com.rekortech.android.commonClass.imgUrl+url)
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
                    commonClass.updateConstantsImageUrl(imgs);
                    PreferenceUtils.saveGroupString(imgs,"imgString",activity);
                    imgViewDirec.setVisibility(View.GONE);
                    closeBtn.setVisibility(View.GONE);
                }
            });
            return convertView;
        }

    }
    public  void focusChange(EditText et){
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ImageButton triggerSearchBar = (ImageButton)getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    public static void search(Activity activity,final String newText){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinkedHashMap listHashMap1 = new LinkedHashMap<>();
                    ArrayList<DoctorsProfile> listLabSearchContacts = new ArrayList<>();
                    listLabContacts = new ArrayList<>();//PreferenceUtils.getGroupContactsJSON("getLabContacts", activity);
                    initData(activity);

                    if (listLabContacts != null)
                        for (int i = 0; i < listLabContacts.size(); i++) {
                            if (((listLabContacts.get(i).getName().toLowerCase().contains(newText.toLowerCase()))) || (listLabContacts.get(i).getName().toLowerCase().contains(newText.toLowerCase())))
                                listLabSearchContacts.add(listLabContacts.get(i));

                        }

                    if (listLabSearchContacts != null) {
                        for (int i = 0; i < listLabSearchContacts.size(); i++) {
                            listHashMap1.put(listLabSearchContacts.get(i).getName(), null);
                            colorList.add(commonClass.randomColors());
                        }

                        ExpandableListAdapter labListAdapter1 = new ExpandableListAdapter(activity, listLabSearchContacts, colorList, listHashMap1, activity, newText);
                        labListView.setAdapter(labListAdapter1);
                        labListAdapter1.notifyDataSetChanged();
                        labListView.setFocusableInTouchMode(true);
                        labListView.requestFocus();
                        labListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            int previousGroup=-1;
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                                if (labListView.isGroupExpanded(groupPosition)) {
                                    labListView.collapseGroupWithAnimation(groupPosition);
                                    previousGroup=-1;
                                } else {
                                    labListView.expandGroupWithAnimation(groupPosition);
                                    if(previousGroup!=-1){
                                        labListView.collapseGroupWithAnimation(previousGroup); }
                                    previousGroup=groupPosition;
                                }
                                return true;
                            }
                        });
                    }
                }
            });
    }

}
