package com.maksof.linkapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class  groupContactAdapter extends BaseAdapter {
    ArrayList<JSONObject> listOfContact;
    Activity act;
    public  groupContactAdapter(ArrayList<JSONObject> listOfContact,Activity activity){
        this.listOfContact =listOfContact;
        this.act = activity;
    }

    @Override
    public int getCount() {
        return this.listOfContact.size();
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
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.contacts_mirror_item, parent, false);
        }
        return convertView;
    }
}
