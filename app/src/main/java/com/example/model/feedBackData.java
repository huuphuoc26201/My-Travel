package com.example.model;

public class feedBackData {
    String name;
    String email;
    String phone;
    String address;
    String tentour;
    String matour;
    String liketour;
    String khonghailong;
    String danhgia;
    String gopy;

    public feedBackData(){
    }
    public feedBackData(String name, String email, String phone, String address, String tentour, String matour, String liketour, String khonghailong, String danhgia, String gopy) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.tentour = tentour;
        this.matour = matour;
        this.liketour = liketour;
        this.khonghailong = khonghailong;
        this.danhgia = danhgia;
        this.gopy = gopy;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTentour() {
        return tentour;
    }

    public void setTentour(String tentour) {
        this.tentour = tentour;
    }

    public String getMatour() {
        return matour;
    }

    public void setMatour(String matour) {
        this.matour = matour;
    }

    public String getLiketour() {
        return liketour;
    }

    public void setLiketour(String liketour) {
        this.liketour = liketour;
    }

    public String getKhonghailong() {
        return khonghailong;
    }

    public void setKhonghailong(String khonghailong) {
        this.khonghailong = khonghailong;
    }

    public String getDanhgia() {
        return danhgia;
    }

    public void setDanhgia(String danhgia) {
        this.danhgia = danhgia;
    }

    public String getGopy() {
        return gopy;
    }

    public void setGopy(String gopy) {
        this.gopy = gopy;
    }
}
