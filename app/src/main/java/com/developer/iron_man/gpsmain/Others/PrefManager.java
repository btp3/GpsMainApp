package com.developer.iron_man.gpsmain.Others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sagar on 21/6/17.
 */

@SuppressLint("CommitPrefEdits")

public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Shared pref file name
    private static final String PREF_NAME = "GpsTracker";

    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setToken(String name) {
        editor.putString("token", name);
        editor.commit();
    }

    public String getToken() {
        return pref.getString("token", null);
    }

    public void setUser(String user)
    {
        editor.putString("user", user);
        editor.commit();
    }

    public String getUser(){
        return pref.getString("user",null);
    }

    public void setMarkers(String markers)
    {
        editor.putString("marker", markers);
        editor.commit();
    }

    public String getMarkers(){
        return pref.getString("marker",null);
    }

    public void setNotification_Flag(String flag)
    {
        editor.putString("flag", flag);
        editor.commit();
    }

    public String getNotification_Flag(){
        return pref.getString("flag",null);
    }

    public void setUsername(String username)
    {
        editor.putString("username", username);
        editor.commit();
    }

    public String getUsername(){
        return pref.getString("username",null);
    }


}