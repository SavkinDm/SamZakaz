package com.development.sdm.SamZakaz.SupportClasses;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Restaurant {
         int id;
        String name;
        double Rating;
        String description;
        Bitmap logo;
        Drawable photo;

        public Restaurant(int id, String name, int Rating, Drawable photo, Bitmap logo) {
                this.name = name;
                this.id = id;
                this.Rating = Rating;
                this.photo = photo;
                this.logo = logo;

        }
        public Restaurant( String name, double Rating, Drawable photo, Bitmap logo) {
                this.name = name;

                this.Rating = Rating;
                this.photo = photo;
                this.logo = logo;

        }
        public Restaurant(int pid) {
                this.id = pid;
        }


        public int getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public double getRating() {
                return Rating;
        }

        public String getDescription() {
                return description;
        }

        public Bitmap getLogo() {
                return logo;
        }

        public Drawable getPhoto() {
                return photo;
        }

        public Restaurant(String name, int Rating, Drawable photo, String description) {
                this.name = name;
                this.Rating = Rating;
                this.photo = photo;
                this.description = description;


        }
}