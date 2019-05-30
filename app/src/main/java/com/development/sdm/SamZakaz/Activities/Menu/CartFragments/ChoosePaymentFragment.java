package com.development.sdm.SamZakaz.Activities.Menu.CartFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.development.sdm.SamZakaz.Activities.Menu.HomeFragments.HomeFragment;
import com.development.sdm.SamZakaz.Activities.Menu.NewMainClass;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.JSONParser;
import com.development.sdm.SamZakaz.SupportClasses.Meal;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChoosePaymentFragment extends Fragment {
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FILIALS = "info";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";
    private static final String TAG_ORDERNUMBER = "OrderNumber";
    private static String url_getfilials = "http://xyu.tmweb.ru/testing/select_all_branches_info.php";
    private static String url_send_zakaz = "http://xyu.tmweb.ru/testing/create_order.php";
    ImageButton close;
    String[] FilialTypes ={"Выберите ресторан выдачи", " "};
    List<String> xFilialTypes;
    int[] FilialTypesID= {1,2,3,4};
    String TypeOfFilial;
    String idi, counti;
    View v;
    int suc, FilialTypesIDi;
    JSONArray filials = null;
    Button AgreePayment;
    int ChoosePayment;
    public ChoosePaymentFragment() {
    }

    public static ChoosePaymentFragment newInstance() {
        return new ChoosePaymentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_choose_payment_cart, container, false);
        xFilialTypes = new ArrayList();
        xFilialTypes.add("Выберите ресторан выдачи");
        if (hasConnection(getContext())) {
            new ChoosePaymentFragment.GetFilialsProcess().execute();
        }else{
            Toast.makeText(getContext(), "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
            xFilialTypes.add(1, "Нет соединения с интернетом!");
        }
        AgreePayment=v.findViewById(R.id.TakePaymentButton);
        close = v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
              v.setVisibility(View.INVISIBLE);
            }
        });


        /*
        описание выпадающего списка с филиалами
         */
        Spinner spin = (Spinner) v.findViewById(R.id.AdressOfFilial);
        ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, xFilialTypes);
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(menuAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                List<String> choose = xFilialTypes;
                TypeOfFilial = choose.get(selectedItemPosition);
                FilialTypesIDi = FilialTypesID[selectedItemPosition];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ChoosePayment=1;
        /*
       конец  описания выпадающего списка с филиалами
         */

        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup14);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.ByCreditCard:
                         ChoosePayment =1;
                        break;
                    case R.id.InRestaurant:
                         ChoosePayment=2;

                        String id, count;
                        Meal a;
                        for (int i=1; i<=((NewMainClass)getActivity()).MealsToPay1.size(); ++i){
                            a =  ((NewMainClass)getActivity()).MealsToPay1.get(i-1);
                            id = String.valueOf(a.getId());
                            count = String.valueOf(a.getCount());
                            if (i==1){
                                idi=id;
                                counti=count;
                            }else{
                                idi=idi+"; "+ id;
                                counti=counti+"; "+ count;
                            }


                        }
                        Log.d("id", idi);
                        Log.d("count", counti);
                        break;

                }
            }
        });





        AgreePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if((!TypeOfFilial.isEmpty())||(ChoosePayment!=0)||(!TypeOfFilial.equalsIgnoreCase(xFilialTypes.get(0)))){
                     new ChoosePaymentFragment.AcceptedZakazProcess().execute();                                                              // раскоментить при появлении скрипта оформления заказа
                    v.setVisibility(View.INVISIBLE);
                }

            }
        });


        return v;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    class GetFilialsProcess extends AsyncTask<String, String, String> {
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


           Meal a =  ((NewMainClass)getActivity()).MealsToPay1.get(0);

            String id = String.valueOf(a.getRestId());

            // Building Parameters

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id));

            JSONObject json = jsonParser.makeHttpRequest(url_getfilials, "POST", params);


            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    suc = 1;
                    filials = json.getJSONArray(TAG_FILIALS);

                        for (int i = 0; i < filials.length(); i++) {
                            JSONObject c = filials.getJSONObject(i);
                            String name = c.getString(TAG_NAME);
                            String pid = c.getString(TAG_ID);
                            FilialTypesID[i] = Integer.parseInt(pid);
                                xFilialTypes.add(name);


                        }
                } else {
                    // failed to create product
                    suc = 0;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            if (suc == 1) {


            } else {



            }

        }
    }

    class AcceptedZakazProcess extends AsyncTask<String, String, String> {

        String id, count;
        Meal a;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();


            params.add(new BasicNameValuePair("branch_id", String.valueOf(FilialTypesIDi-1)));
            params.add(new BasicNameValuePair("Payed", String.valueOf(ChoosePayment)));

            for (int i=0; i<((NewMainClass)getActivity()).MealsToPay1.size(); ++i){

                a =  ((NewMainClass)getActivity()).MealsToPay1.get(i);
                id = String.valueOf(a.getId());
                count = String.valueOf(a.getCount());
                if (i==0){
                    idi=id;
                    counti=count;
                }else{
                    idi=idi+"; "+ id;
                    counti=counti+"; "+ count;
                }

            }
            params.add(new BasicNameValuePair("id", idi));
            params.add(new BasicNameValuePair("count", counti));
            Log.d("Zakaz created: ", params.toString());
            JSONObject json1 = jsonParser.makeHttpRequest(url_send_zakaz, "POST", params);


            Log.d("Zakaz created: ", json1.toString());

            try {
                int success = json1.getInt(TAG_SUCCESS);

                if (success == 1) {
                    suc = 1;
                    ((NewMainClass) getActivity()).OrderNumber = json1.getString(TAG_ORDERNUMBER);

                } else {
                    suc = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {

            if (suc == 1) {
                loadFragment(SuccessfulOrderScreenFragment.newInstance());
            } else {
                Toast.makeText(getContext(), "Что-то пошло не так, попробуйте еще раз!",Toast.LENGTH_LONG).show();
            }

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