package com.example.data_local;

import android.content.Context;

import com.example.model.namedata;
import com.google.gson.Gson;

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

}
