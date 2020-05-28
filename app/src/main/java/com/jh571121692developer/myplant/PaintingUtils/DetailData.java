package com.jh571121692developer.myplant.PaintingUtils;

public class DetailData {
    private String name;
    private int date;
    private int imageResource;

    public DetailData(String name, int date, int imageResource) {
        this.name = name;
        this.date = date;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
