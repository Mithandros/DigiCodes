package com.rekortech.android;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<contactModel> notificationList;
    private ArrayList<String> colorList;


    NotificationAdapter(Activity activity,   ArrayList<contactModel> notificationList, ArrayList<String> colorList){
        this.activity = activity;
        this.notificationList = notificationList;
        this.colorList = colorList;
    }

    @Override
    public int getCount() {
        if(this.notificationList!=null)
        return this.notificationList.size();
    else return 0;}
    public String getColor(int position) {
        return this.colorList.get(position);
    }
    @Override
    public Object getItem(int position) {
        return this.notificationList.get(position).getContact();
    }

    public String getId(int position) {
        try {
            return this.notificationList.get(position).getContact().getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTime(int position) {
        try {
            return this.notificationList.get(position).getContact().getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.notification_view,null);
        TextView uRName = (TextView)convertView.findViewById(R.id.comName);
        TextView time = (TextView)convertView.findViewById(R.id.time);
        TextView uRFLetter = (TextView)convertView.findViewById(R.id.comFLetter);
        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        LinearLayout checked = (LinearLayout)convertView.findViewById(R.id.checked);
        LinearLayout notificationBackround = (LinearLayout)convertView.findViewById(R.id.notification_background);

        JSONObject notif = (JSONObject)getItem(position);
        try {
            uRName.setText(notif.getString("notification"));

            String reg="[a-zA-Z0-9]";
            if((Character.toUpperCase(Character.toUpperCase(notif.getString("notification").charAt(0)))+"").matches(reg))
                uRFLetter.setText(Character.toUpperCase(notif.getString("notification").charAt(0))+"");
            else uRFLetter.setText(Constants.firstChar);
            if(notif.getString("is_read").equals("1")){
                notificationBackround.setBackgroundColor(Color.parseColor("#ffffff"));
                checkBox.setChecked(true);
                checkBox.setVisibility(View.GONE);
            }else {
                notificationBackround.setBackgroundColor(Color.parseColor("#D3D3D3"));
                checkBox.setChecked(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        time.setText(getTime(position));
        if (this.notificationList.get(position).isSel()) {
            {
                checkBox.setChecked(true);
                notificationBackround.setBackgroundColor(Color.parseColor("#ffffff"));
                checkBox.setVisibility(View.GONE);
            }
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notificationList.get(position).setSel(isChecked);
                if(isChecked){
                    checkBox.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("notificationId",getId(position));
                        notificationBackround.setBackgroundColor(Color.parseColor("#ffffff"));
//                        commonClass.apiIntegration(activity, "notification/markreadnotification", jsonObject.toString(),PreferenceUtils.getToken(activity), "markReadNotification");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
            }
        });

        if(uRFLetter!=null) {
            final GradientDrawable gradientDrawable = (GradientDrawable) uRFLetter.getBackground().mutate();
            gradientDrawable.setColor(Color.parseColor((String) getColor(position)));
        }
        return convertView;
    }
}
