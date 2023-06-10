package com.example.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "giohang")
public class gioHangData {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String tentour;
    public long giatour;
    public String hinhtour;
    public String date;
    public int nglon;
    public int treem;
    public String maTour;

    public gioHangData(String tentour, long giatour, String hinhtour, String date, int nglon, int treem, String maTour) {
        this.tentour = tentour;
        this.giatour = giatour;
        this.hinhtour = hinhtour;
        this.date = date;
        this.nglon = nglon;
        this.treem = treem;
        this.maTour = maTour;
    }

    public gioHangData() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTentour() {
        return tentour;
    }

    public void setTentour(String tentour) {
        this.tentour = tentour;
    }

    public long getGiatour() {
        return giatour;
    }

    public void setGiatour(long giatour) {
        this.giatour = giatour;
    }

    public String getHinhtour() {
        return hinhtour;
    }

    public void setHinhtour(String hinhtour) {
        this.hinhtour = hinhtour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNglon() {
        return nglon;
    }

    public void setNglon(int nglon) {
        this.nglon = nglon;
    }

    public int getTreem() {
        return treem;
    }

    public void setTreem(int treem) {
        this.treem = treem;
    }

    public String getMaTour() {
        return maTour;
    }

    public void setMaTour(String maTour) {
        this.maTour = maTour;
    }

}

