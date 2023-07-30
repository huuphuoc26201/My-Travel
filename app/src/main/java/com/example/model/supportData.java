package com.example.model;

public class supportData {
    public String id;
    public String name;
    public String phone;
    public String email;
    public String stkmomo;
    public supportData(){
    }

    public supportData(String id, String name, String phone, String email, String stkmomo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.stkmomo = stkmomo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStkmomo() {
        return stkmomo;
    }

    public void setStkmomo(String stkmomo) {
        this.stkmomo = stkmomo;
    }
}
