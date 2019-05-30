package com.development.sdm.SamZakaz.Activities.authorActivities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.development.sdm.SamZakaz.R;
import com.development.sdm.SamZakaz.SupportClasses.DataBaseHelper;



public class AuthorizationOrRegistrationActivity extends Activity{
    Button btnlogInto;
    Button btnRegistration;
    DataBaseHelper databasehelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("activity starts", "AuthorizationOrRegistrationActivity");
        setContentView(R.layout.authorization_or_registration_activity);
        btnlogInto = (Button) findViewById(R.id.btnlogInto);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);
        databasehelper = new DataBaseHelper(this);
        btnlogInto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), AuthorizationActivity.class);
                startActivity(i);
            }

        });
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(i);
            }
        });
    }


}

