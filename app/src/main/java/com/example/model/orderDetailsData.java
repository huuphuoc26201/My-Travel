package com.example.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class orderDetailsData {
    @PrimaryKey(autoGenerate = true)
    int id;
    String total;
    String name;
    String email;
    String phone;
    String tour;
    String thanhtoan;
    String dateTime;

    public orderDetailsData() {

    }

    public orderDetailsData(String total, String name, String email, String phone, String tour, String thanhtoan, String dateTime) {
        this.total = total;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.tour = tour;
        this.thanhtoan = thanhtoan;
        this.dateTime = dateTime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public String getThanhtoan() {
        return thanhtoan;
    }

    public void setThanhtoan(String thanhtoan) {
        this.thanhtoan = thanhtoan;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
