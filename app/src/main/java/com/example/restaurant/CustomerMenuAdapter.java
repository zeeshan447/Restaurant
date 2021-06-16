package com.example.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomerMenuAdapter  extends RecyclerView.Adapter<CustomerMenuAdapter.ViewHolder> {
    private Context mcontext;
    private List<FoodModel> FoodList;
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView restaurantImage, foodImage;
        TextView foodName, foodDescription,foodPrice;
        ElegantNumberButton additem;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantImage = itemView.findViewById(R.id.customer_menu_restaurant_image);
            foodImage = itemView.findViewById(R.id.customer_menu_food_image);

            foodName = itemView.findViewById(R.id.customer_menu_food_name);
            foodDescription = itemView.findViewById(R.id.customer_menu_food_description);
            foodPrice = itemView.findViewById(R.id.customer_menu_food_price);
            additem = itemView.findViewById(R.id.number_btn);

        }
    }
    public CustomerMenuAdapter(Context context, List<FoodModel> FoodList) {
        this.FoodList = FoodList;
        this.mcontext = context;
    }


    @NonNull
    @NotNull
    @Override
    public CustomerMenuAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customermenuadapter, parent, false);
        return new CustomerMenuAdapter.ViewHolder(view);       }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomerMenuAdapter.ViewHolder holder, int position) {
        final FoodModel displayFood = FoodList.get(position);
        Glide.with(mcontext).load(displayFood.getImageURL()).into(holder.foodImage);
        holder.foodName.setText(displayFood.getName());
        holder.foodDescription.setText(displayFood.getDescription());
        holder.foodPrice.setText("Rs " +displayFood.getPrice());
        displayFood.getRestaurantId();
        holder.additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext, "Cool", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return FoodList.size();
    }
}