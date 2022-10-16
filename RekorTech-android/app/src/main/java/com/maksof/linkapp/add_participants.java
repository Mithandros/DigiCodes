package com.maksof.linkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class add_participants extends AppCompatActivity {
    ListView contacts;
    ArrayList<JSONObject> contact = new ArrayList<>();
    ArrayList<contactModel> contactModal = new ArrayList<>();
    boolean match = false;
    ArrayList<JSONObject> addedMembers = new ArrayList<>();
    String groupId;
    ArrayList<String> name = new ArrayList();
    ArrayList<String> num = new ArrayList<>();
    ArrayList<String> color =  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participants);
        groupId = getIntent().getStringExtra("groupId");
        contacts = (ListView)findViewById(R.id.contactList);
        if(PreferenceUtils.get("adapterInitialized",add_participants.this)!=null)
        {
            if(PreferenceUtils.get("adapterInitialized",add_participants.this).equals("true"))
            {
                if(PreferenceUtils.getGroupString("nameList",add_participants.this)!=null)
                {
                    name = PreferenceUtils.getGroupString("nameList",add_participants.this);
                    num = PreferenceUtils.getGroupString("numList",add_participants.this);
                    color = PreferenceUtils.getGroupString("colorList",add_participants.this);

                }
            }
        }
        for(int i=0;i<name.size();i++){
            try {
                JSONObject jobject = new JSONObject();
                jobject.put("name",name.get(i));
                jobject.put("number",num.get(i));
                jobject.put("color",color.get(i));
                contact.add(jobject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<contact.size();i++){
            contactModal.add(new contactModel(contact.get(i),false));
        }
        createGroupContactsAdapter createGroupContactsAdapter = new createGroupContactsAdapter(add_participants.this,contactModal);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contacts.setAdapter(createGroupContactsAdapter);
            }
        });

        ImageButton participantsBack = (ImageButton) findViewById(R.id.participantsBack);
        participantsBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return true;
            }
        });
        ImageView createG = (ImageView)findViewById(R.id.createG);
        createG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            try {
                                JSONArray groupMembers = new JSONArray();
                                for (int i=0;i<addedMembers.size();i++){
                                    JSONObject memberRecord = new JSONObject();
                                    memberRecord.put("contactName",addedMembers.get(i).getString("name"));
                                    memberRecord.put("phoneNumber",addedMembers.get(i).getString("number"));
                                    groupMembers.put(memberRecord);
                                }

                                JSONObject job = new JSONObject();
                                        job.put("groupId",groupId);
                                        job.put("contactsList",groupMembers);
                                        job.put("directoryProfile","GROUP_CONTACT");
//                                        commonClass.apiIntegration(add_participants.this, "groupcontacts/addcontactstogroup", job.toString(), PreferenceUtils.getToken(add_participants.this), "addMembers");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
        });
    }
    public  class createGroupContactsAdapter extends BaseAdapter {

        Activity act;
        ArrayList<contactModel> gContacts;
        ;

        public createGroupContactsAdapter(Activity activity, ArrayList<contactModel> as) {
            this.act = activity;
            this.gContacts = as;
        }

        @Override
        public int getCount() {
            return gContacts.size();
        }

        @Override
        public JSONObject getItem(int position) {
            return this.gContacts.get(position).getContact();

        }
        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_contact_selection, null);

            TextView gFLetter = (TextView) convertView.findViewById(R.id.gFLetter);
            TextView gContactName = (TextView) convertView.findViewById(R.id.gContactName);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkb);
            TextView gContactNum = (TextView) convertView.findViewById(R.id.gContactNum);
            JSONObject job = getItem(position);
            try {
                gContactName.setText(job.getString("name"));
                gFLetter.setText(job.getString("name").charAt(0) + "");
                gContactNum.setText(job.getString("number"));
                final GradientDrawable gradientDrawable = (GradientDrawable) gFLetter.getBackground().mutate();
                gradientDrawable.setColor(Color.parseColor(job.getString("color")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (this.gContacts.get(position).isSel()) {
                checkBox.setChecked(true);
            } else checkBox.setChecked(false);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    match = false;
                    if(checkBox.isChecked())
                        checkBox.setChecked(false);
                    else checkBox.setChecked(true);
                    gContacts.get(position).setSel(checkBox.isChecked());
                    notifyDataSetChanged();
                    if (checkBox.isChecked()) {
                        match =false;
                        for(int k=0;k<addedMembers.size();k++){
                            if (addedMembers.get(k).equals(getItem(position)))
                            {
                                match =true;
                                break;
                            }
                        }
                        if(match==false)
                            addedMembers.add(getItem(position));
                    }
                    else{
                        for(int k=0;k<addedMembers.size();k++){
                            if (addedMembers.get(k).equals(getItem(position)))
                                addedMembers.remove(k);}}
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // try {
                    match = false;
                    gContacts.get(position).setSel(isChecked);
                    notifyDataSetChanged();
                    if (checkBox.isChecked()) {
                        for(int k=0;k<addedMembers.size();k++){
                            if (addedMembers.get(k).equals(getItem(position)))
                            {    match =true;
                                break;}
                        }
                        if(match==false)
                            addedMembers.add(getItem(position));
                    }
                    else{
                        for(int k=0;k<addedMembers.size();k++){
                            if (addedMembers.get(k).equals(getItem(position)))
                                addedMembers.remove(k);}}
                }
            });

            return convertView;
        }
    }
}
