package com.development.sdm.SamZakaz.Activities.Menu.AccountFragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.sdm.SamZakaz.Activities.Menu.HomeFragments.HomeFragment;
import com.development.sdm.SamZakaz.Activities.authorActivities.AuthorizationOrRegistrationActivity;
import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;
import com.development.sdm.SamZakaz.SupportClasses.ImageUtil;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AccountInfoFragment extends Fragment {
    ImageView backFromAccInfo;
    TextView saveChanges;
    EditText editName;
    EditText editPhoneNumber;
    ImageView AccPhoto;
    DataBaseHelper databasehelper;
    String name, phonenumber;
    int id;
    Bitmap ph1;

    public AccountInfoFragment() {
    }

    public static AccountInfoFragment newInstance() {
        return new AccountInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_info, container, false);
        backFromAccInfo = v.findViewById(R.id.backFromAccInfo);
        saveChanges = v.findViewById(R.id.SaveChanges);
        editName = v.findViewById(R.id.editName);
        editPhoneNumber = v.findViewById(R.id.editPhoneNumber);
        AccPhoto = v.findViewById(R.id.UserPhoto);

        databasehelper = new DataBaseHelper(getContext());
        final SQLiteDatabase db = databasehelper.getWritableDatabase();
        Cursor c = db.query("client", null, null, null, null, null, null);
        try {
            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex("id");
                int NameColIndex = c.getColumnIndex("name");
                int PhoneColIndex = c.getColumnIndex("PhoneNumber");
                int PhotoColIndex= c.getColumnIndex("photo");
                do {
                    id = c.getInt(idColIndex);
                    name = c.getString(NameColIndex);
                    phonenumber = c.getString(PhoneColIndex);
                    ph1 = ImageUtil.convert(c.getString(PhotoColIndex));
                } while (c.moveToNext());
            } else {
                c.close();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Log.d("name, phone", name+ " "+ phonenumber);
        if ((name != null) || (phonenumber != null)) {
            AccPhoto.setImageBitmap(ph1);
            editName.setText(name);
            editPhoneNumber.setText(phonenumber);
        } else {
            editName.setText("Имя");
            editPhoneNumber.setText("+7");
        }


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("save changes", "click");
                    name = editName.getText().toString();
                    phonenumber = editPhoneNumber.getText().toString();
                    //записываем новые даные в бд
                    SQLiteDatabase db1 = databasehelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    if(name!=null){
                        cv.put("name", name);
                    }
                    if(phonenumber!=null) {
                        cv.put("PhoneNumber", phonenumber);
                    }
                    if(ph1!=null) {
                        cv.put("photo", ImageUtil.convert(ph1));
                    }
                    db1.update("client", cv, "id=?", new String[]{String.valueOf(id)});
                    db.close();
                    databasehelper.close();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            loadFragment(AccountFragment.newInstance());
            }

        });
        backFromAccInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(AccountFragment.newInstance());
            }
        });
        AccPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);

            }
        });

        return v;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                ph1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                AccPhoto.setImageBitmap(ph1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}