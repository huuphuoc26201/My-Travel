package com.example.mytravel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.model.UserDao;
import com.example.model.gioHangData;

@Database(entities = {gioHangData.class},version = 1)
public abstract class giohangDatabase extends RoomDatabase {
    private static final String DATABASE_NAME="giohang.db";
    private static giohangDatabase instance;

    public static synchronized giohangDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),giohangDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
}
