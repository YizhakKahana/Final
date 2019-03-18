package com.example.afinal;


import android.arch.lifecycle.ViewModel;

import java.io.Serializable;

public class Blog extends ViewModel implements Serializable {

    private String title;
    private String description;
    private String image;

    public Blog() {
    }

    public Blog(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}