package com.development.sdm.SamZakaz.Activities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.sdm.SamZakaz.Activities.Menu.NewMainClass;
import com.development.sdm.SamZakaz.Activities.authorActivities.AuthorizationOrRegistrationActivity;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;
import com.development.sdm.SamZakaz.SupportClasses.ImageUtil;
import com.development.sdm.SamZakaz.SupportClasses.JSONParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StartScreenActivity extends AppCompatActivity {
    Button btnlogInto;
    Button btnRegistration;
    private static String url_login = "http://xyu.tmweb.ru/login_android.php";
    private static String url_get_restaurans_list = "http://xyu.tmweb.ru/select_rests.php";
    private ProgressDialog pDialog;
    JSONArray restaurants = null;
    JSONParser jsonParser = new JSONParser();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FAVOURITE_RESTAURANTS = "favorrest";
    private static final String TAG_RESTAURANTS = "rests";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_RATING = "rating";
    private static final String TAG_Logo = "logo";
    private static final String TAG_PHOTO = "";
    Thread thread, thread1;
    public int suc = 5;
    DataBaseHelper databasehelper;
    JSONArray favouriteRest;
    String login;
    String password;
    Intent kk;
    ImageView logotip;
    TextView ShowText;
    int succc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen_activity);
        databasehelper = new DataBaseHelper(this);
        logotip = findViewById(R.id.start_screen_logo);
        ShowText = findViewById(R.id.first_seen_text);
        logotip.setImageResource(R.drawable.logotipv1);

        thread1 = new Thread() {
            public void run() {
                DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
                String date = dfDate.format(Calendar.getInstance().getTime());
                DateFormat dfTime = new SimpleDateFormat("HH.mm");
                String time = dfTime.format(Calendar.getInstance().getTime());
                float curTime = Float.parseFloat(time) + 3;
                Log.d("time", String.valueOf(curTime));
                String name1;
                logotip.setImageResource(R.drawable.logo1488);
                SQLiteDatabase db32 = databasehelper.getWritableDatabase();
                Cursor c22 = db32.query("client", null, null, null, null, null, null);

                if (c22.moveToLast()) {
                    int nameCol = c22.getColumnIndex("name");
                    name1 = c22.getString(nameCol);
                } else {
                    name1 = "";
                }
                c22.close();
                db32.close();
                databasehelper.close();
                if (name1 != null) {
                    if ((curTime > 6) && (curTime <= 11)) {
                        ShowText.setText("Доброе утро, " + name1 + "!");
                    } else if ((curTime > 11) && (curTime <= 18)) {
                        ShowText.setText("Добрый день, " + name1 + "!");
                    } else if ((curTime > 18) && (curTime <= 23.59)) {
                        ShowText.setText("Добрый вечер, " + name1 + "!");
                    } else if ((curTime >= 0) && (curTime <= 5.59)) {
                        ShowText.setText("Доброй ночи, " + name1 + "!");
                    }
                } else {
                    if ((curTime > 6) && (curTime <= 11)) {
                        ShowText.setText("Доброе утро!");
                    } else if ((curTime > 11) && (curTime <= 18)) {
                        ShowText.setText("Добрый день!");
                    } else if ((curTime > 18) && (curTime <= 23.59)) {
                        ShowText.setText("Добрый вечер!");
                    } else if ((curTime >= 0) && (curTime <= 5.59)) {
                        ShowText.setText("Доброй ночи!");
                    }
                }
                try {
                    thread1.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                thread.start();
            }

        };
        thread1.start();


        thread = new Thread() {

            public void run() {
                try {
                    thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (hasConnection(getApplicationContext())) {
                    new StartScreenActivity.AuthorizationProcess11().execute();

                } else {
//                        Toast.makeText(getApplicationContext(), "Нет соединения с интернетом!", Toast.LENGTH_LONG).show();
                    while (!hasConnection(getApplicationContext())) {
                        try {
                            thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    new StartScreenActivity.AuthorizationProcess11().execute();
                }


            }
        };



        // new StartScreenActivity.AuthorizationProcess().execute();
    }

    class AuthorizationProcess11 extends AsyncTask<String, String, String> {
        Bitmap ph;
        Target vm;
        Bitmap logo;
        Target vm1;
        String pid;
        boolean ready;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // logotip.setImageResource(R.drawable.logo1488);

        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {



            /*
            берем логин и пароль из базы данных в телефоне
             */
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = databasehelper.getWritableDatabase();
            // db.delete("client", null, null);

            Cursor c2 = db.query("client", null, null, null, null, null, null);
            if (c2.moveToLast()) {
                int loginCol = c2.getColumnIndex("login");
                int passCol = c2.getColumnIndex("password");


                login = c2.getString(loginCol);
                password = c2.getString(passCol);

            } else {
                suc = 0;

            }
            c2.close();
            db.close();
            databasehelper.close();

            // Building Parameters

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("login", login));
            params.add(new BasicNameValuePair("password", password));


            Log.d("params", String.valueOf(params));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login, "POST", params);


            Log.d("Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product

                    suc = 1;
                    succc = 1;
                    favouriteRest = json.getJSONArray(TAG_FAVOURITE_RESTAURANTS);
                    if (favouriteRest != null) {


                        ContentValues cv9 = new ContentValues();
                        SQLiteDatabase db9 = databasehelper.getWritableDatabase();
                        db9.delete("favouriteRestaurants", null, null);
                        for (int i = 0; i < favouriteRest.length(); i++) {
                            String a = (String) favouriteRest.get(i);
                            cv9.put("id", a);
                            long rowID = db9.insert("favouriteRestaurants", null, cv9);
                            Log.d("favouriteRestaurants", "number of fav rest " + rowID);
                        }
                        //db9.delete("Restaurants", null, null);
                        databasehelper.close();
                    }
                    // closing this screen
                } else {
                    // failed to create product
                    suc = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//////////////////////////////////////////закончили с авторизацией если прошла парсим список ресторанов
            if (suc == 1) {
                Log.d("start get restaurants", "start");
                JSONObject json1 = jsonParser.makeHttpRequest(url_get_restaurans_list, "GET", params);

                Log.d("Response", json1.toString());
                ContentValues cv89 = new ContentValues();
                SQLiteDatabase db77 = databasehelper.getWritableDatabase();
                Cursor c3 = db77.query("Restaurants", null, null, null, null, null, null);
                c3.getCount();
                try {
                    int success = json1.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        restaurants = json1.getJSONArray(TAG_RESTAURANTS);
                        if (restaurants.length() != c3.getCount()) {
                            db77.delete("Restaurants", null, null);
                            for (int i = 0; i < restaurants.length(); i++) {
                                JSONObject c = restaurants.getJSONObject(i);
                                pid = c.getString(TAG_ID);

                                vm = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        ph = bitmap; // создание bitmap из таргета
                                        Log.d("onBitmapLoaded photo", String.valueOf(ph));
                                        ready = true;
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                        Log.d("faile photo", String.valueOf(ph));
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        Log.d("prepare photo", String.valueOf(ph));

                                    }
                                };
                                // Загрузка фотографий и добавление их в объект класса Target
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Picasso.get().load("http://xyu.tmweb.ru/img/rests/" + pid + "/ph1.jpg").error(R.drawable.net_photo).into(vm);//////////////////////cюда ссылку!!!! на фтп

                                    }
                                });

                                vm1 = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        logo = bitmap; // создание bitmap из таргета
                                        Log.d("onBitmapLoaded logo", String.valueOf(logo));
                                        ready = true;
                                    }

                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                        Log.d("faile logo", String.valueOf(logo));
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        Log.d("prepare logo", String.valueOf(logo));

                                    }
                                };
                                // Загрузка фотографий и добавление их в объект класса Target
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Picasso.get().load("http://xyu.tmweb.ru/img/rests/" + pid + "/logo.png").error(R.drawable.net_photo).into(vm1);//////////////////////cюда ссылку!!!! на фтп

                                    }
                                });

                                ContentValues cv1 = new ContentValues();
                                SQLiteDatabase db1 = databasehelper.getWritableDatabase();
                                try {
                                    cv1.put("id", c.getString(TAG_ID));
                                    cv1.put("name", c.getString(TAG_NAME));
                                    cv1.put("description", c.getString(TAG_DESCRIPTION));
                                    cv1.put("Rating", c.getString(TAG_RATING));
                                    do {
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } while ((ph == null) || (logo == null));
                                    cv1.put("photo", ImageUtil.convert(ph));
                                    Log.d("loaded", "photo");
                                    cv1.put("logo", ImageUtil.convert(logo));
                                    Log.d("loaded", "logo");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                long rowID = db1.insert("Restaurants", null, cv1);
                                Log.d("database", "row inserted, ID = " + rowID);
                                databasehelper.close();
                                ph = null;
                            }

                        } else {
                            Log.d("restaurants UPDATE", "same count, don't need to update");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                databasehelper.close();
            }


            return null;
        }

        protected void onPostExecute(String file_url) {

            if (succc == 1) {
                Intent ii = new Intent(getApplicationContext(), NewMainClass.class);
                startActivity(ii);

            } else {

                kk = new Intent(getApplicationContext(), AuthorizationOrRegistrationActivity.class);
                startActivity(kk);


            }

        }
    }


    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }
}