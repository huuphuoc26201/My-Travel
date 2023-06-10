package com.example.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface historyDao {
    @Insert
    void inserthistory(orderDetailsData orderDetailsData);

    @Query("SELECT *FROM history")
    List<orderDetailsData> getListHistory();

    @Delete
    void deleteGiohang(orderDetailsData orderDetailsData);

    @Query("DELETE FROM history")
    void deleteAllhistory();
}
