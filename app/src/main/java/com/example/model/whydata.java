package com.example.model;

public class whydata {
    int icon;
    String tieude;
    String ndung;

    public whydata(int icon, String tieude, String ndung) {
        this.icon = icon;
        this.tieude = tieude;
        this.ndung = ndung;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getNdung() {
        return ndung;
    }

    public void setNdung(String ndung) {
        this.ndung = ndung;
    }
}
