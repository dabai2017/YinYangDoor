package com.dabai.TaiChi.utils;

import android.graphics.drawable.Drawable;

public class Fruit {
    private String name;
    private Drawable imageId;

    public Fruit(String name, Drawable imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public Drawable getImage() {
        return imageId;
    }
}