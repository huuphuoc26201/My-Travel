package com.example.data_local;

import android.content.Context;

import com.example.model.gioHangData;
import com.example.model.namedata;
import com.example.model.user;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataLocalManager {
    private static final String PREF_OBJECT_USER ="PREF_OBJECT_USER" ;
    private static final String PREF_LIST_USER ="PREF_LIST_USER" ;
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;
    public  static void init(Context context){
        instance=new DataLocalManager();
        instance.mySharedPreferences=new MySharedPreferences(context);
    }
    public static DataLocalManager getInstance(){
        if(instance==null){
            instance=new DataLocalManager();
        }
        return instance;
    }
    public static void setUser(gioHangData giohangdata){
        Gson gson=new Gson();
        String strJsonUser=gson.toJson(giohangdata);
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PREF_OBJECT_USER,strJsonUser);
    }
    public static gioHangData getUser(){
        String strJsonUser=DataLocalManager.getInstance().mySharedPreferences.getStringValue(PREF_OBJECT_USER);
        Gson gson=new Gson();
        gioHangData giohangdata=gson.fromJson(strJsonUser, gioHangData.class);
        return giohangdata;
    }

    public static void setUserName(user User){
        Gson gson=new Gson();
        String strJsonUser=gson.toJson(User);
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PREF_OBJECT_USER,strJsonUser);
    }
    public static user getUserName(){
        String strJsonUser=DataLocalManager.getInstance().mySharedPreferences.getStringValue(PREF_OBJECT_USER);
        Gson gson=new Gson();
        user User=gson.fromJson(strJsonUser, user.class);
        return User;
    }
    public static void setName(namedata User){
        Gson gson=new Gson();
        String strJsonUser=gson.toJson(User);
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PREF_OBJECT_USER,strJsonUser);
    }
    public static namedata getName(){
        String strJsonUser=DataLocalManager.getInstance().mySharedPreferences.getStringValue(PREF_OBJECT_USER);
        Gson gson=new Gson();
        namedata User=gson.fromJson(strJsonUser, namedata.class);
        return User;
    }

    public static void setListUser(List<gioHangData> manggiohang){
        Gson gson=new Gson();
        JsonArray jsonArray=gson.toJsonTree(manggiohang).getAsJsonArray();
        String strJsonArray=jsonArray.toString();
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PREF_LIST_USER,strJsonArray);
    }
    public static List<gioHangData> getListUser(){
        String strJsonArray=DataLocalManager.getInstance().mySharedPreferences.getStringValue(PREF_OBJECT_USER);
        List<gioHangData> manggiohang=new ArrayList<>();
        try {
            JSONArray jsonArray=new JSONArray(strJsonArray);
            JSONObject jsonObject;
            gioHangData giohangdata;
            Gson gson=new Gson();
            for(int i=0;i<jsonArray.length();i++){
                jsonObject=jsonArray.getJSONObject(i);
                giohangdata=gson.fromJson(jsonObject.toString(),gioHangData.class);
                manggiohang.add(giohangdata);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return manggiohang;
    }
}
