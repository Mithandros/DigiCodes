package com.maksof.linkapp;

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
                               JSONArray imgURLs = httpPostResponse.getJSONArray("data");
                               if(screen.contains("uploadImage")) {
                                   PreferenceUtils.save(imgURLs.get(0).toString(), "imageURL", context);
                                   ArrayList<JSONObject> imgArray = new ArrayList<>();
                                   ArrayList<String> arrayListString = new ArrayList<>();
                                   for (int i = 0; i < imgURLs.length(); i++) {
                                       Constants.imgsURL.add(imgURLs.get(i).toString());
                                       arrayListString.add(imgURLs.get(i).toString());
                                   }

                                   for (int i = 0; i < Constants.imgsURL.size(); i++) {
                                       imgArray.add(new JSONObject().put("" + i + "", Constants.imgsURL.get(i)));
                                   }

                                   PreferenceUtils.saveGroupString(Constants.imgsURL, "imgString", act);
                                   PreferenceUtils.saveGroupJSON(imgArray, "imageURL", act);
                                   PreferenceUtils.saveGroupJSON(imgArray, "imgURLSlist", act);


                                   create_directory.setImgURL(act);
                                   if (this.cls.equals("main_page")) {
                                       phone_home_fragment.images(act);
                                       lab_home_fragment.images(act);
                                       directory_home.images(act);
                                   }
                                   if (this.cls.equals("lab_user_detail")) {
                                       lab_user_details.images(act);
                                   }
                                   if (this.cls.equals("create_directory")) {
                                       create_directory.images(act);
                                   }
                                   if (this.cls.equals("groupContacts")) {
                                       groupContacts.images(act);
                                   }

                                   if (this.cls.equals("directory_profile")) {
                                       directory_profile.images(act);
                                   } }
                               else {
                                   ArrayList<String> arrayListString = new ArrayList<>();
                                   for (int i = 0; i < imgURLs.length(); i++) {
                                       Constants.imgsURL.add(imgURLs.get(i).toString());
                                       arrayListString.add(imgURLs.get(i).toString());
                                       PreferenceUtils.save(imgURLs.get(i).toString(),"directoryProfileImage",act);
                                       create_directory.uploadProfileImage(act);
                                   }
                               }
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
                if(!screen.contains("uploadImage")&&!screen.contains("uploadProfile")){
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;


                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                text = sb.toString();
                if(text!=null&&!"".equals(text)){
                jsonObject=new JSONObject(text);

                if (jsonObject.get("status").toString().contains("OK")){
                    if(screen.contains("getLatestNotification")){
                       try {
                           JSONArray array;
                           try{
                               array = jsonObject.getJSONArray("data");
                           }catch (JSONException je){
                               array = new JSONArray();
                           }
                           ArrayList<JSONObject> latestNotifications = new ArrayList<>();
                           for (int j = 0; j < array.length(); j++) {
                               JSONObject notificatios = new JSONObject();
                               notificatios = array.getJSONObject(j);
                               latestNotifications.add(notificatios);
                           }
                           PreferenceUtils.SaveGroupNamesJSON(latestNotifications, "latestNotification", act);
                           PreferenceUtils.save(array.toString(), "latestNotification", context);
                       }catch (JSONException je){
                           je.printStackTrace();
                       }
                    }

                    if(screen.contains("deleteDirectory")){
                        toastMessage("Directory deleted",act);
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    act.onBackPressed();
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("occupation","");
                                    int distance = 10*Constants.distance;
                                    jsonObject.put("distance", distance);
                                  //  commonClass.apiIntegration(context, "profile/searchdirectoryprofile", jsonObject.toString(), PreferenceUtils.getToken(context), "directorySearch");
                                }catch (JSONException je){}
                            }
                        });

                    }
                    if (screen.contains("signup")) {
                        PreferenceUtils.SaveToken(jsonObject.get("data").toString(),context);
                        try {
                            JSONArray array = jsonObject.getJSONArray("data");
                            PreferenceUtils.SaveTempNumber(array.getJSONObject(0).getString("phone_number"),context);
                            PreferenceUtils.SaveNumber(array.getJSONObject(0).getString("phone_number"),context);
                            PreferenceUtils.SaveEmail(array.getJSONObject(0).getString("email"),context);
                            PreferenceUtils.SaveName(array.getJSONObject(0).getString("first_name")+" "+array.getJSONObject(0).getString("last_name"),context);
                            PreferenceUtils.SavePassword(array.getJSONObject(0).getString("password"),context);
                            PreferenceUtils.SaveFName(array.getJSONObject(0).getString("first_name"),context);
                            PreferenceUtils.SaveLName(array.getJSONObject(0).getString("last_name"),context);
                            PreferenceUtils.SavekeyId(array.getJSONObject(0).getString("id"),context);
                            PreferenceUtils.SaveisAllowToLab(array.getJSONObject(0).getString("isAllowToLab"),context);
                            PreferenceUtils.saveDistanceIn(array.getJSONObject(0).getString("DistanceIn"),context);
                            PreferenceUtils.SaveToken(array.getJSONObject(0).getString("token"),context);
                            Intent groupContactIntent = new Intent(act, mainPage.class);
                            this.context.startActivity(groupContactIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(screen.contains("restrictSharing")){
                        if("1".equals(PreferenceUtils.getisAllowToLab(context)))
                            toastMessage("Restrict sharing mode enabled",act);
                        else
                            toastMessage("Restrict sharing mode disabled",act);
                    }

                    if(screen.contains("deleteFeedback")){
                        lab_user_details.getReviews(act);
                    }

                    if(screen.contains("getReviews")){
                        lab_user_details.updateReviews(jsonObject.getJSONArray("data"),act);
                    }
                    if(screen.contains("distance")){
                            PreferenceUtils.saveDistanceIn(jsonObject.getJSONObject("data").getString("distanceIn"),context);
                            setting_fragment.changeText(act);
                    }

                    if (screen.contains("createGroup")) {
                      //  commonClass.apiIntegration(context, "group/getgroupsofuser", "", PreferenceUtils.getToken(context), "getGroupOfUser");
                        back(act);
                         }

                    if (screen.contains("updateGroup")) {
                        JSONArray groups = jsonObject.getJSONArray("data");
                        if(groups.length()>0) {
                            String groupName = groups.getJSONObject(0).getString("name");
                            PreferenceUtils.SaveGroupName(groupName,act);
                            group_contacts_fragment.updateGroupName(act,groupName);
                        }
                       // commonClass.apiIntegration(context, "group/getgroupsofuser", "", PreferenceUtils.getToken(context), "getGroupOfUser");

                    }

                    if(screen.contains("getGroupOfUser")){
                        try{
                            ArrayList<JSONObject> jsonObjects=new ArrayList<>();
                            JSONArray NameArray = jsonObject.getJSONArray("data");
                            boolean exist ;
                            for (int i = 0; i < NameArray.length(); i++)
                            {
                                exist = false;
                                JSONObject jsonObject = NameArray.getJSONObject(i);
                                for(int j=0;j<jsonObjects.size();j++)
                                {
                                    if(jsonObject.getString("id").equals(jsonObjects.get(j).getString("id")))
                                    {
                                        exist = true;
                                        break;
                                    }
                                }
                                if(exist==false)
                                    jsonObjects.add(jsonObject);
                            }

                            PreferenceUtils.SaveGroupNamesJSON(jsonObjects,"groupNamesJson",act);
                            groups_home_fragment.onResume(act);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(screen.contains("getGroupContacts")||screen.contains("getGroupCont")){
                        JSONArray jsonGroupContacts = jsonObject.getJSONObject("data").getJSONArray("sharedMembers");
                        ArrayList<JSONObject> arrayList = new ArrayList<>();
                        if(jsonGroupContacts.length()>=1)
                        for(int i= 0; i<jsonGroupContacts.length();i++){
                            JSONObject jsonObject = jsonGroupContacts.getJSONObject(i);
                            arrayList.add(jsonObject);
                        }
                        PreferenceUtils.SaveGroupContactsJSON(arrayList,"GroupContacts",act);
                        JSONArray jsonGroupContacts1 = jsonObject.getJSONObject("data").getJSONArray("groupMembers");
                        ArrayList<JSONObject> arrayList1 = new ArrayList<>();
                        if(jsonGroupContacts1.length()>=1)
                        for(int i= 0; i<jsonGroupContacts1.length();i++){
                            JSONObject jsonObject = jsonGroupContacts1.getJSONObject(i);
                            arrayList1.add(jsonObject);
                        }
                        PreferenceUtils.SaveGroupContactsJSON(arrayList1,"GroupMembers",act);
                        group_contacts_fragment.updateContactList(arrayList,act);
                        group_member_fragment.updateContactList(arrayList1,act);
                        if(screen.contains("getGroupContacts"))
                        {
                            JSONObject groupID = new JSONObject(this.myData);
                            Intent groupContactIntent = new Intent(act, groupContacts.class);
                            groupContactIntent.putExtra("groupId",groupID.getString("groupId"));
                            this.context.startActivity(groupContactIntent);
                        }
                    }
                    if(screen.contains("getGroupContactsUpdated")){
                        JSONArray jsonGroupContacts = jsonObject.getJSONObject("data").getJSONArray("sharedMembers");
                        ArrayList<JSONObject> arrayList = new ArrayList<>();
                        if(jsonGroupContacts.length()>=1)
                            for(int i= 0; i<jsonGroupContacts.length();i++){
                                JSONObject jsonObject = jsonGroupContacts.getJSONObject(i);
                                arrayList.add(jsonObject);
                            }
                        PreferenceUtils.SaveGroupContactsJSON(arrayList,"GroupContacts",act);
                        group_contacts_fragment.updateContactList(arrayList,act);
                    }

                    if(screen.contains("recommendUserLab")){
                        lab_user_details.toggleBtnAppearance(true,act);
                        String data = jsonObject.get("data").toString();
                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("contactId", data);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // commonClass.apiIntegration(context, "labcontact/createlabcontact", jsonObject1.toString(), PreferenceUtils.getToken(context),"createLabContact");
                        JSONObject addReviewsObject = new JSONObject();
                        addReviewsObject.put("contactId",jsonObject.get("data").toString());
                        addReviewsObject.put("rating",PreferenceUtils.get("labContactRating",context));
                        addReviewsObject.put("feedback",PreferenceUtils.get("labContactFeedBack",context));
                        addReviewsObject.put("imageUrl",PreferenceUtils.getGroupString("imgString",act));
                      //  commonClass.apiIntegration(context, "review/addreview", addReviewsObject.toString(), PreferenceUtils.getToken(context),"addReviews");
                        PreferenceUtils.saveGroupString(null,"imgString",act);
                        PreferenceUtils.save(null,"labContactRating",context);
                        PreferenceUtils.save(null,"labContactFeedBack",context);
                    }

                    if(screen.contains("addReviews")){
                        PreferenceUtils.saveGroupString(null,"imgString",act);
                        PreferenceUtils.save(null,"labContactRating",context);
                        PreferenceUtils.save(null,"labContactFeedBack",context);
                        updateLab(act);
                        lab_user_details.updateReviews(jsonObject.getJSONArray("data"),act);
                        lab_home_fragment.closeModal(act);
                        phone_home_fragment.closeModal(act);
                    }
                    if(screen.contains("postReviews")){
                        toastMessage("Review posted successfully",act);
                        PreferenceUtils.saveGroupString(null,"imgString",act);
                        PreferenceUtils.save(null,"labContactRating",context);
                        PreferenceUtils.save(null,"labContactFeedBack",context);
                        updateLab(act);
                        lab_user_details.updateReviews(jsonObject.getJSONArray("data"),act);
                        lab_home_fragment.closeModal(act);
                        phone_home_fragment.closeModal(act);
                    }
                    if(screen.contains("createDirectory")){
                        toastMessage("Directory created",act);
                        back(act);
                        try{
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("occupation","");
                            int distance = 10*Constants.distance;
                            jsonObject.put("distance", distance);
                          //  commonClass.apiIntegration(context, "profile/searchdirectoryprofile", jsonObject.toString(), PreferenceUtils.getToken(context), "directorySearch");
                        }catch (JSONException je){}
                    }

                    if(screen.contains("recommendUserGroup")){
                        String data = jsonObject.get("data").toString();
                        JSONObject data1 = new JSONObject();
                        data1.put("contactId",data);
                        data1.put("groupId",this.groupID);
                      //  commonClass.apiIntegration(context, "groupcontacts/addgroupcontact", data1.toString(), PreferenceUtils.getToken(act),"createGroupContact");
                       }
                    if(screen.contains("createGroupContact")||screen.contains("createLabContact")){
                        phone_home_fragment.closeModal(act);
                        lab_home_fragment.closeModal(act);
                        groupContacts.closeModal(act);
                        if(screen.contains("createGroupContact")){
                            toastMessage("Contact shared to group",act);
                            JSONObject addReviewsObject = new JSONObject();
                            addReviewsObject.put("contactId",(new JSONObject(this.myData).getString("contactId")));
                            addReviewsObject.put("rating",PreferenceUtils.get("labContactRating",context));
                            addReviewsObject.put("feedback",PreferenceUtils.get("labContactFeedBack",context));
                            addReviewsObject.put("imageUrl",PreferenceUtils.getGroupString("imgString",act));
                            //commonClass.apiIntegration(context, "review/addreview", addReviewsObject.toString(), PreferenceUtils.getToken(context),"addReviews");
                            PreferenceUtils.saveGroupString(null,"imgString",act);
                            PreferenceUtils.save(null,"labContactRating",context);
                            PreferenceUtils.save(null,"labContactFeedBack",context);
                            phone_home_fragment.closeModal(act);
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("groupId", (new JSONObject(this.myData)).getString("groupId"));
                            //commonClass.apiIntegration(act, "groupcontacts/getgroupcontacts", jsonObject.toString(), PreferenceUtils.getToken(act), "getGroupContactsUpdated");
                        }catch (JSONException je){

                        }}
                        if(screen.contains("createLabContact")){
                            toastMessage("Contact shared to Lynks",act);
                            updateLab(act);
                        }
                    }
                    if(screen.contains("deletelabcontact")){
                        updateLab(act);
                      act.onBackPressed();
                    }
                    if(screen.contains("getLabContacts")){
                        JSONArray jsonGroupContacts = jsonObject.getJSONArray("data");
                        JSONArray sortedArray = sortArray(jsonGroupContacts);
                        ArrayList<JSONObject> arrayList = new ArrayList<>();
                        for(int i= 0; i<sortedArray.length();i++){
                            JSONObject jsonObject = sortedArray.getJSONObject(i);
                            boolean exist = false;
                          for(int j= 0; j<arrayList.size();j++){
                                if(jsonObject.getString("phoneNumber").equals(arrayList.get(j).getString("phoneNumber")))
                                {
                                    exist =true;
                                    break;
                                }
                            }
                            if(exist==false)
                            arrayList.add(jsonObject);
                        }
                        PreferenceUtils.SaveGroupContactsJSON(arrayList,"getLabContacts",act);
                        lab_home_fragment.initData(act);

                    }
                    if(screen.contains("checkUserRegistration")){
                        JSONArray array = jsonObject.getJSONArray("data");
                        ArrayList<JSONObject> userOnLinkApp = new ArrayList<>();
                        for (int j=0; j<array.length();j++){
                            JSONObject users = new JSONObject();
                            users = array.getJSONObject(j);
                            userOnLinkApp.add(users);
                        }
                        PreferenceUtils.SaveGroupNamesJSON(userOnLinkApp,"userRegistration",act);
                        ArrayList<JSONObject> abc =PreferenceUtils.getGroupNamesJSON("userRegistration",act);
                       }
                    if(screen.contains("getLabProfile")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        ArrayList<JSONObject> obj = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++){
                            obj.add(jsonArray.getJSONObject(i));
                        }
                        PreferenceUtils.SaveGroupNamesJSON(obj,"labProfile",act);
                        Intent intent = new Intent(context, lab_user_details.class);
                        context.startActivity(intent);
                    }

                    if (screen.contains("geDirectoryProfile")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        ArrayList<JSONObject> obj = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++){
                            obj.add(jsonArray.getJSONObject(i));
                        }
                        PreferenceUtils.SaveGroupNamesJSON(obj,"labProfile",act);
                        Intent dirProfileIntent = new Intent(context, directory_profile.class);
                        context.startActivity(dirProfileIntent);
                    }

                    if(screen.contains("directorySearch")||screen.contains("directorySearch2")){
                        if (jsonObject.get("message").toString().contains("find some directryProfile")){
                            ArrayList<JSONObject> directoryList= directoryList();
                            PreferenceUtils.SaveGroupNamesJSON(directoryList,"directoryList",act);
                                PreferenceUtils.SaveGroupNamesJSON(directoryList,"directorySecondList",act);
                                directory_home.onResume(act,context);
                        }
                        else if (jsonObject.get("message").toString().contains("not found")){
                            if(PreferenceUtils.get("directorySearch",context)!=null)
                                if( PreferenceUtils.get("directorySearch",context).equals("resume"))
                                    toastMessage("No records found",act);
                                ArrayList<JSONObject> arrayList = new ArrayList<>();
                                PreferenceUtils.SaveGroupNamesJSON(new ArrayList<>(arrayList),"directorySecondList",act);
                                directory_home.onResume(act,context);
                        }
                    }
                    if(screen.contains("unshareProfile")){
                        lab_user_details.toggleBtnAppearance(false,act);
                        updateLab(act);
                    }
                    if(screen.contains("getNotification")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        ArrayList<JSONObject> obj = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            obj.add(jsonArray.getJSONObject(i));
                        }
                        PreferenceUtils.SaveGroupNamesJSON(obj, "notifications", act);
                    }
                    if(screen.contains("updateDirectoryProfile")||screen.contains("initDirectoryProfile")){
                        if(screen.contains("updateDirectoryProfile"))
                            toastMessage("Updated",act);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        ArrayList<JSONObject> obj = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++){
                            obj.add(jsonArray.getJSONObject(i));
                        }
                        PreferenceUtils.SaveGroupNamesJSON(obj,"labProfile",act);
                        directory_profile.updatedProfile(act, obj);
                        Constants.imgsURL.removeAll(Constants.imgsURL);
                    }
                    if(screen.contains("addMembers")){
                        JSONArray jsonGroupContacts = jsonObject.getJSONArray("data");
                        ArrayList<JSONObject> arrayList = new ArrayList<>();
                        for(int i= 0; i<jsonGroupContacts.length();i++){
                            JSONObject jsonObject = jsonGroupContacts.getJSONObject(i);
                            arrayList.add(jsonObject);
                        }
                        PreferenceUtils.SaveGroupContactsJSON(arrayList,"GroupMembers",act);
                        group_member_fragment.updateContactList(arrayList,act);
                        back(act);
                    }
                    if(screen.contains("deleteGroupContact")){
                        toastMessage("Contact deleted",act);
                        back(act);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("groupId", (new JSONObject(this.myData)).getString("groupId"));
                        //commonClass.apiIntegration(act, "groupcontacts/getgroupcontacts", jsonObject.toString(), PreferenceUtils.getToken(act), "getGroupCont");
                    }
                }

                if (jsonObject.get("status").toString().contains("FAIL")) {
                    if(screen.contains("getLabProfile")||screen.contains("directorySearch"))
                        toastMessage("No records found",act);
                    else if(screen.contains("createGroup"))
                    {
                        if(jsonObject.getString("message").equals("You already have created a group with same name!"))
                            toastMessage("Group already exist with same name",act);
                    }
                    else if(screen.contains("createDirectory")){
                            toastMessage(jsonObject.getString("message"),act);
                    }
                    if (screen.contains("signup")) {
                        if(jsonObject.getString("message").equals("Please insert valid 12 digits number "))
                            toastMessage("Invalid phone number",act);
                        else toastMessage(jsonObject.getString("message"),act);
                    }

                    if(screen.contains("getReviews")){
                        if(jsonObject.getString("message").equals("Review not found"))
                            lab_user_details.updateReviews(new JSONArray(),act);
                    }
                    if(screen.contains("getGroupOfUser")){
                        PreferenceUtils.SaveGroupNamesJSON(new ArrayList<>(),"groupNamesJson",act);
                        groups_home_fragment.onResume(act);
                    }
                    if(screen.contains("createLabContact")){
                        updateLab(act);
                        toastMessage("Contact shared to Lynks",act);
                    }
                    if(screen.contains("recommendUserLab")){
                        updateLab(act);
                        if (jsonObject.get("message").toString().contains("Number is already exist"))
                            toastMessage("Number is already exist, posting review",act);
                        else  toastMessage(jsonObject.get("message").toString(),act);
                        if (jsonObject.get("message").toString().contains("Number is already exist"))
                            lab_user_details.toggleBtnAppearance(true,act);
                        else lab_user_details.toggleBtnAppearance(false,act);
                    }

                    if(screen.contains("getLabContacts")){
                        PreferenceUtils.SaveGroupContactsJSON(null,"getLabContacts",act);
                        lab_home_fragment.initData(act);
                    }
                    if(screen.contains("recommendUserLab")) {
                        try {
                            JSONObject addReviewsObject = new JSONObject();
                            addReviewsObject.put("contactId", jsonObject.getJSONObject("data").getString("contactId"));
                            addReviewsObject.put("rating", PreferenceUtils.get("labContactRating", context));
                            addReviewsObject.put("feedback", PreferenceUtils.get("labContactFeedBack", context));
                            addReviewsObject.put("imageUrl", PreferenceUtils.getGroupString("imgString", act));
                           // commonClass.apiIntegration(context, "review/addreview", addReviewsObject.toString(), PreferenceUtils.getToken(context), "addReviews");
                        }
                    catch (JSONException e){
                        e.printStackTrace();}
                    }
                    if(screen.contains("recommendUserGroup")){
                        try{
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject data1 = new JSONObject();
                        data1.put("contactId",data.getString("contactId"));
                        data1.put("groupId",this.groupID);
                        //commonClass.apiIntegration(context, "groupcontacts/addgroupcontact", data1.toString(), PreferenceUtils.getToken(act),"createGroupContact");
                        phone_home_fragment.closeModal(act);}
                        catch (JSONException je){}
                    }
                    if(screen.contains("createGroupContact")){
                        if (jsonObject.get("message").toString().contains("Group contact already exist 2"))
                            toastMessage("Contact already exist in group",act);
                    }
                }}
            }
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
//                commonClass.apiIntegration(activity, "labcontact/getlabcontact", jsonObject.toString(), PreferenceUtils.getToken(activity), "getLabContacts");
            }  } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}