package com.maksof.linkapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class group_contacts_fragment extends Fragment {
    public static AnimatedExpandableListView groupContactsList;
    public static GroupExpandableListAdapter groupContactListAdapter;
    public static List<String> listDataHeader;
    public static List<JSONObject> listGroupContacts,listSearchContact;
    public static HashMap<String,List<String>> listHashMap,listSearchHash;
    public static ArrayList<String> colorList = new ArrayList<>();
    public static ArrayList<String> colorSearchList = new ArrayList<>();
    public static String groupID;
    static AlertDialog dialog;
    static TextView groupName;
    public static TextView noGroup;
    int lastExpandedPosition = -1;

    private OnFragmentInteractionListener mListener;
    public group_contacts_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ImageButton triggerSearchBar = getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Places.initialize(getContext(), "AIzaSyCYlFhfg9LErOaVm_JivuovEcyh2CpuIN0");
        PlacesClient placesClient = Places.createClient(getContext());
        View v= inflater.inflate(R.layout.fragment_group_contacts, container, false);
        EditText mainSearchBox = getActivity().findViewById(R.id.mainSearchBox);

        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int l, int i2, int i3) {
                listSearchContact =new ArrayList<>();
                listSearchHash = new HashMap<>();
                colorSearchList = new ArrayList<>();
                if(charSequence.length()>0){
                for(int j = 0; j< listGroupContacts.size();j++){
                        Pattern pattern = Pattern.compile(charSequence.toString().toLowerCase()+".*");
                    Matcher matcher = null;
                    try {
                        matcher = pattern.matcher(listGroupContacts.get(j).getString("contactName").toLowerCase());
                        if(matcher.matches()) {
                            listSearchContact.add(listGroupContacts.get(j));
                            colorSearchList.add(commonClass.randomColors());
                            listSearchHash.put(listGroupContacts.get(j).getString("contactName"), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                groupContactListAdapter = new GroupExpandableListAdapter(groupID,getContext(),listSearchContact,colorSearchList,listSearchHash, getActivity());
                groupContactsList.setAdapter(groupContactListAdapter);
                }
                else {
                    groupContactListAdapter = new GroupExpandableListAdapter(groupID,getContext(),listGroupContacts,colorList,listHashMap, getActivity());
                    groupContactsList.setAdapter(groupContactListAdapter);
                    animate();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // check Fields For Empty Values
            }
        };

        groupName = (TextView)getActivity().findViewById(R.id.groupName);
        noGroup = (TextView)  v.findViewById(R.id.noGroup);
        groupID = getActivity().getIntent().getStringExtra("groupId");
        if(PreferenceUtils.getGroupName(getContext())!=null)
            groupName.setText(PreferenceUtils.getGroupName(getContext()));
        ImageButton back = (ImageButton)getActivity().findViewById(R.id.groupContactBackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });
        groupContactsList = (AnimatedExpandableListView)v.findViewById(R.id.groupContactList);
        initData();
        mainSearchBox.addTextChangedListener(mTextWatcher);
        if(listGroupContacts!=null){
            if(listGroupContacts.size()==0){
                noGroup.setVisibility(View.VISIBLE);
                groupContactsList.setVisibility(View.GONE);

            }else {
                noGroup.setVisibility(View.GONE);
                groupContactsList.setVisibility(View.VISIBLE);
            }}else {
            noGroup.setVisibility(View.VISIBLE);
            groupContactsList.setVisibility(View.GONE);
        }
        groupContactListAdapter = new GroupExpandableListAdapter(groupID,getContext(),listGroupContacts,colorList,listHashMap, getActivity());
        groupContactsList.setAdapter(groupContactListAdapter);
        groupContactListAdapter.notifyDataSetChanged();
        groupContactsList.setFocusableInTouchMode(true);
        groupContactsList.requestFocus();
        groupContactsList.setOnGroupClickListener(new AnimatedExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (groupContactsList.isGroupExpanded(groupPosition)) {
                    groupContactsList.collapseGroupWithAnimation(groupPosition);
                    lastExpandedPosition=-1;
                } else {
                    groupContactsList.expandGroupWithAnimation(groupPosition);
                    if (lastExpandedPosition != -1
                            && groupPosition != lastExpandedPosition) {
                        groupContactsList.collapseGroupWithAnimation(lastExpandedPosition);
                    }
                    lastExpandedPosition = groupPosition;
                }
                return true;
            }
        });



        return v;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public static void animate(){
        groupContactsList.setFocusableInTouchMode(true);
        groupContactsList.requestFocus();
        groupContactsList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int previousGroup=-1;
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (groupContactsList.isGroupExpanded(groupPosition)) {
                    groupContactsList.collapseGroupWithAnimation(groupPosition);
                    previousGroup=-1;
                } else {
                    if(!groupContactsList.isGroupExpanded(groupPosition))
                        groupContactsList.expandGroupWithAnimation(groupPosition);
                    if(previousGroup!=groupPosition&&previousGroup!=-1){
                        groupContactsList.collapseGroupWithAnimation(previousGroup);
                    }
                    previousGroup=groupPosition;
                }

                return true;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void initData() {
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();
        listGroupContacts = PreferenceUtils.getGroupContactsJSON("GroupContacts",getActivity());
        if(listGroupContacts!=null){
            try {
                for (int i=0; i<listGroupContacts.size();i++) {
                    listDataHeader.add(listGroupContacts.get(i).getString("contactName"));
                    listHashMap.put(listDataHeader.get(0), null);
                    colorList.add(commonClass.randomColors());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public static void closeDialog(Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageButton triggerSearchBar = getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.VISIBLE);
        if(groupName!=null)
            if(PreferenceUtils.getGroupName(getContext())!=null)
                groupName.setText(PreferenceUtils.getGroupName(getContext()));
    }
    public static void updateContactList(List<JSONObject> members, Activity activity){
        listGroupContacts = PreferenceUtils.getGroupContactsJSON("GroupContacts",activity);
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();
        listGroupContacts = members;
        if(listGroupContacts!=null){

            try {
                for (int i=0; i<listGroupContacts.size();i++) {
                    listDataHeader.add(listGroupContacts.get(i).getString("contactName"));
                    listHashMap.put(listDataHeader.get(0), null);
                    colorList.add(commonClass.randomColors());
                }

                if(groupContactsList!=null){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(listGroupContacts.size()==0){
                                noGroup.setVisibility(View.VISIBLE);
                                groupContactsList.setVisibility(View.GONE);

                            }else {
                                noGroup.setVisibility(View.GONE);
                                groupContactsList.setVisibility(View.VISIBLE);
                            }
                            groupContactListAdapter = new GroupExpandableListAdapter(groupID,activity,listGroupContacts,colorList,listHashMap, activity);
                            groupContactsList.setAdapter(groupContactListAdapter);
                            groupContactListAdapter.notifyDataSetChanged();
                            animate();
                        }
                    });}
                else {
                    if(noGroup!=null)
                        noGroup.setVisibility(View.VISIBLE);
                    if(groupContactsList!=null)
                        groupContactsList.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closeSearch(Activity activity){
        LinearLayout mainNavHeader = activity.findViewById(R.id.mainNavHeader);
        RelativeLayout searchCon = (RelativeLayout)activity.findViewById(R.id.searchCon);
        searchCon.setVisibility(View.GONE);
        mainNavHeader.setVisibility(View.VISIBLE);
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        groupContactListAdapter = new GroupExpandableListAdapter(groupID,activity,listGroupContacts,colorList,listHashMap, activity);
        groupContactsList.setAdapter(groupContactListAdapter);
    }
    public static void updateGroupName(Activity activity,String name){
        if(groupName!=null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    groupName.setText(name);
                }
            });
        }
    }
}
