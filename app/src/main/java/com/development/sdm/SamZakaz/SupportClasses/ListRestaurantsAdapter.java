package com.development.sdm.SamZakaz.SupportClasses;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.sdm.SamZakaz.R;

import java.util.ArrayList;

public class ListRestaurantsAdapter extends RecyclerView.Adapter<ListRestaurantsAdapter.PersonViewHolder>{
    private ArrayList<Restaurant> restaurant;
    private View v;

    public ListRestaurantsAdapter(ArrayList<Restaurant> restaurant) {
        this.restaurant = restaurant;
    }

    @NonNull
    @Override
    public ListRestaurantsAdapter.PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_restaurants, viewGroup, false);
        ListRestaurantsAdapter.PersonViewHolder pvh = new ListRestaurantsAdapter.PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListRestaurantsAdapter.PersonViewHolder personViewHolder, final int position) {
        personViewHolder.idView.setText(String.valueOf(restaurant.get(position).id));
        personViewHolder.RestaurantName.setText(restaurant.get(position).name);
        personViewHolder.Photo.setBackground(restaurant.get(position).photo);
        personViewHolder.Logo.setImageBitmap(restaurant.get(position).logo);
        double ratingRest = restaurant.get(position).Rating;


        if (ratingRest >=0 &&ratingRest < 0.5) {
            personViewHolder.Rating1.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating2.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating3.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating4.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        }  else if (ratingRest >=0.5 &&ratingRest < 1) {
            personViewHolder.Rating1.setImageResource(R.drawable.half_star);
            personViewHolder.Rating2.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating3.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating4.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        } else if (ratingRest >=1 &&ratingRest < 1.5) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating3.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating4.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        } else if (ratingRest >=1.5 &&ratingRest < 2) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.half_star);
            personViewHolder.Rating3.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating4.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        } else if (ratingRest >=2 &&ratingRest < 2.5) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating3.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating4.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        } else if (ratingRest >=2.5 &&ratingRest < 3) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating3.setImageResource(R.drawable.half_star);
            personViewHolder.Rating4.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        } else if (ratingRest >=3 &&ratingRest < 3.5) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating3.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating4.setImageResource(R.drawable.emptystar);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        } else if (ratingRest >=3.5 &&ratingRest < 4) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating3.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating4.setImageResource(R.drawable.half_star);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        } else if (ratingRest >=4 &&ratingRest < 4.5) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating3.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating4.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating5.setImageResource(R.drawable.emptystar);
        } else if (ratingRest >=4.5 &&ratingRest < 5) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating3.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating4.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating5.setImageResource(R.drawable.half_star);
        } else if (ratingRest >= 5) {
            personViewHolder.Rating1.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating2.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating3.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating4.setImageResource(R.drawable.fill_star);
            personViewHolder.Rating5.setImageResource(R.drawable.fill_star);

        }


    }

    @Override
    public int getItemCount() {
        return restaurant.size();
    }



    public   class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView RestaurantName;
        TextView RestaurantDescription;
        ConstraintLayout Photo;
        ImageView Rating1;
        ImageView Rating2;
        ImageView Rating3;
        ImageView Rating4;
        ImageView Rating5;
        ImageView Logo;
        TextView idView;
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card);
            RestaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
            idView = itemView.findViewById(R.id.idView);
            Photo = itemView.findViewById(R.id.cardbackground);
            Logo = itemView.findViewById(R.id.RestLogo);
            Rating1 = itemView.findViewById(R.id.RatingStar1);
            Rating2 = itemView.findViewById(R.id.RatingStar2);
            Rating3 = itemView.findViewById(R.id.RatingStar3);
            Rating4 = itemView.findViewById(R.id.RatingStar4);
            Rating5 = itemView.findViewById(R.id.RatingStar5);
        }

        @Override
        public void onClick(View v) {

        }
    }




}