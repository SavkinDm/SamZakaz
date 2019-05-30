package com.development.sdm.SamZakaz.Activities.Menu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.development.sdm.SamZakaz.Activities.Menu.HomeFragments.AllRestaurantsFragment;
import com.development.sdm.SamZakaz.Activities.Menu.HomeFragments.HomeFragment;
import com.development.sdm.SamZakaz.Activities.authorActivities.AuthorizationActivity;
import com.development.sdm.SamZakaz.Activities.authorActivities.AuthorizationOrRegistrationActivity;
import com.development.sdm.SamZakaz.Activities.authorActivities.RegistrationActivity;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.Cart;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;
import com.development.sdm.SamZakaz.SupportClasses.ImageUtil;
import com.development.sdm.SamZakaz.SupportClasses.JSONParser;
import com.development.sdm.SamZakaz.SupportClasses.ListMealsAdapter;
import com.development.sdm.SamZakaz.SupportClasses.ListRestaurantsAdapter;
import com.development.sdm.SamZakaz.SupportClasses.Meal;
import com.development.sdm.SamZakaz.SupportClasses.RecyclerItemClickListener;
import com.development.sdm.SamZakaz.SupportClasses.Restaurant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestaurantDetailsFragment extends Fragment {
    DataBaseHelper databasehelper;
    Restaurant thisRest;
    ImageView PhotoOfRest;
    TextView NameOfRest, DescriptionOfRest, RatingOfRest;
    ArrayList<Meal> MealsOfThisRest;
    boolean isDownloaded = true;
    JSONParser jsonParser = new JSONParser();
    private static String url_get_meals_list = "http://xyu.tmweb.ru/select_foods.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_REST_ID = "rest_id";
    private static final String TAG_FOODS = "foods";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_PRICE = "price";
    private static final String TAG_Logo = "logo";
    private ProgressDialog pDialog;
    int OpenedRestid;
    JSONArray meals = null;
    RecyclerView mealsView;
    LinearLayoutManager llm2;
    ListMealsAdapter adapter2, adapter3, adapter4;
    int CurrentCount = 1;
    View v;
    ImageView back;
    int pricenow, price11;



    ImageButton minus, plus;
    TextView priceOfcurrent, nameOfcurrent, numberOfcurrent, kolvo;
    ImageView bckgndAdd;
    Button addToCart, dontAdd;
    TextView ChId,ChName,ChPrice;



    public RestaurantDetailsFragment() {
    }

    public static RestaurantDetailsFragment newInstance() {
        return new RestaurantDetailsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        databasehelper = new DataBaseHelper(getContext());
        final SQLiteDatabase db = databasehelper.getWritableDatabase();
        OpenedRestid = ((NewMainClass) getActivity()).RestToOpen.getId();
        Log.d("Opened RestaurantDB id=", String.valueOf(OpenedRestid));
        String val[] = new String[1];
        val[0] = String.valueOf(OpenedRestid);
        Cursor c2 = db.rawQuery("SELECT * FROM Restaurants WHERE id=?", val);
        try {
            if (c2.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = c2.getColumnIndex("id");
                int nameColIndex = c2.getColumnIndex("name");
                int RatingColIndex = c2.getColumnIndex("Rating");
                int photoColIndex = c2.getColumnIndex("photo");
                int descriptionColIndex = c2.getColumnIndex("description");
                do {
                    String name = c2.getString(nameColIndex);
                    int rating = c2.getInt(RatingColIndex);
                    String photo = c2.getString(photoColIndex);
                    String description = c2.getString(descriptionColIndex);
                    Bitmap photoOfRestaurant = ImageUtil.convert(photo);
                    Drawable photo1 = new BitmapDrawable(getResources(), photoOfRestaurant);
                    thisRest = new Restaurant(name, rating, photo1, description);
                } while (c2.moveToNext());
            } else {
                c2.close();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        PhotoOfRest = v.findViewById(R.id.PhotoOfRest);
        NameOfRest = v.findViewById(R.id.NameOfRest1);
        DescriptionOfRest = v.findViewById(R.id.DescriptionOfRest1);
        RatingOfRest = v.findViewById(R.id.RatingOfRest);
        PhotoOfRest.setImageDrawable(thisRest.getPhoto());
        NameOfRest.setText(thisRest.getName());
        DescriptionOfRest.setText(thisRest.getDescription());
        RatingOfRest.setText(String.valueOf(thisRest.getRating()));
        back = v.findViewById(R.id.back_from_rest_details);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(AllRestaurantsFragment.newInstance());
            }
        });
        // конец отрисовки шапки


        MealsOfThisRest = new ArrayList<>();


        mealsView = v.findViewById(R.id.spisokBlud1);
        llm2 = new LinearLayoutManager(getActivity());
        mealsView.setLayoutManager(llm2);


        Cursor c3 = db.rawQuery("SELECT * FROM Meals WHERE Restid=?", val);
        try {
            if (c3.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = c3.getColumnIndex("id");
                int nameColIndex = c3.getColumnIndex("name");
                int PriceColIndex = c3.getColumnIndex("Price");
                int photoColIndex = c3.getColumnIndex("photo");
                int descriptionColIndex = c3.getColumnIndex("description");
                do {
                    int pid = c3.getInt(idColIndex);
                    String name = c3.getString(nameColIndex);
                    int price = c3.getInt(PriceColIndex);
                    String photo = c3.getString(photoColIndex);
                    String description = c3.getString(descriptionColIndex);
                    Bitmap photoOfRestaurant = ImageUtil.convert(photo);
                    Drawable photo1 = new BitmapDrawable(getResources(), photoOfRestaurant);
                    MealsOfThisRest.add(new Meal(pid, OpenedRestid, price, name, description, photo1));
                } while (c3.moveToNext());
            } else {
                c3.close();
                db.close();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            isDownloaded = false;
        }


        if (MealsOfThisRest.isEmpty()) {
            if (hasConnection(getContext())) {
                new RestaurantDetailsFragment.DownloadMeals1().execute();
            } else {
                Toast.makeText(getContext(), "Нет соединения с интернетом!", Toast.LENGTH_LONG).show();
                loadFragment(AllRestaurantsFragment.newInstance());
            }

        } else {
            Log.d("ne gruzil", "start");
            isDownloaded = true;


        }

        adapter2 = new ListMealsAdapter(MealsOfThisRest);
        mealsView.setAdapter(adapter2);
        mealsView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mealsView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                          ChId = view.findViewById(R.id.meal_id);
                          ChName = view.findViewById(R.id.meal_name);
                          ChPrice = view.findViewById(R.id.meal_price);

                        bckgndAdd = v.findViewById(R.id.addToCartBckgnd);
                        addToCart = v.findViewById(R.id.addToCart);
                        dontAdd = v.findViewById(R.id.dontAddToCart);
                        minus = v.findViewById(R.id.minus);
                        plus = v.findViewById(R.id.plus);
                        kolvo = v.findViewById(R.id.kolvo);
                        priceOfcurrent = v.findViewById(R.id.PriceOfcurrent);
                        nameOfcurrent = v.findViewById(R.id.nameOfcurrent);
                        numberOfcurrent = v.findViewById(R.id.numberOfcurrent);

                        nameOfcurrent.setText(ChName.getText().toString());
                        price11 = Integer.parseInt(ChPrice.getText().toString());
                        pricenow = price11;
                        priceOfcurrent.setText(String.valueOf((pricenow) + " " + Html.fromHtml(" &#x20bd")));
                        numberOfcurrent.setText(String.valueOf(CurrentCount));

                        nameOfcurrent.setVisibility(View.VISIBLE);
                        priceOfcurrent.setVisibility(View.VISIBLE);
                        numberOfcurrent.setVisibility(View.VISIBLE);
                        kolvo.setVisibility(View.VISIBLE);
                        plus.setVisibility(View.VISIBLE);
                        bckgndAdd.setVisibility(View.VISIBLE);
                        addToCart.setVisibility(View.VISIBLE);
                        dontAdd.setVisibility(View.VISIBLE);

                        plus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CurrentCount++;
                                numberOfcurrent.setText(String.valueOf(CurrentCount));
                                pricenow = pricenow + price11;
                                priceOfcurrent.setText(String.valueOf((pricenow) + " " + Html.fromHtml(" &#x20bd")));
                                minus.setVisibility(View.VISIBLE);
                            }
                        });
                        minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (CurrentCount > 1) {
                                    CurrentCount--;
                                    numberOfcurrent.setText(String.valueOf(CurrentCount));
                                    pricenow = pricenow - price11;
                                    priceOfcurrent.setText(String.valueOf((pricenow) + " " + Html.fromHtml(" &#x20bd")));
                                } else {
                                    minus.setVisibility(View.INVISIBLE);
                                }

                            }
                        });

                        addToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (((NewMainClass) getActivity()).MealsToCart.size() != 0) {
                                    Log.d("added", "1");
                                    Meal m = ((NewMainClass) getActivity()).MealsToCart.get(0);
                                    if (m.getRestId() != OpenedRestid) {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                        builder1.setTitle("Ужас")
                                                .setMessage("Ваша корзина будет очищена")
                                                .setIcon(R.drawable.krestik)
                                                .setCancelable(false)
                                                .setPositiveButton("ок", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        ((NewMainClass) getActivity()).MealsToCart.clear();
                                                        Meal meal2 = new Meal(Integer.parseInt(ChId.getText().toString()), CurrentCount, OpenedRestid);
                                                        ((NewMainClass) getActivity()).MealsToCart.add(meal2);
                                                        dialog.cancel();
                                                    }
                                                })
                                                .setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert1 = builder1.create();
                                        alert1.show();
                                    }else{
                                        Meal meal1 = new Meal(Integer.parseInt(ChId.getText().toString()), CurrentCount, OpenedRestid);
                                        ((NewMainClass) getActivity()).MealsToCart.add(meal1);
                                        Log.d("в корзине", String.valueOf(((NewMainClass) getActivity()).MealsToCart.size()));
                                    }
                                }else{
                                    Meal meal1 = new Meal(Integer.parseInt(ChId.getText().toString()), CurrentCount, OpenedRestid);
                                    ((NewMainClass) getActivity()).MealsToCart.add(meal1);
                                    Log.d("в корзине", String.valueOf(((NewMainClass) getActivity()).MealsToCart.size()));
                                }
                                nameOfcurrent.setVisibility(View.INVISIBLE);
                                priceOfcurrent.setVisibility(View.INVISIBLE);
                                numberOfcurrent.setVisibility(View.INVISIBLE);
                                minus.setVisibility(View.INVISIBLE);
                                plus.setVisibility(View.INVISIBLE);
                                kolvo.setVisibility(View.INVISIBLE);
                                bckgndAdd.setVisibility(View.INVISIBLE);
                                addToCart.setVisibility(View.INVISIBLE);
                                dontAdd.setVisibility(View.INVISIBLE);
                                CurrentCount = 1;
                            }
                        });
                        dontAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nameOfcurrent.setVisibility(View.INVISIBLE);
                                priceOfcurrent.setVisibility(View.INVISIBLE);
                                numberOfcurrent.setVisibility(View.INVISIBLE);
                                minus.setVisibility(View.INVISIBLE);
                                kolvo.setVisibility(View.INVISIBLE);
                                plus.setVisibility(View.INVISIBLE);
                                bckgndAdd.setVisibility(View.INVISIBLE);
                                addToCart.setVisibility(View.INVISIBLE);
                                dontAdd.setVisibility(View.INVISIBLE);
                                CurrentCount = 1;
                            }
                        });


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


        return v;
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("rest details", "on pause");

    }

    class DownloadMeals1 extends AsyncTask<String, String, String> {
        Bitmap ph;
        Target vm;
        Bitmap logo;
        Target vm1;
        String pid;
        boolean ready;

        DownloadMeals1() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter3 = new ListMealsAdapter(MealsOfThisRest);
            mealsView.setAdapter(adapter3);
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Пожалуйста подождите...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", String.valueOf(OpenedRestid)));
            Log.d("start get meals", "start");
            JSONObject json1 = jsonParser.makeHttpRequest(url_get_meals_list, "POST", params);
            Log.d("Response", json1.toString());
            ContentValues cv89 = new ContentValues();
            SQLiteDatabase db77 = databasehelper.getWritableDatabase();
            try {
                int success = json1.getInt(TAG_SUCCESS);
                if (success == 1) {
                    meals = json1.getJSONArray(TAG_FOODS);

                    for (int i = 0; i < meals.length(); i++) {
                        JSONObject c = meals.getJSONObject(i);
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Picasso.get().load("http://xyu.tmweb.ru/img/foods/" + pid + "/ph1.jpg").into(vm);//////////////////////cюда ссылку!!!! на фтп

                            }
                        });

                        ContentValues cv1 = new ContentValues();
                        SQLiteDatabase db1 = databasehelper.getWritableDatabase();
                        try {
                            cv1.put("id", c.getString(TAG_ID));
                            cv1.put("RestId", c.getString(TAG_REST_ID));
                            cv1.put("name", c.getString(TAG_NAME));
                            cv1.put("description", c.getString(TAG_DESCRIPTION));
                            cv1.put("Price", c.getString(TAG_PRICE));
                            do {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } while ((ph == null));
                            cv1.put("photo", ImageUtil.convert(ph));
                            Log.d("loaded", "photo");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        long rowID = db1.insert("Meals", null, cv1);
                        Log.d("database", "row inserted, ID = " + rowID);
                        databasehelper.close();
                        ph = null;
                        db1.close();
                        db77.close();
                        cv1.clear();

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            databasehelper.close();
            databasehelper = new DataBaseHelper(getContext());
            SQLiteDatabase db = databasehelper.getWritableDatabase();
            String val[] = new String[1];
            val[0] = String.valueOf(OpenedRestid);
            Cursor c3 = db.rawQuery("SELECT * FROM Meals WHERE Restid=?", val);
            try {
                if (c3.moveToFirst()) {
                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c3.getColumnIndex("id");
                    int nameColIndex = c3.getColumnIndex("name");
                    int PriceColIndex = c3.getColumnIndex("Price");
                    int photoColIndex = c3.getColumnIndex("photo");
                    int descriptionColIndex = c3.getColumnIndex("description");
                    do {
                        int pid = c3.getInt(idColIndex);
                        String name = c3.getString(nameColIndex);
                        int price = c3.getInt(PriceColIndex);
                        String photo = c3.getString(photoColIndex);
                        String description = c3.getString(descriptionColIndex);
                        Bitmap photoOfRestaurant = ImageUtil.convert(photo);
                        Drawable photo1 = new BitmapDrawable(getResources(), photoOfRestaurant);
                        MealsOfThisRest.add(new Meal(pid, OpenedRestid, price, name, description, photo1));
                    } while (c3.moveToNext());
                } else {
                    db.close();
                    c3.close();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                isDownloaded = false;
            }
            databasehelper.close();
            return "ok";
        }

        protected void onPostExecute(String file_url) {
            isDownloaded = true;
            pDialog.dismiss();
            Log.d("gruzil", "start");
            adapter2 = new ListMealsAdapter(MealsOfThisRest);
            mealsView.setAdapter(adapter2);

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
