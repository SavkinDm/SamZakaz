package com.development.sdm.SamZakaz.SupportClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        // конструктор суперкласса
        super(context, "client", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("database", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table client ("
                + "id integer,"
                + "login text,"
                + "password text,"
                + "name text,"
                + "surname text,"
                + "photo text,"
                + "PhoneNumber text"+ ");");
        db.execSQL("create table favouriteRestaurants ("
                + "id integer" + ");");
        db.execSQL("create table Restaurants ("
                + "id integer,"
                + "Rating float,"
                + "name text,"
                + "description text,"
                + "logo text,"
                + "photo text"+ ");");
        db.execSQL("create table Meals ("
                + "id integer,"
                + "Restid integer,"
                + "Price integer,"
                + "name text,"
                + "description text,"
                + "photo text"+ ");");
        db.execSQL("create table Cart ("
                + "id integer,"
                + "name text,"
                + "description text,"
                + "photo bitmap"+ ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
