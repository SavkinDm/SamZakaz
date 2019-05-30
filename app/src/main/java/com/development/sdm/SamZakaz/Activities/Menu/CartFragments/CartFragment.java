package com.development.sdm.SamZakaz.Activities.Menu.CartFragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.development.sdm.SamZakaz.Activities.Menu.HomeFragments.AllRestaurantsFragment;
import com.development.sdm.SamZakaz.Activities.Menu.NewMainClass;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;
import com.development.sdm.SamZakaz.SupportClasses.ImageUtil;
import com.development.sdm.SamZakaz.SupportClasses.ListCartAdapter;
import com.development.sdm.SamZakaz.SupportClasses.ListRestaurantsAdapter;
import com.development.sdm.SamZakaz.SupportClasses.Meal;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    ArrayList<Meal> CartMeals;
    DataBaseHelper databasehelper;
    RecyclerView spisokRest;
    LinearLayoutManager llm1;
    ListCartAdapter adapter12;
    Cursor c7;
    ArrayList<Meal> MealsToPay;

    public CartFragment() {
    }

    Button zakazatbutton;
    ChoosePaymentFragment choosePaymentFragment;
    FragmentTransaction fTrans;
    View v;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("cart fragment", "start");
        databasehelper = new DataBaseHelper(getContext());
        final SQLiteDatabase db = databasehelper.getWritableDatabase();

        CartMeals = new ArrayList<>();

        if (((NewMainClass) getActivity()).MealsToCart.size() != 0) {

            v = inflater.inflate(R.layout.fragment_cart, container, false);
            zakazatbutton = v.findViewById(R.id.zakazatButton);
            zakazatbutton.setVisibility(View.VISIBLE);
            for (int i = 0; i < ((NewMainClass) getActivity()).MealsToCart.size(); ++i) {
                Meal meal = ((NewMainClass) getActivity()).MealsToCart.get(i);
                int id = meal.getId();
                int Restid = meal.getRestId();
                int count = meal.getCount();
                String val[] = new String[1];
                val[0] = String.valueOf(id);
                c7 = db.rawQuery("SELECT * FROM Meals WHERE id=?", val);
                try {
                    if (c7.moveToFirst()) {
                        // определяем номера столбцов по имени в выборке
                        int idColIndex = c7.getColumnIndex("id");
                        int nameColIndex = c7.getColumnIndex("name");
                        int PriceColIndex = c7.getColumnIndex("Price");
                        int photoColIndex = c7.getColumnIndex("photo");
                        do {
                            int pid = c7.getInt(idColIndex);
                            String name = c7.getString(nameColIndex);
                            int price = c7.getInt(PriceColIndex);
                            String photo = c7.getString(photoColIndex);
                            Bitmap photoOfRestaurant = ImageUtil.convert(photo);
                            Drawable photo1 = new BitmapDrawable(getResources(), photoOfRestaurant);
                            CartMeals.add(new Meal(pid, Restid, price, name, photo1, count));
                        } while (c7.moveToNext());
                    } else {

                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                c7.close();

            }
            spisokRest = v.findViewById(R.id.korzina);
            llm1 = new LinearLayoutManager(getActivity());
            spisokRest.setLayoutManager(llm1);
            adapter12 = new ListCartAdapter(CartMeals);
            spisokRest.setAdapter(adapter12);

            zakazatbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zakazatbutton.setVisibility(View.INVISIBLE);
                    choosePaymentFragment = new ChoosePaymentFragment();
                    fTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    fTrans.replace(R.id.fragmentContainer, choosePaymentFragment);
                    fTrans.commit();
                    MealsToPay = adapter12.getMealsToCart();
                    ((NewMainClass) getActivity()).MealsToPay1 = MealsToPay;
                    for (int i = 0; i < MealsToPay.size(); ++i) {
                        Meal a = MealsToPay.get(i);
                        Log.d("cart element ", a.getName() + " " + "count " + a.getCount());

                    }
                    zakazatbutton.setVisibility(View.VISIBLE);
                }
            });
        } else{
            v = inflater.inflate(R.layout.fragment_cart_empty, container, false);
        }



        db.close();
        return v;
    }



    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }


}
