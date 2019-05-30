package com.development.sdm.SamZakaz.Activities.Menu.CartFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.development.sdm.SamZakaz.Activities.Menu.AccountFragments.SettingsFragment;
import com.development.sdm.SamZakaz.Activities.Menu.HomeFragments.HomeFragment;
import com.development.sdm.SamZakaz.Activities.Menu.NewMainClass;
import com.development.sdm.SamZakaz.R;

public class SuccessfulOrderScreenFragment extends Fragment {

    Button ok;
    TextView textSuc;
    public SuccessfulOrderScreenFragment() { }

    public static SuccessfulOrderScreenFragment newInstance() {
        return new SuccessfulOrderScreenFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.successful_order_screen, container, false);
        textSuc = v.findViewById(R.id.SuccessfulText);
        ok = v.findViewById(R.id.okButton1236);
        textSuc.setText("Заказ №"+((NewMainClass) getActivity()).OrderNumber+ " успешно оформлен!");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
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