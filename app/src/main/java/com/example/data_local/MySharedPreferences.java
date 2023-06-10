package com.example.data_local;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String MY_SHARED_FREFENCES="MY_SHARED_FREFENCES";
    private Context mContext;


    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }
    public void putStringValue(String key, String value){
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(MY_SHARED_FREFENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getStringValue(String key){
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(MY_SHARED_FREFENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
    public void putBooleanValue(String key, boolean value){
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(MY_SHARED_FREFENCES,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public boolean getBooleanValue(String key){
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(MY_SHARED_FREFENCES,0);
        return sharedPreferences.getBoolean(key,false);
    }
}
