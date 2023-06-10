package com.example.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertgiohang(gioHangData giohangdata);

    @Query("SELECT *FROM giohang")
    List<gioHangData> getListGiohang();

    @Delete
    void deleteGiohang(gioHangData giohangdata);

    @Query("DELETE FROM giohang")
    void deleteAllgiohang();
}
