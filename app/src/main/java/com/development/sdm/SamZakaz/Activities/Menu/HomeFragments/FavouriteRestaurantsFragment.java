package com.development.sdm.SamZakaz.Activities.Menu.HomeFragments;

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

public class FavouriteRestaurantsFragment   extends Fragment {
ImageButton back;
DataBaseHelper databasehelper;
Bitmap photoOfRestaurant;
    View v;
    List<String> FavrestId;
    int i;
    public FavouriteRestaurantsFragment() { }

    public static FavouriteRestaurantsFragment newInstance() {
        return new FavouriteRestaurantsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FavrestId = new ArrayList<String>();
        ArrayList<Restaurant> favouriteRestaurants;
        favouriteRestaurants =new ArrayList<>();
        databasehelper = new DataBaseHelper(getContext());
        SQLiteDatabase db = databasehelper.getWritableDatabase();
        i=1;
        Cursor c = db.query("favouriteRestaurants", null, null, null, null, null, null);
        try {
            // ставим позицию курсора на первую строку выборки
            // если в выборке нет строк, вернется false
            if (c.moveToFirst()) {
                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                do {
                    FavrestId.add(String.valueOf(c.getInt(idColIndex)));
                    i++;
                } while (c.moveToNext());
            } else {
                c.close();
            }
            String val[] = new String[1];
            Log.d("FavrestId", String.valueOf( FavrestId));
            for (int k = 0; k < FavrestId.size(); ++k) {

            val[0] =  FavrestId.get(k);
                Log.d("val",  val[0]);

            Cursor c1 = db.rawQuery("SELECT * FROM Restaurants WHERE id=?",val);
                try {
                    if (c1.moveToFirst()) {
                        // определяем номера столбцов по имени в выборке
                        int idColIndex = c1.getColumnIndex("id");
                        int nameColIndex = c1.getColumnIndex("name");
                        int RatingColIndex = c1.getColumnIndex("Rating");
                        int photoColIndex = c1.getColumnIndex("photo");
                        int logoColIndex = c1.getColumnIndex("logo");
                        do {
                            String name = c1.getString(nameColIndex);
                            int rating = c1.getInt(RatingColIndex);
                            String photo = c1.getString(photoColIndex);
                            String logo = c1.getString(logoColIndex);
                            photoOfRestaurant = ImageUtil.convert(photo);
                            Bitmap logoOfRestaurant = ImageUtil.convert(logo);
                            Drawable photo1 = new BitmapDrawable(getResources(),photoOfRestaurant);
                            favouriteRestaurants.add(new Restaurant(name, rating, photo1,logoOfRestaurant ));
                        } while (c1.moveToNext());
                    } else {
                        c1.close();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

        }
            v = inflater.inflate(R.layout.favourite_restaurants, container, false);
            back = v.findViewById(R.id.backFromFavouriteRestaurants);

        }catch (NullPointerException e){
            e.printStackTrace();
             v = inflater.inflate(R.layout.favourite_restaurants_not_found, container, false);
             back = v.findViewById(R.id.backFromFavouriteRestaurants2);
        }

        RecyclerView spisokRest =  v.findViewById(R.id.my_recycler_view1);
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        spisokRest.setLayoutManager(llm1);
        ListRestaurantsAdapter adapter2 = new ListRestaurantsAdapter(favouriteRestaurants);
        spisokRest.setAdapter(adapter2);
        spisokRest.setItemAnimator(new DefaultItemAnimator());
        spisokRest.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), spisokRest ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Restaurant rest = new Restaurant(position+1);
                        ((NewMainClass)getActivity()).RestToOpen = rest;
                        loadFragment(RestaurantDetailsFragment.newInstance());
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );








        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(HomeFragment.newInstance());
            }
        });


        return v;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

}
