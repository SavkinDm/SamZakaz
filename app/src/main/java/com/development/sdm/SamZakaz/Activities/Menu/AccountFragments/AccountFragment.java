package com.development.sdm.SamZakaz.Activities.Menu.AccountFragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.sdm.SamZakaz.Activities.Menu.HomeFragments.HomeFragment;
import com.development.sdm.SamZakaz.Activities.Menu.NewMainClass;
import com.development.sdm.SamZakaz.Activities.authorActivities.AuthorizationOrRegistrationActivity;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;
import com.development.sdm.SamZakaz.SupportClasses.ImageUtil;


public class AccountFragment extends Fragment {
Button exitBtn;
    DataBaseHelper databasehelper;
    ImageView AccInfo;
    ImageView Settings;
    ImageView CartHistory;
    TextView Putname, Putphonenumber;
    String name, phonenumber;
    Bitmap ph1;
    ImageView Accphoto;
    public AccountFragment() { }

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_account, container, false);
        exitBtn = v.findViewById(R.id.ExitBtn);
        AccInfo = v.findViewById(R.id.AccInfo);
        Settings = v.findViewById(R.id.Settings);
        CartHistory =v.findViewById(R.id.CartHistory);
        Accphoto =v.findViewById(R.id.Accph);
        databasehelper = new DataBaseHelper(getContext());
        Putname = v.findViewById(R.id.Putname);
        Putphonenumber = v.findViewById(R.id.PutPhoneNumber);
        SQLiteDatabase db = databasehelper.getWritableDatabase();

        Cursor c = db.query("client", null, null, null, null, null, null);
        try {
            if (c.moveToFirst()) {
                int NameColIndex = c.getColumnIndex("name");
                int PhoneColIndex = c.getColumnIndex("PhoneNumber");
                int PhotoColIndex= c.getColumnIndex("photo");
                do {
                    name = c.getString(NameColIndex);
                    phonenumber = c.getString(PhoneColIndex);
                    ph1 = ImageUtil.convert(c.getString(PhotoColIndex));
                } while (c.moveToNext());
            } else {
                c.close();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Log.d("name, phone", name+ " "+ phonenumber);
        if ((name!=null)&& (phonenumber!=null)){
            Accphoto.setImageBitmap(ph1);
            Putname.setText(name);
            Putphonenumber.setText(phonenumber);
        }else{
            Putname.setText("Имя");
            Putphonenumber.setText("+7");
            Accphoto.setImageResource(R.drawable.account_icon);
        }




        AccInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(AccountInfoFragment.newInstance());
                Log.d("AccInfo", "click");
            }
        });

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadFragment(SettingsFragment.newInstance());
                Log.d("Settings", "click");
            }
        });
        CartHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadFragment(CartHistoryFragment.newInstance());
                Log.d("CartHistory", "click");
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = databasehelper.getWritableDatabase();
                db.delete("client", null, null);
                db.delete("favouriteRestaurants", null, null);
                db.delete("Restaurants", null, null);
                db.delete("Meals", null, null);
                db.close();
                databasehelper.close();
                Intent i = new Intent(getContext(), AuthorizationOrRegistrationActivity.class);
                startActivity(i);
                Log.d("exitBtn", "click");
            }
        });


        databasehelper.close();
        return v;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }




}
