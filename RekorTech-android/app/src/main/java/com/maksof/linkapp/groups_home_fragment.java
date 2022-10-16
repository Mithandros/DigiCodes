package com.maksof.linkapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class groups_home_fragment extends Fragment   {


    public static ArrayList<JSONObject> GroupNameJSON = new ArrayList<>();
    public static ArrayList<String> colorList = new ArrayList<>();
    static GroupsAdapter groupsAdapter;

    static String gCreatedBy,idPosition;
        public static ListView reviewList=null;
        public static TextView noGroups = null;
        public  static SwipeRefreshLayout pullToRefresh;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupNameJSON = PreferenceUtils.getGroupNamesJSON("groupNamesJson",getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View groupsHomeView = inflater.inflate(R.layout.groups_home_fragment, null);
//        commonClass.apiIntegration(getContext(), "group/getgroupsofuser", "", PreferenceUtils.getToken(getContext()), "getGroupOfUser");
        ImageButton triggerSearchBar = (ImageButton)getActivity().findViewById(R.id.triggerSearchBar);
        noGroups = (TextView)groupsHomeView.findViewById(R.id.noGroup);
        triggerSearchBar.setVisibility(View.VISIBLE);
        reviewList = (ListView) groupsHomeView.findViewById(R.id.groupsList);
        SwipeRefreshLayout pullToRefresh = groupsHomeView.findViewById(R.id.phoneHome2);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(PreferenceUtils.getToken(getContext())!=null && !"".equals(PreferenceUtils.getToken(getContext()))) {
//                    commonClass.apiIntegration(getContext(), "group/getgroupsofuser", "", PreferenceUtils.getToken(getContext()), "getGroupOfUser");
                }
                pullToRefresh.setRefreshing(false);

            }
        });
        reviewList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (reviewList.getChildAt(0) != null) {
                    pullToRefresh.setEnabled(reviewList.getFirstVisiblePosition() == 0 && reviewList.getChildAt(0).getTop() == 0);
                    pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                             if(PreferenceUtils.getToken(getContext())!=null && !"".equals(PreferenceUtils.getToken(getContext()))) {
//                                 commonClass.apiIntegration(getContext(), "group/getgroupsofuser", "", PreferenceUtils.getToken(getContext()), "getGroupOfUser");
                             }
                             pullToRefresh.setRefreshing(false);

                        }
                    });

                }
            }
        });
        GroupNameJSON = PreferenceUtils.getGroupNamesJSON("groupNamesJson",getActivity());


        if(GroupNameJSON != null){
            for (int i=0;i<GroupNameJSON.size();i++){
                colorList.add(commonClass.randomColors());
            }
        }
        if (GroupNameJSON != null) {
            if(GroupNameJSON.size()==0){
                reviewList.setVisibility(View.GONE);
                noGroups.setVisibility(View.VISIBLE);
            }else {
                reviewList.setVisibility(View.VISIBLE);
                noGroups.setVisibility(View.GONE);
            }
            groupsAdapter = new GroupsAdapter(getActivity(),GroupNameJSON,colorList);
            if(reviewList!=null)
            reviewList.setAdapter(groupsAdapter);
        }else if(GroupNameJSON==null){
                reviewList.setVisibility(View.GONE);
                noGroups.setVisibility(View.VISIBLE);

        }

        Button createGroup = groupsHomeView.findViewById(R.id.createGroup);
        createGroup.setOnClickListener(v -> {
            Intent create_group = new Intent(getActivity(), create_group.class);
            startActivity(create_group);
        });
        return groupsHomeView;

    }
    @Override
    public void onPause() {
        PreferenceUtils.save("pause","group_home",getContext());
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceUtils.save("resume","group_home",getContext());
//        commonClass.apiIntegration(getContext(), "group/getgroupsofuser", "", PreferenceUtils.getToken(getContext()), "getGroupOfUser");
        ImageButton triggerSearchBar = (ImageButton)getActivity().findViewById(R.id.triggerSearchBar);
        triggerSearchBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceUtils.save("resume","group_home",getContext());
//        commonClass.apiIntegration(getContext(), "group/getgroupsofuser", "", PreferenceUtils.getToken(getContext()), "getGroupOfUser");
    }
    static class  GroupsAdapter extends BaseAdapter {

        private ArrayList<JSONObject> namesOfGroup = new ArrayList<JSONObject>();
        private  ArrayList<String> colorList;
        private Activity act;
        public GroupsAdapter(Activity activity,ArrayList<JSONObject> listDataHeader,ArrayList<String> colorList) {
            this.namesOfGroup = listDataHeader;
            this.act = activity;
            this.colorList = colorList;
        }

        @Override
        public int getCount() {
            if(namesOfGroup==null)
                return 0;
            else return namesOfGroup.size();
        }

        @Override
        public JSONObject getItem(int position) {
            return this.namesOfGroup.get(position);
        }

        public String getColor(int position) {
            if(this.colorList.size()==1||this.colorList.size()<=position)
                return "#000000";
            return this.colorList.get(position);
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = this.act.getLayoutInflater().inflate(R.layout.home_grouplist_item, null);
            RelativeLayout goToGroup = (RelativeLayout) convertView.findViewById(R.id.toGroup);
            TextView mainLetter = (TextView) convertView.findViewById(R.id.groupMainLetter);
            TextView groupName = (TextView) convertView.findViewById(R.id.groupName);
            ImageView deleteGroup = (ImageView) convertView.findViewById(R.id.deleteGroup);
            ImageView updateGroup = (ImageView) convertView.findViewById(R.id.editGroup);
            JSONObject obj = (JSONObject) getItem(position);

            if (getItem(position) != null) {
                String name = null;
                try {
                    name = Character.toUpperCase(obj.getString("name").charAt(0)) + obj.getString("name").substring(1, obj.getString("name").length());
                    idPosition = obj.getString("id");
                    gCreatedBy = obj.getString("createdBy");
                    groupName.setText(name);
                    String reg="[a-zA-Z0-9]";
                    if(((name.charAt(0)+"").matches(reg)))
                        mainLetter.setText(Character.toUpperCase(name.charAt(0)) + "");
                    else mainLetter.setText(Constants.firstChar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            goToGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        JSONObject obj = (JSONObject) getItem(position);
                        idPosition = obj.getString("id");
                        gCreatedBy = obj.getString("createdBy");
                        String id = obj.getString("id"),name = Character.toUpperCase(obj.getString("name").charAt(0)) + obj.getString("name").substring(1, obj.getString("name").length());
                        PreferenceUtils.SaveGroupName(name,act);
                        PreferenceUtils.save(gCreatedBy,"createdBy",act);
                        JSONObject jsonObject= new JSONObject();
                        jsonObject.put("groupId",id);
//                        commonClass.apiIntegration(act, "groupcontacts/getgroupcontacts", jsonObject.toString(), PreferenceUtils.getToken(act), "getGroupContacts");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            final GradientDrawable gradientDrawable = (GradientDrawable) mainLetter.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor(getColor(position)));
            return convertView;
        }
        public void swapItems(ArrayList<JSONObject> items) {
            this.namesOfGroup = items;
        }
    }
    public static  void onResume(Activity act) {
        ArrayList<String> clrList = new ArrayList<>();
             ArrayList<JSONObject> list= new ArrayList<>();
             if(PreferenceUtils.getGroupNamesJSON("groupNamesJson",act)!=null)
                 list = PreferenceUtils.getGroupNamesJSON("groupNamesJson",act);

            for (int i=0;i<list.size();i++){
                clrList.add(commonClass.randomColors());
            }
        groupsAdapter = new GroupsAdapter(act,list,clrList);
                if(reviewList!=null)
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (PreferenceUtils.getGroupNamesJSON("groupNamesJson", act) != null) {
                                    if(PreferenceUtils.getGroupNamesJSON("groupNamesJson", act).size()==0){
                                        reviewList.setVisibility(View.GONE);
                                        noGroups.setVisibility(View.VISIBLE);
                                    }else {
                                        reviewList.setVisibility(View.VISIBLE);
                                        noGroups.setVisibility(View.GONE);
                                    }
                                }else if(PreferenceUtils.getGroupNamesJSON("groupNamesJson", act) == null){
                                    reviewList.setVisibility(View.GONE);
                                    noGroups.setVisibility(View.VISIBLE);
                                }
                                reviewList.setAdapter(groupsAdapter);
                                groupsAdapter.notifyDataSetChanged();

                    }
                });

    }
    public static void search(final  String newText,Activity activity){
        activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinkedHashMap listHashMap1 = new LinkedHashMap<>();
                    ArrayList<JSONObject> listLabSearchContacts = new ArrayList<>();
                    if (GroupNameJSON != null)
                        for (int i = 0; i < GroupNameJSON.size(); i++) {
                            try {
                                if (((GroupNameJSON.get(i).getString("name").toLowerCase().contains(newText.toLowerCase()))) || (GroupNameJSON.get(i).getString("contactName").toLowerCase().contains(newText.toLowerCase())))
                                    listLabSearchContacts.add(GroupNameJSON.get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    if (listLabSearchContacts != null) {
                        try {
                            for (int i = 0; i < listLabSearchContacts.size(); i++) {
                                listHashMap1.put(listLabSearchContacts.get(i).getString("contactName"), null);
                                colorList.add(commonClass.randomColors());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        groupsAdapter = new GroupsAdapter(activity, listLabSearchContacts, colorList);
                        reviewList.setAdapter(groupsAdapter);
                        groupsAdapter.notifyDataSetChanged();
                    }
                }
            });}
}



