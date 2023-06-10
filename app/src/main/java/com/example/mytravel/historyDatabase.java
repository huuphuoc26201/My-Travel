package com.example.mytravel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.model.historyDao;
import com.example.model.orderDetailsData;

@Database(entities = {orderDetailsData.class},version = 1)
public abstract class historyDatabase extends RoomDatabase {
    private static final String DATABASE_NAME="history.db";
    private static historyDatabase instance;

    public static synchronized historyDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),historyDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract historyDao historyDao();
}
