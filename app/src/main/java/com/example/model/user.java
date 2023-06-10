package com.example.model;

public class user {
    public String email, phone,name,imageAvt,key;

    public user() {
    }

    public user(String email, String phone, String name, String imageAvt, String key) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.imageAvt = imageAvt;
        this.key = key;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageAvt() {
        return imageAvt;
    }

    public void setImageAvt(String imageAvt) {
        this.imageAvt = imageAvt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
