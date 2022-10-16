package com.maksof.linkapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class phoneContcts extends AsyncTask<String, Void, String> {
    Activity activity;
    String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;
    String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);
    ArrayList<String> nameList =new ArrayList<>();
    ArrayList<String> numList =new ArrayList<>();
    ArrayList<String> colorList = new ArrayList<>();
    HashMap<String, List<String>> listHashMap = new HashMap<>();
    phoneContcts( Activity activity){
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... strings) {
        Cursor cursor = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ORDER);
        if (cursor != null && cursor.getCount() > 0) {
            int j=0;
            while (cursor!=null && cursor.moveToNext()) {
                if (!(nameList.contains(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))))) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor pCur = activity.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        if (pCur != null && pCur.moveToNext()) {
                            nameList.add(name);
                            String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            numList.add(number);
                            colorList.add(commonClass.randomColors());
                            listHashMap.put(name, null);
                            pCur.close();
                        }
                }}
            cursor.close();
            JSONArray numbersLocal = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            try {
                for( int i=0; i< numList.size();i++){
                    if(!"".equals(numList.get(i)))
                        numbersLocal.put(numList.get(i));}
                jsonObject.put("phoneNumber",numbersLocal);
//                commonClass.apiIntegration(activity, "labcontact/getlabcontact", jsonObject.toString(), PreferenceUtils.getToken(activity),"getLabContacts");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
return null;
    }
}
