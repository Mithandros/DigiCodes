
package com.rekortech.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class create_group extends AppCompatActivity {
    ListView contacts;

    ArrayList<JSONObject> contact = new ArrayList<>();
    ArrayList<contactModel> contactModal = new ArrayList<>();
    boolean match = false;

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> num = new ArrayList<>();
    ArrayList<String> color = new ArrayList<>();

    ArrayList<JSONObject>  addedMembers = new ArrayList<>();

    private final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

    private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ImageButton dir1BackBtn = (ImageButton)findViewById(R.id.dir1BackBtn);
        dir1BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        contacts = (ListView)findViewById(R.id.contactList);
                if(PreferenceUtils.get("adapterInitialized",create_group.this)!=null)
                {
                    if(PreferenceUtils.get("adapterInitialized",create_group.this).equals("true"))
                    {
                        if(PreferenceUtils.getGroupString("nameList",create_group.this)!=null)
                        {
                            name = PreferenceUtils.getGroupString("nameList",create_group.this);
                            num = PreferenceUtils.getGroupString("numList",create_group.this);
                            color = PreferenceUtils.getGroupString("colorList",create_group.this);

                        }}}
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
                    createGroupContactsAdapter createGroupContactsAdapter = new createGroupContactsAdapter(create_group.this,contactModal);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contacts.setAdapter(createGroupContactsAdapter);
                        }
                    });




        ImageView createG = (ImageView)findViewById(R.id.createG);
        createG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final commonClass commonClass = new commonClass();
                AlertDialog.Builder sBuilder = new AlertDialog.Builder(create_group.this);
                View createGroupView = getLayoutInflater().inflate(R.layout.create_group_modal, null);

                final EditText group_name = (EditText) createGroupView.findViewById(R.id.group_name);
                group_name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                Button close = (Button) createGroupView.findViewById(R.id.closeGroupModal);
                Button create = (Button) createGroupView.findViewById(R.id.createGroup);

                sBuilder.setView(createGroupView);
                final AlertDialog dialog = sBuilder.create();
                if(!((Activity) create_group.this).isFinishing())
                {
                    dialog.show();
                    //show dialog
                }


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String groupName = group_name.getText().toString(), data = "";
                        boolean flag = true;
                        String nameReg="[a-zA-Z][a-z A-Z.\\d]{1,25}$";
                        if((groupName.equals(""))){
                            group_name.setError("Enter group's name");
                            flag = false;
                        } else if(!group_name.getText().toString().matches(nameReg)){
                            group_name.setError("Group name cannot contain special characters");
                            flag = false;
                        }
                        if (flag == true) {
                            try {
                                JSONArray groupMembers = new JSONArray();
                                for (int i=0;i<addedMembers.size();i++){
                                    JSONObject memberRecord = new JSONObject();
                                    memberRecord.put("contactName",addedMembers.get(i).getString("name"));
                                    memberRecord.put("phoneNumber",addedMembers.get(i).getString("number"));
                                    groupMembers.put(memberRecord);
                                }
                                JSONObject newGroup = new JSONObject();
                                newGroup.put("groupName",groupName);
                                newGroup.put("contactsList",groupMembers);
//                                commonClass.apiIntegration(create_group.this, "group/creategroup", newGroup.toString(), PreferenceUtils.getToken(create_group.this), "createGroup");
                                //onBackPressed();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

