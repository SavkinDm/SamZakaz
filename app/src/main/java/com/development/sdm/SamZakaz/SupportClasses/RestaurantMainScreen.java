package com.development.sdm.SamZakaz.SupportClasses;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class RestaurantMainScreen {
    String name;
    String description;
    int photoId;
    Bitmap photo;
   public RestaurantMainScreen(String name, String description, int photoId) {
        this.name = name;
        this.description = description;
        this.photoId = photoId;
    }

    public RestaurantMainScreen(String name, String description, Bitmap photo) {
        this.name = name;
        this.description = description;
        this.photo = photo;
    }
}
