package com.example.model;

public class tourLikeData {
    int imageUrl;
    String name;
    String like;

    public tourLikeData(int imageUrl, String name, String like) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.like = like;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
