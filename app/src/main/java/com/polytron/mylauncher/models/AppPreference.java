package com.polytron.mylauncher.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AppPreference {
    static String TAG = "AppPreference";
    private static final String SHARED_PREF_NAME = "myAppPreference";
    static final String KEY_APP_LIST = "app_list";
    static final String KEY_ADMIN_USERNAME = "username";
    static final String KEY_ADMIN_PASSWORD = "password";

    android.content.SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Activity activity;

    public AppPreference(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean saveAdminData(String username, String password) {
        try{
            editor.putString(KEY_ADMIN_USERNAME, username);
            editor.putString(KEY_ADMIN_PASSWORD, password);
            editor.apply();
            Log.i(TAG, "saveAdminData: Success");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveAppList(String appList) {
        try{
            editor.putString(KEY_APP_LIST, appList);
            editor.apply();
            Log.i(TAG, "saveAppList: Success");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void clearAdminData() {
        editor.remove(KEY_ADMIN_USERNAME);
        editor.remove(KEY_ADMIN_PASSWORD);
        editor.apply();
        Log.i(TAG, "clearAdminData: User Data Removed!");
    }

    public void clearAppListData() {
        editor.remove(KEY_APP_LIST);
        editor.apply();
        Log.i(TAG, "clearAppListData: App List Data Removed!");
    }

    public String getKeyAdminUsername() {
        try{
            return sharedPreferences.getString(KEY_ADMIN_USERNAME, "admin");
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getKeyAdminPassword() {
        try{
            return sharedPreferences.getString(KEY_ADMIN_PASSWORD, "password");
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String getKeyAppList() {
        try{
            return sharedPreferences.getString(KEY_APP_LIST, null);
        }
        catch (Exception e) {
            throw e;
        }
    }

}
