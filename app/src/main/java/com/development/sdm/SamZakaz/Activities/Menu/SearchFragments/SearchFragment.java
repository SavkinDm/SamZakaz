package com.development.sdm.SamZakaz.Activities.Menu.SearchFragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.development.sdm.SamZakaz.Activities.Menu.NewMainClass;
import com.development.sdm.SamZakaz.Activities.Menu.RestaurantDetailsFragment;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;
import com.development.sdm.SamZakaz.SupportClasses.ImageUtil;
import com.development.sdm.SamZakaz.SupportClasses.KardviewAdapter;
import com.development.sdm.SamZakaz.SupportClasses.ListRestaurantsAdapter;
import com.development.sdm.SamZakaz.SupportClasses.RecyclerItemClickListener;
import com.development.sdm.SamZakaz.SupportClasses.Restaurant;
import com.development.sdm.SamZakaz.SupportClasses.RestaurantMainScreen;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment{
    ImageButton back;
    DataBaseHelper databasehelper;
    Bitmap photoOfRestaurant;
    View v;
    List<String> FavrestId;
    int i;
    SQLiteDatabase db;
    LinearLayoutManager llm1;
    RecyclerView spisokRest;
    ArrayList<Restaurant> favouriteRestaurants;
    ListRestaurantsAdapter adapter1;



    public SearchFragment() { }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FavrestId = new ArrayList<String>();

        favouriteRestaurants =new ArrayList<>();
        databasehelper = new DataBaseHelper(getContext());
        db = databasehelper.getWritableDatabase();
        v = inflater.inflate(R.layout.fragment_search, container, false);
        back = v.findViewById(R.id.backFromFavouriteRestaurants);
        Thread thread = new Thread() {
            public void run() {
                Cursor c = db.query("Restaurants", null, null, null, null, null, null);
                try {
                    if (c.moveToFirst()) {
                        // определяем номера столбцов по имени в выборке
                        int idColIndex = c.getColumnIndex("id");
                        int nameColIndex = c.getColumnIndex("name");
                        int RatingColIndex = c.getColumnIndex("Rating");
                        int photoColIndex = c.getColumnIndex("photo");
                        int logoColIndex = c.getColumnIndex("logo");
                        do {
                            int id = c.getInt(idColIndex);
                            String name = c.getString(nameColIndex);
                            int rating = c.getInt(RatingColIndex);
                            String photo = c.getString(photoColIndex);
                            String logo = c.getString(logoColIndex);
                            photoOfRestaurant = ImageUtil.convert(photo);
                            Bitmap logoOfRestaurant = ImageUtil.convert(logo);
                            Drawable photo1 = new BitmapDrawable(getResources(), photoOfRestaurant);
                            favouriteRestaurants.add(new Restaurant(id, name, rating, photo1, logoOfRestaurant));

                        } while (c.moveToNext());
                    } else {
                        c.close();
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         spisokRest =  v.findViewById(R.id.spisokRestoranov);
         llm1 = new LinearLayoutManager(getActivity());
         spisokRest.setLayoutManager(llm1);
         adapter1 = new ListRestaurantsAdapter(favouriteRestaurants);
         spisokRest.setAdapter(adapter1);
               // spisokRest.setItemAnimator(new DefaultItemAnimator());


        spisokRest.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), spisokRest ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView ChId1= view.findViewById(R.id.idView);
                        Restaurant rest1 = new Restaurant(Integer.parseInt(ChId1.getText().toString()));
                        ((NewMainClass)getActivity()).RestToOpen = rest1;
                        loadFragment(RestaurantDetailsFragment.newInstance());
                    }

                    @Override public void onLongItemClick(View view, int position) {

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




}


