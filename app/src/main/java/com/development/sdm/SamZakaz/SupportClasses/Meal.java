package com.development.sdm.SamZakaz.SupportClasses;

import android.graphics.drawable.Drawable;

public class Meal {
    private int id;
    private int RestId;
    private int Price;
    private String name;
    private String description;
    private Drawable photo;
    private int count;

    public Meal(int id, int restId, int price, String name, String description, Drawable photo) {
        this.id = id;
        RestId = restId;
        Price = price;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public Meal(int id, int restId, int price, String name, Drawable photo, int count) {
        this.id = id;
        RestId = restId;
        Price = price;
        this.name = name;
        this.photo = photo;
        this.count = count;
    }

    public Meal() { }
    public Meal(int id) { this.id = id;}

    public Meal(int id, int count, int RestId) {
        this.id = id;
        this.RestId = RestId;
        this.count = count;
    }
    public Meal(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public int getRestId() {
        return RestId;
    }

    public int getPrice() {
        return Price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Drawable getPhoto() {
        return photo;
    }
}
