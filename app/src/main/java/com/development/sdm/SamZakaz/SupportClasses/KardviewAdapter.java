package com.development.sdm.SamZakaz.SupportClasses;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.development.sdm.SamZakaz.R;

import java.util.ArrayList;

public class KardviewAdapter extends RecyclerView.Adapter<KardviewAdapter.PersonViewHolder>{
    ArrayList<RestaurantMainScreen> restaurantMainScreens;
    View v;

    public KardviewAdapter(ArrayList<RestaurantMainScreen> restaurantMainScreens) {
        this.restaurantMainScreens = restaurantMainScreens;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder personViewHolder,final int position) {
        personViewHolder.RestaurantName.setText(restaurantMainScreens.get(position).name);
        personViewHolder.Photo.setBackgroundResource(restaurantMainScreens.get(position).photoId);


    }

    @Override
    public int getItemCount() {
        return restaurantMainScreens.size();
    }



    public  class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView RestaurantName;
        TextView RestaurantDescription;
        ConstraintLayout Photo;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card);
            RestaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
            Photo = itemView.findViewById(R.id.cardbckgnd);
        }

        @Override
        public void onClick(View v) {

        }
    }




}