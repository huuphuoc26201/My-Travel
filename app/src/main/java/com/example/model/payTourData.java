package com.example.model;

public class payTourData {
    public String id;
    public String tentour;
    public String giatour;
    public String hinhtour;
    public String date;
    public String nglon;
    public String treem;
    public String maTour;
    public payTourData(){

    }
    public payTourData(String id, String tentour, String giatour, String hinhtour, String date, String nglon, String treem, String maTour) {
        this.id = id;
        this.tentour = tentour;
        this.giatour = giatour;
        this.hinhtour = hinhtour;
        this.date = date;
        this.nglon = nglon;
        this.treem = treem;
        this.maTour = maTour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTentour() {
        return tentour;
    }

    public void setTentour(String tentour) {
        this.tentour = tentour;
    }

    public String getGiatour() {
        return giatour;
    }

    public void setGiatour(String giatour) {
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

    public String getNglon() {
        return nglon;
    }

    public void setNglon(String nglon) {
        this.nglon = nglon;
    }

    public String getTreem() {
        return treem;
    }

    public void setTreem(String treem) {
        this.treem = treem;
    }

    public String getMaTour() {
        return maTour;
    }

    public void setMaTour(String maTour) {
        this.maTour = maTour;
    }
}
