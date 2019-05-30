package com.development.sdm.SamZakaz.Activities.authorActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.development.sdm.SamZakaz.Activities.StartScreenActivity;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;
import com.development.sdm.SamZakaz.SupportClasses.JSONParser;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationActivity extends Activity {
    Button btnLogin;
    private ProgressDialog pDialog;
    EditText inputName;
    EditText inputPass;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_MESSAGE = "message";
    public int suc;
    DataBaseHelper databasehelper;
    private static String url_login = "http://xyu.tmweb.ru/login_android.php";
    private static String url_get_restaurans_list = "http://xyu.tmweb.ru/select_rests.php";
    JSONArray restaurants = null;
    private static final String TAG_FAVOURITE_RESTAURANTS = "favorrest";
    private static final String TAG_RESTAURANTS = "rests";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PHOTO = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization_activity);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        inputName = (EditText) findViewById(R.id.inputLogin);
        inputPass = (EditText) findViewById(R.id.inputPassword);

        databasehelper = new DataBaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasConnection(getApplicationContext())) {
                    new AuthorizationProcess().execute();
                }else{
                    Toast.makeText(getApplicationContext(), "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    class AuthorizationProcess extends AsyncTask<String, String, String> {
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

        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {


            String name = inputName.getText().toString();
            String password = inputPass.getText().toString();

            // Building Parameters

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("login", name));
            params.add(new BasicNameValuePair("password", password));


            Log.d("params", String.valueOf(params));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login, "POST", params);


            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    suc = 1;
 /*
                            начинаем записывать id логин и пароль в бд в телефоне
                               */
                    ContentValues cv = new ContentValues();
                    SQLiteDatabase db = databasehelper.getWritableDatabase();
                    cv.put("login", name);
                    cv.put("password", password);
                    cv.put("id", json.getInt(TAG_USERID));
                    long rowID = db.insert("client", null, cv);
                    Log.d("database", "row inserted, ID = " + rowID);
                    db.close();
                    databasehelper.close();
                             /*
                                закончили запись в дб телефона
                              */


                    // closing this screen
                    finish();
                } else {
                    // failed to create product
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

            if (suc == 1) {
                Intent i = new Intent(getApplicationContext(), StartScreenActivity.class);
                startActivity(i);

            } else {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(AuthorizationActivity.this);
                builder1.setTitle("Провал!")
                        .setMessage("неверный логин или пароль!")
                        .setIcon(R.drawable.krestik)
                        .setCancelable(false)
                        .setNegativeButton("fuck",
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

            }

        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( pDialog!=null && pDialog.isShowing() ){
            pDialog.cancel();
        }
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

}
