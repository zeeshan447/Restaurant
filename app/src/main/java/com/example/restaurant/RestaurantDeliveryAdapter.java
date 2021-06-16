package com.example.restaurant;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantDeliveryAdapter extends RecyclerView.Adapter<RestaurantDeliveryAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateRestaurant> RestaurantList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView RestaurantName, Restaurantlocation;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.detail_restaurant_image);
            RestaurantName = itemView.findViewById(R.id.detail_restaurant_name);
            Restaurantlocation = itemView.findViewById(R.id.detail_restaurant_location);

        }
    }
    public RestaurantDeliveryAdapter(Context context, List<UpdateRestaurant> RestaurantList) {
        this.RestaurantList = RestaurantList;
        this.mcontext = context;
    }

    @NonNull
    @NotNull
    @Override
    public RestaurantDeliveryAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.deliveryadapter, parent, false);
        return new RestaurantDeliveryAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantDeliveryAdapter.ViewHolder holder, int position) {
        final UpdateRestaurant displayRestaurant = RestaurantList.get(position);
        Glide.with(mcontext).load(displayRestaurant.getImageURL()).into(holder.imageView);
        holder.RestaurantName.setText(displayRestaurant.getName());
        holder.Restaurantlocation.setText(displayRestaurant.getAddress());
        displayRestaurant.getRandomUID();
        displayRestaurant.getRestaurantId();
        String restaurantId = displayRestaurant.getRestaurantId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext,CustomerMenu.class);
                intent.putExtra("restaurantID", restaurantId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RestaurantList.size();     }
}
