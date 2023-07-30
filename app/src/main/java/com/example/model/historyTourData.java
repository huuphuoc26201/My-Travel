package com.example.model;

public class historyTourData {
    public String id;
    public String tentour;
    public String giatour;
    public String hinhtour;
    public String date;
    public String nglon;
    public String treem;
    public String maTour;
    public String name;
    public String phone;
    public String email;
    public String payment;
    public String dateTime;
    public historyTourData(){
    }

    public historyTourData(String id, String tentour, String giatour, String hinhtour, String date, String nglon, String treem, String maTour, String name, String phone, String email, String payment, String dateTime) {
        this.id = id;
        this.tentour = tentour;
        this.giatour = giatour;
        this.hinhtour = hinhtour;
        this.date = date;
        this.nglon = nglon;
        this.treem = treem;
        this.maTour = maTour;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.payment = payment;
        this.dateTime = dateTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
