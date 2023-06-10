package com.example.model;

public class danhGiaData {
    public String name;
    public String email;
    public String noidung;
    public String danhgia;

    public danhGiaData(String name, String email, String noidung, String danhgia) {
        this.name = name;
        this.email = email;
        this.noidung = noidung;
        this.danhgia = danhgia;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(String danhgia) {
        this.danhgia = danhgia;
    }
}
