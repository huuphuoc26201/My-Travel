package com.example.model;

public class bookHotelData {
    String name;
    String phone;
    String email;
    String persons;
    String child;
    String numberRooms;
    String fromdate;
    String todate;
    String total;
    String payment;
    String bookDate;
    String bookHotel;
    String id;

    public bookHotelData() {
    }

    public bookHotelData(String name, String phone, String email, String persons, String child, String numberRooms, String fromdate, String todate, String total, String payment, String bookDate, String bookHotel, String id) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.persons = persons;
        this.child = child;
        this.numberRooms = numberRooms;
        this.fromdate = fromdate;
        this.todate = todate;
        this.total = total;
        this.payment = payment;
        this.bookDate = bookDate;
        this.bookHotel = bookHotel;
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

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getNumberRooms() {
        return numberRooms;
    }

    public void setNumberRooms(String numberRooms) {
        this.numberRooms = numberRooms;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookHotel() {
        return bookHotel;
    }

    public void setBookHotel(String bookHotel) {
        this.bookHotel = bookHotel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
