package com.maksof.linkapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PreferenceUtils {

    public PreferenceUtils(){

    }

    public static  String getNumber(Context context){
        return get(Constants.keynumber,context);
    }
    public static boolean SaveNumber(String number, Context context){
        return save(number,Constants.keynumber,context);

    }
    public static  String getisAllowToLab(Context context){
        return get(Constants.isAllowToLab,context);
    }
    public static boolean SaveisAllowToLab(String number, Context context){
        return save(number,Constants.isAllowToLab,context);

    }

    public static  String getDistanceIn(Context context){
        return get(Constants.distanceIn,context);
    }
    public static boolean saveDistanceIn(String number, Context context){
        return save(number,Constants.distanceIn,context);

    }

    public static boolean SavePassword(String password, Context context){
       return save(password,Constants.keyPassword,context);

    }

    public static boolean SaveTempNumber(String number, Context context){
        return save(number,Constants.tempNum,context);
    }

    public static boolean SaveEmail(String email, Context context){
        return save(email,Constants.email,context);
    }

    public static boolean SaveName(String name, Context context){
        return save(name,Constants.name,context);
    }

    public static boolean SaveFName(String name, Context context){
        return save(name,Constants.fName,context);
    }

    public static boolean SaveLName(String name, Context context){
        return save(name,Constants.lName,context);
    }


    public static boolean SaveToken(String token, Context context){
        return save(token,Constants.token,context);
    }
    public static boolean SavekeyId(String id, Context context){
        return save(id,Constants.keyId,context);
    }
    public static boolean SaveGroupName(String gName, Context context){
        return save(gName,Constants.groupName,context);
    }

    public static  String getGroupName(Context context)
    {
        return get(Constants.groupName,context);
    }
    public static  String getEmail(Context context)
    {
        return get(Constants.email,context);
    }

    public static  String getKeyId(Context context){

        return get(Constants.keyId,context);
    }
    public static  String getName(Context context){

        return get(Constants.name,context);
    }
    public static  String getfName(Context context){

        return get(Constants.fName,context);
    }
    public static  String getlName(Context context) {

        return get(Constants.lName,context);
    }
    public static  String getTempfName(Context context){

        return get(Constants.tempFName,context);
    }
    public static  String getTemplName(Context context){

        return get(Constants.tempLName,context);
    }
    public static  String getTempEmail(Context context){

        return get(Constants.tempemail,context);
    }
    public static String getToken(Context context){

        return get(Constants.token,context);
    }
    public static boolean saveNotificationStatus(String bool, Context context){
        return save(bool,Constants.notificationStatus,context);
    }

    public static  String getNotificationStatus(Context context)
    {
        return get(Constants.notificationStatus,context);
    }



    public static boolean save(String value, String constant, Context context){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(constant,value);
        editor.apply();
        return true;
    }

    public static String get(String value, Context context){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(value,null);
    }

    public static boolean SaveGroupNamesJSON(ArrayList<JSONObject> list, String key, Activity activity){
        saveGroupJSON(list,  key,  activity);
                return true;
    }

    public static ArrayList<JSONObject> getGroupNamesJSON(String key , Activity activity){
        return getGroupJSON(key , activity);
    }

    public static boolean SaveGroupContactsJSON(ArrayList<JSONObject> list, String key, Activity activity){
        saveGroupJSON(list,  key,  activity);
        return true;
    }

    public static ArrayList<JSONObject> getGroupContactsJSON(String key , Activity activity){
        return getGroupJSON(key , activity);
    }



    public static void saveGroupJSON(ArrayList<JSONObject> list, String key, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }

    public static ArrayList<JSONObject> getGroupJSON(String key , Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<JSONObject>>() {}.getType();

        return gson.fromJson(json, type);
    }
    public static void saveGroupString(ArrayList<String> list, String key, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }
    public static ArrayList<String> getGroupString(String key , Activity activity)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
