package com.development.sdm.SamZakaz.Activities.Menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;

import com.development.sdm.SamZakaz.Activities.Menu.AccountFragments.AccountFragment;
import com.development.sdm.SamZakaz.Activities.Menu.CartFragments.CartFragment;
import com.development.sdm.SamZakaz.Activities.Menu.HomeFragments.HomeFragment;
import com.development.sdm.SamZakaz.Activities.Menu.SearchFragments.SearchFragment;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.ListRestaurantsAdapter;
import com.development.sdm.SamZakaz.SupportClasses.Meal;
import com.development.sdm.SamZakaz.SupportClasses.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;

public class NewMainClass extends AppCompatActivity {
    Button adresBut1;
    public ArrayList<HashMap<String, Object>> SavedproductsList;
    public String vasya;
    public Restaurant RestToOpen;
    public  ArrayList<Restaurant> saveRestAdapter;
    public ArrayList<Meal> MealsToCart;
    public int RestCartId;
    public String OrderNumber;
    public ArrayList<Meal> MealsToPay1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_search:
                     loadFragment(SearchFragment.newInstance());
                    return true;
                case R.id.navigation_cart:
                    loadFragment(CartFragment.newInstance());
                    return true;
                case R.id.navigation_account:
                     loadFragment(AccountFragment.newInstance());
                    return true;
            }
            return false;
        }
    };



    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(HomeFragment.newInstance());
        MealsToCart =new ArrayList<>();
        MealsToPay1 =new ArrayList<>();
    }
}
