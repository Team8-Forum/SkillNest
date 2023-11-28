package com.example.skillnest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "image")
    private String image;

    public Image() {
    }

    public Image(int userId, String image) {
        this.userId = userId;
        this.image = image;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}