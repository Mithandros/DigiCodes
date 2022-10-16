package com.maksof.linkapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class group_member_fragment extends Fragment {

    public static AnimatedExpandableListView groupContactsList;
    public static GroupExpandableListAdapter groupContactListAdapter;
    public static List<String> listDataHeader;
    public static List<JSONObject> listGroupContacts;
    public static HashMap<String,List<String>> listHashMap;
    public static ArrayList<String> colorList = new ArrayList<>();
    public static String groupID;
    static TextView groupName,noGroup;
    Button addMember;
    public group_member_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ImageButton triggerSearchBar = getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        listGroupContacts = PreferenceUtils.getGroupContactsJSON("GroupMembers",getActivity());
        super.onPause();
    }

    @Override
    public void onResume() {
        ImageButton triggerSearchBar = getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.GONE);
        listGroupContacts = PreferenceUtils.getGroupContactsJSON("GroupMembers",getActivity());
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_group_contacts_member, container, false);
        ImageButton back = (ImageButton)getActivity().findViewById(R.id.groupContactBackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        initData();
        groupID = getActivity().getIntent().getStringExtra("groupId");
        groupName = (TextView)getActivity().findViewById(R.id.groupName);
        groupName.setText(listGroupContacts.size()+" Participants");
        addMember = (Button)v.findViewById(R.id.addMembersToGroup);
        noGroup = (TextView)  v.findViewById(R.id.noGroup);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addM = new Intent(getContext(),add_participants.class);
                addM.putExtra("groupId",groupID);
                startActivity(addM);
            }
        });

        groupContactsList = (AnimatedExpandableListView)v.findViewById(R.id.groupContactList);
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
                    groupContactsList.expandGroupWithAnimation(groupPosition);
                    if(previousGroup!=-1){
                        groupContactsList.collapseGroupWithAnimation(previousGroup); }
                    previousGroup=groupPosition;
                }
                return true;
            }
        });
        return v;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();
        listGroupContacts = PreferenceUtils.getGroupContactsJSON("GroupMembers",getActivity());
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

    public static void updateContactList(List<JSONObject> members, Activity activity){
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

                if(groupContactsList!=null)
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(groupName!=null)
                            groupName.setText(listGroupContacts.size()+" Participants");
                            groupContactListAdapter = new GroupExpandableListAdapter(groupID,activity,listGroupContacts,colorList,listHashMap, activity);
                            groupContactsList.setAdapter(groupContactListAdapter);
                            groupContactListAdapter.notifyDataSetChanged();
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
                                        groupContactsList.expandGroupWithAnimation(groupPosition);
                                        if(previousGroup!=-1){
                                            groupContactsList.collapseGroupWithAnimation(previousGroup); }
                                        previousGroup=groupPosition;
                                    }
                                    return true;
                                }
                            });
                        }
                    });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
