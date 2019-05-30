package com.development.sdm.SamZakaz.Activities.authorActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.development.sdm.SamZakaz.Activities.Menu.NewMainClass;
import com.development.sdm.SamZakaz.Activities.StartScreenActivity;
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

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends Activity {
    Button btnLogin;
    int error;
    private ProgressDialog pDialog;
    EditText inputName;
    EditText inputPass;
    EditText inputLogin;
    EditText inputPhoneNumber;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "id";
    public int suc;
    DataBaseHelper databasehelper;
    private static String url_get_restaurans_list = "http://xyu.tmweb.ru/select_rests.php";
    JSONArray restaurants = null;
    private static final String TAG_FAVOURITE_RESTAURANTS = "favorrest";
    private static final String TAG_RESTAURANTS = "rests";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PHOTO = "";

    private static String url_registration = "http://xyu.tmweb.ru/regist_android.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        inputName = (EditText) findViewById(R.id.inputName);
        inputPass = (EditText) findViewById(R.id.inputPassword);
        inputLogin = (EditText) findViewById(R.id.inputLogin);

        inputPhoneNumber = (EditText) findViewById(R.id.inputPhoneNumber);
        databasehelper = new DataBaseHelper(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConnection(getApplicationContext())) {
                    new RegistrationActivity.RegistrationProcess().execute();
                }else{
                    Toast.makeText(getApplicationContext(), "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }
    class RegistrationProcess extends AsyncTask<String, String, String> {
        Bitmap ph;
        Target vm;
        String pid;
        boolean ready;
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Пожалуйста подождите...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {


            String login = inputLogin.getText().toString();
            String password = inputPass.getText().toString();
            String name = inputName.getText().toString();
           ;
            String phoneNumber = inputPhoneNumber.getText().toString();



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("login", login));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("PhoneNumber", phoneNumber));
            Log.d("params", String.valueOf(params));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json2 = jsonParser.makeHttpRequest(url_registration, "POST", params);


            Log.d("Create Response", json2.toString());

            // check for success tag
            try {
                int success = json2.getInt(TAG_SUCCESS);
                 error = json2.getInt("error");
                if (success == 1) {
                    // successfully created product
                    suc = 1;
                            /*
                            начинаем записывать id логин и пароль в бд в телефоне
                               */
                    ContentValues cv = new ContentValues();
                    SQLiteDatabase db = databasehelper.getWritableDatabase();
                                                                                                   // db.delete("client", null, null);
                    cv.put("login", login);
                    cv.put("password", password);
                    cv.put("name",name);
                    cv.put("PhoneNumber",phoneNumber);
                    cv.put("id", json2.getInt(TAG_ID));
                    long rowID = db.insert("client", null, cv);
                    Log.d("database", "row inserted, ID = " + rowID);
                    databasehelper.close();
                             /*
                                закончили запись в дб телефона
                              */

                    finish();
                } else {

                    suc = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (suc == 1) {
                Intent i = new Intent(getApplicationContext(), StartScreenActivity.class);
                startActivity(i);

            } else {
                switch (error){
                    case 1:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrationActivity.this);
                    builder1.setTitle("Упс!")
                            .setMessage("пользователь с таким логином уже существует")
                            .setIcon(R.drawable.krestik)
                            .setCancelable(false)
                            .setNegativeButton("ок",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                       /*
                                       ЧТО ДЕЛАТЬ ЕСЛИ ПАРОЛЬ ИЛИ ЛОГИН НЕ ВЕРНЫЕ
                                        */
                                        }
                                    });
                    AlertDialog alert1 = builder1.create();
                    alert1.show();
                    break;
                    case 2:
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(RegistrationActivity.this);
                        builder2.setTitle("Упс!")
                                .setMessage("Заполнены не все поля")
                                .setIcon(R.drawable.krestik)
                                .setCancelable(false)
                                .setNegativeButton("ок",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                       /*
                                       ЧТО ДЕЛАТЬ ЕСЛИ ПАРОЛЬ ИЛИ ЛОГИН НЕ ВЕРНЫЕ
                                        */
                                            }
                                        });
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                        break;
                }


            }

        }
    }
}
