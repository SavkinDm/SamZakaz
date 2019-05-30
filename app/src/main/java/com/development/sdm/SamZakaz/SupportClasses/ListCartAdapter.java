package com.development.sdm.SamZakaz.SupportClasses;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.development.sdm.SamZakaz.R;

import java.util.ArrayList;
import java.util.Collections;

public class ListCartAdapter extends RecyclerView.Adapter<ListCartAdapter.PersonViewHolder2> implements ItemTouchHelperAdapter {

    private ArrayList<Meal> Meal;
    int count;
    ArrayList<Meal> MealsToCart;


    public ListCartAdapter(ArrayList<Meal> Meal) {
        this.Meal = Meal;
    }

    @NonNull
    @Override
    public ListCartAdapter.PersonViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup1, int i) {
        View v1 = LayoutInflater.from(viewGroup1.getContext()).inflate(R.layout.list_cart, viewGroup1, false);
        ListCartAdapter.PersonViewHolder2 pvh1 = new ListCartAdapter.PersonViewHolder2(v1);

        return pvh1;
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonViewHolder2 personViewHolder2, final int position) {
        MealsToCart = Meal;
        personViewHolder2.MealId.setText(String.valueOf(Meal.get(position).getId()));
        personViewHolder2.MealName.setText(Meal.get(position).getName());
        personViewHolder2.MealCount.setText(String.valueOf(Meal.get(position).getCount()));
        personViewHolder2.MealPrice.setText(String.valueOf(Meal.get(position).getPrice()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            personViewHolder2.Photo.setForeground(Meal.get(position).getPhoto());
        }
        count = (Meal.get(position).getCount());
        personViewHolder2.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    Meal meal1 = MealsToCart.get(position);
                    meal1.setCount(meal1.getCount() - 1);
                    MealsToCart.set(position, meal1);
                    count--;
                    personViewHolder2.MealCount.setText(String.valueOf(count));
                    personViewHolder2.MealPrice.setText(String.valueOf(Meal.get(position).getPrice() * count));

                } else {
                    personViewHolder2.minus.setVisibility(View.INVISIBLE);
                }
                Log.d("cart", String.valueOf(MealsToCart));
            }
        });
        personViewHolder2.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personViewHolder2.minus.setVisibility(View.VISIBLE);
                Meal meal1 = MealsToCart.get(position);
                meal1.setCount(meal1.getCount() + 1);
                MealsToCart.set(position, meal1);
                count++;
                personViewHolder2.MealCount.setText(String.valueOf(count));
                personViewHolder2.MealPrice.setText(String.valueOf(Meal.get(position).getPrice() * count));
                Log.d("cart", String.valueOf(MealsToCart));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Meal.size();
    }

    public ArrayList<com.development.sdm.SamZakaz.SupportClasses.Meal> getMealsToCart() {
        return MealsToCart;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(Meal, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(Meal, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        Meal.remove(position);
        notifyItemRemoved(position);
    }

    public class PersonViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView MealName;
        TextView MealId;
        TextView MealPrice;
        TextView MealCount;
        ImageView Photo;
        ImageButton plus, minus;

        PersonViewHolder2(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.CartCard);
            MealName = (TextView) itemView.findViewById(R.id.CartElementName);
            MealPrice = itemView.findViewById(R.id.CartElementPrice);
            Photo = itemView.findViewById(R.id.CartElementPhoto);
            MealId = itemView.findViewById(R.id.CartElementId);
            MealCount = itemView.findViewById(R.id.CartElementCount);
            plus = itemView.findViewById(R.id.CartElementPlus);
            minus = itemView.findViewById(R.id.CartElementMinus);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
