package com.development.sdm.SamZakaz.SupportClasses;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.development.sdm.SamZakaz.R;

import java.util.ArrayList;

public class ListMealsAdapter extends RecyclerView.Adapter<ListMealsAdapter.PersonViewHolder1>{

    private ArrayList<Meal> Meal;


    public ListMealsAdapter(ArrayList<Meal> Meal) {
        this.Meal = Meal;
    }

    @NonNull
    @Override
    public ListMealsAdapter.PersonViewHolder1 onCreateViewHolder(@NonNull ViewGroup viewGroup1, int i) {
        View v1 = LayoutInflater.from(viewGroup1.getContext()).inflate(R.layout.list_meals, viewGroup1, false);
        ListMealsAdapter.PersonViewHolder1 pvh1 = new ListMealsAdapter.PersonViewHolder1(v1);

        return pvh1;
    }

    @Override
    public void onBindViewHolder(@NonNull ListMealsAdapter.PersonViewHolder1 personViewHolder1, final int position) {
        personViewHolder1.MealId.setText(String.valueOf(Meal.get(position).getId()));
        personViewHolder1.MealName.setText(Meal.get(position).getName());
        personViewHolder1.MealDescription.setText(Meal.get(position).getDescription());
        personViewHolder1.MealPrice.setText(String.valueOf(Meal.get(position).getPrice()));
        personViewHolder1.Photo.setBackground(Meal.get(position).getPhoto());

    }

    @Override
    public int getItemCount() {
        return Meal.size();
    }



    public    class PersonViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView MealName;
        TextView MealDescription;
        TextView MealId;
        TextView MealPrice;

        ConstraintLayout Photo;

        PersonViewHolder1(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card);
            MealName = (TextView) itemView.findViewById(R.id.meal_name);
            MealDescription = (TextView) itemView.findViewById(R.id.mealdescription);
            Photo = itemView.findViewById(R.id.cardbackground12);
            MealPrice = itemView.findViewById(R.id.meal_price);
            MealId = itemView.findViewById(R.id.meal_id);
        }

        @Override
        public void onClick(View v) {

        }
    }



}
