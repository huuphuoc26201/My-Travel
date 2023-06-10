package com.example.model;

public class orderTourData {
    public String tentour;
    public long giatour;
    public int nglon;
    public int treem;
    public String date;
    public String maTour;

    public orderTourData() {

    }

    public orderTourData(String tentour, long giatour, int nglon, int treem, String date, String maTour) {
        this.tentour = tentour;
        this.giatour = giatour;
        this.nglon = nglon;
        this.treem = treem;
        this.date = date;
        this.maTour = maTour;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaTour() {
        return maTour;
    }

    public void setMaTour(String maTour) {
        this.maTour = maTour;
    }
}
