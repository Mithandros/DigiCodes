package com.rekortech.android;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class APIServices extends AsyncTask<String, Void, String> {
    String myUrl, myData, method, header, screen, groupID ,cls;
    MultipartEntity multipartEntity;
    Context context;
    Activity act;
    BufferedReader reader=null;
    ProgressDialog progressDialog;
    JSONObject jsonObject;

    ArrayList<String> groupNames=new ArrayList<>();
    APIServices(Context ctx, String url, MultipartEntity data, String header, String screen,String cls){
        act=(Activity)ctx;
        this.method="POST";
        this.myUrl=url;
        this.multipartEntity=data;
        this.context=ctx;
        this.header=header;
        this.screen=screen;
        this.cls = cls;

    }

    APIServices(Context ctx, String url, String data,String header,String screen){
        this.act= (Activity) ctx;
        this.method="POST";
        this.myUrl=url;
        this.myData=data;
        this.context=ctx;
        this.header=header;
        this.screen=screen;
    }

    APIServices(Context ctx, String url, String data,String header,String screen, String groupID){
        act=(Activity)ctx;
        this.method="POST";
        this.myUrl=url;
        this.myData=data;
        this.context=ctx;
        this.header=header;
        this.screen=screen;
        this.groupID = groupID;
    }

    APIServices(String method,Context ctx, String url, String header,String screen){
        act=(Activity)ctx;
        this.method=method;
        this.myUrl=url;
        this.context=ctx;
        this.header=header;
        this.screen=screen;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!screen.contains("directorySearch2"))
        if(screen.contains("recommendUserLab")||screen.contains("recommendUserGroup")||screen.contains("getLabContacts")||screen.contains("deleteGroup")||screen.contains("deletelabcontact")||screen.contains("deleteDirectory")||screen.contains("postReviews")||screen.contains("addReviews")||screen.contains("directorySearch")||screen.contains("deleteFeedback")||screen.contains("directorySecondScreenSearch")||screen.contains("deleteGroupContact")||screen.contains("recommendUserBusiness")|| screen.contains("createDirectory")||screen.contains("getLabProfile")||screen.contains("uploadProfile")||screen.contains("uploadImage")||screen.contains("signup")||screen.contains("updateGroup")||screen.contains("getGroupContacts")||screen.contains("getGroupContactsUpdated"))
        {
            String message = "Loading...";
            if(screen.contains("uploadImage"))
                message = "Uploading images...";
            if((screen.contains("directorySearch")&&PreferenceUtils.get("directorySearch", context)!=null)) {
                if (PreferenceUtils.get("directorySearch", context).equals("resume"))
                    progress(message);
            }
            else progress(message);
        }
    }

    public  void progress(String message){
        act.runOnUiThread(new Runnable() {
            public void run() {
                progressDialog=new ProgressDialog(context);
                if(!act.isFinishing())
                    try{
                        progressDialog.show();
                    }catch (WindowManager.BadTokenException e) {
                    }
                progressDialog.getWindow().setGravity(Gravity.CENTER);
                progressDialog.setMessage(message);
                if(screen.contains("getLabContacts"))
                {
                    View v = act.getLayoutInflater().inflate(R.layout.activity_progress, null);
                    progressDialog.setContentView(v);
                    progressDialog.getWindow()
                            .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    progressDialog.setMessage(null);
                }
                if(screen.contains("directorySearch")){
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(true);
                }
                else {
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);}


            }
        });
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String text = "";
            BufferedReader reader=null;
            URL url = new URL(this.myUrl);
            final URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod(this.method);

            if(screen.contains("getGroupCont")||screen.contains("getReviews")||screen.contains("deleteFeedback")||screen.contains("deleteGroupContact")||screen.contains("initDirectoryProfile")||screen.contains("updateDirectoryProfile")||screen.contains("addMembers")||screen.contains("deleteDirectory")||screen.contains("distance")||screen.contains("restrictSharing")||screen.contains("getLatestNotification")||screen.contains("markReadNotification")||screen.contains("getNotification")||screen.contains("recommendUserBusiness")||screen.contains("deletelabcontact")||screen.contains("unshareProfile")||screen.contains("directorySecondScreenSearch")||screen.contains("directorySearch2")||screen.contains("directorySearch")||screen.contains("geDirectoryProfile")||screen.contains("myProfile")||screen.contains("getLabProfile")||screen.contains("createDirectory")||screen.contains("checkUserRegistration")||screen.contains("postReviews")||screen.contains("addReviews")||screen.contains("getLabContacts")||screen.contains("createGroupContact")||screen.contains("recommendUserGroup")||screen.contains("createLabContact")||screen.contains("recommendUserLab")||screen.contains("createGroup")||screen.contains("getGroupOfUser")||screen.contains("updateGroup")||screen.contains("deleteGroup")||screen.contains("getGroupContacts")||screen.contains("getGroupContactsUpdated"))
                httpConn.setRequestProperty("token", this.header);
                if(screen.contains("getGroupCont")||screen.contains("getReviews")||screen.contains("deleteFeedback")||screen.contains("deleteGroupContact")||screen.contains("initDirectoryProfile")||screen.contains("updateDirectoryProfile")||screen.contains("addMembers")||screen.contains("deleteDirectory")||screen.contains("getLatestNotification")||screen.contains("markReadNotification")||screen.contains("getNotification")||screen.contains("recommendUserBusiness")||screen.contains("createLabContact")||screen.contains("createGroupContact")||screen.contains("deletelabcontact")||screen.contains("unshareProfile")||screen.contains("directorySecondScreenSearch")||screen.contains("directorySearch2")||screen.contains("directorySearch")||screen.contains("geDirectoryProfile")||screen.contains("getLabProfile")||screen.contains("createDirectory")||screen.contains("checkUserRegistration")||screen.contains("postReviews")||screen.contains("addReviews")||screen.contains("getLabContacts")||screen.contains("recommendUserLab")||screen.contains("recommendUserGroup")||screen.contains("getGroupContactsUpdated")||screen.contains("getGroupContacts")||screen.contains("createGroup"))
                        httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.connect();
            if(method.equals("POST")) {
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                   if(screen.contains("uploadImage")||screen.contains("uploadProfile"))
                   {
                       HttpClient httpclient = null;
                       httpclient = new DefaultHttpClient();
                       HttpPost httppost = new HttpPost(this.myUrl);
                       try {
                           httppost.setEntity(this.multipartEntity);
                           HttpResponse response = null;
                           response = httpclient.execute(httppost);

                           JSONObject httpPostResponse = new JSONObject();
                           httpPostResponse = new JSONObject(EntityUtils.toString(response.getEntity()));
                           if (httpPostResponse.get("status").toString().contains("OK")) {

                                   if (progressDialog != null)
                                       progressDialog.dismiss();
                           }
                       } catch (UnsupportedEncodingException e) {
                           e.printStackTrace();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
                   else {
                       wr.write(this.myData);
                       wr.flush();
                   }

                //Read server respose
            }
           else {
                //Read server respose
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                text = sb.toString();
                jsonObject=new JSONObject(text);
                if (jsonObject.get("status").toString().contains("OK")) {
                    if (screen.contains("deleteGroup")) {
                        Constants.groupDelete = 1;
                        back(act);
                        PreferenceUtils.SaveGroupNamesJSON(null,"groupNamesJson",act);
//                        commonClass.apiIntegration(context, "group/getgroupsofuser", "", PreferenceUtils.getToken(context), "getGroupOfUser");
                    }
                }
            }


        } catch (final MalformedURLException e) {
            e.printStackTrace();

        }
        catch (IOException e) {
            toastMessage("Poor internet connection",act);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(progressDialog != null && progressDialog.isShowing())
            try{
                progressDialog.dismiss();
            }catch (IllegalArgumentException ie){}
        progressDialog = null;
    }

    public static void toastMessage(final String message, Activity act){
        act.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(act,message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<JSONObject> directoryList(){
        ArrayList<JSONObject> directoryList = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                directoryList.add(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
return directoryList;
    }
    public static void back(Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onBackPressed();
            }
        });
    }
    public static JSONArray sortArray(JSONArray array){
        for(int i=0;i<array.length();i++){
            try {
                int min_idx = i;
            for(int j=0;j<array.length();j++){
                if(array.getJSONObject(j).getString("contactName").toUpperCase().compareTo(array.getJSONObject(min_idx).getString("contactName").toUpperCase())>0){
                    array = swapItem(min_idx,j,array);
                }
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }
    public static JSONArray swapItem(int i,int j,JSONArray jsonArray){
        try {
            JSONObject temp =jsonArray.getJSONObject(i);
            jsonArray.put(i,jsonArray.getJSONObject(j));
            jsonArray.put(j,temp);
            return  jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
    public static void updateLab(Activity activity){
        try {
            JSONArray numbersLocal = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            if(PreferenceUtils.getGroupString("numList",activity)!=null) {
                ArrayList<String> num = PreferenceUtils.getGroupString("numList", activity);
                for (int i = 0; i < num.size(); i++) {
                    if (!"".equals(num.get(i)))
                        numbersLocal.put(num.get(i));
                }
                jsonObject.put("phoneNumber", numbersLocal);
            }  } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}