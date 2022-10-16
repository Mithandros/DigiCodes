package com.rekortech.android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GroupExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    commonClass commonClass =new commonClass();
    private Context context;
    static String contactNumber, number , newContactName, contactId ,newEmail, newOccupation;
    private List<JSONObject> listDataHeader;
    private HashMap<String,List<String>> listHashMap;
    private  ArrayList<String> colorList = new ArrayList<>();
    String groupId;
    private Activity activity;
    private static final String[] CALL_PERMISSION =
            {Manifest.permission.CALL_PHONE};


    public GroupExpandableListAdapter(String groupId,Context context, List<JSONObject> listDataHeader, ArrayList<String> colorList, HashMap<String, List<String>> listHashMap, Activity activity) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.activity = activity;
        this.colorList = colorList;
        this.groupId = groupId;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        try {
            return listDataHeader.get(groupPosition).getString("contactName");
        } catch (JSONException e) {
          //  e.printStackTrace();
        }
        return null;
    }
    public String getAddedBy(int groupPosition) {
        try {
            Object obj = listDataHeader.get(groupPosition).get("addedBy");
            try{
            LinkedTreeMap<Object, Object> t3 = (LinkedTreeMap) obj;
            Object getrow4 = t3.get("nameValuePairs");
            LinkedTreeMap<Object, Object> t4 = (LinkedTreeMap) getrow4;
            return t4.get("first_name")+" "+t4.get("last_name");}
            catch (ClassCastException ce){
                ce.printStackTrace();
                try {
                   JSONObject t3 = (JSONObject) obj;
                   return t3.getString("first_name")+" "+t3.getString("first_name");
                }catch (ClassCastException je){
                    try{
                        return listDataHeader.get(groupPosition).getString("addedBy");
                    }catch (Exception e){
                        e.printStackTrace();
                        return "";
                    }
                }

            }
        } catch (JSONException e) {
            try {
                return (String) listDataHeader.get(groupPosition).get("addedBy");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            //   e.printStackTrace();
            return null;
        }

    }
    public Object getEmail(int groupPosition) {
        try {
            return listDataHeader.get(groupPosition).getString("email");
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public String getColor(int groupPosition) {

            return this.colorList.get(groupPosition);
    }

    public Object getOccupation(int groupPosition) {
        try {
            return listDataHeader.get(groupPosition).getString("occupation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object getNumber(int groupPosition) {
        try {
            return listDataHeader.get(groupPosition).getString("phoneNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getContactId(int groupPosition) {
        try {
            return listDataHeader.get(groupPosition).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lab_list_item,null);
        }
        TextView groupContactName = (TextView)convertView.findViewById(R.id.labName);
        TextView groupContactMainLetter = (TextView)convertView.findViewById(R.id.labFLetter);
        TextView lab_user_occupation = (TextView)convertView.findViewById(R.id.lab_user_occupation);
        TextView lab_total_users_shared = (TextView)convertView.findViewById(R.id.lab_total_users_shared);
        TextView lab_last_person_recommended = (TextView)convertView.findViewById(R.id.lab_last_person_recommended);
        lab_total_users_shared.setVisibility(View.GONE);
        lab_last_person_recommended.setText(getAddedBy(groupPosition));

        String contactName = (String)getGroup(groupPosition);
        groupContactName.setText(contactName);
        lab_user_occupation.setText((String)getOccupation(groupPosition));

        contactNumber = (String)getNumber(groupPosition);
        String reg="[a-zA-Z0-9]";
        if(((contactName.charAt(0))+"").matches(reg))
            groupContactMainLetter.setText(Character.toUpperCase(contactName.charAt(0))+"");
        else groupContactMainLetter.setText(Constants.firstChar);
        final GradientDrawable gradientDrawable = (GradientDrawable) groupContactMainLetter.getBackground().mutate();
        gradientDrawable.setColor(Color.parseColor(getColor(groupPosition)));

        return convertView;
    }

    @Override
    public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lab_list_item_child, null);
        }
        number = (String)getNumber(groupPosition);
        newContactName = (String)getGroup(groupPosition);
        newEmail = (String)getEmail(groupPosition);
        newOccupation = (String)getOccupation(groupPosition);
        contactId = (String)getContactId(groupPosition);

        ImageButton info = (ImageButton)convertView.findViewById(R.id.labInfoBtn);
        ImageButton whatsApp = (ImageButton)convertView.findViewById(R.id.labWhatsappBtn);
        ImageButton call = (ImageButton)convertView.findViewById(R.id.labCallBtn);
        ImageButton sms = (ImageButton)convertView.findViewById(R.id.labSmsBtn);

        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.openWhatsApp(activity,number);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    commonClass.call(number,activity);
                } else {
                    callTask(activity);
                }
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonClass.sms(activity,number);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<JSONObject> obj = new ArrayList<>();
                JSONObject abc = (JSONObject)listDataHeader.get(groupPosition);
                obj.add(abc);
                PreferenceUtils.SaveGroupNamesJSON(obj,"labProfile",activity);
                Intent intent = new Intent(context, lab_user_details.class);
                intent.putExtra("screen","GroupProfile");
                intent.putExtra("groupId",groupId);
                context.startActivity(intent);
                 }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public static void dialCallGroup(Activity act){
        com.rekortech.android.commonClass.call(number,act);
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

    static boolean hasCallPermissions(Activity activity) {
        return EasyPermissions.hasPermissions(activity, CALL_PERMISSION);
    }

    @AfterPermissionGranted(124)
    public static void callTask(Activity activity) {
        if (hasCallPermissions(activity)) {

            com.rekortech.android.commonClass.call(number,activity);
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
    public void swapItems(ArrayList<JSONObject> items) {
        this.listDataHeader = items;
    }
}
