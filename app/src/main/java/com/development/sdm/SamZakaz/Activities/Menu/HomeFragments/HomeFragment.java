package com.development.sdm.SamZakaz.Activities.Menu.HomeFragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Switch;


import com.development.sdm.SamZakaz.Activities.Menu.NewMainClass;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;
import com.development.sdm.SamZakaz.SupportClasses.JSONParser;
import com.development.sdm.SamZakaz.SupportClasses.KardviewAdapter;
import com.development.sdm.SamZakaz.SupportClasses.RecyclerItemClickListener;
import com.development.sdm.SamZakaz.SupportClasses.RestaurantMainScreen;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment  {



    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, Object>> productsList;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_EXTERNAL_INFORMATION = "ExternalInf";
    private static final String TAG_TYPE_OF_FLAT = "TypeOfFlat";
    private static final String TAG_KVADR_METERS = "KvadrMeters";
    private static final String TAG_STREET = "Street";
    private static final String TAG_HOUSE = "house";
    private static final String TAG_PHOTO= "photo";
    private static final String TAG_PRICE_FOR_HOUR= "PriceForHour";
    private static final String TAG_PRICE_FOR_DAY= "PriceForDay";
    private static final String TAG_OWNER= "NameOfOwner";
    private static final String TAG_PHONE_NUMBER= "PhoneNumber";
    private static final String TAG_READY_FOR_RENT = "readyForRent";

    JSONArray products = null;
    DataBaseHelper databasehelper;
    ImageView mimageView;
    String ownerLocal;

    SimpleAdapter adapter;
    String iD;
    ImageButton refreshBtn;
    Switch ReadyStatus;
    int schet;




    public HomeFragment() { }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        setRetainInstance(true);

        //ReadyStatus = v.findViewById(R.id.ReadyStatus);
        databasehelper = new DataBaseHelper(getContext());
        productsList = new ArrayList<HashMap<String, Object>>();
        // ReadyStatus.setOnCheckedChangeListener(this);

        RecyclerView rv =  v.findViewById(R.id.my_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
                ArrayList<RestaurantMainScreen> restaurantMainScreens;
                restaurantMainScreens =new ArrayList<>();
                restaurantMainScreens.add(new RestaurantMainScreen("Избранное","хожу в него с 2 лет все збс вкусно как в раю", R.drawable.favourites));
                restaurantMainScreens.add(new RestaurantMainScreen("Ресторан дня ","не самый лучший ресторан но все же мы выбрали его P.S он просто закинул нам бабла", R.drawable.rest_of_day));
                restaurantMainScreens.add(new RestaurantMainScreen("Все рестораны","а тут может быть ваш ресторан, всего то надо задонатить нам немного", R.drawable.all_rests));
        KardviewAdapter adapter1 = new KardviewAdapter(restaurantMainScreens);
        rv.setAdapter(adapter1);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        switch (position){
                            case 0:
                                loadFragment(FavouriteRestaurantsFragment.newInstance());
                                break;
                            case 1:

                                break;
                            case 2:
                                loadFragment(AllRestaurantsFragment.newInstance());
                                break;
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );


        return v;
    }





    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right);
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
    @Override
    public void onPause() {
        super.onPause();
        ((NewMainClass)getActivity()).SavedproductsList = productsList;
        Log.d("HomeFragment", "pause set");

    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        productsList =  ((NewMainClass)getActivity()).SavedproductsList;
        Log.d("HomeFragment", "resume set");
        adapter = new SimpleAdapter(
                getActivity(), productsList,
                R.layout.list_item, new String[] {TAG_PRICE_FOR_HOUR ,TAG_PHOTO, TAG_NAME,   " ", TAG_PRICE_FOR_DAY, },
                new int[] {R.id.PriceForHourShow, R.id.MainPhotoOfRent, R.id.NameOfRent,  R.id.PriceForDayShow,});

        adapter.setViewBinder(new HomeFragmentViewBinder()); //переопределен метод для отображения фото
        setListAdapter(adapter);
*/

    }


// асинк таск для загрузки данных о любимом(ых) ресторане их id можно хранить в базе например

}